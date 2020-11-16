package cn.osworks.aos.system.modules.service.checkup;

import cn.osworks.aos.core.asset.AOSUtils;
import cn.osworks.aos.core.dao.SqlDao;
import cn.osworks.aos.core.id.AOSId;
import cn.osworks.aos.core.typewrap.Dto;
import cn.osworks.aos.core.typewrap.Dtos;
import cn.osworks.aos.system.dao.mapper.*;
import cn.osworks.aos.system.dao.po.*;
import cn.osworks.aos.system.modules.dao.vo.UserInfoVO;
import cn.osworks.aos.system.modules.service.resource.ModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CheckupTopicService  extends JdbcDaoSupport {
    @Autowired
    private SqlDao sysDao;
    @Autowired
    public JdbcTemplate jdbcTemplate;
    @Autowired
    private Checkup_topicMapper checkup_topicMapper;
    @Autowired
    private Aos_sys_moduleMapper aos_sys_moduleMapper;
    @Autowired
    private Aos_sys_module_userMapper aos_sys_module_userMapper;
    @Autowired
    private Checkup_topic_tablenameidMapper checkup_topic_tablenameidMapper;
    @Autowired
    private Archive_tablefieldlistMapper archive_tablefieldlistMapper;

    @Autowired
    private Archive_check_tablenameidMapper archive_check_tablenameidMapper;
    @Autowired
    private ModuleService moduleService;
    @Resource
    public void setJd(JdbcTemplate jd) {
        super.setJdbcTemplate(jd);
    }

    public List<Checkup_topicPO> listtopic(Dto qDto) {
        return checkup_topicMapper.listPage(qDto);
    }
    public boolean addtopic(Dto qDto) {
        try{
            Checkup_topicPO checkup_topicPO=new Checkup_topicPO();
            AOSUtils.copyProperties(qDto,checkup_topicPO);
            checkup_topicPO.setId_(AOSId.uuid());
            checkup_topicMapper.insert(checkup_topicPO);
            //此时添加到数据compolitation_rw中
            addTopic_tablenameid(qDto,checkup_topicPO.getId_());
            //目录树添加专题tree，并赋予byadmin权限
            addtree(qDto,"0.014.012.004",checkup_topicPO.getId_());
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    //创建目录树
    private void addtree(Dto qDto, String cascade_id_,String module_id) {
        //1.得到管理员账号的id值
        Aos_sys_modulePO aos_sys_module=new Aos_sys_modulePO();
        String tablename=qDto.getString("sjkmc");
        aos_sys_module.setId_(module_id);
        //2.得到当前cascade的id值一些信息
        String parent_id_="";
        String parent_name="";
        List<Map<String, Object>> list = getModule(cascade_id_);
        if (list != null && list.size() > 0) {
            parent_id_=(String)list.get(0).get("id_");
            parent_name=(String)list.get(0).get("Name_");
        }
        int i=0;
        //3.查询当前节点的子节点个数
        i=getCountModele_child(parent_id_);
        if(i<9){
            aos_sys_module.setCascade_id_(cascade_id_+".00"+(i+1));
        }else if(i>=9&&i<99){
            aos_sys_module.setCascade_id_(cascade_id_+".0"+(i+1));
        }else if(i>=99){
            aos_sys_module.setCascade_id_(cascade_id_+"."+(i+1));
        }
        aos_sys_module.setName_(qDto.getString("topic_name"));
        aos_sys_module.setUrl_("archive/topic/initData.jhtml");
        aos_sys_module.setParent_id_(parent_id_);
        aos_sys_module.setIs_auto_expand_("0");
        aos_sys_module.setStatus_("1");
        aos_sys_module.setParent_name_(parent_name);
        aos_sys_module.setSort_no_(i+1);
        aos_sys_module.setTablename(qDto.getString("topic_tablename"));
        aos_sys_moduleMapper.insert(aos_sys_module);
        //父几点变成不是叶子节点
        Aos_sys_modulePO aos_sys_module_parent=new Aos_sys_modulePO();
        aos_sys_module_parent.setId_(parent_id_);
        aos_sys_module_parent.setIs_leaf_("0");
        aos_sys_moduleMapper.updateByKey(aos_sys_module_parent);
        //同时为当前用户添加经办权限
        addpower_handle(aos_sys_module,qDto);
        //同时为当前用户添加管理权限
        addpower_manage(aos_sys_module,qDto);
    }
    //经办权限
    private void addpower_handle(Aos_sys_modulePO aos_sys_module,Dto out) {
        String rootid=getUserId("root");
        UserInfoVO userInfo=out.getUserInfo();
        Aos_sys_module_userPO aos_sys_module_userPO=new Aos_sys_module_userPO();
        aos_sys_module_userPO.setId_(AOSId.uuid());
        aos_sys_module_userPO.setModule_id_(aos_sys_module.getId_());
        aos_sys_module_userPO.setUser_id_(userInfo.getId_());
        aos_sys_module_userPO.setGrant_type_("1");
        aos_sys_module_userPO.setOperate_time_(AOSUtils.getDateTimeStr());
        aos_sys_module_userPO.setOperator_id_(rootid);
        aos_sys_module_userMapper.insert(aos_sys_module_userPO);
    }
    //管理权限
    private void addpower_manage(Aos_sys_modulePO aos_sys_module,Dto out) {
        String rootid=getUserId("root");
        UserInfoVO userInfo=out.getUserInfo();
        Aos_sys_module_userPO aos_sys_module_userPO=new Aos_sys_module_userPO();
        aos_sys_module_userPO.setId_(AOSId.uuid());
        aos_sys_module_userPO.setModule_id_(aos_sys_module.getId_());
        aos_sys_module_userPO.setUser_id_(userInfo.getId_());
        aos_sys_module_userPO.setGrant_type_("2");
        aos_sys_module_userPO.setOperate_time_(AOSUtils.getDateTimeStr());
        aos_sys_module_userPO.setOperator_id_(rootid);
        aos_sys_module_userMapper.insert(aos_sys_module_userPO);
    }
    //得到用户的id值
    private String getUserId(String root) {
        String id_="";
        String sql="select * from aos_sys_user where account_='"+root+"'";
        List<Map<String,Object>> list=jdbcTemplate.queryForList(sql);
        if(list!=null&&list.size()>0){
            id_=(String)list.get(0).get("id_");
        }
        return id_;
    }
    /*
     * 查询表名
     */
    public List<Archive_tablenamePO> findTablename_by() {
        // TODO Auto-generated method stub
        List<Archive_tablenamePO> alist = new ArrayList<Archive_tablenamePO>();
        String sql = "SELECT  *  FROM      archive_TableName WHERE   (TableName NOT LIKE '%_total%') AND (TableName NOT LIKE '%_temporary%') AND   (TableName NOT LIKE '%_receive%')";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                Map<String, Object> map = list.get(i);
                String tablename = (String) map.get("TableName");
                String tabledesc = (String) map.get("TableDesc");
                Archive_tablenamePO archive_tablenamePO = new Archive_tablenamePO();
                archive_tablenamePO.setTablename(tablename);
                archive_tablenamePO.setTabledesc(tabledesc);
                alist.add(archive_tablenamePO);
            }
        }
        return alist;
    }
    /**
     * 修改专题
     * @param qDto
     * @return
     */
    public boolean edittopic(Dto qDto) {
        try{
            Checkup_topicPO checkup_topicPO=new Checkup_topicPO();
            AOSUtils.copyProperties(qDto,checkup_topicPO);
            checkup_topicMapper.updateByKey(checkup_topicPO);
            //目录树添加专题tree，并赋予byadmin权限
            updatetreename(qDto,"0.014.012.004",checkup_topicPO.getId_());
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    /**
     * 修改目录树名称
     * @param qDto
     * @param cascade_id_
     * @param module_id
     */
    private void updatetreename(Dto qDto, String cascade_id_,String module_id) {
        //1.得到管理员账号的id值
        Aos_sys_modulePO aos_sys_module=new Aos_sys_modulePO();
        String tablename=qDto.getString("sjkmc");
        aos_sys_module.setId_(module_id);
        aos_sys_module.setName_(qDto.getString("topic_name"));
        aos_sys_moduleMapper.updateByKey(aos_sys_module);
    }
    //查询子节点个数
    private int getCountModele_child(String parent_id_) {
        String sql="select * from aos_sys_module where parent_id_ ='"+parent_id_+"'";
        return jdbcTemplate.queryForList(sql).size();
    }
    private List<Map<String, Object>> getModule(String cascade_id_) {
        String sql="select * from aos_sys_module where cascade_id_='"+cascade_id_+"'";
        return jdbcTemplate.queryForList(sql);
    }
    /**
     * 检索专题数据
     * @param inDto
     * @param session
     * @return
     */
    public List<Map<String, Object>> getData(Dto inDto, HttpSession session) {

        String tablename=inDto.getString("tablename");
        String id_=inDto.getString("id_");
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        // 排列方式，页码，每页条目数
        Integer limit = Integer.valueOf(inDto.getString("limit"));
        Integer page = Integer.valueOf(inDto.getString("page"));
        // 条目开始索引
        Integer pageStart = (page - 1) * (limit);
        List<Map<String,Object>> listDto=new ArrayList<>();

            String sql="select * from checkup_topic_tablenameid where pid='"+id_+"'  order by tablename_id desc offset "+pageStart+" rows fetch next "+limit+" rows only";
            listDto =jdbcTemplate.queryForList(sql);
        if(listDto!=null&&listDto.size()>0){
            for(int i=0;i<listDto.size();i++){
                getDatafromtopic(list,inDto,limit,page,pageStart,listDto.get(i));
            }
        }
        //此时对list集合分页操作
        //list=getPageList(list,page,limit);
        return list;
    }
    /**
     *
     * 查询条件拼接
     *
     * @param qDto
     * @return
     */
    public String queryConditions(Dto qDto) {
        StringBuffer query = new StringBuffer();
        if (qDto.getString("columnnum") != "") {
            int contentLength = Integer.parseInt(qDto.getString("columnnum"));

            for (int i = 1; i <= contentLength + 1; i++) {
                if (qDto.getString("content" + i) != null
                        && qDto.getString("content" + i) != "") {

                    if (qDto.getString("and" + i).equals("on")) {
                        query.append(" and " + qDto.getString("filedname" + i));
                    }
                    if (qDto.getString("or" + i).equals("on")) {
                        query.append(" or " + qDto.getString("filedname" + i)
                                + " " + qDto.getString("condition" + i));
                    }
                    if (qDto.getString("condition" + i).equals("like")) {
                        query.append(" " + qDto.getString("condition" + i)
                                + " '%" + qDto.getString("content" + i) + "%'");
                    }
                    if (qDto.getString("condition" + i).equals("left")) {
                        query.append(" like '%" + qDto.getString("content" + i)
                                + "'");
                    }
                    if (qDto.getString("condition" + i).equals("right")) {
                        query.append(" like '" + qDto.getString("content" + i)
                                + "%'");
                    }
                    if (qDto.getString("condition" + i).equals("<>")) {
                        query.append(" <>'" + qDto.getString("content" + i)
                                + "'");
                    }
                    if (qDto.getString("condition" + i).equals(">")) {
                        query.append(" >'" + qDto.getString("content" + i) + "'");
                    }
                    if (qDto.getString("condition" + i).equals("<")) {
                        query.append(" <'" + qDto.getString("content" + i) + "'");
                    }
                    if (qDto.getString("condition" + i).equals("<=")) {
                        query.append(" <='" + qDto.getString("content" + i) + "'");
                    }
                    if (qDto.getString("condition" + i).equals(">=")) {
                        query.append(" >='" + qDto.getString("content" + i) + "'");
                    }
                    if (qDto.getString("condition" + i).equals("=")) {
                        query.append(" = '" + qDto.getString("content" + i)
                                + "'");
                    }

                }

            }

        }
        query.append(" and flag<>'1' ");
        return query.toString();
    }
    private List<Map<String, Object>> getPageList(List<Map<String, Object>> list, Integer page, Integer limit) {
        int fromIndex = (page - 1) * limit;
        if (fromIndex >= list.size()) {
            return null;//空数组
        }
        if(fromIndex<0){
            return null;//空数组
        }
        int toIndex = page * limit;
        if (toIndex >= list.size()) {
            toIndex = list.size();
        }
        return list.subList(fromIndex, toIndex);
    }
    /**
     * 查询到一条数据进行新的集合添加
     * @param list
     * @param inDto
     * @param limit
     * @param page
     * @param pageStart
     * @param stringObjectMap
     */
    private void getDatafromtopic(List<Map<String, Object>> list, Dto inDto, Integer limit, Integer page, Integer pageStart, Map<String, Object> stringObjectMap) {
        String sql2="select * from "+stringObjectMap.get("tablename")+" where id_ ='"+stringObjectMap.get("tablename_id")+"'";
        List<Map<String,Object>> listDto =jdbcTemplate.queryForList(sql2);
        if(listDto!=null&&listDto.size()>0){
            listDto.get(0).put("tablename",stringObjectMap.get("tablename"));
            list.add(listDto.get(0));
        }
    }
    /**
     *
     * 查询专题数据总数
     *
     * @return
     */
    public int getPageTotal_Data(Dto inDto) {
        String tablename=inDto.getString("tablename");
        String id_=inDto.getString("id_");
        String sql="select *  from checkup_topic_tablenameid where pid='"+id_+"'";
        List<Map<String,Object>> listDto =jdbcTemplate.queryForList(sql);
        return listDto.size();
    }
    public String queryConditions2_noid(Dto inDto) {
        String tablename=inDto.getString("tablename");
        String id_=inDto.getString("id_");
        String query_ids="";
        String sql="select tablename_id from checkup_topic_tablenameid where pid='"+id_+"' and tablename='"+tablename+"'";
        List<Map<String,Object>> list=jdbcTemplate.queryForList(sql);
        String ids="";
        if(list!=null&&list.size()>0) {
            for (int i = 0; i < list.size(); i++) {
                if (i == 0) {
                    ids = "'" + (String) list.get(i).get("tablename_id") + "'";
                } else {
                    ids = ids + ",'" + (String) list.get(i).get("tablename_id") + "'";
                }
            }
            query_ids=" and id_ not in ("+ids+")";
        }
        return query_ids;
    }

    /**
     *
     * 删除条目
     *
     * @param qDto
     * @return
     */
    public Dto deletetopic_data(Dto dto) {
        Dto outDto = Dtos.newDto();
        String tablename="";
        List<Map<String, Object>> listmodule=getModule_byid_(dto.getString("pid"));
        if(listmodule!=null&&listmodule.size()>0){
            tablename=(String)listmodule.get(0).get("tablename");
        }
        String pid=dto.getString("pid");
        String[] selections = dto.getRows();
        int del = 0;
        for (String id_ : selections) {

            jdbcTemplate.execute(" delete from checkup_topic_tablenameid where pid='"
                    + pid + "' and tablename='"+tablename+"' and tablename_id='"+id_+"'");
            del++;
        }
        String msg = "操作完成，";
        if (del > 0) {
            msg = AOSUtils.merge(msg + "成功删除信息[{0}]个", del);
        }
        outDto.setAppMsg(msg);
        return outDto;

    }

    public boolean updatetopic_data(Dto inDto) {
        try{
            String tablename=inDto.getString("tablename");
            String ids=inDto.getString("ids");
            String dhs=inDto.getString("dhs");
            if(ids!=null&&ids!=""){
                String[] id=ids.split(",");
                String[] dh=dhs.split(",");
                for(int i=0;i<id.length;i++){
                    Checkup_topic_tablenameidPO checkup_topic_tablenameidPO=new Checkup_topic_tablenameidPO();
                    checkup_topic_tablenameidPO.setId_(AOSId.uuid());
                    checkup_topic_tablenameidPO.setPid(inDto.getString("id_"));
                    checkup_topic_tablenameidPO.setTablename(tablename);
                    checkup_topic_tablenameidPO.setTablename_id(id[i]);
                    checkup_topic_tablenameidPO.setDh(dh[i]);
                    checkup_topic_tablenameidMapper.insert(checkup_topic_tablenameidPO);
                }
            }
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    public List<Map<String, Object>> getModule_byid_(String cascade_id_) {
        String sql="select * from aos_sys_module where id_='"+cascade_id_+"'";
        return jdbcTemplate.queryForList(sql);
    }
    private void addTopic_tablenameid(Dto qDto,String id_) {
        String ids=qDto.getString("ids");
        String dhs=qDto.getString("dhs");
        String tablename=qDto.getString("sjkmc");
        if(ids!=null&&ids!=""){
            String[] id=ids.split(",");
            String[] dh=dhs.split(",");
            for(int i=0;i<id.length;i++){
                Checkup_topic_tablenameidPO checkup_topic_tablenameidPO=new Checkup_topic_tablenameidPO();
                checkup_topic_tablenameidPO.setId_(AOSId.uuid());
                checkup_topic_tablenameidPO.setPid(id_);
                checkup_topic_tablenameidPO.setTablename(tablename);
                checkup_topic_tablenameidPO.setTablename_id(id[i]);
                checkup_topic_tablenameidPO.setDh(dh[i]);
                checkup_topic_tablenameidMapper.insert(checkup_topic_tablenameidPO);
            }
        }
    }
    public boolean addtopic_edit(Dto qDto) {
        try{
            Checkup_topicPO checkup_topicPO=new Checkup_topicPO();
            AOSUtils.copyProperties(qDto,checkup_topicPO);
            checkup_topicMapper.updateByKey(checkup_topicPO);
            //此时添加到数据compolitation_rw中
            //此时修改对应的目录树名称(待添加)
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    /**
     *
     * 删除条目
     *
     * @param qDto
     * @return
     */
    public Dto delete_topic(Dto dto) {
        Dto outDto = Dtos.newDto();
        String[] selections = dto.getRows();
        int del = 0;
        for (String id_ : selections) {
            //删除节点
            //1查询条目的任务名称和任务编号组合组合字段，并查询到节点id值
            delete_tree(id_);
            //删除专题数据的目录树
            //删除任务
            jdbcTemplate.execute(" delete from checkup_topic where id_='"
                    + id_ + "'");
            //同时删除当前id对应的checkup_topic_tablenameid表中的关联数据
           checkup_topic_tablenameidMapper.deleteTopic_tablenameid(id_);
            del++;
        }
        String msg = "操作完成，";
        if (del > 0) {
            msg = AOSUtils.merge(msg + "成功删除信息[{0}]个", del);
        }
        outDto.setAppMsg(msg);
        return outDto;
    }
    /**
     * 删除目录树
     * @param id_
     */
    public void delete_tree(String id_) {
        //查询到当前的编号和任务名称组合的节点id值
        //根据名称查询到节点id集合
        String sql2="select * from aos_sys_module where id_='"+id_ +"'";
        List<Map<String,Object>> list2=jdbcTemplate.queryForList(sql2);
        if(list2!=null&&list2.size()>0){
            for(int i=0;i<list2.size();i++){
                Dto out=Dtos.newDto();
                out.put("aos_rows_",(String)list2.get(i).get("id_"));
                moduleService.deleteModule(out);
            }
        }
    }
    public List<Map<String, Object>> listArchive(Dto qDto) {
        // TODO Auto-generated method stub
        String tablename=qDto.getString("tablename");
        String query=qDto.getString("query");
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        // 排列方式，页码，每页条目数
        Integer limit = Integer.valueOf(qDto.getString("limit"));
        Integer page = Integer.valueOf(qDto.getString("page"));
        // 条目开始索引
        Integer pageStart = (page - 1) * (limit);
        String sql="select * from "+tablename+" where 1=1 "+query+" order by dh asc offset "+pageStart+" rows fetch next "+limit+" rows only";
        List listDto = jdbcTemplate.queryForList(sql);
        return listDto;

    }
    /**
     *
     * 查询记录总数
     *
     * @return
     */
    public int getPageAll_archive(Dto qDto) {
        //String query = queryConditions2(qDto);
        String query= qDto.getString("query");
        String tablename=qDto.getString("tablename");
        //查询年度条件是否存在
        String sql = "SELECT COUNT(*) FROM " +tablename +" where 1=1 "+query;
        return jdbcTemplate.queryForInt(sql);
    }
    /**
     *
     * 查询条件拼接
     *
     * @param qDto
     * @return
     */
    public String queryConditions2(Dto qDto) {

        if (qDto.getString("columnnum") != "") {
            int contentLength = Integer.parseInt(qDto.getString("columnnum"));
            StringBuffer query = new StringBuffer();
            for (int i = 1; i <= contentLength + 1; i++) {
                if (qDto.getString("content" + i) != null
                        && qDto.getString("content" + i) != "") {

                    if (qDto.getString("and" + i).equals("on")) {
                        query.append(" and " + qDto.getString("filedname" + i));
                    } else {
                        query.append(" or " + qDto.getString("filedname" + i)
                                + " ");
                    }
                    if (qDto.getString("condition" + i).equals("like")) {
                        query.append(" " + qDto.getString("condition" + i)
                                + " '%" + qDto.getString("content" + i) + "%'");
                    }
                    if (qDto.getString("condition" + i).equals("left")) {
                        query.append(" like '%" + qDto.getString("content" + i)
                                + "'");
                    }
                    if (qDto.getString("condition" + i).equals("right")) {
                        query.append(" like '" + qDto.getString("content" + i)
                                + "%'");
                    }
                    if (qDto.getString("condition" + i).equals(">")) {
                        query.append(" > " + qDto.getString("content" + i));
                    }
                    if (qDto.getString("condition" + i).equals("<")) {
                        query.append(" < " + qDto.getString("content" + i));
                    }
                    if (qDto.getString("condition" + i).equals("<=")) {
                        query.append(" <= " + qDto.getString("content" + i));
                    }
                    if (qDto.getString("condition" + i).equals(">=")) {
                        query.append(" >= " + qDto.getString("content" + i));
                    }
                    if (qDto.getString("condition" + i).equals("=")) {
                        query.append(" = '" + qDto.getString("content" + i)
                                + "'");
                    }

                }

            }
            return " "+query.toString()+" ";
        } else
            return "";

    }

    public List<Archive_tablefieldlistPO> getComboboxList(Dto inDto) {
        List<Archive_tablefieldlistPO> list = archive_tablefieldlistMapper
                .getDataFieldDisplayAll(inDto.getString("tablename"));
        return list;
    }

    public List<Map<String,Object>>   getQueryIds(Dto qDto) {
        // TODO Auto-generated method stub
        String ids="";
        String tablename=qDto.getString("tablename");
        String query=qDto.getString("query");
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        String sql="select id_,dh from "+tablename+" where 1=1 "+query;
        List<Map<String,Object>> listDto = jdbcTemplate.queryForList(sql);

        return listDto;
    }
}

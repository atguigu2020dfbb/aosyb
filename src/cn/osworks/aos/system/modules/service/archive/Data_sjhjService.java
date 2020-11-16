package cn.osworks.aos.system.modules.service.archive;

import cn.osworks.aos.core.asset.AOSCons;
import cn.osworks.aos.core.asset.AOSUtils;
import cn.osworks.aos.core.typewrap.Dto;
import cn.osworks.aos.core.typewrap.Dtos;
import cn.osworks.aos.system.dao.mapper.*;
import cn.osworks.aos.system.dao.po.Archive_tablefieldlistPO;
import cn.osworks.aos.system.dao.po.Checkup_topicPO;
import cn.osworks.aos.system.modules.dao.vo.UserInfoVO;
import cn.osworks.aos.system.modules.service.notification.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class Data_sjhjService  extends JdbcDaoSupport {
    @Autowired
    private Archive_tablenameMapper archive_tablenameMapper;
    @Autowired
    private Archive_tablefieldlistMapper archive_tablefieldlistMapper;
    @Autowired
    private Archive_tableinputMapper archive_tableinputMapper;
    @Autowired
    private Aos_sys_moduleMapper aos_sys_moduleMapper;
    @Autowired
    private Archive_sxjc_dataMapper archive_sxjc_dataMapper;
    @Autowired
    private Aos_sys_userMapper aos_sys_userMapper;
    @Autowired
    private Jnws_temporaryMapper jnws_temporaryMapper;
    @Autowired
    private Archive_zdyjMapper archive_zdyjMapper;
    @Autowired
    private Aos_sys_dicMapper aos_sys_dicMapper;
    @Autowired
    private Aos_sys_dic_indexMapper aos_sys_dic_indexMapper;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    public static String url = "";
    @Autowired
    public static String username = "";
    @Autowired
    public static String password = "";
    @Autowired
    public static String topicAddress = "";
    @Autowired
    public static String filePath = "";
    @Autowired
    public static String linkAddress="";
    @Autowired
    public static Connection connection = null;
    @Autowired
    public static PreparedStatement ps;
    @Autowired
    private LogMapper logMapper;
    @Autowired
    private Archive_receive_resetMapper archive_receive_resetMapper;
    @Autowired
    private Archive_receiveMapper archive_receiveMapper;
    @Autowired
    private Archive_entityMapper archive_entityMapper;
    @Autowired
    private Archive_sxjcMapper archive_sxjcMapper;
    @Autowired
    private Archive_sxjc_resultMapper archive_sxjc_resultMapper;
    @Autowired
    private Archive_receive_reportMapper archive_receive_reportMapper;
    @Autowired
    private NotificationService notificationService;
    // 加载配置文件
    static {
        try {
            Properties props = new Properties();
            InputStream in = DataService.class
                    .getResourceAsStream("/config.properties");
            props.load(in);
            filePath = props.getProperty("filePath");
            linkAddress = props.getProperty("linkAddress");
            topicAddress = props.getProperty("topicAddress");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Resource
    public void setJb(JdbcTemplate jb) {
        super.setJdbcTemplate(jb);
    }


    /**
     *
     * 页面初始化
     *
     * @param tablename
     * @return
     */
    public List<Archive_tablefieldlistPO> getDataFieldListTitle(String tablename) {
        List<Archive_tablefieldlistPO> list = archive_tablefieldlistMapper
                .getDataFieldDisplayAll(tablename);
        return list;
    }
    /**
     *目录树
     * @param qDto
     * @return
     */
    public String findTree(Dto qDto) {
        String name_="";
        String sql="select * from aos_sys_module where id_='"+qDto.getString("aos_module_id_")+"'";
        List<Map<String,Object>> list=jdbcTemplate.queryForList(sql);
        if(list!=null&&list.size()>0){
            name_=(String)list.get(0).get("name_");
        }
        return name_;
    }
    /**
     *目录树
     * @param qDto
     * @return
     */
    public String findNd(Dto qDto) {
        String nd="";
        String sql="select * from aos_sys_module where id_='"+qDto.getString("aos_module_id_")+"'";
        List<Map<String,Object>> list=jdbcTemplate.queryForList(sql);
        if(list!=null&&list.size()>0){
            nd=(String)list.get(0).get("name_");
        }
        return nd;
    }
    /**
     *
     * 判断是不是全部
     *
     * @param pDto
     * @return
     */

    public String isAll(Dto pDto) {
        Dto qDto = Dtos.newDto();
        String sql = "SELECT * FROM aos_sys_module  WHERE cascade_id_='"
                + pDto.getString("cascode_id_") + "'";
        qDto = convertMapListToDto(jdbcTemplate.queryForList(sql));
        String queryclass = " 1=1";
        if (!qDto.getString("name_").equals("全部")) {
            queryclass = " _classtree='" + pDto.getString("cascode_id_") + "'";
        }
        return queryclass;
    }
    /**
     * 功能：转换MapList为Dto
     *
     * @param list
     * @return
     */
    public static Dto convertMapListToDto(List list) {
        Map map = null;
        Dto qDto = Dtos.newDto();
        if (list != null) {
            Iterator iterator = list.iterator();
            while (iterator.hasNext()) {
                map = (Map) iterator.next();
                Iterator<?> keyIt = map.keySet().iterator();
                while (keyIt.hasNext()) {
                    String key = (String) keyIt.next();
                    String value = ((Object) map.get(key)) == null ? ""
                            : ((Object) map.get(key)).toString();
                    qDto.put(key, value);
                }
            }
        }

        return qDto;
    }
    /**
     * 查询当前用户编研状态
     * @param userInfo
     * @return
     */
    public String findCompilation_flag(UserInfoVO userInfo) {
        String compilation_flag="";
        String sql="select * from aos_sys_user where account_='"+userInfo.getAccount_()+"'";
        List<Map<String,Object>> list= jdbcTemplate.queryForList(sql);
        if(list!=null&&list.size()>0) {
            compilation_flag=(String)list.get(0).get("compilation_flag_");
        }
        return compilation_flag;
    }
    /**
     *
     * 查询条件拼接
     *
     * @param qDto
     * @return
     */
    public String queryConditions(Dto qDto) {

        if (qDto.getString("columnnum") != "") {
            int contentLength = Integer.parseInt(qDto.getString("columnnum"));
            StringBuffer query = new StringBuffer();
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
                        query.append(" >'" + qDto.getString("content" + i)+ "'");
                    }
                    if (qDto.getString("condition" + i).equals("<")) {
                        query.append(" <'" + qDto.getString("content" + i)+ "'");
                    }
                    if (qDto.getString("condition" + i).equals("<=")) {
                        query.append(" <='" + qDto.getString("content" + i)+ "'");
                    }
                    if (qDto.getString("condition" + i).equals(">=")) {
                        query.append(" >='" + qDto.getString("content" + i)+ "'");
                    }
                    if (qDto.getString("condition" + i).equals("=")) {
                        query.append(" = '" + qDto.getString("content" + i)
                                + "'");
                    }
                }
            }
            return query.toString();
        } else
            return "";

    }
    public Object getData(Dto qDto) {
        String tablename = qDto.getString("tablename");
        String id = qDto.getString("id_");
        String sql = "SELECT * FROM " + tablename + " WHERE id_='" + id + "'";
        List<Dto> listDto = convertMapListToArrayList(jdbcTemplate
                .queryForList(sql));
        return listDto.get(0);
    }
    //修改条目
    public Dto updateData(Dto qDto) {
        qDto.put("hjsj", AOSUtils.getDateTimeStr());
        Dto outDto = Dtos.newDto();
        String columns = "id_";
        String strsql = "";
        Iterator iter = qDto.entrySet().iterator(); // 获取key和value的set
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next(); // 把hashmap转成Iterator再迭代到entry
            Object key = entry.getKey(); // 从entry获取keyObject
            if (key.equals("app") || key.equals("tablename")|| key.equals("selectvalue")
                    || key.equals("_userInfoVO") || key.equals("id")) {
                continue;
            }
            Object val = "'" + entry.getValue() + "'"; // 从entry获取value}
            // strsql=key+"="+val+"";
            if (strsql.equals("")) {
                strsql = key + "=" + val + "";
            } else
                strsql = strsql + "," + key + "=" + val + "";
        }
        String sql = " UPDATE " + qDto.getString("tablename") + " SET "
                + strsql + " WHERE id_='" + qDto.getString("id_") + "'";
        jdbcTemplate.execute(sql);
        outDto.setAppCode(AOSCons.SUCCESS);
        outDto.setAppMsg("操作完成，保存成功。");
        return outDto;
    }
    /**getDataFieldDisplayAll
     * 功能：转换MapList为数组List
     *
     * @param list
     * @return
     */
    public static List convertMapListToArrayList(List list) {
        Map map = null;
        return list;
    }
    public Dto getpath(String id_,String tablename) {
        Dto out=Dtos.newDto();
        String sql = "select * from " + tablename + "_path where tid ='" + id_ + "'";
        String filepath = "";
        String urlpath = "";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                Map<String, Object> map = list.get(i);
                String path = (String) map.get("_s_path");
                if (path.split("\\.")[1].equals("pdf")) {
                    String filename = (String) map.get("dirname");
                    // 组合路径
                    filepath = filePath  + File.separator+filename
                            + path;
                    urlpath = topicAddress  + File.separator+filename
                            + path;
                    // 获取当前ip地址
                    // 路径批量替换
                    // wsda/2e9275c32e6849afab038892747249c2/数字档案馆系统测试办法（档办发[2014]6号).pdf
                    filepath = filepath.replace("\\", "/");
                    urlpath = urlpath.replace("\\", "/");
                    out.put("pdfpath",filepath);
                    out.put("urlpath",urlpath);
                    break;
                }
            }
        }
        return out;
    }
    /**
     *
     * 显示表头
     *
     * @param qDto
     * @return
     */
    public List<Dto> getDataFieldListDisplayAll(Dto qDto, String username) {
        String query = queryConditions2(qDto);
        String treenumber=qDto.getString("treenumber");
        if(treenumber!=null&&treenumber.length()>0){
            query=query+" and qzh='"+treenumber+"'";
        }
        Integer limit = Integer.valueOf(qDto.getString("limit"));
        Integer page = Integer.valueOf(qDto.getString("page"));
        // 将条件存入到一个数据表中，方便与缓存查询
        String selectmark = qDto.getString("selectmark");
        if (selectmark.equals("1")) {
            // 存入缓存表中
            addSearchData(qDto, username);
        }
        String sql = "";
        String term = "";
        String fieldName;
        String enfield = "id_";
        List<Archive_tablefieldlistPO> list = archive_tablefieldlistMapper
                .getDataFieldDisplayAll(qDto.getString("tablename"));
        String orderenfield = "";
        for (int i = 0; i < list.size(); i++) {
            fieldName = list.get(i).getFieldenname();
            enfield = enfield + "," + fieldName;
            if (i == 3) {
                orderenfield = enfield;
            }
        }
        orderenfield = orderenfield.substring(4);
        // 排列条件传递过去
        String orderBy = getOrderBy(qDto, username);
        if (orderenfield != null) {
            if (orderBy != null){
                orderBy = "( ORDER BY "+orderBy+")";
            }else{
                orderBy = "( ORDER BY "+orderenfield+")";
            }

        } else {
            orderBy = "( ORDER BY "+orderenfield+")";
        }
        if (qDto.getString("page") != null && qDto.getString("page") != "") {
            sql = "WITH aos_query_ AS (SELECT TOP 100 PERCENT ROW_NUMBER () OVER "
                    //+ orderenfield
                    + orderBy
                    + " AS aos_rn_,"
                    + enfield
                    + " FROM "
                    + qDto.getString("tablename")
                    + " WHERE 1=1 "
                    + query
                    + term
                    + ") "
                    + "SELECT * FROM aos_query_ WHERE aos_rn_ BETWEEN "
                    + (limit * (page - 1) + 1)
                    + " AND "
                    + qDto.getPageLimit()
                    * Integer.valueOf(qDto.getString("page"))
                    + " ORDER BY aos_rn_";
        } else {
            sql = "WITH aos_query_ AS (SELECT TOP 100 PERCENT ROW_NUMBER () OVER "
                    //+ orderenfield
                    + orderBy
                    + " AS aos_rn_,"
                    + enfield
                    + " FROM "
                    + qDto.getString("tablename")
                    + " WHERE 1=1 "
                    + query
                    + term
                    + ") "
                    + "SELECT * FROM aos_query_  ORDER BY aos_rn_";
        }
        List listDto = jdbcTemplate.queryForList(sql);
        return listDto;
    }
    /**
     *
     * 获取排列条件(根据表名和用户名)
     *
     * @author PX
     * @param qDto
     *
     *            2019-1-7
     * @param username
     * @return
     */
    private String getOrderBy(Dto qDto, String username) {
        // TODO Auto-generated method stub
        String sql = "select * from archive_Sort_Data where tablename='"
                + qDto.getString("tablename") + "' and cxr='" + username + "'";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        if (list.size() > 0) {
            return list.get(0).get("nr").toString();
        } else {
            return null;
        }
    }
    /**
     * 存入查询缓存
     *
     * @author PX
     * @param qDto
     * @param username2
     *
     *            2019-1-14
     */
    private void addSearchData(Dto qDto, String username) {
        // TODO Auto-generated method stub
        // 得到当前时间毫秒
        // 查询用户中文名
        String chinese = getUserChinese(username);
        long nowtime = System.currentTimeMillis();
        SimpleDateFormat dfDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String bh = dfDateFormat.format(new Date());
        for (int q = 1; q <= 8; q++) {
            String filedname = qDto.getString("filedname" + q);
            String filedcnname = qDto.getString("filedcnname" + q);
            String and = qDto.getString("and" + q);
            String content = qDto.getString("content" + q);
            String condition = qDto.getString("condition" + q);
            String sql = "insert into archive_Reseach_Data(bh,gx,zdmc,nr,tj,tablename,mk,cxr,mk_en) values ('"
                    + bh
                    + "','"
                    + and
                    + "','"
                    + filedname
                    + "','"
                    + content
                    + "','"
                    + condition
                    + "','"
                    + qDto.getString("tablename")
                    + "','"
                    + chinese
                    + "','"
                    + filedcnname
                    + "','"
                    + username
                    + "')";
            // 存入到数据库中
            jdbcTemplate.execute(sql);
        }
    }
    /**
     * 得到中文登录名
     *
     * @author PX
     * @param username2
     *
     *            2019-1-14
     * @return
     */
    private String getUserChinese(String username2) {
        // TODO Auto-generated method stub
        String sql = "select * from aos_sys_user where account_='" + username2
                + "'";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        if (list != null && list.size() > 0) {
            String name_ = (String) list.get(0).get("name_");
            return name_;
        }
        return null;
    }
    /**
     *
     * 查询记录总数
     *
     * @param tablename
     * @return
     */
    public int getPageTotal(Dto qDto) {
        String query = queryConditions2(qDto);
        String queryclass=qDto.getString("queryclass");
        String treenumber=qDto.getString("treenumber");
        if(queryclass!=null&&queryclass!=""){
            query=" and "+queryclass+" "+query;
        }
        if(treenumber!=null&&treenumber!=""){
            query=" and qzh='"+treenumber+"'";
        }else{
            query="";
        }
        String sql = "SELECT COUNT(*) FROM " + qDto.getString("tablename")
                + " WHERE 1=1 " + query;
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
                        query.append(" like '" + qDto.getString("content" + i)
                                + "%'");
                    }
                    if (qDto.getString("condition" + i).equals("right")) {
                        query.append(" like '%" + qDto.getString("content" + i)
                                + "'");
                    }
                    if (qDto.getString("condition" + i).equals("<>")) {
                        query.append(" <> '" + qDto.getString("content" + i)
                                + "'");
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
            return query.toString();
        } else
            return "";

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
        String sql="select * from checkup_topic_tablenameid where pid='"+id_+"'";
        List<Map<String,Object>> listDto =jdbcTemplate.queryForList(sql);
        if(listDto!=null&&listDto.size()>0){
            for(int i=0;i<listDto.size();i++){
                getDatafromtopic(list,inDto,limit,page,pageStart,listDto.get(i));
            }
        }
        //此时对list集合分页操作
        list=getPageList(list,page,limit);
        return list;
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
        String sql2="select * from "+stringObjectMap.get("tablename")+" where id_ in(select tablename_id from checkup_topic_tablenameid where id_='"+stringObjectMap.get("id_")+"')";
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
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        // 排列方式，页码，每页条目数
        Integer limit = Integer.valueOf(inDto.getString("limit"));
        Integer page = Integer.valueOf(inDto.getString("page"));
        // 条目开始索引
        Integer pageStart = (page - 1) * (limit);
        String sql="select * from checkup_topic_tablenameid where pid='"+id_+"'";
        List<Map<String,Object>> listDto =jdbcTemplate.queryForList(sql);
        if(listDto!=null&&listDto.size()>0){
            for(int i=0;i<listDto.size();i++){
                getDatafromtopic(list,inDto,limit,page,pageStart,listDto.get(i));
            }
        }
        return list.size();
    }
}

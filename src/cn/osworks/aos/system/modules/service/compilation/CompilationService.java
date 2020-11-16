package cn.osworks.aos.system.modules.service.compilation;

import cn.osworks.aos.core.asset.AOSCons;
import cn.osworks.aos.core.asset.AOSJson;
import cn.osworks.aos.core.asset.AOSUtils;
import cn.osworks.aos.core.asset.AOSXmlOptionsHandler;
import cn.osworks.aos.core.dao.SqlDao;
import cn.osworks.aos.core.id.AOSId;
import cn.osworks.aos.core.typewrap.Dto;
import cn.osworks.aos.core.typewrap.Dtos;
import cn.osworks.aos.system.dao.mapper.*;
import cn.osworks.aos.system.dao.po.*;
import cn.osworks.aos.system.modules.dao.vo.UserInfoVO;
import cn.osworks.aos.system.modules.service.archive.DataService;
import cn.osworks.aos.system.modules.service.notification.NotificationService;
import cn.osworks.aos.system.modules.service.resource.ModuleService;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.converter.PicturesManager;
import org.apache.poi.hwpf.converter.WordToHtmlConverter;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.hwpf.usermodel.Picture;
import org.apache.poi.hwpf.usermodel.PictureType;
import org.apache.tools.ant.taskdefs.condition.Http;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class CompilationService extends JdbcDaoSupport {
    @Autowired
    public JdbcTemplate jdbcTemplate;
    @Autowired
    public DataService dataService;
    @Autowired
    private SqlDao sysDao;
    @Autowired
    private Aos_sys_moduleMapper aos_sys_moduleMapper;
    @Autowired
    private Aos_sys_orgMapper aos_sys_orgMapper;
    @Autowired
    private Archive_tablefieldlistMapper archive_tablefieldlistMapper;
    @Autowired
    private Aos_sys_userMapper aos_sys_userMapper;
    @Autowired
    private Archive_topicMapper archive_topicMapper;
    @Autowired
    private Archive_write_topicMapper archive_write_topicMapper;
    @Autowired
    private Archive_topic_tablenameidMapper archive_topic_tablenameidMapper;
    @Autowired
    private Archive_topic_userMapper archive_topic_userMapper;
    @Autowired
    private Archive_seminarMapper archive_seminarMapper;
    @Autowired
    private Archive_taskMapper archive_taskMapper;
    @Autowired
    private Archive_compilationMapper archive_compilationMapper;
    @Autowired
    private Archive_compilation_rwMapper archive_compilation_rwMapper;
    @Autowired
    private Archive_compilation_tablenameidMapper archive_compilation_tablenameid;
    @Autowired
    private ModuleService moduleService;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private Aos_sys_module_userMapper aos_sys_module_userMapper;
    @Resource
    public void setJd(JdbcTemplate jd) {
        super.setJdbcTemplate(jd);
    }
    public static String filePath = "";
    public static String linkAddress = "";
    public static String topicAddress = "";
    public final String DEFAULT_PICTURE_FOLDER="pictures";
    public final String DEFAULT_HTML_TYPE=".html";
    // 通过静态代码块读取配置文件
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
    /**
     * 得到数据表集合
     *
     * @author PX
     * @param listtablename
     *
     *            2018-8-23
     * @return
     */
    public List<Map<String, Object>> getListData(String listtablename) {
        // TODO Auto-generated method stub
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        String sql = "select * from " + listtablename;
        list = jdbcTemplate.queryForList(sql);
        return list;
    }
    public List<Map<String, Object>> listCompilation(Dto qDto) {
        // TODO Auto-generated method stub
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        String query=" ";
        String byrwbh="";
        String byrwmc="";
        String aos_module_id_=qDto.getString("aos_module_id_");
        String byrwbhmc=getModuleName(aos_module_id_);
        if(byrwbhmc!=null&&byrwbhmc!=""||byrwbhmc.length()>=8){
            if(byrwbhmc.matches("^\\d{7}([\\u4e00-\\u9fa5_a-zA-Z0-9]+)$")){
                query=" where  byrwbh='"+byrwbhmc.substring(0,7)+"' and byrwmc='"+byrwbhmc.substring(7)+"'";
            };
        }
        // 排列方式，页码，每页条目数
        Integer limit = Integer.valueOf(qDto.getString("limit"));
        Integer page = Integer.valueOf(qDto.getString("page"));
        // 条目开始索引
        Integer pageStart = (page - 1) * (limit);
        String sql="select * from archive_compilation_rw  "+query+" order by byrwbh desc offset "+pageStart+" rows fetch next "+limit+" rows only";
        List<Map<String,Object>> listDto = jdbcTemplate.queryForList(sql);

        return listDto;

    }

    private String getModuleName(String aos_module_id_) {
        String sql="select * from aos_sys_module where id_='"+aos_module_id_+"'";
        String byrwbhmc="";
        List<Map<String,Object>> listDto = jdbcTemplate.queryForList(sql);
        if(listDto!=null&&listDto.size()>0){
            byrwbhmc=(String)listDto.get(0).get("name_");
        }
        return byrwbhmc;
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
        String sql="select * from "+tablename+" where 1=1 "+query+" order by id_ offset "+pageStart+" rows fetch next "+limit+" rows only";
        List listDto = jdbcTemplate.queryForList(sql);
        return listDto;
    }
    public List<Map<String,Object>>   getQueryIds(Dto qDto) {
        // TODO Auto-generated method stub
        String ids="";
        String tablename=qDto.getString("tablename");
        String query=qDto.getString("query");
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        String sql="select id_ from "+tablename+" where 1=1 "+query;
        List<Map<String,Object>> listDto = jdbcTemplate.queryForList(sql);

        return listDto;
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
     *
     * 页面初始化

     * @return
     */
    public List<Dto> getDataUsersListTitle(Dto qDto) {
        Dto dto=Dtos.newDto();
        dto.setOrder("id_ DESC");
        dto.put("delete_flag_",0);
        dto.put("org_cascade_id_",0);
        dto.put("limit",50);
        dto.put("start",0);
        String sql="select account_,name_ from aos_sys_user where delete_flag_='0' and org_cascade_id_='0' order by id_";
        List<Dto> userInfos =(List) jdbcTemplate.queryForList(sql);
        return userInfos;
    }
    /*
     * 查询表名
     */
    public List<Archive_tablenamePO> findTablename() {
        // TODO Auto-generated method stub
        List<Archive_tablenamePO> alist = new ArrayList<Archive_tablenamePO>();
        String sql = "select * from archive_TableName";
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
    /*
     * 查询专题列表
     */
    public List<Map<String,Object>> findzt_by() {
        // TODO Auto-generated method stub
        List<Map<String,Object>> alist = new ArrayList<Map<String,Object>>();
        String sql = "SELECT  *  FROM      compilation_topic  ";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                Map<String, Object> map = list.get(i);
                String name = (String) map.get("topic_name");
                String id_ = (String) map.get("id_");
                Map<String,Object> listmap=new HashMap<String,Object>();
                listmap.put("name",name);
                listmap.put("id_",id_);
                alist.add(listmap);
            }
        }
        return alist;
    }
    /**
     * 操作记录
     * @param qDto
     */
    public boolean addbyrw(Dto qDto) {
        try{
            Archive_compilation_rwPO archive_compilation_rwPO=new Archive_compilation_rwPO();
            AOSUtils.copyProperties(qDto,archive_compilation_rwPO);
            archive_compilation_rwPO.setId_(AOSId.uuid());
            archive_compilation_rwPO.setZt_id(qDto.getString("zt_id"));
            archive_compilation_rwPO.setFlag_submit("未提交");
            //archive_compilation_rwPO.setFlag_submit("未审核");
            archive_compilation_rwPO.setFlag("未撰稿");
            archive_compilation_rwMapper.insert(archive_compilation_rwPO);
            //此时添加到数据compolitation_rw中
            addCompilation_tablenameid(qDto,archive_compilation_rwPO.getId_());
            //在在编课题和专题数据下面添加当前任务的目录树，以及主题数据的数据库名称添加
            addtree(qDto,"0.024.006",archive_compilation_rwPO.getId_());

            //addtree_parent(qDto,"0.024.008");
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    //创建目录树
    private void addtree_parent(Dto qDto, String cascade_id_) {
        //1.得到管理员账号的id值
        Dto dto =Dtos.newDto();
        Aos_sys_modulePO aos_sys_module=new Aos_sys_modulePO();
        String tablename=qDto.getString("sjkmc");
        String tabledesc=qDto.getString("sjkms");

        //2.得到当前cascade的id值一些信息
        String parent_id_="";
        String parent_name="";
        List<Map<String, Object>> list = getModule(cascade_id_);
        if (list != null && list.size() > 0) {
            parent_id_=(String)list.get(0).get("id_");
            parent_name=(String)list.get(0).get("name_");
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
        aos_sys_module.setId_(AOSId.uuid());
        aos_sys_module.setName_(qDto.getString("byrwbh")+qDto.getString("byrwmc")+"--"+tabledesc);
        aos_sys_module.setUrl_("compilation/examine/examine_by_initData.jhtml?byrwbhmc="+qDto.getString("byrwbh")+"&byrwmc="+qDto.getString("byrwmc")+"&byrwbh="+qDto.getString("byrwbh")+"&byrwmc="+qDto.getString("byrwmc")+"");
        aos_sys_module.setParent_id_(parent_id_);
        aos_sys_module.setIs_auto_expand_("0");
        aos_sys_module.setIs_leaf_("1");
        aos_sys_module.setStatus_("1");
        aos_sys_module.setParent_name_(parent_name);
        aos_sys_module.setSort_no_(i+1);
        aos_sys_module.setTablename(tablename);
        aos_sys_moduleMapper.insert(aos_sys_module);

        Aos_sys_modulePO aos_sys_module_parent=new Aos_sys_modulePO();
        aos_sys_module_parent.setId_(parent_id_);
        aos_sys_module_parent.setIs_leaf_("0");
        aos_sys_moduleMapper.updateByKey(aos_sys_module_parent);

        dto.put("parent_id_",aos_sys_module.getId_());
        dto.put("parent_name_",aos_sys_module.getName_());
        dto.put("parent_cascade_id_",aos_sys_module.getCascade_id_());
        dto.put("tablename",tablename);
        dto.put("tabledesc",tabledesc);
        //同时为当前用户添加经办权限
       // addpower_handle(aos_sys_module,qDto);
        //同时为当前用户添加管理权限
       // addpower_manage(aos_sys_module,qDto);
        //添加当前子节点的目录树
        //addtreeparent(dto,qDto);
        //父节点变成非叶子节点
        /*Aos_sys_modulePO aos_sys_module_parent=new Aos_sys_modulePO();
        aos_sys_module_parent.setCascade_id_(cascade_id_);
        aos_sys_module_parent.setId_(parent_id_);
        aos_sys_module_parent.setIs_leaf_("0");
        aos_sys_moduleMapper.updateByKey(aos_sys_module_parent);*/
    }
    //添加子节点目录树
    private void addtreeparent(Dto dto,Dto qDto) {
        Aos_sys_modulePO aos_sys_module=new Aos_sys_modulePO();
        String parent_id_=dto.getString("parent_id_");
        String parent_name_=dto.getString("parent_name_");
        String parent_cascade_id_=dto.getString("parent_cascade_id_");
        String tablename=dto.getString("tablename");
        String tabledesc=dto.getString("tabledesc");
        int i=0;
        //3.查询当前节点的子节点个数
        i=getCountModele_child(parent_id_);
        if(i<9){
            aos_sys_module.setCascade_id_(parent_cascade_id_+".00"+(i+1));
        }else if(i>=9&&i<99){
            aos_sys_module.setCascade_id_(parent_cascade_id_+".0"+(i+1));
        }else if(i>=99){
            aos_sys_module.setCascade_id_(parent_cascade_id_+"."+(i+1));
        }
        //4.在查询当前子节点是否存在
        boolean b=getBooleanModele(parent_id_,tablename);
        if(b){
            aos_sys_module.setId_(AOSId.uuid());
            aos_sys_module.setName_(tabledesc);
            aos_sys_module.setUrl_("compilation/examine/examine_by_initData.jhtml?byrwbh="+qDto.getString("byrwbh")+"&byrwmc="+qDto.getString("byrwmc")+"");
            aos_sys_module.setParent_id_(parent_id_);
            aos_sys_module.setIs_auto_expand_("0");
            //aos_sys_module.setIs_leaf_("1");
            aos_sys_module.setStatus_("1");
            aos_sys_module.setParent_name_(parent_name_);
            aos_sys_module.setSort_no_(i+1);
            aos_sys_module.setTablename(tablename);
            aos_sys_moduleMapper.insert(aos_sys_module);


            //同时为当前用户添加经办权限
            //addpower_handle(aos_sys_module,qDto);
            //同时为当前用户添加管理权限
            //addpower_manage(aos_sys_module,qDto);
        }
    }

    /**
     * 根据父节点名称和表明判断module目录树是否存在
     * @param parent_id_
     * @param tablename
     * @return
     */
    private boolean getBooleanModele(String parent_id_, String tablename) {

        String sql="select * from aos_sys_module where parent_id_='"+parent_id_+"' and tablename ='"+tablename+"'";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        if(list!=null&&list.size()>0){
        return false;
        }
        return true;
    }

    //创建目录树
    private void addtree(Dto qDto, String cascade_id_, String id_) {
        //1.得到管理员账号的id值
        Aos_sys_modulePO aos_sys_module=new Aos_sys_modulePO();
        aos_sys_module.setId_(id_);
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
        aos_sys_module.setName_(qDto.getString("byrwbh")+qDto.getString("byrwmc"));
        aos_sys_module.setUrl_("compilation/articles/subject.jhtml");
        aos_sys_module.setParent_id_(parent_id_);
        aos_sys_module.setIs_auto_expand_("0");
        aos_sys_module.setStatus_("1");
        aos_sys_module.setParent_name_(parent_name);
        aos_sys_module.setSort_no_(i+1);
        aos_sys_moduleMapper.insert(aos_sys_module);
        //父几点变成不是叶子节点
        Aos_sys_modulePO aos_sys_module_parent=new Aos_sys_modulePO();
        aos_sys_module_parent.setId_(parent_id_);
        aos_sys_module_parent.setIs_leaf_("0");
        aos_sys_moduleMapper.updateByKey(aos_sys_module_parent);
        //同时为当前用户添加经办权限
        addpower_handle(aos_sys_module,qDto,"档案编研");
        //同时为当前用户添加管理权限
        addpower_manage(aos_sys_module,qDto,"档案编研");
    }
    //经办权限
    private void addpower_handle(Aos_sys_modulePO aos_sys_module,Dto out,String byname) {
        List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
        //此时如果是档案编研授权操作，需要把编研管理员和分管进行授权
        if("档案编研".equals(byname)){
             String sql="select * from aos_sys_user where bianyan_module like '%管理员%' or bianyan_module like '%审核%' or " +
                     " bianyan_module like '%撰稿%' or bianyan_module like '%合稿%' or bianyan_module like '%校对%' or bianyan_module like '%总编辑%'";
            list=jdbcTemplate.queryForList(sql);
            if(list!=null&&list.size()>0){
                for(int i=0;i<list.size();i++){
                    String rootid=getUserId("root");
                    UserInfoVO userInfo=out.getUserInfo();
                    Aos_sys_module_userPO aos_sys_module_userPO=new Aos_sys_module_userPO();
                    aos_sys_module_userPO.setId_(AOSId.uuid());
                    aos_sys_module_userPO.setModule_id_(aos_sys_module.getId_());
                    aos_sys_module_userPO.setUser_id_((String)list.get(i).get("id_"));
                    aos_sys_module_userPO.setGrant_type_("1");
                    aos_sys_module_userPO.setOperate_time_(AOSUtils.getDateTimeStr());
                    aos_sys_module_userPO.setOperator_id_(rootid);
                    aos_sys_module_userMapper.insert(aos_sys_module_userPO);
                }
            }
        }else{
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

    }
    //管理权限
    private void addpower_manage(Aos_sys_modulePO aos_sys_module,Dto out,String byname) {
        List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
        //此时如果是档案编研授权操作，需要把编研管理员和分管进行授权
        if("档案编研".equals(byname)){
            String sql="select * from aos_sys_user where bianyan_module like '%管理员%' or bianyan_module like '%审核%' or " +
                    " bianyan_module like '%撰稿%' or bianyan_module like '%合稿%' or bianyan_module like '%校对%' or bianyan_module like '%总编辑%'";
            list=jdbcTemplate.queryForList(sql);
            if(list!=null&&list.size()>0){
                for(int i=0;i<list.size();i++){
                    String rootid=getUserId("root");
                    UserInfoVO userInfo=out.getUserInfo();
                    Aos_sys_module_userPO aos_sys_module_userPO=new Aos_sys_module_userPO();
                    aos_sys_module_userPO.setId_(AOSId.uuid());
                    aos_sys_module_userPO.setModule_id_(aos_sys_module.getId_());
                    aos_sys_module_userPO.setUser_id_((String)list.get(i).get("id_"));
                    aos_sys_module_userPO.setGrant_type_("2");
                    aos_sys_module_userPO.setOperate_time_(AOSUtils.getDateTimeStr());
                    aos_sys_module_userPO.setOperator_id_(rootid);
                    aos_sys_module_userMapper.insert(aos_sys_module_userPO);
                }
            }
        }else {
            String rootid = getUserId("root");
            UserInfoVO userInfo = out.getUserInfo();
            Aos_sys_module_userPO aos_sys_module_userPO = new Aos_sys_module_userPO();
            aos_sys_module_userPO.setId_(AOSId.uuid());
            aos_sys_module_userPO.setModule_id_(aos_sys_module.getId_());
            aos_sys_module_userPO.setUser_id_(userInfo.getId_());
            aos_sys_module_userPO.setGrant_type_("2");
            aos_sys_module_userPO.setOperate_time_(AOSUtils.getDateTimeStr());
            aos_sys_module_userPO.setOperator_id_(rootid);
            aos_sys_module_userMapper.insert(aos_sys_module_userPO);
        }
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
    /**
     * 操作记录
     * @param qDto
     */
    public boolean addbyrw_edit(Dto qDto) {
        try{

            Archive_compilation_rwPO archive_compilation_rwPO=new Archive_compilation_rwPO();
            AOSUtils.copyProperties(qDto,archive_compilation_rwPO);
            archive_compilation_rwMapper.updateByKey(archive_compilation_rwPO);
            //目录树添加专题tree，并赋予byadmin权限
            updatetreename(qDto,"0.024.006",archive_compilation_rwPO.getId_());
            //此时添加到数据compolitation_rw中
            //addCompilation_tablenameid(qDto,archive_compilation_rwPO.getId_());
           // addtree(qDto,"0.024.006");
            //addtree_parent(qDto,"0.024.008");
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
        aos_sys_module.setId_(module_id);
        aos_sys_module.setName_(qDto.getString("byrwbh")+qDto.getString("byrwmc"));
        aos_sys_moduleMapper.updateByKey(aos_sys_module);
    }
    private void addCompilation_tablenameid(Dto qDto,String id_) {
        String ids=qDto.getString("ids");
        String tablename=qDto.getString("sjkmc");
        if(ids!=null&&ids!=""){
                String[] id=ids.split(",");
                for(int i=0;i<id.length;i++){
                    Archive_compilation_tablenameidPO archive_compilation_tablenameidPO=new Archive_compilation_tablenameidPO();
                    archive_compilation_tablenameidPO.setId_(AOSId.uuid());
                    archive_compilation_tablenameidPO.setPid(id_);
                    archive_compilation_tablenameidPO.setTablename(tablename);
                    archive_compilation_tablenameidPO.setTablename_id(id[i]);
                    archive_compilation_tablenameid.insert(archive_compilation_tablenameidPO);
                }
        }
    }

    /*
     * 查询表名
     */
    public List<Map<String, Object>> findztmc_by(Dto out) {
        // TODO Auto-generated method stub
        String _classtree=out.getString("_classtree");
        List<Map<String, Object>> list3=new ArrayList<Map<String, Object>>();
        //1.
        String sql2="select name_ from aos_sys_module where parent_id_ in (select id_ from aos_sys_module where cascade_id_ ='"+_classtree+"')";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql2);
        if(list!=null&&list.size()>0){
            for(int i=0;i<list.size();i++){
                Map<String,Object> map=new HashMap<String,Object>();
                map.put("code_",list.get(i).get("name_"));
                map.put("desc_",list.get(i).get("name_"));
                list3.add(map);
            }
        }
        return list3;
    }
    /*
     * 查询表名
     */
    public String findTablename(String id_) {
        // TODO Auto-generated method stub
        String sql="select tablename from archive_topic_tablenameid where pid='"+id_+"'";
        List<Map<String,Object>> list=jdbcTemplate.queryForList(sql);
        if(list!=null&&list.size()>0){
            return (String)list.get(0).get("tablename");
        }else{
            return "";
        }
    }

    /**
     *
     * 显示表头
     *
     * @param qDto
     * @return
     */
    public List<Dto> getDataFieldListDisplayAll(Dto qDto, HttpSession session) {
        String sql = "";
        String query = queryConditions2(qDto);
        // 此时把query存入到session中
        session.setAttribute("query", query);

        //查询年度条件是否存在
        String nd=qDto.getString("nd");
        if(nd!=null&&nd!="null"&&!nd.equals(null)&&!nd.equals("null")&&!nd.equals("")&&nd.length()>0){
            query=query+" and nd='"+nd+"'";
        }
        String fieldName;
        String enfield = "id_";
        // 排列方式，页码，每页条目数
        Integer limit = Integer.valueOf(qDto.getString("limit"));
        Integer page = Integer.valueOf(qDto.getString("page"));
        // 条目开始索引
        Integer pageStart = (page - 1) * (limit) + 1;
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
        if (qDto.getString("page") != null && qDto.getString("page") != "") {
            sql = "WITH aos_query_ AS (SELECT TOP 100 PERCENT ROW_NUMBER () OVER (ORDER BY "
                    + orderenfield
                    + ") AS aos_rn_,"
                    + enfield
                    + " FROM "
                    + qDto.getString("tablename")
                    + " where 1=1  "
                    + query
                    + " ) SELECT * FROM aos_query_ WHERE aos_rn_ BETWEEN "
                    + pageStart
                    + " AND "
                    + limit
                    * page
                    + " ORDER BY aos_rn_";
        } else {
            sql = "WITH aos_query_ AS (SELECT TOP 100 PERCENT ROW_NUMBER () OVER (ORDER BY "
                    + orderenfield
                    + ") AS aos_rn_,"
                    + enfield
                    + " FROM "
                    + qDto.getString("tablename")
                    + " where 1=1  "
                    + query
                    + "  ) SELECT * FROM aos_query_  ORDER BY aos_rn_";
        }
        List listDto = jdbcTemplate.queryForList(sql);
        return listDto;
    }
    /**
     *
     * 显示表头
     *
     * @param qDto
     * @return
     */
    public List<Map<String,Object>> getDataFieldListDisplayAll2(Dto qDto, HttpSession session) {
        String sql="select * from "+qDto.getString("tablename")+" where id_ in (select tablename_id from archive_topic_tablenameid where pid='"+qDto.getString("topic_id_")+"')";
        List<Map<String,Object>> listDto = jdbcTemplate.queryForList(sql);
        return listDto;
    }
    /**
     *
     * 查询记录总数
     *
     * @return
     */
    public int getPageTotal(Dto qDto) {
        String query = queryConditions2(qDto);
        //查询年度条件是否存在
        String nd=qDto.getString("nd");
        if(nd!=null&&!nd.equals("")&&nd.length()>0){
            query=query+" and nd='"+nd+"'";
        }
        String sql = "SELECT COUNT(*) FROM " + qDto.getString("tablename")
                + " WHERE 1=1 " + query ;
        return jdbcTemplate.queryForInt(sql);
    }
    /**
     *
     * 查询记录总数
     *
     * @return
     */
    public int getPageTotal_Data(Dto qDto) {
        String query = queryConditions2(qDto);
        String tablename="";
        List<Map<String, Object>> listmodule=getModule_byid_(qDto.getString("zt_id"));
        if(listmodule!=null&&listmodule.size()>0){
            tablename=(String)listmodule.get(0).get("tablename");
        }
        String sql3="select tablename_id from archive_topic_tablenameid where pid ='"+qDto.getString("zt_id")+"' order by id_ ";
            List<Map<String,Object>> list=jdbcTemplate.queryForList(sql3);
       String ids="";
        if(list!=null&&list.size()>0){
            for(int i=0;i<list.size();i++){
                if(i==0){
                    ids="'"+(String)list.get(i).get("tablename_id")+"'";
                }else{
                    ids=ids+",'"+(String)list.get(i).get("tablename_id")+"'";
                }
            }
            //查询年度条件是否存在
            String sql2="select count(*) from "+tablename+" where id_ in ("+ids+") "+query;
            return jdbcTemplate.queryForInt(sql2);
        }else{
            return 0;
        }
    }
    /**
     *
     * 查询记录总数
     *
     * @return
     */
    public int getPageAll_compilation(Dto qDto) {
        String query=" ";
        String aos_module_id_=qDto.getString("aos_module_id_");
        String byrwbhmc=getModuleName(aos_module_id_);
        if(byrwbhmc!=null&&byrwbhmc!=""||byrwbhmc.length()>=8){
            if(byrwbhmc.matches("^[0-9]{8}")) {
                query = " and  byrwbh='" + byrwbhmc.substring(0, 7) + "' and byrwmc='" + byrwbhmc.substring(7) + "'";
            }
        }
        //查询年度条件是否存在
        String sql = "SELECT COUNT(*) FROM archive_compilation_rw where 1=1 "+query ;
        return jdbcTemplate.queryForInt(sql);
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
     * 存入信息
     *
     * @author PX
     * @param outdto
     *
     *            2018-8-29
     * @param session
     * @return
     * @throws ParseException
     */
    public Dto jymessage(Dto outdto, HttpSession session) throws ParseException {
        // TODO Auto-generated method stub
        Dto out = Dtos.newDto();
        UserInfoVO userInfoVO = (UserInfoVO) session
                .getAttribute("_userInfoVO");
        String user = userInfoVO.getAccount_();
        out.put("user", user);
        return out;
    }

    /**
     * 借阅保存
     *
     * @author PX
     * @param outDto
     *
     *            2018-8-29
     * @return
     */
    public boolean savejy(Dto outDto) {
        // TODO Auto-generated method stub
        // 添加之前判断是否存在
        String sql2 = "select * from archive_jy where tablename='"
                + outDto.getString("tablename") + "' and archive_id='"
                + outDto.getString("archivenumber")
                + "' and archivestate='已借阅' and gh=''";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql2);
        if (list != null && list.size() > 0) {
            return false;
        } else {
            String sql = "insert into archive_jy(id_,jytime,users,archive_id,tablename,jyday,archivestate,gh,person,borrowreason) values('"
                    + AOSId.uuid()
                    + "','"
                    + outDto.getString("borrowData")
                    + "','"
                    + outDto.getString("user")
                    + "','"
                    + outDto.getString("archivenumber")
                    + "','"
                    + outDto.getString("tablename")
                    + "','"
                    + outDto.getString("borrowday")
                    + "','已借阅','','"
                    + outDto.getString("person")
                    + "','"
                    + outDto.getString("borrowreason") + "')";
            jdbcTemplate.execute(sql);
            // 修改条目状态
            String sql3 = "update " + outDto.getString("tablename")
                    + " set _jy='1' where id_='"
                    + outDto.getString("archivenumber") + "'";
            jdbcTemplate.execute(sql3);
        }
        return true;
    }

    public void updatejy(Dto outDto) throws Exception {
        // TODO Auto-generated method stub
        // 修改借阅天数，完成续借
        // 查询原有天数
        String sql = "select * from archive_jy where tablename='"
                + outDto.getString("tablename") + "' and archive_id='"
                + outDto.getString("archivenumber") + "' and gh=''";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        if (list != null && list.size() > 0) {
            String jyday = (String) list.get(0).get("jyday");
            if (jyday != null || !jyday.equals("")) {
                String sql2 = "update archive_jy set jyday='"
                        + (Integer.valueOf(jyday) + Integer.valueOf(outDto
                        .getString("reletday")))
                        + "' where tablename='" + outDto.getString("tablename")
                        + "' and archive_id='"
                        + outDto.getString("archivenumber") + "'";
                jdbcTemplate.execute(sql2);
            }
        }
    }

    /**
     * 归还
     *
     * @author PX
     * @param outDto
     * @param session
     * @return
     *
     *         2018-8-29
     */
    public int returnjy(Dto outDto, HttpSession session) {
        // TODO Auto-generated method stub
        // 判断是否有权限
        String sql = "select * from archive_jy where tablename='"
                + outDto.getString("listTablename") + "' and archive_id='"
                + outDto.getString("id_") + "' and gh=''";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        if (list != null && list.size() > 0) {
            String users = (String) list.get(0).get("users");
            // 判断当前用户和这个用户是不是一个人
            UserInfoVO userInfoVO = (UserInfoVO) session
                    .getAttribute("_userInfoVO");
            String user = userInfoVO.getAccount_();
            if (!users.equals(user)) {
                return -2;
            }
            try {
                // 执行归还操作
                String sql2 = "update archive_jy set archivestate='已归还',gh='1' where tablename='"
                        + outDto.getString("listTablename")
                        + "' and archive_id='" + outDto.getString("id_") + "'";
                jdbcTemplate.execute(sql2);
                // 修改条目标记
                String sql3 = "update " + outDto.getString("listTablename")
                        + " set _jy=null where id_='" + outDto.getString("id_")
                        + "'";
                jdbcTemplate.execute(sql3);
                return 1;
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
                return -1;
            }
        }
        return -1;
    }

    /**
     *
     *
     * @author PX
     *
     *         2018-8-30
     * @param dto
     * @return
     */
    public List<Map<String, Object>> listjy(Dto dto) {
        // TODO Auto-generated method stub
        String tablename = dto.getString("tablename");
        String sql = "";
        if (tablename != null && !tablename.equals("")) {
            sql = "select * from archive_jy where 1=1 and tablename='"
                    + tablename + "'";
        } else {
            sql = "select * from archive_jy where 1=1";
        }
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        return list;
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
                        query.append("and " + qDto.getString("filedname" + i));
                    } else {
                        query.append("or " + qDto.getString("filedname" + i)
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
            return query.toString();
        } else
            return "";

    }

    /**
     * 开始鉴定
     *
     * @author PX
     * @param dto
     * @param query
     *
     *            2018-11-27
     * @param request
     */
    public Dto starttopic(Dto dto, String query, HttpServletRequest request) {
        // TODO Auto-generated method stub
        // 1.判断是否含有成文日期和保管期限代码字段
        Dto outDto = Dtos.newDto();
        String tablename = dto.getString("tablename");

        String sql = "select FieldEnName  from archive_TableFieldList where tid in ( select id_ from archive_tablename where TableName='"
                + tablename + "') ";
        int index = 0;
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        if (list != null && list.size() > 0) {
            String[] str = new String[list.size()];
            for (int i = 0; i < list.size(); i++) {
                str[i] = (String) list.get(i).get("FieldEnName");
            }
            // 判断是否存在保管期限代码和成文日期字段
            if (Arrays.asList(str).contains("cwsj")) {
                if (Arrays.asList(str).contains("bgqxdm")) {
                    // 此时进行鉴定
                    // 通过查询得到进行鉴定的所有档案条目数据
                    String sql2 = "select * from " + tablename + " where 1=1 "
                            + query + " and _jd is null or _jd = 0";
                    List<Map<String, Object>> datalist = jdbcTemplate
                            .queryForList(sql2);
                    if (datalist != null && datalist.size() > 0) {
                        for (int t = 0; t < datalist.size(); t++) {
                            String cwsj = (String) datalist.get(t).get("cwsj");
                            String bgqxdm = (String) datalist.get(t).get(
                                    "bgqxdm");
                            String id_ = (String) datalist.get(t).get("id_");
                            if (cwsj == null || cwsj == "" || bgqxdm == null
                                    || bgqxdm == "") {
                                continue;
                            }
                            // 获取当前时间
                            String dateNowStr = getData();
                            // 获取保管期限值
                            int bgqx = getbgqx(bgqxdm);
                            if (bgqx == 0) {
                                continue;
                            }
                            if (bgqx == 10000) {
                                continue;
                            }
                            if (cwsj.matches("[0-9]+")) {
                                if (cwsj.length() != 8) {
                                    continue;
                                }
                                // 加上保管期限时间
                                String nd = cwsj.substring(0, 4);
                                String gqnd = Integer.valueOf(nd) + bgqx + "";
                                String gqsj = gqnd + cwsj.substring(4, 8);
                                try {
                                    // 把年月日转成毫秒值
                                    long gqtime = new SimpleDateFormat(
                                            "yyyyMMdd").parse(gqsj).getTime();
                                    long nowtime = new SimpleDateFormat(
                                            "yyyyMMdd").parse(dateNowStr)
                                            .getTime();
                                    if (gqtime > nowtime) {
                                        continue;
                                    } else {
                                        // 档案过期
                                        // 修改标记
                                        String sql3 = "update " + tablename
                                                + " set _jd='1' where id_='"
                                                + id_ + "'";
                                        jdbcTemplate.execute(sql3);
                                        index = index + 1;
                                    }
                                } catch (ParseException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                    continue;
                                }
                            } else {
                                continue;
                            }
                        }
                    }
                    // 把销毁信息存入到数据库中
                    String sql4 = "insert into archive_appraise (id_,tablename,tabledesc,appraise_time,users,name,appraise_count,destory_count) values('"
                            + AOSId.uuid()
                            + "','"
                            + dto.getString("tablename")
                            + "','"
                            + dto.getString("tabledesc")
                            + "','"
                            + dto.getString("topicData")
                            + "','"
                            + dto.getString("user")
                            + "','"
                            + dto.getString("person")
                            + "','"
                            + dto.getString("_total")
                            + "','"
                            + index + "')";
                    jdbcTemplate.execute(sql4);
                } else {
                    outDto.setAppCode(-2);
                    return outDto;
                }
            } else {
                outDto.setAppCode(-1);
                return outDto;
            }
        }
        outDto.setAppCode(index);
        return outDto;

    }

    /**
     * 获取保管期限值
     *
     * @author PX
     * @return
     *
     *         2018-11-27
     */
    private int getbgqx(String bgqxdm) {
        // TODO Auto-generated method stub
        if (bgqxdm.equalsIgnoreCase("C")) {
            return 30;
        } else if (bgqxdm.equalsIgnoreCase("D10")) {
            return 10;
        } else if (bgqxdm.equalsIgnoreCase("D30")) {
            return 30;
        } else if (bgqxdm.equalsIgnoreCase("D")) {
            return 10;
        } else if (bgqxdm.equalsIgnoreCase("Y")) {
            return 10000;
        } else {
            return 0;
        }
    }

    /**
     * 获取当前时间
     *
     * @author PX
     * @return
     *
     *         2018-11-27
     */
    private String getData() {
        // TODO Auto-generated method stub
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String dateNowStr = sdf.format(d);
        return dateNowStr;
    }

    /**
     * 查询待销毁列表
     *
     * @author PX
     * @param dto
     *
     *            2018-11-28
     * @return
     */
    public List<Map<String, Object>> listwaitdestroy(Dto dto) {
        // TODO Auto-generated method stub
        String tablename = dto.getString("tablename");
        String enfield = "id_";
        String fieldName="";
        List<Archive_tablefieldlistPO> list = archive_tablefieldlistMapper
                .getDataFieldDisplayAll(dto.getString("tablename"));
        String orderenfield = "";
        for (int i = 0; i < list.size(); i++) {
            fieldName = list.get(i).getFieldenname();
            enfield = enfield + "," + fieldName;
            if (i == 1) {
                orderenfield = enfield;
            }
        }
        int start=(Integer.valueOf(dto.getString("page"))-1)*dto.getPageLimit();
        String sql = "select * from " + tablename + " where _jd='1'";
        sql = "WITH aos_query_ AS (SELECT TOP 100 PERCENT ROW_NUMBER () OVER (ORDER BY id_) AS aos_rn_,"
                + "id_,_xuhao,tm,nd,dh,ys,bgqxdm,_oldnd,bz,cwsj"
                + " FROM "
                + dto.getString("tablename")
                + " WHERE 1=1 and _jd='1') "
                + "SELECT * FROM aos_query_ WHERE aos_rn_ BETWEEN "
                + (start+1)
                + " AND "
                + dto.getPageLimit()
                * Integer.valueOf(dto.getString("page"))
                + " ORDER BY aos_rn_";
        List<Map<String, Object>> list2 = jdbcTemplate.queryForList(sql);
        LinkedList<Map<String, Object>> linkedlist=new LinkedList<Map<String, Object>>();

        //进行迭代，把已保管年限加到里面
        if(list2!=null&&list2.size()>0){
            for(int i=0;i<list2.size();i++){
                try {
                    String cwrq=list2.get(i).get("cwsj")+"";
                    String cwnd=cwrq.substring(0, 4);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
                    Date date = new Date();
                    String nownd = sdf.format(date);
                    Integer datend=Integer.valueOf(nownd)-Integer.valueOf(cwnd);
                    list2.get(i).put("_oldnd",datend);

                    list2.get(i).put("_xuhao",i);
                    list2.get(i).remove("aos_rn_");
                } catch (Exception e) {
                    // TODO: handle exception
                    list2.get(i).put("_oldnd",0);
                    list2.get(i).remove("aos_rn_");
                }
            }
        }
        return list2;
    }

    /**
     * 销毁档案
     *
     * @author PX
     * @param qDto
     *
     *            2018-11-28
     * @return
     */
    public boolean deleteData(Dto qDto) {
        // TODO Auto-generated method stub
        String tablename = qDto.getString("tablename");
        String id_ = qDto.getString("id_");
        try {
            String sql = "delete  from " + tablename + " where id_='" + id_
                    + "'";
            jdbcTemplate.execute(sql);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return false;
        }
        return true;

    }

    /**
     * 还原档案
     *
     * @author PX
     * @param qDto
     *
     *            2018-11-28
     * @return
     */
    public boolean returnData(Dto qDto) {
        // TODO Auto-generated method stub
        String tablename = qDto.getString("tablename");
        String id_ = qDto.getString("id_");
        try {
            String sql = "update " + tablename + " set _jd = NULL where id_='"
                    + id_ + "'";
            jdbcTemplate.execute(sql);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return false;
        }
        return true;
    }
    /**
     * 鉴定记录
     *
     * @author PX
     * @return
     *
     * 2018-11-28
     */
    public List<Map<String, Object>> listappraisedestroy() {
        // TODO Auto-generated method stub
        String sql="select * from archive_appraise";
        return jdbcTemplate.queryForList(sql);
    }
    /**
     * 得到销毁数据当前要展示页面的
     *
     * @author PX
     * @param sql
     *
     * 2019-3-19
     * @return
     */
    public List<Map<String, Object>> getlistString(String sql) {
        // TODO Auto-generated method stub
        List<Map<String,Object>> list = jdbcTemplate.queryForList(sql);
        return list;
    }
    /**
     * 得到制定门类总数
     *
     * @author PX
     * @param qDto
     *
     * 2019-3-19
     * @return
     */
    public int getSumPage(Dto qDto) {
        // TODO Auto-generated method stub
        String sql="select * from "+qDto.getString("tablename")+" where _jd='1'";
        try {
            return jdbcTemplate.queryForList(sql).size();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return 0;
        }

    }
    public List<Map<String, Object>> getFiles(Dto qDto) {
        // TODO Auto-generated method stub
        Dto out=Dtos.newDto();
        String id_=qDto.getString("id_");
        String tablename=qDto.getString("tablename");
        String sql="select * from "+tablename+"_path where tid='"+id_+"' order by _index";
        try {
            List<Map<String,Object>> list = jdbcTemplate.queryForList(sql);
            return list;
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return null;
        }
    }
    public boolean addForm(Dto qDto){
        //根据所有的数据进行添加
        try{
            Archive_topicPO archive_topicPO=new Archive_topicPO();
            AOSUtils.copyProperties(qDto,archive_topicPO);

            archive_topicPO.setId_(AOSId.uuid());
            archive_topicMapper.insert(archive_topicPO);
            String[] selections = qDto.getRows();

            for (String id_ : selections) {
                Archive_topic_tablenameidPO archive_topic_tablenameidPO=new Archive_topic_tablenameidPO();
                archive_topic_tablenameidPO.setId_(AOSId.uuid());
                archive_topic_tablenameidPO.setPid(archive_topicPO.getId_());
                archive_topic_tablenameidPO.setTablename(qDto.getString("tablename"));
                archive_topic_tablenameidPO.setTablename_id(id_);
                archive_topic_tablenameidMapper.insert(archive_topic_tablenameidPO);
            }
            return  true;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }
    /**
     * 操作记录
     * @param qDto
     */
    public List<Archive_topicPO> listopertion(Dto qDto) {
        List<Archive_topicPO> list=archive_topicMapper.list(qDto);
        return list;
    }
    /**
     * 操作记录
     * @param qDto
     */
    public boolean adddetails(Dto qDto,UserInfoVO userInfoVO) {
        try{
            Archive_compilation_rwPO archive_compilation_rwPO=new Archive_compilation_rwPO();
            AOSUtils.copyProperties(qDto,archive_compilation_rwPO);
            archive_compilation_rwMapper.updateByKey(archive_compilation_rwPO);
            //针对所有用户授予目录树权限
            addtree_user(qDto);
            //此时给撰稿人一个消息提醒
            notificationService.SubmitByFirst(qDto,userInfoVO);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 对于所有用户都目录树赋权
     * @param qDto
     */
    private void addtree_user(Dto qDto) {
        String zgrs=qDto.getString("zgr");
        String hgrs=qDto.getString("hgr");
        String jdrs=qDto.getString("jdr");
        String zbjs=qDto.getString("zbj");
        //添加到数组中,保证数组不重复就可以
        List list=new ArrayList<>();
        String[] zgr=zgrs.split(",");
        for(int i=0;i<zgr.length;i++){
            list.add(zgr[i]);
        }
        String[] hgr=hgrs.split(",");
        for(int i=0;i<hgr.length;i++){
            list.add(hgr[i]);
        }
        String[] jdr=jdrs.split(",");
        for(int i=0;i<jdr.length;i++){
            list.add(jdr[i]);
        }
        String[] zbj=zbjs.split(",");
        for(int i=0;i<zbj.length;i++){
            list.add(zbj[i]);
        }
        list= ridRepeat1(list);
        //迭代依次赋予权限
        addtreeuser(qDto,list);



    }

    /**
     * 添加目录梳全线
     * @param qDto
     * @param list
     */
    private void addtreeuser(Dto qDto, List list) {
        if(list!=null&&list.size()>0){
            for(int i=0;i<list.size();i++){
                String user=(String)list.get(i);
                //得到用户和操作用户的id值
                String user_id=getUserId(user);
                String operator_id_= getUserId(qDto.getUserInfo().getAccount_());
                //得到当前任务的节点的id目录树值(在线课题下面的子节点目录树的id值)
                //String kt_children_id_=getModule_kt_id(qDto.getString("id_"));
                //判断是否存在授权，如果存在就调过，不过在添加
                //addTree_rw(user,user_id,operator_id_,kt_children_id_);
                //得到当前任务的节点的id目录数值的父id值（根据在线课题的子节点的id值，查找在线专题id值）
                //String kt_id_= getModule_parentid(kt_children_id_);
                //判断是否存在授权，如果存在就调过，不过在添加
               // addTree_rw(user,user_id,operator_id_,kt_id_);
                //得到任务的节点的id目录树值（在编课题下面的子节点目录树的id值）
                String sj_children_id_=getModule_sj_id(qDto.getString("id_"));
                //判断是否存在授权，如果存在就调过，不过在添加
                addTree_rw(user,user_id,operator_id_,sj_children_id_);
                //得到数据库id（根据专题数据下面的子节点的id值，得到专题数据的id值）
                //String sj_id_= getModule_parentid(sj_children_id_);
                //同时再添加任务的数据库
                //addTree_rw(user,user_id,operator_id_,sj_id_);

            }
        }
    }

    /**
     * 根据父节点id得到子节点
     * @param parent_id_
     * @return
     */
    private String getModule_parentid(String parent_id_) {
        Dto out=Dtos.newDto();
        String module_id_="";
        String sql="select * from aos_sys_module where id_='"+parent_id_+"'";
        List<Map<String,Object>> list=jdbcTemplate.queryForList(sql);
        if(list!=null&&list.size()>0){
            module_id_=(String)list.get(0).get("parent_id_");
        }
        return module_id_;
    }

    /**
     * 添加授权
     * @param user
     * @param user_id
     * @param operator_id_
     * @param module_id_
     */
    private void addTree_rw(String user, String user_id, String operator_id_, String module_id_) {
       Aos_sys_module_userPO aos_sys_module_userPO=new Aos_sys_module_userPO();
       aos_sys_module_userPO.setUser_id_(user_id);
       aos_sys_module_userPO.setModule_id_(module_id_);

       aos_sys_module_userPO.setOperator_id_(operator_id_);
       aos_sys_module_userPO.setGrant_type_("1");
      String sql="select * from aos_sys_module_user where user_id_='"+user_id+"' and module_id_='"+module_id_+"' and grant_type_='1'";
        List<Map<String,Object>> list=jdbcTemplate.queryForList(sql);
        if(list==null||list.size()<=0){
            aos_sys_module_userPO.setId_(AOSId.uuid());
            aos_sys_module_userPO.setOperate_time_(AOSUtils.getDateTimeStr());
            aos_sys_module_userMapper.insert(aos_sys_module_userPO);
        }
    }

    private String getModule_kt_id(String id_) {
        String module_id_="";
        Archive_compilation_rwPO archive_compilation_rwPO=archive_compilation_rwMapper.selectByKey(id_);
        Dto out=Dtos.newDto();
        out.put("name_",archive_compilation_rwPO.getByrwbh()+archive_compilation_rwPO.getByrwmc());
        List<Aos_sys_modulePO> list=aos_sys_moduleMapper.list(out);
        if(list!=null&&list.size()>0){
                for(int i=0;i<list.size();i++){
                    String cascade_id_=list.get(i).getCascade_id_();
                   // if(cascade_id_.contains("0.024.006")){
                        module_id_=list.get(i).getId_();
                   // }
                }
        }
        return module_id_;
    }
    private String getModule_sj_id(String id_) {
        String module_id_="";
        Archive_compilation_rwPO archive_compilation_rwPO=archive_compilation_rwMapper.selectByKey(id_);
        Dto out=Dtos.newDto();
        out.put("name_",archive_compilation_rwPO.getByrwbh()+archive_compilation_rwPO.getByrwmc());
        List<Aos_sys_modulePO> list=aos_sys_moduleMapper.list(out);
        if(list!=null&&list.size()>0){
            for(int i=0;i<list.size();i++){
                String cascade_id_=list.get(i).getCascade_id_();
                // if(cascade_id_.contains("0.024.006")){
                module_id_=list.get(i).getId_();
                // }
            }
        }
        return module_id_;
    }
    public List<String> ridRepeat1(List<String> list) {
        List<String> listNew = new ArrayList<String>();
        for (String str : list) {
            if (!listNew.contains(str)) {
                listNew.add(str);
            }
        }
        return listNew;
    }


    /**
     * 查询审核类型
     * @param id_
     */
    public Archive_topicPO findtopic(String id_) {
        Archive_topicPO archive_topicPO=archive_topicMapper.selectByKey(id_);
        return archive_topicPO;
    }




    /**
     * 操作日志查看
     * @param qDto
     * @return
     */
    public List<Map<String, Object>> listwritelogin(Dto qDto) {
        List<Map<String, Object>> list=new ArrayList<Map<String, Object>>();
        String sql="select archive_write_topic.id_ as id_," +
                " archive_write_topic.tid as tid," +
                "archive_write_topic.task_number as task_number," +
                "archive_write_topic.write_number as write_number," +
                "archive_write_topic.write_datatime as write_datatime," +
                "archive_write_topic.tablename as tablename," +
                "archive_write_topic.write_createperson as write_createperson," +
                "archive_write_topic.write_description as write_description," +
                "archive_write_topic.task_name as task_name," +
                "archive_write_topic.write_name as write_name," +
                "archive_topic_user.operate_description as operate_description ," +
                "archive_topic_user.compilation_message as compilation_message from archive_write_topic,archive_topic_user,archive_task where archive_write_topic.tid=archive_task.id_ and archive_topic_user.pid=archive_write_topic.id_ and " +
                " archive_topic_user.type='1' and archive_write_topic.write_createperson ='"+qDto.getString("user")+"'";

        list=jdbcTemplate.queryForList(sql);
        return list;
    }

    public String getpath(String id_, String topic_id_,String tablename) {
        Archive_topicPO archive_topicPO = archive_topicMapper.selectByKey(topic_id_);
        String sql = "select * from " + tablename + "_path where tid ='" + id_ + "'";
        String filepath = "";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                Map<String, Object> map = list.get(i);
                String path = (String) map.get("_s_path");
                if (path.split("\\.")[1].equals("pdf")) {
                    String filename = (String) map.get("dirname");
                    // 组合路径
                    filepath = topicAddress  + File.separator+filename
                            + path;
                    // 获取当前ip地址
                    // 路径批量替换
                    // wsda/2e9275c32e6849afab038892747249c2/数字档案馆系统测试办法（档办发[2014]6号).pdf
                    filepath = filepath.replace("\\", "/");
                    break;
                }
            }
        }
        return filepath;
    }
    public String getcompilationmessage(Dto dto){
        String topic_id=dto.getString("topic_id_");
        String type=dto.getString("type");
        String user=dto.getString("user");
        String compilation_message="";
        String sql="select compilation_message from archive_topic_user where   pid='"+topic_id+"' order by index_ desc";
        List<Map<String,Object>> list=jdbcTemplate.queryForList(sql);
        if(list!=null&&list.size()>0){
            compilation_message=(String)list.get(0).get("compilation_message");
        }

        return compilation_message;
    }
    public String getcompilation_message(Dto dto){
        String topic_id=dto.getString("topic_id_");
        String type=dto.getString("type");
        String compilation_message="";
        String sql="select compilation_message from archive_topic_user where  pid='"+topic_id+"' order by index_ desc";
        List<Map<String,Object>> list=jdbcTemplate.queryForList(sql);
        if(list!=null&&list.size()>0){
            compilation_message=(String)list.get(0).get("compilation_message");
        }
        return compilation_message;
    }
    /**
     * 专题分类
     * @param qDto
     * @return
     */
    public List<Dto> listseminar(Dto qDto) {

		List<Archive_seminarPO> list = archive_seminarMapper.list(qDto);
		List<Dto> treeNodes = new ArrayList<Dto>();
		String iconPath = System.getProperty(AOSCons.CXT_KEY) + AOSXmlOptionsHandler.getValue("icon_path");
		for (int i=0;i<list.size();i++) {
			Dto treeNode = Dtos.newDto();
			treeNode.put("id", list.get(i).getId_());
			treeNode.put("text", list.get(i).getName_());
			//自定义
			if(i>=0&&i<=9){
				treeNode.put("cascade_id_", "0.00"+(i+1));
			}else if(i>=10&&i<=99){
				treeNode.put("cascade_id_", "0.0"+(i+1));
			}else if(i>=100&&i<=999){
				treeNode.put("cascade_id_", "0."+(i+1));
			}
			//treeNode.put("icon", iconPath + "folder1.png");
			treeNode.put("leaf", true);
			treeNode.put("expanded", true);
			treeNodes.add(treeNode);
		}
		return treeNodes;
    }

    public boolean saveseminar(Dto qDto) {
        try{
            Archive_seminarPO archive_seminarPO=new Archive_seminarPO();
            AOSUtils.copyProperties(qDto,archive_seminarPO);
            archive_seminarPO.setId_(AOSId.uuid());
            archive_seminarMapper.insert(archive_seminarPO);
            return true;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public Archive_seminarPO getSeminarById(String id) {
        Archive_seminarPO archive_seminarPO=archive_seminarMapper.selectByKey(id);
        return archive_seminarPO;
    }

    /**
     * 修改专题
     * @param qDto
     * @return
     */
    public boolean updateSeminar(Dto qDto) {
        try{
            Archive_seminarPO archive_seminarPO=new Archive_seminarPO();
            AOSUtils.copyProperties(qDto,archive_seminarPO);
            archive_seminarMapper.updateByKey(archive_seminarPO);
            return true;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteSeminar(Dto qDto) {
        try{
            archive_seminarMapper.deleteByKey(qDto.getString("id_"));
            return true;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public List<Archive_taskPO> listTask(Dto qDto) {
        try{
            List<Archive_taskPO> list=archive_taskMapper.list(qDto);
            return list;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public boolean savetask(Dto qDto) {
        try{
            Archive_taskPO archive_taskPO=new Archive_taskPO();
            AOSUtils.copyProperties(qDto,archive_taskPO);
            archive_taskPO.setId_(AOSId.uuid());
            archive_taskPO.setFlag("1");
            archive_taskMapper.insert(archive_taskPO);
            return true;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean updatetask(Dto qDto) {
        try{
            Archive_taskPO archive_taskPO=new Archive_taskPO();
            AOSUtils.copyProperties(qDto,archive_taskPO);
            archive_taskMapper.updateByKey(archive_taskPO);
            return true;
        }catch(Exception e){
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
        public Dto deleteTask(Dto qDto){
            Dto outDto = Dtos.newDto();
            String[] selections = qDto.getRows();
            int del = 0;
            for (String id_ : selections) {
                jdbcTemplate.execute(" delete from archive_task where id_='"
                        + id_ + "'");
                del++;
            }
            String msg = "操作完成，";
            if (del > 0) {
                msg = AOSUtils.merge(msg + "成功删除信息[{0}]个", del);
            }
            outDto.setAppMsg(msg);
            return outDto;

    }


    public boolean addwriteForm(Dto qDto) {
        //根据所有的数据进行添加
        try{
            //查询到任务的id值
            String tid="";
            String sql="select * from archive_task where task_number='"+qDto.getString("task_number")+"'";
            List<Map<String,Object>> list=jdbcTemplate.queryForList(sql);
            if(list!=null&&list.size()>0){
                tid=(String)list.get(0).get("id_");
            }
            qDto.put("tid",tid);
            Archive_write_topicPO archive_write_topicPO=new Archive_write_topicPO();
            AOSUtils.copyProperties(qDto,archive_write_topicPO);
            archive_write_topicPO.setId_(AOSId.uuid());
            archive_write_topicMapper.insert(archive_write_topicPO);
            String[] selections = qDto.getRows();
            for (String id_ : selections) {
                Archive_topic_tablenameidPO archive_topic_tablenameidPO=new Archive_topic_tablenameidPO();
                archive_topic_tablenameidPO.setId_(AOSId.uuid());
                archive_topic_tablenameidPO.setPid(archive_write_topicPO.getId_());
                archive_topic_tablenameidPO.setTablename(qDto.getString("tablename"));
                archive_topic_tablenameidPO.setTablename_id(id_);
                archive_topic_tablenameidMapper.insert(archive_topic_tablenameidPO);
            }
            return  true;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public List<Map<String,Object>> listwrite(Dto qDto, HttpSession session) {
        Dto dto=Dtos.newDto();
        String sql="select archive_write_topic.id_ as id_," +
                "archive_write_topic.tid as tid," +
                "archive_write_topic.task_number as task_nummber," +
                "archive_write_topic.write_number as write_number," +
                "archive_write_topic.write_datatime as write_datatime," +
                "archive_write_topic.tablename as tablename," +
                "archive_write_topic.write_createperson as write_createperson," +
                "archive_write_topic.write_description as write_description," +
                "archive_write_topic.task_name as task_name," +
                "archive_write_topic.write_name as write_name," +
                "archive_task.task_name as task_name," +
                "archive_task.general as general from archive_write_topic,archive_task where archive_write_topic.tid=archive_task.id_ and archive_write_topic.write_createperson='"+session.getAttribute("user")+"'";
        List<Map<String,Object>> list=jdbcTemplate.queryForList(sql);
        return list;
    }
    public List<Map<String,Object>> listwrite2(Dto qDto, HttpSession session) {
        Dto dto=Dtos.newDto();
        String sql="select archive_write_topic.id_ as id_," +
                "archive_write_topic.tid as tid," +
                "archive_write_topic.task_number as task_nummber," +
                "archive_write_topic.write_number as write_number," +
                "archive_write_topic.write_datatime as write_datatime," +
                "archive_write_topic.tablename as tablename," +
                "archive_write_topic.write_createperson as write_createperson," +
                "archive_write_topic.write_description as write_description," +
                "archive_write_topic.task_name as task_name," +
                "archive_write_topic.write_name as write_name," +
                "archive_task.task_name as task_name," +
                "archive_task.general as general from archive_write_topic,archive_task where archive_write_topic.tid=archive_task.id_ and archive_write_topic.write_createperson='"+session.getAttribute("user")+"' and archive_write_topic.write_flag='1'";
        List<Map<String,Object>> list=jdbcTemplate.queryForList(sql);
        return list;
    }

    public  List<Map<String,Object>> listTaskName(Dto qDto,HttpSession session) {
            String sql="select * from archive_task where write_user='"+session.getAttribute("user")+"'";
            List<Map<String,Object>> list=jdbcTemplate.queryForList(sql);
            return list;
    }
    //得到用户列表
    public List<Map<String,Object>> getUserList(String user) {
            //根据用户名称得到它的id
        String sql="select org_id_ from aos_sys_user where account_='"+user+"'";
        List<Map<String,Object>> userlist=new ArrayList<Map<String,Object>>();
        List<Map<String,Object>> list=jdbcTemplate.queryForList(sql);
        if(list!=null&&list.size()>0){
            String id_=(String)list.get(0).get("org_id_");
            String sql2="select * from aos_sys_user where org_id_='"+id_+"' and account_<>'"+user+"'";
            userlist=jdbcTemplate.queryForList(sql2);
        }
        return userlist;
    }
    //得到用户列表
    public List<Map<String,Object>> getUserList2(String user) {
        //根据用户名称得到它的id
        List<Map<String,Object>> userlist=new ArrayList<Map<String,Object>>();
            String sql2="select * from aos_sys_user ";
            userlist=jdbcTemplate.queryForList(sql2);
        return userlist;
    }
    public List<Archive_topic_userPO> listoperationlogin(Dto qDto) {
        String pid = qDto.getString("pid");
        List<Archive_topic_userPO> list = archive_topic_userMapper.list(qDto);
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                String type = (String) list.get(i).getType();
                if ("1".equals(type)) {
                    list.get(i).setType("撰稿");
                }else if ("2".equals(type)) {
                    list.get(i).setType("合稿");
                } else {
                    list.get(i).setType("审核");
                }
            }
        }
        return list;
    }


    /**
     *
     * 删除条目
     *
     * @param dto
     * @return
     */
    public Dto delete_rw(Dto dto) {
        Dto outDto = Dtos.newDto();
        String[] selections = dto.getRows();
        int del = 0;
        for (String id_ : selections){
            //删除节点
            //1查询条目的任务名称和任务编号组合组合字段，并查询到节点id值
            delete_tree(id_);
            //删除专题数据的目录树
            //delete_tree_children(id_,outDto.getString("tabledesc"));
            //删除任务
            jdbcTemplate.execute(" delete from archive_compilation_rw where id_='"
                    + id_ + "'");
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
     *
     * 删除条目
     *
     * @param dto
     * @return
     */
    public Dto delete_data(Dto dto) {
        Dto outDto = Dtos.newDto();
        String tablename=dto.getString("tablename");
        String pid=dto.getString("pid");
        String[] selections = dto.getRows();
        int del = 0;
        for (String id_ : selections) {

            jdbcTemplate.execute(" delete from archive_compilation_tablenameid where pid='"
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


    public boolean updatecompilation(Dto dto,UserInfoVO userInfoVO) {
        try{
            Archive_compilation_rwPO archive_compilation_rwPO=new Archive_compilation_rwPO();
            archive_compilation_rwPO.setId_(dto.getString("id_"));
            archive_compilation_rwPO.setFlag_submit("已提交");
            archive_compilation_rwMapper.updateByKey(archive_compilation_rwPO);
            //此时添加到数据compolitation_rw中
            //addCompilation_tablenameid(qDto,archive_compilation_rwPO.getId_());
            notificationService.SubmitBianYian(dto,userInfoVO);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    private List<Map<String, Object>> getModule_byid_(String cascade_id_) {
        String sql="select * from aos_sys_module where id_='"+cascade_id_+"'";
        return jdbcTemplate.queryForList(sql);
    }
    public List<Map<String, Object>> getData(Dto inDto, HttpSession session) {
        String tablename="";
        List<Map<String, Object>> listmodule=getModule_byid_(inDto.getString("zt_id"));
        if(listmodule!=null&&listmodule.size()>0){
            tablename=(String)listmodule.get(0).get("tablename");
        }
        // 排列方式，页码，每页条目数
        Integer limit = Integer.valueOf(inDto.getString("limit"));
        Integer page = Integer.valueOf(inDto.getString("page"));
        // 条目开始索引
        Integer pageStart = (page - 1) * (limit);
        //根据任务id得到所有的条目id值
        String sql2="select * from "+tablename+" where id_ in(select tablename_id from archive_topic_tablenameid where pid ='"+inDto.getString("zt_id")+"' order by id_ offset "+pageStart+" rows fetch next "+limit+" rows only)";
        List<Map<String,Object>> listDto =jdbcTemplate.queryForList(sql2);
        if(listDto!=null&&listDto.size()>0){
            for(int i=0;i<listDto.size();i++){
                listDto.get(i).put("tablename",tablename);
            }
        }
        return listDto;
    }

    public boolean updaterwno(Dto dto,UserInfoVO userInfoVO) {
        try{
            Archive_compilation_rwPO archive_compilation_rwPO=new Archive_compilation_rwPO();
            archive_compilation_rwPO.setId_(dto.getString("id_"));

            String next_kf_message=dto.getString("next_kf_message");
            //更新审核人 审核时间
            String user=userInfoVO.getAccount_();
            long nowtime = System.currentTimeMillis();
            SimpleDateFormat dfDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            String time = dfDateFormat.format(new Date());
            archive_compilation_rwPO.setShry(user);
            archive_compilation_rwPO.setShsj(time);
            archive_compilation_rwPO.setShyj(next_kf_message);
            archive_compilation_rwPO.setFlag_examine("未通过");
            archive_compilation_rwMapper.updateByKey(archive_compilation_rwPO);
            //此时添加到数据compolitation_rw中
            //addCompilation_tablenameid(qDto,archive_compilation_rwPO.getId_());
            notificationService.TaskBianYianNo(dto,userInfoVO);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    public boolean updaterwyes(Dto dto,UserInfoVO userInfoVO) {
        try{
            Archive_compilation_rwPO archive_compilation_rwPO=new Archive_compilation_rwPO();
            archive_compilation_rwPO.setId_(dto.getString("id_"));
            String next_kf_message=dto.getString("next_kf_message");
            //更新审核人 审核时间
            String user=userInfoVO.getAccount_();
            long nowtime = System.currentTimeMillis();
            SimpleDateFormat dfDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            String time = dfDateFormat.format(new Date());
            archive_compilation_rwPO.setShry(user);
            archive_compilation_rwPO.setShsj(time);
            archive_compilation_rwPO.setShyj(next_kf_message);
            archive_compilation_rwPO.setFlag_examine("已通过");
            archive_compilation_rwMapper.updateByKey(archive_compilation_rwPO);
            //此时添加到数据compolitation_rw中
            //addCompilation_tablenameid(qDto,archive_compilation_rwPO.getId_());
            notificationService.TaskBianYian(dto,userInfoVO);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public String queryConditions2_noid(Dto inDto) {
        String tablename=inDto.getString("tablename");
        String id_=inDto.getString("id_");
        String query_ids="";
        String sql="select tablename_id from archive_compilation_tablenameid where pid='"+id_+"' and tablename='"+tablename+"'";
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

    public boolean updatebyrw_data(Dto inDto) {
        try{
            String tablename=inDto.getString("tablename");
            String ids=inDto.getString("ids");
            String by_id=inDto.getString("by_id");
            if(ids!=null&&ids!=""){
                String[] id=ids.split(",");
                for(int i=0;i<id.length;i++){
                    Archive_compilation_tablenameidPO archive_compilation_tablenameidPO=new Archive_compilation_tablenameidPO();
                    archive_compilation_tablenameidPO.setId_(AOSId.uuid());
                    archive_compilation_tablenameidPO.setPid(by_id);
                    archive_compilation_tablenameidPO.setTablename(tablename);
                    archive_compilation_tablenameidPO.setTablename_id(id[i]);
                    archive_compilation_tablenameid.insert(archive_compilation_tablenameidPO);
                }
            }
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public String getcompilation_zhuangao_message(Dto dto) {
        String topic_id=dto.getString("topic_id_");
        String type=dto.getString("type");
        String user=dto.getString("user");
        String compilation_message="";
        String sql2="select compilation_message from archive_topic_user where   pid='"+topic_id+"' and type='"+type+"' order by index_ desc";
        String sql="select zg_message from archive_compilation_rw where id_='"+topic_id+"'";
        List<Map<String,Object>> list=jdbcTemplate.queryForList(sql);
        if(list!=null&&list.size()>0){
            compilation_message=(String)list.get(0).get("zg_message");
        }
        return compilation_message;
    }
    public String getcompilation_hegao_message(Dto dto) {
        String topic_id=dto.getString("topic_id_");
        String type=dto.getString("type");
        String user=dto.getString("user");
        String compilation_message="";
        String sql="select compilation_message from archive_topic_user where   pid='"+topic_id+"' and type='"+type+"' order by index_ desc";
        List<Map<String,Object>> list=jdbcTemplate.queryForList(sql);
        if(list!=null&&list.size()>0){
            compilation_message=(String)list.get(0).get("compilation_message");
        }
        return compilation_message;
    }
    public String getcompilation_jiaodui_message(Dto dto) {
        String topic_id=dto.getString("topic_id_");
        String type=dto.getString("type");
        String user=dto.getString("user");
        String compilation_message="";
        String sql="select compilation_message from archive_topic_user where   pid='"+topic_id+"' and type='"+type+"' order by index_ desc";
        List<Map<String,Object>> list=jdbcTemplate.queryForList(sql);
        if(list!=null&&list.size()>0){
            compilation_message=(String)list.get(0).get("compilation_message");
        }
        return compilation_message;
    }
    public void delete_tree(String rwid) {
        //查询到当前的编号和任务名称组合的节点id值
        String sql="select * from archive_compilation_rw where id_='"+rwid+"'";
        String treename="";
        List<Map<String,Object>> list=jdbcTemplate.queryForList(sql);
        if(list!=null&&list.size()>0){
            treename=(String)list.get(0).get("byrwbh")+(String)list.get(0).get("byrwmc");
        }
        //根据名称查询到节点id集合
        String sql2="select * from aos_sys_module where name_='"+treename +"'";
        List<Map<String,Object>> list2=jdbcTemplate.queryForList(sql2);
        if(list2!=null&&list2.size()>0){
            for(int i=0;i<list2.size();i++){
                Dto out=Dtos.newDto();
                out.put("aos_rows_",(String)list2.get(i).get("id_"));
                moduleService.deleteModule(out);
            }
        }
    }
    public void delete_tree_children(String rwid,String tabledesc) {
        //查询到当前的编号和任务名称组合的节点id值
        String sql="select * from archive_compilation_rw where id_='"+rwid+"'";
        String treename="";
        List<Map<String,Object>> list=jdbcTemplate.queryForList(sql);
        if(list!=null&&list.size()>0){
            treename=(String)list.get(0).get("byrwbh")+(String)list.get(0).get("byrwmc")+"--"+(String)list.get(0).get("sjkms");
        }
        //根据名称查询到节点id集合
        String sql2="select * from aos_sys_module where name_='"+treename +"'";
        List<Map<String,Object>> list2=jdbcTemplate.queryForList(sql2);
        if(list2!=null&&list2.size()>0){
            for(int i=0;i<list2.size();i++){
                Dto out=Dtos.newDto();
                out.put("aos_rows_",(String)list2.get(i).get("id_"));
                moduleService.deleteModule(out);
            }
        }
        //
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

    public boolean booleanpath(Dto qDto) {
        String rw_id_=qDto.getString("rw_id_");
        Archive_compilation_rwPO archive_compilation_rwPO=archive_compilation_rwMapper.selectByKey(rw_id_);
        String _path=archive_compilation_rwPO.get_path();
        //设置全路径名称
        if(_path==null||_path.length()==0){
            return false;
        }
        File file=new File(filePath+"//"+"byrw"+"//"+rw_id_+"//"+_path);
        if(!file.exists()){
            return false;
        }
        return true;
    }
    public String getRwpath(Dto qDto)throws Exception {
        String message="";
        String htmlpath="";
        String topic_id_=qDto.getString("topic_id_");
        String rw_id_=qDto.getString("rw_id_");
        Archive_compilation_rwPO archive_compilation_rwPO=archive_compilation_rwMapper.selectByKey(rw_id_);
        String _path=archive_compilation_rwPO.get_path();
        //设置全路径名称
        if(_path==null||_path.length()==0){
            return message;
        }
        File file=new File(filePath+"//"+"byrw"+"//"+rw_id_+"//"+_path);
        if(!file.exists()){
            return message;
        }
        try {
            htmlpath=wordtohtml(filePath+"//"+"byrw"+"//"+rw_id_+"//"+_path);
            return  htmlpath;
        }catch(Exception e){
            e.printStackTrace();
            message="";
        }
        return message;
    }
    private String wordtohtml(String docpath) {
        File outputFolder = null;
        File outputPictureFolder = null;
        try {
            //转换后HTML文件存放位置
            outputFolder = new File(docpath.substring(0,docpath.lastIndexOf("/")+1));
            if (null != outputFolder) {
                //转换后原word中图片存放位置
                String outputPictureFolderPath = docpath.substring(0,docpath.lastIndexOf("/")+1);
                outputPictureFolder = new File(outputPictureFolderPath);
                outputPictureFolder.mkdir();
            }
        } catch (Exception e1) {

        }
        try {
            //被转换的word文件
            File convertedWordFile = new File(docpath);
            return convert2Html(convertedWordFile, outputFolder, outputPictureFolder);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    public  String convert2Html(File wordFile, File outputFolder,
                                    final File outputPictureFolder) throws TransformerException,
            IOException, ParserConfigurationException {
        //创建被转换的word HWPFDocument对象
        HWPFDocument wordDocument = new HWPFDocument(new FileInputStream(wordFile));
        //创建word转换器，并设置对于图片如何处理
        WordToHtmlConverter wordToHtmlConverter = new WordToHtmlConverter(
                DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument());
        wordToHtmlConverter.setPicturesManager(new PicturesManager() {
            public String savePicture(byte[] content, PictureType pictureType,
                                      String suggestedName, float widthInches, float heightInches) {
                return outputPictureFolder.getName() + File.separator
                        + suggestedName;
            }
        });
        //开始转换word为HTML
        wordToHtmlConverter.processDocument(wordDocument);
        //开始转换word中图片到图片存放目录
        List<Picture> pics = wordDocument.getPicturesTable().getAllPictures();
        if (pics != null) {
            for (int i = 0; i < pics.size(); i++) {
                Picture pic = (Picture) pics.get(i);
                try {
                    pic.writeImageContent(new FileOutputStream(
                            outputPictureFolder.getAbsolutePath()
                                    + File.separator
                                    + pic.suggestFullFileName()));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        //将word转换为HTML，输出到指定目录
        Document htmlDocument = wordToHtmlConverter.getDocument();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DOMSource domSource = new DOMSource(htmlDocument);
        StreamResult streamResult = new StreamResult(out);
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer serializer = tf.newTransformer();
        serializer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        serializer.setOutputProperty(OutputKeys.INDENT, "yes");
        serializer.setOutputProperty(OutputKeys.METHOD, "html");
        serializer.transform(domSource, streamResult);
        out.close();
        return new String(out.toByteArray(),"UTF-8");
    }

    /**
     * 根据任务编号得到附件内容
     * @param inDto
     * @return
     */
    public String getfile_fj(Dto qDto) {
        String message="";
        String htmlpath="";
        String rw_id_;
        String _path="";
        String byrwbh=qDto.getString("byrwbh");
        String sql="select * from archive_compilation_rw where byrwbh='"+byrwbh+"'";
        List<Map<String,Object>> list=jdbcTemplate.queryForList(sql);
        if(list!=null&&list.size()>0){
            rw_id_=(String)list.get(0).get("id_");
            _path=(String)list.get(0).get("_path");
        }else{
            return "";
        }
       // Archive_compilation_rwPO archive_compilation_rwPO=archive_compilation_rwMapper.selectByKey(rw_id_);
        //设置全路径名称
        if(_path==null||_path.length()==0){
            return message;
        }
        File file=new File(filePath+"//"+"byrw"+"//"+rw_id_+"//"+_path);
        if(!file.exists()){
            return message;
        }
        try {
            htmlpath=wordtohtml(filePath+"//"+"byrw"+"//"+rw_id_+"//"+_path);
            return  htmlpath;
        }catch(Exception e){
            e.printStackTrace();
            message="";
        }
        return message;
    }

    public boolean booleanfile(Dto qDto) {
        String byrwbh=qDto.getString("byrwbh");
        String _path="";
        String rw_id_="";
        String sql="select * from archive_compilation_rw where byrwbh='"+byrwbh+"'";
        List<Map<String,Object>> list=jdbcTemplate.queryForList(sql);
        if(list!=null&&list.size()>0){
            rw_id_=(String)list.get(0).get("id_");
            _path=(String)list.get(0).get("_path");
        }else{
            return false;
        }
        //设置全路径名称
        if(_path==null||_path.length()==0){
            return false;
        }
        File file=new File(filePath+"//"+"byrw"+"//"+rw_id_+"//"+_path);
        if(!file.exists()){
            return false;
        }
        return true;
    }

    public String findButton_bianyan(UserInfoVO userInfo) {
            String compilation_flag="";
            String sql="select * from aos_sys_user where account_='"+userInfo.getAccount_()+"'";
            List<Map<String,Object>> list= jdbcTemplate.queryForList(sql);
            if(list!=null&&list.size()>0) {
                compilation_flag=(String)list.get(0).get("bianyan_module");
            }
            return compilation_flag;
    }
}


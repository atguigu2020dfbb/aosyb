package cn.osworks.aos.system.modules.service.compilation;

import cn.osworks.aos.core.dao.SqlDao;
import cn.osworks.aos.core.typewrap.Dto;
import cn.osworks.aos.core.typewrap.Dtos;
import cn.osworks.aos.system.dao.mapper.*;
import cn.osworks.aos.system.dao.po.Aos_sys_modulePO;
import cn.osworks.aos.system.dao.po.Archive_compilation_rwPO;
import cn.osworks.aos.system.dao.po.Archive_topicPO;
import cn.osworks.aos.system.dao.po.Compilation_topicPO;
import cn.osworks.aos.system.modules.service.archive.DataService;
import cn.osworks.aos.system.modules.service.resource.ModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
@Service
public class CompilationResultService extends JdbcDaoSupport {
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
    private Compilation_topicMapper compilation_topicMapper;
    @Autowired
    private Archive_compilation_tablenameidMapper archive_compilation_tablenameid;
    @Autowired
    private ModuleService moduleService;
    @Autowired
    private Aos_sys_module_userMapper aos_sys_module_userMapper;
    @Resource
    public void setJd(JdbcTemplate jd) {
        super.setJdbcTemplate(jd);
    }
    public static String filePath = "";
    public static String linkAddress = "";
    public static String topicAddress = "";
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
     * 根据id得到功能模块名称
     * @param cascode_id_
     * @return
     */
    public String getModuleName(String cascode_id_) {
        String name_="";
        Aos_sys_modulePO aos_sys_modulePO=aos_sys_moduleMapper.selectByKey(cascode_id_);
        if(aos_sys_modulePO!=null){
            name_=aos_sys_modulePO.getName_();
        }
        return name_;
    }
    /**
     *
     * 查询记录总数
     *
     * @return
     */
    public int getPageAll(Dto qDto) {
        String query=" ";
        String aos_module_id_=qDto.getString("aos_module_id_");
        String nd=qDto.getString("nd");
        if(nd!=null&&nd.length()!=0){
            query=" and  nd='"+nd+"'";
        }
        //查询年度条件是否存在
        String sql = "SELECT COUNT(*) FROM archive_compilation_rw where 1=1 "+query ;
        return jdbcTemplate.queryForInt(sql);
    }
    public List<Map<String, Object>> listCompilation(Dto qDto) {
        // TODO Auto-generated method stub
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        String query=" ";
        String byrwbh="";
        String byrwmc="";
        String aos_module_id_=qDto.getString("aos_module_id_");
        String nd=qDto.getString("nd");
        if(nd!=null&&nd.length()!=0){
            query="and nd='"+nd+"'";
        }
        // 排列方式，页码，每页条目数
        Integer limit = Integer.valueOf(qDto.getString("limit"));
        Integer page = Integer.valueOf(qDto.getString("page"));
        // 条目开始索引
        Integer pageStart = (page - 1) * (limit);
        String sql="select * from archive_compilation_rw  where 1=1 "+query+" order by byrwbh desc offset "+pageStart+" rows fetch next "+limit+" rows only";
        List<Map<String,Object>> listDto = jdbcTemplate.queryForList(sql);

        return listDto;
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
        return listDto;
    }
    private List<Map<String, Object>> getModule_byid_(String cascade_id_) {
        String sql="select * from aos_sys_module where id_='"+cascade_id_+"'";
        return jdbcTemplate.queryForList(sql);
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
    public Dto getpath(Dto inDto,String id_, String zt_id) {
        Dto out= Dtos.newDto();
        //根据任务id查询得到tablename名称
        String tablename=findtablename(zt_id);
        Archive_topicPO archive_topicPO = archive_topicMapper.selectByKey(zt_id);
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
     * 根据专题名称查询数据库名称
     * @param zt_id
     * @return
     */
    private String findtablename(String zt_id) {
        String tablename="";
        Compilation_topicPO compilation_topicPO=compilation_topicMapper.selectByKey(zt_id);
        if(compilation_topicPO!=null){
            tablename=compilation_topicPO.getTopic_tablename();
        }
        return tablename;
    }

    private String getRwMessage(String topic_id_) {
        String tablename="";
        Archive_compilation_rwPO archive_compilation_rwPO=archive_compilation_rwMapper.selectByKey(topic_id_);
        if(archive_compilation_rwPO!=null){
            tablename=archive_compilation_rwPO.getSjkmc();
        }
        return tablename;
    }
    public String gethegaomessage(Dto qDto) {
        String id_=qDto.getString("topic_id_");
        String user=qDto.getString("user");
        String hg_path="";
        String sql="select hg_path from archive_compilation_hg where rw_id_='"+id_+"'";
        List<Map<String,Object>> list=jdbcTemplate.queryForList(sql);
        if(list!=null&&list.size()>0){
            hg_path=(String)list.get(0).get("hg_path");
        }
        String hg_message=gethgmessage(hg_path);
        return hg_message;
    }
    public String gethgmessage(String hg_path) {
        StringBuilder result = new StringBuilder();
        try {
//          BufferedReader bfr = new BufferedReader(new FileReader(new File(filePath)));
            BufferedReader bfr = new BufferedReader(new InputStreamReader(new FileInputStream(new File(hg_path)), "GBK"));
            String lineTxt = null;
            while ((lineTxt = bfr.readLine()) != null) {
                result.append(lineTxt).append("\n");
            }
            bfr.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.toString();
    }
    /**
     * 撰稿和合稿组合查询结果（此时是合稿的展开方式）
     * @param inDto
     * @param session
     * @return
     */
    public List<Map<String, Object>> listZg_hegao(Dto inDto, HttpSession session) {
        //此时做一下判断如果合稿操作过就执行三表联合查询语句
        String sql="select * from archive_compilation_hg where rw_id_='"+inDto.getString("topic_id_")+"'" ;
        String sql2="";
        List<Map<String,Object>> list=jdbcTemplate.queryForList(sql);
        if(list!=null&&list.size()>0){
            sql2="select zg.id_ as id_,zg.rw_id_ as rw_id_,zg.username as username,zg.tablename as tablename,zg.zg_path as zg_path,zg.zg_description as zg_description,zg.operate_time as operate_time," +
                    " hg.hg_description as hg_description,hg.jd_description as jd_description,hg.zbj_description as zbj_description from archive_compilation_zg as zg,archive_compilation_rw as rw,archive_compilation_hg as hg where rw.id_=zg.rw_id_ and rw.id_=hg.rw_id_ and  rw.id_='"+inDto.getString("topic_id_")+"'";

        }else{
            sql2="select zg.id_ as id_,zg.rw_id_ as rw_id_,zg.username as username,zg.tablename as tablename,zg.zg_path as zg_path,zg.zg_description as zg_description,zg.operate_time as operate_time" +
                    "  from archive_compilation_zg as zg,archive_compilation_rw as rw where rw.id_=zg.rw_id_ and   rw.id_='"+inDto.getString("topic_id_")+"'";
        }

        return jdbcTemplate.queryForList(sql2);
    }
    public String getzhuangaomessage(Dto inDto) {
        String zg_path=inDto.getString("zg_path");
        StringBuilder result = new StringBuilder();
        try {
//          BufferedReader bfr = new BufferedReader(new FileReader(new File(filePath)));
            BufferedReader bfr = new BufferedReader(new InputStreamReader(new FileInputStream(new File(zg_path)), "GBK"));
            String lineTxt = null;
            while ((lineTxt = bfr.readLine()) != null) {
                result.append(lineTxt).append("\n");
            }
            bfr.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.toString();
    }

    public String getChengGuopath(String id_, String filename) {
            String path=linkAddress+"bycg"+"/"+id_+"/"+filename;
            return path;
    }
}

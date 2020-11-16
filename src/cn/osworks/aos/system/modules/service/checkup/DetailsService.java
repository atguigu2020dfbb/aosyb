package cn.osworks.aos.system.modules.service.checkup;

import cn.osworks.aos.core.asset.AOSJson;
import cn.osworks.aos.core.dao.SqlDao;
import cn.osworks.aos.core.id.AOSId;
import cn.osworks.aos.core.typewrap.Dto;
import cn.osworks.aos.core.typewrap.Dtos;
import cn.osworks.aos.system.dao.mapper.*;
import cn.osworks.aos.system.dao.po.Archive_topic_userPO;
import cn.osworks.aos.system.modules.service.archive.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class DetailsService extends JdbcDaoSupport {
    @Autowired
    public JdbcTemplate jdbcTemplate;
    @Autowired
    public DataService dataService;
    @Autowired
    private SqlDao sysDao;
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
    public List<Map<String,Object>> listnexttrial(Dto qDto, HttpSession session) {
        Dto dto=Dtos.newDto();
        String sql="select * from archive_checkup where  next_compilationperson='"+session.getAttribute("user")+"' and flag='2'";
        List<Map<String,Object>> list=jdbcTemplate.queryForList(sql);
        return list;
    }
    public List<Map<String,Object>> listlasttrial(Dto qDto,HttpSession session) {
        Dto dto=Dtos.newDto();
        String sql="select * from archive_checkup where  last_compilationperson='"+session.getAttribute("user")+"' and flag='3'";
        List<Map<String,Object>> list=jdbcTemplate.queryForList(sql);
        return list;
    }
    public List<Map<String,Object>> findTablename(String id_) {
        String sql="select tablename from archive_check_tablenameid where pid='"+id_+"'";
        return     jdbcTemplate.queryForList(sql);

    }
    public String getLastTrial(Dto qDto) {
        String sql="select operate_description from archive_check_user where pid='"+qDto.getString("aos_rows_")+"'  and type='3' order by index_ desc";
        List<Map<String,Object>> list=jdbcTemplate.queryForList(sql);
        if(list!=null&&list.size()>0){
            return (String)list.get(0).get("operate_description");
        }else{
            return "";
        }
    }
    /*
     * 查询专题列表
     */
    public List<Map<String,Object>> findzt_jd() {
        // TODO Auto-generated method stub
        List<Map<String,Object>> alist = new ArrayList<Map<String,Object>>();
        String sql = "SELECT  *  FROM      checkup_topic  ";
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
}

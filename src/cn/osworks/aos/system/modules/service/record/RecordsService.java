package cn.osworks.aos.system.modules.service.record;

import cn.osworks.aos.core.asset.AOSJson;
import cn.osworks.aos.core.asset.WebCxt;
import cn.osworks.aos.core.typewrap.Dto;
import cn.osworks.aos.core.typewrap.Dtos;
import cn.osworks.aos.system.modules.service.archive.DocService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Properties;
@Service
public class RecordsService   extends JdbcDaoSupport {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Resource
    public void setJb(JdbcTemplate jb) {
        super.setJdbcTemplate(jb);
    }
    public Dto restoreData2(Dto dto, HttpServletResponse response, HttpServletRequest request) {
        // TODO Auto-generated method stub
        Dto out= Dtos.newDto();
        String baddr = request.getRemoteAddr();
        String faddr = request.getLocalAddr();
        //判断是不是服务器机器还原数据库
        if(!baddr.equals(faddr)){
            out.setAppCode(-2);
            WebCxt.write(response, AOSJson.toJson(out));
            return out;
        }
        Properties prop=null;
        try {
            prop = PropertiesLoaderUtils.loadAllProperties("config.properties");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            String strdir = DocService.class.getResource("/").getFile().replace("%20", " ");
            String excelPath = strdir.substring(1,strdir.lastIndexOf("WEB-INF"))+"/temp/record";

            String restorefile = excelPath+ File.separator+dto.getString("localFilename");
            if(restorefile==null||restorefile==""){
                return out;
            }
            Properties prop2=null;
            try {
                prop2 = PropertiesLoaderUtils.loadAllProperties("jdbc.properties");
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            String database=prop2.getProperty("jdbc.sjk");
            String sql2 = "ALTER DATABASE " + database + "  SET OFFLINE  WITH ROLLBACK IMMEDIATE";
            String sql = "use master restore database " + database + " from disk='" + restorefile+ "' with replace";
            String sql3 = "ALTER DATABASE " + database + "  SET ONLINE WITH ROLLBACK IMMEDIATE";
            jdbcTemplate.execute(sql2);
            jdbcTemplate.execute(sql);
            jdbcTemplate.execute(sql3);
            out.setAppCode(1);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            out.setAppCode(-1);
        }
        return out;
    }
}

package cn.osworks.aos.system.modules.controller.Task;

import cn.osworks.aos.system.modules.service.archive.DataService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

public class StockTask extends QuartzJobBean {
    private String sTest;
    public static String filePath = "";
    public static String linkAddress = "";
    public static String firstAddress = "";
    // 通过静态代码块读取配置文件
    static {
        try {
            Properties props = new Properties();
            InputStream in = DataService.class
                    .getResourceAsStream("/config.properties");
            props.load(in);
            filePath = props.getProperty("filePath");
            linkAddress = props.getProperty("linkAddress");
            firstAddress = props.getProperty("firstAddress");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) {
        // TODO Auto-generated method stub
        try{
            String back_path ="E:\\backup";
            String dbname = "aosyb"; //数据库名
            String name =dbname+ new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()); //文件名
            File file = new File(back_path);
            String path = file.getPath() + File.separator + name + ".bak";// name文件名
            String str = "backup database "+dbname+" to disk="+path+" with init";
            String selectsql = "backup database " + dbname + " to disk='" + path+"' with format,name='123'";
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            Connection ct = DriverManager.getConnection("jdbc:sqlserver://localhost:1433; DatabaseName=aosyb","sa","sasys");
            Statement ps = ct.createStatement();
            ps.execute(selectsql);
            //同时对backup文件夹中的问津执行删除操作，大于30个以上的都删除
            File[] backupfiles=file.listFiles();
            if(backupfiles!=null&&backupfiles.length>0&&backupfiles.length>30){
                for(int i=0;i<backupfiles.length-30;i++){
                    backupfiles[i].delete();
                }
            }
        }catch(Exception e){
                e.printStackTrace();
        }
    }
     public String getsTest() {
        return sTest;
     }
     public void setsTest(String sTest) {
        this.sTest = sTest;
     }

}

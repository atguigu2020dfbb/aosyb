package cn.osworks.aos.system.modules.controller.Task;

import cn.osworks.aos.core.asset.AOSUtils;
import cn.osworks.aos.core.captcha.utils.jdbc.JDBCUtils;
import cn.osworks.aos.core.id.AOSId;
import cn.osworks.aos.system.dao.mapper.LogMapper;
import cn.osworks.aos.system.modules.service.archive.DataService;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

/**
 * 温湿度定时器
 */
public class WsdTask extends QuartzJobBean {
    private String sTest;
    @Override
    public void executeInternal(JobExecutionContext jobExecutionContext) {
        // TODO Auto-generated method stub
        //获取当前时间，并拆分
//        String date = DateFormat.getDateTimeInstance().format(new Date());
       /* String date=AOSUtils.getDateTimeStr();
        String year = date.substring(0,date.indexOf("-"));
        String month = date.substring(date.indexOf("-")+1,date.lastIndexOf("-"));
        if (month.length() ==1 ){
            month = "0" + month;
        }
        String day = date.substring(date.lastIndexOf("-")+1,date.lastIndexOf(" "));
        if (day.length() ==1 ){
            day = "0" + day;
        }
        String hour = date.substring(date.lastIndexOf(" ")+1,date.indexOf(":"));
        if (Integer.parseInt(hour)<=10 ){
            hour = "0" + (Integer.parseInt(hour)-1);
        }else{
            hour= String.valueOf((Integer.parseInt(hour)-1));
        }
        String last = date.substring(date.indexOf(":"),date.length());

        //String date_WSD = year +"/" + month+"/"+day + " " + hour+last;
        String date_WSDL =year +"/" + month+"/"+day + " "+hour+last;

        String WSD_TableName = "W" + year + month;
        System.out.println("时间:"+date_WSDL);
        Connection conn = JDBCUtils.getMainConnection();
        Connection connWSD = JDBCUtils.getWSDConnection();
        //预编译
        PreparedStatement ps = null;
        String sql0;
        PreparedStatement psWSD = null;
        ResultSet resWSD = null;
        String sql2;
        try {
            sql0 =  "insert into WSD  " +
                    "(id_,depotid,depotname,fzbm, djr,timexp,wd,sd) " +
                    "values (?,?,?,?,?,?,?) ";
            sql2 =  "SELECT timeXP , DepotID , CgqName , wd , sd  " +
                    " FROM  "+ WSD_TableName +
                    " WHERE timeXP = ? ";
            psWSD = connWSD.prepareStatement(sql2);
//            psWSD.setString(1,WSD_TableName);
            psWSD.setString(1,date_WSDL);
            resWSD = psWSD.executeQuery();
            // 展开结果集数据库
            while (resWSD.next()){
                // 通过字段检索
                String id_ = AOSId.uuid();
                String DepotName="";
                String timeXP = resWSD.getString("timeXP");
                String DepotID = resWSD.getString("DepotID");
                String CgqName = resWSD.getString("CgqName");
                String wd = resWSD.getString("wd");
                String sd = resWSD.getString("sd");
                if("1"==DepotID){
                    DepotName="301";
                }else if("2"==DepotID){
                    DepotName="302";
                }else if("3"==DepotID){
                    DepotName="401";
                }else if("4"==DepotID){
                    DepotName="402";
                }else if("5"==DepotID){
                    DepotName="501";
                }else if("6"==DepotID){
                    DepotName="708";
                }else if("7"==DepotID){
                    DepotName="502";
                }

                //ps = conn.prepareStatement(sql0);
                //参数绑定
                ps.setString(1,id_);
                ps.setString(2,DepotID);
                ps.setString(3,DepotName);
                ps.setString(4,"");
                ps.setString(5,"");
                ps.setString(6,timeXP);
                ps.setString(7,wd);
                ps.setString(8,sd);

                int temp  = ps.executeUpdate();
                if (temp > 0 ){
                    System.out.println("添加成功");
                }else {
                    System.out.println("添加失败");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            //关闭连接
            JDBCUtils.close(conn);
            JDBCUtils.close(ps);
           JDBCUtils.close(resWSD);
            JDBCUtils.close(connWSD);
            JDBCUtils.close(psWSD);
        }*/

    }
     public String getsTest() {
        return sTest;
     }
     public void setsTest(String sTest) {
        this.sTest = sTest;
     }


}

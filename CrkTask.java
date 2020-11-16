package cn.osworks.aos.system.modules.controller.Task;

import cn.osworks.aos.core.asset.AOSUtils;
import cn.osworks.aos.core.captcha.utils.jdbc.JDBCUtils;
import cn.osworks.aos.core.id.AOSId;
import cn.osworks.aos.system.modules.service.archive.DataService;
import org.quartz.JobExecutionContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

/**
 * 出入库定时器
 */
public class CrkTask extends QuartzJobBean {
    private String sTest;
    /**
     * 获取当前整点时间
     * @throws ParseException
     */
   public String  getTime() throws ParseException {
        Date day=new Date();
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String s = format.format(day);
        Date date = format.parse(s);
        return s;
    }
    // 通过静态代码块读取配置文件
    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) {
        // TODO Auto-generated method stub
        //每天的9点到5点没一小时获取一次出入库登记信息
        //获取当前时间，并拆分
        /*String date = AOSUtils.getDateTimeStr();
        System.out.println(date);
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
        String last = date.substring(date.indexOf(":"),date.length());
        String date_WSD = year +"/" + month+"/"+day + " " + hour+last;
        String date_WSDL =year +"/" + month+"/"+day + " " + (Integer.parseInt(hour)-1)+last;
        String date_MJ = year +"-" + month+"-"+day + " " + hour+last;
        String date_MJL =year +"-" + month+"-"+day + " " + (Integer.parseInt(hour)-1)+last;
        Connection connMJ = JDBCUtils.getMJConnection();
        Connection conn = JDBCUtils.getMainConnection();
        //预编译
        PreparedStatement ps = null;
        String sql0;
        String sql;
        PreparedStatement psWSD = null;
        ResultSet resWSD = null;
        String sql2;
        PreparedStatement psMJ = null;
        ResultSet resMJ = null;
        String sql3;
        try {
            sql =   "insert into depot_ry" +
                    "(id_,f_recid ,f_eventtype, f_eventdesc,f_userid ,f_username,f_logdatetime,card,xm,bm,dd,zt,lx) " +
                    "values (?,?,?,?,?,?,?,?,?,?,?,?,?)";
            sql3 =  " SELECT f_RecID ,f_EventType, f_EventDesc,f_UserID,f_UserName ,f_LogDateTime " +
                    " FROM t_s_wglog  " +
                    " WHERE  f_LogDateTime > ?  " +
                    " AND f_LogDateTime < ? ";
            psMJ = connMJ.prepareStatement(sql3);
            psMJ.setString(1,date_MJL);
            psMJ.setString(2,date_MJ);
            resMJ = psMJ.executeQuery();
            while (resMJ.next()){
                int f_RecID = resMJ.getInt("f_RecID");
                String f_EventType = resMJ.getString("f_EventType");
                String f_EventDesc = resMJ.getString("f_EventDesc");
                f_EventDesc=f_EventDesc.replace(" ", "").replace("\n","").replace("\t","").replace("\r","");
                //f_EventDesc进行字段的截取操作
                System.out.println(f_EventDesc);
                String card=f_EventDesc.substring(f_EventDesc.indexOf("卡号:")+3,f_EventDesc.indexOf("姓名:"));
                String xmkf=f_EventDesc.substring(f_EventDesc.indexOf("姓名:")+3,f_EventDesc.indexOf("部门"));
                String xm=xmkf.split("-")[0];
                String kfbh=xmkf.split("-")[1];
                String bm=f_EventDesc.substring(f_EventDesc.indexOf("部门:")+3,f_EventDesc.indexOf("时间:"));
                String dd=f_EventDesc.substring(f_EventDesc.indexOf("地点:")+3,f_EventDesc.indexOf("状态:"));
                String zt=f_EventDesc.substring(f_EventDesc.indexOf("状态:")+3,f_EventDesc.lastIndexOf(","));
                int f_UserID = resMJ.getInt("f_UserID");
                String f_UserName = resMJ.getString("f_UserName");
                String f_LogDateTime = resMJ.getString("f_LogDateTime");
                //ps = conn.prepareStatement(sql);
                ps.setString(1, AOSId.uuid());
                ps.setInt(2,f_RecID);
                ps.setString(3,f_EventType);
                ps.setString(4,f_EventDesc);
                ps.setInt(5,f_UserID);
                ps.setString(6,f_UserName);
                ps.setString(7,f_LogDateTime);
                ps.setString(8,card);
                ps.setString(9,xm);
                ps.setString(10,bm);
                ps.setString(11,dd);
                ps.setString(12,zt);
                ps.setString(13,kfbh);
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
            JDBCUtils.close(resMJ);
           JDBCUtils.close(connMJ);
           JDBCUtils.close(psMJ);
        }*/
    }
     public String getsTest() {
        return sTest;
     }
     public void setsTest(String sTest) {
        this.sTest = sTest;
     }

}

package cn.osworks.aos.system.modules.controller.Task;

import cn.osworks.aos.core.asset.AOSUtils;
import cn.osworks.aos.core.captcha.utils.jdbc.JDBCUtils;
import cn.osworks.aos.core.id.AOSId;
import cn.osworks.aos.system.modules.service.archive.DataService;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

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

    public static void main(String[] args) {
        String str="1.abc.23,五楼西库房 -进门,4861401-胡桂英-501-州档案局\\党政档案管理处-2020-08-31 09:45:18 星期一-五楼西库房 -进门-刷卡开门,卡号: \t4861401\n" +
                "姓名: \t胡桂英-501\n" +
                "部门: \t州档案局\\党政档案管理处\n" +
                "时间: \t2020-08-31 09:45:18 星期一\n" +
                "地点: \t五楼西库房 -进门\n" +
                "状态: \t刷卡开门\n" +
                ",0757f3460000011dD92D4A00000000001F29A94D10000000";
        String no = strP(str,"卡号:","姓名:");
        System.out.println("卡号："+no.trim());
        String name = strP(str,"姓名:","部门:");
        System.out.println("姓名："+name.trim());
        String bm = strP(str,"部门:","时间:");
        System.out.println("部门："+bm.trim());
        String time = strP(str,"时间:","地点:");
        System.out.println("时间："+time.trim());
        String pa = strP(str,"地点:","状态:");
        System.out.println("地点："+pa.trim());
    }

    public static String strP(String str,String a,String b){
       String ret = "0";
        int strB = str.indexOf(a);
        int strE = str.indexOf(b);
        if(strB > 0 && strE > 0){
            ret = str.substring(strB,strE).substring(a.length());
        }
        return ret;
    }
    // 通过静态代码块读取配置文件
    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) {


        // TODO Auto-generated method stub
        //每天的9点到5点没一小时获取一次出入库登记信息
        //获取当前时间，并拆分
        String date = AOSUtils.getDateTimeStr();
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
        //获取前一个小时
        Integer hour_=(Integer.parseInt(hour)-1);
        //此时补位得到两位的小时
        String oldhour="";
        if (hour_<10 ){
            oldhour = "0" + hour_;
        }
        String date_WSDL =year +"/" + month+"/"+day + " " + oldhour+last;
        String date_MJ = year +"-" + month+"-"+day + " " + hour+last;
        String date_MJL =year +"-" + month+"-"+day + " " + oldhour+last;
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
            sql = "insert into depot_ry" +
                    "(id_,f_recid ,f_eventtype, f_eventdesc,f_userid ,f_logdatetime,card,xm,bm,dd,zt,lx,glnd) " +
                    "values (?,?,?,?,?,?,?,?,?,?,?,?,?)";
            sql3 = " SELECT f_RecID ,f_EventType, f_EventDesc,f_UserID,f_UserName ,f_LogDateTime " +
                    " FROM t_s_wglog  " +
                    " WHERE  f_LogDateTime >= ? " +
                    " AND f_LogDateTime < ? ";
            psMJ = connMJ.prepareStatement(sql3);
            /*SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
            Calendar calendar=new GregorianCalendar();
            calendar.setTime(new Date());
            calendar.add(calendar.DATE,-1);
            String dateNowStr=sdf.format(calendar.getTime());*/

            //psMJ.setString(1, dateNowStr);
            psMJ.setString(1,date_MJL);
            psMJ.setString(2,date_MJ);
            resMJ = psMJ.executeQuery();
            while (resMJ.next()){
                int f_RecID = resMJ.getInt("f_RecID");
                String f_EventType = resMJ.getString("f_EventType");
                String f_EventDesc = resMJ.getString("f_EventDesc");
                f_EventDesc = f_EventDesc.replace(" ", "").replace("\n", "").replace("\t", "").replace("\r", "");
                String kfbh = "";
                if(f_EventDesc.contains("二楼数字化室")){
                    kfbh="201";
                }if(f_EventDesc.contains("三楼东库房")){
                    kfbh="302";
                }if(f_EventDesc.contains("三楼西库房")){
                    kfbh="301";
                }if(f_EventDesc.contains("四楼西库房")){
                    kfbh="401";
                }if(f_EventDesc.contains("四楼东库房")){
                    kfbh="402";
                }if(f_EventDesc.contains("五楼西库房")){
                    kfbh="501";
                }if(f_EventDesc.contains("五楼东库房")){
                    kfbh="502";
                }if(f_EventDesc.contains("五楼特藏室")){
                    kfbh="502";
                }if(f_EventDesc.contains("七楼机房")){
                    kfbh="708";
                }



                //f_EventDesc进行字段的截取操作
                if (f_EventDesc != null && f_EventDesc != "" && f_EventDesc.indexOf("卡号")!=-1) {
                    String card = strP(f_EventDesc, "卡号:", "姓名:");
                    String xm = strP(f_EventDesc, "姓名:", "部门:");
                    /*if(xmkf==null||!xmkf.contains("-")){
                        continue;
                    }*/
                    String bm = strP(f_EventDesc, "部门:", "时间:");
                    String time = strP(f_EventDesc, "时间:", "地点:");
                    String dd = strP(f_EventDesc, "地点:", "状态:").split("-")[0].trim();
                    String zt=strP(f_EventDesc, "地点:", "状态:").split("-")[1].trim();
                    int f_UserID = resMJ.getInt("f_UserID");
                    String f_UserName = resMJ.getString("f_UserName");
                    String nd="";
                    String f_LogDateTime = resMJ.getString("f_LogDateTime");
                    if(f_LogDateTime!=null&&f_LogDateTime.length()>4){
                        nd=f_LogDateTime.substring(0,4);
                    }
                    ps = conn.prepareStatement(sql);
                    ps.setString(1, AOSId.uuid());
                    ps.setInt(2, f_RecID);
                    ps.setString(3, f_EventType);
                    ps.setString(4, f_EventDesc);
                    ps.setInt(5, f_UserID);
                    ps.setString(6, f_LogDateTime);
                    ps.setString(7, card);
                    ps.setString(8, xm);
                    ps.setString(9, bm);
                    ps.setString(10, dd);
                    ps.setString(11, zt);
                    ps.setString(12, kfbh);
                    ps.setString(13, nd);
                    int temp = ps.executeUpdate();
                    if (temp > 0) {
                        System.out.println("添加成功");
                    } else {
                        System.out.println("添加失败");
                    }
                }
                }
            } catch(SQLException e){
                e.printStackTrace();

            }finally{
                try{
                    //关闭连接
                    JDBCUtils.close(conn);
                    JDBCUtils.close(ps);
                    JDBCUtils.close(resMJ);
                    JDBCUtils.close(connMJ);
                    JDBCUtils.close(psMJ);
                }catch(Exception e){
                e.printStackTrace();
                }
            }

    }
     public String getsTest() {
        return sTest;
     }
     public void setsTest(String sTest) {
        this.sTest = sTest;
     }
     @Test
     public void test(){

   String str="20100141";
         System.out.println(str.substring(0,4));
   }

}

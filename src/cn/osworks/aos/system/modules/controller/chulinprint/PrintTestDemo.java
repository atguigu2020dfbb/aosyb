package cn.osworks.aos.system.modules.controller.chulinprint;

import org.junit.internal.ArrayComparisonFailure;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Date;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * 此类供打印测试的JSP文件调用
 * 实际使用时，可参照此类进行调用
 * @author Administrator
 *
 */
public class PrintTestDemo {
    /**
     * 标识控件是否已经安装
     */
    public  boolean bIsInstallPrintControl = false;
    /**
     * 控件的Cookie值
     */
    public  String strPrintControlCookie = "";


    /**
     * 设置控件调用的Cookie值，判断是否安装了打印控件
     * @param request
     * @param response
     * @param pSqlHelper
     * @param pJson
     * @param strPrintUrl
     * @return
     */
    public boolean SetCookieURL(HttpServletRequest request, HttpServletResponse response, SqlHelper pSqlHelper,
                                PrintJson pJson, String strPrintUrl) {
        bIsInstallPrintControl = false;
        strPrintControlCookie = "";

        //判断是否存在检测安装的Cookie
        boolean bIsExist = false;
        Cookie[] cookies = request.getCookies();
        for( int i = 0; i < cookies.length; i++ ) {
            Cookie printControlCookie = cookies[i];
            if( printControlCookie.getName().equalsIgnoreCase("InstallPrintControl")) {
                strPrintControlCookie = printControlCookie.getValue();

                //以Cookie值查找在数据表中是否存在
                String strSql = "Select * From CheckInstall Where Cookie = '" + strPrintControlCookie + "' ";
                bIsInstallPrintControl = pSqlHelper.CheckIsExist(strSql);

                //更新Cookie的保存时间
                printControlCookie.setMaxAge(60*60*24*365*10);
                response.addCookie(printControlCookie);
                bIsExist = true;
                break;
            }
        }

        if(!bIsExist) { //Cookie不存在，则新建Cookie
            strPrintControlCookie = java.util.UUID.randomUUID().toString();
            Cookie printControlCookie = new Cookie("InstallPrintControl", strPrintControlCookie);
            printControlCookie.setMaxAge(60*60*24*365*10);
            response.addCookie(printControlCookie);
        }

        String strUrl = strPrintUrl + "/PrintCookie.jsp";
        pJson.SetCookieAndURL(strPrintControlCookie, strUrl);

        return bIsInstallPrintControl;
    }



    public String DepositPreview(HttpServletRequest request, HttpServletResponse response) {
        String strCurPath = request.getSession().getServletContext().getRealPath("/");
        String path = request.getContextPath();
        String strPrintUrl = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path;

        String strPrintFileName = strCurPath + "\\Frp\\DepositAmt.fr3";
        String strPrintTempPath = strCurPath + "\\PrintTemp\\";
        PrintJson pJson = new PrintJson(strPrintTempPath, strPrintFileName);
        SqlHelper pSqlHelper = new SqlHelper();
        //设置控件调用的Cookie值，判断是否安装了打印控件
        SetCookieURL(request, response, pSqlHelper, pJson, strPrintUrl);

        pJson.CheckRegister("注册姓名", "fdksafdsafldsajfdksafj");  //注册信息
        pJson.AddPrintParam("ShopName", "测试酒楼");
        pJson.AddPrintParam("PrintDepositAdd", "说明：本单据为贵客押金收取凭证，盖章有效。退房时请出示，遗失者自负，请妥善保存。退房时间为12:00时，延时退房18:00时以前按半天房费收取，18:00时以后算全天房价。押金单有效期为一个月，过期作废。       贵重物品请交前台寄存，未寄存丢失自负。      谢谢！");

        String strSql = "Select * From CashLog";
        ResultSet dtCashLog = pSqlHelper.GetTable(strSql);
        String strPrintTempFile = pJson.ShowReport(dtCashLog) ; //产生JSON文件内容
        try {
            dtCashLog.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        pSqlHelper.Close();

        //把服务器的URL + 此文件名 传递给控件，由控件下载还原数据进行打印
        String strServerURL = strPrintUrl + "/PrintTemp/";
        String strData = strServerURL + strPrintTempFile;
        String strPrintData = PrintFunction.encodeBase64(strData);

        return strPrintData;
    }


    public String DepositDesign(HttpServletRequest request, HttpServletResponse response) throws SQLException {
        String strCurPath = request.getSession().getServletContext().getRealPath("/");
        String path = request.getContextPath();
        String strPrintUrl = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path;
        String strPrintFileName = strCurPath + "\\Frp\\DepositAmt.fr3";
        String strPrintTempPath = strCurPath + "\\PrintTemp\\";

        PrintJson pJson = new PrintJson(strPrintTempPath, strPrintFileName);
        SqlHelper pSqlHelper = new SqlHelper();
        //设置控件调用的Cookie值，判断是否安装了打印控件
        SetCookieURL(request, response, pSqlHelper, pJson, strPrintUrl);

        pJson.CheckRegister("注册姓名", "fdksafdsafldsajfdksafj");  //注册信息
        pJson.AddPrintParam("ShopName", "测试酒楼");
        pJson.AddPrintParam("PrintDepositAdd", "说明：本单据为贵客押金收取凭证，盖章有效。退房时请出示，遗失者自负，请妥善保存。退房时间为12:00时，延时退房18:00时以前按半天房费收取，18:00时以后算全天房价。押金单有效期为一个月，过期作废。       贵重物品请交前台寄存，未寄存丢失自负。      谢谢！");

        //String strSql = "Select * From CashLog";
        String strSql="select * from jnws where tm like '%州委统战部%'";
        ResultSet dtCashLog = pSqlHelper.GetTable(strSql);
        String strPrintTempFile = pJson.DesignReport(dtCashLog) ; //产生JSON文件内容
        try {
            dtCashLog.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        pSqlHelper.Close();

        //把服务器的URL + 此文件名 传递给控件，由控件下载还原数据进行打印
        String strServerURL = strPrintUrl + "/PrintTemp/";
        String strData = strServerURL + strPrintTempFile;
        String strPrintData = PrintFunction.encodeBase64(strData);

        return strPrintData;
    }


    public String DepositPrint(HttpServletRequest request, HttpServletResponse response) {
        String strCurPath = request.getSession().getServletContext().getRealPath("/");
        String path = request.getContextPath();
        String strPrintUrl = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path;
        String strPrintFileName = strCurPath + "\\Frp\\DepositAmt.fr3";
        String strPrintTempPath = strCurPath + "\\PrintTemp\\";
        PrintJson pJson = new PrintJson(strPrintTempPath, strPrintFileName);
        SqlHelper pSqlHelper = new SqlHelper();
        //设置控件调用的Cookie值，判断是否安装了打印控件
        SetCookieURL(request, response, pSqlHelper, pJson, strPrintUrl);

        pJson.CheckRegister("注册姓名", "fdksafdsafldsajfdksafj");  //注册信息
        pJson.AddPrintParam("ShopName", "测试酒楼");
        pJson.AddPrintParam("PrintDepositAdd", "说明：本单据为贵客押金收取凭证，盖章有效。退房时请出示，遗失者自负，请妥善保存。退房时间为12:00时，延时退房18:00时以前按半天房费收取，18:00时以后算全天房价。押金单有效期为一个月，过期作废。       贵重物品请交前台寄存，未寄存丢失自负。      谢谢！");

        String strSql = "Select * From CashLog";
        ResultSet dtCashLog = pSqlHelper.GetTable(strSql);
        String strPrintTempFile = pJson.PrintReport(dtCashLog) ; //产生JSON文件内容
        try {
            dtCashLog.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        pSqlHelper.Close();

        //把服务器的URL + 此文件名 传递给控件，由控件下载还原数据进行打印
        String strServerURL = strPrintUrl + "/PrintTemp/";
        String strData = strServerURL + strPrintTempFile;
        String strPrintData = PrintFunction.encodeBase64(strData);

        return strPrintData;
    }

    public String DepositDesignPost(HttpServletRequest request, HttpServletResponse response) {
        String strCurPath = request.getSession().getServletContext().getRealPath("/");
        String path = request.getContextPath();
        String strPrintUrl = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path;

        String strPrintFileName = strCurPath + "\\Frp\\DepositAmt.fr3";
        String strPrintTempPath = strCurPath + "\\PrintTemp\\";
        PrintJson pJson = new PrintJson(strPrintTempPath, strPrintFileName);
        SqlHelper pSqlHelper = new SqlHelper();
        //设置控件调用的Cookie值，判断是否安装了打印控件
        SetCookieURL(request, response, pSqlHelper, pJson, strPrintUrl);

        pJson.CheckRegister("注册姓名", "fdksafdsafldsajfdksafj");  //注册信息
        pJson.AddPrintParam("ShopName", "测试酒楼");
        pJson.AddPrintParam("PrintDepositAdd", "说明：本单据为贵客押金收取凭证，盖章有效。退房时请出示，遗失者自负，请妥善保存。退房时间为12:00时，延时退房18:00时以前按半天房费收取，18:00时以后算全天房价。押金单有效期为一个月，过期作废。       贵重物品请交前台寄存，未寄存丢失自负。      谢谢！");
        pJson.SetPostUrl("http://localhost:8080/PrintTest/PostSave.jsp?FileName=DepositAmt.fr3");

        String strSql = "Select * From CashLog";
        ResultSet dtCashLog = pSqlHelper.GetTable(strSql);
        String strPrintTempFile = pJson.DesignReport(dtCashLog) ; //产生JSON文件内容
        try {
            dtCashLog.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        pSqlHelper.Close();

        //把服务器的URL + 此文件名 传递给控件，由控件下载还原数据进行打印
        String strServerURL = strPrintUrl + "/PrintTemp/";
        String strData = strServerURL + strPrintTempFile;
        String strPrintData = PrintFunction.encodeBase64(strData);

        return strPrintData;
    }


    public String MasterPreview(HttpServletRequest request, HttpServletResponse response) {
        String strCurPath = request.getSession().getServletContext().getRealPath("/");
        String path = request.getContextPath();
        String strPrintUrl = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path;

        String strPrintFileName = strCurPath + "\\Frp\\InInfo.fr3";
        String strPrintTempPath = strCurPath + "\\PrintTemp\\";
        PrintJson pJson = new PrintJson(strPrintTempPath, strPrintFileName);
        SqlHelper pSqlHelper = new SqlHelper();
        //设置控件调用的Cookie值，判断是否安装了打印控件
        SetCookieURL(request, response, pSqlHelper, pJson, strPrintUrl);

        pJson.CheckRegister("注册姓名", "fdksafdsafldsajfdksafj");  //注册信息
        pJson.MasterOptions(1, "InNo", false);  //主从关系

        String strSql = "Select * From InInfo";
        ResultSet dtInInfo = pSqlHelper.GetTable(strSql);
        strSql = "Select * From InMaterial";
        ResultSet dtInMaterial = pSqlHelper.GetTable(strSql);

        String strPrintTempFile = pJson.ShowReport(dtInInfo, dtInMaterial) ; //产生JSON文件内容
        try {
            dtInInfo.close();
            dtInMaterial.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        pSqlHelper.Close();

        //把服务器的URL + 此文件名 传递给控件，由控件下载还原数据进行打印
        String strServerURL = strPrintUrl + "/PrintTemp/";
        String strData = strServerURL + strPrintTempFile;
        String strPrintData = PrintFunction.encodeBase64(strData);

        return strPrintData;
    }



    public String MasterDesign(HttpServletRequest request, HttpServletResponse response) {
        String strCurPath = request.getSession().getServletContext().getRealPath("/");
        String path = request.getContextPath();
        String strPrintUrl = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path;

        String strPrintFileName = strCurPath + "\\Frp\\InInfo.fr3";
        String strPrintTempPath = strCurPath + "\\PrintTemp\\";
        PrintJson pJson = new PrintJson(strPrintTempPath, strPrintFileName);
        SqlHelper pSqlHelper = new SqlHelper();
        //设置控件调用的Cookie值，判断是否安装了打印控件
        SetCookieURL(request, response, pSqlHelper, pJson, strPrintUrl);

        pJson.CheckRegister("注册姓名", "fdksafdsafldsajfdksafj");  //注册信息
        pJson.MasterOptions(1, "InNo", false);  //主从关系

        String strSql = "Select * From InInfo";
        ResultSet dtInInfo = pSqlHelper.GetTable(strSql);
        strSql = "Select * From InMaterial";
        ResultSet dtInMaterial = pSqlHelper.GetTable(strSql);

        String strPrintTempFile = pJson.DesignReport(dtInInfo, dtInMaterial) ; //产生JSON文件内容
        try {
            dtInInfo.close();
            dtInMaterial.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        pSqlHelper.Close();

        //把服务器的URL + 此文件名 传递给控件，由控件下载还原数据进行打印
        String strServerURL = strPrintUrl + "/PrintTemp/";
        String strData = strServerURL + strPrintTempFile;
        String strPrintData = PrintFunction.encodeBase64(strData);

        return strPrintData;
    }

    public String PicturePreview(HttpServletRequest request, HttpServletResponse response) {
        String strCurPath = request.getSession().getServletContext().getRealPath("/");
        String path = request.getContextPath();
        String strPrintUrl = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path;

        String strPrintFileName = strCurPath + "\\Frp\\Material.fr3";
        String strPrintTempPath = strCurPath + "\\PrintTemp\\";
        PrintJson pJson = new PrintJson(strPrintTempPath, strPrintFileName);
        SqlHelper pSqlHelper = new SqlHelper();
        //设置控件调用的Cookie值，判断是否安装了打印控件
        SetCookieURL(request, response, pSqlHelper, pJson, strPrintUrl);

        pJson.CheckRegister("注册姓名", "fdksafdsafldsajfdksafj");  //注册信息
        pJson.AddPrintParam("ShopName", "测试酒楼");

        String strSql = "Select * From MaterialInfo";
        ResultSet dtMaterialInfo = pSqlHelper.GetTable(strSql);

        //增加单个图片
        pJson.AddPictureSinglePrint("PicTitle", "Title.bmp", strCurPath + "\\Image\\");
        //数据表中的图片
        ResultSet dtMaterialInfoPic = pSqlHelper.GetTable(strSql);
        pJson.AddPictureTableToPrint("PicMaterial", dtMaterialInfoPic, 1, "PictureFile", strCurPath + "\\Image\\" );

        String strPrintTempFile = pJson.ShowReport(dtMaterialInfo) ; //产生JSON文件内容
        try {
            dtMaterialInfo.close();
            dtMaterialInfoPic.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        pSqlHelper.Close();

        //把服务器的URL + 此文件名 传递给控件，由控件下载还原数据进行打印
        String strServerURL = strPrintUrl + "/PrintTemp/";
        String strData = strServerURL + strPrintTempFile;
        String strPrintData = PrintFunction.encodeBase64(strData);

        return strPrintData;
    }


    public String PictureDesign(HttpServletRequest request, HttpServletResponse response) {
        String strCurPath = request.getSession().getServletContext().getRealPath("/");
        String path = request.getContextPath();
        String strPrintUrl = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path;

        String strPrintFileName = strCurPath + "\\Frp\\Material.fr3";
        String strPrintTempPath = strCurPath + "\\PrintTemp\\";
        PrintJson pJson = new PrintJson(strPrintTempPath, strPrintFileName);
        SqlHelper pSqlHelper = new SqlHelper();
        //设置控件调用的Cookie值，判断是否安装了打印控件
        SetCookieURL(request, response, pSqlHelper, pJson, strPrintUrl);

        pJson.CheckRegister("注册姓名", "fdksafdsafldsajfdksafj");  //注册信息
        pJson.AddPrintParam("ShopName", "测试酒楼");

        String strSql = "Select * From MaterialInfo";
        ResultSet dtMaterialInfo = pSqlHelper.GetTable(strSql);

        //增加单个图片
        pJson.AddPictureSinglePrint("PicTitle", "Title.bmp", strCurPath + "\\Image\\");
        //数据表中的图片
        ResultSet dtMaterialInfoPic = pSqlHelper.GetTable(strSql);
        pJson.AddPictureTableToPrint("PicMaterial", dtMaterialInfoPic, 1, "PictureFile", strCurPath + "\\Image\\" );

        String strPrintTempFile = pJson.DesignReport(dtMaterialInfo) ; //产生JSON文件内容
        try {
            dtMaterialInfo.close();
            dtMaterialInfoPic.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        pSqlHelper.Close();

        //把服务器的URL + 此文件名 传递给控件，由控件下载还原数据进行打印
        String strServerURL = strPrintUrl + "/PrintTemp/";
        String strData = strServerURL + strPrintTempFile;
        String strPrintData = PrintFunction.encodeBase64(strData);

        return strPrintData;
    }

    public String QrcodePreview(HttpServletRequest request, HttpServletResponse response) {
        String strCurPath = request.getSession().getServletContext().getRealPath("/");
        String path = request.getContextPath();
        String strPrintUrl = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path;

        String strPrintFileName = strCurPath + "\\Frp\\QrCodeDemo.fr3";
        String strPrintTempPath = strCurPath + "\\PrintTemp\\";
        PrintJson pJson = new PrintJson(strPrintTempPath, strPrintFileName);
        SqlHelper pSqlHelper = new SqlHelper();
        //设置控件调用的Cookie值，判断是否安装了打印控件
        SetCookieURL(request, response, pSqlHelper, pJson, strPrintUrl);

        pJson.CheckRegister("注册姓名", "fdksafdsafldsajfdksafj");  //注册信息
        pJson.AddPrintParam("ShopName", "测试酒楼");
        pJson.AddPrintParam("PrintDepositAdd", "说明：本单据为贵客押金收取凭证，盖章有效。退房时请出示，遗失者自负，请妥善保存。退房时间为12:00时，延时退房18:00时以前按半天房费收取，18:00时以后算全天房价。押金单有效期为一个月，过期作废。       贵重物品请交前台寄存，未寄存丢失自负。      谢谢！");

        String strSql = "Select * From CashLog";
        ResultSet dtCashLog = pSqlHelper.GetTable(strSql);
        String strPrintTempFile = pJson.ShowReport(dtCashLog) ; //产生JSON文件内容
        try {
            dtCashLog.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        pSqlHelper.Close();

        //把服务器的URL + 此文件名 传递给控件，由控件下载还原数据进行打印
        String strServerURL = strPrintUrl + "/PrintTemp/";
        String strData = strServerURL + strPrintTempFile;
        String strPrintData = PrintFunction.encodeBase64(strData);

        return strPrintData;
    }


    public String QrcodeDesign(HttpServletRequest request, HttpServletResponse response) {
        String strCurPath = request.getSession().getServletContext().getRealPath("/");
        String path = request.getContextPath();
        String strPrintUrl = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path;

        String strPrintFileName = strCurPath + "\\Frp\\QrCodeDemo.fr3";
        String strPrintTempPath = strCurPath + "\\PrintTemp\\";
        PrintJson pJson = new PrintJson(strPrintTempPath, strPrintFileName);
        SqlHelper pSqlHelper = new SqlHelper();
        //设置控件调用的Cookie值，判断是否安装了打印控件
        SetCookieURL(request, response, pSqlHelper, pJson, strPrintUrl);

        pJson.CheckRegister("注册姓名", "fdksafdsafldsajfdksafj");  //注册信息
        pJson.AddPrintParam("ShopName", "测试酒楼");
        pJson.AddPrintParam("PrintDepositAdd", "说明：本单据为贵客押金收取凭证，盖章有效。退房时请出示，遗失者自负，请妥善保存。退房时间为12:00时，延时退房18:00时以前按半天房费收取，18:00时以后算全天房价。押金单有效期为一个月，过期作废。       贵重物品请交前台寄存，未寄存丢失自负。      谢谢！");

        String strSql = "Select * From CashLog";
        ResultSet dtCashLog = pSqlHelper.GetTable(strSql);
        String strPrintTempFile = pJson.DesignReport(dtCashLog) ; //产生JSON文件内容
        try {
            dtCashLog.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        pSqlHelper.Close();

        //把服务器的URL + 此文件名 传递给控件，由控件下载还原数据进行打印
        String strServerURL = strPrintUrl + "/PrintTemp/";
        String strData = strServerURL + strPrintTempFile;
        String strPrintData = PrintFunction.encodeBase64(strData);

        return strPrintData;
    }


    public String ClassDesign(HttpServletRequest request, HttpServletResponse response) {
        String strCurPath = request.getSession().getServletContext().getRealPath("/");
        String path = request.getContextPath();
        String strPrintUrl = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path;

        ArrayList<DataTest> listData = new ArrayList<DataTest>();
        DataTest pData1 = new DataTest(1, 1.1, "Test1", new Date(), true);
        DataTest pData2 = new DataTest(2, 2.1, "Test2", new Date(), false);
        listData.add(pData1);
        listData.add(pData2);


        String strPrintFileName = strCurPath + "\\Frp\\ListClassTest.fr3";
        String strPrintTempPath = strCurPath + "\\PrintTemp\\";
        PrintJson pJson = new PrintJson(strPrintTempPath, strPrintFileName);
        SqlHelper pSqlHelper = new SqlHelper();
        //设置控件调用的Cookie值，判断是否安装了打印控件
        SetCookieURL(request, response, pSqlHelper, pJson, strPrintUrl);
        pSqlHelper.Close();

        pJson.CheckRegister("注册姓名", "fdksafdsafldsajfdksafj");  //注册信息

        String strPrintTempFile = pJson.DesignReport(listData) ; //产生JSON文件内容

        //把服务器的URL + 此文件名 传递给控件，由控件下载还原数据进行打印
        String strServerURL = strPrintUrl + "/PrintTemp/";
        String strData = strServerURL + strPrintTempFile;
        String strPrintData = PrintFunction.encodeBase64(strData);

        return strPrintData;
    }


}

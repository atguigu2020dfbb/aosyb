<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="cn.osworks.aos.system.modules.controller.chulinprint.PrintTestDemo" %>
 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>打印控件调用示例</title>
<script src="http://127.0.0.1/pydnaos/static/js/jquery-1.10.1.min.js" type="text/javascript"></script>
<script language="javascript">
  function PreviewDeposit()
  {
    /*document.FrmWebPrint.action = "print/index.jsp?PrintType=DepositPreview";
    document.FrmWebPrint.submit();*/
      window.parent.fnaddtab('12344','票据打印','/print/DepositPreview.jhtml?PrintType=DepositPreview');
  }

  function DesignDeposit()
  {
    document.FrmWebPrint.action = "print/index.jsp?PrintType=DepositDesign";
    document.FrmWebPrint.submit();
  }

  function PrintDeposit()
  {
    document.FrmWebPrint.action = "index.jsp?PrintType=DepositPrint";
    document.FrmWebPrint.submit();
  }

  function DesignDepositPost()
  {
    document.FrmWebPrint.action = "print/index.jsp?PrintType=DepositDesignPost";
    document.FrmWebPrint.submit();
  }

  function PreviewMaster()
  {
    document.FrmWebPrint.action = "index.jsp?PrintType=MasterPreview";
    document.FrmWebPrint.submit();
  }

  function DesignMaster()
  {
    document.FrmWebPrint.action = "print/index.jsp?PrintType=MasterDesign";
    document.FrmWebPrint.submit();
  }
  
  function PreviewPicture()
  {
    document.FrmWebPrint.action = "print/index.jsp?PrintType=PicturePreview";
    document.FrmWebPrint.submit();
  }

  function DesignPicture()
  {
    document.FrmWebPrint.action = "print/index.jsp?PrintType=PictureDesign";
    document.FrmWebPrint.submit();
 }
  
  
  function PreviewQrcode()
  {
    document.FrmWebPrint.action = "print/index.jsp?PrintType=QrcodePreview";
    document.FrmWebPrint.submit();
  }

  function DesignQrcode()
  {
	    document.FrmWebPrint.action = "print/index.jsp?PrintType=QrcodeDesign";
	    document.FrmWebPrint.submit();
  }
 
  function DesignClass()
  {
	 document.FrmWebPrint.action = "print/index.jsp?PrintType=ClassDesign";
	 document.FrmWebPrint.submit();
  }
  
</script>
</head>


<body>
<jsp:useBean id="pf" scope= "page" class="cn.osworks.aos.system.modules.controller.chulinprint.PrintTestDemo" />
<%
    boolean bIsInstallPrintControl = true;
    String strPrintControlCookie = "";

    String strPrintType = request.getParameter("PrintType");
	String strCurPath = request.getSession().getServletContext().getRealPath("/"); 
	String path = request.getContextPath();  
	String strPrintUrl = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path;


	String strPrintData = "";
	if( strPrintType == null ) {
		;
	} else {
	   if( strPrintType.equals("DepositPreview") ) {
	       strPrintData =  pf.DepositPreview( request, response);
	   } else if( strPrintType.equals("DepositDesign")) {
		   strPrintData = pf.DepositDesign(request, response);
	   } else if( strPrintType.equals("DepositPrint")) {
		   strPrintData = pf.DepositPrint(request, response);
	   } else if( strPrintType.equals("DepositDesignPost")) {
		   strPrintData = pf.DepositDesignPost(request, response);
	   } else if( strPrintType.equals("MasterPreview")) {
		   strPrintData = pf.MasterPreview(request, response);
	   } else if( strPrintType.equals("MasterDesign")) {
		   strPrintData = pf.MasterDesign(request, response);
	   } else if( strPrintType.equals("PicturePreview")) {
		   strPrintData = pf.PicturePreview(request, response);
	   } else if( strPrintType.equals("PictureDesign")) {
		   strPrintData = pf.PictureDesign(request, response);
	   } else if( strPrintType.equals("QrcodePreview")) {
		   strPrintData = pf.QrcodePreview(request, response);
	   } else if( strPrintType.equals("QrcodeDesign")) {
		   strPrintData = pf.QrcodeDesign(request, response);
	   } else if( strPrintType.equals("ClassDesign")) {
		   strPrintData = pf.ClassDesign(request, response);
	   }
	   bIsInstallPrintControl = pf.bIsInstallPrintControl;
	   strPrintControlCookie = pf.strPrintControlCookie;
	}
    %>
    
    <iframe id="ifrm" src="" width="0" height="0"></iframe>
    <script type="text/javascript">
        var strPrintData= "<%=strPrintData%>";
        if( strPrintData != '' ) {
           $("#ifrm")[0].src = "ChuLinPrint:<%=strPrintData%>";
        }
        
        if( '<%=bIsInstallPrintControl%>' == 'false'  ) {
            if (confirm("检测到打印控件未安装，您是否下载安装？")) {
                document.getElementById("downPrintControl").click();
           }
       }
        if ('<%=bIsInstallPrintControl%>' == 'true') {
            var strPrintControlCookie = '<%=strPrintControlCookie%>';
            if( strPrintControlCookie != '') { //延时3秒后再次检测打印控件是否安装
               setTimeout('checkPrintControlInstall()', 3000);
               function checkPrintControlInstall() {
                   $.ajax({
                       type: "Get",
                       url: "PrintCookie.jsp?checkInstall=<%=strPrintControlCookie%>",
                       success: function (strResult) {
                           if (strResult.indexOf("PrintControlInstall") == -1) {
                               if (confirm("检测到打印控件未安装，您是否下载安装？")) {
                            	   document.getElementById("downPrintControl").click();
                               }
                           }
                       }
                   });
               }
            }
        }

    </script>
    
  

<form id="FrmWebPrint" name="FrmWebPrint" method="post" action="">
  <table width="473" height="245" border="0">
    <tr>
      <td width="121"><input name="DepositPreview" type="submit" id="DepositPreview" value="票据预览" onClick="PreviewDeposit();"  /></td>
      <td width="135"><input name="DepositDesign" type="submit" id="DepositDesign" value="票据报表编辑" onClick="DesignDeposit();"></td>
    </tr>
    <tr>
      <td><input name="DepositPrint" type="submit" id="DepositPrint" value="票据直接打印" onClick="PrintDeposit();" /></td>
      <td><input name="DepositDesignPost" type="submit" id="DepositDesignPost" value="票据报表编辑(Post方式保存)" onClick="DesignDepositPost();"></td>
     </tr>
    <tr>
      <td><input name="MasterPreview" type="submit" id="MasterPreview" value="主从报表预览" onClick="PreviewMaster();" /></td>
      <td><input name="MasterDesign" type="submit" id="MasterDesign" value="主从报表编辑" onClick="DesignMaster();" /></td>
    </tr>
    <tr>
      <td><input name="PicturePreview" type="submit" id="PicturePreview" value="含图片报表预览" onClick="PreviewPicture();" /></td>
      <td><input name="PictureDesign" type="submit" id="PictureDesign" value="含图片报表编辑" onClick="DesignPicture();" /></td>
     </tr>
    <tr>
      <td><input name="QrcodePreview" type="submit" id="QrcodePreview" value="二维码预览" onClick="PreviewQrcode();" /></td>
      <td><input name="QrcodeDesign" type="submit" id="QrcodeDesign" value="二维码报表编辑" onClick="DesignQrcode();" /></td>
     </tr>
     <tr>
       <td><input name="ClassDesign" type="submit" id="ClassDesign" value="List<T>编辑" onClick="DesignClass();" /></td>
       <td></td>
     </tr>
  </table>
  <p>&nbsp;</p>
        <div style="margin-top:20px;">
            <a id="downPrintControl" href="PrintActivex.exe" >点击下载打印组件</a></div>
        <br />
  
  
</form>
</body>
</html>
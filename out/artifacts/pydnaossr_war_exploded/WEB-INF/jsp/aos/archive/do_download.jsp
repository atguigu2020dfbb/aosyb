<%@ page contentType="text/html;charset=utf-8"      language="java"   %>
<%
 String filePath=(String)request.getAttribute("filePath"); 

%>
<%@ page language="java" import="java.util.*,java.io.*" pageEncoding="utf-8"%>

<title>万能数据查看系统</title>
<html>
<body  marginheight="100%" marginwidth="100%" topmargin="0">
<table width="100%" height="100%" border="0">
<tbody>
<tr>
<td>如不能查看电子文件<br>
1、请<a href="download.jsp?file=/dll/av193.exe" target='_blank'><font color = "red" >下载</font></a>万能阅读器并安装<br>
2、弹出的阻止窗口选择运行加载项。
</tr>
<tr>
<td valign="top" width="100%" height="100%">

<%--<DN:ViewPrintTag>
{000D0E00-0000-0000-C000-000000001157}
<OBJECT	 ID="Image"  name="wnread" classid="clsid:516A65F1-C07F-4193-B796-7D154DF197A2"
--%>
<OBJECT	 ID="Image"  name="wnread" classid="clsid:516A65F1-C07F-4193-B796-7D154DF197A2"
   width=100%
   height=100%
   align="center"
   hspace=0
   vspace=0>
   <param name="PrintFlag" value="1">
    <!--  <param name="FilePath" value="<%=filePath%>" >-->
    <param name="FilePath" value="http://127.0.0.1/Data/uploadFiles111/003.pdf" >

 </OBJECT>
</td>
 </tr>
 <tr>
 </tr>
 </tbody>
 </table>

	
 </body>


</html>
  



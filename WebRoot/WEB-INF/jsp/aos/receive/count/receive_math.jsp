<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2020/4/20
  Time: 16:41
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>

    <script type="text/javascript">

        window.onload = function(){

        }
        /*function toprint(){
             document.getElementById("newD").style.display="none";
           window.print();
        }*/
        function toprint()
        {
            var newstr = document.all.item('div1').innerHTML;
            var oldstr = document.body.innerHTML;
            document.body.innerHTML = newstr;
            window.print();
            document.body.innerHTML = oldstr;
            return false;
        }
        function funcDownload (content, filename) {
            var filename = 'hello.doc';
            var a = document.createElement('a');
            var blob = new Blob([content]);
            a.download = filename;
            a.href = URL.createObjectURL(blob);
            a.click();
            URL.revokeObjectURL(blob);
        }
        function dn (){
            var ss = document.getElementById('div1').innerHTML;
            funcDownload(ss, 'download.doc');
        }
    </script>
    <style>
        #font{font-size:10px}
        #pfont{font-size:20px}
    </style>
</head>

<body  contentEditable="true" id="printContainer">
<div id="newD">
    <button onclick="toprint()" >打印</button>
    <a onclick="dn()" href=""   id="xz" >下载</a>
</div>
<div class=Section1 style='layout-grid:15.6pt' id="div1">

    <p align="center" id="pfont">接收报告(<%=request.getAttribute("tabledesc")%>)<br /></p>
    <table border="1" cellspacing="0" cellpadding="0" width="630" align="center">
        <tr>
            <td width="240" height="50"><p align="center">交接工作名称</p></td>
            <td width="222" height="50"><p align="center"><%=request.getAttribute("qzh_name")%><%=request.getAttribute("nd")%>年<%=request.getAttribute("table")%>档案接收</p></td>
            <td width="180" height="50"><p align="center">移交方式</p></td>
            <td width="222" height="50"><p align="center">在线</p></td>
        </tr>

        <tr>
            <td width="100" height="50" colspan="1" ><p align="center">内容描述</p></td>
            <td width="222" height="150" colspan="3" ></td>
        </tr>

        <tr>
            <td width="150" height="50"><p align="center">载体起止编号</p><p>（或电子文件起止号）</p></td>
            <td width="222" height="50"><p align="center"><%=request.getAttribute("qzh_name")%><%=request.getAttribute("nd")%>年<%=request.getAttribute("table")%>档案接收</p></td>
            <td width="150" height="50"><p align="center">移交载体类型、规范</p></td>
            <td width="222" height="50"><p align="center">光盘</p></td>
        </tr>

        <tr>
            <td width="100" height="50"><p align="center">接收批次号</p></td>
            <td width="222" height="50"><p align="center">
                <%if(request.getAttribute("pch")==null){
                    %>
                    无
                <%
                }else{%>
                    <%=request.getAttribute("pch")%>
                <%}%>
            </p></td>

            <td width="100" height="50"><p align="center">接收电子档案数量</p></td>
            <td width="222" height="50"><p align="center">40</p></td>
        </tr>
        <tr>
            <td width="100" height="50"><p align="center">全宗号</p></td>
            <td width="222" height="50"><p align="center">
                <%if(request.getAttribute("qzh_number")==null){
                %>
                无
                <%
                }else{%>
                <%=request.getAttribute("qzh_number")%>
                <%}%>
            </p></td>

            <td width="100" height="50"><p align="center">全宗名称</p></td>
            <td width="222" height="50"><p align="center">
                <%if(request.getAttribute("qzh_name")==null){
                %>
                无
                <%
                }else{%>
                <%=request.getAttribute("qzh_name")%>
                <%}%>
            </p></td>
        </tr>
        <tr>
            <td width="150" height="50" rowspan="2"><p align="center">校验内容</p></td>
            <td width="100" height="50" colspan="3" ><p align="center">单位名称</p></td>
        </tr>
        <tr>
            <td width="100" height="50"><p align="center">移交单位</p></td>
            <td width="100" height="50" colspan="2" ><p align="center">接收单位</p></td>

        </tr>
        <tr>
            <td width="100" height="50"><p align="center">真实性检验</p></td>
            <td width="100" height="50"  ><p align="center">通过</p></td>
            <td width="100" height="50" colspan="2" ><p align="center"></p></td>
        </tr>

        <tr>
            <td width="100" height="50"><p align="center">完整性检验</p></td>
            <td width="100" height="50"  ><p align="center">通过</p></td>
            <td width="100" height="50" colspan="2" ><p align="center"></p></td>
        </tr>

        <tr>
            <td width="100" height="50"><p align="center">可用性检验</p></td>
            <td width="100" height="50"  ><p align="center">通过</p></td>
            <td width="100" height="50" colspan="2" ><p align="center"></p></td>
        </tr>
        <tr>
            <td width="100" height="50"><p align="center">安全性检验</p></td>
            <td width="100" height="50"  ><p align="center">通过</p></td>
            <td width="100" height="50" colspan="2" ><p align="center"></p></td>
        </tr>
        <tr>
            <td width="100" height="50"><p align="center">载体外观检验</p></td>
            <td width="100" height="50"  ><p align="center">通过</p></td>
            <td width="100" height="50" colspan="2" ><p align="center"></p></td>
        </tr>

        <tr>
            <td width="100" height="50"><p align="center">填表人（签名）</p></td>
            <td width="100" height="50"  ><p align="center"></p></td>
            <td width="100" height="50" colspan="2" ><p align="center"></p></td>
        </tr>
        <tr>
            <td width="100" height="50"><p align="center">审核人（签名）</p></td>
            <td width="100" height="50"  ><p align="center"></p></td>
            <td width="100" height="50" colspan="2" ><p align="center"></p></td>
        </tr>

    </table>
</div>
</body>
</html>

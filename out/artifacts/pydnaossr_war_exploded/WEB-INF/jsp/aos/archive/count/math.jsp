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

    <p align="center" id="pfont">四性检测报告(<%=request.getAttribute("tabledesc")%>)<br /></p>
    <table border="1" cellspacing="0" cellpadding="0" width="630" align="center">
        <tr>
            <td width="81" height="50" id="font"><p align="center">编号</p></td>
            <td width="341" height="50"><p align="center">检测名称</p></td>
            <td width="81" height="50"><p align="center">错误数量 </p></td>
        </tr>
        <tr>
            <td width="81" height="50"><p align="center">7</p></td>
            <td width="341" height="50"><p align="center">档号规范性检测(是否重复)</p></td>
            <td width="81" height="50"><p align="center">
                <%if(request.getAttribute("dh_equalssum")==null){
                    %>
                    无</p>
                <%
                }else{%>
                <p align="center" style="cursor:hand;color:red;" onclick="dh('dh_equalssum','<%=request.getAttribute("tablename")%>','<%=request.getAttribute("pch")%>', <%=request.getAttribute("dh_equalssum")%> )">
                    <%=request.getAttribute("dh_equalssum")%>
                <%}%>
            </p></td>
        </tr>
        <tr>
            <td width="81" height="50"><p align="center">9</p></td>
            <td width="341" height="50"><p align="center">全宗号是否规范且符合4位</p></td>
            <td width="81" height="50"><p align="center" >
                <%if(request.getAttribute("qzh_sum")==null){
                %>
                无</p>
                <%
                }else{%>
                <p align="center" style="cursor:hand;color:red;" onclick="dh('qzh_sum','<%=request.getAttribute("tablename")%>','<%=request.getAttribute("pch")%>',  <%=request.getAttribute("qzh_sum")%> )">
                <%=request.getAttribute("qzh_sum")%>
                <%}%>
            </p></td>
        </tr>
        <tr>
            <td width="81" height="50"><p align="center">10</p></td>
            <td width="341" height="50"><p align="center">全宗号是否有空值</p></td>
            <td width="81" height="50"><p align="center">
                <%if(request.getAttribute("qzh_null")==null){
                %>
                无</p>
                <%
                }else{%>
                <p align="center" style="cursor:hand;color:red;" onclick="dh('qzh_null','<%=request.getAttribute("tablename")%>','<%=request.getAttribute("pch")%>',<%=request.getAttribute("qzh_null")%> )">
                <%=request.getAttribute("qzh_null")%>
                <%}%>
            </p></td>
        </tr>
        <tr>
            <td width="81" height="50"><p align="center">11</p></td>
            <td width="341" height="50"><p align="center">目录号是否规范且符合4位</p></td>
            <td width="81" height="50"><p align="center" >
                <%if(request.getAttribute("mlh_sum")==null){
                %>
                无</p>
                <%
                }else{%>
                <p align="center" style="cursor:hand;color:red;" onclick="dh('mlh_sum','<%=request.getAttribute("tablename")%>','<%=request.getAttribute("pch")%>', <%=request.getAttribute("mlh_sum")%> )">
                    <%=request.getAttribute("mlh_sum")%>
                    <%}%>
                </p></td>
        </tr>
        <tr>
            <td width="81" height="50"><p align="center">12</p></td>
            <td width="341" height="50"><p align="center">目录号是否有空值</p></td>
            <td width="81" height="50"><p align="center">
                <%if(request.getAttribute("mlh_null")==null){
                %>
                无</p>
                <%
                }else{%>
                <p align="center" style="cursor:hand;color:red;" onclick="dh('mlh_null','<%=request.getAttribute("tablename")%>','<%=request.getAttribute("pch")%>', <%=request.getAttribute("mlh_null")%> )">
                    <%=request.getAttribute("mlh_null")%>
                    <%}%>
                </p></td>
        </tr>
        <tr>
            <td width="81" height="50"><p align="center">15</p></td>
            <td width="341" height="50"><p align="center">件号是否规范且符合4位</p></td>
            <td width="81" height="50"><p align="center" >
                <%if(request.getAttribute("jh_sum")==null){
                %>
                无</p>
                <%
                }else{%>
                <p align="center" style="cursor:hand;color:red;" onclick="dh('jh_sum','<%=request.getAttribute("tablename")%>','<%=request.getAttribute("pch")%>',<%=request.getAttribute("jh_sum")%> )">
                    <%=request.getAttribute("jh_sum")%>
                    <%}%>
                </p></td>
        </tr>
        <tr>
            <td width="81" height="50"><p align="center">16</p></td>
            <td width="341" height="50"><p align="center">件号是否有空值</p></td>
            <td width="81" height="50"><p align="center">
                <%if(request.getAttribute("jh_null")==null){
                %>
                无</p>
                <%
                }else{%>
                <p align="center" style="cursor:hand;color:red;" onclick="dh('jh_null','<%=request.getAttribute("tablename")%>','<%=request.getAttribute("pch")%>',<%=request.getAttribute("jh_null")%> )">
                    <%=request.getAttribute("jh_null")%>
                    <%}%>
                </p></td>
        </tr>
        <tr>
            <td width="81" height="50"><p align="center">13</p></td>
            <td width="341" height="50"><p align="center">年度是否规范且符合4位</p></td>
            <td width="81" height="50"><p align="center" >
                <%if(request.getAttribute("nd_sum")==null){
                %>
                无</p>
                <%
                }else{%>
                <p align="center" style="cursor:hand;color:red;" onclick="dh('nd_sum','<%=request.getAttribute("tablename")%>','<%=request.getAttribute("pch")%>',<%=request.getAttribute("nd_sum")%> )">
                    <%=request.getAttribute("nd_sum")%>
                    <%}%>
                </p></td>
        </tr>
        <tr>
            <td width="81" height="50"><p align="center">14</p></td>
            <td width="341" height="50"><p align="center">年度是否有空值</p></td>
            <td width="81" height="50"><p align="center">
                <%if(request.getAttribute("nd_null")==null){
                %>
                无</p>
                <%
                }else{%>
                <p align="center" style="cursor:hand;color:red;" onclick="dh('nd_null','<%=request.getAttribute("tablename")%>','<%=request.getAttribute("pch")%>',<%=request.getAttribute("nd_null")%> )">
                    <%=request.getAttribute("nd_null")%>
                    <%}%>
                </p></td>
        </tr>
        <tr>
            <td width="81" height="50"><p align="center">3-4</p></td>
            <td width="341" height="50"><p align="center">内容数据的可读性检测</p></td>
            <td width="81" height="50"><p align="center">
                <%if(request.getAttribute("file_booleansum")==null){
                %>
                无</p>
                <%
                }else{%>
                <p align="center" style="cursor:hand;color:red;" onclick="dh('file_booleansum','<%=request.getAttribute("tablename")%>','<%=request.getAttribute("pch")%>',<%=request.getAttribute("file_booleansum")%> )">
                <%=request.getAttribute("file_booleansum")%>
                <%}%>
            </p></td>
        </tr>
    </table>
</div>
<script type="text/javascript">
    function dh(text,tablename,pch,math){
        if(math===0){
            alert("没有错误数据");
            return;
        }
        parent.fnaddtab('456', '错误数据',
            'archive/datatemporary/sxjc_error2.jhtml?text='+text+'&tablename='+tablename+'&pch='+pch);
    }
</script>
</body>
</html>

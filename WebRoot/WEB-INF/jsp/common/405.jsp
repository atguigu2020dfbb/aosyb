<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" isErrorPage="true"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String path = request.getContextPath();
%>
<!DOCTYPE html>
<html>
<head>
    <title>Title</title>
    <script type="text/javascript">
        function to_login(){
            window.history.go(-1);
        }
    </script>
</head>
<style type="text/css">
    body{
        background-image: url("<%=path %>/static/image/background/main/timg.png");
        background-size:100%,100% ;
        background-attachment: fixed;
        background-origin:content-box;
    }
    .div1{font-family: "黑体"}
    .div1{font-weight:bold}
    .div1{color: #EFF3FF}
    .div1{font-size: 30px}
    .div1{margin-top: 200px}
    .div1{text-align:center}
    .div2{font-size: 30px}
    .div2{text-align:center}
    .btn1{text-align:center}
    .btn1{width: 60px}
    .btn1{height: 30px}
</style>
<body>
<div class = "div1"> 未授权该模块</div>
<div class = "div2"> <button class="btn1" onclick="to_login()">后退</button></div>


</body>


</html>
<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="/WEB-INF/tld/aos.tld" prefix="aos"%>
<%@ taglib uri="/WEB-INF/tld/cll.tld" prefix="c"%>
<link rel="stylesheet" type="text/css" href="static/homelogin/css/style.css" />
<html>
<aos:head title="${app_title}">
    <aos:base href="/" />
    <aos:include lib="ext,buttons" />
</aos:head>

<body height="100%">

<!--content部分开始-->
<div>
    <div style="margin-top: 100px;float: left;width: 50%;">
        <div style="margin-left: 30px;width:400px;text-align: center;float: left;" >
            <h1>
                <font style="font-size: 40px;">
                         延边州档案馆数字档案馆综合管理系统
                </font>
            </h1>
        </div>
         <div style="margin-left: -70%;margin-top: 200px;width: 50%;text-align: center;float: left;" >
                    <pre>
                          <font style="font-size: 10px;margin-left: 10px">
                                公            告
                          </font>
                    </pre>
         </div>
        <div style="margin-top: 300px;width: 50%;height:300px;margin-left: 10%; ">键入文字信息列表，您可将文本框放置在什么什么什么什么里,
            键入文字信息列表，您可将文本框放置在什么什么什么什么里
            键入文字信息列表，您可将文本框放置在什么什么什么什么里</div>

        </div>
    </div>
    <div  style="width: 450px;height:600px;float: left;margin-top:10px;margin-left:0px;text-align: center;" >
       <div>
        <c:forEach var="xt" items="${xtDtos}">
            <a style="width:150px;height:150px;float: left;" href="index4.jhtml?module_id_=${xt.id_}&user_id_=${userid}"><div class="tu"><img width="130px" height="130px" src="static/homelogin/images/01.png"/></div>
                <p>${xt.name_}</p>
            </a>
        </c:forEach>
       </div>
        <div style="width: 450px;height:150px;float: left;margin-left:0px;text-align: center" >
            <a href="index4.jhtml?module_id_=7a6d3674e5204937951d01544e18e3aa&user_id_=${userid}"><div class="tu"><img width="130px" height="130px" src="static/homelogin/images/01.png"/></div>
                <p>系统管理</p>
            </a>
        </div>
    </div>
</div>
</body>
</html>

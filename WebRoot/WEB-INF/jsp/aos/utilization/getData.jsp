<%@page contentType="text/html; charset=UTF-8" isELIgnored="false"%>
<%@taglib prefix="aos" uri="/WEB-INF/tld/aos.tld"%>
<%@ taglib uri="/WEB-INF/tld/cll.tld" prefix="c"%>
<%
    String path = request.getContextPath();
    Object fieldDtos = request.getAttribute("fieldDtos");
    //Object id_=request.getAttribute("id_");
%>
<aos:html>
    <aos:head title="检索档案">
        <aos:base href="utilization"/>
        <aos:include lib="ext,swfupload"/>
    </aos:head>
    <aos:body>

    </aos:body>
    <aos:onready>
        <aos:viewport layout="border">
            <aos:panel region="center" layout="fit" bodyBorder="0 0 0 1">
                <aos:iframe id="_iframe"  src="" />
            </aos:panel>
        </aos:viewport>
        <script type="text/javascript">

            window.onload=function(){
                    var url='getBasicSearch.jhtml?tablename=null';
                _iframe.load(url);
            };
           function _fn_load(url){
               if (!AOS.empty(url)) {
                   _iframe.load(url);
               }
           }
        </script>
    </aos:onready>
</aos:html>
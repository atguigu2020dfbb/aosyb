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
        <aos:base href="make"/>
        <aos:include lib="ext,swfupload"/>
    </aos:head>
    <aos:body>

    </aos:body>
    <aos:onready>
        <aos:viewport layout="border">
            <aos:panel region="west" width="160" border="false" layout="auto" >
                <aos:hiddenfield name="recordid" id="recordid" value="${recordid}"/>
                <aos:hiddenfield name="djbh" id="djbh" value="${djbh}"/>
                <aos:hiddenfield name="bj" id="bj" value="${bj}"/>
                <aos:docked forceBoder="${bodyBorder}">
                    <aos:dockeditem xtype="button" icon="icon140.png"/>
                    <aos:dockeditem xtype="tbtext" text="检索分类导航"/>
                </aos:docked>
                <aos:menu floating="false" border="false">
                    <aos:menuitem  text="基本检索" onclick="_fn_load('initBasicSearch.jhtml?djbh=${djbh}&recordid=${recordid}')" icon="icon71.png" />
                    <aos:menuitem  text="跨库检索" onclick="_fn_load('initCrossSearch.jhtml')" icon="icon17.gif" />
                    <aos:menuitem  text="全文检索" onclick="_fn_load('${cxt}/retrieval/retrieval/initRetrieval.jhtml')" icon="node.png" />
                </aos:menu>
            </aos:panel>

            <aos:panel region="center" layout="fit" bodyBorder="0 0 0 1">
                <aos:iframe id="_iframe"  src="" />
            </aos:panel>
        </aos:viewport>
        <script type="text/javascript">

            window.onload=function(){
                    var url='initBasicSearch.jhtml?tablename=null&recordid='+recordid.getValue()+'&djbh='+djbh.getValue()+'&bj='+bj.getValue();
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
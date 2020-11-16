<%@page contentType="text/html; charset=utf-8" %>
<%@taglib prefix="aos" uri="/WEB-INF/tld/aos.tld" %>
<%@ taglib uri="/WEB-INF/tld/cll.tld" prefix="c"%>
<aos:html>
    <aos:head title="查阅审核">
        <aos:include lib="ext"/>
        <aos:base href="utilization/data"/>
    </aos:head>
    <aos:body>
    </aos:body>
    <aos:onready>
        <aos:viewport layout="fit">
            <aos:gridpanel id="_g_consult" url="listConsult.jhtml"  onrender="_g_consult_onrender"
                            hidePagebar="false" pageSize="10">
                <aos:docked>
                    <aos:dockeditem text="查看" icon="query.png"  onclick="_w_readCard_show" />
                    <aos:dockeditem xtype="tbfill" />
                </aos:docked>
                <aos:column header="id_" dataIndex="id_" hidden="true"  locked="true"/>
                <aos:column header="formid" dataIndex="formid" hidden="true"/>
                <aos:column dataIndex="img" hidden="true"/>
                <aos:column dataIndex="users" hidden="true"/>
                <aos:column header="登记编号" dataIndex="zzbh" width="150"/>
                <aos:column header="姓名" dataIndex="xm"/>
                <aos:column header="性别" dataIndex="xb"/>
                <aos:column header="民族或国籍" dataIndex="mzgj"/>
                <aos:column header="出生" dataIndex="cs"/>
                <aos:column header="地址" dataIndex="dz"/>
                <aos:column header="身份证号" dataIndex="sfzh"/>
                <aos:column header="签发机关" dataIndex="qfjg"/>
                <aos:column header="备注" dataIndex="bz"/>
                <aos:column header="" flex="1"/>
            </aos:gridpanel>
        </aos:viewport>

        <script type="text/javascript">
            function _g_consult_onrender(){
                _g_consult_store.load();
            }
            //查询数据表列信息
            function _g_consult_detail_query(){
                var params = {
                    table_desc_ : ""
                };
                var record = AOS.selectone(_g_consult);
                if (!AOS.empty(record)) {

                    params.formid = record.data.id_;
                    params.ckzt=record.data.ckzt;
                    params.yngh=record.data.yngh;
                }
                _g_make_detail_store.getProxy().extraParams = params;
                _g_make_detail_store.load();
            }
            function _w_readCard_show() {
                var record = AOS.selectone(_g_consult);
                if (!AOS.empty(record)) {
                    window.parent.fnaddtab('','详细信息','/utilization/data/contrast.jthml?id_='+record.data.id_);
                }

            }
        </script>
    </aos:onready>
</aos:html>
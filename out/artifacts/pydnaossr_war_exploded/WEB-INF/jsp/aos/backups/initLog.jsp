<%@page contentType="text/html; charset=utf-8" %>
<%@taglib prefix="aos" uri="/WEB-INF/tld/aos.tld" %>
<aos:html>
    <aos:head>
        <aos:include lib="ext"/>
        <aos:base href="backups/strategy"/>
    </aos:head>
    <aos:body>
    </aos:body>
    <aos:onready>
        <aos:viewport layout="fit">
            <aos:gridpanel id="_g_log" onrender="_g_log_query" url="listLog.jhtml">
                <aos:docked forceBoder="0 0 1 0">
                    <aos:triggerfield emptyText="还原表或还原人" name="key_name_" id="key_name_" onenterkey="_g_log_query"
                                      trigger1Cls="x-form-search-trigger" onTrigger1Click="_g_log_query" width="180" />
                </aos:docked>
                <aos:column header="流水号" dataIndex="id_" hidden="true"/>
                <aos:column header="还原表" dataIndex="hyb"/>
                <aos:column header="还原文件" dataIndex="hywj"/>
                <aos:column header="还原时间" dataIndex="hysj"/>
                <aos:column header="还原人" dataIndex="hyr"/>
            </aos:gridpanel>
        </aos:viewport>

        <script type="text/javascript">
            function _g_log_query(){
                var params = {
                    key_name_: key_name_.getValue(),
                };
                _g_log_store.getProxy().extraParams = params;
                _g_log_store.load();
            }



        </script>
    </aos:onready>
</aos:html>
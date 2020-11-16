<%@page contentType="text/html; charset=utf-8" %>
<%@taglib prefix="aos" uri="/WEB-INF/tld/aos.tld" %>
<aos:html>
    <aos:head>
        <aos:include lib="ext"/>
        <aos:base href="notification"/>
    </aos:head>
    <aos:body>
    </aos:body>
    <aos:onready>
        <aos:viewport layout="fit">
            <aos:gridpanel id="_g_wait" onrender="_g_wait_query" url="listWait.jhtml">
                <aos:docked>
                    <aos:dockeditem text="查看" icon="modules_wizard.gif" onclick="_w_wait_show"/>
                </aos:docked>
                <aos:selmodel type="checkbox" mode="multi" />
                <aos:column header="流水号" dataIndex="id_" hidden="true"/>
                <aos:column header="任务名称" dataIndex="rwmc"/>
                <aos:column header="提交人" dataIndex="tjrcn"/>
                <aos:column header="提交时间" dataIndex="tjsj"/>
                <aos:column header="审核人" dataIndex="shrcn"/>
                <aos:column header="审核时间" dataIndex="shsj"/>
                <aos:column hidden="true" header="任务地址" dataIndex="rwurl" />
            </aos:gridpanel>
        </aos:viewport>

        <script type="text/javascript">
            function _g_wait_query(){
                _g_wait_store.load();
            }

            function _w_wait_show(){
                var record = AOS.selectone(_g_wait);
                var url = record.data.rwurl;
                window.parent.fnaddtab('_id_tab_'+record.data.id_, record.data.rwmc, url);
                AOS.ajax({
                    wait:false,
                    url:'updateNotification.jhtml',
                    params:{
                        id_:record.data.id_,
                        sfck:'1'
                    }
                });
                window.parent.fnaddtab('_id_tab_'+record.data.id_, record.data.rwmc, url);
            }

        </script>
    </aos:onready>
</aos:html>
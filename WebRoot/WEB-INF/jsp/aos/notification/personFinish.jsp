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
            <aos:gridpanel id="_g_finish" onrender="_g_finish_query" url="listFinish.jhtml">
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
        <aos:window id="_w_finish" title="新增">
            <aos:formpanel id="_f_finish" layout="anchor" width="700">
                <aos:textfield fieldLabel="保修人" name="bxr"/>
                <aos:datefield fieldLabel="检查时间" name="jcsj"/>
                <aos:textfield fieldLabel="检索类型" name="jslx"/>
                <aos:textfield fieldLabel="检查人员" name="jcry"/>
                <aos:textfield fieldLabel="故障解决问题" name="gzjj"/>
                <aos:textfield fieldLabel="故障描述" name="gzms"/>
                <aos:textfield fieldLabel="故障原因" name="gzyy"/>
                <aos:textfield fieldLabel="维修部门" name="wxbm"/>
                <aos:textfield fieldLabel="维修人员" name="wxry"/>
                <aos:hiddenfield id="lx" name="lx" value="${lx}"/>
                <aos:docked dock="bottom">
                    <aos:dockeditem xtype="tbfill"/>
                    <aos:dockeditem text="添加" icon="add.png" onclick="_f_finish_save"/>
                    <aos:dockeditem text="关闭" icon="close.png" onclick="#_w_finish.hide()"/>
                </aos:docked>
            </aos:formpanel>
        </aos:window>
        <aos:window id="_w_finish_u" title="修改">
            <aos:formpanel id="_f_finish_u" layout="anchor" width="700">
                <aos:hiddenfield name="id_" fieldLabel="流水号" />
                <aos:textfield fieldLabel="保修人" name="bxr"/>
                <aos:datefield fieldLabel="检查时间" name="jcsj"/>
                <aos:textfield fieldLabel="检索类型" name="jslx"/>
                <aos:textfield fieldLabel="检查人员" name="jcry"/>
                <aos:textfield fieldLabel="故障解决问题" name="gzjj"/>
                <aos:textfield fieldLabel="故障描述" name="gzms"/>
                <aos:textfield fieldLabel="故障原因" name="gzyy"/>
                <aos:textfield fieldLabel="维修部门" name="wxbm"/>
                <aos:textfield fieldLabel="维修人员" name="wxry"/>
                <aos:hiddenfield id="lx" name="lx" value="${lx}"/>
                <aos:docked dock="bottom">
                    <aos:dockeditem xtype="tbfill"/>
                    <aos:dockeditem text="保存" icon="save.png" onclick="_f_finish_u_save"/>
                    <aos:dockeditem text="关闭" icon="close.png" onclick="#_w_finish_u.hide()"/>
                </aos:docked>
            </aos:formpanel>
        </aos:window>

        <script type="text/javascript">
            function _g_finish_query(){
                _g_finish_store.load();
            }

            function _w_finish_show(){
                _w_finish.show();
            }
            function _f_finish_save(){
                AOS.ajax({
                    forms:_f_finish,
                    url:'savefinish.jhtml',
                    ok:function(data){
                        if(data.appcode === 1){
                            _w_finish.hide();
                            _g_finish_store.reload();
                            AOS.tip(data.appmsg);
                        }else {
                            AOS.err(data.appmsg);
                        }
                    }
                })
            }
            function _w_finish_u_show(){
                var record = AOS.selectone(_g_finish);
                if(record){
                    _w_finish_u.show();
                    _f_finish_u.loadRecord(record);
                }
            }
            function _f_finish_u_save(){
                AOS.ajax({
                    forms:_f_finish_u,
                    url:'updatefinish.jhtml',
                    ok:function(data){
                        if(data.appcode===1){
                            _w_finish_u.hide();
                            _g_finish_store.reload();
                            AOS.tip(data.appmsg);
                        }else {
                            AOS.err(AOS.appmsg);
                        }
                    }
                })
            }
            function _g_finish_del(){
                var selection = AOS.selection(_g_finish,'id_');
                if(AOS.empty(selection)){
                    AOS.tip("删除前请选中数据。。。");
                    return;
                }
                var rows = AOS.rows(_g_finish);
                var msg = AOS.merge('确定要删除选中的[{0}]条数据吗？',rows);
                AOS.confirm(msg,function(btn){
                    if(btn==='cancel'){
                        AOS.tip('删除被操作取消');
                        return;
                    }
                    AOS.ajax({
                        url:'deletefinish.jhtml',
                        params:{
                            aos_rows_: selection
                        },
                        ok:function(data){
                            AOS.tip(data.appmsg);
                            _g_finish_store.reload();
                        }
                    })
                })
            }
        </script>
    </aos:onready>
</aos:html>
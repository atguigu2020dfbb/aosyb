<%@ page contentType="text/html; charset=UTF-8" %>
<%@taglib prefix="aos" uri="/WEB-INF/tld/aos.tld" %>
<aos:html>
    <aos:head title="入库">
        <aos:include lib="ext"/>
        <aos:base href="backups/strategy"/>
    </aos:head>
    <aos:body>
    </aos:body>
    <aos:onready>
        <aos:viewport layout="fit">
            <aos:gridpanel id="_g_carrier" onrender="_g_carrier_query" url="listCarrier.jhtml">
                <aos:docked>
                    <aos:dockeditem text="新增" icon="add.png" onclick="_w_carrier_show"/>
                    <aos:dockeditem text="修改" icon="edit.png" onclick="_w_carrier_u_show"/>
                    <aos:dockeditem text="删除" icon="del.png" onclick="_g_carrier_del"/>
                </aos:docked>
                <aos:selmodel type="checkbox" mode="multi" />
                <aos:column header="流水号" dataIndex="id_" hidden="true"/>
                <aos:column header="备份名称" dataIndex="bfmc" />
                <aos:column header="备份大小" dataIndex="bfdx"/>
                <aos:column header="备份时间" dataIndex="bfsj"/>
                <aos:column header="备份格式" dataIndex="bfgs"/>
                <aos:column header="备注" dataIndex="bz" />
            </aos:gridpanel>
        </aos:viewport>


        <aos:window id="_w_carrier" title="新增">
            <aos:formpanel id="_f_carrier" width="700" layout="anchor">
                <aos:textfield name="bfmc" fieldLabel="备份名称"/>
                <aos:textfield name="bfdx" fieldLabel="备份大小"/>
                <aos:textfield name="bfsj" fieldLabel="备份时间"/>
                <aos:textfield name="bfgs" fieldLabel="备份格式"/>
                <aos:textfield name="bz" fieldLabel="备注"/>
                <aos:docked dock="bottom" ui="footer">
                    <aos:dockeditem xtype="tbfill"/>
                    <aos:dockeditem text="添加" icon="add.png" onclick="_f_carrier_save"/>
                    <aos:dockeditem text="关闭" icon="close.png" onclick="#_w_carrier.hide()"/>
                </aos:docked>
            </aos:formpanel>
        </aos:window>

        <aos:window id="_w_carrier_u" title="修改">
            <aos:formpanel id="_f_carrier_u" layout="anchor" width="700">
                <aos:docked dock="bottom" ui="footer">
                    <aos:dockeditem xtype="tbfill"/>
                    <aos:dockeditem text="保存" icon="save.png" onclick="_f_carrier_u_save"/>
                    <aos:dockeditem text="关闭" icon="close.png" onclick="#_w_carrier_u.hide()"/>
                </aos:docked>
                <aos:hiddenfield name="id_" fieldLabel="流水号"/>
                <aos:textfield name="bfmc" fieldLabel="备份名称"/>
                <aos:textfield name="bfdx" fieldLabel="备份大小"/>
                <aos:textfield name="bfsj" fieldLabel="备份时间"/>
                <aos:textfield name="bfgs" fieldLabel="备份格式"/>
                <aos:textfield name="bz" fieldLabel="备注"/>
            </aos:formpanel>
        </aos:window>
        <script type="text/javascript">
            function _g_carrier_query(){
                _g_carrier_store.load();
            }
            function _w_carrier_show(){
                //_w_depotout.show();
                _w_carrier.show();
            }
            function _w_carrier_u_show(){
                var record = AOS.selectone(_g_carrier);
                if(record){
                    _w_carrier_u.show();
                    _f_carrier_u.loadRecord(record);
                }

            }
            function _f_carrier_save(){
                AOS.ajax({
                    forms:_f_carrier,
                    url:'saveCarrier.jhtml',
                    ok:function(data){
                        if(data.appcode===-1){
                            AOS.err(data.appmsg);
                        }else{
                            //_w_carrier.hide();
                            _g_carrier_store.reload();
                            AOS.tip(data.appmsg);
                        }
                    }
                })
            }
            function _f_carrier_u_save(){
                AOS.ajax({
                    forms:_f_carrier_u,
                    url:'updateCarrier.jhtml',
                    ok:function(data){
                        if(data.appcode === 1){
                            _w_carrier_u.hide();
                            _g_carrier_store.reload();
                            AOS.tip(data.appmsg);
                        }else{
                            AOS.err(data.appmsg);
                        }
                    }

                })
            }
            function _g_carrier_del(){
                var selection = AOS.selection(_g_carrier,'id_');
                if(AOS.empty(selection)){
                    AOS.tip("删除前请选中数据。。。");
                    return;
                }
                var rows = AOS.rows(_g_carrier);
                var msg = AOS.merge('确定要删除选中的[{0}]条数据吗？',rows);
                AOS.confirm(msg,function(btn){
                    if(btn==='cancel'){
                        AOS.tip('删除被操作取消');
                        return;
                    }
                    AOS.ajax({
                        url:'deleteCarrier.jhtml',
                        params:{
                            aos_rows_: selection
                        },
                        ok:function(data){
                            AOS.tip(data.appmsg);
                            _g_carrier_store.reload();
                        }
                    })
                })
            }
        </script>

    </aos:onready>
</aos:html>

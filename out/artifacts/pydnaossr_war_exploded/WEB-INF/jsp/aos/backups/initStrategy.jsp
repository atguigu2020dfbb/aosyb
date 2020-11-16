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
            <aos:gridpanel id="_g_strategy" onrender="_g_strategy_query" url="listStrategy.jhtml">
                <aos:docked>
                    <aos:dockeditem text="新增" icon="add.png" onclick="_w_strategy_show"/>
                    <aos:dockeditem text="修改" icon="edit.png" onclick="_w_strategy_u_show"/>
                    <aos:dockeditem text="删除" icon="del.png" onclick="_g_strategy_del"/>
                </aos:docked>
                <aos:selmodel type="checkbox" mode="multi" />
                <aos:column header="流水号" dataIndex="id_" hidden="true"/>
                <aos:column header="存储规则" dataIndex="ccgz" />
                <aos:column header="存储路径" dataIndex="cclj"/>
                <aos:column header="存储方式" dataIndex="ccfs"/>
                <aos:column header="备注" dataIndex="bz" />
            </aos:gridpanel>
        </aos:viewport>


        <aos:window id="_w_strategy" title="新增">
            <aos:formpanel id="_f_strategy" width="700" layout="anchor">
                <aos:textfield name="ccgz" fieldLabel="存储规则"/>
                <aos:textfield name="cclj" fieldLabel="存储路径"/>
                <aos:textfield name="ccfs" fieldLabel="存储方式"/>
                <aos:textfield name="bz" fieldLabel="备注"/>
                <aos:docked dock="bottom" ui="footer">
                    <aos:dockeditem xtype="tbfill"/>
                    <aos:dockeditem text="添加" icon="add.png" onclick="_f_strategy_save"/>
                    <aos:dockeditem text="关闭" icon="close.png" onclick="#_w_strategy.hide()"/>
                </aos:docked>
            </aos:formpanel>
        </aos:window>

        <aos:window id="_w_strategy_u" title="修改">
            <aos:formpanel id="_f_strategy_u" layout="anchor" width="700">
                <aos:docked dock="bottom" ui="footer">
                    <aos:dockeditem xtype="tbfill"/>
                    <aos:dockeditem text="上一条" icon="more/go-previous.png"  onclick="_f_previous_data"/><%----%>
                    <aos:dockeditem text="下一条" icon="more/go-next.png" onclick="_f_next_data"/><%----%>
                    <aos:dockeditem text="保存" icon="save.png" onclick="_f_strategy_u_save"/>
                    <aos:dockeditem text="关闭" icon="close.png" onclick="#_w_strategy_u.hide()"/>
                </aos:docked>
                <aos:hiddenfield name="id_" fieldLabel="流水号"/>
                <aos:textfield name="ccgz" fieldLabel="存储规则"/>
                <aos:textfield name="cclj" fieldLabel="存储路径"/>
                <aos:textfield name="ccfs" fieldLabel="存储方式"/>
                <aos:textfield name="bz" fieldLabel="备注"/>
            </aos:formpanel>
        </aos:window>
        <script type="text/javascript">
            function _g_strategy_query(){
                _g_strategy_store.load();
            }
            function _w_strategy_show(){
                //_w_depotout.show();
                _w_strategy.show();
            }
            function _w_strategy_u_show(){
                var record = AOS.selectone(_g_strategy);
                if(record){
                    _w_strategy_u.show();
                    _f_strategy_u.loadRecord(record);
                }

            }
            function _f_strategy_save(){
                AOS.ajax({
                    forms:_f_strategy,
                    url:'saveStrategy.jhtml',
                    ok:function(data){
                        if(data.appcode===-1){
                            AOS.err(data.appmsg);
                        }else{
                            //_w_strategy.hide();
                            _g_strategy_store.reload();
                            AOS.tip(data.appmsg);
                        }
                    }
                })
            }
            function _f_strategy_u_save(){
                AOS.ajax({
                    forms:_f_strategy_u,
                    url:'updateStrategy.jhtml',
                    ok:function(data){
                        if(data.appcode === 1){
                            _w_strategy_u.hide();
                            _g_strategy_store.reload();
                            AOS.tip(data.appmsg);
                        }else{
                            AOS.err(data.appmsg);
                        }
                    }

                })
            }
            function _g_strategy_del(){
                var selection = AOS.selection(_g_strategy,'id_');
                if(AOS.empty(selection)){
                    AOS.tip("删除前请选中数据。。。");
                    return;
                }
                var rows = AOS.rows(_g_strategy);
                var msg = AOS.merge('确定要删除选中的[{0}]条数据吗？',rows);
                AOS.confirm(msg,function(btn){
                    if(btn==='cancel'){
                        AOS.tip('删除被操作取消');
                        return;
                    }
                    AOS.ajax({
                        url:'deleteStrategy.jhtml',
                        params:{
                            aos_rows_: selection
                        },
                        ok:function(data){
                            AOS.tip(data.appmsg);
                            _g_strategy_store.reload();
                        }
                    })
                })
            }
            //上一页
            function _f_previous_data(){
                var count=Ext.getCmp("_g_strategy").getStore().getCount();
                var me=Ext.getCmp("_g_strategy").getSelectionModel().getSelection();
                //var record = AOS.selectone(_g_strategy);
                //得到执行行的坐标
                var rowIndex = Ext.getCmp("_g_strategy").getStore().indexOf(me[0]);
                if(rowIndex==0){
                    AOS.err("当前第一条!");
                    return;
                }
                var s=Ext.getCmp("_g_strategy").getStore().getAt(rowIndex-1);
                //原先行取消选中
                Ext.getCmp("_g_strategy").getSelectionModel().deselect(rowIndex);
                //此时让光标选中上一行
                Ext.getCmp("_g_strategy").getSelectionModel().select(rowIndex-1, true);
                //组件被显示后触发。
                Ext.getCmp("_f_strategy_u").form.setValues(s.data);
            }
            //下一页
            function _f_next_data(){
                var count=Ext.getCmp("_g_strategy").getStore().getCount();
                var me=Ext.getCmp("_g_strategy").getSelectionModel().getSelection();
                //var record = AOS.selectone(_g_strategy);
                //得到执行行的坐标
                var rowIndex = Ext.getCmp("_g_strategy").getStore().indexOf(me[0]);
                if(rowIndex==count-1){
                    AOS.err("当前最后一条!");
                    return;
                }
                var s=Ext.getCmp("_g_strategy").getStore().getAt(rowIndex+1);
                //原先行取消选中
                Ext.getCmp("_g_strategy").getSelectionModel().deselect(rowIndex);
                //此时让光标选中下一行
                Ext.getCmp("_g_strategy").getSelectionModel().select(rowIndex+1, true);
                //组件被显示后触发。
                Ext.getCmp("_f_strategy_u").form.setValues(s.data);
            }
        </script>

    </aos:onready>
</aos:html>

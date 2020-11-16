<%@page contentType="text/html; charset=utf-8" %>
<%@taglib prefix="aos" uri="/WEB-INF/tld/aos.tld" %>
<aos:html>
    <aos:head>
        <aos:include lib="ext"/>
        <aos:base href="depot/environment"/>
    </aos:head>
    <aos:body>
    </aos:body>
    <aos:onready>
        <aos:viewport layout="fit">
            <aos:hiddenfield id="depotname" value="${depotname}"/>
            <aos:hiddenfield id="jcr_cn" value="${jcr_cn}"/>
            <aos:hiddenfield id="jcr_en" value="${jcr_en}"/>
            <aos:gridpanel id="_g_environment" pageSize="100" onrender="_g_environment_query" url="listEnvironment.jhtml">
                <aos:docked>
                    <aos:dockeditem text="新增" icon="add.png" onclick="_w_environment_show"/>
                    <aos:dockeditem text="修改" icon="edit.png" onclick="_w_environment_u_show"/>
                    <aos:dockeditem text="删除" icon="del.png" onclick="_g_environment_del"/>
                    <aos:dockeditem onclick="_w_charts_export" text="导出excel" icon="icon70.png" />
                    <aos:dockeditem onclick="_w_import_show" text="导入excel" icon="folder8.png" />
                    <aos:dockeditem onclick="_w_update_environment" text="自动添加" icon="folder8.png" />
                    <aos:dockeditem onclick="_w_get_environment" text="获取数据" icon="folder22.png" />

                </aos:docked>
                <aos:column header="id_" dataIndex="id_" hidden="true" locked="true"/>
                <aos:column header="监测日期" dataIndex="jcrq" width="230"/>
                <aos:column header="库房编号" dataIndex="DepotName" width="230"/>
                <aos:column header="库房温度" dataIndex="wd" width="230"/>
                <aos:column header="温度状况" dataIndex="wdzk" width="150" hidden="true"/>
                <aos:column header="库房湿度" dataIndex="sd" width="230"/>
                <aos:column header="湿度状况" dataIndex="sdzk" width="150" hidden="true"/>
                <aos:column header="监测人" dataIndex="jcr_cn" width="230"/>
                <aos:column header="监测人" dataIndex="jcr_en" hidden="true" width="150"/>

                <aos:column header="备注" dataIndex="bz" width="230"/>
                <aos:column header="" flex="1" />
            </aos:gridpanel>
        </aos:viewport>
        <aos:window id="_w_environment" title="新增"   onshow="_new_task">
            <aos:formpanel id="_f_environment" layout="column" width="800">
                <aos:textfield name="wd" fieldLabel="库房温度" columnWidth="0.49"/>
                <aos:combobox fieldLabel="温度状况" name="wdzk" value="正常" columnWidth="0.49">
                    <aos:option value="正常" display="正常"></aos:option>
                    <aos:option value="偏高" display="偏高"></aos:option>
                    <aos:option value="偏低" display="偏低"></aos:option>
                </aos:combobox>
                <aos:textfield name="sd" fieldLabel="库房湿度" columnWidth="0.49"/>
                <aos:combobox fieldLabel="湿度状况" name="sdzk" value="正常" columnWidth="0.49">
                    <aos:option value="正常" display="正常"></aos:option>
                    <aos:option value="偏高" display="偏高"></aos:option>
                    <aos:option value="偏低" display="偏低"></aos:option>
                </aos:combobox>
                <aos:textfield name="jcr_cn"  fieldLabel="监测人" value="${jcr_cn}" columnWidth="0.49"/>
                <aos:hiddenfield name="jcr_en"  fieldLabel="监测人" value="${jcr_en}" columnWidth="0.49"/>
                <aos:datefield name="jcrq" fieldLabel="监测日期" columnWidth="0.49"/>
                <aos:textfield name="bz" fieldLabel="备注" columnWidth="0.98"/>
                <aos:hiddenfield id="depotname" name="depotname" value="${depotname}"/>
                <aos:docked dock="bottom">
                    <aos:dockeditem xtype="tbfill"/>
                    <aos:dockeditem text="新增" icon="add.png" onclick="_f_add_environment"/>
                    <aos:dockeditem text="添加" icon="save.png" onclick="_f_environment_save"/>
                    <aos:dockeditem text="关闭" icon="close.png" onclick="#_w_environment.hide()"/>
                </aos:docked>
            </aos:formpanel>
        </aos:window>
        <aos:window id="_w_environment_u" title="修改">
            <aos:formpanel id="_f_environment_u" layout="column" width="800">
                <aos:hiddenfield name="id_" fieldLabel="流水号" />
                <aos:textfield name="wd" fieldLabel="库房温度" columnWidth="0.49"/>
                <aos:textfield name="sd" fieldLabel="库房湿度" columnWidth="0.49"/>
                <aos:textfield name="jcr_cn"  fieldLabel="监测人" columnWidth="0.49"/>
                <aos:hiddenfield name="jcr_en"  fieldLabel="监测人" columnWidth="0.49"/>
                <aos:datefield name="jcrq" fieldLabel="监测日期" columnWidth="0.49"/>
                <aos:textfield name="bz" fieldLabel="备注" columnWidth="0.98"/>
                <aos:docked dock="bottom">
                    <aos:dockeditem xtype="tbfill"/>

                    <aos:dockeditem text="上一条" icon="more/go-previous.png"  onclick="_f_previous_data"/><%----%>
                    <aos:dockeditem text="下一条" icon="more/go-next.png" onclick="_f_next_data"/><%----%>

                    <aos:dockeditem text="保存" icon="save.png" onclick="_f_environment_u_save"/>

                    <aos:dockeditem text="关闭" icon="close.png" onclick="#_w_environment_u.hide()"/>
                </aos:docked>
            </aos:formpanel>
        </aos:window>

        <script type="text/javascript">

            //生成XLS报表
            function _w_charts_export() {

                AOS.ajax({
                    url : 'fillReport_wsd.jhtml',
                    params:{kfbh:Ext.getCmp("kfbh").getValue()},
                    ok : function(data) {
                        AOS.file('${cxt}/report/xls2.jhtml');
                    }
                });
            }
            //导入excel窗口
            function _w_import_show(){
                window.parent.fnaddtab('wsd','温湿度导入','/depot/environment/initWSDimport.jhtml?tablename=WSD&kfbh='+Ext.getCmp("kfbh").getValue());
            }
            function _g_environment_query(){
                var params={
                    depotname:depotname.getValue()
                };
                _g_environment_store.getProxy().extraParams=params;
                _g_environment_store.load();
            }
            function _f_add_environment(){
                var lx= Ext.getCmp("depotname").getValue();
                if("x1"==lx){
                    lx="X1";
                }else if("x2"==lx){
                    lx="X2";
                }else if("x3"==lx){
                    lx="X3";
                }else if("ls"==lx){
                    lx="LS";
                }else if("zl"==lx){
                    lx="ZL";
                }else{
                }
            }
            function _w_get_environment(){
                AOS.ajax({
                    url:'get_addEnvironment.jhtml',
                    params:{
                        depotname: Ext.getCmp("depotname").getValue()
                    },
                    ok:function(data){
                        if(data.appcode===1){
                            AOS.tip("获取成功！");
                            _g_environment_store.reload();
                        }else{
                            AOS.tip("获取失败！");
                        }

                    }
                })
            }
            function _w_update_environment(){
                //更新当前数据的人名
                var msg = AOS.merge('确定要更新监测人和库房编号吗？');
                AOS.confirm(msg,function(btn){
                    if(btn==='cancel'){
                        AOS.tip('修改被操作取消');
                        return;
                    }
                    AOS.ajax({
                        url:'updateEnvironmentAll_lx.jhtml',
                        params:{
                            depotname: Ext.getCmp("depotname").getValue()
                        },
                        ok:function(data){
                            if(data.appcode===1){
                                AOS.tip("修改成功！");
                                _g_environment_store.reload();
                            }else{
                                AOS.tip("修改失败！");
                            }

                        }
                    })
                })
            }
            function _new_task(){
            }
            function _w_environment_show(){
                _w_environment.show();
            }
            function _f_environment_save(){
                AOS.ajax({
                    forms:_f_environment,
                    url:'saveEnvironment.jhtml',
                    ok:function(data){
                        if(data.appcode === 1){
                           // _w_environment.hide();
                            _g_environment_store.reload();
                            AOS.tip(data.appmsg);
                        }else {
                            AOS.err(data.appmsg);
                        }
                    }
                })
            }
            function _w_environment_u_show(){
                var record = AOS.selectone(_g_environment);
                if(record){
                    _w_environment_u.show();
                    _f_environment_u.loadRecord(record);
                }
            }
            function _f_environment_u_save(){
                AOS.ajax({
                    forms:_f_environment_u,
                    url:'updateEnvironment.jhtml',
                    ok:function(data){
                        if(data.appcode===1){
                            _w_environment_u.hide();
                            _g_environment_store.reload();
                            AOS.tip(data.appmsg);
                        }else {
                            AOS.err(AOS.appmsg);
                        }
                    }
                })
            }
            function _g_environment_del(){
                var selection = AOS.selection(_g_environment,'id_');
                if(AOS.empty(selection)){
                    AOS.tip("删除前请选中数据。。。");
                    return;
                }
                var rows = AOS.rows(_g_environment);
                var msg = AOS.merge('确定要删除选中的[{0}]条数据吗？',rows);
                AOS.confirm(msg,function(btn){
                    if(btn==='cancel'){
                        AOS.tip('删除被操作取消');
                        return;
                    }
                    AOS.ajax({
                        url:'deleteEnvironment.jhtml',
                        params:{
                            aos_rows_: selection
                        },
                        ok:function(data){
                            AOS.tip(data.appmsg);
                            _g_environment_store.reload();
                        }
                    })
                })
            }




            //上一页
            function _f_previous_data(){
                var count=Ext.getCmp("_g_environment").getStore().getCount();
                var me=Ext.getCmp("_g_environment").getSelectionModel().getSelection();
                //var record = AOS.selectone(_g_environment);
                //得到执行行的坐标
                var rowIndex = Ext.getCmp("_g_environment").getStore().indexOf(me[0]);
                if(rowIndex==0){
                    AOS.err("当前第一条!");
                    return;
                }
                var s=Ext.getCmp("_g_environment").getStore().getAt(rowIndex-1);
                //原先行取消选中
                Ext.getCmp("_g_environment").getSelectionModel().deselect(rowIndex);
                //此时让光标选中上一行
                Ext.getCmp("_g_environment").getSelectionModel().select(rowIndex-1, true);
                //组件被显示后触发。
                Ext.getCmp("_f_environment_u").form.setValues(s.data);
            }
            //下一页
            function _f_next_data(){
                var count=Ext.getCmp("_g_environment").getStore().getCount();
                var me=Ext.getCmp("_g_environment").getSelectionModel().getSelection();
                //var record = AOS.selectone(_g_environment);
                //得到执行行的坐标
                var rowIndex = Ext.getCmp("_g_environment").getStore().indexOf(me[0]);
                if(rowIndex==count-1){
                    AOS.err("当前最后一条!");
                    return;
                }
                var s=Ext.getCmp("_g_environment").getStore().getAt(rowIndex+1);
                //原先行取消选中
                Ext.getCmp("_g_environment").getSelectionModel().deselect(rowIndex);
                //此时让光标选中下一行
                Ext.getCmp("_g_environment").getSelectionModel().select(rowIndex+1, true);
                //组件被显示后触发。
                Ext.getCmp("_f_environment_u").form.setValues(s.data);
            }
        </script>
    </aos:onready>
</aos:html>
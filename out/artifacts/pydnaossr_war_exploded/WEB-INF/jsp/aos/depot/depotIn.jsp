<%@page contentType="text/html; charset=UTF-8" %>
<%@taglib uri="/WEB-INF/tld/aos.tld" prefix="aos" %>
<%@ taglib prefix="aso" uri="http://www.osworks.cn/tag/aos" %>
<aos:html>
    <aos:head>
        <aos:include lib="ext"/>
        <aos:base href="depot/depotIn"/>

    </aos:head>
    <aos:body>
    </aos:body>
    <aos:onready>
        <aos:viewport layout="fit">
            <aos:hiddenfield id="lx" value="${lx}"/>
            <aos:gridpanel id="_g_depotIn" url="listDepotIn.jhtml" onrender="_g_depotin_query">
                <aos:docked>
                    <aos:dockeditem xtype="tbtext" text="出库管理"/>
                    <aos:dockeditem text="新增" id="add" icon="add.png" onclick="_w_depotIn_show"/>
                    <aos:dockeditem text="修改" id="edit" icon="edit.png" onclick="_w_depotIn_u_show"/>
                    <aos:dockeditem text="删除" icon="del.png" onclick="_g_depotIn_del"/>
                    <aos:dockeditem onclick="_w_charts_export" text="导出excel" icon="icon70.png" />
                    <aos:dockeditem onclick="_w_import_show" text="导入excel" icon="folder8.png" />
                </aos:docked>
                <aos:column header="流水号" dataIndex="id_" hidden="true"  locked="true"/>
                <aos:column header="入库单号" dataIndex="rkdh" />
                <aos:column header="入库人" dataIndex="rkr" />
                <aso:column header="入库原因" dataIndex="rkyy"/>
                <aso:column header="原出库单号" dataIndex="yckdh"/>
                <aos:column header="入库时间" dataIndex="rksj" />
                <aos:column header="入库数量" dataIndex="rksl" />
                <aos:column header="入库状态" dataIndex="rkzt"/>
                <aos:column header="备注" dataIndex="bz" width="400"/>
                <aos:column header="" flex="1" />
            </aos:gridpanel>
        </aos:viewport>
        <aos:window id="_w_depotIn" title="新增" onshow="_w_location_onshow">
            <aos:formpanel id="_f_depotIn" width="700" layout="column">
                <aos:textfield name="rkdh" fieldLabel="入库单号"  columnWidth="0.99"/>
                <aos:textfield name="rkr" fieldLabel="入库人" columnWidth="0.99" value="${user}"/>
                <aos:checkboxgroup fieldLabel="入库原因" columnWidth="0.99" id="rkyy_add"  columns="[100,100]">
                    <aos:checkbox name="c1" boxLabel="征集进馆" readOnly="true"/>
                    <aos:checkbox name="c1" boxLabel="接收进馆"  readOnly="true"/>
                </aos:checkboxgroup>
                <aos:textfield name="yckdh" fieldLabel="原出库单号" columnWidth="0.99"/>
                <aos:datefield name="rksj" fieldLabel="入库时间" columnWidth="0.99"/>
                <aos:textfield name="rksl" fieldLabel="入库数量" columnWidth="0.99"/>
                <aos:combobox name="rkzt" fieldLabel="入库状态" columnWidth="0.99" allowBlank="false">
                    <aos:option value="待入库" display="待入库" />
                    <aos:option value="已入库" display="已入库" />
                </aos:combobox>
                <aos:textfield name="bz" fieldLabel="备注" columnWidth="0.99"/>
                <aos:hiddenfield id="lx" name="lx" value="${lx}"/>
                <aos:docked  dock="bottom">
                    <aos:dockeditem xtype="tbfill"/>
                    <aos:dockeditem text="新增" icon="add.png" onclick="_f_add_depotIn"/>
                    <aos:dockeditem text="保存" icon="save.png" onclick="_f_depotin_save"/>
                    <aos:dockeditem text="关闭" icon="close.png" onclick="#_w_depotIn.hide()"/>
                </aos:docked>
            </aos:formpanel>
        </aos:window>
        <aos:window id="_w_depotout_o" title="出库">
            <aos:formpanel id="_f_depotout_o" width="700" layout="anchor">
                <aos:textareafield name="ckdh" fieldLabel="出库单号"/>
                <aos:textareafield name="ckr" fieldLabel="出库人"/>
                <aos:numberfield name="cksj" fieldLabel="出库时间" />
                <aos:textareafield name="ckyy" fieldLabel="出库原因"/>
                <aos:textareafield name="cksl" fieldLabel="出库数量"/>
                <aos:textareafield name="zt" fieldLabel="状态"/>
                <aos:textareafield name="jkdbh" fieldLabel="借库单编号"/>
                <aos:docked>
                    <aos:dockeditem text="添加" icon="add.png" onclick="_f_depotout_o"/>
                    <aos:dockeditem text="关闭" icon="close.png" onclick="#_w_depotout_o.hide()"/>
                </aos:docked>
            </aos:formpanel>
        </aos:window>
        <aos:window id="_w_depotIn_u" title="修改" >
            <aos:formpanel id="_f_depotIn_u" width="700" layout="column" >
                <aos:hiddenfield name="id_" fieldLabel="流水号"/>
                <aos:textfield name="rkdh" fieldLabel="入库单号"  columnWidth="0.99"/>
                <aos:textfield name="rkr" fieldLabel="入库人" columnWidth="0.99"/>
                <aos:checkboxgroup fieldLabel="入库原因" columnWidth="0.99" id="rkyy_update" columns="[100,100,100,100,100]">
                    <aos:checkbox name="c1" boxLabel="征集进馆"  readOnly="true"/>
                    <aos:checkbox name="c1" boxLabel="接收进馆"  readOnly="true"/>
                </aos:checkboxgroup>
                <aos:textfield name="yckdh" fieldLabel="原出库单号" columnWidth="0.99"/>
                <aos:datefield name="rksj" fieldLabel="入库时间" columnWidth="0.99"/>
                <aos:textfield name="rksl" fieldLabel="入库数量" columnWidth="0.99"/>
                <aos:combobox name="rkzt" fieldLabel="入库状态" columnWidth="0.99" allowBlank="false">
                    <aos:option value="待入库" display="待入库" />
                    <aos:option value="已入库" display="已入库" />
                </aos:combobox>
                <aos:textfield name="bz" fieldLabel="备注" columnWidth="0.99"/>
                <aos:hiddenfield id="lx" name="lx" value="${lx}"/>
                <aos:docked  dock="bottom">
                    <aos:dockeditem xtype="tbfill"/>

                    <aos:dockeditem text="上一条" icon="more/go-previous.png"  onclick="_f_previous_data"/><%----%>
                    <aos:dockeditem text="下一条" icon="more/go-next.png" onclick="_f_next_data"/><%----%>

                    <aos:dockeditem text="保存" icon="save.png" onclick="_f_depotout_u_save"/>

                    <aos:dockeditem text="关闭" icon="close.png" onclick="#_w_depotIn_u.hide()"/>
                </aos:docked>
            </aos:formpanel>
        </aos:window>

        <script type="text/javascript">
            //导入excel窗口
            function _w_import_show(){
                window.parent.fnaddtab('rk','入库导入','/depot/safe/initimport.jhtml?tablename=depot_rk&lx='+Ext.getCmp("lx").getValue());
            }
            //生成XLS报表
            function _w_charts_export() {
                AOS.ajax({
                    url : 'fillReport_rk.jhtml',
                    params:{lx:Ext.getCmp("lx").getValue()},
                    ok : function(data) {
                        AOS.file('${cxt}/report/xls2.jhtml');
                    }
                });
            }
            function _g_depotin_query(){
                var params={
                    lx:lx.getValue()
                };
                _g_depotIn_store.getProxy().extraParams=params;
                _g_depotIn_store.load();
            }
            function _w_depotIn_show(){
                _w_depotIn.show();
            }
            function _f_depotin_save(){
                var rkyy="";
                var xqCheck = Ext.getCmp('rkyy_add').items;
                for(var i = 0; i < xqCheck.length; i++){
                    if(xqCheck.get(i).checked==true){
                        if(rkyy==""){
                            rkyy=xqCheck.get(i).boxLabel;
                        }else{
                            rkyy=rkyy+","+xqCheck.get(i).boxLabel;
                        }
                    }
                }
                AOS.ajax({
                    forms:_f_depotIn,
                    params:{rkyy:rkyy},
                    url:'saveDepotIn.jhtml',
                    ok:function (data){
                        if(data.appcode == -1){
                            AOS.err(data.appmsg);
                        }else {
                            //_w_depotIn.hide();
                            _g_depotIn_store.reload();
                            AOS.tip(data.appmsg);
                        }
                    }
                })
            }
            function _f_add_depotIn(){
                var lx= Ext.getCmp("lx").getValue();
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
                //根据tree名称得到对应的部门名称
                AOS.ajax({
                    params:{
                        name_:"入库流水号",
                        lx:lx,
                        rkdh:_f_depotIn.form.findField("rkdh").getValue()
                    },
                    url:'getDepotInIndex_nd.jhtml',
                    ok:function(data){
                        //设计一个随机数编号
                        //年
                        var time = (new Date).getTime();
                        var yesday=_f_depotIn.form.findField("rkdh").getValue().substring(3,7);
                        _f_depotIn.form.findField("rkdh").setValue(lx+rkdh+data.index);
                    }
                });
            }
            function _w_depotIn_u_show(){
                var record = AOS.selectone(_g_depotIn);
                if(record){
                    _w_depotIn_u.show();
                    _f_depotIn_u.loadRecord(record);
                    var xqCheck = Ext.getCmp('rkyy_update').items;
                    for(var i = 0; i < xqCheck.length; i++){
                        if(record.data.rkyy.indexOf(xqCheck.get(i).boxLabel)!=-1){
                            xqCheck.get(i).setValue(true);
                        }
                    }
                }



            }
            function _w_location_onshow(){
                //根据tree名称得到对应的部门名称
                var lx= Ext.getCmp("lx").getValue();
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
                AOS.ajax({
                    params:{name_:"入库流水号",lx:lx},
                    url:'getDepotInIndex.jhtml',
                    ok:function(data){
                        //设计一个随机数编号
                        //年
                        var time = (new Date).getTime();
                        var yesday = new Date(time); // 获取的是年
                        yesday = yesday.getFullYear()  +"";
                        _f_depotIn.form.findField("rkdh").setValue(lx+yesday+data.index);
                    }




                });
                //params:{name_:'出库流水号'},
                if(lx=="301"||lx=="302"){
                    var xqCheck = Ext.getCmp('rkyy_add').items;
                    xqCheck.get(0).setValue(true);
                }
                if(lx=="401"||lx=="402"||lx=="501"){
                    var xqCheck = Ext.getCmp('rkyy_add').items;
                    xqCheck.get(1).setValue(true);
                }

            }
            function _f_depotout_u_save(){
                var rkyy="";
                var xqCheck = Ext.getCmp('rkyy_update').items;
                for(var i = 0; i < xqCheck.length; i++){
                    if(xqCheck.get(i).checked==true){
                        if(rkyy==""){
                            rkyy=xqCheck.get(i).boxLabel;
                        }else{
                            rkyy=rkyy+","+xqCheck.get(i).boxLabel;
                        }
                    }
                }
                AOS.ajax({
                    forms:_f_depotIn_u,
                    params:{rkyy:rkyy},
                    url:'updateDepotIn.jhtml',
                    ok:function(data){
                        if(data.appcode === 1){
                            _w_depotIn_u.hide();
                            _g_depotIn_store.reload();
                            AOS.tip(data.appmsg);
                        }else{
                            AOS.err(data.appmsg);
                        }
                    }

                })

            }

            function _w_depotIn_o_show(){
                _w_depotIn_o.show();
            }
            /*function _w_depotout_u_onshow(){
                var record = AOS.selectone('_g_depotout');
                AOS.ajax({
                    params:{
                      id_:record.data.id_
                    },
                    url:'getDepotout.jhtml',
                    ok:function(data){
                        _f_depotout_u.form.setValue(data);
                    }
                })

            }*/
            function _g_depotIn_del(){
                var selection = AOS.selection(_g_depotIn,'id_');
                if(AOS.empty(selection)){
                    AOS.tip("删除前请选中数据。。。");
                    return;
                }
                var rows = AOS.rows(_g_depotIn);
                var msg = AOS.merge('确定要删除选中的[{0}]条数据吗？',rows);
                AOS.confirm(msg,function(btn){
                    if(btn==='cancel'){
                        AOS.tip('删除被操作取消');
                        return;
                    }
                    AOS.ajax({
                        url:'deleteDepotIn.jhtml',
                        params:{
                            aos_rows_: selection
                        },
                        ok:function(data){
                            AOS.tip(data.appmsg);
                            _g_depotIn_store.reload();
                        }
                    })
                })
            }




            //上一页
            function _f_previous_data(){
                var count=Ext.getCmp("_g_depotIn").getStore().getCount();
                var me=Ext.getCmp("_g_depotIn").getSelectionModel().getSelection();
                //var record = AOS.selectone(_g_depotIn);
                //得到执行行的坐标
                var rowIndex = Ext.getCmp("_g_depotIn").getStore().indexOf(me[0]);
                if(rowIndex==0){
                    AOS.err("当前第一条!");
                    return;
                }
                var s=Ext.getCmp("_g_depotIn").getStore().getAt(rowIndex-1);
                //原先行取消选中
                Ext.getCmp("_g_depotIn").getSelectionModel().deselect(rowIndex);
                //此时让光标选中上一行
                Ext.getCmp("_g_depotIn").getSelectionModel().select(rowIndex-1, true);
                //组件被显示后触发。
                Ext.getCmp("_f_depotIn_u").form.setValues(s.data);
            }
            //下一页
            function _f_next_data(){
                var count=Ext.getCmp("_g_depotIn").getStore().getCount();
                var me=Ext.getCmp("_g_depotIn").getSelectionModel().getSelection();
                //var record = AOS.selectone(_g_depotIn);
                //得到执行行的坐标
                var rowIndex = Ext.getCmp("_g_depotIn").getStore().indexOf(me[0]);
                if(rowIndex==count-1){
                    AOS.err("当前最后一条!");
                    return;
                }
                var s=Ext.getCmp("_g_depotIn").getStore().getAt(rowIndex+1);
                //原先行取消选中
                Ext.getCmp("_g_depotIn").getSelectionModel().deselect(rowIndex);
                //此时让光标选中下一行
                Ext.getCmp("_g_depotIn").getSelectionModel().select(rowIndex+1, true);
                //组件被显示后触发。
                Ext.getCmp("_f_depotIn_u").form.setValues(s.data);
            }
        </script>
    </aos:onready>

</aos:html>

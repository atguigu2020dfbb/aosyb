<%@page contentType="text/html; charset=UTF-8" %>
<%@taglib uri="/WEB-INF/tld/aos.tld" prefix="aos" %>
<%@ taglib prefix="aso" uri="http://www.osworks.cn/tag/aos" %>
<aos:html>
    <aos:head>
        <aos:include lib="ext"/>
        <aos:base href="properties/sxjc"/>
    </aos:head>
    <aos:body>
    </aos:body>
    <aos:onready>
        <aos:viewport layout="fit">
            <aos:hiddenfield id="lx" value="${lx}"/>
            <aos:gridpanel id="_g_depotIn" url="listSxjc.jhtml" onrender="_g_depotin_query">
                <aos:docked>
                    <aos:dockeditem xtype="tbtext" text="四性检测列表"/>
                    <aos:dockeditem text="新增" id="add" icon="add.png" onclick="_w_depotIn_show"/>
                    <aos:dockeditem text="修改" id="edit" icon="edit.png" onclick="_w_depotIn_u_show"/>
                    <aos:dockeditem text="删除" icon="del.png" onclick="_g_depotIn_del"/>
                </aos:docked>
                <aos:column header="流水号" dataIndex="id_" hidden="true"/>
                <aos:column header="编号" dataIndex="bh" />
                <aos:column header="描述" dataIndex="name"  width="400"/>
                <aso:column header="类型" dataIndex="lx"/>
                <aso:column header="是否启用" dataIndex="flag" hidden="true"/>
                <aos:column header="" flex="1" />
            </aos:gridpanel>
        </aos:viewport>
        <aos:window id="_w_depotIn" title="新增" onshow="_w_location_onshow">
            <aos:formpanel id="_f_depotIn" width="700" layout="column">
                <aos:textfield name="bh" fieldLabel="编号" columnWidth="0.99"/>
                <aos:combobox name="lx" fieldLabel="类型" columnWidth="0.99" allowBlank="false">
                    <aos:option value="准确性检测" display="准确性检测" />
                    <aos:option value="可用性检测" display="可用性检测" />
                    <aos:option value="安全性检测" display="安全性检测" />
                    <aos:option value="完整性检测" display="完整性检测" />
                </aos:combobox>
                <aos:textareafield name="name" fieldLabel="描述" columnWidth="0.99"/>
                <aos:docked  dock="bottom">
                    <aos:dockeditem xtype="tbfill"/>
                    <aos:dockeditem text="新增" icon="add.png" onclick="_f_depotin_save"/>
                    <aos:dockeditem text="关闭" icon="close.png" onclick="#_w_depotIn.hide()"/>
                </aos:docked>
            </aos:formpanel>
        </aos:window>
        <aos:window id="_w_depotIn_u" title="修改" >
            <aos:formpanel id="_f_depotIn_u" width="700" layout="column" >
                <aos:hiddenfield name="id_" fieldLabel="流水号"/>
                <aos:textfield name="bh" fieldLabel="编号" columnWidth="0.99"/>
                <aos:combobox name="lx" fieldLabel="类型" columnWidth="0.99" allowBlank="false">
                    <aos:option value="准确性检测" display="准确性检测" />
                    <aos:option value="可用性检测" display="可用性检测" />
                    <aos:option value="安全性检测" display="安全性检测" />
                    <aos:option value="完整性检测" display="完整性检测" />
                </aos:combobox>
                <aos:textareafield name="name" fieldLabel="描述" columnWidth="0.99"/>
                <aos:docked  dock="bottom">
                    <aos:dockeditem xtype="tbfill"/>
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

                AOS.ajax({
                    forms:_f_depotIn,
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
                AOS.reset(_f_depotIn);
            }
            function _f_depotout_u_save(){
                AOS.ajax({
                    forms:_f_depotIn_u,
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
        </script>
    </aos:onready>

</aos:html>

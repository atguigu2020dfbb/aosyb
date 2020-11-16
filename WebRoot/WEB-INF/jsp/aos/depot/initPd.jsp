<%@page contentType="text/html; charset=utf-8" %>
<%@taglib prefix="aos" uri="/WEB-INF/tld/aos.tld" %>
<aos:html>
    <aos:head>
        <aos:include lib="ext"/>
        <aos:base href="depot/safe"/>
    </aos:head>
    <aos:body>
    </aos:body>
    <aos:onready>
        <aos:viewport layout="fit">
            <aos:hiddenfield id="lx" value="${lx}"/>
            <aos:gridpanel id="_g_pd" onrender="_g_pd_query" url="listPd.jhtml">
                <aos:docked>
                    <aos:hiddenfield name="pdr_cn" id="pdr_cn" value="${pdr_cn}"/>
                    <aos:hiddenfield name="pdr_en" id="pdr_en" value="${pdr_en}"/>
                    <aos:dockeditem text="新增" icon="add.png" onclick="_w_pd_show"/>
                    <aos:dockeditem text="修改" icon="edit.png" onclick="_w_pd_u_show"/>
                    <aos:dockeditem text="删除" icon="del.png" onclick="_g_pd_del"/>
                </aos:docked>
                <aos:column header="id_" dataIndex="id_" hidden="true" locked="true"/>
                <aos:column header="流水号" dataIndex="lsh" width="100" />
                <aos:column header="盘点年度" dataIndex="pdnd" width="100"/>
                <aos:column header="全宗号" dataIndex="pdqz" width="100"/>
                <aos:column header="全宗单位" dataIndex="qzdw" width="150"/>
                <aos:column header="盘点数量" dataIndex="pdsl" width="100"/>
                <aos:column header="盘点区间" dataIndex="pdqj" width="100"/>
                <aos:column header="有无异常" dataIndex="ywyc" width="100"/>
                <aos:column header="问题描述" dataIndex="wtms" width="100"/>
                <aos:column header="盘点人" dataIndex="pdr_cn" width="100"/>
                <aos:column header="盘点人" dataIndex="pdr_en" hidden="true" width="100"/>
                <aos:column header="盘点时间" dataIndex="pdsj" width="100"/>
            </aos:gridpanel>
        </aos:viewport>
        <aos:window id="_w_pd" title="新增"  onshow="_w_pd_onshow">
            <aos:formpanel id="_f_pd" layout="column" width="700">
                <aos:textfield name="lsh" fieldLabel="流水号" columnWidth="0.49"/>
                <aos:textfield name="pdnd" fieldLabel="盘点年度"  columnWidth="0.49"/>
                <aos:textfield name="pdqz" fieldLabel="全宗号"  columnWidth="0.49"/>
                <aos:textfield name="qzdw" fieldLabel="全宗单位"  columnWidth="0.49"/>
                <aos:textfield name="pdqj" fieldLabel="盘点区间"  columnWidth="0.49"/>
                <aos:textfield name="pdsl" fieldLabel="盘点数量"  columnWidth="0.49"/>
                <aos:combobox name="ywyc" fieldLabel="有无异常" value="无"  columnWidth="0.49">
                    <aos:option value="有" display="有"/>
                    <aos:option value="无" display="无"/>
                </aos:combobox>
                <aos:textfield name="wtms" fieldLabel="问题描述"  columnWidth="0.49"/>
                <aos:textfield name="pdr_cn" fieldLabel="盘点人"  columnWidth="0.49"/>
                <aos:hiddenfield name="pdr_en"  fieldLabel="盘点人"  columnWidth="0.49"/>
                <aos:datefield name="pdsj" fieldLabel="盘点时间" columnWidth="0.49"/>
                <aos:textfield name="bz" fieldLabel="备注"  columnWidth="0.98"/>
                <aos:hiddenfield id="lx" name="lx" value="${lx}"/>
                <aos:docked dock="bottom">
                    <aos:dockeditem xtype="tbfill"/>
                    <aos:dockeditem text="新增" icon="add.png" onclick="_f_add_depotPd"/>
                    <aos:dockeditem text="保存" icon="save.png" onclick="_f_pd_save"/>
                    <aos:dockeditem text="关闭" icon="close.png" onclick="#_w_pd.hide()"/>
                </aos:docked>
            </aos:formpanel>
        </aos:window>
        <aos:window id="_w_pd_u" title="修改">
            <aos:formpanel id="_f_pd_u" layout="column" width="700">
                <aos:hiddenfield name="id_" fieldLabel="流水号" />
                <aos:textfield name="lsh" fieldLabel="流水号" columnWidth="0.49"/>
                <aos:textfield name="pdnd" fieldLabel="盘点年度"  columnWidth="0.49"/>
                <aos:textfield name="pdqz" fieldLabel="全宗号"  columnWidth="0.49"/>
                <aos:textfield name="qzdw" fieldLabel="全宗单位"  columnWidth="0.49"/>
                <aos:textfield name="pdqj" fieldLabel="盘点区间"  columnWidth="0.49"/>
                <aos:textfield name="pdsl" fieldLabel="盘点数量"  columnWidth="0.49"/>
                <aos:combobox name="ywyc" fieldLabel="有无异常" value="无"  columnWidth="0.49">
                    <aos:option value="有" display="有"/>
                    <aos:option value="无" display="无"/>
                </aos:combobox>
                <aos:textfield name="wtms" fieldLabel="问题描述"  columnWidth="0.49"/>
                <aos:textfield name="pdr_cn" fieldLabel="盘点人"  columnWidth="0.49"/>
                <aos:hiddenfield name="pdr_en"  fieldLabel="盘点人"  columnWidth="0.49"/>
                <aos:datefield name="pdsj" fieldLabel="盘点时间" columnWidth="0.49"/>
                <aos:textfield name="bz" fieldLabel="备注"  columnWidth="0.98"/>
                <aos:hiddenfield id="lx" name="lx" value="${lx}"/>
                <aos:docked dock="bottom">
                    <aos:dockeditem xtype="tbfill"/>

                    <aos:dockeditem text="上一条" icon="more/go-previous.png"  onclick="_f_previous_data"/><%----%>
                    <aos:dockeditem text="下一条" icon="more/go-next.png" onclick="_f_next_data"/><%----%>

                    <aos:dockeditem text="保存" icon="save.png" onclick="_f_pd_u_save"/>

                    <aos:dockeditem text="关闭" icon="close.png" onclick="#_w_pd_u.hide()"/>
                </aos:docked>
            </aos:formpanel>
        </aos:window>
        <script type="text/javascript">
            function _g_pd_query(){
                var params={
                    lx:lx.getValue()
                };
                _g_pd_store.getProxy().extraParams=params;
                _g_pd_store.load();
            }

            function _w_pd_show(){
                _w_pd.show();
            }

            function _f_pd_save(){
                AOS.ajax({
                    forms:_f_pd,
                    url:'savePd.jhtml',
                    ok:function(data){
                        if(data.appcode === 1){
                            //_w_pd.hide();
                            _g_pd_store.reload();
                            AOS.tip(data.appmsg);
                        }else {
                            AOS.err(data.appmsg);
                        }
                    }
                })
            }
            function _w_pd_u_show(){
                var record = AOS.selectone(_g_pd);
                if(record){
                    _w_pd_u.show();
                    _f_pd_u.loadRecord(record);
                }
            }
            function _f_pd_u_save(){
                AOS.ajax({
                    forms:_f_pd_u,
                    url:'updatePd.jhtml',
                    ok:function(data){
                        if(data.appcode===1){
                            _w_pd_u.hide();
                            _g_pd_store.reload();
                            AOS.tip(data.appmsg);
                        }else {
                            AOS.err(AOS.appmsg);
                        }
                    }
                })
            }
            function _g_pd_del(){
                var selection = AOS.selection(_g_pd,'id_');
                if(AOS.empty(selection)){
                    AOS.tip("删除前请选中数据。。。");
                    return;
                }
                var rows = AOS.rows(_g_pd);
                var msg = AOS.merge('确定要删除选中的[{0}]条数据吗？',rows);
                AOS.confirm(msg,function(btn){
                    if(btn==='cancel'){
                        AOS.tip('删除被操作取消');
                        return;
                    }
                    AOS.ajax({
                        url:'deletePd.jhtml',
                        params:{
                            aos_rows_: selection
                        },
                        ok:function(data){
                            AOS.tip(data.appmsg);
                            _g_pd_store.reload();
                        }
                    })
                })
            }
            function _f_add_depotPd(){
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
                        name_:"盘点流水号",
                        lx:lx,
                        rkdh:_f_pd.form.findField("lsh").getValue()
                    },
                    url:'getDepotPdIndex_nd.jhtml',
                    ok:function(data){
                        //设计一个随机数编号
                        //年
                        var time = (new Date).getTime();
                        var yesday = _f_pd.form.findField("lsh").getValue().substring(3,7);
                        _f_pd.form.findField("lsh").setValue(lx+yesday+data.index);
                    }
                });
            }
            function _w_pd_onshow(){

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
                    params:{name_:"出库流水号",lx:lx},
                    url:'getDepotPdIndex.jhtml',
                    ok:function(data){
                        //设计一个随机数编号
                        //年
                        var time = (new Date).getTime();
                        var yesday = new Date(time); // 获取的是年
                        yesday = yesday.getFullYear()  +"";
                        _f_pd.form.findField("lsh").setValue(lx+yesday+data.index);
                        _f_pd.form.findField("pdnd").setValue(yesday);
                        _f_pd.form.findField("pdr_cn").setValue(pdr_cn.getValue());
                        _f_pd.form.findField("pdr_en").setValue(pdr_en.getValue());
                        var time = (new Date).getTime();
                        var yesday = new Date(time); // 获取的是前一天日期
                        yesday = yesday.getFullYear()  +"-"+(yesday.getMonth()> 9 ? (yesday.getMonth() + 1) : "0" + (yesday.getMonth() + 1))+
                            "-"+(yesday.getDate()> 9 ? (yesday.getDate()) : "0" + yesday.getDate());
                        _f_pd.form.findField("pdsj").setValue(yesday);
                    }
                });
            }




            //上一页
            function _f_previous_data(){
                var count=Ext.getCmp("_g_pd").getStore().getCount();
                var me=Ext.getCmp("_g_pd").getSelectionModel().getSelection();
                //var record = AOS.selectone(_g_pd);
                //得到执行行的坐标
                var rowIndex = Ext.getCmp("_g_pd").getStore().indexOf(me[0]);
                if(rowIndex==0){
                    AOS.err("当前第一条!");
                    return;
                }
                var s=Ext.getCmp("_g_pd").getStore().getAt(rowIndex-1);
                //原先行取消选中
                Ext.getCmp("_g_pd").getSelectionModel().deselect(rowIndex);
                //此时让光标选中上一行
                Ext.getCmp("_g_pd").getSelectionModel().select(rowIndex-1, true);
                //组件被显示后触发。
                Ext.getCmp("_f_pd_u").form.setValues(s.data);
            }
            //下一页
            function _f_next_data(){
                var count=Ext.getCmp("_g_pd").getStore().getCount();
                var me=Ext.getCmp("_g_pd").getSelectionModel().getSelection();
                //var record = AOS.selectone(_g_pd);
                //得到执行行的坐标
                var rowIndex = Ext.getCmp("_g_pd").getStore().indexOf(me[0]);
                if(rowIndex==count-1){
                    AOS.err("当前最后一条!");
                    return;
                }
                var s=Ext.getCmp("_g_pd").getStore().getAt(rowIndex+1);
                //原先行取消选中
                Ext.getCmp("_g_pd").getSelectionModel().deselect(rowIndex);
                //此时让光标选中下一行
                Ext.getCmp("_g_pd").getSelectionModel().select(rowIndex+1, true);
                //组件被显示后触发。
                Ext.getCmp("_f_pd_u").form.setValues(s.data);
            }
        </script>
    </aos:onready>
</aos:html>
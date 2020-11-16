<%@page contentType="text/html; charset=utf-8" %>
<%@taglib prefix="aos" uri="/WEB-INF/tld/aos.tld" %>
<aos:html>
    <aos:head title="查档预约">
        <aos:include lib="ext"/>
        <aos:base href="utilization"/>
    </aos:head>
    <aos:body>
    </aos:body>
    <aos:onready>
        <aos:viewport layout="fit">
            <aos:gridpanel id="_g_advance" url="listAdvance.jhtml" onrender="_g_advance_onrender">
                <aos:docked>
                    <aos:dockeditem xtype="tbtext" text="查档预约"/>
                    <aos:dockeditem text="新增" id="add" icon="add.png" onclick="_w_advance_show"/>
                    <aos:dockeditem text="修改" icon="edit.png" onclick="_w_advance_u_show"/>
                    <aos:dockeditem text="删除" icon="del.png" onclick="_g_advance_del"/>
                    <aos:dockeditem text="数据检索" icon="icon71.png" onclick="_g_advance_read"/>
                    <aos:dockeditem text="转接待查档" icon="more/transform-move.png" onclick="_g_advance_move"/>
                </aos:docked>
                <aos:selmodel type="checkbox" mode="multi" />
                <aos:column type="rowno" />
                <aos:column header="id_" dataIndex="id_" hidden="true"  locked="true"/>
                <aos:column header="预约编号" dataIndex="yybh" />
                <aos:column header="预约单位" dataIndex="yydw"/>
                <aos:column header="预约者姓名" dataIndex="yyr"/>
                <aos:column header="身份证号" dataIndex="sfzh"/>
                <aos:column header="预约方式" dataIndex="yyfs"/>
                <aos:column header="查阅目的" dataIndex="cdmd"/>
                <aos:column header="查阅内容" dataIndex="cdnr"/>
                <aos:column header="接待人" dataIndex="jdr"/>
                <aos:column header="预约时间" dataIndex="yysj"/>
                <aos:column header="备注" dataIndex="bz"/>
                <aos:column header="状态" dataIndex="flag"/>
                <aos:column header="" flex="1"/>
            </aos:gridpanel>
        </aos:viewport>
        <aos:window id="_w_advance" title="新增" onshow="load_advance">
            <aos:formpanel id="_f_advance" width="500" layout="column">
                <aos:rowset><aos:textfield fieldLabel="预约编号" name="yybh" readOnly="true" columnWidth="0.49"/></aos:rowset>

                <aos:textfield fieldLabel="预约单位" name="yydw" columnWidth="0.49"/>
                <aos:textfield name="yyr" fieldLabel="预约者姓名" columnWidth="0.49"/>
                <aos:textfield name="sfzh" fieldLabel="身份证号" columnWidth="0.98"/>
                <aos:combobox name="yyfs" fieldLabel="预约方式" value="电话预约" columnWidth="0.98">
                    <aos:option value="电话预约" display="电话预约"></aos:option>
                    <aos:option value="网站预约" display="网站预约"></aos:option>
                    <aos:option value="微信预约" display="微信预约"></aos:option>
                </aos:combobox>
                <aos:combobox name="cdmd" fieldLabel="查阅目的" dicField="lymd" columnWidth="0.98"/>
                <aos:textfield name="cdnr" fieldLabel="查阅内容" columnWidth="0.98"/>
                <aos:textfield name="jdr" fieldLabel="接待人" columnWidth="0.49" value="${user}"/>
                <aos:datefield name="yysj" fieldLabel="预约时间"  columnWidth="0.49"/>
                <aos:textfield name="bz" fieldLabel="备注"  columnWidth="0.98"/>
                <aos:docked dock="bottom" ui="footer">
                    <aos:dockeditem xtype="tbfill"/>
                    <aos:dockeditem text="保存" icon="save.png" onclick="_f_advance_save"/>
                    <aos:dockeditem text="关闭" icon="close.png" onclick="#_w_advance.hide()"/>
                </aos:docked>
            </aos:formpanel>
        </aos:window>

        <aos:window id="_w_advance_u" title="修改">
            <aos:formpanel id="_f_advance_u" width="500" layout="column">
                <aos:hiddenfield name="id_" fieldLabel="id_" />
                <aos:rowset><aos:textfield fieldLabel="预约编号" name="yybh" readOnly="true" columnWidth="0.49"/></aos:rowset>

                <aos:textfield fieldLabel="预约单位" name="yydw" columnWidth="0.49"/>
                <aos:textfield name="yyr" fieldLabel="预约者姓名" columnWidth="0.49"/>
                <aos:textfield name="sfzh" fieldLabel="身份证号" columnWidth="0.98"/>
                <aos:combobox name="yyfs" fieldLabel="预约方式"  columnWidth="0.98">
                    <aos:option value="电话预约" display="电话预约"></aos:option>
                    <aos:option value="网站预约" display="网站预约"></aos:option>
                    <aos:option value="微信预约" display="微信预约"></aos:option>
                </aos:combobox>
                <aos:combobox name="cdmd" fieldLabel="查阅目的" dicField="lymd" columnWidth="0.98"/>
                <aos:textfield name="cdnr" fieldLabel="查阅内容" columnWidth="0.98"/>
                <aos:textfield name="jdr" fieldLabel="接待人" columnWidth="0.49" value="${user}"/>
                <aos:datefield name="yysj" fieldLabel="预约时间"  columnWidth="0.49"/>
                <aos:textfield name="bz" fieldLabel="备注"  columnWidth="0.98"/>
                <aos:docked dock="bottom" ui="footer">
                    <aos:dockeditem xtype="tbfill"/>

                    <aos:dockeditem text="上一条" icon="more/go-previous.png"  onclick="_f_previous_data"/><%----%>
                    <aos:dockeditem text="下一条" icon="more/go-next.png" onclick="_f_next_data"/><%----%>

                    <aos:dockeditem text="保存" icon="save.png" onclick="_f_advance_u_save"/>
                    <aos:dockeditem text="关闭" icon="close.png" onclick="#_w_advance_u.hide()"/>
                </aos:docked>
            </aos:formpanel>
        </aos:window>
        <script type="text/javascript">
            function _g_advance_onrender(){
                _g_advance_store.load();
            }
            function _w_advance_show(){
                _w_advance.show();
            }
            function _w_advance_u_show(){
                var record = AOS.selectone(_g_advance);
                if(record){
                    _w_advance_u.show();
                    _f_advance_u.loadRecord(record);
                }
            }
            function _g_advance_move(){
                var count=AOS.rows(_g_advance);
                if(count<=0){
                    AOS.tip("请选择需要接待的数据");
                    return;
                }
                var ids="";
                var row=_g_advance.getSelectionModel().getSelection();
                /*for(var i=0;i<AOS.rows(_g_advance);i++){
                    if(i==0){
                        ids=row[i].data.id_;
                    }else{
                        ids+=","+row[i].data.id_;
                    }
                }*/
                var params = {
                    aos_rows_:AOS.selection(_g_advance, 'id_')
                };
                AOS.ajax({
                    url: 'yyTojd.jhtml',
                    params:params,
                    ok: function (data) {
                        if(data.appcode===1){
                            AOS.tip("操作成功!");
                            _g_advance_store.load();
                            //_g_receive_query();
                        }else if(data.appcode===-1){
                            AOS.tip("操作失败!");
                        }
                    }
                });

            }
            function times(){
                //得到当前年月日
                var date = new Date();
                var year=date .getFullYear(); //获取完整的年份(4位)
                var month=date .getMonth(); //获取当前月份(0-11,0代表1月)
                if(month<10){
                    month='0'+(month+1);
                }else{
                    month='0'+(month+1);
                }
                var day=date .getDate(); //获取当前日(1-31)
                var times=year+month+day;
                return times;
            }
            function load_advance(){
                //根据tree名称得到对应的部门名称
                var nd = new Date().getFullYear();
                AOS.ajax({
                    params:{name_:'预约流水号',nd:"YY"+nd},
                    url:'getAdvanceIndex.jhtml',
                    ok:function(data){
                        //设计一个随机数编号
                        var date=new Date();
                        _f_advance.form.findField("yybh").setValue("YY"+nd+data.index);
                    }
                });
            }
            function _f_advance_save(){
                AOS.ajax({
                    forms:_f_advance,
                    url:'saveAdvance.jhtml',
                    ok:function (data){
                        if(data.appcode == -1){
                            AOS.err(data.appmsg);
                        }else {
                            _w_advance.hide();
                            _g_advance_store.reload();
                            AOS.tip(data.appmsg);
                        }
                    }
                })
            }
            function _f_advance_u_save(){
                AOS.ajax({
                    forms:_f_advance_u,
                    url:'updateAdvance.jhtml',
                    ok:function(data){
                        if(data.appcode===1){
                            _w_advance_u.hide();
                            _g_advance_store.reload();
                            AOS.tip(data.appmsg);
                        }else {
                            AOS.err(AOS.appmsg);
                        }
                    }
                })
            }
            function _g_advance_del(){
                var selection = AOS.selection(_g_advance,'id_');
                if(AOS.empty(selection)){
                    AOS.tip("删除前请选中数据。。。");
                    return;
                }
                var rows = AOS.rows(_g_advance);
                var msg = AOS.merge('确定要删除选中的[{0}]条数据吗？',rows);
                AOS.confirm(msg,function(btn){
                    if(btn==='cancel'){
                        AOS.tip('删除被操作取消');
                        return;
                    }
                    AOS.ajax({
                        url:'deleteAdvance.jhtml',
                        params:{
                            aos_rows_: selection
                        },
                        ok:function(data){
                            AOS.tip(data.appmsg);
                            _g_advance_store.reload();
                        }
                    })
                })
            }
            function fn_lyzt_render(value, metaData, record, rowIndex, colIndex,
                                    store) {
                if (value === '1') {
                    return '已查到';
                } else {
                    return '未查到';
                }
            }

            function fn_spzt_render(value, metaData, record, rowIndex, colIndex,
                                    store){
                if (value === '0') {
                    return '未通过';
                } if(value === '1'){
                    return "已通过";
                }if(value === '2'){
                    return "待审核";
                }
                else {
                    return ' ';
                }
            }
            function fn_ckzt_render(value, metaData, record, rowIndex, colIndex,
                                    store){
                if (value === '0') {
                    return '出库中';
                } if(value === '1'){
                    return "已出库";
                }if(value === '2'){
                    return "已入库";
                }else {
                    return ' ';
                }
            }



            function _g_advance_read(){
                //var record = AOS.selectone(_g_advance);
                //console.log(record);
                //parent.fnaddtab('','数据检索','/utilization/getData.jhtml');
                parent.fnaddtab('','数据检索','/make/loadData.jhtml?djbh=12&recordid=1&bj=1');
            }

            //上一页
            function _f_previous_data(){
                var count=Ext.getCmp("_g_advance").getStore().getCount();
                var me=Ext.getCmp("_g_advance").getSelectionModel().getSelection();
                //var record = AOS.selectone(_g_advance);
                //得到执行行的坐标
                var rowIndex = Ext.getCmp("_g_advance").getStore().indexOf(me[0]);
                if(rowIndex==0){
                    AOS.err("当前第一条!");
                    return;
                }
                var s=Ext.getCmp("_g_advance").getStore().getAt(rowIndex-1);
                //原先行取消选中
                Ext.getCmp("_g_advance").getSelectionModel().deselect(rowIndex);
                //此时让光标选中上一行
                Ext.getCmp("_g_advance").getSelectionModel().select(rowIndex-1, true);
                //组件被显示后触发。
                Ext.getCmp("_f_advance_u").form.setValues(s.data);
            }
            //下一页
            function _f_next_data(){
                var count=Ext.getCmp("_g_advance").getStore().getCount();
                var me=Ext.getCmp("_g_advance").getSelectionModel().getSelection();
                //var record = AOS.selectone(_g_advance);
                //得到执行行的坐标
                var rowIndex = Ext.getCmp("_g_advance").getStore().indexOf(me[0]);
                if(rowIndex==count-1){
                    AOS.err("当前最后一条!");
                    return;
                }
                var s=Ext.getCmp("_g_advance").getStore().getAt(rowIndex+1);
                //原先行取消选中
                Ext.getCmp("_g_advance").getSelectionModel().deselect(rowIndex);
                //此时让光标选中下一行
                Ext.getCmp("_g_advance").getSelectionModel().select(rowIndex+1, true);
                //组件被显示后触发。
                Ext.getCmp("_f_advance_u").form.setValues(s.data);
            }
        </script>
    </aos:onready>
</aos:html>
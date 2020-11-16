<%@page contentType="text/html; charset=utf-8" %>
<%@taglib prefix="aos" uri="/WEB-INF/tld/aos.tld" %>
<aos:html>
    <aos:head title="人员管理">
        <aos:include lib="ext"/>
        <aos:base href="depot/personnel"/>
    </aos:head>
    <aos:body>
        
    </aos:body>
    <aos:onready>
        <aos:viewport layout="fit">
            <aos:gridpanel id="_g_personnel" pageSize="20" onrender="_g_personnel_query" url="listPersonnel.jhtml">
                <aos:docked>
                    <aos:hiddenfield  name="xm" id="xm" value="${xm}"/>
                    <aos:hiddenfield  name="lx" id="lx" value="${lx}"/>
                    <aos:dockeditem text="新增" icon="add.png" onclick="_w_personnel_show"/>
                    <aos:dockeditem text="修改" icon="edit.png" onclick="_w_personnel_u_show"/>
                    <aos:dockeditem text="删除" icon="del.png" onclick="_g_personnel_del"/>
                    <aos:dockeditem text="查询" icon="query.png" onclick="_w_select_query_show" />
                    <aos:dockeditem onclick="_w_charts_export" text="导出excel" icon="icon70.png" />
                    <aos:dockeditem onclick="_w_import_show" text="导入excel" icon="folder8.png" />
                </aos:docked>
                <aos:column header="id_" dataIndex="id_" hidden="true" locked="true"/>
                <aos:column hidden="true" header="f_recid" dataIndex="f_recid"/>
                <aos:column hidden="true" header="f_eventtype" dataIndex="f_eventtype"/>
                <aos:column header="管理年度" dataIndex="glnd" width="100"/>
                <aos:column header="出入时间" dataIndex="f_logdatetime" width="150"/>
                <aos:column header="卡号" dataIndex="card"  width="100"/>
                <aos:column header="人员姓名" dataIndex="xm" width="100"/>
                <aos:column header="所属部门" dataIndex="bm" width="150"/>
                <aos:column header="地点" dataIndex="dd" width="150"/>
                <aos:column header="出入库原因" dataIndex="zt" width="150"/>
                <aos:column header="门禁信息" dataIndex="f_eventdesc" width="250"/>
                <aos:column header="流水号" dataIndex="lsh" />
                <aos:column header="备注" dataIndex="bz" />
                <aos:column header="用户id" dataIndex="f_userid" width="80"/>
            </aos:gridpanel>
        </aos:viewport>
        <aos:window id="_w_personnel" title="新增" onshow="_new_task">
            <aos:formpanel id="_f_personnel" layout="column" width="800">
                <aos:textfield fieldLabel="流水号" name="lsh" columnWidth="0.49"/>
                <aos:textfield fieldLabel="管理年度" name="glnd" columnWidth="0.49"/>
                <aos:textfield fieldLabel="库管人员" name="xm" columnWidth="0.49"/>
                <aos:textfield fieldLabel="其他人员" name="ssbm" columnWidth="0.49"/>
                <aos:checkboxgroup fieldLabel="出入库原因" id="zt_add" columns="[80,80,80,80,80,80,80,80,80]" columnWidth="0.98">
                    <aos:checkbox name="r4" boxLabel="调卷" />
                    <aos:checkbox name="r4" boxLabel="接收" />
                    <aos:checkbox name="r4" boxLabel="清点" />
                    <aos:checkbox name="r4" boxLabel="安检" />
                    <aos:checkbox name="r4" boxLabel="虫霉检" />
                    <aos:checkbox name="r4" boxLabel="通风" />
                    <aos:checkbox name="r4" boxLabel="清扫" />
                    <aos:checkbox name="r4" boxLabel="参观" />
                    <aos:checkbox name="r4" boxLabel="进门" />
                </aos:checkboxgroup>
                <aos:datefield fieldLabel="出入时间" name="f_logdatetime" columnWidth="0.49"/>
                <aos:textfield fieldLabel="备注" name="bz" columnWidth="0.49"/>
                <aos:hiddenfield  name="lx" id="lx" value="${lx}"/>
                <aos:docked dock="bottom">
                    <aos:dockeditem xtype="tbfill"/>
                    <aos:dockeditem text="新增" icon="add.png" onclick="_f_add_person"/>
                    <aos:dockeditem text="保存" icon="save.png" onclick="_f_personnel_save"/>
                    <aos:dockeditem text="关闭" icon="close.png" onclick="#_w_personnel.hide()"/>
                </aos:docked>
            </aos:formpanel>
        </aos:window>
        <aos:window id="_w_personnel_u" title="修改"  >
            <aos:formpanel id="_f_personnel_u" layout="column" width="800">
                <aos:hiddenfield name="id_" fieldLabel="流水号" />
                <aos:textfield fieldLabel="流水号" name="lsh" columnWidth="0.49"/>
                <aos:textfield fieldLabel="管理年度" name="glnd" columnWidth="0.49"/>
                <aos:textfield fieldLabel="库管人员" name="xm" columnWidth="0.49"/>
                <aos:textfield fieldLabel="其他人员" name="ssbm" columnWidth="0.49"/>
                <aos:checkboxgroup fieldLabel="出入库原因" id="zt_update" columns="[80,80,80,80,80,80,80,80,80]" columnWidth="0.98">
                    <aos:checkbox name="r4" boxLabel="调卷" />
                    <aos:checkbox name="r4" boxLabel="接收" />
                    <aos:checkbox name="r4" boxLabel="清点" />
                    <aos:checkbox name="r4" boxLabel="安检" />
                    <aos:checkbox name="r4" boxLabel="虫霉检" />
                    <aos:checkbox name="r4" boxLabel="通风" />
                    <aos:checkbox name="r4" boxLabel="清扫" />
                    <aos:checkbox name="r4" boxLabel="参观" />
                    <aos:checkbox name="r4" boxLabel="进门" />
                </aos:checkboxgroup>
                <aos:textfield fieldLabel="出入时间" name="f_logdatetime" columnWidth="0.49" />
                <aos:textfield fieldLabel="备注" name="bz" columnWidth="0.49"/>
                <aos:hiddenfield  name="lx" id="lx" value="${lx}"/>
                <aos:docked dock="bottom">
                    <aos:dockeditem xtype="tbfill"/>
                    <aos:dockeditem text="保存" icon="save.png" onclick="_f_personnel_u_save"/>
                    <aos:dockeditem text="关闭" icon="close.png" onclick="#_w_personnel_u.hide()"/>
                </aos:docked>
            </aos:formpanel>
        </aos:window>
        <aos:window id="_w_query_select" title="查询" width="720" autoScroll="true"
                    layout="fit" >
            <aos:tabpanel id="_tabpanel_edit_select" region="center" activeTab="0"
                          bodyBorder="0 0 0 0" tabBarHeight="30">
                <aos:tab title="列表式搜索" id="_tab_select_edit_org">
                    <aos:formpanel id="_f_select_query" layout="column" columnWidth="1">
                        <aos:hiddenfield name="columnnum"  value="7" />
                        <aos:combobox name="and1" value="on"
                                      labelWidth="10" columnWidth="0.2">
                            <aos:option value="on" display="并且" />
                            <aos:option value="up" display="或者" />
                        </aos:combobox>
                        <aos:combobox name="filedname1" id="filedname1" labelWidth="20" value="card" columnWidth="0.2">
                            <aos:option value="card" display="卡号" />
                            <aos:option value="xm" display="姓名" />
                            <aos:option value="bm" display="部门" />
                            <aos:option value="dd" display="地点" />
                            <aos:option value="f_logdatetime" display="出入时间" />
                        </aos:combobox>
                        <aos:combobox name="condition1" value="like"
                                      labelWidth="20" columnWidth="0.2">
                            <aos:option value="=" display="等于" />
                            <aos:option value=">" display="大于" />
                            <aos:option value=">=" display="大于等于" />
                            <aos:option value="<" display="小于" />
                            <aos:option value="<=" display="小于等于" />
                            <aos:option value="<>" display="不等于" />
                            <aos:option value="like" display="包含" />
                            <aos:option value="left" display="左包含" />
                            <aos:option value="right" display="右包含" />
                            <aos:option value="null" display="空值" />
                        </aos:combobox>
                        <aos:textfield name="content1"
                                       allowBlank="true" columnWidth="0.39" />
                        <aos:combobox name="and2" value="on"
                                      labelWidth="10" columnWidth="0.2">
                            <aos:option value="on" display="并且" />
                            <aos:option value="up" display="或者" />
                        </aos:combobox>
                        <aos:combobox name="filedname2" id="filedname2"  labelWidth="20" columnWidth="0.2"  value="xm">
                            <aos:option value="card" display="卡号" />
                            <aos:option value="xm" display="姓名" />
                            <aos:option value="bm" display="部门" />
                            <aos:option value="dd" display="地点" />
                            <aos:option value="f_logdatetime" display="出入时间" />
                        </aos:combobox>
                        <aos:combobox name="condition2" value="like"
                                      labelWidth="20" columnWidth="0.2">
                            <aos:option value="=" display="等于" />
                            <aos:option value=">" display="大于" />
                            <aos:option value=">=" display="大于等于" />
                            <aos:option value="<" display="小于" />
                            <aos:option value="<=" display="小于等于" />
                            <aos:option value="<>" display="不等于" />
                            <aos:option value="like" display="包含" />
                            <aos:option value="left" display="左包含" />
                            <aos:option value="right" display="右包含" />
                            <aos:option value="null" display="空值" />
                        </aos:combobox>
                        <aos:textfield name="content2"
                                       allowBlank="true" columnWidth="0.39" />
                        <aos:combobox name="and3" value="on"
                                      labelWidth="10" columnWidth="0.2">
                            <aos:option value="on" display="并且" />
                            <aos:option value="up" display="或者" />
                        </aos:combobox>
                        <aos:combobox name="filedname3" id="filedname3"  labelWidth="20" columnWidth="0.2"  value="bm">
                            <aos:option value="card" display="卡号" />
                            <aos:option value="xm" display="姓名" />
                            <aos:option value="bm" display="部门" />
                            <aos:option value="dd" display="地点" />
                            <aos:option value="f_logdatetime" display="出入时间" />
                        </aos:combobox>
                        <aos:combobox name="condition3" value="like"
                                      labelWidth="20" columnWidth="0.2">
                            <aos:option value="=" display="等于" />
                            <aos:option value=">" display="大于" />
                            <aos:option value=">=" display="大于等于" />
                            <aos:option value="<" display="小于" />
                            <aos:option value="<=" display="小于等于" />
                            <aos:option value="<>" display="不等于" />
                            <aos:option value="like" display="包含" />
                            <aos:option value="left" display="左包含" />
                            <aos:option value="right" display="右包含" />
                            <aos:option value="null" display="空值" />
                        </aos:combobox>
                        <aos:textfield name="content3"
                                       allowBlank="true" columnWidth="0.39" />
                        <aos:combobox name="and4" value="on"
                                      labelWidth="10" columnWidth="0.2">
                            <aos:option value="on" display="并且" />
                            <aos:option value="up" display="或者" />
                        </aos:combobox>
                        <aos:combobox name="filedname4" id="filedname4"   labelWidth="20" columnWidth="0.2"  value="dd" >
                            <aos:option value="card" display="卡号" />
                            <aos:option value="xm" display="姓名" />
                            <aos:option value="bm" display="部门" />
                            <aos:option value="dd" display="地点" />
                            <aos:option value="f_logdatetime" display="出入时间" />
                        </aos:combobox>

                        <aos:combobox name="condition4" value="like"
                                      labelWidth="20" columnWidth="0.2">
                            <aos:option value="=" display="等于" />
                            <aos:option value=">" display="大于" />
                            <aos:option value=">=" display="大于等于" />
                            <aos:option value="<" display="小于" />
                            <aos:option value="<=" display="小于等于" />
                            <aos:option value="<>" display="不等于" />
                            <aos:option value="like" display="包含" />
                            <aos:option value="left" display="左包含" />
                            <aos:option value="right" display="右包含" />
                            <aos:option value="null" display="空值" />
                        </aos:combobox>
                        <aos:textfield name="content4"
                                       allowBlank="true" columnWidth="0.39" />

                        <aos:combobox name="and5" value="on"
                                      labelWidth="10" columnWidth="0.2">
                            <aos:option value="on" display="并且" />
                            <aos:option value="up" display="或者" />
                        </aos:combobox>
                        <aos:combobox name="filedname5" id="filedname5"   labelWidth="20" columnWidth="0.2"  value="f_logdatetime">
                            <aos:option value="card" display="卡号" />
                            <aos:option value="xm" display="姓名" />
                            <aos:option value="bm" display="部门" />
                            <aos:option value="dd" display="地点" />
                            <aos:option value="f_logdatetime" display="出入时间" />
                        </aos:combobox>
                        <aos:combobox name="condition5" value="like"
                                      labelWidth="20" columnWidth="0.2">
                            <aos:option value="=" display="等于" />
                            <aos:option value=">" display="大于" />
                            <aos:option value=">=" display="大于等于" />
                            <aos:option value="<" display="小于" />
                            <aos:option value="<=" display="小于等于" />
                            <aos:option value="<>" display="不等于" />
                            <aos:option value="like" display="包含" />
                            <aos:option value="left" display="左包含" />
                            <aos:option value="right" display="右包含" />
                            <aos:option value="null" display="空值" />
                        </aos:combobox>
                        <aos:textfield name="content5"
                                       allowBlank="true" columnWidth="0.39" />
                        <aos:docked dock="bottom" ui="footer">
                            <aos:dockeditem xtype="tbfill" />
                            <aos:dockeditem onclick="_f_select_data_query" text="确定" icon="ok.png" />
                            <aos:dockeditem onclick="#_w_query_select.hide();" text="关闭"
                                            icon="close.png" />
                        </aos:docked>
                    </aos:formpanel>
                </aos:tab>
            </aos:tabpanel>
        </aos:window>

        <script type="text/javascript">
            //查询窗口展开
            function _w_select_query_show() {
                //判断是不是
                _w_query_select.show();
            }
            //生成XLS报表
            function _w_charts_export() {
                AOS.ajax({
                    url : 'fillReport_ry.jhtml',
                    ok : function(data) {
                        AOS.file('${cxt}/report/xls2.jhtml');
                    }
                });
            }
            //导入excel窗口
            function _w_import_show(){
                window.parent.fnaddtab('ry','人员出入库导入','/depot/personnel/initImport.jhtml?tablename=depot_ry');
            }
            function _g_personnel_query(){

                var params = {
                    lx : Ext.getCmp("lx").getValue()
                };
                //这个Store的命名规则为：表格ID+"_store"。
                _g_personnel_store.getProxy().extraParams = params;
                _g_personnel_store.load();
            }

            function _w_personnel_show(){
                _w_personnel.show();
            }
            function _f_add_person(){
                var zt="";
                var xqCheck = Ext.getCmp('zt_add').items;
                for(var i = 0; i < xqCheck.length; i++){
                    if(xqCheck.get(i).checked==true){
                        if(zt==""){
                            zt=xqCheck.get(i).boxLabel;
                        }else{
                            zt=zt+","+xqCheck.get(i).boxLabel;
                        }
                    }
                }
                var lx= Ext.getCmp("lx").getValue();
                //根据tree名称得到对应的部门名称
                AOS.ajax({
                    params:{
                        name_:"保管状况流水号",
                        lx:lx,
                        zt:zt,
                        lsh:_f_personnel.form.findField("lsh").getValue()
                    },
                    url:'getDepotRyIndex_nd.jhtml',
                    ok:function(data){
                        //设计一个随机数编号
                        //年
                        var time = (new Date).getTime();
                        var yesday = _f_personnel.form.findField("lsh").getValue().substring(3,7);
                        _f_personnel.form.findField("lsh").setValue(lx+yesday+data.index);
                    }
                });
            }
            function _new_task(){
                AOS.ajax({
                    params:{name_:"设备流水号",lx:lx.getValue()},
                    url:'getPersonIndex.jhtml',
                    ok:function(data){
                        //设计一个随机数编号
                        //年
                        var time = (new Date).getTime();
                        var yesday = new Date(time);
                        _f_personnel.form.findField("lsh").setValue(lx.getValue()+yesday.getFullYear()+data.index);
                        _f_personnel.form.findField("xm").setValue(Ext.getCmp("xm").getValue());
                        yesday = yesday.getFullYear()  +"-"+(yesday.getMonth()> 9 ? (yesday.getMonth() + 1) : "0" + (yesday.getMonth() + 1))+
                            "-"+(yesday.getDate()> 9 ? (yesday.getDate()) : "0" + yesday.getDate());
                        _f_personnel.form.findField("f_logdatetime").setValue(yesday);
                        _f_personnel.form.findField("glnd").setValue(new Date(time).getFullYear());

                    }
                });
            }
            function _f_personnel_save(){
                var zt="";
                var xqCheck = Ext.getCmp('zt_add').items;
                for(var i = 0; i < xqCheck.length; i++){
                    if(xqCheck.get(i).checked==true){
                        if(zt==""){
                            zt=xqCheck.get(i).boxLabel;
                        }else{
                            zt=zt+","+xqCheck.get(i).boxLabel;
                        }
                    }
                }
                AOS.ajax({
                    forms:_f_personnel,
                    params:{zt:zt},
                    url:'savePersonnel.jhtml',
                    ok:function(data){
                        if(data.appcode === 1){
                            //_w_personnel.hide();
                            _g_personnel_store.reload();
                            AOS.tip(data.appmsg);
                        }else {
                            AOS.err(data.appmsg);
                        }
                    }
                })
            }
            function _w_personnel_u_show(){
                var record = AOS.selectone(_g_personnel);
                if(record){
                    _w_personnel_u.show();
                    AOS.reset(_f_personnel_u);
                    _f_personnel_u.loadRecord(record);
                    var xqCheck = Ext.getCmp('zt_update').items;
                    for(var i = 0; i < xqCheck.length; i++){
                        if(record.data.zt.indexOf(xqCheck.get(i).boxLabel)!=-1){
                            xqCheck.get(i).setValue(true);
                        }
                    }
                }
            }
            function _f_personnel_u_save(){

                var zt="";
                var xqCheck = Ext.getCmp('zt_update').items;
                for(var i = 0; i < xqCheck.length; i++){
                    if(xqCheck.get(i).checked==true){
                        if(zt==""){
                            zt=xqCheck.get(i).boxLabel;
                        }else{
                            zt=zt+","+xqCheck.get(i).boxLabel;
                        }
                    }
                }
                AOS.ajax({
                    forms:_f_personnel_u,
                    params:{zt:zt},
                    url:'updatePersonnel.jhtml',
                    ok:function(data){
                        if(data.appcode===1){
                            _w_personnel_u.hide();
                            _g_personnel_store.reload();
                            AOS.tip(data.appmsg);
                        }else {
                            AOS.err(AOS.appmsg);
                        }
                    }
                })
            }
            function _g_personnel_del(){
                var selection = AOS.selection(_g_personnel,'id_');
                if(AOS.empty(selection)){
                    AOS.tip("删除前请选中数据。。。");
                    return;
                }
                var rows = AOS.rows(_g_personnel);
                var msg = AOS.merge('确定要删除选中的[{0}]条数据吗？',rows);
                AOS.confirm(msg,function(btn){
                    if(btn==='cancel'){
                        AOS.tip('删除被操作取消');
                        return;
                    }
                    AOS.ajax({
                        url:'deletePersonnel.jhtml',
                        params:{
                            aos_rows_: selection
                        },
                        ok:function(data){
                            AOS.tip(data.appmsg);
                            _g_personnel_store.reload();
                        }
                    })
                })
            }
            function _f_select_data_query(){
                var params = AOS.getValue('_f_select_query');
                var form = Ext.getCmp('_f_select_query');
                for(var i=1;i<=5;i++){
                    var str = form.down("[name='filedname"+i+"']");
                    var filedname =str.getValue();
                    if(filedname==null){
                        params["filedname"+i]=str.regexText;
                    }
                }
                _g_personnel_store.getProxy().extraParams = params;
                _g_personnel_store.load();
                _w_query_select.hide();
                AOS.reset(_f_select_query);
            }
        </script>
    </aos:onready>
</aos:html>
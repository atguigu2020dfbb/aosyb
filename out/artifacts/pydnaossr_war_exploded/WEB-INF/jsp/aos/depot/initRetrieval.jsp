<%@page contentType="text/html; charset=utf-8" %>
<%@taglib prefix="aos" uri="/WEB-INF/tld/aos.tld" %>
<aos:html>
    <aos:head>
        <aos:include lib="ext"/>
        <aos:base href="depot/retrieval"/>
    </aos:head>
    <aos:body>
    </aos:body>
    <aos:onready>
        <aos:viewport layout="fit">
            <aos:hiddenfield id="lx" value="${lx}"/>
            <aos:gridpanel id="_g_retrieval" onrender="_g_retrieval_query" url="listRetrieval.jhtml">
                <aos:docked>
                    <aos:dockeditem text="新增" icon="add.png" onclick="_w_retrieval_show"/>
                    <aos:dockeditem text="修改" icon="edit.png" onclick="_w_retrieval_u_show"/>
                    <aos:dockeditem text="删除" icon="del.png" onclick="_g_retrieval_del"/>
                    <aos:dockeditem onclick="_w_charts_export" text="导出excel" icon="icon70.png" />
                    <aos:dockeditem onclick="_w_import_show" text="导入excel" icon="folder8.png" />
                </aos:docked>
                <aos:column header="流水号" dataIndex="id_" hidden="true" locked="true"/>
                <aos:column header="库房编号" dataIndex="kfbh"/>
                <aos:column header="检查时间" dataIndex="jcsj" width="200"/>
                <aos:column header="检查部门" dataIndex="jcbm"/>
                <aos:column header="检查人" dataIndex="jcr"/>
                <aos:column header="档案门类" dataIndex="daml"/>
                <aos:column header="档号" dataIndex="dh"/>
                <aos:column header="题名" dataIndex="tm"/>

                <aos:column header="形成日期" dataIndex="xcrq"/>
                <aos:column header="保管期限" dataIndex="bgqx"/>
                <aos:column header="责任者" dataIndex="zrz"/>
                <aos:column header="页数" dataIndex="ys"/>
                <aos:column header="保管状况" dataIndex="bgzk"/>
                <aos:column header="问题页数" dataIndex="wtys"/>
                <aos:column header="问题描述" dataIndex="wtms" width="300"/>
                <aos:column header="是否处理" dataIndex="sfcl"/>
                <aos:column header="处理人" dataIndex="clr"/>
                <aos:column header="处理时间" dataIndex="clsj" width="200"/>
                <aos:column header="备注" dataIndex="bz" width="300"/>
                <aos:column header="" flex="1" />
            </aos:gridpanel>
        </aos:viewport>
        <aos:window id="_w_pd" title="新增" onshow="_new_task">
            <aos:formpanel id="_f_pd" layout="column" width="700">
                <aos:textfield name="kfbh" fieldLabel="库房编号" columnWidth="0.49" readOnly="true"/>
                <aos:datefield name="jcsj" fieldLabel="检查时间" columnWidth="0.49"/>
                <aos:textfield name="jcbm" fieldLabel="检查部门" columnWidth="0.49"/>
                <aos:textfield name="jcr" fieldLabel="检查人" columnWidth="0.49"/>
                <aos:textfield name="daml" fieldLabel="档案门类" columnWidth="0.49"/>
                <aos:textfield name="dh" fieldLabel="档号" columnWidth="0.49"/>
                <aos:textfield name="tm" fieldLabel="题名" columnWidth="0.49"/>
                <aos:datefield name="xcrq" fieldLabel="形成日期" columnWidth="0.49"/>
                <aos:textfield name="bgqx" fieldLabel="保管期限"  columnWidth="0.49"/>
                <aos:textfield name="zrz" fieldLabel="责任者"  columnWidth="0.49"/>
                <aos:textfield name="ys" fieldLabel="页数"  columnWidth="0.49"/>


                <aos:checkboxgroup fieldLabel="保管状况" columnWidth="0.49" id="bgzk_add" columns="[80,80,80,80]">
                    <aos:checkbox name="c1" boxLabel="完好" />
                    <aos:checkbox name="c1" boxLabel="破损" />
                    <aos:checkbox name="c1" boxLabel="脱页" />
                    <aos:checkbox name="c1" boxLabel="脆黄" />
                </aos:checkboxgroup>


                <aos:textareafield name="wtms" fieldLabel="问题描述"  columnWidth="0.99"/>
                <aos:rowset2><aos:textfield name="wtys" fieldLabel="问题页数"  columnWidth="0.49"/></aos:rowset2>
                <aos:radioboxgroup fieldLabel="是否处理" id="sfcl_add" columns="[100,100]" columnWidth="0.49">
                    <aos:radiobox name="r4" boxLabel="是"  checked="true"/>
                    <aos:radiobox name="r4" boxLabel="否" />
                </aos:radioboxgroup>
                <aos:textfield name="clr" fieldLabel="处理人"  columnWidth="0.49"/>
                <aos:textfield name="clsj" fieldLabel="处理时间"  columnWidth="0.49"/>
                <aos:textfield name="bz" fieldLabel="备注"  columnWidth="0.49"/>
                <aos:hiddenfield id="lx" name="lx" value="${lx}"/>
                <aos:docked dock="bottom">
                    <aos:dockeditem xtype="tbfill"/>
                    <aos:dockeditem text="保存" icon="save.png" onclick="_f_pd_save"/>
                    <aos:dockeditem text="关闭" icon="close.png" onclick="#_w_pd.hide()"/>
                </aos:docked>
            </aos:formpanel>
        </aos:window>
        <aos:window id="_w_pd_u" title="修改">
            <aos:formpanel id="_f_pd_u" layout="column" width="700">
                <aos:hiddenfield name="id_" fieldLabel="流水号" />
                <aos:textfield name="kfbh" fieldLabel="库房编号" columnWidth="0.49" readOnly="true"/>
                <aos:datefield name="jcsj" fieldLabel="检查时间" columnWidth="0.49"/>
                <aos:textfield name="jcbm" fieldLabel="检查部门" columnWidth="0.49"/>
                <aos:textfield name="jcr" fieldLabel="检查人" columnWidth="0.49"/>
                <aos:textfield name="daml" fieldLabel="档案门类" columnWidth="0.49"/>
                <aos:textfield name="dh" fieldLabel="档号" columnWidth="0.49"/>
                <aos:textfield name="tm" fieldLabel="题名" columnWidth="0.49"/>
                <aos:datefield name="xcrq" fieldLabel="形成日期" columnWidth="0.49"/>
                <aos:textfield name="bgqx" fieldLabel="保管期限"  columnWidth="0.49"/>
                <aos:textfield name="zrz" fieldLabel="责任者"  columnWidth="0.49"/>
                <aos:textfield name="ys" fieldLabel="页数"  columnWidth="0.49"/>


                <aos:checkboxgroup fieldLabel="保管状况" columnWidth="0.49" id="bgzk_update" columns="[80,80,80,80]">
                    <aos:checkbox name="c1" boxLabel="完好" />
                    <aos:checkbox name="c1" boxLabel="破损" />
                    <aos:checkbox name="c1" boxLabel="脱页" />
                    <aos:checkbox name="c1" boxLabel="脆黄" />
                </aos:checkboxgroup>


                <aos:textareafield name="wtms" fieldLabel="问题描述"  columnWidth="0.99"/>
                <aos:rowset2><aos:textfield name="wtys" fieldLabel="问题页数"  columnWidth="0.49"/></aos:rowset2>
                <aos:radioboxgroup fieldLabel="是否处理" id="sfcl_update" columns="[100,100]" columnWidth="0.49">
                    <aos:radiobox name="r4" boxLabel="是"  checked="true"/>
                    <aos:radiobox name="r4" boxLabel="否" />
                </aos:radioboxgroup>
                <aos:textfield name="clr" fieldLabel="处理人"  columnWidth="0.49"/>
                <aos:textfield name="clsj" fieldLabel="处理时间"  columnWidth="0.49"/>
                <aos:textfield name="bz" fieldLabel="备注"  columnWidth="0.49"/>
                <aos:hiddenfield id="lx" name="lx" value="${lx}"/>
                <aos:docked dock="bottom">
                    <aos:dockeditem xtype="tbfill"/>
                    <aos:dockeditem text="保存" icon="save.png" onclick="_f_pd_u_save"/>
                    <aos:dockeditem text="关闭" icon="close.png" onclick="#_w_pd_u.hide()"/>
                </aos:docked>
            </aos:formpanel>
        </aos:window>
        <script type="text/javascript">
            //生成XLS报表
            function _w_charts_export() {

                AOS.ajax({
                    url : 'fillReport_bg.jhtml',
                    params:{lx:Ext.getCmp("lx").getValue()},
                    ok : function(data) {
                        AOS.file('${cxt}/report/xls2.jhtml');
                    }
                });
            }
            //导入excel窗口
            function _w_import_show(){
                window.parent.fnaddtab('bgzk','保管状况导入','/depot/safe/initimport.jhtml?tablename=depot_bg&lx='+Ext.getCmp("lx").getValue());
            }
            function _g_pd_query(){
                var params={
                    lx:lx.getValue()
                };
                _g_pd_store.getProxy().extraParams=params;
                _g_pd_store.load();
            }
            function _new_task(){
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
                    params:{name_:"保管状况流水号"},
                    url:'calcId.jhtml',

                    ok:function(data){
                        //设计一个随机数编号
                        //年月日
                        var time = (new Date).getTime();
                        var yesday = new Date(time); // 获取的是前一天日期
                        yesday = yesday.getFullYear()  +""+(yesday.getMonth()> 9 ? (yesday.getMonth() + 1) : "0" +
                            (yesday.getMonth() + 1));
                        _f_pd.form.findField("kfbh").setValue(lx+yesday+data.index);
                    }
                });
            }
            function _w_pd_show(){
                _w_pd.show();
            }
            function _f_pd_save(){
                var bgzk="";
                var xqCheck = Ext.getCmp('bgzk_add').items;
                for(var i = 0; i < xqCheck.length; i++){
                    if(xqCheck.get(i).checked==true){
                        if(bgzk==""){
                            bgzk=xqCheck.get(i).boxLabel;
                        }else{
                            bgzk=bgzk+","+xqCheck.get(i).boxLabel;
                        }
                    }
                }
                var sfcl_add = Ext.getCmp('sfcl_add').items;
                var sfcl="";
                for (var i=0; i<sfcl_add.length;i++){
                    if (sfcl_add.items[i].checked){//此处获取到的是inputValue的值
                        sfcl = sfcl_add.items[i].boxLabel;
                    }
                }
                AOS.ajax({
                    forms:_f_pd,
                    url:'saveBg.jhtml',
                    params:{bgzk:bgzk,sfcl:sfcl},
                    ok:function(data){
                        if(data.appcode === 1){
                            _w_pd.hide();
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

                    var sfcl = Ext.getCmp('sfcl_update').items;
                    for(var i = 0; i < sfcl.length; i++){
                        if(sfcl.get(i).boxLabel==record.data.sfcl){
                            sfcl.items[i].setValue(true);
                        }else{
                            var zz= sfcl_update.items[i];
                            sfcl.items[i].setValue(false);
                        }
                    }
                    var xqCheck = Ext.getCmp('bgzk_update').items;
                    for(var i = 0; i < xqCheck.length; i++){
                        if(record.data.bgzk.indexOf(xqCheck.get(i).boxLabel)!=-1){
                            xqCheck.get(i).setValue(true);
                        }
                    }
                }
            }
            function _f_pd_u_save(){
                var bgzk="";
                var xqCheck = Ext.getCmp('bgzk_update').items;
                for(var i = 0; i < xqCheck.length; i++){
                    if(xqCheck.get(i).checked==true){
                        if(bgzk==""){
                            bgzk=xqCheck.get(i).boxLabel;
                        }else{
                            bgzk=bgzk+","+xqCheck.get(i).boxLabel;
                        }
                    }
                }
                var sfcl_update = Ext.getCmp('sfcl_update').items;
                var sfcl="";
                for (var i=0; i<sfcl_update.length;i++){
                    if (sfcl_update.items[i].checked){//此处获取到的是inputValue的值
                        sfcl = sfcl_update.items[i].boxLabel;
                    }
                }


                AOS.ajax({
                    forms:_f_pd_u,
                    url:'updateBg.jhtml',
                    params:{sfcl:sfcl,bgzk:bgzk},
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
                        url:'deleteBg.jhtml',
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
        </script>
    </aos:onready>
</aos:html>
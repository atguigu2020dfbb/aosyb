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
            <aos:gridpanel id="_g_pd" onrender="_g_pd_query" url="listBg.jhtml">
                <aos:docked>
                    <aos:dockeditem text="新增" icon="add.png" onclick="_w_pd_show"/>
                    <aos:dockeditem text="修改" icon="edit.png" onclick="_w_pd_u_show"/>
                    <aos:dockeditem text="删除" icon="del.png" onclick="_g_pd_del"/>
                    <aos:dockeditem onclick="_w_charts_export" text="导出excel" icon="icon70.png" />
                    <aos:dockeditem onclick="_w_import_show" text="导入excel" icon="folder8.png" />
                </aos:docked>
                <aos:column header="流水号" dataIndex="id_" hidden="true" locked="true"/>
                <aos:column header="流水号" dataIndex="kfbh"/>
                <aos:column header="检查时间" dataIndex="jcsj" width="200"/>
                <aos:column header="检查部门" dataIndex="jcbm"/>
                <aos:column header="检查人" dataIndex="jcr"/>
                <aos:column header="档案门类" dataIndex="daml"/>
                <aos:column header="档号" dataIndex="dh"/>
                <aos:column header="全宗号" dataIndex="qzh"/>
                <aos:column header="目录号" dataIndex="mlh"/>
                <aos:column header="案卷号" dataIndex="ajh"/>
                <aos:column header="问题页号" dataIndex="wtyh"/>
                <aos:column header="问题页数" dataIndex="wtys"/>
                <aos:column header="破损程度" dataIndex="bgzk"/>

                <aos:column header="其他问题描述" dataIndex="wtms" width="300"/>
                <aos:column header="是否处理" dataIndex="sfcl"/>
                <aos:column header="抢救方式" dataIndex="qjfs"/>
                <aos:column header="处理人" dataIndex="clr"/>
                <aos:column header="处理时间" dataIndex="clsj" width="200"/>
                <aos:column header="备注" dataIndex="bz" width="300"/>
                <aos:column header="" flex="1" />
            </aos:gridpanel>
        </aos:viewport>
        <aos:window id="_w_pd" title="新增" onshow="_new_task">
            <aos:formpanel id="_f_pd" layout="column" width="700">
                <aos:textfield name="kfbh" fieldLabel="流水号" columnWidth="0.49" />
                <aos:datefield name="jcsj" fieldLabel="检查时间" columnWidth="0.49"/>
                <aos:textfield name="jcbm" id="jcbm1" fieldLabel="检查部门" columnWidth="0.49"  />

                <aos:textfield name="jcr" fieldLabel="检查人" columnWidth="0.49" value="${user}"/>
                <aos:combobox name="daml" id="daml1" fieldLabel="档案门类" columnWidth="0.49">
                    <aos:option value="文书档案" display="文书档案"></aos:option>
                    <aos:option value="会计档案" display="会计档案"></aos:option>
                    <aos:option value="人事档案" display="人事档案"></aos:option>
                    <aos:option value="科技档案" display="科技档案"></aos:option>
                    <aos:option value="声像档案" display="声像档案"></aos:option>
                    <aos:option value="专业档案" display="专业档案"></aos:option>
                </aos:combobox>
                <aos:combobox name="daml" id="daml2" fieldLabel="档案门类" columnWidth="0.49">
                    <aos:option value="清代档案" display="清代档案"></aos:option>
                    <aos:option value="民国档案" display="民国档案"></aos:option>
                    <aos:option value="革命历史" display="革命历史"></aos:option>
                </aos:combobox>
                <aos:combobox name="daml" id="daml3" fieldLabel="资料门类" columnWidth="0.49">
                    <aos:option value="报纸" display="报纸"></aos:option>
                    <aos:option value="期刊" display="期刊"></aos:option>
                    <aos:option value="图书" display="图书"></aos:option>
                </aos:combobox>
                <aos:textfield name="dh" fieldLabel="档号" columnWidth="0.49" />
                <aos:textfield name="qzh" fieldLabel="全宗号" columnWidth="0.49" onfocus="add_dh"/>
                <aos:textfield name="mlh" fieldLabel="目录号" columnWidth="0.49" onfocus="add_dh"/>
                <aos:textfield name="ajh" fieldLabel="案卷号" columnWidth="0.49" onfocus="add_dh"/>
                <aos:textfield name="wtyh" fieldLabel="问题页号" columnWidth="0.49"/>
                <aos:textfield name="wtys" fieldLabel="问题页数"  columnWidth="0.49"/>
                <aos:checkboxgroup fieldLabel="破损程度" columnWidth="0.49" id="bgzk_add" columns="[80,80,80,80]">
                    <aos:checkbox name="c1" boxLabel="一般" />
                    <aos:checkbox name="c1" boxLabel="严重" />
                </aos:checkboxgroup>
                <aos:textfield name="wtms" fieldLabel="其他问题描述"  columnWidth="0.99"/>

                <aos:radioboxgroup fieldLabel="是否处理" id="sfcl_add" columns="[100,100]" columnWidth="0.49">
                    <aos:radiobox name="r4" boxLabel="是"  checked="true"/>
                    <aos:radiobox name="r4" boxLabel="否" />
                </aos:radioboxgroup>
                <aos:combobox fieldLabel="抢救方式" name="qjfs" value="修表" columnWidth="0.49">
                    <aos:option value="修表" display="修表"></aos:option>
                    <aos:option value="仿真复制" display="仿真复制"></aos:option>
                    <aos:option value="数字化" display="数字化"></aos:option>
                </aos:combobox>
                <aos:textfield name="clr" fieldLabel="处理人"  columnWidth="0.49"/>
                <aos:datefield name="clsj" fieldLabel="处理时间"  columnWidth="0.49"/>
                <aos:textfield name="bz" fieldLabel="备注"  columnWidth="0.98"/>
                <aos:hiddenfield id="lx" name="lx" value="${lx}"/>
                <aos:docked dock="bottom">
                    <aos:dockeditem xtype="tbfill"/>
                    <aos:dockeditem text="新增" icon="add.png" onclick="_f_add_bg"/>
                    <aos:dockeditem text="保存" icon="save.png" onclick="_f_pd_save"/>
                    <aos:dockeditem text="关闭" icon="close.png" onclick="#_w_pd.hide()"/>
                </aos:docked>
            </aos:formpanel>
        </aos:window>
        <aos:window id="_w_pd_u" title="修改" onshow="edit_show">
            <aos:formpanel id="_f_pd_u" layout="column" width="700">
                <aos:hiddenfield name="id_" fieldLabel="流水号" />
                <aos:textfield name="kfbh" fieldLabel="流水号" columnWidth="0.49" />
                <aos:datefield name="jcsj" fieldLabel="检查时间" columnWidth="0.49"/>
                <aos:textfield name="jcbm"  fieldLabel="检查部门" columnWidth="0.49" value="历史档案管理处" />
                <aos:textfield name="jcr" fieldLabel="检查人" columnWidth="0.49" value="${user}"/>
                <aos:combobox name="daml" id="daml4" fieldLabel="档案门类" columnWidth="0.49">
                    <aos:option value="文书档案" display="文书档案"></aos:option>
                    <aos:option value="会计档案" display="会计档案"></aos:option>
                    <aos:option value="人事档案" display="人事档案"></aos:option>
                    <aos:option value="科技档案" display="科技档案"></aos:option>
                    <aos:option value="声像档案" display="声像档案"></aos:option>
                    <aos:option value="专业档案" display="专业档案"></aos:option>
                </aos:combobox>
                <aos:combobox name="daml" id="daml5" fieldLabel="档案门类" columnWidth="0.49">
                    <aos:option value="清代档案" display="清代档案"></aos:option>
                    <aos:option value="民国档案" display="民国档案"></aos:option>
                    <aos:option value="革命历史" display="革命历史"></aos:option>
                </aos:combobox>
                <aos:combobox name="daml" id="daml6" fieldLabel="资料门类" columnWidth="0.49">
                    <aos:option value="报纸" display="报纸"></aos:option>
                    <aos:option value="期刊" display="期刊"></aos:option>
                    <aos:option value="图书" display="图书"></aos:option>
                </aos:combobox>
                <aos:textfield name="dh" fieldLabel="档号" columnWidth="0.49"/>
                <aos:textfield name="qzh" fieldLabel="全宗号" columnWidth="0.49"/>
                <aos:textfield name="mlh" fieldLabel="目录号" columnWidth="0.49"/>
                <aos:textfield name="ajh" fieldLabel="案卷号" columnWidth="0.49"/>
                <aos:textfield name="wtyh" fieldLabel="问题页号" columnWidth="0.49"/>
                <aos:textfield name="wtys" fieldLabel="问题页数"  columnWidth="0.49"/>
                <aos:checkboxgroup fieldLabel="破损程度" columnWidth="0.49" id="bgzk_update" columns="[80,80,80,80]">
                    <aos:checkbox name="c1" boxLabel="一般" />
                    <aos:checkbox name="c1" boxLabel="严重" />
                </aos:checkboxgroup>
                <aos:textfield name="wtms" fieldLabel="其他问题描述"  columnWidth="0.99"/>
                <aos:radioboxgroup fieldLabel="是否处理" id="sfcl_update" columns="[100,100]" columnWidth="0.49">
                    <aos:radiobox name="r4" boxLabel="是"  checked="true"/>
                    <aos:radiobox name="r4" boxLabel="否" />
                </aos:radioboxgroup>
                <aos:combobox fieldLabel="抢救方式" name="qjfs" columnWidth="0.49">
                    <aos:option value="修表" display="修表"></aos:option>
                    <aos:option value="仿真复制" display="仿真复制"></aos:option>
                    <aos:option value="数字化" display="数字化"></aos:option>
                </aos:combobox>
                <aos:textfield name="clr" fieldLabel="处理人"  columnWidth="0.49"/>
                <aos:datefield name="clsj" fieldLabel="处理时间"  columnWidth="0.49"/>
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
            function add_dh(){
                var qzh=_f_pd.form.findField("qzh").getValue();
                var mlh=_f_pd.form.findField("mlh").getValue();
                var ajh=_f_pd.form.findField("ajh").getValue();
                _f_pd.form.findField("dh").setValue(qzh+"-"+mlh+"-"+ajh)
            }
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
            function _f_add_bg(){
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
                        name_:"保管状况流水号",
                        lx:lx,
                        kfbh:_f_pd.form.findField("kfbh").getValue()
                    },
                    url:'getDepotBgIndex_nd.jhtml',
                    ok:function(data){
                        //设计一个随机数编号
                        //年
                        var time = (new Date).getTime();
                        var yesday = _f_pd.form.findField("kfbh").getValue().substring(3,7);
                        _f_pd.form.findField("kfbh").setValue(lx+yesday+data.index);
                    }
                });
            }
            function edit_show(){
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
                if(lx=="301"){
                    Ext.getCmp("_f_pd_u").remove("daml4");
                    Ext.getCmp("_f_pd_u").remove("daml6");
                }
                if(lx=="302"){
                    Ext.getCmp("_f_pd_u").remove("daml4");
                    Ext.getCmp("_f_pd_u").remove("daml5");
                }
                if(lx!="301"&&lx!="302"){
                    Ext.getCmp("_f_pd_u").remove("daml5");
                    Ext.getCmp("_f_pd_u").remove("daml6");
                }

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
               if(lx=="301"){
                   Ext.getCmp("_f_pd").remove("daml1");
                   Ext.getCmp("_f_pd").remove("daml3");
                    Ext.getCmp("jcbm1").setValue("历史档案管理处");
               }
                if(lx=="302"){
                    Ext.getCmp("jcbm1").setValue("历史档案管理处");
                    Ext.getCmp("_f_pd").remove("daml1");
                    Ext.getCmp("_f_pd").remove("daml2");
                }
               if(lx!="301"&&lx!="302"){
                   Ext.getCmp("jcbm1").setValue("党政档案管理处");
                   Ext.getCmp("_f_pd").remove("daml3");
                   Ext.getCmp("_f_pd").remove("daml2");
               }

                AOS.ajax({
                    params:{name_:"保管状况流水号",lx:lx},
                    url:'getDepotBgIndex.jhtml',
                    ok:function(data){
                        //设计一个随机数编号
                        //年月日
                        var time = (new Date).getTime();
                        var yesday = new Date(time); // 获取的是前一天日期
                        yesday = yesday.getFullYear();
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
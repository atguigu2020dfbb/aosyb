<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="/WEB-INF/tld/aos.tld" prefix="aos"%>
<%
    String path = request.getContextPath();
%>
<aos:html>
    <aos:head title="存址登记">
        <aos:include lib="ext" />
        <aos:include css="${cxt}/static/weblib/bootstrap/css/bootstrap.min.css"/>
        <aos:include css="${cxt}/static/css/fileinput.min.css"/>
        <aos:include js="${cxt}/static/js/jquery-3.2.1.min.js"/>
        <aos:include js="${cxt}/static/weblib/bootstrap/js/bootstrap.min.js"/>
        <aos:include js="${cxt}/static/js/fileinput.min.js"/>
        <aos:include js="${cxt}/static/js/zh.js"/>
        <aos:base href="depot/location" />
    </aos:head>
    <aos:body>
        <div id="filediv">
            <input id="file" class="file-loading" name="file" type="file" multiple>
        </div>
    </aos:body>
    <aos:onready>
        <aos:viewport layout="border">
                <aos:gridpanel id="_g_location" url="listLocation.jhtml" region="center"
                                 onrender="_g_location_query" pageSize="24" onitemclick="_g_detail_query"  forceFit="true">
                    <aos:docked forceBoder="0 0 1 0">
                        <aos:hiddenfield name="archive_storehouse" id="archive_storehouse" value="${archive_storehouse}"/>
                        <aos:hiddenfield name="djr_cn" id="djr_cn" value="${djr_cn}"/>
                        <aos:hiddenfield name="djr_en" id="djr_en" value="${djr_en}"/>
                        <aos:hiddenfield name="query" id="query" />
                        <aos:hiddenfield name="ids" id="ids" />
                        <aos:dockeditem onclick="_w_location_show" text="存址登记" icon="add.png" />
                        <aos:dockeditem onclick="_w_location_u_show" text="修改" icon="edit.png" />
                        <aos:dockeditem onclick="_g_location_del" text="删除" icon="del.png" />
                        <aos:dockeditem onclick="_w_charts_show" text="图形显示" icon="chart_flipped.png" />
                        <aos:dockeditem onclick="_w_charts_export" text="导出excel" icon="icon70.png" />
                        <aos:dockeditem onclick="_w_import_show" text="导入excel" icon="folder8.png" />

                        <aos:dockeditem onclick="_w_print_show" text="打印" icon="folder8.png" />
                        <aos:dockeditem xtype="tbseparator" />
                    </aos:docked>
                    <aos:selmodel type="row" mode="multi" />
                    <aos:column type="rowno" />
                    <aos:column header="流水号" dataIndex="id_" hidden="true"  locked="true"/>
                    <aos:column header="登记年度" dataIndex="djnd"  width="100"/>
                    <aos:column header="登记全宗" dataIndex="djqz"   width="100"/>
                    <aos:column header="存址编号" dataIndex="cz_bh"   width="100"/>
                    <aos:column header="库房编号" dataIndex="kfbh"    width="100"/>
                    <aos:column header="列号" dataIndex="cz_liehao"   width="100"/>
                    <aos:column header="节号" dataIndex="cz_jiehao"   width="100"/>
                    <aos:column header="格号" dataIndex="cz_gehao"    width="100"/>
                    <aos:column header="登记人" dataIndex="djr_cn"    width="100"/>
                    <aos:column header="登记人" dataIndex="djr_en" hidden="true"  />
                    <aos:column header="登记时间" dataIndex="djsj"    width="100"/>
                    <aos:column header="备注" dataIndex="bz"   width="100"/>
                    <aos:column header="库房分类" dataIndex="archive_storehouse" hidden="true" />
                    <aos:column header="" flex="1" />
                </aos:gridpanel>
                <aos:gridpanel id="_g_detail" url="listDetail2.jhtml" hidePagebar="false" pageSize="10" region="east" split="true"  splitterBorder="0 1 0 1">
                    <aos:docked forceBoder="0 0 1 0">
                        <aos:dockeditem onclick="_w_detail_show" text="新增" icon="add.png" />
                        <aos:dockeditem onclick="_g_detail_del" text="删除" icon="del.png" />
                        <aos:dockeditem xtype="tbseparator" />
                    </aos:docked>
                    <aos:selmodel type="checkbox" mode="multi" />
                    <aos:column type="rowno" />
                    <aos:column header="流水号" dataIndex="id_" hidden="true"  locked="true"/>
                    <aos:column header="存址编号" dataIndex="cz_bh" />
                    <aos:column header="全宗单位" dataIndex="qmc" />
                    <aos:column header="档号" dataIndex="dh" celltip="true" />
                    <aos:column header="题名" dataIndex="tm"  width="80" />
                    <aos:column header="形成时间" dataIndex="cwrq"  />
                    <aos:column header="盒号" dataIndex="hh" />
                    <aos:column header="备注" dataIndex="bz" flex="1" celltip="true" />
                    <aos:column header="档案门类" dataIndex="tablename" hidden="true"  />
                    <aos:column header="档案门类" dataIndex="tabledesc" />
                </aos:gridpanel>
            <aos:window id="_w_location" title="新增" onshow="_w_location_onshow">
                <aos:formpanel id="_f_location" layout="column" width="700" >
                    <aos:textfield name="djnd" fieldLabel="登记年度" columnWidth="0.49"/>
                    <aos:textfield name="djqz" fieldLabel="登记全宗" columnWidth="0.49"/>
                    <aos:textfield name="cz_bh" fieldLabel="存址编号" readOnly="true" columnWidth="0.49"/>
                    <aos:textfield name="kfbh" fieldLabel="库房编号" readOnly="true" columnWidth="0.49"/>
                    <aos:textfield name="cz_liehao" fieldLabel="列号" onblur="onblur_czbh" columnWidth="0.49"/>
                    <aos:textfield name="cz_jiehao" fieldLabel="节号" onblur="onblur_czbh" columnWidth="0.49"/>
                    <aos:textfield name="cz_gehao" fieldLabel="格号" onblur="onblur_czbh" columnWidth="0.49"/>
                    <aos:textfield name="djr_cn" fieldLabel="登记人" columnWidth="0.49" />
                    <aos:hiddenfield name="djr_en" fieldLabel="登记人" />
                    <aos:textfield name="djsj" fieldLabel="登记时间" columnWidth="0.49"/>
                    <aos:textfield name="bz" fieldLabel="备注" columnWidth="0.49"/>
                    <aos:hiddenfield name="archive_storehouse" fieldLabel="库房分类" />

                    <aos:docked dock="bottom">
                        <aos:dockeditem xtype="tbfill"/>
                        <aos:dockeditem text="添加" icon="add.png" onclick="_f_location_save"/>
                        <aos:dockeditem text="关闭" icon="close.png" onclick="#_w_location.hide()"/>
                    </aos:docked>
                </aos:formpanel>
            </aos:window>
            <aos:window id="_w_location_u" title="修改">
                <aos:formpanel id="_f_location_u" layout="column" width="700">
                    <aos:hiddenfield name="id_" fieldLabel="流水号" />
                    <aos:textfield name="djnd" fieldLabel="登记年度" columnWidth="0.49"/>
                    <aos:textfield name="djqz" fieldLabel="登记全宗" columnWidth="0.49"/>
                    <aos:textfield name="cz_bh" fieldLabel="存址编号" readOnly="true" columnWidth="0.49"/>
                    <aos:textfield name="kfbh" fieldLabel="库房编号" readOnly="true" columnWidth="0.49"/>
                    <aos:textfield name="cz_liehao" fieldLabel="列号" onblur="onblur_czbh" columnWidth="0.49"/>
                    <aos:textfield name="cz_jiehao" fieldLabel="节号" onblur="onblur_czbh" columnWidth="0.49"/>
                    <aos:textfield name="cz_gehao" fieldLabel="格号" onblur="onblur_czbh" columnWidth="0.49"/>
                    <aos:textfield name="djr_cn" fieldLabel="登记人" columnWidth="0.49" />
                    <aos:hiddenfield name="djr_en" fieldLabel="登记人" />
                    <aos:textfield name="djsj" fieldLabel="登记时间" columnWidth="0.49"/>
                    <aos:textfield name="bz" fieldLabel="备注" columnWidth="0.49"/>
                    <aos:hiddenfield name="archive_storehouse" fieldLabel="库房分类" />
                    <aos:docked dock="bottom">
                        <aos:dockeditem xtype="tbfill"/>
                        <aos:dockeditem text="上一条" icon="more/go-previous.png"  onclick="_f_previous_data"/><%----%>
                        <aos:dockeditem text="下一条" icon="more/go-next.png" onclick="_f_next_data"/><%----%>
                        <aos:dockeditem text="保存" icon="save.png" onclick="_f_location_u_save"/>
                        <aos:dockeditem text="关闭" icon="close.png" onclick="#_w_location_u.hide()"/>
                    </aos:docked>
                </aos:formpanel>
            </aos:window>
            <aos:window id="_w_detail" title="新增登记信息" onshow="_w_detail_onshow">
                <aos:formpanel id="_f_detail" layout="anchor" width="700">
                    <aos:textfield name="cz_bh" fieldLabel="存址编号" readOnly="true"/>
                    <aos:textfield name="qzdw" fieldLabel="全宗单位"/>
                    <aos:textfield name="dh" fieldLabel="档号"/>
                    <aos:textfield name="tm" fieldLabel="题名"/>
                    <aos:textfield name="xcsj" fieldLabel="形成时间"/>
                    <aos:textfield name="hh" fieldLabel="盒号"/>
                    <aos:textfield name="bz" fieldLabel="备注"/>
                    <aos:docked dock="bottom">
                        <aos:dockeditem xtype="tbfill"/>
                        <aos:dockeditem text="添加" icon="add.png" onclick="_f_detail_save"/>
                        <aos:dockeditem text="关闭" icon="close.png" onclick="#_w_detail.hide()"/>
                    </aos:docked>
                </aos:formpanel>
            </aos:window>
            <aos:window id="_w_detail_u" title="修改登记信息">
                <aos:formpanel id="_f_detail_u" layout="anchor" width="700">
                    <aos:hiddenfield name="id_" fieldLabel="流水号" />
                    <aos:textfield name="cz_bh" fieldLabel="存址编号" readOnly="true"/>
                    <aos:textfield name="qzdw" fieldLabel="全宗单位"/>
                    <aos:textfield name="dh" fieldLabel="档号"/>
                    <aos:textfield name="tm" fieldLabel="题名"/>
                    <aos:textfield name="xcsj" fieldLabel="形成时间"/>
                    <aos:textfield name="hh" fieldLabel="盒号"/>
                    <aos:textfield name="bz" fieldLabel="备注"/>
                    <aos:docked dock="bottom">
                        <aos:dockeditem xtype="tbfill"/>
                        <aos:dockeditem text="保存" icon="save.png" onclick="_f_detail_u_save"/>
                        <aos:dockeditem text="关闭" icon="close.png" onclick="#_w_detail_u.hide()"/>
                    </aos:docked>
                </aos:formpanel>
            </aos:window>

            <aos:window id="_w_select_kf" title="库房数据选择"  width="800" height="500" onshow="kf_archive_reset">
                <aos:gridpanel id="_g_select_kf" url="listArchive.jhtml" region="center" width="770" height="460"
                               autoScroll="true" pageSize="20" enableLocking="true" >
                    <aos:docked forceBoder="0 0 1 0">
                        <aos:dockeditem xtype="tbtext" text="数据列表" />
                        <aos:combobox fieldLabel="数据表" name="sjkmc"
                                      fields="['tablename','tabledesc']" id="sjkmc" displayField="tabledesc" valueField="tablename"
                                      editable="false" columnWidth="0.7" emptyText="请选择数据表" url="listTablename_by.jhtml" onselect="select_tablename" width="300"/>
                        <aos:dockeditem text="查询" icon="query.png"
                                        onclick="_w_select_query_show" />
                        <aos:dockeditem  text="确定" icon="ok.png"
                                         onclick="ok_save_kf" />
                        <aos:dockeditem  text="全选" icon="more/edit-select-all-4.png"
                                         onclick="ok_save_All_archive" />
                    </aos:docked>
                    <aos:selmodel type="checkbox" mode="multi" />
                    <aos:column type="rowno" />
                    <aos:column dataIndex="id_" header="流水号" hidden="true" />
                    <aos:column header="全宗单位" dataIndex="qzdw" />
                    <aos:column header="档号" dataIndex="dh" celltip="true" />
                    <aos:column header="题名" dataIndex="tm"  width="80" />
                    <aos:column header="年度" dataIndex="nd" />
                    <aos:column header="保管期限" dataIndex="bgqx" />
                    <aos:column header="形成时间" dataIndex="xcsj"  />
                    <aos:column header="盒号" dataIndex="hh" />
                    <aos:column header="备注" dataIndex="bz" flex="1" celltip="true" />
                    <aos:column header="" flex="1" />
                </aos:gridpanel>

            </aos:window>
            <aos:window id="_w_query_select_q" title="查询" width="720" autoScroll="true"
                        layout="fit">
                <aos:tabpanel id="_tabpanel_select" region="center" activeTab="0"
                              bodyBorder="0 0 0 0" tabBarHeight="30">
                    <aos:tab title="列表式搜索" id="_tab_select_org">
                        <aos:formpanel id="_f_select_query" layout="column" columnWidth="1">
                            <aos:hiddenfield name="cascode_id_" value="${cascode_id_ }" />
                            <aos:hiddenfield name="tablename" value="${tablename }" />
                            <aos:hiddenfield name="columnnum" id="columnnum" value="7" />
                            <aos:combobox name="and1" value="on"
                                          labelWidth="10" columnWidth="0.2">
                                <aos:option value="on" display="并且" />
                                <aos:option value="up" display="或者" />
                            </aos:combobox>
                            <aos:combobox name="filedname1"  labelWidth="20" columnWidth="0.2" value="qzdw">
                                <aos:option value="qzdw" display="全宗单位"/>
                                <aos:option value="dh" display="档号"/>
                                <aos:option value="tm" display="题名"/>
                                <aos:option value="nd" display="年度"/>
                                <aos:option value="bgqx" display="保管期限"/>
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
                            <aos:combobox name="filedname2"  labelWidth="20" columnWidth="0.2" value="dh">
                                <aos:option value="qzdw" display="全宗单位"/>
                                <aos:option value="dh" display="档号"/>
                                <aos:option value="tm" display="题名"/>
                                <aos:option value="nd" display="年度"/>
                                <aos:option value="bgqx" display="保管期限"/>
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
                            <aos:combobox name="filedname3"  labelWidth="20" columnWidth="0.2" value="tm">
                                <aos:option value="qzdw" display="全宗单位"/>
                                <aos:option value="dh" display="档号"/>
                                <aos:option value="tm" display="题名"/>
                                <aos:option value="nd" display="年度"/>
                                <aos:option value="bgqx" display="保管期限"/>
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
                            <aos:combobox name="filedname4"  labelWidth="20" columnWidth="0.2" value="nd">
                                <aos:option value="qzdw" display="全宗单位"/>
                                <aos:option value="dh" display="档号"/>
                                <aos:option value="tm" display="题名"/>
                                <aos:option value="nd" display="年度"/>
                                <aos:option value="bgqx" display="保管期限"/>
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
                            <aos:combobox name="filedname5"  labelWidth="20" columnWidth="0.2" value="bgqx">
                                <aos:option value="qzdw" display="全宗单位" />
                                <aos:option value="dh" display="档号"  />
                                <aos:option value="tm" display="题名"/>
                                <aos:option value="nd" display="年度" />
                                <aos:option value="bgqx" display="保管期限" />
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
                                <aos:dockeditem onclick="#_w_query_select_q.hide();" text="关闭"
                                                icon="close.png" />
                            </aos:docked>
                        </aos:formpanel>
                    </aos:tab>
                </aos:tabpanel>
            </aos:window>
            <%--上传文件--%>
            <aos:window id="_fileupload_add" title="上传文件"  width="700" height="450" autoScroll="true"  >
                <aos:formpanel width="680" height="450" id="formpanell" layout="column" contentEl="filediv" autoScroll="true">
                </aos:formpanel>
                <aos:docked dock="bottom" ui="footer">
                    <aos:dockeditem xtype="tbfill" />
                    <aos:dockeditem text="导入数据" onclick="_import_data" icon="add.png" />
                    <aos:dockeditem onclick="#_fileupload_add.hide();" text="关闭" icon="close.png" />
                </aos:docked>
            </aos:window>
        </aos:viewport>

        <script type="text/javascript">
            function onblur_czbh(){
                var cz_liehao = Ext.getCmp('_f_location').getForm().findField('cz_liehao').getValue();
                var cz_jiehao = Ext.getCmp('_f_location').getForm().findField('cz_jiehao').getValue();
                var cz_gehao = Ext.getCmp('_f_location').getForm().findField('cz_gehao').getValue();
                var archive_storehouse = Ext.getCmp("archive_storehouse").getValue();
                Ext.getCmp('_f_location').getForm().findField('cz_bh').setValue(archive_storehouse+'-'+cz_liehao+'-'+cz_jiehao+'-'+cz_gehao);
            }
            function onblur_u_czbh(){
                var cz_liehao = Ext.getCmp('_f_location_u').getForm().findField('cz_liehao').getValue();
                var cz_jiehao = Ext.getCmp('_f_location_u').getForm().findField('cz_jiehao').getValue();
                var cz_gehao = Ext.getCmp('_f_location_u').getForm().findField('cz_gehao').getValue();
                var archive_storehouse = Ext.getCmp("archive_storehouse").getValue();
                Ext.getCmp('_f_location_u').getForm().findField('cz_bh').setValue(archive_storehouse+'-'+cz_liehao+'-'+cz_jiehao+'-'+cz_gehao);
            }
            window.onload=function(){
                Ext.getCmp("_g_detail").setWidth(document.body.scrollWidth*(3/5));
            }
            function _g_location_query(){
                var params = {
                    archive_storehouse: archive_storehouse.getValue()
                };
                _g_location_store.getProxy().extraParams = params;
                _g_location_store.load();
            }
            function _g_location_tree_query(){
                var record = AOS.selectone(_t_catalog);
                _g_location_store.getProxy().extraParams = {
                    archive_storehouse : record.raw.a
                };
                _g_location_store.load();
            }
            function _g_detail_query(){
                var record = AOS.selectone(_g_location);
                _g_detail_store.getProxy().extraParams = {
                    cz_bh : record.data.cz_bh,
                    archive_storehouse : archive_storehouse.getValue()
                };
                _g_detail_store.load();
            }
            function _f_location_save(){
                AOS.ajax({
                    forms:_f_location,
                    url:'saveLocation.jhtml',
                    ok:function(data){
                        if(data.appcode === 1){
                            _w_location.hide();
                            _g_location_store.reload();
                            AOS.tip(data.appmsg);
                        }else {
                            AOS.err(data.appmsg);
                        }
                    }
                })
            }
            //选择了数据表，并加载下方表格数据
            function select_tablename(){
                var params = {
                    tablename : Ext.getCmp("sjkmc").value
                };
                //这个Store的命名规则为：表格ID+"_store"。
                _g_select_kf_store.getProxy().extraParams = params;
                _g_select_kf_store.load();
            }
            function _w_location_u_show(){
                var record = AOS.selectone(_g_location);
                if(record){
                    _w_location_u.show();
                    _f_location_u.loadRecord(record);
                }
            }
            function _f_location_u_save(){
                AOS.ajax({
                    forms:_f_location_u,
                    url:'updateLocation.jhtml',
                    ok:function(data){
                        if(data.appcode===1){
                            _w_location_u.hide();
                            _g_location_store.reload();
                            AOS.tip(data.appmsg);
                        }else {
                            AOS.err(AOS.appmsg);
                        }
                    }
                })
            }
            function _g_location_del(){
                var selection = AOS.selection(_g_location,'id_');
                if(AOS.empty(selection)){
                    AOS.tip("删除前请选中数据。。。");
                    return;
                }
                var rows = AOS.rows(_g_location);
                var msg = AOS.merge('确定要删除选中的[{0}]条数据吗？',rows);
                AOS.confirm(msg,function(btn){
                    if(btn==='cancel'){
                        AOS.tip('删除被操作取消');
                        return;
                    }
                    AOS.ajax({
                        url:'deleteLocation.jhtml',
                        params:{
                            aos_rows_: selection
                        },
                        ok:function(data){
                            AOS.tip(data.appmsg);
                            _g_location_store.reload();
                        }
                    })
                })
            }

            function _w_location_show(){
                _w_location.show();
            }
            function _w_location_onshow(){
                _f_location.form.findField("archive_storehouse").setValue(archive_storehouse.getValue());
                _f_location.form.findField("kfbh").setValue(archive_storehouse.getValue());
                _f_location.form.findField("djr_cn").setValue(djr_cn.getValue());
                _f_location.form.findField("djr_en").setValue(djr_en.getValue());
                var time = (new Date).getTime();
                var yesday = new Date(time); // 获取的是前一天日期
                yesday = yesday.getFullYear()  +"-"+(yesday.getMonth()> 9 ? (yesday.getMonth() + 1) : "0" + (yesday.getMonth() + 1))+
                        "-"+(yesday.getDate()> 9 ? (yesday.getDate()) : "0" + yesday.getDate());
                _f_location.form.findField("djsj").setValue(yesday);

            }
            function _w_detail_show(){
                //_w_detail.show();
                var selection = AOS.selection(_g_location,'id_');
                if(AOS.empty(selection)){
                    AOS.tip("添加前请选中存址。。。");
                    return;
                }
                _w_select_kf.show();

            }
            function kf_archive_reset(){
               Ext.getCmp("sjkmc").setRawValue("");
                Ext.getCmp("sjkmc").setValue("");
                //AOS.reset(sjkmc);
                _g_select_kf_store.removeAll();
            }
            //查询窗口展开
            function _w_select_query_show() {
                //判断是不是
                //判断是否选择了数据表
                Ext.getCmp("ids").setValue("");
                var sjkmc=Ext.getCmp("sjkmc").value;
                if(sjkmc==""||sjkmc==null){
                    AOS.tip("请选择数据表!");
                    return;
                }
                _w_query_select_q.show();
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
                params["tablename"]=Ext.getCmp("sjkmc").value;
                _g_select_kf_store.getProxy().extraParams = params;
                _g_select_kf_store.load();
                AOS.ajax({
                    params:params,
                    url: 'saveQueryData.jhtml',
                    ok: function (data) {
                        //此时在隐藏域中存入查询条件
                        Ext.getCmp("query").setValue(Ext.getCmp("query").getValue()+" "+data.appmsg);
                    }
                });
                _w_query_select_q.hide();
                AOS.reset(_f_select_query);

            }

            function _w_detail_onshow(){
                var record = AOS.selectone(_g_location);

                AOS.setValue('_f_detail.cz_bh',record.data.cz_bh);

            }



            function _f_detail_save(){
                AOS.ajax({
                    forms:_f_detail,
                    url:'saveDetail.jhtml',
                    ok:function(data){
                        if(data.appcode === 1){
                            _w_detail.hide();
                            _g_detail_store.reload();
                            AOS.tip(data.appmsg);
                        }else {
                            AOS.err(data.appmsg);
                        }
                    }
                })
            }
            function _w_detail_u_show(){
                var record = AOS.selectone(_g_detail);
                if(record){
                    _w_detail_u.show();
                    _f_detail_u.loadRecord(record);
                }
            }
            function _f_detail_u_save(){
                AOS.ajax({
                    forms:_f_detail_u,
                    url:'updateDetail.jhtml',
                    ok:function(data){
                        if(data.appcode===1){
                            _w_detail_u.hide();
                            _g_detail_store.reload();
                            AOS.tip(data.appmsg);
                        }else {
                            AOS.err(AOS.appmsg);
                        }
                    }
                })
            }
            function _g_detail_del(){
                var selection_ids = AOS.selection(_g_detail,'id_');
                var selection_tablenames = AOS.selection(_g_detail,'tablename');
                if(AOS.empty(selection_ids)){
                    AOS.tip("删除前请选中数据。。。");
                    return;
                }
                var rows = AOS.rows(_g_detail);
                var msg = AOS.merge('确定要删除选中的[{0}]条数据吗？',rows);
                AOS.confirm(msg,function(btn){
                    if(btn==='cancel'){
                        AOS.tip('删除被操作取消');
                        return;
                    }
                    AOS.ajax({
                        url:'deleteDetail.jhtml',
                        params:{
                            aos_rows_: selection_ids,
                            aos_rows_tablename_:selection_tablenames
                        },
                        ok:function(data){
                            AOS.tip(data.appmsg);
                            _g_detail_store.reload();
                        }
                    })
                })
            }
            function _w_charts_show(){
                var archive_storehouse=Ext.getCmp("archive_storehouse").getValue();
                parent.fnaddtab('','图形显示','depot/location/initCharts.jhtml?archive_storehouse='+archive_storehouse);
            }
            //生成XLS报表
            function _w_charts_export() {
                AOS.ajax({
                    url : 'fillReport_depot.jhtml',
                    ok : function(data) {
                        AOS.file('${cxt}/report/xls2.jhtml');
                    }
                });
            }
            //导入窗口
            function _w_import_show() {
                //我得加个重置
                var tablename="depot_cz";
                Ext.getCmp("_fileupload_add").show();
                $(document).ready(function() {
                    $("#file").fileinput({
                        language: 'zh', //设置语言
                        uploadUrl: '${cxt}/archive/upload/uploadImport_excel.jhtml', //上传的地址
                        uploadExtraData:{
                            tablename: tablename
                        },
                        fileActionSettings:{
                            showZoom:false//显示预览按钮
                        },
                        allowedFileExtensions: ['xls','xlsx'],//接收的文件后缀
                        showRemove: true,//显示删除按钮
                        showUpload: true, //是否显示上传按钮
                        showCaption: true,//是否显示标题
                        browseClass: "btn btn-primary btn-xs", //按钮样式
                        removeClass: "btn btn-danger btn-xs",
                        uploadClass: "btn btn-success btn-xs",
                        dropZoneEnabled: true,//是否显示拖拽区域
                        //dropZoneTitle: "可以将文件拖放到这里",//拖拽区域显示文字
                        maxFileSize: 0,//单位为kb，如果为0表示不限制文件大小
                        minFileCount: 0,//表示允许同时上传的最小文件个数
                        maxFileCount: 1000,//表示允许同时上传的最大文件个数
                        hideThumbnailContent: true //在缩略图预览中隐藏图像，pdf，文本或其他内容---默认false-不隐藏
                    });
                });
                /**/
                //window.parent.fnaddtab('','数据导入','/archive/data_relevance/initImport.jhtml?tablename='+tablename.getValue()+'&pch='+pch);
            }function _import_data(){
                var file123=$("#filename");
                var file=$("#filename").attr("title");
                window.parent.fnaddtab('417','数据导入','archive/datatemporary/initImport_depot.jhtml?tablename=depot_cz&file='+file);
            }
            function ok_save_kf(){
                var count=AOS.rows(_g_select_kf);
                if(count<=0){
                    AOS.tip("请选择数据");
                    return;
                }
                var ids=Ext.getCmp("ids").getValue();
                AOS.ajax({
                    url : 'addKf_archive.jhtml',
                    params:{'ids':ids,
                        'cz_bh':AOS.selectone(_g_location).data.cz_bh,
                    'tablename':Ext.getCmp("sjkmc").value},
                    ok : function(data){
                        if(data.appcode===1){
                            _w_select_kf.hide();
                            //
                            _g_detail_store.reload();
                        }else{
                            AOS.err("添加失败");
                        }

                    }
                });

            }
            //保存当前选中的档案信息
            function ok_save_All_archive(){
                var model = _g_select_kf.getSelectionModel();
                model.selectAll();//选择所有行
                var ids="";
                var dhs="";
                //走后台进行当前条件的所有条目的id集合
                AOS.ajax({
                    url: 'getQueryIds.jhtml',
                    params:{'tablename':Ext.getCmp("sjkmc").getValue(),
                        'query':Ext.getCmp("query").getValue()},
                    ok: function(data){
                        var ids="";
                        var dhs="";
                        for(k in data){
                            if(k==0){
                                ids=data[k].id_;
                            }else{
                                ids=ids+","+data[k].id_;
                            }
                        }
                        Ext.getCmp('ids').setValue(ids);
                    }
                });
            }
            function _w_print_show(){
                AOS.ajax({
                    url : 'print.jhtml',
                    ok : function(data){

                    }
                });
            }

            //上一页
            function _f_previous_data(){
                var count=Ext.getCmp("_g_location").getStore().getCount();
                var me=Ext.getCmp("_g_location").getSelectionModel().getSelection();
                //var record = AOS.selectone(_g_location);
                //得到执行行的坐标
                var rowIndex = Ext.getCmp("_g_location").getStore().indexOf(me[0]);
                if(rowIndex==0){
                    AOS.err("当前第一条!");
                    return;
                }
                var s=Ext.getCmp("_g_location").getStore().getAt(rowIndex-1);
                //原先行取消选中
                Ext.getCmp("_g_location").getSelectionModel().deselect(rowIndex);
                //此时让光标选中上一行
                Ext.getCmp("_g_location").getSelectionModel().select(rowIndex-1, true);
                //组件被显示后触发。
                Ext.getCmp("_f_location_u").form.setValues(s.data);
            }
            //下一页
            function _f_next_data(){
                var count=Ext.getCmp("_g_location").getStore().getCount();
                var me=Ext.getCmp("_g_location").getSelectionModel().getSelection();
                //var record = AOS.selectone(_g_location);
                //得到执行行的坐标
                var rowIndex = Ext.getCmp("_g_location").getStore().indexOf(me[0]);
                if(rowIndex==count-1){
                    AOS.err("当前最后一条!");
                    return;
                }
                var s=Ext.getCmp("_g_location").getStore().getAt(rowIndex+1);
                //原先行取消选中
                Ext.getCmp("_g_location").getSelectionModel().deselect(rowIndex);
                //此时让光标选中下一行
                Ext.getCmp("_g_location").getSelectionModel().select(rowIndex+1, true);
                //组件被显示后触发。
                Ext.getCmp("_f_location_u").form.setValues(s.data);
            }
        </script>
    </aos:onready>
</aos:html>
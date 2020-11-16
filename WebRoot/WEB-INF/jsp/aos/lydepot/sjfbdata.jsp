<%@page contentType="text/html; charset=UTF-8" isELIgnored="false"%>
<%@taglib prefix="aos" uri="/WEB-INF/tld/aos.tld"%>
<%@ taglib uri="/WEB-INF/tld/cll.tld" prefix="c"%>
<%
    String path = request.getContextPath();
    Object fieldDtos = request.getAttribute("fieldDtos");
%>
<aos:html>
    <aos:head title="检索档案">
        <aos:base href="archive/lydepot"/>
        <aos:include lib="ext,swfupload"/>
        <aos:include js="${cxt}/static/pdfObject/pdfobject.js" />
        <aos:include css="${cxt}/static/weblib/bootstrap/css/bootstrap.min.css"/>
        <aos:include css="${cxt}/static/css/fileinput.min.css"/>
        <aos:include js="${cxt}/static/js/jquery-3.2.1.min.js"/>
        <aos:include js="${cxt}/static/weblib/bootstrap/js/bootstrap.min.js"/>
        <aos:include js="${cxt}/static/js/fileinput.min.js"/>
        <aos:include js="${cxt}/static/js/zh.js"/>
        <style>
            .my_row_red .x-grid-cell {
                background-color: #99FF99;
            }
            .grid-one-column .x-grid-cell {
                background-color: #a6caf0;
            }
        </style>
    </aos:head>
    <aos:body>
        <!--html代码  -->
        <div id="documentViewer" style="height: 600px;"></div>
        <div id="filediv">
            <input id="file" class="file-loading" name="file" type="file" multiple class="file-loading">
        </div>
        <video id="videolook" controls="controls" hidden="true" height="500" preload="autoplay"
               width="690">
        </video>
    </aos:body>
    <aos:onready>
        <aos:viewport layout="border">
            <aos:gridpanel id="_g_data" url="listDatils.jhtml" region="north" features="true" height="250"
                           onrender="_g_data_query" split="true" onitemclick="itemclick2" rowclass="true" pageSize="${pagesize}"
                           autoScroll="true">
                <aos:docked forceBoder="1 0 1 0">
                    <aos:hiddenfield  name="rowmath_rw" id="rowmath_rw" value="0"/>
                    <aos:hiddenfield  name="_path" id="_path"/>
                    <aos:dockeditem xtype="tbtext" text="任务信息"/>
                    <aos:dockeditem text="新增" icon="add2.png"
                                    onclick="_w_data_show"  />
                    <aos:dockeditem text="修改" icon="edit.png" onclick="fn_g_data" />
                    <aos:dockeditem text="删除" icon="del.png" onclick="_g_data_del" />
                    <aos:dockeditem text="上传" icon="more/go-top-8.png" onclick="_fileupload_show"/>
                   <%-- <aos:dockeditem text="下载" icon="down.png" onclick="down_fbs"/>--%>
                    <aos:dockeditem onclick="open_path_" text="显示" icon="picture.png" />
                    <aos:button text="在线发布" icon="more/compress.png" scale="small" margin="0 0 0 0" id="zxfb_table_text">
                        <aos:menu plain="false">
                            <aos:menuitem text="政务网" icon="more/application-view-tile.png" onclick="_g_zww_fabu"/>
                            <aos:menuitem text="互联网" icon="more/applications-internet.png"
                                          onclick="_g_hlw_fabu"/>
                        </aos:menu>
                    </aos:button>
                    <aos:button text="导出" icon="folder8.png" scale="small" margin="0 0 0 0" id="importt">
                        <aos:menu plain="false">
                            <aos:menuitem text="导出目录" icon="icon70.png" onclick="fn_xls"
                                          id="_f_import_message"/>
                            <aos:menuitem text="导出全文" icon="go1.gif" onclick="_tijiao_sjfb"
                                          id="_f_import_path_message"/>
                        </aos:menu>
                    </aos:button>
                    <aos:dockeditem xtype="tbfill"/>
                </aos:docked>
                <%--<aos:selmodel type="checkbox" mode="simple" />--%>
                <aos:column dataIndex="id_" header="流水号" hidden="true" locked="true"/>
                <aos:column dataIndex="fbbh" header="发布编号" width="150" ></aos:column>
                <aos:column dataIndex="fbwd" header="发布网段" width="150" ></aos:column>
                <aos:column dataIndex="fbrw" header="发布任务" width="150" ></aos:column>
                <aos:column dataIndex="fbnr" header="发布内容" width="150" ></aos:column>
                <aos:column dataIndex="fblx" header="发布类型" width="150" ></aos:column>
                <aos:column dataIndex="fbsl" header="发布数量" width="150" ></aos:column>
                <aos:column dataIndex="fbzt" header="发布状态" width="150" ></aos:column>
                <aos:column dataIndex="fbr" header="发布人" width="150" ></aos:column>
                <aos:column dataIndex="fbsq" header="发布时期" width="150" ></aos:column>
                <aos:column dataIndex="fbs" header="发布书" width="150" ></aos:column>
                <aos:column dataIndex="bz" header="备注" width="150" ></aos:column>
                <aos:column header="" flex="1"/>
            </aos:gridpanel>
            <aos:gridpanel id="_g_param" url="listAccounts_sjfb.jhtml" split="true" region="center" onitemclick="itemclick"  rowclass="true" onrender="_w_params_show" >
                <aos:docked forceBoder="0 0 1 0">
                    <aos:hiddenfield id="rowmath" name="rowmath" value="0" />
                    <aos:hiddenfield  id="tablename_id" name="tablename_id" value="${id_}"/>
                    <aos:hiddenfield  id="tablename" name="tablename" value="${tablename}"/>
                    <aos:hiddenfield  id="_recordid" name="_recordid" value="${recordid}"/>
                    <aos:hiddenfield  id="query_list" name="query_list" value="${query}"/>
                    <aos:hiddenfield  id="query" name="query" />
                    <aos:hiddenfield  id="user" name="user" value="${user}"/>
                    <aos:hiddenfield  id="ids" name="ids" />
                    <aos:hiddenfield  id="dhs" name="dhs" />
                    <aos:combobox name="listTablename"
                                  fields="[ 'tablename', 'tabledesc']" fieldLabel="选择门类"
                                  id="listTablename" editable="false" columnWidth="0.24"
                                  url="listTablename.jhtml" displayField="tabledesc"
                                  valueField="tablename"  allowBlank="false" onselect="fn_onselect"/>
                    <aos:dockeditem onclick="_g_add_data" text="添加" icon="add.png" />
                    <aos:dockeditem text="全部删除" icon="del.png"
                                    onclick="_g_data_del_term"/>
                    <aos:dockeditem onclick="_w_query_show" text="查询条件" icon="query.png" />
                    <aos:dockeditem onclick="_w_picture_show" text="显示" icon="picture.png" />
                </aos:docked>
                <aos:column type="rowno"  width="60"/>
                <aos:selmodel type="row" mode="multi" />
                <aos:column dataIndex="_path" header="电子文件"
                            rendererFn="fn_path_render" hidden="true"/>
                <aos:column dataIndex="id_" header="流水号" hidden="true" />
                <aos:column dataIndex="tablename_id"  hidden="true" />
                <c:forEach var="field" items="${fieldDtos}">
                    <aos:column dataIndex="${field.fieldenname}"
                                header="${field.fieldcnname}" width="${field.dislen}"  rendererField="field_type_" />
                </c:forEach>
                <aos:column dataIndex="qzdw" header="全宗单位" width="100" />
                <aos:column dataIndex="qzh" header="全宗号" width="100" />
                <aos:column dataIndex="mlh" header="目录号" width="100" />
                <aos:column dataIndex="sxh" header="顺序号" width="100" />
                <aos:column dataIndex="tm" header="题名" width="200" />
                <aos:column dataIndex="wjbh" header="文件编号" width="100" />
                <aos:column dataIndex="zrz" header="责任者" width="100" />
                <aos:column dataIndex="cwrq" header="形成时间" width="100" />
                <aos:column dataIndex="ys" header="页数" width="100" />
                <aos:column dataIndex="mj" header="密级" width="100" />
                <aos:column dataIndex="sfkf" header="是否开放" width="100" />
                <aos:column dataIndex="bgqx" header="保管期限" width="100" />
                <aos:column dataIndex="cz_bh" header="存址编号" width="100" />
                <aos:column dataIndex="dh" header="档号" width="100" />
                <aos:column header="" flex="1" />
            </aos:gridpanel>
            <aos:window id="_w_query_q" title="查询" width="720" autoScroll="true"
                        layout="fit" >
                <aos:tabpanel id="_tabpanel" region="center" activeTab="0"
                              bodyBorder="0 0 0 0" tabBarHeight="30">
                <aos:tab title="列表式搜索" id="_tab_org">
                    <aos:formpanel id="_f_query2" layout="column" columnWidth="1">
                        <aos:hiddenfield name="tablename" value="${tablename }" />
                        <aos:hiddenfield name="columnnum" id="columnnum" value="8" />
                        <aos:hiddenfield name="selectmark" id="selectmark" value="1" />
                        <aos:hiddenfield name="selectmath" id="selectmath" value="0" />
                        <aos:combobox name="and1" value="on"
                                      labelWidth="10" columnWidth="0.2">
                            <aos:option value="on" display="并且" />
                            <aos:option value="up" display="或者" />
                        </aos:combobox>
                        <aos:combobox name="filedname1" id="filedname1" onrender="query_select_field1" url="getComboboxList.jhtml"  fields="['fieldenname','fieldcnname']"
                                      displayField="fieldcnname"
                                      valueField="fieldenname"  labelWidth="20" columnWidth="0.2"  >
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
                        <aos:combobox name="filedname2" id="filedname2" onrender="query_select_field2" url="getComboboxList.jhtml"  fields="['fieldenname','fieldcnname']"
                                      displayField="fieldcnname"
                                      valueField="fieldenname"  labelWidth="20" columnWidth="0.2"  >
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
                        <aos:combobox name="filedname3" id="filedname3" onrender="query_select_field3" url="getComboboxList.jhtml"  fields="['fieldenname','fieldcnname']"
                                      displayField="fieldcnname"
                                      valueField="fieldenname"  labelWidth="20" columnWidth="0.2"  >
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
                        <aos:combobox name="filedname4" id="filedname4" onrender="query_select_field4" url="getComboboxList.jhtml"  fields="['fieldenname','fieldcnname']"
                                      displayField="fieldcnname"
                                      valueField="fieldenname"  labelWidth="20" columnWidth="0.2"  >
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
                        <aos:combobox name="filedname5" id="filedname5" onrender="query_select_field5" url="getComboboxList.jhtml"  fields="['fieldenname','fieldcnname']"
                                      displayField="fieldcnname"
                                      valueField="fieldenname"  labelWidth="20" columnWidth="0.2"  >
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
                            <aos:dockeditem onclick="_f_data_query" text="确定" icon="ok.png" />
                            <aos:dockeditem onclick="#_w_query_q.hide();" text="关闭"
                                            icon="close.png" />
                        </aos:docked>
                    </aos:formpanel>
                </aos:tab>
                </aos:tabpanel>
            </aos:window>
        </aos:viewport>


        <aos:window title="不可提供" id="_w_provide" layout="fit"  modal="false" center="true" maximizable="true"
        >
            <aos:formpanel id="_f_provide" layout="fit" autoScroll="true" labelWidth="70" width="500" height="200">
                <aos:textareafield name="wtgyy" fieldLabel="原因"  />
            </aos:formpanel>
            <aos:docked dock="bottom" ui="footer">
                <aos:dockeditem xtype="tbfill" />
                <aos:dockeditem xtype="button" text="保存数据" icon="ok.png" onclick="_f_provide_save"/>
                <aos:dockeditem text="取消" icon="close.png" onclick="#_w_provide.hide()"/>

            </aos:docked>
        </aos:window>


        <aos:window id="_w_account_find" title="登记编号[双击选择]" height="-200" width="800" layout="fit" >
            <aos:gridpanel id="_g_djform" url="listDjform.jhtml" onrender="_g_djform_query"
                           onitemdblclick="_g_djform_dbclick">
                <aos:docked>
                    <aos:triggerfield emptyText="姓名" id="_xm_" onenterkey="_g_account_query" trigger1Cls="x-form-search-trigger"
                                      onTrigger1Click="_g_djform_query" width="250" />
                </aos:docked>
                <aos:column type="rowno" />
                <aos:column header="id_" dataIndex="id_"/>
                <aos:column header="登记编号" dataIndex="djbh"/>
                <aos:column header="姓名" dataIndex="xm"/>
                <aos:column header="登记日期" dataIndex="djrq"/>
                <aos:column header="档号" dataIndex="dh"/>
                <aos:column header="题名" dataIndex="tm"/>
                <aos:column header="利用状态" dataIndex="lyzt" rendererFn="fn_lyzt_render"/>
                <aos:column header="利用方式" dataIndex="lyfs"/>
                <aos:column header="利用类型" dataIndex="lylx"/>
                <aos:column header="利用类别" dataIndex="lylb"/>
                <aos:column header="利用数量" dataIndex="lysl"/>
                <aos:column header="利用目的" dataIndex="lymd"/>
                <aos:column header="备注" dataIndex="bz"/>
                <aos:column header="" flex="1"/>
            </aos:gridpanel>
        </aos:window>
        <aos:window id="_w_select_edit_archive" title="互联网数据选择"  width="800" height="500" onshow="load_archive">
            <aos:gridpanel id="_g_select_edit_archive" url="listdataSjfb_edit.jhtml" region="north" width="770" height="460"
                           autoScroll="true" pageSize="100" enableLocking="true" hidePagebar="false">
                <aos:docked forceBoder="0 0 1 0">
                    <aos:dockeditem xtype="tbtext" text="数据列表" />
                    <aos:dockeditem text="查询" icon="query.png"
                                    onclick="_w_select_edit_query_show" />
                    <aos:dockeditem  text="确定" icon="ok.png"
                                     onclick="ok_save_archive_edit" />
                    <aos:dockeditem  text="全选" icon="more/edit-select-all-4.png"
                                     onclick="ok_save_All_archive" />
                </aos:docked>
                <aos:selmodel type="checkbox" mode="multi" />
                <aos:column dataIndex="id_" header="流水号" hidden="true" />
                <aos:column header="_path" dataIndex="_path" hidden="true" />
                <aos:column header="全宗单位" dataIndex="qzdw" />
                <aos:column header="档号" dataIndex="dh"  width="200" />
                <aos:column header="责任者" dataIndex="zrz"  width="200" />
                <aos:column header="形成时间" dataIndex="xcsj" />
                <aos:column header="题名" dataIndex="tm"    width="500"/>
                <aos:column header="页号" dataIndex="yh" />
                <aos:column header="页数" dataIndex="ys"  />
                <aos:column header="是否开放" dataIndex="sfkf" />
                <aos:column header="全宗号" dataIndex="qzh" />
                <aos:column header="目录号" dataIndex="mlh"  />
                <aos:column header="保管期限" dataIndex="bgqx" />
                <aos:column header="案卷号" dataIndex="ajh" />
                <aos:column header="顺序号（件号）" dataIndex="sxh" />
                <aos:column header="" flex="1" />
            </aos:gridpanel>
        </aos:window>
            <aos:window id="_w_query_edit_select_q" title="查询" width="720" autoScroll="true"
                        layout="fit" onshow="combobox_tableFileldlist">
                <aos:tabpanel id="_tabpanel_edit_select" region="center" activeTab="0"
                              bodyBorder="0 0 0 0" tabBarHeight="30">
                    <aos:tab title="列表式搜索" id="_tab_select_edit_org">
                        <aos:formpanel id="_f_select_edit_query" layout="column" columnWidth="1">
                            <aos:hiddenfield name="cascode_id_" value="${cascode_id_ }" />
                            <aos:hiddenfield name="tablename" value="${tablename }" />
                            <aos:hiddenfield name="columnnum"  value="7" />
                            <aos:combobox name="and1" value="on"
                                          labelWidth="10" columnWidth="0.2">
                                <aos:option value="on" display="并且" />
                                <aos:option value="up" display="或者" />
                            </aos:combobox>
                            <aos:combobox name="filednames1" id="filednames1" onrender="query_edit_select_field1" url="getComboboxList.jhtml"  fields="['fieldenname','fieldcnname']"
                                          displayField="fieldcnname"
                                          valueField="fieldenname"  labelWidth="20" columnWidth="0.2"  >
                            </aos:combobox>
                            <aos:combobox name="condition1" value="like"
                                          labelWidth="20" columnWidth="0.2">
                                <aos:option value="=" display="等于" />
                                <aos:option value=">" display="大于" />
                                <aos:option value=">=" display="大于等于" />
                                <aos:option value="<" display="小于" />
                                <aos:option value="<=" display="小于等于" />
                                <aos:option value="not like" display="不包含"/>
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
                            <aos:combobox name="filednames2" id="filednames2" onrender="query_edit_select_field2" url="getComboboxList.jhtml"  fields="['fieldenname','fieldcnname']"
                                          displayField="fieldcnname"
                                          valueField="fieldenname"  labelWidth="20" columnWidth="0.2"  >
                            </aos:combobox>

                            <aos:combobox name="condition2" value="like"
                                          labelWidth="20" columnWidth="0.2">
                                <aos:option value="=" display="等于" />
                                <aos:option value=">" display="大于" />
                                <aos:option value=">=" display="大于等于" />
                                <aos:option value="<" display="小于" />
                                <aos:option value="<=" display="小于等于" />
                                <aos:option value="not like" display="不包含"/>
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
                            <aos:combobox name="filednames3" id="filednames3" onrender="query_edit_select_field3" url="getComboboxList.jhtml"  fields="['fieldenname','fieldcnname']"
                                          displayField="fieldcnname"
                                          valueField="fieldenname"  labelWidth="20" columnWidth="0.2"  >
                            </aos:combobox>

                            <aos:combobox name="condition3" value="like"
                                          labelWidth="20" columnWidth="0.2">
                                <aos:option value="=" display="等于" />
                                <aos:option value=">" display="大于" />
                                <aos:option value=">=" display="大于等于" />
                                <aos:option value="<" display="小于" />
                                <aos:option value="<=" display="小于等于" />
                                <aos:option value="not like" display="不包含"/>
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
                            <aos:combobox name="filednames4" id="filednames4" onrender="query_edit_select_field4" url="getComboboxList.jhtml"  fields="['fieldenname','fieldcnname']"
                                          displayField="fieldcnname"
                                          valueField="fieldenname"  labelWidth="20" columnWidth="0.2"  >
                            </aos:combobox>

                            <aos:combobox name="condition4" value="like"
                                          labelWidth="20" columnWidth="0.2">
                                <aos:option value="=" display="等于" />
                                <aos:option value=">" display="大于" />
                                <aos:option value=">=" display="大于等于" />
                                <aos:option value="<" display="小于" />
                                <aos:option value="<=" display="小于等于" />
                                <aos:option value="not like" display="不包含"/>
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
                            <aos:combobox name="filednames5" id="filednames5" onrender="query_edit_select_field5" url="getComboboxList.jhtml"  fields="['fieldenname','fieldcnname']"
                                          displayField="fieldcnname"
                                          valueField="fieldenname"  labelWidth="20" columnWidth="0.2"  >
                            </aos:combobox>

                            <aos:combobox name="condition5" value="like"
                                          labelWidth="20" columnWidth="0.2">
                                <aos:option value="=" display="等于" />
                                <aos:option value=">" display="大于" />
                                <aos:option value=">=" display="大于等于" />
                                <aos:option value="not like" display="不包含"/>
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
                                <aos:dockeditem onclick="_f_select_edit_data_query" text="确定" icon="ok.png" />
                                <aos:dockeditem onclick="#_w_query_edit_select_q.hide();" text="关闭"
                                                icon="close.png" />
                            </aos:docked>
                        </aos:formpanel>
                    </aos:tab>
                </aos:tabpanel>
            </aos:window>
            <aos:window id="_w_data_i" title="新增"   width="660" height="300">
                <aos:formpanel id="_f_data_i" labelWidth="100" height="280" width="600" layout="column" margin="0 0 0 20">
                    <aos:textfield fieldLabel="发布编号" name="fbbh"  columnWidth="0.49"/>
                    <aos:combobox fieldLabel="发布网段" name="fbwd" columnWidth="0.49" >
                        <aos:option value="政务网" display="政务网"/>
                        <aos:option value="互联网" display="互联网"/>
                    </aos:combobox>
                    <aos:textfield fieldLabel="发布任务" name="fbrw"  columnWidth="0.98"/>
                    <aos:combobox fieldLabel="发布内容" name="fbnr" columnWidth="0.49" >
                        <aos:option value="目录" display="目录"/>
                        <aos:option value="全文" display="全文"/>
                        <aos:option value="目录和全文" display="目录和全文"/>
                    </aos:combobox>
                    <aos:combobox fieldLabel="发布类型" name="fblx" columnWidth="0.49" >
                        <aos:option value="开放" display="开放"/>
                        <aos:option value="不开放" display="不开放"/>
                        <aos:option value="控制" display="控制"/>
                    </aos:combobox>
                    <aos:textfield fieldLabel="发布数量" name="fbsl"  columnWidth="0.49"/>
                    <aos:combobox fieldLabel="发布状态" name="fbzt"  columnWidth="0.49">
                        <aos:option value="未发布" display="未发布"/>
                        <aos:option value="已发布" display="已发布"/>
                    </aos:combobox>
                    <aos:textfield fieldLabel="发布人" name="fbr"  columnWidth="0.49"/>
                    <aos:textfield fieldLabel="发布日期" name="fbrq"  columnWidth="0.49"/>
                    <aos:textfield fieldLabel="备注" name="bz"  columnWidth="0.98"/>
                </aos:formpanel>
                <aos:docked dock="bottom" ui="footer">
                    <aos:dockeditem xtype="tbfill"/>
                    <aos:dockeditem onclick="_f_data_i_save" text="保存" icon="icon80.png"/>
                    <aos:dockeditem onclick="#_w_data_i.hide();" text="关闭"
                                    icon="close.png"/>
                </aos:docked>
            </aos:window>

            <aos:window id="_w_data_update" title="修改" draggable="false" closable="false" closeAction="hide"
                        autoScroll="true" x="50" width="660" height="300">
                <aos:formpanel id="_f_data_update" labelWidth="100" height="280" width="600" margin="0 0 0 20">
                    <aos:hiddenfield name="id_"  />
                    <aos:textfield fieldLabel="发布编号" name="fbbh"  columnWidth="0.49"/>
                    <aos:combobox fieldLabel="发布网段" name="fbwd" columnWidth="0.49" >
                        <aos:option value="政务网" display="政务网"/>
                        <aos:option value="互联网" display="互联网"/>
                    </aos:combobox>
                    <aos:textfield fieldLabel="发布任务" name="fbrw"  columnWidth="0.98"/>
                    <aos:combobox fieldLabel="发布内容" name="fbnr" columnWidth="0.49" >
                        <aos:option value="目录" display="目录"/>
                        <aos:option value="全文" display="全文"/>
                        <aos:option value="目录和全文" display="目录和全文"/>
                    </aos:combobox>
                    <aos:combobox fieldLabel="发布类型" name="fblx" columnWidth="0.49" >
                        <aos:option value="开放" display="开放"/>
                        <aos:option value="不开放" display="不开放"/>
                        <aos:option value="控制" display="控制"/>
                    </aos:combobox>
                    <aos:textfield fieldLabel="发布数量" name="fbsl"  columnWidth="0.49"/>
                    <aos:combobox fieldLabel="发布状态" name="fbzt"  columnWidth="0.49">
                        <aos:option value="未发布" display="未发布"/>
                        <aos:option value="已发布" display="已发布"/>
                    </aos:combobox>
                    <aos:textfield fieldLabel="发布人" name="fbr"  columnWidth="0.49"/>
                    <aos:textfield fieldLabel="发布日期" name="fbrq"  columnWidth="0.49"/>
                    <aos:textfield fieldLabel="备注" name="bz"  columnWidth="0.98"/>
                </aos:formpanel>
                <aos:docked dock="bottom" ui="footer">
                    <aos:dockeditem xtype="tbfill"/>
                    <aos:dockeditem text="上一条" icon="more/go-previous.png" onclick="_f_previous_data"/><%----%>
                    <aos:dockeditem text="下一条" icon="more/go-next.png" onclick="_f_next_data"/><%----%>
                    <aos:dockeditem onclick="_f_data_update_save" text="保存" icon="icon80.png"/>
                    <aos:dockeditem onclick="#_w_data_update.hide();" text="关闭"
                                    icon="close.png"/>
                </aos:docked>
            </aos:window>
        <%--上传文件--%>
        <aos:window id="_fileupload_add" title="上传文件" width="700" height="450" autoScroll="true">
            <aos:formpanel width="680" height="450" id="formpanell" layout="column" contentEl="filediv"
                           autoScroll="true">
            </aos:formpanel>
            <aos:docked dock="bottom" ui="footer">
                <aos:dockeditem xtype="tbfill"/>
                <aos:dockeditem onclick="#_fileupload_add.hide();" text="关闭" icon="close.png"/>
            </aos:docked>
        </aos:window>
        <aos:window id="_w_open_path" title="全文信息" width="600" height="600" onshow="load_path_" >
            <aos:panel id="documentViewer3"   height="600" split="true" width="600" contentEl="documentViewer">

            </aos:panel>
        </aos:window>
        <script type="text/javascript">
            function load_path_(){
                document.getElementById('documentViewer3').innerHTML = "";
                var id_=AOS.selectone(_g_data).raw.id_;
                var filename=AOS.selectone(_g_data).raw.fbs;
                AOS.ajax({
                    params : {
                        id_ : id_,
                        filename:filename
                    },
                    url : 'getfbspath.jhtml',
                    ok : function(data) {
                        //将id值赋给hiddlen中
                        if (data.pdfpath == "" || data.pdfpath == null) {
                            Ext.getCmp("_path").setValue("");
                            document.getElementById('documentViewer3').innerHTML = "";
                        } else {
                            //new PDFObject({ url:data.pdfpath}).embed("documentViewer");

                            Ext.getCmp("_path").setValue(data.pdfpath);
                            PDFObject.embed(data.pdfpath, '#documentViewer3');
                        }
                    }
                });
            }
            function open_path_(){
                var selection = AOS.selection(_g_data, 'id_');
                if (AOS.empty(selection)) {
                    AOS.tip('查看全文前请先选中数据。');
                    return;
                }

                _w_open_path.show();
            }
            function fn_xls(){
                var record = AOS.selectone(_g_data);
                var tablename=Ext.getCmp("tablename").getValue();
                if (AOS.empty(record)||tablename==null||tablename==""||typeof(tablename)=="undefined"||tablename=="undefined") {
                    AOS.tip('请选择任务和数据表!');
                    return;
                } else {
                    AOS.ajax({
                        url: 'fillReport.jhtml',
                        params: {
                            tablename: tablename,
                            id_:record.data.id_
                        },
                        ok: function (data) {
                            AOS.file('${cxt}/report/xls.jhtml');
                        }
                    });
                }
            }
            function down_fbs(){
                var record = AOS.selectone(_g_data);
                if (AOS.empty(record)) {
                    AOS.tip('请选择要下载的任务!');
                    return;
                } else {
                    AOS.file('downloadPath.jhtml?id_=' + record.data.id_ + '&fbs=' + record.data.fbs);
                }
            }
            //弹出上传文件窗口
            function _fileupload_show() {
                //我得加个重置
                var record = AOS.selection(_g_data, 'id_');
                if (AOS.empty(record)) {
                    AOS.tip('请选择要上传的条目!');
                    return;
                } else {
                    //初始化方法
                    $("#file").fileinput('destroy');
                    //edit_image();
                    Ext.getCmp("_fileupload_add").show();
                    $("#file").fileinput({
                        language: 'zh', //设置语言
                        uploadUrl: '${cxt}/archive/upload/uploadSjfb_fbs.jhtml', //上传的地址
                        uploadAsync: true,
                        uploadExtraData: {
                            id_: AOS.selectone(_g_data).data.id_
                        },
                        fileActionSettings: {
                            showZoom: false//显示预览按钮
                        },
                        allowedFileExtensions: ['pdf'],//接收的文件后缀
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
                }
            }
            function _tijiao_sjfb(){
                window.location.href = "Datacall://";
            }
            //在线发布把数据发布到政务网
            function _g_zww_fabu(){
                var record = AOS.selectone(_g_data);
                if (AOS.empty(record)) {
                    AOS.tip('请先选中任务。');
                    return;
                }
                AOS.ajax({
                    url: 'updateDetails_zxfb.jhtml',
                    params: {
                        zww:1,
                        id_: record.data.id_
                    },
                    ok: function (data) {
                        AOS.tip(data.appmsg);
                        _g_data_store.reload();
                    }
                });

            }
            //在线发布把数据发布到互联网
            function _g_hlw_fabu(){
                var record = AOS.selectone(_g_data);
                if (AOS.empty(record)) {
                    AOS.tip('请先选中任务。');
                    return;
                }
                AOS.ajax({
                    url: 'updateDetails_zxfb.jhtml',
                    params: {
                        hlw:1,
                        id_: record.data.id_
                    },
                    ok: function (data) {
                        AOS.tip(data.appmsg);
                        _g_data_store.reload();
                    }
                });

            }
            //删除信息
            function _g_data_del() {
                var selection = AOS.selection(_g_data, 'id_');
                if (AOS.empty(selection)) {
                    AOS.tip('删除前请先选中任务。');
                    return;
                }
                var msg = AOS.merge('确认要删除选中的[{0}]个用户数据吗？', AOS.rows(_g_data));
                AOS.confirm(msg, function (btn) {
                    if (btn === 'cancel') {
                        AOS.tip('删除操作被取消。');
                        return;
                    }
                    AOS.ajax({
                        url: 'deleteDetails.jhtml',
                        params: {
                            aos_rows_: selection
                        },
                        ok: function (data) {
                            AOS.tip(data.appmsg);
                            _g_data_store.reload();
                        }
                    });
                });
            }
            function fn_g_data() {
                var selection = AOS.selection(_g_data, 'id_');
                var tms = AOS.selection(_g_data, 'tm');
                if (AOS.empty(selection)) {
                    AOS.tip('修改前请先选中任务。');
                    return;
                }else{
                AOS.ajax({
                    params: {
                        id_: AOS.selectone1(_g_data).data.id_
                    },
                    url: 'getDetail.jhtml',
                    ok: function (data) {
                        _f_data_update.form.setValues(data);
                        _w_data_update.show();
                    }
                });
                }
            }
            function query_edit_select_field1(){
                var params = {
                    tablename : Ext.getCmp("listTablename").getValue()
                };
                filednames1_store.getProxy().extraParams = params;
                filednames1_store.load();
            }
            function query_edit_select_field2(){
                var params = {
                    tablename : Ext.getCmp("listTablename").getValue()
                };
                filednames2_store.getProxy().extraParams = params;
                filednames2_store.load();
            }
            function query_edit_select_field3(){
                var params = {
                    tablename : Ext.getCmp("listTablename").getValue()
                };
                filednames3_store.getProxy().extraParams = params;
                filednames3_store.load();
            }
            function query_edit_select_field4(){
                var params = {
                    tablename : Ext.getCmp("listTablename").getValue()
                };
                filednames4_store.getProxy().extraParams = params;
                filednames4_store.load();
            }
            function query_edit_select_field5(){
                var params = {
                    tablename : Ext.getCmp("listTablename").getValue()
                };
                filednames5_store.getProxy().extraParams = params;
                filednames5_store.load();
            }
            function query_select_field1(){
                var params = {
                    tablename : Ext.getCmp("listTablename").getValue()
                };
                filedname1_store.getProxy().extraParams = params;
                filedname1_store.load();
            }
            function query_select_field2(){
                var params = {
                    tablename : Ext.getCmp("listTablename").getValue()
                };
                filedname2_store.getProxy().extraParams = params;
                filedname2_store.load();
            }
            function query_select_field3(){
                var params = {
                    tablename : Ext.getCmp("listTablename").getValue()
                };
                filedname3_store.getProxy().extraParams = params;
                filedname3_store.load();
            }
            function query_select_field4(){
                var params = {
                    tablename : Ext.getCmp("listTablename").getValue()
                };
                filedname4_store.getProxy().extraParams = params;
                filedname4_store.load();
            }
            function query_select_field5(){
                var params = {
                    tablename : Ext.getCmp("listTablename").getValue()
                };
                filedname5_store.getProxy().extraParams = params;
                filedname5_store.load();
            }
            //卡片新增目录加存
            function _f_data_i_save() {
                AOS.ajax({
                    forms: _f_data_i,
                    url: 'saveDetail.jhtml',
                    ok: function (data) {
                        if (data.appcode === -1) {
                            AOS.err(data.appmsg);
                        } else {
                            _g_data_store.reload();
                            _w_data_i.hide();
                            AOS.tip(data.appmsg);
                            //_w_data_i.hide();
                        }
                    }
                });
            }
            //卡片修改目录保存
            function _f_data_update_save() {
                AOS.ajax({
                    forms: _f_data_update,
                    url: 'updateDetail.jhtml',
                    ok: function (data) {
                        if (data.appcode === -1) {
                            AOS.err(data.appmsg);
                        } else {
                            _g_data_store.reload();
                            _w_data_update.hide();
                            AOS.tip(data.appmsg);
                            //_w_data_i.hide();
                        }
                    }
                });
            }
            window.onload=function(){
                Ext.getCmp("listTablename").setValue("<%=request.getAttribute("tablename")%>");
                Ext.getCmp("listTablename").setRawValue("<%=request.getAttribute("tabledesc")%>");

            }

            function changeRowClass(record, rowIndex, rowParams, store){

                //得到当前行的指定的列的值
                if(record.get("_path")>=1){
                    return 'grid-one-column';
                }else{
                    return 'grid-zero-column';
                }

            }
            function itemclick2(grid, rowIndex, columnIndex, e){
                //点击之前获取当前行的初始颜色，
                //var beginColor=grid.all.elements[e].cells[i].style.backgroundColor;
                var k=Ext.getCmp("rowmath_rw").getValue();
                for(var j=0;j<grid.all.elements[Ext.getCmp("rowmath_rw").getValue()].cells.length;j++){
                    grid.all.elements[Ext.getCmp("rowmath_rw").getValue()].cells[j].style="text-decoration:none";
                }
                var tt=grid.all.elements[e].cells.length;
                for(var i=0;i<tt;i++){
                    grid.all.elements[e].cells[i].style.backgroundColor="#68838B";
                    //此时把当前单元格存到缓冲中
                    Ext.getCmp("rowmath_rw").setValue(e);
                }

            }
            function itemclick(grid, rowIndex, columnIndex, e){
                //点击之前获取当前行的初始颜色，
                //var beginColor=grid.all.elements[e].cells[i].style.backgroundColor;
                var k=Ext.getCmp("rowmath").getValue();
                for(var j=0;j<grid.all.elements[Ext.getCmp("rowmath").getValue()].cells.length;j++){
                    grid.all.elements[Ext.getCmp("rowmath").getValue()].cells[j].style="text-decoration:none";
                }
                var tt=grid.all.elements[e].cells.length;
                for(var i=0;i<tt;i++){
                    grid.all.elements[e].cells[i].style.backgroundColor="#68838B";
                    //此时把当前单元格存到缓冲中
                    Ext.getCmp("rowmath").setValue(e);
                }
            }

            function select_tree(){
                var record = Ext.getCmp("_t_catalog").getStore().getNodeById("4a6520adda08429d84bb00a96e990b3e");

                var ss = Ext.getCmp("_t_catalog").getSelectionModel();
                ss.select(record);

            }
            function add_count(){
                var fieldtext=Ext.getCmp('_f_query').down("[name='fieldtext']").getValue();
                var fieldname=Ext.getCmp('_f_query').down("[name='filedname']").getValue();
                if(fieldname=="null"||fieldname==""||fieldname==null){
                    AOS.err("请选择字段");
                    return;
                }
                var _select_radio_check = Ext.getCmp('_select_radio').items;
                var xq = '';
                for(var i = 0; i < _select_radio_check.length; i++){
                    if(_select_radio_check.get(i).checked){
                        xq =_select_radio_check.get(i).boxLabel;
                    }
                }
                if(xq=="精确查询"){
                    var data=[{
                        'selectorder':"and "+fieldname+" = ",
                        'selectmath':fieldtext
                    }];
                    _g_count_store.loadData(data,true);
                }
                if(xq=="模糊查询"){
                    var data=[{
                        'selectorder':"and "+fieldname+" like ",
                        'selectmath':"%"+fieldtext+"%"
                    }];
                    _g_count_store.loadData(data,true);
                }
                if(xq=="高级检索"){
                    //条件
                    var and=Ext.getCmp('_f_query').down("[name='and']").getValue();
                    //连接符
                    var condition=Ext.getCmp('_f_query').down("[name='condition']").getValue();
                    var selectorder="";
                    var selectmath="";
                    if(condition=="like"){
                        condition="like";
                        selectorder=and+" "+fieldname+" like ";
                        selectmath="%"+fieldtext+"%";
                    }else if(condition=="left"){
                        selectorder=and+" "+fieldname+" like ";
                        selectmath="%"+fieldtext;
                    }else if(condition=="right"){
                        selectorder=and+" "+fieldname+" like ";
                        selectmath=fieldtext+"%";
                    }else if(condition=="null"){
                        selectorder=and+" "+fieldname+" is ";
                        selectmath="null";
                    }else{
                        selectorder=and+" "+fieldname+" "+condition;
                        selectmath=fieldtext;
                    }
                    var data=[{
                        'selectorder':selectorder,
                        'selectmath':selectmath
                    }];
                    _g_count_store.loadData(data,true);
                }
            }
            //弹出新增用户窗口
            function _w_data_show() {
                AOS.reset(_f_data_i);
                _w_data_i.show();
            }

            //全部删除(指定条件的全部删除)
            function _g_data_del_term() {
                var query = Ext.getCmp("query_list").getValue();
                var msg = AOS.merge('确认要删除互联网数据吗？');
                AOS.confirm(msg, function (btn) {
                    if (btn === 'cancel') {
                        AOS.tip('删除操作被取消。');
                        return;
                    }
                    AOS.ajax({
                        url: 'deleteAllData_sjfb.jhtml',
                        params: {
                            query: query,
                            fbwd:AOS.selectone(_g_data).data.fbwd,
                            fbid:AOS.selectone(_g_data).data.id_,
                            tablename: Ext.getCmp("tablename").getValue()
                        },
                        ok: function (data) {
                            AOS.tip(data.appmsg);
                            _g_param_store.reload();
                        }
                    });
                });
            }
            //加载表格数据
            function _g_data_query() {
                _g_data_store.load();
            }
            function delete_count(){
                var row = _g_count.getSelectionModel().getSelection();
                var rowid = _g_count_store.indexOf(row[0]);//获得选中的第一项在store内的行号
                //此时删除严肃，在他的上一行添加
                _g_count_store.removeAt(rowid);
            }
            //根据选择的门类名称，进行字段列表的查询操作
            function _load_fieldlists(){
                //默认把and隐藏
                //把门类字段列表传递过去
                var params = {
                    id_ : Ext.getCmp("tablename_id").getValue(),
                };
                filedname_store.getProxy().extraParams = params;
                filedname_store.load();
            }
            function hidetoshow(){
                var _select_radio_check = Ext.getCmp('_select_radio').items;
                var xq = '';
                for(var i = 0; i < _select_radio_check.length; i++){
                    if(_select_radio_check.get(i).checked){
                        xq =_select_radio_check.get(i).boxLabel;
                    }
                }
                if(xq=="高级检索"){
                    Ext.getCmp("and").show();
                    Ext.getCmp("condition").show();
                }else{
                    Ext.getCmp("and").hide();
                    Ext.getCmp("condition").hide();
                }
            }
            function _w_query_show() {
                _w_query_q.show();
            }
            //查询参数列表
            function _g_param_query(view, record, item, index, e) {
                var url =  record.raw.url_;
                var record = AOS.selectone(_t_catalog);
                var recordid = _recordid.getValue();
                if (!Ext.isEmpty(url)) {
                    window.location.href="loadData.jhtml?id_="+record.raw.id+"&name_="+record.raw.name_+"&recordid="+recordid;
                    //fnaddtab(record.raw.id, record.raw.text, url,root_id_,tablename,record.raw.b);
                }else{
                    if(record.raw.leaf){
                        AOS.tip('没有配置菜单的请求地址。');
                    }
                }
            }
            function _w_params_show(){
                var strtablename=Ext.getCmp("tablename").getValue();
                var params = {
                    tablename: strtablename
                };
                //这个Store的命名规则为：表格ID+"_store"。
                _g_param_store.getProxy().extraParams = params;
                _g_param_store.load();
            }

            //刷新分类树
            function _t_catalog_refresh() {
                var refreshnode = AOS.selectone(_t_catalog);
                if (refreshnode.isLeaf()) {
                    refreshnode = refreshnode.parentNode;
                }
                _t_catalog_store.load({
                    node: refreshnode,
                    callback: function () {
                        refreshnode.collapse();
                        refreshnode.expand();
                    }
                });
            }
            //确定执行统计设计
            function _f_data_query2(){
                //如果统计字段和统计方法没有选中直接不执行
                var s=Ext.getCmp('_g_count').getStore();
                var selectorders="";
                var selectmaths="";
                var query="";
                for(var i = 0 ;i< s.getCount(); i++){
                    query+=s.getAt(i).get('selectorder') +" '"+s.getAt(i).get('selectmath')+"'";
                }
                var params = AOS.getValue('_f_query');
                params["query"]=query;
                params["id_"]=Ext.getCmp("tablename_id").getValue();
                params["tablename"]="<%=request.getAttribute("tablename")%>";
                _g_param_store.getProxy().extraParams = params;
                _g_param_store.load();
                _w_query_q.hide();
            }
            //弹出选择卡号窗口
            function _w_account_find_show() {
                _w_account_find.show();
            }

            //加载表格数据
            function _g_djform_query() {
                var params = {
                    xm : _xm_.getValue()
                };
                //这个Store的命名规则为：表格ID+"_store"。
                _g_djform_store.getProxy().extraParams = params;
                _g_djform_store.load();
            }
            //账户表格双击事件
            function _g_djform_dbclick(obj, record) {
                _g_param.down('[name=djbh]').setValue(record.raw.djbh);
                _g_param.down('[name=recordid]').setValue(record.raw.id_);
                _w_account_find.hide();
            }

            //显示上传面板
            function _w_picture_show() {
                var user=Ext.getCmp("user").getValue();
                var record = AOS.selectone(_g_param);
                var uploadPanel= new Ext.ux.uploadPanel.UploadPanel({
                    addFileBtnText : '选择文件...',
                    uploadBtnText : '上传',
                    deleteBtnText : '移除',
                    downBtnText   : '下载',
                    downAllBtnText:'批量下载',
                    removeBtnText : '移除所有',
                    cancelBtnText : '取消上传',
                    use_query_string : true,
                    listeners:{
                        //双击
                        itemdblclick : function(grid,row,kk,rowIndex){
                            var table=Ext.getCmp("tablename").getValue();
                            //parent.fnaddtab(row.data.id, '电子文件',
                            //					'archive/data/openFile.jhtml?id='+row.data.pid+'&tid='+row.data.tid+'&type='+row.data.type+'&tablename='+tablename.getValue());
                            /*parent.fnaddtab(row.data.id, '电子文件',
                                                'archive/data/openPdfFile.jhtml?id='+row.data.pid+'&tid='+row.data.tid+'&type='+row.data.type+'&tablename='+tablename.getValue());*/
                            parent.parent.fnaddtab(row.data.id, '电子文件',
                                'archive/data/openPdfFile.jhtml?id='+row.data.pid+'&tid='+row.data.tid+'&type='+row.data.type+'&tablename='+table+'&index='+rowIndex);
                            /*parent.fnaddtab(row.data.id, '电子文件',
                                                 'archive/data1/initZpData.jhtml?id='+row.data.pid+'&tid='+row.data.tid+'&type='+row.data.type+'&tablename='+tablename.getValue());*/
                        }
                    },
                    //单机ocr选框
                    ocrPath:function(){
                        var value= Ext.getCmp("ocr").getValue();
                        var type="checkbox";
                        var name="ocr";
                        var tablename=Ext.getCmp("tablename").getValue();
                        //走后台存储当前用户选择的定义以便下次调用
                        remember_load(tablename,type,value,name);
                    },
                    //单机mark选框
                    markPath:function(){
                        var value= Ext.getCmp("mark").getValue();
                        var type="checkbox";
                        var name="mark";
                        var tablename=Ext.getCmp("tablename").getValue();
                        //走后台存储当前用户选择的定义以便下次调用
                        remember_load(tablename,type,value,name);
                    },
                    //单机info选框
                    infoPath:function(){
                        var value= Ext.getCmp("info").getValue();
                        var type="checkbox";
                        var name="info";
                        var tablename=Ext.getCmp("tablename").getValue();
                        //走后台存储当前用户选择的定义以便下次调用
                        remember_load(tablename,type,value,name);
                    },
                    onUpload : function(grid, rowIndex, colIndex){

                        if(user!="root"){
                            AOS.err("没有权限!");
                            return;
                        }
                        var me=Ext.getCmp("uploadpanel").getSelectionModel().getSelection();
                        var pid = me[0].get('pid');
                        var tid = me[0].get('tid');
                        var type=me[0].get('type');
                        var record = AOS.selectone(_g_param);
                        var table=Ext.getCmp("tablename").getValue();
                        parent.fnaddtab(pid, '电子文件',
                            'archive/data/openFile.jhtml?id='+pid+'&tid='+tid+'&type='+type+'&tablename='+table+'&index='+rowIndex);
                    },
                    deletePath:	function(grid, rowIndex, colIndex) {
                        //AOS.tip("请双击文件进行查看。");
                       // return;
                        if(user!="root"){
                            AOS.err("没有权限!");
                            return;
                        }
                        var me=Ext.getCmp("uploadpanel").getSelectionModel().getSelection();

                        var id = me[0].get('pid');
                        var tid = me[0].get('tid');
                        var rowIndex = Ext.getCmp("uploadpanel").getStore().indexOf(me[0]);
                        var table=Ext.getCmp("tablename").getValue();
                        AOS.ajax({
                            params: {id_:id,
                                tablename:table,
                                tid:tid,
                                tm:record.data.tm
                            }, // 提交参数,
                            url: 'deletePath.jhtml',
                            ok: function (data) {
                                var me=Ext.getCmp("uploadpanel");
                                //me.store.reload();
                                me.store.remove(me.store.getAt(rowIndex));
                                // me.store.load();
                                AOS.tip(data.appmsg);
                            }
                        });
                    },
                    onRemoveAll: function (){
                        if(user!="root"){
                            AOS.err("没有权限!");
                            return;
                        }
                        var selection = AOS.selection(_g_data, 'id_');
                        var table=Ext.getCmp("tablename").getValue();
                        AOS.ajax({
                            params: {aos_rows_: selection,
                                tm:record.data.tm,
                                tablename: table
                            },
                            url: 'deletePathAll.jhtml',
                            ok: function (data) {
                                var me=Ext.getCmp("uploadpanel");
                                me.removeAll();
                                AOS.tip(data.appmsg);
                            }
                        });
                    },
                    //下载
                    onDownPath:function(grid, rowIndex, colIndex){

                        if(user!="root"){
                            AOS.err("没有权限!");
                            return;
                        }
                        //得到选中的条目
                        var me=Ext.getCmp("uploadpanel").getSelectionModel().getSelection();
                        var id = me[0].get('pid');
                        var table=Ext.getCmp("tablename").getValue();
                        AOS.file('downloadPath.jhtml?id_='+id+'&tablename='+table);
                    },
                    //批量下载
                    onDownAllPath:function(){
                        if(user!="root"){
                            AOS.err("没有权限!");
                            return;
                        }
                        //得到选中的条目
                        var grid_data=Ext.getCmp("uploadpanel").getStore();
                        for(var i= 0 ;i< grid_data.data.length;i++){
                            var row = grid_data.getAt(i);
                            var id=row.get('pid');
                            var table=Ext.getCmp("tablename").getValue();
                            AOS.file('downloadPath.jhtml?id_='+id+'&tablename='+table);
                        }
                    },
                    upload_complete_handler : function(file){
                        if(user!="root"){
                            AOS.err("没有权限!");
                            return;
                        }
                        var me =Ext.getCmp("uploadpanel");
                        var table=Ext.getCmp("tablename").getValue();
                        AOS.ajax({
                            params: {tid: record.data.id_,tablename:table},
                            url: 'getPath.jhtml',
                            ok: function (data) {
                                for(i in data){
                                    me.store.getAt(file.index).set({"pid":data[i].id_,"tid":data[i].tid});
                                }
                            }
                        });
                    },
                    post_params:{tid:record.data.id_,
                        tablename: Ext.getCmp("tablename").getValue()
                    },
                    file_size_limit : 10000,//MB

                    flash_url : "${cxt}/static/swfupload/swfupload.swf",
                    flash9_url : "${cxt}/static/swfupload/swfupload_f9.swf",
                    upload_url : "${cxt}/archive/upload/archiveUpload.jhtml?tm="+record.data.tm
                });

                var w_data_path = new Ext.Window({
                    title : '电子文件',
                    width : 700,
                    modal:true,
                    closeAction : 'destroy',
                    items:[uploadPanel]
                });
                w_data_path.on("show",w_data_path_onshow);
                //w_data_path.on("close",w_data_path_onclose);
                w_data_path.show();
            }
            function w_data_path_onshow() {
                var tablenameId=Ext.getCmp("tablename").getValue();
                var record = AOS.selectone(Ext.getCmp('_g_param'));
                var me=Ext.getCmp("uploadpanel");
                me.store.removeAll();
                AOS.ajax({
                    params: {tid: record.data.tablename_id,tablename:tablenameId},
                    url: 'getPath.jhtml',
                    ok: function (data) {
                        for(i in data){
                            me.store.add({
                                pid:data[i].id_,
                                tid:data[i].tid,
                                name:data[i]._path,
                                fileName:data[i].filename,
                                type:data[i].filetype,
                                percent:100,
                                status:-4,
                            });
                        }
                    }
                });

            }
            function w_data_path_onclose(){
                _g_param_store.load();
            }

            function fn_lyzt_render(value, metaData, record, rowIndex, colIndex,
                                    store) {
                if (value === 1) {
                    return '已查到';
                } else {
                    return '未查到';
                }
            }

            function fn_path_render(value, metaData, record, rowIndex, colIndex,
                                    store) {
                if (value >= 1) {
                    return '<img src="${cxt}/static/icon/picture.png" />';
                } else {
                    return '<img src="${cxt}/static/icon/picture_empty.png" />';
                }
            }
            function _no_query(){
                var selection = AOS.selection(_g_param,'id_');
                AOS.ajax({
                    url:'insertMakeDetail.jhtml',
                    params:{
                        formid:_recordid.getValue(),
                        aos_rows_: selection,
                        //tid:record.data.id_,
                        tablename:Ext.getCmp("tablename").getValue(),
                        cxjg:0
                    },
                    ok:function (data) {
                        AOS.tip(data.appmsg);
                    }
                })
            }
            function _yes_query(){
                var selection = AOS.selection(_g_param,'id_');
                AOS.ajax({
                    url:'insertMakeDetail.jhtml',
                    params:{
                        formid:_recordid.getValue(),
                        aos_rows_: selection,
                        //tid:record.data.id_,
                        tablename:Ext.getCmp("tablename").getValue(),
                        cxjg:1
                    },
                    ok:function (data) {
                        AOS.tip(data.appmsg);

                    }
                })
            }
            function _yes_provide(){
                AOS.ajax({
                    url:'updateMake.jhtml',
                    params:{
                        id_:_recordid.getValue(),
                        nftg:'可以提供'
                    }
                })
            }
            function _no_provide(){
                _w_provide.show();
            }
            function  _f_provide_save(){
                AOS.ajax({
                    forms:_f_provide,
                    url:'updateMake.jhtml',
                    params:{
                        id_:_recordid.getValue(),
                        nftg:'不可提供'
                    }
                })
                var parenttab=parent.closetab();
                //针对纯的grid的刷新
                var parentframes=parent.frames["_id_tab_d98465c65f9644628552c5b6286d9b36.iframe-frame"];
                var aa=parentframes.Ext.getCmp('_g_make_detail');
                aa.store.reload();
                //刷新真个页面
                var curtab = parenttab.getActiveTab();
                parenttab.remove(curtab);
            }
            function fn_column_sub_onclick(){
                var record = AOS.selectone(Ext.getCmp('_g_data'));
                var msg = AOS.merge('您将要提交该单据！并且启动流程您将无法进行修改和删除！！！ 点击确认进行提交 , 点击取消取消该操作');
                AOS.confirm(msg,function (btn){
                    if(btn === 'cancel'){
                        AOS.tip('操作取消');
                        return;
                    }
                    AOS.ajax({
                        url:'startInstance.jhtml',
                        params:{
                            id_:record.data.id_,
                            state:1
                        },
                        ok:function(data){
                            Ext.getCmp('_g_data').store.load();
                            AOS.tip(data.appmsg);
                        }
                    })
                })
            }
            function fn_onselect(e){
                Ext.getCmp("tablename").setValue(e.value);
                var user=Ext.getCmp("user").getValue();
                var g=AOS.selectone(_g_data);
                var fbwd=AOS.selectone(_g_data).data.fbwd;
                var fbid=AOS.selectone(_g_data).data.id_;
                //不重新进入页面
                var params = {
                    tablename : e.value,
                    fbid:fbid,
                    fbwd:fbwd
                };
                Ext.getCmp("query_list").setValue("");
                _g_param_store.getProxy().extraParams = params;
                _g_param_store.load();
                //window.location.href="init.jhtml?lx=hlw&user="+user+"&tablename="+e.value+"&tabledesc="+e.rawValue+"&recordid="+_recordid.getValue();

            }
            function _f_data_query(){
                var params = AOS.getValue('_f_query2');
                var form = Ext.getCmp('_f_query2');
                var tmp = 5;
                for (var i = 1; i <= tmp; i++) {
                    var str = form.down("[name='filedname" + i + "']");
                    var filedname = str.getValue();
                    if (filedname == null) {
                        params["filedname" + i] = str.regexText;
                    }
                    var emptyfiledcnname = str.emptyText;
                    var filedcnname = Ext.getCmp("filedname" + i).getRawValue();
                    if (emptyfiledcnname == filedcnname && filedcnname != null && filedcnname != "") {
                        params["filedcnname" + i] = filedcnname;
                    } else if (filedcnname == null || filedcnname == "") {
                        params["filedcnname" + i] = emptyfiledcnname;
                    }
                }
                params["tablename"]=Ext.getCmp("tablename").getValue();
                params["fbid"]=AOS.selectone(_g_data).data.id_;
                params["fbwd"]=AOS.selectone(_g_data).data.fbwd;
                AOS.ajax({
                    params:params,
                    url: 'saveQueryData_list_sjfb.jhtml',
                    ok: function (data) {
                        //此时在隐藏域中存入查询条件
                        Ext.getCmp("query_list").setValue(Ext.getCmp("query_list").getValue()+" "+data.appmsg);
                    }
                });
                _g_param_store.getProxy().extraParams = params;
                _g_param_store.load();

                _w_query_q.hide();
                AOS.reset(_f_query2);

            }
            function _g_add_data(){
                //弹出选择档案窗口
                _w_select_edit_archive.show();
                Ext.getCmp("_g_select_edit_archive").getStore().removeAll();
            }
            //查询窗口展开
            function _w_select_edit_query_show() {
                //判断是不是
                Ext.getCmp("query").setValue("");
                _w_query_edit_select_q.show();
            }
            //选择数据表后展开数据
            function by_select_sjkmc(){
                //根据选择的名称
                var sjkmc_value = Ext.getCmp("sjkmc").value;
                var params = {
                    tablename : sjkmc_value,
                    id_ : Ext.getCmp("aos_module_id_").getValue()
                };
                //这个Store的命名规则为：表格ID+"_store"。
                _g_select_edit_archive_store.getProxy().extraParams = params;
                _g_select_edit_archive_store.load();
                Ext.getCmp("ids").setValue("");
                Ext.getCmp("dhs").setValue("");
                Ext.getCmp("query").setValue("");
            }
            //获取字段数据列表
            function combobox_tableFileldlist(){
                var params = {
                    tablename : Ext.getCmp("listTablename").value
                };
                //这个Store的命名规则为：表格ID+"_store"。ok_save
                filednames1_store.getProxy().extraParams = params;
                filednames1_store.load();
                filednames2_store.getProxy().extraParams = params;
                filednames2_store.load();
                filednames3_store.getProxy().extraParams = params;
                filednames3_store.load();
                filednames4_store.getProxy().extraParams = params;
                filednames4_store.load();
                filednames5_store.getProxy().extraParams = params;
                filednames5_store.load();
            }
            //数据选择
            function load_archive(){
                var fbid=AOS.selectone(_g_data).data.id_;
                var fbwd=AOS.selectone(_g_data).data.fbwd;
                var params = {
                    fbid:fbid,
                    fbwd:fbwd,
                    tablename : Ext.getCmp("tablename").getValue()
                };
                _g_select_edit_archive_store.getProxy().extraParams = params;
                _g_select_edit_archive_store.load();
            }
            //用于涉及到全部选择，排除分页的影响

            function ok_save_archive_edit(){
                var tablename=Ext.getCmp("tablename").getValue();
                var count=AOS.rows(_g_select_edit_archive);
                var ids=Ext.getCmp('ids').getValue();
                var dhs=Ext.getCmp('dhs').getValue();
                var fbid=AOS.selectone(_g_data).data.id_;
                var fbwd=AOS.selectone(_g_data).data.fbwd;
                var params = {
                    fbid:fbid,
                    fbwd:fbwd,
                    ids:ids,
                    dhs:dhs,
                    tablename:tablename
                };
                AOS.ajax({
                    url: 'updatehlw_data.jhtml',
                    params:params,
                    ok: function (data) {
                        if(data.appcode===1){
                            AOS.tip("添加成功!");
                            _g_param_store.load();
                            _w_select_edit_archive.hide();
                            //_g_receive_query();
                        }else if(data.appcode===-1){
                            AOS.tip("添加失败!");
                        }
                    }
                });
            }
            //保存当前选中的档案信息
            function ok_save_All_archive(){
                var model = _g_select_edit_archive.getSelectionModel();
                model.selectAll();//选择所有行
                var ids="";
                var dhs="";
                //走后台进行当前条件的所有条目的id集合
                AOS.ajax({
                    url: 'getQueryIds.jhtml',
                    params:{'tablename':Ext.getCmp("tablename").getValue(),
                        'query':Ext.getCmp("query").getValue()},
                    ok: function(data){
                        var ids="";
                        var dhs="";
                        for(k in data){
                            if(k==0){
                                ids=data[k].id_;
                                dhs=data[k].dh;
                            }else{
                                ids=ids+","+data[k].id_;
                                dhs=dhs+","+data[k].dh;
                            }
                        }
                        Ext.getCmp('ids').setValue(ids);
                        Ext.getCmp('dhs').setValue(dhs);
                        _w_select_archive.hide();
                    }
                });
            }
            function _f_select_edit_data_query(){
                var params = AOS.getValue('_f_select_edit_query');
                var form = Ext.getCmp('_f_select_edit_query');
                var tablename = Ext.getCmp("tablename").getValue();
                for(var i=1;i<=5;i++){
                    var str = form.down("[name='filednames"+i+"']");
                    var filedname =str.getValue();
                    if(filedname==null){
                        params["filednames"+i]=str.regexText;
                    }
                }
                //params["query_old"]=Ext.getCmp("query").getValue();
                params["tablename"]=tablename;
                params["fbid"]=AOS.selectone(_g_data).data.id_;
                params["fbwd"]=AOS.selectone(_g_data).data.fbwd;
                _g_select_edit_archive_store.getProxy().extraParams = params;
                _g_select_edit_archive_store.load();
                AOS.ajax({
                    params:params,
                    url: 'saveQueryData_sjfb.jhtml',
                    ok: function (data) {
                        //此时在隐藏域中存入查询条件
                        Ext.getCmp("query").setValue(Ext.getCmp("query").getValue()+" "+data.appmsg);
                    }
                });
                _w_query_edit_select_q.hide();
                AOS.reset(_f_select_edit_query);
            }
            //上一页
            function _f_previous_data() {
                var count = Ext.getCmp("_g_data").getStore().getCount();
                var me = Ext.getCmp("_g_data").getSelectionModel().getSelection();
                //var record = AOS.selectone(_g_data);
                //得到执行行的坐标
                var rowIndex = Ext.getCmp("_g_data").getStore().indexOf(me[0]);
                if (rowIndex == 0) {
                    AOS.err("当前第一条!");
                    return;
                }
                var s = Ext.getCmp("_g_data").getStore().getAt(rowIndex - 1);
                //原先行取消选中
                Ext.getCmp("_g_data").getSelectionModel().deselect(rowIndex);
                //此时让光标选中上一行
                Ext.getCmp("_g_data").getSelectionModel().select(rowIndex - 1, true);
                //组件被显示后触发。
                Ext.getCmp("_f_data_i").form.setValues(s.data);
            }

            //下一页
            function _f_next_data() {
                var count = Ext.getCmp("_g_data").getStore().getCount();
                var me = Ext.getCmp("_g_data").getSelectionModel().getSelection();
                //var record = AOS.selectone(_g_data);
                //得到执行行的坐标
                var rowIndex = Ext.getCmp("_g_data").getStore().indexOf(me[0]);
                if (rowIndex == count - 1) {
                    AOS.err("当前最后一条!");
                    return;
                }
                var s = Ext.getCmp("_g_data").getStore().getAt(rowIndex + 1);
                //原先行取消选中
                Ext.getCmp("_g_data").getSelectionModel().deselect(rowIndex);
                //此时让光标选中下一行
                Ext.getCmp("_g_data").getSelectionModel().select(rowIndex + 1, true);
                //组件被显示后触发。
                Ext.getCmp("_f_data_i").form.setValues(s.data);
            }

        </script>
    </aos:onready>
</aos:html>
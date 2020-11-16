<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="/WEB-INF/tld/aos.tld" prefix="aos"%>
<%@ taglib uri="/WEB-INF/tld/cll.tld" prefix="c"%>
<aos:html>
    <aos:head title="在编课题">
        <aos:include lib="ext,swfupload" />
        <script type="text/javascript" src="${cxt}/static/swfupload_edit/swfupload.js"></script>
        <script type="text/javascript" src="${cxt}/static/swfupload_edit/swfupload.queue.js"></script>
        <script type="text/javascript" src="${cxt}/static/swfupload_edit/fileprogress.js"></script>
        <script type="text/javascript" src="${cxt}/static/swfupload_edit/handlers.js"></script>
        <aos:base href="compilation/articles" />
    </aos:head>
    <aos:body>
    </aos:body>
    <aos:onready>
        <aos:viewport layout="border" id="viewport">
            <aos:gridpanel id="_g_compilation" url="listCompilation.jhtml" region="north"  splitterBorder="1 0 1 0" split="true"
                           onrender="_g_compilation_query" pageSize="10"  onitemclick="get_a_compilation"   enableLocking="true">
                <aos:docked>
                    <aos:hiddenfield id="appraisal" name="appraisal"/>
                    <aos:hiddenfield id="filename" name="filename"/>
                    <aos:hiddenfield name="byrwbh" id="byrwbh"
                                     value="${byrwbh}" />
                    <aos:hiddenfield name="_classtree" id="_classtree"
                                     value="${cascode_id_}" />
                    <aos:hiddenfield name="cascode_id_" id="cascode_id_"
                                     value="${cascode_id_}" />
                    <aos:hiddenfield name="aos_module_id_" id="aos_module_id_"
                                     value="${aos_module_id_}" />
                    <aos:dockeditem xtype="tbtext" text="任务信息" />
                    <aos:dockeditem text="新建任务" icon="add2.png"  onclick="_g_add_rw" id="_f_add_rw"/>
                    <aos:dockeditem text="修改" icon="edit.png" onclick="_g_edit_rw"  id="_f_rw_edit"/>
                    <aos:dockeditem text="删除" icon="del.png" onclick="_w_del_rw"  id="_f_del_rw"/>
                    <aos:dockeditem text="添加附件" id="openExcel" icon="folder2.png" />
                    <aos:dockeditem text="打开附件" id="openFile" icon="folder2.png" onclick="open_file"/>
                    <aos:dockeditem text="提交审核" icon="more/archive-extract-3.png" onclick="_g_receive_rw" id="_f_receive_rw"/>
                    <aos:dockeditem text="领导审核" icon="folder8.png" onclick="_w_leader_examine" id="_f_leader_examine" />
                    <aos:dockeditem text="任务分配" icon="folder8.png" onclick="_w_distribution_rw" id="_f_distribution_rw" />
                    <aos:dockeditem text="编研日志" icon="more/view-history.png" onclick="operation_login" id="_w_formal_receive1" />
                    <aos:dockeditem text="撰稿" icon="edit.png" id="_zhuangao_operator"
                                    onclick="_check_zhuangao" />
                    <aos:dockeditem text="合稿" icon="more/arrow-merge.png" id="_first_operator"
                                    onclick="_check_first" />
                    <aos:dockeditem text="校对" icon="more/checkbox-2.png" id="_next_operator"
                                    onclick="_check_next" />
                    <aos:dockeditem text="总编辑" icon="more/computer-edit.png" id="_last_operator"
                                    onclick="_check_last" />
                </aos:docked>
                <aos:selmodel type="row" mode="multi" />
                <aos:column dataIndex="id_" header="流水号" hidden="true" />
                <aos:column dataIndex="zt_id" header="专题id" hidden="true" />
                <aos:column dataIndex="byrwbh" header="编研任务编号" width="150"/>
                <aos:column dataIndex="byrwmc" header="编研任务名称" />
                <aos:column dataIndex="ztyq" header="总体要求" />
                <aos:column dataIndex="bysj" header="编研时间" />
                <aos:column dataIndex="ckfw" header="参考范围"  />
                <aos:column dataIndex="bybz" header="编研步骤"  />
                <aos:column dataIndex="nd" header="年度"  />
                <aos:column dataIndex="zgr"  header="撰稿人"/>
                <aos:column dataIndex="zgr_cn"  header="撰稿人中文"/>
                <aos:column dataIndex="hgr"  header="合稿人"/>
                <aos:column dataIndex="hgr_cn"  header="合稿人中文"/>
                <aos:column dataIndex="jdr" header="校对人"  />
                <aos:column dataIndex="jdr_cn" header="校对人中文"  />
                <aos:column dataIndex="zbj" header="总编辑" />
                <aos:column dataIndex="zbj_cn" header="总编辑中文" />
                <aos:column dataIndex="qtsm" header="其他说明" />
                <aos:column dataIndex="shry" header="审核人"  />
                <aos:column dataIndex="shsj" header="审核时间"  />
                <aos:column dataIndex="shyj" header="审核意见"  />
                <aos:column dataIndex="hg_time" header="合稿时间" />
                <aos:column dataIndex="jd_time" header="校对时间" />
                <aos:column dataIndex="zbj_time" header="定稿时间" />
                <aos:column dataIndex="flag_submit" header="提交状态" />
                <aos:column dataIndex="flag_examine" header="审核状态" />
                <aos:column dataIndex="cjr" header="创建人" />
                <aos:column dataIndex="_path" header="附件" />
                <aos:column header="" flex="1" />
            </aos:gridpanel>
            <aos:gridpanel id="_g_data" url="listAccounts.jhtml"  region="center" split="true"
                            pageSize="${pagesize }"  enableLocking="true">
                <aos:docked>
                    <aos:dockeditem xtype="tbtext" text="编研数据库数据" />
                    <aos:dockeditem text="显示" icon="picture.png"  id="_f_filename_message"
                                    onclick="_w_picture_show"   />
                </aos:docked>
                <aos:selmodel type="row" mode="multi" />
                <aos:column dataIndex="id_" header="流水号" hidden="true" />
                <aos:column dataIndex="_path" header="电子文件"
                            rendererFn="fn_path_render" />
                <aos:column header="全宗单位" dataIndex="qzdw" />
                <aos:column header="档号" dataIndex="dh" celltip="true" />
                <aos:column header="题名" dataIndex="tm"  width="80" />
                <aos:column header="年度" dataIndex="nd" />
                <aos:column header="保管期限" dataIndex="bgqx" />
                <aos:column header="形成时间" dataIndex="xcsj"  />
                <aos:column header="盒号" dataIndex="hh" />
                <aos:column header="备注" dataIndex="bz" flex="1" celltip="true" />
                <aos:column header="数据库" dataIndex="tablename"/>
                <aos:column header="" flex="1" />
            </aos:gridpanel>
            <aos:window id="_w_rw" title="新增编研任务" > <%--onshow="_new_task"--%>
                <aos:formpanel id="_f_rw" width="420" layout="column" labelWidth="90" >
                    <aos:hiddenfield name="ids" id="ids"/>
                    <aos:textfield name="byrwbh" fieldLabel="编研任务编号"   columnWidth="0.99"/>
                    <aos:textfield name="byrwmc" fieldLabel="编研任务名称"   columnWidth="0.99"/>
                    <aos:combobox fieldLabel="专题列表" name="zt_list" allowBlank="false"
                                  fields="['name','id_']" id="zt_list" displayField="name" valueField="id_"
                                  editable="false" columnWidth="0.99" url="listzt_by.jhtml" width="300"/>
                    <aos:combobox name="nd" fieldLabel="年度" dicField="nd" emptyText="请选择..." columnWidth="0.99" allowBlank="false"/>
                    <aos:textfield name="ztyq" fieldLabel="总体要求" columnWidth="0.99" />
                    <aos:datefield name="bysj" fieldLabel="编研时间" format="Y-m-d"
                                   columnWidth="0.99" />
                    <aos:textareafield name="ckfw" fieldLabel="参考范围" columnWidth="0.99" />
                    <aos:textfield name="bybz" fieldLabel="编研步骤" columnWidth="0.99" />
                    <aos:textfield name="cjr" fieldLabel="创建人"  columnWidth="0.99"/>
                    <aos:textareafield name="qtsm" fieldLabel="其他说明" columnWidth="0.99" />
                </aos:formpanel>
                <aos:docked dock="bottom" ui="footer">
                    <aos:dockeditem xtype="tbfill" />
                    <aos:dockeditem onclick="_f_rw_submit" text="保存" icon="ok.png" />
                    <aos:dockeditem onclick="#_w_rw.hide();" text="关闭" icon="close.png" />
                </aos:docked>
            </aos:window>
            <aos:window id="_w_edit_rw" title="修改编研任务" >
                <aos:formpanel id="_f_edit_rw" width="420" layout="column" labelWidth="90">
                    <aos:hiddenfield name="id_" />
                    <aos:hiddenfield name="sjkmc" />
                    <aos:textfield name="byrwbh" fieldLabel="编研任务编号"   columnWidth="0.99"/>
                    <aos:textfield name="byrwmc" fieldLabel="编研任务名称"   columnWidth="0.99" />
                    <aos:combobox name="nd" fieldLabel="年度" dicField="nd" emptyText="请选择..." columnWidth="0.99" allowBlank="false"/>
                    <aos:textfield name="ztyq" fieldLabel="总体要求" columnWidth="0.99" />
                    <aos:datefield name="bysj" fieldLabel="编研时间" format="Y-m-d"
                                   columnWidth="0.99" />
                    <aos:textareafield name="ckfw" fieldLabel="参考范围" columnWidth="0.99" />
                    <aos:textfield name="bybz" fieldLabel="编研步骤" columnWidth="0.99" />
                    <aos:textfield name="cjr" fieldLabel="创建人"  columnWidth="0.99"/>
                    <aos:textareafield name="qtsm" fieldLabel="其他说明" columnWidth="0.99" />
                </aos:formpanel>
                <aos:docked dock="bottom" ui="footer">
                    <aos:dockeditem xtype="tbfill" />
                    <aos:dockeditem text="上一条" icon="more/go-previous.png"  onclick="_f_previous_data"/><%----%>
                    <aos:dockeditem text="下一条" icon="more/go-next.png" onclick="_f_next_data"/><%----%>
                    <aos:dockeditem onclick="_f_rw_edit_submit" text="保存" icon="ok.png" />
                    <aos:dockeditem onclick="#_w_edit_rw.hide();" text="关闭" icon="close.png" />
                </aos:docked>
            </aos:window>
            <aos:window id="_w_next_kf_message" title="审核意见" width="500" >
                <aos:formpanel  id="_f_next_kf_message" width="480">
                    <aos:textareafield fieldLabel="意见说明" id="next_kf_message"  name="next_kf_message" width="450" allowBlank="false"/>
                </aos:formpanel>
                <aos:docked dock="bottom" ui="footer">
                    <aos:dockeditem xtype="tbfill" />
                    <aos:dockeditem text="通过" onclick="_save_next_kf_message" icon="agree.png" />
                    <aos:dockeditem text="未通过" onclick="no_save_next_kf_message" icon="against.png" />
                    <aos:dockeditem onclick="#_w_next_kf_message.hide();" text="关闭"
                                    icon="close.png" />
                </aos:docked>
            </aos:window>

            <aos:window id="_w_select_archive" title="编研数据选择" onshow="_w_select_archive_onshow" width="800" height="500">
            <aos:gridpanel id="_g_select_archive" url="listArchive.jhtml" region="center" width="770" height="460"
                           autoScroll="true" pageSize="20" enableLocking="true" >
                <aos:docked forceBoder="0 0 1 0">
                    <aos:dockeditem xtype="tbtext" text="数据列表" />
                    <aos:dockeditem text="查询" icon="query.png"
                                    onclick="_w_select_query_show" />
                    <aos:dockeditem  text="确定" icon="ok.png"
                                    onclick="ok_save_archive" />
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

            <aos:window id="_w_select_edit_archive" title="编研数据选择" onshow="_w_select_edit_archive_onshow" width="800" height="500">
                <aos:gridpanel id="_g_select_edit_archive" url="listArchive_edit.jhtml" region="north" width="770" height="460"
                               autoScroll="true" pageSize="20" enableLocking="true" hidePagebar="false">
                    <aos:docked forceBoder="0 0 1 0">
                        <aos:dockeditem xtype="tbtext" text="数据列表" />
                        <aos:dockeditem text="查询" icon="query.png"
                                        onclick="_w_select_edit_query_show" />
                            <aos:dockeditem  text="确定" icon="ok.png"
                                            onclick="ok_save_archive_edit" />
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
            <aos:window id="_w_files" title="电子文件" autoScroll="true" width="800"
                        border="true" height="500" onshow="get_files">
                <aos:gridpanel id="_g_files" region="east" url="getFiles.jhtml"
                               hidePagebar="true" split="true" width="800">
                    <aos:docked forceBoder="0 0 1 0">
                        <aos:dockeditem xtype="tbtext" text="电子文件列表" />
                        <aos:dockeditem text="查看" icon="query.png" onclick="_w_files_look" />
                    </aos:docked>
                    <aos:column type="rowno" />
                    <aos:column header="流水号" dataIndex="id_" hidden="true" />
                    <aos:column header="档案号" dataIndex="tid" width="60" hidden="true" />
                    <aos:column header="上传文件名" dataIndex="_path" width="90" />
                    <aos:column header="路径" dataIndex="dirname" width="90" />
                    <aos:column header="上传时间" dataIndex="sdatetime" width="90" />
                    <aos:column header="文件名" dataIndex="_s_path" width="60" />
                </aos:gridpanel>
                <aos:docked dock="bottom" ui="footer">
                    <aos:dockeditem xtype="tbfill" />
                    <aos:dockeditem onclick="#_w_files.close();" text="关闭"
                                    icon="close.png" />
                </aos:docked>
            </aos:window>
            <aos:window id="_w_query_edit_select_q" title="查询" width="720" autoScroll="true"
                        layout="fit">
                <aos:tabpanel id="_tabpanel_edit_select" region="center" activeTab="0"
                              bodyBorder="0 0 0 0" tabBarHeight="30">
                    <aos:tab title="列表式搜索" id="_tab_select_edit_org">
                        <aos:formpanel id="_f_select_edit_query" layout="column" columnWidth="1">
                            <aos:hiddenfield name="tablename" value="${tablename }" />
                            <aos:hiddenfield name="columnnum"  value="7" />
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
                                <aos:dockeditem onclick="_f_select_edit_data_query" text="确定" icon="ok.png" />
                                <aos:dockeditem onclick="#_w_query_edit_select_q.hide();" text="关闭"
                                                icon="close.png" />
                            </aos:docked>
                        </aos:formpanel>
                    </aos:tab>
                </aos:tabpanel>
            </aos:window>
            <aos:window id="_w_task_reset" title="分配"  >
                <aos:formpanel id="_g_task_reset" width="800" layout="anchor"
                               >
                    <aos:hiddenfield name="id_"  />
                    <aos:textfield name="byrwbh" width="400" fieldLabel="编研任务编号"  readOnly="true" columnWidth="0.49"/>
                    <aos:textfield name="byrwmc" fieldLabel="编研任务名称"  readOnly="true"   columnWidth="0.49"/>
                    <aos:textfield fieldLabel="专题名称" name="ztmc"  columnWidth="0.49" readOnly="true" />
                    <aos:combobox name="zhuangaoren_compilation" id="zhuangaoren_compilation" fieldLabel="撰稿人" allowBlank="false"
                                  columnWidth="1.0" fields="['account_','name_']" displayField="name_"
                                  valueField="account_" onselect="add_zhuangaoren_compilation"
                                  url="queryUsers.jhtml">
                    </aos:combobox>
                    <aos:gridpanel id="_g_zhuangaoren_compilation"  split="true" hidePagebar="true" autoScroll="true"
                                   height="50" width="800" drag="true">
                        <aos:menu>
                            <aos:menuitem text="删除" onclick="_g_zhuangaoren_compilation_del" icon="del.png" />
                        </aos:menu>
                        <aos:column header="撰稿人" dataIndex="fieldennames" width="200"/>
                        <aos:column header="撰稿人中文" dataIndex="fieldcnnames" width="200"/>
                    </aos:gridpanel>


                    <aos:combobox name="first_compilation" id="first_compilation" fieldLabel="合稿人" allowBlank="false"
                                  columnWidth="1.0" fields="['account_','name_']" displayField="name_"
                                  valueField="account_" onselect="add_first_compilation"
                                  url="queryUsers.jhtml">
                    </aos:combobox>
                    <aos:gridpanel id="_g_first_compilation"  split="true" hidePagebar="true" autoScroll="true"
                                   height="50" width="800" drag="true">
                        <aos:menu>
                            <aos:menuitem text="删除" onclick="_g_first_compilation_del" icon="del.png" />
                        </aos:menu>
                        <aos:column header="合稿人" dataIndex="fieldennames" width="200"/>
                        <aos:column header="合稿人中文" dataIndex="fieldcnnames" width="200"/>
                    </aos:gridpanel>
                    <aos:combobox name="next_compilation" id="next_compilation" fieldLabel="校对人" allowBlank="false"
                                  columnWidth="1.0" fields="['account_','name_']" displayField="name_"
                                  valueField="account_" onselect="add_next_compilation"
                                  url="queryUsers.jhtml">
                    </aos:combobox>
                    <aos:gridpanel id="_g_next_compilation" split="true" hidePagebar="true" autoScroll="true"
                                   height="50" width="800" drag="true">
                        <aos:menu>
                            <aos:menuitem text="删除" onclick="_g_next_compilation_del" icon="del.png" />
                        </aos:menu>
                        <aos:column header="校对人" dataIndex="fieldennames" width="200"/>
                        <aos:column header="校对人中文" dataIndex="fieldcnnames" width="200"/>
                    </aos:gridpanel>
                    <aos:combobox name="last_compilation" id="last_compilation" fieldLabel="总编辑人" allowBlank="false"
                                  columnWidth="1.0" fields="['account_','name_']" displayField="name_"
                                  valueField="account_" onselect="add_last_compilation"
                                  url="queryUsers.jhtml">
                    </aos:combobox>
                    <aos:gridpanel id="_g_last_compilation" split="true" hidePagebar="true" autoScroll="true"
                                   height="50" width="800" drag="true">
                        <aos:menu>
                            <aos:menuitem text="删除" onclick="_g_last_compilation_del" icon="del.png" />
                        </aos:menu>
                        <aos:column header="总编辑人" dataIndex="fieldennames" width="200"/>
                        <aos:column header="总编辑人中文" dataIndex="fieldcnnames" width="200"/>
                    </aos:gridpanel>
                </aos:formpanel>
                <aos:docked dock="bottom" ui="footer">
                    <aos:dockeditem xtype="tbfill" />
                    <aos:dockeditem onclick="_w_details_add" text="确定" icon="ok.png" />
                    <aos:dockeditem onclick="#_w_task_reset.hide();" text="关闭" icon="close.png" />
                </aos:docked>
            </aos:window>
            <aos:window id="_w_operation" title="操作记录" autoScroll="true" height="500" width="800" onshow="_w_operation_show">
                <aos:gridpanel id="_g_operation"  url="listoperationlogin.jhtml"
                               hidePagebar="true"  width="5000" autoScroll="true">
                    <aos:docked>
                        <aos:dockeditem xtype="tbtext" text="列表" />
                        <aos:dockeditem onclick="_w_details_open" text="详情" icon="query.png" />
                    </aos:docked>
                    <aos:column type="rowno" />
                    <aos:column  dataIndex="id_" hidden="true" />
                    <aos:column header="操作人" dataIndex="users" width="200" />
                    <aos:column header="操作时间" dataIndex="operate_time" width="200" />
                    <aos:column header="操作描述" dataIndex="operate_description" width="200" />
                    <aos:column header="操作信息" dataIndex="compilation_message" width="200" />
                    <aos:column header="操作类型" dataIndex="type" width="200" />
                    <aos:column header="" flex="1" />
                </aos:gridpanel>
                <aos:docked dock="bottom" ui="footer">
                    <aos:dockeditem xtype="tbfill" />
                    <aos:dockeditem onclick="#_w_operation.close();" text="关闭" icon="close.png" />
                </aos:docked>
            </aos:window>


            <aos:window id="_w_zhuangao_compilation_message" title="详情" width="600" onshow="zhuangao_compilation_load">
                <aos:formpanel  id="_f_zhuangao_compilation_message" width="580" >
                    <aos:htmleditor fieldLabel="编研信息" anchor="80%" height="500" labelAlign="top"  name="zhuangao_compilation_message" id="zhuangao_compilation_message" columnWidth="1" allowBlank="false"/>
                </aos:formpanel>
                <aos:docked dock="bottom" ui="footer">
                    <aos:dockeditem xtype="tbfill" />
                </aos:docked>
            </aos:window>

            <aos:window id="_w_fj_open_path" title="编研数据" onshow="open_path_onload" width="600" height="500">
                <aos:formpanel  id="_f_open_path" width="570" height="480">
                    <aos:htmleditor fieldLabel="附件信息" anchor="80%" height="470" labelAlign="top"  name="_path_message" id="_path_message" columnWidth="1" allowBlank="false"/>
                </aos:formpanel>
                <script type="text/javascript">
                    //< onclick="_select_edit_archive" icon="more/select.png"   columnWidth="0.29"/>-->
                    /*var btn = Ext.get('openExcel-btnInnerEl');
                    //var em = btn.getEl().childd('em');
                    btn.setStyle({
                        position : 'relative',
                        display : 'block'
                    });
                    btn.createChild({
                        tag : 'div',
                        id : 'div_exam'
                    });
                    var swfu = new SWFUpload({
                        upload_url : "${cxt}/archive/upload/uploadSubject_fj.jhtml", //接收上传的服务端url
                        flash9_url : "${cxt}/static/swfupload/swfupload_f9.swf",
                        flash_url : "${cxt}/static/swfupload/swfupload.swf",//swfupload压缩包解压后swfupload.swf的url
                        button_placeholder_id : "div_exam",//上传按钮占位符的id
                        file_types:"*.doc",
                        file_types_description:"Word文件",
                        file_size_limit : "20480",//用户可以选择的文件大小，有效的单位有B、KB、MB、GB，若无单位默认为KB
                        button_width: 40, //按钮宽度
                        button_height: 20, //按钮高度
                        button_text: "附件",//按钮文字
                        file_queue_limit : "0",
                        file_upload_limit : "1",
                        use_query_string:true,
                        upload_success_handler : uploadSuccess,
                        file_dialog_complete_handler:fileDialogComplete
                    });
                    Ext.get(swfu.movieName).setStyle({
                        position:'absolute',
                        left:"20px",
                        opacity:0
                    });*/
                    function uploadSuccess(file, serverData) {
                        _g_compilation_store.reload();
                    }
                    function fileDialogComplete(numFilesSelected, numFilesQueued) {
                        var selection = AOS.selection(_g_compilation, 'id_');
                        if (AOS.empty(selection)) {
                            AOS.tip('请选择要添加附件的任务!');
                            return;
                        }
                        //得到当前选中的任务id值
                        swfu.setPostParams({
                            "id_":AOS.selectone(_g_compilation).raw.id_
                        });
                        swfu.startUpload();

                    }
                    window.onload=function() {
                        Ext.getCmp("_g_compilation").setHeight(document.body.scrollHeight * (1/ 2));
                        Ext.getCmp("_g_data").setHeight(document.body.scrollHeight * (1 / 2));
                        var compilation_flag = "<%=session.getAttribute("compilation_flag_")%>";
                        Ext.getCmp("_f_add_rw").hide();//新建任务
                        Ext.getCmp("_f_rw_edit").hide();
                        Ext.getCmp("_f_del_rw").hide();//删除任务
                        Ext.getCmp("_f_leader_examine").hide();
                        Ext.getCmp("_f_receive_rw").hide();//提交审核
                        Ext.getCmp("_zhuangao_operator").hide();
                        Ext.getCmp("_first_operator").hide();
                        Ext.getCmp("_next_operator").hide();
                        Ext.getCmp("_last_operator").hide();
                        var bianyan_flag = "<%=session.getAttribute("bianyan_flag")%>";
                        if(bianyan_flag!=null&&bianyan_flag!="") {
                            if (bianyan_flag.indexOf("创建人") != -1) {
                                Ext.getCmp("_f_add_rw").show();//新建任务
                                Ext.getCmp("_f_rw_edit").show();
                                Ext.getCmp("_f_del_rw").show();//删除任务
                            }
                        }
                        var zhuangao = "<%=request.getAttribute("zhuangao")%>";
                        var first = "<%=request.getAttribute("first")%>";
                        var next = "<%=request.getAttribute("next")%>";
                        var last = "<%=request.getAttribute("last")%>";
                        //var one = "<%=request.getAttribute("one")%>";
                        //var two = "<%=request.getAttribute("two")%>";
                        //var three = "<%=request.getAttribute("three")%>";
                        //var four = "<%=request.getAttribute("four")%>";
                        if (zhuangao === "1") {
                            Ext.getCmp("_zhuangao_operator").show();
                        }
                        if (first === "1") {
                            Ext.getCmp("_first_operator").show();
                        }
                        if (next === "1") {
                            Ext.getCmp("_next_operator").show();
                        }
                        if (last === "1") {
                            Ext.getCmp("_last_operator").show();
                        }
                        /*if (one === "1") {
                            Ext.getCmp("_zhuangao_operator").show();
                        }
                        if (two === "1") {
                            Ext.getCmp("_first_operator").show();
                        }
                        if (three === "1") {
                            Ext.getCmp("_next_operator").show();
                        }
                        if (four === "1") {
                            Ext.getCmp("_last_operator").show();
                        }*/
                        //如果当前用户是root用户，全体起立
                        var user="<%=session.getAttribute("user")%>";
                        if(user=="root"){
                            Ext.getCmp("_f_add_rw").show();//新建任务
                            Ext.getCmp("_f_rw_edit").show();
                            Ext.getCmp("_f_del_rw").show();//删除任务
                            Ext.getCmp("_f_leader_examine").show();
                            Ext.getCmp("_f_receive_rw").show();//提交审核
                            Ext.getCmp("_zhuangao_operator").show();
                            Ext.getCmp("_first_operator").show();
                            Ext.getCmp("_next_operator").show();
                            Ext.getCmp("_last_operator").show();
                        }
                    }
                    //_path列转换
                    function add_zhuangaoren_compilation(){
                        var fieldenname=_g_task_reset.getForm().findField('zhuangaoren_compilation').getValue();
                        var fieldcnname = Ext.getCmp("zhuangaoren_compilation").displayTplData[0].name_;
                        var params = [{
                            'fieldennames':fieldenname,
                            'fieldcnnames':fieldcnname
                        }];
                        var count=_g_zhuangaoren_compilation_store.getCount();
                        if(count>0){
                            for(var i=0;i<count;i++){
                                var leftenzd = _g_zhuangaoren_compilation_store.getAt(i).get("fieldennames");
                                if(leftenzd===fieldenname){
                                    return;
                                }
                            }
                        }
                        ///_g_field_store.insert(0,params);
                        _g_zhuangaoren_compilation_store.loadData(params,true);
                    }
                    function add_first_compilation(){
                        var fieldenname=_g_task_reset.getForm().findField('first_compilation').getValue();
                        var fieldcnname = Ext.getCmp("first_compilation").displayTplData[0].name_;
                        var params = [{
                            'fieldennames':fieldenname,
                            'fieldcnnames':fieldcnname
                        }];
                        var count=_g_first_compilation_store.getCount();
                        if(count>0){
                            for(var i=0;i<count;i++){
                                var leftenzd = _g_first_compilation_store.getAt(i).get("fieldennames");
                                if(leftenzd===fieldenname){
                                    return;
                                }
                            }
                        }
                        ///_g_field_store.insert(0,params);
                        _g_first_compilation_store.loadData(params,true);
                    }

                    function add_next_compilation(){
                        var fieldenname=_g_task_reset.getForm().findField('next_compilation').getValue();
                        var fieldcnname = Ext.getCmp("next_compilation").displayTplData[0].name_;
                        var params = [{
                            'fieldennames':fieldenname,
                            'fieldcnnames':fieldcnname
                        }];
                        var count=_g_next_compilation_store.getCount();
                        if(count>0){
                            for(var i=0;i<count;i++){
                                var leftenzd = _g_next_compilation_store.getAt(i).get("fieldennames");
                                if(leftenzd===fieldenname){
                                    return;
                                }
                            }
                        }
                        ///_g_field_store.insert(0,params);
                        _g_next_compilation_store.loadData(params,true);
                    }
                    function _w_details_open(){
                        _w_zhuangao_compilation_message.show();
                    }
                    function zhuangao_compilation_load(){
                        var selection = AOS.selection(_g_operation, 'id_');
                        if (AOS.empty(selection)) {
                            AOS.tip('请选择要查看详情的日志!');
                            return;
                        }
                        var path=AOS.selectone(_g_operation).data.compilation_message;
                        AOS.ajax({
                            params: {
                                zg_path: path
                            },
                            url: 'getzhuangaomessage.jhtml',
                            ok: function (data) {
                                _f_zhuangao_compilation_message.getForm().findField('zhuangao_compilation_message').setValue(data.appmsg);
                            }
                        });

                    }
                    function add_last_compilation(){
                        var fieldenname=_g_task_reset.getForm().findField('last_compilation').getValue();
                        var fieldcnname = Ext.getCmp("last_compilation").displayTplData[0].name_;
                        var params = [{
                            'fieldennames':fieldenname,
                            'fieldcnnames':fieldcnname
                        }];
                        var count=_g_last_compilation_store.getCount();
                        if(count>0){
                            for(var i=0;i<count;i++){
                                var leftenzd = _g_last_compilation_store.getAt(i).get("fieldennames");
                                if(leftenzd===fieldenname){
                                    return;
                                }
                            }
                        }
                        ///_g_field_store.insert(0,params);
                        _g_last_compilation_store.loadData(params,true);
                    }
                    //任务分配
                    function _w_distribution_rw(){
                        var selection = AOS.selection(_g_compilation, 'id_');
                        if (AOS.empty(selection)) {
                            AOS.tip('请选择要查看分配的任务!');
                            return;
                        }else{
                            //弹出授权窗口
                            var record = AOS.selectone(_g_compilation);
                            if (record) {
                                _w_task_reset.show();
                                _g_task_reset.loadRecord(record);
                                //把grid都清除
                                Ext.getCmp("_g_zhuangaoren_compilation").getStore().removeAll();
                                Ext.getCmp("_g_first_compilation").getStore().removeAll();
                                Ext.getCmp("_g_next_compilation").getStore().removeAll();
                                Ext.getCmp("_g_last_compilation").getStore().removeAll();
                                Ext.getCmp("zhuangaoren_compilation").setValue("");
                                Ext.getCmp("first_compilation").setValue("");
                                Ext.getCmp("next_compilation").setValue("");
                                Ext.getCmp("last_compilation").setValue("");
                            }
                        }
                    }
                    function _w_details_add(){
                        //1.把三种人编写成集合的形式
                        var z=Ext.getCmp('_g_zhuangaoren_compilation').getStore();
                        var zhuangaoren_fieldennames="";
                        for(var i = 0 ;i< z.getCount(); i++){
                            zhuangaoren_fieldennames+=z.getAt(i).get('fieldennames')+",";
                        }
                        //去掉最后一个字符
                        zhuangaoren_fieldennames=zhuangaoren_fieldennames.substring(0,zhuangaoren_fieldennames.length-1);
                        var zz=Ext.getCmp('_g_zhuangaoren_compilation').getStore();
                        var zhuangaoren_fieldcnnames="";
                        for(var i = 0 ;i< zz.getCount(); i++){
                            zhuangaoren_fieldcnnames+=zz.getAt(i).get('fieldcnnames')+",";
                        }
                        //去掉最后一个字符
                        zhuangaoren_fieldcnnames=zhuangaoren_fieldcnnames.substring(0,zhuangaoren_fieldcnnames.length-1);

                        //1.把三种人编写成集合的形式
                        var s=Ext.getCmp('_g_first_compilation').getStore();
                        var first_fieldennames="";
                        for(var i = 0 ;i< s.getCount(); i++){
                            first_fieldennames+=s.getAt(i).get('fieldennames')+",";
                        }
                        //去掉最后一个字符
                        first_fieldennames=first_fieldennames.substring(0,first_fieldennames.length-1);

                        var ss=Ext.getCmp('_g_first_compilation').getStore();
                        var first_fieldcnnames="";
                        for(var i = 0 ;i< ss.getCount(); i++){
                            first_fieldcnnames+=ss.getAt(i).get('fieldcnnames')+",";
                        }
                        //去掉最后一个字符
                        first_fieldcnnames=first_fieldcnnames.substring(0,first_fieldcnnames.length-1);

                        var s1=Ext.getCmp('_g_next_compilation').getStore();
                        var next_fieldennames="";
                        for(var i = 0 ;i< s1.getCount(); i++){
                            next_fieldennames+=s1.getAt(i).get('fieldennames')+",";
                        }
                        //去掉最后一个字符
                        next_fieldennames=next_fieldennames.substring(0,next_fieldennames.length-1);

                        var ss1=Ext.getCmp('_g_next_compilation').getStore();
                        var next_fieldcnnames="";
                        for(var i = 0 ;i< ss1.getCount(); i++){
                            next_fieldcnnames+=ss1.getAt(i).get('fieldcnnames')+",";
                        }
                        //去掉最后一个字符
                        next_fieldcnnames=next_fieldcnnames.substring(0,next_fieldcnnames.length-1);

                        var s2=Ext.getCmp('_g_last_compilation').getStore();
                        var last_fieldennames="";
                        for(var i = 0 ;i< s2.getCount(); i++){
                            last_fieldennames+=s2.getAt(i).get('fieldennames')+",";
                        }
                        //去掉最后一个字符
                        last_fieldennames=last_fieldennames.substring(0,last_fieldennames.length-1);

                        var ss2=Ext.getCmp('_g_last_compilation').getStore();
                        var last_fieldcnnames="";
                        for(var i = 0 ;i< ss2.getCount(); i++){
                            last_fieldcnnames+=ss2.getAt(i).get('fieldcnnames')+",";
                        }
                        //去掉最后一个字符
                        last_fieldcnnames=last_fieldcnnames.substring(0,last_fieldcnnames.length-1);
                        if(first_fieldennames==""||next_fieldennames==""||last_fieldennames==""||zhuangaoren_fieldennames==""){
                            AOS.err("授权人不能有空数据!");
                            return;
                        }
                        var record = AOS.selectone(_g_compilation);
                        AOS.ajax({
                            forms:_g_task_reset,
                            url: 'adddetails.jhtml',
                            params:{
                                id_:record.data.id_,
                                zgr:zhuangaoren_fieldennames,
                                zgr_cn:zhuangaoren_fieldcnnames,
                                hgr:first_fieldennames,
                                hgr_cn:first_fieldcnnames,
                                jdr:next_fieldennames,
                                jdr_cn:next_fieldcnnames,
                                zbj:last_fieldennames,
                                zbj_cn:last_fieldcnnames
                            },
                            method:'post',
                            ok: function (data) {
                                if(data.appcode===1){
                                    AOS.tip("操作成功!");
                                    _w_task_reset.hide();
                                    _g_compilation_store.reload();
                                }else if(data.appcode===-1){
                                    AOS.tip("操作失败!");
                                }
                            }
                        });
                    }
                    //删除当前选中的节点
                    function _g_zhuangaoren_compilation_del(){
                        var row=_g_zhuangaoren_compilation.getSelectionModel().getSelection();
                        //右侧删除，左侧添加
                        _g_zhuangaoren_compilation_store.remove(row);
                    }
                    //删除当前选中的节点
                    function _g_first_compilation_del(){
                        var row=_g_first_compilation.getSelectionModel().getSelection();
                        //右侧删除，左侧添加
                        _g_first_compilation_store.remove(row);
                    }
                    function _g_next_compilation_del(){
                        var row=_g_next_compilation.getSelectionModel().getSelection();
                        //右侧删除，左侧添加
                        _g_next_compilation_store.remove(row);
                    }
                    function _g_last_compilation_del(){
                        var row=_g_last_compilation.getSelectionModel().getSelection();
                        //右侧删除，左侧添加
                        _g_last_compilation_store.remove(row);
                    }
                    //接收提交
                    function _g_receive_rw(){
                        var selection = AOS.selection(_g_compilation, 'id_');
                        if (AOS.empty(selection)) {
                            AOS.tip('提交前请先选中数据。');
                            return;
                        }
                        var record = AOS.selectone(_g_compilation);
                        var id_="";
                        var flag_submit="";
                        if(!AOS.empty(record)){
                            id_=record.data.id_;
                            flag_submit=record.data.flag_submit;
                        }
                        if(flag_submit=="已提交"){
                            AOS.tip('已提交无须重复提交!');
                            return;
                        }
                        var msg = AOS.merge('确认要提交选中的[{0}]个任务数据吗？', AOS.rows(_g_compilation));
                        AOS.confirm(msg, function (btn) {
                            if (btn === 'cancel') {
                                AOS.tip('提交操作被取消。');
                                return;
                            }
                            var cascode_id_=Ext.getCmp("cascode_id_").getValue();
                            var aos_module_id_=Ext.getCmp("aos_module_id_").getValue();
                            AOS.ajax({
                                params:{'id_':id_,cascode_id_:cascode_id_,aos_module_id_:aos_module_id_},
                                url : 'updatecompilation.jhtml',
                                ok : function(data) {
                                    AOS.tip(data.appmsg);
                                    _g_compilation_store.reload();
                                }
                            });
                        });
                    }
                    function _w_del_data(){
                        var selection = AOS.selection(_g_data, 'id_');
                        var selectioncompilation = AOS.selection(_g_compilation, 'id_');
                        if (AOS.empty(selection)||AOS.empty(selectioncompilation)) {
                            AOS.tip('删除前请先选中数据和任务。');
                            return;
                        }
                        var tablename=AOS.selectone(_g_compilation).data.sjkmc;
                        var pid=AOS.selectone(_g_compilation).data.id_;

                        var msg = AOS.merge('确认要删除选中的[{0}]个数据吗？', AOS.rows(_g_data));
                        AOS.confirm(msg, function (btn) {
                            if (btn === 'cancel') {
                                AOS.tip('删除操作被取消。');
                                return;
                            }
                            AOS.ajax({
                                url: 'delete_data.jhtml',
                                params: {
                                    aos_rows_: selection,
                                    tablename:tablename,
                                    pid:pid
                                },
                                ok: function (data) {
                                    AOS.tip(data.appmsg);
                                    _g_data_store.reload();
                                }
                            });
                        });
                    }
                    function _w_del_rw() {
                        var selection = AOS.selection(_g_compilation, 'id_');
                        if (AOS.empty(selection)) {
                            AOS.tip('删除前请先选中任务吗。');
                            return;
                        }
                        var msg = AOS.merge('确认要删除选中的[{0}]个任务吗？', AOS.rows(_g_compilation));
                        AOS.confirm(msg, function (btn) {
                            if (btn === 'cancel') {
                                AOS.tip('删除操作被取消。');
                                return;
                            }
                            AOS.ajax({
                                url: 'delete_rw.jhtml',
                                params: {
                                    aos_rows_: selection
                                },
                                ok: function (data) {
                                    AOS.tip(data.appmsg);
                                    _g_compilation_store.reload();
                                }
                            });
                        });
                    }
                    function _w_select_archive_onshow(){
                        var params = {
                            tablename : Ext.getCmp("sjkmc").value
                        };
                        //这个Store的命名规则为：表格ID+"_store"。ok_save

                        _g_select_archive_store.getProxy().extraParams = params;
                        _g_select_archive_store.load();
                    }
                    function _w_select_edit_archive_onshow(){

                        var id_ = AOS.selectone(_g_compilation).data.id_;
                        var sjkmc = AOS.selectone(_g_compilation).data.sjkmc;
                        var params = {
                            tablename : sjkmc,
                            id_ : id_
                        };
                        //这个Store的命名规则为：表格ID+"_store"。
                        _g_select_edit_archive_store.getProxy().extraParams = params;
                        _g_select_edit_archive_store.load();
                    }

                    //查询窗口展开
                    function _w_select_query_show() {
                        //判断是不是
                        _w_query_select_q.show();
                    }
                    //查询窗口展开
                    function _w_select_edit_query_show() {
                        //判断是不是
                        _w_query_edit_select_q.show();
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
                        _g_select_archive_store.getProxy().extraParams = params;
                        _g_select_archive_store.load();
                        _w_query_select_q.hide();
                        AOS.reset(_f_select_query);
                    }
                    function get_files(){
                        var selection = AOS.selection(_g_data, 'id_');
                        var id_=selection.substring(0,selection.length-1);
                        var listTablename = AOS.selection(_g_data, 'tablename').substring(0,AOS.selection(_g_data, 'tablename').length-1);
                        var params ={
                            id_:id_,
                            tablename:listTablename
                        };
                        //这个Store的命名规则为：表格ID+"_store"。
                        _g_files_store.getProxy().extraParams = params;
                        _g_files_store.load();
                    }
                    //查看电子文件
                    function _w_files_look(){
                        //得到当前用户,判断是不是档案室和admin用户
                        var user="<%=session.getAttribute("user")%>";
                        var tablename = AOS.selection(_g_data, 'tablename').substring(0,AOS.selection(_g_data, 'tablename').length-1);
                        //得到当前选中的。
                        var row = AOS.selectone(_g_files);
                        if(row.data._s_path.split(".")[1]=="pdf"||row.data._s_path.split(".")[1]=="PDF"){
                            parent.fnaddtab(row.data.id_, '电子文件',
                                'archive/data/openPdfFile.jhtml?id='+row.data.id_+'&tid='+row.data.tid+'&type='+row.data._s_path.split(".")[1]+'&tablename='+tablename);
                        }else if(row.data._s_path.split(".")[1]=="jpg"||row.data._s_path.split(".")[1]=="JPG"||row.data._s_path.split(".")[1]=="doc"||row.data._s_path.split(".")[1]=="DOC"){
                            parent.fnaddtab(row.data.id_,'电子文件','archive/data/openSwfFile.jhtml?id='+row.data.id_+'&tid='+row.data.tid+'&type='+row.data._s_path.split(".")[1]+'&tablename='+tablename);
                        }
                    }
                    function _f_select_edit_data_query(){
                        var params = AOS.getValue('_f_select_edit_query');
                        var form = Ext.getCmp('_f_select_edit_query');
                        for(var i=1;i<=5;i++){
                            var str = form.down("[name='filedname"+i+"']");
                            var filedname =str.getValue();
                            if(filedname==null){
                                params["filedname"+i]=str.regexText;
                            }
                        }
                        params["tablename"]=AOS.selectone(_g_compilation).data.sjkmc;
                        _g_select_edit_archive_store.getProxy().extraParams = params;
                        _g_select_edit_archive_store.load();
                        _w_query_edit_select_q.hide();
                        AOS.reset(_f_select_edit_query);
                    }
                    //保存当前选中的档案信息
                    function ok_save_archive(){
                        var count=AOS.rows(_g_select_archive);
                        if(count<=0){
                            AOS.tip("请选择需要编研的数据");
                            return;
                        }
                        var ids="";
                        var row=_g_select_archive.getSelectionModel().getSelection();
                        for(var i=0;i<AOS.rows(_g_select_archive);i++){
                            if(i==0){
                                ids=row[i].data.id_;
                            }else{
                                ids+=","+row[i].data.id_;
                            }
                        }
                        Ext.getCmp("ids").setValue(ids);
                        _w_select_archive.hide();
                    }
                    function ok_save_archive_edit(){
                        var count=AOS.rows(_g_select_edit_archive);
                        if(count<=0){
                            AOS.tip("请选择需要编研的数据");
                            return;
                        }
                        var ids="";
                        var row=_g_select_edit_archive.getSelectionModel().getSelection();
                        for(var i=0;i<AOS.rows(_g_select_edit_archive);i++){
                            if(i==0){
                                ids=row[i].data.id_;
                            }else{
                                ids+=","+row[i].data.id_;
                            }
                        }
                        var record = AOS.selectone(_g_compilation);
                        var params = {
                            ids:ids,
                            by_id:record.data.id_,
                            tablename : record.data.sjkmc
                        };
                        AOS.ajax({
                            url: 'updatebyrw_data.jhtml',
                            params:params,
                            ok: function (data) {
                                if(data.appcode===1){
                                    AOS.tip("添加成功!");
                                    _g_data_store.load();
                                    _w_select_edit_archive.hide();
                                    //_g_receive_query();
                                }else if(data.appcode===-1){
                                    AOS.tip("添加失败!");
                                }
                            }
                        });

                    }
                    function _g_add_data(){
                        //1.判断是否选中了任务
                        var selection = AOS.selection(_g_compilation, 'id_');
                        if (AOS.empty(selection)) {
                            AOS.tip('请先选中任务。');
                            return;
                        }
                        //2.弹出选择数据页面
                        //根据选择的名称
                        var sjkmc_value = AOS.selection(_g_compilation, 'sjkmc');
                        //弹出选择档案窗口
                        _w_select_edit_archive.show();
                    }

                    //加载数据,（批次号列表信息）
                    function _g_compilation_query() {
                        var params = {
                            aos_module_id_ : aos_module_id_.getValue(),
                            byrwbh:byrwbh.getValue()
                        };
                        //这个Store的命名规则为：表格ID+"_store"。
                        _g_compilation_store.getProxy().extraParams = params;
                        _g_compilation_store.load();
                    }

                    function get_a_compilation(){
                        var record = AOS.selectone(_g_compilation);
                        var params = {
                            zt_id:record.data.zt_id
                        };
                        //这个Store的命名规则为：表格ID+"_store"。
                        _g_data_store.getProxy().extraParams = params;
                        _g_data_store.load();
                    }

                    function fn_g_data(){
                        var selection = AOS.selection(_g_data, 'id_');
                        var tms = AOS.selection(_g_data, 'tm');
                        if (AOS.empty(selection)) {
                            AOS.tip('修改前请先选中数据。');
                            return;
                        }
                        AOS.ajax({
                            params: {id_: AOS.selectone1(_g_data).data.id_,
                                tablename:tablename.getValue()
                            },
                            url: 'getData.jhtml',
                            ok: function (data) {
                                _f_data_i.form.setValues(data);
                                _w_data_i.show();
                            }
                        });
                    }
                    function fn_g_data_from(){
                        var selection = AOS.selection(_g_data_from, 'id_');
                        var tms = AOS.selection(_g_data_from, 'tm');
                        if (AOS.empty(selection)) {
                            AOS.tip('修改前请先选中数据。');
                            return;
                        }
                        Ext.getCmp('insert').setValue('0');
                        _w_data_i_from.show();
                    }
                    //列表时搜索
                    function _f_data_query(){
                        var params = AOS.getValue('_f_query');
                        var form = Ext.getCmp('_f_query');
                        var tmp = columnnum.getValue();
                        for(var i=1;i<=tmp;i++){
                            var str = form.down("[name='filedname"+i+"']");
                            var filedname =str.getValue();
                            if(filedname==null){
                                params["filedname"+i]=str.regexText;
                            }

                        }
                        _g_data_store.getProxy().extraParams = params;
                        _g_data_store.load();
                        _w_query_q.hide();
                        AOS.reset(_f_query);
                    }

                    //删除信息
                    function _g_data_del() {
                        var selection = AOS.selection(_g_data, 'id_');
                        var tms = AOS.selection(_g_data, 'tm');
                        if (AOS.empty(selection)) {
                            AOS.tip('删除前请先选中数据。');
                            return;
                        }
                        var msg = AOS.merge('确认要删除选中的[{0}]个用户数据吗？', AOS.rows(_g_data));
                        AOS.confirm(msg, function (btn) {
                            if (btn === 'cancel') {
                                AOS.tip('删除操作被取消。');
                                return;
                            }
                            AOS.ajax({
                                url: 'deleteData.jhtml',
                                params: {
                                    aos_rows_: selection,
                                    tm:tms,
                                    tablename: tablename.getValue()
                                },
                                ok: function (data) {
                                    AOS.tip(data.appmsg);
                                    _g_data_store.reload();
                                }
                            });
                        });
                    }
                    //删除信息
                    function _g_data_from_del() {
                        var selection = AOS.selection(_g_data_from, 'id_');
                        var tms = AOS.selection(_g_data_from, 'tm');
                        if (AOS.empty(selection)) {
                            AOS.tip('删除前请先选中数据。');
                            return;
                        }
                        var msg = AOS.merge('确认要删除选中的[{0}]个用户数据吗？', AOS.rows(_g_data_from));
                        AOS.confirm(msg, function (btn) {
                            if (btn === 'cancel') {
                                AOS.tip('删除操作被取消。');
                                return;
                            }
                            AOS.ajax({
                                url: 'deleteData_from.jhtml',
                                params: {
                                    aos_rows_: selection,
                                    tm:tms,
                                    tablename: tablename.getValue()+"_z"
                                },
                                ok: function (data) {
                                    AOS.tip(data.appmsg);
                                    _g_data_from_store.reload();
                                }
                            });
                        });
                    }


                    function _f_info_ok(){
                        AOS.ajax({
                            forms : _f_config,
                            url : 'setPagesize.jhtml',
                            params:{
                                tablename : tablename.getValue()
                            },
                            ok : function(data) {
                                _w_config.hide();
                                _g_data_store.reload();
                                AOS.tip(data.appmsg);
                            }
                        });

                    }
                    function _w_config_from_show(){

                        _w_config_from.show();
                    }

                    function _f_info_from_ok(){
                        AOS.ajax({
                            forms : _f_config_from,
                            url : 'setPagesize_from.jhtml',
                            params:{
                                tablename : tablename.getValue()
                            },
                            ok : function(data) {
                                _w_config_from.hide();
                                _g_data_from_store.reload();
                                AOS.tip(data.appmsg);
                            }
                        });
                    }


                    //_path列转换
                    function fn_path_render(value, metaData, record, rowIndex, colIndex,
                                            store) {
                        if (value >= 1) {
                            return '<img src="${cxt}/static/icon/picture.png" />';
                        } else {
                            return '<img src="${cxt}/static/icon/picture-empty.png" />';
                        }
                    }

                    function _w_leader_examine(){
                        var selection = AOS.selection(_g_compilation, 'id_');
                        if (AOS.empty(selection)) {
                            AOS.tip('提交前请先选中数据!');
                            return;
                        }
                        var record = AOS.selectone(_g_compilation);
                        var flag_submit="";
                        var flag_examine="";
                        if(!AOS.empty(record)){
                            flag_submit=record.data.flag_submit;
                            flag_examine=record.data.flag_examine;
                        }
                        if(flag_submit!="已提交"){
                            AOS.tip('未提交领导不能审核!');
                            return;
                        }
                        if(flag_examine=="已通过"){
                            AOS.tip('已通过无须重复审核!');
                            return;
                        }
                        _w_next_kf_message.show();
                    }
                    function _save_next_kf_message(){
                        var record = AOS.selectone(_g_compilation);
                        var id_="";
                        if(!AOS.empty(record)){
                            id_=record.data.id_;
                        }
                        var cascode_id_=Ext.getCmp("cascode_id_").getValue();
                        var aos_module_id_=Ext.getCmp("aos_module_id_").getValue();
                        AOS.ajax({
                            forms : _f_next_kf_message,
                            params:{'id_':id_,cascode_id_:cascode_id_,aos_module_id_:aos_module_id_},
                            url : 'updaterwyes.jhtml',
                            ok : function(data) {
                                _w_next_kf_message.hide();
                                AOS.tip(data.appmsg);
                                _g_compilation_store.reload();
                            }
                        });
                    }
                    function no_save_next_kf_message(){
                        var record = AOS.selectone(_g_compilation);
                        var id_="";
                        if(!AOS.empty(record)){
                            id_=record.data.id_;
                        }
                        var cascode_id_=Ext.getCmp("cascode_id_").getValue();
                        var aos_module_id_=Ext.getCmp("aos_module_id_").getValue();
                        AOS.ajax({
                            forms : _f_next_kf_message,
                            params:{'id_':id_,cascode_id_:cascode_id_,aos_module_id_:aos_module_id_},
                            url : 'updaterwno.jhtml',
                            ok : function(data) {
                                _w_next_kf_message.hide();
                                AOS.tip(data.appmsg);
                                _g_compilation_store.reload();
                            }
                        });
                    }
                    function _new_task(){
                        //根据tree名称得到对应的部门名称
                        AOS.ajax({
                            params:{name_:'编研任务流水号'},
                            url:'calcId.jhtml',
                            ok:function(data){
                                //设计一个随机数编号
                                var date=new Date();
                                _f_rw.form.findField("byrwbh").setValue(date.getFullYear()+data.appmsg);
                            }
                        });
                    }
                    function _f_rw_submit(){
                        AOS.ajax({
                            url: 'addbyrw.jhtml',
                            forms:_f_rw,
                            params:{"zt_id":Ext.getCmp("zt_list").getValue()
                            },
                            ok: function (data) {
                                if(data.appcode===1){
                                    AOS.tip("添加成功!");
                                    _w_rw.hide();
                                    _g_compilation_store.load();
                                    //_g_receive_query();
                                }else if(data.appcode===-1){
                                    AOS.tip("添加失败!");
                                }
                            }
                        });
                    }

                    function _f_rw_edit_submit(){
                        AOS.ajax({
                            url: 'addbyrw_edit.jhtml',
                            forms:_f_edit_rw,
                            ok: function (data) {
                                if(data.appcode===1){
                                    AOS.tip("修改成功!");
                                    _w_edit_rw.hide();
                                    _g_compilation_store.load();
                                    //_g_receive_query();
                                }else if(data.appcode===-1){
                                    AOS.tip("修改失败!");
                                }
                            }
                        });
                    }
                    function _g_field_order_show(){
                        _w_field_order.show();
                    }
                    function _select_archive(){
                        //根据选择的名称
                        var sjkmc_value = Ext.getCmp("sjkmc").value;
                        if(sjkmc_value==""||sjkmc_value==null){
                            AOS.tip("请选择数据库！");
                            return;
                        }

                        //弹出选择档案窗口
                        _w_select_archive.show();
                    }
                    function _select_edit_archive(){
                        //根据选择的名称
                        //弹出选择档案窗口
                        _w_select_edit_archive.show();
                    }


                    function operation_login(){
                        var selection = AOS.selection(_g_compilation, 'id_');
                        if (AOS.empty(selection)) {
                            AOS.tip('请选择要查看操作记录的任务!');
                            return;
                        }else {
                            _w_operation.show();
                        }
                    }
                    function _w_operation_show(){
                        //var selection = AOS.selectone(_g_compilation);
                        var id_=AOS.selectone(_g_compilation).raw.id_;
                        var params = {
                            pid: id_
                        };
                        //这个Store的命名规则为：表格ID+"_store"。
                        _g_operation_store.getProxy().extraParams = params;
                        _g_operation_store.load();
                    }
                    function _check_zhuangao(){
                        var user="<%=session.getAttribute("user")%>";
                        //查询数据库是否可以撰稿，如果可以撰稿跳转页面
                        var selection = AOS.selection(_g_compilation, 'id_');
                        var record = AOS.selectone(_g_compilation);

                        if (AOS.empty(selection)) {
                            AOS.tip('请选择要撰稿的任务!');
                            return;
                        }else{
                            if(record.data.flag_examine!="已通过"){
                                AOS.tip('审核未通过不能完成撰稿!');
                                return;
                            }
                            if(user=="root"){
                                //跳转页面
                                var id_=AOS.selectone(_g_compilation).raw.id_;
                                var zt_id = AOS.selectone(_g_compilation).raw.zt_id;
                                var cascode_id_=Ext.getCmp("cascode_id_").getValue();
                                var aos_module_id_=Ext.getCmp("aos_module_id_").getValue();
                                window.parent.fnaddtab('','档案编研详情','/compilation/examine/archive_details_all_message.jhtml?id_='+id_+ '&zt_id='+zt_id+'&flag=1'+'&cascode_id_='+cascode_id_+'&aos_module_id_='+aos_module_id_);
                            }else{
                                AOS.ajax({
                                    url: 'check_zhuangao.jhtml',
                                    params: {
                                        user:user,
                                        id_:record.data.id_
                                    },
                                    ok: function (data) {
                                        if(data.appcode==1){
                                            //跳转页面
                                            var id_=AOS.selectone(_g_compilation).raw.id_;
                                            var zt_id = AOS.selectone(_g_compilation).raw.zt_id;
                                            var cascode_id_=Ext.getCmp("cascode_id_").getValue();
                                            var aos_module_id_=Ext.getCmp("aos_module_id_").getValue();
                                            window.parent.fnaddtab('','档案编研详情','/compilation/examine/archive_details_all_message.jhtml?id_='+id_+ '&zt_id='+zt_id+'&flag=1'+'&cascode_id_='+cascode_id_+'&aos_module_id_='+aos_module_id_);
                                        }else{
                                            AOS.tip("该任务不能撰稿或者已经完成撰稿！");
                                        }
                                    }
                                });
                            }

                            //_w_operation_message.show();
                            //_g_operation_message.loadRecord(record);
                        }


                        //_write_list4_show.show();
                        //first_trial_examine_show();
                    }
                    function _w_picture_show(){
                        var selection = AOS.selection(_g_data, 'id_');
                        if (AOS.empty(selection)) {
                            AOS.tip('请选择需要查看电子文件的档案!');
                            return;
                        }
                        if(selection.substring(0,selection.length-1).split(",").length>1){
                            alert("请选择单个条目数据！");
                            return;
                        }
                        //弹出一个窗口，展示电子文件列表
                        _w_files.show();
                    }
                    function _check_first(){
                        var user="<%=session.getAttribute("user")%>";
                        //查询数据库是否可以撰稿，如果可以撰稿跳转页面
                        var selection = AOS.selection(_g_compilation, 'id_');
                        var record = AOS.selectone(_g_compilation);
                        if(user=="root"){
                            var id_ = AOS.selectone(_g_compilation).raw.id_;
                            var zt_id = AOS.selectone(_g_compilation).raw.zt_id;
                            var cascode_id_=Ext.getCmp("cascode_id_").getValue();
                            var aos_module_id_=Ext.getCmp("aos_module_id_").getValue();
                            window.parent.fnaddtab('', '档案编研详情', '/compilation/examine/archive_details_first_message.jhtml?id_=' + id_ + '&zt_id='+zt_id+ '&flag=2&byrwbh='+record.data.byrwbh+'&cascode_id_='+cascode_id_+'&aos_module_id_='+aos_module_id_);
                        }else{

                            if (AOS.empty(selection)) {
                                AOS.tip('请选择要合稿的任务!');
                                return;
                            }else {
                                AOS.ajax({
                                    url: 'check_hegao.jhtml',
                                    params: {
                                        user: user,
                                        id_: record.data.id_,
                                        byrwbh:record.data.byrwbh
                                    },
                                    ok: function (data) {
                                        if (data.appcode == 1) {
                                            //跳转页面
                                            var id_ = AOS.selectone(_g_compilation).raw.id_;
                                            var zt_id = AOS.selectone(_g_compilation).raw.zt_id;
                                            var cascode_id_=Ext.getCmp("cascode_id_").getValue();
                                            var aos_module_id_=Ext.getCmp("aos_module_id_").getValue();
                                            window.parent.fnaddtab('', '档案编研详情', '/compilation/examine/archive_details_first_message.jhtml?id_=' + id_ + '&zt_id='+zt_id+ '&flag=2&byrwbh='+record.data.byrwbh+'&cascode_id_='+cascode_id_+'&aos_module_id_='+aos_module_id_);
                                        } else {
                                            AOS.tip("该任务不能合稿或者已经完成合稿！");
                                        }
                                    }
                                });
                            }

                        }
                        //first_trial_examine_show();
                    }
                    function _check_next(){
                        var user="<%=session.getAttribute("user")%>";
                        //查询数据库是否可以撰稿，如果可以撰稿跳转页面
                        var selection = AOS.selection(_g_compilation, 'id_');
                        var record = AOS.selectone(_g_compilation);
                        if(user=="root"){
                            var id_ = AOS.selectone(_g_compilation).raw.id_;
                            var zt_id = AOS.selectone(_g_compilation).raw.zt_id;
                            var cascode_id_ = Ext.getCmp("cascode_id_").getValue();
                            var aos_module_id_ = Ext.getCmp("aos_module_id_").getValue();
                            window.parent.fnaddtab('', '档案编研详情', '/compilation/examine/archive_details_next_message.jhtml?id_=' + id_ + '&zt_id=' + zt_id + '&flag=4' + '&cascode_id_=' + cascode_id_ + '&aos_module_id_=' + aos_module_id_);
                        }else {


                            if (AOS.empty(selection)) {
                                AOS.tip('请选择要校对的任务!');
                                return;
                            } else {
                                AOS.ajax({
                                    url: 'check_jiaodui.jhtml',
                                    params: {
                                        user: user,
                                        id_: record.data.id_
                                    },
                                    ok: function (data) {
                                        if (data.appcode == 1) {
                                            //跳转页面
                                            var id_ = AOS.selectone(_g_compilation).raw.id_;
                                            var zt_id = AOS.selectone(_g_compilation).raw.zt_id;
                                            var cascode_id_ = Ext.getCmp("cascode_id_").getValue();
                                            var aos_module_id_ = Ext.getCmp("aos_module_id_").getValue();
                                            window.parent.fnaddtab('', '档案编研详情', '/compilation/examine/archive_details_next_message.jhtml?id_=' + id_ + '&zt_id=' + zt_id + '&flag=4' + '&cascode_id_=' + cascode_id_ + '&aos_module_id_=' + aos_module_id_);
                                        } else {
                                            AOS.tip("该任务不能校对或者已经完成校对！");
                                        }
                                    }
                                });
                            }

                        }
                        //_w_next_trial.show();

                    }
                    function _check_last(){
                        var user="<%=session.getAttribute("user")%>";
                        //查询数据库是否可以撰稿，如果可以撰稿跳转页面
                        var selection = AOS.selection(_g_compilation, 'id_');
                        var record = AOS.selectone(_g_compilation);
                        if(user=="root"){
                            var cascode_id_=Ext.getCmp("cascode_id_").getValue();
                            var aos_module_id_=Ext.getCmp("aos_module_id_").getValue();
                            var id_ = AOS.selectone(_g_compilation).raw.id_;
                            var zt_id = AOS.selectone(_g_compilation).raw.zt_id;
                            window.parent.fnaddtab('', '档案编研详情', '/compilation/examine/archive_details_last_message.jhtml?id_=' + id_ + '&zt_id='+zt_id+'&flag=3'+'&cascode_id_='+cascode_id_+'&aos_module_id_='+aos_module_id_);
                        }else{
                            if (AOS.empty(selection)) {
                                AOS.tip('请选择要终审的任务!');
                                return;
                            }else {
                                AOS.ajax({
                                    url: 'check_zongbianji.jhtml',
                                    params: {
                                        user: user,
                                        id_: record.data.id_
                                    },
                                    ok: function (data) {
                                        if (data.appcode == 1) {
                                            //跳转页面
                                            var cascode_id_=Ext.getCmp("cascode_id_").getValue();
                                            var aos_module_id_=Ext.getCmp("aos_module_id_").getValue();
                                            var id_ = AOS.selectone(_g_compilation).raw.id_;
                                            var zt_id = AOS.selectone(_g_compilation).raw.zt_id;
                                            window.parent.fnaddtab('', '档案编研详情', '/compilation/examine/archive_details_last_message.jhtml?id_=' + id_ + '&zt_id='+zt_id+'&flag=3'+'&cascode_id_='+cascode_id_+'&aos_module_id_='+aos_module_id_);
                                        } else {
                                            AOS.tip("该任务不能终审或者已经完成终审！");
                                        }
                                    }
                                });
                            }
                        }

                    }
                    //新增批次
                    function _g_add_rw(){
                        AOS.reset(_f_rw);
                        _w_rw.show();
                    }
                    //修改批次
                    function _g_edit_rw(){
                        //AOS.reset(_f_edit_rw);
                        var record = AOS.selectone(_g_compilation);
                        if(AOS.empty(record)){
                            AOS.tip("请选择要修改的数据!");
                            return;
                        }
                        _w_edit_rw.show();
                        _f_edit_rw.loadRecord(record);
                        _f_edit_rw.getForm().findField('byrwbh').setValue(record.data.byrwbh);
                    }
                    function open_file(){
                        var record = AOS.selectone(_g_compilation);
                        if(AOS.empty(record)){
                            AOS.tip("请选择附件的任务!");
                            return;
                        }
                        var byrwbh = AOS.selectone(_g_compilation).raw.byrwbh;
                        AOS.ajax({
                            params : {
                                byrwbh:byrwbh
                            },
                            url : 'booleanfile.jhtml',
                            ok : function(data) {
                                if(data.appcode===-1){
                                    AOS.tip("附件不存在");
                                    return;
                                }
                                _w_fj_open_path.show();
                            }
                        });
                    }
                    function open_path_onload(){
                        var byrwbh = AOS.selectone(_g_compilation).raw.byrwbh;
                        AOS.ajax({
                            params : {
                                byrwbh:byrwbh
                            },
                            url : 'getfile_fj.jhtml',
                            ok : function(data) {
                                _f_open_path.getForm().findField('_path_message').setValue(data.appmsg);
                            }
                        });
                    }

                    //上一页
                    function _f_previous_data(){
                        var count=Ext.getCmp("_g_compilation").getStore().getCount();
                        var me=Ext.getCmp("_g_compilation").getSelectionModel().getSelection();
                        //var record = AOS.selectone(_g_compilation);
                        //得到执行行的坐标
                        var rowIndex = Ext.getCmp("_g_compilation").getStore().indexOf(me[0]);
                        if(rowIndex==0){
                            AOS.err("当前第一条!");
                            return;
                        }
                        var s=Ext.getCmp("_g_compilation").getStore().getAt(rowIndex-1);
                        //原先行取消选中
                        Ext.getCmp("_g_compilation").getSelectionModel().deselect(rowIndex);
                        //此时让光标选中上一行
                        Ext.getCmp("_g_compilation").getSelectionModel().select(rowIndex-1, true);
                        //组件被显示后触发。
                        Ext.getCmp("_f_edit_rw").form.setValues(s.data);
                    }
                    //下一页
                    function _f_next_data(){
                        var count=Ext.getCmp("_g_compilation").getStore().getCount();
                        var me=Ext.getCmp("_g_compilation").getSelectionModel().getSelection();
                        //var record = AOS.selectone(_g_compilation);
                        //得到执行行的坐标
                        var rowIndex = Ext.getCmp("_g_compilation").getStore().indexOf(me[0]);
                        if(rowIndex==count-1){
                            AOS.err("当前最后一条!");
                            return;
                        }
                        var s=Ext.getCmp("_g_compilation").getStore().getAt(rowIndex+1);
                        //原先行取消选中
                        Ext.getCmp("_g_compilation").getSelectionModel().deselect(rowIndex);
                        //此时让光标选中下一行
                        Ext.getCmp("_g_compilation").getSelectionModel().select(rowIndex+1, true);
                        //组件被显示后触发。
                        Ext.getCmp("_f_edit_rw").form.setValues(s.data);
                    }
                </script>
            </aos:window>
        </aos:viewport>
    </aos:onready>
    <script type="text/javascript">

    </script>
</aos:html>
﻿<%@ page contentType="text/html; charset=utf-8" isELIgnored="false"%>
<%@ taglib uri="/WEB-INF/tld/aos.tld" prefix="aos"%>
<%@ taglib uri="/WEB-INF/tld/cll.tld" prefix="c"%>
<%
    String path = request.getContextPath();
    Object fieldDtos = request.getAttribute("fieldDtos");
    Object listtablename = request.getAttribute("listtablename");
%>
<aos:html>
    <aos:head title="登记任务">
        <aos:include lib="ext" />
        <aos:base href="register/register" />
    </aos:head>
    <aos:body>
    </aos:body>
    <aos:onready>
        <aos:viewport layout="border">
            <aos:hiddenfield id="lx" value="${lx}"/>
            <aos:gridpanel id="_g_data" url="listAccounts.jhtml" region="center"
                           autoScroll="true" enableLocking="true" hidePagebar="true" onrender="_to_load_RW"
                           xtype="true">
                <aos:docked forceBoder="0 0 1 0">
                    <aos:dockeditem xtype="tbtext" text="登记" />
                    <aos:dockeditem onclick="_g_xiaodu_add" text="新建任务" icon="add.png" />
                    <aos:dockeditem onclick="_g_xiaodu_edit" text="修改任务" icon="edit.png" />
                    <aos:dockeditem onclick="_w_del_xiaodu_register_load" text="删除任务" icon="del.png" />
                    <aos:dockeditem onclick="_task_allocation" text="任务分配" icon="config.png" />
                    <aos:dockeditem text="任务详情" icon="add.png"
                                    onclick="_w_allocation_message" />
                    <aos:dockeditem text="登记日志" icon="more/view-history.png"
                                    onclick="_w_register_login" />
                </aos:docked>
                <aos:selmodel type="row" mode="multi" />
                <aos:column type="rowno" />
                <aos:column dataIndex="id_" header="流水号" hidden="true" />
                <aos:column header="任务编号" dataIndex="task_number"  width="200" align="true"/>
                <aos:column header="任务名称" dataIndex="task_name"  width="200" align="true" />
                <aos:column header="任务类型" dataIndex="task_type"  width="200" align="true" />
                <aos:column header="门类名称" dataIndex="task_tablename" hidden="true"  width="100" align="true" />
                <aos:column header="门类名称" dataIndex="task_cn_tablename"  width="100" align="true" />
                <aos:column header="档号" dataIndex="dh"  width="100" align="true"/>
                <aos:column header="题名" dataIndex="tm" width="200" align="true" />
                <aos:column header="页数" dataIndex="ys" width="100" align="true"/>
                <aos:column header="电子文件" dataIndex="dzwj"  width="200" align="true" />
                <aos:column header="档案时间" dataIndex="archive_time"  width="200" align="true" />
                <aos:column header="档案存址" dataIndex="archive_path"  width="100" align="true" />
                <aos:column header="提交状态" dataIndex="dj_flag"  width="100" align="true"  />
                <aos:column header="审核状态" dataIndex="sh_flag"  width="100" align="true" />
                <aos:column header="" flex="1" />
            </aos:gridpanel>

            <aos:window id="_w_add_xiaodu_register" title="消毒任务" onshow="_w_add_xiaodu_register_show">
                <aos:formpanel id="_f_add_xiaodu_register" width="500" layout="anchor"
                               labelWidth="90">
                    <aos:hiddenfield name="id_" />
                    <aos:textfield fieldLabel="任务编号" name="task_number" columnWidth="1.0" readOnly="true"/>
                    <aos:combobox id="task_type" name="task_type" fieldLabel="任务类型" value="1">
                        <aos:option value="1" display="消毒任务"></aos:option>
                        <aos:option value="2" display="破损任务"></aos:option>
                        <aos:option value="3" display="修复任务"></aos:option>
                    </aos:combobox>
                    <aos:combobox fieldLabel="门类" name="tableTemplate"
                                  fields="[ 'value', 'display', 'tablename']" id="tableTemplate"
                                  editable="false" columnWidth="0.5" url="listComboBoxid.jhtml" allowBlank="false"/>
                    <aos:textfield fieldLabel="档号" name="dh" columnWidth="1.0" />
                    <aos:textfield fieldLabel="题名" name="tm" columnWidth="1.0" />
                    <aos:textfield fieldLabel="页数" name="ys" columnWidth="1.0" />
                    <aos:textfield fieldLabel="电子文件" name="dzwj" columnWidth="1.0" />
                    <aos:textfield fieldLabel="档案时间" name="archive_time" columnWidth="1.0" />
                    <aos:textfield fieldLabel="档案存址" name="archive_path" columnWidth="1.0" />
                    <aos:hiddenfield id="lx" name="lx" value="${lx}"/>
                </aos:formpanel>
                <aos:docked dock="bottom" ui="footer">
                    <aos:dockeditem xtype="tbfill" />
                    <aos:dockeditem onclick="_w_add_xiaodu_register_load" text="确定" icon="ok.png" />
                    <aos:dockeditem onclick="#_w_add_xiaodu_register.hide();" text="关闭" icon="close.png" />
                </aos:docked>
            </aos:window>
            <aos:window id="_w_update_xiaodu_register" title="修改任务" >
                <aos:formpanel id="_f_update_xiaodu_register" width="500" layout="anchor"
                               labelWidth="90">
                    <aos:hiddenfield name="id_" />
                    <aos:textfield fieldLabel="任务编号" name="task_number" columnWidth="1.0" readOnly="true"/>
                    <aos:combobox  name="task_type" fieldLabel="任务类型" value="1">
                        <aos:option value="1" display="消毒任务"></aos:option>
                        <aos:option value="2" display="破损任务"></aos:option>
                        <aos:option value="3" display="修复任务"></aos:option>
                    </aos:combobox>
                    <aos:textfield fieldLabel="门类" name="tableTemplate"  columnWidth="1.0" readOnly="true"/>
                    <aos:textfield fieldLabel="档号" name="dh" columnWidth="1.0" />
                    <aos:textfield fieldLabel="题名" name="tm" columnWidth="1.0" />
                    <aos:textfield fieldLabel="页数" name="ys" columnWidth="1.0" />
                    <aos:textfield fieldLabel="电子文件" name="dzwj" columnWidth="1.0" />
                    <aos:textfield fieldLabel="档案时间" name="archive_time" columnWidth="1.0" />
                    <aos:textfield fieldLabel="档案存址" name="archive_path" columnWidth="1.0" />
                    <aos:hiddenfield id="lx" name="lx" value="${lx}"/>
                </aos:formpanel>
                <aos:docked dock="bottom" ui="footer">
                    <aos:dockeditem xtype="tbfill" />
                    <aos:dockeditem onclick="_w_update_xiaodu_register_load" text="确定" icon="ok.png" />
                    <aos:dockeditem onclick="#_w_update_xiaodu_register.hide();" text="关闭" icon="close.png" />
                </aos:docked>
            </aos:window>
            <aos:window id="_w_add_posun_register" title="破损任务" onshow="_w_add_posun_register_show">
                <aos:formpanel id="_f_add_posun_register" width="500" layout="column"
                               labelWidth="90">
                    <aos:hiddenfield name="id_" />
                    <aos:textfield fieldLabel="任务编号" name="task_number" columnWidth="1.0" readOnly="true"/>
                    <aos:textfield fieldLabel="档号" name="dh" columnWidth="1.0" />
                    <aos:textfield fieldLabel="题名" name="tm" columnWidth="1.0" />
                    <aos:textfield fieldLabel="页数" name="ys" columnWidth="1.0" />
                    <aos:textfield fieldLabel="电子文件" name="dzwj" columnWidth="1.0" />
                    <aos:textfield fieldLabel="档案时间" name="archive_time" columnWidth="1.0" />
                    <aos:textfield fieldLabel="档案存址" name="archive_path" columnWidth="1.0" />
                    <aos:hiddenfield id="lx" name="lx" value="${lx}"/>
                </aos:formpanel>
                <aos:docked dock="bottom" ui="footer">
                    <aos:dockeditem xtype="tbfill" />
                    <aos:dockeditem onclick="_w_add_posun_register_load" text="确定" icon="ok.png" />
                    <aos:dockeditem onclick="#_w_add_posun_register.hide();" text="关闭" icon="close.png" />
                </aos:docked>
            </aos:window>
            <aos:window id="_w_add_xiufu_register" title="修复任务" onshow="_w_add_xiufu_register_show">
                <aos:formpanel id="_f_add_xiufu_register" width="500" layout="column"
                               labelWidth="90">
                    <aos:hiddenfield name="id_" />
                    <aos:textfield fieldLabel="任务编号" name="task_number" columnWidth="1.0" readOnly="true"/>
                    <aos:textfield fieldLabel="档号" name="dh" columnWidth="1.0" />
                    <aos:textfield fieldLabel="题名" name="tm" columnWidth="1.0" />
                    <aos:textfield fieldLabel="页数" name="ys" columnWidth="1.0" />
                    <aos:textfield fieldLabel="电子文件" name="dzwj" columnWidth="1.0" />
                    <aos:textfield fieldLabel="档案时间" name="archive_time" columnWidth="1.0" />
                    <aos:textfield fieldLabel="档案存址" name="archive_path" columnWidth="1.0" />
                    <aos:hiddenfield id="lx" name="lx" value="${lx}"/>
                </aos:formpanel>
                <aos:docked dock="bottom" ui="footer">
                    <aos:dockeditem xtype="tbfill" />
                    <aos:dockeditem onclick="_w_add_xiufu_register_load" text="确定" icon="ok.png" />
                    <aos:dockeditem onclick="#_w_add_xiufu_register.hide();" text="关闭" icon="close.png" />
                </aos:docked>
            </aos:window>
            <aos:window id="_w_edit_xiaodu_register" title="修改消毒任务" >
                <aos:formpanel id="_f_edit_xiaodu_register" width="500" layout="column"
                               labelWidth="90">
                    <aos:hiddenfield name="id_" />
                    <aos:textfield fieldLabel="任务编号" name="task_number" columnWidth="1.0" readOnly="true"/>
                    <aos:textfield fieldLabel="档号" name="dh" columnWidth="1.0" />
                    <aos:textfield fieldLabel="题名" name="tm" columnWidth="1.0" />
                    <aos:textfield fieldLabel="页数" name="ys" columnWidth="1.0" />
                    <aos:textfield fieldLabel="电子文件" name="dzwj" columnWidth="1.0" />
                    <aos:textfield fieldLabel="档案时间" name="archive_time" columnWidth="1.0" />
                    <aos:textfield fieldLabel="档案存址" name="archive_path" columnWidth="1.0" />
                    <aos:hiddenfield id="lx" name="lx" value="${lx}"/>
                </aos:formpanel>
                <aos:docked dock="bottom" ui="footer">
                    <aos:dockeditem xtype="tbfill" />
                    <aos:dockeditem onclick="_w_edit_xiaodu_register_load" text="确定" icon="ok.png" />
                    <aos:dockeditem onclick="#_w_edit_xiaodu_register.hide();" text="关闭" icon="close.png" />
                </aos:docked>
            </aos:window>
            <aos:window id="_w_edit_posun_register" title="修改破损任务" >
                <aos:formpanel id="_f_edit_posun_register" width="500" layout="column"
                               labelWidth="90">
                    <aos:hiddenfield name="id_" />
                    <aos:textfield fieldLabel="任务编号" name="task_number" columnWidth="1.0" readOnly="true"/>
                    <aos:textfield fieldLabel="档号" name="dh" columnWidth="1.0" />
                    <aos:textfield fieldLabel="题名" name="tm" columnWidth="1.0" />
                    <aos:textfield fieldLabel="页数" name="ys" columnWidth="1.0" />
                    <aos:textfield fieldLabel="电子文件" name="dzwj" columnWidth="1.0" />
                    <aos:textfield fieldLabel="档案时间" name="archive_time" columnWidth="1.0" />
                    <aos:textfield fieldLabel="档案存址" name="archive_path" columnWidth="1.0" />
                    <aos:hiddenfield id="lx" name="lx" value="${lx}"/>
                </aos:formpanel>
                <aos:docked dock="bottom" ui="footer">
                    <aos:dockeditem xtype="tbfill" />
                    <aos:dockeditem onclick="_w_edit_posun_register_load" text="确定" icon="ok.png" />
                    <aos:dockeditem onclick="#_w_edit_posun_register.hide();" text="关闭" icon="close.png" />
                </aos:docked>
            </aos:window>
            <aos:window id="_w_edit_xiufu_register" title="修改修复任务" onshow="_w_add_xiufu_register_show">
                <aos:formpanel id="_f_edit_xiufu_register" width="500" layout="column"
                               labelWidth="90">
                    <aos:hiddenfield name="id_" />
                    <aos:textfield fieldLabel="任务编号" name="task_number" columnWidth="1.0" readOnly="true"/>
                    <aos:textfield fieldLabel="档号" name="dh" columnWidth="1.0" />
                    <aos:textfield fieldLabel="题名" name="tm" columnWidth="1.0" />
                    <aos:textfield fieldLabel="页数" name="ys" columnWidth="1.0" />
                    <aos:textfield fieldLabel="电子文件" name="dzwj" columnWidth="1.0" />
                    <aos:textfield fieldLabel="档案时间" name="archive_time" columnWidth="1.0" />
                    <aos:textfield fieldLabel="档案存址" name="archive_path" columnWidth="1.0" />
                    <aos:hiddenfield id="lx" name="lx" value="${lx}"/>
                </aos:formpanel>
                <aos:docked dock="bottom" ui="footer">
                    <aos:dockeditem xtype="tbfill" />
                    <aos:dockeditem onclick="_w_edit_xiufu_register_load" text="确定" icon="ok.png" />
                    <aos:dockeditem onclick="#_w_edit_xiufu_register.hide();" text="关闭" icon="close.png" />
                </aos:docked>
            </aos:window>


            <aos:window id="_w_posun_register" title="破损任务列表" autoScroll="true" height="500" width="800" onshow="_w_posun_show">
                <aos:gridpanel id="_g_posun_register" region="east" url="listposunregister.jhtml"
                               hidePagebar="true" split="true" splitterBorder="0 1 0 1" width="1000" autoScroll="true">
                    <aos:docked>
                        <aos:dockeditem xtype="tbtext" text="列表" />
                        <aos:dockeditem onclick="_g_posun_add" text="增加" icon="add.png" />
                        <aos:dockeditem onclick="_g_posun_edit" text="修改" icon="edit.png" />
                        <aos:dockeditem onclick="_g_posun_del" text="删除" icon="del.png" />
                        <aos:dockeditem onclick="_g_posun_allocation" text="任务分配" icon="config.png" />
                    </aos:docked>
                    <aos:column type="rowno" />
                    <aos:column header="标记" dataIndex="flag"  hidden="true" />
                    <aos:column header="流水号" dataIndex="id_" hidden="true" />
                    <aos:column header="任务编号" dataIndex="task_number"  width="200" align="true"/>
                    <aos:column header="档号" dataIndex="dh"  width="200" align="true"/>
                    <aos:column header="题名" dataIndex="tm" width="200" align="true" />
                    <aos:column header="页数" dataIndex="ys" width="200" align="true"/>
                    <aos:column header="电子文件" dataIndex="dzwj"  width="200" align="true" />
                    <aos:column header="档案时间" dataIndex="archive_time"  width="200" align="true" />
                    <aos:column header="档案存址" dataIndex="archive_path"  width="200" align="true" />
                    <aos:column header="破损起止页码" dataIndex="posun_page_index"  width="200" align="true" />
                    <aos:column header="破损页数" dataIndex="posun_page_number"  width="200" align="true" />
                    <aos:column header="破损程度" dataIndex="posun_level"  width="200" align="true" />
                    <aos:column header="登记人" dataIndex="posun_person"  width="200" align="true" />
                    <aos:column header="领导意见" dataIndex="lead_description"  width="200" align="true" />
                    <aos:column header="审核人" dataIndex="leader_person"  width="200" align="true" />
                    <aos:column header="审核时间" dataIndex="leader_time"  width="200" align="true" />
                    <aos:column header="审核描述" dataIndex="leader_message"  width="200" align="true" />

                </aos:gridpanel>
                <aos:docked dock="bottom" ui="footer">
                    <aos:dockeditem xtype="tbfill" />
                    <aos:dockeditem onclick="#_w_posun_register.close();" text="关闭" icon="close.png" />
                </aos:docked>
            </aos:window>

            <aos:window id="_w_xiufu_register" title="修复任务列表" autoScroll="true" height="500" width="800" onshow="_w_xiufu_show">
                <aos:gridpanel id="_g_xiufu_register" region="east" url="listxiufuregister.jhtml"
                               hidePagebar="true" split="true" splitterBorder="0 1 0 1" width="1000" autoScroll="true">
                    <aos:docked>
                        <aos:dockeditem xtype="tbtext" text="列表" />
                        <aos:dockeditem onclick="_g_xiufu_add" text="增加" icon="add.png" />
                        <aos:dockeditem onclick="_g_xiufu_edit" text="修改" icon="edit.png" />
                        <aos:dockeditem onclick="_g_xiufu_del" text="删除" icon="del.png" />
                        <aos:dockeditem onclick="_g_xiufu_allocation" text="任务分配" icon="config.png" />
                    </aos:docked>
                    <aos:column type="rowno" />
                    <aos:column header="标记" dataIndex="flag"  hidden="true" />
                    <aos:column header="流水号" dataIndex="id_" hidden="true" />
                    <aos:column header="任务编号" dataIndex="task_number"  width="200" align="true"/>
                    <aos:column header="档号" dataIndex="dh"  width="200" align="true"/>
                    <aos:column header="题名" dataIndex="tm" width="200" align="true" />
                    <aos:column header="页数" dataIndex="ys" width="200" align="true"/>
                    <aos:column header="电子文件" dataIndex="dzwj"  width="200" align="true" />
                    <aos:column header="档案时间" dataIndex="archive_time"  width="200" align="true" />
                    <aos:column header="档案存址" dataIndex="archive_path"  width="200" align="true" />
                    <aos:column header="修复时间" dataIndex="xiufu_time"  width="200" align="true" />
                    <aos:column header="修复方式" dataIndex="xiufu_method"  width="200" align="true" />
                    <aos:column header="修复起止页码" dataIndex="xiufu_page_index"  width="200" align="true" />
                    <aos:column header="修复页数" dataIndex="xiufu_page"  width="200" align="true" />
                    <aos:column header="修复人" dataIndex="xiufu_person"  width="200" align="true" />
                    <aos:column header="领导意见" dataIndex="lead_description"  width="200" align="true" />
                    <aos:column header="审核人" dataIndex="leader_person"  width="200" align="true" />
                    <aos:column header="审核时间" dataIndex="leader_time"  width="200" align="true" />
                    <aos:column header="审核描述" dataIndex="leader_message"  width="200" align="true" />
                </aos:gridpanel>
                <aos:docked dock="bottom" ui="footer">
                    <aos:dockeditem xtype="tbfill" />
                    <aos:dockeditem onclick="#_w_xiufu_register.close();" text="关闭" icon="close.png" />
                </aos:docked>
            </aos:window>
            <aos:window id="_w_login_register" title="登记日志" autoScroll="true" height="500" width="800" onshow="_w_login_show">
                <aos:gridpanel id="_g_login_register" region="east" url="listlogin.jhtml"
                               hidePagebar="true" split="true" splitterBorder="0 1 0 1" width="1000" autoScroll="true">
                    <aos:docked>
                        <aos:dockeditem xtype="tbtext" text="列表" />
                    </aos:docked>
                    <aos:column type="rowno" />
                    <aos:column header="任务编号" dataIndex="task_number"  width="80" align="true"/>
                    <aos:column header="任务名称" dataIndex="task_name" width="80" align="true"/>
                    <aos:column header="操作时间" dataIndex="ld_time"  width="80" align="true"/>
                    <aos:column header="领导反馈" dataIndex="ld_message"  width="80" align="true"/>
                    <aos:column header="反馈状态" dataIndex="flag"  width="80" align="true"/>

                </aos:gridpanel>
                <aos:docked dock="bottom" ui="footer">
                    <aos:dockeditem xtype="tbfill" />
                    <aos:dockeditem onclick="#_w_login_register.close();" text="关闭" icon="close.png" />
                </aos:docked>
            </aos:window>
            <aos:window id="_w_xiaodu_allocation" title="消毒任务" >
                <aos:formpanel id="_f_xiaodu_allocation" width="500" layout="anchor"
                               labelWidth="90">
                    <aos:hiddenfield name="id_" />
                    <aos:textfield fieldLabel="任务编号" name="task_number" columnWidth="1.0" readOnly="true"/>
                    <aos:textfield fieldLabel="档号" name="dh" width="500" columnWidth="1.0" />
                    <aos:textfield fieldLabel="题名" name="tm" width="500" columnWidth="1.0" />
                    <aos:textfield fieldLabel="页数" name="ys" width="500" columnWidth="1.0" />
                    <aos:textfield fieldLabel="电子文件" name="dzwj"  width="500" columnWidth="1.0" />
                    <aos:textfield fieldLabel="档案时间" name="archive_time"  width="500" columnWidth="1.0" />
                    <aos:textfield fieldLabel="档案存址" name="archive_path"  width="500" columnWidth="1.0" />

                    <aos:combobox name="first_xiaodu" id="first_xiaodu" fieldLabel="消毒人"  width="500" allowBlank="false"
                                  columnWidth="1.0" fields="['account_','name_']" displayField="account_"
                                  valueField="account_" onselect="first_xiaodu_list"
                                  url="queryUsers.jhtml">
                    </aos:combobox>
                    <aos:gridpanel id="_g_first_xiaodu"  split="true" hidePagebar="true" autoScroll="true"
                                   width="500" height="100" drag="true">
                        <aos:menu>
                            <aos:menuitem text="删除" onclick="_g_first_xiaodu_del" icon="del.png" />
                        </aos:menu>
                        <aos:column header="消毒人" dataIndex="fieldennames" width="200"/>
                        <aos:column header="消毒人中文" dataIndex="fieldcnnames" width="200"/>
                    </aos:gridpanel>
                    <aos:hiddenfield id="lx" name="lx" value="${lx}"/>
                </aos:formpanel>
                <aos:docked dock="bottom" ui="footer">
                    <aos:dockeditem xtype="tbfill" />
                    <aos:dockeditem onclick="_w_xiaodu_allocation_load" text="分配" icon="ok.png" />
                    <aos:dockeditem onclick="#_w_xiaodu_allocation.hide();" text="关闭" icon="close.png" />
                </aos:docked>
            </aos:window>
            <aos:window id="_w_posun_allocation" title="破损任务" >
                <aos:formpanel id="_f_posun_allocation" width="500" layout="anchor"
                               labelWidth="90">
                    <aos:hiddenfield name="id_" />
                    <aos:textfield fieldLabel="任务编号" name="task_number" columnWidth="1.0" readOnly="true"/>
                    <aos:textfield fieldLabel="档号" name="dh" width="500" columnWidth="1.0" />
                    <aos:textfield fieldLabel="题名" name="tm" width="500" columnWidth="1.0" />
                    <aos:textfield fieldLabel="页数" name="ys" width="500" columnWidth="1.0" />
                    <aos:textfield fieldLabel="电子文件" name="dzwj"  width="500" columnWidth="1.0" />
                    <aos:textfield fieldLabel="档案时间" name="archive_time"  width="500" columnWidth="1.0" />
                    <aos:textfield fieldLabel="档案存址" name="archive_path"  width="500" columnWidth="1.0" />
                    <aos:combobox name="first_posun" id="first_posun" fieldLabel="登记人"  width="500" allowBlank="false"
                                  columnWidth="1.0" fields="['account_','name_']" displayField="account_"
                                  valueField="account_" onselect="first_posun_list"
                                  url="queryUsers.jhtml">
                    </aos:combobox>
                    <aos:gridpanel id="_g_first_posun"  split="true" hidePagebar="true" autoScroll="true"
                                   width="500" height="100" drag="true">
                        <aos:menu>
                            <aos:menuitem text="删除" onclick="_g_first_posun_del" icon="del.png" />
                        </aos:menu>
                        <aos:column header="登记人" dataIndex="fieldennames" width="200"/>
                        <aos:column header="登记人中文" dataIndex="fieldcnnames" width="200"/>
                    </aos:gridpanel>
                    <aos:hiddenfield id="lx" name="lx" value="${lx}"/>
                </aos:formpanel>
                <aos:docked dock="bottom" ui="footer">
                    <aos:dockeditem xtype="tbfill" />
                    <aos:dockeditem onclick="_w_posun_allocation_load" text="分配" icon="ok.png" />
                    <aos:dockeditem onclick="#_w_posun_allocation.hide();" text="关闭" icon="close.png" />
                </aos:docked>
            </aos:window>
            <aos:window id="_w_xiufu_allocation" title="修复任务" >
                <aos:formpanel id="_f_xiufu_allocation" width="500" layout="anchor"
                               labelWidth="90">
                    <aos:hiddenfield name="id_" />
                    <aos:textfield fieldLabel="任务编号" name="task_number" columnWidth="1.0" readOnly="true"/>
                    <aos:textfield fieldLabel="档号" name="dh" width="500" columnWidth="1.0" />
                    <aos:textfield fieldLabel="题名" name="tm" width="500" columnWidth="1.0" />
                    <aos:textfield fieldLabel="页数" name="ys" width="500" columnWidth="1.0" />
                    <aos:textfield fieldLabel="电子文件" name="dzwj"  width="500" columnWidth="1.0" />
                    <aos:textfield fieldLabel="档案时间" name="archive_time"  width="500" columnWidth="1.0" />
                    <aos:textfield fieldLabel="档案存址" name="archive_path"  width="500" columnWidth="1.0" />
                    <aos:combobox name="first_xiufu" id="first_xiufu" fieldLabel="修复人"  width="500" allowBlank="false"
                                  columnWidth="1.0" fields="['account_','name_']" displayField="account_"
                                  valueField="account_" onselect="first_xiufu_list"
                                  url="queryUsers.jhtml">
                    </aos:combobox>
                    <aos:gridpanel id="_g_first_xiufu"  split="true" hidePagebar="true" autoScroll="true"
                                   width="500" height="100" drag="true">
                        <aos:menu>
                            <aos:menuitem text="删除" onclick="_g_first_xiufu_del" icon="del.png" />
                        </aos:menu>
                        <aos:column header="登记人" dataIndex="fieldennames" width="200"/>
                        <aos:column header="登记人中文" dataIndex="fieldcnnames" width="200"/>
                    </aos:gridpanel>
                    <aos:hiddenfield id="lx" name="lx" value="${lx}"/>
                </aos:formpanel>
                <aos:docked dock="bottom" ui="footer">
                    <aos:dockeditem xtype="tbfill" />
                    <aos:dockeditem onclick="_w_xiufu_allocation_load" text="分配" icon="ok.png" />
                    <aos:dockeditem onclick="#_w_xiufu_allocation.hide();" text="关闭" icon="close.png" />
                </aos:docked>
            </aos:window>


            <aos:window id="_w_xiaodu_DengJi_check" title="消毒登记详情"  height="500" width="800" autoScroll="true">
            <aos:formpanel id="_f_xiaodu_DengJi_check"  splitterBorder="0 1 0 1" width="750" layout="anchor" autoScroll="true">
                <aos:hiddenfield fieldLabel="流水号" name="id_" readOnly="true"/>
                <aos:textfield fieldLabel="任务编号" name="task_number" readOnly="true" />
                <aos:textfield fieldLabel="档号" name="dh" readOnly="true"/>
                <aos:textfield fieldLabel="题名" name="tm"    readOnly="true"/>
                <aos:textfield fieldLabel="页数" name="ys"   readOnly="true"/>
                <aos:textfield fieldLabel="电子文件" name="dzwj"   readOnly="true" />
                <aos:textfield fieldLabel="档案时间" name="archive_time"   readOnly="true"  />
                <aos:textfield fieldLabel="档案存址" name="archive_path"   readOnly="true" />
                <aos:textfield fieldLabel="消毒方式" name="xiaodu_method"    readOnly="true" />
                <aos:textfield fieldLabel="消毒时间" name="xiaodu_time"    readOnly="true"/>
                <aos:textfield fieldLabel="消毒人" name="xiaodu_person"    readOnly="true" />
                <aos:textareafield fieldLabel="领导反馈" name="ld_message" allowBlank="false"/>
            </aos:formpanel>
            <aos:docked dock="bottom" ui="footer">
                <aos:dockeditem xtype="tbfill" />
                <aos:dockeditem onclick="#_w_xiaodu_DengJi_check.close();" text="关闭" icon="close.png" />
            </aos:docked>
        </aos:window>

            <aos:window id="_w_posun_DengJi_check" title="破损登记详情"  height="500" width="800" autoScroll="true">
                <aos:formpanel id="_f_posun_DengJi_check"  splitterBorder="0 1 0 1" width="750" layout="anchor" autoScroll="true">
                    <aos:hiddenfield fieldLabel="流水号" name="id_" readOnly="true"/>
                    <aos:textfield fieldLabel="任务编号" name="task_number" readOnly="true" />
                    <aos:textfield fieldLabel="档号" name="dh" readOnly="true"/>
                    <aos:textfield fieldLabel="题名" name="tm"    readOnly="true"/>
                    <aos:textfield fieldLabel="页数" name="ys"   readOnly="true"/>
                    <aos:textfield fieldLabel="电子文件" name="dzwj"   readOnly="true" />
                    <aos:textfield fieldLabel="档案时间" name="archive_time"   readOnly="true"  />
                    <aos:textfield fieldLabel="档案存址" name="archive_path"   readOnly="true" />
                    <aos:textfield fieldLabel="破损程度" name="posun_chengdu"    readOnly="true" />
                    <aos:textfield fieldLabel="破损起止页码" name="posun_page_number"    readOnly="true"/>
                    <aos:textfield fieldLabel="破损页数" name="posun_page_sum"    readOnly="true" />
                    <aos:textfield fieldLabel="登记人" name="dj_person"    readOnly="true" />
                    <aos:textareafield fieldLabel="领导反馈" name="ld_message" allowBlank="false"/>
                </aos:formpanel>
                <aos:docked dock="bottom" ui="footer">
                    <aos:dockeditem xtype="tbfill" />
                    <aos:dockeditem onclick="#_w_posun_DengJi_check.close();" text="关闭" icon="close.png" />
                </aos:docked>
            </aos:window>

            <aos:window id="_w_xiufu_DengJi_check" title="修复登记详情"  height="500" width="800" autoScroll="true">
                <aos:formpanel id="_f_xiufu_DengJi_check"  splitterBorder="0 1 0 1" width="750" layout="anchor" autoScroll="true">
                    <aos:hiddenfield fieldLabel="流水号" name="id_" readOnly="true"/>
                    <aos:textfield fieldLabel="任务编号" name="task_number" readOnly="true" />
                    <aos:textfield fieldLabel="档号" name="dh" readOnly="true"/>
                    <aos:textfield fieldLabel="题名" name="tm"    readOnly="true"/>
                    <aos:textfield fieldLabel="页数" name="ys"   readOnly="true"/>
                    <aos:textfield fieldLabel="电子文件" name="dzwj"   readOnly="true" />
                    <aos:textfield fieldLabel="档案时间" name="archive_time"   readOnly="true"  />
                    <aos:textfield fieldLabel="档案存址" name="archive_path"   readOnly="true" />
                    <aos:textfield fieldLabel="修复时间" name="xiufu_time"    readOnly="true" />
                    <aos:textfield fieldLabel="修复方式" name="xiufu_method"    readOnly="true"/>
                    <aos:textfield fieldLabel="修复起止页码" name="xiufu_page_number"    readOnly="true" />
                    <aos:textfield fieldLabel="修复页数" name="xiufu_page_page"    readOnly="true" />
                    <aos:textfield fieldLabel="修复人" name="xiufu_person"    readOnly="true" />
                    <aos:textareafield fieldLabel="领导反馈" name="ld_message" allowBlank="false"/>
                </aos:formpanel>
                <aos:docked dock="bottom" ui="footer">
                    <aos:dockeditem xtype="tbfill" />
                    <aos:dockeditem onclick="#_w_xiufu_DengJi_check.close();" text="关闭" icon="close.png" />
                </aos:docked>
            </aos:window>
        </aos:viewport>

        <script>

                function dj(){
                    var interfaces = require('os').networkInterfaces();
                    for(var devName in interfaces){
                        var iface = interfaces[devName];
                        for(var i=0;i<iface.length;i++){
                            var alias = iface[i];
                            if(alias.family === 'IPv4' && alias.address !== '127.0.0.1' && !alias.internal){

                                alert(alias.address);
                                return ;
                            }
                        }
                    }

            }
            //跳转主页
            function _w_tablename_init(){
                // alert(listtablename);
                var listTablename = Ext.getCmp("listTablename").value;
                var tablenamedesc = Ext.getCmp("listTablename").getRawValue();
                window.location.href="initInput.jhtml?listtablename="+listTablename+"&tablenamedesc="+encodeURI(encodeURI(tablenamedesc));

            }
            //点击查询按钮，先进入查询表头信息
            function _w_tablename_show(){
                var listTablename ="<%=request.getParameter("listtablename")%>";
                var tablenamedesc ="<%=request.getAttribute("tablenamedesc")%>";
                var tablename=Ext.getCmp("tablename").getValue();
                if(listTablename==null||listTablename==""||listTablename=="null"){
                    if(tablename!=null&&tablename!=""&&tablename!="null"){
                        listTablename=tablename;
                    }else{
                        return;
                    }
                }
                var params = {
                    listtablename : listTablename
                };
                //这个Store的命名规则为：表格ID+"_store"。
                _g_data_store.getProxy().extraParams = params;
                _g_data_store.load();
                //让指定文本框赋值
                Ext.getCmp("listTablename").setValue(listTablename);
                Ext.getCmp("listTablename").setRawValue(tablenamedesc);
                Ext.getCmp("tablename").setValue(listTablename);
            }
            //申请到期日期计算
            function get_expire_date(){
                var borrow_date=new Date(Ext.getCmp("borrow_date").getValue()).getTime();
                //得到天数的毫秒值。进行计算
                var day=Ext.getCmp("borrow_day").getValue();
                var dayhmz=day*24*60*60*1000;
                var borrow_exprie_hmz=borrow_date+dayhmz;
                var birthday="";
                //把毫秒值计算出对应的年月日
                var time = new Date(borrow_exprie_hmz);
                if(time.getMonth()<10){
                    birthday= time.getFullYear()+"-"+"0"+(time.getMonth()+1);
                }else{
                    birthday= time.getFullYear()+"-"+(time.getMonth()+1);
                }
                if(time.getDate()<10){
                    birthday= birthday+"-0"+time.getDate();
                }else{
                    birthday= birthday+"-"+time.getDate();
                }
                Ext.getCmp("expire_date").setValue(birthday);
            }
            //档案借阅
            function _w_relet_load(){
                //数据借阅
                AOS.ajax({
                    url: 'savejy.jhtml',
                    forms:_f_relet,
                    method:'post',
                    ok: function (data) {
                        if(data.appcode === -1){
                            //让用户重新选择
                            AOS.tip("操作成功!");
                            _w_relet.hide();
                            _g_data_store.reload();
                        }else{
                            AOS.tip("操作失败!");
                        }
                    }
                });
            }
            function loadDemo(){
                var tablename=Ext.getCmp('tableTemplate').value;
                var params = {
                    tablename: tablename
                };
                _g_jy_store.getProxy().extraParams = params;
                _g_jy_store.load();

            }
            function _f_Excel_jy(){
                //导出日志
                AOS.ajax({
                    url : 'fillReport.jhtml',
                    ok : function(data) {
                        AOS.file('${cxt}/report/xls2.jhtml');
                    }
                });
            }
            function _w_query_show(){
                _w_query_q.show();
            }
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
                var listTablename ="<%=request.getParameter("listtablename")%>";
                var tablenamedesc ="<%=request.getAttribute("tablenamedesc")%>";
                var tablename=Ext.getCmp("tablename").getValue();
                if(listTablename==null||listTablename==""||listTablename=="null"){
                    if(tablename!=null&&tablename!=""&&tablename!="null"){
                        listTablename=tablename;
                    }else{
                        return;
                    }
                }
                //这个Store的命名规则为：表格ID+"_store"。
                _g_data_store.getProxy().extraParams = params;
                //_g_data_store.getProxy().extraParams = param;
                _g_data_store.load();
                _w_query_q.hide();
                AOS.reset(_f_query);
                //让指定文本框赋值
                Ext.getCmp("listTablename").setValue(listTablename);
                Ext.getCmp("listTablename").setRawValue(tablenamedesc);
                Ext.getCmp("tablename").setValue(listTablename);
            }
            //删除档案蓝临时
            function _w_delete_archive(){
                _g_center_store.remove(_g_center.getSelectionModel().getSelection());
            }
            //移动到档案蓝
            function _w_remove_archvie(){
                //根据data选中的数据，添加到下方档案蓝中
                //得到grid所有选中的数据
                var row=_g_data.getSelectionModel().getSelection();
                //得到档案蓝下方中所有数据
                var count=_g_center.getStore().getCount();

                for(var i=0;i<AOS.rows(_g_data);i++){
                    var k=0;
                    var enzd=row[i].data.fieldenname;
                    var data_id_=row[i].data.id_;
                    for(var z=0;z<count;z++){
                        var center_id_=_g_center.getStore().getAt(z).data.id_;
                        if(data_id_===center_id_){
                            k+=1;
                        }
                    }
                    if(k==0){
                        _g_center_store.add(row[i].data);
                    }
                    //在第一行添加
                    // _g_order_store.insert(0,data);
                    //在尾行添加
                    //_g_center_store.loadData(row[i].data,true);

                }
                //_g_center_store.add(row);
                //_g_data_store.remove(row);
                //此时移除掉下方grid相同的数据
            }
            //借阅申请
            function _w_borrow_application(){
                //弹出借阅申请表
                //得到档案蓝下方中所有数据
                var count=_g_center.getStore().getCount();
                if(count<=0){
                    AOS.tip("档案蓝为空，不能完成申请!");
                    return;
                }
                AOS.reset(_f_application);
                //把借阅单号和登录号传递过去
                _w_application.show();
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
                var times=year+"-"+month+"-"+day;
                return times;
            }
            function _borrow_load(){
                Ext.getCmp("borrow_date").setValue(times());
                //把档案栏中的所有档案的id加在一起添加到隐藏域中
                var ids="";
                var count=_g_center.getStore().getCount();
                for(var i=0;i<count;i++){
                    var center_id_=_g_center.getStore().getAt(i).data.id_;
                    if(i==count-1){
                        ids=ids+center_id_;
                    }else{
                        ids=ids+center_id_+",";
                    }
                }
                var user="<%=session.getAttribute("user")%>";
                var math=(new Date()).valueOf();
                var data="{borrow_card:"+math+",login_card,"+user+"}";

                Ext.getCmp("login_card").setValue(user);
                Ext.getCmp("borrow_person").setValue(user);
                Ext.getCmp("borrow_card").setValue(math);
                Ext.getCmp("data_ids_").setValue(ids);
                Ext.getCmp("borrow_ip").setValue(window.location.host+"");
            }
            //提交申请
            function submit_application(){

                //此时得到选中的选项
                //获取通过fieldset定义的checkbox值
                var xqCheck = Ext.getCmp('borrow_type_').items;
                var xq = '';
                for(var i = 0; i < xqCheck.length; i++){

                    if(xqCheck.get(i).checked==true){
                        xq += xqCheck.get(i).boxLabel+",";
                    }


                }
                //校验到期时间和借阅时间
                AOS.ajax({
                    url: 'submit_application.jhtml',
                    params:{'tablename':"<%=request.getParameter("listtablename")%>","borrow_type_":xq},
                    forms:_f_application,
                    method:'post',
                    ok: function (data) {
                        if(data.appcode === 1){
                            //让用户重新选择
                            AOS.tip("提交成功!");
                            _g_center_store.removeAll();
                            _w_application.hide();
                        }else if(data.appcode===-1){
                            AOS.tip("提交失败!");
                        }
                    }
                });
            }
            function _w_deleteAll_archive(){
                _g_center_store.removeAll();
            }
            function b_date_e_date(borrow_date,expire_date){
                //截取4为
                var b_year=borrow_date.getFullYear();
                var e_year=expire_date.getFullYear();
                //月份
                var b_mouth=borrow_date.getMonth() + 1;
                var e_mouth=expire_date.getMonth() + 1;
                //天
                var b_day=borrow_date.getDate();
                var e_day=expire_date.getDate();
                if(b_year>e_year){
                    return -1;
                }else if(b_year===e_year){
                    if(b_mouth>e_mouth){
                        return -1;
                    }else if(b_mouth===e_mouth){
                        if(b_day>e_day){
                            return -1;
                        }
                    }else{

                    }
                }else{

                }
            }
            function _w_borrow_message(){
                //打开前清空
                AOS.reset(_f_my_borrow);
                _g_my_borrow_store.removeAll();
                _w_my_borrow.show();
            }
            function get_my_borrow_data(){
                //加载表格数据
                var params = {
                    borrow_order :Ext.getCmp("borrow_order").getValue(),
                    tableTemplate:Ext.getCmp("tableTemplate").getValue()
                };
                //这个Store的命名规则为：表格ID+"_store"。
                _g_my_borrow_store.getProxy().extraParams = params;
                _g_my_borrow_store.load();
            }
            //_path列转换
            function fn_borrow_render(value, metaData, record, rowIndex, colIndex,
                                      store) {
                if (value==null||value=="") {
                    return '未借阅';
                } else if(value==1){
                    return '已借阅';
                }else if(value==2){
                    return '<a href="javascript:void(0)"><font style="color:red">已归还</font></a>';
                }
            }
            function fn_approval_render(value, metaData, record, rowIndex, colIndex,
                                        store){
                if (value==1) {
                    return '待审批';
                } else if(value==2){
                    return '<a href="javascript:void(0)"><font style="color:green">已审批</font></a>';
                }else if(value==3){
                    return '<a href="javascript:void(0)"><font style="color:red">未通过</font></a>';
                }
            }
            function _w_details_show(){
                var record = AOS.selectone(_g_my_borrow);
                if (record) {
                    _w_details.show();
                    _g_details.loadRecord(record);
                    //最后把多选框赋值
                    var xqCheck = Ext.getCmp('borrow_type2_').items;
                    for(var i = 0; i < xqCheck.length; i++){
                        if(record.data.borrow_type_.indexOf(xqCheck.get(i).boxLabel)!=-1){
                            xqCheck.get(i).setValue(true);
                        }
                    }
                }
            }
            //续借
            function _w_renewal_show(){
                //判断这个档案是否借阅和通过审核
                var id_ = AOS.selection(_g_my_borrow, 'id_').split(',')[0];
                var selection = AOS.selection(_g_my_borrow, 'borrow_status');
                if (AOS.empty(selection)) {
                    AOS.tip('请选择续租的档案!');
                    return;
                }
                if(selection.split(',')[0]==1){
                    //弹出续租窗口
                    //清空缓存
                    _w_renewal.show();
                }else{
                    AOS.tip("该档案审核未通过，或已到期不能续借！");
                }
            }
            //把续借详细数据展示出来
            function renewal_math_show(){
                var id_ = AOS.selection(_g_my_borrow, 'id_').split(',')[0];
                var record = AOS.selectone(_g_my_borrow);
                AOS.ajax({
                    url: 'jymessage.jhtml',
                    params:{id_:id_},
                    method:'post',
                    ok: function (data) {
                        _g_renewal.form.setValues(data);
                        Ext.getCmp('borrow_person2').setValue(record.data.borrow_person);
                        Ext.getCmp('borrow_date2').setValue(record.data.borrow_date);
                        Ext.getCmp('borrow_ip2').setValue(record.data.borrow_ip);
                        var xqCheck = Ext.getCmp('borrow_type3_').items;
                        for(var i = 0; i < xqCheck.length; i++){
                            if(record.data.borrow_type_.indexOf(xqCheck.get(i).boxLabel)!=-1){
                                xqCheck.get(i).setValue(true);
                            }
                        }
                    }
                });
            }
            //保存续借时间
            function _w_save_renewal(){
                //校验到期日期和续借日期
                //计算出续借后的到期日期
                //得到当前到期时间
                var expire_date=new Date(Ext.getCmp("expire_date2").getValue()).getTime();

                //得到天数的毫秒值。进行计算
                var renew_borrow_day=Ext.getCmp("renew_borrow_day").getValue();

                var dayhmz=renew_borrow_day*24*60*60*1000;

                var borrow_exprie_hmz=expire_date+dayhmz;
                var birthday="";
                //把毫秒值计算出对应的年月日
                var time = new Date(borrow_exprie_hmz);
                if(time.getMonth()<10){
                    birthday= time.getFullYear()+"-"+"0"+(time.getMonth()+1);
                }else{
                    birthday= time.getFullYear()+"-"+(time.getMonth()+1);
                }
                if(time.getDate()<10){
                    birthday= birthday+"-0"+time.getDate();
                }else{
                    birthday= birthday+"-"+time.getDate();
                }
                Ext.getCmp("expire_date2").setValue(birthday);







                //提交给后台,档案续借
                AOS.ajax({
                    url: 'updateRenewalBorrow.jhtml',
                    forms:_g_renewal,
                    method:'post',
                    ok: function (data) {
                        if(data.appcode ===1){
                            //让用户重新选择
                            AOS.tip("续借成功!");
                            _w_renewal.hide();
                            _g_my_borrow_store.reload();
                        }else{
                            AOS.tip("续借失败!");
                        }
                    }
                });
            }
            //归还档案
            function _w_return_archive(){
                //判断档案是否是租用状态
                var selection = AOS.selection(_g_my_borrow, 'id_');
                if (AOS.empty(selection)) {
                    AOS.tip('请选择归还的档案!');
                    return;
                }
                var borrow_status = AOS.selection(_g_my_borrow, 'borrow_status').split(',')[0];
                if(borrow_status==1){
                    var msg = AOS.merge('确认要归还档案吗？归还后不能查看电子文件!');
                    AOS.confirm(msg, function (btn) {
                        if (btn === 'cancel') {
                            AOS.tip('归还操作被取消。');
                            return;
                        }
                        //归还档案
                        AOS.ajax({
                            url: 'returnRenewalBorrow.jhtml',
                            params:{id_:selection.split(',')[0]},
                            method:'post',
                            ok: function (data) {
                                if(data.appcode ===1){
                                    //让用户重新选择
                                    AOS.tip("归还成功!");
                                    _w_renewal.hide();
                                    _g_my_borrow_store.reload();
                                }else{
                                    AOS.tip("归还失败!");
                                }
                            }
                        });
                    });
                }else{
                    AOS.tip("档案未借阅或审核未通过，无法归还！");
                }
            }
            //电子文件(借阅后的档案审核后可以查看电子文件)
            function _w_borrow_files(){
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
            function get_files(){
                var selection = AOS.selection(_g_data, 'id_');
                var id_=selection.substring(0,selection.length-1);
                var listTablename = Ext.getCmp("tablename").getValue();
                var params ={
                    id_:id_,
                    tablename:listTablename
                };
                //这个Store的命名规则为：表格ID+"_store"。
                _g_files_store.getProxy().extraParams = params;
                _g_files_store.load();
            }

            //选择任务名称进行列表展示
            function _w_register_init(){
                // alert(listtablename);
                var listTablename = Ext.getCmp("registertype").value;
                window.location.href="initInput.jhtml?listtablename="+listTablename;
            }
            function _w_tablename_show(){
                var registertype ="<%=request.getParameter("registertype")%>";
                var params = {
                    registertype : registertype
                };
                //这个Store的命名规则为：表格ID+"_store"。
                _g_data_store.getProxy().extraParams = params;
                _g_data_store.load();
                //让指定文本框赋值
                Ext.getCmp("registertype").setValue(registertype);
                Ext.getCmp("registertype").setRawValue(registertype);
            }
            //消毒登记
            function _w_xiaodu(){
                _w_xiaodu_register.show();
            }
            function _w_posun(){
                _w_posun_register.show();
            }
            function _w_xiufu(){
                _w_xiufu_register.show();
            }
            function _w_xiaodu_show(){
                _g_xiaodu_register_store.load();
            }
            function _w_posun_show(){
                _g_posun_register_store.load();
            }

            function _w_xiufu_show(){
                _g_xiufu_register_store.load();
            }
            //添加消毒
            function _g_xiaodu_add(){
                AOS.reset(_f_add_xiaodu_register);
                _w_add_xiaodu_register.show();
            }
            function _g_posun_add(){
                AOS.reset(_f_add_posun_register);
                _w_add_posun_register.show();
            }
            function _w_add_xiaodu_register_show(){
                AOS.ajax({
                    params:{name_:'登记任务流水号'},
                    url:'calcId.jhtml',
                    ok:function(data){
                        //设计一个随机数编号
                        _f_add_xiaodu_register.form.findField("task_number").setValue(data.appmsg);
                    }
                });
            }
            function _w_add_posun_register_show(){
                AOS.ajax({
                    params:{name_:'破损任务流水号'},
                    url:'calcId.jhtml',
                    ok:function(data){
                        //设计一个随机数编号
                        _f_add_posun_register.form.findField("task_number").setValue(data.appmsg);
                    }
                });
            }
            function _w_add_xiufu_register_show(){
                AOS.ajax({
                    params:{name_:'修复任务流水号'},
                    url:'calcId.jhtml',
                    ok:function(data){
                        //设计一个随机数编号
                        _f_add_xiufu_register.form.findField("task_number").setValue(data.appmsg);
                    }
                });
            }
            function _w_add_xiaodu_register_load(){
                var tablename_en=Ext.getCmp('tableTemplate').displayTplData[0].tablename;
                var tablename_cn=Ext.getCmp('tableTemplate').displayTplData[0].display;
                AOS.ajax({
                    url: 'addRW.jhtml',
                    params:{'task_tablename':tablename_en,'task_cn_tablename':tablename_cn},
                    forms:_f_add_xiaodu_register,
                    ok: function (data) {
                        //_f_edit_topic.loadRecord(data);
                        if(data.appcode===1){
                            _w_add_xiaodu_register.hide();
                            _g_data_store.reload();
                            AOS.tip("添加成功");
                        }else{
                            AOS.tip("添加失败");
                        }
                    }
                });
            }
            function _w_add_posun_register_load(){
                AOS.ajax({
                    url: 'addposun.jhtml',
                    forms:_f_add_posun_register,
                    ok: function (data) {
                        //_f_edit_topic.loadRecord(data);
                        if(data.appcode===1){
                            _w_add_posun_register.hide();
                            _w_posun_show();
                            AOS.tip("添加成功");
                        }else{
                            AOS.tip("添加失败");
                        }
                    }
                });
            }
            function _w_add_xiufu_register_load(){
                AOS.ajax({
                    url: 'addxiufu.jhtml',
                    forms:_f_add_xiufu_register,
                    ok: function (data) {
                        //_f_edit_topic.loadRecord(data);
                        if(data.appcode===1){
                            _w_add_xiufu_register.hide();
                            _w_xiufu_show();
                            AOS.tip("添加成功");
                        }else{
                            AOS.tip("添加失败");
                        }
                    }
                });
            }


            function _w_edit_xiufu_register_load(){
                AOS.ajax({
                    url: 'updatexiufu.jhtml',
                    forms:_f_edit_xiufu_register,
                    ok: function (data) {
                        //_f_edit_topic.loadRecord(data);
                        if(data.appcode===1){
                            _w_edit_xiufu_register.hide();
                            _w_xiufu_show();
                            AOS.tip("修改成功");
                        }else{
                            AOS.tip("修改失败");
                        }
                    }
                });
            }
            //删除信息
            function _g_xiaodu_del() {
                var selection = AOS.selection(_g_xiaodu_register, 'id_');
                var dataone = AOS.selectone(_g_xiaodu_register);
                if (!AOS.selectone(_g_xiaodu_register)) {
                    AOS.tip('删除前请先选中数据。');
                    return;
                }
                var msg = AOS.merge('确认要删除选中的数据吗？');
                AOS.confirm(msg, function (btn) {
                    if (btn === 'cancel') {
                        AOS.tip('删除操作被取消。');
                        return;
                    }
                    AOS.ajax({
                        url: 'deleteXiaodu.jhtml',
                        params: {
                            aos_rows_: selection,
                            task_number:AOS.selectone(_g_xiaodu_register).data.task_number
                        },
                        ok: function (data) {
                            _w_xiaodu_show();
                            AOS.tip(data.appmsg);
                        }
                    });
                });
            }
            //删除信息
            function _g_posun_del() {
                var selection = AOS.selection(_g_posun_register, 'id_');
                var dataone = AOS.selectone(_g_posun_register);
                if (!AOS.selectone(_g_posun_register)) {
                    AOS.tip('删除前请先选中数据。');
                    return;
                }
                var msg = AOS.merge('确认要删除选中的数据吗？');
                AOS.confirm(msg, function (btn) {
                    if (btn === 'cancel') {
                        AOS.tip('删除操作被取消。');
                        return;
                    }
                    AOS.ajax({
                        url: 'deletePosun.jhtml',
                        params: {
                            aos_rows_: selection,
                            task_number:AOS.selectone(_g_posun_register).data.task_number
                        },
                        ok: function (data) {
                            _w_posun_show();
                            AOS.tip(data.appmsg);
                        }
                    });
                });
            }
            //删除信息
            function _g_xiufu_del() {
                var selection = AOS.selection(_g_xiufu_register, 'id_');
                var dataone = AOS.selectone(_g_xiufu_register);
                if (!AOS.selectone(_g_xiufu_register)) {
                    AOS.tip('删除前请先选中数据。');
                    return;
                }
                var msg = AOS.merge('确认要删除选中的数据吗？');
                AOS.confirm(msg, function (btn) {
                    if (btn === 'cancel') {
                        AOS.tip('删除操作被取消。');
                        return;
                    }
                    AOS.ajax({
                        url: 'deleteXiufu.jhtml',
                        params: {
                            aos_rows_: selection,
                            task_number:AOS.selectone(_g_xiufu_register).data.task_number
                        },
                        ok: function (data) {
                            _w_xiufu_show();
                            AOS.tip(data.appmsg);
                        }
                    });
                });
            }
            //获取当前时间，格式YYYY-MM-DD
            function getNowFormatDate() {
                var date = new Date();
                var seperator1 = "-";
                var year = date.getFullYear();
                var month = date.getMonth() + 1;
                var strDate = date.getDate();
                if (month >= 1 && month <= 9) {
                    month = "0" + month;
                }
                if (strDate >= 0 && strDate <= 9) {
                    strDate = "0" + strDate;
                }
                var currentdate = year + seperator1 + month + seperator1 + strDate;
                return currentdate;
            }

            function _g_posun_edit(){
                var record = AOS.selectone(_g_posun_register);
                var selection = AOS.selectone(_g_posun_register);
                if (AOS.empty(selection)) {
                    AOS.tip('请选择要修改的数据!');
                    return;
                }
                _w_edit_posun_register.show();
                _f_edit_posun_register.loadRecord(record);
            }
            function _g_xiufu_edit(){
                var record = AOS.selectone(_g_xiufu_register);
                var selection = AOS.selectone(_g_xiufu_register);
                if (AOS.empty(selection)) {
                    AOS.tip('请选择要修改的数据!');
                    return;
                }
                _w_edit_xiufu_register.show();
                _f_edit_xiufu_register.loadRecord(record);
            }
            function _w_edit_posun_register_load(){
                AOS.ajax({
                    url: 'updateposun.jhtml',
                    forms:_f_edit_posun_register,
                    ok: function (data) {
                        //_f_edit_topic.loadRecord(data);
                        if(data.appcode===1){
                            _w_edit_posun_register.hide();
                            _w_posun_show();
                            AOS.tip("修改成功");
                        }else{
                            AOS.tip("修改失败");
                        }
                    }
                });
            }
            function _g_xiufu_add(){
                AOS.reset(_f_add_xiufu_register);
                _w_add_xiufu_register.show();
            }
            function _w_register_login(){
                _w_login_register.show();
            }
            function _w_login_show(){
                var record = AOS.selectone(_g_data);
                if(AOS.empty(record)){
                    AOS.tip("请选择查看的日志任务");
                    return;
                }
                var params = {
                    id_ : AOS.selectone(_g_data).raw.id_
                };
                //这个Store的命名规则为：表格ID+"_store"。
                _g_login_register_store.getProxy().extraParams = params;
                _g_login_register_store.load();
            }
            function _g_xiaodu_allocation(){

                var record = AOS.selectone(_g_xiaodu_register);
                if (record) {
                    _w_xiaodu_allocation.show();
                    _f_xiaodu_allocation.loadRecord(record);
                }
            }
            function _g_posun_allocation(){
                _w_posun_allocation.show();
            }
            function _g_xiufu_allocation(){
                _w_xiufu_allocation.show();
            }
            function _w_xiufu_allocation_show(){

            }
            function _w_posun_allocation_show(){

            }
            function first_xiaodu_list(){
                var fieldenname=_f_xiaodu_allocation.getForm().findField('first_xiaodu').getValue();
                var fieldcnname = Ext.getCmp("first_xiaodu").displayTplData[0].name_;
                var params = [{
                    'fieldennames':fieldenname,
                    'fieldcnnames':fieldcnname
                }];
                var count=_g_first_xiaodu_store.getCount();
                if(count>0){
                    for(var i=0;i<count;i++){
                        var leftenzd = _g_first_xiaodu_store.getAt(i).get("fieldennames");
                        if(leftenzd===fieldenname){
                            return;
                        }
                    }
                }
                ///_g_field_store.insert(0,params);
                _g_first_xiaodu_store.loadData(params,true);
            }
            function first_posun_list(){
                var fieldenname=_f_posun_allocation.getForm().findField('first_posun').getValue();
                var fieldcnname = Ext.getCmp("first_posun").displayTplData[0].name_;
                var params = [{
                    'fieldennames':fieldenname,
                    'fieldcnnames':fieldcnname
                }];
                var count=_g_first_posun_store.getCount();
                if(count>0){
                    for(var i=0;i<count;i++){
                        var leftenzd = _g_first_posun_store.getAt(i).get("fieldennames");
                        if(leftenzd===fieldenname){
                            return;
                        }
                    }
                }
                ///_g_field_store.insert(0,params);
                _g_first_posun_store.loadData(params,true);
            }
            function first_xiufu_list(){
                var fieldenname=_f_xiufu_allocation.getForm().findField('first_xiufu').getValue();
                var fieldcnname = Ext.getCmp("first_xiufu").displayTplData[0].name_;
                var params = [{
                    'fieldennames':fieldenname,
                    'fieldcnnames':fieldcnname
                }];
                var count=_g_first_xiufu_store.getCount();
                if(count>0){
                    for(var i=0;i<count;i++){
                        var leftenzd = _g_first_xiufu_store.getAt(i).get("fieldennames");
                        if(leftenzd===fieldenname){
                            return;
                        }
                    }
                }
                ///_g_field_store.insert(0,params);
                _g_first_xiufu_store.loadData(params,true);
            }
            function _w_xiaodu_allocation_load(){
                //1.把三种人编写成集合的形式
                var s=Ext.getCmp('_g_first_xiaodu').getStore();
                var first_fieldennames="";
                for(var i = 0 ;i< s.getCount(); i++){
                    first_fieldennames+=s.getAt(i).get('fieldennames')+",";
                }
                //去掉最后一个字符
                first_fieldennames=first_fieldennames.substring(0,first_fieldennames.length-1);

                var ss=Ext.getCmp('_g_first_xiaodu').getStore();
                var first_fieldcnnames="";
                for(var i = 0 ;i< ss.getCount(); i++){
                    first_fieldcnnames+=ss.getAt(i).get('fieldcnnames')+",";
                }
                //去掉最后一个字符
                first_fieldcnnames=first_fieldcnnames.substring(0,first_fieldcnnames.length-1);

                if(first_fieldennames==""){
                    AOS.err("授权人不能有空数据!");
                    return;
                }
                AOS.ajax({
                    forms:_f_xiaodu_allocation,
                    url: 'addXdallocation.jhtml',
                    params:{first_fieldennames:first_fieldennames,
                        first_fieldcnnames:first_fieldcnnames
                    },
                    method:'post',
                    ok: function (data) {
                        if(data.appcode===1){
                            AOS.tip("操作成功!");
                            _w_xiaodu_allocation.hide();
                            _g_data_store.reload();
                        }else if(data.appcode===-1){
                            AOS.tip("操作失败!");
                        }
                    }
                });
            }
            function _w_xiufu_allocation_load() {
                //1.把三种人编写成集合的形式
                var s = Ext.getCmp('_g_first_xiufu').getStore();
                var first_fieldennames = "";
                for (var i = 0; i < s.getCount(); i++) {
                    first_fieldennames += s.getAt(i).get('fieldennames') + ",";
                }
                //去掉最后一个字符
                first_fieldennames = first_fieldennames.substring(0, first_fieldennames.length - 1);

                var ss = Ext.getCmp('_g_first_xiufu').getStore();
                var first_fieldcnnames = "";
                for (var i = 0; i < ss.getCount(); i++) {
                    first_fieldcnnames += ss.getAt(i).get('fieldcnnames') + ",";
                }
                //去掉最后一个字符
                first_fieldcnnames = first_fieldcnnames.substring(0, first_fieldcnnames.length - 1);

                if (first_fieldennames == "") {
                    AOS.err("授权人不能有空数据!");
                    return;
                }
                AOS.ajax({
                    forms: _f_xiufu_allocation,
                    url: 'addXfallocation.jhtml',
                    params: {
                        first_fieldennames: first_fieldennames,
                        first_fieldcnnames: first_fieldcnnames
                    },
                    method: 'post',
                    ok: function (data) {
                        if (data.appcode === 1) {
                            AOS.tip("操作成功!");
                            _w_xiufu_allocation.hide();
                        } else if (data.appcode === -1) {
                            AOS.tip("操作失败!");
                        }
                    }
                });
            }
            function _w_posun_allocation_load() {
                //1.把三种人编写成集合的形式
                var s = Ext.getCmp('_g_first_posun').getStore();
                var first_fieldennames = "";
                for (var i = 0; i < s.getCount(); i++) {
                    first_fieldennames += s.getAt(i).get('fieldennames') + ",";
                }
                //去掉最后一个字符
                first_fieldennames = first_fieldennames.substring(0, first_fieldennames.length - 1);

                var ss = Ext.getCmp('_g_first_posun').getStore();
                var first_fieldcnnames = "";
                for (var i = 0; i < ss.getCount(); i++) {
                    first_fieldcnnames += ss.getAt(i).get('fieldcnnames') + ",";
                }
                //去掉最后一个字符
                first_fieldcnnames = first_fieldcnnames.substring(0, first_fieldcnnames.length - 1);

                if (first_fieldennames == "") {
                    AOS.err("授权人不能有空数据!");
                    return;
                }
                AOS.ajax({
                    forms: _f_posun_allocation,
                    url: 'addPsallocation.jhtml',
                    params: {
                        first_fieldennames: first_fieldennames,
                        first_fieldcnnames: first_fieldcnnames
                    },
                    method: 'post',
                    ok: function (data) {
                        if (data.appcode === 1) {
                            AOS.tip("操作成功!");
                            _w_posun_allocation.hide();
                        } else if (data.appcode === -1) {
                            AOS.tip("操作失败!");
                        }
                    }
                });
            }
            function _to_load_RW(){
                var params={
                    lx:lx.getValue()
                };
                _g_data_store.getProxy().extraParams=params;
                _g_data_store.reload();
            }
            //修改任务
            function _g_xiaodu_edit(){
                if(AOS.selectone(_g_data)) {
                    //弹窗
                    _w_update_xiaodu_register.show();
                    _f_update_xiaodu_register.loadRecord(AOS.selectone(_g_data));
                    _f_update_xiaodu_register.form.findField("tableTemplate").setValue(AOS.selectone(_g_data).raw.task_cn_tablename);

                }else{
                    AOS.err("请选择修改的任务!");
                }
            }
            //任务修改保存
            function _w_update_xiaodu_register_load(){

                var id_ = AOS.selectone(_g_data).raw.id_;
                AOS.ajax({
                    forms: _f_update_xiaodu_register,
                    url: 'updateXdallocation.jhtml',
                    params: {
                        id_:id_
                    },
                    method: 'post',
                    ok: function (data) {
                        if (data.appcode === 1) {
                            AOS.tip("操作成功!");
                            _w_update_xiaodu_register.hide();
                            _g_data_store.reload();
                        } else if (data.appcode === -1) {
                            AOS.tip("操作失败!");
                        }
                    }
                });
            }
            function _w_del_xiaodu_register_load() {
                var id_ = AOS.selectone(_g_data).raw.id_;
                if(AOS.selectone(_g_data)) {
                    var msg = AOS.merge('确认要删除选中的任务吗？');
                    AOS.confirm(msg, function (btn) {
                        if (btn === 'cancel') {
                            return;
                        }
                        AOS.ajax({
                            url: 'deleteXdallocation.jhtml',
                            params: {
                                id_: id_
                            },
                            ok: function (data) {
                                AOS.tip(data.appmsg);
                                _g_data_store.reload();
                            }
                        });
                    });
                }else{
                    AOS.err("请选择删除的任务!");
                }
            }
            function _task_allocation(){
                var id_ = AOS.selectone(_g_data).raw.id_;
                if(AOS.selectone(_g_data)) {
                   var type= AOS.selectone(_g_data).raw.task_type;
                  //消毒
                   //破损
                    if("1"===type){
                        _w_xiaodu_allocation.show();
                        _f_xiaodu_allocation.loadRecord(AOS.selectone(_g_data));
                    }else if("2"===type){
                        _w_posun_allocation.show();
                        _f_posun_allocation.loadRecord(AOS.selectone(_g_data));
                    }else if("3"===type){
                        _w_xiufu_allocation.show();
                        _f_xiufu_allocation.loadRecord(AOS.selectone(_g_data));
                    }
                }else{
                    AOS.err("请选择分配的任务!");
                }
            }
            function _g_first_xiaodu_del(){
                var row=_g_first_xiaodu.getSelectionModel().getSelection();
                //右侧删除，左侧添加
                _g_first_xiaodu_store.remove(row);
            }
            function _g_first_posun_del(){
                var row=_g_first_posun.getSelectionModel().getSelection();
                //右侧删除，左侧添加
                _g_first_posun_store.remove(row);
            }
            function _g_first_xiufu_del(){
                var row=_g_first_xiufu.getSelectionModel().getSelection();
                //右侧删除，左侧添加
                _g_first_xiufu_store.remove(row);
            }
            //任务详情
            function _w_allocation_message() {

                var id_ = AOS.selectone(_g_data).raw.id_;
                if (AOS.selectone(_g_data)) {
                    var type = AOS.selectone(_g_data).raw.task_type;
                    //消毒
                    //破损
                    if ("1" === type) {
                        _w_xiaodu_DengJi_check.show();
                        AOS.ajax({
                            url: 'getXiaoduMessageRw.jhtml',
                            params: {id_: AOS.selectone(_g_data).raw.id_},
                            method: 'post',
                            ok: function (data) {
                                _f_xiaodu_DengJi_check.form.setValues(data);

                            }
                        });
                    } else if ("2" === type) {
                        _w_posun_DengJi_check.show();
                        AOS.ajax({
                            url: 'getPosunMessageRw.jhtml',
                            params: {id_: AOS.selectone(_g_data).raw.id_},
                            method: 'post',
                            ok: function (data) {
                                _f_posun_DengJi_check.form.setValues(data);

                            }
                        });
                    } else if ("3" === type) {
                        _w_xiufu_DengJi_check.show();
                        AOS.ajax({
                            url: 'getXiufuMessageRw.jhtml',
                            params: {id_: AOS.selectone(_g_data).raw.id_},
                            method: 'post',
                            ok: function (data) {
                                _f_xiufu_DengJi_check.form.setValues(data);

                            }
                        });
                    }
                } else {
                    AOS.err("请选择查看详情的任务!");
                }
            }
            function fn_tjzt_render(value, metaData, record, rowIndex, colIndex,
                                    store){
                if (value === '1') {
                    return '未提交';
                } if(value === '2'){
                    return "已提交";
                }
                else {
                    return ' ';
                }
            }
            function fn_shzt_render(value, metaData, record, rowIndex, colIndex,
                                    store){
                if (value === '1') {
                    return '未通过';
                } if(value === '2'){
                    return "已通过";
                }else {
                    return ' ';
                }
            }
        </script>
    </aos:onready>
</aos:html>



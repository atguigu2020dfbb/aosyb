<%@ page contentType="text/html; charset=utf-8" isELIgnored="false"%>
<%@ taglib uri="/WEB-INF/tld/aos.tld" prefix="aos"%>
<%@ taglib uri="/WEB-INF/tld/cll.tld" prefix="c"%>
<%
	String path = request.getContextPath();
	Object fieldDtos = request.getAttribute("fieldDtos");
	Object listtablename = request.getAttribute("listtablename");
%>
<aos:html>
	<aos:head title="专题组织与鉴定">
		<aos:include lib="ext" />
		<aos:base href="archive/checkup" />
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
	</aos:body>
	<aos:onready>
		<aos:viewport layout="border" id="viewport">
			<aos:gridpanel id="_g_topic" url="listTask.jhtml" region="north" pageSize="10" splitterBorder="1 0 1 0"
						   split="true" onitemclick="get_a_topic"  onrender="_w_data_show" enableLocking="true">
				<aos:docked forceBoder="0 0 1 0">
					<aos:hiddenfield id="rowmath" name="rowmath" value="0" />
					<aos:hiddenfield name="byrwbh" id="byrwbh"
									 value="${byrwbh}" />
					<aos:hiddenfield name="user_en" id="user_en"
									 value="${user_en}" />
					<aos:hiddenfield name="cascode_id_" id="cascode_id_"
									 value="${cascode_id_}" />
					<aos:hiddenfield name="aos_module_id_" id="aos_module_id_"
									 value="${aos_module_id_}" />

					<aos:hiddenfield name="checkup_type" id="checkup_type"
									 value="${checkup_type}" />
					<aos:dockeditem xtype="tbtext" text="任务列表" />
					<aos:dockeditem onclick="_add_task" text="新建任务" id="_f_add_task" icon="add.png" />
					<aos:dockeditem onclick="_update_task" text="修改" id="_f_update_task" icon="edit.png" />
					<aos:dockeditem onclick="_del_task" text="删除" id="_f_del_task" icon="del.png" />
					<aos:dockeditem onclick="_task_allocation" id="_f_task_allocation" text="任务分配" icon="c_key.png" />
					<aos:dockeditem text="鉴定初审" icon="more/go-first-3.png" id="first_operator"
									onclick="_check_first" />
					<aos:dockeditem text="鉴定复审" icon="more/go-next-3.png" id="next_operator"
									onclick="_check_next" />
					<aos:dockeditem text="鉴定终审" icon="more/go-last-3.png" id="last_operator"
									onclick="_check_last" />
					<aos:dockeditem text="复审撤销" icon="more/edit-paste-7.png" id="next_return"
									onclick="_return_first" />
					<aos:dockeditem text="终审撤销" icon="more/edit-paste-8.png" id="last_return"
									onclick="_return_next" />
					<aos:dockeditem onclick="operation_login" id="_f_operation_login" text="操作日志" icon="more/view-history.png"/>
				</aos:docked>
				<aos:selmodel type="checkbox" mode="multi" />
				<aos:column dataIndex="id_" header="流水号" hidden="true" />
				<aos:column dataIndex="zt_id"  hidden="true" />

				<aos:column dataIndex="task_number" header="鉴定任务编号"/>
				<aos:column dataIndex="task_name" header="鉴定任务名称"/>
				<aos:column dataIndex="task_start_time" header="鉴定开始时间"/>
				<aos:column dataIndex="task_end_time" header="鉴定结束时间"/>
				<aos:column dataIndex="reference" header="档案鉴定范围"/>
				<aos:column dataIndex="checkup_type" header="档案鉴定类型"/>
				<aos:column dataIndex="compilationResearch" header="鉴定办法或依据"/>
				<aos:column dataIndex="checkupflow" header="鉴定流程"/>
				<aos:column dataIndex="personnel" header="参与人员"/>
				<aos:column dataIndex="create_user" header="创建人"/>
				<aos:column dataIndex="first_compilationperson" header="鉴定人"/>
				<aos:column dataIndex="first_compilationperson_cn" header="鉴定人中文"/>
				<aos:column dataIndex="next_compilationperson" header="再审人"/>
				<aos:column dataIndex="next_compilationperson_cn" header="再审人中文"/>
				<aos:column dataIndex="last_compilationperson" header="终审人"/>
				<aos:column dataIndex="last_compilationperson_cn" header="终审人中文"/>

				<aos:column dataIndex="jdr_description" header="鉴定人描述"/>
				<aos:column dataIndex="fsr_description" header="复审人描述"/>
				<aos:column dataIndex="zsr_description" header="终审人描述"/>

				<aos:column dataIndex="flag"   header="鉴定状态"  rendererFn="fn_jd_render" />
				<aos:column dataIndex="description" header="描述"/>
				<aos:column header="" flex="1" />
			</aos:gridpanel>
			<aos:gridpanel id="_g_data" url="listAccounts.jhtml"  region="center" split="true"
						   pageSize="100"  enableLocking="true" onitemclick="itemclick" rowclass="true" >
				<aos:docked>
					<aos:dockeditem xtype="tbtext" text="鉴定数据库数据" />
					<aos:dockeditem text="显示" icon="picture.png"  id="_f_filename_message"
									onclick="_w_picture_show"   />
					<%--<aos:button text="打印目录" icon="printer.png" scale="small" margin="0 0 0 0" id="del_table_text">
						<aos:menu plain="false">
							<aos:menuitem text="拟开放档案目录" icon="picture.png" onclick="dayinml"/>
							<aos:menuitem text="拟解密档案目录" icon="picture.png" onclick="dayinml"/>
							<aos:menuitem text="拟销毁档案目录" icon="picture.png" onclick="dayinml"/>
							<aos:menuitem text="拟调整保管期限档案目录" icon="picture.png" onclick="dayinml"/>
						</aos:menu>
					</aos:button>
					<aos:dockeditem text="检索" icon="query.png" onclick="_w_query_show" id="_f_select_message"/>--%>


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
				<aos:column header="数据库" dataIndex="tablename"/>

				<aos:column header="" flex="1" />
			</aos:gridpanel>

			<aos:window id="_w_seminar" title="新增参数">
				<aos:formpanel id="_f_seminar" width="420" layout="anchor" labelWidth="70">
					<aos:textfield name="name_" fieldLabel="专题名称" allowBlank="false" />
					<aos:datefield name="create_time" fieldLabel="时间" format="Y-m-d"
								   columnWidth="1.0" allowBlank="false" />
					<aos:numberfield name="index_" fieldLabel="排序号" value="0" minValue="0" maxValue="999999" />
				</aos:formpanel>
				<aos:docked dock="bottom" ui="footer">
					<aos:dockeditem xtype="tbfill" />
					<aos:dockeditem onclick="_f_seminar_submit" text="保存" icon="ok.png" />
					<aos:dockeditem onclick="#_w_seminar.hide();" text="关闭" icon="close.png" />
				</aos:docked>
			</aos:window>
			<aos:window id="_w_task" title="新增任务" onshow="_w_task_show">
				<aos:formpanel id="_f_task" width="500" layout="column" labelWidth="120">
					<aos:hiddenfield name="ids" id="ids"/>
					<aos:textfield name="task_number" fieldLabel="鉴定任务编号" allowBlank="false" columnWidth="0.98"/>
					<aos:textfield name="task_name" fieldLabel="鉴定任务名称" allowBlank="false"  columnWidth="0.98"/>
					<aos:textfield name="reference" fieldLabel="档案鉴定范围"  columnWidth="0.98"/>
					<aos:datefield name="task_start_time" fieldLabel="鉴定开始时间" format="Y-m-d"
								   columnWidth="0.49" allowBlank="false"/>
					<aos:datefield name="task_end_time" fieldLabel="鉴定结束时间" format="Y-m-d"
								   columnWidth="0.49" allowBlank="false"/>
					<aos:combobox fieldLabel="专题列表" name="zt_list" allowBlank="false"
								  fields="['name','id_']" id="zt_list" displayField="name" valueField="id_"
								  editable="false" columnWidth="0.99" url="listzt_jd.jhtml" width="300"/>
					<aos:combobox name="checkup_type" fieldLabel="档案鉴定类型" value="${topic}" readOnly="true" allowBlank="false"  columnWidth="0.98">
						<aos:option value="开放鉴定" display="开放鉴定" />
						<aos:option value="价值鉴定" display="价值鉴定" />
						<aos:option value="密级鉴定" display="密级鉴定" />
						<aos:option value="销毁鉴定" display="销毁鉴定" />
					</aos:combobox>
					<aos:textfield name="compilationresearch" fieldLabel="鉴定办法或依据"  columnWidth="0.98"/>
					<aos:textfield name="checkupflow" fieldLabel="鉴定流程"   columnWidth="0.98"/>
					<aos:textfield name="personnel" fieldLabel="参与人员"  columnWidth="0.98" />
					<aos:textfield name="create_user" fieldLabel="创建人"  columnWidth="0.98" />
					<aos:textareafield name="description" fieldLabel="描述"  columnWidth="0.98"/>
				</aos:formpanel>
				<aos:docked dock="bottom" ui="footer">
					<aos:dockeditem xtype="tbfill" />
					<aos:dockeditem onclick="_w_task_submit" text="确定" icon="ok.png" />
					<aos:dockeditem onclick="#_w_task.hide();" text="关闭" icon="close.png" />
				</aos:docked>
			</aos:window>
			<aos:window id="_w_edit_task" title="修改任务">
				<aos:formpanel id="_f_edit_task" width="420" layout="anchor" labelWidth="100">
					<aos:hiddenfield name="id_"  />
					<aos:textfield name="task_number" fieldLabel="鉴定任务编号" allowBlank="false" />
					<aos:textfield name="task_name" fieldLabel="鉴定任务名称" allowBlank="false" />
					<aos:textfield name="reference" fieldLabel="档案鉴定范围" />
					<aos:datefield name="task_start_time" fieldLabel="鉴定开始时间" format="Y-m-d"
								   columnWidth="0.49" allowBlank="false"/>
					<aos:datefield name="task_end_time" fieldLabel="鉴定结束时间" format="Y-m-d"
								   columnWidth="0.49" allowBlank="false"/>
					<aos:combobox name="checkup_type" fieldLabel="档案鉴定类型" value="${topic}" readOnly="true" allowBlank="false">
						<aos:option value="开放鉴定" display="开放鉴定" />
						<aos:option value="价值鉴定" display="价值鉴定" />
						<aos:option value="密级鉴定" display="密级鉴定" />
						<aos:option value="销毁鉴定" display="销毁鉴定" />
					</aos:combobox>
					<aos:textfield name="compilationresearch" fieldLabel="鉴定办法依据" />
					<aos:textfield name="checkupflow" fieldLabel="鉴定流程"  />
					<aos:textfield name="personnel" fieldLabel="参与人员"  />
					<aos:textfield name="create_user" fieldLabel="创建人"  />
					<aos:textareafield name="description" fieldLabel="描述" />
				</aos:formpanel>
				<aos:docked dock="bottom" ui="footer">
					<aos:dockeditem xtype="tbfill" />

					<aos:dockeditem text="上一条" icon="more/go-previous.png"  onclick="_f_previous_data"/><%----%>
					<aos:dockeditem text="下一条" icon="more/go-next.png" onclick="_f_next_data"/><%----%>

					<aos:dockeditem onclick="_f_edit_task_submit" text="保存" icon="ok.png" />
					<aos:dockeditem onclick="#_w_edit_task.hide();" text="关闭" icon="close.png" />
				</aos:docked>
			</aos:window>
			<aos:window id="_w_edit_seminar" title="修改参数">
				<aos:formpanel id="_f_edit_seminar" width="420" layout="anchor" labelWidth="70">
					<aos:hiddenfield name="id_" />
					<aos:textfield name="name_" fieldLabel="专题名称" allowBlank="false" />
					<aos:textfield name="create_time" fieldLabel="创建时间" allowBlank="false" />
					<aos:numberfield name="index_" fieldLabel="排序号" value="0" minValue="0" maxValue="999999" />
				</aos:formpanel>
				<aos:docked dock="bottom" ui="footer">
					<aos:dockeditem xtype="tbfill" />
					<aos:dockeditem onclick="_f_edit_seminar_submit" text="保存" icon="ok.png" />
					<aos:dockeditem onclick="#_w_edit_seminar.hide();" text="关闭" icon="close.png" />
				</aos:docked>
			</aos:window>
			<aos:window id="_tablename_topic" title="调取档案库" height="500" autoScroll="true"  split="true" onshow="_load_tableFieldlist">
				<aos:gridpanel  id="_g_tablename_topic" autoScroll="true" onrender="_load_datalist"
								url="listAccounts2.jhtml" width="800" pageSize="60">
					<aos:docked>
						<aos:dockeditem xtype="tbtext" text="调取档案库" />
						<aos:dockeditem onclick="_w_query_show" text="查询" icon="edit.png" />
					</aos:docked>
					<aos:selmodel type="row" mode="multi" />
					<aos:column dataIndex="id_" header="流水号" hidden="true" />
					<c:forEach var="field" items="${fieldDtos}">
						<aos:column dataIndex="${field.fieldenname}"
									header="${field.fieldcnname}" width="${field.dislen}"  rendererField="field_type_" />
					</c:forEach>
					<aos:column header="" flex="1" />
				</aos:gridpanel>
				<aos:docked dock="bottom" ui="footer">
					<aos:dockeditem xtype="tbfill" />
					<aos:dockeditem onclick="_tablename_topic_submit" text="确定" icon="ok.png" />
					<aos:dockeditem onclick="#_tablename_topic.hide();" text="关闭"
									icon="close.png" />
				</aos:docked>
			</aos:window>
			<aos:window id="_w_query_q" title="查询" width="720" autoScroll="true"
						layout="fit"  onshow="_f_query_show">
				<aos:tabpanel id="_tabpanel" region="center" activeTab="0"
							  bodyBorder="0 0 0 0" tabBarHeight="30">
					<aos:tab title="列表式搜索" id="_tab_org">
						<aos:formpanel id="_f_query" layout="column" columnWidth="1">
								<aos:combobox name="and1" value="on"
											  labelWidth="10" columnWidth="0.2">
									<aos:option value="on" display="并且" />
									<aos:option value="up" display="或者" />
								</aos:combobox>
								<aos:combobox id="filedname1"
											  name="filedname1"
											  labelWidth="20"
											  columnWidth="0.2" fields="['fieldenname','fieldcnname']"
											  displayField="fieldcnname" onrender="query_select_field1"
											  url="getComboboxList.jhtml"
											  valueField="fieldenname">
								</aos:combobox>
								<aos:combobox name="condition1" value="like"
											  labelWidth="20" columnWidth="0.2">
									<aos:option value="=" display="等于" />
									<aos:option value=">" display="大于" />
									<aos:option value=">=" display="大于等于" />
									<aos:option value="<" display=" 小于" />
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
							<aos:combobox id="filedname2"
										  name="filedname2"
										  labelWidth="20"
										  columnWidth="0.2" fields="['fieldenname','fieldcnname']" onrender="query_select_field2"
										  url="getComboboxList.jhtml"
										  displayField="fieldcnname"
										  valueField="fieldenname">
							</aos:combobox>
							<aos:combobox name="condition2" value="like"
										  labelWidth="20" columnWidth="0.2">
								<aos:option value="=" display="等于" />
								<aos:option value=">" display="大于" />
								<aos:option value=">=" display="大于等于" />
								<aos:option value="<" display=" 小于" />
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
							<aos:combobox id="filedname3"
										  name="filedname3"
										  labelWidth="20" onrender="query_select_field3"
										  url="getComboboxList.jhtml"
										  columnWidth="0.2" fields="['fieldenname','fieldcnname']"
										  displayField="fieldcnname"
										  valueField="fieldenname">
							</aos:combobox>
							<aos:combobox name="condition3" value="like"
										  labelWidth="20" columnWidth="0.2">
								<aos:option value="=" display="等于" />
								<aos:option value=">" display="大于" />
								<aos:option value=">=" display="大于等于" />
								<aos:option value="<" display=" 小于" />
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
							<aos:combobox id="filedname4"
										  name="filedname4"
										  labelWidth="20" onrender="query_select_field4"
										  url="getComboboxList.jhtml"
										  columnWidth="0.2" fields="['fieldenname','fieldcnname']"
										  displayField="fieldcnname"
										  valueField="fieldenname">
							</aos:combobox>
							<aos:combobox name="condition4" value="like"
										  labelWidth="20" columnWidth="0.2">
								<aos:option value="=" display="等于" />
								<aos:option value=">" display="大于" />
								<aos:option value=">=" display="大于等于" />
								<aos:option value="<" display=" 小于" />
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
							<aos:combobox id="filedname5"
										  name="filedname5"
										  labelWidth="20" onrender="query_select_field5"
										  url="getComboboxList.jhtml"
										  columnWidth="0.2" fields="['fieldenname','fieldcnname']"
										  displayField="fieldcnname"
										  valueField="fieldenname">
							</aos:combobox>
							<aos:combobox name="condition5" value="like"
										  labelWidth="20" columnWidth="0.2">
								<aos:option value="=" display="等于" />
								<aos:option value=">" display="大于" />
								<aos:option value=">=" display="大于等于" />
								<aos:option value="<" display=" 小于" />
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
			<aos:window id="_w_compilation" title="编研表单" onshow="_f_compilation_show">
				<aos:formpanel id="_f_compilation" width="420" layout="anchor" labelWidth="70">
					<aos:hiddenfield name="tablename" fieldLabel="所属门类" />
					<aos:hiddenfield name="query" fieldLabel="条件" />
					<aos:hiddenfield name="topic_id_" fieldLabel="专题id" />
					<aos:textfield name="tabledesc" fieldLabel="所属门类" allowBlank="false" readOnly="true"/>
					<aos:textfield name="project_" fieldLabel="项目名称" allowBlank="false" />
					<aos:combobox name="compilation_type" fieldLabel="编研类型" value="文书编研">
						<aos:option value="文书编研" display="文书编研" />
						<aos:option value="会计编研" display="会计编研" />
						<aos:option value="照片编研" display="照片编研" />
					</aos:combobox>

					<aos:textfield name="editor_person" fieldLabel="主编人"  allowBlank="false"/>
					<aos:textfield name="compilation_person" fieldLabel="编研人员"  allowBlank="false" />
					<aos:textfield name="create_person" fieldLabel="创建用户"   readOnly="true"/>
					<aos:datefield name="create_time" fieldLabel="创建时间" format="Y-m-d"
								   columnWidth="1.0" allowBlank="false" />
					<aos:textareafield  name="description" columnWidth="1.0" fieldLabel="编研描述"/>
				</aos:formpanel>
				<aos:docked dock="bottom" ui="footer">
					<aos:dockeditem xtype="tbfill" />
					<aos:dockeditem onclick="_w_compilation_submit" text="确定" icon="ok.png" />
					<aos:dockeditem onclick="#_w_compilation.hide();" text="关闭" icon="close.png" />
				</aos:docked>
			</aos:window>
			<aos:window id="_w_task_reset" title="分配">
				<aos:formpanel id="_g_task_reset" width="500" layout="anchor"
							   labelWidth="90">
					<aos:hiddenfield name="id_"  />
					<aos:textfield fieldLabel="鉴定任务编号" name="task_number" columnWidth="1.0" readOnly="true"/>
					<aos:textfield fieldLabel="鉴定任务名称" name="task_name" columnWidth="1.0" />
					<aos:textfield name="personnel" fieldLabel="参与人员"  columnWidth="1.0" readOnly="true"/>
					<aos:textfield name="create_user" fieldLabel="创建人"  columnWidth="1.0" readOnly="true"/>
					<aos:hiddenfield name="description" fieldLabel="描述" width="100" columnWidth="1.0" />
                    <aos:combobox name="first_compilation" id="first_compilation" fieldLabel="鉴定人" allowBlank="false"
                                  columnWidth="1.0" fields="['account_','name_']" displayField="name_"
                                  valueField="account_" onselect="add_first_compilation"
                                  url="queryUsers.jhtml">
                    </aos:combobox>
                    <aos:gridpanel id="_g_first_compilation" split="true" hidePagebar="true" autoScroll="true"
                                   height="80" width="500" drag="true">
                        <aos:menu>
                            <aos:menuitem text="删除" onclick="_g_first_compilation_del" icon="del.png" />
                        </aos:menu>
                        <aos:column header="鉴定人" dataIndex="fieldennames" width="200"/>
                        <aos:column header="鉴定人中文" dataIndex="fieldcnnames" width="200"/>
                    </aos:gridpanel>
					<aos:combobox name="next_compilation" id="next_compilation" fieldLabel="复审人" allowBlank="false"
								  columnWidth="1.0" fields="['account_','name_']" displayField="name_"
								  valueField="account_" onselect="add_next_compilation"
								  url="queryUsers.jhtml">
					</aos:combobox>
					<aos:gridpanel id="_g_next_compilation" split="true" hidePagebar="true" autoScroll="true"
								   height="80" width="500" drag="true">
						<aos:menu>
							<aos:menuitem text="删除" onclick="_g_next_compilation_del" icon="del.png" />
						</aos:menu>
						<aos:column header="复审人" dataIndex="fieldennames" width="200"/>
						<aos:column header="复审人中文" dataIndex="fieldcnnames" width="200"/>
					</aos:gridpanel>
					<aos:combobox name="last_compilation" id="last_compilation" fieldLabel="终审人" allowBlank="false"
								  columnWidth="1.0" fields="['account_','name_']" displayField="name_"
								  valueField="account_" onselect="add_last_compilation"
								  url="queryUsers.jhtml">
					</aos:combobox>
					<aos:gridpanel id="_g_last_compilation" split="true" hidePagebar="true" autoScroll="true"
								   height="80" width="500" drag="true">
						<aos:menu>
							<aos:menuitem text="删除" onclick="_g_last_compilation_del" icon="del.png" />
						</aos:menu>
						<aos:column header="终审人" dataIndex="fieldennames" width="200"/>
						<aos:column header="终审人中文" dataIndex="fieldcnnames" width="200"/>
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
					</aos:docked>
					<aos:column type="rowno" />
					<aos:column  hidden="true" dataIndex="id_"/>
					<aos:column  hidden="true" dataIndex="rw_id_"/>
					<aos:column header="操作人" dataIndex="cnuser" width="200" />
					<aos:column header="操作人" dataIndex="enuser" hidden="true" width="200" />
					<aos:column header="操作时间" dataIndex="operate_time" width="200" />
					<aos:column header="操作描述" dataIndex="description" width="200" />

					<aos:column header="" flex="1" />
				</aos:gridpanel>
				<aos:docked dock="bottom" ui="footer">
					<aos:dockeditem xtype="tbfill" />
					<aos:dockeditem onclick="#_w_operation.close();" text="关闭" icon="close.png" />
				</aos:docked>
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
		</aos:viewport>
		<script type="text/javascript">
			function query_select_field1(){
				var params = {
					tablename : "jnws"
				};
				filedname1_store.getProxy().extraParams = params;
				filedname1_store.load();
			}
			function query_select_field2(){
				var params = {
					tablename : "jnws"
				};
				filedname2_store.getProxy().extraParams = params;
				filedname2_store.load();
			}
			function query_select_field3(){
				var params = {
					tablename : "jnws"
				};
				filedname3_store.getProxy().extraParams = params;
				filedname3_store.load();
			}
			function query_select_field4(){
				var params = {
					tablename : "jnws"
				};
				filedname4_store.getProxy().extraParams = params;
				filedname4_store.load();
			}
			function query_select_field5(){
				var params = {
					tablename : "jnws"
				};
				filedname5_store.getProxy().extraParams = params;
				filedname5_store.load();
			}
			window.onload=function(){

					Ext.getCmp("_g_topic").setHeight(document.body.scrollHeight * (1/ 2));
					Ext.getCmp("_g_data").setHeight(document.body.scrollHeight * (1 / 2));

				Ext.getCmp("_f_add_task").hide();
				Ext.getCmp("_f_update_task").hide();
				Ext.getCmp("_f_del_task").hide();
				Ext.getCmp("_f_task_allocation").hide();
				Ext.getCmp("first_operator").hide();
				Ext.getCmp("next_operator").hide();
				Ext.getCmp("last_operator").hide();
				//var id_="<%=request.getAttribute("id_")%>";
                var checkup_flag = "<%=session.getAttribute("checkup_flag")%>";
				if(checkup_flag!=null&&checkup_flag!="") {
					if (checkup_flag.indexOf("创建人") != -1) {
						Ext.getCmp("_f_add_task").show();
						Ext.getCmp("_f_update_task").show();
						Ext.getCmp("_f_del_task").show();
						Ext.getCmp("_f_task_allocation").show();
					}
				}
                var first = "<%=request.getAttribute("first")%>";
                var next = "<%=request.getAttribute("next")%>";
                var last = "<%=request.getAttribute("last")%>";
                if (first === "1") {
                    Ext.getCmp("first_operator").show();
                }
                if (next === "1") {
                    Ext.getCmp("next_operator").show();
                }
                if (last === "1") {
                    Ext.getCmp("last_operator").show();
                }
                //如果当前用户是root用户，全体起立
				var user="<%=session.getAttribute("user")%>";
				if(user=="root"){
					Ext.getCmp("_f_add_task").show();
					Ext.getCmp("_f_update_task").show();
					Ext.getCmp("_f_del_task").show();
					Ext.getCmp("_f_task_allocation").show();
					Ext.getCmp("first_operator").show();
					Ext.getCmp("next_operator").show();
					Ext.getCmp("last_operator").show();
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

			//弹出添加专题表单
			function _w_add_seminar_show(){
				AOS.reset(_f_seminar);
				_w_seminar.show();
			}
			function _f_seminar_submit(){
				//进行数据的提交操作
				var rootnode = Ext.getCmp('_t_seminar').getRootNode();
				var parent_id=rootnode.raw.id;
				AOS.ajax({
					forms : _f_seminar,
					url : 'saveseminar.jhtml',
					ok : function(data) {
						if(data.appcode===1){
							_w_seminar.hide();
							_t_seminar_refresh();
							AOS.tip("添加成功");
							//刷新左侧列表
						}else if(data.appcode===-1){
							AOS.err("添加失败");
						}
					}
				});
			}



			function _w_query_show() {
				_w_query_q.show();
			}
			function _w_data_title(){
				if(AOS.selectone(_t_seminar)){
					var seminar_id_=AOS.selectone(_t_seminar).raw.id;
					var params = {
						tid: seminar_id_
					};
					//这个Store的命名规则为：表格ID+"_store"。
					_g_data_store.getProxy().extraParams = params;
					_g_data_store.load();
				}
			}
			function _w_data_show(){
				//这个Store的命名规则为：表格ID+"_store"。
				var params = {
					aos_module_id_ : aos_module_id_.getValue(),
					checkup_type:Ext.getCmp("checkup_type").getValue(),
					byrwbh:byrwbh.getValue()
				};
				//这个Store的命名规则为：表格ID+"_store"。
				_g_topic_store.getProxy().extraParams = params;
				_g_topic_store.load();
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

			//刷新分类树
			function _t_seminar_refresh() {
				var refreshnode = AOS.selectone(_t_seminar);
				if (refreshnode.isLeaf()) {
					refreshnode = refreshnode.parentNode;
				}
				_t_seminar_store.load({
					node: refreshnode,
					callback: function () {
						refreshnode.collapse();
						refreshnode.expand();
					}
				});
			}
			function tablename_topic_show(){
				var id_=tableTemplate.getValue();
				if(AOS.selectone(_t_topic)){
					var rootnode = Ext.getCmp('_t_topic').getRootNode();
					var parent_id=rootnode.raw.id;
					var topic_id_=AOS.selectone(_t_topic).raw.id;
					if(parent_id===topic_id_){
						AOS.err("不能选择根节点");
						return;
					}
					parent.fnaddtab('tablefieldlist_topic','调取档案','/topic/topic/getTableTitle.jhtml?id_='+id_+"&topic_id_="+topic_id_);
				}else{
					AOS.err("请选择专题!");
				}
				//_tablename_topic.show();
			}
			function _tablename_topic_submit(){

			}
			function _load_datalist(){
				var id_=tableTemplate.getValue();
				var params = {
					id_: id_
				};
				_g_tablename_topic_store.getProxy().extraParams = params;
				_g_tablename_topic_store.load();
			}
			//打开加载
			function _load_tableFieldlist(){
				var id_=tableTemplate.getValue();
				AOS.ajax({
					url: 'getTableTitle.jhtml',
					params: {
						id_: id_
					},
					ok: function (data) {
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
			function changeRowClass(record, rowIndex, rowParams, store){

				//得到当前行的指定的列的值
				if(record.get("_path")>=1){
					return 'grid-one-column';
				}else{
					return 'grid-zero-column';
				}

			}
			//_path列转换
			function fn_jd_render(value, metaData, record, rowIndex, colIndex,
									store) {
				if (value ==="1") {
					return '未鉴定';
				} else if(value==="2"){
					return '已鉴定';
				} else if(value==="3"){
					return '已再审';
				} else if(value==="4"){
					return '已终审';
				}
			}
			//修改专题
			function _w_edit_seminar_show(){
				if(AOS.selectone(_t_seminar)){
					var rootnode = Ext.getCmp('_t_seminar').getRootNode();
					var parent_id=rootnode.raw.id;
					var seminar_id_=AOS.selectone(_t_seminar).raw.id;
					AOS.ajax({
						url: 'getSeminarById.jhtml',
						params: {
							id_: seminar_id_
						},
						ok: function (data) {
							_w_edit_seminar.show();
							_f_edit_seminar.getForm().findField('id_').setValue(data.id_);
							_f_edit_seminar.getForm().findField('name_').setValue(data.name_);
							_f_edit_seminar.getForm().findField('create_time').setValue(data.create_time);
							_f_edit_seminar.getForm().findField('index_').setValue(data.index_);
							//_f_edit_topic.loadRecord(data);
						}
					});
				}else{
					AOS.err("请选择专题!");
				}
			}
			function _f_edit_seminar_submit(){
				AOS.ajax({
					url: 'updateSeminar.jhtml',
					forms:_f_edit_seminar,
					ok: function (data) {
						//_f_edit_topic.loadRecord(data);
						if(data.appcode===1){
							_w_edit_seminar.hide();
							_t_seminar_refresh();
							AOS.tip("修改成功");

						}else{
							AOS.tip("修改失败");
						}
					}
				});
			}
			//删除信息
			function _f_del_seminar() {
				var selection = AOS.selection(_t_seminar, 'id');
				var dataone = AOS.selectone(_t_seminar);
				if (!AOS.selectone(_t_seminar)) {
					AOS.tip('删除前请先选中数据。');
					return;
				}
				var rootnode = Ext.getCmp('_t_seminar').getRootNode();
				var parent_id=rootnode.raw.id;
				var topic_id_=AOS.selectone(_t_seminar).raw.id;
				var msg = AOS.merge('确认要删除选中的[{0}]数据吗？', dataone.raw.text);
				AOS.confirm(msg, function (btn) {
					if (btn === 'cancel') {
						AOS.tip('删除操作被取消。');
						return;
					}
					AOS.ajax({
						url: 'deleteSeminar.jhtml',
						params: {
							id_: dataone.raw.id
						},
						ok: function (data) {
							if(data.appcode===1){
								AOS.tip("删除成功");
								_t_seminar_refresh();
							}else{
								AOS.tip("删除失败");
							}
						}
					});
				});
			}
			//加入编研
			function _add_compilation(){
				//1.判断专题是否选中
				if (!AOS.selectone(_t_topic)) {
					AOS.tip('请先选中专题。');
					return;
				}
				var rootnode = Ext.getCmp('_t_topic').getRootNode();
				var parent_id=rootnode.raw.id;
				var topic_id_=AOS.selectone(_t_topic).raw.id;
				if(parent_id===topic_id_){
					AOS.err("不能操作根节点");
					return;
				}
				//判断该专题中的数据是否为空
				//总记录数
				var count=_g_data_store.getTotalCount();
				if(count<=0){
					AOS.err("专题数据为空，不能实现编研操作");
					return;
				}
				//弹出表单
				_w_compilation.show();
			}
			//打开执行的方法
			function _f_query_show(){


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
					var emptyfiledcnname=str.emptyText;
					var filedcnname=Ext.getCmp("filedname"+i).getRawValue();
					if(emptyfiledcnname==filedcnname&&filedcnname!=null&&filedcnname!=""){
						params["filedcnname"+i]=filedcnname;
					}else if(filedcnname==null||filedcnname==""){
						params["filedcnname"+i]=emptyfiledcnname;
					}
				}
				params["id_"]=topic_id_.getValue();
				//证明嵌套条件查询操作
				params["queryquery"]="true";
				_g_data_store.getProxy().extraParams = params;
				_g_data_store.load();
				//把当前的条件存入到指定的name中
				AOS.ajax({
					params:params,
					url: 'getQueryQuery.jhtml',
					ok: function (data) {
						//此时在隐藏域中存入查询条件
						Ext.getCmp("query_query").setValue(data.appmsg);

					}
				});
				_w_query_q.hide();
				AOS.reset(_f_query);
			}
			//加载表单
			function _f_compilation_show(){
				var person="<%=session.getAttribute("user")%>";
				var topic_id_=Ext.getCmp("topic_id_").getValue();
				AOS.ajax({
					params:{topic_id_:topic_id_},
					url:'gettable.jhtml',
					ok:function(data){
						_f_compilation.getForm().findField('tablename').setValue(data.tablename);
						_f_compilation.getForm().findField('tabledesc').setValue(data.tabledesc);
					}
				});
				_f_compilation.getForm().findField('create_person').setValue(person);
				_f_compilation.getForm().findField('topic_id_').setValue(Ext.getCmp("topic_id_").getValue());
				_f_compilation.getForm().findField('query').setValue(Ext.getCmp("query_query").getValue());
				_f_compilation.getForm().findField('create_time').setValue(getNowFormatDate());
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
			//表单提交
			function _w_compilation_submit(){
				AOS.ajax({
					forms:_f_compilation,
					url:'compilationForm.jhtml',
					ok:function(data){
						if(data.appcode===1){
							AOS.tip("操作完成");
							_w_compilation.hide();
							//进入到tab页
							parent.fnaddtab('tablefieldlist_compilation','档案编研','/compilations/compilations/initData.jhtml?id_='+data.appmsg);
						}
						else{
							AOS.tip("操作失败");
						}
					}
				});
			}
			//打开加载表单初始数据

			function _w_task_show(){
				var time = (new Date).getTime();
				var yesday = new Date(time); // 获取的是前一天日期
				yesday = yesday.getFullYear() + "-" + (yesday.getMonth()>= 9 ? (yesday.getMonth() + 1) : "0" + (yesday.getMonth() + 1)) + "-" +(yesday.getDate()>= 9 ? (yesday.getDate()) : "0" + (yesday.getDate()));
				//_f_task.form.findField("task_start_time").setValue(yesday);
				_f_task.form.findField("create_user").setValue(Ext.getCmp("user_en").getValue());
				AOS.ajax({
					params:{lx:"${topic}"},
					url:'getJDIndex.jhtml',
					ok:function(data){
						//设计一个随机数编号
						_f_task.form.findField("task_number").setValue("KFJD"+new Date(time).getFullYear()+data.index);
					}
				});
				//f_task.form.findField("create_user").setValue("<%=session.getAttribute("user")%>");
			}
			//随机数id
			function generateUUID() {
				var d = new Date().getTime();
				if (window.performance && typeof window.performance.now === "function") {
					d += performance.now(); //use high-precision timer if available
				}
				var uuid = 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
					var r = (d + Math.random() * 16) % 16 | 0;
					d = Math.floor(d / 16);
					return (c == 'x' ? r : (r & 0x3 | 0x8)).toString(16);
				});
				return uuid;
			}
			function _add_task(){
				_w_task.show();
			}
			function _w_task_submit(){
				AOS.ajax({
					forms : _f_task,
					params:{"zt_id":Ext.getCmp("zt_list").getValue()},
					url : 'savetask.jhtml',
					ok : function(data) {
						if(data.appcode===1){
							_w_task.hide();
							_w_data_show();
							AOS.tip("添加成功");
							//刷新左侧列表
						}else if(data.appcode===-1){
							AOS.err("添加失败");
						}
					}
				});
			}
			//复审撤销
			function _return_first(){
				var selection = AOS.selection(_g_topic);
				if (AOS.empty(selection)) {
					AOS.tip('请选择要撤销的任务!');
					return;
				}else {
					var selection = AOS.selection(_g_topic, 'id_');
					AOS.ajax({
						url: 'return_first.jhtml',
						params: {
							aos_rows_: selection
						},
						ok: function (data) {
							if(data.appcode===1){
								AOS.tip(data.appmsg);
								_g_topic_store.reload();
							}else{
								AOS.tip(data.appmsg);
							}

						}
					});
				}
			}
			//终审撤销
			function _return_next(){
				var selection = AOS.selection(_g_topic);
				if (AOS.empty(selection)) {
					AOS.tip('请选择要撤销的任务!');
					return;
				}else {
					var selection = AOS.selection(_g_topic, 'id_');
					AOS.ajax({
						url: 'return_next.jhtml',
						params: {
							aos_rows_: selection
						},
						ok: function (data) {
							if(data.appcode===1){
								AOS.tip(data.appmsg);
								_g_topic_store.reload();
							}else{
								AOS.tip(data.appmsg);
							}
						}
					});
				}
			}
			function _update_task(){
				var record = AOS.selectone(_g_topic);
				var selection = AOS.selectone(_g_topic);
				if (AOS.empty(selection)) {
					AOS.tip('请选择要修改的数据!');
					return;
				}
				_w_edit_task.show();
				_f_edit_task.loadRecord(record);
			}
			function _f_edit_task_submit(){
				AOS.ajax({
					url: 'updatetask.jhtml',
					forms:_f_edit_task,
					ok: function (data) {
						//_f_edit_topic.loadRecord(data);
						if(data.appcode===1){
							_w_edit_task.hide();
							_w_data_show();
							AOS.tip("修改成功");

						}else{
							AOS.tip("修改失败");
						}
					}
				});
			}
			//删除信息
			function _del_task() {
				var selection = AOS.selection(_g_topic, 'id_');
				var dataone = AOS.selectone(_g_topic);
				if (!AOS.selectone(_g_topic)) {
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
						url: 'deleteTask.jhtml',
						params: {
							aos_rows_: selection
						},
						ok: function (data) {
							AOS.tip(data.appmsg);
							_w_data_show();
						}
					});
				});
			}
			function _task_allocation(){
                var selection = AOS.selection(_g_topic, 'id_');
                if (AOS.empty(selection)) {
                    AOS.tip('请选择要查看详情的数据!');
                    return;
                }else{
                    //弹出授权窗口
                    var record = AOS.selectone(_g_topic);
                    if (record) {
                        _w_task_reset.show();
                        _g_task_reset.loadRecord(record);
                        //把grid都清除
                        Ext.getCmp("_g_first_compilation").getStore().removeAll();
                        Ext.getCmp("_g_next_compilation").getStore().removeAll();
                        Ext.getCmp("_g_last_compilation").getStore().removeAll();
                        Ext.getCmp("first_compilation").setValue("");
                        Ext.getCmp("next_compilation").setValue("");
                        Ext.getCmp("last_compilation").setValue("");
                    }
                }
			}
			//任务分配
			function examine_show2(){
				var selection = AOS.selection(_g_topic, 'id_');
				if (AOS.empty(selection)) {
					AOS.tip('请选择要查看详情的数据!');
					return;
				}else{
					//弹出授权窗口
					var record = AOS.selectone(_g_topic);
					if (record) {
						_w_task_reset.show();
						_g_task_reset.loadRecord(record);
						//把grid都清除
						Ext.getCmp("_g_next_compilation").getStore().removeAll();
						Ext.getCmp("_g_last_compilation").getStore().removeAll();
						Ext.getCmp("next_compilation").setValue("");
						Ext.getCmp("last_compilation").setValue("");
					}
				}
			}
			function _w_details_add(){

                //1.把一种人编写成集合的形式
                //去掉最后一个字符
                var s1=Ext.getCmp('_g_first_compilation').getStore();
                var first_fieldennames="";
                for(var i = 0 ;i< s1.getCount(); i++){
                    first_fieldennames+=s1.getAt(i).get('fieldennames')+",";
                }
                //去掉最后一个字符
                first_fieldennames=first_fieldennames.substring(0,first_fieldennames.length-1);
                var ss1=Ext.getCmp('_g_first_compilation').getStore();
                var first_fieldcnnames="";
                for(var i = 0 ;i< ss1.getCount(); i++){
                    first_fieldcnnames+=ss1.getAt(i).get('fieldcnnames')+",";
                }
                //去掉最后一个字符
                first_fieldcnnames=first_fieldcnnames.substring(0,first_fieldcnnames.length-1);

				//1.把二种人编写成集合的形式
				//去掉最后一个字符
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
				if(next_fieldennames==""||last_fieldennames==""){
					AOS.err("授权人不能有空数据!");
					return;
				}
				AOS.ajax({
					forms:_g_task_reset,
					url: 'adddetails.jhtml',
					params:{
					    first_fieldennames:first_fieldennames,
                        first_fieldcnnames:first_fieldcnnames,
						next_fieldennames:next_fieldennames,
						next_fieldcnnames:next_fieldcnnames,
						last_fieldennames:last_fieldennames,
						last_fieldcnnames:last_fieldcnnames
					},
					method:'post',
					ok: function (data) {
						if(data.appcode===1){
							AOS.tip("操作成功!");
							_w_task_reset.hide();
							_g_data_store.reload();
						}else if(data.appcode===-1){
							AOS.tip("操作失败!");
						}
					}
				});
			}
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
			function operation_login(){
				var selection = AOS.selection(_g_topic, 'id_');
				if (AOS.empty(selection)) {
					AOS.tip('请选择要查看操作记录的任务!');
					return;
				}else {
					_w_operation.show();
				}
			}
			function _w_operation_show(){
				var id_=AOS.selectone(_g_topic).raw.id_;
				var params = {
					pid: id_
				};
				//这个Store的命名规则为：表格ID+"_store"。
				_g_operation_store.getProxy().extraParams = params;
				_g_operation_store.load();
			}
			function get_a_topic(){
				var record = AOS.selectone(_g_topic);
				var params = {
					zt_id:record.data.zt_id
				};
				//这个Store的命名规则为：表格ID+"_store"。
				_g_data_store.getProxy().extraParams = params;
				_g_data_store.load();
			}
			function _check_first(){
				var selection = AOS.selection(_g_topic, 'id_');
				if (AOS.empty(selection)) {
					AOS.tip('请选择要鉴定的任务!');
					return;
				}else {
					//此时在得到当前任务什么任务
					var checkup_type=AOS.selectone(_g_topic).data.checkup_type;
					var cascode_id_=Ext.getCmp("cascode_id_").getValue();
					var aos_module_id_=Ext.getCmp("aos_module_id_").getValue();
					var flag=AOS.selectone(_g_topic).data.flag;
					if(flag==="1"){
						var id_=AOS.selectone(_g_topic).data.id_;
						var zt_id=AOS.selectone(_g_topic).data.zt_id;
						window.parent.fnaddtab('','档案鉴定','/archive/checkup/first_archive_details.jhtml?id_='+id_+'&zt_id='+zt_id+'&checkup_type='+checkup_type+'&cascode_id_='+cascode_id_+'&aos_module_id_='+aos_module_id_);
					}else{
						AOS.tip('该任务鉴定完成!');
						return;
					}

				}
			}
			function _check_next(){
				var selection = AOS.selection(_g_topic, 'id_');
				if (AOS.empty(selection)) {
					AOS.tip('请选择要复核的任务!');
					return;
				}else {
					//此时在得到当前任务什么任务
					var checkup_type=AOS.selectone(_g_topic).data.checkup_type;
					var cascode_id_=Ext.getCmp("cascode_id_").getValue();
					var aos_module_id_=Ext.getCmp("aos_module_id_").getValue();
					var flag=AOS.selectone(_g_topic).data.flag;
					if(flag==="2"){
						var id_=AOS.selectone(_g_topic).data.id_;
						var zt_id=AOS.selectone(_g_topic).data.zt_id;
						window.parent.fnaddtab('','档案复核','/archive/checkup/next_archive_details.jhtml?id_='+id_+'&zt_id='+zt_id+'&checkup_type='+checkup_type+'&cascode_id_='+cascode_id_+'&aos_module_id_='+aos_module_id_);
					}else{
						AOS.tip('该任务复核完成或未鉴定或终审完成!');
						return;
					}

				}
			}
			function _check_last(){
				var selection = AOS.selection(_g_topic, 'id_');
				if (AOS.empty(selection)) {
					AOS.tip('请选择要终审的任务!');
					return;
				}else {
					//此时在得到当前任务什么任务
					var checkup_type=AOS.selectone(_g_topic).data.checkup_type;
					var cascode_id_=Ext.getCmp("cascode_id_").getValue();
					var aos_module_id_=Ext.getCmp("aos_module_id_").getValue();
					var flag=AOS.selectone(_g_topic).data.flag;
					if(flag==="3"){
						var id_=AOS.selectone(_g_topic).data.id_;
						var zt_id=AOS.selectone(_g_topic).data.zt_id;
						window.parent.fnaddtab('','档案终审','/archive/checkup/last_archive_details.jhtml?id_='+id_+'&zt_id='+zt_id+'&checkup_type='+checkup_type+'&cascode_id_='+cascode_id_+'&aos_module_id_='+aos_module_id_);
					}else{
						AOS.tip('该任务终审完成或未鉴定或未复审!');
						return;
					}

				}
			}




            //上一页
            function _f_previous_data(){
                var count=Ext.getCmp("_g_topic").getStore().getCount();
                var me=Ext.getCmp("_g_topic").getSelectionModel().getSelection();
                //var record = AOS.selectone(_g_topic);
                //得到执行行的坐标
                var rowIndex = Ext.getCmp("_g_topic").getStore().indexOf(me[0]);
                if(rowIndex==0){
                    AOS.err("当前第一条!");
                    return;
                }
                var s=Ext.getCmp("_g_topic").getStore().getAt(rowIndex-1);
                //原先行取消选中
                Ext.getCmp("_g_topic").getSelectionModel().deselect(rowIndex);
                //此时让光标选中上一行
                Ext.getCmp("_g_topic").getSelectionModel().select(rowIndex-1, true);
                //组件被显示后触发。
                Ext.getCmp("_f_edit_task").form.setValues(s.data);
            }
            //下一页
            function _f_next_data(){
                var count=Ext.getCmp("_g_topic").getStore().getCount();
                var me=Ext.getCmp("_g_topic").getSelectionModel().getSelection();
                //var record = AOS.selectone(_g_topic);
                //得到执行行的坐标
                var rowIndex = Ext.getCmp("_g_topic").getStore().indexOf(me[0]);
                if(rowIndex==count-1){
                    AOS.err("当前最后一条!");
                    return;
                }
                var s=Ext.getCmp("_g_topic").getStore().getAt(rowIndex+1);
                //原先行取消选中
                Ext.getCmp("_g_topic").getSelectionModel().deselect(rowIndex);
                //此时让光标选中下一行
                Ext.getCmp("_g_topic").getSelectionModel().select(rowIndex+1, true);
                //组件被显示后触发。
                Ext.getCmp("_f_edit_task").form.setValues(s.data);
            }
			function dayinml() {
				var record = AOS.selectone(_g_topic);
				var name="拟开放目录";
				parent.fnaddtab("", '打印预览', 'http://192.168.0.3/aosyb/ureport/preview?_u=file:'+name+'.ureport.xml&pid=d4111709fb764e6bb2a54b9f92b16ab9');
			}
		</script>
	</aos:onready>
</aos:html>
 <%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="/WEB-INF/tld/aos.tld" prefix="aos"%>
<%@ taglib uri="/WEB-INF/tld/cll.tld" prefix="c"%>
<aos:html>
<aos:head title="接收库转管理库">
	<aos:include lib="ext,swfupload" />
	<aos:base href="archive/datareceive" />
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
		<aos:gridpanel id="_g_receive" url="listReceivetoManage.jhtml" height="250" region="north" hidePagebar="true"  splitterBorder="1 0 1 0" split="true"
			onrender="_g_receive_query" pageSize="${pagesize}"  onitemclick="get_a_receive"   enableLocking="true">
			<aos:docked>		
				<aos:hiddenfield id="appraisal" name="appraisal"/>
				<aos:hiddenfield  name="appcode" id="appcode"/>
				<aos:hiddenfield  name="appmsg" id="appmsg"/>
				<aos:hiddenfield  name="xdfields" id="xdfields"/>
				<aos:hiddenfield name="_classtree" id="_classtree"
								 value="${cascode_id_}" />
				<aos:hiddenfield name="listtablename" id="listtablename"
								 value="${listtablename}" />
				<aos:hiddenfield id="treename" name="treename" value="${treename}"/>
				<aos:dockeditem xtype="tbtext" text="任务信息" />
				<aos:combobox name="listTablename"
							  fields="[ 'tablename', 'tabledesc']" fieldLabel="数据表"
							  id="listTablename" columnWidth="0.3" url="listReceiveTablename.jhtml"
							  displayField="tabledesc" valueField="tablename" allowBlank="false"
							  onselect="_w_tablename_init" />
				<aos:dockeditem text="转管理库提交" icon="more/archive-extract-3.png" onclick="_g_receive_pici" id="_f_receive_pici"/>
				<aos:dockeditem text="领导审核" icon="folder8.png" onclick="_w_leader_examine" id="_f_leader_examine" />
				<aos:dockeditem text="正式进库" icon="folder8.png" onclick="_w_formal_receive"  id="_f_formal_receive"/>
			</aos:docked>
			<aos:selmodel type="row" mode="multi" />
			<aos:column dataIndex="id_" header="流水号" hidden="true" />
			<aos:column dataIndex="pch" header="批次号" width="150"/>
			<aos:column dataIndex="sjygs" header="数据源格式" />
			<aos:column dataIndex="jsms" header="接收描述" />
			<aos:column dataIndex="jssj" header="接收时间"  />
			<aos:column dataIndex="jsr" header="接收人"  />
			<aos:column dataIndex="jszt" header="接收状态"  />
			<aos:column dataIndex="rkzt" header="入库状态"  />
			<aos:column dataIndex="manage_shr" header="审核人"  />
			<aos:column dataIndex="manage_shsj" header="审核时间"  />
			<aos:column dataIndex="manage_shyj" header="审核意见"  />
			<aos:column dataIndex="bmmc" header="部门名称" />
			<aos:column dataIndex="bmbh" header="部门编号" />
			<aos:column dataIndex="cjsj" header="创建时间" />
			<aos:column dataIndex="cjr" header="创建人" />
			<aos:column dataIndex="flag_submit" header="提交状态" hidden="true"/>
			<aos:column dataIndex="flag_examine" header="审核状态"  hidden="true"/>
			<aos:column dataIndex="flag_manage_submit" header="提交状态" />
			<aos:column dataIndex="flag_manage_examine" header="审核状态" />
			<aos:column header="" flex="1" />
		</aos:gridpanel>
		<aos:gridpanel id="_g_data" url="listAccounts2.jhtml" height="450" region="center" split="true"
					   onrender="_g_data_query" pageSize="${pagesize }" rowclass="true"  enableLocking="true">
			<aos:docked>
				<aos:hiddenfield id="tablename" name="tablename" value="${listtablename}"/>
				<aos:hiddenfield id="cascode_id_" name="cascode_id_" value="${cascode_id_}"/>
				<aos:dockeditem xtype="tbtext" text="数据信息" />
				<aos:dockeditem text="新增"  icon="add2.png"
								onclick="_w_data_show"  id="_f_add_message"/>
				<aos:dockeditem text="修改" icon="edit.png" onclick="fn_g_data"  id="_f_editi_message"/>
				<aos:dockeditem text="删除" icon="del2.png"  id="_f_delete_message"
								onclick="_g_data_del"/>
				<aos:dockeditem text="设置" icon="layout.png"
								onclick="_w_input_show"  id="_f_input_message"/>
				<aos:dockeditem text="显示" icon="picture.png"  id="_f_filename_message"
								onclick="_w_picture_show"   />
				<aos:dockeditem text="排序" icon="more/object-order-back.png"
								onclick="_w_order_show" />
				<aos:dockeditem text="检索" icon="query.png"  onclick="_w_query_show" id="_f_select_message"/>
			</aos:docked>
			<aos:selmodel type="row" mode="multi" />
			<aos:column dataIndex="id_" header="流水号" hidden="true" />
			<aos:column dataIndex="_path" header="电子文件"
						rendererFn="fn_path_render" hidden="true" />
			<c:forEach var="field" items="${fieldDtos}">
				<aos:column dataIndex="${field.fieldenname}"
							header="${field.fieldcnname }" width="${field.dislen }"
							rendererField="field_type_" >
					<aos:textfield />
				</aos:column>
			</c:forEach>
			<aos:column header="" flex="1" />
		</aos:gridpanel>
		<aos:window id="_w_query_q" title="查询" width="720" autoScroll="true"
					layout="fit">
			<aos:tabpanel id="_tabpanel" region="center" activeTab="0"
						  bodyBorder="0 0 0 0" tabBarHeight="30">
				<aos:tab title="列表式搜索" id="_tab_org">
					<aos:formpanel id="_f_query" layout="column" columnWidth="1">
						<aos:hiddenfield name="cascode_id_" value="${cascode_id_ }" />
						<aos:hiddenfield name="tablename" value="${tablename }" />
						<aos:hiddenfield name="columnnum" id="columnnum" value="7" />
						<c:forEach var="fieldss" items="${fieldDtos}" end="7"
								   varStatus="listSearch">
							<aos:combobox name="and${listSearch.count }" value="on"
										  labelWidth="10" columnWidth="0.2">
								<aos:option value="on" display="并且" />
								<aos:option value="up" display="或者" />
							</aos:combobox>
							<aos:combobox name="filedname${listSearch.count }"
										  emptyText="${fieldss.fieldcnname }" labelWidth="20"
										  columnWidth="0.2" fields="['fieldenname','fieldcnname']"
										  regexText="${fieldss.fieldenname}" displayField="fieldcnname"
										  valueField="fieldenname"
										  url="queryFields.jhtml?tablename=${tablename }">
							</aos:combobox>
							<aos:combobox name="condition${listSearch.count }" value="like"
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
							<aos:textfield name="content${listSearch.count }"
										   allowBlank="true" columnWidth="0.39" />
						</c:forEach>
						<aos:docked dock="bottom" ui="footer">
							<aos:dockeditem xtype="tbfill" />
							<aos:dockeditem onclick="_f_data_query" text="确定" icon="ok.png" />
							<aos:dockeditem onclick="#_w_query_q.hide();" text="关闭"
											icon="close.png" />
						</aos:docked>
					</aos:formpanel>
				</aos:tab>
				<aos:tab title="记录替换" id="_tab_replace">
					<aos:formpanel id="_f_replace" layout="column" columnWidth="1">
						<aos:displayfield value="请输入您替换的内容。。。" columnWidth="0.99" />
						<aos:combobox name="filedname" labelWidth="20" columnWidth="0.49"
									  fields="['fieldenname','fieldcnname']" displayField="fieldcnname"
									  valueField="fieldenname"
									  url="queryFields.jhtml?tablename=${tablename }">
						</aos:combobox>
						<aos:displayfield value="将" columnWidth="0.99" />
						<aos:textfield name="replace_source" allowBlank="true"
									   columnWidth="0.49" />
						<aos:displayfield value="替换为" columnWidth="0.99" />
						<aos:textfield name="replace_new" allowBlank="true"
									   columnWidth="0.49" />
						<aos:docked dock="bottom" ui="footer" center="true">
							<aos:dockeditem xtype="tbfill" />
							<aos:dockeditem onclick="_f_data_replace" text="确定" icon="ok.png" />
							<aos:dockeditem onclick="#_w_query_q.hide();" text="关闭"
											icon="close.png" />
						</aos:docked>
					</aos:formpanel>
				</aos:tab>
				<aos:tab title="记录更新" id="_tab_update">
					<aos:formpanel id="_f_update" layout="column" columnWidth="1">
						<aos:displayfield value="请输入您更新的字段。。。" columnWidth="0.99" />
						<aos:combobox name="filedname" labelWidth="20" columnWidth="0.49"
									  fields="['fieldenname','fieldcnname']" displayField="fieldcnname"
									  valueField="fieldenname"
									  url="queryFields.jhtml?tablename=${tablename }">
						</aos:combobox>
						<aos:displayfield value="更新为" columnWidth="0.99" />
						<aos:textfield name="update_content" allowBlank="true"
									   columnWidth="0.49" />
						<aos:docked dock="bottom" ui="footer" center="true">
							<aos:dockeditem xtype="tbfill" />
							<aos:dockeditem onclick="_f_data_update" text="确定" icon="ok.png" />
							<aos:dockeditem onclick="#_w_query_q.hide();" text="关闭"
											icon="close.png" />
						</aos:docked>
					</aos:formpanel>
				</aos:tab>
				<aos:tab title="字段前后辍" id="_tab_suffix">
					<aos:formpanel id="_f_suffix" layout="column" columnWidth="1">
						<aos:displayfield value="前辍" columnWidth="0.99" />
						<aos:textfield name="suffix_front" allowBlank="true"
									   columnWidth="0.49" />
						<aos:displayfield value="选择字段" columnWidth="0.99" />
						<aos:combobox name="filedname" labelWidth="20" columnWidth="0.49"
									  fields="['fieldenname','fieldcnname']" displayField="fieldcnname"
									  valueField="fieldenname"
									  url="queryFields.jhtml?tablename=${tablename }">
						</aos:combobox>
						<aos:displayfield value="后辍" columnWidth="0.99" />
						<aos:textfield name="suffix_after" allowBlank="true"
									   columnWidth="0.49" />
						<aos:docked dock="bottom" ui="footer" center="true">
							<aos:dockeditem xtype="tbfill" />
							<aos:dockeditem onclick="_f_data_suffix" text="确定" icon="ok.png" />
							<aos:dockeditem onclick="#_w_query_q.hide();" text="关闭"
											icon="close.png" />
						</aos:docked>
					</aos:formpanel>
				</aos:tab>

				<aos:tab title="补位" id="_tab_repair">
					<aos:formpanel id="_f_repair" layout="column" columnWidth="1">
						<aos:displayfield value="补位字段" columnWidth="0.99" />
						<aos:combobox name="filedname" labelWidth="20" columnWidth="0.49"
									  fields="['fieldenname','fieldcnname']" displayField="fieldcnname"
									  valueField="fieldenname"
									  url="queryFields.jhtml?tablename=${tablename }">
						</aos:combobox>
						<aos:displayfield value="长度" columnWidth="0.99" />
						<aos:textfield name="repair_long" allowBlank="true"
									   columnWidth="0.49" />
						<aos:hiddenfield name="repair_value" allowBlank="true"
										 columnWidth="0.49" value="00000" />
						<aos:docked dock="bottom" ui="footer" center="true">
							<aos:dockeditem xtype="tbfill" />
							<aos:dockeditem onclick="_f_data_repair" text="确定" icon="ok.png" />
							<aos:dockeditem onclick="#_w_query_q.hide();" text="关闭"
											icon="close.png" />
						</aos:docked>
					</aos:formpanel>
				</aos:tab>
			</aos:tabpanel>
		</aos:window>
		<aos:window id="_w_query_q_from" title="查询" width="720" autoScroll="true"
					layout="fit">
			<aos:tabpanel id="_tabpanel_from" region="center" activeTab="0"
						  bodyBorder="0 0 0 0" tabBarHeight="30">
				<aos:tab title="列表式搜索" id="_tab_org_from">
					<aos:formpanel id="_f_query_from" layout="column" columnWidth="1">
						<aos:hiddenfield name="cascode_id_from" value="${cascode_id_ }" />
						<aos:hiddenfield name="tablename" value="${tablename}" />
						<aos:hiddenfield name="columnnum_from" id="columnnum_from" value="7" />
						<c:forEach var="fieldss" items="${fieldDtos_z}" end="7"
								   varStatus="listSearch">
							<aos:combobox name="and${listSearch.count }" value="on"
										  labelWidth="10" columnWidth="0.2">
								<aos:option value="on" display="并且" />
								<aos:option value="up" display="或者" />
							</aos:combobox>
							<aos:combobox name="filedname${listSearch.count }"
										  emptyText="${fieldss.fieldcnname }" labelWidth="20"
										  columnWidth="0.2" fields="['fieldenname','fieldcnname']"
										  regexText="${fieldss.fieldenname}" displayField="fieldcnname"
										  valueField="fieldenname"
										  url="queryFields_from.jhtml?tablename=${tablename }">
							</aos:combobox>
							<aos:combobox name="condition${listSearch.count }" value="like"
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
							<aos:textfield name="content${listSearch.count }"
										   allowBlank="true" columnWidth="0.39" />
						</c:forEach>
						<aos:docked dock="bottom" ui="footer">
							<aos:dockeditem xtype="tbfill" />
							<aos:dockeditem onclick="_f_data_query_from" text="确定" icon="ok.png" />
							<aos:dockeditem onclick="#_w_query_q_from.hide();" text="关闭"
											icon="close.png" />
						</aos:docked>
					</aos:formpanel>
				</aos:tab>
				<aos:tab title="记录替换" id="_tab_replace_from">
					<aos:formpanel id="_f_replace_from" layout="column" columnWidth="1">
						<aos:displayfield value="请输入您替换的内容。。。" columnWidth="0.99" />
						<aos:combobox name="filedname" labelWidth="20" columnWidth="0.49"
									  fields="['fieldenname','fieldcnname']" displayField="fieldcnname"
									  valueField="fieldenname"
									  url="queryFields_from.jhtml?tablename=${tablename }">
						</aos:combobox>
						<aos:displayfield value="将" columnWidth="0.99" />
						<aos:textfield name="replace_source_from" allowBlank="true"
									   columnWidth="0.49" />
						<aos:displayfield value="替换为" columnWidth="0.99" />
						<aos:textfield name="replace_new_from" allowBlank="true"
									   columnWidth="0.49" />
						<aos:docked dock="bottom" ui="footer" center="true">
							<aos:dockeditem xtype="tbfill" />
							<aos:dockeditem onclick="_f_data_replace_from" text="确定" icon="ok.png" />
							<aos:dockeditem onclick="#_w_query_q_from.hide();" text="关闭"
											icon="close.png" />
						</aos:docked>
					</aos:formpanel>
				</aos:tab>
				<aos:tab title="记录更新" id="_tab_update_from">
					<aos:formpanel id="_f_update_from" layout="column" columnWidth="1">
						<aos:displayfield value="请输入您更新的字段。。。" columnWidth="0.99" />
						<aos:combobox name="filedname" labelWidth="20" columnWidth="0.49"
									  fields="['fieldenname','fieldcnname']" displayField="fieldcnname"
									  valueField="fieldenname"
									  url="queryFields_from.jhtml?tablename=${tablename }">
						</aos:combobox>
						<aos:displayfield value="更新为" columnWidth="0.99" />
						<aos:textfield name="update_content_from" allowBlank="true"
									   columnWidth="0.49" />
						<aos:docked dock="bottom" ui="footer" center="true">
							<aos:dockeditem xtype="tbfill" />
							<aos:dockeditem onclick="_f_data_update_from" text="确定" icon="ok.png" />
							<aos:dockeditem onclick="#_w_query_q_from.hide();" text="关闭"
											icon="close.png" />
						</aos:docked>
					</aos:formpanel>
				</aos:tab>
				<aos:tab title="字段前后辍" id="_tab_suffix_from">
					<aos:formpanel id="_f_suffix_from" layout="column" columnWidth="1">
						<aos:displayfield value="前辍" columnWidth="0.99" />
						<aos:textfield name="suffix_front_from" allowBlank="true"
									   columnWidth="0.49" />
						<aos:displayfield value="选择字段" columnWidth="0.99" />
						<aos:combobox name="filedname" labelWidth="20" columnWidth="0.49"
									  fields="['fieldenname','fieldcnname']" displayField="fieldcnname"
									  valueField="fieldenname"
									  url="queryFields_from.jhtml?tablename=${tablename }">
						</aos:combobox>
						<aos:displayfield value="后辍" columnWidth="0.99" />
						<aos:textfield name="suffix_after_from" allowBlank="true"
									   columnWidth="0.49" />
						<aos:docked dock="bottom" ui="footer" center="true">
							<aos:dockeditem xtype="tbfill" />
							<aos:dockeditem onclick="_f_data_suffix_from" text="确定" icon="ok.png" />
							<aos:dockeditem onclick="#_w_query_q_from.hide();" text="关闭"
											icon="close.png" />
						</aos:docked>
					</aos:formpanel>
				</aos:tab>

				<aos:tab title="补位" id="_tab_repair_from">
					<aos:formpanel id="_f_repair_from" layout="column" columnWidth="1">
						<aos:displayfield value="补位字段" columnWidth="0.99" />
						<aos:combobox name="filedname" labelWidth="20" columnWidth="0.49"
									  fields="['fieldenname','fieldcnname']" displayField="fieldcnname"
									  valueField="fieldenname"
									  url="queryFields_from.jhtml?tablename=${tablename }">
						</aos:combobox>
						<aos:displayfield value="长度" columnWidth="0.99" />
						<aos:textfield name="repair_long_from" allowBlank="true"
									   columnWidth="0.49" />
						<aos:hiddenfield name="repair_value_from" allowBlank="true"
										 columnWidth="0.49" value="00000" />
						<aos:docked dock="bottom" ui="footer" center="true">
							<aos:dockeditem xtype="tbfill" />
							<aos:dockeditem onclick="_f_data_repair_from" text="确定" icon="ok.png" />
							<aos:dockeditem onclick="#_w_query_q_from.hide();" text="关闭"
											icon="close.png" />
						</aos:docked>
					</aos:formpanel>
				</aos:tab>
			</aos:tabpanel>
		</aos:window>

		<aos:window id="_w_data_i" title="新增" width="1000" height="600"
					autoScroll="true"  y="100" onrender="_w_data_i_render" >
			<aos:formpanel id="_f_data_i" width="980" layout="absolute">
				<aos:hiddenfield  name="_classtree" id="_classtree" value="${cascode_id_}" />
			</aos:formpanel>
			<aos:docked dock="bottom" ui="footer">
				<aos:dockeditem xtype="tbfill" />
				<aos:dockeditem onclick="_f_data_i_save" text="加存" icon="icon80.png" />
				<aos:dockeditem onclick="_f_data_edit" text="保存" icon="icon82.png" />
				<aos:dockeditem onclick="#_w_data_i.close();" text="关闭"
								icon="close.png" />
			</aos:docked>
		</aos:window>
		<aos:window id="_w_pici" title="新增任务" onshow="_new_task">
			<aos:formpanel id="_f_pici" width="420" layout="anchor" labelWidth="90">

				<aos:textfield name="pch" fieldLabel="批次号"  />
				<aos:combobox name="sjygs" value="excel格式" fieldLabel="数据源格式"
							  labelWidth="90">
					<aos:option value="excel格式" display="excel格式" />
					<aos:option value="PDF格式" display="PDF格式" />
					<aos:option value="JPG格式" display="JPG格式" />
				</aos:combobox>
				<aos:textfield name="jsms" fieldLabel="接收描述"  />
				<aos:textfield name="bmmc" fieldLabel="部门名称"  />
				<aos:textfield name="bmbh" fieldLabel="部门编号"  />
				<aos:combobox name="boolean_examine" value="是" fieldLabel="是否需要审核"
							  labelWidth="90">
					<aos:option value="是" display="是" />
					<aos:option value="否" display="否" />
				</aos:combobox>
				<aos:textfield name="cjr" fieldLabel="创建人"  />
				<aos:datefield name="cjsj" fieldLabel="创建时间" format="Y-m-d"
							   columnWidth="1.0" />
			</aos:formpanel>
			<aos:docked dock="bottom" ui="footer">
				<aos:dockeditem xtype="tbfill" />
				<aos:dockeditem onclick="_f_pici_submit" text="保存" icon="ok.png" />
				<aos:dockeditem onclick="#_w_pici.hide();" text="关闭" icon="close.png" />
			</aos:docked>
		</aos:window>
		<aos:window id="_w_receive" title="接收提交">
			<aos:formpanel id="_f_receive" width="420" layout="anchor" labelWidth="70">
				<aos:hiddenfield name="tablename" />
				<aos:textfield name="pch" fieldLabel="批次号"  />
				<aos:textfield name="bmmc" fieldLabel="部门名称"  />
				<aos:textfield name="bmbh" fieldLabel="部门编号"  />
				<aos:textfield name="sqr" fieldLabel="申请人"  />
				<aos:datefield name="sqsj" fieldLabel="申请时间" format="Y-m-d"
							   columnWidth="1.0"  />
				<aos:textareafield name="sqms" fieldLabel="接收描述"  />
			</aos:formpanel>
			<aos:docked dock="bottom" ui="footer">
				<aos:dockeditem xtype="tbfill" />
				<aos:dockeditem onclick="_f_receive_submit" text="保存" icon="ok.png" />
				<aos:dockeditem onclick="#_w_receive.hide();" text="关闭" icon="close.png" />
			</aos:docked>
		</aos:window>
		<aos:window id="_w_receive_list" title="申请列表" autoScroll="true" height="400" width="600"  onshow="_w_receive_show">
			<aos:gridpanel id="_g_receive_list"  url="listreceive.jhtml"
						   hidePagebar="true" width="800"   autoScroll="true">
				<aos:docked>
					<aos:dockeditem xtype="tbtext" text="列表" />
				</aos:docked>
				<aos:column type="rowno" />
				<aos:column header="id_" dataIndex="id_" hidden="true" />
				<aos:column header="批次号" dataIndex="pch" width="100" />
				<aos:column header="部门名称" dataIndex="bmmc" width="80" />
				<aos:column header="部门编号" dataIndex="bmbh" width="80" />
				<aos:column header="申请人" dataIndex="sqr" width="80" />
				<aos:column header="申请时间" dataIndex="sqsj" width="80" />
				<aos:column header="申请描述" dataIndex="sqms" width="100" />
				<aos:column header="数据表" dataIndex="tablename" width="100" />
				<aos:column header="意见描述" dataIndex="opinion_description" width="100" />
				<aos:column header="" flex="1" />
			</aos:gridpanel>
			<aos:docked dock="bottom" ui="footer">
				<aos:dockeditem xtype="tbfill" />
				<aos:dockeditem onclick="#_w_receive_list.close();" text="关闭" icon="close.png" />
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
	</aos:viewport>
	<script type="text/javascript">
		_w_data_input('_f_data_i');
		function onblur_qzh(){
            var qzh = Ext.getCmp('_f_data_i_from').getForm().findField('qzh').getValue();
            var nd = Ext.getCmp('_f_data_i_from').getForm().findField('nd').getValue();
            var damldm = Ext.getCmp('_f_data_i_from').getForm().findField('damldm').getValue();
            var ajh = Ext.getCmp('_f_data_i_from').getForm().findField('ajh').getValue();
            var jh = Ext.getCmp('_f_data_i_from').getForm().findField('jh').getValue();
            Ext.getCmp('_f_data_i_from').getForm().findField('dh').setValue(qzh+'-'+damldm+'·'+nd+'-'+ajh+'-'+jh);
		}
		//生成录入界面
		function _w_data_input(formid){
			var _panel = Ext.getCmp(formid);
			_panel.removeAll();
			//_panel.reload();
			AOS.ajax({
				params: {tablename: tablename.getValue()},
				url: 'getInput.jhtml',
				ok: function (data) {
					//var _panel = Ext.getCmp(formid);
					var strdh ='';
					for (var j in data){
						//档号设置
						if(data[j].dh=='1'){
							var strfieldname = data[j].fieldname.substring(0,data[j].fieldname.length-1);
							if(typeof(data[j].dh1)!='undefined'){
								strdh = strfieldname+','+data[j].dh1;
								if(typeof(data[j].dh2)!='undefined'){
									strdh=strdh+','+data[j].dh2;
									if(typeof(data[j].dh3)!='undefined'){
										strdh=strdh+','+data[j].dh3;
										if(typeof(data[j].dh4)!='undefined'){
											strdh=strdh+','+data[j].dh4;
											if(typeof(data[j].dh5)!='undefined'){
												strdh=strdh+','+data[j].dh5;
												if(typeof(data[j].dh6)!='undefined'){
													strdh=strdh+','+data[j].dh6;
													if(typeof(data[j].dh7)!='undefined'){
														strdh=strdh+','+data[j].dh7;
													}
												}
											}
										}
									}
								}
							}
						}//判断dh
					}
					for(var i in data){
						var items;
						if(data[i].fieldname.charAt(data[i].fieldname.length - 1)=='L'){
							var labeltext=data[i].displayname;
							if(data[i].displayname!=null&&data[i].displayname.length>0){
								while(labeltext.indexOf(" ")!=-1){
									labeltext=labeltext.replace(" ","\xa0");
								}
							}
							//设置标签必录入项
							if(data[i].ynnnull=='0'){
								items=[{
									xtype : 'label',
									//value:data[i].displayname,
									text:labeltext,
									style:'color:red;',
									width : parseInt(data[i].cwidth),
									height : parseInt(data[i].cheight),
									x:parseInt(data[i].cleft)-200,
									y:parseInt(data[i].ctop)-50,
								}]
							}else{
								items=[{
									xtype : 'label',
									//value:data[i].displayname,
									text:labeltext,
									width : parseInt(data[i].cwidth),
									height : parseInt(data[i].cheight),
									x:parseInt(data[i].cleft)-200,
									y:parseInt(data[i].ctop)-50,
								}]
							}
						}else{
							if(data[i].yndic=='1'){
								var ynnull;
								if(data[i].ynnnull==0){
									ynnull=false;
								}else{
									ynnull=true;
								}
								var strdicname=data[i].fieldname.substring(0,data[i].fieldname.length-1);
								items=[{
									name:data[i].fieldname.substring(0,data[i].fieldname.length-1),
									id:data[i].fieldname.substring(0,data[i].fieldname.length-1),
									//fieldLabel: 'ieldLabel',
									xtype: "combo",
									mode:'local',
									//fieldLabel:'数据字典',
									x:parseInt(data[i].cleft)-200,
									y:parseInt(data[i].ctop)-50,
									maxWidth:parseInt(data[i].cwidth),
									height:parseInt(data[i].cheight),
									allowBlank:ynnull,
									listeners:{
										select:function(e){
											if(strdh.indexOf(this.name)>-1){

												var strarray=strdh.split(',');
												//alert(strarray[0]);
												var strtemp='';
												//alert(strdh);
												for(var i=1;i<strarray.length; i++){
													//alert('11');
													//alert(strarray[i]);
													if(i==1){
														strtemp =Ext.getCmp(strarray[i]).getValue();
														continue;
													}

													//strtemp=strtemp+'+"-"+Ext.getCmp("'+strarray[i]+'").getValue()';
													strtemp = strtemp+'-'+Ext.getCmp(strarray[i]).getValue();
													//alert(strtemp);
												}
												//alert(strtemp);
												//alert(eval('(' + str + ')'));
												//Ext.getCmp(strarray[0]).setValue(eval('(' + strtemp + ')'));
												Ext.getCmp(strarray[0]).setValue(strtemp);
											}
										}
									},
									//labelWidth:80,
									store: new Ext.data.SimpleStore({
										fields: ["code_", "desc_"],
										proxy: {
											type: "ajax",
											//params:{"tablename":"3333"},
											url: "load_dic_index.jhtml?key_name_="+data[i].dic,
											actionMethods: {
												read: "POST"  //解决传中文参数乱码问题，默认为“GET”提交
											},
											reader: {
												type: "json",  //返回数据类型为json格式
												root: "root"  //数据
											}
										},
										autoLoad: false  //自动加载数据
									}),
									emptyText:'请选择...',
									displayField: 'desc_',
									valueField :'desc_',
									//hiddenName: 'fieldenname',
								}]
							}
							else{
								items = itemsGroup(data[i],strdh);
							}
						}
						_panel.add(items);
					}
				}
			});
		}

		function itemsGroup(data,strdh){
			var strx=parseInt(data.cleft)-200;
			var stry = parseInt(data.ctop)-50;
			var strwidth = parseInt(data.cwidth);
			var strheight=parseInt(data.cheight);
			var fieldname = data.fieldname.substring(0,data.fieldname.length-1);
			var ynnull;
			if(data.ynnnull==0){
				ynnull=false;
			}else{
				ynnull=true;
			}
			//var ynnull=data.ynnnull=='0';
			var str =[{
				xtype :'textfield',
				id:fieldname,
				name:fieldname,
				maxWidth :strwidth,
				height :strheight,
				x:strx,
				y:stry,
				maxLength:data.edtmax,
				allowBlank:ynnull,
				listeners:{focus:function(e){},
					blur:function(e){
						if(data.ynpw=='1'){
							var val=e.getValue();
							var len=val.length;
							while(len < data.edtmax) {
								val= "0" + val;
								len++;
							}
							e.setValue(val);
						}
						if(strdh.indexOf(this.name)>-1){
							var strarray=strdh.split(',');
							var strtemp='';
							//alert(strdh);
							for(var i=1;i<strarray.length; i++){
								//alert(strarray[i]);
								if(i==1){
									strtemp =Ext.getCmp(strarray[i]).getValue();
									continue;
								}
								strtemp=strtemp+'-'+Ext.getCmp(strarray[i]).getValue();

							}
							//alert(strtemp);
							Ext.getCmp(strarray[0]).setValue(strtemp);

						}
					}
					//离开鼠标事件结尾
				}
			}];
			//alert(str);
			//var item = eval('(' + str + ')');
			return str;
		}
        //加载数据,（批次号列表信息）
        function _g_receive_query() {
			//得到隐藏域的值赋给下拉框
			Ext.getCmp("listTablename").setValue("<%=request.getAttribute("listtablename")%>");
			Ext.getCmp("listTablename").setRawValue("<%=request.getAttribute("tablenamedesc")%>");
			var params = {
				tablename:listtablename.getValue()
			};
			//这个Store的命名规则为：表格ID+"_store"。
			_g_receive_store.getProxy().extraParams = params;
            _g_receive_store.load();
        }
        //加载表格数据
        function _g_data_query() {
            var params = {
                tablename : listtablename.getValue(),
                cascode_id_:cascode_id_.getValue()
            };
            //这个Store的命名规则为：表格ID+"_store"。
            _g_data_store.getProxy().extraParams = params;
            _g_data_store.load();
        }
        //父表点击，字表加载
        function get_a_data(){
            var record = AOS.selectone(_g_data);
            var params = {
                tablename : tablename.getValue(),
				ajh:record.data.ajh,
				jgwt:"<%=session.getAttribute("jgwt")%>",
                cascode_id_:cascode_id_.getValue()
            };
            //这个Store的命名规则为：表格ID+"_store"。
            _g_data_from_store.getProxy().extraParams = params;
            _g_data_from_store.load();
		}
		function get_a_receive(){
			var record = AOS.selectone(_g_receive);
			var params = {
				tablename : tablename.getValue(),
				pch:record.data.pch
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
        //生成录入界面
        function _w_data_input_from(formid){
            var _panel = Ext.getCmp(formid);
            _panel.removeAll();
            //_panel.reload();
            AOS.ajax({
                params: {tablename: tablename.getValue()+"_z"},
                url: 'getInput.jhtml',
                ok: function (data) {
                    //var _panel = Ext.getCmp(formid);
                    var strdh ='';
                    for (var j in data){
                        if(data[j].dh=='1'){
                            var strfieldname = data[j].fieldname.substring(0,data[j].fieldname.length-1);
                            if(typeof(data[j].dh1)!='undefined'){
                                strdh = strfieldname+','+data[j].dh1;
                                if(typeof(data[j].dh2)!='undefined'){
                                    strdh=strdh+','+data[j].dh2;
                                    if(typeof(data[j].dh3)!='undefined'){
                                        strdh=strdh+','+data[j].dh3;
                                        if(typeof(data[j].dh4)!='undefined'){
                                            strdh=strdh+','+data[j].dh4;
                                            if(typeof(data[j].dh5)!='undefined'){
                                                strdh=strdh+','+data[j].dh5;
                                                if(typeof(data[j].dh6)!='undefined'){
                                                    strdh=strdh+','+data[j].dh6;
                                                    if(typeof(data[j].dh7)!='undefined'){
                                                        strdh=strdh+','+data[j].dh7;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }//判断dh
                    }
                    for(var i in data){
                        var items;
                        if(data[i].fieldname.charAt(data[i].fieldname.length - 1)=='L'){
                            items=[{
                                xtype : 'label',
                                //value:data[i].displayname,
                                text:data[i].displayname,
                                width : parseInt(data[i].cwidth),
                                height : parseInt(data[i].cheight),
                                x:parseInt(data[i].cleft)-200,
                                y:parseInt(data[i].ctop)-50,
                            }]
                        }else{
                            if(data[i].yndic=='1'){
                                var strdicname=data[i].fieldname.substring(0,data[i].fieldname.length-1);
                                items=[{
                                    name:data[i].fieldname.substring(0,data[i].fieldname.length-1),
                                    id:data[i].fieldname.substring(0,data[i].fieldname.length-1),
                                    //fieldLabel: 'ieldLabel',
                                    xtype: "combo",
                                    mode:'local',
                                    //fieldLabel:'数据字典',
                                    x:parseInt(data[i].cleft)-200,
                                    y:parseInt(data[i].ctop)-50,
                                    maxWidth:parseInt(data[i].cwidth),
                                    height:parseInt(data[i].cheight),
                                    listeners:{
                                        select:function(e){
                                            if(strdh.indexOf(strdicname)>-1){

                                                var strarray=strdh.split(',');
                                                //alert(strarray[0]);
                                                var strtemp='';
                                                //alert(strdh);
                                                for(var i=1;i<strarray.length; i++){
                                                    //alert('11');
                                                    //alert(strarray[i]);
                                                    if(i==1){
                                                        strtemp ='Ext.getCmp("'+strarray[i]+'").getValue()';
                                                        continue;
                                                    }

                                                    strtemp=strtemp+'+"-"+Ext.getCmp("'+strarray[i]+'").getValue()';

                                                }
                                                //alert(strtemp);
                                                //alert(eval('(' + str + ')'));
                                                Ext.getCmp(strarray[0]).setValue(eval('(' + strtemp + ')'));
                                            }
                                        }
                                    },
                                    //labelWidth:80,
                                    store: new Ext.data.SimpleStore({
                                        fields: ["code_", "desc_"],
                                        proxy: {
                                            type: "ajax",
                                            //params:{"tablename":"3333"},
                                            url: "load_dic_index.jhtml?key_name_="+data[i].dic,
                                            actionMethods: {
                                                read: "POST"  //解决传中文参数乱码问题，默认为“GET”提交
                                            },
                                            reader: {
                                                type: "json",  //返回数据类型为json格式
                                                root: "root"  //数据
                                            }
                                        },
                                        autoLoad: false  //自动加载数据
                                    }),
                                    emptyText:'请选择...',
                                    displayField: 'desc_',
                                    valueField :'desc_',
                                    //hiddenName: 'fieldenname',
                                }]
                            }
                            else{
                                items = itemsGroup(data[i],strdh);
                            }
                        }
                        _panel.add(items);
                    }
                }
            });
        }
		//查询窗口展开
		function _w_query_show() {
		//判断是不是
		_w_query_q.show();
		}
		function _w_query_from_show() {
		//判断是不是
		_w_query_q_from.show();
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
			 params["jgwt"]="<%=session.getAttribute("jgwt")%>";
			_g_data_store.getProxy().extraParams = params;
			_g_data_store.load();
             var params_z = {
                 tablename : tablename.getValue(),
                 _fid:"",
				 jgwt:"<%=session.getAttribute("jgwt")%>"
             };
             //这个Store的命名规则为：表格ID+"_store"。
             _g_data_from_store.getProxy().extraParams = params_z;
             _g_data_from_store.load();
			_w_query_q.hide();
			AOS.reset(_f_query);
	}
        //记录替换
        function _f_data_replace(){
            AOS.ajax({
                forms : _f_replace,
                url : 'replaceRecord.jhtml',
                params:{
                    tablename : tablename.getValue(),
					jgwt:"<%=session.getAttribute("jgwt")%>"
                },
                ok : function(data) {
                    _w_query_q.hide();
                    _g_data_store.reload();
                    var params_z = {
                        tablename : tablename.getValue(),
						jgwt:"<%=session.getAttribute("jgwt")%>",
                        _fid:""
                    };
                    //这个Store的命名规则为：表格ID+"_store"。
                    _g_data_from_store.getProxy().extraParams = params_z;
                    _g_data_from_store.load();
                    AOS.tip(data.appmsg);
                }
            });
        }
        //记录更新
        function _f_data_update(){
            AOS.ajax({
                forms : _f_update,
                url : 'updateRecord.jhtml',
                params:{
					jgwt:"<%=session.getAttribute("jgwt")%>",
                    tablename : tablename.getValue()
                },
                ok : function(data) {
                    _w_query_q.hide();
                    _g_data_store.reload();
                    var params_z = {
                        tablename : tablename.getValue(),
						jgwt:"<%=session.getAttribute("jgwt")%>",
                        _fid:""
                    };
                    //这个Store的命名规则为：表格ID+"_store"。
                    _g_data_from_store.getProxy().extraParams = params_z;
                    _g_data_from_store.load();
                    AOS.tip(data.appmsg);
                }
            });
        }
        //记录更新
        function _f_data_update_from(){
			var record = AOS.selectone(_g_data);
            AOS.ajax({
                forms : _f_update_from,
                url : 'updateRecord_from.jhtml',
                params:{
                    tablename : tablename.getValue(),
					jgwt:"<%=session.getAttribute("jgwt")%>",
					ajh:record.data.ajh
                },
                ok : function(data) {
                    _w_query_q_from.hide();
                    _g_data_from_store.reload();
                    var params_z = {
                        tablename : tablename.getValue(),
						jgwt:"<%=session.getAttribute("jgwt")%>",
                        ajh:record.data.ajh
                    };
                    //这个Store的命名规则为：表格ID+"_store"。
                    _g_data_from_store.getProxy().extraParams = params_z;
                    _g_data_from_store.load();
                    AOS.tip(data.appmsg);
                }
            });
        }
        //前后辍
        function _f_data_suffix(){
            AOS.ajax({
                forms : _f_suffix,
                url : 'suffixRecord.jhtml',
                params:{
					jgwt:"<%=session.getAttribute("jgwt")%>",
                    tablename : tablename.getValue()
                },
                ok : function(data) {
                    _w_query_q.hide();
                    _g_data_store.reload();
                    var params_z = {
                        tablename : tablename.getValue(),
						jgwt:"<%=session.getAttribute("jgwt")%>",
                        _fid:""
                    };
                    //这个Store的命名规则为：表格ID+"_store"。
                    _g_data_from_store.getProxy().extraParams = params_z;
                    _g_data_from_store.load();
                    AOS.tip(data.appmsg);
                }
            });
        }
        //部位
        function _f_data_repair(){
            AOS.ajax({
                forms : _f_repair,
                url : 'repairRecord.jhtml',
                params:{
					jgwt:"<%=session.getAttribute("jgwt")%>",
                    tablename : tablename.getValue()
                },
                ok : function(data) {
                    _w_query_q.hide();
                    _g_data_store.reload();
                    var params_z = {
						jgwt:"<%=session.getAttribute("jgwt")%>",
                        tablename : tablename.getValue(),
                        _fid:""
                    };
                    //这个Store的命名规则为：表格ID+"_store"。
                    _g_data_from_store.getProxy().extraParams = params_z;
                    _g_data_from_store.load();
                    AOS.tip(data.appmsg);
                }
            });
        }
        //列表时搜索
        function _f_data_query_from(){
            var params = AOS.getValue('_f_query_from');
            var form = Ext.getCmp('_f_query_from');
            var tmp = columnnum_from.getValue();
            for(var i=1;i<=tmp;i++){
                var str = form.down("[name='filedname"+i+"']");
                var filedname =str.getValue();
                if(filedname==null){
                    params["filedname"+i]=str.regexText;
                }

            }
			params["jgwt"]="<%=session.getAttribute("jgwt")%>";
            var record = AOS.selectone(_g_data);
            //把父亲的id传过去
			params["ajh"]=record.data.ajh;
            _g_data_from_store.getProxy().extraParams = params;
            _g_data_from_store.load();
            _w_query_q_from.hide();
            AOS.reset(_f_query_from);
        }
        //记录替换
        function _f_data_replace_from(){
			var record = AOS.selectone(_g_data);

            AOS.ajax({
                forms : _f_replace_from,
                url : 'replaceRecord_from.jhtml',
                params:{
                    tablename : tablename.getValue(),
					ajh:record.data.ajh,
					jgwt:"<%=session.getAttribute("jgwt")%>"
                },
                ok : function(data) {
                    _w_query_q_from.hide();
                    _g_data_from_store.reload();
                    AOS.tip(data.appmsg);
                }
            });
        }
        //前后辍
        function _f_data_suffix_from(){
			var record = AOS.selectone(_g_data);
            AOS.ajax({
                forms : _f_suffix_from,
                url : 'suffixRecord_from.jhtml',
                params:{
                    tablename : tablename.getValue(),
					ajh:record.data.ajh,
					jgwt:"<%=session.getAttribute("jgwt")%>"
                },
                ok : function(data) {
                    _w_query_q_from.hide();
                    _g_data_from_store.reload();
                    AOS.tip(data.appmsg);
                }
            });
        }
        //部位
        function _f_data_repair_from(){
			var record = AOS.selectone(_g_data);
            AOS.ajax({
                forms : _f_repair_from,
                url : 'repairRecord_from.jhtml',
                params:{
					tablename : tablename.getValue(),
					ajh:record.data.ajh,
					jgwt:"<%=session.getAttribute("jgwt")%>"
                },
                ok : function(data) {
                    _w_query_q_from.hide();
                    _g_data_from_store.reload();

                    AOS.tip(data.appmsg);
                }
            });
        }
        //弹出新增用户窗口
        function _w_data_show() {
            //展开之前进行重置
			var selection = AOS.selection(_g_receive, 'id_');
			if (AOS.empty(selection)) {
				AOS.tip('新增数据前请先选中对应任务!');
				return;
			}
            AOS.reset(_f_data_i);
            _w_data_i.show();
        }
        //弹出新增用户窗口
        function _w_data_from_show() {
            //展开之前进行重置
            //AOS.reset(_f_data_i_from);
            _w_data_i_from.show();
        }
        function _w_data_i_from_onshow(){
            if(typeof(AOS.selectone(_g_data_from))!='undefined'){
                AOS.ajax({
                    params: {id_: AOS.selectone(_g_data_from).data.id_,
                        tablename:tablename.getValue()+"_z"
                    },
                    url: 'getData_from.jhtml',
                    ok: function (data) {
                        _f_data_i_from.form.setValues(data);
                    }
                });
                //此时把案卷号的值传过去
            }
			var record = AOS.selectone(_g_data);
			var ajh="";
			if(!AOS.empty(record)){
				ajh=record.data.ajh;
			}
			//
			_f_data_i_from.form.findField("ajh_z").setValue(ajh);
        }
        function _w_data_i_render(){
            //_w_data_input('_f_data_i');
        }
        //
        function _w_data_i_from_render(){
            _w_data_input_from('_f_data_i_from');
        }
		//卡片新增目录加存
		function _f_data_i_save(){
			AOS.ajax({
				forms : _f_data_i,
				url : 'saveData.jhtml',
				params:{
					tablename : tablename.getValue(),
					_classtree:_classtree.getValue()
				},
				ok : function(data) {
					if (data.appcode === -1) {
						AOS.err(data.appmsg);
					} else {
						//_w_data_i.hide();
						//_w_data_input('_f_data_u');
						_g_data_store.reload();
						if(data.xdfields!=1){
							var xdarray=data.xdfields.split(",");
							for(var i in xdarray){
								Ext.getCmp(xdarray[i]).setValue('');
							}
						}
						//取消窗口关闭功能
						//去除data数据进行补位操作
						for(var i in data){
							Ext.getCmp(i).setValue(data[i]);
						}
						AOS.tip(data.appmsg);
						//_w_data_i.hide();
					}
				}
			});
		}
        //卡片新增目录加存
        function _f_data_i_save_from(){
            var record = AOS.selectone(_g_data);
            AOS.ajax({
                forms : _f_data_i_from,
                url : 'saveData_from.jhtml',
                params:{
                    tablename : tablename.getValue()+"_z",
					_pid:record.data.id_,
					jgwt:"<%=session.getAttribute("jgwt")%>",
                    _classtree:_classtree_from.getValue()
                },
                ok : function(data) {
                    if (data.appcode === -1) {
                        AOS.err(data.appmsg);
                    } else {
                        //_w_data_i.hide();
                        //_w_data_input('_f_data_u');
                        _g_data_from_store.reload();
                        AOS.tip(data.appmsg);
                    }
                }
            });
        }
        //修改目录保存
        function _f_data_edit(){
            var record = AOS.selectone(_g_data);
            AOS.ajax({
                forms : _f_data_i,
                url : 'updateData.jhtml',
                params:{
                    tablename : tablename.getValue(),
                    id:record.data.id_
                },
                ok : function(data) {
                    if (data.appcode === -1) {
                        AOS.err(data.appmsg);
                    } else {
                        //_w_data_i.hide();
                        _g_data_store.reload();
						_w_data_i.hide();
                        AOS.tip(data.appmsg);
                    }
                }
            });
        }
        //修改目录保存
        function _f_data_edit_from(){
            var record_from = AOS.selectone(_g_data_from);
            var record = AOS.selectone(_g_data);
            AOS.ajax({
                forms : _f_data_i_from,
                url : 'updateData_from.jhtml',
                params:{
                    tablename : tablename.getValue()+"_z",
                    id:record_from.data.id_,
					jgwt:"<%=session.getAttribute("jgwt")%>",
					ajh:record.data.ajh,
					_pid:record.data.id_
                },
                ok : function(data) {
                    if (data.appcode === -1) {
                        AOS.err(data.appmsg);
                    } else {
                        //_w_data_i.hide();
                        _g_data_from_store.reload();
                        AOS.tip(data.appmsg);
                    }
                }
            });
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
        function _w_picture_show(){
            var record = AOS.selectone(_g_data_from);
            var uploadPanel= new Ext.ux.uploadPanel.UploadPanel({
                addFileBtnText : '选择文件...',
                uploadBtnText : '上传',
                deleteBtnText : '移除',
                downBtnText   : '下载',
                removeBtnText : '移除所有',
                cancelBtnText : '取消上传',
                use_query_string : true,
                listeners: {
					//双击
					itemdblclick: function (grid, row) {
						//parent.fnaddtab(row.data.id, '电子文件',
						//					'archive/data/openFile.jhtml?id='+row.data.pid+'&tid='+row.data.tid+'&type='+row.data.type+'&tablename='+tablename.getValue());
						//此时根据当前用户走后台判断是否可以看电子文件
						//得到当前用户,判断是不是档案室和admin用户
						var user = "<%=session.getAttribute("user")%>";
						var selection = AOS.selection(_g_data_from, 'id_');
						var boolean;
						//判断当前用户是不是兼职管理员或者普通用户，并且在判断门类是不是文书档案，在判断是哪个部门的，匹配了就可以看电子文件。不然得借阅电子文件
						AOS.ajax({
							url: 'getdepartment.jhtml',
							params: {user: user, tablename: tablename.getValue()+"_z", ids_: selection},
							method: 'post',
							ok: function (data) {
								if (data.appcode == 1) {
									//此时直接看电子文件了
									parent.fnaddtab(row.data.id, '电子文件',
											'archive/data/openSwfFile.jhtml?id=' + row.data.pid + '&tid=' + row.data.tid + '&type=' + row.data.type + '&tablename=' + tablename.getValue() + "_z");
								} else {
									AOS.ajax({
										url: 'booleanBorrowFile.jhtml',
										params: {
											id_: row.data.tid,
											jytype: "浏览"
										},
										method: 'post',
										ok: function (data) {
											if (data.appcode === -1) {
												//让用户重新选择
												AOS.tip("对不起!您没有权限看电子文件,请借阅电子文件!");
												return;
											} else {
												parent.fnaddtab(row.data.id, '电子文件',
														'archive/data/openSwfFile.jhtml?id=' + row.data.pid + '&tid=' + row.data.tid + '&type=' + row.data.type + '&tablename=' + tablename.getValue() + "_z");
											}
										}
									});
								}
							}
						});
					},
				},
                onUpload : function(){
                    var me=Ext.getCmp("uploadpanel");
                    this.swfupload.addPostParam("ocr",Ext.getCmp("ocr").getValue());
                    this.swfupload.addPostParam("mark",Ext.getCmp("mark").getValue());
                    if (this.swfupload&&this.store.getCount()>0) {
                        if (this.swfupload.getStats().files_queued > 0) {
                            this.showBtn(this,false);
                            this.swfupload.uploadStopped = false;
                            this.swfupload.startUpload();
                        }
                    }
                    // this.swfupload.destroy();

                },
                deletePath:	function(grid, rowIndex, colIndex) {

					var msg = AOS.merge('确认要删除电子文件吗？');
					AOS.confirm(msg, function (btn) {
						if (btn === 'cancel') {
							AOS.tip('删除操作被取消。');
							return;
						} else {
							var me = Ext.getCmp("uploadpanel").getSelectionModel().getSelection();
							var id = me[0].get('pid');
							var tid = me[0].get('tid');

							var rowIndex = Ext.getCmp("uploadpanel").getStore().indexOf(me[0]);
							AOS.ajax({
								params: {
									id_: id,
									tablename: tablename.getValue() + "_z",
									tid: tid,
									tm: record.data.tm
								}, // 提交参数,
								url: 'deletePath.jhtml',
								ok: function (data) {
									var me = Ext.getCmp("uploadpanel");
									//me.store.reload();
									me.store.remove(me.store.getAt(rowIndex));
									me.store.load();
									AOS.tip(data.appmsg);
								}
							});
						}
					});
                },
                onRemoveAll: function (){
					var msg = AOS.merge('确认要删除电子文件吗？');
					AOS.confirm(msg, function (btn) {
						if (btn === 'cancel') {
							AOS.tip('删除操作被取消。');
							return;
						} else {
							var selection = AOS.selection(_g_data_from, 'id_');
							AOS.ajax({
								params: {
									aos_rows_: selection,
									tm: record.data.tm,
									tablename: tablename.getValue() + "_z"
								},
								url: 'deletePathAll.jhtml',
								ok: function (data) {
									var me = Ext.getCmp("uploadpanel");
									me.removeAll();
									AOS.tip(data.appmsg);
								}
							});
						}
					});
                },
                //下载
                onDownPath:function(grid, rowIndex, colIndex){
					var me=Ext.getCmp("uploadpanel").getSelectionModel().getSelection();
					var id = me[0].get('pid');
					var tid = me[0].get('tid');
					var user="<%=session.getAttribute("user")%>";
					var selection = AOS.selection(_g_data_from, 'id_');
					var boolean;
					//判断当前用户是不是兼职管理员或者普通用户，并且在判断门类是不是文书档案，在判断是哪个部门的，匹配了就可以看电子文件。不然得借阅电子文件
					AOS.ajax({
						url: 'getdepartment.jhtml',
						params:{user:user,tablename:tablename.getValue()+"_z",ids_:selection},
						method:'post',
						ok: function (data) {
							if(data.appcode==1){
								//此时直接下载电子文件了
								AOS.file('downloadPath.jhtml?id_='+id+'&tablename='+tablename.getValue()+"_z");
							}else{
								AOS.ajax({
									url: 'booleanBorrowFile.jhtml',
									params:{id_:tid,
										jytype:"下载"},
									method:'post',
									ok: function (data) {
										if(data.appcode === -1){
											//让用户重新选择
											AOS.tip("对不起!您没有权限看电子文件,请借阅电子文件!");
											return;
										}else{
											var me=Ext.getCmp("uploadpanel").getSelectionModel().getSelection();
											var id = me[0].get('pid');
											AOS.file('downloadPath.jhtml?id_='+id+'&tablename='+tablename.getValue()+"_z");
										}
									}
								});
							}
						}
					});
                    //得到选中的条目

                },

                upload_complete_handler : function(file){

                    var me =Ext.getCmp("uploadpanel");

                    AOS.ajax({
                        params: {tid: record.data.id_,tablename:tablename.getValue()+"_z"},
                        url: 'getPath.jhtml',
                        ok: function (data) {
                            for(i in data){
                                me.store.getAt(file.index).set({"pid":data[i].id_,"tid":data[i].tid});
                            }
                        }
                    });
                },

                post_params:{tid:record.data.id_,
                    tablename:tablename.getValue()
                },
                file_size_limit : 10000,//MB
                flash_url : "${cxt}/static/swfupload/swfupload.swf",
                flash9_url : "${cxt}/static/swfupload/swfupload_f9.swf",
                upload_url : "${cxt}/archive/upload/archiveUpload.jhtml?tm="+record.data.tm+"&tablename="+tablename.getValue()+"_z"

            });

            var w_data_path = new Ext.Window({
                title : '电子文件',
                width : 700,
                modal:true,
                closeAction : 'destroy',
                items:[uploadPanel]
            });
            w_data_path.on("show",w_data_path_onshow);
            w_data_path.on("close",w_data_path_onclose);
            w_data_path.show();
        }

        function w_data_path_onshow() {
            //var me = this.settings.custom_settings.scope_handler;

            var record = AOS.selectone(Ext.getCmp('_g_data_from'));
            var me=Ext.getCmp("uploadpanel");
            me.store.removeAll();
            AOS.ajax({
                params: {tid: record.data.id_,tablename:tablename.getValue()+"_z"},
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
            _g_data_from_store.load();

        }
        //设置录入窗口
        function _w_input_show() {
            window.parent.fnaddtab('','设置录入','/dbtable/input/initInput.jhtml?tablename='+tablename.getValue());

        }
        //设置录入窗口
        function _w_input_from_show() {
            window.parent.fnaddtab('','设置录入','/dbtable/input/initInput.jhtml?tablename='+tablename.getValue()+"_z");

        }
        function _w_config_show(){

            _w_config.show();
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
        //生成XLS报表
        function fn_xls() {
			var query="<%=session.getAttribute("querySession")%>";
			var jgwt="<%=session.getAttribute("jgwt")%>";
			AOS.ajax({
				url : 'fillReport.jhtml',
				params:{
					jgwt:jgwt,
					query:query,
					tablename : tablename.getValue()
				},
                ok : function(data) {
                    AOS.file('${cxt}/report/xls.jhtml');
                }
            });
        }
        //生成XLS报表
        function fn_xls_from() {
            AOS.ajax({
                url : 'fillReport.jhtml',
                params:{
                    tablename : tablename.getValue()+"_z"
                },
                ok : function(data) {
                    AOS.file('${cxt}/report/xls.jhtml');
                }
		});
        }

        //导入窗口
        function _w_import_show() {
			var record = AOS.selectone(_g_receive);
			var _classtree=Ext.getCmp("_classtree").getValue();
			var pch="";
			if(!AOS.empty(record)){
				pch=record.data.pch;
				window.parent.fnaddtab('417','数据导入','archive/data/initImport_temporary.jhtml?tablename='+tablename.getValue()+'&pch='+pch+'&_classtree='+_classtree);
			}else{
				AOS.tip("请选择要导入数据的任务!");
			}

            //window.parent.fnaddtab('','数据导入','/archive/data_relevance/initImport.jhtml?tablename='+tablename.getValue()+'&pch='+pch);

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
        //新增批次
        function _g_add_pici(){
			AOS.reset(_f_pici);
			_w_pici.show();
		}
		//接收提交
		function _g_receive_pici(){
			var selection = AOS.selection(_g_receive, 'id_');
			if (AOS.empty(selection)) {
				AOS.tip('提交前请先选中数据。');
				return;
			}
			var record = AOS.selectone(_g_receive);
			var id_="";
			var flag_manage_submit="";
			if(!AOS.empty(record)){
				id_=record.data.id_;
				flag_manage_submit=record.data.flag_manage_submit;
			}
			if(flag_manage_submit=="已提交"){
				AOS.tip('已提交无须重复提交!');
				return;
			}

			var msg = AOS.merge('确认要提交选中的[{0}]个任务数据吗？', AOS.rows(_g_receive));
			AOS.confirm(msg, function (btn) {
				if (btn === 'cancel') {
					AOS.tip('提交操作被取消。');
					return;
				}
				AOS.ajax({
					params:{'id_':id_ },
					url : 'updatemanage.jhtml',
					ok : function(data) {
						AOS.tip(data.appmsg);
						_g_receive_store.reload();
					}
				});
			});
		}
		//接收进行提交操作
		function _f_receive_submit(){
			AOS.ajax({
				forms : _f_receive,
				url : 'savereceive.jhtml',
				ok : function(data) {
					_w_receive.hide();
					AOS.tip(data.appmsg);

				}
			});
		}
		//接收申请列表
		function _g_receive_grid(){
			_w_receive_list.show();
		}
		function _w_receive_show(){
			_g_receive_list_store.reload();
		}
		function _w_leader_examine(){
			var selection = AOS.selection(_g_receive, 'id_');
			if (AOS.empty(selection)) {
				AOS.tip('提交前请先选中数据!');
				return;
			}
			var record = AOS.selectone(_g_receive);
			var flag_manage_submit="";
			var flag_manage_examine="";
			if(!AOS.empty(record)){
				flag_manage_submit=record.data.flag_manage_submit;
				flag_manage_examine=record.data.flag_manage_examine;
			}
			if(flag_manage_submit!="已提交"){
				AOS.tip('未提交领导不能审核!');
				return;
			}
			if(flag_manage_examine=="已通过"){
				AOS.tip('已通过无须重复审核!');
				return;
			}
			_w_next_kf_message.show();
		}
		function _save_next_kf_message(){
			var record = AOS.selectone(_g_receive);
			var id_="";
			if(!AOS.empty(record)){
				id_=record.data.id_;
			}
			AOS.ajax({
				forms : _f_next_kf_message,
				params:{'id_':id_},
				url : 'updatemanageyes.jhtml',
				ok : function(data) {
					_w_next_kf_message.hide();
					AOS.tip(data.appmsg);
					_g_receive_store.reload();

				}
			});
		}
		function no_save_next_kf_message(){
			var record = AOS.selectone(_g_receive);
			var id_="";
			if(!AOS.empty(record)){
				id_=record.data.id_;
			}
			AOS.ajax({
				forms : _f_next_kf_message,
				params:{'id_':id_},
				url : 'updatemanageno.jhtml',
				ok : function(data) {
					_w_next_kf_message.hide();
					AOS.tip(data.appmsg);
					_g_receive_store.reload();
				}
			});
		}
		//正式接收
		function _w_formal_receive(){
			var selection = AOS.selection(_g_receive, 'id_');
			if (AOS.empty(selection)) {
				AOS.tip('提交前请先选中数据!');
				return;
			}
			var record = AOS.selectone(_g_receive);
			var flag_manage_submit="";
			var flag_manage_examine="";
			var boolean_examine="";
			if(!AOS.empty(record)){
				flag_manage_submit=record.data.flag_manage_submit;
				flag_manage_examine=record.data.flag_manage_examine;
			}
				var selection = AOS.selection(_g_receive, 'id_');
				if (AOS.empty(selection)) {
					AOS.tip('进库前请先选中任务!');
					return;
				}
				if(flag_manage_submit!="已提交"&&flag_manage_examine!="已通过"){
					AOS.tip('未提交或提交后未通过不能正式进库!');
					return;
				}
				var id_="";
				var tablename=Ext.getCmp("tablename").getValue();
				if(!AOS.empty(record)){
					id_=record.data.id_;
				}
				AOS.ajax({
					params:{id_:id_,tablename:tablename},
					url : 'receiveTomanageData.jhtml',
					ok : function(data) {
						AOS.tip(data.appmsg);
						_g_receive_store.reload();
						_g_data_store.reload();
					}
				});

		}
		function _new_task(){
			//根据tree名称得到对应的部门名称
			var treename=Ext.getCmp("treename").getValue();
			AOS.ajax({
				params:{treename:treename},
				url:'calcId.jhtml',
				ok:function(data){
					//设计一个随机数编号
					//年月日
					var time = (new Date).getTime();
					var yesday = new Date(time); // 获取的是前一天日期
					yesday = yesday.getFullYear()  +""+(yesday.getMonth()> 9 ? (yesday.getMonth() + 1) : "0" + (yesday.getMonth() + 1))+""+(yesday.getDate()> 9 ? (yesday.getDate()) : "0" + (yesday.getDate()));
					_f_pici.form.findField("pch").setValue(data.dictionary+yesday+data.index);
				}
			});
		}

		function _f_pici_submit(){
			AOS.ajax({
				url: 'addpici.jhtml',
                params:{treename:treename.getValue()},
				forms:_f_pici,
				ok: function (data) {
					if(data.appcode===1){
						AOS.tip("添加成功!");
						_w_pici.hide();
						_g_receive_query();
					}else if(data.appcode===-1){
						AOS.tip("添加失败!");
					}
				}
			});
		}
		function _w_tablename_init(){
			// alert(listtablename);
			var listTablename = Ext.getCmp("listTablename").value;
			var tablenamedesc = Ext.getCmp("listTablename").getRawValue();
			window.location.href="initInput.jhtml?listtablename="+listTablename+"&tablenamedesc="+encodeURI(encodeURI(tablenamedesc));
		}
	</script>
</aos:onready>
</aos:html>

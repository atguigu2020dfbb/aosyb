﻿<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="/WEB-INF/tld/aos.tld" prefix="aos"%>
<%@ taglib uri="/WEB-INF/tld/cll.tld" prefix="c"%>
<aos:html>
	<aos:head title="档案征集">
		<aos:include lib="ext,swfupload" />
		<aos:include css="${cxt}/static/weblib/bootstrap/css/bootstrap.min.css"/>
		<aos:include css="${cxt}/static/css/fileinput.min.css"/>
		<aos:include js="${cxt}/static/js/jquery-3.2.1.min.js"/>
		<aos:include js="${cxt}/static/weblib/bootstrap/js/bootstrap.min.js"/>
		<aos:include js="${cxt}/static/js/fileinput.min.js"/>
		<aos:include js="${cxt}/static/js/zh.js"/>
		<aos:base href="archive/datatemporary" />
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
		<div id="filediv">
			<input id="file" class="file-loading" name="file" type="file" multiple>
		</div>
		<div id="_div_yijiaoshu" class="x-hidden" align="center">
			<img id="_img_yijiaoshu" class="app_cursor_pointer"  width="680"
				 height="500">
		</div>
	</aos:body>
	<aos:onready>
		<aos:viewport layout="border" id="viewport">
			<aos:gridpanel id="_g_ziliao" url="listzlzj.jhtml" height="250" region="north" split="true"
						   onrender="_g_ziliao_query" pageSize="${pagesize }"  onitemclick="get_a_receive" enableLocking="true">
				<aos:docked>
					<aos:hiddenfield id="tablename" name="tablename" value="${tablename}"/>
					<aos:hiddenfield id="cascode_id_" name="cascode_id_" value="${cascode_id_}"/>
					<aos:hiddenfield id="appraisal" name="appraisal"/>
					<aos:hiddenfield  name="flag" id="flag" value="${flag}"/>
					<aos:hiddenfield  name="appcode" id="appcode"/>
					<aos:hiddenfield  name="appmsg" id="appmsg"/>
					<aos:hiddenfield  name="xdfields" id="xdfields"/>
					<aos:hiddenfield name="_classtree_f" id="_classtree_f" value="${cascode_id_}" />
					<aos:hiddenfield id="treename" name="treename" value="${treename}"/>
					<aos:hiddenfield id="aos_module_id_" name="aos_module_id_" value="${aos_module_id_}"/>
					<aos:hiddenfield id="nd" name="nd" value="${nd}"/>
					<aos:hiddenfield id="lx" name="lx" value="${lx}"/>
					<aos:hiddenfield id="zjnd" name="zjnd" value="${zjnd}"/>
					<aos:dockeditem xtype="tbtext" text="接收信息" />
					<aos:dockeditem text="新增任务" icon="add.png"   onclick="_w_ziliao_show"/>
					<aos:dockeditem text="修改" icon="edit.png" onclick="_w_ziliao_u_show"  id="_f_editi_message" />
					<aos:dockeditem text="删除" icon="del2.png" onclick="_g_ziliao_del"/>
					<aos:dockeditem text="显示" icon="config1.png" onclick="_w_config_show" />
					<aos:dockeditem text="排序" icon="more/object-order-back.png"
									onclick="_w_order_show" />
					<aos:dockeditem text="顺序" icon="icon146.png" onclick="_g_field_order_show" />

					<aos:button text="导入" icon="folder8.png" scale="small" margin="0 0 0 0" id="importt">
						<aos:menu plain="false">
							<aos:menuitem text="导入目录" icon="folder8.png" onclick="_w_import_show"  id="_f_import_message"/>
							<aos:menuitem text="导入全文" icon="folder8.png" onclick="_w_import_path_show"  id="_f_import_path_message"/>
						</aos:menu>
					</aos:button>
					<aos:button text="移交书" icon="folder8.png" scale="small" margin="0 0 0 0" id="yjs">
						<aos:menu plain="false">
							<aos:menuitem text="移交书上传" icon="folder2.png" onclick="_fileupload_show" />
							<aos:menuitem text="打开移交书" id="openFile" icon="folder2.png" onclick="open_file"/>
						</aos:menu>
					</aos:button>

<%--					<aos:dockeditem text="接收报告" icon="more/network-receive.png" onclick="_w_receive_report" id="_i_jieshou_report" />--%>
					<aos:dockeditem text="提交审核" icon="more/archive-extract-3.png" onclick="_g_receive_pici" />
					<aos:dockeditem text="领导审核" icon="folder8.png" onclick="_w_leader_examine" />
					<aos:dockeditem text="正式接收" icon="folder8.png" onclick="_w_formal_receive"  />
				</aos:docked>
				<aos:selmodel type="checkbox" mode="multi" />
				<aos:column dataIndex="id_" header="流水号" hidden="true"  locked="true"/>
				<aos:column dataIndex="pch" header="征集编号"  width="100" />
				<aos:column dataIndex="zjnd" header="征集年度"  width="100" />
				<aos:column dataIndex="zjsj" header="征集时间"  width="100" />
				<aos:column dataIndex="zjr" header="征集人"  width="100" />
				<aos:column dataIndex="yjbm" header="移交部门"  width="100" />
				<aos:column dataIndex="yjr" header="移交人"  width="100" />
				<aos:column dataIndex="tm" header="题名"  width="100" />
				<aos:column dataIndex="zlml" header="资料门类"  width="100" />
				<aos:column dataIndex="xcsj" header="形成时间"  width="100" />
				<aos:column dataIndex="cbs" header="出版社"  width="100" />
				<aos:column dataIndex="zz" header="作者"  width="100" />
				<aos:column dataIndex="ztlx" header="载体类型"  width="100" />
				<aos:column dataIndex="ys" header="页数"  width="100" />
				<aos:column dataIndex="wz" header="文种"  width="100" />
				<aos:column dataIndex="sc" header="时长"  width="100" />
				<aos:column dataIndex="cs" header="册(本)数"  width="100" />
				<aos:column dataIndex="sfzj" header="是否转交"  width="100" />
				<aos:column dataIndex="zhjsj" header="转交时间"  width="100" />
				<aos:column dataIndex="zhjbm" header="转交部门"  width="100" />
				<aos:column dataIndex="zhjr" header="转交人"  width="100" />
				<aos:column dataIndex="yjssc" header="移交书上传"  width="200" />
				<aos:column dataIndex="cjr" header="创建人"  width="100" hidden="true"/>
				<aos:column dataIndex="jsr" header="接收人"  width="100" />
				<aos:column dataIndex="jssj" header="接收时间"  width="100" />
				<aos:column dataIndex="jszt" header="接收状态"  width="100" />
				<aos:column dataIndex="flag_submit" header="提交状态"  width="100" />
				<aos:column dataIndex="flag_examine" header="审核状态" width="100" />
				<aos:column dataIndex="shperson" header="审核人" width="100" />
				<aos:column dataIndex="shsj" header="审核时间" width="100" />
				<aos:column dataIndex="shyj" header="审核意见" width="100" />
				<aos:column dataIndex="bz" header="备注"  width="100" />
				<aos:column header="" flex="1" />
			</aos:gridpanel>
			<aos:gridpanel id="_g_data" url="listAccounts.jhtml" height="450" region="center" split="true"
						     rowclass="true" pageSize="${pagesize }"  enableLocking="true">
				<aos:docked>
					<aos:hiddenfield id="cascode_id_" name="cascode_id_" value="${cascode_id_}"/>
					<aos:dockeditem xtype="tbtext" text="数据信息" />
					<aos:dockeditem text="新增"  icon="add2.png"
									onclick="_w_data_show"  id="_f_add_message"/>
					<aos:dockeditem text="修改" icon="edit.png" onclick="fn_g_data"   />
					<aos:dockeditem text="删除" icon="del2.png"  id="_f_delete_message"
									onclick="_g_data_del"/>
					<aos:dockeditem text="上传文件"  icon="more/go-top-8.png" onclick="_fileupload_data_show" />
					<aos:dockeditem text="显示" icon="picture.png"  id="_f_filename_message"
									onclick="_w_picture_show"  />
					<aos:dockeditem text="设置" icon="layout.png"
									onclick="_w_input_show"  id="_f_input_message"/>
					<aos:dockeditem text="设置条目数" icon="config1.png" onclick="_w_config_show" />
					<aos:dockeditem text="排序" icon="more/object-order-back.png"
									onclick="_w_order_show" />
					<aos:dockeditem text="检索" icon="query.png"  onclick="_w_query_show"  />
					<aos:dockeditem text="显示顺序" icon="icon146.png" onclick="_g_field_order_show" />
				</aos:docked>
				<aos:selmodel type="checkbox" mode="multi" />
				<aos:column dataIndex="id_" header="流水号" hidden="true" />
				<aos:column dataIndex="_path" header="电子文件"
							rendererFn="fn_path_render"  hidden="true"  />

				<c:forEach var="field" items="${fieldDtos}">
					<aos:column dataIndex="${field.fieldenname}"
								header="${field.fieldcnname }" width="${field.dislen }"
								rendererField="field_type_" >
						<aos:textfield />
					</aos:column>
				</c:forEach>
				<aos:column header="操作" width="100" align="center" rendererFn="fn_button_render"/>
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
			<aos:window id="_w_config" title="设置" autoScroll="true" layout="column"
						width="300" border="false">
				<aos:formpanel id="_f_config" layout="column" labelWidth="80"
							   columnWidth="1">
					<aos:textfield fieldLabel="每页显示" name="pagesize" value="${pagesize }"
								   columnWidth="0.9" />
					<aos:docked dock="bottom" ui="footer">
						<aos:dockeditem xtype="tbfill" />
						<aos:dockeditem onclick="_f_info_ok" xtype="button" text="确定"
										icon="ok.png" />
						<aos:dockeditem onclick="#_w_config.hide();" xtype="button"
										text="取消" icon="del.png" />
						<aos:dockeditem xtype="tbfill" />
					</aos:docked>
				</aos:formpanel>
			</aos:window>
			<aos:window id="_w_field_order"  title="显示顺序" height="530" autoScroll="true" onshow="_g_order_query">
				<aos:gridpanel hidePagebar="true" id="_g_field_order"
							   url="listOrderInfos.jhtml" width="700"
							   pageSize="60" drag="true">
					<aos:docked>
						<aos:dockeditem xtype="tbtext" text="数据表字段" />
					</aos:docked>
					<aos:column header="流水号" dataIndex="id_" hidden="true" />
					<aos:column header="是否显示" dataIndex="FieldView" width="30" type="check" />
					<aos:column header="字段中文名称" dataIndex="FieldCnName" width="90" />
					<aos:column header="字段英文名称" dataIndex="FieldEnName" width="90" />
					<aos:column header="字段长度" dataIndex="indx" width="90" hidden="true" />
				</aos:gridpanel>
				<aos:docked dock="bottom" ui="footer">
					<aos:dockeditem xtype="tbfill" />
					<aos:dockeditem onclick="_submit_order" text="保存" icon="ok.png" />
					<aos:dockeditem onclick="#_w_field_order.hide();" text="关闭"
									icon="close.png" />
				</aos:docked>
			</aos:window>
			<aos:window id="_w_order" title="档案排序" height="290" width="555"
						autoScroll="true" onactivate="load_order">
				<aos:formpanel id="_f_order" width="540" layout="column"
							   labelWidth="75">
					<aos:panel region="east" width="200" height="200"  title="源字段列表">
						<!-- 添加grid -->
						<aos:gridpanel id="_g_field" region="east"
									   url="queryFields.jhtml?tablename=${tablename}" split="true"
									   hidePagebar="true"
									   splitterBorder="0 1 0 1" width="200" height="250">
							<aos:selmodel type="checkbox" mode="multi" />
							<aos:column header="流水号" dataIndex="id_" hidden="true" />
							<aos:column header="字段英文" dataIndex="fieldenname" width="60" />
							<aos:column header="字段中文" dataIndex="fieldcnname" width="60" />
						</aos:gridpanel>
					</aos:panel>
					<aos:panel region="center" layout="column" width="80" height="200" title="操作">
						<aos:button text="右移" onclick="_g_remove_field"
									style="marginTop:'15px'" icon="right.png" scale="small"
									columnWidth="1" />
						<aos:button text="左移" style="marginTop:'15px'" icon="left.png"
									scale="small" columnWidth="1" onclick="_g_delete_field" />
						<aos:button text="上移" style="marginTop:'15px'" icon="up.png"
									scale="small" columnWidth="1" onclick="_g_up_field" />
						<aos:button text="下移" style="marginTop:'15px'" icon="down.png"
									scale="small" columnWidth="1" onclick="_g_down_field" />
					</aos:panel>
					<aos:panel region="west" width="240" height="200" title="待排列字段列表"><!-- 添加grid-->
						<aos:gridpanel id="_g_order" split="true" hidePagebar="true" url="addOrder.jhtml"
									   width="500">
							<aos:plugins>
								<aos:editing ptype="cellediting" clicksToEdit="1" onedit="fn_order_edit" />
							</aos:plugins>
							<aos:column header="升降序" dataIndex="orderascing" hidden="true"/>
							<aos:column header="字段英文" dataIndex="orderenname" width="60" />
							<aos:column header="字段中文" dataIndex="ordercnname" width="60" />
							<aos:column header="升降序" dataIndex="orderascdesc"
										width="60" >
								<aos:combobox name="orderascdesccombobox"
											  fields="['orderascenname', 'orderasccnname']" id="orderascdesccombobox"
											  editable="false" columnWidth="0.49" url="orderComboBox.jhtml"
											  displayField="orderasccnname" valueField="orderasccnname" />
							</aos:column>
							<aos:column header="数值" id="ordermath" dataIndex="ordermath"
										width="60" type="check" />
						</aos:gridpanel>
					</aos:panel>
					<aos:docked dock="bottom" ui="footer">
						<aos:dockeditem xtype="tbfill" />
						<aos:dockeditem xtype="button" text="保存" icon="ok.png"
										onclick="order_by_" />
						<aos:dockeditem xtype="button" text="关闭" icon="del.png"
										onclick="#_w_order.hide()" />
						<aos:dockeditem xtype="tbfill" />
					</aos:docked>
				</aos:formpanel>
			</aos:window>
			<aos:window id="_w_test" title="四性检测结果" onshow="_w_test_onshow">
				<aos:formpanel id="_f_test" width="400" layout="anchor" labelWidth="70" >
					<aos:textareafield name="sxjcjg" fieldLabel="原因" readOnly="true" />
				</aos:formpanel>
				<aos:docked dock="bottom" ui="footer">
					<aos:dockeditem xtype="tbfill" />
					<aos:dockeditem onclick="#_w_test.hide();" text="关闭" icon="close.png" />
				</aos:docked>
			</aos:window>
			<aos:window id="_w_sxjc_window" title="四性检测配置" onshow="_w_sxjc_onshow">
				<aos:formpanel id="_f_sxjc_window"  layout="column" width="400">
					<aos:hiddenfield name="id_" />
					<aos:radioboxgroup fieldLabel="档号重复和非空" labelWidth="200"  id="dh_equals" columns="[100,100]">
						<aos:radiobox name="dh_equals" boxLabel="需要" /><aos:radiobox name="dh_equals" boxLabel="不需要" />
					</aos:radioboxgroup>
					<aos:radioboxgroup fieldLabel="是否有电子文件且能打开" labelWidth="200" id="file_boolean" columns="[100,100]">
						<aos:radiobox name="file_boolean" boxLabel="需要" /><aos:radiobox name="file_boolean" boxLabel="不需要" />
					</aos:radioboxgroup>
					<aos:radioboxgroup fieldLabel="是否含有年度且四位" labelWidth="200" id="nd_" columns="[100,100]">
						<aos:radiobox name="nd_" boxLabel="需要" /><aos:radiobox name="nd_" boxLabel="不需要" />
					</aos:radioboxgroup>
					<aos:radioboxgroup fieldLabel="是否含有全宗号" labelWidth="200" id="qzh_" columns="[100,100]">
						<aos:radiobox name="qzh_" boxLabel="需要" /><aos:radiobox name="qzh_" boxLabel="不需要" />
					</aos:radioboxgroup>
					<aos:radioboxgroup fieldLabel="是否含有件号且四位" labelWidth="200" id="jh_" columns="[100,100]">
						<aos:radiobox name="jh_" boxLabel="需要" /><aos:radiobox name="jh_" boxLabel="不需要" />
					</aos:radioboxgroup>
				</aos:formpanel>
				<aos:docked dock="bottom" ui="footer">
					<aos:dockeditem xtype="tbfill" />
					<aos:dockeditem onclick="save_sxjc_properties" text="保存" icon="save.png" />
					<aos:dockeditem onclick="#_w_sxjc_window.hide();" text="关闭" icon="close.png" />
				</aos:docked>
			</aos:window>
			<aos:window id="_w_sxjc_properties" title="四性检测" width="500" height="500" onshow="load_sxjc_properties">
				<aos:gridpanel id="_g_sxjc_properties"  enableLocking="true" split="true" splitterBorder="1 0 1 0" width="480" hidePagebar="true" url="getSxjc.jhtml">
					<aos:column type="rowno" />
					<aos:selmodel type="checkbox" mode="multi" />
					<aos:column header="编号" dataIndex="code_" />
					<aos:column header="检测项目" dataIndex="desc_" width="500" />
					<aos:column header="" flex="1" />
				</aos:gridpanel>
				<aos:docked dock="bottom" ui="footer">
					<aos:dockeditem xtype="tbfill" />
					<aos:dockeditem onclick="go_sxjc_properties" text="确定" icon="ok.png" />
					<aos:dockeditem onclick="#_w_sxjc_properties.hide();" text="关闭" icon="close.png" />
				</aos:docked>
			</aos:window>
			<aos:window id="_w_ziliao" title="新增"   onshow="_new_task">
				<aos:formpanel id="_f_ziliao" layout="column" width="800">
					<aos:textfield name="pch" fieldLabel="征集编号" columnWidth="0.49" />
					<aos:textfield name="zjnd" value="${zjnd}" fieldLabel="征集年度" columnWidth="0.49"/>
					<aos:datefield name="zjsj" fieldLabel="征集时间" columnWidth="0.49"/>
					<aos:textfield name="zjr" fieldLabel="征集人" columnWidth="0.49" value="${user}"/>
					<aos:textfield name="yjbm" fieldLabel="移交部门" columnWidth="0.49"/>
					<aos:textfield name="yjr" fieldLabel="移交人" columnWidth="0.49"/>
					<aos:textfield name="tm" fieldLabel="任务名称" columnWidth="0.98"/>
					<aos:checkboxgroup fieldLabel="资料门类" columnWidth="0.49" id="zlml_add" columns="[80,80,80]">
						<aos:checkbox name="c5" boxLabel="报纸" />
						<aos:checkbox name="c5" boxLabel="期刊" />
						<aos:checkbox name="c5" boxLabel="图书" />
					</aos:checkboxgroup>

					<aos:datefield name="xcsj" fieldLabel="形成时间" columnWidth="0.49"/>
					<aos:textfield name="cbs" fieldLabel="出版社" columnWidth="0.49"/>
					<aos:textfield name="zz" fieldLabel="作者" columnWidth="0.49"/>
					<aos:checkboxgroup fieldLabel="载体类型" columnWidth="0.49" id="ztlx_add" columns="[80,80,80,80]">
						<aos:checkbox name="c1" boxLabel="文字" />
						<aos:checkbox name="c1" boxLabel="照片" />
						<aos:checkbox name="c1" boxLabel="光盘" />
						<aos:checkbox name="c1" boxLabel="实物" />
					</aos:checkboxgroup>
					<aos:textfield name="ys" fieldLabel="页数" columnWidth="0.49"/>

					<aos:checkboxgroup fieldLabel="文种" columnWidth="0.49" id="wz_add" columns="[80,80,80,80,80]">
						<aos:checkbox name="c7" boxLabel="中文" />
						<aos:checkbox name="c7" boxLabel="朝文" />
						<aos:checkbox name="c7" boxLabel="日文" />
						<aos:checkbox name="c7" boxLabel="英文" />
						<aos:checkbox name="c7" boxLabel="其他" />
					</aos:checkboxgroup>
					<aos:textfield name="sc" fieldLabel="时长" columnWidth="0.49"/>
					<aos:textfield name="cs" fieldLabel="册(本)数" columnWidth="0.49"/>
					<aos:hiddenfield  fieldLabel="创建人"  name="cjr" value="${user}"/>
					<aos:combobox fieldLabel="是否转交" columnWidth="0.49" name="sfzj" value="是">
						<aos:option value="是" display="是" />
						<aos:option value="否" display="否" />
					</aos:combobox>
					<aos:datefield name="zhjsj" fieldLabel="转交时间"  columnWidth="0.49"/>
					<aos:textfield name="zhjbm" fieldLabel="转交部门" columnWidth="0.49"/>
					<aos:textfield name="zhjr" fieldLabel="转交人" columnWidth="0.49"/>
					<aos:textfield name="bz" fieldLabel="备注" columnWidth="0.98"/>
					<aos:hiddenfield id="lx" name="lx" value="${lx}"/>
					<aos:docked dock="bottom">
						<aos:dockeditem xtype="tbfill"/>
						<aos:dockeditem text="上一条" icon="more/go-previous.png"  onclick="_f_previous_data"/><%----%>
						<aos:dockeditem text="下一条" icon="more/go-next.png" onclick="_f_next_data"/><%----%>
						<aos:dockeditem text="添加" icon="add.png" onclick="_f_ziliao_save"/>
						<aos:dockeditem text="关闭" icon="close.png" onclick="#_w_ziliao.hide()"/>
					</aos:docked>
				</aos:formpanel>
			</aos:window>
			<aos:window id="_w_ziliao_u" title="修改"  >
				<aos:formpanel id="_f_ziliao_u" layout="column" width="800">
					<aos:hiddenfield name="id_"  />
					<aos:textfield name="pch" fieldLabel="征集编号" columnWidth="0.49" />
					<aos:textfield name="zjnd" fieldLabel="征集年度" columnWidth="0.49"/>
						<aos:datefield name="zjsj" fieldLabel="征集时间" columnWidth="0.49"/>
					<aos:textfield name="zjr" fieldLabel="征集人" columnWidth="0.49" />
					<aos:textfield name="yjbm" fieldLabel="移交部门" columnWidth="0.49"/>
					<aos:textfield name="yjr" fieldLabel="移交人" columnWidth="0.49"/>
					<aos:textfield name="tm" fieldLabel="任务名称" columnWidth="0.98"/>
					<aos:checkboxgroup fieldLabel="资料门类" columnWidth="0.49" id="zlml_update" columns="[80,80,80]">
						<aos:checkbox name="c5" boxLabel="报纸" />
						<aos:checkbox name="c5" boxLabel="期刊" />
						<aos:checkbox name="c5" boxLabel="图书" />
					</aos:checkboxgroup>

					<aos:datefield name="xcsj" fieldLabel="形成时间" columnWidth="0.49"/>
					<aos:textfield name="cbs" fieldLabel="出版社" columnWidth="0.49"/>
					<aos:textfield name="zz" fieldLabel="作者" columnWidth="0.49"/>
					<aos:checkboxgroup fieldLabel="载体类型" columnWidth="0.49" id="ztlx_update" columns="[80,80,80,80]">
						<aos:checkbox name="c1" boxLabel="文字" />
						<aos:checkbox name="c1" boxLabel="照片" />
						<aos:checkbox name="c1" boxLabel="光盘" />
						<aos:checkbox name="c1" boxLabel="实物" />
					</aos:checkboxgroup>
					<aos:textfield name="ys" fieldLabel="页数" columnWidth="0.49"/>

					<aos:checkboxgroup fieldLabel="文种" columnWidth="0.49" id="wz_update" columns="[80,80,80,80,80]">
						<aos:checkbox name="c7" boxLabel="中文" />
						<aos:checkbox name="c7" boxLabel="朝文" />
						<aos:checkbox name="c7" boxLabel="日文" />
						<aos:checkbox name="c7" boxLabel="英文" />
						<aos:checkbox name="c7" boxLabel="其他" />
					</aos:checkboxgroup>
					<aos:textfield name="sc" fieldLabel="时长" columnWidth="0.49"/>
						<aos:textfield name="cs" fieldLabel="册(本)数" columnWidth="0.49"/>
					<aos:combobox fieldLabel="是否转交" columnWidth="0.49" name="sfzj" value="是">
						<aos:option value="是" display="是" />
						<aos:option value="否" display="否" />
					</aos:combobox>
					<aos:hiddenfield  fieldLabel="创建人"  name="cjr"/>
					<aos:datefield name="zjsj" fieldLabel="转交时间"  columnWidth="0.49"/>
					<aos:textfield name="zjbm" fieldLabel="转交部门" columnWidth="0.49"/>
					<aos:textfield name="zhjr" fieldLabel="转交人" columnWidth="0.49"/>
					<aos:textfield name="bz" fieldLabel="备注" columnWidth="0.98"/>
					<aos:hiddenfield id="lx" name="lx" value="${lx}"/>
					<aos:docked dock="bottom">
						<aos:dockeditem xtype="tbfill"/>
						<aos:dockeditem text="上一条" icon="more/go-previous.png"  onclick="_f_previous_data2"/><%----%>
						<aos:dockeditem text="下一条" icon="more/go-next.png" onclick="_f_next_data2"/><%----%>
						<aos:dockeditem text="添加" icon="add.png" onclick="_f_ziliao_u_save"/>
						<aos:dockeditem text="关闭" icon="close.png" onclick="#_w_ziliao_u.hide()"/>
					</aos:docked>
				</aos:formpanel>
			</aos:window>
			<aos:window id="_w_data_i" title="新增"   draggable="false"  closable="false" closeAction="hide"
						autoScroll="true" x="50" width="860" height="500"
						onrender="_w_data_i_render" onshow="open_window">
				<aos:formpanel id="_f_data_i" labelWidth="100"   height="800"  width="750" margin="0 0 0 20">
					<aos:hiddenfield  name="_classtree" id="_classtree" value="${cascode_id_}" />
				</aos:formpanel>
				<aos:docked dock="bottom" ui="footer">
					<aos:dockeditem xtype="tbfill" />
					<aos:dockeditem text="上一条" icon="more/go-previous.png"  onclick="_f_previous_data3"/><%----%>
					<aos:dockeditem text="下一条" icon="more/go-next.png" onclick="_f_next_data3"/><%----%>
					<aos:dockeditem onclick="_f_data_i_save" text="添加" icon="icon80.png" />
					<aos:dockeditem onclick="_f_data_edit" text="保存" icon="icon82.png" />
					<aos:dockeditem onclick="#_w_data_i.close();" text="关闭"
									icon="close.png" />
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
			<aos:window id="_w_fj_open_path" title="移交书" onshow="open_path_onload">
				<aos:formpanel id="_f_open_path" width="700" layout="column">
					<aos:fieldset title="移交书" labelWidth="70" columnWidth="0.99" contentEl="_div_yijiaoshu" >
					</aos:fieldset>
					<aos:docked dock="bottom" ui="footer">
						<aos:dockeditem xtype="tbfill"/>
						<aos:dockeditem text="关闭" icon="close.png" onclick="#_w_fj_open_path.hide()"/>
					</aos:docked>
				</aos:formpanel>
			</aos:window>
		</aos:viewport>
		<script type="text/javascript">
			window.onload=function() {
				var compilation_flag = "<%=session.getAttribute("compilation_flag_")%>";
				if (compilation_flag === "数字科员") {
					Ext.getCmp("_f_leader_examine").hide();
				} else if (compilation_flag === "数字管理员") {
					Ext.getCmp("_f_receive_pici").hide();
				}
				_w_data_input('_f_data_i');
			}
			function open_file(){
				var selection = AOS.selection(_g_ziliao, 'id_');
				if (AOS.empty(selection)) {
					AOS.tip('请选择移交书的任务!');
					return;
				}
				var pch = AOS.selectone(_g_ziliao).raw.pch;
				AOS.ajax({
					params : {
						pch:pch
					},
					url : 'booleanfile_ziliao.jhtml',
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
				var pch = AOS.selectone(_g_ziliao).raw.pch;
				AOS.ajax({
					params : {
						pch:pch
					},
					url : 'getfile_fj.jhtml',
					ok : function(data) {
						document.getElementById("_img_yijiaoshu").src="data:image/jpeg;base64,"+data.appmsg;
						//_f_open_path.getForm().findField('_path_message').setValue(data.appmsg);
					}
				});
			}



			//弹出新增用户窗口
			function _w_data_show() {
				var record = AOS.selectone(_g_ziliao);
				if(record) {
					AOS.reset(_f_data_i);
					_w_data_i.show();
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
			function _f_ziliao_u_save(){
				var zlml="";
				var xqCheck = Ext.getCmp('zlml_update').items;
				for(var i = 0; i < xqCheck.length; i++){
					if(xqCheck.get(i).checked==true){
						if(zlml==""){
							zlml=xqCheck.get(i).boxLabel;
						}else{
							zlml=zlml+","+xqCheck.get(i).boxLabel;
						}
					}
				}
				var ztlx="";
				var xqCheck = Ext.getCmp('ztlx_update').items;
				for(var i = 0; i < xqCheck.length; i++){
					if(xqCheck.get(i).checked==true){
						if(ztlx==""){
							ztlx=xqCheck.get(i).boxLabel;
						}else{
							ztlx=ztlx+","+xqCheck.get(i).boxLabel;
						}
					}
				}
				var wz="";
				var xqCheck = Ext.getCmp('wz_update').items;
				for(var i = 0; i < xqCheck.length; i++){
					if(xqCheck.get(i).checked==true){
						if(wz==""){
							wz=xqCheck.get(i).boxLabel;
						}else{
							wz=wz+","+xqCheck.get(i).boxLabel;
						}
					}
				}
				//得到选项框选择的内容
				AOS.ajax({
					forms:_f_ziliao_u,
					params:{_classtree:cascode_id_.getValue(),zlml:zlml,ztlx:ztlx,wz:wz},
					url:'updateZiliao.jhtml',
					ok:function(data){
						if(data.appcode===1){
							_w_ziliao_u.hide();
							_g_ziliao_store.reload();
							AOS.tip(data.appmsg);
						}else {
							AOS.err(AOS.appmsg);
						}
					}
				})
			}
			function _f_ziliao_save(){
				var zlml="";
				var xqCheck = Ext.getCmp('zlml_add').items;
				for(var i = 0; i < xqCheck.length; i++){
					if(xqCheck.get(i).checked==true){
						if(zlml==""){
							zlml=xqCheck.get(i).boxLabel;
						}else{
							zlml=zlml+","+xqCheck.get(i).boxLabel;
						}
					}
				}
				var ztlx="";
				var xqCheck = Ext.getCmp('ztlx_add').items;
				for(var i = 0; i < xqCheck.length; i++){
					if(xqCheck.get(i).checked==true){
						if(ztlx==""){
							ztlx=xqCheck.get(i).boxLabel;
						}else{
							ztlx=ztlx+","+xqCheck.get(i).boxLabel;
						}
					}
				}
				var wz="";
				var xqCheck = Ext.getCmp('wz_add').items;
				for(var i = 0; i < xqCheck.length; i++){
					if(xqCheck.get(i).checked==true){
						if(wz==""){
							wz=xqCheck.get(i).boxLabel;
						}else{
							wz=wz+","+xqCheck.get(i).boxLabel;
						}
					}
				}
				AOS.ajax({
					forms:_f_ziliao,
					params:{_classtree:cascode_id_.getValue(),zlml:zlml,ztlx:ztlx,wz:wz},
					url:'saveZiliao.jhtml',
					ok:function(data){
						if(data.appcode === 1){
							_w_ziliao.hide();
							_g_ziliao_store.reload();
							AOS.tip(data.appmsg);
						}else {
							AOS.err(data.appmsg);
						}
					}
				})
			}
			function _w_ziliao_u_show(){
				var record = AOS.selectone(_g_ziliao);
				if(record) {
					_w_ziliao_u.show();
					_f_ziliao_u.loadRecord(record);
					var xqCheck = Ext.getCmp('ztlx_update').items;
					for(var i = 0; i < xqCheck.length; i++){
						if(record.data.ztlx.indexOf(xqCheck.get(i).boxLabel)!=-1){
							xqCheck.get(i).setValue(true);
						}
					}
					var zlml = Ext.getCmp('zlml_update').items;
					for(var i = 0; i < zlml.length; i++){
						if(record.data.zlml.indexOf(zlml.get(i).boxLabel)!=-1){
							zlml.get(i).setValue(true);
						}
					}
					var wz = Ext.getCmp('wz_update').items;
					for(var i = 0; i < wz.length; i++){
						if(record.data.wz.indexOf(wz.get(i).boxLabel)!=-1){
							wz.get(i).setValue(true);
						}
					}
				}
			}

			//生成录入界面
			function _w_data_input(formid){
				var _panel = Ext.getCmp(formid);
				_panel.removeAll();
				//_panel.reload();
				AOS.ajax({
					params: {tablename: tablename.getValue()},
					url: 'getInputColumn.jhtml',
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
								//设置标签必录入项
								if(data[i].ynnnull=='0'){
									/*  items=[{
                                          xtype : 'label',
                                          //value:data[i].displayname,
                                          text:data[i].displayname,
                                          style:'color:red',

                                          width : parseInt(data[i].cwidth),
                                          height : parseInt(data[i].cheight),
                                          x:parseInt(data[i].cleft)-200,
                                          y:parseInt(data[i].ctop)-50,
                                      }]*/
								}else{
									/*items=[{
                                        xtype : 'label',
                                        //value:data[i].displayname,
                                        text:data[i].displayname,
                                        width : parseInt(data[i].cwidth),
                                        height : parseInt(data[i].cheight),
                                        x:parseInt(data[i].cleft)-200,
                                        y:parseInt(data[i].ctop)-50,
                                    }]*/
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
										fieldLabel:data[i].displayname,
										//x:parseInt(data[i].cleft)-200,
										//y:parseInt(data[i].ctop)-50,
										maxWidth:parseInt(data[i].cwidth),
										width:parseInt(data[i].cwidth),
										//height:parseInt(data[i].cheight),
										margin:'2,0,0,0',
										allowBlank:ynnull,
										listeners:{
											select:function(e){
												if(strdh.indexOf(this.name)>-1){
													var strarray=strdh.split(',');
													var strtemp='';
													for(var i=1;i<strarray.length; i++){
														if(i==1){
															strtemp =Ext.getCmp(strarray[i]).getValue();
															continue;
														}
														strtemp = strtemp+'-'+Ext.getCmp(strarray[i]).getValue();
													}
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

									if(data[i].fieldname=="tmD"){
										items = itemsGroup_textareafield(data[i],strdh);
									}else{
										items = itemsGroup(data[i],strdh);
									}

								}
							}
							_panel.add(items);
						}
					}
				});
			}

			function itemsGroup_textareafield(data,strdh){
				var strx=parseInt(data.cleft)-200;
				var stry = parseInt(data.ctop)-50;
				var strwidth = parseInt(data.cwidth);
				var strheight=parseInt(data.cheight);
				var displayname=data.displayname;

				var fieldname = data.fieldname.substring(0,data.fieldname.length-1);
				var ynnull;
				var strdisplayname;
				if(data.ynnnull==0){
					ynnull=false;
					strdisplayname='<lable style="color: red;">'+displayname+'</lable>';
				}else{
					ynnull=true;
					strdisplayname=displayname;
				}
				//var ynnull=data.ynnnull=='0';
				var str =[{
					xtype :'textareafield',
					id:fieldname,
					name:fieldname,
					width:strwidth,
					margin:'2,0,0,0',
					//maxWidth :strwidth,
					height :strheight,
					//x:strx,
					//y:stry,
					//columnWidth: 0.5,

					fieldLabel:strdisplayname,
					//maxLength:data.edtmax,
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
			function itemsGroup(data,strdh){
				var strx=parseInt(data.cleft)-200;
				var stry = parseInt(data.ctop)-50;
				var strwidth = parseInt(data.cwidth);
				var strheight=parseInt(data.cheight);
				var displayname=data.displayname;

				var fieldname = data.fieldname.substring(0,data.fieldname.length-1);
				var ynnull;
				var strdisplayname;
				if(data.ynnnull==0){
					ynnull=false;
					strdisplayname='<lable style="color: red;">'+displayname+'</lable>';
				}else{
					ynnull=true;
					strdisplayname=displayname;
				}
				//var ynnull=data.ynnnull=='0';
				var str =[{
					xtype :'textfield',
					id:fieldname,
					name:fieldname,
					width:strwidth,
					margin:'2,0,0,0',
					//maxWidth :strwidth,
					height :strheight,
					//x:strx,
					//y:stry,
					//columnWidth: 0.5,

					fieldLabel:strdisplayname,
					//maxLength:data.edtmax,
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
			function _new_task(){
				//根据tree名称得到对应的部门名称
				var treename=Ext.getCmp("treename").getValue();
				AOS.ajax({
					params:{
						name_:"实体接收流水号",
						tablename:Ext.getCmp("tablename").getValue(),
						lx:lx.getValue(),
						zjnd:Ext.getCmp("nd").getValue()
					},
					url:'zlzjlsh.jhtml',
					ok:function(data){
						_f_ziliao.form.findField("pch").setValue("ZL"+Ext.getCmp("nd").getValue()+data.index);
					}
				});
				var tablename=Ext.getCmp("tablename").getValue();
				if(tablename=="zlbz_receive"){
					Ext.getCmp('zlml_add').items.get(0).setValue(true);
				}
				if(tablename=="zlqf_receive"){
					Ext.getCmp('zlml_add').items.get(1).setValue(true);
				}
				if(tablename=="zlts_receive"){
					Ext.getCmp('zlml_add').items.get(2).setValue(true);
				}
				//_f_ziliao.form.findField("cjr").setValue("<%=session.getAttribute("user")%>");

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
			//弹出上传文件窗口
			function _fileupload_data_show() {

				//我得加个重置
				var tablename=Ext.getCmp("tablename").getValue();

				var selection = AOS.selection(_g_ziliao, 'id_');
				var ids = AOS.selection(_g_data, 'id_');
				if (AOS.empty(selection)||AOS.empty(ids)) {
					AOS.tip('请选中任务和要上传的条目数据');
					return;
				}else{
					Ext.getCmp("_fileupload_add").show();
					$(document).ready(function() {
						$("#file").fileinput({
							language: 'zh', //设置语言
							uploadUrl: '${cxt}/archive/upload/uploadfiles_data.jhtml', //上传的地址
							uploadExtraData:{
								tablename: tablename,
								id_:AOS.selectone(_g_data).data.id_
							},
							fileActionSettings:{
								showZoom:false//显示预览按钮
							},
							allowedFileExtensions: ['xls','xlsx','jpg', 'gif', 'png','pdf','tif'],//接收的文件后缀
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
				}

			}
			//接收提交
			function _g_receive_pici(){
				var selection = AOS.selection(_g_ziliao, 'id_');
				if (AOS.empty(selection)) {
					AOS.tip('提交前请先选中数据。');
					return;
				}
				var record = AOS.selectone(_g_ziliao);
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
				var msg = AOS.merge('确认要提交选中的[{0}]个任务数据吗？', AOS.rows(_g_ziliao));
				AOS.confirm(msg, function (btn) {
					if (btn === 'cancel') {
						AOS.tip('提交操作被取消。');
						return;
					}
					var _classtree_f=Ext.getCmp("_classtree_f").getValue();
					var tablename=Ext.getCmp("tablename").getValue();
					var aos_module_id_=Ext.getCmp("aos_module_id_").getValue();
					AOS.ajax({
						params:{
							aos_module_id_:aos_module_id_,
							tablename:tablename,
							cascode_id_:_classtree_f,
							lx:${lx},
							zjnd:record.data.zjnd,
							'id_':id_
						},
						url : 'update_ziliao.jhtml',
						ok : function(data) {
							AOS.tip(data.appmsg);
							_g_ziliao_store.reload();
						}
					});
				});
			}
			function _w_leader_examine(){
				var selection = AOS.selection(_g_ziliao, 'id_');
				if (AOS.empty(selection)) {
					AOS.tip('提交前请先选中数据!');
					return;
				}
				var record = AOS.selectone(_g_ziliao);
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
			//加载表格数据
			function _g_data_query() {
				var params = {
					tablename : tablename.getValue(),
					cascode_id_:cascode_id_.getValue()
				};
				//这个Store的命名规则为：表格ID+"_store"。
				_g_data_store.getProxy().extraParams = params;
				_g_data_store.load();
			}
			function _w_ziliao_show(){
				_w_ziliao.show();
			}
			//加载数据,（批次号列表信息）
			function _g_receive_query() {
				var params = {
					flag:flag.getValue(),
					tablename:tablename.getValue(),
					nd:nd.getValue()
				};
				//这个Store的命名规则为：表格ID+"_store"。
				_g_receive_store.getProxy().extraParams = params;
				_g_receive_store.load();
			}
			//加载表格数据
			function _g_ziliao_query() {
				var params = {
					lx:${lx},
					zjnd:${zjnd}
				};
				//这个Store的命名规则为：表格ID+"_store"。
				_g_ziliao_store.getProxy().extraParams = params;
				_g_ziliao_store.load();
			}
			//父表点击，字表加载
			function get_a_data(){
				var record = AOS.selectone(_g_ziliao);
				var params = {
					tablename : tablename.getValue(),
					ajh:record.data.ajh,
					jgwt:"<%=session.getAttribute("jgwt")%>",
					cascode_id_:cascode_id_.getValue()
				};
				//这个Store的命名规则为：表格ID+"_store"。
				_g_ziliao_from_store.getProxy().extraParams = params;
				_g_ziliao_from_store.load();
			}
			function get_a_receive(){
				var record = AOS.selectone(_g_ziliao);
				var params = {
					tablename : tablename.getValue(),
					pch:record.data.pch,
					cascode_id_:cascode_id_.getValue()
				};
				//这个Store的命名规则为：表格ID+"_store"。
				_g_data_store.getProxy().extraParams = params;
				_g_data_store.load();
			}

			function fn_g_ziliao(){
				var selection = AOS.selection(_g_ziliao, 'id_');
				var tms = AOS.selection(_g_ziliao, 'tm');
				if (AOS.empty(selection)) {
					AOS.tip('修改前请先选中数据。');
					return;
				}
				AOS.ajax({
					params: {id_: AOS.selectone1(_g_ziliao).data.id_,
						tablename:tablename.getValue()
					},
					url: 'getData.jhtml',
					ok: function (data) {
						_f_data_i.form.setValues(data);
						_w_data_i.show();
					}
				});
			}
			//删除信息
			function _g_data_del() {
				var selection = AOS.selection(_g_data, 'id_');
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
							tablename: tablename.getValue()
						},
						ok: function (data) {
							_g_data_store.reload();
						}
					});
				});
			}
			function fn_g_ziliao_from(){
				var selection = AOS.selection(_g_ziliao_from, 'id_');
				var tms = AOS.selection(_g_ziliao_from, 'tm');
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
				_g_data_store.getProxy().extraParams = params;
				_g_data_store.load();
				_w_query_q.hide();
				AOS.reset(_f_query);
			}
			function _g_del_pici(){
				var selection = AOS.selection(_g_receive, 'id_');
				if (AOS.empty(selection)) {
					AOS.tip('删除前请先选中任务。');
					return;
				}
				var msg = AOS.merge('确认要删除选中的[{0}]个用户数据吗？', AOS.rows(_g_receive));
				AOS.confirm(msg, function (btn) {
					if (btn === 'cancel') {
						AOS.tip('删除操作被取消。');
						return;
					}
					AOS.ajax({
						url: 'deleteRw_pici.jhtml',
						params: {
							aos_rows_: selection
						},
						ok: function (data) {
							AOS.tip(data.appmsg);
							_g_receive_store.reload();
						}
					});
				});
			}
			//记录替换
			function _f_data_replace(){
				AOS.ajax({
					forms : _f_replace,
					url : 'replaceRecord.jhtml',
					params:{
						tablename : tablename.getValue()
					},
					ok : function(data) {
						_w_query_q.hide();
						_g_data_store.reload();
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
						AOS.tip(data.appmsg);
					}
				});
			}
			//记录更新
			function _f_data_update_from(){
				var record = AOS.selectone(_g_ziliao);
				AOS.ajax({
					forms : _f_update_from,
					url : 'updateRecord_from.jhtml',
					params:{
						tablename : tablename.getValue(),
						jgwt:"<%=session.getAttribute("jgwt")%>",
						ajh:record.data.ajh
					},
					ok : function(data) {
						_w_query_q.hide();
						_g_ziliao_store.reload();
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
				var record = AOS.selectone(_g_ziliao);
				//把父亲的id传过去
				params["ajh"]=record.data.ajh;
				_g_ziliao_from_store.getProxy().extraParams = params;
				_g_ziliao_from_store.load();
				_w_query_q_from.hide();
				AOS.reset(_f_query_from);
			}
			//记录替换
			function _f_data_replace_from(){
				var record = AOS.selectone(_g_ziliao);

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
						_g_ziliao_from_store.reload();
						AOS.tip(data.appmsg);
					}
				});
			}
			//前后辍
			function _f_data_suffix_from(){
				var record = AOS.selectone(_g_ziliao);
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
						_g_ziliao_from_store.reload();
						AOS.tip(data.appmsg);
					}
				});
			}
			//部位
			function _f_data_repair_from(){
				var record = AOS.selectone(_g_ziliao);
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
						_g_ziliao_from_store.reload();

						AOS.tip(data.appmsg);
					}
				});
			}

			//弹出新增用户窗口
			function _w_data_from_show() {
				//展开之前进行重置
				//AOS.reset(_f_data_i_from);
				_w_data_i_from.show();
			}
			function _w_data_i_from_onshow(){
				if(typeof(AOS.selectone(_g_ziliao_from))!='undefined'){
					AOS.ajax({
						params: {id_: AOS.selectone(_g_ziliao_from).data.id_,
							tablename:tablename.getValue()+"_z"
						},
						url: 'getData_from.jhtml',
						ok: function (data) {
							_f_data_i_from.form.setValues(data);
						}
					});
					//此时把案卷号的值传过去
				}
				var record = AOS.selectone(_g_ziliao);
				var ajh="";
				if(!AOS.empty(record)){
					ajh=record.data.ajh;
				}
				//
				_f_data_i_from.form.findField("ajh_z").setValue(ajh);
			}
			//弹出上传文件窗口
			function _fileupload_show() {
				//我得加个重置
				var tablename=Ext.getCmp("tablename").getValue();
				var selection = AOS.selection(_g_ziliao, 'id_');
				if (AOS.empty(selection)) {
					AOS.tip('上传前请先选中任务!');
					return;
				}
				Ext.getCmp("_fileupload_add").show();
				$("#file").fileinput('destroy');
				$(document).ready(function() {
					$("#file").fileinput({
						language: 'zh', //设置语言
						uploadUrl: '${cxt}/archive/upload/uploadTemporary_zl.jhtml', //上传的地址
						uploadExtraData:{
							tablename: tablename,
							id_:AOS.selectone(_g_ziliao).data.id_
						},
						fileActionSettings:{
							showZoom:false//显示预览按钮
						},
						allowedFileExtensions: ['xls','xlsx','jpg', 'gif', 'png','pdf','tif'],//接收的文件后缀
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
						pch:AOS.selectone(_g_ziliao).data.pch,
						_classtree:_classtree.getValue()
					},
					ok : function(data) {
						if (data.appcode === -1) {
							AOS.err(data.appmsg);
						} else {
							//_w_data_i.hide();
							//_w_data_input('_f_data_u');
							//_g_ziliao_store.reload();
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
							_g_data_store.reload();
							//_w_data_i.hide();
						}
					}
				});
			}
			//卡片新增目录加存
			function _f_data_i_save_from(){
				var record = AOS.selectone(_g_ziliao);
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
							_g_ziliao_from_store.reload();
							AOS.tip(data.appmsg);
						}
					}
				});
			}
			//下一页
			function _f_next_data(){
				var count=Ext.getCmp("_g_data").getStore().getCount();
				var me=Ext.getCmp("_g_data").getSelectionModel().getSelection();
				//var record = AOS.selectone(_g_data);
				//得到执行行的坐标
				var rowIndex = Ext.getCmp("_g_data").getStore().indexOf(me[0]);
				if(rowIndex==count-1){
					AOS.err("当前最后一条!");
					return;
				}
				var s=Ext.getCmp("_g_data").getStore().getAt(rowIndex+1);
				//原先行取消选中
				Ext.getCmp("_g_data").getSelectionModel().deselect(rowIndex);
				//此时让光标选中下一行
				Ext.getCmp("_g_data").getSelectionModel().select(rowIndex+1, true);
				//组件被显示后触发。
				Ext.getCmp("_f_data_i").form.setValues(s.data);
			}

			//上一页
			function _f_previous_data2(){
				var count=Ext.getCmp("_g_ziliao").getStore().getCount();
				var me=Ext.getCmp("_g_ziliao").getSelectionModel().getSelection();
				//var record = AOS.selectone(_g_ziliao);
				//得到执行行的坐标
				var rowIndex = Ext.getCmp("_g_ziliao").getStore().indexOf(me[0]);
				if(rowIndex==0){
					AOS.err("当前第一条!");
					return;
				}
				var s=Ext.getCmp("_g_ziliao").getStore().getAt(rowIndex-1);
				//原先行取消选中
				Ext.getCmp("_g_ziliao").getSelectionModel().deselect(rowIndex);
				//此时让光标选中上一行
				Ext.getCmp("_g_ziliao").getSelectionModel().select(rowIndex-1, true);
				//组件被显示后触发。
				Ext.getCmp("_f_ziliao_u").form.setValues(s.data);
			}
			//上一页
			function _f_previous_data2(){
				var count=Ext.getCmp("_g_ziliao").getStore().getCount();
				var me=Ext.getCmp("_g_ziliao").getSelectionModel().getSelection();
				//var record = AOS.selectone(_g_ziliao);
				//得到执行行的坐标
				var rowIndex = Ext.getCmp("_g_ziliao").getStore().indexOf(me[0]);
				if(rowIndex==0){
					AOS.err("当前第一条!");
					return;
				}
				var s=Ext.getCmp("_g_ziliao").getStore().getAt(rowIndex-1);
				//原先行取消选中
				Ext.getCmp("_g_ziliao").getSelectionModel().deselect(rowIndex);
				//此时让光标选中上一行
				Ext.getCmp("_g_ziliao").getSelectionModel().select(rowIndex-1, true);
				//组件被显示后触发。
				Ext.getCmp("_f_ziliao_u").form.setValues(s.data);
			}
			//下一页
			function _f_next_data2(){
				var count=Ext.getCmp("_g_ziliao").getStore().getCount();
				var me=Ext.getCmp("_g_ziliao").getSelectionModel().getSelection();
				//var record = AOS.selectone(_g_ziliao);
				//得到执行行的坐标
				var rowIndex = Ext.getCmp("_g_ziliao").getStore().indexOf(me[0]);
				if(rowIndex==count-1){
					AOS.err("当前最后一条!");
					return;
				}
				var s=Ext.getCmp("_g_ziliao").getStore().getAt(rowIndex+1);
				//原先行取消选中
				Ext.getCmp("_g_ziliao").getSelectionModel().deselect(rowIndex);
				//此时让光标选中下一行
				Ext.getCmp("_g_ziliao").getSelectionModel().select(rowIndex+1, true);
				//组件被显示后触发。
				Ext.getCmp("_f_ziliao_u").form.setValues(s.data);
			}
			//上一页
			function _f_previous_data3(){
				var count=Ext.getCmp("_g_data").getStore().getCount();
				var me=Ext.getCmp("_g_data").getSelectionModel().getSelection();
				//var record = AOS.selectone(_g_ziliao);
				//得到执行行的坐标
				var rowIndex = Ext.getCmp("_g_data").getStore().indexOf(me[0]);
				if(rowIndex==0){
					AOS.err("当前第一条!");
					return;
				}
				var s=Ext.getCmp("_g_data").getStore().getAt(rowIndex-1);
				//原先行取消选中
				Ext.getCmp("_g_data").getSelectionModel().deselect(rowIndex);
				//此时让光标选中上一行
				Ext.getCmp("_g_data").getSelectionModel().select(rowIndex-1, true);
				//组件被显示后触发。
				Ext.getCmp("_f_data_i").form.setValues(s.data);
			}
			//下一页
			function _f_next_data3(){
				var count=Ext.getCmp("_g_data").getStore().getCount();
				var me=Ext.getCmp("_g_data").getSelectionModel().getSelection();
				//var record = AOS.selectone(_g_ziliao);
				//得到执行行的坐标
				var rowIndex = Ext.getCmp("_g_data").getStore().indexOf(me[0]);
				if(rowIndex==count-1){
					AOS.err("当前最后一条!");
					return;
				}

				var s=Ext.getCmp("_g_data").getStore().getAt(rowIndex+1);
				//原先行取消选中
				Ext.getCmp("_g_data").getSelectionModel().deselect(rowIndex);
				//此时让光标选中下一行
				Ext.getCmp("_g_data").getSelectionModel().select(rowIndex+1, true);
				//组件被显示后触发。
				Ext.getCmp("_f_data_i").form.setValues(s.data);
			}

			//修改目录保存
			function _f_data_edit(){
				var record = AOS.selectone(_g_ziliao);
				var archive_record=AOS.selectone(_g_data);
				AOS.ajax({
					forms : _f_data_i,
					url : 'updateData.jhtml',
					params:{
						tablename : tablename.getValue(),
						pch:record.data.pch,
						id:archive_record.data.id_
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
				var record_from = AOS.selectone(_g_ziliao_from);
				var record = AOS.selectone(_g_ziliao);
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
							_g_ziliao_from_store.reload();
							AOS.tip(data.appmsg);
						}
					}
				});
			}
			//删除信息
			function _g_ziliao_del() {
				var selection = AOS.selection(_g_ziliao, 'id_');
				if (AOS.empty(selection)) {
					AOS.tip('删除前请先选中数据。');
					return;
				}
				var msg = AOS.merge('确认要删除选中的[{0}]个任务数据吗？', AOS.rows(_g_ziliao));
				AOS.confirm(msg, function (btn) {
					if (btn === 'cancel') {
						AOS.tip('删除操作被取消。');
						return;
					}
					AOS.ajax({
						url: 'deleteZiliao.jhtml',
						params: {
							aos_rows_: selection,
							tablename: "zlzj_n"
						},
						ok: function (data) {
							AOS.tip(data.appmsg);
							_g_ziliao_store.reload();
						}
					});
				});
			}

			function open_window(){
				var tablename=Ext.getCmp("tablename").getValue();
				var lx="";
				if(tablename=="zlbz_receive"){
					lx="BK";
				}
				if(tablename=="zlts_receive"){
					lx="TS";
				}
				if(tablename=="zlqf_receive"){
					lx="QK";
				}
				Ext.getCmp('_f_data_i').getForm().findField('damldm').setValue(lx);
			}
			//删除信息
			function _g_ziliao_from_del() {
				var selection = AOS.selection(_g_ziliao_from, 'id_');
				var tms = AOS.selection(_g_ziliao_from, 'tm');
				if (AOS.empty(selection)) {
					AOS.tip('删除前请先选中数据。');
					return;
				}
				var msg = AOS.merge('确认要删除选中的[{0}]个用户数据吗？', AOS.rows(_g_ziliao_from));
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
							_g_ziliao_from_store.reload();
						}
					});
				});
			}
			function _w_picture_show(){
				var record = AOS.selectone(_g_data);
				var row = AOS.selectone(_g_data);
				var uploadPanel= new Ext.ux.uploadPanel.UploadPanel({
					//addFileBtnText : '选择文件...',
					//uploadBtnText : '上传',
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
							var selection = AOS.selection(_g_ziliao, 'id_');
							var boolean;
							//判断当前用户是不是兼职管理员或者普通用户，并且在判断门类是不是文书档案，在判断是哪个部门的，匹配了就可以看电子文件。不然得借阅电子文件

							//此时直接看电子文件了
							parent.fnaddtab(row.data.id, '电子文件',
									'archive/data/openPdfFile.jhtml?id=' + row.data.pid + '&tid=' + row.data.tid + '&type=' + row.data.type + '&tablename=' + tablename.getValue());

						},
					},
					onUpload : function(){
						var me=Ext.getCmp("uploadpanel");
						//this.swfupload.addPostParam("ocr",Ext.getCmp("ocr").getValue());
						//this.swfupload.addPostParam("mark",Ext.getCmp("mark").getValue());
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
										tablename: tablename.getValue(),
										tid: tid,
										tm: record.data.tm
									}, // 提交参数,
									url: 'deletePath.jhtml',
									ok: function (data) {
										var me = Ext.getCmp("uploadpanel");
										//me.store.reload();
										me.store.remove(me.store.getAt(rowIndex));
										//me.store.load();
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
								var selection = AOS.selection(_g_ziliao,'id_');
								AOS.ajax({
									params: {
										aos_rows_: selection,
										tm: record.data.tm,
										tablename: tablename.getValue()
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
						var selection = AOS.selection(_g_ziliao, 'id_');
						var boolean;
						//判断当前用户是不是兼职管理员或者普通用户，并且在判断门类是不是文书档案，在判断是哪个部门的，匹配了就可以看电子文件。不然得借阅电子文件
						var me=Ext.getCmp("uploadpanel").getSelectionModel().getSelection();
						var id = me[0].get('pid');
						AOS.file('downloadPath.jhtml?id_='+id+'&tablename='+tablename.getValue());
						//得到选中的条目
					},
					upload_complete_handler : function(file){

						var me =Ext.getCmp("uploadpanel");

						AOS.ajax({
							params: {tid: record.data.id_,tablename:tablename.getValue()},
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
					upload_url : "${cxt}/archive/upload/archiveUpload.jhtml?tm="+record.data.tm+"&tablename="+tablename.getValue()

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

				var record = AOS.selectone(Ext.getCmp('_g_data'));
				var me=Ext.getCmp("uploadpanel");
				me.store.removeAll();
				AOS.ajax({
					params: {tid: record.data.id_,tablename:tablename.getValue()},
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
				_g_data_store.load();

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
						_g_ziliao_store.reload();
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
						_g_ziliao_from_store.reload();
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
				//我得加个重置
				var tablename=Ext.getCmp("tablename").getValue();
				var selection = AOS.selection(_g_ziliao, 'id_');
				if (AOS.empty(selection)) {
					AOS.tip('导入数据前请先选中任务!');
					return;
				}
				Ext.getCmp("_fileupload_add").show();
				$("#file").fileinput('destroy');
				$(document).ready(function() {
					$("#file").fileinput({
						language: 'zh', //设置语言
						uploadUrl: '${cxt}/archive/upload/uploadImport_excel.jhtml', //上传的地址
						uploadExtraData:{
							tablename: tablename,
							id_:AOS.selectone(_g_ziliao).data.id_
						},
						fileActionSettings:{
							showZoom:false//显示预览按钮
						},
						allowedFileExtensions: ['xls','xlsx','jpg', 'gif', 'png','pdf','tif'],//接收的文件后缀
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
			}
			function _import_data(){
				var record = AOS.selectone(_g_ziliao);
				var _classtree=Ext.getCmp("_classtree_f").getValue();
				var file123=$("#filename");
				var file=$("#filename").attr("title");
				var pch="";
				if(!AOS.empty(record)){
					pch=record.data.pch;
					window.parent.fnaddtab('417','数据导入','archive/datatemporary/initImport_temporary.jhtml?tablename='+tablename.getValue()+'&pch='+pch+'&_classtree='+_classtree+'&file='+file);
				}else{
					AOS.tip("请选择要导入数据的任务!");
				}
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

			//正式接收（实体接收直接有临时库到资源管理库）
			function _w_formal_receive(){
				var selection = AOS.selection(_g_ziliao, 'id_');
				if (AOS.empty(selection)) {
					AOS.tip('提交前请先选中任务!');
					return;
				}
				var msg = AOS.merge('确认要提交[{0}]个批次数据吗？', AOS.rows(_g_ziliao));
				AOS.confirm(msg, function (btn) {
					if (btn === 'cancel') {
						AOS.tip('提交操作被取消。');
						return;
					}
					var record = AOS.selectone(_g_ziliao);
					var flag_submit="";
					var flag_examine="";
					var zjbm="";
					var pch="";
					var nd="";
					if(!AOS.empty(record)){
						flag_submit=record.data.flag_submit;
						flag_examine=record.data.flag_examine;
						pch=record.data.pch;
						nd=record.data.nd;
					}
						var id_="";
						var tablename=Ext.getCmp("tablename").getValue();
						if(!AOS.empty(record)){
							id_=record.data.id_;
						}
						if(flag_submit=="已提交"&&flag_examine=="已通过"){
							var id_="";
							var tablename=Ext.getCmp("tablename").getValue();
							if(!AOS.empty(record)){
								id_=record.data.id_;
							}
							AOS.ajax({
								params:{id_:id_,tablename:tablename,pch:pch,nd:nd,rw_tablename:"zlzj_n",lx_bh:"pch"},
								url : 'receiveTodata.jhtml',
								ok : function(data) {
									AOS.tip(data.appmsg);
									_g_data_store.reload();
									_g_ziliao_store.reload();
								}
							});
						}else{
							AOS.tip('未提交或提交后未通过不能正式接收!');
							return;
						}
				});
			}

			function _f_pici_submit(){

				var params = AOS.getValue('_f_pici');
				var bmmc=Ext.getCmp('_f_pici').down("[name='bumenmc']").getRawValue();
				var bmbh=Ext.getCmp('_f_pici').down("[name='bumenmc']").getValue();
				AOS.ajax({
					url: 'addpici.jhtml',
					params:{treename:treename.getValue(),
						bmbh:bmbh,
						bmmc:bmmc,
						flag:flag.getValue(),
						tablename:tablename.getValue()
					},
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
			function _g_field_order_show(){
				_w_field_order.show();
			}
			//查询数据表列信息
			function _g_order_query() {
				//AOS.reset(_g_table);
				var params = {
					tablename : tablename.getValue()
				};
				_g_field_order_store.getProxy().extraParams = params;
				_g_field_order_store.load();
			}

			function _submit_order(){
				var store=Ext.getCmp("_g_field_order").getStore();
				var count=store.getCount();
				var zdid_="";
				var view="";
				for(var i=0;i<count;i++){
					zdid_+=store.getAt(i).data.id_+",";
					view+=store.getAt(i).data.FieldView+",";
				}
				//去掉最后一个逗号
				zdid_=zdid_.substring(0,zdid_.length-1);
				view=view.substring(0,view.length-1);
				AOS.ajax({
					params: {'zdid_': zdid_,'view':view,'tablename':tablename.getValue()},
					url: 'updatezdOrder.jhtml',
					ok: function (data) {
						if (data.appcode ===-1) {
							AOS.tip(data.appmsg);
						}else{
							//_w_order.hide();
							_w_field_order.hide();

							//_g_ziliao_store.load();
							location.reload();

							//_g_ziliao_query;
						}
					}
				});
			}
			function _w_order_show(){
				_w_order.show();
			}
			//升降序数据字典查询
			function load_order(){

				var params = {
					dicname:"sjx"
				};
				orderascdesccombobox_store.getProxy().extraParams = params;
				orderascdesccombobox_store.load();
				_g_field_list();
				_g_order_store.removeAll();
			}
			//移动字段选项，到右侧表框中
			function _g_remove_field(){
				//1.得到被选中的选矿行内容，
				var selection = AOS.selection(_g_field, 'id_');
				if (AOS.empty(selection)) {
					AOS.tip('移动前请先选中数据。');
					return;
				}
				//左侧下拉框选中的
				var leftenzd="";
				var leftcnzd="";
				var row=_g_field.getSelectionModel().getSelection();
				for(var i=0;i<AOS.rows(_g_field);i++){
					if(i==0){
						leftenzd=row[i].data.fieldenname;
						leftcnzd=row[i].data.fieldcnname;
					}else{
						leftenzd+=","+row[i].data.fieldenname;
						leftcnzd+=","+row[i].data.fieldcnname;
					}

				}
				//右侧下拉框已经存在的
				var rightenzd="";
				var rightcnzd="";
				//升降序文字
				var orderascdesc="";
				//升降序数值
				var orderascing="";
				//数值排列
				var ordermath="";
				//从grid中获取之前的行列数据传递给后台
				var count=_g_order_store.getCount();
				if(count>0){
					for(var i=0;i<count;i++){
						if(i==0){
							rightenzd=_g_order_store.getAt(i).get("orderenname");
							rightcnzd=_g_order_store.getAt(i).get("ordercnname");
							orderascdesc=_g_order_store.getAt(i).get("orderascdesc");
							orderascing=_g_order_store.getAt(i).get("orderascing");
							ordermath=_g_order_store.getAt(i).get("ordermath");
						}else{
							rightenzd+=","+_g_order_store.getAt(i).get("orderenname");
							rightcnzd+=","+_g_order_store.getAt(i).get("ordercnname");
							orderascdesc+=","+_g_order_store.getAt(i).get("orderascdesc");
							orderascing+=","+_g_order_store.getAt(i).get("orderascing");
							ordermath+=","+_g_order_store.getAt(i).get("ordermath");
						}
					}
				}
				var params = {
					rightenzd:rightenzd,
					rightcnzd:rightcnzd,
					orderascdesc:orderascdesc,
					orderascing:orderascing,
					ordermath:ordermath,
					leftcnzd:leftcnzd,
					leftenzd:leftenzd
				};
				_g_order_store.getProxy().extraParams = params;
				_g_order_store.reload();
				//走后台，
				_g_field_store.remove(row);
			}
			function fn_order_edit(editor, e){
				var selectValue = Ext.getCmp("orderascdesccombobox").displayTplData[0].orderascenname;
				_g_order_store.getAt(e.rowIdx).set('orderascenname',selectValue);
				_g_order_store.getAt(e.rowIdx).set('orderascing',selectValue);
				e.record.commit();
			}
			//删除右侧的添加排序条件
			function _g_delete_field(){
				var selection = AOS.selection(_g_order, 'id_');
				if (AOS.empty(selection)) {
					AOS.tip('删除前请先选中数据。');
					return;
				}
				var row=_g_order.getSelectionModel().getSelection();
				var orenzd=row[0].data.orderenname;
				var orcnzd=row[0].data.ordercnname;
				//右侧删除，左侧添加
				_g_order_store.remove(row);
				//左侧添加
				var data=[{
					'fieldenname':orenzd,
					'fieldcnname':orcnzd
				}];
				_g_field_store.loadData(data,true);

			}
			//上移
			function _g_up_field(){
				var row = _g_order.getSelectionModel().getSelection();//获得选中的项
				var rowid = _g_order_store.indexOf(row[0]);//获得选中的第一项在store内的行号
				//第一行无法上移
				if(rowid==0){
					return;
				}
				//此时删除严肃，在他的上一行添加
				_g_order_store.removeAt(rowid);
				_g_order_store.insert(rowid-1,row);
			}
			//下移
			function _g_down_field(){
				var count=_g_order_store.getCount();
				var row = _g_order.getSelectionModel().getSelection();//获得选中的项
				var rowid = _g_order_store.indexOf(row[0]);//获得选中的第一项在store内的行号
				//最有一行无法下移
				if(rowid==(count-1)){
					return;
				}
				//此时删除严肃，在他的上一行添加
				_g_order_store.removeAt(rowid);
				_g_order_store.insert(rowid+1,row);
			}
			function _g_field_list(){
				var params = {
					tablename : tablename.getValue()
				};
				//这个Store的命名规则为：表格ID+"_store"。
				_g_field_store.getProxy().extraParams = params;
				_g_field_store.load();
			}
			//排列条目数据
			function order_by_(){
				var s=Ext.getCmp('_g_order').getStore();
				var myArray="";
				for(var i = 0 ;i< s.getCount(); i++){
					var orderenname=s.getAt(i).get('orderenname');
					var ordercnname=s.getAt(i).get('ordercnname');
					var value = s.getAt(i).get('orderascing')
					var ordermath=s.getAt(i).get('ordermath');
					//存放到数组里面
					if(ordermath==""){
						ordermath="false";
					}
					myArray+=orderenname+":"+ordercnname+":"+value+":"+ordermath+";";
				}
				//去掉最后一个字符
				myArray=myArray.substring(0,myArray.length-1);
				//传递后台
				AOS.ajax({
					url : 'toOrder_archive.jhtml',
					params:{
						tablename : tablename.getValue(),
						orders:myArray
					},
					ok : function(data) {
						//此时判断是否存入数据库成功
						if(data.appcode===1){
							//关闭当前窗口，刷新后台页面
							_w_order.hide();
							_g_ziliao_store.load();
						}else if(data.appcode===0){
							AOS.tip("排列失败!");
						}
					}
				});
			}
			//四性检测列转换
			function fn_test_render(value, metaData, record, rowIndex, colIndex,
									store){
				//1无问题2有问题 其他未检测
				if (value == 1) {
					return '<img src="${cxt}/static/icon/security.png"  />';
				}if (value == 2) {
					return '<img src="${cxt}/static/icon/del2.png" onclick="_fn_peril();" />';
				} else {
					return '<img src="${cxt}/static/icon/help.png"/>';
				}
			}
			function _w_test_onshow(){

				var record = AOS.selectone(_g_ziliao,true);
				_f_test.loadRecord(record);

			}
			//导入电子文件跳转页面
			function _w_import_path_show(){
				var selection = AOS.selection(_g_ziliao,'id_');
				if(AOS.empty(selection)){
					AOS.tip('导入电子文件前请选中任务。。。');
					return;
				}
				window.location.href="Webcall://";
			}
			function _fn_safetest(){
				var selection = AOS.selection(_g_receive,'pch');
				var user = "<%=session.getAttribute("user")%>";
				if(AOS.empty(selection)){
					AOS.tip('四性检测前请选中任务。。。');
					return;
				}
				var pch=AOS.selectone(_g_receive).data.pch;
				AOS.ajax({
					url:'safeTest.jhtml',
					wmsg:('正在进行四性检测。。。'),
					params:{
						user:user,
						pch:pch
					},
					ok:function (data) {
						AOS.tip(data.appmsg);
						_g_ziliao_store.reload();
					}
				});
			}
			function fn_button_render(){
				return '<input type="button" value="详情" class="cbtn" onclick="_w_details_show(this);" />';
			}


			function _w_sxjc_onshow(){
				var user = "<%=session.getAttribute("user")%>";
				//从后台查询当前用户的四性检测
				AOS.ajax({
					params:{user:user},
					url: 'getsxjc_properties.jhtml',
					ok: function (data){
						if(data==""||data==null){
							radio_check("不需要","dh_equals");
							radio_check("不需要","file_boolean");
							radio_check("不需要","nd");
							radio_check("不需要","jh");
							radio_check("不需要","qzh");
						}else{
							Ext.getCmp('_f_sxjc_window').getForm().findField('id_').setValue(data.id_);
							radio_check(data.dh_equals,"dh_equals");
							radio_check(data.file_boolean,"file_boolean");
							radio_check(data.nd,"nd_");
							radio_check(data.jh,"jh_");
							radio_check(data.qzh,"qzh_");
						}
					}
				});
			}
			//同意接受
			function _save_next_kf_message(){
				var record = AOS.selectone(_g_ziliao);
				var id_="";
				if(!AOS.empty(record)){
					id_=record.data.id_;
				}
				var _classtree_f=Ext.getCmp("_classtree_f").getValue();
				var tablename=Ext.getCmp("tablename").getValue();
				var aos_module_id_=Ext.getCmp("aos_module_id_").getValue();
				AOS.ajax({
					forms : _f_next_kf_message,
					params:{
						aos_module_id_:aos_module_id_,
						tablename:tablename,
						lx:${lx},
						zjnd:record.data.zjnd,
						user:record.data.cjr,
						cascode_id_:_classtree_f,
						'id_':id_
					},
					url : 'updatezhengjiyes.jhtml',
					ok : function(data) {
						_w_next_kf_message.hide();
						AOS.tip(data.appmsg);
						_g_ziliao_store.reload();
					}
				});
			}
			//拒绝接受
			function no_save_next_kf_message(){
				var record = AOS.selectone(_g_ziliao);
				var id_="";
				if(!AOS.empty(record)){
					id_=record.data.id_;
				}
				var _classtree_f=Ext.getCmp("_classtree_f").getValue();
				var tablename=Ext.getCmp("tablename").getValue();
				var aos_module_id_=Ext.getCmp("aos_module_id_").getValue();
				AOS.ajax({
					forms : _f_next_kf_message,
					params:{
						aos_module_id_:aos_module_id_,
						tablename:tablename,
						lx:${lx},
						zjnd:record.data.zjnd,
						cascode_id_:_classtree_f,
						user:record.data.cjr,
						'id_':id_
					},
					url : 'updatezhengjino.jhtml',
					ok : function(data) {
						_w_next_kf_message.hide();
						AOS.tip(data.appmsg);
						_g_ziliao_store.reload();
					}
				});
			}
			function kkk(sxjcleixing){
				var flag="";
				var equRad=Ext.getCmp(sxjcleixing).getRefItems();
				for (var i=0;i<equRad.length;i++) {
					if (equRad[i].checked) {
						flag=equRad[i].boxLabel;
						break;
					}
				}
				return flag;
			}
			function radio_check(sxjc,sxjcleixing){

				var equRad=Ext.getCmp(sxjcleixing).getRefItems();
				for(var b=0;b<equRad.length;b++){
					if(equRad[b].boxLabel===sxjc){
						equRad[b].checked=true; //设置选中取值的修改
						equRad[b].setValue(true);//设置显示页面是否选中
					}else{
						equRad[b].checked=false;
						equRad[b].setValue(false);
					}
				}
			}
			//四性配置
			function _fn_sxjc_properties(){
				//先弹出一个窗口选项
				//_w_sxjc_window.show();
				var selection = AOS.selection(_g_ziliao,'pch');
				if(AOS.empty(selection)){
					AOS.tip('四性检测前请选中任务。。。');
					return;
				}
				_w_sxjc_properties.show();
			}
			//保存四性检测配置
			function save_sxjc_properties(){
				var user = "<%=session.getAttribute("user")%>";
				var dh_equals=kkk("dh_equals");
				var file_boolean=kkk("file_boolean");
				var nd=kkk("nd_");
				var jh=kkk("jh_");
				var qzh=kkk("qzh_");
				var id_=Ext.getCmp('_f_sxjc_window').getForm().findField('id_').getValue();
				//从后台查询当前用户的四性检测
				AOS.ajax({
					params:{
						id_:id_,
						user:user,
						dh_equals:dh_equals,
						file_boolean:file_boolean,
						nd:nd,
						jh:jh,
						qzh:qzh
					},
					url: 'savesxjc_properties.jhtml',
					ok: function (data){
						if (data.appcode == 1) {
							AOS.tip("保存成功!");
							_w_sxjc_window.hide();
						} else {
							AOS.tip("保存失败!");
						}
					}
				});
			}
			function load_sxjc_properties(){
				_g_sxjc_properties_store.reload();
			}
			//执行四性检测操作
			function go_sxjc_properties(){
				var selection = AOS.selection(_g_sxjc_properties,'code_');
				var tablename=Ext.getCmp("tablename").getValue();
				var pch=AOS.selectone(_g_ziliao).data.pch;
				AOS.ajax({
					params:{
						aos_rows_:selection,
						pch:pch,
						tablename:tablename
					},
					url: 'operator_sxjc.jhtml',
					ok: function (data){
						AOS.tip(data.appmsg);
						_g_ziliao_store.reload();
						_w_sxjc_properties.hide();
					}
				});
			}
			function _w_sxjc_report(){
				var selection = AOS.selection(_g_ziliao,'pch');
				if(AOS.empty(selection)){
					AOS.tip('检测报告前请选中任务。。。');
					return;
				}
				var pch=AOS.selectone(_g_ziliao).data.pch;
				var tablename=Ext.getCmp("tablename").getValue();
				parent.fnaddtab('123', '检测报告',
						'archive/datatemporary/sxjc_report.jhtml?pch='+pch+'&tablename='+tablename);
			}
			function _w_jccw_report(){
				var selection = AOS.selection(_g_ziliao,'pch');
				if(AOS.empty(selection)){
					AOS.tip('查看错误前请选中任务。。。');
					return;
				}
				var pch=AOS.selectone(_g_ziliao).data.pch;
				var sxjczt=2;
				var cascode_id_=Ext.getCmp("cascode_id_").getValue();
				var tablename=Ext.getCmp("tablename").getValue();
				parent.fnaddtab('456', '错误数据',
						'archive/datatemporary/sxjc_error.jhtml?pch='+pch+'&tablename='+tablename+'&sxjczt='+sxjczt+'&cascode_id_='+cascode_id_);
			}
			function _w_receive_report(){
				var selection = AOS.selection(_g_ziliao,'pch');
				if(AOS.empty(selection)){
					AOS.tip('接收报告前请选中任务。。。');
					return;
				}
				var pch=AOS.selectone(_g_ziliao).data.pch;
				var tablename=Ext.getCmp("tablename").getValue();
				parent.fnaddtab('456', '接收报告',
						'archive/datatemporary/receive_report.jhtml?pch='+pch+'&tablename='+tablename);
			}

		</script>
	</aos:onready>
	<script type="text/javascript">
		function _w_details_show(a){
			var record=Ext.getCmp('_g_ziliao').getSelectionModel().getSelection();
			var id_=record[0].data.id_;
			var tablename=Ext.getCmp("tablename").getValue();
			AOS.ajax({
				params: {id_: id_,
					tablename:tablename
				},
				url: 'getData.jhtml',
				ok: function (data) {
					Ext.getCmp("_f_data_i").form.setValues(data);
					Ext.getCmp("_w_data_i").show();
				}
			});
		}
		function _w_path_show(e){
			var record = Ext.getCmp('_g_ziliao').getSelectionModel().getSelection();
			var tm= record[0].data.tm;
			var id_= record[0].data.id_;
			var uploadPanel2= new Ext.ux.uploadPanel.UploadPanel({
				//addFileBtnText : '选择文件...',
				//uploadBtnText : '上传',
				deleteBtnText : '移除',
				removeBtnText : '移除所有',
				cancelBtnText : '取消上传',
				use_query_string : true,
				listeners: {
					itemdblclick: function (grid, row) {
						//parent.fnaddtab(row.data.id, '电子文件',
						//					'archive/data/openFile.jhtml?id='+row.data.pid+'&tid='+row.data.tid+'&type='+row.data.type+'&tablename='+tablename.getValue());
						//此时根据当前用户走后台判断是否可以看电子文件
						//得到当前用户,判断是不是档案室和admin用户
						var user = "<%=session.getAttribute("user")%>";
						var selection = AOS.selection(_g_ziliao, 'id_');
						var boolean;
						//判断当前用户是不是兼职管理员或者普通用户，并且在判断门类是不是文书档案，在判断是哪个部门的，匹配了就可以看电子文件。不然得借阅电子文件
						AOS.ajax({
							url: 'getdepartment.jhtml',
							params: {user: user, tablename: Ext.getCmp('tablename').getValue(), ids_: selection},
							method: 'post',
							ok: function (data) {
								if (data.appcode == 1) {
									//此时直接看电子文件了
									parent.fnaddtab(row.data.id, '电子文件',
											'archive/data/openPdfFile.jhtml?id=' + row.data.pid + '&tid=' + row.data.tid + '&type=' + row.data.type + '&tablename=' + Ext.getCmp('tablename').getValue());

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
														'archive/data/openSwfFile.jhtml?id=' + row.data.pid + '&tid=' + row.data.tid + '&type=' + row.data.type + '&tablename=' + Ext.getCmp('tablename').getValue());
											}
										}
									});
								}
							}
						});
					},
				},
				onUpload : function(){
					var me=Ext.getCmp("uploadpanel2");
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
							var me = Ext.getCmp("uploadpanel2").getSelectionModel().getSelection();
							var id = me[0].get('pid');
							var tid = me[0].get('tid');

							var rowIndex = Ext.getCmp("uploadpanel2").getStore().indexOf(me[0]);
							AOS.ajax({
								params: {
									id_: id,
									tablename: Ext.getCmp('tablename').getValue(),
									tid: tid,
									tm: tm
								}, // 提交参数,
								url: 'deletePath.jhtml',
								ok: function (data) {
									var me = Ext.getCmp("uploadpanel2");
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
							var selection = AOS.selection(_g_ziliao, 'id_');
							AOS.ajax({
								params: {
									aos_rows_: selection,
									tm: tm,
									tablename: Ext.getCmp('tablename').getValue()
								},
								url: 'deletePathAll.jhtml',
								ok: function (data) {
									var me = Ext.getCmp("uploadpanel2");
									me.removeAll();
									AOS.tip(data.appmsg);
								}
							});
						}
					});
				},
				//下载
				onDownPath:function(grid, rowIndex, colIndex){

				},

				upload_complete_handler : function(file){

					var me =Ext.getCmp("uploadpanel2");

					AOS.ajax({
						params: {tid: id_,tablename:Ext.getCmp('tablename').getValue()},
						url: 'getPath.jhtml',
						ok: function (data) {
							for(i in data){
								me.store.getAt(file.index).set({"pid":data[i].id_,"tid":data[i].tid});
							}
						}
					});
				},

				post_params:{tid:id_,
					tablename:Ext.getCmp('tablename').getValue()
				},
				file_size_limit : 10000,//MB
				flash_url : "${cxt}/static/swfupload/swfupload.swf",
				flash9_url : "${cxt}/static/swfupload/swfupload_f9.swf",
				upload_url : "${cxt}/archive/upload/archiveUpload.jhtml?tm="+tm+"&tablename="+Ext.getCmp('tablename').getValue()

			});

			var w_data_path = new Ext.Window({
				title : '电子文件',
				width : 700,
				modal:true,
				closeAction : 'destroy',
				items:[uploadPanel2]
			});
			w_data_path.on("show",w_data_path_onshow);
			w_data_path.on("close",w_data_path_onclose);
			w_data_path.show();
		}
		function w_data_path_onshow() {
			//var me = this.settings.custom_settings.scope_handler;

			var record = AOS.selectone(Ext.getCmp('_g_ziliao'));
			var me=Ext.getCmp("uploadpanel2");
			me.store.removeAll();
			AOS.ajax({
				params: {tid: record.data.id_,tablename:Ext.getCmp("tablename").getValue()},
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
			Ext.getCmp("_g_ziliao").getStore().load();

		}

		function _fn_peril(){
			Ext.getCmp('_w_test').show();
		}

	</script>
</aos:html>
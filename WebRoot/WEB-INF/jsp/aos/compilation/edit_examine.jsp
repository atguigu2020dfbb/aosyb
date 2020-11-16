<%@ page contentType="text/html; charset=utf-8" isELIgnored="false"%>
<%@ taglib uri="/WEB-INF/tld/aos.tld" prefix="aos"%>
<%@ taglib uri="/WEB-INF/tld/cll.tld" prefix="c"%>
<%
	String path = request.getContextPath();
	Object fieldDtos = request.getAttribute("fieldDtos");
	Object listtablename = request.getAttribute("listtablename");
%>
<aos:html>
	<aos:head title="编研审核">
		<aos:include lib="ext" />
		<aos:base href="compilation/examine" />
	</aos:head>
	<aos:body>
	</aos:body>
	<aos:onready>
		<aos:viewport layout="fit">
			<aos:gridpanel id="_g_data" region="center" url="listCompilation_examine.jhtml" onrender="_w_next_trial_show"
						   autoScroll="true" pageSize="20" enableLocking="true">
				<aos:docked forceBoder="0 0 1 0">
					<aos:dockeditem xtype="tbtext" text="专题与编研" />
					<aos:hiddenfield name="cascode_id_" id="cascode_id_" value="${cascode_id_}"/>
					<aos:hiddenfield name="sjkmc" id="sjkmc" value="${sjkmc}"/>
					<aos:hiddenfield name="ztmc" id="ztmc" value="${ztmc}"/>
					<aos:hiddenfield name="byrwmc" id="byrwmc" value="${byrwmc}"/>
					<aos:hiddenfield name="byrwbh" id="byrwbh" value="${byrwbh}"/>
					<aos:hiddenfield name="query" id="query" />
					<!--当前查询记录数-->
					<aos:hiddenfield name="_total" id="_total" />
					<aos:dockeditem text="撰稿" icon="edit.png" id="_zhuangao_operator"
									onclick="_check_zhuangao" />
					<aos:dockeditem text="合稿" icon="more/arrow-merge.png" id="_first_operator"
									onclick="_check_first" />
					<aos:dockeditem text="校对" icon="more/checkbox-2.png" id="_next_operator"
									onclick="_check_next" />
					<aos:dockeditem text="总编辑" icon="more/computer-edit.png" id="_last_operator"
									onclick="_check_last" />
				</aos:docked>
				<aos:column type="rowno" />
				<aos:column dataIndex="id_" hidden="true"/>
				<aos:column dataIndex="byrwbh" header="编研任务编号" width="150"/>
				<aos:column dataIndex="byrwmc" header="编研任务名称" />
				<aos:column dataIndex="sjkmc" header="数据库名称" />
				<aos:column dataIndex="ztmc" header="专题名称" />
				<aos:column dataIndex="ztyq" header="总体要求" />
				<aos:column dataIndex="bysj" header="编研时间" />
				<aos:column dataIndex="ckfw" header="参考范围"  />
				<aos:column dataIndex="bybz" header="编研步骤"  />
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
				<aos:column header="" flex="1" />
			</aos:gridpanel>
			<aos:window id="_w_bloom" title="编研设定" width="500"
						autoScroll="true" onshow="_load_bloom">
				<aos:formpanel id="_f_bloom" width="480" layout="column">
					<aos:textfield fieldLabel="任务编号" name="task_number" columnWidth="1.0"
								   readOnly="true" />
					<aos:textfield fieldLabel="任务名称" name="task_name" columnWidth="1.0" allowBlank="false"/>
					<aos:datefield name="datatime" fieldLabel="时间" format="Y-m-d"
								   columnWidth="1.0" allowBlank="false" />
					<aos:hiddenfield fieldLabel="编研名称" name="tablename"/>
					<aos:textfield fieldLabel="编研范围" name="tabledesc" columnWidth="1.0"
								   readOnly="true" />
					<aos:textfield fieldLabel="创建人" name="createperson" columnWidth="1.0" readOnly="true"/>
					<aos:textareafield  name="description" allowBlank="false"  columnWidth="1.0" fieldLabel="编研描述"/>
				</aos:formpanel>
				<aos:docked dock="bottom" ui="footer">
					<aos:dockeditem xtype="tbfill" />
					<aos:dockeditem text="确定" onclick="_w_bloom_load" icon="ok.png" />
					<aos:dockeditem onclick="#_w_bloom.hide();" text="关闭"
									icon="close.png" />
				</aos:docked>
			</aos:window>



			<aos:window id="_w_query_q" title="查询" width="720" autoScroll="true"
						layout="fit">
				<aos:tabpanel id="_tabpanel" region="center" activeTab="0"
							  bodyBorder="0 0 0 0" tabBarHeight="30">
					<aos:tab title="列表式搜索" id="_tab_org">
						<aos:formpanel id="_f_query" layout="column" columnWidth="1">
							<aos:hiddenfield name="listtablename" value="${listtablename }" />
							<aos:hiddenfield name="columnnum" id="columnnum" value="7" />
							<c:forEach var="fieldss" items="${fieldDtos}" end="7"
									   varStatus="listSearch">
								<aos:combobox name="and${listSearch.count }" value="on"
											  labelWidth="10" columnWidth="0.2">
									<aos:option value="on" display="并且" />
									<aos:option value="up" display="或者" />
								</aos:combobox>
								<aos:combobox name="filedname${listSearch.count}"
											  emptyText="${fieldss.fieldcnname }" labelWidth="20"
											  columnWidth="0.2" fields="['fieldenname','fieldcnname']"
											  regexText="${fieldss.fieldenname }" displayField="fieldcnname"
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
				</aos:tabpanel>
			</aos:window>

			<aos:window id="_w_operation" title="操作记录" autoScroll="true" height="500" width="800" onshow="_w_operation_show">
				<aos:gridpanel id="_g_operation"  url="listoperationlogin.jhtml"
							   hidePagebar="true"  width="5000" autoScroll="true">
					<aos:docked>
						<aos:dockeditem xtype="tbtext" text="列表" />
						<aos:dockeditem text="详情" icon="detail.png"
										onclick="_w_examine_show" />
					</aos:docked>
					<aos:column type="rowno" />
					<aos:column header="流水号" dataIndex="id_" hidden="true" />
					<aos:column header="tid" dataIndex="tid" hidden="true" />
					<aos:column header="撰稿人" dataIndex="users" width="200" />
					<aos:column header="操作时间" dataIndex="operate_time" width="200" />
					<aos:column header="操作描述" dataIndex="operate_description" width="200" />
					<aos:column header="操作信息" dataIndex="compilation_message" width="200" />
					<aos:column header="" flex="1" />
				</aos:gridpanel>
				<aos:docked dock="bottom" ui="footer">
					<aos:dockeditem xtype="tbfill" />
					<aos:dockeditem onclick="#_w_operation.close();" text="关闭" icon="close.png" />
				</aos:docked>
			</aos:window>

			<aos:window id="_write_list4_show" title="详情" onshow="open_write_list4_show">
				<aos:formpanel id="_g_write_list4_show" width="500" layout="anchor"
							   labelWidth="90">
					<aos:hiddenfield name="id_"  />
					<aos:textfield fieldLabel="编研任务编号" name="task_number" columnWidth="1.0"  readOnly="true"/>
					<aos:textfield fieldLabel="编研任务名称" name="task_name" columnWidth="1.0"  readOnly="true"/>
					<aos:textfield fieldLabel="总体要求" name="general" columnWidth="1.0"  readOnly="true"/>
					<aos:textfield fieldLabel="编研时间" name="task_time" columnWidth="1.0" />
					<aos:textfield fieldLabel="参考范围" name="reference" columnWidth="1.0" />
					<aos:textfield fieldLabel="编研步骤" name="compilationResearch" columnWidth="1.0" />
					<aos:textfield fieldLabel="参与人员" name="personnel" columnWidth="1.0"/>
					<aos:textareafield fieldLabel="其他说明" name="otherdescription" columnWidth="1.0"/>
					<aos:textareafield fieldLabel="校对人反馈信息" name="next_message" columnWidth="1.0"/>
				</aos:formpanel>
				<aos:docked dock="bottom" ui="footer">
					<aos:dockeditem xtype="tbfill" />
					<aos:dockeditem onclick="first_trial_examine_show" text="合稿" icon="more/arrow-merge.png" />
					<aos:dockeditem onclick="#_write_list4_show.hide();" text="关闭" icon="close.png" />
				</aos:docked>
			</aos:window>


			<aos:window id="_write_list_show" title="详情">
				<aos:formpanel id="_g_write_list_show" width="500" layout="anchor"
							   labelWidth="90">
					<aos:hiddenfield name="id_"  />
					<aos:textfield fieldLabel="任务名称" name="task_name" columnWidth="1.0" allowBlank="false" readOnly="true"/>
					<aos:textfield fieldLabel="撰稿编号" name="write_number" columnWidth="1.0" allowBlank="false" readOnly="true"/>
					<aos:textfield fieldLabel="撰稿名称" name="write_name" columnWidth="1.0" allowBlank="false" readOnly="true"/>
					<aos:textfield fieldLabel="撰稿时间" name="write_datatime" columnWidth="1.0" allowBlank="false"/>
					<aos:textfield fieldLabel="撰稿档案名称" name="tablename" columnWidth="1.0" allowBlank="false"/>
					<aos:textfield fieldLabel="撰稿创建人" name="write_createperson" columnWidth="1.0" allowBlank="false"/>
					<aos:textfield fieldLabel="撰稿描述" name="write_description" columnWidth="1.0" allowBlank="false"/>
				</aos:formpanel>
				<aos:docked dock="bottom" ui="footer">
					<aos:dockeditem xtype="tbfill" />
					<aos:dockeditem onclick="#_write_list_show.hide();" text="关闭" icon="close.png" />
				</aos:docked>
			</aos:window>
			<aos:window id="_write_list2_show" title="详情">
				<aos:formpanel id="_g_write_list2_show" width="500" layout="column"
							   labelWidth="90">
					<aos:hiddenfield name="id_"  />
					<aos:textfield fieldLabel="任务名称" name="task_name" columnWidth="1.0" allowBlank="false" readOnly="true"/>
					<aos:textfield fieldLabel="撰稿编号" name="write_number" columnWidth="1.0" allowBlank="false" readOnly="true"/>
					<aos:textfield fieldLabel="撰稿名称" name="write_name" columnWidth="1.0" allowBlank="false" readOnly="true"/>
					<aos:textfield fieldLabel="撰稿时间" name="write_datatime" columnWidth="1.0" allowBlank="false"/>
					<aos:textfield fieldLabel="撰稿档案名称" name="tablename" columnWidth="0.8" readOnly="true"/>
					<aos:button text="撰稿" onclick="write_archive_details" margin="0 0 0 10"  columnWidth="0.2"/>

					<aos:textfield fieldLabel="撰稿创建人" name="write_createperson" columnWidth="1.0" allowBlank="false"/>
					<aos:textfield fieldLabel="撰稿描述" name="write_description" columnWidth="1.0" allowBlank="false"/>
				</aos:formpanel>
				<aos:docked dock="bottom" ui="footer">
					<aos:dockeditem xtype="tbfill" />
					<aos:dockeditem onclick="#_write_list2_show.hide();" text="关闭" icon="close.png" />
				</aos:docked>
			</aos:window>
			<aos:window id="_w_write_list" title="方案列表" autoScroll="true" height="500" width="800" onshow="_w_write_list_show">
				<aos:gridpanel id="_g_write_list" region="east" url="listwrite.jhtml"
							   hidePagebar="true" split="true" splitterBorder="0 1 0 1" width="1000" autoScroll="true">
					<aos:docked>
						<aos:dockeditem xtype="tbtext" text="列表" />
						<aos:dockeditem onclick="write_list_show" text="方案详情" icon="ok.png" />
						<aos:dockeditem onclick="archive_write_list" text="档案详情" icon="query.png" />
						<aos:dockeditem onclick="archive_compilation_articles" text="档案编研详情" icon="query.png" />
					</aos:docked>
					<aos:column type="rowno" />
					<aos:column header="流水号" dataIndex="id_" hidden="true" />
					<aos:column  dataIndex="write_flag" hidden="true" />
					<aos:column  dataIndex="first_flag" hidden="true" />
					<aos:column  dataIndex="next_flag" hidden="true" />
					<aos:column  dataIndex="last_flag" hidden="true" />
					<aos:column header="任务名称" dataIndex="task_name"  width="80" align="true"/>
					<aos:column header="撰稿编号" dataIndex="write_number"  width="100" align="true"/>
					<aos:column header="撰稿名称" dataIndex="write_name"  width="100" align="true"/>
					<aos:column header="撰稿时间" dataIndex="write_datatime" width="100" align="true" />
					<aos:column header="撰稿档案名称" dataIndex="tablename" width="60" align="true" hidden="true"/>
					<aos:column header="撰稿创建人" dataIndex="write_createperson"  width="80" align="true" />
					<aos:column header="撰稿描述" dataIndex="write_description"  width="80" align="true" />

				</aos:gridpanel>
				<aos:docked dock="bottom" ui="footer">
					<aos:dockeditem xtype="tbfill" />
					<aos:dockeditem onclick="#_w_write_list.close();" text="关闭" icon="close.png" />
				</aos:docked>
			</aos:window>
			<aos:window id="_w_write_list2" title="方案列表" autoScroll="true" height="500" width="800" onshow="_w_write_list2_show">
				<aos:gridpanel id="_g_write_list2" region="east" url="listwrite2.jhtml"
							   hidePagebar="true" split="true" splitterBorder="0 1 0 1" width="1000" autoScroll="true">
					<aos:docked>
						<aos:dockeditem xtype="tbtext" text="列表" />
						<aos:dockeditem onclick="write_list2_show" text="方案详情" icon="ok.png" />
					</aos:docked>
					<aos:column type="rowno" />
					<aos:column header="流水号" dataIndex="id_" hidden="true" />
					<aos:column  dataIndex="write_flag" hidden="true" />
					<aos:column  dataIndex="first_flag" hidden="true" />
					<aos:column  dataIndex="next_flag" hidden="true" />
					<aos:column  dataIndex="last_flag" hidden="true" />
					<aos:column header="任务名称" dataIndex="task_name"  width="80" align="true"/>
					<aos:column header="撰稿编号" dataIndex="write_number"  width="100" align="true"/>
					<aos:column header="撰稿名称" dataIndex="write_name"  width="100" align="true"/>
					<aos:column header="撰稿时间" dataIndex="write_datatime" width="100" align="true" />
					<aos:column header="撰稿档案名称" dataIndex="tablename" width="60" align="true" hidden="true"/>
					<aos:column header="撰稿创建人" dataIndex="write_createperson"  width="80" align="true" />
					<aos:column header="撰稿描述" dataIndex="write_description"  width="80" align="true" />

				</aos:gridpanel>
				<aos:docked dock="bottom" ui="footer">
					<aos:dockeditem xtype="tbfill" />
					<aos:dockeditem onclick="#_w_write_list2.close();" text="关闭" icon="close.png" />
				</aos:docked>
			</aos:window>
			<aos:window id="_bloom_first_check" title="开放编研" width="500"
						autoScroll="true" onshow="_load_bloom_first_check">
				<aos:formpanel id="_f_bloom_first_check" width="480" layout="column">
					<aos:hiddenfield  name="id_" />
					<aos:textfield fieldLabel="数据表名称" name="tablename" columnWidth="1.0"
								   readOnly="true" />
					<aos:textfield fieldLabel="数据表描述" name="tabledesc" columnWidth="1.0"
								   readOnly="true" />
					<aos:combobox name="check_type"  fieldLabel="编研类型" value="开放编研"
								  columnWidth="1.0" readOnly="true">
						<aos:option value="开放编研" display="开放编研" />
						<aos:option value="保管期限编研" display="保管期限编研" />
						<aos:option value="密级编研" display="密级编研" />
						<aos:option value="销毁编研" display="销毁编研" />
					</aos:combobox>
					<aos:datefield name="datatime" fieldLabel="日期" format="Y-m-d"
								   columnWidth="1.0" allowBlank="false" />
					<aos:textfield fieldLabel="操作人" name="person" columnWidth="1.0" readOnly="true"/>
					<aos:textfield fieldLabel="初审人" name="first_compilation" columnWidth="1.0"
								   readOnly="true" />
					<aos:textareafield  name="description" allowBlank="false"  columnWidth="1.0" fieldLabel="编研描述" readOnly="true"/>
					<aos:radioboxgroup fieldLabel="是否开放" id="_first_bloom_radio"  columns="[80,80]">
						<aos:radiobox name="kaifang" boxLabel="开放" checked="true"/>
						<aos:radiobox name="kaifang" boxLabel="不开放" />
					</aos:radioboxgroup>
				</aos:formpanel>
				<aos:docked dock="bottom" ui="footer">
					<aos:dockeditem xtype="tbfill" />
					<aos:dockeditem text="确定" onclick="_bloom_first_check_load" icon="ok.png" />
					<aos:dockeditem onclick="#_bloom_first_check.hide();" text="关闭"
									icon="close.png" />
				</aos:docked>
			</aos:window>
			<aos:window id="_bloom_next_check" title="开放编研" width="500"
						autoScroll="true" onshow="_load_bloom_next_check">
				<aos:formpanel id="_f_bloom_next_check" width="480" layout="column">
					<aos:hiddenfield  name="id_" />
					<aos:textfield fieldLabel="数据表名称" name="tablename" columnWidth="1.0"
								   readOnly="true" />
					<aos:textfield fieldLabel="数据表描述" name="tabledesc" columnWidth="1.0"
								   readOnly="true" />
					<aos:combobox name="check_type"  fieldLabel="编研类型" value="开放编研"
								  columnWidth="1.0" readOnly="true">
						<aos:option value="开放编研" display="开放编研" />
						<aos:option value="保管期限编研" display="保管期限编研" />
						<aos:option value="密级编研" display="密级编研" />
						<aos:option value="销毁编研" display="销毁编研" />
					</aos:combobox>
					<aos:datefield name="datatime" fieldLabel="日期" format="Y-m-d"
								   columnWidth="1.0" allowBlank="false" />
					<aos:textfield fieldLabel="操作人" name="person" columnWidth="1.0" readOnly="true"/>
					<aos:textfield fieldLabel="校对人" name="next_compilation" columnWidth="1.0"
								   readOnly="true" />
					<aos:textareafield  name="description" allowBlank="false"  columnWidth="1.0" fieldLabel="编研描述" readOnly="true"/>
					<aos:radioboxgroup fieldLabel="是否开放" id="_next_bloom_radio"  columns="[80,80]">
						<aos:radiobox name="kaifang" boxLabel="开放" checked="true"/>
						<aos:radiobox name="kaifang" boxLabel="不开放" />
					</aos:radioboxgroup>
				</aos:formpanel>
				<aos:docked dock="bottom" ui="footer">
					<aos:dockeditem xtype="tbfill" />
					<aos:dockeditem text="确定" onclick="_bloom_next_check_load" icon="ok.png" />
					<aos:dockeditem onclick="#_bloom_next_check.hide();" text="关闭"
									icon="close.png" />
				</aos:docked>
			</aos:window>
			<aos:window id="_w_task" title="任务分配列表" autoScroll="true" height="500" width="800" onshow="_w_task_show">
				<aos:gridpanel id="_g_task" region="east" url="listarticles.jhtml"
							   hidePagebar="true" split="true" splitterBorder="0 1 0 1" width="1000" autoScroll="true">
					<aos:docked>
						<aos:dockeditem xtype="tbtext" text="列表" />
						<aos:dockeditem onclick="examine_show2" text="分配" icon="ok.png" />
						<aos:dockeditem onclick="examine_del" text="删除" icon="del.png" />
						<aos:dockeditem onclick="archive_details" text="档案详情" icon="query.png" />
					</aos:docked>
					<aos:column type="rowno" />
					<aos:column header="流水号" dataIndex="id_" hidden="true" />
					<aos:column header="任务编号" dataIndex="task_number"  width="200" align="true"/>
					<aos:column header="任务名称" dataIndex="task_name"  width="200" align="true"/>
					<aos:column header="时间" dataIndex="datatime" width="200" align="true" />
					<aos:column header="数据表名称" dataIndex="tablename" width="200" align="true" hidden="true"/>
					<aos:column header="编研范围" dataIndex="tabledesc"  width="200" align="true" />
					<aos:column header="撰稿人" dataIndex="createperson"  width="200" align="true" />
					<aos:column header="合稿人" dataIndex="first_compilationperson" width="200" align="true"/>
					<aos:column header="合稿人中文" dataIndex="first_compilationperson_cn" width="200" align="true"/>
					<aos:column header="校对人" dataIndex="next_compilationperson" width="200" align="true"/>
					<aos:column header="校对人中文" dataIndex="next_compilationperson_cn" width="200" align="true"/>
					<aos:column header="总编辑人"  dataIndex="last_compilationperson" width="200" align="true"/>
					<aos:column header="总编辑人中文" dataIndex="last_compilationperson_cn" width="200" align="true"/>
					<aos:column header="编研描述" dataIndex="description" width="200" align="true"/>
				</aos:gridpanel>
				<aos:docked dock="bottom" ui="footer">
					<aos:dockeditem xtype="tbfill" />
					<aos:dockeditem onclick="#_w_articles.close();" text="关闭" icon="close.png" />
				</aos:docked>
			</aos:window>
			<aos:window id="_w_task_reset" title="分配" onshow="_w_task_load">
				<aos:formpanel id="_g_task_reset" width="500" layout="anchor"
							   labelWidth="90">
					<aos:hiddenfield name="id_"  />
					<aos:textfield fieldLabel="任务编号" name="task_number" columnWidth="1.0" readOnly="true"/>
					<aos:textfield fieldLabel="任务名称" name="task_name" columnWidth="1.0" />
					<aos:textfield fieldLabel="时间" name="datatime" columnWidth="1.0" readOnly="true"/>
					<aos:textfield fieldLabel="编研范围" name="tabledesc" columnWidth="1.0" readOnly="true"/>
					<aos:textfield fieldLabel="创建人" name="createperson" columnWidth="1.0" readOnly="true"/>
					<aos:combobox name="first_compilation" id="first_compilation" fieldLabel="合稿人" allowBlank="false"
								  columnWidth="1.0" fields="['account_','name_']" displayField="account_"
								  valueField="account_" onselect="add_first_compilation"
								  url="queryUsers.jhtml">
					</aos:combobox>
					<aos:gridpanel id="_g_first_compilation"  split="true" hidePagebar="true" autoScroll="true"
								   height="100" width="500" drag="true">
						<aos:menu>
							<aos:menuitem text="删除" onclick="_g_first_compilation_del" icon="del.png" />
						</aos:menu>
						<aos:column header="合稿人" dataIndex="fieldennames" width="200"/>
						<aos:column header="合稿人中文" dataIndex="fieldcnnames" width="200"/>
					</aos:gridpanel>
					<aos:combobox name="next_compilation" id="next_compilation" fieldLabel="校对人" allowBlank="false"
								  columnWidth="1.0" fields="['account_','name_']" displayField="account_"
								  valueField="account_" onselect="add_next_compilation"
								  url="queryUsers.jhtml">
					</aos:combobox>
					<aos:gridpanel id="_g_next_compilation" split="true" hidePagebar="true" autoScroll="true"
								   height="100" width="500" drag="true">
						<aos:menu>
							<aos:menuitem text="删除" onclick="_g_next_compilation_del" icon="del.png" />
						</aos:menu>
						<aos:column header="校对人" dataIndex="fieldennames" width="200"/>
						<aos:column header="在审人中文" dataIndex="fieldcnnames" width="200"/>
					</aos:gridpanel>
					<aos:combobox name="last_compilation" id="last_compilation" fieldLabel="总编辑人" allowBlank="false"
								  columnWidth="1.0" fields="['account_','name_']" displayField="account_"
								  valueField="account_" onselect="add_last_compilation"
								  url="queryUsers.jhtml">
					</aos:combobox>
					<aos:gridpanel id="_g_last_compilation" split="true" hidePagebar="true" autoScroll="true"
								   height="100" width="500" drag="true">
						<aos:menu>
							<aos:menuitem text="删除" onclick="_g_last_compilation_del" icon="del.png" />
						</aos:menu>
						<aos:column header="总编辑人" dataIndex="fieldennames" width="200"/>
						<aos:column header="总编辑人中文" dataIndex="fieldcnnames" width="200"/>
					</aos:gridpanel>
					<aos:textareafield fieldLabel="编研描述" name="description" columnWidth="1.0"/>

				</aos:formpanel>

				<aos:docked dock="bottom" ui="footer">
					<aos:dockeditem xtype="tbfill" />
					<aos:dockeditem onclick="_w_details_add" text="确定" icon="add.png" />
					<aos:dockeditem onclick="#_w_task_reset.hide();" text="关闭" icon="close.png" />
				</aos:docked>
			</aos:window>
			<!--初审人-->
			<aos:window id="_w_first_trial" title="编研列表" autoScroll="true" height="500" width="800" onshow="_w_first_trial_show">
				<aos:gridpanel id="_g_first_trial" region="east" url="listfirsttrial.jhtml"
							   hidePagebar="true" split="true" splitterBorder="0 1 0 1" width="1000" autoScroll="true">
					<aos:docked>
						<aos:dockeditem xtype="tbtext" text="列表" />
						<aos:dockeditem onclick="open_write_list" text="合稿操作" icon="more/arrow-merge.png" />
					</aos:docked>
					<aos:column type="rowno" />
					<aos:column dataIndex="id_" header="流水号" hidden="true" />
					<aos:column dataIndex="task_number" header="编研任务编号"/>
					<aos:column dataIndex="task_name" header="编研任务名称"/>
					<aos:column dataIndex="general" header="总体要求"/>
					<aos:column dataIndex="task_time" header="编研时间"/>
					<aos:column dataIndex="reference" header="参考范围"/>
					<aos:column dataIndex="compilationResearch" header="编研步骤"/>
					<aos:column dataIndex="personnel" header="参与人员"/>
					<aos:column dataIndex="otherdescription" header="其他说明"/>


				</aos:gridpanel>
				<aos:docked dock="bottom" ui="footer">
					<aos:dockeditem xtype="tbfill" />
					<aos:dockeditem onclick="#_w_first_trial.close();" text="关闭" icon="close.png" />
				</aos:docked>
			</aos:window>
			<!--初审人-->
			<aos:window id="_w_next_trial" title="校对列表" autoScroll="true" height="500" width="800" onshow="_w_next_trial_show">
				<aos:gridpanel id="_g_next_trial" region="east" url="listnexttrial.jhtml"
							   hidePagebar="true" split="true" splitterBorder="0 1 0 1" width="1000" autoScroll="true">
					<aos:docked>
						<aos:dockeditem xtype="tbtext" text="列表" />
						<aos:dockeditem onclick="next_open_write_list" text="校对" icon="more/checkbox-2.png" />
					</aos:docked>
					<aos:column type="rowno" />
					<aos:column dataIndex="id_" header="流水号" hidden="true" />
					<aos:column dataIndex="task_number" header="编研任务编号"/>
					<aos:column dataIndex="task_name" header="编研任务名称"/>
					<aos:column dataIndex="general" header="总体要求"/>
					<aos:column dataIndex="task_time" header="编研时间"/>
					<aos:column dataIndex="reference" header="参考范围"/>
					<aos:column dataIndex="compilationResearch" header="编研步骤"/>
					<aos:column dataIndex="personnel" header="参与人员"/>
					<aos:column dataIndex="otherdescription" header="其他说明"/>
				</aos:gridpanel>
				<aos:docked dock="bottom" ui="footer">
					<aos:dockeditem xtype="tbfill" />
					<aos:dockeditem onclick="#_w_next_trial.close();" text="关闭" icon="close.png" />
				</aos:docked>
			</aos:window>
			<!--初审人-->
			<aos:window id="_w_last_trial" title="总编辑列表" autoScroll="true" height="500" width="800" onshow="_w_last_trial_show">
				<aos:gridpanel id="_g_last_trial" region="east" url="listlasttrial.jhtml"
							   hidePagebar="true" split="true" splitterBorder="0 1 0 1" width="1000" autoScroll="true">
					<aos:docked>
						<aos:dockeditem xtype="tbtext" text="列表" />
						<aos:dockeditem onclick="last_trial_examine_show" text="总编辑" icon="more/computer-edit.png" />
					</aos:docked>
					<aos:column type="rowno" />
					<aos:column dataIndex="id_" header="流水号" hidden="true" />
					<aos:column dataIndex="task_number" header="编研任务编号"/>
					<aos:column dataIndex="task_name" header="编研任务名称"/>
					<aos:column dataIndex="general" header="总体要求"/>
					<aos:column dataIndex="task_time" header="编研时间"/>
					<aos:column dataIndex="reference" header="参考范围"/>
					<aos:column dataIndex="compilationResearch" header="编研步骤"/>
					<aos:column dataIndex="personnel" header="参与人员"/>
					<aos:column dataIndex="otherdescription" header="其他说明"/>
				</aos:gridpanel>
				<aos:docked dock="bottom" ui="footer">
					<aos:dockeditem xtype="tbfill" />
					<aos:dockeditem onclick="#_w_last_trial.close();" text="关闭" icon="close.png" />
				</aos:docked>
			</aos:window>
			<!--初审人列表-->
			<aos:window id="_w_next_trial_reset" title="校对">
				<aos:formpanel id="_g_next_trial_reset" width="500" layout="column"
							   labelWidth="90">
					<aos:hiddenfield name="id_" />
					<aos:textfield fieldLabel="任务编号" name="task_number" columnWidth="1.0" readOnly="true"/>
					<aos:textfield fieldLabel="任务名称" name="task_name" columnWidth="1.0" />
					<aos:textfield fieldLabel="时间" name="Datatime" columnWidth="1.0" readOnly="true"/>
					<aos:textfield fieldLabel="编研范围" name="tabledesc" columnWidth="0.8" readOnly="true"/>
					<aos:button text="详情" onclick="next_archive_details" margin="0 0 0 10"  columnWidth="0.2"/>
					<aos:textfield fieldLabel="创建人" name="createperson" columnWidth="1.0" readOnly="true"/>
					<aos:textareafield fieldLabel="编研描述" name="description" columnWidth="1.0"/>
					<aos:textareafield  fieldLabel="总编辑意见" id="last_message" name="last_message" columnWidth="1.0" />
				</aos:formpanel>
				<aos:docked dock="bottom" ui="footer">
					<aos:dockeditem xtype="tbfill" />
					<aos:dockeditem onclick="#_w_next_trial_reset.hide();" text="关闭" icon="close.png" />
				</aos:docked>
			</aos:window>
			<aos:window id="_w_last_trial_reset" title="终审">
				<aos:formpanel id="_g_last_trial_reset" width="500" layout="column"
							   labelWidth="90">
					<aos:hiddenfield name="id_" />
					<aos:textfield fieldLabel="任务编号" name="task_number" columnWidth="1.0" readOnly="true"/>
					<aos:textfield fieldLabel="任务名称" name="task_name" columnWidth="1.0" />
					<aos:textfield fieldLabel="时间" name="Datatime" columnWidth="1.0" readOnly="true"/>
					<aos:textfield fieldLabel="编研范围" name="tabledesc" columnWidth="0.8" readOnly="true"/>
					<aos:button text="详情" onclick="last_archive_details" margin="0 0 0 10"  columnWidth="0.2"/>
					<aos:textfield fieldLabel="创建人" name="createperson" columnWidth="1.0" readOnly="true"/>
					<aos:textareafield fieldLabel="编研描述" name="description" columnWidth="1.0"/>
				</aos:formpanel>
				<aos:docked dock="bottom" ui="footer">
					<aos:dockeditem xtype="tbfill" />
					<aos:dockeditem onclick="#_w_last_trial_reset.hide();" text="关闭" icon="close.png" />
				</aos:docked>
			</aos:window>
			<aos:window id="_w_details_message" title="详情">
				<aos:formpanel id="_g_details_message" width="500" layout="anchor"
							   labelWidth="90">
					<aos:hiddenfield name="id_"  />
					<aos:textfield fieldLabel="操作人" name="users" columnWidth="1.0" readOnly="true"/>
					<aos:textfield fieldLabel="操作时间" name="operate_time" columnWidth="1.0" readOnly="true"/>
					<aos:textareafield fieldLabel="操作描述" name="operate_description" columnWidth="1.0" readOnly="true"/>
					<aos:textfield fieldLabel="方案编号" name="task_number" columnWidth="1.0" readOnly="true"/>
					<aos:textfield fieldLabel="方案名称" name="task_name" columnWidth="1.0" readOnly="true"/>

					<aos:textfield fieldLabel="创建日期" name="Datatime" columnWidth="1.0" readOnly="true"/>
					<aos:textfield fieldLabel="编研范围" name="tablename" columnWidth="1.0" readOnly="true"/>
					<aos:textareafield fieldLabel="编研描述" name="tabledesc" columnWidth="1.0" readOnly="true"/>

					<aos:textfield fieldLabel="方案创建人" name="createperson" columnWidth="1.0" readOnly="true"/>

					<aos:textfield fieldLabel="合稿人中文" name="first_compilationperson_cn" columnWidth="1.0" readOnly="true"/>
					<aos:textfield fieldLabel="校对人中文" name="next_compilationperson_cn" columnWidth="1.0" readOnly="true"/>
					<aos:textfield fieldLabel="总编辑人中文" name="last_compilationperson_cn" columnWidth="1.0" readOnly="true"/>
					<aos:textareafield fieldLabel="描述" name="description" columnWidth="1.0" readOnly="true"/>
				</aos:formpanel>
				<aos:docked dock="bottom" ui="footer">
					<aos:dockeditem xtype="tbfill" />
					<aos:dockeditem onclick="#_w_details.hide();" text="关闭" icon="close.png" />
				</aos:docked>
			</aos:window>
			<aos:window id="_w_write" title="撰稿设计" width="500"
						autoScroll="true" onshow="_load_write">
				<aos:formpanel id="_f_write" width="480" layout="anchor">
					<aos:combobox name="task_name" fieldLabel="任务名称" id="task_name" allowBlank="false"
								  columnWidth="0.49" fields="['task_name','task_number']"
								  regexText="task_number" displayField="task_name"
								  valueField="task_number"
								  url="listTaskName.jhtml">
					</aos:combobox>
					<aos:textfield fieldLabel="撰稿编号" name="write_number" columnWidth="1.0" allowBlank="false" readOnly="true"/>
					<aos:textfield fieldLabel="撰稿名称" name="write_name" columnWidth="1.0" allowBlank="false"/>
					<aos:datefield name="write_datatime" fieldLabel="撰稿时间" format="Y-m-d"
								   columnWidth="1.0" allowBlank="false" />
					<aos:textfield fieldLabel="撰稿档案名称" name="tablename" columnWidth="1.0" readOnly="true"/>
					<aos:textfield fieldLabel="撰稿创建人" name="write_createperson" columnWidth="1.0" readOnly="true"/>
					<aos:textareafield  name="write_description" allowBlank="false"  columnWidth="1.0" fieldLabel="撰稿描述"/>
				</aos:formpanel>
				<aos:docked dock="bottom" ui="footer">
					<aos:dockeditem xtype="tbfill" />
					<aos:dockeditem text="确定" onclick="_w_write_load" icon="ok.png" />
					<aos:dockeditem onclick="#_w_write.hide();" text="关闭"
									icon="close.png" />
				</aos:docked>
			</aos:window>
			<aos:window id="_w_operation_message" title="详情">
				<aos:formpanel id="_g_operation_message" width="500" layout="anchor"
							   labelWidth="90">
					<aos:hiddenfield name="id_"  />
					<aos:textfield fieldLabel="操作人" name="users" columnWidth="1.0"  readOnly="true"/>
					<aos:textfield fieldLabel="操作时间" name="operate_time" columnWidth="1.0"  readOnly="true"/>
					<aos:textareafield fieldLabel="操作描述" name="operate_description" columnWidth="1.0"  readOnly="true"/>
					<aos:textareafield fieldLabel="撰稿描述" name="compilation_message" columnWidth="1.0"  readOnly="true"/>
				</aos:formpanel>
				<aos:docked dock="bottom" ui="footer">
					<aos:dockeditem xtype="tbfill" />
					<aos:dockeditem onclick="#_w_operation_message.hide();" text="关闭" icon="close.png" />
				</aos:docked>
			</aos:window>


			<aos:window id="_write_list5_show" title="详情" onshow="open_write_list5_show">
				<aos:formpanel id="_g_write_list5_show" width="500" layout="anchor"
							   labelWidth="90">
					<aos:hiddenfield name="id_"  />
					<aos:textfield fieldLabel="编研任务编号" name="task_number" columnWidth="1.0" readOnly="true"/>
					<aos:textfield fieldLabel="编研任务名称" name="task_name" columnWidth="1.0"  readOnly="true"/>
					<aos:textfield fieldLabel="总体要求" name="general" columnWidth="1.0"  readOnly="true"/>
					<aos:textfield fieldLabel="编研时间" name="task_time" columnWidth="1.0" />
					<aos:textfield fieldLabel="参考范围" name="reference" columnWidth="1.0" />
					<aos:textfield fieldLabel="编研步骤" name="compilationResearch" columnWidth="1.0" />
					<aos:textfield fieldLabel="参与人员" name="personnel" columnWidth="1.0" />
					<aos:textareafield fieldLabel="其他说明" name="otherdescription" columnWidth="1.0"/>
					<aos:textareafield fieldLabel="总编辑人反馈信息" name="last_message" columnWidth="1.0" />
				</aos:formpanel>
				<aos:docked dock="bottom" ui="footer">
					<aos:dockeditem xtype="tbfill" />
					<aos:dockeditem onclick="next_trial_examine_show" text="校对" icon="more/checkbox-2.png" />
					<aos:dockeditem onclick="#_write_list4_show.hide();" text="关闭" icon="close.png" />
				</aos:docked>
			</aos:window>
		</aos:viewport>
		<script>
			window.onload=function() {
				//Ext.getCmp("_zhuangao_operator").setAttribute("disabled", true);
				//Ext.getCmp("_zhuangao_operator").setFieldStyle('disabled:true;');
				var zhuangao = "<%=request.getAttribute("zhuangao")%>";
				var first = "<%=request.getAttribute("first")%>";
				var next = "<%=request.getAttribute("next")%>";
				var last = "<%=request.getAttribute("last")%>";
				if (zhuangao === "0") {
					Ext.getCmp("_zhuangao_operator").hide();
				} else if (zhuangao === "1") {

				}
				if (first === "0") {
					Ext.getCmp("_first_operator").hide();
				} else if (first === "1") {

				}
				if (next === "0") {
					Ext.getCmp("_next_operator").hide();
				} else if (next === "1") {

				}
				if (last === "0") {
					Ext.getCmp("_last_operator").hide();
				} else if (last === "1") {

				}
			}
			//跳转主页
			function _w_tablename_init(){
				// alert(listtablename);
				var listTablename = Ext.getCmp("listTablename").value;
				var tablenamedesc = Ext.getCmp("listTablename").getRawValue();
				window.location.href="initData.jhtml?listtablename="+listTablename+"&tablenamedesc="+encodeURI(encodeURI(tablenamedesc));
			}
			//点击查询按钮，先进入查询表头信息
			function _w_tablename_show(){
				var tablename ="<%=request.getParameter("tablename")%>";
				var nd ="<%=session.getAttribute("nd")%>";
				var params = {
					tablename : tablename,
					nd:nd
				};
				//这个Store的命名规则为：表格ID+"_store"。
				_g_data_store.getProxy().extraParams = params;
				_g_data_store.load();
			}
			//加载数据
			function _load_borrow(){
				//传值
				_f_articles.form.findField("tablename").setValue(Ext.getCmp("tablename").getValue());
				//传递描述
				_f_articles.form.findField("tabledesc").setValue(Ext.getCmp("listTablename").getValue());
				AOS.ajax({
					url: 'jymessage.jhtml',
					method:'post',
					ok: function (data) {
						_f_articles.form.setValues(data);
					}
				});
			}
			function loadDemo
			(){
				var tablename=Ext.getCmp('tableTemplate').value;
				var page=1;
				Ext.getCmp('page').setValue(page);
				var limit=_g_destroy_store.pageSize;
				//根据用户名得到条目数，求出总页数
				AOS.ajax({
					url: 'getSumPage.jhtml',
					params:{'tablename':tablename},
					ok : function(data) {
						//得到总条目数
						Ext.getCmp('count').setValue(data.appcode);
						//为记录和当前页赋值
						Ext.getCmp('count_dock').setValue(data.appcode);
						//当前页
						Ext.getCmp('page_dock').setValue(1);
					}
				});
				var params = {
					tablename: tablename,
					page:page,
					limit:limit
				};
				_g_destroy_store.getProxy().extraParams = params;
				_g_destroy_store.load();
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
				var tablename=Ext.getCmp("tablename").getValue();
				//先根据当前条件在后台设计一个query
				params["tablename"]=tablename;
				AOS.ajax({
					url: 'getQuery.jhtml',
					params:params,
					ok : function(data) {
						Ext.getCmp("query").setValue(data.query);
					}
				});

				//这个Store的命名规则为：表格ID+"_store"。
				_g_data_store.getProxy().extraParams = params;
				_g_data_store.load();
				_w_query_q.hide();
				AOS.reset(_f_query);
				//让指定文本框赋值
			}
			function _w_bloom_show(){
				AOS.reset(_f_bloom);
				_w_bloom.show();
			}
			function _load_bloom(){
				var listTablename ="<%=request.getParameter("listtablename")%>";
				var tablenamedesc ="<%=request.getAttribute("tablenamedesc")%>";
				//让指定文本框赋值
				_f_bloom.form.findField("tablename").setValue(listTablename);
				//传递描述
				_f_bloom.form.findField("tabledesc").setValue(tablenamedesc);
				var time = (new Date).getTime();
				var yesday = new Date(time); // 获取的是前一天日期
				yesday = yesday.getFullYear() + "-" + (yesday.getMonth()> 9 ? (yesday.getMonth() + 1) : "0" + (yesday.getMonth() + 1)) + "-" +(yesday.getDate()> 9 ? (yesday.getDate()) : "0" + (yesday.getDate()));
				_f_bloom.form.findField("datatime").setValue(yesday);
				var user ="<%=request.getAttribute("user")%>";
				_f_bloom.form.findField("createperson").setValue(user);
				//设计一个随机数编号
				_f_bloom.form.findField("task_number").setValue(generateUUID());

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

			function _load_articles(){
				var listTablename ="<%=request.getParameter("listtablename")%>";
				var tablenamedesc ="<%=request.getAttribute("tablenamedesc")%>";
				//让指定文本框赋值
				_g_articles.form.findField("tablename").setValue(listTablename);
				//传递描述
				_g_articles.form.findField("tabledesc").setValue(tablenamedesc);
				var time = (new Date).getTime();
				var yesday = new Date(time); // 获取的是前一天日期
				yesday = yesday.getFullYear() + "-" + (yesday.getMonth()> 9 ? (yesday.getMonth() + 1) : "0" + (yesday.getMonth() + 1)) + "-" +(yesday.getDate()> 9 ? (yesday.getDate()) : "0" + (yesday.getDate()));
				_g_articles.form.findField("datatime").setValue(yesday);
				var user ="<%=request.getAttribute("user")%>";
				_g_articles.form.findField("createperson").setValue(user);
				//设计一个随机数编号
				_g_articles.form.findField("task_number").setValue(generateUUID());
			}
			function _load_articles_grid(){
				_g_articles_store.load();
			}
			//加载(开放)数据
			function _w_bloom_load(){
				var selection = AOS.selection(_g_data, 'id_');
				AOS.ajax({
					url: 'addbloomForm.jhtml',
					forms:_f_bloom,
					params:{aos_rows_: selection,
						flag:1},
					ok: function (data) {
						if(data.appcode===1){
							AOS.tip("添加成功!");
							_w_bloom.hide();
						}else if(data.appcode===-1){
							AOS.tip("添加失败!");
						}
					}
				});
			}
			//加载(开放)数据
			function _w_write_load(){
				//write_flag=1代表此方案中的编研只是创建，未进行编研操作
				//write_flag=2代表此方案中的编研创建，进行编研操作
				//first_flag=0未进行合稿操作
				//first_flag=1进行合稿操作
				////next_flag=0未进行合稿操作
				////next_flag=1进行合稿操作
				////last_flag=0未进行合稿操作
				////last_flag=1进行合稿操作
				var task_number = Ext.getCmp("task_name").value;
				var task_name = Ext.getCmp("task_name").getRawValue();
				var selection = AOS.selection(_g_data, 'id_');
				AOS.ajax({
					url: 'addwriteForm.jhtml',
					forms:_f_write,
					params:{aos_rows_: selection,
						task_number:task_number,
						task_name:task_name,
						tablename:tablename.getValue(),
						write_flag:1},
					ok: function (data) {
						if(data.appcode===1){
							AOS.tip("添加成功!");
							_w_write.hide();
						}else if(data.appcode===-1){
							AOS.tip("添加失败!");
						}
					}
				});
			}
			//加载(保管期限)数据
			function _w_bgqx_load(){
				var selection = AOS.selection(_g_data, 'id_');
				AOS.ajax({
					url: 'addbgqxForm.jhtml',
					forms:_f_bgqx,
					params:{aos_rows_: selection,
						flag:1},
					ok: function (data) {
						if(data.appcode===1){
							AOS.tip("添加成功!");
							_w_bgqx.hide();
						}else if(data.appcode===-1){
							AOS.tip("添加失败!");
						}
					}
				});
			}
			function archive_write_list(){
				var selection = AOS.selection(_g_write_list, 'id_');
				if (AOS.empty(selection)) {
					AOS.tip('查看详情前请先选中数据。');
					return;
				}
				var id_=AOS.selectone(_g_write_list).raw.id_;
				var tablename=AOS.selectone(_g_write_list).raw.tablename;
				window.parent.fnaddtab('','档案详情','/compilation/articles/archive_details.jhtml?id_='+id_+'&tablename='+tablename);
			}
			function _w_write_show(){
				AOS.reset(_f_write);
				_w_write.show();
			}
			//加载数据
			function _load_write(){
				//传值
				_f_write.form.findField("tablename").setValue(tablename.getValue());
				var user ="<%=request.getAttribute("user")%>";
				_f_write.form.findField("write_createperson").setValue(user);
				//设计一个随机数编号
				_f_write.form.findField("write_number").setValue(generateUUID());
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
			//加载(撰稿)数据
			function _w_articles_load(){
				var selection = AOS.selection(_g_data, 'id_');
				AOS.ajax({
					url: 'addarticlesForm.jhtml',
					forms:_f_articles,
					params:{aos_rows_: selection,
						flag:1},
					ok: function (data) {
						if(data.appcode===1){
							AOS.tip("添加成功!");
							_w_articles.hide();
						}else if(data.appcode===-1){
							AOS.tip("添加失败!");
						}
					}
				});
			}
			//待销毁列表
			function _w_wait_destroy(){
				AOS.reset(_f_destroy);
				_g_destroy_store.removeAll();
				_w_destroy.show();
			}
			//编研历史
			function _w_operation_login(){
				_w_operation.show();
			}
			//销毁
			function _w_org_show(){
				var params={'tablename':tableTemplate.getValue()};
				var msg = AOS.merge('确认销毁该数据吗？');
				AOS.confirm(msg, function (btn) {
					if (btn === 'cancel') {
						AOS.tip('销毁操作被取消。');
						return;
					}
					var row = AOS.selectone(_g_destroy);
					AOS.ajax({
						url:'deleteData.jhtml',
						params: {
							tablename: tableTemplate.getValue(),
							id_: row.data.id_
						},
						ok: function (data) {
							if(data.appcode ===-1){
								AOS.tip('销毁失败!');
							}else if(data.appcode ===1){
								AOS.tip('销毁成功!');
								_g_destroy_store.getProxy().extraParams = params;
								//执行加载
								//此时总数减少一个，并刷新了页面
								//得到总数
								var count=Ext.getCmp('count').getValue();
								if(count>=1){
									Ext.getCmp('count').setValue(count-1);
									Ext.getCmp('count_dock').setValue(count-1);

								}
								_g_destroy_store.load();
							}

						}
					});
				});
			}
			//还原
			function _w_org2_show(){
				var params={'tablename':tableTemplate.getValue()};
				var msg = AOS.merge('确认还原该数据吗？');
				AOS.confirm(msg, function (btn) {
					if (btn === 'cancel') {
						AOS.tip('还原操作被取消。');
						return;
					}
					//得到被选中的记录的第一个选中的id值
// 		        Ext.getCmp("_g_destroy").getSelectionModel().getSelection()[0].data.id_;
					var row = AOS.selectone(_g_destroy);
					AOS.ajax({
						url:'returnData.jhtml',
						params: {
							tablename: Ext.getCmp("tableTemplate").getValue(),
							id_: row.data.id_
						},
						ok: function (data) {
							if(data.appcode ===-1){
								AOS.tip('还原失败!');
							}else if(data.appcode ===1){
								AOS.tip('还原成功!');
								_g_destroy_store.getProxy().extraParams = params;
								//执行加载
								//此时总数减少一个，并刷新了页面
								//得到总数
								var count=Ext.getCmp('count').getValue();
								if(count>=1){
									Ext.getCmp('count').setValue(count-1);
									Ext.getCmp('count_dock').setValue(count-1);
								}
								_g_destroy_store.load();
							}
						}
					});
				});
			}
			function _w_appraise_show(){
				_g_appraise_store.load();

			}
			//导出销毁表单
			function _w_destory_form(){
				var tablename=Ext.getCmp("tableTemplate").getValue();
				if(tablename==""){
					AOS.tip("请选择门类!");
					return;
				}
				//每页数量
				var limit=_g_destroy_store.pageSize;
				//当前页
				var page=Ext.getCmp('page_dock').getValue();
				AOS.ajax({
					url : 'fillReport.jhtml',
					params: {
						limit:limit,
						page:page,
						tablename: Ext.getCmp("tableTemplate").getValue()
					},
					ok : function(data) {
						AOS.file('${cxt}/report/xls3.jhtml');
					}
				});
			}
			//得到上一页数据
			function _w_destory_Previous(){
				var tablename=Ext.getCmp("tableTemplate").getValue();
				if(tablename==""){
					AOS.tip("请选择门类!");
					return;
				}
				//当前页减一是不是小于等于0
				var page=Number(Ext.getCmp("page").getValue());
				if(page-1<=0){
					AOS.tip("当前第一页");
					return;
				}
				var limit=_g_destroy_store.pageSize;
				var params = {
					tablename: Ext.getCmp("tableTemplate").getValue(),
					page:page-1,
					limit:limit
				};
				_g_destroy_store.getProxy().extraParams = params;
				//让当前页吗减少一
				Ext.getCmp("page").setValue(page-1);
				//当前页
				Ext.getCmp('page_dock').setValue(page-1);
				_g_destroy_store.load();
			}
			//得到下一页数据
			function _w_destory_next(){
				var tablename=Ext.getCmp("tableTemplate").getValue();
				if(tablename==""){
					AOS.tip("请选择门类!");
					return;
				}
				//当前页减一是不是小于等于0
				var page=Number(Ext.getCmp("page").getValue());
				//得到总数
				var count=Number(Ext.getCmp('count').getValue());
				var limit=Number(_g_destroy_store.pageSize);
				//总页数
				var allpage;
				if(count/limit==0){
					allpage=1;
				}else if(count%limit==0){
					allpage=count/limit;
				}else if(count%limit>0){
					allpage=count/limit+1;
				}
				if(page+1>=allpage){
					AOS.tip("当前最后一页");
					return;
				}
				var limit=_g_destroy_store.pageSize;
				var params = {
					tablename: Ext.getCmp("tableTemplate").getValue(),
					page:page+1,
					limit:limit
				};
				_g_destroy_store.getProxy().extraParams = params;
				//让当前页吗减少一
				Ext.getCmp("page").setValue(page+1);
				//当前页
				Ext.getCmp('page_dock').setValue(page+1);
				_g_destroy_store.load();
			}
			//展开操作记录
			function _w_operation_show(){
				_g_operation_store.reload();
			}
			//_状态列转换
			function fn_first_flag_render(value, metaData, record, rowIndex, colIndex,
										  store) {
				if (value == 1) {
					return '<a><font color="green">已编研</font></a>';
				} else {
					return '<a><font color="red">未编研</font></a>';
				}
			}
			function fn_next_flag_render(value, metaData, record, rowIndex, colIndex,
										 store) {
				if (value == 1) {
					return '<a><font color="green">已编研</font></a>';
				} else {
					return '<a><font color="red">未编研</font></a>';
				}
			}
			function fn_export(){
				AOS.ajax({
					url : 'fillReport.jhtml',
					ok : function(data) {
						AOS.file('${cxt}/report/xls2.jhtml');
					}
				});
			}
			///双击修改数据
			function _w_examine_show(){
				var selection = AOS.selection(_g_operation, 'id_');
				var record = AOS.selectone(_g_operation);
				if (AOS.empty(selection)) {
					AOS.tip('请选择要查看详情的数据!');
					return;
				}else{
					_w_operation_message.show();
					_g_operation_message.loadRecord(record);
				}
			}
			//单机查看详情
			function _w_examine_show2(){
				var selection = AOS.selection(_g_operation, 'id_');
				if (AOS.empty(selection)) {
					AOS.tip('请选择要查看详情的数据!');
					return;
				}else{
					//弹出审核窗口
					var record = AOS.selectone(_g_operation);
					if (record) {
						_w_details_message.show();
						_g_details_message.loadRecord(record);
					}
				}
			}
			//修改编研
			function fn_update(){
				var selection = AOS.selection(_g_operation, 'id_');
				if (AOS.empty(selection)) {
					AOS.tip('请选择要修改的数据!');
					return;
				}else{
					var record = AOS.selectone(_g_operation);
					_w_update.show();
					_g_update.loadRecord(record);
				}
			}
			function _update_articles(){
				//判断操作状态是不是已编研
				var first_flag=_g_update.form.findField("first_flag").getValue();
				var next_flag=_g_update.form.findField("next_flag").getValue();
				if(first_flag==="1"&&next_flag==="1"){
					AOS.err("已经编研，不能修改数据!");
					return ;
				}
				//修改编研
				AOS.ajax({
					url: 'updatearticles.jhtml',
					forms:_g_update,
					ok: function (data) {
						if(data.appcode===1){
							AOS.tip("修改成功!");
							_w_update.hide();
							_g_operation_store.reload();
						}else if(data.appcode===-1){
							AOS.tip("修改失败!");
						}
					}
				});
			}
			function fn_delete(){
				var msg = AOS.merge('确认要删除当前数据吗？');
				var record = AOS.selectone(_g_operation);
				AOS.confirm(msg, function (btn) {
					if (btn === 'cancel') {
						AOS.tip('删除操作被取消。');
						return;
					}
					AOS.ajax({
						url: 'deletearticles.jhtml',
						params: {
							id_:record.data.id_
						},
						ok: function (data) {
							if(data.appcode===1){
								AOS.tip("删除成功!");
								_w_update.hide();
								_g_operation_store.reload();
							}else if(data.appcode===-1){
								AOS.tip("删除失败!");
							}
						}
					});
				});
			}
			//编研列表展开操作
			function _go_articles(){
				_w_articles.show();
			}
			function _w_task_show(){
				_g_task_store.reload();
			}
			//初审编研
			function fn_first_check(){
				//1.判断选中条目
				var selection = AOS.selection(_g_articles, 'id_');
				if (AOS.empty(selection)) {
					AOS.tip('请选择要编研的数据!');
					return;
				}
				//2.判断是不是对应可以执行编研的用户
				var row = AOS.selectone(_g_articles);
				var first_person=row.data.first_compilation;
				//得到当前用户
				var root="<%=session.getAttribute("user")%>";
				if(!(first_person===root)){
					AOS.err('当前用户和初审用户不匹配不能完成编研!');
					return;
				}
				//3.判断是否已经完成了编研操作
				var first_flag=row.data.first_flag;
				if(first_flag==="1"){
					AOS.err('已经完成初审操作，无需再次操作!');
					return;
				}
				//4.判断当前所有编研的类型、
				var check_type=row.data.check_type;
				if(check_type==="开放编研"){
					_bloom_first_check.show();
				}else if(check_type==="保管期限编研"){
					_bgqx_first_check.show();
				}
			}
			function _load_bloom_first_check(){
				var record = AOS.selectone(_g_articles);
				_f_bloom_first_check.loadRecord(record);
			}
			function _load_bloom_next_check(){
				var record = AOS.selectone(_g_articles);
				_f_bloom_next_check.loadRecord(record);
			}
			//开放编研初审执行
			function _bloom_first_check_load(){

				var bloom_radio_check = Ext.getCmp('_first_bloom_radio').items;
				var xq = '';
				for(var i = 0; i < bloom_radio_check.length; i++){
					if(bloom_radio_check.get(i).checked){
						xq =bloom_radio_check.get(i).boxLabel;
					}
				}
				AOS.ajax({
					forms:_f_bloom_first_check,
					url: 'bloomfirstarticles.jhtml',
					params:{kaifangdescription:xq},
					method:'post',
					ok: function (data) {
						if(data.appcode===1){
							AOS.tip("操作成功!");
							_bloom_first_check.hide();
							_g_articles_store.reload();
						}else if(data.appcode===-1){
							AOS.tip("操作失败!");
						}
					}
				});
			}

			//开放编研再审执行
			function _bloom_next_check_load(){
				var bloom_radio_check = Ext.getCmp('_first_bloom_radio').items;
				var xq = '';
				for(var i = 0; i < bloom_radio_check.length; i++){
					if(bloom_radio_check.get(i).checked){
						xq =bloom_radio_check.get(i).boxLabel;
					}
				}
				AOS.ajax({
					forms:_f_bloom_next_check,
					url: 'bloomnextarticles.jhtml',
					params:{kaifangdescription:xq},
					method:'post',
					ok: function (data) {
						if(data.appcode===1){
							AOS.tip(data.appmsg);
							_bloom_next_check.hide();
							_g_articles_store.reload();
						}else if(data.appcode===-1){
							AOS.tip(data.appmsg);
						}
					}
				});
			}

			//展开编研列表
			function _list_articles(){
				_w_articles.show();
			}
			//展开编研列表
			function _list_write(){
				_w_write_list.show();
			}
			//展开编研列表
			function write_task(){
				_w_write_list2.show();
			}
			//展开详情列表
			function write_list_show(){
				var selection = AOS.selection(_g_write_list, 'id_');
				if (AOS.empty(selection)) {
					AOS.tip('请选择要查看详情的数据!');
					return;
				}else{
					//弹出窗口
					var record = AOS.selectone(_g_write_list);
					if (record) {
						_write_list_show.show();
						_g_write_list_show.loadRecord(record);
					}
				}
			}
			//展开详情列表
			function write_list2_show(){
				var selection = AOS.selection(_g_write_list2, 'id_');
				if (AOS.empty(selection)) {
					AOS.tip('请选择要查看详情的数据!');
					return;
				}else{
					//弹出窗口
					var record = AOS.selectone(_g_write_list2);
					if (record) {
						_write_list2_show.show();
						_g_write_list2_show.loadRecord(record);
					}
				}
			}
			function examine_show2(){
				var selection = AOS.selection(_g_task, 'id_');
				if (AOS.empty(selection)) {
					AOS.tip('请选择要查看详情的数据!');
					return;
				}else{
					//弹出审核窗口
					var record = AOS.selectone(_g_task);
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
			function _list_task(){
				_w_task.show();
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
			function _w_details_add(){
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
				if(first_fieldennames==""||next_fieldennames==""||last_fieldennames==""){
					AOS.err("授权人不能有空数据!");
					return;
				}
				AOS.ajax({
					forms:_g_task_reset,
					url: 'adddetails.jhtml',
					params:{first_fieldennames:first_fieldennames,
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
							_g_task_store.reload();
						}else if(data.appcode===-1){
							AOS.tip("操作失败!");
						}
					}
				});
			}
			function _first_field(){
				var count=_g_task_store.getCount();
				if(count>0){
					for(var i=0;i<count;i++){
						var leftenzd = _g_task_store.getAt(i).get("first_compilation");
						var rightcnzd = _g_task_store.getAt(i).get("first_compilation_cn");
						var leftenzds=leftenzd.split(',');
						var rightcnzds=rightcnzd.split(',');
						if(leftenzds.length===rightcnzds.length){
							var strtemp='';
							for(var i=0;i<leftenzds.length; i++){
								var params = [{
									'fieldennames':leftenzds[i],
									'fieldcnnames':rightcnzds[i]
								}];
								_g_first_compilation_store.loadData(params,true);
							}
						}
					}
				}
			}
			function _next_field(){
				var count=_g_task_store.getCount();
				if(count>0){
					for(var i=0;i<count;i++){
						var leftenzd = _g_task_store.getAt(i).get("next_compilation");
						var rightcnzd = _g_task_store.getAt(i).get("next_compilation_cn");
						var leftenzds=leftenzd.split(',');
						var rightcnzds=rightcnzd.split(',');
						if(leftenzds.length===rightcnzds.length){
							var strtemp='';
							for(var i=0;i<leftenzds.length; i++){
								var params = [{
									'fieldennames':leftenzds[i],
									'fieldcnnames':rightcnzds[i]
								}];
								_g_next_compilation_store.loadData(params,true);
							}
						}
					}
				}
			}
			function _last_field(){
				var count=_g_task_store.getCount();
				if(count>0){
					for(var i=0;i<count;i++){
						var leftenzd = _g_task_store.getAt(i).get("last_compilation");
						var rightcnzd = _g_task_store.getAt(i).get("last_compilation_cn");
						var leftenzds=leftenzd.split(',');
						var rightcnzds=rightcnzd.split(',');
						if(leftenzds.length===rightcnzds.length){
							var strtemp='';
							for(var i=0;i<leftenzds.length; i++){
								var params = [{
									'fieldennames':leftenzds[i],
									'fieldcnnames':rightcnzds[i]
								}];
								_g_last_compilation_store.loadData(params,true);
							}
						}
					}
				}
			}
			//加载到表格中
			function _w_task_load(){
				//_first_field();
				//_next_field();
				//_last_field();
				///_g_field_store.insert(0,params);
			}
			//删除方案
			function examine_del(){
				var selection = AOS.selection(_g_task, 'id_');
				var tms = AOS.selection(_g_task, 'tm');
				if (AOS.empty(selection)) {
					AOS.tip('删除前请先选中数据。');
					return;
				}
				var msg = AOS.merge('确认要删除选中的[{0}]个方案数据吗？', AOS.rows(_g_task));
				AOS.confirm(msg, function (btn) {
					if (btn === 'cancel') {
						AOS.tip('删除操作被取消。');
						return;
					}
					AOS.ajax({
						url: 'deleteexamine.jhtml',
						params: {
							aos_rows_: selection
						},
						ok: function (data) {
							AOS.tip(data.appmsg);
							_g_task_store.reload();
						}
					});
				});
			}
			//档案详情
			function archive_details(){
				var selection = AOS.selection(_g_task, 'id_');
				if (AOS.empty(selection)) {
					AOS.tip('查看详情前请先选中数据。');
					return;
				}
				var id_=AOS.selectone(_g_task).raw.id_;
				window.parent.fnaddtab('','档案详情','/compilation/articles/archive_details.jhtml?id_='+id_);
			}
			//档案详情
			function archive_articles(){
				var selection = AOS.selection(_g_articles, 'id_');
				if (AOS.empty(selection)) {
					AOS.tip('查看详情前请先选中数据。');
					return;
				}
				var id_=AOS.selectone(_g_articles).raw.id_;
				window.parent.fnaddtab('','档案详情','/compilation/articles/archive_details.jhtml?id_='+id_);
			}
			//档案编研详情
			function archive_compilation_articles(){
				var selection = AOS.selection(_g_articles, 'id_');
				if (AOS.empty(selection)) {
					AOS.tip('查看编研详情前请先选中数据。');
					return;
				}
				var id_=AOS.selectone(_g_articles).raw.id_;
				window.parent.fnaddtab('','档案编研详情','/compilation/articles/archive_compilation_details.jhtml?id_='+id_);
			}
			function _w_first_trial_show(){
				var user="<%=session.getAttribute("user")%>";
				var params = {
					user: user
				};
				//这个Store的命名规则为：表格ID+"_store"。
				_g_first_trial_store.getProxy().extraParams = params;
				_g_first_trial_store.load();
			}


			function _w_last_trial_show(){
				var user="<%=session.getAttribute("user")%>";
				var params = {
					user: user
				};
				//这个Store的命名规则为：表格ID+"_store"。
				_g_last_trial_store.getProxy().extraParams = params;
				_g_last_trial_store.load();
			}
			function _check_zhuangao(){
				var user="<%=session.getAttribute("user")%>";
				//查询数据库是否可以撰稿，如果可以撰稿跳转页面
				var selection = AOS.selection(_g_data, 'id_');
				var record = AOS.selectone(_g_data);

				if (AOS.empty(selection)) {
					AOS.tip('请选择要撰稿的任务!');
					return;
				}else{
					if(record.data.flag_examine!="已通过"){
						AOS.tip('审核未通过不能完成撰稿!');
						return;
					}
					AOS.ajax({
						url: 'check_zhuangao.jhtml',
						params: {
							user:user,
							id_:record.data.id_
						},
						ok: function (data) {
							if(data.appcode==1){
								//跳转页面
								var id_=AOS.selectone(_g_data).raw.id_;
								window.parent.fnaddtab('','档案编研详情','/compilation/examine/archive_details_all_message.jhtml?id_='+id_+'&flag=1');
							}else{
								AOS.tip("该任务不能撰稿或者已经完成撰稿！");
							}
						}
					});
					//_w_operation_message.show();
					//_g_operation_message.loadRecord(record);
				}


				//_write_list4_show.show();
				//first_trial_examine_show();
			}
			function _check_first(){
				var user="<%=session.getAttribute("user")%>";
				//查询数据库是否可以撰稿，如果可以撰稿跳转页面
				var selection = AOS.selection(_g_data, 'id_');
				var record = AOS.selectone(_g_data);
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
								var id_ = AOS.selectone(_g_data).raw.id_;
								window.parent.fnaddtab('', '档案编研详情', '/compilation/examine/archive_details_first_message.jhtml?id_=' + id_ + '&flag=2&byrwbh='+record.data.byrwbh);
							} else {
								AOS.tip("该任务不能合稿或者已经完成合稿！");
							}
						}
					});

				}
				//first_trial_examine_show();


			}
			function _check_next(){
				var user="<%=session.getAttribute("user")%>";
				//查询数据库是否可以撰稿，如果可以撰稿跳转页面
				var selection = AOS.selection(_g_data, 'id_');
				var record = AOS.selectone(_g_data);
				if (AOS.empty(selection)) {
					AOS.tip('请选择要校对的任务!');
					return;
				}else {
					AOS.ajax({
						url: 'check_jiaodui.jhtml',
						params: {
							user: user,
							id_: record.data.id_
						},
						ok: function (data) {
							if (data.appcode == 1) {
								//跳转页面
								var id_ = AOS.selectone(_g_data).raw.id_;
								window.parent.fnaddtab('', '档案编研详情', '/compilation/examine/archive_details_next_message.jhtml?id_=' + id_ + '&flag=4');
							} else {
								AOS.tip("该任务不能校对或者已经完成校对！");
							}
						}
					});
				}


					//_w_next_trial.show();

			}
			function _check_last(){
				var user="<%=session.getAttribute("user")%>";
				//查询数据库是否可以撰稿，如果可以撰稿跳转页面
				var selection = AOS.selection(_g_data, 'id_');
				var record = AOS.selectone(_g_data);
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
								var id_ = AOS.selectone(_g_data).raw.id_;
								window.parent.fnaddtab('', '档案编研详情', '/compilation/examine/archive_details_last_message.jhtml?id_=' + id_ + '&flag=3');
							} else {
								AOS.tip("该任务不能终审或者已经完成终审！");
							}
						}
					});
				}

			}
			function first_trial_examine_show(){
				//弹出相信信息窗口



				var selection = AOS.selection(_g_data, 'id_');
				if (AOS.empty(selection)) {
					AOS.tip('请选择要操作的任务!');
					return;
				}else{
					//弹出审核窗口
					var record = AOS.selectone(_g_data);
					if (record) {
						//此时看看有没有再审意见，如果有就将控件展示出来
						var id_=record.data.id_;
						window.parent.fnaddtab('','合稿列表','/compilation/examine/first_details_all.jhtml?id_='+id_);
					}
				}
			}
			function next_trial_examine_show(){
				var selection = AOS.selection(_g_data, 'id_');
				if (AOS.empty(selection)) {
					AOS.tip('请选择要操作的任务!');
					return;
				}else{
					//弹出审核窗口
					var record = AOS.selectone(_g_data);
					if (record) {
						//此时看看有没有再审意见，如果有就将控件展示出来
						var id_=record.data.id_;
						window.parent.fnaddtab('','校对列表','/compilation/examine/next_details_all.jhtml?id_='+id_);
					}
				}
			}
			function last_trial_examine_show(){
				var selection = AOS.selection(_g_data, 'id_');
				if (AOS.empty(selection)) {
					AOS.tip('请选择要操作的方案!');
					return;
				}else{
					//弹出审核窗口
					var record = AOS.selectone(_g_data);
					if (record) {
						//此时看看有没有再审意见，如果有就将控件展示出来
						var id_=record.data.id_;
						window.parent.fnaddtab('','总编辑列表','/compilation/examine/last_details_all.jhtml?id_='+id_);
					}
				}
			}
			function write_archive_details(){
				var id_=AOS.selectone(_g_write_list2).raw.id_;
				window.parent.fnaddtab('','档案详情','/compilation/articles/archive_details_all.jhtml?id_='+id_+'&flag=1');
			}
			function next_archive_details(){
				var id_=AOS.selectone(_g_next_trial).raw.id_;
				window.parent.fnaddtab('','档案详情','/compilation/articles/archive_details_all.jhtml?id_='+id_+'&flag=2');
			}
			function last_archive_details(){
				var id_=AOS.selectone(_g_last_trial).raw.id_;
				window.parent.fnaddtab('','档案详情','/compilation/articles/archive_details_all.jhtml?id_='+id_+'&flag=3');
			}
			//得当当前用户的方案列表
			function _w_write_list_show(){
				_g_write_list_store.load();
			}
			//得当当前用户的方案列表
			function _w_write_list2_show(){
				_g_write_list2_store.load();
			}
			function open_write_list(){
				_write_list4_show.show();
			}
			function open_write_list4_show(){
				var record = AOS.selectone(_g_first_trial);
				_g_write_list4_show.loadRecord(record);
				AOS.ajax({
					url: 'getNextTrial.jhtml',
					params: {
						aos_rows_: record.data.id_
					},
					ok: function (data) {
						if(data.appmsg===""){
							Ext.getCmp("next_message").hide();
						}else{
							_g_write_list4_show.getForm().findField('next_message').setValue(data.appmsg);
						}
					}
				});

			}
			function next_open_write_list(){
				_write_list5_show.show();
			}
			function open_write_list5_show(){
				var record = AOS.selectone(_g_next_trial);
				_g_write_list5_show.loadRecord(record);
				AOS.ajax({
					url: 'getLastTrial.jhtml',
					params: {
						aos_rows_: record.data.id_
					},
					ok: function (data) {
						if(data.appmsg===""){
							Ext.getCmp("last_message").hide();
						}else{
							_g_write_list5_show.getForm().findField('last_message').setValue(data.appmsg);
						}
					}
				});

			}
				function _w_next_trial_show(){
					var user="<%=session.getAttribute("user")%>";
					var params = {
						user: user,
						byrwmc:byrwmc.getValue(),
						byrwbh:byrwbh.getValue(),
						sjkmc:sjkmc.getValue()
					};
					//这个Store的命名规则为：表格ID+"_store"。
					_g_data_store.getProxy().extraParams = params;
					_g_data_store.load();
				}

		</script>
	</aos:onready>

</aos:html>
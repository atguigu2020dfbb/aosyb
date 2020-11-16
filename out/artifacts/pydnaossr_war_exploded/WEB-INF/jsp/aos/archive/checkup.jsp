<%@ page contentType="text/html; charset=utf-8" isELIgnored="false"%>
<%@ taglib uri="/WEB-INF/tld/aos.tld" prefix="aos"%>
<%@ taglib uri="/WEB-INF/tld/cll.tld" prefix="c"%>
<%
	String path = request.getContextPath();
	Object fieldDtos = request.getAttribute("fieldDtos");
	Object listtablename = request.getAttribute("listtablename");
%>
<aos:html>
<aos:head title="档案鉴定">
	<aos:include lib="ext" />
	<aos:base href="archive/checkup" />
</aos:head>
<aos:body>
</aos:body>
<aos:onready>
	<aos:viewport layout="fit">
		<aos:gridpanel id="_g_data" url="listAccounts.jhtml" region="center"
			autoScroll="true" pageSize="20" enableLocking="true"
			onrender="_w_tablename_show">
			<aos:docked forceBoder="0 0 1 0">
				<aos:dockeditem xtype="tbtext" text="档案鉴定" />
				<aos:hiddenfield name="tablename" id="tablename" />
				<aos:hiddenfield name="query" id="query" />
				<!--当前查询记录数-->
				<aos:hiddenfield name="_total" id="_total" />
				<aos:combobox name="listTablename"
					fields="[ 'tablename', 'tabledesc']" fieldLabel="数据表"
					id="listTablename" columnWidth="0.3" url="listTablename.jhtml"
					displayField="tabledesc" valueField="tablename" allowBlank="false" onselect="_w_tablename_init"/>
				<aos:dockeditem text="查询" icon="query.png"
					onclick="_w_query_show" />
				<aos:dockeditem text="开放鉴定设定" icon="more/lock-open.png"
					onclick="_w_bloom_show" />
				<aos:dockeditem text="保管期限鉴定设定" icon="more/house.png"
					onclick="_w_bgqx_show" />
				<aos:dockeditem text="密级鉴定设定" icon="more/view-table-of-contents-ltr.png"
					onclick="_w_mj_show" />
				<aos:dockeditem text="销毁鉴定设定" icon="more/edit-destroy.png"
								onclick="_w_xh_show" />
				<aos:dockeditem text="方案列表" icon="c_key.png"
								onclick="_list_checkup" />
				<aos:dockeditem text="任务分配" icon="c_key.png"
								onclick="_list_task" />
				<aos:dockeditem text="初审" icon="c_key.png"
								onclick="_check_first" />
				<aos:dockeditem text="再审" icon="c_key.png"
								onclick="_check_next" />
				<aos:dockeditem text="终审" icon="c_key.png"
								onclick="_check_last" />

				<aos:dockeditem text="操作日志" icon="more/view-history.png"
								onclick="_w_operation_login" />
			</aos:docked>
			<aos:selmodel type="checkbox" mode="multi" />
			<aos:column type="rowno" />
			<aos:column dataIndex="id_" header="流水号" hidden="true" />
			<c:forEach var="field" items="${fieldDtos}">
				<aos:column dataIndex="${field.fieldenname}"
					header="${field.fieldcnname}" width="${field.dislen}"  rendererField="field_type_" />
			</c:forEach>
			<aos:column header="" flex="1" />
		</aos:gridpanel>
		<aos:window id="_w_bloom" title="开放鉴定设定" width="500"
			autoScroll="true" onshow="_load_bloom">
			<aos:formpanel id="_f_bloom" width="480" layout="column">
				<aos:textfield fieldLabel="鉴定任务编号" name="task_number" columnWidth="1.0"
							   readOnly="true" />
				<aos:textfield fieldLabel="任务名称" name="task_name" columnWidth="1.0" allowBlank="false"/>
				<aos:datefield name="datatime" fieldLabel="时间" format="Y-m-d"
					columnWidth="1.0" allowBlank="false" />
				<aos:hiddenfield fieldLabel="鉴定名称" name="tablename"/>
				<aos:textfield fieldLabel="鉴定范围" name="tabledesc" columnWidth="1.0"
							   readOnly="true" />
				<aos:textfield fieldLabel="创建人" name="createperson" columnWidth="1.0" readOnly="true"/>
				<aos:textfield fieldLabel="鉴定类型" name="checkup_type" columnWidth="1.0"
							   value="开放鉴定" readOnly="true" />
				<aos:textareafield  name="description" allowBlank="false"  columnWidth="1.0" fieldLabel="鉴定描述"/>
			</aos:formpanel>
			<aos:docked dock="bottom" ui="footer">
				<aos:dockeditem xtype="tbfill" />
				<aos:dockeditem text="确定" onclick="_w_bloom_load" icon="ok.png" />
				<aos:dockeditem onclick="#_w_bloom.hide();" text="关闭"
					icon="close.png" />
			</aos:docked>
		</aos:window>

		<aos:window id="_w_bgqx" title="开放鉴定设定" width="500"
					autoScroll="true" onshow="_load_bgqx">
			<aos:formpanel id="_f_bgqx" width="480" layout="column">
				<aos:textfield fieldLabel="鉴定任务编号" name="task_number" columnWidth="1.0"
							   readOnly="true" />
				<aos:textfield fieldLabel="任务名称" name="task_name" columnWidth="1.0" allowBlank="false"/>
				<aos:datefield name="datatime" fieldLabel="时间" format="Y-m-d"
							   columnWidth="1.0" allowBlank="false" />
				<aos:hiddenfield fieldLabel="鉴定名称" name="tablename"/>
				<aos:textfield fieldLabel="鉴定范围" name="tabledesc" columnWidth="1.0"
							   readOnly="true" />
				<aos:textfield fieldLabel="创建人" name="createperson" columnWidth="1.0" readOnly="true"/>
				<aos:textfield fieldLabel="鉴定类型" name="checkup_type" columnWidth="1.0"
							   value="保管期限鉴定" readOnly="true" />
				<aos:textareafield  name="description" allowBlank="false"  columnWidth="1.0" fieldLabel="鉴定描述"/>
			</aos:formpanel>
			<aos:docked dock="bottom" ui="footer">
				<aos:dockeditem xtype="tbfill" />
				<aos:dockeditem text="确定" onclick="_w_bgqx_load" icon="ok.png" />
				<aos:dockeditem onclick="#_w_bgqx.hide();" text="关闭"
								icon="close.png" />
			</aos:docked>
		</aos:window>


		<aos:window id="_w_mj" title="密级鉴定设定" width="500"
					autoScroll="true" onshow="_load_mj">
			<aos:formpanel id="_f_mj" width="480" layout="column">
				<aos:textfield fieldLabel="鉴定任务编号" name="task_number" columnWidth="1.0"
							   readOnly="true" />
				<aos:textfield fieldLabel="任务名称" name="task_name" columnWidth="1.0" allowBlank="false"/>
				<aos:datefield name="datatime" fieldLabel="时间" format="Y-m-d"
							   columnWidth="1.0" allowBlank="false" />
				<aos:hiddenfield fieldLabel="鉴定名称" name="tablename"/>
				<aos:textfield fieldLabel="鉴定范围" name="tabledesc" columnWidth="1.0"
							   readOnly="true" />
				<aos:textfield fieldLabel="创建人" name="createperson" columnWidth="1.0" readOnly="true"/>
				<aos:textfield fieldLabel="鉴定类型" name="checkup_type" columnWidth="1.0"
							   value="密级鉴定" readOnly="true" />
				<aos:textareafield  name="description" allowBlank="false"  columnWidth="1.0" fieldLabel="鉴定描述"/>
			</aos:formpanel>
			<aos:docked dock="bottom" ui="footer">
				<aos:dockeditem xtype="tbfill" />
				<aos:dockeditem text="确定" onclick="_w_mj_load" icon="ok.png" />
				<aos:dockeditem onclick="#_w_mj.hide();" text="关闭"
								icon="close.png" />
			</aos:docked>
		</aos:window>


		<aos:window id="_w_xh" title="销毁鉴定设定" width="500"
					autoScroll="true" onshow="_load_xh">
			<aos:formpanel id="_f_xh" width="480" layout="column">
				<aos:textfield fieldLabel="鉴定任务编号" name="task_number" columnWidth="1.0"
							   readOnly="true" />
				<aos:textfield fieldLabel="任务名称" name="task_name" columnWidth="1.0" allowBlank="false"/>
				<aos:datefield name="datatime" fieldLabel="时间" format="Y-m-d"
							   columnWidth="1.0" allowBlank="false" />
				<aos:hiddenfield fieldLabel="鉴定名称" name="tablename"/>
				<aos:textfield fieldLabel="鉴定范围" name="tabledesc" columnWidth="1.0"
							   readOnly="true" />
				<aos:textfield fieldLabel="创建人" name="createperson" columnWidth="1.0" readOnly="true"/>
				<aos:textfield fieldLabel="鉴定类型" name="checkup_type" columnWidth="1.0"
							   value="销毁鉴定" readOnly="true" />
				<aos:textareafield  name="description" allowBlank="false"  columnWidth="1.0" fieldLabel="鉴定描述"/>
			</aos:formpanel>
			<aos:docked dock="bottom" ui="footer">
				<aos:dockeditem xtype="tbfill" />
				<aos:dockeditem text="确定" onclick="_w_xh_load" icon="ok.png" />
				<aos:dockeditem onclick="#_w_xh.hide();" text="关闭"
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
							url="queryFields.jhtml?tablename=${listtablename }">
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
			<aos:gridpanel id="_g_operation"  url="listlogin.jhtml"
						   hidePagebar="true"  width="5000" autoScroll="true"
						    onitemdblclick="_w_examine_show">
				<aos:docked>
					<aos:dockeditem xtype="tbtext" text="列表" />
					<aos:dockeditem text="详情" icon="detail.png"
									onclick="_w_examine_show2" />
				</aos:docked>
				<aos:column type="rowno" />
				<aos:column header="流水号" dataIndex="id_" hidden="true" />
				<aos:column header="tid" dataIndex="tid" hidden="true" />
				<aos:column header="操作人" dataIndex="users" width="100" />
				<aos:column header="操作时间" dataIndex="operate_time" width="200" />
				<aos:column header="操作描述" dataIndex="operate_description" width="200" />
				<aos:column header="方案编号" dataIndex="task_number" width="200" />
				<aos:column header="方案名称" dataIndex="task_name" width="200" />
				<aos:column header="创建日期" dataIndex="Datatime" width="200" />
				<aos:column header="鉴定范围" dataIndex="tablename" width="200" />
				<aos:column header="鉴定描述" dataIndex="tabledesc" width="200" />
				<aos:column header="方案创建人" dataIndex="createperson" width="200" />

				<aos:column header="初鉴人中文" dataIndex="first_trialperson_cn" width="200" />
				<aos:column header="再审人中文" dataIndex="next_trialperson_cn" width="200" />
				<aos:column header="终审人中文" dataIndex="last_trialperson_cn" width="200" />
				<aos:column header="描述" dataIndex="description" width="200" />
				<aos:column header="" flex="1" />
			</aos:gridpanel>
			<aos:docked dock="bottom" ui="footer">
				<aos:dockeditem xtype="tbfill" />
				<aos:dockeditem onclick="#_w_operation.close();" text="关闭" icon="close.png" />
			</aos:docked>
		</aos:window>
		<aos:window id="_w_details" title="详情">
		<aos:formpanel id="_g_details" width="500" layout="anchor"
					   labelWidth="90">
			<aos:hiddenfield name="id_"  />
			<aos:textfield fieldLabel="任务编号" name="task_number" columnWidth="1.0" readOnly="true"/>
			<aos:textfield fieldLabel="任务名称" name="task_name" columnWidth="1.0" readOnly="true"/>
			<aos:textfield fieldLabel="时间" name="datatime" columnWidth="1.0" readOnly="true"/>
			<aos:textfield fieldLabel="鉴定范围" name="tabledesc" columnWidth="1.0" readOnly="true"/>
			<aos:textfield fieldLabel="创建人" name="createperson" columnWidth="1.0" readOnly="true"/>
			<aos:textfield fieldLabel="鉴定类型" name="checkup_type" columnWidth="1.0" readOnly="true"/>
			<aos:textfield fieldLabel="初审人" name="first_trialperson" columnWidth="1.0" readOnly="true"/>
			<aos:textfield fieldLabel="初审人中文" name="first_trialperson_cn" columnWidth="1.0" readOnly="true"/>
			<aos:textfield fieldLabel="再审人" name="next_trialperson" columnWidth="1.0" readOnly="true"/>
			<aos:textfield fieldLabel="再审人中文" name="next_trialperson_cn" columnWidth="1.0" readOnly="true"/>
			<aos:textfield fieldLabel="终审人" name="last_trialperson" columnWidth="1.0" readOnly="true"/>
			<aos:textfield fieldLabel="终审人中文" name="last_trialperson_cn" columnWidth="1.0" readOnly="true"/>
			<aos:textareafield fieldLabel="鉴定描述" name="description" columnWidth="1.0" readOnly="true"/>
		</aos:formpanel>
		<aos:docked dock="bottom" ui="footer">
			<aos:dockeditem xtype="tbfill" />
			<aos:dockeditem onclick="#_w_details.hide();" text="关闭" icon="close.png" />
		</aos:docked>
	</aos:window>
		<aos:window id="_w_checkup" title="方案列表" autoScroll="true" height="500" width="800" onshow="_w_checkup_show">
			<aos:gridpanel id="_g_checkup" region="east" url="listcheckup.jhtml"
						   hidePagebar="true" split="true" splitterBorder="0 1 0 1" width="1000" autoScroll="true"
						   onrender="_w_checkup_show">
				<aos:docked>
					<aos:dockeditem xtype="tbtext" text="列表" />
					<aos:dockeditem onclick="examine_show" text="方案详情" icon="ok.png" />
					<aos:dockeditem onclick="archive_checkup" text="档案详情" icon="query.png" />
				</aos:docked>
				<aos:column type="rowno" />
				<aos:column header="流水号" dataIndex="id_" hidden="true" />
				<aos:column header="任务编号" dataIndex="task_number"  width="80" align="true"/>
				<aos:column header="任务名称" dataIndex="task_name"  width="100" align="true"/>
				<aos:column header="时间" dataIndex="datatime" width="100" align="true" />
				<aos:column header="数据表名称" dataIndex="tablename" width="60" align="true" hidden="true"/>
				<aos:column header="鉴定范围" dataIndex="tabledesc"  width="80" align="true" />
				<aos:column header="创建人" dataIndex="createperson"  width="80" align="true" />
				<aos:column header="鉴定类型" dataIndex="checkup_type" width="60" align="true" />
				<aos:column header="初审人" dataIndex="first_trialperson" width="80" align="true"/>
				<aos:column header="初审人中文" dataIndex="first_trialperson_cn" width="80" align="true"/>
				<aos:column header="再审人" dataIndex="next_trialperson" width="80" align="true"/>
				<aos:column header="再审人中文" dataIndex="next_trialperson_cn" width="80" align="true"/>
				<aos:column header="终审人"  dataIndex="last_trialperson" width="60" align="true"/>
				<aos:column header="终审人中文"  dataIndex="last_trialperson_cn" width="60" align="true"/>
				<aos:column header="鉴定描述" dataIndex="description" width="200" align="true"/>
			</aos:gridpanel>
			<aos:docked dock="bottom" ui="footer">
				<aos:dockeditem xtype="tbfill" />
				<aos:dockeditem onclick="#_w_checkup.close();" text="关闭" icon="close.png" />
			</aos:docked>
		</aos:window>
		<aos:window id="_bloom_first_check" title="开放鉴定" width="500"
					autoScroll="true" onshow="_load_bloom_first_check">
			<aos:formpanel id="_f_bloom_first_check" width="480" layout="column">
				<aos:hiddenfield  name="id_" />
				<aos:textfield fieldLabel="数据表名称" name="tablename" columnWidth="1.0"
							   readOnly="true" />
				<aos:textfield fieldLabel="数据表描述" name="tabledesc" columnWidth="1.0"
							   readOnly="true" />
				<aos:combobox name="check_type"  fieldLabel="鉴定类型" value="开放鉴定"
							  columnWidth="1.0" readOnly="true">
					<aos:option value="开放鉴定" display="开放鉴定" />
					<aos:option value="保管期限鉴定" display="保管期限鉴定" />
					<aos:option value="密级鉴定" display="密级鉴定" />
					<aos:option value="销毁鉴定" display="销毁鉴定" />
				</aos:combobox>
				<aos:datefield name="datatime" fieldLabel="日期" format="Y-m-d"
							   columnWidth="1.0" allowBlank="false" />
				<aos:textfield fieldLabel="操作人" name="person" columnWidth="1.0" readOnly="true"/>
				<aos:textfield fieldLabel="初审人" name="first_trialperson" columnWidth="1.0"
							   readOnly="true" />
				<aos:textareafield  name="description" allowBlank="false"  columnWidth="1.0" fieldLabel="鉴定描述" readOnly="true"/>
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
		<aos:window id="_bloom_next_check" title="开放鉴定" width="500"
					autoScroll="true" onshow="_load_bloom_next_check">
			<aos:formpanel id="_f_bloom_next_check" width="480" layout="column">
				<aos:hiddenfield  name="id_" />
				<aos:textfield fieldLabel="数据表名称" name="tablename" columnWidth="1.0"
							   readOnly="true" />
				<aos:textfield fieldLabel="数据表描述" name="tabledesc" columnWidth="1.0"
							   readOnly="true" />
				<aos:combobox name="check_type"  fieldLabel="鉴定类型" value="开放鉴定"
							  columnWidth="1.0" readOnly="true">
					<aos:option value="开放鉴定" display="开放鉴定" />
					<aos:option value="保管期限鉴定" display="保管期限鉴定" />
					<aos:option value="密级鉴定" display="密级鉴定" />
					<aos:option value="销毁鉴定" display="销毁鉴定" />
				</aos:combobox>
				<aos:datefield name="datatime" fieldLabel="日期" format="Y-m-d"
							   columnWidth="1.0" allowBlank="false" />
				<aos:textfield fieldLabel="操作人" name="person" columnWidth="1.0" readOnly="true"/>
				<aos:textfield fieldLabel="再审人" name="next_trialperson" columnWidth="1.0"
							   readOnly="true" />
				<aos:textareafield  name="description" allowBlank="false"  columnWidth="1.0" fieldLabel="鉴定描述" readOnly="true"/>
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
			<aos:gridpanel id="_g_task" region="east" url="listcheckup.jhtml"
						   hidePagebar="true" split="true" splitterBorder="0 1 0 1" width="1000" autoScroll="true"
						   onrender="_w_task_show">
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
				<aos:column header="鉴定范围" dataIndex="tabledesc"  width="200" align="true" />
				<aos:column header="创建人" dataIndex="createperson"  width="200" align="true" />
				<aos:column header="鉴定类型" dataIndex="checkup_type" width="200" align="true" />
				<aos:column header="初审人" dataIndex="first_trialperson" width="200" align="true"/>
				<aos:column header="初审人中文" dataIndex="first_trialperson_cn" width="200" align="true"/>
				<aos:column header="再审人" dataIndex="next_trialperson" width="200" align="true"/>
				<aos:column header="再审人中文" dataIndex="next_trialperson_cn" width="200" align="true"/>
				<aos:column header="终审人"  dataIndex="last_trialperson" width="200" align="true"/>
				<aos:column header="终审人中文" dataIndex="last_trialperson_cn" width="200" align="true"/>
				<aos:column header="鉴定描述" dataIndex="description" width="200" align="true"/>
			</aos:gridpanel>
			<aos:docked dock="bottom" ui="footer">
				<aos:dockeditem xtype="tbfill" />
				<aos:dockeditem onclick="#_w_checkup.close();" text="关闭" icon="close.png" />
			</aos:docked>
		</aos:window>
		<aos:window id="_w_task_reset" title="分配" onshow="_w_task_load">
			<aos:formpanel id="_g_task_reset" width="500" layout="anchor"
						   labelWidth="90">
				<aos:hiddenfield name="id_"  />
				<aos:textfield fieldLabel="任务编号" name="task_number" columnWidth="1.0" readOnly="true"/>
				<aos:textfield fieldLabel="任务名称" name="task_name" columnWidth="1.0" />
				<aos:textfield fieldLabel="时间" name="datatime" columnWidth="1.0" readOnly="true"/>
				<aos:textfield fieldLabel="鉴定范围" name="tabledesc" columnWidth="1.0" readOnly="true"/>
				<aos:textfield fieldLabel="创建人" name="createperson" columnWidth="1.0" readOnly="true"/>
				<aos:textfield fieldLabel="鉴定类型" name="checkup_type" columnWidth="1.0" readOnly="true"/>
				<aos:combobox name="first_trialperson" id="first_trialperson" fieldLabel="初审人" allowBlank="false"
							  columnWidth="1.0" fields="['account_','name_']" displayField="account_"
							  valueField="account_" onselect="add_first_trialperson"
							  url="queryUsers.jhtml">
				</aos:combobox>
				<aos:gridpanel id="_g_first_trialperson"  split="true" hidePagebar="true" autoScroll="true"
							   height="100" width="500" drag="true">
					<aos:menu>
						<aos:menuitem text="删除" onclick="_g_first_trialperson_del" icon="del.png" />
					</aos:menu>
					<aos:column header="初审人" dataIndex="fieldennames" width="200"/>
					<aos:column header="初审人中文" dataIndex="fieldcnnames" width="200"/>
				</aos:gridpanel>
				<aos:combobox name="next_trialperson" id="next_trialperson" fieldLabel="再审人" allowBlank="false"
							  columnWidth="1.0" fields="['account_','name_']" displayField="account_"
							  valueField="account_" onselect="add_next_trialperson"
							  url="queryUsers.jhtml">
				</aos:combobox>
				<aos:gridpanel id="_g_next_trialperson" split="true" hidePagebar="true" autoScroll="true"
							   height="100" width="500" drag="true">
					<aos:menu>
						<aos:menuitem text="删除" onclick="_g_next_trialperson_del" icon="del.png" />
					</aos:menu>
					<aos:column header="再审人" dataIndex="fieldennames" width="200"/>
					<aos:column header="在审人中文" dataIndex="fieldcnnames" width="200"/>
				</aos:gridpanel>
				<aos:combobox name="last_trialperson" id="last_trialperson" fieldLabel="终审人" allowBlank="false"
							  columnWidth="1.0" fields="['account_','name_']" displayField="account_"
							  valueField="account_" onselect="add_last_trialperson"
							  url="queryUsers.jhtml">
				</aos:combobox>
				<aos:gridpanel id="_g_last_trialperson" split="true" hidePagebar="true" autoScroll="true"
							   height="100" width="500" drag="true">
					<aos:menu>
						<aos:menuitem text="删除" onclick="_g_last_trialperson_del" icon="del.png" />
					</aos:menu>
					<aos:column header="终审人" dataIndex="fieldennames" width="200"/>
					<aos:column header="终审人中文" dataIndex="fieldcnnames" width="200"/>
				</aos:gridpanel>
				<aos:textareafield fieldLabel="鉴定描述" name="description" columnWidth="1.0"/>

			</aos:formpanel>

			<aos:docked dock="bottom" ui="footer">
				<aos:dockeditem xtype="tbfill" />
				<aos:dockeditem onclick="_w_details_add" text="确定" icon="add.png" />
				<aos:dockeditem onclick="#_w_task_reset.hide();" text="关闭" icon="close.png" />
			</aos:docked>
		</aos:window>
		<!--初审人-->
		<aos:window id="_w_first_trial" title="初鉴列表" autoScroll="true" height="500" width="800" onshow="_w_first_trial_show">
			<aos:gridpanel id="_g_first_trial" region="east" url="listfirsttrial.jhtml"
						   hidePagebar="true" split="true" splitterBorder="0 1 0 1" width="1000" autoScroll="true">
				<aos:docked>
					<aos:dockeditem xtype="tbtext" text="列表" />
					<aos:dockeditem onclick="first_trial_examine_show" text="操作" icon="ok.png" />
				</aos:docked>
				<aos:column type="rowno" />
				<aos:column header="流水号" dataIndex="id_" hidden="true" />
				<aos:column header="任务编号" dataIndex="task_number"  width="200" align="true"/>
				<aos:column header="任务名称" dataIndex="task_name"  width="200" align="true"/>
				<aos:column header="时间" dataIndex="Datatime" width="200" align="true" />
				<aos:column header="数据表名称" dataIndex="tablename" width="200" align="true" hidden="true"/>
				<aos:column header="鉴定范围" dataIndex="tabledesc"  width="200" align="true" />
				<aos:column header="创建人" dataIndex="createperson"  width="200" align="true" />
				<aos:column header="鉴定类型" dataIndex="checkup_type" width="200" align="true" />
				<aos:column header="鉴定描述" dataIndex="description" width="200" align="true"/>
			</aos:gridpanel>
			<aos:docked dock="bottom" ui="footer">
				<aos:dockeditem xtype="tbfill" />
				<aos:dockeditem onclick="#_w_first_trial.close();" text="关闭" icon="close.png" />
			</aos:docked>
		</aos:window>
		<!--初审人-->
		<aos:window id="_w_next_trial" title="再审列表" autoScroll="true" height="500" width="800" onshow="_w_next_trial_show">
			<aos:gridpanel id="_g_next_trial" region="east" url="listnexttrial.jhtml"
						   hidePagebar="true" split="true" splitterBorder="0 1 0 1" width="1000" autoScroll="true">
				<aos:docked>
					<aos:dockeditem xtype="tbtext" text="列表" />
					<aos:dockeditem onclick="next_trial_examine_show" text="操作" icon="ok.png" />
				</aos:docked>
				<aos:column type="rowno" />
				<aos:column header="流水号" dataIndex="id_" hidden="true" />
				<aos:column header="任务编号" dataIndex="task_number"  width="200" align="true"/>
				<aos:column header="任务名称" dataIndex="task_name"  width="200" align="true"/>
				<aos:column header="时间" dataIndex="Datatime" width="200" align="true" />
				<aos:column header="数据表名称" dataIndex="tablename" width="200" align="true" hidden="true"/>
				<aos:column header="鉴定范围" dataIndex="tabledesc"  width="200" align="true" />
				<aos:column header="创建人" dataIndex="createperson"  width="200" align="true" />
				<aos:column header="鉴定类型" dataIndex="checkup_type" width="200" align="true" />
				<aos:column header="鉴定描述" dataIndex="description" width="200" align="true"/>
			</aos:gridpanel>
			<aos:docked dock="bottom" ui="footer">
				<aos:dockeditem xtype="tbfill" />
				<aos:dockeditem onclick="#_w_next_trial.close();" text="关闭" icon="close.png" />
			</aos:docked>
		</aos:window>
		<!--初审人-->
		<aos:window id="_w_last_trial" title="终审列表" autoScroll="true" height="500" width="800" onshow="_w_last_trial_show">
			<aos:gridpanel id="_g_last_trial" region="east" url="listlasttrial.jhtml"
						   hidePagebar="true" split="true" splitterBorder="0 1 0 1" width="1000" autoScroll="true">
				<aos:docked>
					<aos:dockeditem xtype="tbtext" text="列表" />
					<aos:dockeditem onclick="last_trial_examine_show" text="操作" icon="ok.png" />
				</aos:docked>
				<aos:column type="rowno" />
				<aos:column header="流水号" dataIndex="id_" hidden="true" />
				<aos:column header="任务编号" dataIndex="task_number"  width="200" align="true"/>
				<aos:column header="任务名称" dataIndex="task_name"  width="200" align="true"/>
				<aos:column header="时间" dataIndex="Datatime" width="200" align="true" />
				<aos:column header="数据表名称" dataIndex="tablename" width="200" align="true" hidden="true"/>
				<aos:column header="鉴定范围" dataIndex="tabledesc"  width="200" align="true" />
				<aos:column header="创建人" dataIndex="createperson"  width="200" align="true" />
				<aos:column header="鉴定类型" dataIndex="checkup_type" width="200" align="true" />
				<aos:column header="鉴定描述" dataIndex="description" width="200" align="true"/>
			</aos:gridpanel>
			<aos:docked dock="bottom" ui="footer">
				<aos:dockeditem xtype="tbfill" />
				<aos:dockeditem onclick="#_w_last_trial.close();" text="关闭" icon="close.png" />
			</aos:docked>
		</aos:window>
		<!--初审人列表-->
		<aos:window id="_w_first_trial_reset" title="初鉴">
			<aos:formpanel id="_g_first_trial_reset" width="500" layout="column"
						   labelWidth="90">
				<aos:hiddenfield name="id_" />
				<aos:textfield fieldLabel="任务编号" name="task_number" columnWidth="1.0" readOnly="true"/>
				<aos:textfield fieldLabel="任务名称" name="task_name" columnWidth="1.0" />
				<aos:textfield fieldLabel="时间" name="Datatime" columnWidth="1.0" readOnly="true"/>
				<aos:textfield fieldLabel="鉴定范围" name="tabledesc" columnWidth="0.8" readOnly="true"/>
				<aos:button text="详情" onclick="first_archive_details" margin="0 0 0 10"  columnWidth="0.2"/>
				<aos:textfield fieldLabel="创建人" name="createperson" columnWidth="1.0" readOnly="true"/>
				<aos:textfield fieldLabel="鉴定类型" name="checkup_type" columnWidth="1.0" readOnly="true"/>
				<aos:textareafield fieldLabel="鉴定描述" name="description" columnWidth="1.0"/>
				<aos:textareafield  fieldLabel="再审意见" id="next_message" name="next_message" columnWidth="1.0" />
			</aos:formpanel>
			<aos:docked dock="bottom" ui="footer">
				<aos:dockeditem xtype="tbfill" />
				<aos:dockeditem onclick="#_w_first_trial_reset.hide();" text="关闭" icon="close.png" />
			</aos:docked>
		</aos:window>
		<aos:window id="_w_next_trial_reset" title="再审">
			<aos:formpanel id="_g_next_trial_reset" width="500" layout="column"
						   labelWidth="90">
				<aos:hiddenfield name="id_" />
				<aos:textfield fieldLabel="任务编号" name="task_number" columnWidth="1.0" readOnly="true"/>
				<aos:textfield fieldLabel="任务名称" name="task_name" columnWidth="1.0" />
				<aos:textfield fieldLabel="时间" name="Datatime" columnWidth="1.0" readOnly="true"/>
				<aos:textfield fieldLabel="鉴定范围" name="tabledesc" columnWidth="0.8" readOnly="true"/>
				<aos:button text="详情" onclick="next_archive_details" margin="0 0 0 10"  columnWidth="0.2"/>
				<aos:textfield fieldLabel="创建人" name="createperson" columnWidth="1.0" readOnly="true"/>
				<aos:textfield fieldLabel="鉴定类型" name="checkup_type" columnWidth="1.0" readOnly="true"/>
				<aos:textareafield fieldLabel="鉴定描述" name="description" columnWidth="1.0"/>
				<aos:textareafield  fieldLabel="终审意见" id="last_message" name="last_message" columnWidth="1.0" />
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
				<aos:textfield fieldLabel="鉴定范围" name="tabledesc" columnWidth="0.8" readOnly="true"/>
				<aos:button text="详情" onclick="last_archive_details" margin="0 0 0 10"  columnWidth="0.2"/>
				<aos:textfield fieldLabel="创建人" name="createperson" columnWidth="1.0" readOnly="true"/>
				<aos:textfield fieldLabel="鉴定类型" name="checkup_type" columnWidth="1.0" readOnly="true"/>
				<aos:textareafield fieldLabel="鉴定描述" name="description" columnWidth="1.0"/>
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
				<aos:textfield fieldLabel="鉴定范围" name="tablename" columnWidth="1.0" readOnly="true"/>
				<aos:textareafield fieldLabel="鉴定描述" name="tabledesc" columnWidth="1.0" readOnly="true"/>

				<aos:textfield fieldLabel="方案创建人" name="createperson" columnWidth="1.0" readOnly="true"/>

				<aos:textfield fieldLabel="初审人中文" name="first_trialperson_cn" columnWidth="1.0" readOnly="true"/>
				<aos:textfield fieldLabel="再审人中文" name="next_trialperson_cn" columnWidth="1.0" readOnly="true"/>
				<aos:textfield fieldLabel="终审人中文" name="last_trialperson_cn" columnWidth="1.0" readOnly="true"/>
				<aos:textareafield fieldLabel="描述" name="description" columnWidth="1.0" readOnly="true"/>
			</aos:formpanel>
			<aos:docked dock="bottom" ui="footer">
				<aos:dockeditem xtype="tbfill" />
				<aos:dockeditem onclick="#_w_details.hide();" text="关闭" icon="close.png" />
			</aos:docked>
		</aos:window>
	</aos:viewport>
	<script>
	//跳转主页
	function _w_tablename_init(){	
		// alert(listtablename);
		var listTablename = Ext.getCmp("listTablename").value;
		var tablenamedesc = Ext.getCmp("listTablename").getRawValue();
		window.location.href="initData.jhtml?listtablename="+listTablename+"&tablenamedesc="+encodeURI(encodeURI(tablenamedesc));		
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
	//加载数据
	function _load_borrow(){
		 //传值
		 _f_checkup.form.findField("tablename").setValue(Ext.getCmp("tablename").getValue());
		//传递描述
		 _f_checkup.form.findField("tabledesc").setValue(Ext.getCmp("listTablename").getValue());
		 AOS.ajax({
             url: 'jymessage.jhtml',
             method:'post',           
             ok: function (data) {
            	 _f_checkup.form.setValues(data);
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
		//先根据当前条件在后台设计一个query
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
		Ext.getCmp("listTablename").setValue(listTablename);
		Ext.getCmp("listTablename").setRawValue(tablenamedesc);
		Ext.getCmp("tablename").setValue(listTablename);


		}
		function _w_bloom_show(){
			AOS.reset(_f_bloom);
			_w_bloom.show();
		}
	function _w_bgqx_show(){
		AOS.reset(_f_bgqx);
		_w_bgqx.show();
	}
	function _w_mj_show(){
		AOS.reset(_f_mj);
		_w_mj.show();
	}
	function _w_xh_show(){
		AOS.reset(_f_xh);
		_w_xh.show();
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

	function _load_bgqx(){
		var listTablename ="<%=request.getParameter("listtablename")%>";
		var tablenamedesc ="<%=request.getAttribute("tablenamedesc")%>";
		//让指定文本框赋值
		_f_bgqx.form.findField("tablename").setValue(listTablename);
		//传递描述
		_f_bgqx.form.findField("tabledesc").setValue(tablenamedesc);
		var time = (new Date).getTime();
		var yesday = new Date(time); // 获取的是前一天日期
		yesday = yesday.getFullYear() + "-" + (yesday.getMonth()> 9 ? (yesday.getMonth() + 1) : "0" + (yesday.getMonth() + 1)) + "-" +(yesday.getDate()> 9 ? (yesday.getDate()) : "0" + (yesday.getDate()));
		_f_bgqx.form.findField("datatime").setValue(yesday);
		var user ="<%=request.getAttribute("user")%>";
		_f_bgqx.form.findField("createperson").setValue(user);
		//设计一个随机数编号
		_f_bgqx.form.findField("task_number").setValue(generateUUID());
	}
	function _load_mj(){
		var listTablename ="<%=request.getParameter("listtablename")%>";
		var tablenamedesc ="<%=request.getAttribute("tablenamedesc")%>";
		//让指定文本框赋值
		_f_mj.form.findField("tablename").setValue(listTablename);
		//传递描述
		_f_mj.form.findField("tabledesc").setValue(tablenamedesc);
		var time = (new Date).getTime();
		var yesday = new Date(time); // 获取的是前一天日期
		yesday = yesday.getFullYear() + "-" + (yesday.getMonth()> 9 ? (yesday.getMonth() + 1) : "0" + (yesday.getMonth() + 1)) + "-" +(yesday.getDate()> 9 ? (yesday.getDate()) : "0" + (yesday.getDate()));
		_f_mj.form.findField("datatime").setValue(yesday);
		var user ="<%=request.getAttribute("user")%>";
		_f_mj.form.findField("createperson").setValue(user);
		//设计一个随机数编号
		_f_mj.form.findField("task_number").setValue(generateUUID());
	}

	function _load_xh(){
		var listTablename ="<%=request.getParameter("listtablename")%>";
		var tablenamedesc ="<%=request.getAttribute("tablenamedesc")%>";
		//让指定文本框赋值
		_f_xh.form.findField("tablename").setValue(listTablename);
		//传递描述
		_f_xh.form.findField("tabledesc").setValue(tablenamedesc);
		var time = (new Date).getTime();
		var yesday = new Date(time); // 获取的是前一天日期
		yesday = yesday.getFullYear() + "-" + (yesday.getMonth()> 9 ? (yesday.getMonth() + 1) : "0" + (yesday.getMonth() + 1)) + "-" +(yesday.getDate()> 9 ? (yesday.getDate()) : "0" + (yesday.getDate()));
		_f_xh.form.findField("datatime").setValue(yesday);
		var user ="<%=request.getAttribute("user")%>";
		_f_xh.form.findField("createperson").setValue(user);
		//设计一个随机数编号
		_f_xh.form.findField("task_number").setValue(generateUUID());
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
	//加载(密级)数据
	function _w_mj_load(){
		var selection = AOS.selection(_g_data, 'id_');
		AOS.ajax({
			url: 'addmjForm.jhtml',
			forms:_f_mj,
			params:{aos_rows_: selection,
				flag:1},
			ok: function (data) {
				if(data.appcode===1){
					AOS.tip("添加成功!");
					_w_mj.hide();
				}else if(data.appcode===-1){
					AOS.tip("添加失败!");
				}
			}
		});
	}
	//加载(销毁)数据
	function _w_xh_load(){
		var selection = AOS.selection(_g_data, 'id_');
		AOS.ajax({
			url: 'addxhForm.jhtml',
			forms:_f_xh,
			params:{aos_rows_: selection,
				flag:1},
			ok: function (data) {
				if(data.appcode===1){
					AOS.tip("添加成功!");
					_w_xh.hide();
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
		//鉴定历史
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
			return '<a><font color="green">已鉴定</font></a>';
		} else {
			return '<a><font color="red">未鉴定</font></a>';
		}
	}
	function fn_next_flag_render(value, metaData, record, rowIndex, colIndex,
							store) {
		if (value == 1) {
			return '<a><font color="green">已鉴定</font></a>';
		} else {
			return '<a><font color="red">未鉴定</font></a>';
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
		var record = AOS.selectone(_g_operation);
		_w_update.show();
		_g_update.loadRecord(record);
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
	//修改鉴定
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
	function _update_checkup(){
		//判断操作状态是不是已鉴定
		var first_flag=_g_update.form.findField("first_flag").getValue();
		var next_flag=_g_update.form.findField("next_flag").getValue();
		if(first_flag==="1"&&next_flag==="1"){
			AOS.err("已经鉴定，不能修改数据!");
			return ;
		}
		//修改鉴定
		AOS.ajax({
			url: 'updatecheckup.jhtml',
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
				url: 'deletecheckup.jhtml',
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
	//鉴定列表展开操作
	function _go_checkup(){
		_w_checkup.show();
	}
	function _w_checkup_show(){
		_g_checkup_store.reload();
	}
	function _w_task_show(){
		_g_task_store.reload();
	}
	//初审鉴定
	function fn_first_check(){
		//1.判断选中条目
		var selection = AOS.selection(_g_checkup, 'id_');
		if (AOS.empty(selection)) {
			AOS.tip('请选择要鉴定的数据!');
			return;
		}
		//2.判断是不是对应可以执行鉴定的用户
		var row = AOS.selectone(_g_checkup);
		var first_person=row.data.first_trialperson;
		//得到当前用户
		var root="<%=session.getAttribute("user")%>";
		if(!(first_person===root)){
			AOS.err('当前用户和初审用户不匹配不能完成鉴定!');
			return;
		}
		//3.判断是否已经完成了鉴定操作
		var first_flag=row.data.first_flag;
		if(first_flag==="1"){
			AOS.err('已经完成初审操作，无需再次操作!');
			return;
		}
		//4.判断当前所有鉴定的类型、
		var check_type=row.data.check_type;
		if(check_type==="开放鉴定"){
			_bloom_first_check.show();
		}else if(check_type==="保管期限鉴定"){
			_bgqx_first_check.show();
		}else if(check_type==="密级鉴定"){
			_mj_first_check.show();
		}else if(check_type==="销毁鉴定"){
			_xh_first_check.show();
		}
	}
	function _load_bloom_first_check(){
		var record = AOS.selectone(_g_checkup);
		_f_bloom_first_check.loadRecord(record);
	}
	function _load_bloom_next_check(){
		var record = AOS.selectone(_g_checkup);
			_f_bloom_next_check.loadRecord(record);
	}
	function _load_bgqx_first_check(){
		var record = AOS.selectone(_g_checkup);
		_f_bgqx_first_check.loadRecord(record);
	}
	function _load_bgqx_next_check(){
		var record = AOS.selectone(_g_checkup);
			_f_bgqx_next_check.loadRecord(record);

	}
	function _load_mj_first_check(){
		var record = AOS.selectone(_g_checkup);
		_f_mj_first_check.loadRecord(record);
	}
	function _load_mj_next_check(){
		var record = AOS.selectone(_g_checkup);
			_f_mj_next_check.loadRecord(record);

	}
	//开放鉴定初审执行
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
			url: 'bloomfirstcheckup.jhtml',
			params:{kaifangdescription:xq},
			method:'post',
			ok: function (data) {
				if(data.appcode===1){
					AOS.tip("操作成功!");
					_bloom_first_check.hide();
					_g_checkup_store.reload();
				}else if(data.appcode===-1){
					AOS.tip("操作失败!");
				}
			}
		});
	}
	//保管期限鉴定初审执行
	function _bgqx_first_check_load(){
		AOS.ajax({
			forms:_f_bgqx_first_check,
			url: 'bgqxfirstcheckup.jhtml',
			method:'post',
			ok: function (data) {
				if(data.appcode===1){
					AOS.tip("操作成功!");
					_bgqx_first_check.hide();
					_g_checkup_store.reload();
				}else if(data.appcode===-1){
					AOS.tip("操作失败!");
				}
			}
		});
	}
	//密级鉴定初审执行
	function _mj_first_check_load(){
		var mj_radio_check = Ext.getCmp('_first_mj_radio').items;
		var xq = '';
		for(var i = 0; i < mj_radio_check.length; i++){
			if(mj_radio_check.get(i).checked){
				xq =mj_radio_check.get(i).boxLabel;
			}
		}
		AOS.ajax({
			forms:_f_mj_first_check,
			url: 'mjfirstcheckup.jhtml',
			params:{mjdescription:xq},
			method:'post',
			ok: function (data) {
				if(data.appcode===1){
					AOS.tip("操作成功!");
					_mj_first_check.hide();
					_g_checkup_store.reload();
				}else if(data.appcode===-1){
					AOS.tip("操作失败!");
				}
			}
		});
	}
	//密级鉴定再审执行
	function _mj_next_check_load(){
		var mj_radio_check = Ext.getCmp('_next_mj_radio').items;
		var xq = '';
		for(var i = 0; i < mj_radio_check.length; i++){
			if(mj_radio_check.get(i).checked){
				xq =mj_radio_check.get(i).boxLabel;
			}
		}
		AOS.ajax({
			forms:_f_mj_next_check,
			url: 'mjnextcheckup.jhtml',
			params:{mjdescription:xq},
			method:'post',
			ok: function (data) {
				if(data.appcode===1){
					AOS.tip("操作成功!");
					_mj_next_check.hide();
					_g_checkup_store.reload();
				}else if(data.appcode===-1){
					AOS.tip("操作失败!");
				}
			}
		});
	}
	//开放鉴定再审执行
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
			url: 'bloomnextcheckup.jhtml',
			params:{kaifangdescription:xq},
			method:'post',
			ok: function (data) {
				if(data.appcode===1){
					AOS.tip(data.appmsg);
					_bloom_next_check.hide();
					_g_checkup_store.reload();
				}else if(data.appcode===-1){
					AOS.tip(data.appmsg);
				}
			}
		});
	}
	//保管期限鉴定初审执行
	function _bgqx_next_check_load(){
		AOS.ajax({
			forms:_f_bgqx_next_check,
			url: 'bgqxnextcheckup.jhtml',
			method:'post',
			ok: function (data) {
				if(data.appcode===1){
					AOS.tip("操作成功!");
					_bgqx_next_check.hide();
					_g_checkup_store.reload();
				}else if(data.appcode===-1){
					AOS.tip("操作失败!");
				}
			}
		});
	}
	//展开鉴定列表
	function _list_checkup(){
		_w_checkup.show();

	}
	//展开详情列表
	function examine_show(){
		var selection = AOS.selection(_g_checkup, 'id_');
		if (AOS.empty(selection)) {
			AOS.tip('请选择要查看详情的数据!');
			return;
		}else{
			//弹出审核窗口
			var record = AOS.selectone(_g_checkup);
			if (record) {
				_w_details.show();
				_g_details.loadRecord(record);
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
				Ext.getCmp("_g_first_trialperson").getStore().removeAll();
				Ext.getCmp("_g_next_trialperson").getStore().removeAll();
				Ext.getCmp("_g_last_trialperson").getStore().removeAll();
				Ext.getCmp("first_trialperson").setValue("");
				Ext.getCmp("first_trialperson").setValue("");
				Ext.getCmp("first_trialperson").setValue("");
			}
		}
	}
	//任务分配
	function _list_task(){
		_w_task.show();
	}
	function add_first_trialperson(){
		var fieldenname=_g_task_reset.getForm().findField('first_trialperson').getValue();
		var fieldcnname = Ext.getCmp("first_trialperson").displayTplData[0].name_;
		var params = [{
			'fieldennames':fieldenname,
			'fieldcnnames':fieldcnname
		}];
		var count=_g_first_trialperson_store.getCount();
		if(count>0){
			for(var i=0;i<count;i++){
				var leftenzd = _g_first_trialperson_store.getAt(i).get("fieldennames");
				if(leftenzd===fieldenname){
					return;
				}
			}
		}
		///_g_field_store.insert(0,params);
		_g_first_trialperson_store.loadData(params,true);
	}
	function add_next_trialperson(){
		var fieldenname=_g_task_reset.getForm().findField('next_trialperson').getValue();
		var fieldcnname = Ext.getCmp("next_trialperson").displayTplData[0].name_;
		var params = [{
			'fieldennames':fieldenname,
			'fieldcnnames':fieldcnname
		}];
		var count=_g_next_trialperson_store.getCount();
		if(count>0){
			for(var i=0;i<count;i++){
				var leftenzd = _g_next_trialperson_store.getAt(i).get("fieldennames");
				if(leftenzd===fieldenname){
					return;
				}
			}
		}
		///_g_field_store.insert(0,params);
		_g_next_trialperson_store.loadData(params,true);
	}
	function add_last_trialperson(){
		var fieldenname=_g_task_reset.getForm().findField('last_trialperson').getValue();
		var fieldcnname = Ext.getCmp("last_trialperson").displayTplData[0].name_;
		var params = [{
			'fieldennames':fieldenname,
			'fieldcnnames':fieldcnname
		}];
		var count=_g_last_trialperson_store.getCount();
		if(count>0){
			for(var i=0;i<count;i++){
				var leftenzd = _g_last_trialperson_store.getAt(i).get("fieldennames");
				if(leftenzd===fieldenname){
					return;
				}
			}
		}
		///_g_field_store.insert(0,params);
		_g_last_trialperson_store.loadData(params,true);
	}
	//删除当前选中的节点
	function _g_first_trialperson_del(){
		var row=_g_first_trialperson.getSelectionModel().getSelection();
		//右侧删除，左侧添加
		_g_first_trialperson_store.remove(row);
	}
	function _g_next_trialperson_del(){
		var row=_g_next_trialperson.getSelectionModel().getSelection();
		//右侧删除，左侧添加
		_g_next_trialperson_store.remove(row);
	}
	function _g_last_trialperson_del(){
		var row=_g_last_trialperson.getSelectionModel().getSelection();
		//右侧删除，左侧添加
		_g_last_trialperson_store.remove(row);
	}
	function _w_details_add(){
		//1.把三种人编写成集合的形式
		var s=Ext.getCmp('_g_first_trialperson').getStore();
		var first_fieldennames="";
		for(var i = 0 ;i< s.getCount(); i++){
			first_fieldennames+=s.getAt(i).get('fieldennames')+",";
		}
		//去掉最后一个字符
		first_fieldennames=first_fieldennames.substring(0,first_fieldennames.length-1);

		var ss=Ext.getCmp('_g_first_trialperson').getStore();
		var first_fieldcnnames="";
		for(var i = 0 ;i< ss.getCount(); i++){
			first_fieldcnnames+=ss.getAt(i).get('fieldcnnames')+",";
		}
		//去掉最后一个字符
		first_fieldcnnames=first_fieldcnnames.substring(0,first_fieldcnnames.length-1);

		var s1=Ext.getCmp('_g_next_trialperson').getStore();
		var next_fieldennames="";
		for(var i = 0 ;i< s1.getCount(); i++){
			next_fieldennames+=s1.getAt(i).get('fieldennames')+",";
		}
		//去掉最后一个字符
		next_fieldennames=next_fieldennames.substring(0,next_fieldennames.length-1);

		var ss1=Ext.getCmp('_g_next_trialperson').getStore();
		var next_fieldcnnames="";
		for(var i = 0 ;i< ss1.getCount(); i++){
			next_fieldcnnames+=ss1.getAt(i).get('fieldcnnames')+",";
		}
		//去掉最后一个字符
		next_fieldcnnames=next_fieldcnnames.substring(0,next_fieldcnnames.length-1);

		var s2=Ext.getCmp('_g_last_trialperson').getStore();
		var last_fieldennames="";
		for(var i = 0 ;i< s2.getCount(); i++){
			last_fieldennames+=s2.getAt(i).get('fieldennames')+",";
		}
		//去掉最后一个字符
		last_fieldennames=last_fieldennames.substring(0,last_fieldennames.length-1);

		var ss2=Ext.getCmp('_g_last_trialperson').getStore();
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
				var leftenzd = _g_task_store.getAt(i).get("first_trialperson");
				var rightcnzd = _g_task_store.getAt(i).get("first_trialperson_cn");
				var leftenzds=leftenzd.split(',');
				var rightcnzds=rightcnzd.split(',');
				if(leftenzds.length===rightcnzds.length){
					var strtemp='';
					for(var i=0;i<leftenzds.length; i++){
						var params = [{
							'fieldennames':leftenzds[i],
							'fieldcnnames':rightcnzds[i]
						}];
						_g_first_trialperson_store.loadData(params,true);
					}
				}
			}
		}
	}
	function _next_field(){
		var count=_g_task_store.getCount();
		if(count>0){
			for(var i=0;i<count;i++){
				var leftenzd = _g_task_store.getAt(i).get("next_trialperson");
				var rightcnzd = _g_task_store.getAt(i).get("next_trialperson_cn");
				var leftenzds=leftenzd.split(',');
				var rightcnzds=rightcnzd.split(',');
				if(leftenzds.length===rightcnzds.length){
					var strtemp='';
					for(var i=0;i<leftenzds.length; i++){
						var params = [{
							'fieldennames':leftenzds[i],
							'fieldcnnames':rightcnzds[i]
						}];
						_g_next_trialperson_store.loadData(params,true);
					}
				}
			}
		}
	}
	function _last_field(){
		var count=_g_task_store.getCount();
		if(count>0){
			for(var i=0;i<count;i++){
				var leftenzd = _g_task_store.getAt(i).get("last_trialperson");
				var rightcnzd = _g_task_store.getAt(i).get("last_trialperson_cn");
				var leftenzds=leftenzd.split(',');
				var rightcnzds=rightcnzd.split(',');
				if(leftenzds.length===rightcnzds.length){
					var strtemp='';
					for(var i=0;i<leftenzds.length; i++){
						var params = [{
							'fieldennames':leftenzds[i],
							'fieldcnnames':rightcnzds[i]
						}];
						_g_last_trialperson_store.loadData(params,true);
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
		window.parent.fnaddtab('','档案详情','/archive/checkup/archive_details.jhtml?id_='+id_);
	}
	//档案详情
	function archive_checkup(){
		var selection = AOS.selection(_g_checkup, 'id_');
		if (AOS.empty(selection)) {
			AOS.tip('查看详情前请先选中数据。');
			return;
		}
		var id_=AOS.selectone(_g_checkup).raw.id_;
		window.parent.fnaddtab('','档案详情','/archive/checkup/archive_details.jhtml?id_='+id_);
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
	function _w_next_trial_show(){
		var user="<%=session.getAttribute("user")%>";
		var params = {
			user: user
		};
		//这个Store的命名规则为：表格ID+"_store"。
		_g_next_trial_store.getProxy().extraParams = params;
		_g_next_trial_store.load();
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
	function _check_first(){
		var user="<%=session.getAttribute("user")%>";
		_w_first_trial.show();
	}
	function _check_next(){
		var user="<%=session.getAttribute("user")%>";
		_w_next_trial.show();
	}
	function _check_last(){
		var user="<%=session.getAttribute("user")%>";
		_w_last_trial.show();
	}
	function first_trial_examine_show(){
		var selection = AOS.selection(_g_first_trial, 'id_');
		if (AOS.empty(selection)) {
			AOS.tip('请选择要操作的方案!');
			return;
		}else{
			//弹出审核窗口
			var record = AOS.selectone(_g_first_trial);
			if (record) {
				_w_first_trial_reset.show();
				_g_first_trial_reset.loadRecord(record);
				//此时看看有没有再审意见，如果有就将控件展示出来
				AOS.ajax({
					url: 'getNextTrial.jhtml',
					params: {
						aos_rows_: record.data.id_
					},
					ok: function (data) {
						if(data.appmsg===""){
							Ext.getCmp("next_message").hide();
						}else{
							_g_first_trial_reset.getForm().findField('next_message').setValue(data.appmsg);

						}
					}
				});
			}
		}
	}
	function next_trial_examine_show(){
		var selection = AOS.selection(_g_next_trial, 'id_');
		if (AOS.empty(selection)) {
			AOS.tip('请选择要操作的方案!');
			return;
		}else{
			//弹出审核窗口
			var record = AOS.selectone(_g_next_trial);
			if (record) {
				_w_next_trial_reset.show();
				_g_next_trial_reset.loadRecord(record);
				//此时看看有没有再审意见，如果有就将控件展示出来
				AOS.ajax({
					url: 'getLastTrial.jhtml',
					params: {
						aos_rows_: record.data.id_
					},
					ok: function (data) {
						if(data.appmsg===""){
							Ext.getCmp("last_message").hide();
						}else{
							_g_next_trial_reset.getForm().findField('last_message').setValue(data.appmsg);

						}
					}
				});
			}
		}
	}
	function last_trial_examine_show(){
		var selection = AOS.selection(_g_last_trial, 'id_');
		if (AOS.empty(selection)) {
			AOS.tip('请选择要操作的方案!');
			return;
		}else{
			//弹出审核窗口
			var record = AOS.selectone(_g_last_trial);
			if (record) {
				_w_last_trial_reset.show();
				_g_last_trial_reset.loadRecord(record);
				//此时看看有没有再审意见，如果有就将控件展示出来
			}
		}
	}
	function first_archive_details(){
		var id_=AOS.selectone(_g_first_trial).raw.id_;
		window.parent.fnaddtab('','档案详情','/archive/checkup/first_archive_details.jhtml?id_='+id_);
	}
	function next_archive_details(){
		var id_=AOS.selectone(_g_next_trial).raw.id_;
		window.parent.fnaddtab('','档案详情','/archive/checkup/next_archive_details.jhtml?id_='+id_);
	}
	function last_archive_details(){
		var id_=AOS.selectone(_g_last_trial).raw.id_;
		window.parent.fnaddtab('','档案详情','/archive/checkup/last_archive_details.jhtml?id_='+id_);
	}
	</script>
</aos:onready>
</aos:html>
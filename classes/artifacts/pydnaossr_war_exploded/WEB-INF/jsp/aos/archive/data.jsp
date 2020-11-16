 <%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="/WEB-INF/tld/aos.tld" prefix="aos"%>
<%@ taglib uri="/WEB-INF/tld/cll.tld" prefix="c"%>
<aos:html>
<aos:head title="数据信息">
	<aos:include lib="ext,swfupload" />
	<aos:base href="archive/data" />
</aos:head>
<aos:body>
	
</aos:body>
<aos:onready>
	<aos:viewport layout="border">
		<aos:gridpanel id="_g_data" url="listAccounts.jhtml" region="center"
			onrender="_g_data_query" pageSize="${pagesize }" onitemdblclick="fn_g_data" >
			<aos:docked>		
				<aos:hiddenfield id="appraisal" name="appraisal" />
				<aos:dockeditem text="查询" icon="query.png" onclick="_w_query_show" />
				<aos:dockeditem text="新增" id="add_btn" icon="add2.png"
					onclick="_w_data_show" />
				<aos:button text="删除" icon="del2.png" scale="small" margin="0 0 0 0">
					<aos:menu plain="false">
						<aos:menuitem text="单项删除" icon="del2.png" onclick="_g_data_del" />
						<aos:menuitem text="全部删除" icon="del.png"
							onclick="_g_data_del_term" />
					</aos:menu>
				</aos:button>
				<aos:dockeditem text="批量修改" icon="edit.png" onclick="_g_data_edit_term" />
				<aos:dockeditem text="电子文件" icon="picture.png"
					onclick="_w_picture_show" />
				<aos:dockeditem text="设置录入" icon="layout.png"
					onclick="_w_input_show" />
				<aos:dockeditem text="设置" icon="config1.png" onclick="_w_config_show" />
				<aos:button text="导出" icon="icon154.png" scale="small"
					margin="0 0 0 0">
					<aos:menu plain="false">
						<aos:menuitem text="导出XLS报表" icon="icon70.png" onclick="fn_xls" />
						<aos:menuitem text="导出XLSX报表" icon="icon7.png" onclick="fn_xlsx" />
						<aos:menuitem text="导出XML数据" icon="icon7.png" onclick="fn_xml" />
					</aos:menu>

				</aos:button>				
				
				<aos:dockeditem text="导入" icon="folder8.png" onclick="_w_import_show" />
				<aos:dockeditem xtype="tbfill" />
				<aos:triggerfield id="tablename" width="200" value="${tablename }"
					style="display:'none'" />
				<aos:triggerfield id="cascode_id_" width="200"
					value="${cascode_id_ }" style="display:'none'" />
				<aos:triggerfield emptyText="内容" id="nrzy"
					onenterkey="_g_data_query" trigger1Cls="x-form-search-trigger"
					onTrigger1Click="_g_data_query" width="200" />
			</aos:docked>

			<aos:plugins>
				<aos:editing id="id_plugin" ptype="cellediting" clicksToEdit="2" />
			</aos:plugins>
			<aos:selmodel type="row" mode="multi" />
			<aos:column dataIndex="id_" header="流水号" hidden="true" />
			<aos:column dataIndex="_path" header="电子文件"
				rendererFn="fn_path_render" />				
			<c:forEach var="field" items="${fieldDtos}">
				<aos:column dataIndex="${field.fieldenname}"
					header="${field.fieldcnname }" width="${field.dislen }" rendererField="field_type_" />
			</c:forEach>
			<aos:column header="元数据" rendererFn="fn_render_meta" />
			<aos:column header="指导意见" rendererFn="fn_render_zdyj" dataIndex="_zd"/>
			<aos:column header="" flex="1" />
		</aos:gridpanel>
	</aos:viewport>

	<aos:window id="_w_data_i" title="新增" width="1000" height="600"
		autoScroll="true" onshow="_w_data_i_onshow" y="100"  onrender="_w_data_i_render" >
		<aos:formpanel id="_f_data_i" width="980" layout="absolute" >
			<aos:hiddenfield  name="_classtree" id="_classtree" value="${cascode_id_}" />
		</aos:formpanel>
		<aos:docked dock="bottom" ui="footer">
			<aos:dockeditem xtype="tbfill" />
			<aos:dockeditem onclick="_f_data_i_save" text="添加保存" icon="icon80.png" />
			<aos:dockeditem onclick="_f_data_edit" text="修改保存" icon="icon82.png" />
			<aos:dockeditem onclick="#_w_data_i.hide();" text="关闭"
				icon="close.png" />
		</aos:docked>
	</aos:window>
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
			<aos:tab title="记录替换" id="_tab_replace">
				<aos:formpanel id="_f_replace" layout="column" columnWidth="1">
					<aos:displayfield value="请输入您替换的条件。。。" columnWidth="0.99" />
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
						<aos:dockeditem onclick="#_w_query_edit_term.hide();" text="关闭"
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
	<aos:window id="_w_query_edit_term" title="批量修改" width="720" autoScroll="true"
		layout="fit">
		<aos:tabpanel id="_tabpanel3" region="center" activeTab="0"
			bodyBorder="0 0 0 0" tabBarHeight="30">
			<aos:tab title="记录替换" id="_tab_replace2">
				<aos:formpanel id="_f_replace2" layout="column" columnWidth="1">
					<aos:displayfield value="请输入您替换的条件。。。" columnWidth="0.99" />
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
						<aos:dockeditem onclick="_f_data_replace2" text="确定" icon="ok.png" />
						<aos:dockeditem onclick="#_w_query_edit_term.hide();" text="关闭"
							icon="close.png" />
					</aos:docked>
				</aos:formpanel>
			</aos:tab>
			<aos:tab title="记录更新" id="_tab_update2">
				<aos:formpanel id="_f_update2" layout="column" columnWidth="1">
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
						<aos:dockeditem onclick="_f_data_update2" text="确定" icon="ok.png" />
						<aos:dockeditem onclick="#_w_query_edit_term.hide();" text="关闭"
							icon="close.png" />
					</aos:docked>
				</aos:formpanel>
			</aos:tab>
			<aos:tab title="字段前后辍" id="_tab_suffix2">
				<aos:formpanel id="_f_suffix2" layout="column" columnWidth="1">
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
						<aos:dockeditem onclick="_f_data_suffix2" text="确定" icon="ok.png" />
						<aos:dockeditem onclick="#_w_query_edit_term.hide();" text="关闭"
							icon="close.png" />
					</aos:docked>
				</aos:formpanel>
			</aos:tab>
			<aos:tab title="补位" id="_tab_repair2">
				<aos:formpanel id="_f_repair2" layout="column" columnWidth="1">
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
						<aos:dockeditem onclick="_f_data_repair2" text="确定" icon="ok.png" />
						<aos:dockeditem onclick="#_w_query_edit_term.hide();" text="关闭"
							icon="close.png" />
					</aos:docked>
				</aos:formpanel>
			</aos:tab>
			<aos:tab title="更新分类" id="_tab_catelogy">
				<aos:formpanel id="_f_catelogy" layout="column" columnWidth="1">
					<aos:displayfield value="选择分类" columnWidth="0.99" />
					<aos:combobox name="cascade" labelWidth="20" columnWidth="0.49"
						fields="['cascade_id_','name_']" displayField="name_"
						valueField="cascade_id_"
						url="queryCatelogys.jhtml?tablename=${tablename }&cascade_id_=${cascode_id_}"
						allowBlank="false">
					</aos:combobox>
					<aos:docked dock="bottom" ui="footer" center="true">
						<aos:dockeditem xtype="tbfill" />
						<aos:dockeditem onclick="_f_data_refresh" text="确定" icon="ok.png" />
						<aos:dockeditem onclick="#_w_query_edit_term.hide();" text="关闭"
							icon="close.png" />
					</aos:docked>
				</aos:formpanel>
			</aos:tab>
		</aos:tabpanel>
		</aos:window>
	
	<aos:window id="_w_config" title="设置" 
		autoScroll="true"  layout="column" width="300" border="false" >
		<aos:formpanel id="_f_config" layout="column" labelWidth="80" columnWidth="1" >
				<aos:textfield fieldLabel="每页显示" name="pagesize" value="${pagesize }" columnWidth="0.9" />
				<aos:docked dock="bottom" ui="footer">
					<aos:dockeditem xtype="tbfill" />
					<aos:dockeditem onclick="_f_info_ok" xtype="button" text="确定" icon="ok.png" />
					<aos:dockeditem onclick="#_w_config.hide();" xtype="button" text="取消" icon="del.png" />
					<aos:dockeditem xtype="tbfill" />
				</aos:docked>
			</aos:formpanel>
	</aos:window>
	<aos:window id="_w_query_del_term" title="批量删除" width="720" autoScroll="true"
		layout="fit">
		<aos:tabpanel id="_tabpanel2" region="center" activeTab="0"
			bodyBorder="0 0 0 0" tabBarHeight="30">
			<aos:tab title="高级删除" id="_tab_term">
				<aos:formpanel id="_f_term" layout="column" columnWidth="1">
				<aos:hiddenfield name="cascode_id_" value="${cascode_id_ }" />
					<aos:hiddenfield name="tablename" value="${tablename }" />
					<!-- 隐藏域记录总查询个数 -->
					<aos:hiddenfield name="columnnum" id="columnnum" value="7" />
					<!-- 默认7个查询条件数据 -->
					<c:forEach var="fieldss" items="${fieldDtos}" end="7"
						varStatus="listSearch">
							<!-- 默认包含 -->
						<aos:combobox name="and${listSearch.count }" value="on"
							labelWidth="10" columnWidth="0.2">
							<aos:option value="on" display="并且" />
							<aos:option value="up" display="或者" />
						</aos:combobox>
						<!-- 查询字段下拉列表集合 -->
						<aos:combobox name="filedname${listSearch.count }"
							emptyText="${fieldss.fieldcnname }" labelWidth="20"
							columnWidth="0.2" fields="['fieldenname','fieldcnname']"
							regexText="${fieldss.fieldenname }" displayField="fieldcnname"
							valueField="fieldenname"
							url="queryFields.jhtml?tablename=${tablename }">
						</aos:combobox>
						<!-- 默认包含 -->
						<aos:combobox name="condition${listSearch.count }" value="like"
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
						<aos:textfield name="content${listSearch.count }"
							allowBlank="true" columnWidth="0.39" />
					</c:forEach>
					<aos:docked dock="bottom" ui="footer">
						<aos:dockeditem xtype="tbfill" />
						<aos:dockeditem onclick="_f_data_term" text="确定" icon="ok.png" />
						<aos:dockeditem onclick="#_w_query_del_term.hide();" text="关闭"
							icon="close.png" />
					</aos:docked>
				</aos:formpanel>
			</aos:tab>
		</aos:tabpanel>
		</aos:window>
	
		<aos:window id="_w_data_transfer" title="移交" 
			autoScroll="true"  layout="column" width="500" border="false" >
			<aos:formpanel id="_f_data_transfer" layout="column" labelWidth="80" columnWidth="1" >
					<aos:displayfield value="导出文件夹名:" columnWidth="0.99" />
					<aos:combobox name="filedname" labelWidth="20" columnWidth="0.49"
						fields="['fieldenname','fieldcnname']" displayField="fieldcnname"
						valueField="fieldenname"
						url="queryFields.jhtml?tablename=${tablename }" allowBlank="false">
					</aos:combobox>				
				<aos:docked dock="bottom" ui="footer">
					<aos:dockeditem xtype="tbfill" />
					<aos:dockeditem onclick="_f_transfer(1)" xtype="button" text="移交电子文件" icon="ok.png" />
					<aos:dockeditem onclick="_f_transfer(2)" xtype="button" text="移交条目数据" icon="ok.png" />
					<aos:dockeditem onclick="_f_transfer(0)" xtype="button" text="全部移交" icon="ok.png" />
					<aos:dockeditem onclick="#_w_data_transfer.hide();" xtype="button" text="取消" icon="del.png" />
					<aos:dockeditem xtype="tbfill" />
				</aos:docked>
			</aos:formpanel>
	</aos:window>
	
	<aos:window id="_w_meta" title="元数据信息" region="center" onshow="_w_meta_onshow" >
		<aos:tabpanel id="_t_meta" bodyBorder="0 0 0 0" tabBarHeight="30" width="700">
			<aos:tab title="业务信息" id="_t_tab_ywxx" >
				<aos:gridpanel id="_g_yw"  url="listyw.jhtml"  width="500" hidePagebar="true">
					<aos:column header="id_" hidden="true"/>
					<aos:column header="业务标识" dataIndex="ywbz"/>
					<aos:column header="业务状态" dataIndex="ywzt"/>
					<aos:column header="业务时间" dataIndex="ywsj"/>
					<aos:column header="业务描述" dataIndex="ywms"/>
				</aos:gridpanel>
				<aos:formpanel id="_f_tab_ywxx" width="800" layout="column"
				labelWidth="75" >
				<aos:textfield fieldLabel="身份证号" name="id_no_" columnWidth="0.9" />
				<aos:textfield fieldLabel="身份证号" name="id_no_" columnWidth="0.9" />
				<aos:textfield fieldLabel="身份证号" name="id_no_" columnWidth="0.9" />
				<aos:textfield fieldLabel="身份证号" name="id_no_" columnWidth="0.9" />
			</aos:formpanel>
			</aos:tab>
			<aos:tab title="机构人员" id="_t_tab_jgry" width="700" >
				<aos:gridpanel id="_g_jg"  url="listjg.jhtml" hidePagebar="true" >
					<aos:column header="id_" hidden="true"/>
					<aos:column header="机构标识" dataIndex="jgbz"/>
					<aos:column header="机构名称" dataIndex="jgmc"/>
					<aos:column header="机构代码" dataIndex="jgdm"/>
					<aos:column header="机构人员信息" dataIndex="jgryxx"/>
					<aos:column header="个人位置" dataIndex="grwz"/>
				</aos:gridpanel>
				<aos:formpanel id="_f_tab_jgry" width="800" layout="column"
				labelWidth="75" >
				<aos:docked>
					<aos:dockeditem xtype="tbtext" text="机构人员" />
				</aos:docked>
				<aos:textareafield name="zd_description" id="zd_descript" fieldLabel="指导意见" height="100"
					columnWidth="0.99" />
			</aos:formpanel>
			</aos:tab>
		</aos:tabpanel>
			<aos:docked dock="bottom" ui="footer">
				<aos:dockeditem xtype="tbfill" />
				<aos:dockeditem onclick="#_w_meta_add();" text="添加" icon="add.png" />
				<aos:dockeditem onclick="#_w_meta.hide();" text="关闭" icon="close.png" />
			</aos:docked>
		</aos:window>
	<aos:window id="_w_zdyj" title="指导意见" height="500" autoScroll="true" onshow="_w_zdyj_load" >
			<aos:gridpanel  id="_g_zdyj_demo"  height="250" hidePagebar="true"
				url="getZDYJ.jhtml" width="800" pageSize="10">
				<aos:docked>
					<aos:dockeditem xtype="tbtext" text="指导列表" />
				</aos:docked>
				<aos:selmodel type="row" mode="multi" />
				<aos:column type="rowno" />
				<aos:column header="流水号" dataIndex="id_" hidden="true" />
				<aos:column header="数据表" dataIndex="tablename" hidden="true" width="90" />
				<aos:column header="tid" dataIndex="tid" width="90" hidden="true"/>
				<aos:column header="指导人" dataIndex="zd_person" width="90" />
				<aos:column header="指导时间" dataIndex="zd_time" width="90" />
				<aos:column header="指导内容" dataIndex="zd_description" width="90" />
			</aos:gridpanel>
			<aos:formpanel id="_f_table" width="800" layout="column"
				labelWidth="75" >
				<aos:docked>
					<aos:dockeditem xtype="tbtext" text="指导意见" />
				</aos:docked>
				<aos:textareafield name="zd_description" id="zd_description" fieldLabel="指导意见" height="100"
					columnWidth="0.99" />
			</aos:formpanel>
			<aos:docked dock="bottom" ui="footer">
				<aos:dockeditem xtype="tbfill" />
				<aos:dockeditem onclick="_f_description_add" text="添加" icon="ok.png" />
				<aos:dockeditem onclick="#_w_zdyj.hide();" text="关闭"
					icon="close.png" />
			</aos:docked>
		</aos:window>
	<script type="text/javascript">
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
                   //此时最后一个id，
                 
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
		//alert(datanext.fieldname.substring(0,datanext.fieldname.length-1));
		//var nextfieldname = datanext.fieldname.substring(0,datanext.fieldname.length-1);
		var str ='[{'   
                +'xtype : "textfield",'
                +'id:"'+fieldname+'",'
                +'name:"'+fieldname+'",'
                +'maxWidth : '+strwidth+','
				+'height : '+strheight+','
                +'x:'+strx+','
                +'y:'+stry+','
                +'enableKeyEvents:true'+','
                +'maxLength:'+data.edtmax+','
                ;
                if(data.ynnnull=='0'){
	                str=str+'allowBlank:false,';
	            }
	            str=str+'listeners:{keydown:function(e){'
	            str=str+'},keydown:function(e){if(window.event.keyCode==13){}},blur:function(e){'
	            if(data.ynpw=='1'){
	            	str=str+'var val=e.getValue();var len=val.length;  while(len < '+data.edtmax+') {val= "0" + val;len++;}e.setValue(val);';
	            }
	            
	            if(strdh.indexOf(fieldname)>-1){
	            
	            var strarray=strdh.split(',');
	            //alert(strarray[0]);
	            var strtemp='';
	            //alert(strdh);
	            for(var i=1;i<strarray.length; i++){
		            if(i==1){
		            strtemp ='Ext.getCmp("'+strarray[i]+'").getValue()';
		            continue;
		            }
	             strtemp=strtemp+'+"-"+Ext.getCmp("'+strarray[i]+'").getValue()';
	            }
	            str=str+'Ext.getCmp("'+strarray[0]+'").setValue('+strtemp+')';
	            }
	            str=str+'}'//离开鼠标事件结尾
	            str=str+'}';
                str=str+'}]';
                //alert(str);
         var item = eval('(' + str + ')');
		return item;
		}
		
		function _w_data_i_render(){
		_w_data_input('_f_data_i');
		}
		function _w_data_u_onshow(){
		var record = AOS.selectone(_g_data);
                AOS.ajax({
                    params: {id_: record.data.id_,
                    tablename:tablename.getValue()
                    },
                    url: 'getData.jhtml',
                    ok: function (data) {
                        _f_data_u.form.setValues(data);
                    }
                });
		
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


		//获取表格当前行数的API
		function getCount() {
			var count = _g_print_store.getCount();
			console.log(count);
		}

		_g_data.on("cellclick", function(pGrid, rowIndex, columnIndex, e) {
			var record = AOS.selectone(_g_data);
		});

		//弹出新增用户窗口
        function _w_data_show() {
                _w_data_i.show();
            }
        function _w_data_i_onshow(){
        if(typeof(AOS.selectone(_g_data))!='undefined'){
	        AOS.ajax({
	                    params: {
	                    	id_: AOS.selectone(_g_data).data.id_,
	                   		tablename:tablename.getValue()
	                    },
	                    url: 'getData.jhtml',
	                    ok: function (data) {
	                        _f_data_i.form.setValues(data);
	                    }
	                });
	        
	        }
	        setTimeout(_f_data_KeyEvents,1000);
	      //  var els = Ext.query("#_f_data_i input");
		//alert('6666');
	        
        }
            
        function _w_query_show() {
                _w_query_q.show();
        }
            
        //设置录入窗口
        function _w_input_show() {
           window.parent.fnaddtab('','设置录入','/dbtable/input/initInput.jhtml?tablename='+tablename.getValue());
             
        }
        
         //新增目录加存
			function _f_data_save(){
 				AOS.ajax({
					forms : _f_data_u,
					url : 'saveData.jhtml',
					params:{
				tablename : tablename.getValue()
			},
					ok : function(data) {
						if (data.appcode === -1) {
                            AOS.err(data.appmsg);
                        } else {
                            _w_data_u.hide();
                            _g_data_store.reload();
                            AOS.tip(data.appmsg);
                        }	
					}
				});
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
		
		//显示上传面板
		function _w_picture_show() {
		var record = AOS.selectone(_g_data);
		var uploadPanel= new Ext.ux.uploadPanel.UploadPanel({
		 addFileBtnText : '选择文件...',
		 uploadBtnText : '上传',
		 deleteBtnText : '移除',
		 downBtnText   : '下载',
		 removeBtnText : '移除所有',
		 cancelBtnText : '取消上传',
         use_query_string : true, 
         listeners:{  
		//双击  
		itemdblclick : function(grid,row){ 
		//parent.fnaddtab(row.data.id, '电子文件',
		//					'archive/data/openFile.jhtml?id='+row.data.pid+'&tid='+row.data.tid+'&type='+row.data.type+'&tablename='+tablename.getValue());
        parent.fnaddtab(row.data.id, '电子文件',
							'archive/data/openPdfFile.jhtml?id='+row.data.pid+'&tid='+row.data.tid+'&type='+row.data.type+'&tablename='+tablename.getValue());
      
      
       }
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
	            var me=Ext.getCmp("uploadpanel").getSelectionModel().getSelection();
				var id = me[0].get('pid');
				var tid = me[0].get('tid');
				
				var rowIndex = Ext.getCmp("uploadpanel").getStore().indexOf(me[0]);
                    AOS.ajax({
                    params: {id_:id,
                    tablename:tablename.getValue(),
                    tid:tid,
                    tm:record.data.tm
                    }, // 提交参数,
                    url: 'deletePath.jhtml',
                    ok: function (data) {
                    var me=Ext.getCmp("uploadpanel");
                    //me.store.reload();
                    me.store.remove(me.store.getAt(rowIndex));
                    me.store.load();
                    AOS.tip(data.appmsg);
                    }
                });
	},
       onRemoveAll: function (){
        var selection = AOS.selection(_g_data, 'id_');
        AOS.ajax({
                    params: {aos_rows_: selection,
                    		tm:record.data.tm,
                            tablename: tablename.getValue()
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
    	 //得到选中的条目
           var me=Ext.getCmp("uploadpanel").getSelectionModel().getSelection();
			var id = me[0].get('pid');
           AOS.file('downloadPath.jhtml?id_='+id+'&tablename='+tablename.getValue());
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
	
	//_path列转换
	function fn_path_render(value, metaData, record, rowIndex, colIndex,
				store) {
			if (value >= 1) {
				return '<img src="${cxt}/static/icon/picture.png" />';
			} else {
				return '<img src="${cxt}/static/icon/picture_empty.png" />';
			}
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
			_g_data_store.getProxy().extraParams = params;
			_g_data_store.load();
			_w_query_q.hide();
			AOS.reset(_f_query); 
			
	}
	
	function fn_g_data(){
	_w_data_i.show();
	
	}
	//记录更新
	function _f_data_update(){
	AOS.ajax({
					forms : _f_update,
					url : 'updateRecord.jhtml',
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
	//前后辍
	function _f_data_suffix(){
	AOS.ajax({
					forms : _f_suffix,
					url : 'suffixRecord.jhtml',
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
	//部位
	function _f_data_repair(){
		AOS.ajax({
			forms : _f_repair,
			url : 'repairRecord.jhtml',
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
	//记录更新2
	function _f_data_update2(){
	AOS.ajax({
					forms : _f_update2,
					url : 'updateRecord.jhtml',
					params:{
				tablename : tablename.getValue()
			},
					ok : function(data) {
						    _w_query_edit_term.hide();
                            _g_data_store.reload();
                            AOS.tip(data.appmsg);
					}
				});
	}
	
	//记录替换2
	function _f_data_replace2(){
	AOS.ajax({
					forms : _f_replace2,
					url : 'replaceRecord.jhtml',
					params:{
				tablename : tablename.getValue()
			},
					ok : function(data) {
						    _w_query_edit_term.hide();
                            _g_data_store.reload();
                            AOS.tip(data.appmsg);
					}
				});
	}
	
	//前后辍2
	function _f_data_suffix2(){
	AOS.ajax({
					forms : _f_suffix2,
					url : 'suffixRecord.jhtml',
					params:{
				tablename : tablename.getValue()
			},
					ok : function(data) {
						    _w_query_edit_term.hide();
                            _g_data_store.reload();
                            AOS.tip(data.appmsg);
					}
				});
	}
	
	//补位2
	function _f_data_repair2(){
		AOS.ajax({
			forms : _f_repair2,
			url : 'repairRecord.jhtml',
			params:{
		tablename : tablename.getValue()
	},
			ok : function(data) {
				    _w_query_edit_term.hide();
                    _g_data_store.reload();
                    AOS.tip(data.appmsg);
			}
		});
	}
	//生成XLS报表
		function fn_xls() {
			AOS.ajax({
				url : 'fillReport.jhtml',
				params:{
				tablename : tablename.getValue(),
				cascode_id_:cascode_id_.getValue()
			},
				ok : function(data) {
					AOS.file('${cxt}/report/xls.jhtml');
				}
			});
		}

		//生成XLSX报表
		function fn_xlsx() {
			AOS.ajax({
				url : 'fillReport.jhtml',
				params:{
				tablename : tablename.getValue()
			},
				ok : function(data) {
					AOS.file('${cxt}/report/xlsx.jhtml');
				}
			});
		}
		//生成XML报表
		function fn_xml() {
			AOS.ajax({
				url : 'fillReportxml.jhtml',
				params:{
				tablename : tablename.getValue(),
				cascode_id_:cascode_id_.getValue()
			},
				ok : function(data) {
					AOS.file('${cxt}/report/zip.jhtml?zipfilename='+data.appmsg);
				}
			});
		}
		//导入窗口
        function _w_import_show() {
        window.parent.fnaddtab('','数据导入','/archive/data/initImport.jhtml?tablename='+tablename.getValue());
             
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
		//鉴定
	function _appraisal_check_show(){
		//1.得到当前session中的查询条件
		//表名
		var tablename="<%=session.getAttribute("tablename")%>";
		//查询条件
		var query="<%=session.getAttribute("querySession")%>";
		var appraisal="1";
		var params={'query': query,'tablename':tablename,'appraisal':appraisal};
		_g_data_store.getProxy().extraParams = params;
		//执行加载
		_g_data_store.load();
		Ext.getCmp("appraisal").setValue("1");
		//弹窗
		AOS.tip("下面为过期或需要销毁的档案!");
	}


//移动
	function _move_check_show(){
		//从session域中看数据是否鉴定完了
		var appraisal=Ext.getCmp("appraisal").getValue();
		if(appraisal!="1"){
			AOS.tip('移动前请先鉴定数据。');
			return;
		}
		 var selection = AOS.selection(_g_data, 'id_');
		 var tm = AOS.selection(_g_data,'tm');
         if (AOS.empty(selection)){
             AOS.tip('移动前请先选中数据。');
             return;
         }
         var tablename="<%=session.getAttribute("tablename")%>";
         var record = AOS.selectone(_g_data);
         //此时开始移动
         AOS.ajax({
             params: {aos_rows_: selection,
            	 tablename:tablename,
            	 tm:tm},
             url: 'removeappraisal.jhtml',
             ok: function (data) {
             	if(data.appcode ===-1){
                     AOS.tip("移动失败!");
                 }else{
                 	 AOS.tip("移动完成!");
                 }        
 			}
 		});
	}
	function _g_data_del_term(){
		//结合条件删除
		var tablename="<%=session.getAttribute("tablename")%>";	
		_w_query_del_term.show();
		}
	//条件删除
	function _f_data_term(){		
		 AOS.ajax({
             url: 'deleteTermData.jhtml',
             forms:_f_term,
             ok: function (data) {
                 AOS.tip(data.appmsg);
                 _g_data_store.reload();
             }
         });
		//将form清空
		_w_query_del_term.hide();
		AOS.reset(_f_term); 
		}
	//批量修改
	function _g_data_edit_term(){
		//结合条件删除
		var tablename="<%=session.getAttribute("tablename")%>";	
		_w_query_edit_term.show();
	}
	//电子文件移交
	function _f_transfer(flag){
		var tablename="<%=session.getAttribute("tablename")%>";
		var queryclass="<%=session.getAttribute("queryclass")%>";
		
		AOS.ajax({
            url: 'transferData.jhtml',
            forms:_f_data_transfer,
            params: {
            	 flag:flag,
                 tablename:tablename,
                 queryclass:queryclass
            },           
            ok: function (data) {
            	if(data.appcode===1){
            		AOS.tip("电子文件移交成功");
            		return;
            	}
            	if(data.appcode===2){            		     	
            		AOS.file('${cxt}/report/transferxls.jhtml?path='+encodeURI(encodeURI(data.appmsg)));
            		AOS.tip("条目移交成功");
            	}
            	if(data.appcode===3){            		     	
            		AOS.file('${cxt}/report/transferxls.jhtml?path='+encodeURI(encodeURI(data.appmsg)));
            		AOS.tip("全部移交成功");
            	}
            	                             
            }
        });
	}
	//全部删除(指定条件的全部删除)
	function _g_data_del_term(){
		var msg = AOS.merge('确认要删除用户数据吗？');
        AOS.confirm(msg, function (btn) {
            if (btn === 'cancel') {
                AOS.tip('删除操作被取消。');
                return;
            }
		 AOS.ajax({
             url: 'deleteAllData.jhtml',
             params: {                
                 tablename: tablename.getValue()
             },
             ok: function (data) {
                 AOS.tip(data.appmsg);
                 _g_data_store.reload();
             }
         });
         });
	}
	function fn_render_meta(){
		return '<input type="button" value="元数据" class="cbtn_warn" onclick="fn_meta_onclick();" />';
	}
	function fn_render_zdyj(value, metaData, record, rowIndex, colIndex,
				store){
		if (value === 1) {
				return '<input type="button" value="指导意见" class="cbtn_danger" onclick="fn_zdyj_onclick();" />';
			}
	}
	
	function _g_meta_query(){
		_g_meta_store.load();
	}
	function _w_meta_onshow(){
		var record = AOS.selectone(_g_data,true);
		var params={
			tid:record.data.id_
		
		};
		_g_yw_store.getProxy().extraParams = params;
		_g_yw_store.load();
		_g_jg_store.getProxy().extraParams = params;
		_g_jg_store.load();
	}
	function fn_button_render(){
			AOS.reset(_f_table);
		_w_zdyj.show();
		//window.parent.fnaddtab('','档案接收','receive/batch/initReceive.jhtml?tablename='+tablename);
	}
	//456464654
	//更新分类
	function _f_data_refresh(){
		AOS.ajax({
			forms : _f_catelogy,
			url : 'refreshcategory.jhtml',
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
	function _w_zdyj_load(){
		var record=Ext.getCmp('_g_data').getSelectionModel().getSelection();
		var id_=record[0].data.id_;
		var tablename="<%=session.getAttribute("tablename")%>";	
		var params ={
			tid:id_,
		zd_tablename:tablename
		};
			//这个Store的命名规则为：表格ID+"_store"。
			_g_zdyj_demo_store.getProxy().extraParams = params;
		    _g_zdyj_demo_store.load();
	}
	function _f_description_add(){
		var record=Ext.getCmp('_g_data').getSelectionModel().getSelection();
		var id_=record[0].data.id_;
		var tablename="<%=session.getAttribute("tablename")%>";	
		var zd_description=Ext.getCmp('zd_description').getValue();
		AOS.ajax({
			 params: {tid:id_,
				 zd_tablename:tablename,
				 zd_description:zd_description
			 },
			 url: 'addZddescription.jhtml',
			 ok: function (data) {
					 if(data.appCode==-1){
						 AOS.tip("添加失败");
					 }else{
						 _g_zdyj_demo_store.load();
					 }
				 }
			 });	
	}
	function _f_data_KeyEvents(){
	AOS.ajax({
		url: 'getNextcontrol.jhtml',
		params: {
			tablename: tablename.getValue()
		},
		ok:function(data){
			Ext.create("Ext.util.KeyMap", "_f_data_i", {
					key: 13,
					fn: function(key, e) {
					var el=e.target,ln=data.length;
					var focusname = e.target.name;
					//var nextfocus11 = data[28].fieldname.substring([28].fieldname.length-1);
					for(var i=0;i<ln;i++){
						var strname = data[i].fieldname.substring(0,data[i].fieldname.length-1);
						var firstfocus = data[0].fieldname.substring(0,data[0].fieldname.length-1);
						if(strname == focusname){
							if(i+1 == ln){
								Ext.getCmp(firstfocus).focus();
							}else{
							var nextfocus = data[i+1].fieldname.substring(0,data[i+1].fieldname.length-1);
								Ext.getCmp(nextfocus).focus();
							}
							
						}
					}
		}
	
	})
					}
				});
	
	}
	
	function _w_meta_add(){
		AOS.Ajax({
			params:{
				
			},
			ok:'',
			url:''
		
		})
	
	}
	</script>
</aos:onready>
<script type="text/javascript">
	//表格列按钮单击事件
	function fn_meta_onclick() {
		Ext.getCmp('_w_meta').show();
	}
	function fn_zdyj_onclick() {
	Ext.getCmp('zd_description').setValue("");
		Ext.getCmp('_w_zdyj').show();
	}
	
</script>
</aos:html>
﻿<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="/WEB-INF/tld/aos.tld" prefix="aos"%>
<%@ taglib uri="/WEB-INF/tld/cll.tld" prefix="c"%>
<aos:html>
<aos:head title="数据信息">
	<aos:include lib="ext,swfupload" />
	<aos:base href="archive/data" />
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
	<div id="filediv">
		<input id="file" class="file-loading" name="file" type="file" multiple>
	</div>
	<video id="videolook"  controls="controls" hidden="true"   height="500" preload="autoplay"
		   width="690">

	</video>
</aos:body>
<aos:onready>
	<aos:viewport layout="border">
		<aos:gridpanel id="_g_data" url="listAccounts.jhtml" region="center" split="true"
			onrender="_g_data_query" onitemclick="itemclick" rowclass="true" pageSize="${pagesize }"
			 enableLocking="false" >
			<aos:docked>
				<aos:hiddenfield id="rowmath" name="rowmath" value="0" />
				<aos:hiddenfield id="user" name="user" value="${user}"/>
				<aos:hiddenfield id="index" name="index" />
				<aos:hiddenfield  name="insert" id="insert"/>
				<aos:hiddenfield  name="appcode" id="appcode"/>
				<aos:hiddenfield  name="appmsg" id="appmsg"/>
				<aos:hiddenfield  name="xdfields" id="xdfields"/>
				<aos:hiddenfield id="appraisal" name="appraisal" />
				<aos:hiddenfield id="aos_module_id_" name="aos_module_id_" value="${aos_module_id_}"/>
				<aos:hiddenfield id="treenumber" name="treenumber" value="${treenumber}"/>
				<aos:hiddenfield name="_classtree" id="_classtree"
								 value="${cascode_id_}" />
				<aos:hiddenfield id="treename" name="treename" value="${treename}"/>
				<aos:hiddenfield id="tablename" name="tablename" value="${tablename}"/>
				<aos:hiddenfield id="dzdalx" name="dzdalx" value="${dalx}"/>
				<aos:hiddenfield id="mldm" name="mldm" value="${mldm}"/>
				<!-- 存储当前页面的查询条件 -->
				<aos:hiddenfield id="querySession" name="querySession" />
				<aos:dockeditem xtype="tbtext" text="目录信息" />
				<aos:dockeditem text="新增"  icon="add2.png"
								onclick="_w_data_show"  id="_f_add_message"/>
				<aos:dockeditem text="修改" icon="edit.png" onclick="fn_g_data"  id="_f_editi_message"/>
				<aos:dockeditem text="批量修改" icon="edit.png" id="edit_all_table_text"
								onclick="_g_data_edit_term" />
				<aos:button text="删除" icon="del2.png" scale="small" margin="0 0 0 0" id="del_table_text">
					<aos:menu plain="false">
						<aos:menuitem text="单项删除" icon="del2.png" onclick="_g_data_del" />
						<aos:menuitem text="全部删除" icon="del.png"
									  onclick="_g_data_del_term" />
					</aos:menu>
				</aos:button>
				<aos:button text="扫描" icon="webcam.png" scale="small" margin="0 0 0 0" id="scan_table_text">
					<aos:menu plain="false">
						<aos:menuitem text="扫描" icon="webcam.png" onclick="fn_scan" />
						<aos:menuitem text="扫描设置" icon="config3.png"
									  onclick="_w_setScan_show" />
					</aos:menu>
				</aos:button>
				<aos:dockeditem text="设置" icon="layout.png"
								onclick="_w_input_show"  id="_f_input_message"/>
				<aos:dockeditem text="设置条目数" icon="config1.png" onclick="_w_config_show" />
				<aos:dockeditem text="上传文件"  icon="more/go-top-8.png" onclick="_fileupload_show" />
				<aos:dockeditem text="显示" icon="picture.png"  id="_f_filename_message"
								onclick="_w_picture_show"   />
				<aos:dockeditem text="排序" icon="more/object-order-back.png"
								onclick="_w_order_show" />
				<aos:dockeditem text="转总库" icon="more/object-order-back.png"
								onclick="_w_dataTototal_show" />
				<aos:dockeditem text="检索" icon="query.png"  onclick="_w_query_show" id="_f_select_message"/>
				<aos:dockeditem text="数据核检" icon="more/tools-check-spelling.png"  onclick="_w_check_data" id="_f_check_data"/>
				<%--<aos:dockeditem text="测试数据" icon="query.png"  onclick="_w_ceshi_show" />--%>
				<aos:button text="导出" icon="icon154.png" scale="small"
					margin="0 0 0 0">
					<aos:menu plain="false">
						<aos:menuitem text="导出XLS报表" icon="icon70.png" onclick="fn_xls" />
						<aos:menuitem text="导出XLSX报表" icon="icon7.png" onclick="fn_xlsx" />
					</aos:menu>
				</aos:button>
				<aos:dockeditem text="导入" icon="folder8.png"
					onclick="_w_import_show" />
				<aos:dockeditem text="显示顺序" icon="icon146.png" onclick="_g_field_order_show" />
				<aos:dockeditem text="打印" icon="icon146.png" onclick="#_w_print.show()" />
				<aos:dockeditem xtype="tbfill" />
			</aos:docked>
			<aos:selmodel type="checkbox" mode="multi" />
			<aos:column dataIndex="id_" header="流水号" hidden="true" locked="true"/>
			<aos:column dataIndex="_path" header="电子文件" hidden="true"
				rendererFn="fn_path_render" />
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
	</aos:viewport>
	<aos:window id="_w_data_i" title="新增"   x="50" width="750"
		onrender="_w_data_i_render" >
		<aos:formpanel id="_f_data_i" labelWidth="100"     width="750" >
			<aos:hiddenfield  name="_classtree" id="_classtree" value="${cascode_id_}" />
			<aos:textfield name="nd" fieldLabel="年度"  columnWidth="0.49"/>
			<aos:textfield name="flh"  fieldLabel="分类号" columnWidth="0.49"/>
			<aos:textfield name="tm" fieldLabel="题名" columnWidth="0.98"/>
			<aos:combobox fieldLabel="资源类型" columnWidth="0.49" name="zylx" >
				<aos:option display="报纸" value="报纸" />
				<aos:option display="新闻" value="新闻" />
				<aos:option display="网上信息" value="网上信息" />
			</aos:combobox>
			<aos:textfield name="qkh" fieldLabel="期刊号" columnWidth="0.49"/>
			<aos:combobox fieldLabel="文种" columnWidth="0.49" name="wz" >
				<aos:option display="中文" value="中文" />
				<aos:option display="朝文" value="朝文" />
				<aos:option display="日文" value="日文" />
				<aos:option display="英文" value="英文" />
				<aos:option display="其他" value="其他" />
			</aos:combobox>
			<aos:datefield name="xcsj" fieldLabel="形成时间"  columnWidth="0.49"/>
			<aos:textfield name="cbs" fieldLabel="出版社" columnWidth="0.49"/>
			<aos:textfield name="zz" fieldLabel="作者" columnWidth="0.49"/>
			<aos:checkboxgroup fieldLabel="载体类型" columnWidth="0.49" id="zztlx_add" columns="[80,80]">
				<aos:checkbox name="c10" boxLabel="图片" />
				<aos:checkbox name="c10" boxLabel="视频" />
			</aos:checkboxgroup>
			<aos:textfield name="ys" fieldLabel="页数" columnWidth="0.49"/>
			<aos:textfield name="sc" fieldLabel="时长" columnWidth="0.49"/>
			<aos:textfield name="cs" fieldLabel="册(本)数" columnWidth="0.49"/>
			<aos:textfield name="zy" fieldLabel="摘要" columnWidth="0.49"/>
			<aos:textfield name="bz" fieldLabel="备注" columnWidth="0.49"/>
			<aos:hiddenfield id="lx" name="lx" value="${lx}"/>
		</aos:formpanel>
		<aos:docked dock="bottom" ui="footer">
			<aos:dockeditem xtype="tbfill" />
			<aos:dockeditem text="上一条" icon="more/go-previous.png"  onclick="_f_previous_data"/><%----%>
			<aos:dockeditem text="下一条" icon="more/go-next.png" onclick="_f_next_data"/><%----%>
			<aos:dockeditem onclick="_f_data_i_save" text="加存" icon="icon80.png" />
			<aos:dockeditem onclick="_f_data_edit" text="保存" icon="icon82.png" />
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
					<aos:hiddenfield name="tablename" value="${tablename }" />
					<aos:hiddenfield name="columnnum" id="columnnum" value="8" />
					<aos:hiddenfield name="selectmark" id="selectmark" value="1" />
					<aos:hiddenfield name="selectmath" id="selectmath" value="0" />
					<c:forEach var="fieldss" items="${fieldDtos}" end="7"
						varStatus="listSearch">
						<aos:combobox name="and${listSearch.count}" value="on"
							labelWidth="10" columnWidth="0.2">
							<aos:option value="on" display="并且" />
							<aos:option value="up" display="或者" />
						</aos:combobox>
						<aos:combobox id="filedname${listSearch.count}"
							name="filedname${listSearch.count}"
							emptyText="${fieldss.fieldcnname}" labelWidth="20"
							columnWidth="0.2" fields="['fieldenname','fieldcnname']"
							regexText="${fieldss.fieldenname}" displayField="fieldcnname"
							valueField="fieldenname" 
							url="queryFields.jhtml?tablename=${tablename}">
						</aos:combobox>
						<aos:combobox name="condition${listSearch.count }" value="like"
							labelWidth="20" columnWidth="0.2">
							<aos:option value="=" display="等于" />
							<aos:option value=">" display="大于" />
							<aos:option value=">=" display="大于等于" />
							<aos:option value="<" display=" 小于" />
							<aos:option value="<=" display="小于等于" />
							<aos:option value="<>" display="不等于" />
							<aos:option value="like" display="包含" />
							<aos:option value="not like" display="不包含" />
							<aos:option value="left" display="左包含" />
							<aos:option value="right" display="右包含" />
							<aos:option value="null" display="空值" />
						</aos:combobox>
						<aos:textfield name="content${listSearch.count }"
							allowBlank="true" columnWidth="0.39" />
					</c:forEach>
					<aos:docked dock="bottom" ui="footer">
						<aos:dockeditem xtype="tbfill" />
						<aos:dockeditem onclick="_f_last_query" text="返回上一次查询条件"
							icon="ok.png" />
						<aos:dockeditem onclick="_f_next_query" text="返回下一次查询条件"
							icon="ok.png" />
						<aos:dockeditem onclick="_f_drop_query" text="清空查询条件缓存"
							icon="ok.png" />
						<aos:dockeditem onclick="_f_data_query" text="确定" icon="ok.png" />
						<aos:dockeditem onclick="#_w_query_q.hide();" text="关闭"
							icon="close.png" />
					</aos:docked>
				</aos:formpanel>
			</aos:tab>

		</aos:tabpanel>


	</aos:window>
	<aos:window id="_w_query_edit_term" title="批量修改" width="720"
		autoScroll="true" layout="fit">
		<aos:tabpanel id="_tabpanel3" region="center" activeTab="0"
			bodyBorder="0 0 0 0" tabBarHeight="30">
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
						<aos:dockeditem onclick="#_w_query_edit_term.hide();" text="关闭"
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
						<aos:dockeditem onclick="#_w_query_edit_term.hide();" text="关闭"
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
								  url="queryCatelogys.jhtml?tablename=${tablename }"
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
			<aos:tab title="记录处理" id="_tab_tablefield">
				<aos:formpanel id="_f_tablefield" layout="column" columnWidth="1" >
					<aos:rowset>
					<aos:combobox id="updatetablefield"
								  name="updatetablefield" fieldLabel="更新字段"
								  emptyText="${fieldss.fieldcnname}" maxWidth="300" labelWidth="60"
								  columnWidth="0.39" fields="['fieldenname','fieldcnname']"
								  regexText="${fieldss.fieldenname}" displayField="fieldcnname"
								  valueField="fieldenname" labelAlign="left"
								  url="queryFields.jhtml?tablename=${tablename}">
					</aos:combobox>
					</aos:rowset>
					<aos:combobox id="selecttablefield"  fieldLabel="选取字段"
								  name="selecttablefield"
								  emptyText="${fieldss.fieldcnname}" maxWidth="300" labelWidth="60"
								  columnWidth="0.39" fields="['fieldenname','fieldcnname']"
								  regexText="${fieldss.fieldenname}" displayField="fieldcnname"
								  valueField="fieldenname" labelAlign="left"
								  url="queryFields.jhtml?tablename=${tablename}">
					</aos:combobox>
					<aos:combobox name="condition"
								  labelWidth="20" columnWidth="0.1"  editable="true">
						<aos:option value=" " display=" " />
						<aos:option value="+" display="+" />
						<aos:option value="-" display="-" />
						<aos:option value="*" display="*" />
						<aos:option value="_" display="_" />
					</aos:combobox>
					<aos:textfield name="repair_long"
								   columnWidth="0.19" />
					<aos:button text="添加"  iconVecAlign="right" columnWidth="0.1" onclick="add_tablefield"/>
					<aos:button text="删除"  iconVecAlign="right" columnWidth="0.1" onclick="delete_tablefield"/>
					<aos:button text="清除"  iconVecAlign="right" columnWidth="0.1" onclick="remove_tablefield"/>
				</aos:formpanel>
				<aos:gridpanel id="_g_tablefield" split="true" hidePagebar="true" autoScroll="true" height="300">
					<aos:column header="拼接条件" dataIndex="jointorder" width="60"/>
					<aos:column header="拼接值" dataIndex="jointmath" width="90"/>
				</aos:gridpanel>
				<aos:docked dock="bottom" ui="footer" center="true">
					<aos:dockeditem xtype="tbfill" />
					<aos:dockeditem onclick="_f_data_tablefield" text="确定" icon="ok.png" />
					<aos:dockeditem onclick="#_w_query_edit_term.hide();" text="关闭"
									icon="close.png" />
				</aos:docked>
			</aos:tab>

		</aos:tabpanel>
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

	<aos:window id="_w_query_del_term" title="批量删除" width="720"
		autoScroll="true" layout="fit">
		<aos:tabpanel id="_tabpanel2" region="center" activeTab="0"
			bodyBorder="0 0 0 0" tabBarHeight="30">
			<aos:tab title="高级删除" id="_tab_term">
				<aos:formpanel id="_f_term" layout="column" columnWidth="1">
					<aos:hiddenfield name="tablename" value="${tablename }" />
					<!-- 隐藏域记录总查询个数 -->
					<aos:hiddenfield name="columnnum" id="columnnum" value="8" />
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

	<aos:window id="_w_data_transfer" title="移交" autoScroll="true"
		layout="column" width="500" border="false">
		<aos:formpanel id="_f_data_transfer" layout="column" labelWidth="80"
			columnWidth="1">
			<aos:displayfield value="导出文件夹名:" columnWidth="0.99" />
			<aos:combobox name="filedname" labelWidth="20" columnWidth="0.49"
				fields="['fieldenname','fieldcnname']" displayField="fieldcnname"
				valueField="fieldenname"
				url="queryFields.jhtml?tablename=${tablename }" allowBlank="false">
			</aos:combobox>
			<aos:docked dock="bottom" ui="footer">
				<aos:dockeditem xtype="tbfill" />
				<aos:dockeditem onclick="_f_transfer(1)" xtype="button"
					text="移交电子文件" icon="ok.png" />
				<aos:dockeditem onclick="_f_transfer(2)" xtype="button"
					text="移交条目数据" icon="ok.png" />
				<aos:dockeditem onclick="_f_transfer(0)" xtype="button" text="全部移交"
					icon="ok.png" />
				<aos:dockeditem onclick="#_w_data_transfer.hide();" xtype="button"
					text="取消" icon="del.png" />
				<aos:dockeditem xtype="tbfill" />
			</aos:docked>
		</aos:formpanel>
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
			<aos:panel region="west" width="240" height="200" title="待排列字段列表"
			>
				<!-- 添加grid-->
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
	<aos:window id="_w_count" title="统计" width="800" autoScroll="true"
		layout="fit">
			<aos:tabpanel id="_tabpanel4" region="center" activeTab="0"
				bodyBorder="0 0 0 0" tabBarHeight="30" width="300">
				<aos:tab title="统计" id="_tab_org2">
				<aos:formpanel id="_f_count" width="300" layout="column">

					<aos:combobox fieldLabel="统计字段" name="countfield" labelWidth="70"
						columnWidth="0.49">
						<aos:option value="_path" display="附件" />
						<aos:option value="ys" display="实扫页数" />
						<aos:option value="qzh" display="全宗统计" />
					</aos:combobox>
					<aos:combobox fieldLabel="统计方法" name="countmethod" labelWidth="70"
						columnWidth="0.49">
						<aos:option value="sum" display="总数" />
					</aos:combobox>
					<aos:displayfield value="请输入您查询的条件。。。" columnWidth="0.99" />
					<aos:combobox name="filedname" labelWidth="20" columnWidth="0.39"
						fields="['fieldenname','fieldcnname']" displayField="fieldcnname"
						valueField="fieldenname"
						url="queryFields.jhtml?tablename=${tablename }">
					</aos:combobox>
					<aos:combobox name="condition" value="="
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
					<aos:textfield name="content" allowBlank="true"
						columnWidth="0.39" />
					<aos:combobox name="r1" fieldLabel="条件" value="and" 
							labelWidth="50" columnWidth="0.2">
							<aos:option value="and" display="并且" />
							<aos:option value="or" display="或者" />
					</aos:combobox>
					<aos:button text="添加"  iconVecAlign="right" columnWidth="0.1" onclick="add_order"/>
					<aos:button text="删除" iconVecAlign="right" columnWidth="0.1" onclick="delete_order"/>
					<aos:button text="清除" iconVecAlign="right" columnWidth="0.1" onclick="remove_order"/>
				</aos:formpanel>
				<aos:gridpanel id="_g_count" split="true" hidePagebar="true" autoScroll="true"
					height="250">
						<aos:column header="查询条件" dataIndex="selectorder" width="60"/>
						<aos:column header="查询值" dataIndex="selectmath" width="90"/>
					</aos:gridpanel>
			</aos:tab>
			</aos:tabpanel>
		<aos:gridpanel id="_g_qzh" split="true" hidePagebar="true" autoScroll="true"
					   height="500" >
			<aos:column header="全宗号" dataIndex="qzh" width="50"/>
			<aos:column header="全宗号的和值" dataIndex="qzhsum" width="50"/>
			<aos:column header="实扫页数" dataIndex="yssum" width="50"/>
		</aos:gridpanel>
		<aos:gridpanel id="_g_ys" split="true" hidePagebar="true" autoScroll="true"
					   height="500">
			<aos:column header="实扫页数" dataIndex="yssum" width="150"/>
		</aos:gridpanel>
		<aos:gridpanel id="_g_path" split="true" hidePagebar="true" autoScroll="true"
					   height="500">
			<aos:column header="附件" dataIndex="pathsum" width="150"/>
		</aos:gridpanel>
		<aos:docked dock="bottom" ui="footer">
			<aos:dockeditem xtype="tbfill" />
			<aos:dockeditem onclick="_w_count_submit" text="查询" icon="query.png" />
			<aos:dockeditem onclick="#_w_count.hide();" text="关闭"
				icon="close.png" />
		</aos:docked>
	</aos:window>

	<aos:window id="_w_print"  title="打印目录" height="500" width="800" onshow="_w_print_load" >
		<aos:gridpanel  id="_g_print"  height="500" hidePagebar="true"  autoScroll="true" enableLocking="false"
						url="getPrintData.jhtml" width="800" pageSize="${pagesize }" region="center" split="true">
			<aos:docked>
				<aos:dockeditem  icon="edit.png"
								onclick="_g_data_edit_term" />
			</aos:docked>
			<aos:selmodel type="row" mode="multi" />
			<aos:column type="rowno" />
			<c:forEach var="field" items="${fieldDtos}">
				<aos:column dataIndex="${field.fieldenname}"
							header="${field.fieldcnname }" width="100"
							rendererField="field_type_" >
					<aos:textfield />
				</aos:column>
			</c:forEach>
		</aos:gridpanel>
	</aos:window>
	<aos:window id="_w_look_order"  title="显示顺序" height="530" autoScroll="true" onshow="_g_order_query">
		<aos:gridpanel hidePagebar="true" id="_g_look_order"
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
			<aos:dockeditem onclick="#_w_look_order.hide();" text="关闭"
							icon="close.png" />
		</aos:docked>
	</aos:window>



	<aos:window id="_w_print" title="打印列表" height="400" width="300"
				autoScroll="true" onshow="load_print_list">
				<aos:gridpanel id="_g_print" region="center"
							   url="reportprint.jhtml" split="true"
							   hidePagebar="true"
							   splitterBorder="0 1 0 1" width="280">
					<aos:selmodel type="row" mode="multi" />
					<aos:column header="打印名称" dataIndex="printname" width="280" />
				</aos:gridpanel>
			<aos:docked dock="bottom" ui="footer">
				<aos:dockeditem xtype="tbfill" />
				<aos:dockeditem xtype="button" text="打印" icon="ok.png"
								onclick="print_report" />
				<aos:dockeditem xtype="button" text="关闭" icon="del.png"
								onclick="#_w_print.hide()" />
				<aos:dockeditem xtype="tbfill" />
			</aos:docked>
	</aos:window>


	<aos:window id="_w_setScan_i" title="扫描设置"  width="500"  height="300" y="100"
				resizable="false" autoScroll="false" onshow="_w_setScan_i_onshow" layout="fit">

		<aos:formpanel id="_f_setScan_i" width="480" layout="column" columnWidth="1" padding="10">
			<aos:hiddenfield id="tablename" name="tablename" value="${tablename}"/>
			<aos:hiddenfield id="id" name="id_" />
			<aos:combobox id="selpath" name="selpath" emptyText="${fieldss.fieldcnname }"
						  labelWidth="70" fieldLabel="扫描路径"
						  columnWidth="0.42" fields="['fieldenname','fieldcnname']"
						  regexText="${fieldss.fieldenname }" displayField="fieldcnname"
						  valueField="fieldenname" padding="10"
						  url="queryFields.jhtml?tablename=${tablename }">
			</aos:combobox>
			<aos:button id="load1" columnWidth="0.18" text="加载" margin="10" onclick="fn_load1"/>
			<aos:textfield id="caption" name="caption" allowBlank="true"
						   columnWidth="0.4" padding="10" readOnly="true"/>

			<aos:combobox id="selname" name="selname" emptyText="${fieldss.fieldcnname }"
						  labelWidth="70" fieldLabel="文件名"
						  columnWidth="0.42" fields="['fieldenname','fieldcnname']"
						  regexText="${fieldss.fieldenname }" displayField="fieldcnname"
						  valueField="fieldenname"  padding="10"
						  url="queryFields.jhtml?tablename=${tablename }">
			</aos:combobox>
			<aos:button id="load2" columnWidth="0.18" text="加载" margin="10" onclick="fn_load2"/>
			<aos:textfield id="capname" name="capname" allowBlank="true"
						   columnWidth="0.4" padding="10" readOnly="true"/>

			<aos:combobox id="selShowName" name="selShowName" emptyText="${fieldss.fieldcnname }"
						  labelWidth="70" fieldLabel="显示文件名"
						  columnWidth="0.42" fields="['fieldenname','fieldcnname']"
						  regexText="${fieldss.fieldenname }" displayField="fieldcnname"
						  valueField="fieldenname" padding="10"
						  url="queryFields.jhtml?tablename=${tablename }">
			</aos:combobox>
			<aos:button id="load3" columnWidth="0.18" text="加载" margin="10" onclick="fn_load3" />
			<aos:textfield id="tmname" name="tm" allowBlank="true"
						   columnWidth="0.4" margin="10" readOnly="true"/>
		</aos:formpanel>
		<aos:docked dock="bottom" ui="footer">
			<aos:dockeditem xtype="tbfill"/>
			<aos:dockeditem onclick="_f_setScan_i_save" text="保存" icon="icon80.png"/>
			<aos:dockeditem onclick="#_w_setScan_i.hide();" text="关闭" icon="close.png"/>
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
	<aos:window id="_video_look" title="视频播放"  width="700" height="555"  >
		<aos:formpanel width="695" height="550" id="_f_video_look" layout="column" contentEl="videolook" >
		</aos:formpanel>
		<aos:docked dock="bottom" ui="footer">
			<aos:dockeditem xtype="tbfill" />
			<aos:dockeditem onclick="#_video_look.hide();" text="关闭" icon="close.png" />
		</aos:docked>
	</aos:window>

	<script type="text/javascript">
		/*function _w_ceshi_show(){
			AOS.ajax({
				url: 'getShuju.jhtml',
				ok: function (data) {
				}
			});
		}*/
		_video_look.on("close",init_video);
		function init_video(){
			document.getElementById("videolook").src="";
		}

		function open(){
			_video_look.show();
			//document.getElementById("videolook").show();
			document.getElementById("videolook").style.display="block";
			//this.getElementByXid(‘domXid’).style.display=”block”;
			var me=Ext.getCmp("uploadpanel").getSelectionModel().getSelection();
			var id = me[0].get('pid');
			var tid = me[0].get('tid');
			var dirname=me[0].get('dirname');
			var name=me[0].get('name');
			document.getElementById("videolook").src="../../../Data/"+dirname+name;

			//var rowIndex = Ext.getCmp("uploadpanel").getStore().indexOf(me[0]);
		}
		function _open_video(){
			/*var file = "D//延边速生林栽培技术培训考察团-访欧纪实1_4.RMVB";
			var url = URL.createObjectURL(file);
			document.getElementById("videolook").show() = file;*/

		}


		function _w_data_i_render(){
		//_w_data_input('_f_data_i');

		}
		function _w_data_u_onshow(){
		var record = AOS.selectone(_g_data);
			var insert=Ext.getCmp('insert').getValue();
			//此时把年度。 全宗号，机构问题代码 直接携带过去
				AOS.ajax({
					params: {
						id_: record.data.id_,
						tablename: tablename.getValue()
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
				treenumber:treenumber.getValue(),
                _classtree:_classtree.getValue(),
                cascode_id_:_classtree.getValue(),
				tablename : tablename.getValue()
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
			var record = AOS.selectone1(_g_data);
		});
		//弹出新增用户窗口
        function _w_data_show() {
			AOS.reset(_f_data_i);
                _w_data_i.show();
                var dalx= Ext.getCmp("dzdalx").getValue();
                var treename=Ext.getCmp("treename").getValue();
                var treenumber=Ext.getCmp("treenumber").getValue();
                if(treename!=null&&treename!=""&&treename==="全部"){

				}else{
					//Ext.getCmp('_f_data_i').getForm().findField('qzmc').setValue(Ext.getCmp("treename").getValue());
					//Ext.getCmp('_f_data_i').getForm().findField('qzh').setValue(Ext.getCmp("treenumber").getValue());
				}
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
				forms : _f_data_i,
				url : 'saveData.jhtml',
				params:{
					_classtree:_classtree.getValue(),
					tablename : tablename.getValue()
				},
				ok : function(data) {
					if (data.appcode === -1) {
						AOS.err(data.appmsg);
					} else {
						_w_data_i.hide();
						_g_data_store.reload();
						AOS.tip(data.appmsg);
					}
				}
			});
		}
		//转入管理库
		function _w_dataTototal_show(){
			var query=Ext.getCmp("querySession").getValue();
			AOS.ajax({
				url : 'dataTototal.jhtml',
				params:{
					query:query,
					tablename : tablename.getValue()
				},
				ok : function(data) {
					_g_data_store.reload();
					AOS.tip(data.appmsg);
				}
			});
		}
		//卡片新增目录加存
			function _f_data_i_save(){
				var ztlx="";
				var ztCheck = Ext.getCmp('zztlx_add').items;
				for(var i = 0; i < ztCheck.length; i++){
					if(ztCheck.get(i).checked==true){
						if(ztlx==""){
							ztlx=ztCheck.get(i).boxLabel;
						}else{
							ztlx=ztlx+","+ztCheck.get(i).boxLabel;
						}
					}
				}
				AOS.ajax({
					forms : _f_data_i,
					url : 'saveData.jhtml',
					params:{
						tablename : tablename.getValue(),
						ztlx:ztlx,
						flag:0,
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
		//修改目录保存
			function _f_data_edit(){
				var ztlx="";
				var ztCheck = Ext.getCmp('zztlx_add').items;
				for(var i = 0; i < ztCheck.length; i++){
					if(ztCheck.get(i).checked==true){
						if(ztlx==""){
							ztlx=ztCheck.get(i).boxLabel;
						}else{
							ztlx=ztlx+","+ztCheck.get(i).boxLabel;
						}
					}
				}
				var record = AOS.selectone(_g_data);
				AOS.ajax({
					forms : _f_data_i,
					url : 'updateData.jhtml',
					params:{
						tablename : tablename.getValue(),
						ztlx:ztlx,
						id:record.data.id_
					},
					ok : function(data) {
						if (data.appcode === -1) {
							AOS.err(data.appmsg);
						} else {
							//_w_data_i.hide();
							//_g_data_store.reload();
							AOS.tip(data.appmsg);
							_w_data_i.hide();
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
                        url: 'deleteData_back.jhtml',
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
			var user=Ext.getCmp("user").getValue();
		var record = AOS.selectone(_g_data);
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
		//parent.fnaddtab(row.data.id, '电子文件',
		//					'archive/data/openFile.jhtml?id='+row.data.pid+'&tid='+row.data.tid+'&type='+row.data.type+'&tablename='+tablename.getValue());
			//+'&dh='+record.data.dh
			var type=row.data.type;
			if(type=="mp4"){
				open();
			}else{
				parent.fnaddtab(row.data.pid, '电子文件',
						'archive/data/openPdfFile.jhtml?id='+row.data.pid+'&tid='+row.data.tid+'&type='+row.data.type+'&tablename='+tablename.getValue());
			}

			/*parent.fnaddtab(row.data.id, '电子文件',
					'archive/data/openFile.jhtml?id='+row.data.pid+'&tid='+row.data.tid+'&type='+row.data.type+'&tablename='+tablename.getValue()+'&index='+rowIndex);*/
            /*parent.fnaddtab(row.data.id, '电子文件',
                                 'archive/data1/initZpData.jhtml?id='+row.data.pid+'&tid='+row.data.tid+'&type='+row.data.type+'&tablename='+tablename.getValue());*/
		}
       },
       onUpload : function(){
		   if(user!="root"){
			   AOS.err("没有权限!");
			   return;
		   }
       var me=Ext.getCmp("uploadpanel");
       if (this.swfupload&&this.store.getCount()>0) {
            if (this.swfupload.getStats().files_queued > 0){
                this.showBtn(this,false);
                this.swfupload.uploadStopped = false;
                this.swfupload.startUpload();
            }
        }
       // this.swfupload.destroy();
	},
	deletePath:	function(grid, rowIndex, colIndex) {
		if(user!="root"){
			AOS.err("没有权限!");
			return;
		}
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
		   if(user!="root"){
			   AOS.err("没有权限!");
			   return;
		   }
    	 //得到选中的条目
           var me=Ext.getCmp("uploadpanel").getSelectionModel().getSelection();
			var id = me[0].get('pid');
           AOS.file('downloadPath.jhtml?id_='+id+'&tablename='+tablename.getValue());
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
					AOS.file('downloadPath.jhtml?id_='+id+'&tablename='+tablename.getValue());
				}
			},
       upload_complete_handler : function(file){
		   if(user!="root"){
			   AOS.err("没有权限!");
			   return;
		   }
       var me =Ext.getCmp("uploadpanel");
       
        AOS.ajax({
		params: {tid: record.data.id_,tablename:tablename.getValue()},
		url: 'getPath.jhtml',
		ok: function (data) {
		for(i in data){
		me.store.getAt(file.index).set({"pid":data[i].id_,"tid":data[i].tid,"dirname":data[i].dirname});
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
		 upload_url : "${cxt}/archive/upload/archiveUpload.jhtml"
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
	function remember_load(tablename,type,value,name){
		AOS.ajax({
			params: {
				tablename:tablename,
				type:type,
				flag:value,
				name:name
			}, // 提交参数,
			url: 'saveRemember.jhtml',
			ok: function (data) {

			}
		});
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
		dirname:data[i].dirname,
        fileName:data[i].filename,
        type:data[i].filetype,
        percent:100,
        status:-4,
        });
		}
		}
		});
		//在得到当前电子文件选框的状态
		AOS.ajax({
			params: {
				tablename:Ext.getCmp("tablename").getValue(),
				type:"checkbox"
			}, // 提交参数,
			url: 'getRemember.jhtml',
			ok: function (data) {
				/*if(data.ocr==""||typeof(data.ocr) == "undefined"){
					Ext.getCmp("ocr").setValue(false);
				}else{
					Ext.getCmp("ocr").setValue(data.ocr);
				}
				if(data.mark==""||typeof(data.mark) == "undefined"){
					Ext.getCmp("mark").setValue(false);
				}else{
					Ext.getCmp("mark").setValue(data.mark);
				}*/
			}
		});
	}
	function w_data_path_onclose(){
	_g_data_store.load();
	}

		function changeRowClass(record, rowIndex, rowParams, store){

			//得到当前行的指定的列的值
			if(record.get("_path")>=1){
				return 'grid-one-column';
			}else{
				return 'grid-zero-column';
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
	function _f_data_query() {
		var params = AOS.getValue('_f_query');
		var form = Ext.getCmp('_f_query');
		var tmp = columnnum.getValue();
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
		//走后台判断一下是不是敏感词语，如果是敏感词拦截 不让其进行查询操作
			params["cascode_id_"]=_classtree.getValue();
					_g_data_store.getProxy().extraParams = params;
					_g_data_store.load();
					_w_query_q.hide();
					AOS.reset(_f_query);
					AOS.ajax({
						params:params,
						url: 'saveQueryData.jhtml',
						ok: function (data) {
							//此时在隐藏域中存入查询条件
							Ext.getCmp("querySession").setValue(data.appmsg);
						}
					});
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
					var ztCheck = Ext.getCmp('zztlx_add').items;
					for(var i = 0; i < ztCheck.length; i++){
						if(AOS.selectone(_g_data).data.ztlx.indexOf(ztCheck.get(i).boxLabel)!=-1){
							ztCheck.get(i).setValue(true);
						}
					}
					_w_data_i.show();
				}
			});



	}
	//记录更新
	function _f_data_update(){
		var query=Ext.getCmp("querySession").getValue();
	AOS.ajax({
					forms : _f_update,
					url : 'updateRecord.jhtml',
					params:{
						query:query,
				tablename : tablename.getValue()
			},
					ok : function(data) {
						_w_query_edit_term.hide();
                            _g_data_store.reload();
                            AOS.tip(data.appmsg);
					}
				});
	}
	//记录替换
	function _f_data_replace(){
		var query=Ext.getCmp("querySession").getValue();
	AOS.ajax({
					forms : _f_replace,
					url : 'replaceRecord.jhtml',
					params:{
						query:query,
				tablename : tablename.getValue()
			},
					ok : function(data) {
						_w_query_edit_term.hide();
                            _g_data_store.reload();
                            AOS.tip(data.appmsg);
					}
				});
	}
	//前后辍
	function _f_data_suffix(){
		var query=Ext.getCmp("querySession").getValue();
	AOS.ajax({
					forms : _f_suffix,
					url : 'suffixRecord.jhtml',
					params:{query:query,
				tablename : tablename.getValue()
			},
					ok : function(data) {
						_w_query_edit_term.hide();
                            _g_data_store.reload();
                            AOS.tip(data.appmsg);
					}
				});
	}
	//部位
	function _f_data_repair(){
        	var query=Ext.getCmp("querySession").getValue();

		AOS.ajax({
			forms : _f_repair,
			url : 'repairRecord.jhtml',
			params:{
				query:query,
		tablename : tablename.getValue()
	},
			ok : function(data) {
				_w_query_edit_term.hide();
                    _g_data_store.reload();
                    
                    AOS.tip(data.appmsg);
			}
		});
	}
	//记录更新2
	function _f_data_update2(){
		var query=Ext.getCmp("querySession").getValue();

	AOS.ajax({
					forms : _f_update2,
					url : 'updateRecord.jhtml',
					params:{
						query:query,
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
		var query=Ext.getCmp("querySession").getValue();
	AOS.ajax({
					forms : _f_replace2,
					url : 'replaceRecord.jhtml',
					params:{
						query:query,
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
		var query=Ext.getCmp("querySession").getValue();
	AOS.ajax({
					forms : _f_suffix2,
					url : 'suffixRecord.jhtml',
					params:{
						query:query,
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
		var query=Ext.getCmp("querySession").getValue();
		AOS.ajax({
			forms : _f_repair2,
			url : 'repairRecord.jhtml',
			params:{
				query:query,
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
			var query=Ext.getCmp("querySession").getValue();
			AOS.ajax({
				url : 'fillReport.jhtml',
				params:{
					query:query,
				tablename : tablename.getValue()
			},
				ok : function(data) {
					AOS.file('${cxt}/report/xls.jhtml');
				}
			});
		}

		//生成XLSX报表
		function fn_xlsx() {
			var query=Ext.getCmp("querySession").getValue();
			var _classtree=Ext.getCmp("_classtree").getValue();
			AOS.ajax({
				url : 'fillReport.jhtml',
				params:{
					query:query,
					_classtree:_classtree,
				tablename : tablename.getValue()
			},
				ok : function(data) {
					AOS.file('${cxt}/report/xlsx.jhtml');
				}
			});
		}

		//导入窗口
        function _w_import_show() {
			//我得加个重置
			var tablename=Ext.getCmp("tablename").getValue();
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
		function _import_data(){
			var _classtree=Ext.getCmp("_classtree").getValue();
			var file=$("#filename").attr("title");
			window.parent.fnaddtab('1213','数据导入','/archive/data/initImport.jhtml?tablename='+tablename.getValue()+"&_classtree="+_classtree+'&file='+file);
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
		function fn_button_render(){
			return '<input type="button" value="详情" class="cbtn" onclick="_w_details_show(this);" />';
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
	//移交
	function _w_transfer_show(){
	var strtemp=tablename.getValue();
	if(strtemp=="wsda"|| strtemp=="ctda"){
	AOS.ajax({
				url : 'fillReportgd.jhtml',
				params:{
				tablename : tablename.getValue()
			},
				ok : function(data) {
					AOS.file('${cxt}/report/xls.jhtml');
				}
			});
	}else
		return;
	
	//	_w_data_transfer.show();
	
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
		var query=Ext.getCmp("querySession").getValue();
		var msg = AOS.merge('确认要删除用户数据吗？');
        AOS.confirm(msg, function (btn) {
            if (btn === 'cancel') {
                AOS.tip('删除操作被取消。');
                return;
            }
		 AOS.ajax({
             url: 'deleteAllData_back.jhtml',
             params: {
				 query:query,
                 tablename: tablename.getValue()
             },
             ok: function (data) {
                 AOS.tip(data.appmsg);
                 _g_data_store.reload();
             }
         });
         });
	}
	//统计档案
	function countarchive(){
		 var tablename="<%=session.getAttribute("tablename")%>";
		 var id=Ext.getCmp('countname').getValue();
	      var params = {
					tablename:tablename
				};
				 if (!AOS.empty(id)) {
					params.tid = id;
				}
		_g_field_demo_store.getProxy().extraParams = params;
		_g_field_demo_store.load();
	}
	function _g_field_list(){
		var params = {
				tablename : tablename.getValue()
			};
			//这个Store的命名规则为：表格ID+"_store"。
			_g_field_store.getProxy().extraParams = params;
			_g_field_store.load();
	}
	function _w_order_show(){
        _w_order.show();
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
	//下载说明书
	function backup_book(){	  
	       AOS.file("downloadbook.jhtml");	            	
	   }
	//更新分类
	function _f_data_refresh(){
		AOS.ajax({
			forms : _f_catelogy,
			url : 'refreshcategory.jhtml',
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
					_g_data_store.load();
				}else if(data.appcode===0){
					AOS.tip("排列失败!");
				}
			}
		});
	}
	//返回上一次查询
	function _f_last_query(){
		//获取当前查询次数状态，
		var tablename="<%=session.getAttribute("tablename")%>";
		var selectmath=Ext.getCmp("selectmath").getValue();
		Ext.Ajax.request({   
			 url:'getSelectWhereLast.jhtml',
            params:{tablename:tablename,
            	selectmath:selectmath},
           method : 'post', 
			success:function(response,config){
				//对后台输出的Json进行解码
				json=Ext.decode(response.responseText);
			
				if(json.total!=0){
					for(var k=0;k<json.total;k++){
						var andvalue=json.data[k].and;
						var filednamevalue=json.data[k].filedname;
						var conditionvalue=json.data[k].condition;
						var contentvalue=json.data[k].content;
						Ext.getCmp('_f_query').down("[name='and"+(k+1)+"']").setValue(andvalue);
						Ext.getCmp('_f_query').down("[name='filedname"+(k+1)+"']").setValue(filednamevalue);
						Ext.getCmp('_f_query').down("[name='condition"+(k+1)+"']").setValue(conditionvalue);
						Ext.getCmp('_f_query').down("[name='content"+(k+1)+"']").setValue(contentvalue);
					}
				}
				Ext.getCmp("selectmath").setValue(json.selectmath);
			}
		});
	}
	function _f_next_query(){
		//获取当前查询次数状态，
		var tablename="<%=session.getAttribute("tablename")%>";
		var selectmath=Ext.getCmp("selectmath").getValue();
		Ext.Ajax.request({   
			 url:'getSelectWhereNext.jhtml',
            params:{tablename:tablename,
            	selectmath:selectmath},
           method : 'post', 
			success:function(response,config){
				//对后台输出的Json进行解码
				json=Ext.decode(response.responseText);
			
				if(json.total!=0){
					for(var k=0;k<json.total;k++){
						var andvalue=json.data[k].and;
						var filednamevalue=json.data[k].filedname;
						var conditionvalue=json.data[k].condition;
						var contentvalue=json.data[k].content;
						Ext.getCmp('_f_query').down("[name='and"+(k+1)+"']").setValue(andvalue);
						Ext.getCmp('_f_query').down("[name='filedname"+(k+1)+"']").setValue(filednamevalue);
						Ext.getCmp('_f_query').down("[name='condition"+(k+1)+"']").setValue(conditionvalue);
						Ext.getCmp('_f_query').down("[name='content"+(k+1)+"']").setValue(contentvalue);
					}
				}
				Ext.getCmp("selectmath").setValue(json.selectmath);
			}
		});
	}
	//清空查询条件
	function _f_drop_query(){
		var tablename="<%=session.getAttribute("tablename")%>";
		AOS.ajax({
			url : 'removeDataWhere.jhtml',
			params:{
				tablename : tablename
	        },
			ok : function(data) {
				Ext.getCmp("selectmath").setValue("0");
			}
		});
	}

	function ceshi(){
		var pagesize=_g_data_store.pageSize;
		for(var i=0;i<pagesize;i++){
			var _path=_g_data_store.getAt(i).get("_path");
			alert(_path);
				//Ext.getCmp("_g_data").getView().getRow(i).style.backgroundColor='#F7FE2E';
		}
	}
	//导出html格式报表
	function fn_html(){
		//吧页面中的数值传递过去
		//当前页，每页条目数，查询条件，排列方式传递过去
		//查询方式
		var query=Ext.getCmp("querySession").getValue();
		//查询方式含有中文进行编码
		var chquery=encodeURI(query);
		AOS.file("downloadHtml.jhtml?page="+_g_data_store.currentPage+"&pagesize="+_g_data_store.pageSize+"&query="+chquery+"&tablename="+tablename.getValue());
	}
	//导出txt格式报表
	function fn_txt(){
		//吧页面中的数值传递过去
		//当前页，每页条目数，查询条件，排列方式传递过去
		//查询方式
		var query=Ext.getCmp("querySession").getValue();
		//查询方式含有中文进行编码
		var chquery=encodeURI(query);
		AOS.file("downloadTxt.jhtml?page="+_g_data_store.currentPage+"&pagesize="+_g_data_store.pageSize+"&query="+chquery+"&tablename="+tablename.getValue());
	}
	//统计数据
	function _w_count_show(){
		_w_count.show();
		//隐藏三个
		Ext.getCmp("_g_qzh").hide();
		Ext.getCmp("_g_ys").hide();
		Ext.getCmp("_g_path").hide();
	}
	//添加条件
	function add_order(){
		//获取对应几个文本框的值
		//1.获取选择条件
		var fieldname=Ext.getCmp('_f_count').down("[name='filedname']").getValue();
		var condition=Ext.getCmp('_f_count').down("[name='condition']").getValue();
		var content=Ext.getCmp('_f_count').down("[name='content']").getValue();
		var r1=Ext.getCmp('_f_count').down("[name='r1']").getValue();
		if(fieldname==null){
			return;
		}else{	
			var selectorder="";
			var selectmath="";
			if(condition=="like"){
				condition="like";
				selectorder=r1+" "+fieldname+" like ";
				selectmath="%"+content+"%";
			}else if(condition=="left"){
				selectorder=r1+" "+fieldname+" like ";
				selectmath="%"+content;
			}else if(condition=="right"){
				selectorder=r1+" "+fieldname+" like ";
				selectmath=content+"%";
			}else if(condition=="null"){
				selectorder=r1+" "+fieldname+" is ";
				selectmath="null";
			}else{
				selectorder=r1+" "+fieldname+" "+condition;
				selectmath=content;
			}
        	var data=[{
					'selectorder':selectorder,
					'selectmath':selectmath             	
					}];
			//在第一行添加
			// _g_order_store.insert(0,data);
			//在尾行添加
			_g_count_store.loadData(data,true);
		}
	}
	//删除单添加项
	function delete_order(){
		var row=_g_count.getSelectionModel().getSelection();
		//右侧删除，左侧添加
		_g_count_store.remove(row);
	}
	function remove_order(){
		_g_count_store.removeAll();
	}
	//确定执行统计设计
	function _w_count_submit(){
		//如果统计字段和统计方法没有选中直接不执行
		var countfield=Ext.getCmp('_f_count').down("[name='countfield']").getValue();
		var countmethod=Ext.getCmp('_f_count').down("[name='countmethod']").getValue();
		if(countfield==null||countmethod==null){
			AOS.tip("请选中统计的字段和统计方法!");
			return;
		}
		var s=Ext.getCmp('_g_count').getStore();
		var selectorders="";
		var selectmaths="";
        for(var i = 0 ;i< s.getCount(); i++){       	
            selectorders+=s.getAt(i).get('selectorder')+",";
            selectmaths+=s.getAt(i).get('selectmath')+",";
        }
        //去掉最后一个字符
        selectorders=selectorders.substring(0,selectorders.length-1);
        selectmaths=selectmaths.substring(0,selectmaths.length-1);
		//此时执行查询操作
		var tablename="<%=session.getAttribute("tablename")%>";
		AOS.ajax({
			url : 'selectCount.jhtml',
			forms:_f_count,
			params:{
				tablename : tablename,
				selectorders:selectorders,
				selectmaths:selectmaths
	        },
			ok : function(data) {
				//var countmethoddisplay=Ext.getCmp('_f_count').down("[name='countmethod']").getRawValue();
				if(countfield=="qzh"){

					//此时把对应的进行赋值
					Ext.getCmp("_g_ys").hide();
					Ext.getCmp("_g_path").hide();
					Ext.getCmp("_g_qzh").show();
					//赋值
					_g_qzh_store.removeAll();
					for(var i=0;i<data.length;i++){

						var row=[{
							'qzh':data[i]["qzh"],
							'qzhsum':data[i]["coun"],
							'yssum':data[i]["ys"]
						}];
						_g_qzh_store.insert(0,row);
					}
				}else if(countfield=="ys"){
					Ext.getCmp("_g_path").hide();
					Ext.getCmp("_g_qzh").hide();
					Ext.getCmp("_g_ys").show();
					_g_ys_store.removeAll();
					for(var i=0;i<data.length;i++){
						var row=[{
							'yssum':data[i]["ys"]
						}];
						_g_ys_store.insert(0,row);
					}
				}else if(countfield=="_path"){
					Ext.getCmp("_g_ys").hide();
					Ext.getCmp("_g_qzh").hide();
					Ext.getCmp("_g_path").show();
					_g_path_store.removeAll();
					for(var i=0;i<data.length;i++){
						var row=[{
							'pathsum':data[i]["_path"]
						}];
						_g_path_store.insert(0,row);
					}
				}
			}
		});
	}
	//字段组合添加
	function add_tablefield(){
		var updatetablefield=Ext.getCmp('_f_tablefield').down("[name='updatetablefield']").getValue();
		var selecttablefield=Ext.getCmp('_f_tablefield').down("[name='selecttablefield']").getValue();
		var content=Ext.getCmp('_f_tablefield').down("[name='condition']").getValue();
		if(content==null||content=="null"||content==""){
			countent="";
		}
		var repair_long=Ext.getCmp('_f_tablefield').down("[name='repair_long']").getValue();
		if(updatetablefield==""||updatetablefield==null){
			AOS.tip("请选择更新字段！");
			return;
		}
		if(selecttablefield==""||selecttablefield==null){
			AOS.tip("请选择合成字段！");
			return;
		}
		
		//得到文本框添加的内容
		var data=[{
			'jointorder':selecttablefield,
			'jointmath':content+repair_long            	
			}];
	//在第一行添加
	// _g_order_store.insert(0,data);
	//在尾行添加
	_g_tablefield_store.loadData(data,true);
	}
	//删除单添加项
	function delete_tablefield(){
		var row=_g_tablefield.getSelectionModel().getSelection();
		//右侧删除，左侧添加
		_g_tablefield_store.remove(row);
	}
	function remove_tablefield(){
		_g_tablefield_store.removeAll();
	}
	//拼接
	function _f_data_tablefield(){
		var updatetablefield=Ext.getCmp('_f_tablefield').down("[name='updatetablefield']").getValue();
		if(updatetablefield==""||updatetablefield==null){
			AOS.tip("请选择更新字段！");
			return;
		}
		var s=Ext.getCmp('_g_tablefield').getStore();
		if(s.getCount()==0){
			AOS.tip("组合条件为空!");
			return;
		}
		var jointorders="";
		var jointmaths="";
        for(var i = 0 ;i< s.getCount(); i++){       	
        	jointorders+=s.getAt(i).get('jointorder')+",";
            jointmaths+=s.getAt(i).get('jointmath')+",";
        }
        //去掉最后一个字符
        jointorders=jointorders.substring(0,jointorders.length-1);
        jointmaths=jointmaths.substring(0,jointmaths.length-1);
		//此时执行查询操作
		var tablename="<%=session.getAttribute("tablename")%>";
		var query=Ext.getCmp("querySession").getValue();
		AOS.ajax({
			url : 'selectjoint.jhtml',
			forms:_f_tablefield,
			params:{
				tablename : tablename,
				query:query,
				updatetablefield:updatetablefield,
				jointorders:jointorders,
				jointmaths:jointmaths
	        },
			ok : function(data) {
				if (data.appcode === -1) {
					AOS.tip(data.appmsg);
				}else{
					AOS.tip("更新成功!");
					_w_query_edit_term.hide();
					_g_data_store.load();
				}
			}
		});		
	}
	//对当前选中的行进行编辑操作
	function _w_edit_show(){
		var record=AOS.selection(_g_data,'id_');
		if (AOS.empty(record)){
            AOS.tip('请选择要标记的条目!');
            return;
        }
		Ext.form.Field.prototype.msgTarget = 'qtip';
		editing = _g_data.getPlugin('id_plugin');
		//editing.cancelEdit();
		//得到当前选中的航标
		//选中行索引
		var selectone=AOS.selectone(_g_data);
		var index=_g_data_store.indexOf(selectone);
		editing.startEdit(index, 2);
	}
	//编辑模式，
	function fn_beforeedit(obj, e){
		var card_type_ = e.record.data.card_type_;
		editing = _g_data.getPlugin('id_plugin');
		var form = editing.editor.form;
		//将录入人和录入日期设置为只读模式,初检人和初检日期只读模式
		/*form.findField('_lrr').setReadOnly(true);
		form.findField('_lrrq').setReadOnly(true);
		form.findField('_cjr').setReadOnly(true);
		form.findField('_cjrq').setReadOnly(true);*/
		//将制定不可修改的值设为不可编辑findField
		form.findField('_lrr').readOnly=true;
		form.findField('_lrrq').readOnly=true;
		/*form.findField('_cjr').readOnly=true;
		form.findField('_cjrq').readOnly=true;*/
		/*AOS.read(form.findField('_lrr'));
		AOS.read(form.findField('_lrrq'));
		AOS.read(form.findField('_cjr'));
		AOS.read(form.findField('_cjrq'));*/
		//根据当前行的数据控制行编辑器
		/*if(card_type_ == '2'){
			 AOS.read(form.findField('nd'));
		}else{
			 AOS.edit(form.findField('nd'));
		}*/
	}
	function fn_edit(editor, e) {
		if (!e.record.dirty) {
			AOS.tip('数据没变化，提交操作取消。');
			return;
		}
		AOS.ajax({
			params : e.record.data,
			url : 'editGrid.jhtml',
			ok : function(data) {
				if (data.appcode === -1) {
                    AOS.err("修改失败");
                } else {
                    //_w_data_i.hide();
                     AOS.tip("修改成功");
                    _g_data_store.reload();
                  //客户端数据状态提交
    				e.record.commit();
                }	
				
			}
		});
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
	//导出xml封装数据的文件
	function fn_xml(){
		var query=Ext.getCmp("querySession").getValue();
		var tablename="<%=session.getAttribute("tablename")%>";
		AOS.ajax({
			url : 'deriveXML.jhtml',
			params:{
				query:query,
		tablename : tablename
		},
			ok : function(data) {

			}
		});
	}
	function Demo(record,rowIndex,rowParams,store){
		var path=record.get("_path");
		if(path>=1) {
			return "my_row_red";
		}
		}
		function viewScore_2(){
			AOS.ajax({
				url : 'jieshou.jhtml',
				ok : function(data) {

				}
			});
		}
	function viewScore(){
		//此时展开一个窗口，把当前的数据grid在展示出来
		//print(Ext.getCmp('_g_data').getStore());
		_w_print.show();
	}
	function _w_print_load(){
		var states=Ext.getCmp("_g_data").getStore();
		var prints=Ext.getCmp("_g_print").getStore();
			//进行行的判断，对于相同的进行显示，不相同的不予显示
		var excel_gridData = JSON.parse(JSON.stringify(Ext.pluck(states.data.items, 'data')));
		for(var key in excel_gridData) {
			prints.add(excel_gridData[key]);
		}
	}
		//生成PDF报表
		function fn_pdf() {
			AOS.ajax({
				url : 'fillReport_wsda.jhtml',
				params:{tablename:tablename.getValue()},
				ok : function(data) {
					parent.fnaddtab('${param.aos_module_id_ }_2', '预览PDF报表',
							'report/pdf.jhtml');
				}
			});
		}
		function _g_field_order_show(){
			_w_look_order.show();
		}
		//查询数据表列信息
		function _g_order_query() {
			//AOS.reset(_g_table);
			var params = {
				tablename : tablename.getValue()
			};
			_g_look_order_store.getProxy().extraParams = params;
			_g_look_order_store.load();
		}

		function _submit_order(){
			var store=Ext.getCmp("_g_look_order").getStore();
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
						_w_look_order.hide();
						//_g_data_store.load();
						location.reload();
						//_g_data_query;
					}
				}
			});
		}
		function print_report(){
			var record=AOS.selection(_g_print,'printname');
			if (AOS.empty(record)){
				AOS.tip('请选择要打印的报表!');
				return;
			}
			if(AOS.rows(_g_print)>1){
				AOS.tip('只能选择一个报表进行打印!');
				return;
			}
			var printname=AOS.selectone(_g_print).data.printname;
        	//当前用户
			var user="<%=session.getAttribute("user")%>";
        	//当前页
			var currentPage=_g_data_store.currentPage;
			//页面大小（每页多少条）
			var pagesize=_g_data_store.pageSize;
			AOS.ajax({
				params:{
					user:user,
					tablename:Ext.getCmp("tablename").getValue(),
					_classtree:Ext.getCmp("_classtree").getValue(),
					querySession:Ext.getCmp("querySession").getValue(),
					treenumber:Ext.getCmp("treenumber").getValue(),
					currentPage:currentPage,
					pagesize:pagesize,
					printname:printname
				},
				url : 'fillReport_gd.jhtml',
				ok : function(data) {
					parent.fnaddtab('123_2', '预览PDF报表',
							'report/pdf.jhtml');
				}
			});
		}
		function load_print_list(){
			_g_print_store.reload();
		}
		//数据核检
		function _w_check_data(){
			var treenumber=Ext.getCmp("treenumber").getValue();

			var cascode_id_=Ext.getCmp("_classtree").getValue();
			var aos_module_id_=Ext.getCmp("aos_module_id_").getValue();

			var tablename=Ext.getCmp("tablename").getValue();
			var treename=Ext.getCmp("treename").getValue();
			var querySession=Ext.getCmp("querySession").getValue();
			parent.fnaddtab('sjhj', '数据核检',
					'archive/data_sjhj/initData_sjhj.jhtml?treenumber='+treenumber+"&tablename="+tablename+"&treename="+treename+"&querySession="+querySession+"&cascode_id_="+cascode_id_+"&aos_module_id_="+aos_module_id_);

		}
		//打印2操作编辑页面
		function print_edit(){
			window.parent.fnaddtab('12344','票据打印','printindex.jhtml?PrintType=DepositDesign');
		}
		//弹出上传文件窗口
		function _fileupload_show() {
        	//我得加个重置
			var record=AOS.selection(_g_data,'id_');
			if (AOS.empty(record)){
				AOS.tip('请选择要上传的条目!');
				return;
			}else{
				var tablename=Ext.getCmp("tablename").getValue();
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
						allowedFileExtensions: ['jpg', 'gif', 'png','pdf','tif'],//接收的文件后缀
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
		//文件预览
		function _onlinefile_show() {
			var record = AOS.selectone(_g_data);
			if (!AOS.empty(record)) {
				parent.fnaddtab("", '电子文件', 'archive/data/openOnlineFile.jhtml?id='+ record.data.id_,"",tablename.getValue(),"");
			}
		}
		//上一页
		function _f_previous_data(){
			var count=Ext.getCmp("_g_data").getStore().getCount();
			var me=Ext.getCmp("_g_data").getSelectionModel().getSelection();
			//var record = AOS.selectone(_g_data);
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
		function _w_setScan_show() {
			pathStr="";
			_w_setScan_i.show();
		}

		function _w_setScan_i_onshow() {

			AOS.ajax({
				url: 'getSetScan.jhtml',
				params: {
					tablename: tablename.getValue()
				},
				ok: function (data) {
					if (data.appcode === -1) {
						AOS.err(data.appmsg);
					} else {
						Ext.getCmp("id").setValue(data.id_);
						Ext.getCmp("caption").setValue(data.caption);
						Ext.getCmp("tmname").setValue(data.tm);
						Ext.getCmp("capname").setValue(data.capname);

					}
				}
			});
		}

		//扫描保存
		function _f_setScan_i_save() {
			//_f_setScan_i.form.reset();
			AOS.ajax({
				forms: _f_setScan_i,
				url: 'saveSetScan.jhtml',
				params: {
					//tablename: tablename.getValue()
				},
				ok: function (data) {
					if (data.appcode === -1) {
						AOS.err(data.appmsg);
					} else {
						_w_setScan_i.hide();
						AOS.tip(data.appmsg);
					}
				}
			});
		}

		//加载
		function fn_load1() {
			var path = Ext.getCmp('selpath').value;
			if ( pathStr.length=== 0) {
				pathStr = path;
			}else {
				pathStr += "-" + path;
			}
			Ext.getCmp("caption").setValue(pathStr);

		}

		function fn_load2() {
			var selname = Ext.getCmp('selname').value;
			Ext.getCmp('capname').setValue(selname);
		}

		function fn_load3() {
			var selShowName = Ext.getCmp('selShowName').value;
			Ext.getCmp("tmname").setValue(selShowName);
		}
		function fn_scan(){
			var record = AOS.selectone(_g_data);
			var querySession=Ext.getCmp("querySession").getValue();
			//alert(querySession);
			AOS.ajax({
				url: 'addSetscan.jhtml',
				params:{
					query:querySession,
					tablename : tablename.getValue()
				},
				ok: function (data) {
					if (data.appcode === -1) {
						AOS.err(data.appmsg);
					}if(data.appcode === 1){
						var str1="Winpdf://"+tablename.getValue()+","+record.data.id_+","+user.getValue();
						window.location.href=str1;
					}
				}
			});
		}
	</script>

</aos:onready>
	<script type="text/javascript">
		function _w_details_show(a){
			var record=Ext.getCmp('_g_data').getSelectionModel().getSelection();
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
	</script>
</aos:html>
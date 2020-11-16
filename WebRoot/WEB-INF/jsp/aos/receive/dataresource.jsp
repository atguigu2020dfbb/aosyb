<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="/WEB-INF/tld/aos.tld" prefix="aos"%>
<%@ taglib uri="/WEB-INF/tld/cll.tld" prefix="c"%>
<aos:html>
<aos:head title="资源信息">
	<aos:include lib="ext,swfupload" />
	<aos:base href="archive/datatemporary" />
	<style>
		.my_row_red .x-grid-cell {
		background-color: #99FF99;
		}
	</style>
</aos:head>

<aos:body>
</aos:body>
<aos:onready>
	<aos:viewport layout="border">
		<aos:gridpanel id="_g_data" url="listresource.jhtml" region="center" split="true"
			onrender="_g_entity_query"
			 enableLocking="false" >
			<aos:docked>
				<aos:hiddenfield id="tablename" name="tablename" value="archive_resource" />
				<aos:dockeditem xtype="tbtext" text="实体信息" />
				<aos:dockeditem text="新建资源" icon="add2.png"  onclick="_g_add_pici" id="_f_add_pici"/>
				<aos:dockeditem text="附件" icon="picture.png"  onclick="_w_picture_show" id="_f_add_path"/>
				<aos:dockeditem text="提交审核" icon="more/archive-extract-3.png" onclick="_g_receive_pici" id="_f_receive_pici"/>
				<aos:dockeditem text="领导审核" icon="folder8.png" onclick="_w_leader_examine" id="_f_leader_examine" />
				<aos:dockeditem text="正式接收" icon="folder8.png" onclick="_w_formal_receive"  id="_f_formal_receive"/>
			</aos:docked>
			<aos:selmodel type="row" mode="multi" />
			<aos:column dataIndex="id_" header="流水号" hidden="true" />
			<aos:column dataIndex="_path" header="电子文件" rendererFn="fn_path_render" />
			<aos:column dataIndex="zymc" header="资源名称" />
			<aos:column dataIndex="zyms" header="资源描述" />
			<aos:column dataIndex="zysj" header="资源时间"  />
			<aos:column dataIndex="jsr" header="接收人"  />
			<aos:column dataIndex="jszt" header="接收状态"  />
			<aos:column dataIndex="shr" header="审核人"  />
			<aos:column dataIndex="shsj" header="审核时间"  />
			<aos:column dataIndex="shyj" header="审核意见"  />
			<aos:column dataIndex="flag_submit" header="提交状态" />
			<aos:column dataIndex="flag_examine" header="审核状态" />
			<aos:column header="" flex="1" />
		</aos:gridpanel>
	</aos:viewport>
	<aos:window id="_w_data_i" title="新增" width="1000" height="600"
		autoScroll="true" onshow="_w_data_i_onshow" y="100"
		onrender="_w_data_i_render">
		<aos:formpanel id="_f_data_i" width="980" layout="absolute">
			<aos:hiddenfield name="_classtree" id="_classtree"
				value="${cascode_id_}" />
		</aos:formpanel>
		<aos:docked dock="bottom" ui="footer">
			<aos:dockeditem xtype="tbfill" />
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
					<aos:hiddenfield name="cascode_id_" value="${cascode_id_ }" />
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
					<aos:combobox id="updatetablefield"
								  name="updatetablefield" fieldLabel="更新字段"
								  emptyText="${fieldss.fieldcnname}" maxWidth="300" labelWidth="60"
								  columnWidth="0.49" fields="['fieldenname','fieldcnname']"
								  regexText="${fieldss.fieldenname}" displayField="fieldcnname"
								  valueField="fieldenname" labelAlign="left"
								  url="queryFields.jhtml?tablename=${tablename}">
					</aos:combobox>
					<aos:displayfield columnWidth="0.49" />
					<aos:combobox id="selecttablefield"  fieldLabel="选取字段"
								  name="selecttablefield"
								  emptyText="${fieldss.fieldcnname}" maxWidth="300" labelWidth="60"
								  columnWidth="0.49" fields="['fieldenname','fieldcnname']"
								  regexText="${fieldss.fieldenname}" displayField="fieldcnname"
								  valueField="fieldenname" labelAlign="left"
								  url="queryFields.jhtml?tablename=${tablename}">
					</aos:combobox>
					<aos:displayfield columnWidth="0.49" />
					<aos:combobox name="condition"
								  labelWidth="20" columnWidth="0.2" value="-">
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
	<aos:window id="_w_query_del_term" title="批量删除" width="720"
		autoScroll="true" layout="fit">
		<aos:tabpanel id="_tabpanel2" region="center" activeTab="0"
			bodyBorder="0 0 0 0" tabBarHeight="30">
			<aos:tab title="高级删除" id="_tab_term">
				<aos:formpanel id="_f_term" layout="column" columnWidth="1">
					<aos:hiddenfield name="cascode_id_" value="${cascode_id_ }" />
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
				<aos:hiddenfield name="_classtree" id="_classtree"
				value="${cascode_id_}" />
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
	<aos:window id="_w_pici" title="新增任务" >
		<aos:formpanel id="_f_pici" width="420" layout="anchor" labelWidth="90">
			<aos:textfield name="zymc" fieldLabel="资源名称"  />
			<aos:datefield name="zysj" fieldLabel="资源时间" format="Y-m-d"
						   columnWidth="1.0" />
			<aos:textfield name="jsr" fieldLabel="接收人"  />
			<aos:textareafield name="zyms" fieldLabel="资源描述"  />
		</aos:formpanel>
		<aos:docked dock="bottom" ui="footer">
			<aos:dockeditem xtype="tbfill" />
			<aos:dockeditem onclick="_f_pici_submit" text="保存" icon="ok.png" />
			<aos:dockeditem onclick="#_w_pici.hide();" text="关闭" icon="close.png" />
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
		var str ='[{'   
                +'xtype : "textfield",'
                +'id:"'+fieldname+'",'
                +'name:"'+fieldname+'",'
                +'maxWidth : '+strwidth+','
				+'height : '+strheight+','
                +'x:'+strx+','
                +'y:'+stry+','
                +'maxLength:'+data.edtmax+','
                ;
                if(data.ynnnull=='0'){
	                str=str+'allowBlank:false,';
	            }
	            str=str+'listeners:{focus:function(e){';
	           
	            str=str+'},'//获得焦点事件结尾
	            +'blur:function(e){'
	             if(data.ynpw=='1'){
	            str=str+'var val=e.getValue();var len=val.length;  while(len < '+data.edtmax+') {val= "0" + val;len++;}e.setValue(val);';
	            
	            }
	            if(strdh.indexOf(fieldname)>-1){
	            
	            var strarray=strdh.split(',');
	            //alert(strarray[0]);
	            var strtemp='';
	            //alert(strdh);
	            for(var i=1;i<strarray.length; i++){
	            //alert(strarray[i]);
		            if(i==1){
		            strtemp ='Ext.getCmp("'+strarray[i]+'").getValue()';
		            continue;
		            }
	             
	             strtemp=strtemp+'+"-"+Ext.getCmp("'+strarray[i]+'").getValue()';
	             
	            }
	            //alert(strtemp);
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
			var record = AOS.selectone1(_g_data);
		});
		//弹出新增用户窗口
        function _w_data_show() {
                _w_data_i.show();
            }
        function _w_data_i_onshow(){
			var insert=Ext.getCmp('insert').getValue();
             AOS.ajax({
                    params: {id_: AOS.selectone1(_g_data).data.id_,
                    tablename:tablename.getValue()
                    },
                    url: 'getData.jhtml',
                    ok: function (data) {
                        _f_data_i.form.setValues(data);
                    }
                });

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
        parent.fnaddtab(row.data.id, '电子文件',
							'archive/datatemporary/openPdfFile.jhtml?id='+row.data.pid+'&tid='+row.data.tid+'&type='+row.data.type+'&tablename='+tablename.getValue());
			/*parent.fnaddtab(row.data.id, '电子文件',
					'archive/data/openFile.jhtml?id='+row.data.pid+'&tid='+row.data.tid+'&type='+row.data.type+'&tablename='+tablename.getValue()+'&index='+rowIndex);*/
            /*parent.fnaddtab(row.data.id, '电子文件',
                                 'archive/data1/initZpData.jhtml?id='+row.data.pid+'&tid='+row.data.tid+'&type='+row.data.type+'&tablename='+tablename.getValue());*/
		}
       },
       onUpload : function(){
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
			//批量下载
			onDownAllPath:function(){
				//得到选中的条目
				var grid_data=Ext.getCmp("uploadpanel").getStore();
				for(var i= 0 ;i< grid_data.data.length;i++){
					var row = grid_data.getAt(i);
					var id=row.get('pid');
					AOS.file('downloadPath.jhtml?id_='+id+'&tablename='+tablename.getValue());
				}
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
		w_data_path.on("close",w_data_path_onclose);
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
	function _g_entity_query() {
		_g_data_store.load();
	}
	function fn_g_data(){
	_w_data_i.show();
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
			var _classtree=Ext.getCmp("_classtree").getValue();
			AOS.ajax({
				url : 'fillReport.jhtml',
				params:{
					query:query,
					_classtree:_classtree,
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
			var _classtree=Ext.getCmp("_classtree").getValue();
			window.parent.fnaddtab('1213','数据导入','/archive/data/initImport.jhtml?tablename='+tablename.getValue()+"&_classtree="+_classtree);
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
		AOS.file("downloadHtml.jhtml?cascode_id_="+cascode_id_.getValue()+"&page="+_g_data_store.currentPage+"&pagesize="+_g_data_store.pageSize+"&query="+chquery+"&tablename="+tablename.getValue());
	}
	//导出txt格式报表
	function fn_txt(){
		//吧页面中的数值传递过去
		//当前页，每页条目数，查询条件，排列方式传递过去
		//查询方式
		var query=Ext.getCmp("querySession").getValue();
		//查询方式含有中文进行编码
		var chquery=encodeURI(query);
		AOS.file("downloadTxt.jhtml?cascode_id_="+cascode_id_.getValue()+"&page="+_g_data_store.currentPage+"&pagesize="+_g_data_store.pageSize+"&query="+chquery+"&tablename="+tablename.getValue());
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
	/*function fn_button_render(){
		return '<input type="button" value="指导意见" class="cbtn" onclick="_w_receive_show(this);" />';
		}*/
	function fn_button_render(){
			AOS.reset(_f_table);
		_w_zdyj.show();
		//window.parent.fnaddtab('','档案接收','receive/batch/initReceive.jhtml?tablename='+tablename);
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
		//新增批次
		function _g_add_pici(){
			AOS.reset(_f_pici);
			_w_pici.show();
		}
		function _f_pici_submit(){
			AOS.ajax({
				url: 'addresource.jhtml',
				forms:_f_pici,
				ok: function (data) {
					if(data.appcode===1){
						AOS.tip("添加成功!");
						_w_pici.hide();
						_g_data_store.reload();
					}else if(data.appcode===-1){
						AOS.tip("添加失败!");
					}
				}
			});
		}
        //接收提交
        function _g_receive_pici(){
            var selection = AOS.selection(_g_data, 'id_');
            if (AOS.empty(selection)) {
                AOS.tip('提交前请先选中数据。');
                return;
            }
            var record = AOS.selectone(_g_data);
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

            var msg = AOS.merge('确认要提交选中的[{0}]个任务数据吗？', AOS.rows(_g_data));
            AOS.confirm(msg, function (btn) {
                if (btn === 'cancel') {
                    AOS.tip('提交操作被取消。');
                    return;
                }
                AOS.ajax({
                    params:{'id_':id_ },
                    url : 'updateresource.jhtml',
                    ok : function(data) {
                        AOS.tip(data.appmsg);
                        _g_data_store.reload();
                    }
                });
            });
        }
        function _w_leader_examine(){
            var selection = AOS.selection(_g_data, 'id_');
            if (AOS.empty(selection)) {
                AOS.tip('审核前请先选中数据!');
                return;
            }
            var record = AOS.selectone(_g_data);
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
            var record = AOS.selectone(_g_data);
            var id_="";
            if(!AOS.empty(record)){
                id_=record.data.id_;
            }
            AOS.ajax({
                forms : _f_next_kf_message,
                params:{'id_':id_},
                url : 'updateresourceyes.jhtml',
                ok : function(data) {
                    _w_next_kf_message.hide();
					_g_resource_store.reload();
                    AOS.tip(data.appmsg);

                }
            });
        }
        function no_save_next_kf_message(){
            var record = AOS.selectone(_g_data);
            var id_="";
            if(!AOS.empty(record)){
                id_=record.data.id_;
            }
            AOS.ajax({
                forms : _f_next_kf_message,
                params:{'id_':id_},
                url : 'updateresourceno.jhtml',
                ok : function(data) {
                    _w_next_kf_message.hide();
                    AOS.tip(data.appmsg);

                }
            });
        }
        //正式接收
        function _w_formal_receive(){
            var selection = AOS.selection(_g_data, 'id_');
            if (AOS.empty(selection)) {
                AOS.tip('提交前请先选中数据!');
                return;
            }
            var record = AOS.selectone(_g_data);
            var flag_submit="";
            var flag_examine="";
            var boolean_examine="";
            var id_="";
            if(!AOS.empty(record)){
                flag_submit=record.data.flag_submit;
                flag_examine=record.data.flag_examine;
                boolean_examine=record.data.boolean_examine;
                id_=record.data.id_;
            }
                AOS.ajax({
                    params:{id_:id_},
                    url : 'resourceToreceiveData.jhtml',
                    ok : function(data) {
                        AOS.tip(data.appmsg);
                        _g_data_store.reload();
                    }
                });
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
	</script>
</aos:onready>
</aos:html>
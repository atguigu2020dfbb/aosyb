<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="/WEB-INF/tld/aos.tld" prefix="aos"%>
<aos:html>
<aos:head title="元数据信息设定">
	<aos:include lib="ext" />
	<aos:base href="system/metadata" />
</aos:head>
<aos:body>
</aos:body>
<aos:onready>
<aos:viewport layout="border">
		<aos:gridpanel id="_g_table" region="center" url="listAccounts.jhtml"
			onitemclick="_g_field_query" onrender="_g_table_query">
			<aos:docked>
				<aos:dockeditem xtype="tbtext" text="元数据表操作" />
			</aos:docked>
			<aos:plugins>
				<aos:editing id="id_plugin" ptype="cellediting" clicksToEdit="2" />
			</aos:plugins>
			<aos:selmodel type="row" mode="multi" />
			<aos:column header="表流水号" dataIndex="id_" hidden="true" />
			<aos:column header="数据表名称" dataIndex="tablename" width="90" />
			<aos:column header="数据表描述" dataIndex="tabledesc" width="80" />
		</aos:gridpanel>
		<aos:gridpanel id="_g_field" region="east" url="listFieldInfos.jhtml"
			split="true" splitterBorder="0 1 0 1" width="800" pageSize="60">
			<aos:menu>
				<aos:menuitem text="新增" onclick="_w_field_show" icon="add.png" />
				<aos:menuitem text="修改" onclick="_w_field_u_show" icon="edit.png" />
				<aos:menuitem text="删除" onclick="_g_field_del" icon="del.png" />
				<aos:menuitem xtype="menuseparator" />
				<aos:menuitem text="刷新" onclick="#_g_param_store.reload();"
					icon="refresh.png" />
			</aos:menu>
			<aos:docked>
				<aos:dockeditem xtype="tbtext" text="数据表字段操作" />
				<aos:dockeditem xtype="tbfill" />
				<aos:dockeditem text="新增" icon="add2.png" onclick="_w_field_show" />
				<aos:dockeditem text="修改" icon="edit2.png" onclick="_w_field_u_show" />
				<aos:dockeditem text="删除" icon="del2.png" onclick="_g_field_del" />
				<aos:dockeditem text="显示顺序" icon="icon146.png"
					onclick="_g_field_order_show" />
			</aos:docked>
			<aos:selmodel type="checkbox" mode="multi" />
			<aos:column type="rowno" />
			<aos:column header="流水号" dataIndex="id_" hidden="true" />
			<aos:column header="字段英文名" dataIndex="fieldenname" width="90" />
			<aos:column header="字段中文名称" dataIndex="fieldcnname" width="90" />
			<aos:column header="字段类型" dataIndex="fieldclass" width="90" />
			<aos:column header="字段长度" dataIndex="fieldsize" width="90" />
			<aos:column header="显示长度" dataIndex="dislen" width="90" />
			<aos:column header="是否显示" dataIndex="fieldview" width="90"
				rendererFn="fn_fieldview_render" />
		</aos:gridpanel>
		<aos:window id="_w_table" title="新增数据表" height="500" autoScroll="true">
			<aos:gridpanel hidePagebar="true" id="_g_field_demo"
				url="listFieldInfos.jhtml" width="800" pageSize="60">
				<aos:docked>
					<aos:dockeditem xtype="tbtext" text="数据表字段" />
				</aos:docked>
				<aos:column type="rowno" />
				<aos:column header="流水号" dataIndex="id_" hidden="true" />
				<aos:column header="字段英文名" dataIndex="fieldenname" width="90" />
				<aos:column header="字段中文名称" dataIndex="fieldcnname" width="90" />
				<aos:column header="字段类型" dataIndex="fieldclass" width="90" />
				<aos:column header="字段长度" dataIndex="fieldsize" width="90" />
			</aos:gridpanel>

			<aos:docked dock="bottom" ui="footer">
				<aos:dockeditem xtype="tbfill" />
				<aos:dockeditem onclick="_f_table_submit" text="保存" icon="ok.png" />
				<aos:dockeditem onclick="#_w_table.hide();" text="关闭"
					icon="close.png" />
			</aos:docked>
		</aos:window>

		<aos:window id="_w_table_u" title="修改数据表">
			<aos:formpanel id="_f_table_u" width="800" layout="column"
				labelWidth="75" height="500">
				<aos:fieldset title="创建数据表" columnWidth="1" checkboxToggle="false"
					collapsed="false">
					<aos:hiddenfield name="id_" />
					<aos:textfield fieldLabel="数据表名称" readOnly="true" name="tablename"
						vtype="alphanum" emptyText="只能输入字母和数字" columnWidth="0.5" />
					<aos:textfield fieldLabel="数据表描述" name="tabledesc"
						columnWidth="0.49" />
				</aos:fieldset>

			</aos:formpanel>
			<aos:docked dock="bottom" ui="footer">
				<aos:dockeditem xtype="tbfill" />
				<aos:dockeditem onclick="_f_table_u_submit" text="保存" icon="ok.png" />
				<aos:dockeditem onclick="#_w_table_u.hide();" text="关闭"
					icon="close.png" />
			</aos:docked>
		</aos:window>



		<aos:window id="_w_field" title="新增数据表字段">
			<aos:formpanel id="_f_field" width="500" layout="anchor"
				labelWidth="90">
				<aos:hiddenfield name="tid" fieldLabel="所属数据表流水号" />
				<aos:hiddenfield name="fieldmetch" fieldLabel="fieldmetch" value="0" />
				<aos:textfield name="tablename" fieldLabel="数据表名称"
					allowBlank="false" readOnly="true" />
				<aos:textfield name="tabledesc" fieldLabel="数据表描述"
					allowBlank="false" readOnly="true" />
				<aos:textfield name="fieldenname" fieldLabel="字段英文名称"
					allowBlank="false" />
				<aos:textfield name="fieldcnname" fieldLabel="字段中文名称"
					allowBlank="false" />

				<aos:combobox name="fieldclass" allowBlank="false" fieldLabel="字段类型"
					emptyText="请选择..." columnWidth="0.49">
					<aos:option value="int" display="int" />
					<aos:option value="varchar" display="varchar" />
					<aos:option value="datetime" display="datetime" />
				</aos:combobox>
				<aos:textfield name="fieldsize" fieldLabel="字段长度" allowBlank="false"
					maxLength="100" />
				<aos:combobox name="fieldview" fieldLabel="是否显示" emptyText="请选择..."
					columnWidth="0.49" value="1">
					<aos:option value="1" display="是" />
					<aos:option value="0" display="否" />
				</aos:combobox>
			</aos:formpanel>
			<aos:docked dock="bottom" ui="footer">
				<aos:dockeditem xtype="tbfill" />
				<aos:dockeditem onclick="_f_field_submit" text="保存" icon="ok.png" />
				<aos:dockeditem onclick="#_w_field.hide();" text="关闭"
					icon="close.png" />
			</aos:docked>
		</aos:window>

		<aos:window id="_w_field_u" title="修改数据表字段">
			<aos:formpanel id="_f_field_u" width="500" layout="anchor"
				labelWidth="90">
				<aos:hiddenfield name="id_" fieldLabel="数据表字段流水号" />
				<aos:hiddenfield name="tid" fieldLabel="数据表流水号" />
				<aos:textfield name="tablename" fieldLabel="数据表名称"
					allowBlank="false" readOnly="true" />
				<aos:textfield name="tabledesc" fieldLabel="数据表描述"
					allowBlank="false" readOnly="true" />
				<aos:textfield name="fieldenname" fieldLabel="字段英文名称"
					allowBlank="false" readOnly="true"/>
				<aos:textfield name="fieldcnname" fieldLabel="字段中文名称"
					allowBlank="false" />
				<aos:textfield name="dislen" fieldLabel="显示长度" />
				<aos:combobox name="fieldview" fieldLabel="是否显示" emptyText="请选择..."
					columnWidth="0.49">
					<aos:option value="1" display="是" />
					<aos:option value="0" display="否" />
				</aos:combobox>

				<aos:combobox name="fieldclass" allowBlank="false" readOnly="true" fieldLabel="字段类型"
					emptyText="请选择..." columnWidth="0.49">
					<aos:option value="int" display="int" />
					<aos:option value="varchar" display="varchar" />
					<aos:option value="datetime" display="datetime" />
				</aos:combobox>
				<aos:textfield name="fieldsize" fieldLabel="字段长度" allowBlank="false"
					maxLength="100" />
			</aos:formpanel>
			<aos:docked dock="bottom" ui="footer">
				<aos:dockeditem xtype="tbfill" />
				<aos:dockeditem onclick="_f_field_u_submit" text="保存" icon="ok.png" />
				<aos:dockeditem onclick="#_w_field_u.hide();" text="关闭"
					icon="close.png" />
			</aos:docked>
		</aos:window>


		<aos:window id="_w_order" title="显示顺序" height="500" autoScroll="true">
			<aos:gridpanel hidePagebar="true" id="_g_order"
				url="listOrderInfos.jhtml" width="800" onrender="_g_order_query"
				pageSize="60" drag="true">
				<aos:docked>
					<aos:dockeditem xtype="tbtext" text="数据表字段" />
				</aos:docked>
				<aos:column type="rowno" />
				<aos:column header="流水号" dataIndex="id_" hidden="true" />
				<aos:column header="字段中文名称" dataIndex="fieldcnname" width="90" />
				<aos:column header="字段长度" dataIndex="indx" width="90" hidden="true" />
			</aos:gridpanel>

			<aos:docked dock="bottom" ui="footer">
				<aos:dockeditem xtype="tbfill" />
				<aos:dockeditem onclick="_submit_order" text="保存" icon="ok.png" />
				<aos:dockeditem onclick="#_w_order.hide();" text="关闭"
					icon="close.png" />
			</aos:docked>
		</aos:window>

	</aos:viewport>
		<script type="text/javascript">
		function _g_table_query() {
			_g_table_store.load();
		}
		//查询数据表列信息
		function _g_field_query() {
			var params = {
				table_desc_ : ""
			};
			
			var record = AOS.selectone(_g_table);
			if (!AOS.empty(record)) {
			
				params.tid = record.data.id_;
			}
			_g_field_store.getProxy().extraParams = params;
			_g_field_store.load();
		}
		//查询数据表列信息
		function _g_order_query() {
			var params = {
				table_desc_ : ""
			};
			var record = AOS.selectone(_g_table);
			if (!AOS.empty(record)) {
			
				params.tid = record.data.id_;
			}
			_g_order_store.getProxy().extraParams = params;
			_g_order_store.load();
		}
	  //是否显示渲染
      function fn_fieldview_render(value, metaData, record, rowIndex, colIndex,
				store){
      if (value == '0') {
				return '否';
			}
				return '是';
      }
      </script>
</aos:onready>
</aos:html>
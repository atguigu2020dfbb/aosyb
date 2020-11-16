<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="/WEB-INF/tld/aos.tld" prefix="aos"%>
<aos:html>
	<aos:head title="数据表设置">
		<aos:include lib="ext" />
		<aos:base href="system/data" />
	</aos:head>
	<aos:onready>
		<aos:viewport layout="border">
			<aos:gridpanel id="_g_table" region="center" url="listAccounts.jhtml" width="450"
						   onitemclick="_g_field_query" onrender="_g_table_query">
				<aos:menu>
					<aos:menuitem  text="新增" onclick="_w_table_show"  icon="add.png" />
					<aos:menuitem text="修改" onclick="_w_table_u_show" icon="edit.png" />
					<aos:menuitem text="删除" onclick="_g_table_del" icon="del.png" />
					<aos:menuitem text="索引" onclick="_g_table_index" icon="key.png" />
					<aos:menuitem text="清空" icon="more/edit-clear-2.png"
									onclick="_g_table_reset" />
					<aos:menuitem text="还原" icon="refresh.png"
								  onclick="_g_table_return" />
					<aos:menuitem xtype="menuseparator" />
					<aos:menuitem text="刷新" onclick="#_g_param_store.reload();"
								  icon="refresh.png" />
				</aos:menu>
				<aos:docked>
					<aos:dockeditem xtype="tbtext" text="数据表操作" />
					<aos:dockeditem xtype="tbfill" />
					<aos:dockeditem text="新增" icon="add2.png" onclick="_w_table_show" id="_w_table_add" />
					<aos:dockeditem text="修改" icon="edit2.png" onclick="_w_table_u_show" id="_w_table_edit"/>
					<aos:dockeditem text="删除" icon="del.png" onclick="_g_table_del" />
					<aos:dockeditem text="索引" onclick="_g_table_index" icon="key.png" />
					<aos:dockeditem text="清空" icon="more/edit-clear-2.png"
									onclick="_g_table_reset" />
					<aos:dockeditem text="还原" icon="refresh.png"
								  onclick="_g_table_return" />
				</aos:docked>
				<aos:selmodel type="row" mode="multi" />
				<aos:column header="表流水号" dataIndex="id_" hidden="true" />
				<aos:column header="数据表名称" dataIndex="tablename" width="90" />
				<aos:column header="数据表描述" dataIndex="tabledesc" width="80" />
			</aos:gridpanel>
			<aos:gridpanel id="_g_field"  url="listFieldInfos.jhtml" region="east" width="600"
						   split="true" splitterBorder="0 1 0 1"  pageSize="60">
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
					<aos:dockeditem text="新增" id="kkk"  icon="add2.png" onclick="_w_field_show" />
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
			<aos:gridpanel id="_g_correlation" region="east" width="450"
						   url="listcorrelation.jhtml" split="true" splitterBorder="0 1 0 1"
						   pageSize="60" onrender="get_correlationlist">
				<aos:docked>
					<aos:dockeditem xtype="tbtext" text="数据表之间关联" />
					<aos:dockeditem xtype="tbfill" />
					<aos:dockeditem text="新增" icon="add2.png"
									onclick="_w_add_tablename_show" />
					<aos:dockeditem text="删除" icon="del2.png"
									onclick="_w_del_fieldname_show" />
				</aos:docked>
				<aos:selmodel type="checkbox" mode="multi" />
				<aos:column type="rowno" />
				<aos:column header="id值" dataIndex="id" width="90" hidden="true"/>
				<aos:column header="源表名" dataIndex="s_table" width="90" />
				<aos:column header="源字段" dataIndex="s_field" width="90" />
				<aos:column header="目的表名" dataIndex="t_table" width="90" />
				<aos:column header="目的字段" dataIndex="t_field" width="90" />
			</aos:gridpanel>
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
			<aos:window id="_w_order"  title="显示顺序" height="530" autoScroll="true" onshow="_g_order_query">
				<aos:gridpanel hidePagebar="true" id="_g_order"
							   url="listOrderInfos.jhtml" width="700"
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



			<aos:window id="_w_correlation" title="创建关联" width="720"
						autoScroll="true" layout="fit" >
				<aos:formpanel  id="_f_tablefield" layout="column" columnWidth="1" width="700">
					<aos:combobox fieldLabel="源表" name="sourcetablename"
								  fields="[ 'value', 'display', 'tablename']" id="sourcetablename"
								  editable="false" columnWidth="0.49" url="listComboBoxid.jhtml" onselect="get_source_tablefield"/>
					<aos:combobox  fieldLabel="目的表" name="objectivetablename"
								   fields="[ 'value', 'display', 'tablename']" id="objectivetablename"
								   editable="false" columnWidth="0.49" url="listComboBoxid.jhtml" onselect="get_objective_tablefield"/>
				</aos:formpanel>
				<aos:gridpanel id="_g_tablefield" split="true" hidePagebar="true"  width="700" url="getFieledlist.jhtml"
							   autoScroll="true" height="300">
					<aos:plugins>
						<aos:editing ptype="cellediting" clicksToEdit="1" onedit="fn_correlation_edit" />
					</aos:plugins>
					<aos:column  dataIndex="sourcetablenameString" hidden="true"/>
					<aos:column  dataIndex="objectivetablenameString"  hidden="true"/>
					<aos:column header="源字段" dataIndex="sourcetablefield">
						<aos:combobox name="sourcetablfieldscombobox"
									  fields="['sourcefieldenname', 'sourcefieldcnname']" id="sourcetablfieldscombobox"
									  editable="false" columnWidth="0.49" url="sourcelistComboBox.jhtml"
									  displayField="sourcefieldcnname" valueField="sourcefieldcnname" />
					</aos:column>
					<aos:column header="目的字段" dataIndex="objectivetablfield">
						<aos:combobox name="objectivetablfieldscombobox"
									  fields="['objectivefieldenname', 'objectivefieldcnname']" id="objectivetablfieldscombobox"
									  editable="false" columnWidth="0.49" url="objectivelistComboBox.jhtml"
									  displayField="objectivefieldcnname" valueField="objectivefieldcnname" />
					</aos:column>
				</aos:gridpanel>
				<aos:docked dock="bottom" ui="footer" center="true">
					<aos:dockeditem xtype="tbfill" />
					<aos:dockeditem text="添加" icon="add.png" onclick="add_tablefield" />
					<aos:dockeditem text="删除" icon="del.png" onclick="delete_tablefield" />
					<aos:dockeditem text="清除" icon="del2.png" onclick="remove_tablefield" />
					<aos:dockeditem onclick="_f_data_tablefield" text="确定" icon="ok.png" />
					<aos:dockeditem onclick="#_w_correlation.hide();" text="关闭"
									icon="close.png" />
				</aos:docked>
			</aos:window>


			<aos:window id="_w_index" title="创建表索引"
						layout="column" width="400" height="400" onshow="fn_index">
				<aos:gridpanel id="_g_index"  hidePagebar="true" height="150" width="380" url="getIndex.jhtml"
							   autoScroll="true" >
					<aos:column header="索引名称" dataIndex="indexdata_en" hidden="true"/>
					<aos:column header="索引名称" dataIndex="indexdata_cn" />
					<aos:docked dock="bottom" ui="footer" >
						<aos:dockeditem xtype="tbfill" />
						<aos:dockeditem text="新建索引" icon="add.png" onclick="add_tablefield_index" />
						<aos:dockeditem text="删除索引" icon="del2.png" onclick="remove_tablefield_index" />
						<aos:dockeditem onclick="_f_data_index" text="保存" icon="save.png" />
					</aos:docked>
				</aos:gridpanel>
				<aos:gridpanel id="_g_index_tablefield" hidePagebar="true" height="200" width="380" url="getFieldname.jhtml"
							   autoScroll="true">
					<aos:docked>
						<aos:dockeditem xtype="tbtext" text="数据表字段" />
					</aos:docked>
					<aos:plugins>
						<aos:editing ptype="cellediting" clicksToEdit="1" onedit="fn_edit" />
					</aos:plugins>
					<aos:column dataIndex="sourcetablefield_value" header="字段英文名称" hidden="true" />
					<aos:column header="字段名称" dataIndex="sourcetablefield">
						<aos:combobox  name="targetTemplate"
									   fields="[ 'fieldenname', 'fieldcnname']" id="targetTemplate"
									   editable="false" columnWidth="0.5" url="listComboBox.jhtml"
									   displayField="fieldcnname" valueField="fieldcnname" />
					</aos:column>
				</aos:gridpanel>
				<aos:docked dock="bottom" ui="footer" center="true">
					<aos:dockeditem xtype="tbfill" />
					<aos:dockeditem onclick="#_w_index.hide();" text="关闭"
									icon="close.png" />
				</aos:docked>
			</aos:window>
			<aos:window id="_w_table" title="新增数据表" height="500" autoScroll="true"  split="true">
				<aos:formpanel id="_f_table" width="800" layout="column"
							   labelWidth="75">
					<aos:textfield fieldLabel="数据表名称" name="tablename"
								   allowBlank="false" vtype="alphanum" emptyText="只能输入字母和数字"
								   columnWidth="0.5" />
					<aos:textfield fieldLabel="数据表描述" name="tabledesc"
								   allowBlank="false" columnWidth="0.49" />
					<aos:combobox fieldLabel="数据表模板" name="tableTemplate"
								  fields="[ 'value', 'display', 'tablename']" id="tableTemplate"
								  editable="false" columnWidth="0.5" url="listComboBoxid.jhtml"
								  onselect="loadDemo" />
					<aos:checkboxgroup fieldLabel="属性" columns="[80,80]">
						<aos:checkbox name="path" boxLabel="附件" checked="true" />
						<aos:checkbox name="backup" boxLabel="备份" checked="true" />
					</aos:checkboxgroup>
				</aos:formpanel>
				<aos:gridpanel hidePagebar="true" id="_g_field_demo" autoScroll="true"
							   url="listFieldInfos.jhtml" width="800" pageSize="60">
					<aos:docked>
						<aos:dockeditem xtype="tbtext" text="数据表字段" />
						<aos:dockeditem text="新增" icon="add2.png" onclick="_g_field_demo_show"/>
						<aos:dockeditem text="删除" icon="del.png" onclick="_g_field_demo_del" />
					</aos:docked>
					<aos:plugins>
						<aos:editing ptype="cellediting" clicksToEdit="1" onedit="fn_edit_add" />
					</aos:plugins>
					<aos:column type="rowno" />
					<aos:column header="流水号" dataIndex="id_" hidden="true" />
					<aos:column header="字段英文名" dataIndex="fieldenname" width="90"  >
					<aos:textfield allowBlank="false" />
					</aos:column>
					<aos:column header="字段中文名称" dataIndex="fieldcnname" width="90"  >
						<aos:textfield allowBlank="false" />
					</aos:column>
					<aos:column header="字段类型" dataIndex="fieldclass" width="90" />
					<aos:column header="字段长度" dataIndex="fieldsize" width="90" />
					<aos:column header="显示长度" dataIndex="fieldview" hidden="true" />
					<aos:column header="是否显示" dataIndex="fieldmetch" hidden="true" />
				</aos:gridpanel>
				<aos:docked dock="bottom" ui="footer">
					<aos:dockeditem xtype="tbfill" />
					<aos:dockeditem style="color:'red'" iconVecAlign="left" xtype="tbtext" text="注:录入人、录入日期、目录树、附件、借阅、鉴定为系统字段，自动创建，用户不必添加" />
					<aos:dockeditem onclick="_f_table_submit" text="保存" icon="ok.png" />
					<aos:dockeditem onclick="#_w_table.hide();" text="关闭"
									icon="close.png" />
				</aos:docked>
			</aos:window>
			<aos:window id="_w_field_demo" title="新增数据表字段">
				<aos:formpanel id="_f_field_demo" width="500" layout="anchor"
							   labelWidth="90">
					<aos:hiddenfield name="tid" fieldLabel="所属数据表流水号" />
					<aos:hiddenfield name="fieldmetch" fieldLabel="fieldmetch" value="0" />
					<aos:textfield name="fieldenname" fieldLabel="字段英文名称" vtype="alphanum" emptyText="只能输入字母和数字"
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
					<aos:numberfield name="dislen" fieldLabel="显示长度" allowBlank="false" editable="false"  step="50" maxValue="1000" minValue="50" emptyText="请选择..."/>
				</aos:formpanel>
				<aos:docked dock="bottom" ui="footer">
					<aos:dockeditem xtype="tbfill" />
					<aos:dockeditem onclick="_f_field_demo_submit" text="保存" icon="ok.png" />
					<aos:dockeditem onclick="#_w_field_demo.hide();" text="关闭"
									icon="close.png" />
				</aos:docked>
			</aos:window>

		</aos:viewport>
		<script type="text/javascript">

			//查询数据表列信息
			function _g_field_query(){
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
			function _g_table_query(){
				_g_table_store.load();
			}
			//弹出新增数据表窗口
			function _w_table_show(){
				AOS.reset(_f_table);
				_g_field_demo_store.removeAll();
				_w_table.show();
			}
			//新增数据表保存
			function _f_table_submit(){
				//1.判断grid英文名列是否有重复数据
				var arr = [];
				var count=_g_field_demo_store.getCount();
				if(count>0){
					for(var i=0;i<count;i++){
						arr[i]=_g_field_demo_store.getAt(i).get("fieldenname");
					}
				}
				//1.判断英文字段列是否有重复值
				_same_fieldenname(arr);
				//2.判断是否包含系统字段
				_system_contains(arr);
				var tem=Ext.getCmp('tableTemplate');
				if(tem.value!=null){
					var datatem=tem.displayTplData[0].tablename;
				}
				var fieldennames="";
				var fieldcnnames="";
				var fieldclasss="";
				var fieldsizes="";
				var fieldviews="";
				var fieldmetchs="";
				var count=_g_field_demo_store.getCount();
				if(count>0){
					for(var i=0;i<count;i++){
						if(i==0){
							fieldennames=_g_field_demo_store.getAt(i).get("fieldenname");
							fieldcnnames=_g_field_demo_store.getAt(i).get("fieldcnname");
							fieldclasss=_g_field_demo_store.getAt(i).get("fieldclass");
							fieldsizes=_g_field_demo_store.getAt(i).get("fieldsize");
							fieldviews=_g_field_demo_store.getAt(i).get("fieldview");
							fieldmetchs=_g_field_demo_store.getAt(i).get("fieldmetch");
						}else{
							fieldennames+=";"+_g_field_demo_store.getAt(i).get("fieldenname");
							fieldcnnames+=";"+_g_field_demo_store.getAt(i).get("fieldcnname");
							fieldclasss+=";"+_g_field_demo_store.getAt(i).get("fieldclass");
							fieldsizes+=";"+_g_field_demo_store.getAt(i).get("fieldsize");
							fieldviews+=";"+_g_field_demo_store.getAt(i).get("fieldview");
							fieldmetchs+=";"+_g_field_demo_store.getAt(i).get("fieldmetch");
						}
					}
				}
				AOS.ajax({
					forms: _f_table,
					params:{tablenametemplate:datatem,
						fieldennames:fieldennames,
						fieldcnnames:fieldcnnames,
						fieldclasss:fieldclasss,
						fieldsizes:fieldsizes,
						fieldviews:fieldviews,
						fieldmetchs:fieldmetchs
					},
					url: 'saveTable.jhtml',
					ok: function (data) {
						if(data.appcode === -1){
							AOS.err(data.appmsg);
						}else{
							_w_table.hide();
							_g_table_store.reload();
							AOS.tip(data.appmsg);
						}
					}
				});
			}

			//弹出修改数据表窗口
			function _w_table_u_show() {
				var record = AOS.selectone(_g_table);
				if (record) {
					_w_table_u.show();
					_f_table_u.loadRecord(record);
					//_tp_u_catalog_id_.setRawValue(record.data.catalog_name_);
				}
			}
			//修改数据表索引保存
			function _f_table_u_submit() {
				AOS.ajax({
					forms: _f_table_u,
					url: 'updateTable.jhtml',
					ok: function (data) {
						if (data.appcode === 1) {
							_w_table_u.hide();
							_g_table_store.reload();
							AOS.tip(data.appmsg);
						} else {
							AOS.err(data.appmsg);
						}
					}
				});
			}
			//数据表重置
			function _g_table_reset(){
				var selection = AOS.selection(_g_table, 'id_');
				var records = AOS.selectone(_g_table);
				var tablename = records.data.tablename;
				if (AOS.empty(selection)) {
					AOS.tip('重置前请先选中数据。');
					return;
				}
				var msg=AOS.merge('数据表内容将被清空且无法恢复！');
				AOS.confirm(msg, function (btn) {
					if (btn === 'cancel') {
						AOS.tip('重置操作被取消。');
						return;
					}
					AOS.ajax({
						url: 'resetTable.jhtml',
						params:{tablename:tablename},
						ok: function (data) {
							AOS.tip(data.appmsg);
							_g_table_store.reload();
						}
					});
				});
			}


			//删除数据表
			function _g_table_del() {
				var selection = AOS.selection(_g_table, 'id_');
				var records = AOS.selectone(_g_table);
				var tablename = records.data.tablename;
				if (AOS.empty(selection)) {
					AOS.tip('删除前请先选中数据。');
					return;
				}
				var rows = AOS.rows(_g_table);
				var msg = AOS.merge('确认要删除选中的[{0}]条数据表吗？', rows);
				AOS.confirm(msg, function (btn) {
					if (btn === 'cancel') {
						AOS.tip('删除操作被取消。');
						return;
					}
					AOS.ajax({
						url: 'deleteTable.jhtml',
						params: {
							aos_rows_: selection,
							tablename:tablename
						},
						ok: function (data) {
							AOS.tip(data.appmsg);
							_g_table_store.reload();
						}
					});
				});
			}
			//弹出新增数据表列对照窗口
			function _w_field_show() {
				AOS.reset(_f_field);
				var record = AOS.selectone(_g_table, true);
				if (AOS.empty(record)) {
					AOS.tip('请先在数据表上选中1个数据表。');
					return;
				}
				_f_field.down('[name=tablename]').setValue(record.data.tablename);
				_f_field.down('[name=tabledesc]').setValue(record.data.tabledesc);
				_f_field.down('[name=tid]').setValue(record.data.id_);
				_w_field.show();
			}
			//新增数据表字段保存
			function _f_field_submit() {
				AOS.ajax({
					forms: _f_field,
					url: 'saveField.jhtml',
					ok: function (data) {
						_w_field.hide();
						_g_field_store.reload();
						AOS.tip(data.appmsg);
					}
				});

			}

			//弹出修改数据表字段窗口
			function _w_field_u_show(){
				var record = AOS.selectone(_g_field);
				if (record) {
					_w_field_u.show();
					_f_field_u.loadRecord(record);
					tableRecord = AOS.selectone(_g_table);
					_f_field_u.down('[name=tid]').setValue(tableRecord.data.id_);
					_f_field_u.down('[name=tablename]').setValue(tableRecord.data.tablename);
					_f_field_u.down('[name=tabledesc]').setValue(tableRecord.data.tabledesc);
					//_f_field_u.down('[name=fieldview]').setValue(record.data.fieldview.toString());
					_f_field_u.down('[name=fieldview]').setValue(record.data.fieldview.toString());
					//_tp_u_catalog_id_.setRawValue(record.data.catalog_name_);
				}

			}
			//修改数据表字段索引保存
			function _f_field_u_submit(){
				AOS.ajax({
					forms: _f_field_u,
					url: 'updateField.jhtml',
					ok: function (data) {
						if (data.appcode === 1) {
							_w_field_u.hide();
							_g_field_store.reload();
							AOS.tip(data.appmsg);
						} else {
							AOS.err(data.appmsg);
						}
					}
				});
			}
			//删除数据表字段
			function _g_field_del(){
				var selection = AOS.selection(_g_field, 'id_');
				//var records = AOS.selectone(_g_table);
				var tablename = AOS.selectone(_g_table).data.tablename;
				var fieldenname = AOS.selectone(_g_field).data.fieldenname;
				if (AOS.empty(selection)) {
					AOS.tip('删除前请先选中数据。');
					return;
				}
				var rows = AOS.rows(_g_field);
				var msg = AOS.merge('确认要删除选中的[{0}]条数据表字段吗？', rows);
				AOS.confirm(msg, function (btn) {
					if (btn === 'cancel') {
						AOS.tip('删除操作被取消。');
						return;
					}
					AOS.ajax({
						url: 'deleteField.jhtml',
						params: {
							aos_rows_: selection,
							tablename:tablename,
							fieldenname:fieldenname
						},
						ok: function (data) {
							AOS.tip(data.appmsg);
							_g_field_store.reload();
						}
					});
				});
			}
			function _g_field_order_show(){
				_w_order.show();

			}


			function loadDemo(){

				var id=Ext.getCmp('tableTemplate').value;

				var params = {
					table_desc_ : ""
				};
				if (!AOS.empty(id)) {
					params.tid = id;
				}
				_g_field_demo_store.getProxy().extraParams = params;
				_g_field_demo_store.load();

			}

			//是否显示渲染
			function fn_fieldview_render(value, metaData, record, rowIndex, colIndex,
										 store){
				if (value == '0') {
					return '否';
				}
				return '是';
			}

			//查询数据表列信息
			function _g_order_query() {
				//AOS.reset(_g_table);
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
			function _submit_order(){
				var tablename = AOS.selectone(_g_table).data.tablename;
				var store=Ext.getCmp("_g_order").getStore();
				var count=store.getCount();
				var zdid_="";
				for(var i=0;i<count;i++){
					zdid_+=store.getAt(i).data.id_+",";
				}
				//去掉最后一个逗号
				zdid_=zdid_.substring(0,zdid_.length-1);
				AOS.ajax({
					params: {'zdid_': zdid_,'tablename':tablename},
					url: 'updatezdOrder.jhtml',
					ok: function (data) {
						if (data.appcode ===-1) {
							AOS.tip(data.appmsg);
						}else{
							//_w_order.hide();
							_w_order.hide();
							location.reload();
							//_g_data_query;
						}
					}
				});
			}


			//获得关联列表
			function get_correlationlist(){
				_g_correlation_store.load();
			}
			//关联窗口展示
			function _w_add_tablename_show(){
				_w_correlation.show();
			}

			//当表的下拉框选中的时候进行清除操作
			function remove_tablefield(){
				_g_tablefield_store.removeAll();

			}
			//删除
			function delete_tablefield(){
				var row=_g_tablefield.getSelectionModel().getSelection();
				//右侧删除，左侧添加
				_g_tablefield_store.remove(row);
			}
			//进行提交操作
			function _f_data_tablefield(){
				var sourcetablename_id=Ext.getCmp("sourcetablename").value;
				var objectivetablename_id=Ext.getCmp("objectivetablename").value;
				if(sourcetablename_id==""||sourcetablename_id==null||objectivetablename_id==""||objectivetablename_id==null){
					AOS.tip("请选择源表和目的表");
					return;
				}
				if(sourcetablename_id==objectivetablename_id){
					AOS.tip("源表和目的表不能重复");
					return;
				}
				var store=Ext.getCmp("_g_tablefield").getStore();
				var count=store.getCount();
				if(count==0){
					AOS.tip("请添加关联字段!");
					return;
				}
				var sourcetablenameString="";
				var objectivetablenameString="";
				//从grid中获取之前的行列数据传递给后台
				var count=_g_tablefield_store.getCount();
				if(count>0){
					for(var i=0;i<count;i++){
						if(i==0){
							sourcetablenameString=_g_tablefield_store.getAt(i).get("sourcetablenameString");
							objectivetablenameString=_g_tablefield_store.getAt(i).get("objectivetablenameString");
						}else{
							sourcetablenameString+=","+_g_tablefield_store.getAt(i).get("sourcetablenameString");
							objectivetablenameString+=","+_g_tablefield_store.getAt(i).get("objectivetablenameString");
						}
					}
				}
				//将所需要的值传递给后台
				AOS.ajax({
					params: {'sourcetablename_id':sourcetablename_id,
						'objectivetablename_id':objectivetablename_id,
						'sourcefieldall':sourcetablenameString,
						'objectfieldall':objectivetablenameString
					},
					url: 'addrelative.jhtml',
					ok: function (data) {
						if(data.appcode === -1){
							AOS.err(data.appmsg);
						}else{
							_w_correlation.hide();
							AOS.tip(data.appmsg);
							_g_correlation_store.reload();

						}
					}
				});

			}

			function _w_del_fieldname_show(){
				//删除关联关系
				//1.得到当前选中的关联条目
				var selection = AOS.selection(_g_correlation, 'id');
				if (AOS.empty(selection)) {
					AOS.tip('删除前请先选中数据。');
					return;
				}
				var rows = AOS.rows(_g_correlation);
				var msg = AOS.merge('确认要删除选中的[{0}]条数据表字段吗？', rows);
				AOS.confirm(msg, function (btn) {
					if (btn === 'cancel') {
						AOS.tip('删除操作被取消。');
						return;
					}
					AOS.ajax({
						url: 'deletecorrelation.jhtml',
						params: {
							ids_: selection
						},
						ok: function (data) {
							AOS.tip(data.appmsg);
							_g_correlation_store.reload();
						}
					});
				});
			}
			//索引
			function _g_table_index(){
				var selection = AOS.selection(_g_table, 'id_');
				var records = AOS.selectone(_g_table);
				var tablename = records.data.tablename;
				if (AOS.empty(selection)) {
					AOS.tip('请先选中数据。');
					return;
				}
				_w_index.show();
			}
			function add_tablefield_index(){

				_g_index_tablefield_store.removeAll();
				//移除之前的
				var records = AOS.selectone(_g_table);
				var tablename = records.data.tablename;
				var params = {
					tablename:tablename
				};
				//这个Store的命名规则为：表格ID+"_store"。
				_g_index_tablefield_store.getProxy().extraParams = params;
				_g_index_tablefield_store.reload();
				//
			}
			function fn_index(){
				_g_index_tablefield_store.removeAll();
				var records = AOS.selectone(_g_table);
				var tablename = records.data.tablename;
				var params = {
					tablename:tablename
				};
				//这个Store的命名规则为：表格ID+"_store"。
				_g_index_store.getProxy().extraParams = params;
				_g_index_store.reload();
				//这个Store的命名规则为：表格ID+"_store"。
				targetTemplate_store.getProxy().extraParams = params;
				targetTemplate_store.reload();
			}
			//删除索引
			function remove_tablefield_index(){
				var records = AOS.selectone(_g_index);
				var indexdata_en=records.data.indexdata_en;
				var records = AOS.selectone(_g_table);
				var tablename = records.data.tablename;
				AOS.ajax({
					url: 'deleteIndex.jhtml',
					params: {
						indexdata_en: indexdata_en,
						tablename:tablename
					},
					ok: function (data) {
						AOS.tip(data.appmsg);
						var params = {
							tablename:tablename
						};
						//这个Store的命名规则为：表格ID+"_store"。
						_g_index_store.getProxy().extraParams = params;
						_g_index_store.reload();
					}
				});
			}
			//单元格编辑监听事件
			function fn_edit(editor, e){
				var selectValue = Ext.getCmp("targetTemplate").displayTplData[0].fieldenname;
				_g_index_tablefield_store.getAt(e.rowIdx).set('fieldenname',selectValue);
				_g_index_tablefield_store.getAt(e.rowIdx).set('sourcetablefield_value',selectValue);
				e.record.commit();
			}
			function fn_correlation_edit(editor, e){
				if(e.colIdx==0){
					var selectValue = Ext.getCmp("sourcetablfieldscombobox").displayTplData[0].sourcefieldenname;
					_g_tablefield_store.getAt(e.rowIdx).set('sourcefieldenname',selectValue);
					_g_tablefield_store.getAt(e.rowIdx).set('sourcetablenameString',selectValue);
					e.record.commit();
				}else if(e.colIdx==1){
					var selectValue = Ext.getCmp("objectivetablfieldscombobox").displayTplData[0].objectivefieldenname;
					_g_tablefield_store.getAt(e.rowIdx).set('objectivefieldenname',selectValue);
					_g_tablefield_store.getAt(e.rowIdx).set('objectivetablenameString',selectValue);
					e.record.commit();
				}
			}
			//保存当前创建的索引
			function _f_data_index(){
				//得到选中的值
				var records = AOS.selectone(_g_table);
				var tablename = records.data.tablename;
				//得到当前选中的数据
				//var tablefieldlist=Ext.getCmp("sourcetablefield_value").getValue();
				var tablefieldlist = _g_index_tablefield_store.getAt(0).get("sourcetablefield_value");
				var tablefieldlist_cn = _g_index_tablefield_store.getAt(0).get("sourcetablefield");
				//var tablefieldlist=Ext.getCmp("targetTemplate").getRawValue();
				AOS.ajax({
					url: 'saveIndex.jhtml',
					params: {
						tablefieldlist: tablefieldlist,
						tablefieldlist_cn:tablefieldlist_cn,
						tablename:tablename
					},
					ok: function (data) {
						AOS.tip(data.appmsg);
						var params = {
							tablename:tablename
						};
						//这个Store的命名规则为：表格ID+"_store"。
						_g_index_store.getProxy().extraParams = params;
						_g_index_store.reload();
					}
				});
			}
			//关联中的数据表选中得到字段下拉列表
			function get_source_tablefield(){
				_g_tablefield_store.removeAll();
				var sourcetablename_id=Ext.getCmp("sourcetablename").value;
				//走后台
				var params = {
					tid:sourcetablename_id
				};
				//这个Store的命名规则为：表格ID+"_store"。
				sourcetablfieldscombobox_store.getProxy().extraParams = params;
				sourcetablfieldscombobox_store.reload();
			}
			//关联中的数据表选中得到字段下拉列表
			function get_objective_tablefield(){
				_g_tablefield_store.removeAll();
				var objectivetablename_id=Ext.getCmp("objectivetablename").value;
				//走后台
				var params = {
					tid:objectivetablename_id
				};
				//这个Store的命名规则为：表格ID+"_store"。
				objectivetablfieldscombobox_store.getProxy().extraParams = params;
				objectivetablfieldscombobox_store.reload();
			}
			//新建
			function add_tablefield(){
				var sourcetablename_id=Ext.getCmp("sourcetablename").value;
				var objectivetablename_id=Ext.getCmp("objectivetablename").value;
				if(sourcetablename_id==""||sourcetablename_id==null||objectivetablename_id==""||objectivetablename_id==null){
					AOS.tip("请选择源表和目的表");
					return;
				}
				if(sourcetablename_id==objectivetablename_id){
					AOS.tip("源表和目的表不能重复");
					return;
				}
				var sourcetablenameString="";
				var sourcetablefield="";
				var objectivetablenameString="";
				var objectivetablfield="";
				//从grid中获取之前的行列数据传递给后台
				var count=_g_tablefield_store.getCount();
				if(count>0){
					for(var i=0;i<count;i++){
						if(i==0){
							sourcetablenameString=_g_tablefield_store.getAt(i).get("sourcetablenameString");
							sourcetablefield=_g_tablefield_store.getAt(i).get("sourcetablefield");
							objectivetablenameString=_g_tablefield_store.getAt(i).get("objectivetablenameString");
							objectivetablfield=_g_tablefield_store.getAt(i).get("objectivetablfield");
						}else{
							sourcetablenameString+=","+_g_tablefield_store.getAt(i).get("sourcetablenameString");
							sourcetablefield+=","+_g_tablefield_store.getAt(i).get("sourcetablefield");
							objectivetablenameString+=","+_g_tablefield_store.getAt(i).get("objectivetablenameString");
							objectivetablfield+=","+_g_tablefield_store.getAt(i).get("objectivetablfield");
						}
					}
				}
				//走后台查询到字段下拉框
				var params = {
					sourcetablename_id:sourcetablename_id,
					objectivetablename_id:objectivetablename_id,
					sourcetablenameString:sourcetablenameString,
					sourcetablefield:sourcetablefield,
					objectivetablenameString:objectivetablenameString,
					objectivetablfield:objectivetablfield
				};
				//这个Store的命名规则为：表格ID+"_store"。
				_g_tablefield_store.getProxy().extraParams = params;
				_g_tablefield_store.reload();
			}
			function fn_edit_add(editor, e) {
				e.record.commit();
			}

			/**
			 * 判断是否含有相同的英文字段列名
			 * @param arr
			 * @private
			 */
			function _same_fieldenname(arr){
				if((new Set(arr)).size != arr.length){
					AOS.err("字段英文名有重复值","错误");
					return;
				}
			}
			/**
			 * 判断是否包含系统字段
			 * @param arr
			 * @private
			 */
			function _system_contains(arr){
				if(arr.indexOf("_lrrq")!=-1||arr.indexOf("_lrr")!=-1||arr.indexOf("_jd")!=-1||arr.indexOf("_jy")!=-1||arr.indexOf("_path")!=-1||arr.indexOf("_classtree")!=-1){
					AOS.err("包含系统自动创建字段,请删除","错误");
					return;
				}

				/*for(var k=0;k<arr.length;k++){
					alert(jd.test(arr[k]));
					var lrrq=new RegExp("_lrrq");
					var lrr=new RegExp("_lrr");
					var jy=new RegExp("_jy");
					var jd=new RegExp("_jd");
					var path=new RegExp("_path");
					var classtree=new RegExp("_classtree");
					if(lrrq.test(arr[k])||lrr.test(arr[k])||jy.test(arr[k])||jd.test(arr[k])||path.test(arr[k])||classtree.test(arr[k])){
						AOS.err("包含系统自动创建字段,请删除","错误");
						return;
					}
				}*/
			}
			//删除当前选中行
			function _g_field_demo_del(){
				var row=_g_field_demo.getSelectionModel().getSelection();
				//右侧删除，左侧添加
				_g_field_demo_store.remove(row);
			}
			//添加一行
			function _g_field_demo_show(){
				AOS.reset(_f_field_demo);
				_w_field_demo.show();
			}
			//字段临时保存操作
			function _f_field_demo_submit(){
				AOS.ajax({
					forms: _f_field_demo,
					url: 'saveTemporaryFieldName.jhtml',
					ok: function (data) {
						var fieldenname=  Ext.getCmp('_f_field_demo').down("[name='fieldenname']").getValue();
						var fieldcnname=  Ext.getCmp('_f_field_demo').down("[name='fieldcnname']").getValue();
						var fieldclass=  Ext.getCmp('_f_field_demo').down("[name='fieldclass']").getValue();
						var dislen=  Ext.getCmp('_f_field_demo').down("[name='dislen']").getValue();
						var fieldsize=  Ext.getCmp('_f_field_demo').down("[name='fieldsize']").getValue();
						var fieldview=  Ext.getCmp('_f_field_demo').down("[name='fieldview']").getValue();
						var json={'fieldenname':fieldenname,
							'fieldclass':fieldclass,
							'fieldcnname':fieldcnname,
							'dislen':dislen,
							'fieldsize':fieldsize,
							'fieldview':fieldview
						};
						_g_field_demo_store.insert(0,json);
						_w_field_demo.hide();
					}
				});
			}
			//还原数据表操作
			function _g_table_return(){
				var selection = AOS.selection(_g_table, 'id_');
				var records = AOS.selectone(_g_table);
				var tablename = records.data.tablename;
				if (AOS.empty(selection)) {
					AOS.tip('还原前请先选中数据。');
					return;
				}
				AOS.ajax({
					url: 'returnTable.jhtml',
					params:{tablename:tablename},
					ok: function (data) {
						AOS.tip(data.appmsg);
						_g_table_store.reload();
					}
				});
			}
		</script>
	</aos:onready>
</aos:html>






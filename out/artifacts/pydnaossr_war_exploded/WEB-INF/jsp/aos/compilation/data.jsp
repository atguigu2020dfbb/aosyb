<%@ page contentType="text/html; charset=utf-8" isELIgnored="false"%>
<%@ taglib uri="/WEB-INF/tld/aos.tld" prefix="aos"%>
<%@ taglib uri="/WEB-INF/tld/cll.tld" prefix="c"%>
<%
	String path = request.getContextPath();
	Object fieldDtos = request.getAttribute("fieldDtos");
	Object listtablename = request.getAttribute("listtablename");
%>
<aos:html>
	<aos:head title="撰稿人档案展示页面">
		<aos:include lib="ext" />
		<aos:base href="compilation/examine" />
	</aos:head>
	<aos:body>
	</aos:body>
	<aos:onready>
		<aos:viewport layout="fit">
			<aos:gridpanel id="_g_data" url="listAccounts_topic.jhtml"  region="center" split="true" onrender="_load_data_topic"
						   pageSize="20"  enableLocking="true">
				<aos:docked>
					<aos:dockeditem xtype="tbtext" text="编研数据库数据" />
					<aos:hiddenfield name="aos_module_id_" id="aos_module_id_" value="${aos_module_id_}"/>
					<aos:dockeditem text="增加" icon="add.png"  onclick="_g_add_data" id="_f_add_data"/>
					<aos:dockeditem text="删除" icon="del.png" onclick="_w_del_data"  id="_f_del_data"/>
				</aos:docked>
				<aos:selmodel type="row" mode="multi" />
				<aos:column dataIndex="id_" header="流水号" hidden="true" />
				<aos:column dataIndex="_path" header="电子文件"
							rendererFn="fn_path_render" />
				<aos:column header="全宗单位" dataIndex="qzdw" />
				<aos:column header="档号" dataIndex="dh" celltip="true" />
				<aos:column header="题名" dataIndex="tm"  width="80" />
				<aos:column header="年度" dataIndex="nd" />
				<aos:column header="保管期限" dataIndex="bgqx" />
				<aos:column header="形成时间" dataIndex="xcsj"  />
				<aos:column header="盒号" dataIndex="hh" />
				<aos:column header="备注" dataIndex="bz" flex="1" celltip="true" />
				<aos:column header="数据表" dataIndex="tablename"/>
				<aos:column header="" flex="1" />
			</aos:gridpanel>
			<aos:window id="_w_select_edit_archive" title="编研数据选择"  width="800" height="500">
				<aos:gridpanel id="_g_select_edit_archive" url="listdata_edit.jhtml" region="north" width="770" height="460"
							   autoScroll="true" pageSize="20" enableLocking="true" hidePagebar="false">
					<aos:docked forceBoder="0 0 1 0">
						<aos:dockeditem xtype="tbtext" text="数据列表" />

						<aos:combobox fieldLabel="编研数据库" name="sjkmc" allowBlank="false"
									  fields="['tablename','tabledesc']" id="sjkmc" displayField="tabledesc" valueField="tablename"
									  editable="false" columnWidth="0.7" url="listTablename_by.jhtml" onselect="by_select_sjkmc" width="300"/>
						<aos:dockeditem text="查询" icon="query.png"
										onclick="_w_select_edit_query_show" />
						<aos:dockeditem  text="确定" icon="ok.png"
										 onclick="ok_save_archive_edit" />
					</aos:docked>
					<aos:selmodel type="checkbox" mode="multi" />
					<aos:column type="rowno" />
					<aos:column dataIndex="id_" header="流水号" hidden="true" />
					<aos:column header="全宗单位" dataIndex="qzdw" />
					<aos:column header="档号" dataIndex="dh" celltip="true" />
					<aos:column header="题名" dataIndex="tm"  width="80" />
					<aos:column header="年度" dataIndex="nd" />
					<aos:column header="保管期限" dataIndex="bgqx" />
					<aos:column header="形成时间" dataIndex="xcsj"  />
					<aos:column header="盒号" dataIndex="hh" />
					<aos:column header="备注" dataIndex="bz" flex="1" celltip="true" />
					<aos:column header="" flex="1" />
				</aos:gridpanel>

			</aos:window>
			<aos:window id="_w_query_edit_select_q" title="查询" width="720" autoScroll="true"
						layout="fit" onshow="combobox_tableFileldlist">
				<aos:tabpanel id="_tabpanel_edit_select" region="center" activeTab="0"
							  bodyBorder="0 0 0 0" tabBarHeight="30">
					<aos:tab title="列表式搜索" id="_tab_select_edit_org">
						<aos:formpanel id="_f_select_edit_query" layout="column" columnWidth="1">
							<aos:hiddenfield name="cascode_id_" value="${cascode_id_ }" />
							<aos:hiddenfield name="tablename" value="${tablename }" />
							<aos:hiddenfield name="columnnum"  value="7" />
							<aos:combobox name="and1" value="on"
										  labelWidth="10" columnWidth="0.2">
								<aos:option value="on" display="并且" />
								<aos:option value="up" display="或者" />
							</aos:combobox>
							<aos:combobox name="filedname1" id="filedname1" url="getComboboxList.jhtml"  fields="['fieldenname','fieldcnname']"
										  displayField="fieldcnname"
										  valueField="fieldenname"  labelWidth="20" columnWidth="0.2"  >
							</aos:combobox>

							<aos:combobox name="condition1" value="like"
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
							<aos:textfield name="content1"
										   allowBlank="true" columnWidth="0.39" />

							<aos:combobox name="and2" value="on"
										  labelWidth="10" columnWidth="0.2">
								<aos:option value="on" display="并且" />
								<aos:option value="up" display="或者" />
							</aos:combobox>
							<aos:combobox name="filedname2" id="filedname2" url="getComboboxList.jhtml"  fields="['fieldenname','fieldcnname']"
										  displayField="fieldcnname"
										  valueField="fieldenname"  labelWidth="20" columnWidth="0.2"  >
							</aos:combobox>

							<aos:combobox name="condition2" value="like"
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
							<aos:textfield name="content2"
										   allowBlank="true" columnWidth="0.39" />

							<aos:combobox name="and3" value="on"
										  labelWidth="10" columnWidth="0.2">
								<aos:option value="on" display="并且" />
								<aos:option value="up" display="或者" />
							</aos:combobox>
							<aos:combobox name="filedname3" id="filedname3" url="getComboboxList.jhtml"  fields="['fieldenname','fieldcnname']"
										  displayField="fieldcnname"
										  valueField="fieldenname"  labelWidth="20" columnWidth="0.2"  >
							</aos:combobox>

							<aos:combobox name="condition3" value="like"
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
							<aos:textfield name="content3"
										   allowBlank="true" columnWidth="0.39" />
							<aos:combobox name="and4" value="on"
										  labelWidth="10" columnWidth="0.2">
								<aos:option value="on" display="并且" />
								<aos:option value="up" display="或者" />
							</aos:combobox>
							<aos:combobox name="filedname4" id="filedname4" url="getComboboxList.jhtml"  fields="['fieldenname','fieldcnname']"
										  displayField="fieldcnname"
										  valueField="fieldenname"  labelWidth="20" columnWidth="0.2"  >
							</aos:combobox>

							<aos:combobox name="condition4" value="like"
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
							<aos:textfield name="content4"
										   allowBlank="true" columnWidth="0.39" />

							<aos:combobox name="and5" value="on"
										  labelWidth="10" columnWidth="0.2">
								<aos:option value="on" display="并且" />
								<aos:option value="up" display="或者" />
							</aos:combobox>
							<aos:combobox name="filedname5" id="filedname5" url="getComboboxList.jhtml"  fields="['fieldenname','fieldcnname']"
										  displayField="fieldcnname"
										  valueField="fieldenname"  labelWidth="20" columnWidth="0.2"  >
							</aos:combobox>

							<aos:combobox name="condition5" value="like"
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
							<aos:textfield name="content5"
										   allowBlank="true" columnWidth="0.39" />

							<aos:docked dock="bottom" ui="footer">
								<aos:dockeditem xtype="tbfill" />
								<aos:dockeditem onclick="_f_select_edit_data_query" text="确定" icon="ok.png" />
								<aos:dockeditem onclick="#_w_query_edit_select_q.hide();" text="关闭"
												icon="close.png" />
							</aos:docked>
						</aos:formpanel>
					</aos:tab>
				</aos:tabpanel>
			</aos:window>

		</aos:viewport>
		<script>
			function _g_add_data(){
				//弹出选择档案窗口

				_w_select_edit_archive.show();
               //AOS.reset(sjkmc)
                Ext.getCmp('sjkmc').setValue('');
                Ext.getCmp("_g_select_edit_archive").getStore().removeAll();
			}
			function _w_select_edit_archive_onshow(){
				var params = {
					id_ : Ext.getCmp("aos_module_id_").getValue()
				};
				//这个Store的命名规则为：表格ID+"_store"。
				_g_select_edit_archive_store.getProxy().extraParams = params;
				_g_select_edit_archive_store.load();
			}
			//获取字段数据列表
			function combobox_tableFileldlist(){
				var params = {
					tablename : Ext.getCmp("sjkmc").value
				};
				//这个Store的命名规则为：表格ID+"_store"。ok_save
				filedname1_store.getProxy().extraParams = params;
				filedname1_store.load();
				filedname2_store.getProxy().extraParams = params;
				filedname2_store.load();
				filedname3_store.getProxy().extraParams = params;
				filedname3_store.load();
				filedname4_store.getProxy().extraParams = params;
				filedname4_store.load();
				filedname5_store.getProxy().extraParams = params;
				filedname5_store.load();
			}
			//查询窗口展开
			function _w_select_edit_query_show() {
				//判断是不是
				_w_query_edit_select_q.show();
			}
			function _f_select_edit_data_query(){
				var tablename=Ext.getCmp("sjkmc").value;
				if(tablename==""||tablename==null){
					AOS.tip("请选择数据库");
					return;
				}
				var params = AOS.getValue('_f_select_edit_query');
				var form = Ext.getCmp('_f_select_edit_query');
				for(var i=1;i<=5;i++){
					var str = form.down("[name='filedname"+i+"']");
					var filedname =str.getValue();
					if(filedname==null){
						params["filedname"+i]=str.regexText;
					}
				}
				params["id_"]=Ext.getCmp("aos_module_id_").getValue();
				params["tablename"]=tablename;
				_g_select_edit_archive_store.getProxy().extraParams = params;
				_g_select_edit_archive_store.load();
				_w_query_edit_select_q.hide();
				AOS.reset(_f_select_edit_query);
			}
			function _w_del_data(){
				var selection = AOS.selection(_g_data, 'id_');
				if (AOS.empty(selection)) {
					AOS.tip('删除前请先选中数据');
					return;
				}
				var msg = AOS.merge('确认要删除选中的[{0}]个数据吗？', AOS.rows(_g_data));
				AOS.confirm(msg, function (btn) {
					if (btn === 'cancel') {
						AOS.tip('删除操作被取消。');
						return;
					}
					AOS.ajax({
						url: 'deletetopic_data.jhtml',
						params: {
							aos_rows_: selection,
							pid:Ext.getCmp("aos_module_id_").getValue()
						},
						ok: function (data) {
							AOS.tip(data.appmsg);
							_g_data_store.reload();
						}
					});
				});
			}
			//加载任务数据
			function _load_data_topic(){
				var params = {
					id_ : Ext.getCmp("aos_module_id_").getValue()
				};
				//这个Store的命名规则为：表格ID+"_store"。
				_g_data_store.getProxy().extraParams = params;
				_g_data_store.reload();

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
			function ok_save_archive_edit(){
				var tablename=Ext.getCmp("sjkmc").value;
				var count=AOS.rows(_g_select_edit_archive);
				if(count<=0){
					AOS.tip("请选择专题数据");
					return;
				}
				var ids="";
				var row=_g_select_edit_archive.getSelectionModel().getSelection();
				for(var i=0;i<AOS.rows(_g_select_edit_archive);i++){
					if(i==0){
						ids=row[i].data.id_;
					}else{
						ids+=","+row[i].data.id_;
					}
				}
				var params = {
					ids:ids,
					tablename:tablename,
					id_ : Ext.getCmp("aos_module_id_").getValue()
				};
				AOS.ajax({
					url: 'updatetopic_data.jhtml',
					params:params,
					ok: function (data) {
						if(data.appcode===1){
							AOS.tip("添加成功!");
							_g_data_store.load();
							_w_select_edit_archive.hide();
							//_g_receive_query();
						}else if(data.appcode===-1){
							AOS.tip("添加失败!");
						}
					}
				});
			}
			//选择数据表后展开数据
			function by_select_sjkmc(){
				//根据选择的名称
				var sjkmc_value = Ext.getCmp("sjkmc").value;
				var params = {
					tablename : sjkmc_value,
					id_ : Ext.getCmp("aos_module_id_").getValue()
				};
				//这个Store的命名规则为：表格ID+"_store"。
				_g_select_edit_archive_store.getProxy().extraParams = params;
				_g_select_edit_archive_store.load();
			}
		</script>
	</aos:onready>
</aos:html>
<%@ page contentType="text/html; charset=utf-8" isELIgnored="false"%>
<%@ taglib uri="/WEB-INF/tld/aos.tld" prefix="aos"%>
<%@ taglib uri="/WEB-INF/tld/cll.tld" prefix="c"%>
<%
	String path = request.getContextPath();
	Object fieldDtos = request.getAttribute("fieldDtos");
	Object listtablename = request.getAttribute("listtablename");
%>
<aos:html>
	<aos:head title="专题数据展示页面">
		<aos:include lib="ext" />
		<aos:base href="archive/topic" />
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
		<aos:viewport layout="fit">
			<aos:gridpanel id="_g_data" url="listAccounts_topic.jhtml"  region="center" split="true" onrender="_load_data_topic"
						   pageSize="100" onitemclick="itemclick"  enableLocking="true" rowclass="true">
				<aos:docked>
					<aos:dockeditem xtype="tbtext" text="鉴定数据库数据" />
					<aos:hiddenfield id="rowmath" name="rowmath" value="0" />
					<aos:hiddenfield id="query" name="query" />
					<aos:hiddenfield name="ids" id="ids"/>
					<aos:hiddenfield name="dhs" id="dhs"/>
					<aos:hiddenfield name="aos_module_id_" id="aos_module_id_" value="${aos_module_id_}"/>
					<aos:dockeditem text="增加" icon="add.png"  onclick="_g_add_data" id="_f_add_data"/>
					<aos:dockeditem text="删除" icon="del.png" onclick="_w_del_data"  id="_f_del_data"/>
				</aos:docked>
				<aos:selmodel type="checkbox" mode="multi" />
				<aos:column dataIndex="id_" header="流水号" hidden="true"  locked="true"/>
				<aos:column header="_path" dataIndex="_path" hidden="true" />
				<aos:column header="全宗单位" dataIndex="qzdw" />
				<aos:column header="档号" dataIndex="dh"  width="200" />
				<aos:column header="责任者" dataIndex="zrz"  width="200" />
				<aos:column header="形成时间" dataIndex="xcsj" />
				<aos:column header="题名" dataIndex="tm"    width="500"/>
				<aos:column header="页号" dataIndex="yh" />
				<aos:column header="页数" dataIndex="ys"  />
				<aos:column header="是否开放" dataIndex="sfkf" />
				<aos:column header="全宗号" dataIndex="qzh" />
				<aos:column header="目录号" dataIndex="mlh"  />
				<aos:column header="保管期限" dataIndex="bgqx" />
				<aos:column header="案卷号" dataIndex="ajh" />
				<aos:column header="顺序号（件号）" dataIndex="sxh" />
				<aos:column header="数据库" dataIndex="tablename"/>
				<aos:column header="" flex="1" />
			</aos:gridpanel>
			<aos:window id="_w_select_edit_archive" title="鉴定数据选择"  width="800" height="500">
				<aos:gridpanel id="_g_select_edit_archive" url="listdata_edit.jhtml" region="north" width="770" height="460"
							   autoScroll="true" pageSize="100" enableLocking="true" hidePagebar="false">
					<aos:docked forceBoder="0 0 1 0">
						<aos:dockeditem xtype="tbtext" text="数据列表" />

						<aos:combobox fieldLabel="鉴定数据库" name="sjkmc" allowBlank="false"
									  fields="['tablename','tabledesc']" id="sjkmc" displayField="tabledesc" valueField="tablename"
									  editable="false" columnWidth="0.7" url="listTablename_by.jhtml" onselect="by_select_sjkmc" width="300"/>
						<aos:dockeditem text="查询" icon="query.png"
										onclick="_w_select_edit_query_show" />
						<aos:dockeditem  text="确定" icon="ok.png"
										 onclick="ok_save_archive_edit" />
						<aos:dockeditem  text="全选" icon="more/edit-select-all-4.png"
										 onclick="ok_save_All_archive" />
					</aos:docked>
					<aos:selmodel type="checkbox" mode="multi" />
					<aos:column dataIndex="id_" header="流水号" hidden="true" />
					<aos:column header="_path" dataIndex="_path" hidden="true" />
					<aos:column header="全宗单位" dataIndex="qzdw" />
					<aos:column header="档号" dataIndex="dh"  width="200" />
					<aos:column header="责任者" dataIndex="zrz"  width="200" />
					<aos:column header="形成时间" dataIndex="xcsj" />
					<aos:column header="题名" dataIndex="tm"    width="500"/>
					<aos:column header="页号" dataIndex="yh" />
					<aos:column header="页数" dataIndex="ys"  />
					<aos:column header="是否开放" dataIndex="sfkf" />
					<aos:column header="全宗号" dataIndex="qzh" />
					<aos:column header="目录号" dataIndex="mlh"  />
					<aos:column header="保管期限" dataIndex="bgqx" />
					<aos:column header="案卷号" dataIndex="ajh" />
					<aos:column header="顺序号（件号）" dataIndex="sxh" />
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
			//保存当前选中的档案信息
			function ok_save_All_archive(){
				var model = _g_select_edit_archive.getSelectionModel();
				model.selectAll();//选择所有行
				var ids="";
				var dhs="";
				//走后台进行当前条件的所有条目的id集合
				AOS.ajax({
					url: 'getQueryIds.jhtml',
					params:{'tablename':Ext.getCmp("sjkmc").getValue(),
						'query':Ext.getCmp("query").getValue()},
					ok: function(data){
						var ids="";
						var dhs="";
						for(k in data){
							if(k==0){
								ids=data[k].id_;
								dhs=data[k].dh;
							}else{
								ids=ids+","+data[k].id_;
								dhs=dhs+","+data[k].dh;
							}
						}
						Ext.getCmp('ids').setValue(ids);
						Ext.getCmp('dhs').setValue(dhs);
						_w_select_archive.hide();
					}
				});
			}
			function _g_add_data(){
				//弹出选择档案窗口
				_w_select_edit_archive.show();
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
			//查询窗口展开
			function _w_select_edit_query_show() {
				//判断是不是
				Ext.getCmp("query").setValue("");
				_w_query_edit_select_q.show();
			}
			function _f_select_edit_data_query(){
				var params = AOS.getValue('_f_select_edit_query');
				var form = Ext.getCmp('_f_select_edit_query');
				var tablename = Ext.getCmp("sjkmc").getValue();
				for(var i=1;i<=5;i++){
					var str = form.down("[name='filedname"+i+"']");
					var filedname =str.getValue();
					if(filedname==null){
						params["filedname"+i]=str.regexText;
					}
				}
				params["id_"]=Ext.getCmp("aos_module_id_").getValue();
				params["query_old"]=Ext.getCmp("query").getValue();
				params["tablename"]=tablename;
				_g_select_edit_archive_store.getProxy().extraParams = params;
				_g_select_edit_archive_store.load();
				AOS.ajax({
					params:params,
					url: 'saveQueryData.jhtml',
					ok: function (data) {
						//此时在隐藏域中存入查询条件
						Ext.getCmp("query").setValue(Ext.getCmp("query").getValue()+" "+data.appmsg);
					}
				});
				_w_query_edit_select_q.hide();
				AOS.reset(_f_select_edit_query);
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
			function changeRowClass(record, rowIndex, rowParams, store){

				//得到当前行的指定的列的值
				if(record.get("_path")>=1){
					return 'grid-one-column';
				}else{
					return 'grid-zero-column';
				}

			}
			//用于涉及到全部选择，排除分页的影响

			function ok_save_archive_edit(){
				var tablename=Ext.getCmp("sjkmc").value;
				var count=AOS.rows(_g_select_edit_archive);
				var ids=Ext.getCmp('ids').getValue();
				var dhs=Ext.getCmp('dhs').getValue();

				var params = {
					ids:ids,
					dhs:dhs,
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
				Ext.getCmp("ids").setValue("");
				Ext.getCmp("dhs").setValue("");
				Ext.getCmp("query").setValue("");
			}
		</script>
	</aos:onready>
</aos:html>
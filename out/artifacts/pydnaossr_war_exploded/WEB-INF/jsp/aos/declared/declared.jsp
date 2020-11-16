<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="/WEB-INF/tld/aos.tld" prefix="aos"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<aos:html>
	<aos:head title="公告设置">
		<aos:include lib="ext" />
		<aos:base href="archive/declared"/>
	</aos:head>
	<aos:body>
	</aos:body>
	<aos:onready>
		<aos:viewport layout="fit">
			<aos:gridpanel id="_g_declared" url="listdeclared.jhtml"
						   onrender="_g_declared_query">
				<aos:docked>
					<aos:dockeditem xtype="tbtext" text="公告列表" />
					<aos:dockeditem xtype="tbseparator" />
					<aos:dockeditem text="新增" icon="folder15.png" onclick="_w_declared_add" />
					<aos:dockeditem text="修改" icon="folder15.png" onclick="_w_declared_update" />
					<aos:dockeditem text="删除" icon="del.png" onclick="_w_declared_del" />
				</aos:docked>
				<aos:column type="rowno" />
				<aos:column header="流水号" dataIndex="id_" hidden="true" align="center" />
				<aos:column header="公告名称" dataIndex="name_" width="60"
							align="center" />
				<aos:column header="公告内容" dataIndex="desc_" width="60"
							align="center" />
				<aos:column header="创建人" dataIndex="create_person" width="60"
							align="center" />
				<aos:column header="创建时间" dataIndex="create_time" width="60"
							align="center" />
			</aos:gridpanel>
			<aos:window id="_w_add" title="新增">
				<aos:formpanel id="_f_add" width="400" layout="column"
							   labelWidth="70" >
					<aos:textfield name="name_" fieldLabel="公告名称" columnWidth="0.99"/>
					<aos:textareafield name="desc_" fieldLabel="公告描述" columnWidth="0.99"/>
					<aos:textfield name="create_person" fieldLabel="创建人"  columnWidth="0.99"/>
					<aos:datefield name="create_time" fieldLabel="创建时间" format="Y-m-d"
								   columnWidth="0.99" allowBlank="false" />
				</aos:formpanel>
				<aos:docked dock="bottom" ui="footer">
					<aos:dockeditem xtype="tbfill" />
					<aos:dockeditem onclick="_w_add_reset" text="新增" icon="add.png" />
					<aos:dockeditem onclick="#_w_add.hide();" text="关闭" icon="close.png" />
				</aos:docked>
			</aos:window>
			<aos:window id="_w_update" title="修改" onshow="_w_update_show">
				<aos:formpanel id="_f_update" width="400" layout="column"
							   labelWidth="70" >
					<aos:hiddenfield name="id_" />
					<aos:textfield name="name_" fieldLabel="公告名称" columnWidth="0.99"/>
					<aos:textfield name="desc_" fieldLabel="公告描述" columnWidth="0.99"/>
					<aos:textfield name="create_person" fieldLabel="创建人" columnWidth="1.0"/>
					<aos:datefield name="create_time" fieldLabel="创建时间" format="Y-m-d"
								   columnWidth="1.0" allowBlank="false" />
			</aos:formpanel>
				<aos:docked dock="bottom" ui="footer">
					<aos:dockeditem xtype="tbfill" />
					<aos:dockeditem onclick="_w_update_reset" text="保存" icon="save.png" />
					<aos:dockeditem onclick="#_w_update.hide();" text="关闭" icon="close.png" />
				</aos:docked>
			</aos:window>
		</aos:viewport>

		<script type="text/javascript">
			function _g_log_query(){
				var params = {
					party : party.getValue()
				};
				//这个Store的命名规则为：表格ID+"_store"。
				_g_log_store.getProxy().extraParams = params;
				_g_log_store.load();
			}
			//弹出窗口加载
			function _w_log_onshow(){
				var record = AOS.selectone(_g_log, true);
				AOS.ajax({
					params : {
						id: record.data.id
					},
					url: 'getlogInfo.jhtml',
					ok: function (data) {
						_f_log.form.setValues(data);
					}
				});
			}
			//打开执行的方法
			function _f_update_show(record){
					var tablename=record.raw.category_tablename;
				AOS.ajax({
					url:'getQueryTitlename.jhtml',
					params:{
						tablename:tablename
					},
					ok:function(data){
						//后台的值就行迭代赋予给前端
						var fieldennames=data[0].fieldenname;
						var fieldcnname=data[0].fieldcnname;
						//传入默认值
						Ext.getCmp('fieldname2').getStore().add(data);
						Ext.getCmp('fieldname2').setValue(fieldennames);

					}
				});
			}
			//导出日志
			function fn_export_declared(){
				AOS.ajax({
					url : 'fillReport.jhtml',
					ok : function(data) {
						AOS.file('${cxt}/report/xls2.jhtml');
					}
				});
			}
			function add_field_list(){
				//通过点击下拉框在下方grid中添加
				var fieldcnname=Ext.getCmp("fieldname").getRawValue();
				var fieldenname=Ext.getCmp("fieldname").getValue();
				var params = [{
					'fieldennames':fieldenname,
					'fieldcnnames':fieldcnname
				}];
				var count=_g_field_store.getCount();
				if(count>0){
					for(var i=0;i<count;i++){
							var leftenzd = _g_field_store.getAt(i).get("fieldennames");
							if(leftenzd===fieldenname){
								return;
							}
					}
				}
				///_g_field_store.insert(0,params);
				_g_field_store.loadData(params,true);
			}
			function add_declared(){
				var fieldcnname=_f_add.getForm().findField('declared').getValue();
				var params = [{
					'fieldcnnames':fieldcnname
				}];
				var count=_g_field_store.getCount();
				if(count>0){
					for(var i=0;i<count;i++){
						var leftenzd = _g_field_store.getAt(i).get("fieldcnnames");
						if(leftenzd===fieldcnname){
							return;
						}
					}
				}
				///_g_field_store.insert(0,params);
				_g_field_store.loadData(params,true);
			}
			//加载列表
			function _g_declared_query(){
				_g_declared_store.reload();
			}
			function _w_declared_add(){
				_w_add.show();
				_f_add.getForm().findField('create_person').setValue("<%=session.getAttribute("user")%>");
				_f_add.getForm().findField('create_time').setValue(getNowFormatDate());
			}
			function getNowFormatDate() {
				var date = new Date();
				var seperator1 = "-";
				var year = date.getFullYear();
				var month = date.getMonth() + 1;
				var strDate = date.getDate();
				if (month >= 1 && month <= 9) {
					month = "0" + month;
				}
				if (strDate >= 0 && strDate <= 9) {
					strDate = "0" + strDate;
				}
				var currentdate = year + seperator1 + month + seperator1 + strDate;
				return currentdate;
			}
			function _w_add_reset(){
				AOS.ajax({
					forms:_f_add,
					url: 'adddeclared.jhtml',
					ok: function (data) {
						if(data.appcode===1){
							_w_add.hide();
							_g_declared_store.reload();
							AOS.tip("添加成功");
						}else{
							AOS.tip("添加失败");
						}
					}
				});
			}
			function _w_declared_update(){
				var record=AOS.selectone(_g_declared);
				if(AOS.empty(record)){
					AOS.err("请选择要修改的公告");
					return;
				}
				_w_update.show();
			}
			function _w_update_show(){
				//数据加载过去
				var record=AOS.selectone(_g_declared);
				_f_update.loadRecord(record);
			}

			function _w_update_reset(){
				var s=Ext.getCmp('_g_field2').getStore();
				AOS.ajax({
					forms:_f_update,
					url: 'updatedeclared.jhtml',
					ok: function (data) {
						if(data.appcode===1){
							_w_update.hide();
							AOS.tip("修改成功");
							_g_declared_store.reload();
						}else{
							AOS.tip("修改失败");
						}
					}
				});
			}
			function _w_declared_del(){
				var record = AOS.selectone(_g_declared, true);
				var msg = AOS.merge('确认要删除选中的[{0}]个公告吗？', AOS.rows(_g_declared));
				if (AOS.empty(record)) {
					AOS.tip('删除前请先选中数据!');
					return;
				}else{
					AOS.confirm(msg, function (btn) {
						if (btn === 'cancel') {
							AOS.tip('删除操作被取消。');
							return;
						}
						AOS.ajax({
							params : {
								id_: record.data.id_
							},
							url: 'deldeclared.jhtml',
							ok: function (data) {
								if (data.appcode === 1) {
									_w_update.hide();
									AOS.tip("删除成功");
									_g_declared_store.reload();
								} else {
									AOS.tip("删除失败");
								}
							}
						});
					});
				}
			}
			//删除当前选中的节点
			function _g_field_del(){
				var row=_g_field.getSelectionModel().getSelection();
				//右侧删除，左侧添加
				_g_field_store.remove(row);
			}
			function _g_field_del2(){
				var row=_g_field2.getSelectionModel().getSelection();
				//右侧删除，左侧添加
				_g_field2_store.remove(row);
			}
		</script>
	</aos:onready>
	<script type="text/javascript">
		//显示详情1窗口
		function _w_show(){
			Ext.getCmp('_w_log').show();
		}
	</script>
</aos:html>
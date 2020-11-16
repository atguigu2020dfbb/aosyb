<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="/WEB-INF/tld/aos.tld" prefix="aos"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<aos:html>
	<aos:head title="统计定义">
		<aos:include lib="ext" />
		<aos:base href="census/census" />
	</aos:head>
	<aos:body>
	</aos:body>
	<aos:onready>
		<aos:viewport layout="fit">
			<aos:gridpanel id="_g_census" url="listcensus.jhtml"
						   onrender="_g_census_query" onitemdblclick="_w_census_update">
				<aos:docked>
					<aos:dockeditem xtype="tbtext" text="统计定义表" />
					<aos:dockeditem xtype="tbseparator" />
					<aos:combobox fieldLabel="统计档案门类" name="tableTemplate"
								  fields="[ 'value', 'display', 'tablename']" id="tableTemplate"
								  editable="false" columnWidth="0.5" url="listComboBoxid.jhtml" />
					<aos:dockeditem text="新增" icon="folder15.png" onclick="_w_census_add" />
					<aos:dockeditem text="修改" icon="folder15.png" onclick="_w_census_update" />
					<aos:dockeditem text="删除" icon="del.png" onclick="_w_census_del" />
				</aos:docked>
				<aos:column type="rowno" />
				<aos:column header="流水号" dataIndex="id_" hidden="true" align="center" />
				<aos:column header="统计编号" dataIndex="bh_number" width="60"
							align="center" />
				<aos:column header="表名称" dataIndex="category_tablename" width="60"
							align="center" />
				<aos:column header="统计名称" dataIndex="name_" width="60"
							align="center" />
				<aos:column header="统计描述" dataIndex="desc_" width="60"
							align="center" />
				<aos:column header="统计列表" dataIndex="census_list" width="60" hidden="true"
							align="center" />
				<aos:column header="创建人" dataIndex="create_person" width="60"
							align="center" />
				<aos:column header="创建时间" dataIndex="create_time" width="60"
							align="center" />
			</aos:gridpanel>
			<aos:window id="_w_add" title="新增" onshow="_f_query_show">
				<aos:formpanel id="_f_add" width="400" layout="anchor"
							   labelWidth="70" >
					<aos:textfield name="bh_number" fieldLabel="统计编号"/>
					<aos:textfield name="name_" fieldLabel="统计名称" />
					<aos:textfield name="category_tablename" fieldLabel="表名称" />
					<aos:textfield name="desc_" fieldLabel="统计描述" />
					<aos:combobox id="fieldname" fieldLabel="选择字段"
								  name="fieldname" fields="['fieldenname','fieldcnname']"
								  displayField="fieldcnname"
								  valueField="fieldenname" onselect="add_field_list">
					</aos:combobox>

					<aos:textfield name="create_person" fieldLabel="创建人" />
					<aos:datefield name="create_time" fieldLabel="创建时间" format="Y-m-d"
								   columnWidth="1.0" allowBlank="false" />
				</aos:formpanel>
				<aos:gridpanel id="_g_field" split="true" hidePagebar="true" autoScroll="true"
							   height="100" width="400" drag="true">
					<aos:menu>
						<aos:menuitem text="删除" onclick="_g_field_del" icon="del.png" />
					</aos:menu>
					<aos:column header="字段名称" dataIndex="fieldennames" width="200"/>
					<aos:column header="字段描述" dataIndex="fieldcnnames" width="200"/>
				</aos:gridpanel>
				<aos:docked dock="bottom" ui="footer">
					<aos:dockeditem xtype="tbfill" />
					<aos:dockeditem onclick="_w_add_reset" text="新增" icon="add.png" />
					<aos:dockeditem onclick="#_w_add.hide();" text="关闭" icon="close.png" />
				</aos:docked>
			</aos:window>
			<aos:window id="_w_update" title="修改" onshow="_w_update_show">
				<aos:formpanel id="_f_update" width="400" layout="anchor"
							   labelWidth="70" >
					<aos:hiddenfield name="id_" />
					<aos:textfield name="bh_number" fieldLabel="统计编号"/>
					<aos:textfield name="name_" fieldLabel="统计名称" />
					<aos:textfield name="category_tablename" fieldLabel="表名称" />
					<aos:combobox id="fieldname2" fieldLabel="选择字段"
								  name="fieldname2" fields="['fieldenname','fieldcnname']"
								  displayField="fieldcnname"
								  valueField="fieldenname" onselect="update_field_list">
					</aos:combobox>
					<aos:gridpanel id="_g_field2" split="true" hidePagebar="true" autoScroll="true"
								   height="300">
						<aos:menu>
							<aos:menuitem text="删除" onclick="_g_field_del2" icon="del.png" />
						</aos:menu>
						<aos:column header="字段名称" dataIndex="fieldennames" width="60"/>
						<aos:column header="字段描述" dataIndex="fieldcnnames" width="60"/>
					</aos:gridpanel>

					<aos:textfield name="create_person" fieldLabel="创建人" />
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
			function _f_query_show(){

				var tablename_id=Ext.getCmp("tableTemplate").getValue();
				AOS.ajax({
					url:'getQueryTitle.jhtml',
					params:{
						tablename_id:tablename_id
					},
					ok:function(data){
						//后台的值就行迭代赋予给前端
							var fieldenname=data[0].fieldenname;
							var fieldcnname=data[0].fieldcnname;
							//传入默认值
						Ext.getCmp('fieldname').getStore().add(data);
						Ext.getCmp('fieldname').setValue(fieldenname);

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
			function fn_export_census(){
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
			function update_field_list(){
				//通过点击下拉框在下方grid中添加
				var fieldcnname=Ext.getCmp('fieldname2').getRawValue();
				var fieldenname=Ext.getCmp('fieldname2').getValue();

				var params = [{
					'fieldennames':fieldenname,
					'fieldcnnames':fieldcnname
				}];
				var count=_g_field2_store.getCount();
				if(count>0){
					for(var i=0;i<count;i++){
						var leftenzd = _g_field2_store.getAt(i).get("fieldennames");
						if(leftenzd===fieldenname){
							return;
						}
					}
				}
				///_g_field_store.insert(0,params);
				_g_field2_store.loadData(params,true);
			}
			//加载列表
			function _g_census_query(){
				_g_census_store.reload();
			}
			function _w_census_add(){
				_w_add.show();
				_f_add.getForm().findField('create_person').setValue("<%=session.getAttribute("user")%>");
				_f_add.getForm().findField('create_time').setValue(getNowFormatDate());

				var tablename_id=Ext.getCmp("tableTemplate").getValue();

				AOS.ajax({
					url:'gettablename.jhtml',
					params:{
						tablename_id:tablename_id
					},
					ok:function(data){
						_f_add.getForm().findField('category_tablename').setValue(data.appmsg);

					}
				});



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
				var s=Ext.getCmp('_g_field').getStore();
				var fieldennames="";
				var fieldcnnames="";
				for(var i = 0 ;i< s.getCount(); i++){
					fieldennames+=s.getAt(i).get('fieldennames')+",";
					fieldcnnames+=s.getAt(i).get('fieldcnnames')+",";
				}
				//去掉最后一个字符
				fieldennames=fieldennames.substring(0,fieldennames.length-1);
				fieldcnnames=fieldcnnames.substring(0,fieldcnnames.length-1);
				AOS.ajax({
					forms:_f_add,
					params:{
						fieldennames:fieldennames,
						fieldcnnames:fieldcnnames
					},
					url: 'addcensus.jhtml',
					ok: function (data) {
						if(data.appcode===1){
							_w_add.hide();
							_g_census_store.reload();
							AOS.tip("添加成功");
						}else{
							AOS.tip("添加失败");
						}
					}
				});
			}
			function _w_census_update(){
				var record=AOS.selectone(_g_census);
				if(AOS.empty(record)){
					AOS.err("请选择要修改的全宗信息");
					return;
				}
				_w_update.show();
			}
			function _w_update_show(){
				//数据加载过去
				var record=AOS.selectone(_g_census);
				_f_update.loadRecord(record);
				_f_update_show(record);
				_g_field2_store.removeAll();
				var fieldcnnames=record.raw.desc_;
				var fieldennames=record.raw.census_list;
				var fieldcnname=fieldcnnames.split(',');
				var fieldenname=fieldennames.split(',');
				if(fieldcnname.length===fieldenname.length){
					for(var i=0;i<fieldcnname.length; i++){
						var enname=fieldenname[i];
						var cnname=fieldcnname[i];
						var data=[
							{
								'fieldennames':enname,
								'fieldcnnames':cnname
							}
						];
						_g_field2_store.loadData(data,true);
					}

				}

			}

			function _w_update_reset(){
				var s=Ext.getCmp('_g_field2').getStore();
				var fieldennames="";
				var fieldcnnames="";
				for(var i = 0 ;i< s.getCount(); i++){
					fieldennames+=s.getAt(i).get('fieldennames')+",";
					fieldcnnames+=s.getAt(i).get('fieldcnnames')+",";
				}
				//去掉最后一个字符
				fieldennames=fieldennames.substring(0,fieldennames.length-1);
				fieldcnnames=fieldcnnames.substring(0,fieldcnnames.length-1);
				AOS.ajax({
					forms:_f_update,
					url: 'updatecensus.jhtml',
					params:{
						fieldennames:fieldennames,
						fieldcnnames:fieldcnnames
					},
					ok: function (data) {
						if(data.appcode===1){
							_w_update.hide();
							AOS.tip("修改成功");
							_g_census_store.reload();
						}else{
							AOS.tip("修改失败");
						}
					}
				});
			}
			function _w_census_del(){
				var record = AOS.selectone(_g_census, true);
				var msg = AOS.merge('确认要删除选中的[{0}]个统计定义吗？', AOS.rows(_g_census));
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
							url: 'delcensus.jhtml',
							ok: function (data) {
								if (data.appcode === 1) {
									_w_update.hide();
									AOS.tip("删除成功");
									_g_census_store.reload();
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
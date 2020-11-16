<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="/WEB-INF/tld/aos.tld" prefix="aos"%>
<%@ taglib uri="/WEB-INF/tld/c.tld" prefix="c"%>
<aos:html>
<aos:head title="数据中心">
	<aos:include lib="ext" />
	<aos:base href="system/maintain" />
</aos:head>
<aos:body>
</aos:body>
<aos:onready>
	<aos:viewport layout="border">
		<aos:gridpanel id="_g_table" region="west" width="300" url="listAccounts.jhtml"
			onitemclick="_g_data_click" onrender="_g_table_onrender" hidePagebar="true" split="true">
			<aos:selmodel type="row" mode="multi" />
			<aos:column type="rowno" />
			<aos:column header="表流水号" dataIndex="id_" hidden="true" />
			<aos:column header="数据表名称" dataIndex="tablename" width="90" />
			<aos:column header="数据表描述" dataIndex="tabledesc" width="80" />
		</aos:gridpanel>
		
		<aos:gridpanel id="_g_data" region="center" url="listData.jhtml" autoScroll="true" forceFit="false">
		
		<aos:docked>
				<aos:dockeditem xtype="tbtext" text="数据信息" />
				<aos:dockeditem xtype="tbfill" />
				<aos:dockeditem text="查询" icon="query.png" onclick="_w_query_show" />
				<aos:dockeditem text="归档入库" icon="folder6.png" onclick="_w_export_show
				" />
				<aos:dockeditem text="清空数据表" icon="del.png" onclick="_g_data_del"/>
			</aos:docked>
			<aos:column type="rowno" />
			<aos:column dataIndex="id_" hidden="true"/>
			
			
		</aos:gridpanel>
		
		<aos:window id="_w_export" title="导出" width="620" height="600"
		autoScroll="true" >
				<aos:formpanel id="_f_info" layout="column" width="600" labelWidth="70" header="false" icon="user8.png" >
					<aos:combobox fieldLabel="目的表" name="tableTemplate"
					fields="[ 'value', 'display', 'tablename']" id="tableTemplate"
					editable="false" columnWidth="0.5" url="listComboBoxid.jhtml"
					onselect="fn_combo_onselect"  valueField="tablename" />
				<aos:checkboxgroup fieldLabel="属性" columns="[80,80]">
					<aos:checkbox name="dztm" boxLabel="电子条目" checked="true"/>
					<aos:checkbox name="path" boxLabel="附件" />
				</aos:checkboxgroup>
				</aos:formpanel>
		
		
		
		<aos:gridpanel id="_g_export" region="center" width="600" url="getSourceField.jhtml"  hidePagebar="true">
				<aos:menu>
					<aos:menuitem text="删除" onclick="!_g_exprot_remove" icon="del.png" />
					<aos:menuitem xtype="menuseparator" />
				</aos:menu>
			<aos:docked>
				<aos:dockeditem xtype="tbtext" text="数据表字段" />
			</aos:docked>
			<aos:plugins>
				<aos:editing ptype="cellediting" clicksToEdit="1" onedit="fn_edit" />
			</aos:plugins>
			<aos:column type="rowno" />
			<aos:column header="源字段名称" dataIndex="sourcefieldname" width="400" />
			<aos:column header="目标字段名称" dataIndex="targetfieldname"  width="400" >
			<!--  -->
				<aos:combobox name="fieldTemplate" id="fieldTemplate"
					fields="[ 'fieldenname', 'fieldcnname']" 
					editable="false" columnWidth="0.5" url="listComboBox.jhtml" 
					 displayField="fieldcnname" valueField="fieldcnname"  />
			</aos:column>
			<aos:column header="源字段英文名称" dataIndex="sourcefieldenname" hidden="true" />
			<aos:column header="目的字段英文名称" dataIndex="targetfieldenname" hidden="true" />
		</aos:gridpanel>
		<aos:docked dock="bottom" ui="footer">
			<aos:dockeditem xtype="tbfill" />
			<aos:dockeditem text="确定" icon="ok.png" onclick="fn_ok" />
			<aos:dockeditem text="退出" icon="close.png" onclick="#_w_export.hide()" />
		</aos:docked>
	</aos:window>
	
		<aos:window id="_w_query_q" title="查询" width="720" autoScroll="true"
		layout="fit" onshow="_f_query_show">
		<aos:tabpanel id="_tabpanel" region="center" activeTab="0"
			bodyBorder="0 0 0 0" tabBarHeight="30">
			<aos:tab title="列表式搜索" id="_tab_org">
				<aos:formpanel id="_f_query" layout="column" columnWidth="1">
					<aos:hiddenfield name="columnnum" id="columnnum" value="8" />
					<c:forEach var="fieldss"  begin="0" end="7"
						varStatus="listSearch">
						<aos:combobox name="and${listSearch.count}" value="on"
							labelWidth="10" columnWidth="0.2">
							<aos:option value="on" display="并且" />
							<aos:option value="up" display="或者" />
						</aos:combobox>						
						<aos:combobox  name="filedname${listSearch.count }"  id="filedname${listSearch.count }"
							 labelWidth="20" 
							columnWidth="0.2" fields="['fieldenname','fieldcnname']"
							 displayField="fieldcnname"  
							valueField="fieldenname" >
						</aos:combobox >
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
	</aos:viewport>
<script type="text/javascript">
	function _g_table_onrender(){
		_g_table_store.load();
	}
	
	function _g_data_click(){
	var record = AOS.selectone(_g_table);
		AOS.ajax({
			url:'getTableTitle.jhtml',
			params:{
				tablename:record.data.tablename
			},
			ok:function(data){
			var fields=[];
			var dataitem = [];
			var columns=[{ xtype:"rownumberer", text: "序号", width:40 }];
			
				for(var i=0;i<data.length;i++){
					var field={name:data[i].fieldenname};
					fields.push(field);
					var column={header:data[i].fieldcnname,dataIndex:data[i].fieldenname,width:data[i].dislen};
					columns.push(column);
				};
				var _g_temptable_store = Ext.create('Ext.data.Store', {
				fields : fields,
				autoLoad: true,
				pageSize:50,
					proxy : {
					type : 'ajax',
					url:'listData.jhtml',
					extraParams :{
						tablename:record.data.tablename
					},
					reader : {
						type : 'json',
						totalProperty: '_total', 
						root: '_rows'
					}
				}
				});
           _g_data.reconfigure(_g_temptable_store,columns);
			}
		})
		var params={
			tablename:record.data.tablename
		};
		_g_data_store.getProxy().extraParams = params;
		_g_data_store.load();
	}
	
	function _g_export_query(){
		var record = AOS.selectone(_g_table);
		var params={
			tablename:record.data.tablename
		};
		_g_export_store.getProxy().extraParams=params;
		_g_export_store.load();
	}
	function _w_export_show(){
		var selection = AOS.selection(_g_table, 'tablename');
		if(AOS.empty(selection)){
			 AOS.tip('请选择一条记录，操作取消。');
             return;
		}
		_w_export.show();
	}
	function fn_edit(editor, e){
		var selectValue = Ext.getCmp("fieldTemplate").displayTplData[0].fieldenname;
		_g_export_store.getAt(e.rowIdx).set('targetfieldenname',selectValue);
		e.record.commit();
	}
	function fn_combo_onselect(){
		var record = AOS.selectone(_g_table);
		var targetTablename=Ext.getCmp('tableTemplate').value;
      	var params = {
				table_desc_ : "",
				targetTablename:targetTablename,
				sourceTablename:record.data.tablename
			};
			 if (!AOS.empty(id)) {
				params.tid = id;
			}
			_g_export_store.getProxy().extraParams = params;
			fieldTemplate_store.getProxy().extraParams = params;
			_g_export_store.load();
	}
	function fn_ok(){
		var record = AOS.selectone(_g_table);
		var targetTablename=Ext.getCmp('tableTemplate').value;
		var excel_gridData = JSON.stringify(Ext.pluck(_g_export_store.data.items, 'data'));
	AOS.ajax({
				params :{
					aos_rows_ : excel_gridData,
					sourceTablename : record.data.tablename,
					targetTablename:targetTablename
				},
				forms: _f_info,
				url:'insertData.jhtml',
				ok:function(data) {
					_w_export.hide();
				AOS.tip(data.appmsg);
					//e.record.commit();
				}
			});
	//alert('111');
	}
	
	function _g_exprot_remove(e,a,c){
		var row=_g_export.getSelectionModel().getSelection();
		_g_export_store.remove(row);
	
	
	}
	function fn_onselect(){
	
		alert('9999');
	}
	function _g_data_del(){
		var record = AOS.selectone(_g_table);
		var msg = AOS.merge('确认清空表中数据吗？');
		AOS.confirm(msg,function(btn){
			if(btn === 'cancel'){
				AOS.tip('操作取消。');
			}
			AOS.ajax({
				url:'deleteData.jhtml',
				params:{
					tablename:record.data.tablename
				},
				ok:function(data){
					AOS.tip(data.appmsg);
					_g_data_click();
				}
			
			})
			
		
		
		})
	}
	function _w_query_show() {
         _w_query_q.show();
    }
    
    //打开执行的方法
	function _f_query_show(){
	 var record = AOS.selectone(_g_table);
	 	//走后台得到要查询的title集合数据,然后在展开
	 	var params={
			tablename:record.data.tablename
		};
		_g_data_store.getProxy().extraParams = params;
	 	AOS.ajax({
			url:'getQueryTitle.jhtml',
			params:{
				tablename:record.data.tablename
			},
			ok:function(data){
				//后台的值就行迭代赋予给前端
				for(var i=0;i<8;i++){
					var fieldenname=data[i].fieldenname;
					var fieldcnname=data[i].fieldcnname;
					//传入默认值
					Ext.getCmp('filedname'+(i+1)).getStore().add(data);
					Ext.getCmp('filedname'+(i+1)).setValue(fieldenname);
				}
			}
		});
	}
	function _f_data_query(){
	var record = AOS.selectone(_g_table);
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
	
	params["tablename"]=record.data.tablename;
	var record = AOS.selectone(_g_table);
		AOS.ajax({
			url:'getTableTitle.jhtml',
			params:{
				tablename:record.data.tablename
			},
			ok:function(data){
			var fields=[];
			var dataitem = [];
			var columns=[{ xtype:"rownumberer", text: "序号", width:40 }];
			
				for(var i=0;i<data.length;i++){
					var field={name:data[i].fieldenname};
					fields.push(field);
					var column={header:data[i].fieldcnname,dataIndex:data[i].fieldenname,width:data[i].dislen};
					columns.push(column);
				};
				var _g_temptable_store = Ext.create('Ext.data.Store', {
				fields : fields,
				autoLoad: true,
				pageSize:50,
					proxy : {
					type : 'ajax',
					url:'listData.jhtml',
					extraParams :params,
					reader : {
						type : 'json',
						totalProperty: '_total', 
						root: '_rows'
					}
				}
				});
           _g_data.reconfigure(_g_temptable_store,columns);
			}
		})
		_g_data_store.getProxy().extraParams = params;
		_g_data_store.load();
			_w_query_q.hide();
			AOS.reset(_f_query);
			
	}
</script>
</aos:onready>
</aos:html>
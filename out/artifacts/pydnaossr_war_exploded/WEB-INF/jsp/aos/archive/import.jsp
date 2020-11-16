<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="/WEB-INF/tld/aos.tld" prefix="aos"%>
<%@ taglib uri="/WEB-INF/tld/cll.tld" prefix="c"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<aos:html>
<aos:head title="数据导入">
	<aos:include lib="ext,swfupload" />
	<aos:base href="archive/import" />
</aos:head>
<aos:body>
</aos:body>
<aos:onready>
  	<aos:viewport layout="fit">
		<aos:gridpanel id="_g_excel" url="getExcel.jhtml" hidePagebar="true" onrender="_g_excel_account" >
			<aos:docked>
			<aos:hiddenfield id="localFilename" value="${localFilename }"/>
			<aos:hiddenfield id="tablename" value="${tablename }"/>
				<aos:hiddenfield id="ajtm" value="${ajtm }"/>
				<aos:hiddenfield id="_classtree" value="${_classtree}"/>
			<aos:dockeditem text="导入" id="importExcel" icon="folder6.png" onclick="_w_import_show" />
			</aos:docked>
			<aos:column type="rowno" />
			<aos:selmodel type="row" mode="multi" />
			<c:forEach var="field" items="${fieldDtos}">
				<aos:column dataIndex="${field.fieldenname }"
					header="${field.fieldcnname }"    rendererField="field_type_" />
			</c:forEach>
			<aos:column header="" flex="1" />
		</aos:gridpanel>
	</aos:viewport>
	<aos:window id="_w_excel" title="导入" width="620" height="600"
		autoScroll="true" >
		<aos:gridpanel id="_g_import" region="center" width="600" url="getSourceField.jhtml" onrender="_g_import_query" hidePagebar="true">
			<aos:plugins>
				<aos:editing ptype="cellediting" clicksToEdit="1" onedit="fn_edit" />
			</aos:plugins>
			<aos:column type="rowno" />
			<aos:column header="源字段名称" dataIndex="sourcefieldname" width="400" />
			<aos:column header="目标字段名称" dataIndex="targetfieldname"  width="400" >
			<!--  -->
			<aos:combobox  name="targetTemplate"
										fields="[ 'fieldenname', 'fieldcnname']" id="targetTemplate"
										editable="false" columnWidth="0.5" url="listComboBox.jhtml?tablename=${tablename }"
										displayField="fieldcnname" valueField="fieldenname" />
				</aos:column>
			<aos:column header="英文名称" dataIndex="fieldenname" hidden="true" />
		</aos:gridpanel>
		<aos:docked dock="bottom" ui="footer">
			<aos:dockeditem xtype="tbfill" />
			<aos:dockeditem text="确定" icon="ok.png" onclick="fn_ok" />
			<aos:dockeditem text="退出" icon="close.png" onclick="#_w_excel.hide()" />
		</aos:docked>
	</aos:window>
	<script>

	function _g_excel_account(){
		var params = {
				localFilename:localFilename.getValue()
			};
			//这个Store的命名规则为：表格ID+"_store"。
			_g_excel_store.getProxy().extraParams = params;
			_g_excel_store.load();
	}

	function _w_import_show(){
	_w_excel.show();
	}
	function _g_import_query(){
	var params = {
				localFilename:localFilename.getValue(),
				tablename:tablename.getValue()
			};
			//这个Store的命名规则为：表格ID+"_store"。
			_g_import_store.getProxy().extraParams = params;
			_g_import_store.load();
	}
	//单元格编辑监听事件
	function fn_edit(editor, e){
		_g_import_store.getAt(e.rowIdx).set('targetfieldname',Ext.getCmp('targetTemplate').getRawValue());
		//e.record.commit();
		_g_import_store.getAt(e.rowIdx).set('fieldenname',Ext.getCmp('targetTemplate').getValue());
	}	
	//确定导入
	function fn_ok(){
	
	var excel_gridData = JSON.stringify(Ext.pluck(_g_excel_store.data.items, 'data'));
	
	//alert(excel_gridData);
	var excel_importData = JSON.stringify(Ext.pluck(_g_import_store.data.items, 'data'));
				AOS.ajax({
				params :{
					aos_rows_ : excel_gridData,
					aos_row1_ : excel_importData,
					_classtree:_classtree.getValue(),
					ajtm:ajtm.getValue(),
					tablename : tablename.getValue()
				},
				url:'insertData.jhtml',
				ok:function(data) {
					_w_excel.hide();
				AOS.tip(data.appmsg);
					e.record.commit();
				}
			});
	}
	</script>
</aos:onready>
</aos:html>
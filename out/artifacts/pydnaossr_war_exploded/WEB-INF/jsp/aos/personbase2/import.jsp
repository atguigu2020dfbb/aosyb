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
				<aos:hiddenfield id="_classtree" value="${_classtree}"/>
				<aos:hiddenfield id="tablename" value="${listtablename}" />
				<aos:hiddenfield id="tabledesc" value="${listtabledesc}" />
				<aos:combobox name="listTablename"
							  fields="[ 'tablename', 'tabledesc']" fieldLabel="数据表"
							  id="listTablename" columnWidth="0.3" url="listZLKTablename.jhtml"
							  displayField="tabledesc" valueField="tablename" allowBlank="false" onselect="update_tablename"/>
				<aos:dockeditem text="打开" id="openExcel" icon="folder2.png"/>
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
					editable="false" columnWidth="0.5" url="listComboBox.jhtml?tablename=${tablename}"
					displayField="fieldcnname" valueField="fieldcnname" 
					 />
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
	window.onload=function(){
	var btn = Ext.get('openExcel-btnInnerEl');
	//var em = btn.getEl().childd('em');
	btn.setStyle({
            position : 'relative',
            display : 'block'
        });
        btn.createChild({
            tag : 'div',
            id : 'div_exam'
        });
	
	swfu = new SWFUpload({
	upload_url : "${cxt}/archive/upload/uploadExcel.jhtml", //接收上传的服务端url
	flash9_url : "${cxt}/static/swfupload/swfupload_f9.swf",
    flash_url : "${cxt}/static/swfupload/swfupload.swf",//swfupload压缩包解压后swfupload.swf的url
    button_placeholder_id : "div_exam",//上传按钮占位符的id
    file_types:"*.xls;*.xlsx",
    file_types_description:"Excel文件",
    file_size_limit : "20480",//用户可以选择的文件大小，有效的单位有B、KB、MB、GB，若无单位默认为KB
    button_width: 40, //按钮宽度
    button_height: 20, //按钮高度
    button_text: "打开",//按钮文字
    file_dialog_complete_handler:file_complete_handler, 
    file_queued_handler : file_queued_handler,
    upload_success_handler :upload_success_handler
	})
     Ext.get(swfu.movieName).setStyle({
			position:'absolute',
			left:"20px",
			opacity:0
			});
	}
	function update_tablename(){
		var tablename=Ext.getCmp("listTablename").getValue();
		var tabledesc=Ext.getCmp("listTablename").getRawValue();
		Ext.getCmp("tablename").setValue(tablename);
		Ext.getCmp("tabledesc").setValue(tabledesc);
	}
	function _g_excel_account(){
		var listTablename ="<%=request.getAttribute("listtablename")%>";
		var tablenamedesc ="<%=request.getAttribute("tablenamedesc")%>";
		if(listTablename===null||listTablename==""||listTablename=="null"||!listTablename){
			listTablename="";
			tablenamedesc="";
		}
		//让指定文本框赋值
		Ext.getCmp("listTablename").setValue(listTablename);
		Ext.getCmp("listTablename").setRawValue(tablenamedesc);
		Ext.getCmp("tablename").setValue(listTablename);
		Ext.getCmp("tabledesc").setValue(tablenamedesc);
		var params = {
				localFilename:localFilename.getValue()
			};
			//这个Store的命名规则为：表格ID+"_store"。
			_g_excel_store.getProxy().extraParams = params;
			_g_excel_store.load();
	}
	function file_queued_handler(file){
	localFilename.setValue(file.name);
	}
	
	
	//打开之后事件
	function file_complete_handler(file){
	this.startUpload();
	//window.location.href="loadExcelGrid.jhtml?localFilename="+localFilename.getValue();
	}
	//上传完之后的事件
	function upload_success_handler(){
		var listtablename = Ext.getCmp("tablename").getValue();
		var tablenamedesc = Ext.getCmp("tabledesc").getValue();
	window.location.href="loadExcelGrid_backup.jhtml?localFilename="+localFilename.getValue()+"&_classtree="+_classtree.getValue()+"&listtablename="+listtablename+"&tablenamedesc="+tablenamedesc;
	}
	function _w_import_show(){
		var listTablename = Ext.getCmp("listTablename").value;
		var tablenamedesc = Ext.getCmp("listTablename").getRawValue();
		if(tablenamedesc==""||tablenamedesc==null||tablenamedesc=="null"){
				AOS.tip("请选择操作的门类");
				return;
		}
	_w_excel.show();
	}
	function _g_import_query(){
		var listTablename = Ext.getCmp("tablename").getValue();

	    var params = {
				localFilename:localFilename.getValue(),
				tablename:listTablename
			};
			//这个Store的命名规则为：表格ID+"_store"。
			_g_import_store.getProxy().extraParams = params;
			_g_import_store.load();
	}
	//单元格编辑监听事件
	function fn_edit(editor, e){
	var selectValue = Ext.getCmp("targetTemplate").displayTplData[0].targetfieldenname;
	_g_import_store.getAt(e.rowIdx).set('targetfieldenname',selectValue);
	e.record.commit();
	}	
	//确定导入
	function fn_ok(){
	var excel_gridData = JSON.stringify(Ext.pluck(_g_excel_store.data.items, 'data'));
	var excel_importData = JSON.stringify(Ext.pluck(_g_import_store.data.items, 'data'));
		var listTablename = Ext.getCmp("tablename").getValue();
				AOS.ajax({
				params :{
					aos_rows_ : excel_gridData,
					aos_row1_ : excel_importData,
					_classtree:_classtree.getValue(),
					tablename : listTablename
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
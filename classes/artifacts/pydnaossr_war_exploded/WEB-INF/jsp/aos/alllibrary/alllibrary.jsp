<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="/WEB-INF/tld/aos.tld" prefix="aos"%>
<%@ taglib uri="/WEB-INF/tld/cll.tld" prefix="c"%>
<aos:html>
<aos:head title="全库检索">
	<aos:include lib="ext" />
	<aos:base href="alllibrary/alllibrary" />
</aos:head>
<aos:body>
</aos:body>
<aos:onready>
	<aos:viewport layout="border">
		<aos:formpanel id="_f_info" layout="column" autoScroll="true" region="north" border="false"
			labelWidth="70">
			<aos:docked forceBoder="0 0 1 0">
				<aos:dockeditem xtype="tbtext" text="查询条件" />
			</aos:docked>
				<aos:textfield fieldLabel="" id="contentId" name="content" allowBlank="true"
					columnWidth="0.7" />
					<aos:button text="查询" margin="0 0 0 10" icon="query.png"
					onclick="_w_query" />
		</aos:formpanel>
		<aos:gridpanel id="_g_data" url="getDataList.jhtml" region="center" autoScroll="true" enableLocking="true">
		<aos:docked>
				<aos:dockeditem xtype="tbtext" text="目录信息" />
				<aos:dockeditem text="电子文件" icon="picture.png" onclick="_w_picture_show" />
			</aos:docked>
			<aos:column dataIndex="id_" header="流水号" hidden="true"/>
			<aos:column dataIndex="tablename" header="对应表名" hidden="true"/>
			<c:forEach var="field" items="${fieldDtos}">
				<aos:column dataIndex="${field.fieldenname}"
					header="${field.fieldcnname }"  rendererField="field_type_" />
			</c:forEach>
		</aos:gridpanel>
	</aos:viewport>
	<aos:window id="_w_picture_i" title="电子文件"    height="400"
		autoScroll="true" y="100"  onshow="_w_picture_onshow">
		<aos:gridpanel id="_g_picture" url="getPath.jhtml" width="700"  region="center" hidePagebar="true" onitemdblclick="_w_picture_dbclick">
			<aos:column type="rowno" />
			<aos:column dataIndex="id_" header="流水号" hidden="true"/>
			<aos:column dataIndex="_path" header="文件名" />
			<aos:column dataIndex="sdatetime" header="上传日期" />
		</aos:gridpanel>
		<aos:docked dock="bottom" ui="footer">
			<aos:dockeditem xtype="tbfill" />
			<aos:dockeditem onclick="#_w_picture_i.hide();" text="关闭"
				icon="close.png" />
		</aos:docked>
	</aos:window>
	<script>
		function _w_create(){
		
		AOS.ajax({
                    forms:_f_info,
                    url: 'listAccounts.jhtml',
                    ok: function (data) {
                        _f_print_u.form.setValues(data);
                    }
                });
		
		}
		//查询
		function _w_query(){
		var content=contentId.getValue();
		var params = {				
				content:content
			};
			//这个Store的命名规则为：表格ID+"_store"。
			_g_data_store.getProxy().extraParams = params;
			_g_data_store.load();
		}
		//弹出新增用户窗口
        function _w_picture_show() {
                _w_picture_i.show();
            }
        function _w_picture_u_rende(){
       // var selection = AOS.selection(_g_picture, 'id_');
       // alert(selection);
        var record = AOS.selectone(_g_data);
        //alert(record.data.id_);
        var params = {
				tablename : "wsgdwj",
				tid:record.data.id_
			};
			//这个Store的命名规则为：表格ID+"_store"。
			_g_picture_store.getProxy().extraParams = params;
			_g_picture_store.load();
        }
        function _w_picture_onshow(){
       var record = AOS.selectone(_g_data);
       if (AOS.empty(record)) {
           AOS.tip('请选择要查看的条目。');
           return;
       }
        var params = {
				tid:record.data.id_,
				tablename:record.data.tablename
			};
			//这个Store的命名规则为：表格ID+"_store"。
			_g_picture_store.getProxy().extraParams = params;
			_g_picture_store.load();
        }
        function _w_picture_dbclick(grid,row){
        var record = AOS.selectone(_g_data);
        parent.fnaddtab(row.data.id, '电子文件',
							'archive/data/openFile.jhtml?id='+row.data.id_+'&tablename='+record.data.tablename);
        }
	</script>
</aos:onready>
</aos:html>
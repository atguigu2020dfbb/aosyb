<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="/WEB-INF/tld/aos.tld" prefix="aos"%>
<%@ taglib uri="/WEB-INF/tld/cll.tld" prefix="c"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<aos:html>
<aos:head title="元数据信息">
	<aos:include lib="ext,swfupload,flexpaper" />
	<aos:base href="archive/data" />
<aos:include js="${cxt}/static/pdfObject/pdfobject.js" />
</aos:head>
<aos:body>
<div id="aa"> </div>
<!--  <div id="documentViewer" class="flexpaper_viewer" style="position:absolute;left:10px;top:10px;width:870px;height:600px"></div> -->
</aos:body>
<aos:onready>
	<aos:viewport layout="border" >
	<aos:panel id="documentViewer" width="800"  region="west" >
		
		</aos:panel>
		<aos:panel region="center" border="false">
		<aos:layout type="vbox" align="stretch" />
		<aos:gridpanel flex="1" id="_g_path" url="getPath.jhtml"  enableLocking="true" onitemdblclick="fn_g_path"  onrender="_g_path_account">
			<aos:docked>
				<aos:dockeditem xtype="tbtext" text="电子文件信息" />
				<aos:dockeditem xtype="tbfill" />
				<aos:triggerfield id="tablename"  value="${tablename }"
					style="display:'none'" />
					<aos:triggerfield id="pid"  value="${id }"
					style="display:'none'" />
					<aos:triggerfield id="tid"  value="${tid }"
					style="display:'none'" />
			</aos:docked>
			<aos:column type="rowno" />
			<aos:column header="流水号" dataIndex="id_" hidden="true" />
			<aos:column header="文件名称" dataIndex="_path" width="200"  />
			<aos:column header="上传时间" dataIndex="sdatetime" width="200"   />
			<aos:column header="文件类型" dataIndex="filetype" width="100"   />
			
		</aos:gridpanel>
			<!-- kjkjhkjhkjhkh -->

		</aos:panel>
	</aos:viewport>
　　<script type="text/javascript">
		window.onload = function (){
		//var aa = '${strswf}';
		
		//alert(aa);
    //PDFObject.embed('${cxt}/static/pdfObject/001.pdf', '#documentViewer');
    PDFObject.embed('${strswf}', '#documentViewer');
		};

        //加载表格数据
		function _g_account_query() {
		//var record = AOS.selectone(_g_account);
			var params = {
				id : pid.getValue(),
				tablename:tablename.getValue()
			};
			//这个Store的命名规则为：表格ID+"_store"。
			_g_account_store.getProxy().extraParams = params;
			_g_account_store.load();
		}
		
		//加载电子文件列表
		function _g_path_account(){
		var params = {
				tid : tid.getValue(),
				tablename:tablename.getValue()
			};
			//这个Store的命名规则为：表格ID+"_store"。
			_g_path_store.getProxy().extraParams = params;
			_g_path_store.load();
		}
		
		function fn_g_path(grid,row){
		AOS.ajax({
                 params: {id: row.data.id_,
                 tablename:tablename.getValue(),
                 type:row.data.filetype
                 },
                 url: 'openFileDbclick.jhtml',
                 ok: function (data) {
                 var pageCount=data[0].pageCount;
                 $FlexPaper("documentViewer").loadSwf("${cxt}/temp/{page[*,0].swf,"+pageCount+"}");
                 }
             });
		}
    </script>  
</aos:onready>
</aos:html>

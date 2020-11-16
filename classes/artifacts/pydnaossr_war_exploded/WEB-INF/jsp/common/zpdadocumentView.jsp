<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="/WEB-INF/tld/aos.tld" prefix="aos"%>
<%@ taglib uri="/WEB-INF/tld/cll.tld" prefix="c"%>
<% String count = (String) request.getAttribute("count"); %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<aos:html>
<aos:head title="元数据信息">
	<aos:include lib="ext,swfupload,flexpaper" />
	<aos:base href="archive/data" />
</aos:head>
<aos:body>

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
		<aos:gridpanel flex="1" id="_g_account" url="listExifs.jhtml"  enableLocking="true"  onrender="_g_account_query">
			<aos:docked>
				<aos:dockeditem xtype="tbtext" text="元数据信息" />
				<aos:dockeditem xtype="tbfill" />
				<aos:triggerfield id="tablename"  value="${tablename }"
					style="display:'none'" />
					<aos:triggerfield id="pid"  value="${id }"
					style="display:'none'" />
			</aos:docked>
			<aos:column type="rowno" />
			<aos:column header="流水号" dataIndex="id_" hidden="true" />
			<c:forEach var="field" items="${fieldDtos}">
				<aos:column dataIndex="${field.fieldenname}"
					header="${field.fieldcnname }" width="${field.dislen }" rendererField="field_type_" />
			</c:forEach>
		</aos:gridpanel>
		</aos:panel>
	</aos:viewport>
　　<script type="text/javascript">  
        $('#documentViewer').FlexPaperViewer(
        {  
             config : {  
                 //SwfFile : '${strswf }',//要转换的成pdf的swf文件
                 //SwfFile : 'http://127.0.0.1/Data/datata/gdwj/11200074/Paper.swf',//要转换的成pdf的swf文件  
                 SwfFile : '${cxt}/temp/page1.swf',
                 Scale : 0.6,   
                 ZoomTransition : 'easeOut',  
                 ZoomTime : 0.5,   
                 ZoomInterval : 0.1,  
                 FitPageOnLoad : true,  
                 FitWidthOnLoad : true,   
                 FullScreenAsMaxWindow : false,  
                 ProgressiveLoading : false,  
                 MinZoomSize : 0.2,  
                 MaxZoomSize : 5,  
                 SearchMatchAll : false,  
                 InitViewMode : 'Portrait',  
                 RenderingOrder : 'flash,flash',  
                 ViewModeToolsVisible : true,  
                 ZoomToolsVisible : true,  
                 NavToolsVisible : true,  
                 CursorToolsVisible : true,  
                 SearchToolsVisible : true,  
                 jsDirectory : '${cxt}/static/flexpaper/',  
                 JSONDataType : 'jsonp',  
                 key : '',  
                 WMode : 'window',  
                 localeChain: 'zh_CN'
            }  
        });  
        
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

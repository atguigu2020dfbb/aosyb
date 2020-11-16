<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="/WEB-INF/tld/aos.tld" prefix="aos"%>
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
	<aos:viewport layout="border">
	<aos:panel id="documentViewer" width="800"  region="west" >
		</aos:panel>
		<aos:gridpanel id="_g_account" url="listWsdaExifs.jhtml" region="center" enableLocking="true"  onrender="_g_account_query">
			<aos:docked>
				<aos:dockeditem xtype="tbtext" text="元数据信息" />
				<aos:dockeditem xtype="tbfill" />
				<aos:dockeditem text="导入" id="importExcel" icon="folder6.png" onclick="_w_import_show" />
				<aos:triggerfield id="tablename"  value="${tablename }"
					style="display:'none'" />
					<aos:triggerfield id="pid"  value="${id }" style="display:'none'" />
			</aos:docked>
			<aos:column type="rowno" />
			<aos:column header="流水号" dataIndex="id_" hidden="true" />
			<aos:column header="图像宽度" dataIndex="intw" width="80" minWidth="80" maxWidth="80"  />
			<aos:column header="设备型号" dataIndex="inth"  width="60" />
			<aos:column header="分辨率" dataIndex="dpi" width="140" />
			<aos:column header="文件大小" dataIndex="fsize" width="80" />
			<aos:column header="纸张大小" dataIndex="paper"  />
			<aos:column header="色彩空间" dataIndex="sckj"  />
			<aos:column header="压缩率" dataIndex="ysl" width="60" />
			<aos:column header="设备类型" dataIndex="sblx" />
			<aos:column header="设备制造商" dataIndex="sbzzs" width="60" />
			<aos:column header="设备型号" dataIndex="sbxh" width="160" />
			<aos:column header="设备系列号" dataIndex="sbxlh" width="180"/>
			<aos:column header="设备感光器" dataIndex="sbggq" width="180"/>
			<aos:column header="数字化软件名称" dataIndex="szhrjmc" width="180"/>
			<aos:column header="数字化软件版本" dataIndex="szhrjbb" width="180"/>
			<aos:column header="数字化软件生产商" dataIndex="szhrjscs" width="500" flex="0"/>
		</aos:gridpanel>
	</aos:viewport>

　　<script type="text/javascript">  

        $('#documentViewer').FlexPaperViewer(
        //'static/flexpaper/FlexPaperViewer.swf',
        {  
             config : {  
                 //SwfFile : 'temp/page.swf',//要转换的成pdf的swf文件  
                 SwfFile : '{${cxt}/temp/Paper[*,0].swf,31}',
                 
                 //SwfFile:'${cxt}/temp/Paper.swf',
                 Scale : 0.6,   
                 ZoomTransition : 'easeOut',  
                 ZoomTime : 0.5,   
                 ZoomInterval : 0.1,  
                 FitPageOnLoad : true,  
                 FitWidthOnLoad : false,   
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
 //                localeChain: 'en_US'
                 localeChain: 'zh_CN',
            }  
        });  
        
        $('#documentViewer').bind('onCurrentPageChanged', function (e, totalPages) {
		var aa=e;
		var totalPages=totalPages;
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
		function _w_import_show(){
		$FlexPaper("documentViewer").getCurrPage('2');
		//$('#documentViewer').bind('onCurrentPageChanged', function (e, totalPages) {
		//$FlexPaper("documentViewer").loadSwf('${cxt}/temp/Paper41.swf');
		
		
		//alert(e);
                   // $.messager.progress('close');
            //    });
		
		//var bb=$FlexPaper("documentViewer").loadSwf('${cxt}/temp/Paper40.swf');
		
		}
    </script>  
</aos:onready>
</aos:html>

<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="/WEB-INF/tld/aos.tld" prefix="aos"%>
<aos:html>
<aos:head title="档案统计">
	<aos:include lib="ext" />
	<aos:base href="archive/statistics" />
</aos:head>
<aos:body>
</aos:body>
<aos:onready>
	<aos:viewport layout="fit">
		<aos:gridpanel id="_g_statistics" url="liststatistics.jhtml"
			onrender="_g_statistics_query">
			<aos:docked>
				<aos:dockeditem xtype="tbtext" text="档案统计分析" />
				<aos:dockeditem xtype="tbseparator" />
				<aos:dockeditem text="导出Excel" icon="more/document-export-4.png"
					onclick="fn_export_statistics" />
			</aos:docked>
			<aos:column type="rowno" />
			<aos:column header="流水号" dataIndex="id" hidden="true" align="center" />
			<aos:column header="档案门类" dataIndex="category" width="60"
				align="center" />
			<aos:column header="案卷数" dataIndex="ajs" width="60"
				align="center" />
			<aos:column header="文件数" dataIndex="wjs" width="60"
				align="center" />
			<aos:column header="案卷页数" dataIndex="ajys" width="60"
				align="center" />
		</aos:gridpanel>
	</aos:viewport>
	<script type="text/javascript">
		function _g_statistics_query(){
			_g_statistics_store.load();
		}
		//导出日志
		function fn_export_statistics(){
			AOS.ajax({
				url : 'fillReport.jhtml',
				ok : function(data) {
					AOS.file('${cxt}/report/xls2.jhtml');
				}
			});
		}
	</script>
</aos:onready>
</aos:html>
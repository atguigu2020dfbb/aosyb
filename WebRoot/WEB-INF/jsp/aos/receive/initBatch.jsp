<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="/WEB-INF/tld/aos.tld" prefix="aos"%>
<aos:html>
<aos:head title="档案统计">
	<aos:include lib="ext" />
	<aos:base href="receive/batch" />
</aos:head>
<aos:body>
</aos:body>
<aos:onready>
	<aos:viewport layout="fit">
		<aos:gridpanel id="_g_data" url="listAccounts.jhtml"
			onrender="_g_data_query">
			<aos:column type="rowno" />
			<aos:column header="表名称" dataIndex="tablename" hidden="true" />
			<aos:column header="档案类别" dataIndex="tabledesc" width="60"
				align="center" />
			<aos:column header="目录数" dataIndex="mls" width="60"
				align="center" />
			<aos:column header="已挂接" dataIndex="ygj" width="60"
				align="center" />
			<aos:column header="未挂接" dataIndex="wgj" width="60"
				align="center" />
			<aos:column header="操作" width="60" align="center" rendererFn="fn_button_render"/>
		</aos:gridpanel>
	</aos:viewport>
	<script type="text/javascript">
	function _g_data_query(){
			_g_data_store.load();
		}
	function fn_button_render(){
	return '<input type="button" value="接收" class="cbtn" onclick="_w_receive_show(this);" />';
	
	}
	</script>
</aos:onready>
<script type="text/javascript">
	function _w_receive_show(a){
		var record=Ext.getCmp('_g_data').getSelectionModel().getSelection();
		var tablename=record[0].data.tablename;
		window.parent.fnaddtab('','档案接收','receive/batch/initReceive.jhtml?tablename='+tablename);
	}
</script>
</aos:html>
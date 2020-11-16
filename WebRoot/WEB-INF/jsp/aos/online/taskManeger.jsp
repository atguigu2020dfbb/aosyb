<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="/WEB-INF/tld/aos.tld" prefix="aos"%>
<aos:html>
<aos:head title="任务列表">
	<aos:include lib="ext" />
	<aos:base href="online/leave" />
</aos:head>
<aos:body>

</aos:body>

<aos:onready>
	<aos:viewport layout="border">
		<aos:gridpanel id="_g_task" region="center"
			url="findMyPersonTask.jhtml" onrender="_g_task_query">
			<aos:column header="主键" dataIndex="id_" />
			<aos:column header="任务名称" dataIndex="name"  />
			<aos:column header="创建时间"  dataIndex ="createTime"/>
			<aos:column header="任务代理人" dataIndex="assignee" />
			<aos:column header ="操作" rendererFn="fn_button_render"/>
		</aos:gridpanel>

	</aos:viewport>
	<script type="text/javascript">
		function _g_task_query() {

			_g_task_store.load();

		}
		function fn_button_render(){
		
			return '<input type="button" class="cbtn" value="待办任务" onclick="fn_column_click();" />';
			
		}
	</script>
</aos:onready>
<script type="text/javascript">

function fn_column_click(){
	var record = AOS.selectone(Ext.getCmp('_g_task'));
	AOS.ajax({
		url:'completeMyTask.jhtml',
		params:{
			task_id:record.data.id_
		}
	})

}

</script>
</aos:html>
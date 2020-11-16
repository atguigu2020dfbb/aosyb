<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="/WEB-INF/tld/aos.tld" prefix="aos"%>
<aos:html>
<aos:head title="归档审批">
	<aos:include lib="ext" />
		<aos:base href="preprocessing/application" />
</aos:head>
<aos:body>
</aos:body>
<aos:onready>
<aos:viewport layout="fit">
		<aos:gridpanel id="_g_data" url="listPersonTask.jhtml"
			onrender="_g_data_query" enableLocking="true" >
			<aos:column dataIndex="taskid" header="任务ID" hidden="true"/>
			<aos:column dataIndex="formid" header="表单ID" hidden="true"/>
			<aos:column dataIndex="sqr" header="申请人"/>
			<aos:column dataIndex="name" header="任务名称" />
			<aos:column header="办理任务" rendererFn="fn_task_render" align="center" width="200"  />
		</aos:gridpanel>
	</aos:viewport>
<script type="text/javascript">

function _g_data_query(){

_g_data_store.load();
}
//按钮列转换
		function fn_task_render(value, metaData, record, rowIndex, colIndex,
				store) {
			return '<input type="button" value="办理任务" class="cbtn"  onclick="fn_task_column_onclick();"/>';
		}
		function fn_state_render(value, metaData, record, rowIndex, colIndex,
				store) {
		return '<input type="button" value="111" class="cbtn"   />';
		
		}
		
function fn_button_render2(value, metaData, record, rowIndex, colIndex,
				store){

return '<input type="button" value="办理测试" class="cbtn"  onclick="fn_task_column2_onclick();"/>';
}
</script>
</aos:onready>
<script type="text/javascript">
function fn_task_column_onclick(){
	var record = AOS.selectone(Ext.getCmp('_g_data'));
	window.parent.fnaddtab('','办理任务','/preprocessing/application/setTaskManager.jhtml?taskid='+record.data.taskid);
}
function fn_task_column2_onclick(){
var record = AOS.selectone(Ext.getCmp('_g_data'));
	AOS.ajax({
		url:'finish.jhtml',
		params:{
			taskid:record.data.id_
			
		}
	
	})
	

}
</script>
</aos:html>
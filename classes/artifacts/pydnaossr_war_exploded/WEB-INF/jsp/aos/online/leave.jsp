<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="/WEB-INF/tld/aos.tld" prefix="aos"%>
<aos:html>
<aos:head title="请假管理">
	<aos:include lib="ext" />
	<aos:base href="online/leave" />
</aos:head>

<aos:body>
</aos:body>

<aos:onready>
	<aos:viewport layout="border">
		<aos:gridpanel id="_g_leave" region="center" url="listAccounts.jhtml"
			onrender="_g_leave_query">
			<aos:docked>
				<aos:dockeditem text="新增" icon="add2.png" onclick="_w_leave_show"/>
			</aos:docked>
			
			<aos:column header="主键" dataIndex="id_" />
			<aos:column header="标题" dataIndex="title" />
			<aos:column header="开始时间" dataIndex="start_time" />
			<aos:column header="结束时间" dataIndex="end_time" />
			<aos:column header="请假天数" dataIndex="day" />
			<aos:column header="请假理由" dataIndex="reason" />
			<aos:column header="状态" dataIndex="state"
				rendererFn="fn_state_render" />
			<aos:column header="操作" type="action" dataIndex="state">
				<aos:action icon="edit.png" tooltip="编辑" />
				<aos:action icon="del.png" tooltip="删除" />
			</aos:column>
			<aos:column header="提交" rendererFn="fn_button_render" align="center" />

		</aos:gridpanel>
		<aos:window id="_w_leave" title="添加请假单">
			<aos:formpanel id="_f_leave" layout="anchor">
				<aos:textfield name="title" fieldLabel="标题"/>
				<aos:textfield name="start_time" fieldLabel="开始时间"/>
				<aos:textfield name="end_time" fieldLabel="结束时间"/>
				<aos:textfield name="day" fieldLabel="请假天数"/>
				<aos:textfield name="reason" fieldLabel="请假理由"/>
			<aos:docked dock="bottom">
				<aos:dockeditem text="保存" icon="save.png" onclick="_f_leave_save()"/>
				<aos:dockeditem text="关闭" icon="close.png" onclick="#_w_leave.hide();" />
			</aos:docked>
			</aos:formpanel>
		</aos:window>
	</aos:viewport>
	<script type="text/javascript">
		function _g_leave_query() {
			_g_leave_store.load();
		}
		function fn_state_render(value,metaData,record,rowIndex,colIndex,store){
		
		if(value=='0'){
			return "未提交";
		}else if(value=='1'){
			return "审核中";
		}
		
		}
		function fn_button_render(value,mdtaData,rowIndex,colIndex,store){
		
			return '<input type="button" value="提交" class="cbtn" onclick="fn_column_onclick()">';
		}
		function _w_leave_show(){
			_w_leave.show();
		
		}
		function _f_leave_save(){
			AOS.ajax({
				forms:_f_leave,
				url:'saveLeave.jhtml',
				ok:function(data){
						_f_leave.hide();
						_g_leave.load();
						AOS.tip(data.appmsg);
				}
			
			})
			
		}
	</script>

</aos:onready>
<script type="text/javascript">

function fn_column_onclick(obj){
	var record = AOS.selectone(Ext.getCmp('_g_leave'));
	
	var msg=AOS.merge("您将要提交单据，并且启动流程您将无法进行修改和删除！！！点击确认进行提交，点击取消取消该操作.");
	AOS.confirm(msg,function(btn){
		if(btn === 'cancel'){
			AOS.tip('操作被取消。');
			return;
		}
		AOS.ajax({
			url:'startProcessInstance.jhtml',
			params:{
				id_:record.data.id_},
			//ok:function(data){
			//	AOS.tip(data.appmsg);
			//	_g_leave_store.reload();
			//}
		
		});
	});
}
</script>
</aos:html>
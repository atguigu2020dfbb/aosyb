<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="/WEB-INF/tld/aos.tld" prefix="aos"%>
<aos:html>
<aos:head title="申请列表">
	<aos:include lib="ext" />
	<aos:base href="preprocessing/application" />
</aos:head>
<aos:body>
</aos:body>
<aos:onready>
	<aos:viewport layout="fit">
		<aos:gridpanel id="_g_data" url="listAccounts.jhtml" onrender="_g_data_query" enableLocking="true" >
			<aos:docked>
				<aos:dockeditem text="新增" icon="add2.png" onclick="fn_add" />
			</aos:docked>
			
			<aos:column dataIndex="id_" header="主键" hidden="true"/>
			<aos:column dataIndex="proc_ins_id" header="任务实例ID" hidden="true"/>
			<aos:column header="申请人"  dataIndex="xm" />
			<aos:column dataIndex="sqrq" header="申请日期" />
			<aos:column dataIndex="state" rendererFn="fn_state_render" header="状态" />
			<aos:column header="提交" dataIndex="state" rendererFn="fn_button_render" width="200"/>
		</aos:gridpanel>
		<aos:window id="_w_history" onshow="_w_history_onshow" title="单据流转历史">
			<aos:gridpanel id="_g_history" url="getFlowHistory.jhtml" enableLocking="true"  height="-200" width="600" layout="fit" >
				<aos:column dataIndex="taskName" header="任务名称" />
				<aos:column dataIndex="assignee" header="办理人名称" />
				<aos:column dataIndex="message" header="办理意见" width="260"/>
				<aos:column dataIndex="endTime" header="办理时间" width="150" />
			</aos:gridpanel>
		
		
		</aos:window>
	</aos:viewport>
	<script type="text/javascript">
		function _g_data_query(){
		
			_g_data_store.load();
		
		}
		
		//按钮列转换
		function fn_button_render(value, metaData, record, rowIndex, colIndex,
				store) {
				if(value === 0){
					return '<input type="button" value="提交" class="cbtn" onclick="fn_column_sub_onclick();" />&nbsp&nbsp'
						+'<input type="button" value="查看" class="cbtn_warn" onclick="fn_column_check_onclick();" />&nbsp&nbsp'
						+'<input type="button" value="编辑" class="cbtn_warn" onclick="fn_column_edit_onclick();" />&nbsp&nbsp'
						+'<input type="button" value="删除" class="cbtn_danger" onclick="fn_column_del_onclick();" />&nbsp&nbsp';
				}if(value === 1){
					return '<input type="button" value="流转" class="cbtn" onclick="fn_history_onclick();" />&nbsp&nbsp'
						+'<input type="button" value="流程" class="cbtn" onclick="fn_column_proc_onclick();" />&nbsp&nbsp'
						+'<input type="button" value="查看" class="cbtn_warn" onclick="fn_column_check_onclick();" />&nbsp&nbsp'
						+'<input type="button" value="辙回" class="cbtn_danger" onclick="fn_column_callBack_onclick();" />&nbsp&nbsp';
				}if(value === 2){
					return '<input type="button" value="流转" class="cbtn" onclick="fn_history_onclick();" />&nbsp&nbsp'
						+'<input type="button" value="流程" class="cbtn" onclick="fn_column_proc_onclick();" />&nbsp&nbsp'
						+'<input type="button" value="查看" class="cbtn_warn" onclick="fn_column_check_onclick();" />&nbsp&nbsp';
				}
		}

//按钮列转换
function fn_button_render2(value, metaData, record, rowIndex, colIndex,
				store) {
	return '<input type="button" value="流程跟踪" class="cbtn_warn" onclick="fn_column_button_onclick();" />';
}

		//按钮列转换
		function fn_button_render3(value, metaData, record, rowIndex, colIndex,
				store) {
			return '<input type="button" value="删除" class="cbtn_danger" onclick="fn_column_button_onclick();" />';
		}
		function fn_add(){
			
			parent.fnaddtab('4d3cfce7b9b146d2bc84','申请登记','preprocessing/application/initApplication.jhtml');
		}
		
		function fn_state_render(value, metaData, record, rowIndex, colIndex,
				store){
			if(value===0){
			//return '<input type="button" value="未提交" class="cbtn_danger"  />';
			return '未提交';
			}if(value === 1) {
			return '审核中';
			}else{
			return '已完成';
			}
			
		}
	</script>
</aos:onready>
<script type="text/javascript">
function fn_column_sub_onclick(){
	var record = AOS.selectone(Ext.getCmp('_g_data'));
	var msg = AOS.merge('您将要提交该单据！并且启动流程您将无法进行修改和删除！！！ 点击确认进行提交 , 点击取消取消该操作');
	AOS.confirm(msg,function (btn){
		if(btn === 'cancel'){
			AOS.tip('操作取消');
			return;
		}
		AOS.ajax({
			url:'startInstance.jhtml',
			params:{
				id_:record.data.id_,
				state:1
			},
			ok:function(data){
			Ext.getCmp('_g_data').store.load();
			AOS.tip(data.appmsg);
			}
		})
	})
}
function fn_column_del_onclick(){
	var record = AOS.selectone(Ext.getCmp('_g_data'));
	var msg = AOS.merge('您是否要删除所选文件信息？点击确认进行删除，点击取消取消该操作');
	AOS.confirm(msg,function(btn){
		if(btn === 'cancel'){
			AOS.tip('操作取消。');
			return;
		}
		AOS.ajax({
			url:'deleteForm.jhtml',
			params:{
				id_:record.data.id_
			},
			ok:function (data){
				Ext.getCmp('_g_data').store.load();
				AOS.tip(data.appmsg);
			
			}
		})
	
	})

}

function fn_history_onclick(){
	Ext.getCmp('_w_history').show();

}
function _w_history_onshow(){
	var data=Ext.getCmp('_g_history');
	var record = AOS.selectone(Ext.getCmp('_g_data'),true);
	data.getStore().getProxy().extraParams={
			proc_ins_id:record.data.proc_ins_id,
	}
	data.getStore().load();
}
function fn_column_callBack_onclick(){
	var record = AOS.selectone(Ext.getCmp('_g_data'));
	var msg = AOS.merge('您将要撤回该单据流程！<p>该操作将取回单据，您将可以对单据进行修改，并可将单据重新送交审批。</p>点击确认进行撤回单据 , 点击取消取消该操作');
	AOS.confirm(msg,function(btn){
		if(btn==='cancel'){
			AOS.tip('操作取消！！！');
			return;
		}
		AOS.ajax({
			url:'callBack.jhtml',
			params:{
				id_:record.data.id_,
				proc_ins_id:record.data.proc_ins_id,
				state:0,
			},
			ok:function(data){
				Ext.getCmp('_g_data').getStore().load();
				AOS.tip(data.appmsg);
			
			}
		})
	})
	
}
function fn_column_check_onclick(){
	var record = AOS.selectone(Ext.getCmp('_g_data'));
	parent.fnaddtab('4d3cfce7b9b146d2bc84','查看登记','preprocessing/application/applicationList.jhtml?id='+record.data.id_+'&taskid='+record.data.proc_ins_id,);

}
function fn_column_edit_onclick(){
var record = AOS.selectone(Ext.getCmp('_g_data'));
	parent.fnaddtab('4d3cfce7b9b146d2bc84','编辑登记','preprocessing/application/applicationEdit.jhtml?id='+record.data.id_+'&taskid='+record.data.proc_ins_id,);


}
function refreshGrid(){
		Ext.getCmp('_g_data').store.load();
		
}
</script>
</aos:html>
<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="/WEB-INF/tld/aos.tld" prefix="aos"%>
<aos:html>
<aos:head title="模型管理">
	<aos:include lib="ext" />
	<aos:base href="workflow/modeler" />
</aos:head>
<aos:body>
</aos:body>
<aos:onready>
	<aos:viewport layout="fit">
		<aos:gridpanel id="_g_modeler" url="listModelers.jhtml" onrender="_g_modeler_query" >
		<aos:docked forceBoder="0 0 1 0">
				<aos:dockeditem text="流程列表" xtype="tbtext" />
				<aos:dockeditem xtype="tbseparator" />
				<aos:dockeditem text="添加"  onclick="_w_modeler_show" icon="folder6.png" />
				<aos:triggerfield emptyText="流程名称" name="name_" id="name_" onenterkey="" trigger1Cls="x-form-search-trigger"
					 width="200" />
			</aos:docked>
			<aos:column type="rowno" />
			<aos:column header="id" dataIndex="id_" hidden="true" />
			<aos:column header="标识" dataIndex="key_" />
			<aos:column header="名称" dataIndex="name_" />
			<aos:column header="版本" dataIndex="version_"  />
			<aos:column header="创建时间" dataIndex="create_time_" />
			<aos:column header="更新时间" dataIndex="last_update_time_" />
			<aos:column header="操作" type="action" dataIndex="" width="150"  >
				<aos:action handler="fn_edit"  icon="edit.png" tooltip="编辑" />
				<aos:action  handler="fn_dep" icon="go.png" tooltip="部署" />
				<aos:action handler="fn_del" icon="del.png" tooltip="删除" />
			</aos:column>
		</aos:gridpanel>
	</aos:viewport>
<aos:window id="_w_modeler" title="新增流程">
		<aos:formpanel id="_f_modeler" width="500" layout="anchor" labelWidth="100">
		<aos:textfield name="key_" fieldLabel="模型唯一标识" allowBlank="false" maxLength="100" />
		<aos:textfield name="name_" fieldLabel="模型名称" allowBlank="false" maxLength="50" />
			
		</aos:formpanel>
		<aos:docked dock="bottom" ui="footer">
			<aos:dockeditem xtype="tbfill" />
			<aos:dockeditem onclick="_f_modeler_save" text="保存" icon="ok.png" />
			<aos:dockeditem onclick="#_w_modeler.hide();" text="关闭" icon="close.png" />
		</aos:docked>
	</aos:window>
	<script type="text/javascript">
		//查询流程任务列表
		function _g_modeler_query() {
			//var params = {
			//	name_ : AOS.getValue('name_')
			//};
			//_g_modeler_store.getProxy().extraParams = params;
			_g_modeler_store.load();
		}
		function _w_modeler_show(){
		
		_w_modeler.show();
		}
		function _f_modeler_save(){
		AOS.ajax({
                    forms: _f_modeler,
                    url: 'crateModeler.jhtml',
                    ok: function (data) {
                    	if(data.appcode !== -1){
                            _w_modeler.hide();
                            _g_modeler_store.reload();
                    	}
                        AOS.tip(data.appmsg);
                    }
                });
		}
		function fn_edit(grid, rowIndex, colIndex){
		//a = '<a class="label label-table label-success" href="#(ctx)/processEditor/modeler.html?modelId='+row.ID_+'" target="_blank">编辑</a>&nbsp;';
		var rec = grid.getStore().getAt(rowIndex);
		//	AOS.tip("可以获取当前行的任意数据传给后台: " + "[卡号：" + rec.get('card_id_') + "]");
		window.parent.fnaddtab('','流程设计','static/processEditor/modeler.html?modelId='+rec.get('id_'));
		//window.parent.fnaddtab('','设置录入','static/processEditor/modeler.html?modelId=66');
		//window.parent.fnaddtab('','设置录入','/workflow/modeler/create.jhtml');
		}
		function fn_del(){
		
		//var record = AOS.selectone(Ext.getCmp('_g_north'));
		var record = AOS.selectone(_g_modeler);
		//alert(record.data.id_);
		//return;
		var msg=AOS.merge('确定要删除选中的[{0}]数据吗?',record.data.key_);
		AOS.confirm(msg,function (btn){
			if(btn === 'cancel'){
			 AOS.tip('删除操作被取消。');
			 return;
			}
			AOS.ajax({
				url:'delModeler.jhtml',
				params:{id:record.data.id_},
				ok:function(data){
					AOS.tip(data.appmsg);
                    _g_modeler_store.reload();
				}
				
			})
		
		});
		
		}
		function fn_dep(grid, rowIndex, colIndex){
		var record = grid.getStore().getAt(rowIndex);
			AOS.ajax({
				url:'deployModeler.jhtml',
				params:{id:record.data.id_},
				ok:function(data){
					AOS.tip(data.appmsg);
				}
			})
		
		}
	</script>
</aos:onready>
</aos:html>
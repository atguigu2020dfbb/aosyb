<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="/WEB-INF/tld/aos.tld" prefix="aos"%>
<aos:html>
<aos:head title="全宗定义">
	<aos:include lib="ext" />
	<aos:base href="general/general" />
</aos:head>
<aos:body>
</aos:body>
<aos:onready>
	<aos:viewport layout="fit">
		<aos:gridpanel id="_g_general" url="listgenerals.jhtml"
			onrender="_g_general_query" onitemdblclick="_w_general_update">
			<aos:docked>
				<aos:dockeditem xtype="tbtext" text="全宗信息表" />
				<aos:dockeditem xtype="tbseparator" />
				<aos:dockeditem text="新增" icon="folder15.png" onclick="_w_general_add" />
				<aos:dockeditem text="修改" icon="folder15.png" onclick="_w_general_update" />
				<aos:dockeditem text="删除" icon="del.png" onclick="_w_general_del" />
				<aos:dockeditem text="导出全宗" icon="more/document-export-4.png"
					onclick="fn_export_general" />
			</aos:docked>
			<aos:column type="rowno" />
			<aos:column header="流水号" dataIndex="id_" hidden="true" align="center" />
			<aos:column header="全宗编号" dataIndex="general_number" width="60"
				align="center" />
			<aos:column header="全宗名称" dataIndex="general_name" width="60"
				align="center" />
			<aos:column header="创建人" dataIndex="create_person" width="60"
				align="center" />
			<aos:column header="创建时间" dataIndex="create_time" width="60"
				align="center" />
		</aos:gridpanel>
		<aos:window id="_w_add" title="新增">
			<aos:formpanel id="_f_add" width="400" layout="anchor"
						   labelWidth="70" >
				<aos:textfield name="general_number" fieldLabel="全宗编号"/>
				<aos:textfield name="general_name" fieldLabel="全宗名称" />
				<aos:textfield name="create_person" fieldLabel="创建人" />
				<aos:datefield name="create_time" fieldLabel="创建时间" format="Y-m-d"
							   columnWidth="1.0" allowBlank="false" />
			</aos:formpanel>
			<aos:docked dock="bottom" ui="footer">
				<aos:dockeditem xtype="tbfill" />
				<aos:dockeditem onclick="_w_add_reset" text="新增" icon="add.png" />
				<aos:dockeditem onclick="#_w_add.hide();" text="关闭" icon="close.png" />
			</aos:docked>
		</aos:window>
		<aos:window id="_w_update" title="修改" onshow="_w_update_show">
			<aos:formpanel id="_f_update" width="400" layout="anchor"
						   labelWidth="70" >
				<aos:hiddenfield name="id_" />
				<aos:textfield name="general_number" fieldLabel="全宗编号"/>
				<aos:textfield name="general_name" fieldLabel="全宗名称" />
				<aos:textfield name="create_person" fieldLabel="创建人" />
				<aos:datefield name="create_time" fieldLabel="创建时间" format="Y-m-d"
							   columnWidth="1.0" allowBlank="false" />
			</aos:formpanel>
			<aos:docked dock="bottom" ui="footer">
				<aos:dockeditem xtype="tbfill" />
				<aos:dockeditem onclick="_w_update_reset" text="保存" icon="save.png" />
				<aos:dockeditem onclick="#_w_update.hide();" text="关闭" icon="close.png" />
			</aos:docked>
		</aos:window>
	</aos:viewport>
	<script type="text/javascript">
		function _g_log_query(){
			var params = {
					party : party.getValue()
				};
				//这个Store的命名规则为：表格ID+"_store"。
				_g_log_store.getProxy().extraParams = params;
				_g_log_store.load();
		}
		//弹出窗口加载
		function _w_log_onshow(){
			var record = AOS.selectone(_g_log, true);
            AOS.ajax({
            	params : {
            		id: record.data.id
            	},
                url: 'getlogInfo.jhtml',
                ok: function (data) {
                	_f_log.form.setValues(data);
                }
            });
		}

		//导出日志
		function fn_export_general(){
			AOS.ajax({
				url : 'fillReport.jhtml',
				ok : function(data) {
					AOS.file('${cxt}/report/xls2.jhtml');
				}
			});
		}
		//加载列表
		function _g_general_query(){
			_g_general_store.reload();
		}
		function _w_general_add(){

			_w_add.show();
			_f_add.getForm().findField('create_person').setValue("<%=session.getAttribute("user")%>");
			_f_add.getForm().findField('create_time').setValue(getNowFormatDate());
		}
		function getNowFormatDate() {
			var date = new Date();
			var seperator1 = "-";
			var year = date.getFullYear();
			var month = date.getMonth() + 1;
			var strDate = date.getDate();
			if (month >= 1 && month <= 9) {
				month = "0" + month;
			}
			if (strDate >= 0 && strDate <= 9) {
				strDate = "0" + strDate;
			}
			var currentdate = year + seperator1 + month + seperator1 + strDate;
			return currentdate;
		}
		function _w_add_reset(){
			AOS.ajax({
				forms:_f_add,
				url: 'addgeneral.jhtml',
				ok: function (data) {
					if(data.appcode===1){
						_w_add.hide();
						_g_general_store.reload();
						AOS.tip("添加成功");
					}else{
						AOS.tip("添加失败");
					}
				}
			});
		}
		function _w_general_update(){
			var record=AOS.selectone(_g_general);
			if(AOS.empty(record)){
				AOS.err("请选择要修改的全宗信息");
				return;
			}
			_w_update.show();
		}
		function _w_update_show(){
			//数据加载过去
			var record=AOS.selectone(_g_general);
			_f_update.loadRecord(record);
		}
		function _w_update_reset(){
			AOS.ajax({
				forms:_f_update,
				url: 'updategeneral.jhtml',
				ok: function (data) {
					if(data.appcode===1){
						_w_update.hide();
						AOS.tip("修改成功");
						_g_general_store.reload();
					}else{
						AOS.tip("修改失败");
					}
				}
			});
		}
		function _w_general_del(){
			var record = AOS.selectone(_g_general, true);
			var msg = AOS.merge('确认要删除选中的[{0}]个全宗数据吗？', AOS.rows(_g_general));
			if (AOS.empty(record)) {
				AOS.tip('删除前请先选中数据!');
				return;
			}else{
				AOS.confirm(msg, function (btn) {
					if (btn === 'cancel') {
						AOS.tip('删除操作被取消。');
						return;
					}
					AOS.ajax({
						params : {
							id_: record.data.id_
						},
						url: 'delgeneral.jhtml',
						ok: function (data) {
							if (data.appcode === 1) {
								_w_update.hide();
								AOS.tip("删除成功");
								_g_general_store.reload();
							} else {
								AOS.tip("删除失败");
							}
						}
					});
				});
			}
		}

	</script>
</aos:onready>
<script type="text/javascript">
	//显示详情1窗口
	function _w_show(){
		Ext.getCmp('_w_log').show();
	}
</script>
</aos:html>
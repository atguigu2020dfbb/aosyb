<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="/WEB-INF/tld/aos.tld" prefix="aos"%>
<aos:html>
<aos:head title="表格①">
	<aos:include lib="ext" />
	<aos:base href="archive/log" />
</aos:head>
<aos:body>
</aos:body>
<aos:onready>
	<aos:viewport layout="fit">
		<aos:gridpanel id="_g_log" url="listlogs.jhtml"
			onrender="_g_log_query" onitemdblclick="_w_show">
			<aos:docked>
				<aos:dockeditem xtype="tbtext" text="日志信息表" />
				<aos:dockeditem xtype="tbseparator" />
				<aos:dockeditem text="查看" icon="folder15.png" onclick="_w_show" />
				<aos:dockeditem text="删除" icon="del.png" onclick="fn_remove_rows" />
				<aos:dockeditem text="清空日志" icon="del2.png"
					onclick="fn_removeall_rows" />
				<aos:dockeditem text="导出日志" icon="more/document-export-4.png"
					onclick="fn_export_log" />
				<aos:triggerfield emptyText="用户名称" id="party"
					onenterkey="_g_log_query" trigger1Cls="x-form-search-trigger"
					onTrigger1Click="_g_log_query" width="200" />
			</aos:docked>
			<aos:column type="rowno" />
			<aos:column header="流水号" dataIndex="id" hidden="true" align="center" />
			<aos:column header="用户名称" dataIndex="party" width="60"
				align="center" />
			<aos:column header="类别" dataIndex="category" width="60"
				align="center" />
			<aos:column header="标题" dataIndex="title" width="60"
				align="center" />
			<aos:column header="利用" dataIndex="take" width="60"
				align="center" />
			<aos:column header="ip地址" dataIndex="ip_address" width="60"
				align="center" />
			<aos:column header="时间" dataIndex="create_time" width="60"
				align="center" />
			<aos:column header="详情1" rendererFn="fn_button_render" align="center"
				width="50" minWidth="50" maxWidth="50" />
		</aos:gridpanel>
		<aos:window id="_w_log" title="详情1[实时再查询一次]" onshow="_w_log_onshow">
			<aos:formpanel id="_f_log" width="400" layout="anchor"
				labelWidth="70">
				<aos:textfield name="party" fieldLabel="用户名称" readOnly="true" />
				<aos:textfield name="category" fieldLabel="类别" readOnly="true" />
				<aos:textfield name="title" fieldLabel="标题" readOnly="true" />
				<aos:textfield name="take" fieldLabel="利用" readOnly="true" />
				<aos:textfield name="ip_address" fieldLabel="ip地址" readOnly="true" />
				<aos:textfield name="create_time" fieldLabel="时间" readOnly="true" />
			</aos:formpanel>
			<aos:docked dock="bottom" ui="footer">
				<aos:dockeditem xtype="tbfill" />
				<aos:dockeditem onclick="#_w_log.hide();" text="关闭" icon="close.png" />
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
		//按钮列转换
		function fn_button_render(value, metaData, record, rowIndex, colIndex,
				store) {
			return '<input type="button" value="详情1" class="cbtn" onclick="_w_show();" />';
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
		//删除单条记录
		function fn_remove_rows(){
			var record = AOS.selectone(_g_log, true);
			var msg = AOS.merge('确认要删除选中的[{0}]个日志数据吗？', AOS.rows(_g_log));
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
                		id: record.data.id
                	},
                    url: 'dellogInfo.jhtml',
                    ok: function (data) {
                    	 AOS.tip(data.appmsg);
                    	 _g_log_store.reload();
                    }
                });
               });
            }
		}
		//清空日志
		function fn_removeall_rows(){
			//如果没有数据，提示清空的日志不存在
			var count=_g_log_store.getCount();
			if(count===0){
				AOS.tip("要清空的日志不存在!");
				return;
			}
			var msg = AOS.merge('确认要清空[{0}]个日志数据吗？', count);
			
            	 AOS.confirm(msg, function (btn) {
                     if (btn === 'cancel') {
                         AOS.tip('清空操作被取消。');
                         return;
                     }
					//如果有数据走后台执行清空操作
                     AOS.ajax({
         				url:'delAlllogInfo.jhtml',
         				ok:function(data){
         					AOS.tip(data.appmsg);
                        		_g_log_store.reload();
         				}
         			});
         			});
			
		}
		//导出日志
		function fn_export_log(){
			AOS.ajax({
				url : 'fillReport.jhtml',
				ok : function(data) {
					AOS.file('${cxt}/report/xls2.jhtml');
				}
			});
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
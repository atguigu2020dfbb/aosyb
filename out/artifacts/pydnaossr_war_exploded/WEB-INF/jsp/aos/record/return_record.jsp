<%@ page contentType="text/html; charset=utf-8" isELIgnored="false"%>
<%@ taglib uri="/WEB-INF/tld/aos.tld" prefix="aos"%>
<%@ taglib uri="/WEB-INF/tld/cll.tld" prefix="c"%>
<%
	String path = request.getContextPath();
	Object fieldDtos = request.getAttribute("fieldDtos");
	Object listtablename = request.getAttribute("listtablename");
%>
<aos:html>
	<aos:head title="数据还原">
		<aos:include lib="ext,swfupload" />
		<aos:base href="archive/record" />
		<script type="text/javascript" src="<%=path %>/static/flexpaper/jquery.min.js"></script>
	</aos:head>
	<aos:body>
	</aos:body>
	<aos:onready>
		<aos:viewport layout="border">
			<aos:gridpanel id="_g_data" url="listreturn.jhtml" region="center" onrender="_w_return_show">
				<aos:docked forceBoder="0 0 1 0">
					<aos:dockeditem xtype="tbtext" text="还原记录" />
					<aos:hiddenfield id="localFilename" value="${localFilename }"/>
					<aos:dockeditem  id="restoreFile" text="还原数据库" icon="add.png" />
					<aos:dockeditem onclick="del_return" text="删除记录" icon="del.png" />
				</aos:docked>
				<aos:column type="rowno"  />
				<aos:selmodel type="row" mode="multi" />
				<aos:column dataIndex="id_" header="还原流水号" hidden="true" />
				<aos:column dataIndex="operator_time" header="操作时间" width="200"/>
				<aos:column dataIndex="operator_en_person" header="操作人(英文)" width="200"/>
				<aos:column dataIndex="operator_cn_person" header="操作人(中文)" width="200"/>
				<aos:column dataIndex="operator_name" header="操作名称" width="200"/>
				<aos:column header="" flex="1" />
			</aos:gridpanel>
		</aos:viewport>
		<script type="text/javascript">
			window.onload = function() {
				var btn = Ext.get('restoreFile-btnInnerEl');
				//var em = btn.getEl().childd('em');
				btn.setStyle({
					position : 'relative',
					display : 'block'
				});
				btn.createChild({
					tag : 'div',
					id : 'div_exam'
				});

				swfu = new SWFUpload({
					upload_url : "${cxt}/archive/upload/uploadRecord.jhtml", //接收上传的服务端url
					flash9_url : "${cxt}/static/swfupload/swfupload_f9.swf",
					flash_url : "${cxt}/static/swfupload/swfupload.swf",//swfupload压缩包解压后swfupload.swf的url
					button_placeholder_id : "div_exam",//上传按钮占位符的id
					//file_types : "*.xls;*.xlsx",
					file_types : "*",
					file_types_description : "数据库备份文件",
					//file_size_limit : "20480",//用户可以选择的文件大小，有效的单位有B、KB、MB、GB，若无单位默认为KB
					button_width : 40, //按钮宽度
					button_height : 20, //按钮高度
					button_text : "确定",//按钮文字
					file_dialog_complete_handler : file_complete_handler,
					file_queued_handler : file_queued_handler,
					upload_success_handler : upload_success_handler
				})
				Ext.get(swfu.movieName).setStyle({
					position : 'absolute',
					left : "20px",
					opacity : 0
				});

			}
			function _g_excel_account(){
				var params = {
					localFilename:localFilename.getValue()
				};
				//这个Store的命名规则为：表格ID+"_store"。
				_g_excel_store.getProxy().extraParams = params;
				_g_excel_store.load();
			}
			function file_queued_handler(file){
				localFilename.setValue(file.name);
			}
			//打开之后事件
			function file_complete_handler(file){
				//此时加入还原日志
				_g_data_store.reload();
				this.startUpload();
				//window.location.href="loadExcelGrid.jhtml?localFilename="+localFilename.getValue();


			}
			//上传完之后的事件
			function upload_success_handler() {
				//上传完之后开始针对这个路径里面的内容进行恢复数据库操作
				//window.location.href="loadRecordGrid.jhtml?localFilename="+localFilename.getValue();
			}
				//获取日志
			function _w_return_show(){
				_g_data_store.reload();
			}
			//删除造作
			function del_return(){
				var selection = AOS.selection(_g_data, 'id_');
				var tms = AOS.selection(_g_data, 'tm');
				if (AOS.empty(selection)) {
					AOS.tip('删除前请先选中数据。');
					return;
				}
				var msg = AOS.merge('确认要删除选中的[{0}]个操作记录吗？', AOS.rows(_g_data));
				AOS.confirm(msg, function (btn) {
					if (btn === 'cancel') {
						AOS.tip('删除操作被取消。');
						return;
					}
					AOS.ajax({
						url: 'deleteReturn.jhtml',
						params: {
							aos_rows_: selection
						},
						ok: function (data) {
							AOS.tip(data.appmsg);
							_g_data_store.reload();
						}
					});
				});
			}
			//上传完之后的事件
			function upload_success_handler(){
				//上传完之后开始针对这个路径里面的内容进行恢复数据库操作
				//window.location.href="loadRecordGrid.jhtml?localFilename="+localFilename.getValue();

				AOS.ajax({
					url : 'loadRecordGrid.jhtml',
					params:{
						localFilename:Ext.getCmp("localFilename").getValue()
					},
					ok : function(data) {
						if (data.appcode === 1) {
							AOS.tip("还原成功,请重新启动服务器！");
							return;
						}
						if (data.appcode === -1) {
							AOS.tip("还原失败");
							return;
						}
						if (data.appcode === -2) {
							AOS.tip("请在服务器机器上完成数据库的还原操作!");
							return;
						}
					}
				});

			}
		</script>
	</aos:onready>
</aos:html>
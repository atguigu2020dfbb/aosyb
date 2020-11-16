<%@ page contentType="text/html; charset=utf-8" isELIgnored="false"%>
<%@ taglib uri="/WEB-INF/tld/aos.tld" prefix="aos"%>
<%@ taglib uri="/WEB-INF/tld/cll.tld" prefix="c"%>
<%
	String path = request.getContextPath();
	Object fieldDtos = request.getAttribute("fieldDtos");
	Object listtablename = request.getAttribute("listtablename");
%>
<aos:html>
	<aos:head title="数据备份">
		<aos:include lib="ext" />
		<aos:base href="archive/record" />
		<script type="text/javascript" src="<%=path %>/static/flexpaper/jquery.min.js"></script>
	</aos:head>
	<aos:body>
	</aos:body>
	<aos:onready>
		<aos:viewport layout="border">
			<aos:gridpanel id="_g_data" url="listbackup.jhtml" region="center" onrender="_w_backup_show">
				<aos:docked forceBoder="0 0 1 0">
					<aos:dockeditem xtype="tbtext" text="备份记录" />
					<aos:dockeditem onclick="sjb_backup" text="备份数据库" icon="add.png" />
					<aos:dockeditem onclick="del_backup" text="删除记录" icon="del.png" />
				</aos:docked>
				<aos:column type="rowno"  />
				<aos:selmodel type="row" mode="multi" />
				<aos:column dataIndex="id_" header="备份流水号" hidden="true" />
				<aos:column dataIndex="operator_time" header="操作时间" width="200"/>
				<aos:column dataIndex="operator_en_person" header="操作人(英文)" width="200"/>
				<aos:column dataIndex="operator_cn_person" header="操作人(中文)" width="200"/>
				<aos:column dataIndex="operator_name" header="操作名称" width="200"/>
				<aos:column header="" flex="1" />
			</aos:gridpanel>
		</aos:viewport>
		<script type="text/javascript">
			function sjb_backup() {
				AOS.ajax({
					url: 'backupData.jhtml',
					ok: function (data) {
						if (data.appcode === 1) {
							AOS.file("download.jhtml?filepath=" + data.filepath + "&name=" + data.name + "");
							_g_data_store.reload();
						}
					}
				});
			}
			//获取日志
			function _w_backup_show(){
				_g_data_store.reload();
			}
			//删除造作
			function del_backup(){
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
						url: 'deleteBack.jhtml',
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
		</script>
	</aos:onready>
</aos:html>
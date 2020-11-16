<%@ page contentType="text/html; charset=utf-8" isELIgnored="false"%>
<%@ taglib uri="/WEB-INF/tld/aos.tld" prefix="aos"%>
<%@ taglib uri="/WEB-INF/tld/cll.tld" prefix="c"%>
<%
	String path = request.getContextPath();
%>
<aos:html>
	<aos:head title="问题列表">
		<aos:include lib="ext" />
		<aos:base href="utilization" />
	</aos:head>
	<aos:body>
	</aos:body>
	<aos:onready>
		<aos:viewport layout="border">
				<aos:gridpanel  id="_g_error_details" autoScroll="true" region="center" hidePagebar="true"  onrender="_load_error_details"
								url="listAccounts3.jhtml"  pageSize="60">
					<aos:docked>
						<aos:dockeditem xtype="tbtext" text="问题列表" />
						<aos:dockeditem text="已修改" icon="more/date-edit.png" onclick="_w_edit_error"/>
						<aos:dockeditem text="未修改" icon="more/edit-undo-2.png" onclick="_w_no_edit_error"/>
					</aos:docked>
					<aos:column type="rowno" />
					<aos:selmodel type="row" mode="multi" />
					<aos:column dataIndex="id_" header="流水号" hidden="true" align="center"/>
					<aos:column dataIndex="djbh" header="登记编号" width="200" align="center"/>
					<aos:column dataIndex="error_archive" header="错误门类" width="200" align="center"/>
					<aos:column dataIndex="register_time" header="登记时间" width="200" align="center"/>
					<aos:column dataIndex="dh" header="档号" width="200" align="center"/>
					<aos:column dataIndex="error_description" header="错误描述" width="400" align="center"/>
					<aos:column dataIndex="flag" header="状态" width="100" rendererFn="fn_error_render" align="center"/>
					<aos:column header="" flex="1" />
				</aos:gridpanel>
		</aos:viewport>
		<script type="text/javascript">
			function _load_error_details(){
				_g_error_details_store.load();
			}
			function _w_register_error(){
				var selection = AOS.selection(_g_tablename_details, 'id_');
				if (AOS.empty(selection)) {
					AOS.tip('请选择数据!');
					return;
				}
				_w_error_register.show();
			}
			function _f_error_register_save(){
				AOS.ajax({
					url: 'saveErrorRegister.jhtml',
					params:{djbh:djbh.getValue()},
					ok: function (data) {
						if(data.appcode===1){
							AOS.tip("添加成功!");
							_w_error_register.hide();
							var parenttab=parent.closetab();
							//针对纯的grid的刷新
							var curtab = parenttab.getActiveTab();
							parenttab.remove(curtab);
						}else if(data.appcode===-1){
							AOS.tip("添加失败!");
						}
					}
				});
			}
			function fn_error_render(value, metaData, record, rowIndex, colIndex,
									store){
				if (value === 0) {
					return '未修改';
				} if(value === 1){
					return "已修改";
				}
				else {
					return ' ';
				}
			}
			function _w_edit_error(){
				var selection = AOS.selection(_g_error_details, 'id_');
				if (AOS.empty(selection)) {
					AOS.tip('请选择数据!');
					return;
				}
				var record=AOS.selectone(_g_error_details);
				AOS.ajax({
					url: 'updateErrorRegister.jhtml',
					params:{id_:record.data.id_,flag:'1'},
					ok: function (data) {
						if(data.appcode===1){
							AOS.tip("修改成功!");
							_g_error_details_store.reload();
						}else if(data.appcode===-1){
							AOS.tip("修改失败!");
						}
					}
				});

			}
			function _w_no_edit_error(){
				var selection = AOS.selection(_g_error_details, 'id_');
				if (AOS.empty(selection)) {
					AOS.tip('请选择数据!');
					return;
				}
				var record=AOS.selectone(_g_error_details);
				AOS.ajax({
					url: 'updateErrorRegister.jhtml',
					params:{id_:record.data.id_,flag:'0'},
					ok: function (data) {
						if(data.appcode===1){
							AOS.tip("修改成功!");
							_g_error_details_store.reload();
						}else if(data.appcode===-1){
							AOS.tip("修改失败!");
						}
					}
				});
			}
		</script>
	</aos:onready>
</aos:html>
<%@ page contentType="text/html; charset=utf-8" isELIgnored="false"%>
<%@ taglib uri="/WEB-INF/tld/aos.tld" prefix="aos"%>
<%@ taglib uri="/WEB-INF/tld/cll.tld" prefix="c"%>
<%
	String path = request.getContextPath();
%>
<aos:html>
	<aos:head title="档案详情">
		<aos:include lib="ext" />
		<aos:base href="transfer/transfer" />
	</aos:head>
	<aos:body>
	</aos:body>
	<aos:onready>
		<aos:viewport layout="border">
				<aos:gridpanel  id="_g_tablename_details" autoScroll="true" region="center" hidePagebar="true"  onrender="_load_tablename_details"
								url="listAccounts2.jhtml"  pageSize="60">
					<aos:docked>
						<aos:dockeditem xtype="tbtext" text="档案详情" />
						<aos:dockeditem text="移除" icon="del.png" onclick="_w_del_show" />
						<aos:dockeditem text="同步" icon="ok.png"  />
						<aos:dockeditem text="返回" icon="more/document-revert-5.png"  />
					</aos:docked>
					<aos:hiddenfield  name="tablename" id="tablename" value="${tablename}"/>
					<aos:hiddenfield  name="transfer_id_" id="transfer_id_" value="${transfer_id_}"/>
					<aos:column type="rowno" />
					<aos:selmodel type="checkbox" mode="multi" />
					<aos:column dataIndex="id_" header="流水号" hidden="true" />
					<c:forEach var="field" items="${fieldDtos}">
						<aos:column dataIndex="${field.fieldenname}"
									header="${field.fieldcnname}" width="${field.dislen}"  rendererField="field_type_" />
					</c:forEach>
					<aos:column header="" flex="1" />
				</aos:gridpanel>
		</aos:viewport>
		<script type="text/javascript">
			function _load_tablename_details(){
					var params = {
						tablename: tablename.getValue(),
						transfer_id_:transfer_id_.getValue()
					};
					//这个Store的命名规则为：表格ID+"_store"。
				_g_tablename_details_store.getProxy().extraParams = params;
				_g_tablename_details_store.load();
			}
			//移除操作
			function _w_del_show(){
				var selection = AOS.selection(_g_tablename_details, 'id_');
				if (AOS.empty(selection)) {
					AOS.tip('移除前请先选中数据。');
					return;
				}
				var msg = AOS.merge('确认要移除选中的[{0}]个用户数据吗？', AOS.rows(_g_tablename_details));
				AOS.confirm(msg, function (btn) {
					if (btn === 'cancel') {
						AOS.tip('删除操作被取消。');
						return;
					}
					AOS.ajax({
						url: 'deleteTransferData.jhtml',
						params: {
							aos_rows_: selection,
							transfer_id_:transfer_id_.getValue(),
							tablename: tablename.getValue()
						},
						ok: function (data) {
							AOS.tip(data.appmsg);
							_g_tablename_details_store.reload();
						}
					});
				});
			}
		</script>
	</aos:onready>
</aos:html>
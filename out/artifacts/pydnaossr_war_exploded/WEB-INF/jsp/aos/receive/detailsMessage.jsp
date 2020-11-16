<%@ page contentType="text/html; charset=utf-8" isELIgnored="false"%>
<%@ taglib uri="/WEB-INF/tld/aos.tld" prefix="aos"%>
<%@ taglib uri="/WEB-INF/tld/cll.tld" prefix="c"%>
<%
	String path = request.getContextPath();
%>
<aos:html>
	<aos:head title="档案详情">
		<aos:include lib="ext" />
		<aos:base href="archive/datatemoprary" />
	</aos:head>
	<aos:body>
	</aos:body>
	<aos:onready>
		<aos:viewport layout="border">
				<aos:gridpanel  id="_g_tablename_details" autoScroll="true" region="center" hidePagebar="true"  onrender="_load_tablename_details"
								url="listAccounts2.jhtml"  pageSize="60">
					<aos:docked>
						<aos:dockeditem xtype="tbtext" text="档案详情" />
					</aos:docked>
					<aos:hiddenfield  name="tablename" id="tablename" value="${tablename}"/>
					<aos:hiddenfield  name="pch" id="pch" value="${pch}"/>
					<aos:selmodel type="checkbox" mode="multi" />
					<aos:column type="rowno" />
					<aos:column dataIndex="id_" header="流水号" hidden="true" />
					<c:forEach var="field" items="${fieldDtos}">
						<aos:column dataIndex="${field.fieldenname}"
									header="${field.fieldcnname}" width="${field.dislen}"  rendererField="field_type_" />
					</c:forEach>
					<aos:column header="" flex="1" />
					<aos:docked dock="bottom" ui="footer">
						<aos:dockeditem xtype="tbfill" />
						<aos:dockeditem onclick="_first_save_kf_details" text="入库并添加描述" icon="ok.png" />
					</aos:docked>
				</aos:gridpanel>
			<aos:window id="_w_first_kf_message" title="入库意见说明" width="500" >
				<aos:formpanel  id="_f_first_kf_message" width="480">
					<aos:textareafield fieldLabel="意见说明" id="first_kf_message"  name="first_kf_message" width="450" allowBlank="false"/>
				</aos:formpanel>
				<aos:docked dock="bottom" ui="footer">
					<aos:dockeditem xtype="tbfill" />
					<aos:dockeditem text="确定" onclick="_save_first_kf_message" icon="ok.png" />
					<aos:dockeditem onclick="#_w_first_kf_message.hide();" text="关闭"
									icon="close.png" />
				</aos:docked>
			</aos:window>
		</aos:viewport>
		<script type="text/javascript">
			function _load_tablename_details(){
					var params = {
						tablename: tablename.getValue(),
						pch:pch.getValue()
					};
					//这个Store的命名规则为：表格ID+"_store"。
				_g_tablename_details_store.getProxy().extraParams = params;
				_g_tablename_details_store.load();
			}
			//保存开放信息
			function _first_save_kf_details(){
				//此时把当前的信息全部保存到数据表中并添加信息
				var selection = AOS.selection(_g_tablename_details, 'id_');
				if (AOS.empty(selection)) {
					AOS.tip('入库前请先选中数据。');
					return;
				}
				_w_first_kf_message.show();
				Ext.getCmp("first_kf_message").setValue("");
			}
			function _save_first_kf_message(){
				var selection = AOS.selection(_g_tablename_details, 'id_');
				var msg = AOS.merge('确认要入库选中的[{0}]个用户数据吗？', AOS.rows(_g_tablename_details));
				AOS.confirm(msg, function (btn) {
					if (btn === 'cancel') {
						AOS.tip('入库操作被取消。');
						return;
					}
					AOS.ajax({
						url: 'temporaryToreceiveData.jhtml',
						params: {
							aos_rows_: selection,
							opinion_description:Ext.getCmp("first_kf_message").getValue(),
							pch:Ext.getCmp("pch").getValue(),
							tablename: tablename.getValue()
						},
						ok: function (data) {
							AOS.tip(data.appmsg);
							var parenttab=parent.closetab();
							var curtab = parenttab.getActiveTab();
							parenttab.remove(curtab);
							//_g_tablename_details_store.reload();
						}
					});
				});

			}
		</script>
	</aos:onready>
</aos:html>
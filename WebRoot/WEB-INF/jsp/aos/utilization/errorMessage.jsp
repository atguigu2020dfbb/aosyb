<%@ page contentType="text/html; charset=utf-8" isELIgnored="false"%>
<%@ taglib uri="/WEB-INF/tld/aos.tld" prefix="aos"%>
<%@ taglib uri="/WEB-INF/tld/cll.tld" prefix="c"%>
<%
	String path = request.getContextPath();
%>
<aos:html>
	<aos:head title="档案详情">
		<aos:include lib="ext" />
		<aos:base href="utilization" />
	</aos:head>
	<aos:body>
	</aos:body>
	<aos:onready>
		<aos:viewport layout="border">
				<aos:gridpanel  id="_g_tablename_details" autoScroll="true" region="center" hidePagebar="true"  onrender="_load_tablename_details"
								url="listAccounts2.jhtml"  pageSize="60">
					<aos:docked>
						<aos:dockeditem xtype="tbtext" text="档案详情" />
						<aos:dockeditem text="错误登记" icon="more/page-error.png" onclick="_w_register_error"/>
					</aos:docked>
					<aos:hiddenfield  name="tablename" id="tablename" value="${tablename}"/>
					<aos:hiddenfield  name="error_id_" id="error_id_" value="${error_id_}"/>
					<aos:hiddenfield  name="djbh" id="djbh" value="${djbh}"/>
					<aos:column type="rowno" />
					<aos:selmodel type="row" mode="multi" />
					<aos:column dataIndex="id_" header="流水号" hidden="true" />
					<c:forEach var="field" items="${fieldDtos}">
						<aos:column dataIndex="${field.fieldenname}"
									header="${field.fieldcnname}" width="${field.dislen}"  rendererField="field_type_" />
					</c:forEach>
					<aos:column header="" flex="1" />
				</aos:gridpanel>
				<aos:window id="_w_error_register" title="错误登记" onshow="error_show">
					<aos:formpanel id="_f_error_register" width="700" layout="anchor">
						<aos:hiddenfield name="id_" fieldLabel="id_" />
						<aos:textfield name="register_person" fieldLabel="登记人" readOnly="true"/>
						<aos:textfield name="register_time" fieldLabel="登记时间" readOnly="true"/>
						<aos:textfield name="error_archive" fieldLabel="错误门类" readOnly="true"/>
						<aos:textfield name="dh" fieldLabel="档号" readOnly="true"/>
						<aos:textareafield fieldLabel="错误描述" name="error_description"/>
						<aos:docked dock="bottom" ui="footer">
							<aos:dockeditem xtype="tbfill"/>
							<aos:dockeditem text="保存" icon="save.png" onclick="_f_error_register_save"/>
							<aos:dockeditem text="关闭" icon="close.png" onclick="#_w_error_register.hide()"/>
						</aos:docked>
					</aos:formpanel>
				</aos:window>
		</aos:viewport>
		<script type="text/javascript">
			function _load_tablename_details(){
					var params = {
						tablename: tablename.getValue(),
						error_id_:error_id_.getValue()
					};
					//这个Store的命名规则为：表格ID+"_store"。
				_g_tablename_details_store.getProxy().extraParams = params;
				_g_tablename_details_store.load();
			}
			function _w_register_error(){
				var selection = AOS.selection(_g_tablename_details, 'id_');
				if (AOS.empty(selection)) {
					AOS.tip('请选择数据!');
					return;
				}
				_w_error_register.show();
			}
			function error_show(){
				var record = AOS.selectone(_g_tablename_details);
				//让指定文本框赋值
				_f_error_register.form.findField("error_archive").setValue(tablename.getValue());
				var time = (new Date).getTime();
				var yesday = new Date(time); // 获取的是前一天日期
				yesday = yesday.getFullYear() + "-" + (yesday.getMonth()> 9 ? (yesday.getMonth() + 1) : "0" + (yesday.getMonth() + 1)) + "-" +(yesday.getDate()> 9 ? (yesday.getDate()) : "0" + (yesday.getDate()));
				_f_error_register.form.findField("register_time").setValue(yesday);
				var user ="<%=session.getAttribute("user")%>";
				_f_error_register.form.findField("register_person").setValue(user);
				_f_error_register.form.findField("dh").setValue(record.data.dh);
			}
			function _f_error_register_save(){
				AOS.ajax({
					url: 'saveErrorRegister.jhtml',
					forms:_f_error_register,
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
		</script>
	</aos:onready>
</aos:html>
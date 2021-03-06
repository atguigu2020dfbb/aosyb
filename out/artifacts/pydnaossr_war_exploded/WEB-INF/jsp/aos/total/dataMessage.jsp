﻿<%@ page contentType="text/html; charset=utf-8" isELIgnored="false"%>
<%@ taglib uri="/WEB-INF/tld/aos.tld" prefix="aos"%>
<%@ taglib uri="/WEB-INF/tld/cll.tld" prefix="c"%>
<%
	String path = request.getContextPath();
%>
<aos:html>
	<aos:head title="档案详情">
		<aos:include lib="ext" />
		<aos:base href="archive/datatotal" />
	</aos:head>
	<aos:body>
	</aos:body>
	<aos:onready>
		<aos:viewport layout="border">
				<aos:gridpanel  id="_g_tablename_details" autoScroll="true" region="center"   onrender="_load_tablename_details"
								url="listAccounts4.jhtml"  pageSize="${pagesize}">
					<aos:docked>
						<aos:dockeditem xtype="tbtext" text="档案详情" />
					</aos:docked>
					<aos:hiddenfield  name="tablename" id="tablename" value="${tablename}"/>
					<aos:hiddenfield  name="pch" id="pch" value="${pch}"/>
					<aos:column type="rowno" />
					<aos:selmodel type="row" mode="multi" />
					<aos:column dataIndex="id_" header="流水号" hidden="true" locked="true" />
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
						pch:pch.getValue()
					};
					//这个Store的命名规则为：表格ID+"_store"。
				_g_tablename_details_store.getProxy().extraParams = params;
				_g_tablename_details_store.load();
			}
		</script>
	</aos:onready>
</aos:html>
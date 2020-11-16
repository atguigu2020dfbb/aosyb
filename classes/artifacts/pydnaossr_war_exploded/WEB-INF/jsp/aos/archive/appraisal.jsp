<%@page import="com.mysql.jdbc.Field"%>
<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="/WEB-INF/tld/aos.tld" prefix="aos"%>
<%@ taglib uri="/WEB-INF/tld/cll.tld" prefix="c"%>
<aos:html>
<aos:head title="综合实例①">
	<aos:include lib="ext,swfupload" />
	<aos:base href="appraisal" />
</aos:head>
<aos:body>
</aos:body>
<aos:onready>
	<aos:viewport layout="border">
		<aos:formpanel id="_f_appraisal" layout="column" labelWidth="70" header="false" region="north" border="false">
			<aos:docked forceBoder="0 0 1 0">
				<aos:dockeditem xtype="tbtext" text="销毁或过期档案" />
			</aos:docked>
			<aos:textfield name="name" id="name" value="wsda" disabled="true" fieldLabel="数据表名称" columnWidth="0.5" />
			<aos:textfield name="description" value="文书档案" disabled="true" fieldLabel="数据表描述" columnWidth="0.5" />
			<aos:docked dock="bottom" ui="footer" margin="0 0 8 0">
				<aos:dockeditem xtype="tbfill" />
				<aos:dockeditem xtype="button" text="查询销毁" onclick="_g_appraisal_query" icon="query.png" />
				<aos:dockeditem xtype="button" text="删除档案" onclick="_g_appraisal_del" icon="del.png" />
				<aos:dockeditem xtype="button" text="重置信息" onclick="AOS.reset(_f_appraisal);" icon="refresh.png" />
				<aos:dockeditem xtype="tbfill" />
			</aos:docked>
		</aos:formpanel>
		<aos:gridpanel id="_g_org"  url="selectappraisal.jhtml"   region="center">
			<aos:docked forceBoder="1 0 1 0">
				<aos:dockeditem xtype="tbtext" text="组织机构信息" />
			</aos:docked>
			<aos:column type="rowno" />
			<aos:column dataIndex="id_" header="流水号" hidden="true" />
			<!-- 迭代字段名称,非常重要的一部分 -->
			<c:forEach var="field" items="${fieldDtos}">
				<aos:column dataIndex="${field.fieldenname}"
					header="${field.fieldcnname}" width="${field.dislen}"
					rendererField="field_type_" />
			</c:forEach>
		</aos:gridpanel>
	</aos:viewport>
	<script type="text/javascript">
		//弹出窗口
		function _g_appraisal_query(){
			var params = {
				 	//检索条件
					tableTemplate :'wsda'
				};
				//这个Store的命名规则为：表格ID+"_store"。
				_g_org_store.getProxy().extraParams = params;
				_g_org_store.load();
		}
		function _g_appraisal_del(){
			var selection = AOS.selection(_g_org, 'id_');
            if (AOS.empty(selection)) {
                AOS.tip('删除前请先选中数据。');
                return;
            }
            var tms = AOS.selection(_g_org, 'tm').split(",")[0];
            var msg = AOS.merge('确认要删除选中的[{0}]个用户数据吗？', AOS.rows(_g_org));
            AOS.confirm(msg, function (btn) {
                if (btn === 'cancel') {
                    AOS.tip('删除操作被取消。');
                    return;
                }
                var tablename=Ext.getCmp('name').getValue();
                AOS.ajax({
                    url: 'deleteData.jhtml',
                    params: {
                        aos_rows_: selection,
                        tablename: tablename,
                        tm:tms
                    },
                    ok: function (data) {
                        AOS.tip(data.appmsg);
                        _g_org_store.reload();
                    }
                });
            });
		}
		//加载表格数据
		/*function _g_org_query() {
			var params = AOS.getValue('_f_query');
			_g_org_store.getProxy().extraParams = params;
			_g_org_store.load();
		}	*/		
		//窗口弹出事件监听
		function _w_org2_onshow() {
			var record = AOS.selectone(_g_org, true);
			_f_org2.loadRecord(record);
		}
		
	</script>
</aos:onready>
<script type="text/javascript">
	//显示详情1窗口
	function _w_org_show(){
		Ext.getCmp('_w_org').show();
	}
	//显示详情2窗口
	function _w_org2_show(){
		Ext.getCmp('_w_org2').show();
	}
</script>
</aos:html>
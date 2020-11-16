<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="/WEB-INF/tld/aos.tld" prefix="aos"%>
<aos:html>
<aos:head title="电子档案统计">
	<aos:include lib="ext" />
	<aos:base href="count/statistics_dianzi" />
</aos:head>
<aos:body>
</aos:body>
<aos:onready>
	<aos:viewport layout="fit">
		<aos:gridpanel id="_g_statistics" url="liststatistics.jhtml"
			onrender="_g_statistics_query">
			<aos:docked>
				<aos:dockeditem xtype="tbtext" text="档案统计分析" />
				<aos:dockeditem xtype="tbseparator" />
					<aos:dockeditem text="查询" icon="query.png"
					onclick="_w_query_show" />
			</aos:docked>
			<aos:column type="rowno" />
			<aos:column header="流水号" dataIndex="id" hidden="true" align="center" />
			<aos:column header="档案门类" dataIndex="category" width="60"
				align="center" />
			<aos:column header="案卷数" dataIndex="ajs" width="60"
				align="center" />
			<aos:column header="文件数" dataIndex="wjs" width="60"
				align="center" />
			<aos:column header="案卷页数" dataIndex="ajys" width="60"
				align="center" />

			<aos:column header="盒数" dataIndex="hs" width="60"
						align="center" />
			<aos:column header="30年" dataIndex="bgqx30" width="60"
						align="center" />
			<aos:column header="10年" dataIndex="bgqx10" width="60"
						align="center" />
			<aos:column header="永久" dataIndex="bgqxy" width="60"
						align="center" />


		</aos:gridpanel>
		<aos:window id="_w_query_q" title="查询" width="720" autoScroll="true"
		layout="fit">
		<aos:tabpanel id="_tabpanel" region="center" activeTab="0"
			bodyBorder="0 0 0 0" tabBarHeight="30">
			<aos:tab title="列表式搜索" id="_tab_org">
				<aos:formpanel id="_f_query" layout="column" columnWidth="1">
						<aos:combobox name="and1" value="on"
							labelWidth="10" columnWidth="0.2">
							<aos:option value="on" display="并且" />
							<aos:option value="up" display="或者" />
						</aos:combobox>						
						<aos:combobox name="filedname1" id="filedname1"
							 labelWidth="20" regexText="nd"
							columnWidth="0.2" emptyText="年度" value="nd" disabled="true">
						</aos:combobox>
						<aos:combobox name="condition1" value="like"
							labelWidth="20" columnWidth="0.2">
							<aos:option value="=" display="等于" />
							<aos:option value=">" display="大于" />
							<aos:option value=">=" display="大于等于" />
							<aos:option value="<" display="小于" />
							<aos:option value="<=" display="小于等于" />
							<aos:option value="<>" display="不等于" />
							<aos:option value="like" display="包含" />
							<aos:option value="left" display="左包含" />
							<aos:option value="right" display="右包含" />
							<aos:option value="null" display="空值" />
						</aos:combobox>
						<aos:textfield name="content1"
							allowBlank="true" columnWidth="0.39" />
						<aos:combobox name="and2" value="on"
							labelWidth="10" columnWidth="0.2">
							<aos:option value="on" display="并且" />
							<aos:option value="up" display="或者" />
						</aos:combobox>						
						<aos:combobox name="filedname2" id="filedname2"
							 labelWidth="20"  regexText="bgqx"
							columnWidth="0.2" 
							  emptyText="保管期限" value="bgqx" disabled="true"
							>
						</aos:combobox>
						<aos:combobox name="condition2" value="like"
							labelWidth="20" columnWidth="0.2">
							<aos:option value="=" display="等于" />
							<aos:option value=">" display="大于" />
							<aos:option value=">=" display="大于等于" />
							<aos:option value="<" display="小于" />
							<aos:option value="<=" display="小于等于" />
							<aos:option value="<>" display="不等于" />
							<aos:option value="like" display="包含" />
							<aos:option value="left" display="左包含" />
							<aos:option value="right" display="右包含" />
							<aos:option value="null" display="空值" />
						</aos:combobox>
						<aos:textfield name="content2"
							allowBlank="true" columnWidth="0.39" />
						<aos:combobox name="and3" value="on"
							labelWidth="10" columnWidth="0.2">
							<aos:option value="on" display="并且" />
							<aos:option value="up" display="或者" />
						</aos:combobox>						
						<aos:combobox name="filedname3" id="filedname3"
							 labelWidth="20"  regexText="jgwt"
							columnWidth="0.2" 
							  emptyText="机构问题" value="jgwt" disabled="true"
							>
						</aos:combobox>
						<aos:combobox name="condition3" value="like"
							labelWidth="20" columnWidth="0.2">
							<aos:option value="=" display="等于" />
							<aos:option value=">" display="大于" />
							<aos:option value=">=" display="大于等于" />
							<aos:option value="<" display="小于" />
							<aos:option value="<=" display="小于等于" />
							<aos:option value="<>" display="不等于" />
							<aos:option value="like" display="包含" />
							<aos:option value="left" display="左包含" />
							<aos:option value="right" display="右包含" />
							<aos:option value="null" display="空值" />
						</aos:combobox>
						<aos:textfield name="content3"
							allowBlank="true" columnWidth="0.39" />
					<aos:docked dock="bottom" ui="footer">
						<aos:dockeditem xtype="tbfill" />
						<aos:dockeditem onclick="_f_data_query" text="确定" icon="ok.png" />
						<aos:dockeditem onclick="#_w_query_q.hide();" text="关闭"
							icon="close.png" />
					</aos:docked>
				</aos:formpanel>
			</aos:tab>		
		</aos:tabpanel>
	</aos:window>	
	</aos:viewport>
	<script type="text/javascript">
		function _g_statistics_query(){
			_g_statistics_store.load();
		}
		 function _w_query_show() {
        	//判断是不是 
                _w_query_q.show();
        }
        function _f_data_query(){
			var params = AOS.getValue('_f_query');
			 var form = Ext.getCmp('_f_query');
			var ndstr = form.down("[name='filedname1']");
			var ndvalue =ndstr.getValue();
			if(ndvalue==null){
			params["filedname1"]=ndstr.regexText;
			}
			var bgqxstr = form.down("[name='filedname2']");
			var bgqxvalue =bgqxstr.getValue();
			if(bgqxvalue==null){
			params["filedname2"]=bgqxstr.regexText;
			}
			var jgwtstr = form.down("[name='filedname2']");
			var jgwtvalue =bgqxstr.getValue();
			if(jgwtvalue==null){
			params["filedname3"]=jgwtstr.regexText;
			}
			_g_statistics_store.getProxy().extraParams = params;
			_g_statistics_store.load();
			_w_query_q.hide();
			AOS.reset(_f_query); 
	}
		//导出日志
		function fn_export_statistics(){
			AOS.ajax({
				url : 'fillReport.jhtml',
				ok : function(data) {
					AOS.file('${cxt}/report/xls2.jhtml');
				}
			});
		}
	</script>
</aos:onready>
</aos:html>
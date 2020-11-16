<%@ page contentType="text/html; charset=utf-8" isELIgnored="false"%>
<%@ taglib uri="/WEB-INF/tld/aos.tld" prefix="aos"%>
<%@ taglib uri="/WEB-INF/tld/cll.tld" prefix="c"%>
<%
	String path = request.getContextPath();
	Object fieldDtos = request.getAttribute("fieldDtos");
	Object listtablename = request.getAttribute("listtablename");
%>
<aos:html>
	<aos:head title="基本检索">
		<aos:include lib="ext" />
		<aos:base href="archive/basicSearch" />
	</aos:head>
	<aos:body>
	</aos:body>
	<aos:onready>
		<aos:viewport layout="border">
			<aos:treepanel id="_t_catalog" width="220" region="west" url="${cxt}/system/listcategory.jhtml"  onshow="select_tree"
						   onitemclick="_g_param_query" singleClick="false" useArrows="false" rootVisible="true" rootText="管理库管理"
						   nodeParam="parent_id_" bodyBorder="0 1 0 0" headerBorder="0 1 0 0" rootId="4e62f8c862e0459891deb2eb89370c40" >
				<aos:docked forceBoder="0 1 1 0">
					<aos:dockeditem xtype="tbtext" text="参数分类科目" />
					<aos:dockeditem xtype="tbfill" />
				</aos:docked>
				<aos:menu>
					<aos:menuitem text="刷新分类" onclick="_t_catalog_refresh" icon="refresh.png" />
				</aos:menu>
			</aos:treepanel>
			<aos:gridpanel id="_g_param" url="listAccounts.jhtml" region="center" onrender="_w_params_show">

				<aos:docked forceBoder="0 0 1 0">
					<aos:dockeditem xtype="tbtext" text="请选择档案门类" id="ddd" />
					<aos:hiddenfield  id="tablename_id" name="tablename_id" value="${id_}"/>
					<aos:dockeditem onclick="_w_query_show" text="查询" icon="edit.png" />
					<aos:button text="导出" icon="icon154.png" scale="small"
								margin="0 0 0 0">
						<aos:menu plain="false">
							<aos:menuitem text="导出XLS报表" icon="icon70.png" onclick="fn_xls" />
							<aos:menuitem text="导出XLSX报表" icon="icon7.png" onclick="fn_xlsx" />
						</aos:menu>
					</aos:button>
				</aos:docked>
				<aos:column type="rowno"  />
				<aos:selmodel type="row" mode="multi" />
				<aos:column dataIndex="id_" header="流水号" hidden="true" />
				<c:forEach var="field" items="${fieldDtos}">
					<aos:column dataIndex="${field.fieldenname}"
								header="${field.fieldcnname}" width="${field.dislen}"  rendererField="field_type_" />
				</c:forEach>
				<aos:column header="" flex="1" />
			</aos:gridpanel>
			<aos:window id="_w_query_q" title="查询" width="720" autoScroll="true"
						layout="fit" onshow="_load_fieldlists">
			<aos:tabpanel id="_tabpanel" region="center" activeTab="0"
						  bodyBorder="0 0 0 0" tabBarHeight="30">
				<aos:tab title="搜索记录" id="_tab_org">
					<aos:formpanel id="_f_query" layout="column" columnWidth="1">
						<aos:displayfield value="请输入您查询的条件" columnWidth="0.99" />
						<aos:hiddenfield id="tablename" name="tablename" value="wsda2"/>
						<aos:combobox id="filedname"
										  name="filedname"
										  emptyText="请选择需要查询的字段"
										  columnWidth="0.3" fields="['fieldenname','fieldcnname']"
										  regexText="fieldenname" displayField="fieldcnname"
										  valueField="fieldenname"
										  url="queryFields.jhtml">
						</aos:combobox>
						<aos:combobox name="condition" id="condition" value="like"
									  labelWidth="20" columnWidth="0.2">
							<aos:option value="=" display="等于" />
							<aos:option value=">" display="大于" />
							<aos:option value=">=" display="大于等于" />
							<aos:option value="<" display=" 小于" />
							<aos:option value="<=" display="小于等于" />
							<aos:option value="<>" display="不等于" />
							<aos:option value="like" display="包含" />
							<aos:option value="left" display="左包含" />
							<aos:option value="right" display="右包含" />
							<aos:option value="null" display="空值" />
						</aos:combobox>
						<aos:textfield name="fieldtext" id="fieldtext" columnWidth="0.4"/>
						<aos:radioboxgroup fieldLabel="" id="_select_radio"   columns="[80,80,80]" columnWidth="0.4" >
							<aos:radiobox name="select" boxLabel="精确查询" checked="true"/>
							<aos:radiobox name="select" boxLabel="模糊查询" />
							<aos:radiobox name="select" boxLabel="高级检索" onchang="hidetoshow"/>
						</aos:radioboxgroup>
						<aos:combobox name="and" value="on" id="and"
									  labelWidth="10" columnWidth="0.3">
							<aos:option value="and" display="并且" />
							<aos:option value="or" display="或者" />
						</aos:combobox>
						<aos:button text="添加" onclick="add_count" columnWidth="0.1"/>
						<aos:button text="删除" onclick="delete_count" columnWidth="0.1"/>
						<aos:gridpanel id="_g_count" split="true" hidePagebar="true" autoScroll="true" columnWidth="1.0"
									   height="200">
							<aos:column header="查询条件" dataIndex="selectorder" width="60"/>
							<aos:column header="查询值" dataIndex="selectmath" width="90"/>
						</aos:gridpanel>
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
			window.onload=function(){
				var tabledesc="<%=request.getAttribute("name_")%>";
				Ext.getCmp("ddd").setText(tabledesc);
				//让指定节点选中
				/*var record = Ext.getCmp("_t_catalog").getStore().getNodeById("5bc1c0994dbf46269b07a2526dbf7ff2");

				var ss = Ext.getCmp("_t_catalog").getSelectionModel();
				ss.select(record);*/

			}
			function select_tree(){

			}
			function add_count(){
				var fieldtext=Ext.getCmp('_f_query').down("[name='fieldtext']").getValue();
				var fieldname=Ext.getCmp('_f_query').down("[name='filedname']").getValue();
				if(fieldname=="null"||fieldname==""||fieldname==null){
					AOS.err("请选择字段");
					return;
				}
				var _select_radio_check = Ext.getCmp('_select_radio').items;
				var xq = '';
				for(var i = 0; i < _select_radio_check.length; i++){
					if(_select_radio_check.get(i).checked){
						xq =_select_radio_check.get(i).boxLabel;
					}
				}
				if(xq=="精确查询"){
					var data=[{
						'selectorder':"and "+fieldname+" = ",
						'selectmath':fieldtext
					}];
					_g_count_store.loadData(data,true);
				}
				if(xq=="模糊查询"){
					var data=[{
						'selectorder':"and "+fieldname+" like ",
						'selectmath':"%"+fieldtext+"%"
					}];
					_g_count_store.loadData(data,true);
				}
				if(xq=="高级检索"){
					//条件
					var and=Ext.getCmp('_f_query').down("[name='and']").getValue();
					//连接符
					var condition=Ext.getCmp('_f_query').down("[name='condition']").getValue();
					var selectorder="";
					var selectmath="";
					if(condition=="like"){
						condition="like";
						selectorder=and+" "+fieldname+" like ";
						selectmath="%"+fieldtext+"%";
					}else if(condition=="left"){
						selectorder=and+" "+fieldname+" like ";
						selectmath="%"+fieldtext;
					}else if(condition=="right"){
						selectorder=and+" "+fieldname+" like ";
						selectmath=fieldtext+"%";
					}else if(condition=="null"){
						selectorder=and+" "+fieldname+" is ";
						selectmath="null";
					}else{
						selectorder=and+" "+fieldname+" "+condition;
						selectmath=fieldtext;
					}
					var data=[{
						'selectorder':selectorder,
						'selectmath':selectmath
					}];
					_g_count_store.loadData(data,true);
				}
			}
			function delete_count(){
				var row = _g_count.getSelectionModel().getSelection();
				var rowid = _g_count_store.indexOf(row[0]);//获得选中的第一项在store内的行号
				//此时删除严肃，在他的上一行添加
				_g_count_store.removeAt(rowid);
			}
			//根据选择的门类名称，进行字段列表的查询操作
			function _load_fieldlists(){
				//默认把and隐藏
				Ext.getCmp("and").hide();
				Ext.getCmp("condition").hide();
				//把门类字段列表传递过去
				var params = {
					id_ : Ext.getCmp("tablename_id").getValue(),
				};
				filedname_store.getProxy().extraParams = params;
				filedname_store.load();
			}
			function hidetoshow(){
				var _select_radio_check = Ext.getCmp('_select_radio').items;
				var xq = '';
				for(var i = 0; i < _select_radio_check.length; i++){
					if(_select_radio_check.get(i).checked){
						xq =_select_radio_check.get(i).boxLabel;
					}
				}
				if(xq=="高级检索"){
					Ext.getCmp("and").show();
					Ext.getCmp("condition").show();
				}else{
					Ext.getCmp("and").hide();
					Ext.getCmp("condition").hide();
				}
			}
			function _w_query_show() {
				_w_query_q.show();
			}
			//查询参数列表
			function _g_param_query() {
				var record = AOS.selectone(_t_catalog);
				if (record) {
					window.location.href="initInput.jhtml?id_="+record.raw.id+"&name_="+record.raw.name_;
				}
			}
			function _w_params_show(){
				var id_=Ext.getCmp("tablename_id").getValue();
				var params = {
					id_: id_
				};
				//这个Store的命名规则为：表格ID+"_store"。
				_g_param_store.getProxy().extraParams = params;
				_g_param_store.load();
			}
			//修改参数保存
			function _f_param_u_submit() {
				var overwrite_field_ = _f_param_u.form.findField('overwrite_field_').getValue();
				var is_overwrite_ = _f_param_u.form.findField('is_overwrite_').getValue();
				if (AOS.empty(overwrite_field_) && is_overwrite_ === '1') {
					AOS.tip('覆盖字段为空的时候，是否覆盖只能选择否。请核对。');
					return;
				}
				AOS.ajax({
					forms: _f_param_u,
					url: 'updateParam.jhtml',
					ok: function (data) {
						if(data.appcode !== -1){
							_w_param_u.hide();
							_g_param_store.reload();
						}
						AOS.tip(data.appmsg);
					}
				});
			}

			//刷新分类树
			function _t_catalog_refresh() {
				var refreshnode = AOS.selectone(_t_catalog);
				if (refreshnode.isLeaf()) {
					refreshnode = refreshnode.parentNode;
				}
				_t_catalog_store.load({
					node: refreshnode,
					callback: function () {
						refreshnode.collapse();
						refreshnode.expand();
					}
				});
			}
			//确定执行统计设计
			function _f_data_query(){
				//如果统计字段和统计方法没有选中直接不执行
				var s=Ext.getCmp('_g_count').getStore();
				var selectorders="";
				var selectmaths="";
				var query="";
				for(var i = 0 ;i< s.getCount(); i++){
					query+=s.getAt(i).get('selectorder') +" '"+s.getAt(i).get('selectmath')+"'";
				}
				var params = AOS.getValue('_f_query');
				params["query"]=query;
				params["id_"]=Ext.getCmp("tablename_id").getValue();

				_g_param_store.getProxy().extraParams = params;
				_g_param_store.load();
				_w_query_q.hide();

			}


			function fn_xls(){
				var tablename_id=Ext.getCmp("tablename_id").getValue();
				AOS.ajax({
					url : 'fillReport.jhtml',
					params:{
						id_ : tablename_id
					},
					ok : function(data) {
						AOS.file('${cxt}/report/xls.jhtml');
					}
				});
			}
			function fn_xlsx(){
				var tablename_id=Ext.getCmp("tablename_id").getValue();
				AOS.ajax({
					url : 'fillReport.jhtml',
					params:{
						id_ : tablename_id
					},
					ok : function(data) {
						AOS.file('${cxt}/report/xlsx.jhtml');
					}
				});
			}
			function _w_param_u_show(){

			}
		</script>
	</aos:onready>
</aos:html>
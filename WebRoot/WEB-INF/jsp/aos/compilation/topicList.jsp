<%@ page contentType="text/html; charset=utf-8" isELIgnored="false"%>
<%@ taglib uri="/WEB-INF/tld/aos.tld" prefix="aos"%>
<%@ taglib uri="/WEB-INF/tld/cll.tld" prefix="c"%>
<%
	String path = request.getContextPath();
	Object fieldDtos = request.getAttribute("fieldDtos");
	Object listtablename = request.getAttribute("listtablename");
%>
<aos:html>
	<aos:head title="专题数据列表">
		<aos:include lib="ext" />
		<aos:base href="compilation/examine" />
	</aos:head>
	<aos:body>
	</aos:body>
	<aos:onready>
		<aos:viewport layout="fit">
			<aos:gridpanel id="_g_topic" region="center" url="listtopic.jhtml" onrender="_load_listtopic"
						   autoScroll="true" pageSize="20" enableLocking="true">
				<aos:docked forceBoder="0 0 1 0">
					<aos:dockeditem xtype="tbtext" text="专题列表" />
					<aos:hiddenfield name="nd" id="nd" value="${nd}"/>
					<aos:hiddenfield name="flag" id="flag" value="${flag}"/>
					<aos:hiddenfield name="tablename" id="tablename" value="${tablename}"/>
					<aos:hiddenfield name="query" id="query" />
					<!--当前查询记录数-->
					<aos:hiddenfield name="_total" id="_total" />
					<aos:dockeditem onclick="_w_topic_add" text="增加" icon="add.png" />
					<aos:dockeditem onclick="_w_topic_update" text="修改" icon="edit.png" />
					<aos:dockeditem onclick="_w_topic_del" text="删除" icon="del.png" />
				</aos:docked>
				<aos:column type="rowno" />
				<aos:column header="流水号" dataIndex="id_" hidden="true" />
				<aos:column header="专题名称" dataIndex="topic_name" width="200" align="true"/>
				<aos:column header="门类名称" dataIndex="topic_tablename" width="200" align="true"/>
				<aos:column header="专题描述" dataIndex="topic_description" width="200" align="true"/>
				<aos:column header="创建人" dataIndex="create_person" width="200" align="true"/>
				<aos:column header="创建时间" dataIndex="create_time" width="200" align="true"/>
				<aos:column header="" flex="1" />
			</aos:gridpanel>
			<aos:window id="_w_add_topic" title="新增专题" >
				<aos:formpanel id="_f_add_topic" width="420" layout="column" labelWidth="90" >
					<aos:hiddenfield name="ids"/>
					<aos:textfield name="topic_name" fieldLabel="专题名称" columnWidth="0.99"/>
					<aos:combobox fieldLabel="编研数据库" name="sjkmc" allowBlank="false"
								  fields="['tablename','tabledesc']" id="sjkmc" displayField="tabledesc" valueField="tablename"
								  editable="false" columnWidth="0.7" url="listTablename_by.jhtml" width="300"/>
					<aos:button text="选择档案" onclick="_select_archive" icon="more/select.png"   columnWidth="0.29"/>
					<aos:textfield name="create_person" fieldLabel="创建人"  columnWidth="0.99"/>
					<aos:datefield name="create_time" fieldLabel="创建时间" format="Y-m-d" columnWidth="0.99"/>
					<aos:textareafield name="topic_description" fieldLabel="专题描述" columnWidth="0.99" />
				</aos:formpanel>
				<aos:docked dock="bottom" ui="footer">
					<aos:dockeditem xtype="tbfill" />
					<aos:dockeditem onclick="_f_add_topic_submit" text="保存" icon="ok.png" />
					<aos:dockeditem onclick="#_w_add_topic.hide();" text="关闭" icon="close.png" />
				</aos:docked>
			</aos:window>

			<aos:window id="_w_edit_topic" title="修改专题" >
				<aos:formpanel id="_f_edit_topic" width="420" layout="column" labelWidth="90" >
					<aos:hiddenfield name="ids" />
					<aos:textfield name="topic_name" fieldLabel="专题名称" columnWidth="0.99" />
					<aos:textfield name="create_person" fieldLabel="创建人"  columnWidth="0.99"/>
					<aos:datefield name="create_time" fieldLabel="创建时间" format="Y-m-d" columnWidth="0.99"/>
					<aos:textareafield name="topic_description" fieldLabel="专题描述" columnWidth="0.99" />
				</aos:formpanel>
				<aos:docked dock="bottom" ui="footer">
					<aos:dockeditem xtype="tbfill" />
					<aos:dockeditem text="上一条" icon="more/go-previous.png"  onclick="_f_previous_data"/><%----%>
					<aos:dockeditem text="下一条" icon="more/go-next.png" onclick="_f_next_data"/><%----%>
					<aos:dockeditem onclick="_f_edit_topic_submit" text="保存" icon="ok.png" />
					<aos:dockeditem onclick="#_w_edit_topic.hide();" text="关闭" icon="close.png" />
				</aos:docked>
			</aos:window>

			<aos:window id="_w_select_archive" title="编研数据选择" onshow="_w_select_archive_onshow" width="800" height="500">
				<aos:gridpanel id="_g_select_archive" url="listArchive.jhtml" region="center" width="770" height="460"
							   autoScroll="true" pageSize="100" enableLocking="true" >
					<aos:docked forceBoder="0 0 1 0">
						<aos:dockeditem xtype="tbtext" text="数据列表" />
						<aos:dockeditem text="查询" icon="query.png"
										onclick="_w_select_query_show" />
						<aos:dockeditem  text="确定" icon="ok.png"
										 onclick="ok_save_archive" />
						<aos:dockeditem  text="全选" icon="more/edit-select-all-4.png"
										 onclick="ok_save_All_archive" />
					</aos:docked>
					<aos:selmodel type="checkbox" mode="multi" />
					<aos:column type="rowno" />
					<aos:column dataIndex="id_" header="流水号" hidden="true" />
					<aos:column header="全宗单位" dataIndex="qzdw" />
					<aos:column header="档号" dataIndex="dh" celltip="true" />
					<aos:column header="题名" dataIndex="tm"  width="80" />
					<aos:column header="年度" dataIndex="nd" />
					<aos:column header="保管期限" dataIndex="bgqx" />
					<aos:column header="形成时间" dataIndex="xcsj"  />
					<aos:column header="盒号" dataIndex="hh" />
					<aos:column header="备注" dataIndex="bz" flex="1" celltip="true" />
					<aos:column header="" flex="1" />
				</aos:gridpanel>
			</aos:window>
			<aos:window id="_w_query_select_q" title="查询" width="720" autoScroll="true"
						layout="fit" onshow="combobox_tableFileldlist">
				<aos:tabpanel id="_tabpanel_select" region="center" activeTab="0"
							  bodyBorder="0 0 0 0" tabBarHeight="30">
					<aos:tab title="列表式搜索" id="_tab_select_org">
						<aos:formpanel id="_f_select_query" layout="column" columnWidth="1">
							<aos:hiddenfield name="cascode_id_" value="${cascode_id_ }" />
							<aos:hiddenfield name="tablename" value="${tablename }" />
							<aos:hiddenfield name="columnnum" id="columnnum" value="7" />
							<aos:combobox name="and1" value="on"
										  labelWidth="10" columnWidth="0.2">
								<aos:option value="on" display="并且" />
								<aos:option value="up" display="或者" />
							</aos:combobox>
							<aos:combobox name="filedname1" id="filedname1" url="getComboboxList.jhtml"  fields="['fieldenname','fieldcnname']"
										  displayField="fieldcnname"
										  valueField="fieldenname"  labelWidth="20" columnWidth="0.2"  >
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
							<aos:combobox name="filedname2" id="filedname2" url="getComboboxList.jhtml"  fields="['fieldenname','fieldcnname']"
										  displayField="fieldcnname"
										  valueField="fieldenname"  labelWidth="20" columnWidth="0.2"  >
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
							<aos:combobox name="filedname3" id="filedname3" url="getComboboxList.jhtml"  fields="['fieldenname','fieldcnname']"
										  displayField="fieldcnname"
										  valueField="fieldenname"  labelWidth="20" columnWidth="0.2"  >
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
							<aos:combobox name="and4" value="on"
										  labelWidth="10" columnWidth="0.2">
								<aos:option value="on" display="并且" />
								<aos:option value="up" display="或者" />
							</aos:combobox>
							<aos:combobox name="filedname4" id="filedname4" url="getComboboxList.jhtml"  fields="['fieldenname','fieldcnname']"
										  displayField="fieldcnname"
										  valueField="fieldenname"  labelWidth="20" columnWidth="0.2"  >
							</aos:combobox>

							<aos:combobox name="condition4" value="like"
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
							<aos:textfield name="content4"
										   allowBlank="true" columnWidth="0.39" />

							<aos:combobox name="and5" value="on"
										  labelWidth="10" columnWidth="0.2">
								<aos:option value="on" display="并且" />
								<aos:option value="up" display="或者" />
							</aos:combobox>
							<aos:combobox name="filedname5" id="filedname5" url="getComboboxList.jhtml"  fields="['fieldenname','fieldcnname']"
										  displayField="fieldcnname"
										  valueField="fieldenname"  labelWidth="20" columnWidth="0.2"  >
							</aos:combobox>

							<aos:combobox name="condition5" value="like"
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
							<aos:textfield name="content5"
										   allowBlank="true" columnWidth="0.39" />

							<aos:docked dock="bottom" ui="footer">
								<aos:dockeditem xtype="tbfill" />
								<aos:dockeditem onclick="_f_select_data_query" text="确定" icon="ok.png" />
								<aos:dockeditem onclick="#_w_query_select_q.hide();" text="关闭"
												icon="close.png" />
							</aos:docked>
						</aos:formpanel>
					</aos:tab>
				</aos:tabpanel>
			</aos:window>
		</aos:viewport>
		<script>
			//跳转主页
			//查询窗口展开
			function _w_select_query_show() {
				//判断是不是
				_w_query_select_q.show();
			}
			function _select_archive(){
				//根据选择的名称
				var sjkmc_value = Ext.getCmp("sjkmc").value;
				if(sjkmc_value==""||sjkmc_value==null){
					AOS.tip("请选择数据库！");
					return;
				}
				//弹出选择档案窗口
				_w_select_archive.show();
			}
			//获取字段数据列表
			function combobox_tableFileldlist(){
				var params = {
					tablename : Ext.getCmp("sjkmc").value
				};
				//这个Store的命名规则为：表格ID+"_store"。ok_save
				filedname1_store.getProxy().extraParams = params;
				filedname1_store.load();
				filedname2_store.getProxy().extraParams = params;
				filedname2_store.load();
				filedname3_store.getProxy().extraParams = params;
				filedname3_store.load();
				filedname4_store.getProxy().extraParams = params;
				filedname4_store.load();
				filedname5_store.getProxy().extraParams = params;
				filedname5_store.load();
			}
			function _w_select_archive_onshow(){
				var params = {
					tablename : Ext.getCmp("sjkmc").value
				};
				//这个Store的命名规则为：表格ID+"_store"。ok_save

				_g_select_archive_store.getProxy().extraParams = params;
				_g_select_archive_store.load();
			}
			function _f_add_topic_submit(){
				var ids=_f_add_topic.getForm().findField('ids').getValue();
				if(ids==null||ids==""){
					AOS.tip("请选择要编研的数据！");
					return;
				}else{
					AOS.ajax({
						url: 'addtopic.jhtml',
						forms:_f_add_topic,
						params:{topic_tablename:Ext.getCmp("sjkmc").getValue(),
							topic_tabledesc:Ext.getCmp("sjkmc").getRawValue(),
							ids:ids
						},
						ok: function (data) {
							if(data.appcode===1){
								AOS.tip("添加成功!");
								_w_add_topic.hide();
								_g_topic_store.load();
							}else if(data.appcode===-1){
								AOS.tip("添加失败!");
							}
						}
					});
				}
			}
			function _f_edit_topic_submit(){
				var record = AOS.selectone(_g_topic);
				if(AOS.empty(record)){
					AOS.tip("请选择要修改的专题!");
					return;
				}
				AOS.ajax({
					url: 'edittopic.jhtml',
					forms:_f_edit_topic,
					params:{'id_':record.data.id_},
					ok: function (data) {
						if(data.appcode===1){
							AOS.tip("修改成功!");
							_w_edit_topic.hide();
							_g_topic_store.load();
						}else if(data.appcode===-1){
							AOS.tip("修改失败!");
						}
					}
				});
			}
			//保存当前选中的档案信息
			function ok_save_archive(){
				var count=AOS.rows(_g_select_archive);
				if(count<=0){
					AOS.tip("请选择数据");
					return;
				}
				var ids="";
				var row=_g_select_archive.getSelectionModel().getSelection();
				for(var i=0;i<AOS.rows(_g_select_archive);i++){
					if(i==0){
						ids=row[i].data.id_;
					}else{
						ids+=","+row[i].data.id_;
					}
				}
				_f_add_topic.getForm().findField('ids').setValue(ids);
				_w_select_archive.hide();
			}
			//保存当前选中的档案信息
			function ok_save_All_archive(){
				var model = _g_select_archive.getSelectionModel();
				model.selectAll();//选择所有行
				var ids="";
				//走后台进行当前条件的所有条目的id集合
				AOS.ajax({
					url: 'getQueryIds.jhtml',
					params:{'tablename':Ext.getCmp("sjkmc").getValue(),
					'query':Ext.getCmp("query").getValue()},
					ok: function(data){
						var ids="";
						for(k in data){
							if(k==0){
								ids=data[k].id_;
							}else{
								ids=ids+","+data[k].id_;
							}
						}
						_f_add_topic.getForm().findField('ids').setValue(ids);
						_w_select_archive.hide();
					}
				});
			}
			//加载数据
			function _load_listtopic(){
				_g_topic_store.reload();
			}
			function _w_topic_add(){
				AOS.reset(_f_add_topic);//查询窗口展开
				_w_add_topic.show();
			}
			function _w_select_query_show() {
				//判断是不是
				_w_query_select_q.show();

			}
			function _f_select_data_query(){
				var params = AOS.getValue('_f_select_query');
				var form = Ext.getCmp('_f_select_query');
				for(var i=1;i<=5;i++){
					var str = form.down("[name='filedname"+i+"']");
					var filedname =str.getValue();
					if(filedname==null){
						params["filedname"+i]=str.regexText;
					}
				}
				params["tablename"]=Ext.getCmp("sjkmc").value;
				_g_select_archive_store.getProxy().extraParams = params;
				_g_select_archive_store.load();
				AOS.ajax({
					params:params,
					url: 'saveQueryData.jhtml',
					ok: function (data) {
						//此时在隐藏域中存入查询条件
						Ext.getCmp("query").setValue(data.query);
					}
				});
				_w_query_select_q.hide();
				AOS.reset(_f_select_query);
			}
			function ok_save_archive_edit(){
				var count=AOS.rows(_g_select_edit_archive);
				if(count<=0){
					AOS.tip("请选择需要编研的数据");
					return;
				}
				var ids="";
				var row=_g_select_edit_archive.getSelectionModel().getSelection();
				for(var i=0;i<AOS.rows(_g_select_edit_archive);i++){
					if(i==0){
						ids=row[i].data.id_;
					}else{
						ids+=","+row[i].data.id_;
					}
				}
				var record = AOS.selectone(_g_compilation);
				var params = {
					ids:ids,
					by_id:record.data.id_,
					tablename : record.data.sjkmc
				};
				AOS.ajax({
					url: 'updatebyrw_data.jhtml',
					params:params,
					ok: function (data) {
						if(data.appcode===1){
							AOS.tip("添加成功!");
							_g_data_store.load();
							_w_select_edit_archive.hide();
							//_g_receive_query();
						}else if(data.appcode===-1){
							AOS.tip("添加失败!");
						}
					}
				});
			}
			//修改批次
			function _w_topic_update(){
				//AOS.reset(_f_edit_rw);
				var record = AOS.selectone(_g_topic);
				if(AOS.empty(record)){
					AOS.tip("请选择要修改的专题!");
					return;
				}
				_w_edit_topic.show();
				_f_edit_topic.loadRecord(record);
			}
			function _f_rw_edit_submit(){
				AOS.ajax({
					url: 'addtopic_edit.jhtml',
					forms:_f_edit_topic,
					ok: function (data) {
						if(data.appcode===1){
							AOS.tip("修改成功!");
							_w_edit_topic.hide();
							_g_topic_store.load();
							//_g_receive_query();
						}else if(data.appcode===-1){
							AOS.tip("修改失败!");
						}
					}
				});
			}
			function _w_topic_del() {
				var selection = AOS.selection(_g_topic, 'id_');
				if (AOS.empty(selection)) {
					AOS.tip('删除前请先选中专题吗。');
					return;
				}
				var msg = AOS.merge('确认要删除选中的[{0}]个专题吗？', AOS.rows(_g_topic));
				AOS.confirm(msg, function (btn) {
					if (btn === 'cancel') {
						AOS.tip('删除操作被取消。');
						return;
					}
					AOS.ajax({
						url: 'delete_topic.jhtml',
						params: {
							aos_rows_: selection
						},
						ok: function (data) {
							AOS.tip(data.appmsg);
							_g_topic_store.reload();
						}
					});
				});
			}
			//上一页
			function _f_previous_data(){
				var count=Ext.getCmp("_g_topic").getStore().getCount();
				var me=Ext.getCmp("_g_topic").getSelectionModel().getSelection();
				//var record = AOS.selectone(_g_topic);
				//得到执行行的坐标
				var rowIndex = Ext.getCmp("_g_topic").getStore().indexOf(me[0]);
				if(rowIndex==0){
					AOS.err("当前第一条!");
					return;
				}
				var s=Ext.getCmp("_g_topic").getStore().getAt(rowIndex-1);
				//原先行取消选中
				Ext.getCmp("_g_topic").getSelectionModel().deselect(rowIndex);
				//此时让光标选中上一行
				Ext.getCmp("_g_topic").getSelectionModel().select(rowIndex-1, true);
				//组件被显示后触发。
				Ext.getCmp("_f_edit_topic").form.setValues(s.data);
			}
			//下一页
			function _f_next_data(){
				var count=Ext.getCmp("_g_topic").getStore().getCount();
				var me=Ext.getCmp("_g_topic").getSelectionModel().getSelection();
				//var record = AOS.selectone(_g_topic);
				//得到执行行的坐标
				var rowIndex = Ext.getCmp("_g_topic").getStore().indexOf(me[0]);
				if(rowIndex==count-1){
					AOS.err("当前最后一条!");
					return;
				}
				var s=Ext.getCmp("_g_topic").getStore().getAt(rowIndex+1);
				//原先行取消选中
				Ext.getCmp("_g_topic").getSelectionModel().deselect(rowIndex);
				//此时让光标选中下一行
				Ext.getCmp("_g_topic").getSelectionModel().select(rowIndex+1, true);
				//组件被显示后触发。
				Ext.getCmp("_f_edit_topic").form.setValues(s.data);
			}
		</script>
	</aos:onready>
</aos:html>
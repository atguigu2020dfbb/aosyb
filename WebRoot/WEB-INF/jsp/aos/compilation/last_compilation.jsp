<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="/WEB-INF/tld/aos.tld" prefix="aos"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<aos:html>
	<aos:head title="总编辑">
		<aos:include lib="ext" />
		<aos:base href="compilation/examine" />
		<aos:include js="${cxt}/static/pdfObject/pdfobject.js" />
	</aos:head>
	<aos:body>
		<!--html代码  -->
		<div id="documentViewer" style="height: 600px;"></div>
	</aos:body>
	<aos:onready>
		<aos:viewport layout="border">
			<aos:gridpanel id="_g_last_examine" url="listexaminedescription.jhtml" region="north" split="true" pageSize="${pagesize }" hidePagebar="true"
						   enableLocking="false" height="200" onrender="_g_last_examine_show" onitemclick="_last_examine">
				<aos:selmodel type="row" mode="multi" />
				<aos:hiddenfield  name="tablename" id="tablename" value="${tablename}"/>
				<aos:hiddenfield  name="topic_id_" id="topic_id_" value="${topic_id_}"/>
				<aos:column type="rowno" />
				<aos:column dataIndex="id_" header="流水号" hidden="true" />
				<aos:column dataIndex="tablename" defaultValue="${tablename}" header="表明" hidden="true" />
				<aos:column header="流水号" dataIndex="id_" hidden="true" />
				<aos:column  dataIndex="write_flag" hidden="true" />
				<aos:column  dataIndex="first_flag" hidden="true" />
				<aos:column  dataIndex="next_flag" hidden="true" />
				<aos:column  dataIndex="last_flag" hidden="true" />
				<aos:column header="任务名称" dataIndex="task_name"  width="100" align="true"/>
				<aos:column header="撰稿编号" dataIndex="write_number"  width="100" align="true"/>
				<aos:column header="撰稿名称" dataIndex="write_name"  width="100" align="true"/>
				<aos:column header="撰稿时间" dataIndex="write_datatime" width="100" align="true" />
				<aos:column header="撰稿档案名称" dataIndex="tablename" width="100" align="true"/>
				<aos:column header="撰稿创建人" dataIndex="write_createperson"  width="100" align="true" />
				<aos:column header="撰稿描述" dataIndex="write_description"  width="200" align="true" />
				<aos:column header="档案编研描述" dataIndex="operate_description"  width="200" align="true" />
				<aos:column header="编研信息" dataIndex="compilation_message"  width="200" align="true" />
				<aos:column header="" flex="1" />
			</aos:gridpanel>
			<aos:panel region="east"  split="true" width="500">
				<aos:formpanel id="_f_description"  layout="column">
					<aos:textareafield name="compilation_message" id="compilation_message" allowBlank="false" width="750" height="150"  columnWidth="1.0" fieldLabel="编研信息"/>
				</aos:formpanel>
				<aos:docked dock="bottom" ui="footer">
					<aos:dockeditem xtype="tbfill" />
					<aos:dockeditem onclick="_last_save_compilation_details" text="执行并添加描述" icon="ok.png" />
				</aos:docked>
			</aos:panel>
			<aos:panel id="documentViewer2"  region="center" split="true" width="600" >
				<aos:formpanel id="_f_last_examine" width="600" layout="column" labelWidth="80" title="方案编研详情" bodyBorder="1 0 0 0">
					<aos:textfield fieldLabel="任务名称" name="task_name"   readOnly="true" columnWidth="0.49" />
					<aos:textfield fieldLabel="撰稿编号" name="write_number"   readOnly="true" columnWidth="0.49" />
					<aos:textfield fieldLabel="撰稿名称" name="write_name"   readOnly="true" columnWidth="0.49" />
					<aos:textfield fieldLabel="撰稿时间" name="write_datatime"   readOnly="true" columnWidth="0.49" />
					<aos:textfield fieldLabel="撰稿档案名称" name="tablename"   readOnly="true" columnWidth="0.49" />
					<aos:textfield fieldLabel="撰稿创建人" name="write_createperson"   readOnly="true" columnWidth="0.49" />
					<aos:textareafield fieldLabel="撰稿描述" name="write_description"   readOnly="true" columnWidth="0.49" />
					<aos:textareafield fieldLabel="档案编研描述" name="operate_description"   readOnly="true" columnWidth="0.49" />
					<aos:textareafield fieldLabel="编研信息" name="compilation_message"   readOnly="true" columnWidth="0.49" />
				</aos:formpanel>
			</aos:panel>

			<aos:window id="_w_last_compilation_message" title="总编辑审核说明" width="500" >
				<aos:formpanel  id="_f_last_compilation_message" width="480">
					<aos:textareafield fieldLabel="操作说明" id="last_compilation_message"  name="last_compilation_message" width="450" allowBlank="false"/>
				</aos:formpanel>
				<aos:docked dock="bottom" ui="footer">
					<aos:dockeditem xtype="tbfill" />
					<aos:dockeditem text="同意" onclick="_save_last_compilation_message" icon="agree.png" />
					<aos:dockeditem text="拒绝" onclick="no_save_last_compilation_message" icon="against.png" />
					<aos:dockeditem onclick="#_w_last_compilation_message.hide();" text="关闭" icon="close.png" />
				</aos:docked>
			</aos:window>


		</aos:viewport>
		<script type="text/javascript">
			function _g_last_examine_show(){
				var topic_id_=Ext.getCmp("topic_id_").getValue();
				var user="<%=session.getAttribute("user")%>";
				var tablename=Ext.getCmp("tablename").getValue();
				var params = {
					topic_id_: topic_id_,
					tablename:tablename
				};
				//这个Store的命名规则为：表格ID+"_store"。
				_g_last_examine_store.getProxy().extraParams = params;
				_g_last_examine_store.load(function(records, operation, success) {
					Ext.getCmp("_g_last_examine").getSelectionModel().select(Number(0), true);
					//默认选中第一个
					//并加载数据信息
					var se=Ext.getCmp("_g_last_examine").getStore().getAt(0).get("id_");
					if(!AOS.selectone(_g_last_examine)){
						return;
					}
					var record=AOS.selectone(_g_last_examine);
					_f_last_examine.loadRecord(record);

				});
				//此时在走后台查询当前合稿人的最近的编辑文字
				AOS.ajax({
					params: {
						type:0,
						user:user,
						topic_id_: topic_id_
					},
					url: 'getcompilationmessage.jhtml',
					ok: function (data) {
						Ext.getCmp("compilation_message").setValue(data.appmsg);
					}
				});
			}
			//电子文件加载方法
			function _last_examine() {
				var topic_id_=Ext.getCmp("topic_id_").getValue();
				var tablename=Ext.getCmp("tablename").getValue();
				if(!AOS.selectone(_g_last_examine)){
					return;
				}
				var record=AOS.selectone(_g_last_examine);
				_f_last_examine.loadRecord(record);

			}
			function _save_last_compilation_message(){
				//执行保存操作
				var tablename_gridData = JSON.stringify(Ext.pluck(_g_last_examine_store.data.items, 'data'));
				var user="<%=session.getAttribute("user")%>";
				AOS.ajax({
					url: 'savelastcompilationdetails.jhtml',
					forms:_f_last_compilation_message,
					params:{aos_rows_ : tablename_gridData,
						tablename:tablename.getValue(),
						compilation_message:Ext.getCmp("compilation_message").getValue(),
						topic_id_:topic_id_.getValue(),
						user:user
					},
					ok: function (data) {
						if(data.appcode===1){
							AOS.tip("操作成功!");
							var parenttab=parent.closetab();
							//针对纯的grid的刷新
							//parent.frames["_id_tab_f14e3d4567d045a789a5a8268634d5d4.iframe-frame"].refreshGrid();
							//刷新真个页面
							parent.frames["_id_tab_e276d2b79197449290304944af3f60ea.iframe-frame"].location.reload();
							var curtab = parenttab.getActiveTab();
							parenttab.remove(curtab);

						}else if(data.appcode===-1){
							AOS.tip("操作失败!");
						}
					}
				});
			}
			function no_save_last_compilation_message(){
				//执行保存操作
				var tablename_gridData = JSON.stringify(Ext.pluck(_g_last_examine_store.data.items, 'data'));
				var user="<%=session.getAttribute("user")%>";
				AOS.ajax({
					url: 'nosavelastcompilationdetails.jhtml',
					forms:_f_last_compilation_message,
					params:{aos_rows_ : tablename_gridData,
						tablename:tablename.getValue(),
						compilation_message:Ext.getCmp("compilation_message").getValue(),
						topic_id_:topic_id_.getValue(),
						user:user
					},
					ok: function (data) {
						if(data.appcode===1){
							AOS.tip("操作成功!");
							var parenttab=parent.closetab();
							//针对纯的grid的刷新
							//parent.frames["_id_tab_f14e3d4567d045a789a5a8268634d5d4.iframe-frame"].refreshGrid();
							//刷新真个页面
							parent.frames["_id_tab_e276d2b79197449290304944af3f60ea.iframe-frame"].location.reload();
							var curtab = parenttab.getActiveTab();
							parenttab.remove(curtab);
						}else if(data.appcode===-1){
							AOS.tip("操作失败!");
						}
					}
				});
			}
			function _last_save_compilation_details(){
				//此时把当前的信息全部保存到数据表中并添加信息
				_w_last_compilation_message.show();
				Ext.getCmp("last_compilation_message").setValue("");
			}
		</script>
	</aos:onready>
</aos:html>
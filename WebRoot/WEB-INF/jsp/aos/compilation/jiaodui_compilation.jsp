<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="/WEB-INF/tld/aos.tld" prefix="aos"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<aos:html>
<aos:head title="校对">
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
		<aos:gridpanel id="_g_account" url="listZg.jhtml" region="north" split="true" pageSize="${pagesize }" hidePagebar="true"
					   enableLocking="false" height="200" onrender="_g_account_query" onitemclick="_first_examine">
			<aos:selmodel type="row" mode="multi" />
			<aos:hiddenfield  name="tablename" id="tablename" value="${tablename}"/>
			<aos:hiddenfield  name="topic_id_" id="topic_id_" value="${zt_id}"/>
			<aos:hiddenfield  name="rw_id_" id="rw_id_" value="${rw_id_}"/>
			<aos:hiddenfield  name="byrwbh" id="byrwbh" value="${byrwbh}"/>
			<aos:hiddenfield  name="hguser" id="hguser" value="${hguser}"/>
			<aos:hiddenfield name="cascode_id_" id="cascode_id_"
							 value="${cascode_id_}" />
			<aos:hiddenfield name="aos_module_id_" id="aos_module_id_"
							 value="${aos_module_id_}" />
			<aos:hiddenfield  name="_path" id="_path"/>
			<aos:column type="rowno" />
			<aos:column dataIndex="id_" header="流水号" hidden="true" />
			<aos:column dataIndex="rw_id_"  header="任务名称" hidden="true"  />
			<aos:column dataIndex="tablename"  header="表明"  />
			<aos:column dataIndex="username"  header="撰稿人"  />
			<aos:column dataIndex="zg_path"  header="撰稿路径" hidden="true"  />
			<aos:column dataIndex="zg_description"  header="撰稿描述"  width="400" />
			<aos:column dataIndex="hg_description"  header="合稿描述"  width="400" />
			<aos:column dataIndex="jd_description"  header="校对描述"  width="400" />
			<aos:column dataIndex="zbj_description"  header="总编辑描述"  width="400" />
			<aos:column dataIndex="operate_time"  header="操作时间"  />
			<aos:column header="" flex="1" />
		</aos:gridpanel>
		<aos:panel region="east"  split="true" width="700">
			<aos:formpanel id="_f_description"  layout="column" title="合稿">
				<aos:htmleditor fieldLabel="合稿信息" anchor="80%" height="500" labelAlign="top"  name="compilation_hegao_message" id="compilation_hegao_message" columnWidth="1" allowBlank="false"/>
			</aos:formpanel>
			<aos:docked dock="bottom" ui="footer">
				<aos:dockeditem xtype="tbfill" />
				<aos:dockeditem onclick="_download_zhuangao_message" text="另存为" icon="save.png"  />
				<aos:dockeditem onclick="_next_save_compilation_details" text="上报" icon="ok.png" />
			</aos:docked>
		</aos:panel>

		<!--此时合稿展示撰稿的条目信息以及撰稿的详情内容，方便合稿-->
		<aos:panel id="documentViewer2"  region="center" split="true" width="700" >
			<aos:formpanel  id="_f_zhuangao_compilation_message" width="580" title="撰稿">
				<aos:htmleditor fieldLabel="撰稿信息" anchor="80%" height="500" labelAlign="top"  name="zhuangao_compilation_message" id="zhuangao_compilation_message" columnWidth="1" allowBlank="false"/>
			</aos:formpanel>
			<aos:docked dock="bottom" ui="footer">
				<aos:dockeditem xtype="tbfill" />
				<aos:dockeditem onclick="_show_archive" text="全文" icon="query.png" />
			</aos:docked>
		</aos:panel>

		<aos:window id="_w_next_compilation_message" title="校对人校对说明" width="500" >
			<aos:formpanel  id="_f_next_compilation_message" width="480" >
				<aos:textareafield fieldLabel="操作说明" id="next_compilation_message"  name="next_compilation_message" width="450" allowBlank="false"/>
			</aos:formpanel>
			<aos:docked dock="bottom" ui="footer">
				<aos:dockeditem xtype="tbfill" />
				<aos:dockeditem text="同意" onclick="_save_next_compilation_message" icon="agree.png" />
				<aos:dockeditem text="拒绝" onclick="no_save_next_compilation_message" icon="against.png" />
				<aos:dockeditem onclick="#_w_next_compilation_message.hide();" text="关闭" icon="close.png" />
			</aos:docked>
		</aos:window>
		<aos:window id="_w_open_path" title="全文信息" width="600" height="600" onshow="load_path_" >
			<aos:panel id="documentViewer3"   height="600" split="true" width="600" contentEl="documentViewer">

			</aos:panel>
		</aos:window>
		<aos:window id="_w_select_archive" title="编研数据" onshow="_w_select_archive_onshow" width="800" height="500">
			<aos:gridpanel id="_g_select_archive" url="listAccounts.jhtml" region="center" width="770" height="460"
						   autoScroll="true" pageSize="20" enableLocking="true" >
				<aos:docked forceBoder="0 0 1 0">
					<aos:dockeditem xtype="tbtext" text="数据列表" />
					<aos:dockeditem  text="全文" icon="picture.png"
									 onclick="open_path_" />
				</aos:docked>
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
	</aos:viewport>
	<script type="text/javascript">
		function open_path_(){
			var selection = AOS.selection(_g_select_archive, 'id_');
			if (AOS.empty(selection)) {
				AOS.tip('查看全文前请先选中数据。');
				return;
			}

			_w_open_path.show();
		}
		function _w_select_archive_onshow(){

			var params = {
				zt_id:Ext.getCmp("topic_id_").getValue(),
				rw_id_:Ext.getCmp("rw_id_").getValue(),
				tablename : Ext.getCmp("tablename").getValue(),
				byrwbh:Ext.getCmp("byrwbh").getValue()
			};
			//这个Store的命名规则为：表格ID+"_store"。
			_g_select_archive_store.getProxy().extraParams = params;
			_g_select_archive_store.load();
		}
		function _show_archive(){
			_w_select_archive.show();
		}
		function _download_zhuangao_message(){
			var compilation_message=Ext.getCmp("compilation_hegao_message").getValue();
			var topic_id_=Ext.getCmp("topic_id_").getValue();
			AOS.ajax({
				url : 'fillReport.jhtml',
				params:{
					compilation_message:compilation_message
				},
				ok : function(data) {
					//AOS.file('${cxt}/report/xls.jhtml');
					AOS.file('downloadcompilation.jhtml?id_='+topic_id_);
				}
			});

		}
		function _g_account_query(){
			var topic_id_=Ext.getCmp("topic_id_").getValue();
			var rw_id_=Ext.getCmp("rw_id_").getValue();
			var user="<%=session.getAttribute("user")%>";
			var tablename=Ext.getCmp("tablename").getValue();
			var params = {
				topic_id_: topic_id_,
				rw_id_:rw_id_,
				tablename:tablename
			};
			//这个Store的命名规则为：表格ID+"_store"。
			_g_account_store.getProxy().extraParams = params;
			_g_account_store.load(function(records, operation, success) {
				Ext.getCmp("_g_account").getSelectionModel().select(Number(0), true);
				//默认选中第一个
				//并加载数据信息
				var se = Ext.getCmp("_g_account").getStore().getAt(0).get("id_");
				if (!AOS.selectone(_g_account)) {
					return;
				}
				var record = AOS.selectone(_g_account);
				var zg_path=record.data.zg_path;
				AOS.ajax({
					params: {
						zg_path: zg_path
					},
					url: 'getzhuangaomessage.jhtml',
					ok: function (data) {
						Ext.getCmp("zhuangao_compilation_message").setValue(data.appmsg);
					}
				});

			});
			AOS.ajax({
				params: {
					user:"<%=session.getAttribute("user")%>",
					rw_id_:rw_id_,
					topic_id_: topic_id_
				},
				url: 'gethegaomessage.jhtml',
				ok: function (data) {
					Ext.getCmp("compilation_hegao_message").setValue(data.hegaomessage);
				}
			});
		}
		function _g_first_examine_show(){
			var topic_id_=Ext.getCmp("topic_id_").getValue();
			var user="<%=session.getAttribute("user")%>";
			var tablename=Ext.getCmp("tablename").getValue();
			var params = {
				topic_id_: topic_id_,
				tablename:tablename
			};
			//这个Store的命名规则为：表格ID+"_store"。
			_g_first_examine_store.getProxy().extraParams = params;
			_g_first_examine_store.load(function(records, operation, success) {
				Ext.getCmp("_g_first_examine").getSelectionModel().select(Number(0), true);
				//默认选中第一个
				//并加载数据信息
				var se=Ext.getCmp("_g_first_examine").getStore().getAt(0).get("id_");
				if(!AOS.selectone(_g_first_examine)){
					return;
				}
				var record=AOS.selectone(_g_first_examine);
				_f_first_examine.loadRecord(record);

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
		function _first_examine() {
			var topic_id_=Ext.getCmp("topic_id_").getValue();
			var tablename=Ext.getCmp("tablename").getValue();
			if(!AOS.selectone(_g_account)){
				return;
			}
			var record = AOS.selectone(_g_account);
			var zg_path=record.data.zg_path;
			AOS.ajax({
				params: {
					zg_path: zg_path
				},
				url: 'getzhuangaomessage.jhtml',
				ok: function (data) {
					Ext.getCmp("zhuangao_compilation_message").setValue(data.appmsg);
				}
			});


		}
		function _save_first_compilation_message(){
			//执行保存操作
			//var tablename_gridData = JSON.stringify(Ext.pluck(_g_first_examine_store.data.items, 'data'));
			var user="<%=session.getAttribute("user")%>";
			AOS.ajax({
				url: 'savefirstcompilationdetails.jhtml',
				forms:_f_first_compilation_message,
				params:{
					//aos_rows_ : tablename_gridData,
					tablename:tablename.getValue(),
					compilation_message:Ext.getCmp("compilation_hegao_message").getValue(),
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
						//parent.frames["_id_tab_e276d2b79197449290304944af3f60ea.iframe-frame"].location.reload();
						var curtab = parenttab.getActiveTab();
						parenttab.remove(curtab);

					}else if(data.appcode===-1){
						AOS.tip("操作失败!");
					}
				}
			});
		}
		function _save_next_compilation_message(){
			//执行保存操作
			//var tablename_gridData = JSON.stringify(Ext.pluck(_g_next_examine_store.data.items, 'data'));
			var user="<%=session.getAttribute("user")%>";
			var cascode_id_=Ext.getCmp("cascode_id_").getValue();
			var aos_module_id_=Ext.getCmp("aos_module_id_").getValue();
			AOS.ajax({
				url: 'savenextcompilationdetails.jhtml',
				forms:_f_next_compilation_message,
				params:{
					//aos_rows_ : tablename_gridData,
					tablename:tablename.getValue(),
					cascode_id_:cascode_id_,
					aos_module_id_:aos_module_id_,
					compilation_message:Ext.getCmp("compilation_hegao_message").getValue(),
					topic_id_:topic_id_.getValue(),
					rw_id_:rw_id_.getValue(),
					user:user,
					hguser:hguser.getValue()
				},
				ok: function (data) {
					if(data.appcode===1){
						AOS.tip("操作成功!");
						var parenttab=parent.closetab();
						//针对纯的grid的刷新
						//parent.frames["_id_tab_f14e3d4567d045a789a5a8268634d5d4.iframe-frame"].refreshGrid();
						//刷新真个页面
						//parent.frames["_id_tab_e276d2b79197449290304944af3f60ea.iframe-frame"].location.reload();
						var curtab = parenttab.getActiveTab();
						parenttab.remove(curtab);

					}else if(data.appcode===-1){
						AOS.tip("操作失败!");
					}
				}
			});
		}
		function no_save_next_compilation_message(){
			//执行保存操作
			//var tablename_gridData = JSON.stringify(Ext.pluck(_g_next_examine_store.data.items, 'data'));
			var user="<%=session.getAttribute("user")%>";
			var cascode_id_=Ext.getCmp("cascode_id_").getValue();
			var aos_module_id_=Ext.getCmp("aos_module_id_").getValue();
			AOS.ajax({
				url: 'nosavenextcompilationdetails.jhtml',
				forms:_f_next_compilation_message,
				params:{
					//aos_rows_ : tablename_gridData,
					tablename:tablename.getValue(),
					cascode_id_:cascode_id_,
					aos_module_id_:aos_module_id_,
					compilation_message:Ext.getCmp("compilation_hegao_message").getValue(),
					topic_id_:topic_id_.getValue(),
					rw_id_:rw_id_.getValue(),
					user:user,
                    hguser:hguser.getValue()
				},
				ok: function (data) {
					if(data.appcode===1){
						AOS.tip("操作成功!");
						var parenttab=parent.closetab();
						//针对纯的grid的刷新
						//parent.frames["_id_tab_f14e3d4567d045a789a5a8268634d5d4.iframe-frame"].refreshGrid();
						//刷新真个页面
						//parent.frames["_id_tab_e276d2b79197449290304944af3f60ea.iframe-frame"].location.reload();
						var curtab = parenttab.getActiveTab();
						parenttab.remove(curtab);
					}else if(data.appcode===-1){
						AOS.tip("操作失败!");
					}
				}
			});
		}
		function _next_save_compilation_details(){
			//此时把当前的信息全部保存到数据表中并添加信息
			_w_next_compilation_message.show();
			Ext.getCmp("next_compilation_message").setValue("");
		}
		//得到撰稿信息
		function zhuangao_compilation_message_show() {
			_w_zhuangao_compilation_message.show();
		}

		function zhuangao_compilation_load(){
			var topic_id_=Ext.getCmp("topic_id_").getValue();
			var user="<%=session.getAttribute("user")%>";
			var tablename=Ext.getCmp("tablename").getValue();
			AOS.ajax({
				params: {
					type:1,
					topic_id_: topic_id_
				},
				url: 'getcompilation_zhuagao_message.jhtml',
				ok: function (data) {
					_f_zhuangao_compilation_message.getForm().findField('zhuangao_compilation_message').setValue(data.appmsg);
				}
			});
		}
		function load_path_(){
			document.getElementById('documentViewer3').innerHTML = "";
			var id_=AOS.selectone(_g_select_archive).raw.id_;
			AOS.ajax({
				params : {
					id_ : id_,
					tablename:Ext.getCmp("tablename").getValue(),
					topic_id_:Ext.getCmp("topic_id_").getValue()
				},
				url : 'getpath.jhtml',
				ok : function(data) {
					//将id值赋给hiddlen中
					if (data.urlpath == "" || data.urlpath == null) {
						Ext.getCmp("_path").setValue("");
						document.getElementById('documentViewer3').innerHTML = "";
					} else {
						//new PDFObject({ url:data.pdfpath}).embed("documentViewer");

						Ext.getCmp("_path").setValue(data.pdfpath);
						PDFObject.embed(data.urlpath, '#documentViewer3');
					}
				}
			});
		}
	</script>
</aos:onready>
</aos:html>
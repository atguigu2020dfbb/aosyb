<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="/WEB-INF/tld/aos.tld" prefix="aos"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<aos:html>
<aos:head title="撰稿编研">
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
		<aos:gridpanel id="_g_account" url="listAccounts2.jhtml" region="north" split="true" pageSize="${pagesize }"
					   enableLocking="false"  onrender="_g_account_query" onitemclick="path">
			<aos:selmodel type="row" mode="multi" />
			<aos:hiddenfield  name="tablename" id="tablename" value="${tablename}"/>
			<aos:hiddenfield name="cascode_id_" id="cascode_id_"
							 value="${cascode_id_}" />
			<aos:hiddenfield name="aos_module_id_" id="aos_module_id_"
							 value="${aos_module_id_}" />
			<aos:hiddenfield  name="topic_id_" id="topic_id_" value="${zt_id}"/>
			<aos:hiddenfield  name="rw_id_" id="rw_id_" value="${rw_id_}"/>
			<aos:hiddenfield  name="_path" id="_path" />
			<aos:column type="rowno" />
			<aos:column dataIndex="id_" header="流水号" hidden="true" />
			<aos:column dataIndex="tablename" defaultValue="${tablename}" header="表明" hidden="true" />
			<aos:column dataIndex="id_" header="流水号" hidden="true" />
			<aos:column header="全宗单位" dataIndex="qzdw" />
			<aos:column header="档号" dataIndex="dh" celltip="true" />
			<aos:column header="题名" dataIndex="tm"  width="80" />
			<aos:column header="年度" dataIndex="nd" />
			<aos:column header="保管期限" dataIndex="bgqx" />
			<aos:column header="形成时间" dataIndex="xcsj"  />
			<aos:column header="盒号" dataIndex="hh" />
			<aos:column header="备注" dataIndex="bz" flex="1" celltip="true" />
			<aos:column header="数据表" dataIndex="tablename"/>
			<aos:column header="" flex="1" />
		</aos:gridpanel>
		<aos:panel region="east" id="edit_message"  split="true" >
			<aos:formpanel id="_f_description"  layout="column">
				<aos:htmleditor fieldLabel="编研信息" anchor="80%" height="500" labelAlign="top"  name="compilation_message" id="compilation_message" allowBlank="false" columnWidth="1" />
			</aos:formpanel>
			<aos:docked dock="bottom" ui="footer">
				<aos:dockeditem xtype="tbfill" />
				<aos:dockeditem onclick="_download_zhuangao_message" text="另存为" icon="save.png" />
				<aos:dockeditem onclick="_write_ocr_compilation" text="ocr识别" icon="query.png" />
				<aos:dockeditem onclick="_write_save_compilation_details" text="报送" icon="ok.png" />
			</aos:docked>
		</aos:panel>
		<aos:panel id="documentViewer2"  region="center" split="true"  contentEl="documentViewer">

		</aos:panel>
		<aos:window id="_w_write_compilation_message" title="撰稿人说明" width="500" >
			<aos:formpanel  id="_f_write_compilation_message" width="480">
				<aos:textareafield fieldLabel="操作说明" id="write_compilation_message"  name="write_compilation_message" width="450" allowBlank="false"/>
			</aos:formpanel>
			<aos:docked dock="bottom" ui="footer">
				<aos:dockeditem xtype="tbfill" />
				<aos:dockeditem text="确定" onclick="_save_write_compilation_message" icon="ok.png" />
				<aos:dockeditem onclick="#_w_write_compilation_message.hide();" text="关闭"
								icon="close.png" />
			</aos:docked>
		</aos:window>
	</aos:viewport>
	<script type="text/javascript">
		window.onload=function() {
			Ext.getCmp("documentViewer2").setWidth(document.body.scrollWidth * (1/ 2));
			Ext.getCmp("edit_message").setWidth(document.body.scrollWidth * (1/ 2));
			Ext.getCmp("_g_account").setHeight(document.body.scrollHeight * (1/3));
		}
		function _download_zhuangao_message(){
			var compilation_message=Ext.getCmp("compilation_message").getValue();
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
		function write_archive_details(){
			popwin('worddocument.jsp',1000,600,1);
		}
		function _g_account_query(){
			var topic_id_=Ext.getCmp("topic_id_").getValue();

			var user="<%=session.getAttribute("user")%>";
			var tablename=Ext.getCmp("tablename").getValue();
			var params = {
				topic_id_: topic_id_,
				tablename:tablename
			};
			//这个Store的命名规则为：表格ID+"_store"。
			_g_account_store.getProxy().extraParams = params;
			_g_account_store.load(function(records, operation, success) {
				Ext.getCmp("_g_account").getSelectionModel().select(Number(0), true);
				//默认选中第一个
				//并加载数据信息
				var se=Ext.getCmp("_g_account").getStore().getAt(0).get("id_");
				if(!AOS.selectone(_g_account)){
					return;
				}
				var id_=AOS.selectone(_g_account).raw.id_;
				AOS.ajax({
					params : {
						id_ : id_,
						tablename:tablename,
						topic_id_:topic_id_
					},
					url : 'getpath.jhtml',
					ok : function(data) {
						//将id值赋给hiddlen中
						if (data.urlpath == "" || data.urlpath == null) {
							Ext.getCmp("_path").setValue("");
							document.getElementById('documentViewer').innerHTML = "";
						} else {
							//new PDFObject({ url:data.pdfpath}).embed("documentViewer");

							Ext.getCmp("_path").setValue(data.pdfpath);
							PDFObject.embed(data.urlpath, '#documentViewer');
						}
					}
				});
			});
			//此时在走后台查询当前合稿人的最近的编辑文字
			AOS.ajax({
				params: {
					type:1,
					user:user,
					topic_id_: topic_id_
				},
				url: 'getcompilation_zhuangao_message.jhtml',
				ok: function (data) {
					Ext.getCmp("compilation_message").setValue(data.appmsg);
				}
			});
		}
		//电子文件加载方法
		function path() {
			var topic_id_=Ext.getCmp("topic_id_").getValue();
			var tablename=Ext.getCmp("tablename").getValue();
			if(!AOS.selectone(_g_account)){
				return;
			}
			var id_=AOS.selectone(_g_account).raw.id_;
			AOS.ajax({
				params : {
					id_ : id_,
					tablename:tablename,
					topic_id_:topic_id_
				},
				url : 'getpath.jhtml',
				ok : function(data) {
					//将id值赋给hiddlen中
					if (data.urlpath == "" || data.urlpath == null) {
						Ext.getCmp("_path").setValue("");
						document.getElementById('documentViewer').innerHTML = "";
					} else {
						//new PDFObject({ url:data.pdfpath}).embed("documentViewer");

						Ext.getCmp("_path").setValue(data.pdfpath);
						PDFObject.embed(data.urlpath, '#documentViewer');
					}
				}
			});
		}
		function _write_ocr_compilation(){
			var _path=Ext.getCmp("_path").getValue();
			AOS.ajax({
				url: 'getOCR_compilation.jhtml',
				params: {
					_path: _path
				},
				ok: function (data) {
					Ext.getCmp("compilation_message").setValue(Ext.getCmp("compilation_message").getValue()+'\r\n'+data.appmsg);
				}
			});
		}
		function _save_write_compilation_message(){
			//执行保存操作
			//var tablename_gridData = JSON.stringify(Ext.pluck(_g_account_store.data.items, 'data'));
			var user="<%=session.getAttribute("user")%>";
			var cascode_id_=Ext.getCmp("cascode_id_").getValue();
			var aos_module_id_=Ext.getCmp("aos_module_id_").getValue();
			AOS.ajax({
				url: 'savewritecompilationdetails.jhtml',
				forms:_f_write_compilation_message,
				params:{
					//aos_rows_ : tablename_gridData,
					tablename:tablename.getValue(),
					compilation_message:Ext.getCmp("compilation_message").getValue(),
					topic_id_:topic_id_.getValue(),
					rw_id_:rw_id_.getValue(),
					cascode_id_:cascode_id_,
					aos_module_id_:aos_module_id_,
					user:user
				},
				ok: function (data) {
					if(data.appcode===1){
						AOS.tip("操作成功!");
						var parenttab=parent.closetab();
						//针对纯的grid的刷新
						//parent.frames["_id_tab_f14e3d4567d045a789a5a8268634d5d4.iframe-frame"].refreshGrid();
						//刷新真个页面
						//parent.frames["_id_tab_f14e3d4567d045a789a5a8268634d5d4.iframe-frame"].location.reload();
						var curtab = parenttab.getActiveTab();
						parenttab.remove(curtab);

					}else if(data.appcode===-1){
						AOS.tip("操作失败!");
					}
				}
			});
		}
		function _write_save_compilation_details(){
			//此时把当前的信息全部保存到数据表中并添加信息
			_w_write_compilation_message.show();
			Ext.getCmp("write_compilation_message").setValue("");
		}
	</script>
</aos:onready>
</aos:html>
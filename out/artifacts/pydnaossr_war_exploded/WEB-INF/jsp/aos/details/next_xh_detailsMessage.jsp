<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="/WEB-INF/tld/aos.tld" prefix="aos"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<aos:html>
	<aos:head title="档案详情(销毁鉴定)">
		<aos:include lib="ext" />
		<aos:base href="archive/checkup" />
		<aos:include js="${cxt}/static/pdfObject/pdfobject.js" />
	</aos:head>
	<aos:body>
		<!--html代码  -->
		<div id="documentViewer" style="height: 600px;"></div>
	</aos:body>
	<aos:onready>
		<aos:viewport layout="border">
			<aos:gridpanel id="_g_account" url="listAccounts2.jhtml" region="north" split="true" pageSize="${pagesize }"
						   enableLocking="true"  onrender="_g_account_query" onitemclick="path">
				<aos:selmodel type="checkbox" mode="multi" />
				<aos:hiddenfield name="_classtree" id="_classtree"
								 value="${_classtree}" />
				<aos:hiddenfield name="aos_module_id_" id="aos_module_id_"
								 value="${aos_module_id_}" />
				<aos:hiddenfield  name="tablename" id="tablename" value="${tablename}"/>
				<aos:hiddenfield  name="topic_id_" id="topic_id_" value="${zt_id}"/>

				<aos:hiddenfield  name="check_id_" id="check_id_" value="${check_id_}"/>

				<aos:hiddenfield  name="_path" id="_path" />
				<aos:hiddenfield  name="id_" id="id_" />
				<aos:hiddenfield  name="index" id="index" />
				<aos:menu>
					<aos:menuitem text="留存" onclick="!save_xh"   />
					<aos:menuitem text="销毁" onclick="!save_xh"  />
				</aos:menu>
				<aos:column dataIndex="id_" header="流水号" hidden="true" />
				<aos:column dataIndex="_xh" header="密级类型" hidden="true" />
				<aos:column dataIndex="tablename" defaultValue="${tablename}" header="表明" hidden="true" />
				<c:forEach var="field" items="${fieldDtos}">
					<aos:column dataIndex="${field.fieldenname}"
								header="${field.fieldcnname }" width="${field.dislen }"
								rendererField="field_type_" >
						<aos:textfield />
					</aos:column>
				</c:forEach>
				<aos:column header="" flex="1" />
			</aos:gridpanel>
			<aos:panel region="east" id="edit_message"  split="true" >
				<aos:formpanel id="_f_description" title="销毁" layout="column" >
					<aos:textfield id="dh" name="dh" fieldLabel="档号" columnWidth="0.5" width="300"/>
					<aos:textfield id="zrz" name="zrz" fieldLabel="责任者" columnWidth="0.5" width="300"/>
					<aos:textfield id="tm" name="tm" fieldLabel="题名" columnWidth="0.99"/>
					<aos:textfield id="wjbh" name="wjbh" fieldLabel="文件序号" columnWidth="0.5"/>
					<aos:textfield id="cwrq" name="cwrq" fieldLabel="形成时间" columnWidth="0.5"/>
					<aos:textfield id="mj" name="mj" fieldLabel="密级" columnWidth="0.5"/>
					<aos:textfield id="ys" name="ys" fieldLabel="页数" columnWidth="0.5"/>
					<aos:textfield id="yh" name="yh" fieldLabel="页号" columnWidth="0.5"/>
					<aos:textfield id="dzhfs" name="dzhfs" fieldLabel="电子化幅数" columnWidth="0.5"/>
					<aos:textfield id="gdnd" name="gdnd" fieldLabel="归档年度" columnWidth="0.5"/>
					<aos:radioboxgroup fieldLabel="销毁类型"  id="xhleixing" columns="[100,120,100]">
						<aos:radiobox name="_xh" boxLabel="留存" />
						<aos:radiobox name="_xh" boxLabel="销毁" />
					</aos:radioboxgroup>

				</aos:formpanel>
				<aos:docked dock="bottom" ui="footer">
					<aos:dockeditem xtype="tbfill" />
					<aos:dockeditem text="保存" onclick="saveselect" icon="save.png" />
					<aos:dockeditem text="上条" onclick="lastselect" icon="more/go-previous-2.png" />
					<aos:dockeditem text="下条" onclick="nextselect" icon="more/go-next-2.png" />
					<aos:dockeditem onclick="_last_save_xh_details" text="提交终审" icon="ok.png" />
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
			<aos:window id="_w_next_xh_message" title="再审销毁鉴定说明" width="500" >
				<aos:formpanel  id="_f_next_xh_message" width="480">
					<aos:textareafield fieldLabel="操作说明" id="next_xh_message"  name="next_xh_message" width="450" allowBlank="false"/>
				</aos:formpanel>
				<aos:docked dock="bottom" ui="footer">
					<aos:dockeditem xtype="tbfill" />
					<aos:dockeditem text="同意" onclick="_save_next_xh_message" icon="agree.png" />
					<aos:dockeditem text="拒绝" onclick="no_save_next_xh_message" icon="against.png" />
					<aos:dockeditem onclick="#_w_next_xh_message.hide();" text="关闭"
									icon="close.png" />
				</aos:docked>
			</aos:window>
		</aos:viewport>
		<script type="text/javascript">
			//选中条目加载表单
			function _w_data_u_onshow(id_,tablename){
				AOS.ajax({
					params: {
						id_:id_,
						tablename: tablename
					},
					url: 'getData.jhtml',
					ok: function (data) {
						_f_description.form.setValues(data);
					}
				});
			}
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
					Ext.getCmp("index").setValue(0);
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
							radio_check();
							//字段信息传递过去
							_w_data_u_onshow(id_, tablename);
						}
					});
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
				AOS.ajax({
					url: 'savewritecompilationdetails.jhtml',
					forms:_f_write_compilation_message,
					params:{
						//aos_rows_ : tablename_gridData,
						tablename:tablename.getValue(),
						compilation_message:Ext.getCmp("compilation_message").getValue(),
						topic_id_:topic_id_.getValue(),
						rw_id_:rw_id_.getValue(),
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

			//上一条
			function lastselect(){
				if(typeof(AOS.selectone(_g_account))!='undefined'){
					//此时选中了。
					//判断是不是第一条
					var selectone=AOS.selectone(_g_account);
					var index=_g_account_store.indexOf(selectone);
					//此时是第一行
					if(index==0){
						//上一条不用操作了
						AOS.tip("已经是第一条了！");
					}else{
						//清空form表单
						Ext.getCmp('_f_description').form.reset();
						var tablename="<%=session.getAttribute("tablename")%>";
						AOS.ajax({
							params: {id_: _g_account_store.getAt(index-1).get("id_"),
								tablename:_g_account_store.getAt(index-1).get("tablename")
							},
							url: 'getpath.jhtml',
							ok: function (data) {
								//_f_data_u.form.setValues(data);
								//为隐藏域id_赋值
								if (data.urlpath == "" || data.urlpath == null) {
									Ext.getCmp("_path").setValue("");
									document.getElementById('documentViewer').innerHTML = "";
								} else {
									//new PDFObject({ url:data.pdfpath}).embed("documentViewer");

									Ext.getCmp("_path").setValue(data.pdfpath);
									PDFObject.embed(data.urlpath, '#documentViewer');
								}

								Ext.getCmp("id_").setValue(_g_account_store.getAt(index-1).get("id_"));
								Ext.getCmp("index").setValue(Number(index)+1);
								//选中当前行的上一行
								Ext.getCmp("_g_account").getSelectionModel().selectPrevious();
								radio_check();
							}
						});
						//字段信息传递过去
						_w_data_u_onshow(_g_account_store.getAt(index-1).get("id_"),_g_account_store.getAt(index-1).get("tablename"));
					}
				}
			}
			//下一条
			function nextselect(){
				if(typeof(AOS.selectone(_g_account))!='undefined'){
					//此时选中了。
					//判断是不是最后一条
					var selectone=AOS.selectone(_g_account);
					var index=_g_account_store.indexOf(selectone);
					//此时是最后一行
					if(Number(index)+1>=_g_account_store.getCount()){
						//下一条不用操作了
						AOS.tip("已经是最后一条了！");
					}else{
						//清空form表单
						Ext.getCmp('_f_description').form.reset();
						AOS.ajax({
							params: {id_: _g_account_store.getAt(index+1).get("id_"),
								tablename:_g_account_store.getAt(index+1).get("tablename")
							},
							url: 'getpath.jhtml',
							ok: function (data) {
								//_f_data_u.form.setValues(data);
								//为隐藏域id_赋值
								//将id值赋给hiddlen中
								if (data.urlpath == "" || data.urlpath == null) {
									Ext.getCmp("_path").setValue("");
									document.getElementById('documentViewer').innerHTML = "";
								} else {
									//new PDFObject({ url:data.pdfpath}).embed("documentViewer");

									Ext.getCmp("_path").setValue(data.pdfpath);
									PDFObject.embed(data.urlpath, '#documentViewer');
								}

								Ext.getCmp("id_").setValue(_g_account_store.getAt(index+1).get("id_"));
								Ext.getCmp("index").setValue(Number(index)+1);
								//选中当前行的下一行
								Ext.getCmp("_g_account").getSelectionModel().selectNext();
								radio_check();
							}
						});
						//字段信息传递过去
						_w_data_u_onshow(_g_account_store.getAt(index+1).get("id_"),_g_account_store.getAt(index+1).get("tablename"));
					}
				}
			}
			//电子文件加载方法
			function path() {
				//清空form表单
				Ext.getCmp('_f_description').form.reset();
				//此时判断点击了几个，
				var row=_g_account.getSelectionModel().getSelection();
				if(row.length>=2){
					return;
				}
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
						var selectone=AOS.selectone(_g_account);
						var index=_g_account_store.indexOf(selectone);
						Ext.getCmp("index").setValue(index);
						radio_check();
					}
				});
				//字段信息传递过去
				_w_data_u_onshow(AOS.selectone(_g_account).raw.id_,AOS.selectone(_g_account).raw.tablename);
			}
			function radio_check(){
				var xh=AOS.selectone(_g_account).raw._xh;
				var equRad=Ext.getCmp("xhleixing").getRefItems();
				for(var b=0;b<equRad.length;b++){
					if(equRad[b].boxLabel===xh){
						equRad[b].checked=true; //设置选中取值的修改
						equRad[b].setValue(true);//设置显示页面是否选中
					}else{
						equRad[b].checked=false;
						equRad[b].setValue(false);
					}
				}
			}
			function saveselect(){
				var row=_g_account.getSelectionModel().getSelection();
				if(row.length>=2){
					alert("此操作只能针对单条记录！");
					return;
				}
				var flag="";
				var equRad=Ext.getCmp("xhleixing").getRefItems();
				for (var i=0;i<equRad.length;i++) {
					if (equRad[i].checked) {
						flag=equRad[i].boxLabel;
						break;
					}
				}
//把这个值存放到数据库中
				var id_=AOS.selectone(_g_account).raw.id_;
				var tablename=AOS.selectone(_g_account).raw.tablename;
				AOS.ajax({
					forms:_f_description,
					params : {
						id_ : id_,
						tablename:tablename,
						selectvalue:flag
					},
					url : 'updatexh_lx.jhtml',
					ok : function(data) {

					}

				});
				setTimeout(function() {
					var tablename = AOS.selectone(_g_account).raw.tablename;
					var index = Ext.getCmp("index").getValue();
					var params = {
						index: Ext.getCmp("index").getValue()
					};
					//这个Store的命名规则为：表格ID+"_store"。
					//_g_account_store.getProxy().extraParams = params;
					_g_account_store.load(function (records, operation, success) {

						Ext.getCmp("_g_account").getSelectionModel().select(Number(index), true);
						var se = Ext.getCmp("_g_account").getStore().getAt(index).get("id_");
						if (!AOS.selectone(_g_account)) {
							return;
						}
						var id_ = AOS.selectone(_g_account).raw.id_;
						AOS.ajax({
							params: {
								id_: id_,
								tablename: tablename,
								topic_id_: topic_id_
							},
							url: 'getpath.jhtml',
							ok: function (data) {
								//将id值赋给hiddlen中
								if (data.urlpath == "" || data.urlpath == null) {
									Ext.getCmp("_path").setValue("");
									document.getElementById('documentViewer').innerHTML = "";
								} else {
									//new PDFObject({ url:data.pdfpath}).embed("documentViewer");
									Ext.getCmp("_path").setValue(data.pdfpath);
									PDFObject.embed(data.urlpath, '#documentViewer');
								}
								radio_check();
							}
						});
					});
				},500);
			}
			//多条开放
			function save_xh(obj){
				var row=_g_account.getSelectionModel().getSelection();
				var flag=obj.text.replace('<span class="app-normal">','').replace('</span>','');
				//把这个值存放到数据库中
				var selection = AOS.selection(_g_account, 'id_');
				var tablenames = AOS.selection(_g_account, 'tablename');
				AOS.ajax({
					params : {
						aos_rows_ : selection,
						tablenames:tablenames,
						selectvalue:flag
					},
					url : 'updatexh_lx_all.jhtml',
					ok : function(data) {
						if(data.appcode===1){
							AOS.tip("操作成功");
							_g_account_store.load();
							return;
						}else if(data.appcode===-1){
							AOS.tip("操作失败");
						}
					}

				});
			}

			//保存开放信息
			function _last_save_xh_details(){
				//此时把当前的信息全部保存到数据表中并添加信息
				_w_next_xh_message.show();
				Ext.getCmp("next_xh_message").setValue("鉴定复审已完毕，请终审。");
			}
			function _save_next_xh_message(){
				//执行保存操作
				//var tablename_gridData = JSON.stringify(Ext.pluck(_g_tablename_details_store.data.items, 'data'));
				var user="<%=session.getAttribute("user")%>";
				var _classtree=Ext.getCmp("_classtree").getValue();
				var aos_module_id_=Ext.getCmp("aos_module_id_").getValue();
				AOS.ajax({
					url: 'savenextxhdetails.jhtml',
					forms:_f_next_xh_message,
					params:{
						_classtree : _classtree,
						aos_module_id_:aos_module_id_,
						check_id_:check_id_.getValue(),
						user:user,
						next:1
					},
					ok: function (data) {
						if(data.appcode===1){
							AOS.tip("操作成功!");
							var parenttab=parent.closetab();
							//针对纯的grid的刷新
							//parent.frames["_id_tab_f14e3d4567d045a789a5a8268634d5d4.iframe-frame"].refreshGrid();
							//刷新整个页面
							//parent.frames["_id_tab_"+check_id_.getValue()+".iframe-frame"].location.reload();
							var curtab = parenttab.getActiveTab();
							parenttab.remove(curtab);
							_w_next_xh_message.hide();
						}else if(data.appcode===-1){
							AOS.tip("操作失败!");
						}
					}
				});
			}
			function _no_save_next_xh_message(){
				//执行保存操作
				//var tablename_gridData = JSON.stringify(Ext.pluck(_g_tablename_details_store.data.items, 'data'));
				var user="<%=session.getAttribute("user")%>";
				var _classtree=Ext.getCmp("_classtree").getValue();
				var aos_module_id_=Ext.getCmp("aos_module_id_").getValue();
				AOS.ajax({
					url: 'savenextxhdetails.jhtml',
					forms:_f_next_xh_message,
					params:{
						_classtree : _classtree,
						aos_module_id_:aos_module_id_,
						check_id_:check_id_.getValue(),
						user:user,
						next:-1
					},
					ok: function (data) {
						if(data.appcode===1){
							AOS.tip("操作成功!");
							var parenttab=parent.closetab();
							//针对纯的grid的刷新
							//parent.frames["_id_tab_f14e3d4567d045a789a5a8268634d5d4.iframe-frame"].refreshGrid();
							//刷新整个页面
							//parent.frames["_id_tab_"+check_id_.getValue()+".iframe-frame"].location.reload();
							var curtab = parenttab.getActiveTab();
							parenttab.remove(curtab);
							_w_next_xh_message.hide();
						}else if(data.appcode===-1){
							AOS.tip("操作失败!");
						}
					}
				});
			}
		</script>
	</aos:onready>
</aos:html>
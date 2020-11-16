<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="/WEB-INF/tld/aos.tld" prefix="aos"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<aos:html>
	<aos:head title="档案详情(开放鉴定)">
		<aos:include lib="ext" />
		<aos:base href="archive/checkup" />
		<aos:include js="${cxt}/static/pdfObject/pdfobject.js" />
		<style>
			.my_row_red .x-grid-cell {
				background-color: #99FF99;
			}
			.grid-one-column .x-grid-cell {
				background-color: #a6caf0;
			}
		</style>
	</aos:head>
	<aos:body>
		<!--html代码  -->
		<div id="documentViewer" style="height: 600px;"></div>
	</aos:body>
	<aos:onready>
		<aos:viewport layout="border">
			<aos:gridpanel id="_g_account" url="listAccounts2.jhtml" region="north" split="true" pageSize="${pagesize}"
						   enableLocking="true"  onrender="_g_account_query" rowclass="true" onitemclick="path">
				<aos:selmodel type="checkbox" mode="multi" />
				<aos:hiddenfield name="_classtree" id="_classtree"
								 value="${_classtree}" />
				<aos:hiddenfield name="aos_module_id_" id="aos_module_id_"
								 value="${aos_module_id_}" />
				<aos:hiddenfield  name="rowmath" id="rowmath" value="0" />
				<aos:hiddenfield  name="tablename" id="tablename" value="${tablename}"/>
				<aos:hiddenfield  name="topic_id_" id="topic_id_" value="${zt_id}"/>
				<aos:hiddenfield  name="check_id_" id="check_id_" value="${check_id_}"/>
				<aos:hiddenfield  name="_path" id="_path" />
				<aos:hiddenfield  name="id_" id="id_" />
				<aos:hiddenfield  name="index" id="index" />
				<aos:column dataIndex="id_" header="流水号" hidden="true" />
				<aos:column dataIndex="_kaifang" header="开放类型" hidden="true" />
				<aos:column dataIndex="tablename" defaultValue="${tablename}" header="表明" hidden="true" />
				<aos:selmodel type="checkbox" mode="multi" />
				<aos:column type="rowno" width="60"/>
				<aos:column header="数据表" dataIndex="tablename"/>

				<aos:column header="档号" dataIndex="dh"  width="180" />
				<aos:column header="责任者" dataIndex="zrz"  width="180" />
				<aos:column header="题名" dataIndex="tm"    width="400"/>
				<aos:column header="是否开放" dataIndex="sfkf" />
				<aos:column header="审查结果"  dataIndex="scjg"/>
				<aos:column header="页号" dataIndex="yh" />
				<aos:column header="页数" dataIndex="ys"  />
				<aos:column header="形成时间" dataIndex="cwrq" />
				<aos:column  dataIndex="wjbh" header="文件编号" width="200" />
				<aos:column header="全宗单位" dataIndex="qzdw" />
				<aos:column header="全宗号" dataIndex="qzh" />
				<aos:column header="目录号" dataIndex="mlh"  />
				<aos:column header="保管期限" dataIndex="bgqx" />
				<aos:column header="密级" dataIndex="mj" />
				<aos:column header="修改编号" dataIndex="xgbh" />
				<aos:column header="备注" dataIndex="bz" />
				<aos:column header="是否调卷处理" dataIndex="sfdjcl" />
				<aos:column header="案卷号" dataIndex="ajh" />
				<aos:column header="顺序号（件号）" dataIndex="sxh" />
				<aos:column header="终审意见" dataIndex="zsyj" />
				<aos:column header="复审结果" dataIndex="fsjg" hidden="true"/>
				<aos:column header="复审未通过理由" dataIndex="fswtgly" hidden="true"/>
				<aos:column header="终审结果" dataIndex="zsjg" hidden="true"/>
				<aos:column header="终审未通过理由" dataIndex="zswtgly" hidden="true"/>
				<aos:column header="" flex="1" />
			</aos:gridpanel>
			<aos:panel region="east" id="edit_message"  split="true" >
				<aos:formpanel id="_f_description" title="鉴定" layout="column" autoScroll="true" width="600">
					<aos:textfield id="dh" name="dh" fieldLabel="档号" columnWidth="0.49" />
					<aos:textfield id="wjbh" name="wjbh" fieldLabel="文件编号" columnWidth="0.49"/>
					<aos:textareafield id="tm" name="tm" fieldLabel="题名" columnWidth="0.98"/>
					<aos:textfield id="zrz" name="zrz" fieldLabel="责任者" columnWidth="0.49"/>
					<aos:textfield id="cwrq" name="cwrq" fieldLabel="形成时间" columnWidth="0.49"/>
					<aos:textfield id="ys" name="ys" fieldLabel="页数" columnWidth="0.49"/>
					<aos:textfield id="yh" name="yh" fieldLabel="页号" columnWidth="0.49"/>
					<aos:textfield id="mj" name="mj" fieldLabel="密级" columnWidth="0.49"/>
					<aos:textfield id="sfkf" name="sfkf" fieldLabel="是否开放" columnWidth="0.49"/>

					<aos:radioboxgroup fieldLabel="是否调卷处理" columnWidth="0.49" id="sfdjcl" columns="[100,100]">
						<aos:radiobox name="c3" boxLabel="是" />
						<aos:radiobox name="c3" boxLabel="否" />
					</aos:radioboxgroup>
					<aos:textfield id="xgbh" name="xgbh" fieldLabel="修改编号" columnWidth="0.49"/>
					<aos:textfield id="bz" name="bz" fieldLabel="备注" columnWidth="0.98"/>
					<aos:radioboxgroup fieldLabel="鉴定结果"  id="kaifangleixing" columnWidth="0.98" columns="[100,120,100]">
						<aos:radiobox name="c1" boxLabel="开放" />
						<aos:radiobox name="c1" boxLabel="不开放" />
						<aos:radiobox name="c1" boxLabel="控制" />
					</aos:radioboxgroup>
					<aos:radioboxgroup fieldLabel="审查结果"  id="shenchajieguo" columnWidth="0.98" columns="[100,120,100]">
						<aos:radiobox name="c2" boxLabel="开放" />
						<aos:radiobox name="c2" boxLabel="不开放" />
						<aos:radiobox name="c2" boxLabel="控制" />
					</aos:radioboxgroup>
					<aos:radioboxgroup fieldLabel="终审结果"  id="zhongshenjieguo" columnWidth="0.49"  columns="[100,100]">
						<aos:radiobox name="c3" boxLabel="通过" onchang="onchang"/>
						<aos:radiobox name="c3" boxLabel="未通过" onchang="onchang"/>
					</aos:radioboxgroup>
					<aos:textfield  id="zswtgly" name="zswtgly" fieldLabel="未通过理由" columnWidth="0.49"/>
					<aos:textfield id="fsyj" name="fsyj" fieldLabel="复审意见" columnWidth="0.98"/>
					<aos:textfield id="zsyj" name="zsyj" fieldLabel="终审意见" columnWidth="0.98"/>
				</aos:formpanel>
				<aos:docked dock="bottom" ui="footer">
					<aos:dockeditem xtype="tbfill" />

					<aos:dockeditem text="保存" onclick="saveselect1" icon="save.png" />
					<aos:dockeditem text="上一条" onclick="lastselect" />
					<aos:dockeditem text="下一条" onclick="nextselect"  />
					<aos:dockeditem onclick="_last_save_kf_details" text="保存并添加描述" icon="ok.png" />
					<aos:dockeditem text="结果替换" onclick="saveselect" icon="save.png" />
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
			<aos:window id="_w_last_kf_message" title="复核开放鉴定说明" width="500" >
				<aos:formpanel  id="_f_last_kf_message" width="480">
					<aos:textareafield fieldLabel="操作说明" id="last_kf_message"  name="last_kf_message" width="450" allowBlank="false"/>
				</aos:formpanel>
				<aos:docked dock="bottom" ui="footer">
					<aos:dockeditem xtype="tbfill" />
					<aos:dockeditem text="同意" onclick="_save_last_kf_message" icon="agree.png" />
					<aos:dockeditem text="拒绝" onclick="no_save_last_kf_message" icon="against.png" />
					<aos:dockeditem onclick="#_w_last_kf_message.hide();" text="关闭"
									icon="close.png" />
				</aos:docked>
			</aos:window>
		</aos:viewport>
		<script type="text/javascript">

			function changeRowClass(record, rowIndex, rowParams, store){

				//得到当前行的指定的列的值
				if(record.get("_path")>=1){
					return 'grid-one-column';
				}else{
					return 'grid-zero-column';
				}

			}
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
			//下一条
			function nextselect(){
				if(!booleanUpdate()){
					var msg = AOS.merge('你有未提交的数据，是否保存？');
					AOS.confirm(msg, function (btn) {
						if (btn === 'cancel') {
							AOS.tip('操作被取消。');
							return;
						}
						saveselect();
					});
				}else {
				if(typeof(AOS.selectone(_g_account))!='undefined') {
					//此时选中了。
					//判断是不是最后一条
					var selectone = AOS.selectone(_g_account);
					var index = _g_account_store.indexOf(selectone);
					//此时是最后一行
					if (Number(index) + 1 >= _g_account_store.getCount()) {
						//下一条不用操作了
						AOS.tip("已经是最后一条了！");
					} else {
						//清空form表单
						Ext.getCmp('_f_description').form.reset();
						AOS.ajax({
							params: {
								id_: _g_account_store.getAt(index + 1).get("id_"),
								tablename: _g_account_store.getAt(index + 1).get("tablename")
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

								Ext.getCmp("id_").setValue(_g_account_store.getAt(index + 1).get("id_"));
								Ext.getCmp("index").setValue(Number(index) + 1);
								//选中当前行的上一行
								Ext.getCmp("_g_account").getSelectionModel().selectNext();
								radio_check();
							}
						});
						//字段信息传递过去
						_w_data_u_onshow(_g_account_store.getAt(index + 1).get("id_"), _g_account_store.getAt(index + 1).get("tablename"));
					}
				}
				}
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
				if(!booleanUpdate()){
					var msg = AOS.merge('你有未提交的数据，是否保存？');
					AOS.confirm(msg, function (btn) {
						if (btn === 'cancel') {
							AOS.tip('操作被取消。');
							return;
						}
						saveselect();
					});
				}else {
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
			}
			function booleanUpdate(){
				//走后台判断下
				var form=Ext.getCmp("_f_description").getForm();
				var grid=AOS.selectone(_g_account);

				//档号
				if(form.findField("dh").getValue()!=grid.data.dh){
					return false;
				}
				//文件编号
				if(form.findField("wjbh").getValue()!=grid.data.wjbh){
					return false;
				}
				//题名
				if(form.findField("tm").getValue()!=grid.data.tm){
					return false;
				}
				//责任者
				if(form.findField("zrz").getValue()!=grid.data.zrz){
					return false;
				}
				//形成时间
				if(form.findField("cwrq").getValue()!=grid.data.cwrq){
					return false;
				}
				//页数
				if(form.findField("ys").getValue()!=grid.data.ys){
					return false;
				}
				//页号
				if(form.findField("yh").getValue()!=grid.data.yh){
					return false;
				}
				//密级
				if(form.findField("mj").getValue()!=grid.data.mj){
					return false;
				}
				//是否开放
				if(form.findField("sfkf").getValue()!=grid.data.sfkf){
					return false;
				}

				//修改编号
				if(form.findField("xgbh").getValue()!=grid.data.xgbh){
					return false;
				}
				//备注
				if(form.findField("bz").getValue()!=grid.data.bz){
					return false;
				}
				var sfdjcl="";
				var sfCheck = Ext.getCmp('sfdjcl').getRefItems();
				for(var i = 0; i < sfCheck.length; i++){
					if(sfCheck[i].checked){
						sfdjcl=sfCheck[i].boxLabel;
						break;
					}
				}
				//
				//是否调卷处理
				if(sfdjcl!=grid.data.sfdjcl){
					return false;
				}
				var cjjg="";
				var cjCheck = Ext.getCmp('kaifangleixing').getRefItems();
				for(var i = 0; i < cjCheck.length; i++){
					if(cjCheck[i].checked){
						cjjg=cjCheck[i].boxLabel;
						break;
					}
				}
				//
				//初鉴结果
				var sfkf=grid.data.sfkf;
				if(sfkf!="开放"&&sfkf!="不开放"&&sfkf!="控制"){
					sfkf="";
				}
				if(cjjg!=sfkf){
					return false;
				}

				var scjg="";
				var scCheck = Ext.getCmp('shenchajieguo').getRefItems();
				for(var i = 0; i < scCheck.length; i++){
					if(scCheck[i].checked){
						scjg=scCheck[i].boxLabel;
						break;
					}
				}
				//
				//审查结果
				if(scjg!=grid.data.scjg){
					return false;
				}
				var zsjg="";
				var zsCheck = Ext.getCmp('zhongshenjieguo').getRefItems();
				for(var i = 0; i < zsCheck.length; i++){
					if(zsCheck[i].checked){
						zsjg=zsCheck[i].boxLabel;
						break;
					}
				}
				//
				//终审结果
				if(zsjg!=grid.data.zsjg){
					return false;
				}

				//复审未通过理由
				if(form.findField("zswtgly").getValue()!=grid.data.zswtgly){
					return false;
				}
				//复审意见
				if(form.findField("zsyj").getValue()!=grid.data.zsyj){
					return false;
				}
				return true;
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
				itemclick();
			}
			function radio_check(){
				var data=AOS.selectone(_g_account).raw;
				var sfCheck = Ext.getCmp('kaifangleixing').items;
				for(var i = 0; i < sfCheck.length; i++){
					if(data.sfkf.indexOf(sfCheck.get(i).boxLabel)!=-1){
						sfCheck.get(i).setValue(true);
					}
				}
				_f_description.form.findField("sfkf").setValue(data.sfkf);
				var kfCheck = Ext.getCmp('shenchajieguo').items;
				for(var i = 0; i < kfCheck.length; i++){
					if(data.scjg==kfCheck.get(i).boxLabel){
						kfCheck.get(i).setValue(true);
					}
				}
				var xqCheck = Ext.getCmp('sfdjcl').items;
				for(var i = 0; i < xqCheck.length; i++){
					if(data.sfdjcl!=null&&data.sfdjcl!=""){
						if(data.sfdjcl==xqCheck.get(i).boxLabel){
							xqCheck.get(i).setValue(true);
						}
					}
				}
				var zsCheck = Ext.getCmp('zhongshenjieguo').items;
				for(var i = 0; i < zsCheck.length; i++){
					if(data.zsjg!=null&&data.zsjg!=""){
						if(data.zsjg==zsCheck.get(i).boxLabel){
							zsCheck.get(i).setValue(true);
						}
					}
				}

			}

			function onchang(){
				var scjg="";
				var scCheck = Ext.getCmp('zhongshenjieguo').getRefItems();
				for(var i = 0; i < scCheck.length; i++){
					if(scCheck[i].checked){
						scjg=scCheck[i].boxLabel;
						break;
					}
				}
				if(scjg==null||scjg==""){
					Ext.getCmp("zswtgly").show();
				}else if(scjg=="未通过"){
					Ext.getCmp("zswtgly").show();
				}else{
					Ext.getCmp("zswtgly").hide();
				}

			}
			function saveselect(){
				var selection = AOS.selection(_g_account, 'id_');
				if (AOS.empty(selection)) {
					AOS.tip('请选中操作的数据!');
					return;
				}
				var tablename=Ext.getCmp("tablename").getValue();
				//当前页
				//var currentPage=_g_data_store.currentPage;
				//页面大小（每页多少条）
				var pagesize=_g_account_store.getCount();
				var row=_g_account.getSelectionModel().getSelection();
				var topic_id_=Ext.getCmp("topic_id_").getValue();
				if(row.length==pagesize){
					AOS.ajax({
						url: 'updateAllkaifang.jhtml',
						params: {
							tablename:tablename,
							topic_id_:topic_id_
						},
						ok: function (data) {
							if(data.appcode===1){
								AOS.tip("操作完成!");
								_g_account_store.reload();
							}else{
								AOS.tip("操作失败!");
							}

						}
					});
				}else{
					AOS.ajax({
						url: 'updatekaifang.jhtml',
						params: {
							tablename:tablename,
							aos_rows_: selection
						},
						ok: function (data) {
							if(data.appcode===1){
								AOS.tip("操作完成!");
								_g_account_store.reload();
							}else{
								AOS.tip("操作失败!");
							}
						}
					});
				}

			}
			function saveselect1(){
				var row=_g_account.getSelectionModel().getSelection();
				if(row.length>=2){
					alert("此操作只能针对单条记录！");
					return;
				}
				var flag="";
				var equRad=Ext.getCmp("kaifangleixing").getRefItems();
				for (var i=0;i<equRad.length;i++) {
					if (equRad[i].checked) {
						flag=equRad[i].boxLabel;
						break;
					}
				}
				var scjg="";
				var scCheck = Ext.getCmp('shenchajieguo').getRefItems();
				for(var i = 0; i < scCheck.length; i++){
					if(scCheck[i].checked){
						scjg=scCheck[i].boxLabel;
						break;
					}
				}
				var sfdjcl="";
				var sfCheck = Ext.getCmp('sfdjcl').getRefItems();
				for(var i = 0; i < sfCheck.length; i++){
					if(sfCheck[i].checked){
						sfdjcl=sfCheck[i].boxLabel;
						break;
					}
				}
				var zsjg="";
				var zsCheck = Ext.getCmp('zhongshenjieguo').getRefItems();
				for(var i = 0; i < zsCheck.length; i++){
					if(zsCheck[i].checked){
						zsjg=zsCheck[i].boxLabel;
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
						scjg:scjg,
						zsjg:zsjg,
						sfdjcl:sfdjcl,
						tablename:tablename,
						selectvalue:flag
					},
					url : 'updatekaifang_lx.jhtml',
					ok : function(data) {

					}

				});
				setTimeout(function(){
						var tablename=AOS.selectone(_g_account).raw.tablename;
						var index=Ext.getCmp("index").getValue();
						var params = {
							index: Ext.getCmp("index").getValue()
						};
						//这个Store的命名规则为：表格ID+"_store"。
						//_g_account_store.getProxy().extraParams = params;
						_g_account_store.load(function (records, operation, success) {

							Ext.getCmp("_g_account").getSelectionModel().select(Number(index), true);
							var se=Ext.getCmp("_g_account").getStore().getAt(index).get("id_");
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
								}
							});
						});
				}, 500);

			}

			//多条开放
			function save_kaifang(){
				var row=_g_account.getSelectionModel().getSelection();
				var flag=obj.text.replace('<span class="app-normal">','').replace('</span>','');
				//把这个值存放到数据库中
				var selection = AOS.selection(_g_account, 'id_');
				var tablenames = AOS.selection(_g_account, 'tablename');
				AOS.ajax({
					params : {
						aos_rows_ : selection,
						tablenames:tablenames,
						selectvalue:"开放"
					},
					url : 'updatekaifang_lx_all.jhtml',
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
			//多条不开放
			function save_bukaifang(){
				var row=_g_account.getSelectionModel().getSelection();
				var flag="不开放";
				var equRad=Ext.getCmp("kaifangleixing").getRefItems();
				for (var i=0;i<equRad.length;i++) {
					if (equRad[i].checked) {
						flag=equRad[i].boxLabel;
						break;
					}
				}
				//把这个值存放到数据库中
				var selection = AOS.selection(_g_account, 'id_');
				var tablenames = AOS.selection(_g_account, 'tablename');
				AOS.ajax({
					params : {
						aos_rows_ : selection,
						tablenames:tablenames,
						selectvalue:"不开放"
					},
					url : 'updatekaifang_lx_all.jhtml',
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
			//多条控制
			function save_kongzhi(){
				var row=_g_account.getSelectionModel().getSelection();
				var flag="控制";
				var equRad=Ext.getCmp("kaifangleixing").getRefItems();
				for (var i=0;i<equRad.length;i++) {
					if (equRad[i].checked) {
						flag=equRad[i].boxLabel;
						break;
					}
				}
				//把这个值存放到数据库中
				var selection = AOS.selection(_g_account, 'id_');
				var tablenames = AOS.selection(_g_account, 'tablename');
				AOS.ajax({
					params : {
						aos_rows_ : selection,
						tablenames:tablenames,
						selectvalue:"控制"
					},
					url : 'updatekaifang_lx_all.jhtml',
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
			function _last_save_kf_details(){
				//此时把当前的信息全部保存到数据表中并添加信息
				_w_last_kf_message.show();
				Ext.getCmp("last_kf_message").setValue("");
			}
			function _save_last_kf_message(){
				//执行保存操作
				//var tablename_gridData = JSON.stringify(Ext.pluck(_g_tablename_details_store.data.items, 'data'));
				var user="<%=session.getAttribute("user")%>";
				var _classtree=Ext.getCmp("_classtree").getValue();
				var aos_module_id_=Ext.getCmp("aos_module_id_").getValue();
				AOS.ajax({
					url: 'savelastkfdetails.jhtml',
					forms:_f_last_kf_message,
					params:{
						_classtree : _classtree,
						aos_module_id_:aos_module_id_,
						check_id_:check_id_.getValue(),
						next:1,
						user:user
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
							_w_last_kf_message.hide();
						}else if(data.appcode===-1){
							AOS.tip("操作失败!");
						}
					}
				});
			}
			function no_save_last_kf_message(){
				//执行保存操作
				//var tablename_gridData = JSON.stringify(Ext.pluck(_g_tablename_details_store.data.items, 'data'));
				var user="<%=session.getAttribute("user")%>";
				var _classtree=Ext.getCmp("_classtree").getValue();
				var aos_module_id_=Ext.getCmp("aos_module_id_").getValue();
				AOS.ajax({
					url: 'savelastkfdetails.jhtml',
					forms:_f_last_kf_message,
					params:{
						_classtree : _classtree,
						aos_module_id_:aos_module_id_,

						check_id_:check_id_.getValue(),
						next:-1,
						user:user
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
							_w_last_kf_message.hide();
						}else if(data.appcode===-1){
							AOS.tip("操作失败!");
						}
					}
				});
			}
		</script>
	</aos:onready>
</aos:html>
<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="/WEB-INF/tld/aos.tld" prefix="aos"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<aos:html>
	<aos:head title="数据核检">
		<aos:include lib="ext" />
		<aos:base href="archive/data_sjhj" />
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
                <aos:hiddenfield id="treename" name="treename" value="${treename}"/>
                <aos:hiddenfield id="treenumber" name="treenumber" value="${treenumber}"/>
				<aos:hiddenfield name="aos_module_id_" id="aos_module_id_"  value="${aos_module_id_}" />
				<aos:hiddenfield name="aos_page_id_" id="aos_page_id_"  value="${aos_module_id_}" />
				<aos:hiddenfield  name="tablename" id="tablename" value="${tablename}"/>
				<aos:hiddenfield  name="_path" id="_path" />
				<aos:hiddenfield  name="id_" id="id_" />
				<aos:hiddenfield  name="index" id="index" />
				<aos:column dataIndex="id_" header="流水号" hidden="true"  locked="true"/>
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
			<aos:panel region="east" id="edit_message"  split="true" autoScroll="true">
				<aos:formpanel id="_f_description" title="数据核检" layout="absolute"  autoScroll="true">

				</aos:formpanel>
				<aos:docked dock="bottom" ui="footer">
					<aos:dockeditem xtype="tbfill" />
					<aos:dockeditem text="保存" onclick="saveselect" icon="save.png" />
					<aos:dockeditem text="上条" onclick="lastselect" icon="more/go-previous-2.png" />
					<aos:dockeditem text="下条" onclick="nextselect" icon="more/go-next-2.png" />
				</aos:docked>
			</aos:panel>
			<aos:panel id="documentViewer2"  region="center" split="true"  contentEl="documentViewer">
			</aos:panel>
		</aos:viewport>
		<script type="text/javascript">

			_w_data_input('_f_description');
			//生成录入界面
			function _w_data_input(formid){
				var _panel = Ext.getCmp(formid);
				_panel.removeAll();
				//_panel.reload();
				AOS.ajax({
					params: {tablename: tablename.getValue()},
					url: 'getInput.jhtml',
					ok: function (data) {
						//var _panel = Ext.getCmp(formid);
						var strdh ='';
						for (var j in data){
							//档号设置
							if(data[j].dh=='1'){
								var strfieldname = data[j].fieldname.substring(0,data[j].fieldname.length-1);
								if(typeof(data[j].dh1)!='undefined'){
									strdh = strfieldname+','+data[j].dh1;
									if(typeof(data[j].dh2)!='undefined'){
										strdh=strdh+','+data[j].dh2;
										if(typeof(data[j].dh3)!='undefined'){
											strdh=strdh+','+data[j].dh3;
											if(typeof(data[j].dh4)!='undefined'){
												strdh=strdh+','+data[j].dh4;
												if(typeof(data[j].dh5)!='undefined'){
													strdh=strdh+','+data[j].dh5;
													if(typeof(data[j].dh6)!='undefined'){
														strdh=strdh+','+data[j].dh6;
														if(typeof(data[j].dh7)!='undefined'){
															strdh=strdh+','+data[j].dh7;
														}
													}
												}
											}
										}
									}
								}
							}//判断dh
						}
						for(var i in data){

							var items;
							if(data[i].fieldname.charAt(data[i].fieldname.length - 1)=='L'){
								var labeltext=data[i].displayname;
								if(data[i].displayname!=null&&data[i].displayname.length>0){
									while(labeltext.indexOf(" ")!=-1){
										labeltext=labeltext.replace(" ","\xa0");
									}
								}
								//设置标签必录入项
								if(data[i].ynnnull=='0'){
									items=[{
										xtype : 'label',
										//value:data[i].displayname,
										text:labeltext,
										style:'color:red',
										width : parseInt(data[i].cwidth),
										height : parseInt(data[i].cheight),
										x:parseInt(data[i].cleft)-200,
										y:parseInt(data[i].ctop)-50,
									}]
								}else{
									items=[{
										xtype : 'label',
										//value:data[i].displayname,
										text:labeltext,
										width : parseInt(data[i].cwidth),
										height : parseInt(data[i].cheight),
										x:parseInt(data[i].cleft)-200,
										y:parseInt(data[i].ctop)-50,
									}]
								}
							}else{
								if(data[i].yndic=='1'){
									var ynnull;
									if(data[i].ynnnull==0){
										ynnull=false;
									}else{
										ynnull=true;
									}
									var strdicname=data[i].fieldname.substring(0,data[i].fieldname.length-1);
									items=[{
										name:data[i].fieldname.substring(0,data[i].fieldname.length-1),
										id:data[i].fieldname.substring(0,data[i].fieldname.length-1),
										//fieldLabel: 'ieldLabel',
										xtype: "combo",
										mode:'local',
										//fieldLabel:'数据字典',
										x:parseInt(data[i].cleft)-200,
										y:parseInt(data[i].ctop)-50,
										maxWidth:parseInt(data[i].cwidth),
										height:parseInt(data[i].cheight),
										allowBlank:ynnull,
										listeners:{
											select:function(e){
												if(strdh.indexOf(this.name)>-1){
													var strarray=strdh.split(',');
													var strtemp='';
													for(var i=1;i<strarray.length; i++){
														if(i==1){
															strtemp =Ext.getCmp(strarray[i]).getValue();
															continue;
														}
														strtemp = strtemp+'-'+Ext.getCmp(strarray[i]).getValue();
													}
													Ext.getCmp(strarray[0]).setValue(strtemp);
												}
											}
										},
										//labelWidth:80,
										store: new Ext.data.SimpleStore({
											fields: ["code_", "desc_"],
											proxy: {
												type: "ajax",
												//params:{"tablename":"3333"},
												url: "load_dic_index.jhtml?key_name_="+data[i].dic,
												actionMethods: {
													read: "POST"  //解决传中文参数乱码问题，默认为“GET”提交
												},
												reader: {
													type: "json",  //返回数据类型为json格式
													root: "root"  //数据
												}
											},
											autoLoad: false  //自动加载数据
										}),
										emptyText:'请选择...',
										displayField: 'desc_',
										valueField :'desc_',
										//hiddenName: 'fieldenname',
									}]
								}
								else{
									items = itemsGroup(data[i],strdh);
								}
							}
							_panel.add(items);
						}
					}
				});
			}

			function itemsGroup(data,strdh){
				var strx=parseInt(data.cleft)-200;
				var stry = parseInt(data.ctop)-50;
				var strwidth = parseInt(data.cwidth);
				var strheight=parseInt(data.cheight);
				var fieldname = data.fieldname.substring(0,data.fieldname.length-1);
				var ynnull;
				if(data.ynnnull==0){
					ynnull=false;
				}else{
					ynnull=true;
				}
				//var ynnull=data.ynnnull=='0';
				var str =[{
					xtype :'textfield',
					id:fieldname,
					name:fieldname,
					maxWidth :strwidth,
					height :strheight,
					x:strx,
					y:stry,
					maxLength:data.edtmax,
					allowBlank:ynnull,
					listeners:{focus:function(e){},
						blur:function(e){
							if(data.ynpw=='1'){
								var val=e.getValue();
								var len=val.length;
								while(len < data.edtmax) {
									val= "0" + val;
									len++;
								}
								e.setValue(val);
							}
							if(strdh.indexOf(this.name)>-1){
								var strarray=strdh.split(',');
								var strtemp='';
								//alert(strdh);
								for(var i=1;i<strarray.length; i++){
									//alert(strarray[i]);
									if(i==1){
										strtemp =Ext.getCmp(strarray[i]).getValue();
										continue;
									}
									strtemp=strtemp+'-'+Ext.getCmp(strarray[i]).getValue();

								}
								//alert(strtemp);
								Ext.getCmp(strarray[0]).setValue(strtemp);

							}
						}
						//离开鼠标事件结尾
					}
				}];
				//alert(str);
				//var item = eval('(' + str + ')');
				return str;
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
				Ext.getCmp("documentViewer2").setWidth(document.body.scrollWidth * (1/ 3));
				Ext.getCmp("edit_message").setWidth(document.body.scrollWidth * (2/ 3));
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
				var user="<%=session.getAttribute("user")%>";
				var tablename=Ext.getCmp("tablename").getValue();
				var treename=Ext.getCmp("treename").getValue();
                var treenumber=Ext.getCmp("treenumber").getValue();
				var params = {
					tablename:tablename,
                    treename:treename,
                    treenumber:treenumber
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
							tablename:tablename
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
							//字段信息传递过去
							_w_data_u_onshow(id_, tablename);
						}
					});
				});
			}

			function _save_write_compilation_message(){
				//执行保存操作
				//var tablename_gridData = JSON.stringify(Ext.pluck(_g_account_store.data.items, 'data'));
				var user="<%=session.getAttribute("user")%>";
				var _classtree=Ext.getCmp("_classtree").getValue();
				var aos_module_id_=Ext.getCmp("aos_module_id_").getValue();
				AOS.ajax({
					url: 'savewritecompilationdetails.jhtml',
					forms:_f_write_compilation_message,
					params:{
						//aos_rows_ : tablename_gridData,
						_classtree:_clastree,
						aos_module_id_:aos_module_id_,
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
							}
						});
						//字段信息传递过去
						_w_data_u_onshow(_g_account_store.getAt(index-1).get("id_"),Ext.getCmp("tablename").getValue());
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
								//选中当前行的上一行
								Ext.getCmp("_g_account").getSelectionModel().selectNext();
							}
						});
						//字段信息传递过去
						_w_data_u_onshow(_g_account_store.getAt(index+1).get("id_"),Ext.getCmp("tablename").getValue());
					}
				}
			}
			//电子文件加载方法
			function path() {
				//此时判断点击了几个，
				var row=_g_account.getSelectionModel().getSelection();
				if(row.length>=2){
					return;
				}
				var tablename=Ext.getCmp("tablename").getValue();
				if(!AOS.selectone(_g_account)){
					return;
				}
				var id_=AOS.selectone(_g_account).raw.id_;
				AOS.ajax({
					params : {
						id_ : id_,
						tablename:tablename
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
					}
				});
				//字段信息传递过去
				_w_data_u_onshow(AOS.selectone(_g_account).raw.id_,Ext.getCmp("tablename").getValue());
			}

			function saveselect(){
				var row=_g_account.getSelectionModel().getSelection();
				if(row.length>=2){
					alert("此操作只能针对单条记录！");
					return;
				}
                //把这个值存放到数据库中
				var id_=AOS.selectone(_g_account).raw.id_;
				var tablename=Ext.getCmp("tablename").getValue();
                var user="<%=session.getAttribute("user")%>";
				AOS.ajax({
					forms:_f_description,
					params : {
						id_ : id_,
                        hjr:user,
						tablename:tablename
					},
					url : 'updateData.jhtml',
					ok : function(data) {

					}

				});
				setTimeout(function() {
					var tablename = Ext.getCmp("tablename").getValue();
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
								tablename: tablename
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
							}
						});
					});
				},500);
			}
			//多条开放
			function save_jiazhi(obj){
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
					url : 'updatejiazhi_lx_all.jhtml',
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
			function _first_save_jiazhi_details(){
				//此时把当前的信息全部保存到数据表中并添加信息
				_w_first_jiazhi_message.show();
				Ext.getCmp("first_jiazhi_message").setValue("");
			}
			function _save_first_jiazhi_message(){
				//执行保存操作
				//var tablename_gridData = JSON.stringify(Ext.pluck(_g_tablename_details_store.data.items, 'data'));
				var user="<%=session.getAttribute("user")%>";
				var _classtree=Ext.getCmp("_classtree").getValue();
				var aos_module_id_=Ext.getCmp("aos_module_id_").getValue();
				AOS.ajax({
					url: 'savefirstbgqxdetails.jhtml',
					forms:_f_first_jiazhi_message,
					params:{
						_classtree : _classtree,
						aos_module_id_:aos_module_id_,
						//zt_id:zt_id.getValue(),
						check_id_:check_id_.getValue(),
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

						}else if(data.appcode===-1){
							AOS.tip("操作失败!");
						}
					}
				});
			}
		</script>
	</aos:onready>
</aos:html>
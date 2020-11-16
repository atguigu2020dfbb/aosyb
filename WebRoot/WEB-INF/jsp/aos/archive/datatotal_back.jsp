sharp.jsp<%@ page contentType="text/html; charset=utf-8" isELIgnored="false"%>
<%@ taglib uri="/WEB-INF/tld/aos.tld" prefix="aos"%>
<%@ taglib uri="/WEB-INF/tld/cll.tld" prefix="c"%>
<%
	String path = request.getContextPath();
	Object fieldDtos = request.getAttribute("fieldDtos");
	Object listtablename = request.getAttribute("listtablename");
%>
<aos:html>
	<aos:head title="回收站">
		<aos:include lib="ext" />
		<aos:base href="archive/data_back" />
	</aos:head>
	<aos:body>
	</aos:body>
	<aos:onready>
		<aos:viewport layout="border">
			<aos:gridpanel id="_g_data" url="listAccounts.jhtml" region="center"
						   autoScroll="true" enableLocking="true" pageSize="20"
						   onrender="_w_tablename_show" xtype="true">
				<aos:docked forceBoder="0 0 1 0">
					<aos:dockeditem xtype="tbtext" text="回收站" />
					<aos:hiddenfield name="tablename" id="tablename" />
					<aos:hiddenfield id="querySession" name="querySession" />
					<aos:combobox name="listTablename"
								  fields="[ 'tablename', 'tabledesc']" fieldLabel="数据表"
								  id="listTablename" columnWidth="0.3" url="listTablename.jhtml"
								  displayField="tabledesc" valueField="tablename" allowBlank="false"
								  onselect="_w_tablename_init" />
					<aos:dockeditem text="查询" icon="query.png" onclick="_w_query_show" />
					<aos:button text="删除" icon="del2.png" scale="small" margin="0 0 0 0">
						<aos:menu plain="false">
							<aos:menuitem text="单项清除" icon="del2.png" onclick="_g_data_del" />
							<aos:menuitem text="全部清除" icon="del.png"
										  onclick="_g_data_del_term" />
						</aos:menu>
					</aos:button>
					<aos:button text="还原" icon="more/document-revert-5.png" scale="small" margin="0 0 0 0">
						<aos:menu plain="false">
							<aos:menuitem text="单项还原" icon="more/document-revert-2.png" onclick="_g_data_return" />
							<aos:menuitem text="全部还原" icon="more/document-revert-5.png"
										  onclick="_g_data_return_term" />
						</aos:menu>
					</aos:button>
				</aos:docked>
				<aos:column type="rowno" />
				<aos:selmodel type="row" mode="multi" />
				<aos:column dataIndex="id_" header="流水号" hidden="true" />
				<c:forEach var="field" items="${fieldDtos}">
					<aos:column dataIndex="${field.fieldenname }"
								header="${field.fieldcnname }" rendererField="field_type_" />
				</c:forEach>
				<aos:column header="" flex="1" />
			</aos:gridpanel>
			<aos:window id="_w_query_q" title="查询" width="720" autoScroll="true"
						layout="fit">
				<aos:tabpanel id="_tabpanel" region="center" activeTab="0"
							  bodyBorder="0 0 0 0" tabBarHeight="30">
					<aos:tab title="列表式搜索" id="_tab_org">
						<aos:formpanel id="_f_query" layout="column" columnWidth="1">
							<aos:hiddenfield name="listtablename" value="${listtablename }" />
							<aos:hiddenfield name="columnnum" id="columnnum" value="7" />
							<c:forEach var="fieldss" items="${fieldDtos}" end="7"
									   varStatus="listSearch">
								<aos:combobox name="and${listSearch.count }" value="on"
											  labelWidth="10" columnWidth="0.2">
									<aos:option value="on" display="并且" />
									<aos:option value="up" display="或者" />
								</aos:combobox>
								<aos:combobox name="filedname${listSearch.count}"
											  emptyText="${fieldss.fieldcnname }" labelWidth="20"
											  columnWidth="0.2" fields="['fieldenname','fieldcnname']"
											  regexText="${fieldss.fieldenname }" displayField="fieldcnname"
											  valueField="fieldenname"
											  url="queryFields.jhtml?tablename=${listtablename }">
								</aos:combobox>
								<aos:combobox name="condition${listSearch.count }" value="like"
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
								<aos:textfield name="content${listSearch.count }"
											   allowBlank="true" columnWidth="0.39" />
							</c:forEach>
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

			<aos:window id="_w_my_borrow" title="调档表单" autoScroll="true"
						width="520" border="true" height="300" onshow="_w_my_borrow_show">
				<aos:formpanel id="_f_my_borrow" layout="column" autoScroll="true" width="500">
					<aos:textfield fieldLabel="调档编号" allowBlank="false" readOnly="true"
									   name="transfer_number" id="transfer_number" columnWidth="1" />
					<aos:textfield fieldLabel="调档门类" allowBlank="false" readOnly="true"
								   name="tablename"  columnWidth="1" />
						<aos:textfield name="transfer_purpose" fieldLabel="调档目的"
									   columnWidth="1" id="transfer_purpose"/>
						<aos:datefield name="transfer_time" fieldLabel="调档时间" id="transfer_time"   format="Y-m-d"
									   columnWidth="1"   allowBlank="false"/>
						<aos:textareafield name="transfer_description" id="transfer_description" fieldLabel="备注"
										   columnWidth="1" />
				</aos:formpanel>
				<aos:docked dock="bottom" ui="footer">
					<aos:dockeditem xtype="tbfill" />
					<aos:dockeditem xtype="button" text="确定" icon="ok.png"
									onclick="submit_transfer" />
					<aos:dockeditem xtype="button" text="关闭" icon="close.png"
									onclick="#_w_my_borrow.hide()" />
				</aos:docked>
			</aos:window>
			<aos:window id="_w_files" title="电子文件" autoScroll="true" width="800"
						border="true" height="500" onshow="get_files">
				<aos:gridpanel id="_g_files" region="east" url="getFiles.jhtml"
							   hidePagebar="true" split="true" width="800">
					<aos:docked forceBoder="0 0 1 0">
						<aos:dockeditem xtype="tbtext" text="电子文件列表" />
						<aos:dockeditem text="查看" icon="query.png" onclick="_w_files_look" />
					</aos:docked>
					<aos:column type="rowno" />
					<aos:column header="流水号" dataIndex="id_" hidden="true" />
					<aos:column header="档案号" dataIndex="tid" width="60" hidden="true" />
					<aos:column header="上传文件名" dataIndex="_path" width="90" />
					<aos:column header="路径" dataIndex="dirname" width="90" />
					<aos:column header="上传时间" dataIndex="sdatetime" width="90" />
					<aos:column header="文件名" dataIndex="_s_path" width="60" />
				</aos:gridpanel>
				<aos:docked dock="bottom" ui="footer">
					<aos:dockeditem xtype="tbfill" />
					<aos:dockeditem onclick="#_w_files.close();" text="关闭"
									icon="close.png" />
				</aos:docked>
			</aos:window>
			<aos:window id="_w_transfer_list" title="调档列表" autoScroll="true" width="800"
						border="true" height="500" onshow="get_transfer_list">
				<aos:gridpanel id="_g_transfer_list" region="east" url="getTransfer.jhtml"
							   hidePagebar="true" split="true" width="800">
					<aos:docked forceBoder="0 0 1 0">
						<aos:dockeditem xtype="tbtext" text="调档列表" />
						<aos:dockeditem text="档案详情" icon="query.png" onclick="_w_list_archive" />
					</aos:docked>
					<aos:column type="rowno" />
					<aos:column header="流水号" dataIndex="id_" hidden="true" />
					<aos:column header="调档编号" dataIndex="transfer_number" width="60" />
					<aos:column header="调档门类" dataIndex="tablename" width="90" />
					<aos:column header="调档目的" dataIndex="transfer_purpose" width="90" />
					<aos:column header="调档时间" dataIndex="transfer_time" width="90" />
					<aos:column header="备注" dataIndex="transfer_description" width="90" />
				</aos:gridpanel>
				<aos:docked dock="bottom" ui="footer">
					<aos:dockeditem xtype="tbfill" />
					<aos:dockeditem onclick="#_w_files.close();" text="关闭"
									icon="close.png" />
				</aos:docked>
			</aos:window>
		</aos:viewport>
		<script>
			//跳转主页
			function _w_tablename_init(){
				// alert(listtablename);
				var listTablename = Ext.getCmp("listTablename").value;
				var tablenamedesc = Ext.getCmp("listTablename").getRawValue();
				window.location.href="initData.jhtml?listtablename="+listTablename+"&tablenamedesc="+encodeURI(encodeURI(tablenamedesc));

			}
			//点击查询按钮，先进入查询表头信息
			function _w_tablename_show(){
				var listTablename ="<%=request.getParameter("listtablename")%>";
				var tablenamedesc ="<%=request.getAttribute("tablenamedesc")%>";
				var tablename=Ext.getCmp("tablename").getValue();
				if(listTablename==null||listTablename==""||listTablename=="null"){
					if(tablename!=null&&tablename!=""&&tablename!="null"){
						listTablename=tablename;
					}else{
						return;
					}
				}
				var params = {
					listtablename : listTablename
				};
				//这个Store的命名规则为：表格ID+"_store"。
				_g_data_store.getProxy().extraParams = params;
				_g_data_store.load();
				//让指定文本框赋值
				Ext.getCmp("listTablename").setValue(listTablename);
				Ext.getCmp("listTablename").setRawValue(tablenamedesc);
				Ext.getCmp("tablename").setValue(listTablename);
			}
			//申请到期日期计算
			function get_expire_date(){
				var borrow_date=new Date(Ext.getCmp("borrow_date").getValue()).getTime();
				//得到天数的毫秒值。进行计算
				var day=Ext.getCmp("borrow_day").getValue();
				var dayhmz=day*24*60*60*1000;
				var borrow_exprie_hmz=borrow_date+dayhmz;
				var birthday="";
				//把毫秒值计算出对应的年月日
				var time = new Date(borrow_exprie_hmz);
				if(time.getMonth()<10){
					birthday= time.getFullYear()+"-"+"0"+(time.getMonth()+1);
				}else{
					birthday= time.getFullYear()+"-"+(time.getMonth()+1);
				}
				if(time.getDate()<10){
					birthday= birthday+"-0"+time.getDate();
				}else{
					birthday= birthday+"-"+time.getDate();
				}
				Ext.getCmp("expire_date").setValue(birthday);
			}
			//档案借阅
			function _w_relet_load(){
				//数据借阅
				AOS.ajax({
					url: 'savejy.jhtml',
					forms:_f_relet,
					method:'post',
					ok: function (data) {
						if(data.appcode === -1){
							//让用户重新选择
							AOS.tip("操作成功!");
							_w_relet.hide();
							_g_data_store.reload();
						}else{
							AOS.tip("操作失败!");
						}
					}
				});
			}
			function loadDemo(){
				var tablename=Ext.getCmp('tableTemplate').value;
				var params = {
					tablename: tablename
				};
				_g_jy_store.getProxy().extraParams = params;
				_g_jy_store.load();

			}
			function _f_Excel_jy(){
				//导出日志
				AOS.ajax({
					url : 'fillReport.jhtml',
					ok : function(data) {
						AOS.file('${cxt}/report/xls2.jhtml');
					}
				});
			}
			function _w_query_show(){
				_w_query_q.show();
			}
			function _f_data_query(){
				var params = AOS.getValue('_f_query');
				var form = Ext.getCmp('_f_query');
				var tmp = columnnum.getValue();
				for(var i=1;i<=tmp;i++){
					var str = form.down("[name='filedname"+i+"']");
					var filedname =str.getValue();
					if(filedname==null){
						params["filedname"+i]=str.regexText;
					}
				}
				var listTablename ="<%=request.getParameter("listtablename")%>";
				var tablenamedesc ="<%=request.getAttribute("tablenamedesc")%>";
				var tablename=Ext.getCmp("tablename").getValue();
				if(listTablename==null||listTablename==""||listTablename=="null"){
					if(tablename!=null&&tablename!=""&&tablename!="null"){
						listTablename=tablename;
					}else{
						return;
					}
				}
				//这个Store的命名规则为：表格ID+"_store"。
				_g_data_store.getProxy().extraParams = params;
				//_g_data_store.getProxy().extraParams = param;
				_g_data_store.load();
				_w_query_q.hide();
				AOS.reset(_f_query);
				//让指定文本框赋值
				Ext.getCmp("listTablename").setValue(listTablename);
				Ext.getCmp("listTablename").setRawValue(tablenamedesc);
				Ext.getCmp("tablename").setValue(listTablename);
				//让拼接条件得到返回值
				AOS.ajax({
					params:params,
					url: 'saveQueryData.jhtml',
					ok: function (data) {
						//此时在隐藏域中存入查询条件
						Ext.getCmp("querySession").setValue(data.appmsg);
					}
				});
			}
			//删除档案蓝临时
			function _w_delete_archive(){
				_g_center_store.remove(_g_center.getSelectionModel().getSelection());
			}
			//移动到档案蓝
			function _w_remove_archvie(){
				//根据data选中的数据，添加到下方档案蓝中
				//得到grid所有选中的数据
				var row=_g_data.getSelectionModel().getSelection();
				//得到档案蓝下方中所有数据
				var count=_g_center.getStore().getCount();

				for(var i=0;i<AOS.rows(_g_data);i++){
					var k=0;
					var enzd=row[i].data.fieldenname;
					var data_id_=row[i].data.id_;
					for(var z=0;z<count;z++){
						var center_id_=_g_center.getStore().getAt(z).data.id_;
						if(data_id_===center_id_){
							k+=1;
						}
					}
					if(k==0){
						_g_center_store.add(row[i].data);
					}
					//在第一行添加
					// _g_order_store.insert(0,data);
					//在尾行添加
					//_g_center_store.loadData(row[i].data,true);

				}
				//_g_center_store.add(row);
				//_g_data_store.remove(row);
				//此时移除掉下方grid相同的数据
			}
			//借阅申请
			function _w_borrow_application(){
				//弹出借阅申请表
				//得到档案蓝下方中所有数据
				var count=_g_center.getStore().getCount();
				if(count<=0){
					AOS.tip("档案蓝为空，不能完成申请!");
					return;
				}
				AOS.reset(_f_application);
				//把借阅单号和登录号传递过去
				_w_application.show();
			}
			function times(){
				//得到当前年月日
				var date = new Date();
				var year=date .getFullYear(); //获取完整的年份(4位)
				var month=date .getMonth(); //获取当前月份(0-11,0代表1月)
				if(month<10){
					month='0'+(month+1);
				}else{
					month='0'+(month+1);
				}
				var day=date .getDate(); //获取当前日(1-31)
				var times=year+"-"+month+"-"+day;
				return times;
			}
			function _borrow_load(){
				Ext.getCmp("borrow_date").setValue(times());
				//把档案栏中的所有档案的id加在一起添加到隐藏域中
				var ids="";
				var count=_g_center.getStore().getCount();
				for(var i=0;i<count;i++){
					var center_id_=_g_center.getStore().getAt(i).data.id_;
					if(i==count-1){
						ids=ids+center_id_;
					}else{
						ids=ids+center_id_+",";
					}
				}
				var user="<%=session.getAttribute("user")%>";
				var math=(new Date()).valueOf();
				var data="{borrow_card:"+math+",login_card,"+user+"}";

				Ext.getCmp("login_card").setValue(user);
				Ext.getCmp("borrow_person").setValue(user);
				Ext.getCmp("borrow_card").setValue(math);
				Ext.getCmp("data_ids_").setValue(ids);
				Ext.getCmp("borrow_ip").setValue(window.location.host+"");
			}
			//提交申请
			function submit_application(){

				//此时得到选中的选项
				//获取通过fieldset定义的checkbox值
				var xqCheck = Ext.getCmp('borrow_type_').items;
				var xq = '';
				for(var i = 0; i < xqCheck.length; i++){

					if(xqCheck.get(i).checked==true){
						xq += xqCheck.get(i).boxLabel+",";
					}


				}
				//校验到期时间和借阅时间
				AOS.ajax({
					url: 'submit_application.jhtml',
					params:{'tablename':"<%=request.getParameter("listtablename")%>","borrow_type_":xq},
					forms:_f_application,
					method:'post',
					ok: function (data) {
						if(data.appcode === 1){
							//让用户重新选择
							AOS.tip("提交成功!");
							_g_center_store.removeAll();
							_w_application.hide();
						}else if(data.appcode===-1){
							AOS.tip("提交失败!");
						}
					}
				});
			}
			function _w_deleteAll_archive(){
				_g_center_store.removeAll();
			}
			function b_date_e_date(borrow_date,expire_date){
				//截取4为
				var b_year=borrow_date.getFullYear();
				var e_year=expire_date.getFullYear();
				//月份
				var b_mouth=borrow_date.getMonth() + 1;
				var e_mouth=expire_date.getMonth() + 1;
				//天
				var b_day=borrow_date.getDate();
				var e_day=expire_date.getDate();
				if(b_year>e_year){
					return -1;
				}else if(b_year===e_year){
					if(b_mouth>e_mouth){
						return -1;
					}else if(b_mouth===e_mouth){
						if(b_day>e_day){
							return -1;
						}
					}else{

					}
				}else{

				}
			}
			function _w_borrow_message(){
				//打开前清空
				AOS.reset(_f_my_borrow);
				_w_my_borrow.show();
			}
			function get_my_borrow_data(){
				//加载表格数据
				var params = {
					borrow_order :Ext.getCmp("borrow_order").getValue(),
					tableTemplate:Ext.getCmp("tableTemplate").getValue()
				};
				//这个Store的命名规则为：表格ID+"_store"。
				_g_my_borrow_store.getProxy().extraParams = params;
				_g_my_borrow_store.load();
			}
			//_path列转换
			function fn_borrow_render(value, metaData, record, rowIndex, colIndex,
									  store) {
				if (value==null||value=="") {
					return '未借阅';
				} else if(value==1){
					return '已借阅';
				}else if(value==2){
					return '<a href="javascript:void(0)"><font style="color:red">已归还</font></a>';
				}
			}
			function fn_approval_render(value, metaData, record, rowIndex, colIndex,
										store){
				if (value==1) {
					return '待审批';
				} else if(value==2){
					return '<a href="javascript:void(0)"><font style="color:green">已审批</font></a>';
				}else if(value==3){
					return '<a href="javascript:void(0)"><font style="color:red">未通过</font></a>';
				}
			}
			function _w_details_show(){
				var record = AOS.selectone(_g_my_borrow);
				if (record) {
					_w_details.show();
					_g_details.loadRecord(record);
					//最后把多选框赋值
					var xqCheck = Ext.getCmp('borrow_type2_').items;
					for(var i = 0; i < xqCheck.length; i++){
						if(record.data.borrow_type_.indexOf(xqCheck.get(i).boxLabel)!=-1){
							xqCheck.get(i).setValue(true);
						}
					}
				}
			}
			//续借
			function _w_renewal_show(){
				//判断这个档案是否借阅和通过审核
				var id_ = AOS.selection(_g_my_borrow, 'id_').split(',')[0];
				var selection = AOS.selection(_g_my_borrow, 'borrow_status');
				if (AOS.empty(selection)) {
					AOS.tip('请选择续租的档案!');
					return;
				}
				if(selection.split(',')[0]==1){
					//弹出续租窗口
					//清空缓存
					_w_renewal.show();
				}else{
					AOS.tip("该档案审核未通过，或已到期不能续借！");
				}
			}
			//把续借详细数据展示出来
			function renewal_math_show(){
				var id_ = AOS.selection(_g_my_borrow, 'id_').split(',')[0];
				var record = AOS.selectone(_g_my_borrow);
				AOS.ajax({
					url: 'jymessage.jhtml',
					params:{id_:id_},
					method:'post',
					ok: function (data) {
						_g_renewal.form.setValues(data);
						Ext.getCmp('borrow_person2').setValue(record.data.borrow_person);
						Ext.getCmp('borrow_date2').setValue(record.data.borrow_date);
						Ext.getCmp('borrow_ip2').setValue(record.data.borrow_ip);
						var xqCheck = Ext.getCmp('borrow_type3_').items;
						for(var i = 0; i < xqCheck.length; i++){
							if(record.data.borrow_type_.indexOf(xqCheck.get(i).boxLabel)!=-1){
								xqCheck.get(i).setValue(true);
							}
						}
					}
				});
			}
			//保存续借时间
			function _w_save_renewal(){
				//校验到期日期和续借日期
				//计算出续借后的到期日期
				//得到当前到期时间
				var expire_date=new Date(Ext.getCmp("expire_date2").getValue()).getTime();

				//得到天数的毫秒值。进行计算
				var renew_borrow_day=Ext.getCmp("renew_borrow_day").getValue();

				var dayhmz=renew_borrow_day*24*60*60*1000;

				var borrow_exprie_hmz=expire_date+dayhmz;
				var birthday="";
				//把毫秒值计算出对应的年月日
				var time = new Date(borrow_exprie_hmz);
				if(time.getMonth()<10){
					birthday= time.getFullYear()+"-"+"0"+(time.getMonth()+1);
				}else{
					birthday= time.getFullYear()+"-"+(time.getMonth()+1);
				}
				if(time.getDate()<10){
					birthday= birthday+"-0"+time.getDate();
				}else{
					birthday= birthday+"-"+time.getDate();
				}
				Ext.getCmp("expire_date2").setValue(birthday);







				//提交给后台,档案续借
				AOS.ajax({
					url: 'updateRenewalBorrow.jhtml',
					forms:_g_renewal,
					method:'post',
					ok: function (data) {
						if(data.appcode ===1){
							//让用户重新选择
							AOS.tip("续借成功!");
							_w_renewal.hide();
							_g_my_borrow_store.reload();
						}else{
							AOS.tip("续借失败!");
						}
					}
				});
			}
			//归还档案
			function _w_return_archive(){
				//判断档案是否是租用状态
				var selection = AOS.selection(_g_my_borrow, 'id_');
				if (AOS.empty(selection)) {
					AOS.tip('请选择归还的档案!');
					return;
				}
				var borrow_status = AOS.selection(_g_my_borrow, 'borrow_status').split(',')[0];
				if(borrow_status==1){
					var msg = AOS.merge('确认要归还档案吗？归还后不能查看电子文件!');
					AOS.confirm(msg, function (btn) {
						if (btn === 'cancel') {
							AOS.tip('归还操作被取消。');
							return;
						}
						//归还档案
						AOS.ajax({
							url: 'returnRenewalBorrow.jhtml',
							params:{id_:selection.split(',')[0]},
							method:'post',
							ok: function (data) {
								if(data.appcode ===1){
									//让用户重新选择
									AOS.tip("归还成功!");
									_w_renewal.hide();
									_g_my_borrow_store.reload();
								}else{
									AOS.tip("归还失败!");
								}
							}
						});
					});
				}else{
					AOS.tip("档案未借阅或审核未通过，无法归还！");
				}
			}
			//电子文件(借阅后的档案审核后可以查看电子文件)
			function _w_borrow_files(){
				var selection = AOS.selection(_g_data, 'id_');
				if (AOS.empty(selection)) {
					AOS.tip('请选择需要查看电子文件的档案!');
					return;
				}
				if(selection.substring(0,selection.length-1).split(",").length>1){
					alert("请选择单个条目数据！");
					return;
				}
				//弹出一个窗口，展示电子文件列表
				_w_files.show();
			}
			function get_files(){
				var selection = AOS.selection(_g_data, 'id_');
				var id_=selection.substring(0,selection.length-1);
				var listTablename = Ext.getCmp("tablename").getValue();
				var params ={
					id_:id_,
					tablename:listTablename
				};
				//这个Store的命名规则为：表格ID+"_store"。
				_g_files_store.getProxy().extraParams = params;
				_g_files_store.load();
			}
			//查看电子文件
			function _w_files_look(){
				//得到当前用户,判断是不是档案室和admin用户
				var user="<%=session.getAttribute("user")%>";
				//得到当前选中的。
				var row = AOS.selectone(_g_files);
				//判断当前用户的岗位是不是系统管理员岗位
				AOS.ajax({
					url: 'getPostUser.jhtml',
					params:{"user":user},
					method: 'post',
					ok: function (data) {
						if (data.appmsg.indexOf("兼职档案员") != -1||data.appmsg.indexOf("普通用户") != -1) {


							AOS.ajax({
								url: 'booleanBorrowFile.jhtml',
								params:{id_:row.data.tid},
								method:'post',
								ok: function (data) {
									if(data.appcode === -1){
										//让用户重新选择
										AOS.tip("对不起!您没有权限看电子文件,请借阅电子文件!");
										return;
									}else{
										var row1 = AOS.selectone(_g_files);
										if(row1.data._s_path.split(".")[1]=="pdf"){
											parent.fnaddtab(row1.data.id_, '电子文件',
													'archive/data/openPdfFile.jhtml?id='+row1.data.id_+'&tid='+row1.data.tid+'&type='+row1.data._s_path.split(".")[1]+'&tablename='+Ext.getCmp("tablename").getValue());
										}else if(row1.data._s_path.split(".")[1]=="jpg"||row1.data._s_path.split(".")[1]=="doc"){
											parent.fnaddtab(row1.data.id_,'电子文件','archive/data/openSwfFile.jhtml?id='+row1.data.id_+'&tid='+row1.data.tid+'&type='+row1.data._s_path.split(".")[1]+'&tablename='+Ext.getCmp("tablename").getValue());
										}
									}
								}
							});
						}else{
							if(row.data._s_path.split(".")[1]=="pdf"){
								parent.fnaddtab(row.data.id_, '电子文件',
										'archive/data/openPdfFile.jhtml?id='+row.data.id_+'&tid='+row.data.tid+'&type='+row.data._s_path.split(".")[1]+'&tablename='+Ext.getCmp("tablename").getValue());
							}else if(row.data._s_path.split(".")[1]=="jpg"||row.data._s_path.split(".")[1]=="doc"){
								parent.fnaddtab(row.data.id_,'电子文件','archive/data/openSwfFile.jhtml?id='+row.data.id_+'&tid='+row.data.tid+'&type='+row.data._s_path.split(".")[1]+'&tablename='+Ext.getCmp("tablename").getValue());
							}
						}
					}
				});

			}
			//展开加载任务编号
			function _w_my_borrow_show(){
				//夹在门类名称
				var listTablename ="<%=request.getParameter("listtablename")%>";
				_f_my_borrow.form.findField("tablename").setValue(listTablename);
				AOS.ajax({
					params:{name_:'调档任务流水号'},
					url:'calcId.jhtml',
					ok:function(data){
						//设计一个随机数编号
						_f_my_borrow.form.findField("transfer_number").setValue(data.appmsg);
					}
				});
			}
			function submit_transfer(){
				var selection = AOS.selection(_g_data, 'id_');
				//保存操作
				AOS.ajax({
					forms:_f_my_borrow,
					params:{aos_rows_: selection},
					url:'saveTransfer.jhtml',
					ok:function(data){
						if(data.appcode===1){
							AOS.tip("操作成功!");
							_w_my_borrow.hide();
						}else if(data.appcode===-1){
							AOS.tip("操作失败!");
						}
					}
				});
			}
			function _w_borrow_list(){
				_w_transfer_list.show();
			}
			function get_transfer_list(){
				_g_transfer_list_store.load();
			}
			function _w_list_archive(){
				var selection = AOS.selection(_g_transfer_list, 'id_');
				if (AOS.empty(selection)) {
					AOS.tip('查看详情前请先选中数据。');
					return;
				}
				var id_=AOS.selectone(_g_transfer_list).raw.id_;
				var tablename=AOS.selectone(_g_transfer_list).raw.tablename;
				window.parent.fnaddtab('','档案详情','/transfer/transfer/archive_details.jhtml?id_='+id_+'&tablename='+tablename);
			}
			//删除信息
			function _g_data_del() {
				var selection = AOS.selection(_g_data, 'id_');
				var tms = AOS.selection(_g_data, 'tm');
				if (AOS.empty(selection)) {
					AOS.tip('删除前请先选中数据。');
					return;
				}
				var msg = AOS.merge('确认要删除选中的[{0}]个用户数据吗？', AOS.rows(_g_data));
				AOS.confirm(msg, function (btn) {
					if (btn === 'cancel') {
						AOS.tip('删除操作被取消。');
						return;
					}
					AOS.ajax({
						url: 'deleteData.jhtml',
						params: {
							aos_rows_: selection,
							tm:tms,
							tablename: tablename.getValue()
						},
						ok: function (data) {
							AOS.tip(data.appmsg);
							_g_data_store.reload();
						}
					});
				});
			}
			function _g_data_return(){
				var selection = AOS.selection(_g_data, 'id_');
				var tms = AOS.selection(_g_data, 'tm');
				if (AOS.empty(selection)) {
					AOS.tip('还原前请先选中数据。');
					return;
				}
				var msg = AOS.merge('确认要还原选中的[{0}]个用户数据吗？', AOS.rows(_g_data));
				AOS.confirm(msg, function (btn) {
					if (btn === 'cancel') {
						AOS.tip('还原操作被取消。');
						return;
					}
					AOS.ajax({
						url: 'returnData.jhtml',
						params: {
							aos_rows_: selection,
							tm:tms,
							tablename: tablename.getValue()
						},
						ok: function (data) {
							AOS.tip(data.appmsg);
							_g_data_store.reload();
						}
					});
				});
			}
			//全部删除(指定条件的全部删除)
			function _g_data_del_term(){
				var query=Ext.getCmp("querySession").getValue();
				var msg = AOS.merge('确认要删除用户数据吗？');
				AOS.confirm(msg, function (btn) {
					if (btn === 'cancel') {
						AOS.tip('删除操作被取消。');
						return;
					}
					AOS.ajax({
						url: 'deleteAllData.jhtml',
						params: {
							query:query,
							tablename: tablename.getValue()
						},
						ok: function (data) {
							AOS.tip(data.appmsg);
							_g_data_store.reload();
						}
					});
				});
			}
			function _g_data_return_term(){
				var query=Ext.getCmp("querySession").getValue();
				var msg = AOS.merge('确认要还原用户数据吗？');
				AOS.confirm(msg, function (btn) {
					if (btn === 'cancel') {
						AOS.tip('还原操作被取消。');
						return;
					}
					AOS.ajax({
						url: 'returnAllData.jhtml',
						params: {
							query:query,
							tablename: tablename.getValue()
						},
						ok: function (data) {
							AOS.tip(data.appmsg);
							_g_data_store.reload();
						}
					});
				});
			}
		</script>
	</aos:onready>
</aos:html>
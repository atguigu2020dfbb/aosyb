<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="/WEB-INF/tld/aos.tld" prefix="aos"%>
<%@ taglib uri="/WEB-INF/tld/cll.tld" prefix="c"%>
<aos:html>
<aos:head title="数据信息">
	<aos:include lib="ext,swfupload" />
	<aos:base href="make" />
	<style>
		.my_row_red .x-grid-cell {
		background-color: #99FF99;
		}
		td {background-color:#eeeeee}
		input {width:400px;}
		.butt {width:155px; height:30px}
		.title {padding-left:10px; width:130px}
	</style>
	<%
		String context = request.getContextPath();

	%>
</aos:head>
<object id="CertCtl" TYPE="application/cert-reader" width=0 height=0"></object>
<aos:body>
	<div id="_div_photo2" class="x-hidden" align="center">
		<img id="_img_photo2" class="app_cursor_pointer" src="${cxt}/static/image/empty_head_photo.png" width="200"
			 onclick="read_photo()" height="200">
	</div>
	<div id="_div_photo" class="x-hidden" align="center">
		<img id="PhotoID" name="PhotoID" class="app_cursor_pointer"  style="width:96px; height:118px;margin-top:30px">
	</div>
</aos:body>
<aos:onready>
	<aos:viewport layout="border">
		<aos:gridpanel id="_g_data" url="zz_listAccounts.jhtml" region="center" split="true"
			onrender="_g_data_query" pageSize="${pagesize }"
			 enableLocking="false" >
			<aos:docked>

				<aos:hiddenfield id="index" name="index" />
				<aos:hiddenfield  name="insert" id="insert"/>
				<aos:hiddenfield id="appraisal" name="appraisal" />
				<aos:hiddenfield id="treenumber" name="treenumber" value="${treenumber}"/>
				<aos:hiddenfield name="_classtree" id="_classtree"
								 value="${cascode_id_}" />
				<aos:hiddenfield  id="_recordid" name="_recordid" value="${recordid}"/>
				<aos:hiddenfield id="treename" name="treename" value="${treename}"/>
				<aos:hiddenfield id="tablename" name="tablename" value="${tablename}"/>
				<!-- 存储当前页面的查询条件 -->
				<aos:hiddenfield id="querySession" name="querySession" />
				<aos:dockeditem xtype="tbtext" text="目录信息" />
				<aos:dockeditem text="检索" icon="query.png"  onclick="_w_query_show" id="_f_select_message"/>
				<aos:dockeditem text="申请" icon="more/mail-filters-apply.png"  onclick="_w_apply_show" id="_f_apply_show"/>
				<aos:dockeditem text="读取身份证" icon="more/mail-filters-apply.png"  onclick="_w_readCard_show" id="_f_apply_show"/>
				<aos:dockeditem xtype="tbfill" />
			</aos:docked>
			<aos:selmodel type="checkbox" mode="multi" />
			<aos:column dataIndex="id_" header="流水号" hidden="true"  locked="true"/>
			<aos:column dataIndex="_path" header="电子文件"
				rendererFn="fn_path_render" />
			<c:forEach var="field" items="${fieldDtos}">
				<aos:column dataIndex="${field.fieldenname}"
					header="${field.fieldcnname }" width="${field.dislen }"
					rendererField="field_type_" >
					<aos:textfield />
					</aos:column>
			</c:forEach>
			<aos:column header="" flex="1" />
		</aos:gridpanel>
	</aos:viewport>
	<aos:window id="_w_query_q" title="查询" width="720" autoScroll="true"
		layout="fit">
		<aos:tabpanel id="_tabpanel" region="center" activeTab="0"
			bodyBorder="0 0 0 0" tabBarHeight="30">
			<aos:tab title="列表式搜索" id="_tab_org">
				<aos:formpanel id="_f_query" layout="column" columnWidth="1">
					<aos:hiddenfield name="tablename" value="${tablename }" />
					<aos:hiddenfield name="columnnum" id="columnnum" value="8" />
					<aos:hiddenfield name="selectmark" id="selectmark" value="1" />
					<aos:hiddenfield name="selectmath" id="selectmath" value="0" />
					<c:forEach var="fieldss" items="${fieldDtos}" end="7"
						varStatus="listSearch">
						<aos:combobox name="and${listSearch.count}" value="on"
							labelWidth="10" columnWidth="0.2">
							<aos:option value="on" display="并且" />
							<aos:option value="up" display="或者" />
						</aos:combobox>
						<aos:combobox id="filedname${listSearch.count}"
							name="filedname${listSearch.count}"
							emptyText="${fieldss.fieldcnname}" labelWidth="20"
							columnWidth="0.2" fields="['fieldenname','fieldcnname']"
							regexText="${fieldss.fieldenname}" displayField="fieldcnname"
							valueField="fieldenname" 
							url="queryFields.jhtml?tablename=${tablename}">
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
	<aos:window title="读取身份证" id="_w_readCard" width="850" layout="fit"  modal="false" center="true" maximizable="true" onshow="_w_apply_onshow" >
		<aos:formpanel id="_f_apply"  width="700" layout="column" >
			<aos:fieldset title="基本信息" labelWidth="70" columnWidth="0.65" margin="0 10 0 0" height="250">
				<aos:textfield name="zzbh" id="zzbh" fieldLabel="自助编号" readOnly="true" columnWidth="0.99"/>
				<aos:textfield name="text_name" id="text_name" fieldLabel="姓名" columnWidth="0.99"/>
			    <aos:textfield name="text_sex" id="text_sex" fieldLabel="性别" columnWidth="0.99"/>
			<aos:textfield name="text_nation" id="text_nation" fieldLabel="民族或国籍" columnWidth="0.99"/>
			<aos:textfield name="text_birthday" id="text_birthday" fieldLabel="出生" columnWidth="0.99"/>
			<aos:textfield name="text_address" id="text_address" fieldLabel="地址" columnWidth="0.99"/>
			<aos:textfield name="text_idNum" id="text_idNum" fieldLabel="身份证号" columnWidth="0.99"/>
			<aos:textfield name="text_dept" id="text_dept" fieldLabel="签发机关" columnWidth="0.99"/>
<%--			<aos:textfield name="text_effDate" id="text_effDate" fieldLabel="开始期限" columnWidth="0.99"/>--%>
<%--			<aos:textfield name="text_expDate" id="text_expDate" fieldLabel="结束期限" columnWidth="0.99"/>--%>
<%--			<aos:textfield name="text_certType" id="text_certType" fieldLabel="证件类型" columnWidth="0.99"/>--%>
			<aos:textfield name="text_result" id="text_result" fieldLabel="执行结果" columnWidth="0.99"/>
			<aos:textareafield name="text_json" id="text_json" fieldLabel="返回数据" columnWidth="0.99"/>
		</aos:fieldset>
		<aos:fieldset title="图像" labelWidth="70" columnWidth="0.35" contentEl="_div_photo" height="250" >
		</aos:fieldset>
		</aos:formpanel>
		<aos:docked dock="bottom" ui="footer">
			<aos:dockeditem xtype="tbfill" />
				<aos:dockeditem xtype="button" text="读取" icon="ok.png" onclick="doReadCert"/>
				<aos:dockeditem xtype="button" text="查询" icon="query.png" onclick="select_archive"/>
				<aos:dockeditem text="关闭" icon="close.png" onclick="#_w_readCard.hide()"/>
		</aos:docked>
	</aos:window>
	<aos:window title="申请信息" id="_w_apply" width="850" layout="fit"  modal="false" center="true" maximizable="true"
				onshow="_w_apply_onshow">
		<aos:formpanel id="_f_apply2"  width="700" layout="column">
			<aos:fieldset title="基本信息" labelWidth="70" columnWidth="0.65" margin="0 10 0 0" height="250">
				<aos:textfield name="zzbh" fieldLabel="自助编号" readOnly="true" columnWidth="0.99"/>
				<aos:textfield name="xm" fieldLabel="姓名" columnWidth="0.69"/>
				<aos:button text="读取身份证" margin="0 0 0 10"  icon="query.png" columnWidth="0.3" onclick="read_cardID"/>
				<aos:textfield name="sfzh" fieldLabel="身份证号"  maxLength="50" columnWidth="0.99" />
				<aos:textfield name="lymd" fieldLabel="利用目的"  maxLength="50" columnWidth="0.99" />
				<aos:textfield name="cdnr" fieldLabel="查阅内容"  columnWidth="0.99" />
				<aos:textfield name="bz" fieldLabel="备注"   columnWidth="0.99" />
			</aos:fieldset>
			<aos:fieldset title="介绍信" labelWidth="70" columnWidth="0.35" contentEl="_div_photo" height="250" >
			</aos:fieldset>
		</aos:formpanel>
		<aos:docked dock="bottom" ui="footer">
			<aos:dockeditem xtype="tbfill" />
			<aos:dockeditem xtype="button" text="保存数据" icon="ok.png" onclick="_f_apply_save"/>
			<aos:dockeditem text="关闭" icon="close.png" onclick="#_w_apply.hide()"/>
		</aos:docked>
	</aos:window>
	<script type="text/javascript">
	function select_archive(){
			var xm = Ext.getCmp("_f_apply").getForm().findField('text_name').getValue();
			AOS.ajax({
				forms : _f_apply,

				url : 'addSFXX.jhtml',
				url : 'zz_listAccounts.jhtml',
				params:{
					tablename : tablename.getValue(),
				},

				ok : function(data) {
					if (data.appcode === -1) {
						AOS.err(data.appmsg);
					} else {
						_g_data_store.reload();
						AOS.tip(data.appmsg);
						_w_readCard.hide();
					}
				}
			});

		// window.parent.fnaddtab('', '数据导入', 'make/zz_listAccounts.jhtml?tablename=' + tablename.getValue()+'&tm='+xm);
	}
		function _w_readCard_show(){
			_w_readCard.show();
			{
				clearText();

				var strBusiSerial_32 = "0000000000000000AAAAAAAAAAAAAAAA";
				var strStaffCode_16 = "1111111111111111";
				var strChannelCode_32 = "1111111111111111BBBBBBBBBBBBBBBB";
				var strBusiArre_200 = "0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000";
				strBusiArre_200 += "0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000";

				//var strResult = CertCtl.readCert(strBusiSerial_32, strStaffCode_16, strChannelCode_32, strBusiArre_200);
				var strResult = CertCtl.readCert();
				Ext.getCmp("text_json").setValue(strResult);

				var jsValue = eval('(' + strResult + ')');
				if(jsValue.resultFlag == 0)
				{
					Ext.getCmp("text_name").setValue(jsValue.resultContent.partyName);
					if(jsValue.resultContent.gender=="1"){
						Ext.getCmp("text_sex").setValue("男");
					}else if(jsValue.resultContent.gender=="0"){
						Ext.getCmp("text_sex").setValue("女");
					}

					Ext.getCmp("text_nation").setValue(jsValue.resultContent.nation);
					Ext.getCmp("text_birthday").setValue(jsValue.resultContent.bornDay);
					Ext.getCmp("text_address").setValue(jsValue.resultContent.certAddress);
					Ext.getCmp("text_idNum").setValue(jsValue.resultContent.certNumber);
					Ext.getCmp("text_dept").setValue(jsValue.resultContent.certOrg);
					//Ext.getCmp("text_effDate").setValue(jsValue.resultContent.effDate);
					//Ext.getCmp("text_expDate").setValue(jsValue.resultContent.expDate);
					//Ext.getCmp("text_certType").setValue(jsValue.resultContent.certType);
					Ext.getCmp("text_result").setValue('成功');
					document.all('PhotoID').src = 'data:image/jpeg;base64,' + jsValue.resultContent.identityPic;
				}
				else
				{
					Ext.getCmp("text_result").setValue('失败');
				}
			}
		}
		function clearText()
		{
			Ext.getCmp("text_name").setValue("");
			Ext.getCmp("text_sex").setValue("");
			Ext.getCmp("text_nation").setValue("");
			Ext.getCmp("text_birthday").setValue("");
			Ext.getCmp("text_address").setValue("");
			Ext.getCmp("text_idNum").setValue("");
			Ext.getCmp("text_dept").setValue("");
			//Ext.getCmp("text_effDate").setValue("");
			//Ext.getCmp("text_expDate").setValue("");
			//Ext.getCmp("text_certType").setValue("");
			Ext.getCmp("text_result").setValue("");
			Ext.getCmp("text_json").setValue("");
		}

		function doConnect()
		{
			clearText();
			var strResult = CertCtl.connect();

			Ext.getCmp("text_json").setValue(strResult);

			var jsValue = eval('(' + strResult + ')');
			if(jsValue.resultFlag == 0)
			{
				Ext.getCmp("text_result").setValue('成功');
			}
			else
			{
				Ext.getCmp("text_result").setValue('失败');
			}
		}

		function doGetVersion()
		{
			clearText();
			var strResult = CertCtl.getVersion();
			Ext.getCmp("text_json").setValue(strResult);

			var jsValue = eval('(' + strResult + ')');
			if(jsValue.resultFlag == 0)
			{
				Ext.getCmp("text_result").setValue('成功');
			}
			else
			{
				Ext.getCmp("text_result").setValue('失败');
			}
		}

		function doDisconnect()
		{
			clearText();
			var strResult = CertCtl.disconnect();
			Ext.getCmp("text_json").setValue(strResult);

			var jsValue = eval('(' + strResult + ')');
			if(jsValue.resultFlag == 0)
			{
				Ext.getCmp("text_result").setValue('成功');
			}
			else
			{
				Ext.getCmp("text_result").setValue('失败');
			}
		}

		function doGetStatus()
		{
			clearText();
			var strResult = CertCtl.getStatus();

			Ext.getCmp("text_json").setValue(strResult);

			var jsValue = eval('(' + strResult + ')');
			if(jsValue.resultFlag == 0)
			{
				Ext.getCmp("text_result").setValue('成功');
			}
			else
			{
				Ext.getCmp("text_result").setValue('失败');
			}
		}

		function doReadCert()
		{
			clearText();

			var strBusiSerial_32 = "0000000000000000AAAAAAAAAAAAAAAA";
			var strStaffCode_16 = "1111111111111111";
			var strChannelCode_32 = "1111111111111111BBBBBBBBBBBBBBBB";
			var strBusiArre_200 = "0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000";
			strBusiArre_200 += "0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000";

			//var strResult = CertCtl.readCert(strBusiSerial_32, strStaffCode_16, strChannelCode_32, strBusiArre_200);
			var strResult = CertCtl.readCert();
			Ext.getCmp("text_json").setValue(strResult);

			var jsValue = eval('(' + strResult + ')');
			if(jsValue.resultFlag == 0)
			{
				Ext.getCmp("text_name").setValue(jsValue.resultContent.partyName);
				if(jsValue.resultContent.gender=="1"){
					Ext.getCmp("text_sex").setValue("男");
				}else if(jsValue.resultContent.gender=="0"){
					Ext.getCmp("text_sex").setValue("女");
				}
				
				Ext.getCmp("text_nation").setValue(jsValue.resultContent.nation);
				Ext.getCmp("text_birthday").setValue(jsValue.resultContent.bornDay);
				Ext.getCmp("text_address").setValue(jsValue.resultContent.certAddress);
				Ext.getCmp("text_idNum").setValue(jsValue.resultContent.certNumber);
				Ext.getCmp("text_dept").setValue(jsValue.resultContent.certOrg);
				//Ext.getCmp("text_effDate").setValue(jsValue.resultContent.effDate);
				//Ext.getCmp("text_expDate").setValue(jsValue.resultContent.expDate);
				//Ext.getCmp("text_certType").setValue(jsValue.resultContent.certType);
				Ext.getCmp("text_result").setValue('成功');
				document.all('PhotoID').src = 'data:image/jpeg;base64,' + jsValue.resultContent.identityPic;
			}
			else
			{
				Ext.getCmp("text_result").setValue('失败');
			}
		}

		function doCloudReadCert()
		{
			clearText();

			var appId_32 = "0000000000000000AAAAAAAAAAAAAAAA";
			var timestamp_32 = "1111111111111111BBBBBBBBBBBBBBBB";
			var nonce_32 = "1111111111111111BBBBBBBBBBBBBBBB";
			var businessExt_200 = "0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000";
			businessExt_200 += "0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000";

			var signature_200 = "1111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111";
			signature_200 += "1111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111";

			var strResult = CertCtl.cloudReadCert(appId_32, timestamp_32, nonce_32, businessExt_200, signature_200);
			Ext.getCmp("text_json").setValue(strResult);

			var jsValue = eval('(' + strResult + ')');
			if(jsValue.resultFlag == 0)
			{
				Ext.getCmp("text_result").setValue('成功');
			}
			else
			{
				Ext.getCmp("text_result").setValue('失败');
			}
		}




















		function read_cardID(){
			AOS.tip("暂无身份证读取器接口!");
			return;
		}
		function _w_data_u_onshow(){
		var record = AOS.selectone(_g_data);
			var insert=Ext.getCmp('insert').getValue();
			//此时把年度。 全宗号，机构问题代码 直接携带过去
				AOS.ajax({
					params: {
						id_: record.data.id_,
						tablename: tablename.getValue()
					},
					url: 'getData.jhtml',
					ok: function (data) {
						_f_data_u.form.setValues(data);
					}
				});
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
			var times=year+month+day;
			return times;
		}
		//加载表格数据
		function _g_data_query() {
			var params = {
				treenumber:treenumber.getValue(),
				tablename : tablename.getValue()
			};
			//这个Store的命名规则为：表格ID+"_store"。
			_g_data_store.getProxy().extraParams = params;
			_g_data_store.load();
		}
		function _f_data_query() {
			var params = AOS.getValue('_f_query');
			var form = Ext.getCmp('_f_query');
			var tmp = columnnum.getValue();
			for (var i = 1; i <= tmp; i++) {
				var str = form.down("[name='filedname" + i + "']");
				var filedname = str.getValue();
				if (filedname == null) {
					params["filedname" + i] = str.regexText;
				}
				var emptyfiledcnname = str.emptyText;
				var filedcnname = Ext.getCmp("filedname" + i).getRawValue();
				if (emptyfiledcnname == filedcnname && filedcnname != null && filedcnname != "") {
					params["filedcnname" + i] = filedcnname;
				} else if (filedcnname == null || filedcnname == "") {
					params["filedcnname" + i] = emptyfiledcnname;
				}
			}
			//走后台判断一下是不是敏感词语，如果是敏感词拦截 不让其进行查询操作

			_g_data_store.getProxy().extraParams = params;
			_g_data_store.load();
			_w_query_q.hide();
			AOS.reset(_f_query);
			AOS.ajax({
				params:params,
				url: 'saveQueryData.jhtml',
				ok: function (data) {
					//此时在隐藏域中存入查询条件
					Ext.getCmp("querySession").setValue(data.appmsg);
				}
			});
		}
        function _w_query_show() {       		
                _w_query_q.show();
        }
		//_path列转换
	function fn_path_render(value, metaData, record, rowIndex, colIndex,
				store) {
			if (value >= 1) {
				return '<img src="${cxt}/static/icon/picture.png" />';
			} else {
				return '<img src="${cxt}/static/icon/picture_empty.png" />';
			}
		}

	//返回上一次查询
	function _f_last_query(){
		//获取当前查询次数状态，
		var tablename="<%=session.getAttribute("tablename")%>";
		var selectmath=Ext.getCmp("selectmath").getValue();
		Ext.Ajax.request({   
			 url:'getSelectWhereLast.jhtml',
            params:{tablename:tablename,
            	selectmath:selectmath},
           method : 'post', 
			success:function(response,config){
				//对后台输出的Json进行解码
				json=Ext.decode(response.responseText);
			
				if(json.total!=0){
					for(var k=0;k<json.total;k++){
						var andvalue=json.data[k].and;
						var filednamevalue=json.data[k].filedname;
						var conditionvalue=json.data[k].condition;
						var contentvalue=json.data[k].content;
						Ext.getCmp('_f_query').down("[name='and"+(k+1)+"']").setValue(andvalue);
						Ext.getCmp('_f_query').down("[name='filedname"+(k+1)+"']").setValue(filednamevalue);
						Ext.getCmp('_f_query').down("[name='condition"+(k+1)+"']").setValue(conditionvalue);
						Ext.getCmp('_f_query').down("[name='content"+(k+1)+"']").setValue(contentvalue);
					}
				}
				Ext.getCmp("selectmath").setValue(json.selectmath);
			}
		});
	}
		function _w_apply_onshow(){
			//根据tree名称得到对应的部门名称
			AOS.ajax({
				params:{name_:'自助流水号'},
				url:'calcId.jhtml',
				ok:function(data){
					//设计一个随机数编号
					var date=new Date();
					_f_apply.form.findField("zzbh").setValue("ZZ"+times()+data.appmsg);
				}
			});
		}
	function _f_next_query(){
		//获取当前查询次数状态，
		var tablename="<%=session.getAttribute("tablename")%>";
		var selectmath=Ext.getCmp("selectmath").getValue();
		Ext.Ajax.request({   
			 url:'getSelectWhereNext.jhtml',
            params:{tablename:tablename,
            	selectmath:selectmath},
           method : 'post', 
			success:function(response,config){
				//对后台输出的Json进行解码
				json=Ext.decode(response.responseText);
			
				if(json.total!=0){
					for(var k=0;k<json.total;k++){
						var andvalue=json.data[k].and;
						var filednamevalue=json.data[k].filedname;
						var conditionvalue=json.data[k].condition;
						var contentvalue=json.data[k].content;
						Ext.getCmp('_f_query').down("[name='and"+(k+1)+"']").setValue(andvalue);
						Ext.getCmp('_f_query').down("[name='filedname"+(k+1)+"']").setValue(filednamevalue);
						Ext.getCmp('_f_query').down("[name='condition"+(k+1)+"']").setValue(conditionvalue);
						Ext.getCmp('_f_query').down("[name='content"+(k+1)+"']").setValue(contentvalue);
					}
				}
				Ext.getCmp("selectmath").setValue(json.selectmath);
			}
		});
	}
	//清空查询条件
	function _f_drop_query(){
		var tablename="<%=session.getAttribute("tablename")%>";
		AOS.ajax({
			url : 'removeDataWhere.jhtml',
			params:{
				tablename : tablename
	        },
			ok : function(data) {
				Ext.getCmp("selectmath").setValue("0");
			}
		});
	}
		function _f_apply_save(){
			var aos_rows_=AOS.selection(_g_data,'id_');
			AOS.ajax({
				forms:_f_apply,
				params:{aos_rows_:aos_rows_,tablename:Ext.getCmp("tablename").getValue()},
				url:'saveApply.jhtml',
				ok:function (data){
					if(data.appcode == -1){
						AOS.err(data.appmsg);
					}else {
						_w_apply.hide();
						_g_data_store.reload();
						AOS.tip(data.appmsg);
					}
				}
			})
		}
	//查档申请
	function _w_apply_show(){
		//弹出用户添加表单窗口
		var selection = AOS.selection(_g_data,'id_');
		if(AOS.empty(selection)){
			AOS.tip("申请前请选中数据。。。");
			return;
		}

		_w_apply.show();
	}

	</script>
</aos:onready>
	<script type="text/javascript">
		function read_photo(){
			alert("暂无扫描仪接口！");
		}
	</script>
</aos:html>
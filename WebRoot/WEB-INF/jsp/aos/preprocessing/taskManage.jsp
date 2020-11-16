<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="/WEB-INF/tld/aos.tld" prefix="aos"%>
<%@ taglib uri="/WEB-INF/tld/c.tld" prefix="c"%>
<aos:html>
<aos:head title="办理任务">
	<aos:include lib="ext" />
	<aos:base href="preprocessing/application" />
	<style>
#_div_tips {
	line-height: 25px;
	margin-right: 10px;
}
</style>
</aos:head>
<aos:body>
	<div id="_div_tips" class="x-hidden">
		<ul>
			<li>a) 检测、确认电子文件及其元数据无病毒；</li>
			<li>b) 检测、确认电子文件及其组件的计算机文件格式符合要求；</li>
			<li>c) 检测、确认电子文件标识符、内容与形式特征、物理结构元数据齐全、完整，通过计算机文件
名能够准确实现元数据与电子文件的一一对应。</li>
		</ul>
	</div>
	<div id="_div_img" class="x-hidden" align="center" style="vertical-align: middle;">
		<img id="_img" style="vertical-align: middle; margin:10px;" />
	</div>
</aos:body>
<aos:onready>
	<aos:viewport layout="border">
		<aos:panel region="west" width="250" bodyBorder="0 1 0 0">
			<aos:layout type="vbox" align="stretch" />
					<aos:panel height="100">
				<aos:layout type="vbox" align="stretch" />
				<c:forEach var="outcome" items="${list}">
				<aos:button text="${outcome}"  icon="add2.png" scale="medium" margin="10" onclick="!_f_data_save" />
				</c:forEach>
			</aos:panel>
			<aos:panel height="200" layout="fit" onrender="_f_data_render">
				<aos:docked forceBoder="1 0 1 0">
					<aos:dockeditem xtype="tbtext" text="申请人信息" />
				</aos:docked>
				<aos:formpanel id="_f_data" layout="anchor" labelWidth="60" header="false"  icon="user8.png">
					<aos:hiddenfield name="taskid" id="taskid" value="${taskid }"/>
					<aos:hiddenfield name="id_" />
					<aos:textfield name="xm" fieldLabel="申请人"   anchor="99%" readOnly="true" />
					<aos:textfield name="idno_" fieldLabel="身份证号"   anchor="99%" readOnly="true"/>
					<aos:textfield name="sqrq" fieldLabel="申请日期"  anchor="99%" readOnly="true"/>
					<aos:textareafield name="comment" fieldLabel="批注" allowBlank="false" />
					<aos:hiddenfield name="state" value="0"/>
				</aos:formpanel>
			</aos:panel>
			<aos:panel flex="1" layout="fit" contentEl="_div_tips" autoScroll="true">
				<aos:docked forceBoder="1 0 1 0">
					<aos:dockeditem xtype="tbtext" text="注意事项" />
				</aos:docked>
			</aos:panel>
		</aos:panel>
		<aos:panel region="center" border="false">
			<%-- 垂直盒子布局，里面可以放置任意多个固定高度或者自适应高度的组件 --%>
			<aos:layout type="vbox" align="stretch" />
			<aos:gridpanel flex="1" id="_g_center" url="listTaskAccounts.jhtml" onrender="_g_center_query">
				<aos:docked forceBoder="1 1 1 0">
					<aos:dockeditem xtype="tbtext" text="目录信息" />
				</aos:docked>
				<aos:column type="rowno" />
				<aos:column dataIndex="id_" hidden="true"/>
				<c:forEach var="field" items="${fieldDtos}">
				<aos:column dataIndex="${field.fieldenname}"
					header="${field.fieldcnname }" width="${field.dislen }" rendererField="field_type_" />
			</c:forEach>
			<aos:column header="指导意见" rendererFn="fn_render_zdyj"/>
			<aos:column header="" flex="1"/>
			</aos:gridpanel>
		</aos:panel>

	</aos:viewport>
	<aos:window id="_w_zdyj" title="指导意见" height="500" autoScroll="true" onshow="_w_zdyj_load" >
			<aos:gridpanel  id="_g_zdyj_demo"  height="250" hidePagebar="true"
				url="getZDYJ.jhtml" width="800" pageSize="10">
				<aos:docked>
					<aos:dockeditem xtype="tbtext" text="指导列表" />
				</aos:docked>
				<aos:selmodel type="row" mode="multi" />
				<aos:column type="rowno" />
				<aos:hiddenfield id="zdtablename" />
				<aos:column header="流水号" dataIndex="id_" hidden="true" />
				<aos:column header="数据表" dataIndex="tablename" hidden="false" width="90" />
				<aos:column header="tid" dataIndex="tid" width="90" hidden="true"/>
				<aos:column header="指导人" dataIndex="zd_person" width="90" />
				<aos:column header="指导时间" dataIndex="zd_time" width="90" />
				<aos:column header="指导内容" dataIndex="zd_description" width="90" />
			</aos:gridpanel>
			<aos:formpanel id="_f_table" width="800" layout="column"
				labelWidth="75" >
				<aos:docked>
					<aos:dockeditem xtype="tbtext" text="指导意见" />
				</aos:docked>
				<aos:textareafield name="zd_description" id="zd_description" fieldLabel="指导意见" height="100"
					columnWidth="0.99" />
			</aos:formpanel>
			<aos:docked dock="bottom" ui="footer">
				<aos:dockeditem xtype="tbfill" />
				<aos:dockeditem onclick="_f_description_add" text="添加" icon="ok.png" />
				<aos:dockeditem onclick="#_w_zdyj.hide();" text="关闭"
					icon="close.png" />
			</aos:docked>
		</aos:window>
		
	<script type="text/javascript">
		//加载center表格数据
		function _g_center_query() {
		var params={
			taskid: taskid.getValue()
		
		};
			_g_center_store.getProxy().extraParams=params;
			_g_center_store.load();
		}

		//监听窗口弹出事件
		function _w_max_onshow(){
			document.getElementById('_img').src = "${cxt}/static/image/demo/wxb.jpg";
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
		function _w_query_show(){
		 _w_query_q.show();
		
		}
		function _g_data_query(){
			_g_data.store.load();
		}
		function _w_data_query(){
			_w_query_q.show();
		}
		function _f_data_render(){
			//var taskid = taskid.getValue();
			var taskid = Ext.getCmp('taskid').getValue();
			AOS.ajax({
				url:'getFormList.jhtml',
				params:{
					taskid:taskid
				},
				ok:function(data){
				//alert(data);
					_f_data.form.setValues(data);
				
				}
			
			})
		}
		function _f_data_save(e){
			var outcome =e.btnEl.dom.innerText;
			AOS.ajax({
				url:'saveSubmitTask.jhtml',
				forms:_f_data,
				params:{
					outcome:outcome,
					state:2
				},
				ok:function(data){
					AOS.tip(data.appmsg);
				}
			})
		}
		function fn_render_zdyj(value, metaData, record, rowIndex, colIndex,
				store){
			return '<input type="button" value="指导意见" class="cbtn_danger" onclick="fn_zdyj_onclick();" />';
	}
	function _w_zdyj_load(){
		var record=Ext.getCmp('_g_center').getSelectionModel().getSelection();
		var id_=record[0].data.id_;
		var tablename="wsda2";	
//		Ext.getCmp('zdtablename').setValue("3333");
		var params ={
			tid:id_,
		zd_tablename:tablename
		};
			//这个Store的命名规则为：表格ID+"_store"。
			_g_zdyj_demo_store.getProxy().extraParams = params;
		    _g_zdyj_demo_store.load();
	}
	function _f_description_add(){
		var record=Ext.getCmp('_g_center').getSelectionModel().getSelection();
		var id_=record[0].data.id_;
		var tablename="wsda2";	
		var zd_description=Ext.getCmp('zd_description').getValue();
		AOS.ajax({
			 params: {tid:id_,
				 zd_tablename:tablename,
				 zd_description:zd_description
			 },
			 url: 'addZddescription.jhtml',
			 ok: function (data) {
					 if(data.appCode==-1){
						 AOS.tip("添加失败");
					 }else{
						 _g_zdyj_demo_store.load();
					 }
				 }
			 });	
	}
	</script>
</aos:onready>
<script type="text/javascript">
	function fn_zdyj_onclick() {
	Ext.getCmp('zd_description').setValue("");
		Ext.getCmp('_w_zdyj').show();
	}
</script>
</aos:html>
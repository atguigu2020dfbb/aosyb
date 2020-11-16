<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="/WEB-INF/tld/aos.tld" prefix="aos"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<aos:html>
<aos:head title="接收申请列表">
	<aos:include lib="ext" />
	<aos:base href="archive/datatemoprary" />
</aos:head>
<aos:body>
	<!--html代码  -->
	<div id="documentViewer" style="height: 600px;"></div>
</aos:body>
<aos:onready>
	<aos:viewport layout="border">
		<aos:gridpanel id="_g_receive_examine" url="listreceive.jhtml" region="center" split="true" pageSize="${pagesize }" hidePagebar="true"
					   enableLocking="false" height="200" onrender="_g_receive_show">
			<aos:hiddenfield  name="tablename" id="tablename" value="${tablename}"/>
			<aos:docked>
				<aos:dockeditem xtype="tbtext" text="列表" />
				<aos:dockeditem onclick="_w_query_show" text="查看档案" icon="query.png" />
			</aos:docked>
			<aos:selmodel type="row" mode="multi" />
			<aos:column type="rowno" />
			<aos:column header="id_" dataIndex="id_" hidden="true" />
			<aos:column header="批次号" dataIndex="pch" width="100" />
			<aos:column header="部门名称" dataIndex="bmmc" width="80" />
			<aos:column header="部门编号" dataIndex="bmbh" width="80" />
			<aos:column header="申请人" dataIndex="sqr" width="80" />
			<aos:column header="申请时间" dataIndex="sqsj" width="80" />
			<aos:column header="申请描述" dataIndex="sqms" width="100" />
			<aos:column header="数据表" dataIndex="tablename" width="100" />
			<aos:column header="意见描述" dataIndex="opinion_description" width="100" />
			<aos:column header="" flex="1" />
		</aos:gridpanel>

		<aos:window id="_w_first_compilation_message" title="合稿人合稿说明" width="500" >
			<aos:formpanel  id="_f_first_compilation_message" width="480">
				<aos:textareafield fieldLabel="操作说明" id="first_compilation_message"  name="first_compilation_message" width="450" allowBlank="false"/>
			</aos:formpanel>
			<aos:docked dock="bottom" ui="footer">
				<aos:dockeditem xtype="tbfill" />
				<aos:dockeditem text="确定" onclick="_save_first_compilation_message" icon="ok.png" />
				<aos:dockeditem onclick="#_w_first_compilation_message.hide();" text="关闭"
								icon="close.png" />
			</aos:docked>
		</aos:window>

	</aos:viewport>
	<script type="text/javascript">
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
			if(!AOS.selectone(_g_first_examine)){
				return;
			}
			var record=AOS.selectone(_g_first_examine);
			_f_first_examine.loadRecord(record);

		}
		function _save_first_compilation_message(){
			//执行保存操作
			var tablename_gridData = JSON.stringify(Ext.pluck(_g_first_examine_store.data.items, 'data'));
			var user="<%=session.getAttribute("user")%>";
			AOS.ajax({
				url: 'savefirstcompilationdetails.jhtml',
				forms:_f_first_compilation_message,
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
		function _first_save_compilation_details(){
			//此时把当前的信息全部保存到数据表中并添加信息
			_w_first_compilation_message.show();
			Ext.getCmp("first_compilation_message").setValue("");
		}
		function _g_receive_show(){
			_g_receive_examine_store.reload();
		}
		//申请的档案列表
		function _w_query_show(){
			var selection = AOS.selection(_g_receive_examine, 'id_');
			if (AOS.empty(selection)) {
				AOS.tip('查看档案前请先选中数据。');
				return;
			}
			var pch=AOS.selectone(_g_receive_examine).raw.pch;
			var tablename=AOS.selectone(_g_receive_examine).raw.tablename;
			window.parent.fnaddtab('','档案详情','/archive/datatemoprary/archive_details.jhtml?pch='+pch+'&tablename='+tablename);
		}
	</script>
</aos:onready>
</aos:html>
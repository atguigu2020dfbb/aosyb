<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="/WEB-INF/tld/aos.tld" prefix="aos"%>
<%@ taglib uri="/WEB-INF/tld/cll.tld" prefix="c"%>
<aos:html>
	<aos:head title="全宗">
		<aos:include lib="ext,swfupload" />
		<aos:include css="${cxt}/static/weblib/bootstrap/css/bootstrap.min.css"/>
		<aos:include css="${cxt}/static/css/fileinput.min.css"/>
		<aos:include js="${cxt}/static/js/jquery-3.2.1.min.js"/>
		<aos:include js="${cxt}/static/weblib/bootstrap/js/bootstrap.min.js"/>
		<aos:include js="${cxt}/static/js/fileinput.min.js"/>
		<aos:include js="${cxt}/static/js/zh.js"/>
		<aos:base href="archive/qzj" />
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
		<div id="filediv">
			<input id="file" class="file-loading" name="file" type="file" multiple>
		</div>
	</aos:body>
	<aos:onready>
		<aos:viewport layout="border">
			<aos:treepanel id="_t_module" region="west" bodyBorder="0 1 0 0" width="260" singleClick="false" rootVisible="false"
						   onitemclick="_g_module_query"  url="listModuleTree.jhtml" nodeParam="parent_id_" rootId="p">
				<aos:docked forceBoder="0 1 1 0">
					<aos:dockeditem xtype="tbtext" text="全宗模块菜单树" />
					<aos:dockeditem xtype="tbfill" />
					<aos:checkbox boxLabel="级联显示菜单" id="_cascade" name="cascade" onchang="_g_module_query" checked="false" />
				</aos:docked>
				<aos:menu>
					<aos:menuitem text="新增全宗模块" onclick="_w_module_show" icon="add.png" />
					<aos:menuitem xtype="menuseparator" />
					<aos:menuitem text="刷新" onclick="_t_module_refresh" icon="refresh.png" />
				</aos:menu>
			</aos:treepanel>
			<aos:panel region="center" border="false">
			<aos:layout type="vbox" align="stretch" />
			<aos:gridpanel id="_g_module" url="listModules.jhtml" region="center" hidePagebar="true" bodyBorder="1 0 0 0"
						   forceFit="false" onrender="_g_module_query" onitemdblclick="_w_module_u_show" onitemclick="_g_catalog_query" flex="0.4">
				<aos:docked forceBoder="0 0 1 0">
					<aos:dockeditem text="新增" tooltip="新增全宗模块" onclick="_w_module_show" icon="add.png" />
					<aos:dockeditem text="修改" tooltip="修改全宗模块" onclick="_w_module_u_show" icon="edit.png" />
					<aos:dockeditem text="删除" tooltip="删除全宗模块" onclick="_g_module_del" icon="del.png" />
					<aos:dockeditem xtype="tbseparator" />
					<aos:triggerfield emptyText="功能模块名称" name="name_" id="_name_" onenterkey="_g_module_query"
									  trigger1Cls="x-form-search-trigger" onTrigger1Click="_g_module_query" width="180" />
				</aos:docked>
				<aos:menu>
					<aos:menuitem text="新增全宗模块" onclick="_w_module_show" icon="add.png" />
					<aos:menuitem text="修改全宗模块" onclick="_w_module_u_show" icon="edit.png" />
					<aos:menuitem text="删除全宗模块" onclick="_g_module_del" icon="del.png" />
					<aos:menuitem xtype="menuseparator" />
					<aos:menuitem text="刷新" onclick="#_g_module_store.reload();" icon="refresh.png" />
				</aos:menu>
				<aos:selmodel type="checkbox" mode="multi" />
				<aos:column type="rowno" />
				<aos:column header="菜单流水号" dataIndex="id_" hidden="true" />
				<aos:column header="父节点流水号" dataIndex="parent_id_" hidden="true" />
				<aos:column header="页面url_" dataIndex="url_" hidden="true" />
				<aos:column header="节点语义ID" dataIndex="cascade_id_" hidden="true" />
				<aos:column header="全宗单位" dataIndex="name_" width="100" celltip="true" />
				<aos:column header="接收编号" dataIndex="jsbh" width="100" celltip="true" />
				<aos:column header="接收年度" dataIndex="jsnd" width="100" celltip="true" />
				<aos:column header="移交部门" dataIndex="yjbm" width="100" celltip="true" />
				<aos:column header="移交人" dataIndex="yjr" width="100" celltip="true" />
				<aos:column header="交接时间" dataIndex="jjsj" width="100" celltip="true" />
				<aos:column header="接收人" dataIndex="jsr" width="100" celltip="true" />
				<aos:column header="题名" dataIndex="tm" width="100" celltip="true" />
				<aos:column header="形成单位" dataIndex="scdw" width="100" celltip="true" />
				<aos:column header="形成时间" dataIndex="scsj" width="100" celltip="true" />
				<aos:column header="页数" dataIndex="ys" width="100" celltip="true" />
				<aos:column header="顺序号" dataIndex="sxh" width="100" celltip="true" />
				<aos:column header="备注" dataIndex="bz" width="100" celltip="true" />

				<aos:column header="全宗号" dataIndex="qzh_" width="250" celltip="true" hidden="true"/>

				<aos:column header="自动展开" dataIndex="is_auto_expand_" rendererField="bool_" width="80" hidden="true"/>
				<aos:column header="排序号" dataIndex="sort_no_" width="60" />
				<aos:column header="节点图标" dataIndex="icon_name_" width="120" hidden="true"/>
				<aos:column header="矢量图标" dataIndex="vector_" width="120" hidden="true"/>
				<aos:column header="热键" dataIndex="hotkey_" hidden="true"/>
				<aos:column header="叶子节点" dataIndex="is_leaf_" rendererField="bool_" width="80" hidden="true"/>
				<aos:column header="当前状态" dataIndex="status_" width="80" rendererField="enabled_" hidden="true"/>
				<aos:column header="上级功能模块菜单" dataIndex="parent_name_" width="180" celltip="true" hidden="true"/>
			</aos:gridpanel>
			<aos:gridpanel id="_g_catalog" url="listCatalogs.jhtml"  pageSize="${pagesize}"
							    flex="0.6" onitemdblclick="_w_catalog_u_show" rowclass="true">
					<aos:docked forceBoder="1 0 1 0">
						<aos:dockeditem xtype="tbtext" text="目录管理" />
						<aos:dockeditem xtype="tbseparator" />
                        <aos:hiddenfield name="tablename" id="tablename" value="Aos_sys_qzj_catalog"/>
                        <aos:hiddenfield id="querySession" name="querySession" />
						<aos:dockeditem text="新增" tooltip="新增目录" onclick="_w_catalog_show" icon="add.png" />
						<aos:dockeditem text="修改" tooltip="修改目录" onclick="_w_catalog_u_show" icon="edit.png" />
						<aos:dockeditem text="删除" tooltip="删除目录" onclick="_g_catalog_del" icon="del.png" />
                        <aos:dockeditem text="查询" icon="query.png" onclick="_w_query_show" />
						<aos:dockeditem text="设置条目数" icon="config1.png" onclick="_w_config_show" />
						<aos:dockeditem text="上传文件"  icon="more/go-top-8.png" onclick="_fileupload_data_show" />
						<aos:dockeditem text="显示" icon="picture.png"  id="_f_filename_message"
										onclick="_w_picture_show"  />
						<aos:dockeditem xtype="tbseparator" />
					</aos:docked>
					<aos:menu>
						<aos:menuitem text="新增目录" onclick="_w_catalog_show" icon="add.png" />
						<aos:menuitem text="修改目录" onclick="_w_catalog_u_show" icon="edit.png" />
						<aos:menuitem text="删除目录" onclick="_g_catalog_del" icon="del.png" />
						<aos:menuitem xtype="menuseparator" />
						<aos:menuitem text="刷新" onclick="_g_catalog_store.reload();" icon="refresh.png" />
					</aos:menu>
					<aos:selmodel type="checkbox" mode="multi" />
					<aos:column type="rowno" />
					<aos:column header="流水号" dataIndex="id_" hidden="true" />
					<aos:column header="全宗号id" dataIndex="tid" hidden="true" />
				    <aos:column header="全宗号" dataIndex="qzh"  celltip="true" width="150" />
				    <aos:column header="全宗单位" dataIndex="qzdw"  celltip="true" width="200"/>
					<aos:column header="类目名称" dataIndex="lb" celltip="true" width="200"/>
					<aos:column header="类号" dataIndex="lh" celltip="true" width="150"/>
					<aos:column header="件号" dataIndex="jh" celltip="true" width="150"/>
					<aos:column header="全宗卷编号" dataIndex="qzjbh"  celltip="true" width="200"/>
					<aos:column header="题名" dataIndex="tm"  celltip="true" width="300"/>
					<aos:column header="责任者" dataIndex="zrz"  celltip="true" width="200"/>
					<aos:column header="日期" dataIndex="rq" celltip="true" width="100" />
					<aos:column header="页数" dataIndex="ys"  celltip="true" width="150" />
					<aos:column header="电子文件" dataIndex="_path" width="200" celltip="true" />
					<aos:column header="备注" dataIndex="bz" width="200" celltip="true" />
				<aos:column header="" flex="1" />
				</aos:gridpanel>
			</aos:panel>
		</aos:viewport>
        <aos:window id="_w_query_q" title="查询" width="720" autoScroll="true"
                    layout="fit">
            <aos:tabpanel id="_tabpanel" region="center" activeTab="0"
                          bodyBorder="0 0 0 0" tabBarHeight="30">
                <aos:tab title="列表式搜索" id="_tab_org">
                    <aos:formpanel id="_f_query" layout="column" columnWidth="1">
                        <aos:hiddenfield name="listtablename"  value="${listtablename }" />
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
		<aos:window id="_w_config" title="设置"
					autoScroll="true"  layout="column" width="300" border="false" >
			<aos:formpanel id="_f_config" layout="column" labelWidth="80" columnWidth="1" >
				<aos:textfield fieldLabel="每页显示" name="pagesize" value="${pagesize }" columnWidth="0.9" />
				<aos:docked dock="bottom" ui="footer">
					<aos:dockeditem xtype="tbfill" />
					<aos:dockeditem onclick="_f_info_ok" xtype="button" text="确定" icon="ok.png" />
					<aos:dockeditem onclick="#_w_config.hide();" xtype="button" text="取消" icon="del.png" />
					<aos:dockeditem xtype="tbfill" />
				</aos:docked>
			</aos:formpanel>
		</aos:window>
		<aos:window id="_w_module_find" title="上级菜单[双击选择]" height="-10" layout="fit" autoScroll="true"
					onshow="_t_module_find_refresh">
			<aos:treepanel id="_t_module_find" singleClick="false" width="320" bodyBorder="0 0 0 0" url="listModuleTree.jhtml"
						   nodeParam="parent_id_" rootId="2a4c060f44d944ea926fe44522ce7b39" rootVisible="true" rootExpanded="false" rootText="${_root.name_}"
						   rootIcon="${_root.icon_name_}" onitemdblclick="_t_module_find_select">
				<aos:menu>
					<aos:menuitem text="选择" icon="ok1.png" onclick="_t_module_find_select" />
					<aos:menuitem text="刷新" onclick="_t_module_find_refresh" icon="refresh1.png" />
					<aos:menuitem xtype="menuseparator" />
					<aos:menuitem text="关闭" onclick="#_w_module_find.hide();" icon="del.png" />
				</aos:menu>
			</aos:treepanel>
		</aos:window>
		<aos:window id="_w_module" title="新增全宗菜单" width="600" onshow="_new_jsbh">
			<aos:formpanel id="_f_module" width="600" layout="column" >
				<aos:hiddenfield name="parent_id_" />
				<aos:triggerfield fieldLabel="上级菜单" name="parent_id_desc" allowBlank="false" editable="false" columnWidth="0.49"
								  onfocus="_w_module_find_show" trigger1Cls="x-form-search-trigger" onTrigger1Click="_w_module_find_show" />
				<aos:textfield name="name_" fieldLabel="全宗单位"  columnWidth="0.49" autoScroll="false"/>
				<aos:textfield name="jsbh" fieldLabel="接收编号"  columnWidth="0.49" />
				<aos:textfield name="jsnd" fieldLabel="接收年度"  columnWidth="0.49" />

				<aos:textfield name="yjbm" fieldLabel="移交部门"  columnWidth="0.49" />
				<aos:textfield name="yjr" fieldLabel="移交人"  columnWidth="0.49" />
				<aos:datefield name="jssj" fieldLabel="交接时间"  columnWidth="0.49" />
				<aos:textfield name="jsr" fieldLabel="接收人"  columnWidth="0.49" value="${user}"/>
				<aos:textfield name="tm" fieldLabel="题名"  columnWidth="0.98" />
				<aos:textfield name="xcdw" fieldLabel="形成单位"  columnWidth="0.49" />
				<aos:datefield name="xcsj" fieldLabel="形成时间"  columnWidth="0.49" />
				<aos:textfield name="ys" fieldLabel="页数"  columnWidth="0.49" />
				<aos:textfield name="sxh" fieldLabel="顺序号"  columnWidth="0.49" />
				<aos:textfield name="bz" fieldLabel="备注"  columnWidth="0.98" />
				<aos:numberfield name="sort_no_" fieldLabel="排序号" columnWidth="0.98" value="1" minWidth="0" maxValue="99999999" />
			</aos:formpanel>
			<aos:docked dock="bottom" ui="footer">
				<aos:dockeditem xtype="tbfill" />
				<aos:dockeditem onclick="_f_module_save" text="保存" icon="ok.png" />
				<aos:dockeditem onclick="#_w_module.hide();" text="关闭" icon="close.png" />
			</aos:docked>
		</aos:window>
		<aos:window id="_w_module_u" title="修改功能模块菜单" width="600">
			<aos:formpanel id="_f_module_u" width="600" layout="column" labelWidth="65">
				<aos:hiddenfield name="id_" fieldLabel="菜单流水号" />
				<aos:hiddenfield name="parent_id_" />
				<aos:triggerfield fieldLabel="上级菜单" name="parent_id_desc" allowBlank="false" editable="false"
								  onfocus="_w_module_find_show" trigger1Cls="x-form-search-trigger" columnWidth="0.49" onTrigger1Click="_w_module_find_show" />
				<aos:textfield name="name_" fieldLabel="全宗单位"  columnWidth="0.49"  allowBlank="false"/>
				<aos:textfield name="yjbm" fieldLabel="移交部门"  columnWidth="0.49" />
				<aos:textfield name="yjr" fieldLabel="移交人"  columnWidth="0.49" />
				<aos:datefield name="jssj" fieldLabel="交接时间"  columnWidth="0.49" />
				<aos:textfield name="jsr" fieldLabel="接收人"  columnWidth="0.49" value="${user}"/>
				<aos:textfield name="tm" fieldLabel="题名"  columnWidth="0.98" />
				<aos:textfield name="xcdw" fieldLabel="形成单位"  columnWidth="0.49" />
				<aos:datefield name="xcsj" fieldLabel="形成时间"  columnWidth="0.49" />
				<aos:textfield name="ys" fieldLabel="页数"  columnWidth="0.49" />
				<aos:textfield name="sxh" fieldLabel="顺序号"  columnWidth="0.49" />
				<aos:textfield name="bz" fieldLabel="备注"  columnWidth="0.98" />
				<aos:numberfield name="sort_no_" fieldLabel="排序号" columnWidth="0.98" value="1" minWidth="0" maxValue="99999999" />
			</aos:formpanel>
			<aos:docked dock="bottom" ui="footer">
				<aos:dockeditem xtype="tbfill" />
				<aos:dockeditem text="上一条" icon="more/go-previous.png"  onclick="_f_previous_data"/><%----%>
				<aos:dockeditem text="下一条" icon="more/go-next.png" onclick="_f_next_data"/><%----%>

				<aos:dockeditem onclick="_f_module_u_update" text="保存" icon="ok.png" />
				<aos:dockeditem onclick="#_w_module_u.hide();" text="关闭" icon="close.png" />
			</aos:docked>
		</aos:window>
		<aos:window id="_w_catalog" title="新增全宗目录">
			<aos:formpanel id="_f_catalog" width="600" layout="column" labelWidth="65">
				<aos:hiddenfield name="id_" />
				<aos:hiddenfield name="tid" />
				<aos:textfield name="qzh" fieldLabel="全宗号"  columnWidth="0.49" onblur="add_qzbh"/>
				<aos:textfield name="qzdw" fieldLabel="全宗单位"  columnWidth="0.49" />
				<aos:combobox name="lb" fieldLabel="类目名称" onselect="select_lb" columnWidth="0.49" allowBlank="false" emptyText="请选择..." dicField="qzj_lb" />
				<aos:textfield name="lh" fieldLabel="类号" columnWidth="0.49" onblur="add_qzbh"/>
				<aos:textfield name="jh" fieldLabel="件号" columnWidth="0.49" onblur="add_qzbh"/>
				<aos:textfield name="qzjbh" fieldLabel="全宗编号" columnWidth="0.49" />
				<aos:textfield name="tm" fieldLabel="题名" columnWidth="0.98" />
				<aos:textfield name="zrz" fieldLabel="责任者" columnWidth="0.49"/>
				<aos:datefield name="rq" fieldLabel="日期" columnWidth="0.49"/>
				<aos:textfield name="ys" fieldLabel="页数" columnWidth="0.49"/>
				<aos:textfield name="_path" fieldLabel="电子文件" columnWidth="0.49"/>
				<aos:textareafield name="bz" fieldLabel="备注" columnWidth="0.98" />
			</aos:formpanel>
			<aos:docked dock="bottom" ui="footer">
				<aos:dockeditem xtype="tbfill" />
				<aos:dockeditem onclick="_f_catalog_save" text="保存" icon="ok.png" />
				<aos:dockeditem onclick="#_w_catalog.hide();" text="关闭" icon="close.png" />
			</aos:docked>
		</aos:window>
		<aos:window id="_w_catalog_u" title="修改全宗目录" >
			<aos:formpanel id="_f_catalog_u" width="600" layout="column">
				<aos:hiddenfield name="id_" />
				<aos:hiddenfield name="tid" />
				<aos:textfield name="qzh" fieldLabel="全宗号"  columnWidth="0.49" onblur="add_qzbh2"/>
				<aos:textfield name="qzdw" fieldLabel="全宗单位"  columnWidth="0.49" />
				<aos:combobox name="lb" fieldLabel="类目名称" columnWidth="0.49"  onselect="select_lb2" allowBlank="false" emptyText="请选择..." dicField="qzj_lb" />
				<aos:textfield name="lh" fieldLabel="类号" columnWidth="0.49" onblur="add_qzbh2"/>
				<aos:textfield name="jh" fieldLabel="件号" columnWidth="0.49" onblur="add_qzbh2"/>
				<aos:textfield name="qzjbh" fieldLabel="全宗卷编号" columnWidth="0.49" />
				<aos:textfield name="tm" fieldLabel="题名" columnWidth="0.98" />
				<aos:textfield name="zrz" fieldLabel="责任者" columnWidth="0.49"/>
				<aos:datefield name="rq" fieldLabel="日期" columnWidth="0.49"/>
				<aos:textfield name="ys" fieldLabel="页数" columnWidth="0.49"/>
				<aos:textfield name="_path" fieldLabel="电子文件" columnWidth="0.49"/>
				<aos:textareafield name="bz" fieldLabel="备注" columnWidth="0.98" />
			</aos:formpanel>
			<aos:docked dock="bottom" ui="footer">
				<aos:dockeditem xtype="tbfill" />
				<aos:dockeditem text="上一条" icon="more/go-previous.png"  onclick="_f_previous_data2"/><%----%>
				<aos:dockeditem text="下一条" icon="more/go-next.png" onclick="_f_next_data2"/><%----%>

				<aos:dockeditem onclick="_f_catalog_u_save" text="保存" icon="ok.png" />
				<aos:dockeditem onclick="#_w_catalog_u.hide();" text="关闭" icon="close.png" />
			</aos:docked>
		</aos:window>
		<%--上传文件--%>
		<aos:window id="_fileupload_add" title="上传文件"  width="700" height="450" autoScroll="true"  >
			<aos:formpanel width="680" height="450" id="formpanell" layout="column" contentEl="filediv" autoScroll="true">
			</aos:formpanel>
			<aos:docked dock="bottom" ui="footer">
				<aos:dockeditem xtype="tbfill" />

				<aos:dockeditem onclick="#_fileupload_add.hide();" text="关闭" icon="close.png" />
			</aos:docked>
		</aos:window>
		<script type="text/javascript">
			//_path列转换
			function fn_path_render(value, metaData, record, rowIndex, colIndex,
									store) {
				if (value >= 1) {
					return '<img src="${cxt}/static/icon/picture.png" />';
				} else {
					return '<img src="${cxt}/static/icon/picture_empty.png" />';
				}
			}
			function changeRowClass(record, rowIndex, rowParams, store){

				//得到当前行的指定的列的值
				if(record.get("_path")>=1){
					return 'grid-one-column';
				}else{
					return 'grid-zero-column';
				}

			}
			//弹出选择上级菜单窗口：新增
			function _w_module_find_show(){
				_w_module_find.show();
			}
			//弹出上传文件窗口
			function _fileupload_data_show() {

				//我得加个重置
				var tablename=Ext.getCmp("tablename").getValue();

				var selection = AOS.selection(_g_module, 'id_');
				var ids = AOS.selection(_g_catalog, 'id_');
				if (AOS.empty(selection)||AOS.empty(ids)) {
					AOS.tip('请选中全宗和要上传的条目数据');
					return;
				}else{
					Ext.getCmp("_fileupload_add").show();
					$("#file").fileinput('destroy');
					$(document).ready(function() {
						$("#file").fileinput({
							language: 'zh', //设置语言
							uploadUrl: '${cxt}/archive/upload/uploadfiles_data.jhtml', //上传的地址
							uploadExtraData:{
								tablename: tablename,
								id_:AOS.selectone(_g_catalog).data.id_
							},
							fileActionSettings:{
								showZoom:false//显示预览按钮
							},
							allowedFileExtensions: ['jpg', 'gif', 'png','pdf','tif'],//接收的文件后缀
							showRemove: true,//显示删除按钮
							showUpload: true, //是否显示上传按钮
							showCaption: true,//是否显示标题
							browseClass: "btn btn-primary btn-xs", //按钮样式
							removeClass: "btn btn-danger btn-xs",
							uploadClass: "btn btn-success btn-xs",
							dropZoneEnabled: true,//是否显示拖拽区域
							//dropZoneTitle: "可以将文件拖放到这里",//拖拽区域显示文字
							maxFileSize: 0,//单位为kb，如果为0表示不限制文件大小
							minFileCount: 0,//表示允许同时上传的最小文件个数
							maxFileCount: 1000,//表示允许同时上传的最大文件个数
							hideThumbnailContent: true //在缩略图预览中隐藏图像，pdf，文本或其他内容---默认false-不隐藏
						});
					});
				}

			}
			//刷新上级组织树
			function _t_module_find_refresh(){
				var refreshnode = AOS.selectone(_t_module_find);
				if (!refreshnode) {
					refreshnode = _t_module_find.getRootNode();
				}
				if (refreshnode.isLeaf()) {
					refreshnode = refreshnode.parentNode;
				}
				_t_module_find_store.load({
					node : refreshnode,
					callback : function() {
						//收缩再展开才能在刷新时正确显示expanded=true的节点的子节点
						refreshnode.collapse();
						refreshnode.expand();
					}
				});
			}
			//显示上传面板
			function _w_picture_show() {
				var record = AOS.selectone(_g_catalog);
				var uploadPanel= new Ext.ux.uploadPanel.UploadPanel({
					//addFileBtnText : '选择文件...',
					//uploadBtnText : '上传',
					deleteBtnText : '移除',
					downBtnText   : '下载',
					removeBtnText : '移除所有',
					cancelBtnText : '取消上传',
					use_query_string : true,
					listeners:{
						//双击
						itemdblclick : function(grid,row,kk,rowIndex){
							//parent.fnaddtab(row.data.id, '电子文件',
							//					'archive/data/openFile.jhtml?id='+row.data.pid+'&tid='+row.data.tid+'&type='+row.data.type+'&tablename='+tablename.getValue());
							parent.fnaddtab(row.data.id, '电子文件',
									'archive/data/openPdfFile.jhtml?id='+row.data.pid+'&tid='+row.data.tid+'&type='+row.data.type+'&tablename='+tablename.getValue());
							/*parent.fnaddtab(row.data.id, '电子文件',
                                    'archive/data/openFile.jhtml?id='+row.data.pid+'&tid='+row.data.tid+'&type='+row.data.type+'&tablename='+tablename.getValue()+'&index='+rowIndex);*/
							/*parent.fnaddtab(row.data.id, '电子文件',
                                                 'archive/data1/initZpData.jhtml?id='+row.data.pid+'&tid='+row.data.tid+'&type='+row.data.type+'&tablename='+tablename.getValue());*/
						}
					},
					onUpload : function(){
						var me=Ext.getCmp("uploadpanel");
						if (this.swfupload&&this.store.getCount()>0) {
							if (this.swfupload.getStats().files_queued > 0){
								this.showBtn(this,false);
								this.swfupload.uploadStopped = false;
								this.swfupload.startUpload();
							}
						}
						// this.swfupload.destroy();
					},
					deletePath:	function(grid, rowIndex, colIndex) {
						var me=Ext.getCmp("uploadpanel").getSelectionModel().getSelection();
						var id = me[0].get('pid');
						var tid = me[0].get('tid');
						var rowIndex = Ext.getCmp("uploadpanel").getStore().indexOf(me[0]);
						AOS.ajax({
							params: {id_:id,
								tablename:tablename.getValue(),
								tid:tid,
								tm:record.data.tm
							}, // 提交参数,
							url: 'deletePath.jhtml',
							ok: function (data) {
								var me=Ext.getCmp("uploadpanel");
								//me.store.reload();
								me.store.remove(me.store.getAt(rowIndex));
								// me.store.load();
								AOS.tip(data.appmsg);
							}
						});
					},
					onRemoveAll: function (){
						var selection = AOS.selection(_g_catalog, 'id_');
						AOS.ajax({
							params: {aos_rows_: selection,
								tm:record.data.tm,
								tablename: tablename.getValue()
							},
							url: 'deletePathAll.jhtml',
							ok: function (data) {
								var me=Ext.getCmp("uploadpanel");
								me.removeAll();
								AOS.tip(data.appmsg);
							}
						});
					},
					//下载
					onDownPath:function(grid, rowIndex, colIndex){
						//得到选中的条目
						var me=Ext.getCmp("uploadpanel").getSelectionModel().getSelection();
						var id = me[0].get('pid');
						AOS.file('downloadPath.jhtml?id_='+id+'&tablename='+tablename.getValue());
					},
					upload_complete_handler : function(file){

						var me =Ext.getCmp("uploadpanel");

						AOS.ajax({
							params: {tid: record.data.id_,tablename:tablename.getValue()},
							url: 'getPath.jhtml',
							ok: function (data) {
								for(i in data){
									me.store.getAt(file.index).set({"pid":data[i].id_,"tid":data[i].tid});
								}
							}
						});
					},
					post_params:{tid:record.data.id_,
						tablename:tablename.getValue()
					},
					file_size_limit : 10000,//MB

					flash_url : "${cxt}/static/swfupload/swfupload.swf",
					flash9_url : "${cxt}/static/swfupload/swfupload_f9.swf",
					upload_url : "${cxt}/archive/upload/archiveUpload.jhtml?tm="+record.data.tm
				});

				var w_data_path = new Ext.Window({
					title : '电子文件',
					width : 700,
					modal:true,
					closeAction : 'destroy',
					items:[uploadPanel]
				});
				w_data_path.on("show",w_data_path_onshow);
				w_data_path.on("close",w_data_path_onclose);
				w_data_path.show();
			}
			function w_data_path_onclose(){
				_g_catalog_store.load();
			}
			function remember_load(tablename,type,value,name){
				AOS.ajax({
					params: {
						tablename:tablename,
						type:type,
						flag:value,
						name:name
					}, // 提交参数,
					url: 'saveRemember.jhtml',
					ok: function (data) {

					}
				});
			}
			function w_data_path_onshow() {
				//var me = this.settings.custom_settings.scope_handler;

				var record = AOS.selectone(Ext.getCmp('_g_catalog'));
				var me=Ext.getCmp("uploadpanel");
				me.store.removeAll();
				AOS.ajax({
					params: {tid: record.data.id_,tablename:tablename.getValue()},
					url: 'getPath.jhtml',
					ok: function (data) {
						for(i in data){
							me.store.add({
								pid:data[i].id_,
								tid:data[i].tid,
								name:data[i]._path,
								fileName:data[i].filename,
								type:data[i].filetype,
								percent:100,
								status:-4,
							});
						}
					}
				});
				//在得到当前电子文件选框的状态
				AOS.ajax({
					params: {
						tablename:Ext.getCmp("tablename").getValue(),
						type:"checkbox"
					}, // 提交参数,
					url: 'getRemember.jhtml',
					ok: function (data) {
						/*if(data.ocr==""||typeof(data.ocr) == "undefined"){
                            Ext.getCmp("ocr").setValue(false);
                        }else{
                            Ext.getCmp("ocr").setValue(data.ocr);
                        }
                        if(data.mark==""||typeof(data.mark) == "undefined"){
                            Ext.getCmp("mark").setValue(false);
                        }else{
                            Ext.getCmp("mark").setValue(data.mark);
                        }*/
					}
				});
			}
			//上级菜单节点选择事件
			function _t_module_find_select(){
				var record = AOS.selectone(_t_module_find);
				if(_w_module.isVisible()){
					//新增窗口
					_f_module.down('[name=parent_id_]').setValue(record.raw.id);
					_f_module.down('[name=parent_id_desc]').setValue(record.raw.text);
				}else{
					//修改窗口
					_f_module_u.down('[name=parent_id_]').setValue(record.raw.id);
					_f_module_u.down('[name=parent_id_desc]').setValue(record.raw.text);
				}
				_w_module_find.hide();
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
				_g_catalog_store.getProxy().extraParams = params;
                //_g_data_store.getProxy().extraParams = param;
				_g_catalog_store.load();
                _w_query_q.hide();
                AOS.reset(_f_query);
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
			//查询菜单树列表
			function _g_module_query() {
				var params = {
					name_ : _name_.getValue(),
					cascade : _cascade.getValue()
				};
				var record = AOS.selectone(_t_module);
				if(!AOS.empty(record)){
					params.id_ = record.raw.id;
					params.cascade_id_ = record.raw.cascade_id_;
				}else{
					//页面初始化第一次查询
					params.cascade = 'true';
				}
				_g_module_store.getProxy().extraParams = params;
				_g_module_store.load({
					callback : function(a) {
						if(!AOS.empty(record)){
							var g_record = _g_module_store.find('id_', record.data.id);
							_g_module.getSelectionModel().select(g_record);
						}
					}
				});
			}
			function _w_config_show(){

				_w_config.show();
			}

			function _f_info_ok(){
				AOS.ajax({
					forms : _f_config,
					url : 'setPagesize.jhtml',
					params:{
						tablename : tablename.getValue()
					},
					ok : function(data) {
						_w_config.hide();
						_g_catalog_store.reload();
						AOS.tip(data.appmsg);
					}
				});

			}
			//刷新菜单树
			function _t_module_refresh(root) {
				var refreshnode = AOS.selectone(_t_module);
				if (!refreshnode) {
					refreshnode = _t_module.getRootNode();
				}
				if (refreshnode.isLeaf()) {
					refreshnode = refreshnode.parentNode;
				}
				if(root){
					refreshnode = _t_module.getRootNode();
				}
				var params = {
				};
				_t_module_store.getProxy().extraParams = params;
				_t_module_store.load({
					node : refreshnode,
					callback : function() {
						//收缩再展开才能在刷新时正确显示expanded=true的节点的子节点
						refreshnode.collapse();
						refreshnode.expand();
						//如果刷新节点的子节点为空，则矫正节点再刷新其父节点
						if(!refreshnode.hasChildNodes()){
							_t_module_refresh2(refreshnode.parentNode);
						}
					}
				});
			}

			//刷新菜单树供回调使用
			function _t_module_refresh2(refreshnode) {
				_t_module_store.load({
					node : refreshnode,
					callback : function() {
						refreshnode.collapse();
						refreshnode.expand();
					}
				});
			}

			//弹出新增功能模块菜单
			function _w_module_show(){
				AOS.reset(_f_module);
				var record = AOS.selectone(_t_module);
				if(!AOS.empty(record)){
					_f_module.down('[name=parent_id_]').setValue(record.raw.id);
					_f_module.down('[name=parent_id_desc]').setValue(record.raw.text);
				}
				_w_module.show();
			}

			//弹出修改功能模块菜单窗口
			function _w_module_u_show(){
				AOS.reset(_f_module_u);
				var record = AOS.selectone(_g_module);
				if(record){
					_w_module_u.show();
					_f_module_u.loadRecord(record);
					_f_module_u.down('[name=parent_id_]').setValue(record.data.parent_id_);
					_f_module_u.down('[name=parent_id_desc]').setValue(record.data.parent_name_);
					//根节点不允许修改上级节点字段
					if(record.data.cascade_id_ === '0'){
						AOS.reads(_f_module_u, 'parent_id_');
					}else{
						AOS.edits(_f_module_u, 'parent_id_');
					}
				}
			}
			//新增功能模块保存
			function _f_module_save(){
				AOS.ajax({
					forms : _f_module,
					url : 'saveModule.jhtml',
					ok : function(data) {
						_w_module.hide();
						_g_module_store.reload();
						_t_module_refresh();
						AOS.tip(data.appmsg);
					}
				});
			}

			//修改功能模块保存
			function _f_module_u_update(){
				AOS.ajax({
					forms : _f_module_u,
					url : 'updateModule.jhtml',
					ok : function(data) {
						AOS.tip(data.appmsg);
						_w_module_u.hide();
						_t_module_refresh();
						_g_module_store.reload();
					}
				});
			}

			//删除功能模块菜单
			function _g_module_del(){
				var rows = AOS.rows(_g_module);
				if(rows === 0){
					AOS.tip('删除前请先选中数据。');
					return;
				}
				var msg =  AOS.merge('确认要删除选中的[{0}]个功能模块菜单数据吗？', rows);
				AOS.confirm(msg, function(btn){
					if(btn === 'cancel'){
						AOS.tip('删除操作被取消。');
						return;
					}
					AOS.ajax({
						url : 'deleteModule.jhtml',
						params:{
							aos_rows_:  AOS.selection(_g_module, 'id_')
						},
						ok : function(data) {
							if(data.appcode === -1){
								AOS.err(data.appmsg);
								return ;
							}
							AOS.tip(data.appmsg);
							_g_module_store.reload();
							_t_module_refresh();
						}
					});
				});
			}
			function _c_url(){
				var relate_value = relate_table_url.getValue();
				if(relate_value == 1){
					_f_module.down('[name=url_]').setValue('archive/data/initData.jhtml');
				}
			}

			//弹出页面元素新增窗口
			function _w_catalog_show(){
				AOS.reset(_f_catalog);
				var rows = AOS.rows(_g_module);
				var record = AOS.selectone(_t_module);
				if(AOS.empty(record)){
					AOS.tip('操作被取消，请选择全宗树。');
					return;
				}
				if(rows === 1){
					_w_catalog.show();
					var record = AOS.selectone(_g_module,true);
					_f_catalog.down('[name=tid]').setValue(record.data.id_);
					return;
				}else{
					AOS.tip('操作被取消，请先选择一条全宗记录。');
				}
			}
			//查询页面元素列表
			function _g_catalog_query() {
				var params = {
				};
				var record = AOS.selectone(_g_module);
				if(!AOS.empty(record) && record.data.id != '0'){
					params.tid = record.data.id_;
				}
				_g_catalog_store.getProxy().extraParams = params;
				_g_catalog_store.load();
			}
			//新增全宗目录保存
			function _f_catalog_save(){
				AOS.ajax({
					forms : _f_catalog,
					url : 'saveCatalog.jhtml',
					ok : function(data) {
						if(data.appcode === 1){
							_w_catalog.hide();
							AOS.tip("添加成功!");
							_g_catalog_store.reload();
						}else{
							AOS.tip("添加失败!");
						}
					}
				});
			}
			//修改全宗目录弹出窗口
			function _w_catalog_u_show(){
				AOS.reset(_f_catalog_u);
				var record = AOS.selectone(_g_catalog);
				if(record){
					_w_catalog_u.show();
					_f_catalog_u.loadRecord(record);
					_f_catalog_u.down('[name=tid]').setValue(record.data.tid);
				}
			}
			//修改全宗目录信息
			function _f_catalog_u_save(){
				AOS.ajax({
					forms : _f_catalog_u,
					url : 'updateCatalog.jhtml',
					ok : function(data) {
						if(data.appcode === 1){
							_w_catalog_u.hide();
							AOS.tip("添加成功!");
							_g_catalog_store.reload();
						}else{
							AOS.tip("添加失败!");
						}
					}
				});
			}
			//删除全宗目录信息
			function _g_catalog_del(){
				var rows = AOS.rows(_g_catalog);
				if(rows === 0){
					AOS.tip('删除前请先选中全宗目录。');
					return;
				}
				var msg =  AOS.merge('确认要删除选中的[{0}]条全宗目录吗？', rows);
				AOS.confirm(msg, function(btn){
					if(btn === 'cancel'){
						AOS.tip('删除操作被取消。');
						return;
					}
					AOS.ajax({
						url : 'deleteCatalog.jhtml',
						params:{
							aos_rows_: AOS.selection(_g_catalog, 'id_')
						},
						ok : function(data) {
							AOS.tip(data.appmsg);
							_g_catalog_store.reload();
						}
					});
				});
			}
			//导入窗口
			function _w_import_show() {
				var record = AOS.selectone(_g_module);
				if(AOS.empty(record)){
					AOS.tip('导入前请选择全宗名称。');
					return;
				}
				var tablename="Aos_sys_qzj_catalog";
				window.parent.fnaddtab('1213','全宗目录导入','/archive/qzj/initImport.jhtml?tid='+record.data.id_+'&tablename='+tablename);
			}
			//生成XLS报表
			function fn_xls() {
				var tablename="Aos_sys_qzj_catalog";
				var record = AOS.selectone(_g_module);
				if(AOS.empty(record)){
					AOS.tip('导出前请选择全宗名称。');
					return;
				}
				AOS.ajax({
					url : 'fillReport.jhtml',
					params:{
						tid:record.data.id_,
						query:querySession.getValue(),
						tablename : tablename
					},
					ok : function(data) {
						AOS.file('${cxt}/report/xls_qzj.jhtml');
					}
				});
			}

			//生成XLSX报表
			function fn_xlsx() {
				var tablename="Aos_sys_qzj_catalog";
				var record = AOS.selectone(_g_module);
				if(AOS.empty(record)){
					AOS.tip('导出前请选择全宗名称。');
					return;
				}
				AOS.ajax({
					url : 'fillReport.jhtml',
					params:{
						tid:record.data.id_,
						tablename : tablename
					},
					ok : function(data) {
						AOS.file('${cxt}/report/xlsx_qzj.jhtml');
					}
				});
			}
			//选择类别
			function select_lb(){
				var lb=_f_catalog.form.findField("lb").getValue();
				if(lb=="1.全宗(馆藏)介绍类"){
					_f_catalog.form.findField("lh").setValue("1");
				}else if(lb=="2.档案收集类"){
					_f_catalog.form.findField("lh").setValue("2");
				}else if(lb=="3.档案整理类"){
					_f_catalog.form.findField("lh").setValue("3");
				}else if(lb=="4.档案鉴定类"){
					_f_catalog.form.findField("lh").setValue("4");
				}else if(lb=="5.档案保管类"){
					_f_catalog.form.findField("lh").setValue("5");
				}else if(lb=="6.档案统计类"){
					_f_catalog.form.findField("lh").setValue("6");
				}else if(lb=="7.档案利用类"){
					_f_catalog.form.findField("lh").setValue("7");
				}else if(lb=="8.新技术应用类"){
					_f_catalog.form.findField("lh").setValue("8");
				}
			}
			//选择类别
			function select_lb2(){
				var lb=_f_catalog_u.form.findField("lb").getValue();
				if(lb=="1.全宗(馆藏)介绍类"){
					_f_catalog_u.form.findField("lh").setValue("1");
				}else if(lb=="2.档案收集类"){
					_f_catalog_u.form.findField("lh").setValue("2");
				}else if(lb=="3.档案整理类"){
					_f_catalog_u.form.findField("lh").setValue("3");
				}else if(lb=="4.档案鉴定类"){
					_f_catalog_u.form.findField("lh").setValue("4");
				}else if(lb=="5.档案保管类"){
					_f_catalog_u.form.findField("lh").setValue("5");
				}else if(lb=="6.档案统计类"){
					_f_catalog_u.form.findField("lh").setValue("6");
				}else if(lb=="7.档案利用类"){
					_f_catalog_u.form.findField("lh").setValue("7");
				}else if(lb=="8.新技术应用类"){
					_f_catalog_u.form.findField("lh").setValue("8");
				}
			}
			function add_qzbh(){
				var qzh=_f_catalog.form.findField("qzh").getValue();
				var lh=_f_catalog.form.findField("lh").getValue();
				var jh=_f_catalog.form.findField("jh").getValue();
				_f_catalog.form.findField("qzjbh").setValue(qzh+"-"+lh+"-"+jh);
			}
			function add_qzbh2(){
				var qzh=_f_catalog_u.form.findField("qzh").getValue();
				var lh=_f_catalog_u.form.findField("lh").getValue();
				var jh=_f_catalog_u.form.findField("jh").getValue();
				_f_catalog_u.form.findField("qzjbh").setValue(qzh+"-"+lh+"-"+jh);
			}
			function _new_jsbh(){
				AOS.ajax({
					params:{
						name_:"全宗卷接收编号"
					},
					url:'qzjjsbh.jhtml',
					ok:function(data){
						//设计一个随机数编号
						//年月日
						var time = (new Date).getTime();
						var yesday = new Date(time).getFullYear(); // 获取的是前一天日期
						_f_module.form.findField("jsbh").setValue("JS"+yesday+data.index);
					}
				});
			}
			function _import_data(){
				var record = AOS.selectone(_g_catalog);
				var _classtree=Ext.getCmp("_classtree_f").getValue();
				var file123=$("#filename");
				var file=$("#filename").attr("title");
				var pch="";
				if(!AOS.empty(record)){
					pch=record.data.pch;
					window.parent.fnaddtab('417','数据导入','archive/datatemporary/initImport_temporary.jhtml?tablename='+tablename.getValue()+'&pch='+pch+'&_classtree='+_classtree+'&file='+file);
				}else{
					AOS.tip("请选择要导入数据的任务!");
				}
			}
			//上一页
			function _f_previous_data(){
				var count=Ext.getCmp("_g_module").getStore().getCount();
				var me=Ext.getCmp("_g_module").getSelectionModel().getSelection();
				//var record = AOS.selectone(_g_module);
				//得到执行行的坐标
				var rowIndex = Ext.getCmp("_g_module").getStore().indexOf(me[0]);
				if(rowIndex==0){
					AOS.err("当前第一条!");
					return;
				}
				var s=Ext.getCmp("_g_module").getStore().getAt(rowIndex-1);
				//原先行取消选中
				Ext.getCmp("_g_module").getSelectionModel().deselect(rowIndex);
				//此时让光标选中上一行
				Ext.getCmp("_g_module").getSelectionModel().select(rowIndex-1, true);
				//组件被显示后触发。
				Ext.getCmp("_f_module_u").form.setValues(s.data);
			}
			//下一页
			function _f_next_data(){
				var count=Ext.getCmp("_g_module").getStore().getCount();
				var me=Ext.getCmp("_g_module").getSelectionModel().getSelection();
				//var record = AOS.selectone(_g_module);
				//得到执行行的坐标
				var rowIndex = Ext.getCmp("_g_module").getStore().indexOf(me[0]);
				if(rowIndex==count-1){
					AOS.err("当前最后一条!");
					return;
				}
				var s=Ext.getCmp("_g_module").getStore().getAt(rowIndex+1);
				//原先行取消选中
				Ext.getCmp("_g_module").getSelectionModel().deselect(rowIndex);
				//此时让光标选中下一行
				Ext.getCmp("_g_module").getSelectionModel().select(rowIndex+1, true);
				//组件被显示后触发。
				Ext.getCmp("_f_module_u").form.setValues(s.data);
			}




			//上一页
			function _f_previous_data2(){
				var count=Ext.getCmp("_g_catalog").getStore().getCount();
				var me=Ext.getCmp("_g_catalog").getSelectionModel().getSelection();
				//var record = AOS.selectone(_g_catalog);
				//得到执行行的坐标
				var rowIndex = Ext.getCmp("_g_catalog").getStore().indexOf(me[0]);
				if(rowIndex==0){
					AOS.err("当前第一条!");
					return;
				}
				var s=Ext.getCmp("_g_catalog").getStore().getAt(rowIndex-1);
				//原先行取消选中
				Ext.getCmp("_g_catalog").getSelectionModel().deselect(rowIndex);
				//此时让光标选中上一行
				Ext.getCmp("_g_catalog").getSelectionModel().select(rowIndex-1, true);
				//组件被显示后触发。
				Ext.getCmp("_f_catalog_u").form.setValues(s.data);
			}
			//下一页
			function _f_next_data2(){
				var count=Ext.getCmp("_g_catalog").getStore().getCount();
				var me=Ext.getCmp("_g_catalog").getSelectionModel().getSelection();
				//var record = AOS.selectone(_g_catalog);
				//得到执行行的坐标
				var rowIndex = Ext.getCmp("_g_catalog").getStore().indexOf(me[0]);
				if(rowIndex==count-1){
					AOS.err("当前最后一条!");
					return;
				}
				var s=Ext.getCmp("_g_catalog").getStore().getAt(rowIndex+1);
				//原先行取消选中
				Ext.getCmp("_g_catalog").getSelectionModel().deselect(rowIndex);
				//此时让光标选中下一行
				Ext.getCmp("_g_catalog").getSelectionModel().select(rowIndex+1, true);
				//组件被显示后触发。
				Ext.getCmp("_f_catalog_u").form.setValues(s.data);
			}
		</script>
	</aos:onready>
</aos:html>
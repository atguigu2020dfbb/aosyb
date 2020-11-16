<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="/WEB-INF/tld/aos.tld" prefix="aos"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
	String path = request.getContextPath();
%>
<aos:html>
<aos:head title="欢迎">
	<aos:base href="system/portal"/>
	<aos:base href="/" />
	<aos:include lib="ext,buttons" />
	<aos:include css="${cxt}/static/css/modules/index.css" />
	<%@ include file="../indexStyle.jsp"%>
	<style>
	body {
	 	background-color: #FFFFFF !important;
		background-attachment:fixed !important;
		background-position:bottom center !important;
		background-repeat: repeat-x !important;
	}
		#_div_archive {
			text-indent: 2em;
			margin: 5px;
		}

	#_div_image {
		overflow: hidden;
		margin-top: 20px;
		height: 300px;
	}

	.itm {
		width: 80px;
		height: 80px;
		float: left;
		text-align: center;
	}

	.itm .imgs a img {
		width: 30px;
		height: 30px;
	}

	#_div_image {
		overflow: hidden;
		margin-top: 20px;
		height: 300px
	}

	.itm {
		width: 120px;
		height: 80px;
		float: left;
		text-align: center;
	}

	.itm .imgs a img {
		width: 50px;
		height: 50px;
	}
	</style>
</aos:head>
<aos:body >
	<div id="gg" >
		<ul>
			<c:forEach var="declared" items="${declareds}">
				<li>
					<a style="font-size: 10px;color: #1BA3F9;cursor:pointer;"  id="${declared.id_}" href="javascript:void(0)"
					   onclick="get_description_declared('${declared.id_}','${declared.name_}','${declared.desc_}')">${declared.name_}</a>
				</li>
			</c:forEach>
		</ul>
	</div>
	<div id="_div_image" >
		<c:forEach var="card" items="${cardDtostop}">
		<div class="itm" style="cursor: hand;">
			<div class="imgs">
				<a id="_id_nav_${card.id_}" onclick="_fn_nav_btn_click('${card.id_}');"> <img style="cursor: pointer;" src="${cxt}/static/icon/big64/${card.icon_name_}" >
				</a>
			</div>
			<p>
				<a  onclick="_fn_nav_btn_click('${card.id_}');"><font color="red">${card.name_}</font></a>
			</p>
		</div>
		</c:forEach>
	</div>
</aos:body>
	<script type="text/javascript">
		var _g_visited_domid = '_id_nav_0b218509ae63491896289ce7c4173a41';
		//横向导航和左侧导航的互动
		function _fn_nav_btn_click(id_) {
			//此时默认点击第一个了。
			//单击哪个就加载哪个
			//重定位当前卡片位
			var _sys_nav = parent.Ext.getCmp('_sys_nav');
			if(!_sys_nav.isVisible()){
				parent.Ext.getCmp('_west').setActiveTab(_sys_nav);
			}
			var domid = '_id_nav_' + id_;
			var _dom_obj = parent.Ext.getDom(domid);
			if(_g_visited_domid==='_id_nav_0b218509ae63491896289ce7c4173a41'){
				var _dom_visited = parent.Ext.getDom(_g_visited_domid);
				_dom_visited.className = '${navDto.nav_bar_style}';
				//停止ICON转动
				if(parent.Ext.get(_dom_visited).down('i')) parent.Ext.get(_dom_visited).down('i').removeCls('fa-spin');
			}
			if (!AOS.empty(_g_visited_domid)) {
				var _dom_visited = parent.Ext.getDom(_g_visited_domid);
				_dom_visited.className = '${navDto.nav_bar_style}';
				//停止ICON转动
				if(parent.Ext.get(_dom_visited).down('i')) {
					parent.Ext.get(_dom_visited).down('i').removeCls('fa-spin');
				}
			}
			//ICON转动
			if(parent.Ext.get(_dom_obj).down('i')) {
				parent.Ext.get(_dom_obj).down('i').addCls('fa-spin');
			}
			_dom_obj.className = '${navDto.nav_bar_style_visited}';
			_g_visited_domid = domid;
			_fn_nav_left_mode1(id_);
			//此时刷新加载
			//走后台得到当前选中的节点id值
			<c:forEach var="card" items="${cardDtostop}">
			var id="${card.id_}";
			var is_auto_expand_="${card.is_auto_expand_}";
			if(id==id_){
				parent.Ext.getCmp("_id_card_${card.id_}").show();
			}else{
				parent.Ext.getCmp("_id_card_${card.id_}").hide();

			}
			</c:forEach>

		}
		//当导航模式为1时，水平和左侧导航的互动模式
		function _fn_nav_left_mode1(id_) {
			var cmpid = '_id_card_' + id_;
			var _cmp_card = parent.Ext.getCmp(cmpid);
			if (_cmp_card.getCollapsed()) {
                _cmp_card.expand(true)
            }
		}

		//打开菜单功能页面
		function fnaddtab(module_id_, menuname, url, root_id_,tablename,cascode_id_) {
			if (Ext.isEmpty(url)) {
				return;
			}
			var id = "_id_tab_" + module_id_;
			url = url.indexOf('http') === 0 ? url : '${cxt}/' + url;
			var index = url.indexOf('?');
			//一级菜单的主页面所属的页面元素其page_id_同module_id_。
			url = url + (index === -1 ? '?' : '&') + 'aos_module_id_=' + module_id_
					+ '&aos_page_id_=' + module_id_+'&tablename='+tablename+'&cascode_id_='+cascode_id_;
			var _tabs = parent.Ext.getCmp('_tabs');
			var tab = _tabs.getComponent(id);
			var tempflag = 0;
			if (!tab) {
				var iframe = Ext.create('AOS.ux.IFrame', {
					id : id + '.iframe',
					mask : true,
					layout : 'fit',
					//这个参数仅起到将iframe组件自带的mask调节到相对居中位置的作用
					height : document.body.clientHeight - 200,
					loadMask : '${page_load_msg_}'
				});
				tab = _tabs.add({
					id : id,
					module_id_: module_id_, //供Tab与导航树逆向联动使用。
					root_id_: root_id_, //菜单节点所属的那个卡片标识，也是当前菜单树的根节点。供Tab与导航树逆向联动使用。
					title : '<span class="app-container-title-normal">' + menuname + '</span>',
					closable : true,
					layout : 'fit',
					items : [ iframe ]
				});
				tab.on('activate', function() {
					//防止已打开的功能页面切回时再次加载
					if (tempflag === 0) {
						iframe.load(url);
						tempflag = 1;
					}
					//切换的时候和导航树保持同步
					fn_sync_tab_tree(tab);
				});
			}
			//激活新增Tab
			_tabs.setActiveTab(tab);
		}
		//切换卡片的时候和导航树保持同步
		function fn_sync_tab_tree(tab) {
			if (AOS.empty(tab.root_id_) || AOS.empty(tab.module_id_)) {
				return;
			}
			var _sys_nav_tab = parent.Ext.getCmp('_sys_nav');
			if (!_sys_nav_tab.isVisible()) {
				return;
			}
			var expanded_tree = parent.Ext.getCmp('_id_card_' + tab.root_id_);
			var expanded_tree_store = expanded_tree.getStore();
			var cur_node = expanded_tree_store.getById(tab.module_id_);
			if (AOS.empty(cur_node)) return;
			//如果节点所属卡片不可见，则使之可见
			_fn_nav_left_mode1(tab.root_id_);
			//如果节点不可见，则使之可见
			if (!cur_node.isVisible()) expanded_tree.expandPath(cur_node.getPath());
			var sm = expanded_tree.getSelectionModel();
			sm.select(cur_node);
		}
		//数据列表
		function get_description_declared(id_,name_,desc_){
			Ext.getCmp("_w_declared_description").show();
			Ext.getCmp("desc_desc_").setValue(desc_);
			Ext.getCmp("name_name_").setValue(name_);
		}

	</script>
<aos:onready ux="statusBar">
	<aos:viewport layout="column"  >
		<aos:panel id="center" layout="column" region="west" contentEl="_div_image">
		</aos:panel>
		<aos:panel id="west" layout="column" region="west"    title="系统公告" contentEl="gg" >
		</aos:panel>
		<!-- center --!>
		<aos:panel  id="gonggao" region="east"  title="待办事项"    >
			<aos:gridpanel id="_g_finish" onrender="_g_finish_query" url="listFinish.jhtml"  hidePagebar="true"  autoScroll="true">
				<aos:column header="流水号" dataIndex="id_" hidden="true"/>
				<aos:column header="任务名称" dataIndex="rwmc" width="150"/>
				<aos:column header="提交人" dataIndex="tjrcn" width="150"/>
				<aos:column header="提交时间" dataIndex="tjsj" width="150"/>
				<aos:column header="审核人" dataIndex="shrcn" width="150"/>
				<aos:column header="审核时间" dataIndex="shsj" width="150"/>
				<aos:column hidden="true" header="任务地址" dataIndex="rwurl" width="150" />
			</aos:gridpanel>
		</aos:panel>
	</aos:viewport>
	<aos:window id="_w_declared_description" title="公告详情" width="800" >
		<aos:formpanel  id="_f_declared_description" width="780">
			<aos:textfield  fieldLabel="名称"  name="name_name_" id="name_name_" width="750" />
			<aos:textareafield fieldLabel="内容"   name="desc_desc_" id="desc_desc_" width="750" height="300"/>
		</aos:formpanel>
		<aos:docked dock="bottom" ui="footer">
			<aos:dockeditem xtype="tbfill" />
			<aos:dockeditem onclick="#_w_declared_description.hide();" text="关闭"
							icon="close.png" />
		</aos:docked>
	</aos:window>
<script type="text/javascript">
	window.onload=function(){
		//去掉边框线
		Ext.getCmp("center").setBorder(false);
		Ext.getCmp("center").setWidth(window.innerWidth);
		Ext.getCmp("center").setHeight(window.innerHeight*(1/5));
		document.getElementById("center").style.marginLeft="0px";
		document.getElementById("center").style.marginTop="0px";

		Ext.getCmp("west").setWidth(window.innerWidth*(2/5));
		Ext.getCmp("west").setHeight(window.innerHeight*(2/3));
		document.getElementById("west").style.marginLeft="0px";
		document.getElementById("west").style.marginTop=(window.innerHeight-window.innerHeight*(2/3)-window.innerHeight*(1/5)-10)+"px";
		document.getElementById("west").style.marginBottom="10px";

		Ext.getCmp("gonggao").setWidth(window.innerWidth*(2/5));
		Ext.getCmp("gonggao").setHeight(window.innerHeight*(2/3));
		document.getElementById("gonggao").style.marginLeft=window.innerWidth*(2/5);
		document.getElementById("gonggao").style.marginTop=(window.innerHeight-window.innerHeight*(2/3)-window.innerHeight*(1/5)-10)+"px";
	    document.getElementById("gonggao").style.marginBottom="10px";

	}
	//加载完毕
	function _fn_onload() {
	}
		//这个页面的背景图片需要延时设置，不能使用body标签的backGround属性
		Ext.util.CSS.createStyleSheet(
						'body {background-image:url("${cxt}/static/image/background/main/${curSkin}.png") !important; ',
						'_id_css_01');

		function _fn_spdata(){
			parent.fnaddtab('4d3cfce7b9b146d2bc8482ec477518aa', '归档审批', 'preprocessing/application/initPersonTaskList.jhtml');
		}
		//项目选中事件
		function _fn_onselectionchange(me, selecteds, eOpts) {
			var selectedIcon = selecteds[0];
			if (selectedIcon.data.type_ === '1' || selectedIcon.data.type_ === '2') {
				_detail.setWidth(200, true);
				_detail.setValue(selectedIcon.data.name_);
			} else {
				var text = selectedIcon.data.name_ + ' 或 '
						+ '<i class="fa {0}"></i>';
				text = AOS.merge(text, selectedIcon.data.name_);
				_detail.setWidth(380, true);
				_detail.setValue(text);
			}
		}
	//数据列表
	function get_description_declared(id_,name_,desc_){
		Ext.getCmp("_w_declared_description").show();
		Ext.getCmp("desc_desc_").setValue(desc_);
		Ext.getCmp("name_name_").setValue(name_);
	}
	function _g_finish_query(){
		var user="<%=session.getAttribute("user")%>";
		var params = {
			user:user
		};
		//这个Store的命名规则为：表格ID+"_store"。
		_g_finish_store.getProxy().extraParams = params;
		_g_finish_store.load();
	}
	</script>
</aos:onready>
</aos:html>
<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="/WEB-INF/tld/aos.tld" prefix="aos"%>
<%@ taglib uri="/WEB-INF/tld/cll.tld" prefix="c"%>

<aos:html>

<aos:head title="${app_title}">
	<aos:base href="/" />
	<aos:include lib="ext,buttons" />
	<aos:include css="${cxt}/static/css/modules/index.css" />
	<aos:include js="${cxt}/static/flexpaper/jquery.min.js" />
	<%@ include file="indexStyle.jsp"%>
	<style>
		#_div_archive {
			text-indent: 2em;
			margin: 5px;
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
<aos:body>
	<%-- 显示loading --%>
	<div id="loading-mask"></div>
	<div id="loading">
		<c:choose>
			<c:when test="${page_load_gif == 'run.gif'}">
				<img src="${cxt}/static/image/gif/pageload/${page_load_gif}">
			</c:when>
			<c:otherwise>
				<img width="100" height="100" src="${cxt}/static/image/gif/pageload/${page_load_gif}">
			</c:otherwise>
		</c:choose>
	</div>
	<%-- Center导航1111 --%>
	<div id="_div_center">
		<iframe id="_id_tab_welcome.frame" name="_id_tab_welcome.frame" ></iframe>
	<FORM id="myform" METHOD=POST ACTION="${cxt}/system/portal/init.jhtml" TARGET="_id_tab_welcome.frame">
		<INPUT TYPE="hidden" NAME="cardDtostop" value="${cardDtostop}">
	</FORM>
	<%-- Banner导航 --%>
	<div id="_id_north_el" class="x-hidden north_el">
		<table>
			<tr>
				<td width="20%"><img src="${cxt}/static/image/logo/${left_logo_}"></td>
				<td align="left" width="60%">
				<c:if test="${navDto.is_show_top_nav_ == 'true' }">
						<c:if test="${navDto.nav_button_style == 'tight' }">
							<div class="button-group">
								<c:forEach var="card" items="${cardDtostop}">
									<button id="_id_nav_${card.id_}" type="button" onclick="_fn_nav_btn_click
											('${card.id_}');"
										class="${navDto.nav_bar_style}">
										<c:if test="${card.vector_ != null}">
											<i id="_id_icon_${card.id_}" class="fa ${card.vector_}"></i>
										</c:if>
										${card.name_}
									</button>
								</c:forEach>
							</div>
						</c:if>
						<c:if test="${navDto.nav_button_style == 'standalone' }">
							<c:forEach var="card" items="${cardDtostop}">
								<a id="_id_nav_${card.id_}" onclick="_fn_nav_btn_click('${card.id_}');" class="${navDto.nav_bar_style}"> <c:if
										test="${card.vector_ != null}">
										<i id="_id_icon_${card.id_}" class="fa ${card.vector_}"></i>
									</c:if> ${card.name_}
								</a>
							</c:forEach>
						</c:if>
					</c:if>
				</td>
				<td align="right" style="padding: 5px">
					<table style="border-spacing: 3px;">
						<tr>
							<td colspan="5" class="main_text"><nobr>
									<i class="fa fa-rss"></i> ${date} ${week} <span id="rTime"></span>
								</nobr></td>
						<tr>
						<tr align="right">
							<td>
								<a id="notice" class="${navDto.right_button_style}" onmouseout="_btn_onmouseout(this)" onclick="_left_task()"
								   onmouseover="_btn_onmouseover(this);"><i id="icon" class="fa fa-bell"></i> 消息 <i id="num" style="color: red">0</i></a>
								<a
								class="${navDto.right_button_style}" onclick="_fn_logout()" onmouseout="_btn_onmouseout(this)"
								onmouseover="_btn_onmouseover(this);"><i class="fa fa-power-off"></i> 退出</a></td>
						<tr>
					</table>
				</td>
			</tr>
		</table>
	</div>
	<div id="_id_south_el" class="x-hidden">
		<div class="south_el_left main_text">
			<i class="fa fa-pagelines"></i> ${welcome}, ${username}. 所属组织:${deptname}.
		</div>
		<div class="south_el_right main_text">
			<i class="fa fa-copyright"></i> ${copyright}
		</div>
	</div>
</aos:body>

<aos:onready ux="iframe" elAuth="false">
	<aos:viewport layout="border" id="_test">
		<aos:panel id="_north" region="north" contentEl="_id_north_el" height="60" maxHeight="60" minHeight="60" border="false"
			header="false" collapsible="true" collapseMode="mini" split="true">
		</aos:panel>
<!--height="${statusbar_height}"-->
		<aos:panel id="_south" region="south" contentEl="_id_south_el"  border="false" header="false"
			bodyStyle="backgroundColor:'${south_back_color_}'">
		</aos:panel>
		<!--width="${searchfield_width}"-->
		<aos:tabpanel id="_west" region="west" activeTab="0" plain="true" tabBarHeight="30" bodyBorder="0 1 1 1" split="true" 
			maxWidth="300" border="true" minWidth="220" width="240" collapsible="true" collapseMode="mini" header="false">
			<aos:tab id="_sys_nav" title="系统导航" layout="accordion" animate="false">
				<aos:docked forceBoder="0 0 1 0">
					<aos:button text="返回首页" onclick="removeAll_show" icon="more/draw-arrow-back.png"/>
					<c:forEach var="card" items="${cardDtostop}">
						<aos:hiddenfield  id="_id_hidden_${card.id_}" name="_id_hidden_${card.id_}" value="0"/>
					</c:forEach>
					<aos:dockeditem xtype="tbfill" />
					<aos:dockeditem text="" tooltip="更多选型" icon="icon141.png">
						<aos:menu>
							<aos:menuitem text="首选项" icon="config1.png" onclick="_fn_preference" />
							<aos:menuitem xtype="menuseparator" />
							<aos:menuitem text="安全退出" icon="close2.png" onclick="_fn_logout" />
						</aos:menu>
					</aos:dockeditem>
				</aos:docked>
				<!--这个处理一下-->
				<c:forEach var="card" items="${cardDtostop}">
					<c:if test="${card.hidden == 1}">
					<aos:treepanel id="_id_card_${card.id_}" onitemclick="fn_node_click"
						icon="${card.icon_name_}" expandAll="false"  title="${card.name_}" hidden="false" rootVisible="false" rootId="${card.cascade_id_}"
						url="getModuleTree.jhtml"  nodeParam="cascade_id_">
					</aos:treepanel>
					</c:if>
					<c:if test="${card.hidden != 1}">
						<aos:treepanel id="_id_card_${card.id_}" onitemclick="fn_node_click"
									   icon="${card.icon_name_}" expandAll="false"  title="${card.name_}" hidden="true" rootVisible="false" rootId="${card.cascade_id_}"
									   url="getModuleTree.jhtml"   nodeParam="cascade_id_">
						</aos:treepanel>
					</c:if>
				</c:forEach>
			</aos:tab>
			<aos:tab title="个人任务" layout="fit">
				<aos:treepanel id="_t_quick" bodyBorder="0 0 0 0"  singleClick="false"
							   rootVisible="true" expandAll="true"  onitemclick="fn_quick_click">
					<aos:treenode text="个人任务" leaf="false" icon="user8.png">
						<aos:treenode text="待办事项" leaf="false" icon="task1.png" a="notification/initWait.jhtml">

						</aos:treenode>
						<aos:treenode text="已办事项" leaf="false" icon="task_finish.png" a="notification/initFinish.jhtml">

						</aos:treenode>
					</aos:treenode>

				</aos:treepanel>
			</aos:tab>
		</aos:tabpanel>
		<aos:tabpanel id="_tabs" region="center" activeTab="0" plain="true" tabBarHeight="30" bodyBorder="0 1 1 1" >
			<aos:plugins>
				<aos:tabCloseMenu extraItemsTail1="最大化 还原:fn_collapse_expand:shape_move_back.png"  extraItemsTail2="刷新:fn_reload:refresh2.png" />
				<aos:tabReorderer/>
			</aos:plugins>
			<aos:tab id="_id_tab_welcome" reorderable="false" title="${welcome_page_title}" contentEl="_div_center" />
		</aos:tabpanel>
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
	</aos:viewport>
	<script type="text/javascript">


		//数据列表
		function fn_find_modules(){
			AOS.tip('预留功能接口，暂未实现……');
		}
		function removeAll_show(){
			//
			var tabs=Ext.getCmp('_tabs');
			for(var f=tabs.items.length-1;f<tabs.items.length;f--){
				var tab=tabs.items.get(f);
				var tabid=tab.id;
				if(tabid!="_id_tab_welcome"){
					tabs.remove(tab);
				}
			}

			//window.history.go(-1);

		}
	    //刷新当前活动卡片
	    function fn_reload(){
	    	var cur_tab_id = _tabs.getLayout().activeItem.id;
	    	if(cur_tab_id === '_id_tab_welcome'){
	    		//Ext.get('_id_tab_welcome.frame').dom.contentWindow.location.reload();
	    	}else{
	    		Ext.getCmp(cur_tab_id + '.iframe').load();
	    	}
	    }
	    
	    //最大化 还原
	    function fn_collapse_expand(){
	    	_west.toggleCollapse();
	    	_north.toggleCollapse();
	    }
		
		//响应卡片展开事件
		function _fn_card_onexpand(me, eOpts) {

			var id_ = me.id.substr(9);
			_fn_nav_btn_click(id_);
		}
		//响应导航菜单树节点单击事件
		function fn_node_click(view, record, item, index, e) {
			//菜单节点所属的那个卡片标识，也是当前菜单树的根节点
			var root_id_ = record.getPath().split('/')[2];
			var url =  record.raw.a;
			var tablename = record.raw.c;
			if (!Ext.isEmpty(url)) {
				fnaddtab(record.raw.id, record.raw.text, url,root_id_,tablename,record.raw.b);
			}else{
				if(record.raw.leaf){
					 AOS.tip('没有配置菜单的请求地址。');
				}
			}
		}
		//响应快捷菜单单击事件
		function fn_quick_click(view, record, item, index, e) {
			var url = record.raw.a;
			fnaddtab(record.raw.id, record.raw.text, url);
		}


		//刷新快捷菜单树
		function _t_quick_load() {
			refreshnode = _t_quick.getRootNode();
			_t_quick_store.load({
				callback : function() {
					_t_quick.expandAll();
				}
			});
		}
		
		_west.setActiveTab(${nav_tab_index_});
		//数据列表
		function get_description_declared(id_,name_,desc_){
			_w_declared_description.show();
			_f_declared_description.getForm().findField('name_').setValue(name_);
			_f_declared_description.getForm().findField('desc_').setValue(desc_);
		}

		//更新消息数量
		function updateMsg(){
            AOS.ajax({
                url : 'notification/sendMsg.jhtml',
                wait : false,
                ok : function(data) {
                    var rows = data.rows;
                    var num=document.getElementById('num');
                    if(rows==0){
                        num.innerText=0;
                    }else{
                        num.innerText=rows;
                    }

                }
            });
		}

        //websocket
        //建立socket连接
        var sock;
        if ('WebSocket' in window) {
            sock = new WebSocket("ws://127.0.0.1:80/aosyb/websocketDemo/${user}");
        } else {
            alert('该浏览器不支持WebSocket!');
        }
        sock.onopen = function (e) {
            console.log(e);
        };
        sock.onmessage = function (e) {
            console.log(e);
            updateMsg();
            var loop =Ext.create(
                'widget.uxNotification',
                {
                    position : 'br',
                    title : '<span class="app-container-title-normal"><i class="fa fa-bell-o"></i> 通知</span>',
                    closable : false,
                    useXAxis : false,
                    slideInDuration : 150,
                    autoCloseDelay : 10000,
                    width : 300,
                    html : e.data
                });
            loop.show();
        };
        sock.onerror = function (e) {
            console.log(e);
            alert(e.data);
        };
        sock.onclose = function (e) {
            console.log(e);
        };

	</script>
</aos:onready>
<script type="text/javascript">

	var navDto="${navDto}";


	//加载完毕执行函数
	window.onload = function() {
		showTime();
		//AOS.task(showTime, 1000);
		//页面加载完毕后选中第一个导航按钮
		if (!AOS.empty('${first_card_id_}')) {
			var nav_tab_index_ = '${nav_tab_index_}';
			if(nav_tab_index_ === '0'){
				_fn_nav_btn_click('${first_card_id_}');
			}
		}
		document.getElementById("myform").submit();

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
		/*if (typeof(tablename) == "undefined")
		{
			tablename="jnws";
		}
		if (typeof(cascode_id_) == "undefined")
		{
			cascode_id_="jnws";
		}*/
		url = url + (index === -1 ? '?' : '&') + 'aos_module_id_=' + module_id_
				+ '&aos_page_id_=' + module_id_+'&tablename='+tablename+'&cascode_id_='+cascode_id_;
		var _tabs = Ext.getCmp('_tabs');
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
				//tab.root_id_+":"+tab.module_id_
				fn_sync_tab_tree(tab);
				if (typeof(tab.root_id_) != "undefined")
				{
					_fn_nav_btn_click(tab.root_id_);
				}
			});
		}
		//激活新增Tab
		_tabs.setActiveTab(tab);
	}
	
	//切换卡片的时候和导航树保持同步
	function fn_sync_tab_tree(tab){
		if(AOS.empty(tab.root_id_) || AOS.empty(tab.module_id_)){
			return;
		}
		var _sys_nav_tab = Ext.getCmp('_sys_nav');
		if(!_sys_nav_tab.isVisible()){
			return;
		}
		var expanded_tree = Ext.getCmp('_id_card_' + tab.root_id_);
		var expanded_tree_store = expanded_tree.getStore();
		var cur_node = expanded_tree_store.getById(tab.module_id_);
		if(AOS.empty(cur_node)) return;
		//如果节点所属卡片不可见，则使之可见
		_fn_nav_left_mode1(tab.root_id_);
		expanded_tree.expandPath(cur_node.getPath());
		var sm = expanded_tree.getSelectionModel();
		sm.select(cur_node);
	}

	//当前已按下的导航按钮
	var _g_visited_domid = '';
	//横向导航和左侧导航的互动
	function _fn_nav_btn_click(id_) {
		//此时默认点击第一个了。
		//单击哪个就加载哪个
		//重定位当前卡片位
		var _sys_nav = Ext.getCmp('_sys_nav');
		if(!_sys_nav.isVisible()){
			Ext.getCmp('_west').setActiveTab(_sys_nav);
		}
		var domid = '_id_nav_' + id_;
		var _dom_obj = Ext.getDom(domid);
		if (!AOS.empty(_g_visited_domid)) {
			var _dom_visited = Ext.getDom(_g_visited_domid);
			_dom_visited.className = '${navDto.nav_bar_style}';
			//停止ICON转动
			if(Ext.get(_dom_visited).down('i')) Ext.get(_dom_visited).down('i').removeCls('fa-spin');
		}
		//ICON转动
		if(Ext.get(_dom_obj).down('i'))
			Ext.get(_dom_obj).down('i').addCls('fa-spin');
		_dom_obj.className = '${navDto.nav_bar_style_visited}';
		_g_visited_domid = domid;
		_fn_nav_left_mode1(id_);
		//此时刷新加载
		//走后台得到当前选中的节点id值
		<c:forEach var="card" items="${cardDtostop}">
			var id="${card.id_}";
			var is_auto_expand_="${card.is_auto_expand_}";
			if(id==id_){
				//此时从后台得到当前需要显示菜单，并动态添加到列表中
				//此时走后台动态添加tree树
				Ext.getCmp("_id_card_${card.id_}").show();
			}else{
				Ext.getCmp("_id_card_${card.id_}").hide();
			}
		</c:forEach>

	}

	//当导航模式为1时，水平和左侧导航的互动模式
	function _fn_nav_left_mode1(id_) {
		var cmpid = '_id_card_' + id_;
		var _cmp_card = Ext.getCmp(cmpid);
		if (_cmp_card.getCollapsed()) {
			_cmp_card.expand(true)
		}
	}

	//移除首页正在加载的缓冲div
	Ext.EventManager.on(window, 'load', function() {
		AOS.job(function() {
			Ext.get('loading').fadeOut({
				duration : 500, //遮罩渐渐消失
				remove : true
			});
			Ext.get('loading-mask').fadeOut({
				duration : 500,
				remove : true
			});
		}, 50); //做这个延时，只是为在Dom加载很快的时候GIF动画效果更稍微显著一点

	});

	//注销
	function _fn_logout() {
		AOS.confirm('注销并安全退出系统吗？', function(btn) {
			if (btn === 'cancel') {
				AOS.tip('操作被取消。');
				return;
			}
			AOS.mask('正在注销, 请稍候...');
			AOS.ajax({
				url : 'logout.jhtml',
				wait : false,
				ok : function(data) {
					AOS.unmask();
					window.location.href = '${cxt}/login.jhtml';
				}
			});
		});
	}

	//打开首选项页面
	function _fn_preference() {
		fnaddtab('4d3cfce7b9b146d2bc8482ec477517aa', '首选项', 'system/preference/init.jhtml');
	}
	function _left_preference(){
		window.history.go(-1);
	}
    function _left_task(){

        fnaddtab('1c5d3562562', '待办事项', 'notification/initWait.jhtml');
    }

	//按钮矢量图标动画控制
	function _btn_onmouseout(me) {
		//停止ICON转动
		Ext.get(me).down('i').removeCls('fa-spin');
	}

	//按钮矢量图标动画控制
	function _btn_onmouseover(me) {
		//ICON转动
		Ext.get(me).down('i').addCls('fa-spin');
	}

	//显示系统时钟
	function showTime() {
		Ext.get('rTime').update(Ext.Date.format(new Date(), 'H:i:s'));
	}


function closetab(){
	var _tabs = Ext.getCmp('_tabs');
	return _tabs;
}
//数据列表
function get_description_declared(id_,name_,desc_){
		Ext.getCmp("_w_declared_description").show();
		Ext.getCmp("desc_desc_").setValue(desc_);
		Ext.getCmp("name_name_").setValue(name_);
		//document.getElementById("name_name_").innerText="234";
		//_f_declared_description.getForm().findField('name_').setValue(name_);
		//_f_declared_description.getForm().findField('desc_').setValue(desc_);
}

</script>
</aos:html>
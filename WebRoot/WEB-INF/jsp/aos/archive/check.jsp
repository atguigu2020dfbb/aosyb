<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="/WEB-INF/tld/aos.tld" prefix="aos"%>
<aos:html>
<aos:head title="博美审批">
	<aos:include lib="ext,swfupload" />
	<aos:base href="archive/print" />
</aos:head>
<aos:body>
</aos:body>
<aos:onready>
	<aos:viewport layout="fit">
		<aos:gridpanel id="_g_print" url="listAccounts1.jhtml"
			onrender="_g_print_query" pageSize="20">
			<aos:docked>
				<aos:dockeditem xtype="tbtext" text="打印目录信息" />
				<aos:dockeditem text="新增" icon="add2.png" onclick="_w_print_show" />
				<aos:dockeditem xtype="tbfill" />
				<aos:triggerfield emptyText="内容" id="nrzy"
					onenterkey="_g_print_query" trigger1Cls="x-form-search-trigger"
					onTrigger1Click="_g_print_query" width="200" />
			</aos:docked>
			<aos:column type="rowno" />
			<aos:column header="流水号" dataIndex="id_" hidden="true" />
			<aos:column header="内容摘要" dataIndex="nrzy" width="160" minWidth="80"
				maxWidth="160" align="center" />
			<aos:column header="纸张大小" rendererFn="fn_zzdx_render"
				dataIndex="zzdx" width="40" />
			<aos:column header="单双面" rendererFn="fn_dsm_render" dataIndex="dsm"
				width="40" />
			<aos:column header="页数" dataIndex="ys" width="40" />
			<aos:column header="份数" dataIndex="fs" width="40" />
			<aos:column header="颜色" rendererFn="fn_yans_render" dataIndex="yans"
				width="40" />
			<aos:column header="方向" rendererFn="fn_fx_render" dataIndex="fx"
				width="40" />
			<aos:column header="备注" dataIndex="bz" width="40" />
			<aos:column header="电子文件" rendererFn="fn_button_render" width="40" />
			<aos:column header="审核" dataIndex="tg" rendererFn="fn_button_render2" width="40" />
		</aos:gridpanel>
	</aos:viewport>
	<script type="text/javascript">

		//加载表格数据
		function _g_print_query() {
			var params = {
				name_ : nrzy.getValue()
			};
			//这个Store的命名规则为：表格ID+"_store"。
			_g_print_store.getProxy().extraParams = params;
			_g_print_store.load();
		}


		//获取表格当前行数的API
		function getCount() {
			var count = _g_print_store.getCount();
			console.log(count);
		}

		_g_print.on("cellclick", function(pGrid, rowIndex, columnIndex, e) {
			var record = AOS.selectone(_g_print);
		});

		//超链接列转换
		function fn_link_render(value, metaData, record, rowIndex, colIndex,
				store) {

			return '<a href="javascript:void(0);">' + record.data.card_id_
					+ '</a>';
		}
		//按钮列转换
		function fn_button_render(value, metaData, record, rowIndex, colIndex,
				store) {
			return '<input type="button" value="查看" class="cbtn" onclick="_w_print_path_show();" />';
		}
		//按钮审核列转换
		function fn_button_render2(value, metaData, record, rowIndex, colIndex,
				store) {
				if(value==="1"){
				return "已通过";
				}
			return '<input type="button" value="通过" class="cbtn" onclick="_fn_check();" />';
		}
		
		
         //新增目录保存
			function _f_print_save(){
 				AOS.ajax({
					forms : _f_print,
					url : 'savePrint.jhtml',
					ok : function(data) {
						if (data.appcode === -1) {
                            AOS.err(data.appmsg);
                        } else {
                            _w_print.hide();
                            _g_print_store.reload();
                            AOS.tip(data.appmsg);
                        }	
					}
				});
			}
		//zzdx列转换
		function fn_zzdx_render(value, metaData, record, rowIndex, colIndex,
				store) {
			if (value === '1') {
				return 'A4';
			} else if (value === '2') {
				return 'A3';
			} 
		}
		//dsm列转换
		function fn_dsm_render(value, metaData, record, rowIndex, colIndex,
				store) {
			if (value === '1') {
				return '单面';
			} else if (value === '2') {
				return '双面';
			} 
		}
		//yans列转换
		function fn_yans_render(value, metaData, record, rowIndex, colIndex,
				store) {
			if (value === '1') {
				return '黑白';
			} else if (value === '2') {
				return '彩色';
			} 
		}
		//fx列转换
		function fn_fx_render(value, metaData, record, rowIndex, colIndex,
				store) {
			if (value === '1') {
				return '横向';
			} else if (value === '2') {
				return '纵向';
			} 
		}
		//删除信息
            function _g_print_del() {
                var selection = AOS.selection(_g_print, 'id_');
                if (AOS.empty(selection)) {
                    AOS.tip('删除前请先选中数据。');
                    return;
                }
                var msg = AOS.merge('确认要删除选中的[{0}]个用户数据吗？', AOS.rows(_g_print));
                AOS.confirm(msg, function (btn) {
                    if (btn === 'cancel') {
                        AOS.tip('删除操作被取消。');
                        return;
                    }
                    AOS.ajax({
                        url: 'deletePrint.jhtml',
                        params: {
                            aos_rows_: selection
                        },
                        ok: function (data) {
                            AOS.tip(data.appmsg);
                            _g_print_store.reload();
                        }
                    });
                });
            }
            //监听弹出修改目录窗口事件
            function _w_user_u_onshow() {
                var record = AOS.selectone(_g_print);
                AOS.ajax({
                    params: {id_: record.data.id_},
                    url: 'getPrint.jhtml',
                    ok: function (data) {
                        _f_print_u.form.setValues(data);
                    }
                });
            }
            //弹出修改用户窗口
            function _w_print_u_show() {
                AOS.reset(_f_print_u);
    			if(AOS.selectone(_g_print)){
    				_w_print_u.show();
    	     	}
            }

            //修改目录保存
            function _f_print_u_save() {
                AOS.ajax({
                    forms: _f_print_u,
                    url: 'updatePrint.jhtml',
                    ok: function (data) {
                        if (data.appcode === -1) {
                            AOS.err(data.appmsg);
                            return;
                        }
                        _w_print_u.hide();
                        _g_print_store.reload();
                        AOS.tip(data.appmsg);
                    }
                });
            }
            
	</script>
</aos:onready>
<script type="text/javascript">
	function _w_print_path_show() {
	var record = AOS.selectone(Ext.getCmp('_g_print'));
		var uploadPanel= new Ext.ux.uploadPanel.UploadPanel({
		 addFileBtnText : '选择文件...',
		 uploadBtnText : '上传',
		 downloadBtnText : '下载',
		 cancelBtnText : '取消上传',
         use_query_string : true, 
         listeners:{  
		//双击  
		itemdblclick : function(grid,row){ 
		//$("base").attr("href","pydnaso/bb/");
		//alert(row.data.id);
		window.open("initPath.jhtml?id="+row.data.id);
       }
       },
         post_params:{tid:record.data.id_},
		 file_size_limit : 10000,//MB
		 flash_url : "${cxt}/static/swfupload/swfupload.swf",
		 flash9_url : "${cxt}/static/swfupload/swfupload_f9.swf",
		 upload_url : "${cxt}/archive/print/archiveUpload.jhtml"
		 });
	
		var w_print_path = new Ext.Window({
			title : '电子文件',
			width : 700,
			modal:true,
			closeAction : 'hide',
			items:[uploadPanel]
		});
		Ext.getCmp("uploadpanel").store.removeAll();
		//var aa=Ext.getCmp("uploadpanel").flash9_url;
		//alert(aa);
		w_print_path.on("show",w_print_path_onshow);
		w_print_path.show();
	}
	function w_print_path_onshow() {
	//var me = this.settings.custom_settings.scope_handler;
		var record = AOS.selectone(Ext.getCmp('_g_print'));
		var me=Ext.getCmp("uploadpanel");
		AOS.ajax({
		params: {tid: record.data.id_},
		url: 'getPath.jhtml',
		ok: function (data) {
		
		for(i in data){
        me.store.add({
        id:data[i].id_,
        name:data[i].name,
        fileName:data[i].filename,
        size:data[i].size,
        type:data[i].type,
        percent:100,
        status:-4,
        
        });
		}
		}
		});
	}
//提交审核
		function _fn_check(){
		var record = AOS.selectone(Ext.getCmp('_g_print'));
		AOS.ajax({
		params: {id_: record.data.id_},
                    url: 'updateTg.jhtml',
                    ok: function (data) {
                        if (data.appcode === -1) {
                            AOS.err(data.appmsg);
                            return;
                        }
                       // _g_print_store.reload();
                       Ext.getCmp('_g_print').getStore().reload();
                        AOS.tip(data.appmsg);
                    }
                });
		}
</script>
</aos:html>
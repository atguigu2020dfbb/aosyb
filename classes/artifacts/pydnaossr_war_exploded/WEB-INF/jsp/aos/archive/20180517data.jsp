<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="/WEB-INF/tld/aos.tld" prefix="aos"%>
<%@ taglib uri="/WEB-INF/tld/cll.tld" prefix="c"%>
<aos:html>
<aos:head title="数据信息">
	<aos:include lib="ext,swfupload" />
	<aos:base href="archive/data" />
</aos:head>
<aos:body>

</aos:body>
<aos:onready>
	<aos:viewport layout="fit">
		<aos:gridpanel id="_g_data" url="listAccounts.jhtml"
			onrender="_g_data_query" pageSize="20" onitemdblclick="fn_g_data">
			<aos:docked>
				<aos:dockeditem xtype="tbtext" text="目录信息" />
				<aos:dockeditem text="查询" icon="query.png" onclick="_w_query_show" />
				<aos:dockeditem text="新增" id="add" icon="add2.png"
					onclick="_w_data_show" />
				<aos:dockeditem text="删除" icon="del2.png" onclick="_g_data_del" />
				<aos:dockeditem text="电子文件" icon="picture.png"
					onclick="_w_picture_show" />

				<aos:dockeditem text="设置录入" icon="layout.png"
					onclick="_w_input_show" />
				<aos:button text="导出" icon="icon154.png" scale="small"
					margin="0 0 0 0">
					<aos:menu plain="false">
						<aos:menuitem text="导出XLS报表" icon="icon70.png" onclick="fn_xls" />
						<aos:menuitem text="导出XLSX报表" icon="icon7.png" onclick="fn_xlsx" />
					</aos:menu>

				</aos:button>
				<aos:dockeditem text="导入" icon="folder8.png"
					onclick="_w_import_show" />
				<aos:dockeditem xtype="tbfill" />
				<aos:triggerfield id="tablename" width="200" value="${tablename }"
					style="display:'none'" />
				<aos:triggerfield id="cascode_id_" width="200"
					value="${cascode_id_ }" style="display:'none'" />
				<aos:triggerfield emptyText="内容" id="nrzy"
					onenterkey="_g_data_query" trigger1Cls="x-form-search-trigger"
					onTrigger1Click="_g_data_query" width="200" />

			</aos:docked>

			<aos:column dataIndex="id_" header="流水号" hidden="true" />
			<aos:column dataIndex="_path" header="电子文件"
				rendererFn="fn_path_render" />
			<c:forEach var="field" items="${fieldDtos}">
				<aos:column dataIndex="${field.fieldenname }"
					header="${field.fieldcnname }" rendererField="field_type_" />
			</c:forEach>
			<aos:column header="" flex="1" />
		</aos:gridpanel>
	</aos:viewport>

	<aos:window id="_w_data_i" title="卡片" width="1000" height="600"
		autoScroll="true" onshow="_w_data_i_onshow" y="100" onrender="_w_data_i_render" >
		<aos:formpanel id="_f_data_i" width="980" layout="absolute">
			<aos:hiddenfield name="_classtree" value="${cascode_id_}" />
		</aos:formpanel>
		<aos:docked dock="bottom" ui="footer">
			<aos:dockeditem xtype="tbfill" />
			<aos:dockeditem onclick="_f_data_i_save" text="加存" icon="icon80.png" />
			<aos:dockeditem onclick="_f_data_edit" text="保存" icon="icon82.png" />
			<aos:dockeditem onclick="#_w_data_i.hide();" text="关闭"
				icon="close.png" />
		</aos:docked>
	</aos:window>


	


	<aos:window id="_w_query_q" title="查询" width="720" autoScroll="true"
		layout="fit">
		<aos:tabpanel id="_tabpanel" region="center" activeTab="0"
			bodyBorder="0 0 0 0" tabBarHeight="30">
			<aos:tab title="列表式搜索" id="_tab_org">


				<aos:formpanel id="_f_query" layout="column" columnWidth="1">
					<aos:hiddenfield name="tablename" value="${tablename }" />
					<aos:hiddenfield name="columnnum" id="columnnum" value="7" />
					<c:forEach var="fieldss" items="${fieldDtos}" end="7"
						varStatus="listSearch">

						<aos:radiobox columnWidth="0.1" name="and${listSearch.count }"
							boxLabel="并且" checked="true" />
						<aos:radiobox columnWidth="0.1" name="or${listSearch.count }"
							boxLabel="或者" />
						<aos:combobox name="filedname${listSearch.count }"
							emptyText="${fieldss.fieldcnname }" labelWidth="20"
							columnWidth="0.2" fields="['fieldenname','fieldcnname']"
							regexText="${fieldss.fieldenname }" displayField="fieldcnname"
							valueField="fieldenname"
							url="queryFields.jhtml?tablename=${tablename }">
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

			<aos:tab title="记录更新" id="_tab_update">
				<aos:formpanel id="_f_update" layout="column" columnWidth="1">
					<aos:displayfield value="请输入您更新的字段。。。" columnWidth="0.99" />
					<aos:combobox name="filedname" labelWidth="20" columnWidth="0.49"
						fields="['fieldenname','fieldcnname']" displayField="fieldcnname"
						valueField="fieldenname"
						url="queryFields.jhtml?tablename=${tablename }">
					</aos:combobox>
					<aos:displayfield value="更新为" columnWidth="0.99" />
					<aos:textfield name="update_content" allowBlank="true"
						columnWidth="0.49" />
					<aos:docked dock="bottom" ui="footer" center="true">
						<aos:dockeditem xtype="tbfill" />
						<aos:dockeditem onclick="_f_data_update" text="确定" icon="ok.png" />
						<aos:dockeditem onclick="#_w_query_q.hide();" text="关闭"
							icon="close.png" />
					</aos:docked>
				</aos:formpanel>
			</aos:tab>


			<aos:tab title="记录替换" id="_tab_replace">
				<aos:formpanel id="_f_replace" layout="column" columnWidth="1">
					<aos:displayfield value="请输入您替换的条件。。。" columnWidth="0.99" />
					<aos:combobox name="filedname" labelWidth="20" columnWidth="0.49"
						fields="['fieldenname','fieldcnname']" displayField="fieldcnname"
						valueField="fieldenname"
						url="queryFields.jhtml?tablename=${tablename }">
					</aos:combobox>
					<aos:displayfield value="将" columnWidth="0.99" />
					<aos:textfield name="replace_source" allowBlank="true"
						columnWidth="0.49" />
					<aos:displayfield value="替换为" columnWidth="0.99" />
					<aos:textfield name="replace_new" allowBlank="true"
						columnWidth="0.49" />
					<aos:docked dock="bottom" ui="footer" center="true">
						<aos:dockeditem xtype="tbfill" />
						<aos:dockeditem onclick="_f_data_replace" text="确定" icon="ok.png" />
						<aos:dockeditem onclick="#_w_query_q.hide();" text="关闭"
							icon="close.png" />
					</aos:docked>
				</aos:formpanel>
			</aos:tab>

			<aos:tab title="字段前后辍" id="_tab_suffix">
				<aos:formpanel id="_f_suffix" layout="column" columnWidth="1">
					<aos:displayfield value="前辍" columnWidth="0.99" />
					<aos:textfield name="suffix_front" allowBlank="true"
						columnWidth="0.49" />
					<aos:displayfield value="选择字段" columnWidth="0.99" />
					<aos:combobox name="filedname" labelWidth="20" columnWidth="0.49"
						fields="['fieldenname','fieldcnname']" displayField="fieldcnname"
						valueField="fieldenname"
						url="queryFields.jhtml?tablename=${tablename }">
					</aos:combobox>
					<aos:displayfield value="后辍" columnWidth="0.99" />
					<aos:textfield name="suffix_after" allowBlank="true"
						columnWidth="0.49" />
					<aos:docked dock="bottom" ui="footer" center="true">
						<aos:dockeditem xtype="tbfill" />
						<aos:dockeditem onclick="_f_data_suffix" text="确定" icon="ok.png" />
						<aos:dockeditem onclick="#_w_query_q.hide();" text="关闭"
							icon="close.png" />
					</aos:docked>
				</aos:formpanel>
			</aos:tab>

			<aos:tab title="补位" id="_tab_repair">
				<aos:formpanel id="_f_repair" layout="column" columnWidth="1">
					<aos:displayfield value="补位字段" columnWidth="0.99" />
					<aos:combobox name="filedname" labelWidth="20" columnWidth="0.49"
						fields="['fieldenname','fieldcnname']" displayField="fieldcnname"
						valueField="fieldenname"
						url="queryFields.jhtml?tablename=${tablename }">
					</aos:combobox>
					<aos:displayfield value="长度" columnWidth="0.99" />
					<aos:textfield name="repair_long" allowBlank="true"
						columnWidth="0.49" />
					<aos:hiddenfield name="repair_value" allowBlank="true"
						columnWidth="0.49" value="00000" />
					<aos:docked dock="bottom" ui="footer" center="true">
						<aos:dockeditem xtype="tbfill" />
						<aos:dockeditem onclick="_f_data_suffix" text="确定" icon="ok.png" />
						<aos:dockeditem onclick="#_w_query_q.hide();" text="关闭"
							icon="close.png" />
					</aos:docked>
				</aos:formpanel>
			</aos:tab>

		</aos:tabpanel>


	</aos:window>
	<script type="text/javascript">
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
                    for(var i in data){
                    var items;
                   if(data[i].fieldname.charAt(data[i].fieldname.length - 1)=='L'){
                   items=[{   
                xtype : 'label', 
                //value:data[i].displayname,
                text:data[i].displayname,
                width : parseInt(data[i].cwidth),
				height : parseInt(data[i].cheight),
                x:parseInt(data[i].cleft)-200,
                y:parseInt(data[i].ctop)-50,
            }]
                   }else{
                  if(data[i].yndic=='1'){
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
                   items = itemsGroup(data[i]);
                    /* items=[{   
                xtype : 'textfield', 
                name:data[i].fieldname.substring(0,data[i].fieldname.length-1),
                maxWidth : parseInt(data[i].cwidth),
				height : parseInt(data[i].cheight),
                x:parseInt(data[i].cleft)-200,
                y:parseInt(data[i].ctop)-50,
                }]*/
                  /* else{
                     items=[{   
                xtype : 'textfield', 
                name:data[i].fieldname.substring(0,data[i].fieldname.length-1),
                maxWidth : parseInt(data[i].cwidth),
				height : parseInt(data[i].cheight),
                x:parseInt(data[i].cleft)-200,
                y:parseInt(data[i].ctop)-50,
            }]*/
            }
            }
            //autoDh(data[i]);
            //alert(items);
            _panel.add(items);
            
            
                    }
                    }
                });
		}
		
		function itemsGroup(data){
		var strx=parseInt(data.cleft)-200;
		var stry = parseInt(data.ctop)-50;
		var strwidth = parseInt(data.cwidth);
		var strheight=parseInt(data.cheight);
		var fieldname = data.fieldname.substring(0,data.fieldname.length-1);
		var str ='[{'   
                +'xtype : "textfield",'
                +'id:"'+fieldname+'",'
                +'name:"'+fieldname+'",'
                +'maxWidth : '+strwidth+','
				+'height : '+strheight+','
                +'x:'+strx+','
                +'y:'+stry+','
                +'maxLength:'+data.edtmax+','
                ;
                if(data.ynnnull=='0'){
	                str=str+'allowBlank:false,';
	            }
	            //str=str+'listeners:{focus:function(){';
	            if(data.dh=='1'){
	                str=str+'listeners:{focus:function(){';
	                //Ext.getCmp(fieldname).setValue(Ext.getCmp(data.dh1).getValue()+);
	                //alert(data.dh4)
	                if(typeof(data.dh1)!='undefined'){
	                //alert(Ext.getCmp('qzh').getValue());
	                str=str+'Ext.getCmp("'+fieldname+'").setValue(Ext.getCmp("'+data.dh1+'").getValue()';
		                if(typeof(data.dh2)!='undefined'){
		                str=str+'+"-"+Ext.getCmp("'+data.dh2+'").getValue()';
			                if(typeof(data.dh3)!='undefined'){
			                str=str+'+"-"+Ext.getCmp(data.dh3).getValue()';
				                if(typeof(data.dh4)!='undefined'){
				                str=str+'+"-"+Ext.getCmp(data.dh4).getValue()';
					                if(typeof(data.dh5)!='undefined'){
					                str=str+'+"-"+Ext.getCmp(data.dh5).getValue()';
						                if(typeof(data.dh6)!='undefined'){
						                str=str+'+"-"+Ext.getCmp(data.dh6).getValue()';
							                if(typeof(data.dh7)!='undefined'){
							                str=str+'+"-"+Ext.getCmp(data.dh7).getValue()';
							                }
						                }
					                }
				                }
			                }
		                }
		                str=str+')';
	                }
	                  
	                  
	               // }
	               str=str+'},';
	               
	               //alert(data.ynpw);
	               //alert('11');
	               
                   //alert(str);         
              //  }
                
                }
                //if(data.ynpw=='1'){
	               
		         //     str=str+ 'blur:function(e){var val='+e.getValue()+';var len = '+val.length+';while(len < 4) {val= "0" + val;len++;}Ext.getCmp("name").setValue(val);'
		           //    
		               
	             //  } 
            		//str=str+'}';
                str=str+'}]';
                alert(str);
         var item = eval('(' + str + ')');
		return item;
		}
		
		function _w_data_i_render(){
		_w_data_input('_f_data_i');
		}
		function _w_data_u_onshow(){
		var record = AOS.selectone(_g_data);
                AOS.ajax({
                    params: {id_: record.data.id_,
                    tablename:tablename.getValue()
                    },
                    url: 'getData.jhtml',
                    ok: function (data) {
                        _f_data_u.form.setValues(data);
                    }
                });
		
		}
		//加载表格数据
		function _g_data_query() {
			var params = {
				tablename : tablename.getValue(),
				cascode_id_:cascode_id_.getValue()
			};
			//这个Store的命名规则为：表格ID+"_store"。
			_g_data_store.getProxy().extraParams = params;
			_g_data_store.load();
		}


		//获取表格当前行数的API
		function getCount() {
			var count = _g_print_store.getCount();
			console.log(count);
		}

		_g_data.on("cellclick", function(pGrid, rowIndex, columnIndex, e) {
			var record = AOS.selectone(_g_data);
		});

		//弹出新增用户窗口
        function _w_data_show() {
                _w_data_i.show();
            }
        function _w_data_i_onshow(){
        if(typeof(AOS.selectone(_g_data))!='undefined'){
        AOS.ajax({
                    params: {id_: AOS.selectone(_g_data).data.id_,
                    tablename:tablename.getValue()
                    },
                    url: 'getData.jhtml',
                    ok: function (data) {
                        _f_data_i.form.setValues(data);
                    }
                });
        
        }
        }
            
        function _w_query_show() {
                _w_query_q.show();
        }
            
        //设置录入窗口
        function _w_input_show() {
           window.parent.fnaddtab('','设置录入','/dbtable/input/initInput.jhtml?tablename='+tablename.getValue());
             
        }
        
         //新增目录加存
			function _f_data_save(){
 				AOS.ajax({
					forms : _f_data_u,
					url : 'saveData.jhtml',
					params:{
				tablename : tablename.getValue()
			},
					ok : function(data) {
						if (data.appcode === -1) {
                            AOS.err(data.appmsg);
                        } else {
                            _w_data_u.hide();
                            _g_data_store.reload();
                            AOS.tip(data.appmsg);
                        }	
					}
				});
			}
		//卡片新增目录加存
			function _f_data_i_save(){
 				AOS.ajax({
					forms : _f_data_i,
					url : 'saveData.jhtml',
					params:{
				tablename : tablename.getValue()
			},
					ok : function(data) {
						if (data.appcode === -1) {
                            AOS.err(data.appmsg);
                        } else {
                            //_w_data_i.hide();
                            //_w_data_input('_f_data_u');
                            _g_data_store.reload();
                            AOS.tip(data.appmsg);
                        }	
					}
				});
			}
		//修改目录保存
			function _f_data_edit(){
			var record = AOS.selectone(_g_data);
 				AOS.ajax({
					forms : _f_data_i,
					url : 'updateData.jhtml',
					params:{
				tablename : tablename.getValue(),
				id:record.data.id_
			},
					ok : function(data) {
						if (data.appcode === -1) {
                            AOS.err(data.appmsg);
                        } else {
                            //_w_data_i.hide();
                            _g_data_store.reload();
                            AOS.tip(data.appmsg);
                        }	
					}
				});
			}
		
		
		//删除信息
            function _g_data_del() {
                var selection = AOS.selection(_g_data, 'id_');
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
                            tablename: tablename.getValue()
                        },
                        ok: function (data) {
                            AOS.tip(data.appmsg);
                            _g_data_store.reload();
                        }
                    });
                });
            }
		
		//显示上传面板
		function _w_picture_show() {
		var record = AOS.selectone(_g_data);
		var uploadPanel= new Ext.ux.uploadPanel.UploadPanel({
		 addFileBtnText : '选择文件...',
		 uploadBtnText : '上传',
		 deleteBtnText : '移除',
		 removeBtnText : '移除所有',
		 cancelBtnText : '取消上传',
         use_query_string : true, 
         listeners:{  
		//双击  
		itemdblclick : function(grid,row){ 
		parent.fnaddtab(row.data.id, '电子文件',
							'archive/data/openFile.jhtml?id='+row.data.pid+'&type='+row.data.type+'&tablename='+tablename.getValue());
       }
       },
       onUpload : function(){
       var me=Ext.getCmp("uploadpanel");
		if (this.swfupload&&this.store.getCount()>0) {
            if (this.swfupload.getStats().files_queued > 0) {
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
                    tid:tid
                    }, // 提交参数,
                    url: 'deletePath.jhtml',
                    ok: function (data) {
                    var me=Ext.getCmp("uploadpanel");
                    //me.store.reload();
                    me.store.remove(me.store.getAt(rowIndex));
                    me.store.load();
                    AOS.tip(data.appmsg);
                    }
                });
	},
       onRemoveAll: function (){
        var selection = AOS.selection(_g_data, 'id_');
        AOS.ajax({
                    params: {aos_rows_: selection,
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
       
         post_params:{tid:record.data.id_,tablename:tablename.getValue()},
		 file_size_limit : 10000,//MB
		 flash_url : "${cxt}/static/swfupload/swfupload.swf",
		 flash9_url : "${cxt}/static/swfupload/swfupload_f9.swf",
		 upload_url : "${cxt}/archive/upload/archiveUpload.jhtml"
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
	function w_data_path_onshow() {
	//var me = this.settings.custom_settings.scope_handler;
	
		var record = AOS.selectone(Ext.getCmp('_g_data'));
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
	}
	
	function w_data_path_onclose(){
	_g_data_store.load();
	
	}
	
	//_path列转换
	function fn_path_render(value, metaData, record, rowIndex, colIndex,
				store) {
			//metaData.tdAttr = 'data-qtip="'+value+'"';
			if (value >= 1) {
			
				return '<img src="${cxt}/static/icon/picture.png" />';
			} else {
				return '<img src="${cxt}/static/icon/picture_empty.png" />';
			}
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
			_g_data_store.getProxy().extraParams = params;
			_g_data_store.load();
			_w_query_q.hide();
			AOS.reset(_f_query); 
			
	}
	
	function fn_g_data(){
	_w_data_i.show();
	
	}
	//记录更新
	function _f_data_update(){
	AOS.ajax({
					forms : _f_update,
					url : 'updateRecord.jhtml',
					params:{
				tablename : tablename.getValue()
			},
					ok : function(data) {
							_w_query_q.hide();
                            _g_data_store.reload();
                            AOS.tip(data.appmsg);
					}
				});
	}
	//记录替换
	function _f_data_replace(){
	AOS.ajax({
					forms : _f_replace,
					url : 'replaceRecord.jhtml',
					params:{
				tablename : tablename.getValue()
			},
					ok : function(data) {
							_w_query_q.hide();
                            _g_data_store.reload();
                            AOS.tip(data.appmsg);
					}
				});
	}
	//前后辍
	function _f_data_suffix(){
	AOS.ajax({
					forms : _f_repair,
					url : 'repairRecord.jhtml',
					params:{
				tablename : tablename.getValue()
			},
					ok : function(data) {
							_w_query_q.hide();
                            _g_data_store.reload();
                            AOS.tip(data.appmsg);
					}
				});
	}
	//生成XLS报表
		function fn_xls() {
			AOS.ajax({
				url : 'fillReport.jhtml',
				params:{
				tablename : tablename.getValue()
			},
				ok : function(data) {
					AOS.file('${cxt}/report/xls.jhtml');
				}
			});
		}

		//生成XLSX报表
		function fn_xlsx() {
			AOS.ajax({
				url : 'fillReport.jhtml',
				params:{
				tablename : tablename.getValue()
			},
				ok : function(data) {
					AOS.file('${cxt}/report/xlsx.jhtml');
				}
			});
		}
		//导入窗口
        function _w_import_show() {
        window.parent.fnaddtab('','数据导入','/archive/data/initImport.jhtml?tablename='+tablename.getValue());
             
        }
	</script>
</aos:onready>
</aos:html>
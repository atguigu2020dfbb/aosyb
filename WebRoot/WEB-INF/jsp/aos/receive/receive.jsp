<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="/WEB-INF/tld/aos.tld" prefix="aos"%>
<aos:html>
<aos:head title="档案接收">
	<aos:include lib="ext,swfupload_receive" />
	<script type="text/javascript" src="${cxt}/static/swfupload_receive/swfupload.js"></script>
	<script type="text/javascript" src="${cxt}/static/swfupload_receive/swfupload.queue.js"></script>
	<script type="text/javascript" src="${cxt}/static/swfupload_receive/fileprogress.js"></script>
	<script type="text/javascript" src="${cxt}/static/swfupload_receive/handlers.js"></script>
</aos:head>
<aos:body>
</aos:body>
<aos:onready>
	<aos:viewport layout="fit">
		<aos:panel id="mainpanel" layout="card" autoShow="true">
			<aos:formpanel id="_f_data" layout="column" border="false"
				onrender="_f_data_render">
				<aos:radioboxgroup id="sount" fieldLabel="文件源" columnWidth="0.99"
					columns="[120,120]" margin="10 0 10 0">
					<aos:radiobox name="r1" boxLabel="本地文件" checked="true" />
					<aos:radiobox name="r1" boxLabel="服务器文件" />
				</aos:radioboxgroup>
				<aos:hiddenfield id="tablename" name="tablename" value="${inDto.tablename }"
					columnWidth="0.99" />
				<aos:combobox name="filedname" fieldLabel="挂接方式" columnWidth="0.7"
					fields="['fieldenname','fieldcnname']" displayField="fieldcnname"
					valueField="fieldenname"
					url="queryFields.jhtml?tablename=${inDto.tablename }">
				</aos:combobox>
				<aos:radioboxgroup id="wjlx" fieldLabel="文件类型" columnWidth="0.99"
					columns="[100,100,100,100,100,100]" margin="10 0 10 0">
					<aos:radiobox name="r2" boxLabel="PDF" checked="true" />
					<aos:radiobox name="r2" boxLabel="JPG" />
					<aos:radiobox name="r2" boxLabel="JPG+PDF" />
					<aos:radiobox name="r2" boxLabel="AVI" />
					<aos:radiobox name="r2" boxLabel="MP3" />
					<aos:radiobox name="r2" boxLabel="MP4" />
				</aos:radioboxgroup>
			</aos:formpanel>
			<!-- 默认收缩 -->
			<aos:panel id="_f_directory" layout="border">
				<aos:treepanel id="_t_dirctory" singleClick="false" width="320"
					bodyBorder="0 1 0 0" rootVisible="true" nodeParam="parent_id_"
					rootIcon="home.png" rootExpanded="false" rootAttribute="aa:'a1'"
					region="west" onitemclick="fn_itemclick">
					<aos:treenode text="第1层目录设置" leaf="false" expanded="true" a="" b=""
						c="">
					</aos:treenode>
					<aos:docked forceBoder="0 1 1 0">
						<aos:dockeditem xtype="tbtext" text="目录层次信息" />
						<aos:dockeditem xtype="tbfill" />
						<aos:button text="添加" margin="0 0 0 10" icon="folder21.png"
							onclick="_fn_addnode" />
						<aos:button text="删除" margin="0 0 0 10" icon="folder18.png"
							onclick="_fn_removenode" />
					</aos:docked>
				</aos:treepanel>
				
				<aos:panel region="center" layout="hbox">
					<aos:docked forceBoder="0 1 1 0">
						<aos:dockeditem xtype="tbtext" text="目录名称配置" />
						<aos:dockeditem xtype="tbfill" />
					</aos:docked>
					<aos:combobox id="wjm" emptyText="请选择..." width="140" margin="10"
						onchang="fn_wjm">
						<aos:option value="cl" display="常量" />
						<aos:option value="sjbzd" display="数据表字段" />
					</aos:combobox>
					<aos:textfield id="clz" fieldLabel="常量值" labelWidth="50"
						width="140" margin="10" readOnly="true" onblur="fn_clz"
						onfocus="fn_clz" />

					<aos:combobox id="zdm" name="zdm" fieldLabel="字段名" margin="10"
						labelWidth="50" readOnly="true" onchang="fn_zdm"
						fields="['fieldenname','fieldcnname']" displayField="fieldcnname"
						valueField="fieldenname"
						url="queryFields.jhtml?tablename=${inDto.tablename }">
					</aos:combobox>
					<aos:hiddenfield id="directoryId" fieldLabel="ID" labelWidth="40"
						margin="10" />
				</aos:panel>
			</aos:panel>


			<aos:formpanel id="_f_pretest" layout="column">
				<aos:textfield id="biz_code_" name="biz_code_" fieldLabel="上传文件"
					columnWidth="0.7" margin="0 0 10 0" />
				<aos:button text="选择文件" margin="0 0 10 0" id="SWFUploadButton" />

				<aos:textareafield name="remark_" id="remark_" fieldLabel="操作日志" height="500"
					columnWidth="0.99"/>
				<aos:docked dock="bottom" ui="footer">
					<aos:dockeditem xtype="tbfill" />
					<aos:dockeditem xtype="button" onclick="start_preview" text="开始预检"
						margin="0 20 0 0" width="100" icon="ok.png" scale="medium" />
					<aos:dockeditem xtype="button" text="直接挂接" margin="0 20 0 0"
						width="100" icon="ok.png" scale="medium" onclick="start_mount" />
					<aos:dockeditem xtype="tbfill" />
				</aos:docked>
			</aos:formpanel>
			<aos:docked dock="bottom" ui="footer">
				<aos:dockeditem xtype="tbfill" />
				<aos:dockeditem xtype="button" id="moveprev" text="上一步"
					onclick="fn_navigate('prev')" margin="0 20 0 0" width="100"
					icon="left.png" scale="medium" />
				<aos:dockeditem xtype="button" id="movenext" text="下一步"
					onclick="fn_navigate('next')" margin="0 20 0 0" width="100"
					icon="right.png" scale="medium" />
				<aos:dockeditem xtype="button" text="返回" onclick="fn_aa"
					margin="0 20 0 0" width="100" icon="redo.png" scale="medium" />
				<aos:dockeditem xtype="tbfill" />
			</aos:docked>
		</aos:panel>
		<!-- 记录选择的上传文件和上传类型 -->
		<aos:gridpanel id="_g_data" layout="card" hidePagebar="true"
					   region="center" onrender="_g_data_render">
			<aos:column type="rowno" />
			<aos:selmodel type="row" mode="multi" />
			<aos:column dataIndex="filename" header="电子文件名" />
			<aos:column dataIndex="size" header="大小" />
			<aos:column dataIndex="id" header="id" />
			<aos:column header="" flex="1" />
		</aos:gridpanel>
		
	</aos:viewport>
	<script type="text/javascript">
		var em = Ext.get('SWFUploadButton-btnWrap');
		//SWFUpload_0
		em.setStyle({
			position : 'relative',
			display : 'block'
		});
		em.createChild({
			tag : 'div',
			id : 'SWFUploadButton1',

		});
		var swfu = new SWFUpload({
			file_size_limit : "20480",//用户可以选择的文件大小，有效的单位有B、KB、MB、GB，若无单位默认为KB
			button_width : 90, //按钮宽度
			button_height : 30, //按钮高度
			upload_url : "${cxt}/receive/batch/previewfilename.jhtml",
			flash_url : "${cxt}/static/swfupload_receive/swfupload.swf",
			flash9_url : "http://www.swfupload.org/swfupload_fp9.swf",
			button_placeholder_id : "SWFUploadButton1",
            file_queue_limit : "0",
            file_upload_limit : "0",
            use_query_string:true,
            file_dialog_start_handler:fileDialogStart,
            file_queued_handler : fileQueued,
            file_queue_error_handler : fileQueueError,
            file_dialog_complete_handler : fileDialogComplete,
            upload_progress_handler : uploadProgress,
            upload_error_handler : uploadError,
            upload_success_handler : uploadSuccess,
            upload_complete_handler : uploadComplete

		//button_text: "选择文件"//按钮文字               
		//yukon：这里有个新参数，将会使用js在id为"spanSWFUploadButton"的标签容器如span，div中创建一个"选择"按钮
		});

		Ext.get(swfu.movieName).setStyle({
			position : 'absolute',
			top : 0,
			left : 10,
			opacity : 0
		});
		function file_complete_handler() {
			//this.startUpload();
			this.swfupload.startUpload();
		}
		//选择文件展开前清空
		function file_dialog_start_function() {
			Ext.getCmp("biz_code_").setValue("");
		}
		function fn_validate() {
			Ext.MessageBox.show({
				title : '请稍等...',
				msg : '玩命加载中...',
				progressText : '',
				width : 300,
				progress : true,
				closable : false,
			});

			// this hideous block creates the bogus progress
			var f = function(v) {
				return function() {
					if (v == 12) {
						Ext.MessageBox.hide();
						Ext.example.msg('Done', 'Your fake items were loaded!');
					} else {
						var i = v / 11;
						Ext.MessageBox.updateProgress(i, Math.round(100 * i)
								+ '%');
					}
				};
			};
			for ( var i = 1; i < 13; i++) {
				setTimeout(f(i), i * 500);
			}

		}

		function fn_navigate(direction) {
			if (direction == 'next') {
				/*var biz_code_=Ext.getCmp("biz_code_").getValue();
				if(biz_code_==""){
					AOS.err("挂接文件不能为空");
					return;
				}*/
				var str = Ext.getCmp("_f_data").down("[name='filedname']");
				var filedname = str.getValue();
				if (filedname == null) {
					AOS.err("挂接方式不能为空");
					return;
				}
			}
			var layout = Ext.getCmp('mainpanel').getLayout();
			//layout.setActiveItem(2);
			if (direction == 'prev') {
				layout.prev();
			}
			if (direction == 'next') {
				layout.next();
			}
			//layout[direction]();
			Ext.getCmp('moveprev').setDisabled(!layout.getPrev());
			Ext.getCmp('movenext').setDisabled(!layout.getNext());

		}

		function _f_data_render() {

			Ext
					.getCmp('_f_data')
					.setTitle(
							"<span style='color:red;font-size:15px;'>①选择文件</span>>②存储配置>③预检");
			Ext
					.getCmp('_f_directory')
					.setTitle(
							"①选择文件><span style='color:red;font-size:15px;'>②存储配置</span>>③预检");
			Ext
					.getCmp('_f_pretest')
					.setTitle(
							"①选择文件>②存储配置><span style='color:red;font-size:15px;'>③预检</span>");

			//进入页面时让上一步隐藏
			Ext.getCmp('moveprev').setDisabled(true);

			//此时刚进入页面，把文件源、挂接方式、文件类型存入到_g_file
			//Ext.getCmp('wjy').setValue(Ext.getCmp('sount').getChecked()[0].boxLabel);

		}
		function fn_aa() {
			//Ext.getCmp('mainpanel').setTitle("<font color='red'>12313</font>mmmmmmm");

			swfu.startUpload();
			//_f_directory
		}

		function _g_data_render() {

			_g_data.hide();
		}
		function _g_file_render() {

			_g_file.hide();
		}
		function _g_save_render() {

			_g_save.hide();
		}
		function _fn_addnode() {
			var root = Ext.getCmp('_t_dirctory').getSelectionModel()
					.getLastSelected();
			var i = root.getDepth() + 2;
			//alert(i);
			root.appendChild({
				iconCls : 'album-btn',
				text : '第' + i + '层目录设置',
				expanded : true,
				//第一个下拉框
				wjm : '',
				clz : '',
				zdm : '',
				children : []
			});
		}

		function _fn_removenode() {
			var root = Ext.getCmp('_t_dirctory').getSelectionModel()
					.getLastSelected();
			root.remove();
		}
		function fn_itemclick(view, record, item, index, e) {
			//var cc=Ext.getCmp('directoryId');
			//alert(record.internalId);
			//把树节点的id值添加给那个文本框
			Ext.getCmp('directoryId').setValue(item.id);

			//var record = AOS.selectone(_t_dirctory);

			//var aa=record.raw.a1;

			//alert(aa);
			//获取之前选择的值，赋给当前的值
			Ext.getCmp('wjm').setValue(record.raw.wjm);
			if (record.raw.wjm == 'cl') {
				Ext.getCmp('clz').setValue(record.raw.clz);
				Ext.getCmp('zdm').setValue("");
				AOS.read(zdm);
			} else if (record.raw.wjm == 'sjbzd') {
				Ext.getCmp('zdm').setValue(record.raw.zdm);
				Ext.getCmp('clz').setValue("");
				Ext.getCmp('clz').readOnly = true;
				//cl.setReadOnly(true);   
			} else {
				Ext.getCmp('clz').setValue("");
				Ext.getCmp('zdm').setValue("");
				AOS.read(zdm);
				Ext.getCmp('clz').readOnly = true;

			}
		}
		//类型被选中后，
		function fn_wjm(value, metaData, record, rowIndex, colIndex, store) {
			//var windowid= Ext.getCmp('directoryId').getValue();
			//得到选中的节点树对象
			var root = Ext.getCmp('_t_dirctory').getSelectionModel()
					.getLastSelected();
			//此时让当前选中的节点赋予这个文本框的值
			//此时在判断一下如果选择了常量，让右侧的下拉框全部清除，变成可输入的文本框
			//alert(metaData);
			if (metaData == "cl") {
				AOS.edit(clz);
				AOS.read(zdm);
				Ext.getCmp('zdm').setValue("");
				root.raw.wjm = metaData;
			}
			if (metaData == "sjbzd") {
				AOS.edit(zdm);
				AOS.read(clz);
				Ext.getCmp('clz').setValue("");
				root.raw.wjm = metaData;
			}
			//如果选择了数据表字段，就让其展开下拉框
			var record = AOS.selectone(_t_dirctory);
			//alert(root.raw.wjm);
			//var aa = Ext.getCmp("rootnode2");		
			//.dbfield.wjm=value;
		}
		function fn_clz(kk, The, eOpts) {
			//得到选中的节点树对象
			var root = Ext.getCmp('_t_dirctory').getSelectionModel()
					.getLastSelected();
			root.raw.clz = kk.value;
		}
		function fn_zdm(value, metaData, record, rowIndex, colIndex, store) {
			//得到选中的节点树对象
			var root = Ext.getCmp('_t_dirctory').getSelectionModel()
					.getLastSelected();
			root.raw.zdm = metaData;
		}
		//开始预检
		function start_preview() {
			//1.预检监测选择的电子文件的类型，电子文件是否为空字节。
			/*var layout = Ext.getCmp('mainpanel').getLayout();
			swfu.startUpload();
			layout.setActiveItem(0);*/
			//swfu.startUpload();
			//2.预检选择的挂接方式的条目有多少个符合要求的
			//3.预检选择的目录层级有多少个符合要求的
			/*if (this.swfupload&&this.store.getCount()>0) {
			    if (this.swfupload.getStats().files_queued > 0) {
			        this.showBtn(this,false);	
			        this.swfupload.startUpload();
			    }
			}*/
			//文件类型
			var wjlx = Ext.getCmp('wjlx').getChecked()[0].boxLabel;
			//挂接方式
			var filedname = Ext.getCmp('_f_data').down("[name='filedname']")
					.getValue();
			//得到所有选择的电子文件的名称和大小
			var file_gridData = JSON.stringify(Ext.pluck(
					_g_data_store.data.items, 'data'));
			//挂接的表明
			var tablename=Ext.getCmp("tablename").getValue();
			AOS.ajax({
				url : 'startPreview.jhtml',
				params : {
					tablename : tablename,
					wjlx : wjlx,
					filedname : filedname,
					file_gridData : file_gridData
				},
				ok : function(data) {
					//把预检的到的信息数据传递为文本框中	
					//Ext.getCmp('remark_').innerHTML=data.appmsg;
					//Ext.getCmp("remark_").html=data.appmsg;
					var a=data.count+"";
					var b=data.gjcount+"";
					var c=data.filecount+"";
					var d=data.gjfilecount+"";
					
					var message="******预检结果******\n条目数据总数:"+ a
				+ "个,可以挂接电子条目数:" + b
				+ "个,\n电子文件总数:" + c
				+ "个,可以挂接电子文件总数:"+d+"个";
					//document.getElementById("remark_").innerHTML=message;
					Ext.getCmp("remark_").setValue(message);
				}
			});
		}
        //开始挂接
        function start_mount() {

            //文件类型
            var wjlx = Ext.getCmp('wjlx').getChecked()[0].boxLabel;
            //挂接方式
            var filedname = Ext.getCmp('_f_data').down("[name='filedname']")
                .getValue();
            //得到所有选择的电子文件的名称和大小（一组一组的减值对）
            var file_gridData = JSON.stringify(Ext.pluck(
                _g_data_store.data.items, 'data'));
            file_gridData= encodeURI(encodeURI(file_gridData));
            //挂接的表明
            var tablename = Ext.getCmp("tablename").getValue();
            //文件源：
            var wjy = Ext.getCmp('sount').getChecked()[0].boxLabel;
            wjy= encodeURI(decodeURI(wjy));
            //存储配置路径
            var root = Ext.getCmp('_t_dirctory').getRootNode();
            //开始递归
            var list_map ="";
            //把根节点的值传递过去
            var parentfilename = root.raw.wjm;
            if(parentfilename==="cl"&&root.raw.clz!=""){
                list_map+=parentfilename+":"+root.raw.clz;
            }else if(parentfilename==="sjbzd"&&root.raw.zdm!=""){
                list_map+=parentfilename+":"+root.raw.zdm;
            }else{
                AOS.err("节点文件夹不允许为空,请认真检查!");
                return;
            }
            //此时判断是不是只有一层节点，如果不是只有一层节点就继续递归
            list_map=findchildnode(root, list_map);
            //此时把这些值付给上传控件中，通过上传空间获取操作
            //swfu.addPostParam("ocr","1234");
            swfu.setPostParams({
                'wjlx':wjlx,
                'filedname':filedname,
                'file_gridData':file_gridData,
                'wjy':wjy,
                'tablename':tablename,
                'list_map':list_map
            });
            var message="\n******挂接结果******";
            //document.getElementById("remark_").innerHTML=message;
            Ext.getCmp("remark_").setValue(Ext.getCmp("remark_").getValue()+message);
            swfu.startUpload();
            //alert(swfu.files_queued);
            //var count =  _g_data_store.getCount();
            //   if (swfu.getStats().files_queued > 0) {
            //       swfu.startUpload();
            //    }
        }
        function findchildnode(node, list_map) {
            var childnodes = node.childNodes;
            if(childnodes.length==0){
                return list_map;
            }else{
                for ( var i = 0; i < childnodes.length; i++) { //从节点中取出子节点依次遍历
                    var rootnode = childnodes[i];
                    var filename = rootnode.raw.wjm;
                    if (filename === "cl" && rootnode.raw.clz != "") {
                        list_map+=","+filename+":"+rootnode.raw.clz;
                    } else if (filename === "sjbzd" && rootnode.raw.zdm != "") {
                        list_map+=","+filename+":"+rootnode.raw.zdm;
                    } else {
                        AOS.err("节点文件夹不允许为空,请认真检查!");
                        return;
                    }
                    if (rootnode.childNodes.length > 0) { //判断子节点下是否存在子节点
                        findchildnode(rootnode, list_map); //如果存在子节点 递归
                    }else{
                        return list_map;
                    }
                }
            }
        }
	</script>
</aos:onready>
</aos:html>

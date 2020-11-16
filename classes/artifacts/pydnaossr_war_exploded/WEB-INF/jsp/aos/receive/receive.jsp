<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="/WEB-INF/tld/aos.tld" prefix="aos"%>
<aos:html>
<aos:head title="档案接收">
	<aos:include lib="ext,swfupload"/>
	<aos:base href="receive/batch" />
</aos:head>
<aos:body>
</aos:body>
<aos:onready>
<aos:viewport layout="fit">
<aos:panel id="mainpanel" layout="card"    autoShow="true"   >

		<aos:formpanel id="_f_data" layout="column" border="false" onrender="_f_data_render">
		
		<aos:radioboxgroup fieldLabel="文件源" columnWidth="0.99" columns="[120,80,100,100,100]" margin="10 0 10 0">
					<aos:radiobox name="r1" boxLabel="本地文件" checked="true" />
					<aos:radiobox name="r1" boxLabel="服务器文件" />
				</aos:radioboxgroup>
		<aos:hiddenfield name="tablename" value ="${inDto.tablename }"  columnWidth="0.99"/>
		<aos:textfield name="biz_code_" fieldLabel="上传文件" columnWidth="0.7" margin="0 0 10 0"/>
		<aos:button text="选择文件" margin="0 0 10 0" id="SWFUploadButton"  />
		
		<aos:combobox name="filedname" fieldLabel="挂接方式" columnWidth="0.7"
						fields="['fieldenname','fieldcnname']" displayField="fieldcnname"
						valueField="fieldenname"
						url="queryFields.jhtml?tablename=${inDto.tablename }">
		</aos:combobox>
		
		<aos:radioboxgroup fieldLabel="文件类型" columnWidth="0.99" columns="[120,80,100,100,100]" margin="10 0 10 0">
				<aos:radiobox name="r2" boxLabel="PDF" checked="true" />
				<aos:radiobox name="r2" boxLabel="JPG" />
		</aos:radioboxgroup>
		</aos:formpanel>
		
			<aos:panel id="_f_directory" layout="border" >
				<aos:treepanel id="_t_dirctory" singleClick="false" width="320" bodyBorder="0 1 0 0" 
						  rootVisible="true" nodeParam="parent_id_"
						rootIcon="home.png" rootExpanded="false" rootAttribute="aa:'a1'" region="west" onitemclick="fn_itemclick">
						<aos:treenode  text="第1层目录设置" leaf="false" expanded="true" a="">
						</aos:treenode>
						<aos:docked forceBoder="0 1 1 0">
				<aos:dockeditem xtype="tbtext" text="目录层次信息" />
				<aos:dockeditem xtype="tbfill" />
				<aos:button text="添加" margin="0 0 0 10"  icon="folder21.png" onclick="_fn_addnode" />
				<aos:button text="删除" margin="0 0 0 10"  icon="folder18.png" onclick="_fn_removenode" />
			</aos:docked>
					</aos:treepanel>
				<aos:panel  region="center" layout="hbox">
				<aos:docked forceBoder="0 1 1 0">
				<aos:dockeditem xtype="tbtext" text="目录名称配置" />
				<aos:dockeditem xtype="tbfill" />
				</aos:docked>
				
				
					<aos:combobox id="wjm" emptyText="请选择..."  margin="10" onchang="fn_wjm">
					
					<aos:option value="cl" display="常量" />
					<aos:option value="sjbzd" display="数据表字段" />
					</aos:combobox>
					<aos:textfield id="clz" fieldLabel="常量值" labelWidth="50" margin="10" readOnly="true"/>
					<aos:textfield  id="directoryId" fieldLabel="ID" labelWidth="50"  />
					<aos:combobox name="zdm" fieldLabel="字段名" margin="10"
						fields="['fieldenname','fieldcnname']" displayField="fieldcnname"
						valueField="fieldenname"
						url="queryFields.jhtml?tablename=${inDto.tablename }">
		</aos:combobox>
					
				</aos:panel>
				</aos:panel>
			
			<aos:formpanel id="_f_pretest" layout="column" >
			<aos:textfield name="address_" fieldLabel="预检进度" maxLength="200" columnWidth="0.99" />
			<aos:textareafield name="remark_" fieldLabel="预检日志" height="500" columnWidth="0.99" />
			
			<aos:docked dock="bottom" ui="footer" >
			<aos:dockeditem xtype="tbfill" />
				<aos:dockeditem xtype="button"  text="开始预检"  margin="0 20 0 0" width="100" icon="ok.png" scale="medium"/>
				<aos:dockeditem xtype="button"  text="直接挂接"  margin="0 20 0 0" width="100" icon="ok.png" scale="medium"/>
			<aos:dockeditem xtype="tbfill" />
			</aos:docked>
			</aos:formpanel>
			
			
		<aos:docked dock="bottom" ui="footer" >
		<aos:dockeditem xtype="tbfill" />
			<aos:dockeditem xtype="button" id="moveprev" text="上一步" onclick="fn_navigate('prev')" margin="0 20 0 0" width="100" icon="left.png" scale="medium"/>
			<aos:dockeditem xtype="button" id="movenext" text="下一步" onclick="fn_navigate('next')" margin="0 20 0 0" width="100" icon="right.png" scale="medium"/>
			<aos:dockeditem xtype="button" text="返回" onclick="fn_aa" margin="0 20 0 0" width="100" icon="redo.png" scale="medium"/>
			<aos:dockeditem xtype="tbfill" />
			</aos:docked>
			
			
			
			
			
			
			
	</aos:panel>
<aos:gridpanel id="_g_data"  layout="card"  hidePagebar="true"   region="center" onrender="_g_data_render">
			<aos:column type="rowno" />
			<aos:selmodel type="row" mode="multi" />
				<aos:column dataIndex="filename" header="电子文件名" />
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
    button_width: 90, //按钮宽度
    button_height: 30, //按钮高度
    
	upload_url : "http://www.swfupload.org/upload.php",
	flash_url : "${cxt}/static/swfupload/swfupload.swf",
	//flash9_url : "http://www.swfupload.org/swfupload_fp9.swf",
	button_placeholder_id : "SWFUploadButton1",
	file_queued_handler: file_queued_function,
	//button_text: "选择文件"//按钮文字               
    //yukon：这里有个新参数，将会使用js在id为"spanSWFUploadButton"的标签容器如span，div中创建一个"选择"按钮
});

 Ext.get(swfu.movieName).setStyle({
                    position : 'absolute',
                    top : 0,
                    left : 10,
                    opacity:0
                });	


	
	function file_queued_function(file){
	
	//alert(file.name);
	_g_data_store.add({
	filename:file.name
	});
	}
	
	function fn_validate(){
		Ext.MessageBox.show({
           title: '请稍等...',
           msg: '玩命加载中...',
           progressText: '',
           width:300,
           progress:true,
           closable:false,
       });

       // this hideous block creates the bogus progress
       var f = function(v){
            return function(){
                if(v == 12){
                    Ext.MessageBox.hide();
                    Ext.example.msg('Done', 'Your fake items were loaded!');
                }else{
                    var i = v/11;
                    Ext.MessageBox.updateProgress(i, Math.round(100*i)+'%');
                }
           };
       };
       for(var i = 1; i < 13; i++){
           setTimeout(f(i), i*500);
       }
	
	
	}
	
	function fn_navigate(direction){
	var layout = Ext.getCmp('mainpanel').getLayout();
	layout[direction]();
		Ext.getCmp('moveprev').setDisabled(!layout.getPrev());
		Ext.getCmp('movenext').setDisabled(!layout.getNext());
	
	
	}
	
	
	
	function _f_data_render(){
	
	Ext.getCmp('_f_data').setTitle("<span style='color:red;font-size:15px;'>①选择文件</span>>②存储配置>③预检");
	Ext.getCmp('_f_directory').setTitle("①选择文件><span style='color:red;font-size:15px;'>②存储配置</span>>③预检");
	Ext.getCmp('_f_pretest').setTitle("①选择文件>②存储配置><span style='color:red;font-size:15px;'>③预检</span>");
	}
	function fn_aa(){
	//Ext.getCmp('mainpanel').setTitle("<font color='red'>12313</font>mmmmmmm");
	
	
	//_f_directory
	}
	
	function _g_data_render(){
	
		_g_data.hide();
	}
	function _fn_addnode(){
       var root =Ext.getCmp('_t_dirctory').getSelectionModel().getLastSelected();
		var i=root.getDepth()+2;
		//alert(i);
		root.appendChild({
            iconCls: 'album-btn',
            text: '第'+i+'层目录设置',
            expanded:true,
            //第一个下拉框
            wjm:'',
            clz:'',
            zdm:'',
            children: []
		});
	}
	
	function _fn_removenode(){
	var root =Ext.getCmp('_t_dirctory').getSelectionModel().getLastSelected();
	root.remove();
	}
	
	function fn_itemclick(view, record, item, index, e){
	
	//var cc=Ext.getCmp('directoryId');
	//alert(record.internalId);
	Ext.getCmp('directoryId').setValue(item.id);
	
	//var record = AOS.selectone(_t_dirctory);
	
	//var aa=record.raw.a1;
	
	//alert(aa);
	Ext.getCmp('wjm').setValue(record.raw.wjm);
	
		
	}
	function fn_wjm(value, metaData, record, rowIndex, colIndex,
				store){
		//var windowid= Ext.getCmp('directoryId').getValue();
		
		var root =Ext.getCmp('_t_dirctory').getSelectionModel().getLastSelected();
		root.raw.wjm=metaData;
		
		var record = AOS.selectone(_t_dirctory);
		//alert(root.raw.wjm);
		//var aa = Ext.getCmp("rootnode2");
		
		//.dbfield.wjm=value;
		
		
	}
</script>
</aos:onready>
</aos:html>

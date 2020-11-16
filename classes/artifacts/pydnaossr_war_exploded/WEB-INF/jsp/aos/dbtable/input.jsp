<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	String tablename = (String)request.getAttribute("tablename");
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">

<title>录入设置</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
<link rel="stylesheet" type="text/css"
	href="static/weblib/ext/resources/css/ext-all.css" />

<script type="text/javascript" src="static/weblib/ext/ext-all.js"></script>
<script type="text/javascript" src="static/js/aos.js"></script>
<script type="text/javascript" src="static/weblib/jquery/jquery-1.10.2.js"></script>
<style>
.classDiv2{font-size: 18px; color: #EEE000; } 

</style>
<script type="text/javascript">
$(document).keydown(function (event){
  if(event.keyCode==46){ 
  deletetextfield();
  }
 });
    Ext.onReady(function() {
    var tablename =  Ext.getDom('tablename').value;
    var buyTypeStore = new Ext.data.Store({  
        fields: ["fieldcnname", "fieldenname"],  
        proxy: {  
            type: "ajax",  
            //params:{"tablename":"3333"},
            url: "dbtable/input/loadFieldsCombo.jhtml?tablename="+tablename+"",  
            actionMethods: {  
                read: "POST"  //解决传中文参数乱码问题，默认为“GET”提交  
            },  
            reader: {  
                type: "json",  //返回数据类型为json格式  
                root: "root"  //数据  
            }  
        },  
        autoLoad: false  //自动加载数据  
    });  
    var tableFieldList = new Ext.data.Store({
    
     fields: ["fieldcnname", "fieldenname"],  
        proxy: {  
            type: "ajax",  
            //params:{"tablename":"3333"},
            url: "dbtable/input/listFieldInfos.jhtml?tablename="+tablename+"",  
            actionMethods: {  
                read: "POST"  //解决传中文参数乱码问题，默认为“GET”提交  
            },  
            reader: {  
                type: "json",  //返回数据类型为json格式  
                root: "root"  //数据  
            }  
        },  
        autoLoad: false  //自动加载数据  
    });
    
        var viewport = Ext.create('Ext.Viewport', {
            id: 'border-example',
            layout: 'border',
            items: [{
                xtype: 'panel',
                region: 'east',
                animCollapse: true,
                collapsible: true,
                split: true,
                width: 225, // give east and west regions a width
                minSize: 175,
                maxSize: 400,
                margins: '0 5 0 0',
                activeTab: 1,
                tabPosition: 'bottom',
                items: [
                    Ext.create('Ext.form.FormPanel', {
                    title: '属性',  
               // height: 300,
               hidden:false,  
               id:'toppanel',
                //width: 200,
                items: [  
                {xtype: "textfield", name: "textfieldid",id:"textfieldid", fieldLabel: "textfieldid",disabled:true,labelWidth:80,},
                 {
                 name:'fieldenname',
                 //fieldLabel: 'ieldLabel',
                 xtype: "combo",  
                 id:"dbcombo",
                 mode:'local',
                 fieldLabel:'数据源',
                 width:220,
                 labelWidth:80,
                 //valueField:'fieldenname',
                 store:buyTypeStore,
                 displayField: 'fieldcnname',
                 emptyText:'请选择一个数据源',
                 //hiddenName: 'fieldenname',
                 listeners:{
                 select:function(e){
                 var filename=e.displayTplData;
                 //alert('333');
                  var windowid = Ext.getCmp("textfieldid").getValue();
                  
                  //alert(filename[0].fieldenname);
                  Ext.getCmp(windowid).setValue(e.getValue());
                  var aa=Ext.getCmp(windowid);
                  Ext.getCmp(windowid).dbfield.fieldname=filename[0].fieldenname;
                  Ext.getCmp(windowid).dbfield.displayname=filename[0].fieldcnname;
                 }
                 },
                 listConfig: {
            getInnerTpl: function() {
                return '<div data-qtip="{fieldcnname}">{fieldcnname} ({fieldenname})</div>';
            }
        }
                 },
                 {
                 name:'yndic',
                 id:'yndic',
                 //fieldLabel: 'ieldLabel',
                 xtype: "combo",  
                 mode:'local',
                 fieldLabel:'数据字典',
                 width:220,
                 labelWidth:80,
                 store: new Ext.data.SimpleStore({
                fields:["value","text"],
                data: [                  
               ["1","启用"],
               ["0","停用"],
         ]
         }),
                 emptyText:'请选择...',
                 displayField: 'text',
                 valueField :'value',
                 listeners:{
                 select:function(e){
                 //如果启用了，让指定的文本框或下拉框可以使用
                 if(e.getValue()=='1'){
                 var windowid = Ext.getCmp("textfieldid").getValue();
                 Ext.getCmp(windowid).dbfield.yndic='1';
                 Ext.getCmp('dic').setDisabled(false);
                 }else{
                  var windowid = Ext.getCmp("textfieldid").getValue();
                 Ext.getCmp(windowid).dbfield.yndic='0';
                 Ext.getCmp(windowid).dbfield.dic='';
                 Ext.getCmp('dic').setDisabled(true);
                 Ext.getCmp('dic').setValue('');
                 //Ext.getCmp('dic').dbfield.dic='';
                 
                 }
                 
                 }
                 
                 }
                 //hiddenName: 'fieldenname',
                 },
                 {
                 name:'dic',
                 id:'dic',
                 //fieldLabel: 'ieldLabel',
                 xtype: "combo",  
                 mode:'local',
                 fieldLabel:'字典标识',
                 width:220,
                 labelWidth:80,
                 store: new Ext.data.Store({  
        fields: ["key_", "name_"],  
        proxy: {  
            type: "ajax",  
            //params:{"tablename":"3333"},
            url: "dbtable/input/loadFieldsDic.jhtml",  
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
                 displayField: 'name_',
                 valueField :'key_',
                 listeners:{
                 select:function(e){
                 var windowid = Ext.getCmp("textfieldid").getValue();
                 Ext.getCmp(windowid).dbfield.dic=e.getValue();
                 }
                 
                 }
                 //hiddenName: 'fieldenname',
                 },
                 
                 {
                 name:'dh',
                 id:'dh',
                 //fieldLabel: 'ieldLabel',
                 xtype: "combo",  
                 mode:'local',
                 fieldLabel:'档号自动生成',
                 width:220,
                 labelWidth:80,
                 store: new Ext.data.SimpleStore({
                fields:["value","text"],
                data: [                  
               ["1","启用"],
               ["0","停用"],
         ]
         }),
                 emptyText:'请选择...',
                 displayField: 'text',
                 valueField :'value',
                 listeners:{
                 select:function(e){
                 if(e.getValue()=='1'){
                 var windowid = Ext.getCmp("textfieldid").getValue();
                 Ext.getCmp(windowid).dbfield.dh='1';
                 //设置7个默认的文本框或者下拉框
                 Ext.getCmp("dh1").setDisabled(false);
                 Ext.getCmp("dh2").setDisabled(false);
                 Ext.getCmp("dh3").setDisabled(false);
                 Ext.getCmp("dh4").setDisabled(false);
                 Ext.getCmp("dh5").setDisabled(false);
                 Ext.getCmp("dh6").setDisabled(false);
                 Ext.getCmp("dh7").setDisabled(false);
                 }else{
                  var windowid = Ext.getCmp("textfieldid").getValue();
                 Ext.getCmp(windowid).dbfield.dh='0';
                 Ext.getCmp(windowid).dbfield.dh1='';
                 Ext.getCmp(windowid).dbfield.dh2='';
                 Ext.getCmp(windowid).dbfield.dh3='';
                 Ext.getCmp(windowid).dbfield.dh4='';
                 Ext.getCmp(windowid).dbfield.dh5='';
                 Ext.getCmp(windowid).dbfield.dh6='';
                 Ext.getCmp(windowid).dbfield.dh7='';
                 
                 Ext.getCmp("dh1").setDisabled(true);
                 Ext.getCmp("dh2").setDisabled(true);
                 Ext.getCmp("dh3").setDisabled(true);
                 Ext.getCmp("dh4").setDisabled(true);
                 Ext.getCmp("dh5").setDisabled(true);
                 Ext.getCmp("dh6").setDisabled(true);
                 Ext.getCmp("dh7").setDisabled(true);
                 
                 Ext.getCmp('dh1').setValue('');
                 Ext.getCmp('dh2').setValue('');
                 Ext.getCmp('dh3').setValue('');
                 Ext.getCmp('dh4').setValue('');
                 Ext.getCmp('dh5').setValue('');
                 Ext.getCmp('dh6').setValue('');
                 Ext.getCmp('dh7').setValue('');
                 }
                 
                 }
                 
                 }
                 //hiddenName: 'fieldenname',
                 },
                 
                 {
                 name:'dh1',
                 //fieldLabel: 'ieldLabel',
                 xtype: "combo",  
                 id:"dh1",
                 mode:'local',
                 fieldLabel:'第一项',
                 width:220,
                 labelWidth:80,
                 //valueField:'fieldenname',
                 store:tableFieldList,
                 displayField: 'fieldcnname',
                 emptyText:'请选择一个数据源',
                 //hiddenName: 'fieldenname',
                 listeners:{
                 select:function(e){
                 var filename=e.displayTplData;
                  var windowid = Ext.getCmp("textfieldid").getValue();
                  Ext.getCmp(windowid).dbfield.dh1=filename[0].fieldenname;
                 }
                 },
                 listConfig: {
            getInnerTpl: function() {
                return '<div data-qtip="{fieldcnname}">{fieldcnname} ({fieldenname})</div>';
            }
        }
                 },
                 {
                 name:'dh2',
                 //fieldLabel: 'ieldLabel',
                 xtype: "combo",  
                 id:"dh2",
                 mode:'local',
                 fieldLabel:'第二项',
                 width:220,
                 labelWidth:80,
                 //valueField:'fieldenname',
                 store:tableFieldList,
                 displayField: 'fieldcnname',
                 emptyText:'请选择一个数据源',
                 //hiddenName: 'fieldenname',
                 listeners:{
                 select:function(e){
                 var filename=e.displayTplData;
                  var windowid = Ext.getCmp("textfieldid").getValue();
                  Ext.getCmp(windowid).dbfield.dh2=filename[0].fieldenname;
                 }
                 },
                 listConfig: {
            getInnerTpl: function() {
                return '<div data-qtip="{fieldcnname}">{fieldcnname} ({fieldenname})</div>';
            }
        }
                 },
                 
                 {
                 name:'dh3',
                 //fieldLabel: 'ieldLabel',
                 xtype: "combo",  
                 id:"dh3",
                 mode:'local',
                 fieldLabel:'第三项',
                 width:220,
                 labelWidth:80,
                 //valueField:'fieldenname',
                 store:tableFieldList,
                 displayField: 'fieldcnname',
                 emptyText:'请选择一个数据源',
                 //hiddenName: 'fieldenname',
                 listeners:{
                 select:function(e){
                var filename=e.displayTplData;
                  var windowid = Ext.getCmp("textfieldid").getValue();
                  Ext.getCmp(windowid).dbfield.dh3=filename[0].fieldenname;
                 }
                 },
                 listConfig: {
            getInnerTpl: function() {
                return '<div data-qtip="{fieldcnname}">{fieldcnname} ({fieldenname})</div>';
            }
        }
                 },
                 
                 {
                 name:'dh4',
                 //fieldLabel: 'ieldLabel',
                 xtype: "combo",  
                 id:"dh4",
                 mode:'local',
                 fieldLabel:'第四项',
                 width:220,
                 labelWidth:80,
                 //valueField:'fieldenname',
                 store:tableFieldList,
                 displayField: 'fieldcnname',
                 emptyText:'请选择一个数据源',
                 //hiddenName: 'fieldenname',
                 listeners:{
                 select:function(e){
                var filename=e.displayTplData;
                  var windowid = Ext.getCmp("textfieldid").getValue();
                  Ext.getCmp(windowid).dbfield.dh4=filename[0].fieldenname;
                 }
                 },
                 listConfig: {
            getInnerTpl: function() {
                return '<div data-qtip="{fieldcnname}">{fieldcnname} ({fieldenname})</div>';
            }
        }
                 },
                 {
                 name:'dh5',
                 //fieldLabel: 'ieldLabel',
                 xtype: "combo",  
                 id:"dh5",
                 mode:'local',
                 fieldLabel:'第五项',
                 width:220,
                 labelWidth:80,
                 //valueField:'fieldenname',
                 store:tableFieldList,
                 displayField: 'fieldcnname',
                 emptyText:'请选择一个数据源',
                 //hiddenName: 'fieldenname',
                 listeners:{
                 select:function(e){
                 var filename=e.displayTplData;
                 var windowid = Ext.getCmp("textfieldid").getValue();
                 Ext.getCmp(windowid).dbfield.dh5=filename[0].fieldenname;
                 }
                 },
                 listConfig: {
            getInnerTpl: function() {
                return '<div data-qtip="{fieldcnname}">{fieldcnname} ({fieldenname})</div>';
            }
        }
                 },
                 {
                 name:'dh6',
                 //fieldLabel: 'ieldLabel',
                 xtype: "combo",  
                 id:"dh6",
                 mode:'local',
                 fieldLabel:'第六项',
                 width:220,
                 labelWidth:80,
                 //valueField:'fieldenname',
                 store:tableFieldList,
                 displayField: 'fieldcnname',
                 emptyText:'请选择一个数据源',
                 //hiddenName: 'fieldenname',
                 listeners:{
                 select:function(e){
                  var filename=e.displayTplData;
                  var windowid = Ext.getCmp("textfieldid").getValue();
                  Ext.getCmp(windowid).dbfield.dh6=filename[0].fieldenname;
                 }
                 },
                 listConfig: {
            getInnerTpl: function() {
                return '<div data-qtip="{fieldcnname}">{fieldcnname} ({fieldenname})</div>';
            }
        }
                 },{
                 name:'dh7',
                 //fieldLabel: 'ieldLabel',
                 xtype: "combo",  
                 id:"dh7",
                 mode:'local',
                 fieldLabel:'第七项',
                 width:220,
                 labelWidth:80,
                 //valueField:'fieldenname',
                 store:tableFieldList,
                 displayField: 'fieldcnname',
                 emptyText:'请选择一个数据源',
                 //hiddenName: 'fieldenname',
                 //事件
                 listeners:{
                 select:function(e){
                 var filename=e.displayTplData;
                  var windowid = Ext.getCmp("textfieldid").getValue();
                  Ext.getCmp(windowid).dbfield.dh7=filename[0].fieldenname;
                 }
                 },
                 listConfig: {
            getInnerTpl: function() {
                return '<div data-qtip="{fieldcnname}">{fieldcnname} ({fieldenname})</div>';
            }
        }
                 },
                 {
                 name:'ynnnull',
                 id:'ynnnull',
                 //fieldLabel: 'ieldLabel',
                 xtype: "combo",  
                 mode:'local',
                 fieldLabel:'是否空值',
                 width:220,
                 labelWidth:80,
                 store: new Ext.data.SimpleStore({
                fields:["value","text"],
                data: [                  
               ["1","是"],
               ["0","否"],
         ]
         }),
                 emptyText:'请选择...',
                 displayField: 'text',
                 valueField :'value',
                 listeners:{
                 select:function(e){
                 if(e.getValue()=='1'){
                 
                 var windowid = Ext.getCmp("textfieldid").getValue();
                 //alert(windowid);
                 Ext.getCmp(windowid).dbfield.ynnnull='1';
                // alert(Ext.getCmp(windowid).dbfield.ynnnull);
                 }else{
                 var windowid = Ext.getCmp("textfieldid").getValue();
                 Ext.getCmp(windowid).dbfield.ynnnull='0';
                 }
                 
                 }
                 
                 }
                 //hiddenName: 'fieldenname',
                 },
                 {
                 name:'ynxd',
                 id:'ynxd',
                 //fieldLabel: 'ieldLabel',
                 xtype: "combo",  
                 mode:'local',
                 fieldLabel:'是否携带',
                 width:220,
                 labelWidth:80,
                 store: new Ext.data.SimpleStore({
                fields:["value","text"],
                data: [                  
               ["1","是"],
               ["0","否"],
         ]
         }),
                 emptyText:'请选择...',
                 displayField: 'text',
                 valueField :'value',
                 listeners:{
                 select:function(e){
                 if(e.getValue()=='1'){
                 
                 var windowid = Ext.getCmp("textfieldid").getValue();
                 //alert(windowid);
                 Ext.getCmp(windowid).dbfield.ynxd='1';
                // alert(Ext.getCmp(windowid).dbfield.ynnnull);
                 }else{
                 var windowid = Ext.getCmp("textfieldid").getValue();
                 Ext.getCmp(windowid).dbfield.ynxd='0';
                 }
                 
                 }
                 
                 }
                 //hiddenName: 'fieldenname',
                 },
                 
                 {
                 name:'ynpw',
                 id:'ynpw',
                 //fieldLabel: 'ieldLabel',
                 xtype: "combo",  
                 mode:'local',
                 fieldLabel:'是否补位',
                 width:220,
                 labelWidth:80,
                 store: new Ext.data.SimpleStore({
                fields:["value","text"],
                data: [                  
               ["1","是"],
               ["0","否"],
         ]
         }),
                 emptyText:'请选择...',
                 displayField: 'text',
                 valueField :'value',
                 listeners:{
                 select:function(e){
                 if(e.getValue()=='1'){
                 
                 var windowid = Ext.getCmp("textfieldid").getValue();
                 //alert(windowid);
                 Ext.getCmp(windowid).dbfield.ynpw='1';
                 //Ext.getCmp('edtmax').setDisabled(false);
                // alert(Ext.getCmp(windowid).dbfield.ynnnull);
                 }else{
                 var windowid = Ext.getCmp("textfieldid").getValue();
                 Ext.getCmp(windowid).dbfield.ynpw='0';
                 //Ext.getCmp('edtmax').setDisabled(true);
                 Ext.getCmp('edtmax').setValue('');
                 }
                 
                 }
                 
                 }
                 //hiddenName: 'fieldenname',
                 },
                 {xtype: "textfield", name: "edtmax",id:"edtmax", fieldLabel: "最大长度",labelWidth:80,
	                 allowBlank:false,
	                 //打开键盘事件
	                 enableKeyEvents:true,
	                 
	                 listeners:{
		                 keyup:function(e){
		                 var windowid = Ext.getCmp("textfieldid").getValue();
		                 
		                 Ext.getCmp(windowid).dbfield.edtmax=e.getValue();
		                 
	                 	},
                 	}
                 },
                 //xtyp开始
                  {xtype: "textfield", name: "nextcontrol",id:"nextcontrol", fieldLabel: "录入顺序",labelWidth:80,
	                 allowBlank:false,
	                 //打开键盘事件
	                 enableKeyEvents:true,
	                 
	                 listeners:{
		                 keyup:function(e){
		                 var windowid = Ext.getCmp("textfieldid").getValue();
		                 
		                 Ext.getCmp(windowid).dbfield.nextcontrol=e.getValue();
		                 
	                 	},
                 	}
                 },
                 //结束
                 ]
                 
                    }),
                     Ext.create('Ext.form.FormPanel', {
                    title: '属性',  
                    id:'bottompanel',
               hidden:false, 
               hidden:true, 
                width: 300,
                items: [  
                {xtype: "textfield", name: "labelid",id:"labelid", fieldLabel: "labelid",disabled:true},
                {xtype: "textfield", name: "displayname",id:"displayname", fieldLabel: "名称",
                enableKeyEvents:true,
                listeners:{
                keyup:function(e){
                var labelid = Ext.getCmp("labelid").getValue();
                var textlabel=Ext.getCmp(labelid);
                textlabel.setValue(e.value);
                textlabel.dbfield.fieldname=labelid;
                textlabel.dbfield.displayname=e.value;
                }
                
                }
                
                },
                 ]
                    })
                    ]
            },
            // in this instance the TabPanel is not wrapped by another panel
            // since no title is needed, this Panel is added directly
            // as a Container
            Ext.create('Ext.panel.Panel',{
            			region: 'center',
            			layout:'absolute',
            			autoScroll:true,
                       id:"Mainpanel", 
                       dockedItems: [{
					        xtype: 'toolbar',
					        dock: 'top',
					        items: [{
					            text: '设置录入'
					            ,xtype:'tbtext'
					        },{text:'添加标签'
					        ,icon:'static/icon/add2.png'
					        ,handler: function(){addLabel();}
					        }
					        ,{text: '添加录入'
					        ,icon:'static/icon/database_add.png'
					        ,handler: function(){addTextfield();}
					        },{text: '重置'
					        ,icon:'static/icon/refresh.png'
					        ,handler: function(){reset();}
					        }
					        ,{text: '保存'
					        ,icon:'static/icon/save.png',
					        handler:function(){save();}
					        }
					        ,{text: '删除',
					        id:'deltoolbar',
					        icon:'static/icon/del2.png',
					        handler: function(){deletetextfield();}
					        }
					        ]
					    }]
					    ,listeners:{
					    afterrender:function(){
					    AOS.ajax({
							url : 'dbtable/input/loadInput.jhtml',
							params: {
		                            tablename: Ext.getDom('tablename').value
		                        },
							ok : function(data) {
								createTextfield(data);
							}
						});
					    
					    }
					    }
             })
            
            ]
        });
    });
    
    function addLabel(){
    var _panel = Ext.getCmp("Mainpanel");
    var items={   
                xtype : 'textfield', 
                value:'',
                //dbfield:dbfield,
                inputType:'L',
                inputclass:'all',
                width:'60',
                height:'25',
                dbfield:{
                fieldname:'',
                displayname:'',
                yndisplay:'1',
                },
                //offsetWidth:'20',
                fieldStyle:'background-color: #F5F5DC;',
                grow:true,
                growMin:'60',
                x:30,
                y:30,
                draggable: true,
                resizable: true,
               listeners:{
               render: function(p) {  
      // Append the Panel to the click handler's argument list.  
     p.getEl().on('click', function(p,m,e){  
                //处理点击事件代码  
                Ext.getCmp("toppanel").setVisible(false);
                Ext.getCmp("bottompanel").setVisible(true);
                Ext.getCmp("labelid").setValue(this.id);
                Ext.getCmp("displayname").setValue(m.value);
               // p.pinned=true;
                
                //Ext.getCmp("labelname").setValue(m.value);
      });  
      }
                
                }
            }; 
            _panel.add(items)
    }
    
    function addTextfield(){
    var _panel = Ext.getCmp("Mainpanel");
    var items={   
                xtype : 'textfield', 
                value:'',
                inputType:'D',
                inputclass:'all',
                width:'70',
                height:'25',
                //offsetWidth:'20',
                x:80,
                y:30,
                dbfield:{
                fieldname:'',
                displayname:'',
                yndisplay:'1',
                ynxd:'1',
                yndic:'0',
                dic:'',
                ynnnull:'1',
                edtmax:'',
                nextcontrol:'',
                ynpw:'0',
                dh:'',
                dh1:'',
                dh2:'',
                dh3:'',
                dh4:'',
                dh5:'',
                dh6:'',
                dh7:''
                },
                grow:true,
                growMin:'60',
                draggable: true,
                resizable: {
                pinned:false
                },
               listeners:{
               render: function(p) {  
      // Append the Panel to the click handler's argument list.  
     p.getEl().on('click', function(p,m,e){  
     
     			var pid = Ext.getCmp(this.id);
                //处理点击事件代码  
                //alert(Ext.getCmp(this.id).inputtype);
                Ext.getCmp("toppanel").setVisible(true);
                Ext.getCmp("bottompanel").setVisible(false);
                Ext.getCmp("textfieldid").setValue(this.id);
                Ext.getCmp("dbcombo").setValue(pid.dbfield.fieldname);
                Ext.getCmp("yndic").setValue(pid.dbfield.yndic);
                Ext.getCmp("dic").setValue(pid.dbfield.dic);
                Ext.getCmp("ynxd").setValue(pid.dbfield.ynxd);
                Ext.getCmp("ynpw").setValue(pid.dbfield.ynpw);
                Ext.getCmp("edtmax").setValue(pid.dbfield.edtmax);
                Ext.getCmp("nextcontrol").setValue(pid.dbfield.nextcontrol);
                Ext.getCmp("ynnnull").setValue(pid.dbfield.ynnnull);
                if(pid.dbfield.dic=='1'){
                Ext.getCmp("dic").setDisabled(false);
                }else{
                Ext.getCmp("dic").setDisabled(true);
                }if(pid.dbfield.fieldname==''){
                Ext.getCmp("dbcombo").setDisabled(false);
                }if(pid.dbfield.dh=='1'){
                 Ext.getCmp("dh1").setDisabled(false);
                 Ext.getCmp("dh2").setDisabled(false);
                 Ext.getCmp("dh3").setDisabled(false);
                 Ext.getCmp("dh4").setDisabled(false);
                 Ext.getCmp("dh5").setDisabled(false);
                 Ext.getCmp("dh6").setDisabled(false);
                 Ext.getCmp("dh7").setDisabled(false);
                }else{
                Ext.getCmp("dh1").setDisabled(true);
                 Ext.getCmp("dh2").setDisabled(true);
                 Ext.getCmp("dh3").setDisabled(true);
                 Ext.getCmp("dh4").setDisabled(true);
                 Ext.getCmp("dh5").setDisabled(true);
                 Ext.getCmp("dh6").setDisabled(true);
                 Ext.getCmp("dh7").setDisabled(true);
                }
                
                //Ext.getCmp("dbcombo").setValue(m.Value);
                //Ext.getCmp("labelname").setValue(this.id);
      });  
      }
                
                }
            }; 
            _panel.add(items)
    
    }
    
    function deletetextfield(){
    
    var _panel = Ext.getCmp("Mainpanel");
    var aid=Ext.ComponentQuery.query("form[hidden='false']");
    var bid=aid[0].items.items;
    var cid = bid[0].value;
    if(typeof(cid)=="undefined"){
    AOS.tip('删除前请先选中数据。');
    }
    _panel.remove(Ext.getCmp(cid));
    }
    
    
    
    //进入界面初始化
    function createTextfield(data){
    var _panel = Ext.getCmp("Mainpanel");
    for(var i in data){
    var groundcolor='';
    if(data[i].fieldname.charAt(data[i].fieldname.length - 1)=='L'){
    groundcolor='background-color:#F5F5DC;';
    }
    var items=[{   
                xtype : 'textfield', 
                value:data[i].displayname,
                //style:'border-right: 0px solid;border-top: 0px solid;border-left: 0px solid;border-bottom: #000000 1px solid;',
                dbfield:{
                fieldname:data[i].fieldname.substring(0,data[i].fieldname.length-1),
                displayname:data[i].displayname,
                yndic:data[i].yndic,
                dic:data[i].dic,
                dh:data[i].dh,
                dh1:data[i].dh1,
                dh2:data[i].dh2,
                dh3:data[i].dh3,
                dh4:data[i].dh4,
                dh5:data[i].dh5,
                dh6:data[i].dh6,
                dh7:data[i].dh7,
                ynnnull:data[i].ynnnull,
                ynxd:data[i].ynxd,
                ynpw:data[i].ynpw,
                edtmax:data[i].edtmax,
                nextcontrol:data[i].nextcontrol
                },
                inputType:data[i].fieldname.charAt(data[i].fieldname.length - 1),
                inputclass:'all',
                width : parseInt(data[i].cwidth),
				height : parseInt(data[i].cheight),
                grow:true,
                growMin:'70',
                fieldStyle:groundcolor,
                draggable: true,
                resizable: {
                pinned:false
                },
                x:parseInt(data[i].cleft),
                y:parseInt(data[i].ctop),
               listeners:{
               render: function(p) {  
      // Append the Panel to the click handler's argument list.  
      p.getEl().on({
        click:function(m,n){
        //处理点击事件代码  
                var pid = Ext.getCmp(this.id);
                var type = pid.inputType;
                if(type === 'D'){
                Ext.getCmp("toppanel").setVisible(true);
                Ext.getCmp("bottompanel").setVisible(false);
                Ext.getCmp("textfieldid").setValue(this.id);
                
                Ext.getCmp('dbcombo').setDisabled(true);
                Ext.getCmp('dbcombo').setValue(pid.dbfield.fieldname);
                //数据字典
                Ext.getCmp('yndic').setValue(pid.dbfield.yndic);
                Ext.getCmp('dic').setDisabled(!isTrue(pid.dbfield.yndic));
                Ext.getCmp('dic').setValue(pid.dbfield.dic);
                
                //档号自动合成
                Ext.getCmp('dh').setValue(pid.dbfield.dh);
                Ext.getCmp('dh1').setDisabled(!isTrue(pid.dbfield.dh));
                Ext.getCmp('dh2').setDisabled(!isTrue(pid.dbfield.dh));
                Ext.getCmp('dh3').setDisabled(!isTrue(pid.dbfield.dh));
                Ext.getCmp('dh4').setDisabled(!isTrue(pid.dbfield.dh));
                Ext.getCmp('dh5').setDisabled(!isTrue(pid.dbfield.dh));
                Ext.getCmp('dh6').setDisabled(!isTrue(pid.dbfield.dh));
                Ext.getCmp('dh7').setDisabled(!isTrue(pid.dbfield.dh));
                
                Ext.getCmp('dh1').setValue(pid.dbfield.dh1);
                Ext.getCmp('dh2').setValue(pid.dbfield.dh2);
                Ext.getCmp('dh3').setValue(pid.dbfield.dh3);
                Ext.getCmp('dh4').setValue(pid.dbfield.dh4);
                Ext.getCmp('dh5').setValue(pid.dbfield.dh5);
                Ext.getCmp('dh6').setValue(pid.dbfield.dh6);
                Ext.getCmp('dh7').setValue(pid.dbfield.dh7);
                
                Ext.getCmp('ynnnull').setValue(pid.dbfield.ynnnull);
                
                Ext.getCmp('ynxd').setValue(pid.dbfield.ynxd);
                
                Ext.getCmp('ynpw').setValue(pid.dbfield.ynpw);
                //Ext.getCmp('edtmax').setDisabled(!isTrue(pid.dbfield.ynpw));
                Ext.getCmp('edtmax').setValue(pid.dbfield.edtmax);
                Ext.getCmp('nextcontrol').setValue(pid.dbfield.nextcontrol);
                }else{
	                Ext.getCmp("toppanel").setVisible(false);
	                Ext.getCmp("bottompanel").setVisible(true);
	                Ext.getCmp("labelid").setValue(this.id);
	                Ext.getCmp("displayname").setValue(n.value);
                }
        }
});
      }
                
                }
                
            }]
            _panel.add(items);
    }
    
    }
    
    
    function save(){
var container = Ext.ComponentQuery.query('[inputclass=all]');
var tablename =  Ext.getDom('tablename').value;
var objmoList=new Array();//数组
for(var i in container){
//alert(container[i].dbfield.ynxd);
var objmo=new Object();//对象
	objmo.cwidth=container[i].getWidth();
	objmo.cheight=container[i].getHeight();
	objmo.ctop=container[i].getY()-28;
	objmo.cleft=container[i].getX()-1;
	objmo.fieldname=container[i].dbfield.fieldname+container[i].inputType;
	objmo.yndisplay=1;
	objmo.ynxd=container[i].dbfield.ynxd;
	objmo.displayname=container[i].dbfield.displayname;
	objmo.yndic=container[i].dbfield.yndic;
	objmo.dic=container[i].dbfield.dic;
	objmo.dh=container[i].dbfield.dh;
	objmo.dh1=container[i].dbfield.dh1;
	objmo.dh2=container[i].dbfield.dh2;
	objmo.dh3=container[i].dbfield.dh3;
	objmo.dh4=container[i].dbfield.dh4;
	objmo.dh5=container[i].dbfield.dh5;
	objmo.dh6=container[i].dbfield.dh6;
	objmo.dh7=container[i].dbfield.dh7;
	objmo.ynnnull=container[i].dbfield.ynnnull;
	objmo.ynpw=container[i].dbfield.ynpw;
	objmo.edtmax=container[i].dbfield.edtmax;
	objmo.nextcontrol=container[i].dbfield.nextcontrol;
	//objmo.ynnnull=1;
	objmo.tablename=tablename;
	objmoList.push(objmo)

} 
 AOS.ajax({
					url : 'dbtable/input/saveInput.jhtml',
					params: {'mydata':JSON.stringify(objmoList),
					
					tablename:tablename
					
					},
					ok:function(data) {
						AOS.tip(data.appmsg);
					}
    }); 
    }
    
    
   // var dbfield={fieldname:'',displayname:''};
    function reset(){
    var _panel = Ext.getCmp("Mainpanel");
    _panel.removeAll();
    var tablename =  Ext.getDom('tablename').value;
     AOS.ajax({
					url : 'dbtable/input/resetInput.jhtml',
					params: {
					
					tablename:tablename
					
					},
					ok:function(data) {
					resetTextfield(data);
					}
    }); 
    }
    //重置
    function resetTextfield(data){
     var _panel = Ext.getCmp("Mainpanel");
    var inputType,x=300,y=20;
    for(var i in data){
    y=y+35;
    if(i==20){
    x=600;
    y=50;
    }
    var items=[{   
                xtype : 'textfield', 
                value:data[i].fieldcnname,
                fieldStyle:'background-color: #F5F5DC;',
                dbfield:{fieldname:data[i].fieldenname,
                displayname:data[i].fieldcnname,
                yndisplay:'1'
                },
                inputType:'L',
                inputclass:'all',
                width : 70,
				height : 25,
                //offsetWidth:'20',
                grow:true,
                growMin:'60',
                draggable: true,
                resizable: true,
                x:x,
                y:y,
               listeners:{
               render: function(p) {  
      // Append the Panel to the click handler's argument list.  
     p.getEl().on('click', function(p,m,e){  
                //处理点击事件代码  
                //alert(this.id);
                Ext.getCmp("toppanel").setVisible(false);
                Ext.getCmp("bottompanel").setVisible(true);
                Ext.getCmp("labelid").setValue(this.id);
                Ext.getCmp("displayname").setValue(m.value);
               // alert(p.getWidth);
      });  
      }
                
                }
            },
             {
            xtype : 'textfield', 
                value:data[i].fieldcnname,
                dbfield:{fieldname:data[i].fieldenname.substring(0,data[i].fieldenname.length),
                displayname:data[i].fieldcnname,
                yndisplay:'1',
                ynxd:'1',
                yndic:'0',
                dic:'',
                ynnnull:'1',
                edtmax:data[i].fieldsize,
                nextcontrol:'',
                ynpw:'0',
                dh:'',
                dh1:'',
                dh2:'',
                dh3:'',
                dh4:'',
                dh5:'',
                dh6:'',
                dh7:''
                },
                inputType:'D',
                inputclass:'all',
                width : 70,
				height : 25,
                //offsetWidth:'20',
                grow:true,
                growMin:'60',
                draggable: true,
                resizable: true,
                x:x+80,
                y:y,
               listeners:{
               render: function(p) {  
      // Append the Panel to the click handler's argument list.  
     p.getEl().on('click', function(p,m,e){  
                //处理点击事件代码  
                //alert(this.id);
                Ext.getCmp("toppanel").setVisible(true);
                Ext.getCmp("bottompanel").setVisible(false);
                Ext.getCmp("textfieldid").setValue(this.id);
                Ext.getCmp("displayname").setValue(m.value);
               // alert(p.getWidth);
      });  
      }
                
                }
            
            } 
            ] 
            _panel.add(items);
    }
    _panel.doLayout();
    }
    function isTrue(data){
    var temp =false;
    if(data=='1'){
    temp=true;
    }
    return temp;
    }
    </script>
</head>
<body>
<input type="hidden" name="tablename" id="tablename" value="<%=tablename%>"/>
</body>
</html>

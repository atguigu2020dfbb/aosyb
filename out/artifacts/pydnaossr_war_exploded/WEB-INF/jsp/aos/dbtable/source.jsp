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
<style>
.classDiv2{font-size: 18px; color: #EEE000; } 

</style>
<script type="text/javascript">
    Ext.onReady(function() {
    var buyTypeStore = new Ext.data.Store({  
        fields: ["fieldcnname", "fieldenname"],  
        proxy: {  
            type: "ajax",  
            param:{"tablename":"wsgdwj"},
            url: 'dbtable/input/loadFieldsCombo.jhtml',  
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
                width: 300,
                items: [  
                {xtype: "textfield", name: "windowid",id:"windowid", fieldLabel: "windowid"},
                 {
                 name:'fieldenname',
                 //fieldLabel: 'ieldLabel',
                 xtype: "combo",  
                 id:"dbcombo",
                 mode:'local',
                 fieldLabel:'数据源',
                 labelWidth:'left',
                 store:buyTypeStore,
                 displayField: 'fieldcnname',
                 emptyText:'请选择一个数据源',
                 hiddenName: 'fieldenname',
                 listeners:{
                 select:function(e){
                  var windowid = Ext.getCmp("windowid").getValue();
                  var window=Ext.getCmp(windowid).items.items;
				  var form=window[0].items.items;
				  var windowdb =form[0].items.items;
				  var filedenname = e.displayTplData;
				  windowdb[4].setValue(filedenname[0].fieldenname);
				  Ext.getCmp(windowid).setTitle(filedenname[0].fieldcnname);
				  
                 }
                 },
                 listConfig: {
            getInnerTpl: function() {
                return '<div data-qtip="{fieldcnname}">{fieldcnname} ({fieldenname})</div>';
            }
        }
                 }
                 ]
                    
                    
                    })
                    
                    ]
            },
            // in this instance the TabPanel is not wrapped by another panel
            // since no title is needed, this Panel is added directly
            // as a Container
            Ext.create('Ext.panel.Panel',{
            			region: 'center',
                       id:"Mainpanel"
                      , dockedItems: [{
					        xtype: 'toolbar',
					        dock: 'top',
					        items: [{
					            text: '设置录入'
					            ,xtype:'tbtext'
					        },{text:'添加标签'
					        ,icon:'static/icon/add2.png'
					        ,handler: function(){addlabel();}
					        }
					        ,{text: '添加录入'
					        ,icon:'static/icon/database_add.png'
					        ,handler: function(){addWindow();}
					        }
					        ,{text: '保存'
					        ,icon:'static/icon/save.png',
					        handler:function(){save();}
					        }
					        ,{text: '删除'
					        ,icon:'static/icon/del2.png'
					        ,handler: function(){deletetextfield();}
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
						createWindow(data);
					}
				});
					    
					    }
					    }
             })
            
            ]
        });
    });
    
    function addlabel(){
    var _panel = Ext.getCmp("Mainpanel"); //这里还没搞懂
	var item={
                      　       xtype: 'label'
                      ,text:'text',
                    // cls:'classDiv2',
                header:false
				,draggable: true
                ,resizable: true
     };
	_panel.add(item); //这就是添加事件了
	_panel.doLayout(); //主义这里一定要加 不然显示不出来 作用是重新加载布局
    
    }
    function deletetextfield(){
    
    }
    function createWindow(data){
    for(var i in data){
    Ext.create('Ext.Window', {
shadow:true,
closable:false,
width : data[i].cwidth,
height :data[i].chight,
x:parseInt(data[i].cleft),
y:parseInt(data[i].ctop),
title:data[i].displayname,
layout : 'border',
listeners:{
move:function(e){
 

}
}
    
    
}).show();}
    }
    
    function save(){
//var panelsWithinmyCt = Ext.ComponentQuery.query('window>form>container>textfield'); 

var container = Ext.ComponentQuery.query('window>form>container'); 
var tablename =  Ext.getDom('tablename').value;
var objmoList=new Array();//数组
var objmo=new Object();//对象
for(var i in container){
	var textArray=container[i].items.items;
	objmo.cwidth=textArray[0].getValue();
	objmo.cheight=textArray[1].getValue();
	objmo.ctop=textArray[2].getValue();
	objmo.cleft=textArray[3].getValue();
	objmo.fieldname=textArray[4].getValue()+"D";
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
    
    
    function addWindow(){
    
    var win = Ext.create("Ext.window.Window", {

    layout: "border",
    closable:true,
width : 130,
height : 50,
x:50,
y:50,
    items: [
        {
            xtype: "form",
            defaultType: 'textfield',
            defaults: {
                anchor: '100%',
            },
            fieldDefaults: {
                labelWidth: 80,
                labelAlign: "left",
                flex: 1,
                margin: 5
            },
            items: [
                {
                    xtype: "container",
                    layout: "column",
                    items: [
                        { xtype: "textfield", name: "cwidth", fieldLabel: "宽度"},
                        { xtype: "textfield", name: "cheight", fieldLabel: "高度" },
                        { xtype: "textfield", name: "ctop", fieldLabel: "上边距"},
                        { xtype: "textfield", name: "cleft", fieldLabel: "左边距"},
                        { xtype: "textfield", name: "fieldname", fieldLabel: "数据源"}
                    ]
                }
            ]
        }
    ],
    focus:function(e,m){
    Ext.getCmp("windowid").setValue(this.id);
   // alert(this.width);
    },
    listeners:{
     move:function(e){
    var window=e.items.items;
    var form=window[0].items.items;
    var txtarray =form[0].items.items;
    txtarray[0].setValue(e.width);
    txtarray[1].setValue(e.height);
    txtarray[2].setValue(e.x);
    txtarray[3].setValue(e.y);
    }
    }
});
win.show();
    }
    </script>
</head>
<body>
<input type="hidden" name="tablename" id="tablename" value="<%=tablename%>"/>
</body>
</html>

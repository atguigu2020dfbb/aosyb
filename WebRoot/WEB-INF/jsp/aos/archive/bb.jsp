<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="/WEB-INF/tld/aos.tld" prefix="aos"%>
<aos:html>
<aos:head title="基本表单2">
	<aos:include lib="ext" />
	<aos:base href="demo" />
</aos:head>
<aos:body backGround="true">
</aos:body>
<aos:onready>
<script>
	
Ext.onReady(function(){
	var navigate = function(panel,direction){
		//var layout = panel.getLayout();
		var layout = Ext.getCmp('cardlayout').getLayout();
		alert(direction);
		layout[direction]();
		Ext.getCmp('move-prev').setDisabled(!layout.getPrev());
		Ext.getCmp('move-next').setDisabled(!layout.getNext());
	};
	Ext.create('Ext.panel.Panel',{
				title:'Example Wizard',
				height:500,
				id:'cardlayout',
				width:400,
				layout: 'card',
				//activeItem:0,
				bodyStyle:'padding:15px',
				animCollapse:true,
				renderTo:Ext.getBody(),
				defaults:{
       				 // applied to each contained panel
        			border: false
   				},
				bbar:[{
					id:'move-prev',
					text:'back',
					handler:function(btn){
						navigate(btn.up("panel"),"prev");
					},
					disabled:true
				},'->',{
					id:'move-next',
					text:"next",
					handler:function(btn){
						navigate(btn.up("panel"),"next");
					}
				}],
				items:[{
					id:'card-0',
					html:'<h1>Welcome to the Wizard!</h1>'
				},{
					id:'card-1',
					html:'<p>step 2 of 3 </p>'
				},{
					id:'card-2',
					html:'<h1>Congratulations!</h1><p>Step 3 of  3-complete</p>'
				},
				{
					id:'card-3',
					html:'<h1>Congratulations!</h1><p>888888</p>'
				},
				]
			});
});

	</script>
</aos:onready>
</aos:html>
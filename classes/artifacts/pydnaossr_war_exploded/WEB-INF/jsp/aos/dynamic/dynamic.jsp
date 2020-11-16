<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="/WEB-INF/tld/aos.tld" prefix="aos"%>
<%@ taglib uri="/WEB-INF/tld/cll.tld" prefix="c"%>
<%
	String tablename = (String) request.getAttribute("tablename");
	String path = request.getContextPath();
%>
<aos:html>
<aos:head title="动态分析">
	<aos:include lib="ext" />
	<aos:base href="dynamic" />
</aos:head>
<aos:body>
</aos:body>
<aos:onready>
	<aos:viewport layout="border">
		<aos:formpanel id="_f_dynamic" layout="column" autoScroll="true"
			region="north" border="false" labelWidth="70">
			<aos:docked forceBoder="0 0 1 0">
				<aos:dockeditem xtype="tbtext" text="动态分析" />
			</aos:docked>
			<aos:combobox name="listTablename"
				fields="[ 'tablename', 'tabledesc']" fieldLabel="选择门类"
				id="listTablename" editable="false" columnWidth="0.3"
				url="listTablename.jhtml" displayField="tabledesc"
				valueField="tablename" value="${tablename}" allowBlank="false" />
			<aos:combobox name="listTableField" fieldLabel="选择条件"
				id="listTableField" editable="false" columnWidth="0.3"
				allowBlank="false">
				<aos:option value="nd" display="年度" />
				<aos:option value="bgqx" display="保管期限" />
				<aos:option value="qzh" display="全宗号" />
			</aos:combobox>
		</aos:formpanel>
		<aos:gridpanel id="_g_data" url="getDataList.jhtml" region="center"
			autoScroll="true" enableLocking="true">
			<aos:docked>
				<aos:dockeditem xtype="tbtext" text="目录信息" />
				<aos:dockeditem text="折线统计图" icon="picture.png"
					onclick="_w_zhexian_picture" />
				<aos:dockeditem text="柱状统计图" icon="picture.png"
					onclick="_w_broken_picture" />
				<aos:dockeditem text="饼状统计图" icon="picture.png"
					onclick="_w_bing_picture" />
			</aos:docked>
		</aos:gridpanel>
	</aos:viewport>
	<script>
		function _w_bing_picture(){
			var listTablename = Ext.getCmp("listTablename").getValue();
			var tablenamedesc=Ext.getCmp('listTablename').getRawValue();
			var tablefielddesc=Ext.getCmp("listTableField").getRawValue();
			var listTableField = Ext.getCmp("listTableField").getValue();
			Ext.Ajax.request({   
   			 url:'brokenpicture..jhtml',
                params:{'listTablename':listTablename,'listTableField':listTableField},
                method : 'post', 
   			 success:function(response,config){
					//对后台输出的Json进行解码
					json=Ext.decode(response.responseText);
					//这里需要从后台动态加载
			var store1= Ext.create('Ext.data.JsonStore', {
	                		fields: ['name', 'total'],
							data:json.data,
							autoLoad:true
					});	
			
			 var chart = new Ext.chart.Chart({  
				  xtype: 'chart',
				  width: 700,     
				  height: 600,     
				  store: store1,  
				  animate: true,  
				  shadow: false,//一定会关闭阴影，否则拼饼突出的时候很不好看。
				  legend: {
				            position: 'right'
				        },
				  series: [{         
				   type: 'pie',         
				   field: 'total',
				   showInLegend: true,//显示名称列表
				   donut: true,//圆中空圈显示（如果显示给出数字）
				   label: {//这里能够使拼饼上面显示，该拼饼属于的部分             
				    field: 'name',             
				    display: 'name', 
				    contrast: true,           
				    font: '18px Arial'        
				    },         
				   highlight: {//这里是突出效果的声明，margin越大，鼠标悬停在拼饼上面，拼饼突出得越多             
				    segment: {                 
				     margin: 20            
				     }         
				    }        
				     
				   }] 
				 }); 
					var panel = new Ext.Panel({
						title : tablefielddesc+'统计图',
						renderTo : Ext.getBody(),
						width : 800,
						height : 500,
						frame : true,
						layout : 'fit',
						items : [ chart ]
					});
					//这里的json.columnModel是从后台加载来的
					var win3 = new Ext.Window({
						title : '面板演示',
						width : 800,
						plain : true,
				        iconCls: "addicon",  
				        resizable: true,  
				        collapsible: true,
				        constrainHeader:true,
				        autoScroll:true,
						layout : 'anchor',
						renderTo : Ext.getBody(),
						items : [ panel ],
						buttons : [ {
		                    text: '下载图表',
		                    handler: function() {
		                        Ext.MessageBox.confirm('下载提示', '是否下载当前图表?', function(choice){
		                            if(choice == 'yes'){
		                                chart.save({
		                                    type: 'image/png'
		                                });
		                            }
		                        });
		                    }
		                },{
							text : '关闭',
							handler : function() {
								win3.hide();
							}
						}]
					});
					win3.show();
   			 },
					});
	}
		function _w_zhexian_picture() {
			var listTablename = Ext.getCmp("listTablename").getValue();
			var tablenamedesc=Ext.getCmp('listTablename').getRawValue();
			var tablefielddesc=Ext.getCmp("listTableField").getRawValue();
			var listTableField = Ext.getCmp("listTableField").getValue();
			Ext.Ajax.request({   
   			 url:'brokenpicture..jhtml',
                params:{'listTablename':listTablename,'listTableField':listTableField},
                method : 'post', 
   			 success:function(response,config){
					//对后台输出的Json进行解码
					json=Ext.decode(response.responseText);
					//这里需要从后台动态加载
			var store1= Ext.create('Ext.data.JsonStore', {
	                		fields: ['name', 'total'],
							data:json.data,
							autoLoad:true
					});	
					if(json.count<20){
						json.count=20;
					}
	            var chart = Ext.create('Ext.chart.Chart', {
	                style: 'background:#fff',
	                animate: true,        //动画
	                shadow: true,         //阴影
	                store: store1,        //##
	                legend: {             
	                  position: 'right'   //图例
	                },
	                axes: [{
	                    type: 'Numeric',  //显示图形类型- line：则线图；column：柱形图；radar：
	                    position: 'left',        //
	                    //fields: ['total', 'passed', 'deleted'],
	                    minimum: 0,  //如果小于这个数，图标向下（相当于设置了一个起始点）
	                    maximum:json.count,
	                    label: {
	                        renderer: Ext.util.Format.numberRenderer('0,0')
	                    },
	                    grid: true,
	                    title: "数量"
	                }, {
	                    type: 'Category',
	                    minimum: 0,  //如果小于这个数，图标向下（相当于设置了一个起始点）
	                    position: 'bottom',
	                    fields: ['name'],
	                    title: tablenamedesc+"·"+tablefielddesc
	                }],
	               series: [{
	                    type: 'line',//折线  
	                    xField: 'name',
	                    yField: 'total',
	                    title:'name'
	                    }
	                ]
	            });
					var panel = new Ext.Panel({
						title : tablefielddesc+'统计图',
						renderTo : Ext.getBody(),
						width : 800,
						height : 500,
						frame : true,
						layout : 'fit',
						items : [ chart ]
					});
					//这里的json.columnModel是从后台加载来的
					var win3 = new Ext.Window({
						title : '面板演示',
						width : 800,
						plain : true,
				        iconCls: "addicon",  
				        resizable: true,  
				        collapsible: true,
				        constrainHeader:true,
				        autoScroll:true,
						layout : 'anchor',
						renderTo : Ext.getBody(),
						items : [ panel ],
						buttons : [ {
		                    text: '下载图表',
		                    handler: function() {
		                        Ext.MessageBox.confirm('下载提示', '是否下载当前图表?', function(choice){
		                            if(choice == 'yes'){
		                                chart.save({
		                                    type: 'image/png'
		                                });
		                            }
		                        });
		                    }
		                },{
							text : '关闭',
							handler : function() {
								win3.hide();
							}
						}]
					});
					win3.show();
   			 },
					});
				}
		function _w_broken_picture() {
			var listTablename = Ext.getCmp("listTablename").getValue();
			var tablenamedesc=Ext.getCmp('listTablename').getRawValue();
			var tablefielddesc=Ext.getCmp("listTableField").getRawValue();
			var listTableField = Ext.getCmp("listTableField").getValue();
			Ext.Ajax.request({   
   			 url:'brokenpicture..jhtml',
                params:{'listTablename':listTablename,'listTableField':listTableField},
                method : 'post', 
   			 success:function(response,config){
					//对后台输出的Json进行解码
					json=Ext.decode(response.responseText);
					//这里需要从后台动态加载
			var store1= Ext.create('Ext.data.JsonStore', {
	                		fields: ['name', 'total'],
							data:json.data,
							autoLoad:true
					});				
	            var chart = Ext.create('Ext.chart.Chart', {
	                style: 'background:#fff',
	                animate: true,        //动画
	                shadow: true,         //阴影
	                store: store1,        //##
	                legend: {             
	                  position: 'right'   //图例
	                },
	                axes: [{
	                    type: 'Numeric',  //显示图形类型- line：则线图；column：柱形图；radar：
	                    position: 'bottom',        //
	                    //fields: ['total', 'passed', 'deleted'],
	                    xField: 'name',
	                    yField: ['total'],
	                    minimum: 0,  //如果小于这个数，图标向下（相当于设置了一个起始点）
	                    label: {
	                        renderer: Ext.util.Format.numberRenderer('0,0')
	                    },
	                    grid: true,
	                    title: tablenamedesc+"·"+tablefielddesc
	                }, {
	                    type: 'Category',
	                    position: 'left',
	                    fields: ['name']
	                    //, title: '员工绩效统计图'
	                }],
	               series: [{
	                	 type: 'bar',
	                	 //type : 'pie',
	                     axis: 'bottom',
	                    xField: 'name',
	                    yField: ['total']
	                    }
	                ]
	            });
					var panel = new Ext.Panel({
						title : tablefielddesc+'统计图',
						renderTo : Ext.getBody(),
						width : 800,
						height : 500,
						frame : true,
						layout : 'fit',
						items : [ chart ]
					});
					//这里的json.columnModel是从后台加载来的
					var win3 = new Ext.Window({
						title : '面板演示',
						width : 800,
						plain : true,
				        iconCls: "addicon",  
				        resizable: true,  
				        collapsible: true,
				        constrainHeader:true,
				        autoScroll:true,
						layout : 'anchor',
						renderTo : Ext.getBody(),
						items : [ panel ],
						buttons : [ {
		                    text: '下载图表',
		                    handler: function() {
		                        Ext.MessageBox.confirm('下载提示', '是否下载当前图表?', function(choice){
		                            if(choice == 'yes'){
		                                chart.save({
		                                    type: 'image/png'
		                                });
		                            }
		                        });
		                    }
		                },{
							text : '关闭',
							handler : function() {
								win3.hide();
							}
						}]
					});
					win3.show();
   			 },
					});
				}
	</script>
</aos:onready>
</aos:html>
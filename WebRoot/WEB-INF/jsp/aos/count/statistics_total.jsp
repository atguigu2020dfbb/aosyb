<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="/WEB-INF/tld/aos.tld" prefix="aos"%>
<%
	String path = request.getContextPath();
%>
<aos:html>
	<aos:head title="档案总库统计">
		<aos:include lib="ext" />
		<aos:base href="count/statistics_total" />
		<style>
			._div_tips table tr {
				align:"true"
			}
			._div_tips table tr td{
				align:"true"
			}
		</style>
	</aos:head>
	<aos:body>
		<div id="_div_tips" class="x-hidden">
		</div>
	</aos:body>
	<aos:onready>
		<aos:viewport layout="border" id="viewport"  autoScroll="true" split="true">

					<aos:gridpanel id="_g_tablename" region="west" url="listTablename.jhtml" height="200"
								   hidePagebar="true" onrender="_g_tablename_query" autoScroll="true" >
						<aos:hiddenfield id="type" name="type" value="${type}"/>
						<aos:selmodel type="checkbox" mode="multi" />
						<aos:column type="rowno" />
						<aos:column header="门类列表" dataIndex="tablename" hidden="true" />
						<aos:column header="门类名称" dataIndex="tabledesc"  />
				    </aos:gridpanel>
					<aos:formpanel id="_f_info" layout="column" region="center" labelWidth="70" height="200"
											  header="false" icon="user8.png">
						<aos:hiddenfield id="flag" name="flag"/>
						<aos:combobox  name="qzmc" id="qzmc" fieldLabel="全宗名称" margin="50 0 0 0" dicField="qzmc"  columnWidth="0.24" />
						<aos:button text="统计" onclick="tj1_load" margin="50 0 0 100" width="100" ></aos:button>
					</aos:formpanel>
			<!--添加对应的table表格-->
						<aos:panel region="south"  id="grid_nd" contentEl="_div_tips" autoScroll="true"></aos:panel>
		</aos:viewport>
		<script type="text/javascript">
			window.onload=function(){
				Ext.getCmp("grid_nd").setHeight(window.innerHeight * 2/ 3);
			}
			function tj1_load(){
				//得到选中的门类列表

				var selection = AOS.selection(_g_tablename, 'id_');
				if (AOS.empty(selection)) {
					AOS.tip('请选择门类!');
					return;
				}
				var tablenames="";
				var row=_g_tablename.getSelectionModel().getSelection();
				if(row.length>1){
					alert("请选择一个门类！");
					return;
				};
				for(var i=0;i<AOS.rows(_g_tablename);i++){
					if(i==0){
						tablenames=row[i].data.tablename;
					}else{
						tablenames+=","+row[i].data.tablename;
					}
				}
				Ext.getCmp("flag").setValue("1");
				//动态的走后台进行数据的查询操作
				AOS.ajax({
					forms:_f_info,
					params:{tablenames:tablenames,
					qzdesc:Ext.getCmp("qzmc").getRawValue()},
					url : 'countMath.jhtml',
					ok : function(data) {
						//添加table的名称   已经列名
						var tab='<table border="1" style="margin: auto;margin-top: 50px"  cellspacing="0" align="true" width="800">';
						tab+='<caption style="font-size: 30px" >档案数量统计表</caption>';
						tab+='<tr style="font-size:15px;text-align: center;">\n' +
								'\t\t\t\t\t<th tyle="text-align: center;" rowspan="2" >全宗名称</th>\n' +
								'\t\t\t\t\t<th tyle="text-align: center;" rowspan="2">年度</th>\n' +
								'\t\t\t\t\t<th tyle="text-align: center;" colspan="9">保管期限</th>\n' +
								'\t\t\t\t\t<th tyle="text-align: center;" colspan="3" rowspan="2">合计（实有件数）</th>\n' +
								'\t\t\t\t</tr>';
						tab+='<tr style="font-size:15px;text-align: center;">\n' +
								'\t\t\t\t\t<td tyle="text-align: center;" colspan="3">10年</td>\n' +
								'\t\t\t\t\t<td tyle="text-align: center;" colspan="3">30年</td>\n' +
								'\t\t\t\t\t<td  tyle="text-align: center;" colspan="3">永久</td>\n' +
								'\t\t\t\t</tr>';
						var obj=eval(data);
						for (var x in obj) { // 遍历Map
							if(x==0){
								tab+='<tr style="font-size:15px;text-align: center;">\n' +
										'\t\t\t\t\t<td tyle="text-align: center;" rowspan="'+obj.length*2+'">'+obj[x].qzmc+'</td>\n' +
										'\t\t\t\t\t<td tyle="text-align: center;" rowspan="2">'+obj[x].nd+'</td>\n' +
										'\t\t\t\t\t<td tyle="text-align: center;">件数</td>\n' +
										'\t\t\t\t\t<td tyle="text-align: center;">盒数</td>\n' +
										'\t\t\t\t\t<td tyle="text-align: center;">页数</td>\n' +
										'\t\t\t\t\t<td tyle="text-align: center;">件数</td>\n' +
										'\t\t\t\t\t<td tyle="text-align: center;">盒数</td>\n' +
										'\t\t\t\t\t<td tyle="text-align: center;">页数</td>\n' +
										'\t\t\t\t\t<td tyle="text-align: center;">件数</td>\n' +
										'\t\t\t\t\t<td tyle="text-align: center;">盒数</td>\n' +
										'\t\t\t\t\t<td tyle="text-align: center;">页数</td>\n' +
										'\t\t\t\t\t<td tyle="text-align: center;">件数</td>\n' +
										'\t\t\t\t\t<td tyle="text-align: center;">盒数</td>\n' +
										'\t\t\t\t\t<td tyle="text-align: center;">页数</td>\n' +
										'\t\t\t\t</tr>';
							}else{
								tab+='<tr style="font-size:15px;text-align: center;">\n' +
										'\t\t\t\t\t<td tyle="text-align: center;" rowspan="2">'+obj[x].nd+'</td>\n' +
										'\t\t\t\t\t<td tyle="text-align: center;">件数</td>\n' +
										'\t\t\t\t\t<td tyle="text-align: center;">盒数</td>\n' +
										'\t\t\t\t\t<td tyle="text-align: center;">页数</td>\n' +
										'\t\t\t\t\t<td tyle="text-align: center;">件数</td>\n' +
										'\t\t\t\t\t<td tyle="text-align: center;">盒数</td>\n' +
										'\t\t\t\t\t<td tyle="text-align: center;">页数</td>\n' +
										'\t\t\t\t\t<td tyle="text-align: center;">件数</td>\n' +
										'\t\t\t\t\t<td tyle="text-align: center;">盒数</td>\n' +
										'\t\t\t\t\t<td tyle="text-align: center;">页数</td>\n' +
										'\t\t\t\t\t<td tyle="text-align: center;">件数</td>\n' +
										'\t\t\t\t\t<td tyle="text-align: center;">盒数</td>\n' +
										'\t\t\t\t\t<td tyle="text-align: center;">页数</td>\n' +
										'\t\t\t\t</tr>';
							}

							tab+='<tr style="font-size:15px;text-align: center;">\n' +
									'\t\t\t\t\t<td tyle="text-align: center;">'+obj[x].js_10+'</td>\n' +
									'\t\t\t\t\t<td tyle="text-align: center;">'+obj[x].hs_10+'</td>\n' +
									'\t\t\t\t\t<td tyle="text-align: center;">'+obj[x].ys_10+'</td>\n' +
									'\t\t\t\t\t<td tyle="text-align: center;">'+obj[x].js_30+'</td>\n' +
									'\t\t\t\t\t<td tyle="text-align: center;">'+obj[x].hs_30+'</td>\n' +
									'\t\t\t\t\t<td tyle="text-align: center;">'+obj[x].ys_30+'</td>\n' +
									'\t\t\t\t\t<td tyle="text-align: center;">'+obj[x].js_y+'</td>\n' +
									'\t\t\t\t\t<td tyle="text-align: center;">'+obj[x].hs_y+'</td>\n' +
									'\t\t\t\t\t<td tyle="text-align: center;">'+obj[x].ys_y+'</td>\n' +
									'\t\t\t\t\t<td tyle="text-align: center;">'+obj[x].js_z+'</td>\n' +
									'\t\t\t\t\t<td tyle="text-align: center;">'+obj[x].hs_z+'</td>\n' +
									'\t\t\t\t\t<td tyle="text-align: center;">'+obj[x].ys_z+'</td>\n' +
									'\t\t\t\t</tr>';
						}
						tab+='</table>';
						document.getElementById('_div_tips').innerHTML=tab;
						grid_nd.doLayout();
					}
				});
			}
			function tj2_load(){
				//动态的走后台进行数据的查询操作
				Ext.getCmp("flag").setValue("2");
				AOS.ajax({
					forms:_f_info,
					url : 'countMath2.jhtml',
					ok : function(data) {
						//添加table的名称   已经列名
						var tab='<table border="1" style="margin: auto;margin-top: 50px"  cellspacing="0" align="true" width="800">';
						tab+='<caption style="font-size: 30px" >电子档案统计表</caption>';
						tab+='<tr style="font-size:15px;text-align: center;">\n' +
								'\t\t\t\t\t<th tyle="text-align: center;" rowspan="3" >全宗名称</th>\n' +
								'\t\t\t\t\t<th tyle="text-align: center;" rowspan="3">年度</th>\n' +
								'\t\t\t\t\t<th tyle="text-align: center;" colspan="15">保管期限</th>\n' +
								'\t\t\t\t\t<th tyle="text-align: center;" colspan="3" rowspan="2">合计</th>\n' +
								'\t\t\t\t</tr>';
						tab+='<tr style="font-size:15px;text-align: center;">\n' +
								'\t\t\t\t\t<td tyle="text-align: center;" colspan="5">10年</td>\n' +
								'\t\t\t\t\t<td tyle="text-align: center;" colspan="5">30年</td>\n' +
								'\t\t\t\t\t<td  tyle="text-align: center;" colspan="5">永久</td>\n' +
								'\t\t\t\t</tr>';
						tab+='<tr style="font-size:15px;text-align: center;">\n' +
								'\t\t\t\t\t<td tyle="text-align: center;">实有件数</td>\n' +
								'\t\t\t\t\t<td tyle="text-align: center;">实扫件数</td>\n' +
								'\t\t\t\t\t<td tyle="text-align: center;">应扫页数</td>\n' +
								'\t\t\t\t\t<td tyle="text-align: center;">实扫页数</td>\n' +
								'\t\t\t\t\t<td tyle="text-align: center;">实扫容量</td>\n' +

								'\t\t\t\t\t<td tyle="text-align: center;">实有件数</td>\n' +
								'\t\t\t\t\t<td tyle="text-align: center;">实扫件数</td>\n' +
								'\t\t\t\t\t<td tyle="text-align: center;">应扫页数</td>\n' +
								'\t\t\t\t\t<td tyle="text-align: center;">实扫页数</td>\n' +
								'\t\t\t\t\t<td tyle="text-align: center;">实扫容量</td>\n' +

								'\t\t\t\t\t<td tyle="text-align: center;">实有件数</td>\n' +
								'\t\t\t\t\t<td tyle="text-align: center;">实扫件数</td>\n' +
								'\t\t\t\t\t<td tyle="text-align: center;">应扫页数</td>\n' +
								'\t\t\t\t\t<td tyle="text-align: center;">实扫页数</td>\n' +
								'\t\t\t\t\t<td tyle="text-align: center;">实扫容量</td>\n' +
								'\t\t\t\t\t<td tyle="text-align: center;">实有件数</td>\n' +
								'\t\t\t\t\t<td tyle="text-align: center;">实扫件数</td>\n' +
								'\t\t\t\t\t<td tyle="text-align: center;">应扫页数</td>\n' +
								'\t\t\t\t</tr>';
						var obj=eval(data);
						for (var x in obj) { // 遍历Map
							if(x==0){
								tab+='<tr style="font-size:15px;text-align: center;">\n' +
										'\t\t\t\t\t<td tyle="text-align: center;" rowspan="'+obj.length+'">'+obj[x].qzmc+'</td>\n' +
										'\t\t\t\t\t<td tyle="text-align: center;">'+obj[x].nd+'</td>\n' +

										'\t\t\t\t\t<td tyle="text-align: center;">'+obj[x].syjs_10+'</td>\n' +
										'\t\t\t\t\t<td tyle="text-align: center;">'+obj[x].ssjs_10+'</td>\n' +
										'\t\t\t\t\t<td tyle="text-align: center;">'+obj[x].ysys_10+'</td>\n' +
										'\t\t\t\t\t<td tyle="text-align: center;">'+obj[x].ssys_10+'</td>\n' +
										'\t\t\t\t\t<td tyle="text-align: center;">'+obj[x].ssrl_10+'</td>\n' +


										'\t\t\t\t\t<td tyle="text-align: center;">'+obj[x].syjs_30+'</td>\n' +
										'\t\t\t\t\t<td tyle="text-align: center;">'+obj[x].ssjs_30+'</td>\n' +
										'\t\t\t\t\t<td tyle="text-align: center;">'+obj[x].ysys_30+'</td>\n' +
										'\t\t\t\t\t<td tyle="text-align: center;">'+obj[x].ssys_30+'</td>\n' +
										'\t\t\t\t\t<td tyle="text-align: center;">'+obj[x].ssrl_30+'</td>\n' +


										'\t\t\t\t\t<td tyle="text-align: center;">'+obj[x].syjs_y+'</td>\n' +
										'\t\t\t\t\t<td tyle="text-align: center;">'+obj[x].ssjs_y+'</td>\n' +
										'\t\t\t\t\t<td tyle="text-align: center;">'+obj[x].ysys_y+'</td>\n' +
										'\t\t\t\t\t<td tyle="text-align: center;">'+obj[x].ssys_y+'</td>\n' +
										'\t\t\t\t\t<td tyle="text-align: center;">'+obj[x].ssrl_y+'</td>\n' +

										'\t\t\t\t\t<td tyle="text-align: center;">'+obj[x].syjs_z+'</td>\n' +
										'\t\t\t\t\t<td tyle="text-align: center;">'+obj[x].ssjs_z+'</td>\n' +
										'\t\t\t\t\t<td tyle="text-align: center;">'+obj[x].ysys_z+'</td>\n' +
										'\t\t\t\t</tr>';
							}else{

							tab+='<tr style="font-size:15px;text-align: center;">\n' +
									'\t\t\t\t\t<td tyle="text-align: center;">'+obj[x].nd+'</td>\n' +
									'\t\t\t\t\t<td tyle="text-align: center;">'+obj[x].syjs_10+'</td>\n' +
									'\t\t\t\t\t<td tyle="text-align: center;">'+obj[x].ssjs_10+'</td>\n' +
									'\t\t\t\t\t<td tyle="text-align: center;">'+obj[x].ysys_10+'</td>\n' +
									'\t\t\t\t\t<td tyle="text-align: center;">'+obj[x].ssys_10+'</td>\n' +
									'\t\t\t\t\t<td tyle="text-align: center;">'+obj[x].ssrl_10+'</td>\n' +


									'\t\t\t\t\t<td tyle="text-align: center;">'+obj[x].syjs_30+'</td>\n' +
									'\t\t\t\t\t<td tyle="text-align: center;">'+obj[x].ssjs_30+'</td>\n' +
									'\t\t\t\t\t<td tyle="text-align: center;">'+obj[x].ysys_30+'</td>\n' +
									'\t\t\t\t\t<td tyle="text-align: center;">'+obj[x].ssys_30+'</td>\n' +
									'\t\t\t\t\t<td tyle="text-align: center;">'+obj[x].ssrl_30+'</td>\n' +


									'\t\t\t\t\t<td tyle="text-align: center;">'+obj[x].syjs_y+'</td>\n' +
									'\t\t\t\t\t<td tyle="text-align: center;">'+obj[x].ssjs_y+'</td>\n' +
									'\t\t\t\t\t<td tyle="text-align: center;">'+obj[x].ysys_y+'</td>\n' +
									'\t\t\t\t\t<td tyle="text-align: center;">'+obj[x].ssys_y+'</td>\n' +
									'\t\t\t\t\t<td tyle="text-align: center;">'+obj[x].ssrl_y+'</td>\n' +

									'\t\t\t\t\t<td tyle="text-align: center;">'+obj[x].syjs_z+'</td>\n' +
									'\t\t\t\t\t<td tyle="text-align: center;">'+obj[x].ssjs_z+'</td>\n' +
									'\t\t\t\t\t<td tyle="text-align: center;">'+obj[x].ysys_z+'</td>\n' +
									'\t\t\t\t</tr>';
							}
						}
						tab+='</table>';
						document.getElementById('_div_tips').innerHTML=tab;
						grid_nd.doLayout();
					}
				});
			}
			function tj3_load(){
				Ext.getCmp("flag").setValue("3");
				//动态的走后台进行数据的查询操作
						//添加table的名称   已经列名
						var tab='<table border="1" style="margin: auto;margin-top: 50px"  cellspacing="0" align="true" width="800">';
						tab+='<caption style="font-size: 30px" >电子档案统计表</caption>';
						tab+='<tr style="font-size:15px;text-align: center;">\n' +
								'\t\t\t\t\t<th tyle="text-align: center;" rowspan="2" >全宗名称</th>\n' +
								'\t\t\t\t\t<th tyle="text-align: center;" rowspan="2">借阅方式</th>\n' +
								'\t\t\t\t\t<th tyle="text-align: center;" rowspan="2">查档人次</th>\n' +
								'\t\t\t\t\t<th tyle="text-align: center;" rowspan="2">查档件次</th>\n' +
								'\t\t\t\t\t<th tyle="text-align: center;" colspan="6">利用目的</th>\n' +
								'\t\t\t\t</tr>';
						tab+='<tr style="font-size:15px;text-align: center;">\n' +
								'\t\t\t\t\t<td tyle="text-align: center;">工作查考</td>\n' +
								'\t\t\t\t\t<td tyle="text-align: center;">学术研究</td>\n' +
								'\t\t\t\t\t<td  tyle="text-align: center;">经济建设</td>\n' +
								'\t\t\t\t\t<td  tyle="text-align: center;">落实政策</td>\n' +
								'\t\t\t\t\t<td  tyle="text-align: center;">个人取证</td>\n' +
								'\t\t\t\t\t<td  tyle="text-align: center;">其他</td>\n' +
								'\t\t\t\t</tr>';
						tab+='<tr style="font-size:15px;text-align: center;">\n' +
								'\t\t\t\t\t<td tyle="text-align: center;" rowspan="2">州交通运输局</td>\n' +
								'\t\t\t\t\t<td tyle="text-align: center;">借阅</td>\n' +
								'\t\t\t\t\t<td tyle="text-align: center;">0</td>\n' +
								'\t\t\t\t\t<td tyle="text-align: center;">0</td>\n' +
								'\t\t\t\t\t<td tyle="text-align: center;">0</td>\n' +

								'\t\t\t\t\t<td tyle="text-align: center;">0</td>\n' +
								'\t\t\t\t\t<td tyle="text-align: center;">0</td>\n' +
								'\t\t\t\t\t<td tyle="text-align: center;">0</td>\n' +
								'\t\t\t\t\t<td tyle="text-align: center;">0</td>\n' +
								'\t\t\t\t\t<td tyle="text-align: center;">0</td>\n' +
								'\t\t\t\t</tr>';
						tab+='<tr style="font-size:15px;text-align: center;">\n' +
								'\t\t\t\t\t<td tyle="text-align: center;">借出</td>\n' +
								'\t\t\t\t\t<td tyle="text-align: center;">0</td>\n' +
								'\t\t\t\t\t<td tyle="text-align: center;">0</td>\n' +
								'\t\t\t\t\t<td tyle="text-align: center;">0</td>\n' +

								'\t\t\t\t\t<td tyle="text-align: center;">0</td>\n' +
								'\t\t\t\t\t<td tyle="text-align: center;">0</td>\n' +
								'\t\t\t\t\t<td tyle="text-align: center;">0</td>\n' +
								'\t\t\t\t\t<td tyle="text-align: center;">0</td>\n' +
								'\t\t\t\t\t<td tyle="text-align: center;">0</td>\n' +
								'\t\t\t\t</tr>';
						tab+='</table>';
						document.getElementById('_div_tips').innerHTML=tab;
						grid_nd.doLayout();
			}
			function zhexian1_picture(){
				Ext.Ajax.request({
					url:'broken1picture.jhtml',
					params:{'flag':Ext.getCmp("flag").getValue(),'tablename':Ext.getCmp("listTablename").value,'qzmc':Ext.getCmp("qzmc").getValue(),'start_listnd':Ext.getCmp("start_listnd").getValue(),'end_listnd':Ext.getCmp("end_listnd").getValue()},
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
								title: Ext.getCmp("listTablename").value+Ext.getCmp("start_listnd").getValue()+"~"+Ext.getCmp("end_listnd").getValue()+"数量统计表"
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
							title :  Ext.getCmp("listTablename").getRawValue()+'统计图',
							renderTo : Ext.getBody(),
							width : 900,
							height : 500,
							frame : true,
							layout : 'fit',
							items : [ chart ]
						});
						//这里的json.columnModel是从后台加载来的
						var win3 = new Ext.Window({
							title : '面板演示',
							width : 900,
							plain : true,
							iconCls: "addicon",
							resizable: true,
							collapsible: true,
							constrainHeader:true,
							autoScroll:true,
							layout : 'anchor',
							renderTo : Ext.getBody(),
							items : [ panel ],
							buttons : [{
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
			function zhuzhuang1_picture(){
				Ext.Ajax.request({
					url:'broken1picture.jhtml',
					params:{'flag':Ext.getCmp("flag").getValue(),'tablename':Ext.getCmp("listTablename").value,'qzmc':Ext.getCmp("qzmc").getValue(),'start_listnd':Ext.getCmp("start_listnd").getValue(),'end_listnd':Ext.getCmp("end_listnd").getValue()},
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
								title: Ext.getCmp("listTablename").value+Ext.getCmp("start_listnd").getValue()+"~"+Ext.getCmp("end_listnd").getValue()+"数量统计表"
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
								label: {
									contrast: true,
									display: 'insideEnd',
									field: 'total',
									color: '#000',
									orientation: 'vertical',
									'text-anchor': 'middle'
								},
								xField: 'name',
								yField: ['total']
							}
							]
						});
						var panel = new Ext.Panel({
							title : Ext.getCmp("listTablename").getRawValue()+'统计图',
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
			function bingzhuang1_picture(){
				Ext.Ajax.request({
					url:'broken1picture.jhtml',
					params:{'flag':Ext.getCmp("flag").getValue(),'tablename':Ext.getCmp("listTablename").value,'qzmc':Ext.getCmp("qzmc").getValue(),'start_listnd':Ext.getCmp("start_listnd").getValue(),'end_listnd':Ext.getCmp("end_listnd").getValue()},
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
								label: {
									contrast: true,
									display: 'insideEnd',
									field: 'name',
									color: '#000',
									tips : {
										trackMouse : true
									},
									orientation: 'vertical',
									'text-anchor': 'middle'
								},
								highlight: {//这里是突出效果的声明，margin越大，鼠标悬停在拼饼上面，拼饼突出得越多
									segment: {
										margin: 20
									}
								}
							}]
						});
						var panel = new Ext.Panel({
							title : Ext.getCmp("listTablename").value+Ext.getCmp("start_listnd").getValue()+"~"+Ext.getCmp("end_listnd").getValue()+"数量饼状统计图",
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
			function zhexian2_picture(){
				Ext.Ajax.request({
					url:'broken2picture.jhtml',
					params:{'flag':Ext.getCmp("flag").getValue(),'tablename':Ext.getCmp("listTablename").value,'qzmc':Ext.getCmp("qzmc").getValue(),'start_listnd':Ext.getCmp("start_listnd").getValue(),'end_listnd':Ext.getCmp("end_listnd").getValue()},
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
								title: Ext.getCmp("listTablename").value+Ext.getCmp("start_listnd").getValue()+"~"+Ext.getCmp("end_listnd").getValue()+"数量统计表"
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
							title :  Ext.getCmp("listTablename").getRawValue()+'统计图',
							renderTo : Ext.getBody(),
							width : 900,
							height : 500,
							frame : true,
							layout : 'fit',
							items : [ chart ]
						});
						//这里的json.columnModel是从后台加载来的
						var win3 = new Ext.Window({
							title : '面板演示',
							width : 900,
							plain : true,
							iconCls: "addicon",
							resizable: true,
							collapsible: true,
							constrainHeader:true,
							autoScroll:true,
							layout : 'anchor',
							renderTo : Ext.getBody(),
							items : [ panel ],
							buttons : [{
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
			function zhuzhuang2_picture(){
				Ext.Ajax.request({
					url:'broken2picture.jhtml',
					params:{'flag':Ext.getCmp("flag").getValue(),'tablename':Ext.getCmp("listTablename").value,'qzmc':Ext.getCmp("qzmc").getValue(),'start_listnd':Ext.getCmp("start_listnd").getValue(),'end_listnd':Ext.getCmp("end_listnd").getValue()},
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
								title: Ext.getCmp("listTablename").value+Ext.getCmp("start_listnd").getValue()+"~"+Ext.getCmp("end_listnd").getValue()+"数量统计表"
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
								label: {
									contrast: true,
									display: 'insideEnd',
									field: 'total',
									color: '#000',
									orientation: 'vertical',
									'text-anchor': 'middle'
								},
								xField: 'name',
								yField: ['total']
							}
							]
						});
						var panel = new Ext.Panel({
							title : Ext.getCmp("listTablename").getRawValue()+'统计图',
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
			function bingzhuang2_picture(){
				Ext.Ajax.request({
					url:'broken2picture.jhtml',
					params:{'flag':Ext.getCmp("flag").getValue(),'tablename':Ext.getCmp("listTablename").value,'qzmc':Ext.getCmp("qzmc").getValue(),'start_listnd':Ext.getCmp("start_listnd").getValue(),'end_listnd':Ext.getCmp("end_listnd").getValue()},
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
								label: {
									contrast: true,
									display: 'insideEnd',
									field: 'name',
									color: '#000',
									tips : {
										trackMouse : true
									},
									orientation: 'vertical',
									'text-anchor': 'middle'
								},
								highlight: {//这里是突出效果的声明，margin越大，鼠标悬停在拼饼上面，拼饼突出得越多
									segment: {
										margin: 20
									}
								}
							}]
						});
						var panel = new Ext.Panel({
							title : Ext.getCmp("listTablename").value+Ext.getCmp("start_listnd").getValue()+"~"+Ext.getCmp("end_listnd").getValue()+"数量饼状统计图",
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
			function _g_tablename_query(){
				var params={type:type.getValue()};
				_g_tablename_store.getProxy().extraParams = params;
				_g_tablename_store.reload();
			}
			//折线统计图
			function zhexian_picture() {
				if(Ext.getCmp("flag").getValue()=='1'){
					zhexian1_picture();
				}else if(Ext.getCmp("flag").getValue()=='2'){
					zhexian2_picture();
				}
			}
			//柱状统计图
			function zhuzhuang_picture(){
				if(Ext.getCmp("flag").getValue()=='1'){
					zhuzhuang1_picture();
				}else if(Ext.getCmp("flag").getValue()=='2'){
					zhuzhuang2_picture();
				}
			}
			//扇形统计图
			function bingzhuang_picture(){
				if(Ext.getCmp("flag").getValue()=='1'){
					bingzhuang1_picture();
				}else if(Ext.getCmp("flag").getValue()=='2'){
					bingzhuang2_picture();
				}
			}
		</script>
	</aos:onready>
	<script type="text/javascript">
	</script>
</aos:html>
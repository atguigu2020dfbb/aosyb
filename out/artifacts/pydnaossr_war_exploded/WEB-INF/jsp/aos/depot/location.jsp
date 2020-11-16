<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="/WEB-INF/tld/aos.tld" prefix="aos"%>
<%
	String path = request.getContextPath();
%>
<aos:html>
	<aos:head title="存址登记">
		<aos:include lib="ext" />
		<aos:base href="depot/location" />
		<style>

		</style>
	</aos:head>
	<aos:body>
		<div id="_div_tips" class="x-hidden">
		</div>
	</aos:body>
	<aos:onready>
		<aos:viewport layout="border" id="viewport" autoScroll="true">
			<aos:panel region="center" border="false">
				<%-- 垂直盒子布局，里面可以放置任意多个固定高度或者自适应高度的组件 --%>
				<aos:layout type="vbox" align="stretch" />
				<%-- 这里也可以设置flex属性，不写死高度 --%>
				<aos:panel height="200" layout="fit" autoScroll="true">
					<aos:formpanel id="_f_info" layout="column" labelWidth="70"
								   header="false" icon="user8.png">
						<aos:docked forceBoder="0 0 1 0">
							<aos:dockeditem xtype="tbtext" text="登记信息" />
						</aos:docked>
						<aos:hiddenfield id="flag" name="flag"/>
						<aos:combobox name="archive_storehouse" fieldLabel="库房名称" id="archive_storehouse" value="xl1" columnWidth="0.24" allowBlank="false">
							<aos:option value="X1" display="现行库房1"/>
							<aos:option value="X2" display="现行库房2"/>
							<aos:option value="X3" display="现行库房3"/>
							<aos:option value="X4" display="现行库房4"/>
							<aos:option value="LS" display="历史库房"/>
							<aos:option value="ZL" display="资料库房"/>
						</aos:combobox>
						<aos:textfield  name="column_number" id="column_number" fieldLabel="列号"  columnWidth="0.24" allowBlank="false"/>
						<aos:textfield  name="group_number" id="group_number" fieldLabel="组号"  columnWidth="0.24" allowBlank="false"/>
						<aos:button text="存址登记" onclick="location_register"></aos:button>
					</aos:formpanel>
				</aos:panel>
				<aos:panel layout="fit" id="nd_tjt" >
					<aos:docked forceBoder="1 0 1 0">
						<aos:dockeditem xtype="tbtext" text="存址登记表" />
						<aos:dockeditem text="保存存址信息" icon="picture.png" onclick="save_location"
						/>
						<aos:dockeditem text="操作日志" icon="more/view-history.png" onclick="get_log"
						/>
					</aos:docked>
				</aos:panel>
				<!--添加对应的table表格-->
				<aos:panel layout="fit" id="grid_nd" contentEl="_div_tips">
				</aos:panel>
			</aos:panel>
		</aos:viewport>
		<aos:window id="_w_location_form" title="分配" onshow="_w_location_load">
			<aos:formpanel id="_f_location_form" width="500" layout="anchor"
						   labelWidth="90">
				<aos:hiddenfield name="id_"  />
				<aos:textfield fieldLabel="存址编号" name="location_number" columnWidth="1.0" readOnly="true"/>
				<aos:textfield fieldLabel="库房名称" name="archive_storehouse" columnWidth="1.0" readOnly="true"/>
				<aos:textfield fieldLabel="组号" name="zuhao" columnWidth="1.0" readOnly="true"/>
				<aos:textfield fieldLabel="列号" name="liehao" columnWidth="1.0" readOnly="true"/>
				<aos:textfield fieldLabel="录入人" name="lrr" columnWidth="1.0" readOnly="true"/>
				<aos:textfield fieldLabel="录入时间" name="lrsj" columnWidth="1.0" readOnly="true"/>
				<aos:textareafield fieldLabel="录入描述" name="lr_description" columnWidth="1.0" />
			</aos:formpanel>
			<aos:docked dock="bottom" ui="footer">
				<aos:dockeditem xtype="tbfill" />
				<aos:dockeditem onclick="_w_location_form_add" text="确定" icon="add.png" />
				<aos:dockeditem onclick="#_w_location_form.hide();" text="关闭" icon="close.png" />
			</aos:docked>
		</aos:window>
		<aos:window id="_w_category_log" title="操作日志" onshow="_w_log_load" width="820">
			<aos:gridpanel id="_g_category_log" url="listelog.jhtml" region="north" split="true" width="800"
						   enableLocking="false" height="400">
				<aos:selmodel type="row" mode="multi" />
				<aos:column header="存址编号" dataIndex="location_number"  width="100" align="true" />
				<aos:column header="库房名称" dataIndex="archive_storehouse"  width="100" align="true" />
				<aos:column header="组号" dataIndex="zuhao"  width="100" align="true" />
				<aos:column header="列号" dataIndex="liehao"  width="100" align="true" />
				<aos:column header="录入人" dataIndex="lrr"  width="100" align="true" />
				<aos:column header="录入时间" dataIndex="lrsj"  width="100" align="true" />
				<aos:column header="录入描述" dataIndex="lr_description"  width="200" align="true" />
				<aos:column header="" flex="1" />
			</aos:gridpanel>
		</aos:window>
		<script type="text/javascript">
			//
			function location_register(){
				var archive_storehouse=Ext.getCmp("archive_storehouse").getValue();
				var archive_storehouse_raw=Ext.getCmp("archive_storehouse").getRawValue();
				var column_number=Ext.getCmp("column_number").getValue();
				var group_number=Ext.getCmp("group_number").getValue();
				var table='<table border="1" id="location_table" style="margin-top: 50px;margin-left: 50px" cellspacing="0" >';
				table+='<caption style="font-size: 30px" >'+archive_storehouse_raw+'档案存放位置索引标示图</caption>';
				table+='<tr style="font-size:15px;text-aligh:center"><td style="text-align: center" rowspan="3"  >组号</td>' +
						'<td style="text-align: center"  >列号</td><td style="text-align: center" colspan="8" id="liehao_number">'+column_number+'</td></tr>';
				table+='<tr style="font-size:15px;text-aligh:center"><td style="text-align: center"  >AB面</td>' +
						'<td  style="text-align: center" colspan="4" >A</td><td  style="text-align: center" colspan="4">B</td></tr>';
				table+='<tr style="font-size:15px;text-aligh:center"><td style="text-align: center" width="45px">节号隔号</td>' +
						'<td  style="text-align: center" >1</td><td  style="text-align: center" >2</td>' +
						'<td  style="text-align: center" >3</td><td  style="text-align: center" >4</td>' +
						'<td  style="text-align: center" >1</td><td  style="text-align: center" >2</td>' +
						'<td  style="text-align: center" >3</td><td  style="text-align: center" >4</td></tr>';

				table+='<tr style="font-size:15px;text-aligh:center"><td style="text-align: center" rowspan="6"  id="zuhao_number">'+group_number+'</td>' +
						'<td style="text-align: center" >1</td>' +
						'<td  style="text-align: center" ><input id="input11" type="text" style="width: 100px"></input></td>' +
						'<td  style="text-align: center" ><input id="input21" type="text" style="width: 100px"></input></td>' +
						'<td  style="text-align: center" ><input id="input31" type="text" style="width: 100px"></input></td>' +
						'<td  style="text-align: center" ><input id="input41" type="text" style="width: 100px"></input></td>' +
						'<td  style="text-align: center" ><input id="input51" type="text" style="width: 100px"></input></td>' +
						'<td  style="text-align: center" ><input id="input61" type="text" style="width: 100px"></input></td>' +
						'<td  style="text-align: center" ><input id="input71" type="text" style="width: 100px"></input></td>' +
						'<td  style="text-align: center" ><input id="input81" type="text" style="width: 100px"></input></td>' +
						'</tr>';
				table+='<tr style="font-size:15px;text-aligh:center">' +
						'<td style="text-align: center" >2</td>' +
						'<td  style="text-align: center" ><input id="input12" type="text" style="width: 100px"></input></td>' +
						'<td  style="text-align: center" ><input id="input22" type="text" style="width: 100px"></input></td>' +
						'<td  style="text-align: center" ><input id="input32" type="text" style="width: 100px"></input></td>' +
						'<td  style="text-align: center" ><input id="input42" type="text" style="width: 100px"></input></td>' +
						'<td  style="text-align: center" ><input id="input52" type="text" style="width: 100px"></input></td>' +
						'<td  style="text-align: center" ><input id="input62" type="text" style="width: 100px"></input></td>' +
						'<td  style="text-align: center" ><input id="input72" type="text" style="width: 100px"></input></td>' +
						'<td  style="text-align: center" ><input id="input82" type="text" style="width: 100px"></input></td>' +
						'</tr>';
				table+='<tr style="font-size:15px;text-aligh:center">' +
						'<td style="text-align: center" >3</td>' +
						'<td  style="text-align: center" ><input id="input13" type="text" style="width: 100px"></input></td>' +
						'<td  style="text-align: center" ><input id="input23" type="text" style="width: 100px"></input></td>' +
						'<td  style="text-align: center" ><input id="input33" type="text" style="width: 100px"></input></td>' +
						'<td  style="text-align: center" ><input id="input43" type="text" style="width: 100px"></input></td>' +
						'<td  style="text-align: center" ><input id="input53" type="text" style="width: 100px"></input></td>' +
						'<td  style="text-align: center" ><input id="input63" type="text" style="width: 100px"></input></td>' +
						'<td  style="text-align: center" ><input id="input73" type="text" style="width: 100px"></input></td>' +
						'<td  style="text-align: center" ><input id="input83" type="text" style="width: 100px"></input></td>' +
						'</tr>';
				table+='<tr style="font-size:15px;text-aligh:center">' +
						'<td style="text-align: center" >4</td>' +
						'<td  style="text-align: center" ><input id="input14" type="text" style="width: 100px"></input></td>' +
						'<td  style="text-align: center" ><input id="input24" type="text" style="width: 100px"></input></td>' +
						'<td  style="text-align: center" ><input id="input34" type="text" style="width: 100px"></input></td>' +
						'<td  style="text-align: center" ><input id="input44" type="text" style="width: 100px"></input></td>' +
						'<td  style="text-align: center" ><input id="input54" type="text" style="width: 100px"></input></td>' +
						'<td  style="text-align: center" ><input id="input64" type="text" style="width: 100px"></input></td>' +
						'<td  style="text-align: center" ><input id="input74" type="text" style="width: 100px"></input></td>' +
						'<td  style="text-align: center" ><input id="input84" type="text" style="width: 100px"></input></td>' +
						'</tr>';
				table+='<tr style="font-size:15px;text-aligh:center">' +
						'<td style="text-align: center" >5</td>' +
						'<td  style="text-align: center" ><input id="input15"  type="text" style="width: 100px"></input></td>' +
						'<td  style="text-align: center" ><input id="input25" type="text" style="width: 100px"></input></td>' +
						'<td  style="text-align: center" ><input id="input35" type="text" style="width: 100px"></input></td>' +
						'<td  style="text-align: center" ><input id="input45" type="text" style="width: 100px"></input></td>' +
						'<td  style="text-align: center" ><input id="input55" type="text" style="width: 100px"></input></td>' +
						'<td  style="text-align: center" ><input id="input65" type="text" style="width: 100px"></input></td>' +
						'<td  style="text-align: center" ><input id="input75" type="text" style="width: 100px"></input></td>' +
						'<td  style="text-align: center" ><input id="input85" type="text" style="width: 100px"></input></td>' +
						'</tr>';
				table+='<tr style="font-size:15px;text-aligh:center">' +
						'<td style="text-align: center" >6</td>' +
						'<td  style="text-align: center" ><input id="input16" type="text" style="width: 100px"></input></td>' +
						'<td  style="text-align: center" ><input id="input26" type="text" style="width: 100px"></input></td>' +
						'<td  style="text-align: center" ><input id="input36" type="text" style="width: 100px"></input></td>' +
						'<td  style="text-align: center" ><input id="input46" type="text" style="width: 100px"></input></td>' +
						'<td  style="text-align: center" ><input id="input56" type="text" style="width: 100px"></input></td>' +
						'<td  style="text-align: center" ><input id="input66" type="text" style="width: 100px"></input></td>' +
						'<td  style="text-align: center" ><input id="input76" type="text" style="width: 100px"></input></td>' +
						'<td  style="text-align: center" ><input id="input86" type="text" style="width: 100px"></input></td>' +
						'</tr>';
				table+='</table>';




				document.getElementById('_div_tips').innerHTML=table;
				grid_nd.doLayout();

			}
			//保存新建的存址信息
			function save_location() {

				//弹出表单，然后填写表单
				_w_location_form.show();
			}
			function _w_location_load(){
				var time = (new Date).getTime();
				var yesday = new Date(time); // 获取的是前一天日期
				yesday = yesday.getFullYear() + "-" + (yesday.getMonth()> 9 ? (yesday.getMonth() + 1) : "0" + (yesday.getMonth() + 1)) + "-" +(yesday.getDate()> 9 ? (yesday.getDate()) : "0" + (yesday.getDate()));
				_f_location_form.getForm().findField('archive_storehouse').setValue(Ext.getCmp("archive_storehouse").getValue());
				_f_location_form.getForm().findField('zuhao').setValue(document.getElementById("liehao_number").innerHTML);
				_f_location_form.getForm().findField('liehao').setValue(document.getElementById("zuhao_number").innerHTML);
				_f_location_form.getForm().findField('lrr').setValue("<%=session.getAttribute("user")%>");
				_f_location_form.getForm().findField('lrsj').setValue(yesday);
				AOS.ajax({
					params:{name_:'存址流水号'},
					url:'calcId.jhtml',
					ok:function(data){
						//设计一个随机数编号
						_f_location_form.getForm().findField('location_number').setValue(data.appmsg);
					}
				});
			}
			function  _w_location_form_add(){
				var arr=new Array();
				var  k=0;
				var location_table=document.getElementById("table");
				var archive_storehouse= Ext.getCmp("archive_storehouse").getValue();
				//列号：第一行第三列的值
				var column_number= document.getElementById("liehao_number").innerHTML;
				//组号：第四行第一列的值
				var group_number= document.getElementById("zuhao_number").innerHTML;
				//循环迭代进行组合存址信息(A面)
				for(var i=1;i<=4;i++){
					for(var j=1;j<=6;j++){
						var cz_dh=archive_storehouse+"-"+column_number+"-"+group_number+"-"+"A"+"-"+i+"-"+j+"-"+document.getElementById("input"+i+""+j+"").value;
						arr[k]=cz_dh;
						k++;
					}
				}
				//循环迭代进行组合存址信息(B面)
				for(var i=5;i<=8;i++){
					for(var j=1;j<=6;j++){
						var cz_dh=archive_storehouse+"-"+column_number+"-"+group_number+"-"+"B"+"-"+i+"-"+j+"-"+document.getElementById("input"+i+""+j+"").value;
						arr[k]=cz_dh;
						k++;
					}
				}
				var strs=arr.join(',');
				AOS.ajax({
					params:{arr:strs,archive_storehouse:archive_storehouse},
					forms:_f_location_form,
					traditional:true,
					url : 'addlocation.jhtml',
					ok : function(data) {
						if (data.appcode === 1) {
							AOS.tip("保存成功!");
							_w_location_form.hide();
						}else{
							AOS.tip("保存失败!");
						}
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
			//得到操作日志
			function get_log(){
				_w_category_log.show();
			}
			function _w_log_load(){
				_g_category_log_store.reload();
			}
		</script>
	</aos:onready>
	<script type="text/javascript">
	</script>
</aos:html>
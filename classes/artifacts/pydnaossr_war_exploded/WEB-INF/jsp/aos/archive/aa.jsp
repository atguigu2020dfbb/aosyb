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
<aos:panel id="mainpanel" layout="card" autoShow="true" title="向导" center="true" width="1000" height="500" y="50" x="50">
		<aos:formpanel id="_f_info1" layout="column" labelWidth="80" padding="10 25 0 0" columnWidth="1" >
				<aos:textfield fieldLabel="姓名" name="name_" columnWidth="0.5" allowBlank="false" />
				<aos:numberfield fieldLabel="年龄" name="age_" minWidth="1" maxValue="120" columnWidth="0.5" />
				<aos:textfield fieldLabel="身份证号" name="id_no_" columnWidth="0.5" />
				<aos:textfield fieldLabel="所属银行" name="org_id_" columnWidth="0.5" />
				<aos:numberfield fieldLabel="可用余额" name="balance_" columnWidth="0.5" />
				<aos:numberfield fieldLabel="信用额度" name="credit_line_" columnWidth="0.5" value="100000" readOnly="true" />
				<aos:textareafield fieldLabel="兴趣爱好" name="interests_" columnWidth="1" />
				<aos:htmleditor fieldLabel="备注" name="remark_" columnWidth="1" />
				<aos:docked dock="bottom" ui="footer">
					<aos:dockeditem xtype="tbfill" />
					<aos:dockeditem xtype="button" text="保存数据" icon="ok.png" />

					<aos:dockeditem xtype="button" text="删除" icon="del.png" />

					<aos:dockeditem xtype="tbfill" />
				</aos:docked>
			</aos:formpanel>
			
			
		<aos:formpanel id="_f_info2" layout="column" autoScroll="true" labelWidth="70"  >
				<aos:fieldset title="请查询" columnWidth="1">
					<aos:textfield fieldLabel="卡号" name="card" allowBlank="false" columnWidth="0.4" />
					<aos:button text="查询" margin="0 0 0 10" icon="query.png" />
				</aos:fieldset>
				<aos:fieldset title="持卡人信息" columnWidth="1" checkboxToggle="true" collapsed="false">
					<aos:textfield fieldLabel="姓名" name="name" columnWidth="0.5" />
					<aos:textfield fieldLabel="身份证号" name="name" columnWidth="0.49" />
					<aos:textfield fieldLabel="现住址" name="name" columnWidth="0.5" />
					<aos:textfield fieldLabel="户口地" name="name" columnWidth="0.49" />
				</aos:fieldset>
				<aos:fieldset title="发卡行信息(复杂表单布局)" columnWidth="1">
					<aos:textfield fieldLabel="支行名称" name="bankName" columnWidth="0.99" />
					<aos:rowset2>
						<aos:numberfield fieldLabel="上班时间" name="satrt1" value="8" columnWidth="0.2" />
						<aos:displayfield value="点" columnWidth="0.02" />
						<aos:numberfield name="name1" value="30" columnWidth="0.1" />
						<aos:displayfield value="分 (强制换行)" columnWidth="0.3" />
					</aos:rowset2>
					<aos:textfield fieldLabel="邮编" name="bankName" columnWidth="0.4" />
					<aos:textfield fieldLabel="地址" name="bankName" columnWidth="0.59" />
				</aos:fieldset>
			</aos:formpanel>
		
		<aos:docked dock="bottom" ui="footer">
			<aos:dockeditem xtype="tbfill" />
			<aos:dockeditem onclick="_f_prev" text="上一步" icon="icon80.png" />
			<aos:dockeditem onclick="_f_next" text="下一步"  icon="icon82.png" />
			<aos:dockeditem onclick="#_w_data_i.hide();" text="关闭"
				icon="close.png" />
		</aos:docked>
	</aos:panel>
	<script>
	function _f_prev(){
	
	var aa=[{
					id:'card-0',
					html:'<h1>Welcome to the Wizard!</h1>'
				},
				{
					id:'card-1',
					html:'<h1>2222222</h1>'
				}
				
				];
			Ext.getCmp('mainpanel').add(aa);	
				
	}
	function _f_next(){
	
	var layout = Ext.getCmp('mainpanel').getLayout(); 
	//layout.setActiveItem(1);
	
	//alert(layout);   
        layout["next"]();     
	
	}
	function fn_ok(){
	
	AOS.ajax({
                 url: 'openFileDbclick.jhtml',
                 ok: function (data) {
                 }
             });
	}
	</script>
</aos:onready>
</aos:html>
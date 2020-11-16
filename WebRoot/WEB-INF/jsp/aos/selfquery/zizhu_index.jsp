<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="/WEB-INF/tld/aos.tld" prefix="aos"%>
<%
	String context = request.getContextPath();
	String basePath = request.getScheme()
			+ "://" + request.getServerName()
			+ ":"
			+ request.getServerPort()
			+ context
			+ "/";
	System.out.print(basePath);
%>
<aos:html>
<aos:head title="自助查档">
	<aos:include lib="ext" />
	<aos:base href="utilization/data" />
</aos:head>
<aos:body backGround="true">
	<div id="_div_photo2" class="x-hidden" align="center">
		<img id="_img_photo2" class="app_cursor_pointer" src="${cxt}/static/image/empty_head_photo.png" width="200"
			 onclick="read_photo()" height="200">
	</div>
	<div id="_div_photo" class="x-hidden" align="center">
		<img id="PhotoID" name="PhotoID" class="app_cursor_pointer"  style="width:96px; height:118px;margin-top:30px">
	</div>
</aos:body>
<aos:onready>
	<aos:viewport layout="fit">
			<aos:window title="自助查档系统" id="_w_readCard" width="850" height="400" layout="fit" autoShow="true"
						modal="false" center="true" maximizable="true" closable="false"  >
				<aos:formpanel id="_f_apply"  width="700" layout="column" >
					<aos:fieldset title="基本信息" labelWidth="70" columnWidth="0.65" margin="0 10 0 0" height="300">
						<aos:textfield name="zzbh" id="zzbh" fieldLabel="自助编号" readOnly="true" columnWidth="0.99"/>
						<aos:textfield name="xm" id="xm" fieldLabel="姓名" columnWidth="0.99"/>
						<aos:textfield name="xb" id="xb" fieldLabel="性别" columnWidth="0.99"/>
						<aos:textfield name="mzgj" id="mzgj" fieldLabel="民族或国籍" columnWidth="0.99"/>
						<aos:textfield name="cs" id="cs" fieldLabel="出生" columnWidth="0.99"/>
						<aos:textfield name="dz" id="dz" fieldLabel="地址" columnWidth="0.99"/>
						<aos:textfield name="sfzh" id="sfzh" fieldLabel="身份证号" columnWidth="0.99"/>
						<aos:textfield name="qfjg" id="qfjg" fieldLabel="签发机关" columnWidth="0.99"/>
						<aos:hiddenfield name="tpsj" id="tpsj" fieldLabel="图像64码" columnWidth="0.99"/>
					</aos:fieldset>
					<aos:fieldset title="图像" labelWidth="70" columnWidth="0.35" contentEl="_div_photo"  height="300" >
					</aos:fieldset>
				</aos:formpanel>
				<aos:docked dock="bottom" ui="footer">
					<aos:dockeditem xtype="tbfill" />
					<aos:dockeditem xtype="button" text="读身份证" icon="query.png"/>
					<aos:dockeditem xtype="button" text="确定" icon="ok.png" onclick="select_archive"/>
					<aos:dockeditem text="关闭" icon="close.png" />
				</aos:docked>
			</aos:window>

	</aos:viewport>
	<script type="text/javascript">
		window.onload=function(){
			//根据tree名称得到对应的部门名称
			AOS.ajax({
				params:{name_:'自助查档流水号'},
				url:'zizhucalcId.jhtml',
				ok:function(data){
					//设计一个随机数编号
					var date=new Date();
					_f_apply.form.findField("zzbh").setValue("ZZ"+date.getFullYear()+data.index);
				}
			});
		}
	function select_archive() {
		//先保存数据库登录信心，在急性跳转
		AOS.ajax({
			forms:_f_apply,
			url:'saveZiZhuMessage.jhtml',
			ok:function(data){
				if(data.appcode===1){
					var user = _f_apply.form.findField("xm").getValue();
					window.open("<%=context%>/utilization/data/zizhu_initData.jhtml?user=" + user );
					}else{
					AOS.tip("请检查登录信息!");
				}
			}
		});

	}
	</script>
</aos:onready>
</aos:html>
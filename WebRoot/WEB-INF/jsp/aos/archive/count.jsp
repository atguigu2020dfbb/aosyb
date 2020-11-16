<%@page import="com.mysql.jdbc.Field"%>
<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="/WEB-INF/tld/aos.tld" prefix="aos"%>
<%@ taglib uri="/WEB-INF/tld/cll.tld" prefix="c"%>
<aos:html>
	<aos:head title="档案汇总">
		<aos:include lib="ext,swfupload" />
		<aos:base href="archive/count" />
	</aos:head>
	<aos:body>
		<div name="div" id="div" ></div>
	</aos:body>
	<script>
		function _g_count_query1() {
			//走后台
			htmlcount();
		}
	</script>
	<aos:onready>
		<aos:viewport layout="border">
			<aos:formpanel id="_f_count" layout="column" labelWidth="70"
						   header="false" region="north" border="false">
				<aos:docked forceBoder="0 0 1 0">
					<aos:dockeditem xtype="tbtext" text="档案汇总" />
				</aos:docked>
				<aos:combobox name="year" id="year" fieldLabel="年度" dicField="nd"
							  columnWidth="0.33" />
				<aos:dockeditem xtype="button" text="打印预览" onclick="_g_count_query"
								icon="query.png" />
				<aos:dockeditem xtype="button" text="清空"
								onclick="AOS.reset(_f_count);" icon="refresh.png" />
			</aos:formpanel>
		</aos:viewport>
		<script type="text/javascript">
			//调用打印
			function _g_count_query() {
				AOS.ajax({
					forms : _f_count,
					url : 'htmlcount.jhtml',
					ok : function(data) {
						/*var mm="";
                        var str=data.appmsg;
                        var s=str.split(",");
                        //弹出新页面
                        for(var i=0;i<s.length;i++){
                            mm+="mm"+(i+1)+"="+s[i]+"&";
                        }
                        mm=mm.substring(0,mm.length-1);*/
						//window.parent.fnaddtab('','报表预览','archive/count/look.jhtml?'+mm);
						window.parent.fnaddtab('','报表预览','archive/count/look.jhtml');

					}
				});
			}
			//456456456
		</script>
	</aos:onready>
</aos:html>
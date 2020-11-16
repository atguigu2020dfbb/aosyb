<%@page import="cn.osworks.aos.core.typewrap.Dto"%>
<%@page import="java.util.List"%>
<%@page import="com.mysql.jdbc.Field"%>
<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="/WEB-INF/tld/aos.tld" prefix="aos"%>
<%@ taglib uri="/WEB-INF/tld/cll.tld" prefix="c"%>
<%
	String path = request.getContextPath();
%>
<aos:html>
<aos:head title="数据备份与恢复">
	<aos:include lib="ext,swfupload" />
	<aos:base href="archive/record" />
	<script type="text/javascript" src="<%=path %>/static/flexpaper/jquery.min.js"></script>
</aos:head>
<aos:body>
	
</aos:body>
<aos:onready>
	<aos:viewport>
		<aos:panel autoShow="true" center="true" width="300">
			<aos:formpanel id="_form1" width="150" layout="column" labelWidth="80" title="备份与还原" bodyBorder="1 0 0 0">
				<aos:docked dock="bottom" ui="footer" padding="0 0 5 0">
					<aos:dockeditem xtype="tbfill" />
					<aos:dockeditem xtype="button" text="备份" onclick="backup_record" icon="ok.png" />
					<aos:dockeditem xtype="button" text="还原" onclick="restore_record" icon="refresh.png" />
					<aos:dockeditem xtype="tbfill" />
				</aos:docked>
			</aos:formpanel>
		</aos:panel>
	</aos:viewport>
	<script type="text/javascript">
	   //数据库备份
	   function backup_record(){
	  // AOS.file('backupData.jhtml');
		   AOS.ajax({
	            url: 'backupData.jhtml',         
	            ok: function (data) {
	            	if(data.appcode===1){
	            	AOS.file("download.jhtml?filepath="+data.filepath+"&name="+data.name+"");
	            	}
	            }
	        });
	   }	
	   //数据库还原
	   function restore_record(){
		   AOS.ajax({
	            url: 'restoreData.jhtml',         
	            ok: function (data) {
	            	if(data.appcode===1){
	            		AOS.tip("还原成功,请重新启动服务器！");
	            		return;
	            	}
	            	if(data.appcode===-1){
	            		AOS.tip("还原失败");
	            		return;
	            	}
	            	if(data.appcode===-2){
	            		AOS.tip("请在服务器机器上完成数据库的还原操作!");
	            		return;
	            	}
	            }
	        }); 
	   }
	</script>
</aos:onready>
</aos:html>


<%@ page contentType="text/html; charset=utf-8" isELIgnored="false"%>
<%@ taglib uri="/WEB-INF/tld/aos.tld" prefix="aos"%>
<%@ taglib uri="/WEB-INF/tld/cll.tld" prefix="c"%>
<%
	String path = request.getContextPath();
	Object fieldDtos = request.getAttribute("fieldDtos");
	Object listtablename = request.getAttribute("listtablename");
%>
<aos:html>
<aos:head title="档案利用">
	<aos:include lib="ext" />
	<aos:base href="archive/sharp" />
</aos:head>
<aos:body>
</aos:body>
<aos:onready>
	<aos:viewport layout="fit">
		<aos:gridpanel id="_g_data" url="listAccounts.jhtml" region="center"
			autoScroll="true" pageSize="20" enableLocking="true"
			onrender="_w_tablename_show">
			<aos:docked forceBoder="0 0 1 0">
				<aos:dockeditem xtype="tbtext" text="档案利用" />
				<aos:hiddenfield name="tablename" id="tablename" />
				<aos:combobox name="listTablename"
					fields="[ 'tablename', 'tabledesc']" fieldLabel="数据表"
					id="listTablename" columnWidth="0.3" url="listTablename.jhtml"
					displayField="tabledesc" valueField="tablename" allowBlank="false" />
				<aos:dockeditem text="查询" icon="query.png" onclick="_w_tablename_init" />
				<aos:dockeditem text="借阅" icon="more/applications-science-3.png"
					onclick="_w_borrow_read" />
				<aos:dockeditem text="归还" icon="basket.png"
					onclick="_w_borrow_return" />
				<aos:dockeditem text="历史记录" icon="more/view-history.png"
					onclick="_w_borrow_history" />
			</aos:docked>
			<aos:column type="rowno" />
			<aos:selmodel type="row" mode="multi" />
			<aos:column dataIndex="id_" header="流水号" hidden="true" />
			<aos:column dataIndex="_jy" header="借阅" rendererFn="fn_jy_render" />
			<c:forEach var="field" items="${fieldDtos}">
				<aos:column dataIndex="${field.fieldenname }"
					header="${field.fieldcnname }" rendererField="field_type_" />
			</c:forEach>
			<aos:column header="" flex="1" />
		</aos:gridpanel>
		<aos:window id="_w_borrow" title="借阅信息1" height="250"
			autoScroll="true" onshow="_load_borrow">
			<aos:formpanel id="_f_borrow" width="400" layout="column"
				labelWidth="75">
				<aos:textfield fieldLabel="门类名称" name="tablename" columnWidth="1.0"
					readOnly="true" />
				<aos:textfield fieldLabel="档案号" name="archivenumber"
					columnWidth="1.0" readOnly="true" />
				<aos:datefield name="borrowData" fieldLabel="借阅日期" format="Y-m-d"
					columnWidth="1.0" allowBlank="false" />
				<aos:textfield fieldLabel="用户名" name="user" readOnly="true"
					columnWidth="1.0" />
				<!-- 显示档案的状态，红色字体提示档案借阅过期，白色字体未借阅，绿色字体提示已借阅 -->
				<aos:textfield fieldLabel="档案状态" name="archivestate" readOnly="true"
					columnWidth="1.0" />
				<aos:combobox fieldLabel="借阅天数" id="borrowday" name="borrowday"
					columnWidth="1.0" allowBlank="false">
					<aos:option value="5" display="5" />
					<aos:option value="10" display="10" />
					<aos:option value="15" display="15" />
					<aos:option value="20" display="20" />
					<aos:option value="25" display="25" />
					<aos:option value="30" display="30" />
				</aos:combobox>
			</aos:formpanel>
			<aos:docked dock="bottom" ui="footer">
				<aos:dockeditem xtype="tbfill" />
				<aos:dockeditem text="保存" onclick="_w_borrow_load" icon="ok.png" />
				<aos:dockeditem onclick="#_w_borrow.hide();" text="关闭"
					icon="close.png" />
			</aos:docked>
		</aos:window>
		<aos:window id="_w_relet" title="借阅信息2" height="250" autoScroll="true"
			onshow="_load_relet">
			<aos:formpanel id="_f_relet" width="400" layout="column"
				labelWidth="75">
				<aos:textfield fieldLabel="门类名称" name="tablename" readOnly="true"
					columnWidth="1.0" />
				<aos:textfield fieldLabel="档案号" name="archivenumber" readOnly="true"
					columnWidth="1.0" />
				<aos:datefield name="borrowData" fieldLabel="借阅日期" format="Y-m-d"
					columnWidth="1.0" readOnly="true" />
				<aos:textfield fieldLabel="用户名" name="user" readOnly="true"
					columnWidth="1.0" />
				<!-- 显示档案的状态，红色字体提示档案借阅过期，白色字体未借阅，绿色字体提示已借阅 -->
				<aos:textfield fieldLabel="档案状态" name="archivestate" readOnly="true"
					columnWidth="1.0" />
				<aos:combobox fieldLabel="续租天数" name="reletday" id="reletday"
					columnWidth="1.0" allowBlank="false">
					<aos:option value="5" display="5" />
					<aos:option value="10" display="10" />
					<aos:option value="15" display="15" />
					<aos:option value="20" display="20" />
					<aos:option value="25" display="25" />
					<aos:option value="30" display="30" />
				</aos:combobox>

			</aos:formpanel>
			<aos:docked dock="bottom" ui="footer">
				<aos:dockeditem xtype="tbfill" />
				<aos:dockeditem text="保存" onclick="_w_relet_load" icon="ok.png" />
				<aos:dockeditem onclick="#_w_relet.hide();" text="关闭"
					icon="close.png" />
			</aos:docked>
		</aos:window>
		<aos:window id="_w_jy" title="归还列表" autoScroll="true"  height="500">
		<aos:formpanel id="_f_jy" width="500" layout="anchor"
				labelWidth="90">
		<aos:combobox fieldLabel="数据表模板" name="tableTemplate"
					fields="[ 'value', 'display', 'tablename']" id="tableTemplate"
					editable="false" columnWidth="0.5" url="listComboBoxid.jhtml"
					onselect="loadDemo" displayField="display" valueField="tablename"/>
		</aos:formpanel>
			<aos:gridpanel id="_g_jy" region="east" url="listjy.jhtml" hidePagebar="true"
				split="true" splitterBorder="0 1 0 1" width="800" pageSize="10">
				<aos:docked>
					<aos:dockeditem xtype="tbtext" text="借阅列表" />
				</aos:docked>
				<aos:column type="rowno" />
				<aos:column header="流水号" dataIndex="id_" hidden="true" />
				<aos:column header="用户" dataIndex="users" width="90" />
				<aos:column header="借阅时间" dataIndex="jytime" width="90" />
				<aos:column header="档案号" dataIndex="archive_id" width="90" />
				<aos:column header="操作门类" dataIndex="tablename" width="90" />
				<aos:column header="借阅天数" dataIndex="jyday" width="90" />
				<aos:column header="借阅状态" dataIndex="archivestate" width="90" />
				<aos:column header="是否归还" dataIndex="gh" width="90" />
			</aos:gridpanel>
			<aos:docked dock="bottom" ui="footer">
				<aos:dockeditem xtype="tbfill" />
				<aos:dockeditem onclick="_f_Excel_jy" text="导出Excel" icon="ok.png" />
				<aos:dockeditem onclick="#_w_jy.close();" text="关闭"
					icon="close.png" />
			</aos:docked>
		</aos:window>
	</aos:viewport>
	<script>
	//跳转主页
	function _w_tablename_init(){	
		// alert(listtablename);
		var listTablename = Ext.getCmp("listTablename").value;
		var tablenamedesc = Ext.getCmp("listTablename").getRawValue();
		window.location.href="initInput.jhtml?listtablename="+listTablename+"&tablenamedesc="+encodeURI(encodeURI(tablenamedesc));
		
	}
	//点击查询按钮，先进入查询表头信息
		function _w_tablename_show(){	
			var listTablename ="<%=request.getParameter("listtablename")%>";
			var tablenamedesc ="<%=request.getAttribute("tablenamedesc")%>";
			var tablename=Ext.getCmp("tablename").getValue();
			if(listTablename==null||listTablename==""||listTablename=="null"){
				if(tablename!=null&&tablename!=""&&tablename!="null"){
					listTablename=tablename;
				}else{
					return;
				}
			}
		var params = {
				listtablename : listTablename
			};
			//这个Store的命名规则为：表格ID+"_store"。
			_g_data_store.getProxy().extraParams = params;
			_g_data_store.load();
			//让指定文本框赋值
			Ext.getCmp("listTablename").setValue(listTablename);
			Ext.getCmp("listTablename").setRawValue(tablenamedesc);
			Ext.getCmp("tablename").setValue(listTablename);
	}
		//_path列转换
		function fn_jy_render(value, metaData, record, rowIndex, colIndex,
					store) {
				if (value >= 1) {
					return '<a><font color="red">已借阅</font></a>';
				} else {
					return '<a><font color="green">未借阅</font></a>';
				}
			}
	//借阅
	function _w_borrow_read(){
         var _jy = AOS.selection(_g_data, '_jy');
         var selection = AOS.selection(_g_data, 'id_');
         if (AOS.empty(selection)) {
             AOS.tip('借阅前请先选中数据。');
             return;
         }       
         _jy=_jy.split(",")[0];
         if(_jy=="1"){
        	 //展开借阅窗口,相当于表单        	 
             _w_relet.show();
         }else{
        	//展开借阅窗口,相当于表单
             _w_borrow.show(); 
         }       
	}
	//加载数据
	function _load_borrow(){
		 var _jy = AOS.selection(_g_data, '_jy');
		 //门类数据表
    	 var listTablename ="<%=request.getParameter("listtablename")%>";
    	 //得到档案号
    	 var id_=AOS.selectone(_g_data).data.id_; 
		 
    	 //得到当前用户
 		AOS.ajax({
              url: 'jymessage.jhtml',
              params:{id_:id_.split(",")[0],listTablename:listTablename,_jy:_jy.split(",")[0]},
              method:'post',           
              ok: function (data) { 
            	  _f_borrow.form.setValues(data);
              }
          });		
	}
	//加载数据
	function _load_relet(){
		var _jy = AOS.selection(_g_data, '_jy');
		 //门类数据表
    	 var listTablename ="<%=request.getParameter("listtablename")%>";
    	 //得到档案号
    	 var id_=AOS.selectone(_g_data).data.id_;
		//得到当前用户
 		AOS.ajax({
              url: 'jymessage.jhtml',
              params:{id_:id_.split(",")[0],listTablename:listTablename,_jy:_jy.split(",")[0]},
              method:'post',           
              ok: function (data) { 
            	  _f_relet.form.setValues(data);
              }
          });
	}
	//档案借阅
	function _w_borrow_load(){
		//数据借阅
 		AOS.ajax({
              url: 'savejy.jhtml',
              forms:_f_borrow,
              method:'post',           
              ok: function (data) { 
            	  if(data.appcode === 1){
                  	//让用户重新选择
                  	AOS.tip("操作成功!");
                  	_w_borrow.hide();
                    _g_data_store.reload();
                  }else if(data.appcode === -2){
                	 AOS.tip("档案已被借阅!");
                  }else{
                	  AOS.tip("操作失败!"); 
                  }
              }
          });
	}
	//档案借阅
	function _w_relet_load(){
		//数据借阅
 		AOS.ajax({
              url: 'savejy.jhtml',
              forms:_f_relet,
              method:'post',           
              ok: function (data) { 
            	  if(data.appcode === -1){
                    	//让用户重新选择
                     AOS.tip("操作成功!");
                     _w_relet.hide();
                     _g_data_store.reload();
                    }else{
                  	 AOS.tip("操作失败!");
                    }
              }
          });
	}
	function _w_borrow_return(){
		var _jy = AOS.selection(_g_data, '_jy');
        var selection = AOS.selection(_g_data, 'id_');
	    //门类数据表
	 	 var listTablename ="<%=request.getParameter("listtablename")%>";
	 	 //得到档案号
	 	 var id_=AOS.selectone(_g_data).data.id_;
        if (AOS.empty(selection)) {
            AOS.tip('归还前请先选中数据。');
            return;
        }    
        if (_jy.split(",")[0]!="1") {
            AOS.tip('图书未借阅，无需归还！');
            return;
        } 
        var msg = AOS.merge('确认要归还选中的[{0}]个用户数据吗？', AOS.rows(_g_data));
        AOS.confirm(msg, function (btn) {
            if (btn === 'cancel') {
                AOS.tip('归还操作被取消。');
                return;
            }else{
            	AOS.ajax({
                    url: 'returnjy.jhtml',
                    params:{listTablename:listTablename,id_:id_},
                    method:'post',           
                    ok: function (data) { 
                  	  if(data.appcode === 1){
                          	//让用户重新选择
                           AOS.tip("归还成功!");
                           _g_data_store.reload();
                          }else if(data.appcode===-2){
                        	 AOS.tip("没有权限!");
                          }else{
                        	  AOS.tip("归还失败!");
                          }
                    }
                });
            }
        });
        
	}
	function _w_borrow_history(){			
		AOS.reset(_f_jy);
		_g_jy_store.removeAll();
		_w_jy.show();	
	}
	 function loadDemo(){
	      var tablename=Ext.getCmp('tableTemplate').value;
	      var params = {
					tablename: tablename
				};
				 _g_jy_store.getProxy().extraParams = params;
				 _g_jy_store.load();
	      
	      } 
	function _f_Excel_jy(){
		//导出日志
			AOS.ajax({
				url : 'fillReport.jhtml',
				ok : function(data) {
					AOS.file('${cxt}/report/xls2.jhtml');
				}
			});
	}
		
	</script>
</aos:onready>
</aos:html>
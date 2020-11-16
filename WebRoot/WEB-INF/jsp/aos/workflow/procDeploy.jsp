<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="/WEB-INF/tld/aos.tld" prefix="aos"%>
<aos:html>
<aos:head title="流程部署">
	<aos:include lib="ext" />
	<aos:base href="workflow/procDeploy" />
</aos:head>
<aos:body>
	<div id="_div_diagram" class="x-hidden" align="center">
		<img id="_img_diagram" style="vertical-align: middle;" />
	</div>
</aos:body>
<aos:onready>
	<aos:viewport layout="fit">
		<aos:gridpanel id="_g_proc" onrender="_g_proc_query" pageType="client">
			<aos:selmodel type="checkbox" mode="multi" />
			<aos:column type="rowno" />
			<aos:column header="流程ID" dataIndex="id_" hidden="true" />
			<aos:column header="流程标识" dataIndex="name_" width="100" celltip="true" />
			<aos:column header="流程名称" dataIndex="key_" width="160" celltip="true" />
			<aos:column header="流程版本" dataIndex="version_" width="160" celltip="true" />
			<aos:column header="流程xml" dataIndex="resource_name_" width="180" celltip="true" />
			<aos:column header="流程图片" dataIndex="dgrm_resource_name_" rendererField="procdef_suspension_state_" width="80" />
			<aos:column header="部署时间" dataIndex="deploy_time_" width="120" />
			<aos:column header="操作3" type="action" dataIndex="" width="150"  >
				<aos:action handler="fn_preview"  icon="edit.png" tooltip="预览" />
			</aos:column>
		</aos:gridpanel>
	</aos:viewport>

	<script type="text/javascript">
	    
	  // 查询流程列表 客户端分页
		function _g_proc_query() {
			var params = {};
			AOS.mask('正在读取数据, 请稍候...', _g_proc);
			AOS.ajax({
				wait : false,
				url : 'listProcs.jhtml',
				params : params,
				ok : function(data) {
					_g_proc_store.proxy.data = data;
					_g_proc_store.load({
						callback : function() {
							AOS.unmask();
						}
					});
				}
			});
		}
	
	   //显示部署窗口
	   function _w_deploy_show(){
		   AOS.reset(_f_deploy);
	       _w_deploy.show();
	   }
	    
	
	   //部署(注：文件上传操作不能使用AOS.Ajax()方法，只能使用Form自带的submit()函数)。
	   function _f_deploy_save() {
	       var form = _f_deploy.getForm();
	       if (!form.isValid()) {
	           return;
	       }
	       AOS.wait();
	       form.submit({
	           url: 'deployProc.jhtml',
	           success: function (form, action) {
	               _w_deploy.hide();
	               AOS.hide();
	               AOS.tip(action.result.appmsg);
	               _g_proc_query();
	           }
	       });
	   }
		
       //关闭流程图窗口
       function _w_diagram_hide() {
           _w_diagram.hide();
           _w_diagram.center();
       }

       //显示流程图窗口
       

       //监听流程图弹出窗口
       function _w_diagram_onshow() {
       	AOS.mask(null,_w_diagram);
       	   var record = AOS.selectone(_g_proc);
           document.getElementById('_img_diagram').src = "${cxt}/workflow/graphByProcdefID.jhtml?proc_def_id_=" + record.data.id_;
           var imgObj = document.getElementById('_img_diagram');
           //图像数据加载完毕
           if(imgObj.complete){
           	AOS.unmask();
           }
           imgObj.onload = function(){
           	AOS.unmask();
               var width = Ext.get('_img_diagram').getWidth() + 100;
               var height = Ext.get('_img_diagram').getHeight() + 65;
               var viewWidth = Ext.getBody().getViewSize().width;
               var viewHeight = Ext.getBody().getViewSize().height;
               width = width > viewWidth ? viewWidth : width;
               height = height > viewHeight ? viewHeight : height;
               var left = (viewWidth - width) / 2;
               var top = (viewHeight - height) / 2;
               _w_diagram.animate({
                   to: {
                       width: width,
                       height: height,
                       top: top,
                       left: left
                   }
               });
           }
       }

       //删除模型
       function _g_proc_del() {
           var rows = AOS.rows(_g_proc);
           if (rows == 0) {
               AOS.tip('删除前请先选中数据。');
               return;
           }
           var msg = AOS.merge('确认要删除选中的[{0}]条模型数据吗？', rows);
           AOS.confirm(msg, function (btn) {
               if (btn === 'cancel') {
                   AOS.tip('删除操作被取消。');
                   return;
               }
               AOS.ajax({
                   url: 'delProcDef.jhtml',
                   params: {
                       aos_rows_: AOS.selection(_g_proc, 'id_')
                   },
                   ok: function (data) {
                       AOS.tip(data.appmsg);
                       _g_proc_query();
                   }
               });
           });
       }
       
    //操作按钮列
   	function fn_status_render(value, metaData, record, rowIndex, colIndex, store){
    	var outHtml = '<input type="button" value="预览" class="cbtn" onclick="fn_preview111();" />'+ 
    	'<input type="button" value="挂起" class="cbtn" onclick="fn_update_status();" /> ' +
    	'<input type="button" value="删除" class="cbtn" onclick="fn_update_status();" />';
    	if(record.data.suspension_state_ === '2'){
    		outHtml = '<input type="button" value="激活" class="cbtn" onclick="fn_update_status();" />';
    	}
   		return outHtml;
   	}
       
     //流程图按钮列
	function fn_diagram_render(value, metaData, record, rowIndex, colIndex, store){
		return '<input type="button" value="流程图" class="cbtn" onclick="fn_showDiagramWidow();" />';
	}
	function fn_preview(){
	//return '<input type="button" value="预览" class="cbtn" onclick="fn_showDiagramWidow();" />';
		var record = AOS.selectone(_g_proc);
          var processDefinitionId = record.data.id_;
          var aa='021926eb-fb48-11e8-8f72-2c6e852d6bbd';
          //var processDefinitionId='process:17:039abdde-fb48-11e8-8f72-2c6e852d6bbd';
          window.parent.fnaddtab('','流程预览','/static/processEditor/diagram-viewer/index.html?processDefinitionId='+processDefinitionId+'&processInstanceId='+aa);
	}
 </script>
</aos:onready>
<script type="text/javascript">
    //显示流程图窗口
    function fn_showDiagramWidow1111(){
    	 Ext.getCmp('_w_diagram').show();
    }
    //修改流程定义状态
    function fn_update_status(){
    	var record = AOS.selectone(_g_proc);
    	AOS.ajax({
            url: 'delProcDef.jhtml',
            params: {
                id_:record.data.id_,
                suspension_state_:record.data.suspension_state_
            },
            ok: function (data) {
                AOS.tip(data.appmsg);
                _g_proc_query();
            }
        });
    }
</script>
</aos:html>
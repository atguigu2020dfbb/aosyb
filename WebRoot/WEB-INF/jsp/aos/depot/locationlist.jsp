<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="/WEB-INF/tld/aos.tld" prefix="aos"%>
<aos:html>
<aos:head title="存址列表">
	<aos:include lib="ext" />
	<aos:base href="depot/location" />
</aos:head>
<aos:body>
</aos:body>
<aos:onready>
	<aos:viewport layout="fit">
		<aos:gridpanel id="_g_location" url="listlocation.jhtml"
			onrender="_g_location_query" onitemdblclick="_w_location_show">
			<aos:docked>
				<aos:dockeditem xtype="tbtext" text="存址列表" />
				<aos:dockeditem xtype="tbseparator" />
				<aos:dockeditem text="查看" icon="folder15.png" onclick="_w_location_show" />
				<aos:dockeditem text="修改" icon="edit.png"
								onclick="fn_edit_location" />
				<aos:dockeditem text="删除" icon="del.png" onclick="fn_remove_location" />
				<aos:dockeditem text="导出Excel" icon="more/document-export-4.png"
					onclick="fn_export_location" />
			</aos:docked>
			<aos:column type="rowno" />
			<aos:column header="流水号" dataIndex="id_" hidden="true" align="center" />
			<aos:column header="档号" dataIndex="dh" width="60"
				align="center" />
			<aos:column header="库房名称" dataIndex="archive_storehouse" width="60"
						align="center" />
			<aos:column header="存址列号" dataIndex="cz_liehao" width="60"
				align="center" />
			<aos:column header="存址组号" dataIndex="cz_zuhao" width="60"
				align="center" />
			<aos:column header="存址AB面" dataIndex="cz_mian" width="60"
				align="center" />
			<aos:column header="存址节号" dataIndex="cz_jiehao" width="60"
				align="center" />
			<aos:column header="存址隔号" dataIndex="cz_gehao" width="60"
				align="center" />
			<aos:column header="名称" dataIndex="cz_name" width="60"
						align="center" />
		</aos:gridpanel>
		<aos:window id="_w_location" title="详情">
			<aos:formpanel id="_f_location" width="400" layout="anchor"
				labelWidth="70" >
				<aos:textfield name="archive_storehouse" fieldLabel="库房名称" readOnly="true" />
				<aos:textfield name="dh" fieldLabel="档号" readOnly="true" />
				<aos:textfield name="cz_liehao" fieldLabel="存址列号" readOnly="true" />
				<aos:textfield name="cz_zuhao" fieldLabel="存址组号" readOnly="true" />
				<aos:textfield name="cz_mian" fieldLabel="存址AB面" readOnly="true" />
				<aos:textfield name="cz_jiehao" fieldLabel="存址节号" readOnly="true" />
				<aos:textfield name="cz_gehao" fieldLabel="存址隔号" readOnly="true" />
				<aos:textfield name="cz_name" fieldLabel="名称" readOnly="true" />
			</aos:formpanel>
			<aos:docked dock="bottom" ui="footer">
				<aos:dockeditem xtype="tbfill" />
				<aos:dockeditem onclick="#_w_location.hide();" text="关闭" icon="close.png" />
			</aos:docked>
		</aos:window>


		<aos:window id="_w_edit_location" title="修改" width="520" height="350"
					autoScroll="true" >
			<aos:formpanel id="_f_edit_location" width="500" layout="anchor">
				<aos:hiddenfield name="id_"/>
				<aos:combobox name="archive_storehouse"  fieldLabel="库房名称" emptyText="请选择..."
							  labelWidth="100" columnWidth="0.45" value="X1" allowBlank="false"  onselect="onblur_qzh">
					<aos:option value="X1" display="现行库房1"/>
					<aos:option value="X2" display="现行库房2"/>
					<aos:option value="X3" display="现行库房3"/>
					<aos:option value="X4" display="现行库房4"/>
					<aos:option value="LS" display="历史库房"/>
					<aos:option value="ZL" display="资料库房"/>
				</aos:combobox>
				<aos:textfield name="cz_liehao" columnWidth="0.45" fieldLabel="存址列号"  allowBlank="false" onblur="onblur_qzh"/>
				<aos:textfield name="cz_zuhao" columnWidth="0.45" fieldLabel="存址组号"   allowBlank="false" onblur="onblur_qzh"/>
				<aos:combobox name="cz_mian"  fieldLabel="存址AB面" emptyText="请选择..." allowBlank="false"
							  labelWidth="100" columnWidth="0.45" value="A"  onselect="onblur_qzh">
					<aos:option value="A" display="A面"/>
					<aos:option value="B" display="B面"/>
				</aos:combobox>
				<aos:textfield name="cz_jiehao" columnWidth="0.45" fieldLabel="存址节号" allowBlank="false" onblur="onblur_qzh"/>
				<aos:textfield name="cz_gehao" columnWidth="0.45"  fieldLabel="存址隔号" allowBlank="false" onblur="onblur_qzh"/>
				<aos:textfield name="cz_name" columnWidth="0.45" fieldLabel="名称" allowBlank="false" onblur="onblur_qzh"/>
				<aos:textfield name="dh" columnWidth="0.45" fieldLabel="档号"  allowBlank="false"/>

			</aos:formpanel>
			<aos:docked dock="bottom" ui="footer">
				<aos:dockeditem xtype="tbfill" />
				<aos:dockeditem onclick="_f_location_edit_from" text="保存" icon="icon82.png" />
				<aos:dockeditem onclick="#_w_edit_location.close();" text="关闭"
								icon="close.png" />
			</aos:docked>
		</aos:window>
	</aos:viewport>
	<script type="text/javascript">
		function _g_location_query(){
			_g_location_store.load();
		}
		function _w_location_show(){
			var record=AOS.selection(_g_location,'id_');
			if (AOS.empty(record)){
				AOS.tip('请选择要查看的条目!');
				return;
			}
			_w_location.show();
			_f_location.loadRecord(AOS.selectone(_g_location));
		}
		function _g_log_ip_address_query(){
			var params = {
				ip_address : ip_address.getValue()
			};
			//这个Store的命名规则为：表格ID+"_store"。
			_g_log_store.getProxy().extraParams = params;
			_g_log_store.load();
		}

		function fn_edit_location(){
			var record=AOS.selection(_g_location,'id_');
			if (AOS.empty(record)){
				AOS.tip('请选择要修改的条目!');
				return;
			}
			_w_edit_location.show();
			_f_edit_location.loadRecord(AOS.selectone(_g_location));
		}
		function onblur_qzh(){
			var cz_liehao = Ext.getCmp('_f_edit_location').getForm().findField('cz_liehao').getValue();
			var cz_zuhao = Ext.getCmp('_f_edit_location').getForm().findField('cz_zuhao').getValue();
			var cz_mian = Ext.getCmp('_f_edit_location').getForm().findField('cz_mian').getValue();
			var cz_jiehao = Ext.getCmp('_f_edit_location').getForm().findField('cz_jiehao').getValue();
			var cz_gehao = Ext.getCmp('_f_edit_location').getForm().findField('cz_gehao').getValue();
			var cz_name = Ext.getCmp('_f_edit_location').getForm().findField('cz_name').getValue();
			var archive_storehouse = Ext.getCmp('_f_edit_location').getForm().findField('archive_storehouse').getValue();
			Ext.getCmp('_f_edit_location').getForm().findField('dh').setValue(archive_storehouse+'-'+cz_liehao+'-'+cz_zuhao+'-'+cz_mian+'-'+cz_jiehao+'-'+cz_gehao+'-'+cz_name);
		}
		function _f_location_edit_from(){
			AOS.ajax({
				forms:_f_edit_location,
				url: 'updatelocation.jhtml',
				ok: function (data) {
					if (data.appcode === -1) {
						AOS.tip("修改失败!请检查是否有重复的数据");
					}else{
						AOS.tip("修改成功!");
						_w_edit_location.hide();
						_g_location_store.load();
					}
				}
			});




		}
		function _g_log_create_time_query(){
			var params = {
				create_time : create_time.getValue()
			};
			//这个Store的命名规则为：表格ID+"_store"。
			_g_log_store.getProxy().extraParams = params;
			_g_log_store.load();
		}
		//按钮列转换
		function fn_button_render(value, metaData, record, rowIndex, colIndex,
				store) {
			return '<input type="button" value="详情1" class="cbtn" onclick="_w_show();" />';
		}
		//弹出窗口加载
		function _w_log_onshow(){
			var record = AOS.selectone(_g_log, true);
            AOS.ajax({
            	params : {
            		id: record.data.id
            	},
                url: 'getlogInfo.jhtml',
                ok: function (data) {
                	_f_log.form.setValues(data);
                }
            });
		}
		//删除单条记录
		function fn_remove_location(){
			var record = AOS.selectone(_g_location, true);
			var msg = AOS.merge('确认要删除选中的[{0}]个数据吗？', AOS.rows(_g_location));
			if (AOS.empty(record)) {
                AOS.tip('删除前请先选中数据!');
                return;
            }else{
            	 AOS.confirm(msg, function (btn) {
                     if (btn === 'cancel') {
                         AOS.tip('删除操作被取消。');
                         return;
                     }
    			AOS.ajax({
                	params : {
                		id_: record.data.id_
                	},
                    url: 'dellocation.jhtml',
                    ok: function (data) {
						if (data.appcode === -1) {
							AOS.tip("删除失败!");
						}else{
							AOS.tip("删除成功!");
							_g_location_store.load();
						}
                    }
                });
               });
            }
		}
		//清空日志
		function fn_removeall_rows(){
			//如果没有数据，提示清空的日志不存在
			var count=_g_log_store.getCount();
			if(count===0){
				AOS.tip("要清空的日志不存在!");
				return;
			}
			var msg = AOS.merge('确认要清空[{0}]个日志数据吗？', count);

            	 AOS.confirm(msg, function (btn) {
                     if (btn === 'cancel') {
                         AOS.tip('清空操作被取消。');
                         return;
                     }
					//如果有数据走后台执行清空操作
                     AOS.ajax({
         				url:'delAlllogInfo.jhtml',
         				ok:function(data){
         					AOS.tip(data.appmsg);
                        		_g_log_store.reload();
         				}
         			});
         			});

		}
		//导出日志
		function fn_export_location(){
			AOS.ajax({
				url : 'fillReport.jhtml',
				ok : function(data) {
					AOS.file('${cxt}/report/xls2.jhtml');
				}
			});
		}
	</script>
</aos:onready>
<script type="text/javascript">
	//显示详情1窗口
	function _w_show(){
		Ext.getCmp('_w_log').show();
	}

</script>
</aos:html>
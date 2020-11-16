<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="/WEB-INF/tld/aos.tld" prefix="aos"%>
<%@ taglib uri="/WEB-INF/tld/cll.tld" prefix="c"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<aos:html>
	<aos:head title="水印设置预览">
		<aos:include lib="ext,swfupload,flexpaper" />
		<aos:base href="backups/strategy" />
		<aos:include js="${cxt}/static/pdfObject/pdfobject.js" />
	</aos:head>
	<aos:body>
		<div id="aa">

		</div>
	<!--  <div id="documentViewer" class="flexpaper_viewer" style="position:absolute;left:10px;top:10px;width:870px;height:600px"></div> -->
	</aos:body>
	<aos:onready>
		<aos:viewport layout="border" >
			<aos:panel id="documentViewer" width="800"  contentEl="aa"  region="west" >

			</aos:panel>
			<aos:panel region="center" border="false">
				<aos:layout type="vbox" align="stretch" />
			<%--
			<aos:gridpanel flex="1" id="_g_path" url="getPath.jhtml"  enableLocking="true" onitemdblclick="fn_g_path"  onrender="_g_path_account">
				<aos:docked>
					<aos:dockeditem xtype="tbtext" text="电子文件信息" />
					<aos:dockeditem xtype="tbfill" />
					<aos:triggerfield id="tablename"  value="${tablename }"
						style="display:'none'" />
						<aos:triggerfield id="pid"  value="${id }"
						style="display:'none'" />
						<aos:triggerfield id="tid"  value="${tid }"
						style="display:'none'" />
				</aos:docked>
				<aos:column type="rowno" />
				<aos:column header="流水号" dataIndex="id_" hidden="true" />
				<aos:column header="文件名称" dataIndex="_path" width="200"  />
				<aos:column header="上传时间" dataIndex="sdatetime" width="200"   />
				<aos:column header="文件类型" dataIndex="filetype" width="100"   />

			</aos:gridpanel>
			--%>
				<!-- kjkjhkjhkjhkh -->
				<aos:window id="_w_watermark" title="水印设置" closable="false" resizable="false"
							width="420" layout="fit" x="0" y="0">
					<aos:formpanel id="p_set" layout="column" autoScroll="true"   border="false"
								   onrender="getSetwatermark" columnWidth="1" >
						<aos:hiddenfield name="id_" id="id_"/>
						<aos:textfield id="txtword" name="txtword" fieldLabel="文字内容" allowBlank="false" columnWidth="0.9" height="30"/>
						<aos:combobox id="wordsize" name="wordsize" fieldLabel="字号" allowBlank="false" columnWidth="0.9" height="30">
							<aos:option value="20" display="20"/>
							<aos:option value="22" display="22"/>
							<aos:option value="24" display="24"/>
							<aos:option value="26" display="26"/>
							<aos:option value="28" display="28"/>
							<aos:option value="30" display="30"/>
							<aos:option value="32" display="32"/>
							<aos:option value="34" display="34"/>
							<aos:option value="36" display="36"/>
							<aos:option value="38" display="38"/>
							<aos:option value="40" display="40"/>
							<aos:option value="42" display="42"/>
							<aos:option value="44" display="44"/>
							<aos:option value="46" display="46"/>
							<aos:option value="48" display="48"/>
							<aos:option value="50" display="50"/>
							<aos:option value="55" display="55"/>
							<aos:option value="60" display="60"/>
							<aos:option value="65" display="65"/>
							<aos:option value="70" display="70"/>
							<aos:option value="75" display="75"/>
							<aos:option value="80" display="80"/>
							<aos:option value="85" display="85"/>
							<aos:option value="90" display="90"/>
							<aos:option value="95" display="95"/>
							<aos:option value="100" display="100"/>
						</aos:combobox>
						<aos:combobox id="wordfont" name="wordfont" fieldLabel="字体"  allowBlank="false" columnWidth="0.9" height="30">
							<aos:option value="simkai.ttf" display="正楷"/>
							<aos:option value="FZSTK.TTF" display="方正舒体"/>
							<aos:option value="FZYTK.TTF" display="方正姚体"/>
							<aos:option value="SIMFANG.TTF" display="仿宋体"/>
							<aos:option value="SIMHEI.TTF" display="黑体"/>
							<aos:option value="STCAIYUN.TTF" display="华文彩云"/>
							<aos:option value="STFANGSO.TTF" display="华文仿宋"/>
							<aos:option value="STXIHEI.TTF" display="华文细黑"/>
							<aos:option value="STXINWEI.TTF" display="华文新魏"/>
							<aos:option value="STXINGKA.TTF" display="华文行楷"/>
							<aos:option value="STZHONGS.TTF" display="华文中宋"/>
							<aos:option value="SIMLI.TTF" display="隶书"/>
							<aos:option value="SIMYOU.TTF" display="幼圆"/>
						</aos:combobox>
						<aos:combobox id="wordrad" name="wordrad" fieldLabel="倾斜角度" allowBlank="false" columnWidth="0.9" height="30">
							<aos:option value="0" display="0°"/>
							<aos:option value="30" display="30°"/>
							<aos:option value="45" display="45°"/>
							<aos:option value="60" display="60°"/>
							<aos:option value="90" display="90°"/>
						</aos:combobox>
						<aos:combobox id="wordopacity" name="wordopacity" fieldLabel="透明度" allowBlank="false" columnWidth="0.9" height="30">
							<aos:option value="0" display="0"/>
							<aos:option value="0.1" display="10%"/>
							<aos:option value="0.2" display="20%"/>
							<aos:option value="0.3" display="30%"/>
							<aos:option value="0.4" display="40%"/>
							<aos:option value="0.5" display="50%"/>
							<aos:option value="0.6" display="60%"/>
							<aos:option value="0.7" display="70%"/>
							<aos:option value="0.8" display="80%"/>
							<aos:option value="0.9" display="90%"/>
							<aos:option value="1" display="100%"/>
						</aos:combobox>
						<aos:combobox id="density" name="density" fieldLabel="密度" allowBlank="false"
						columnWidth="0.9" height="30">
							<aos:option value="0" display="0"></aos:option>
							<aos:option value="10" display="10%"></aos:option>
							<aos:option value="20" display="20%"></aos:option>
							<aos:option value="30" display="30%"></aos:option>
							<aos:option value="40" display="40%"></aos:option>
							<aos:option value="50" display="50%"></aos:option>
							<aos:option value="60" display="60%"></aos:option>
							<aos:option value="70" display="70%"></aos:option>
							<aos:option value="80" display="80%"></aos:option>
							<aos:option value="90" display="90%"></aos:option>
							<aos:option value="100" display="100%"></aos:option>

						</aos:combobox>
						<aos:docked dock="bottom" ui="footer">
							<aos:dockeditem xtype="tbfill" />

							<aos:dockeditem text="预览" icon="icon71.png" onclick="btnPreview" />

							<aos:dockeditem text="保 存" icon="save.png"  onclick="btnSave" />

						</aos:docked>
					</aos:formpanel>
				</aos:window>
			</aos:panel>
		</aos:viewport>

		<script type="text/javascript">

			//显示设置窗口
			_w_watermark.show();


			window.onload = function (){

				//var aa = '${strswf}';

				//PDFObject.embed('${cxt}/static/pdfObject/001.pdf', '#documentViewer');
				PDFObject.embed('${strswf}', '#documentViewer');
			};

			//获取设置
			function getSetwatermark() {
				AOS.ajax({
					url: 'getSetwatermark.jhtml',
					params: {
						tablename: "setWatermark"
					},
					ok: function (data) {
						if (data.appcode === -1) {
							AOS.err(data.appmsg);
						} else {
							Ext.getCmp("id_").setValue(data.id_);
							Ext.getCmp("txtword").setValue(data.txtword);
							Ext.getCmp("wordsize").setValue(data.wordsize);
							Ext.getCmp("wordfont").setValue(data.wordfont);
							Ext.getCmp("wordrad").setValue(data.wordrad);
							Ext.getCmp("wordopacity").setValue(data.wordopacity);
                            Ext.getCmp("density").setValue(data.density);
						}
					}
				});
			}

			//保存设置
			function btnSave() {
				AOS.ajax({
					forms : p_set,
					url : 'saveData.jhtml',
					ok: function (data) {
						if (data.appcode === -1) {
						   // AOS.err(data.appmsg);
						} else {
						   // AOS.tip(data.appmsg);
						}
					}
				})
			}

			//预览
			function btnPreview() {
                AOS.ajax({
                    forms : p_set,
                    url : 'saveData.jhtml',
                    ok: function (data) {
                        if (data.appcode === -1) {
                            // AOS.err(data.appmsg);
                        } else {
                            // AOS.tip(data.appmsg);
                            AOS.ajax({
                                url:'getPreview',
                                params:{
                                    waterShow: "true",
                                    QRshow: "false"
                                },
                                ok: function (data) {

                                    if (data.appcode === -1) {
                                        // AOS.err(data.appmsg);
                                    } else {
                                        // console.log(data.appmsg);
                                        // AOS.tip(data.appmsg);
                                        setTimeout(PDFObject.embed(data.appmsg,'#documentViewer'),3000);
                                    }
                                }
                            })
                        }
                    }
                })


			}

			//加载表格数据
	/*
			function _g_account_query() {
				//var record = AOS.selectone(_g_account);
				var params = {
					id : pid.getValue(),
					tablename:tablename.getValue()
				};
				//这个Store的命名规则为：表格ID+"_store"。
				_g_account_store.getProxy().extraParams = params;
				_g_account_store.load();
			}
	*/

			/*//加载电子文件列表
			function _g_path_account(){
			var params = {
					tid : tid.getValue(),
					tablename:tablename.getValue()
				};
				//这个Store的命名规则为：表格ID+"_store"。
				_g_path_store.getProxy().extraParams = params;
				_g_path_store.load();
			}*/
	/*

			function fn_g_path(grid,row){
			AOS.ajax({
					 params: {
						id: row.data.id_,
						tablename:tablename.getValue(),
						type:row.data.filetype
					 },
					 url: 'openFileDbclick.jhtml',
					 ok: function (data) {
					 var pageCount=data[0].pageCount;

					 $FlexPaper("documentViewer").loadSwf("{cxt}$/temp/{page[*,0].swf,"+pageCount+"}");
					 }
				 });
			}
	*/
		</script>
</aos:onready>
</aos:html>

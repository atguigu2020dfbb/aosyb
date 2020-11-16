<%--
  Created by IntelliJ IDEA.
  User: Ricardo
  Date: 2020/8/27
  Time: 11:56
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="/WEB-INF/tld/aos.tld" prefix="aos"%>
<aos:html>
    <aos:head title="水印设置">
        <aos:include lib="ext" />
        <aos:base href="backups/strategy" />
    </aos:head>
    <aos:onready>
        <aos:viewport layout="fit">



           <%-- <aos:formpanel id="preview" region="east" >

            </aos:formpanel>--%>
        </aos:viewport>

        <aos:window id="_w_watermark" title="水印设置"
                    width="420" layout="hbox" margin="0">
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

                <aos:docked dock="bottom" ui="footer">
                    <aos:dockeditem xtype="tbfill" />
                    <%-- <aos:button text="预览" width="100" height="30" margin="0 0 0 10" onclick="btnPreview" />--%>

                    <aos:dockeditem text="保 存" icon="save.png"  onclick="btnSave" />

                </aos:docked>
            </aos:formpanel>
        </aos:window>
        <script type="text/javascript">

            _w_watermark.show();
            //保存设置
            function btnSave() {
                AOS.ajax({
                    forms : p_set,
                    url : 'saveData.jhtml',
                    ok: function (data) {
                        if (data.appcode === -1) {
                            AOS.err(data.appmsg);
                        } else {
                            AOS.tip(data.appmsg);
                        }
                    }
                })
            }

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
                        }
                    }
                });
            }
        </script>
    </aos:onready>
</aos:html>


<%@page contentType="text/html; charset=UTF-8" %>
<%@taglib uri="/WEB-INF/tld/aos.tld" prefix="aos" %>
<%@ taglib prefix="aso" uri="http://www.osworks.cn/tag/aos" %>
<aos:html>
    <aos:head>
        <aos:include lib="ext"/>
        <aos:base href="depot/lyxg"/>
    </aos:head>
    <aos:body>
    </aos:body>
    <aos:onready>
        <aos:viewport layout="fit">
            <aos:hiddenfield id="nd" value="${nd}"/>
            <aos:gridpanel id="_g_lyxg" url="listLYXGIn.jhtml" onrender="_g_lyxg_query">
                <aos:docked>
                    <aos:dockeditem text="新增" id="add" icon="add.png" onclick="_w_depotIn_show"/>
                    <aos:dockeditem text="修改" id="edit" icon="edit.png" onclick="_w_lyxg_u_show"/>
                    <aos:dockeditem text="删除" icon="del.png" onclick="_g_depotIn_del"/>
                    <%--                    <aos:dockeditem onclick="_w_charts_export" text="导出excel" icon="icon70.png" />--%>
                    <%--                    <aos:dockeditem onclick="_w_import_show" text="导入excel" icon="folder8.png" />--%>
                </aos:docked>
                <aos:column header="流水号" dataIndex="id_" hidden="true"/>
                <aos:column header="顺序号" dataIndex="sxh"/>
                <aos:column header="登记编号" dataIndex="djbh"/>
                <aso:column header="查档单位" dataIndex="cddw"/>
                <aso:column header="查档人姓名" dataIndex="cdrxm"/>
                <aos:column header="题名" dataIndex="tm"/>
                <aos:column header="利用效果情况" dataIndex="lyxgdj"/>
                <aos:column header="登记人" dataIndex="djr"/>
                <aos:column header="登记时间" dataIndex="djsj"/>
                <aos:column header="备注" dataIndex="bz" width="400"/>
                <aos:column header="" flex="1"/>
            </aos:gridpanel>
        </aos:viewport>
        <aos:window id="_w_depotIn" title="新增" onshow="_w_location_onshow">
            <aos:formpanel id="_f_depotIn" width="700" layout="column">
                <aos:textfield name="sxh" fieldLabel="顺序号" columnWidth="0.49"/>
                <aos:textfield name="djbh" fieldLabel="登记编号" columnWidth="0.49"/>
                <aos:textfield name="cddw" fieldLabel="查档单位" columnWidth="0.49"/>
                <aos:textfield name="cdrxm" fieldLabel="查档人姓名" columnWidth="0.49" />
                <aos:textfield name="tm" fieldLabel="题名" columnWidth="0.98"/>
                <aos:textareafield name="lyxgqk" fieldLabel="利用效果情况" columnWidth="0.98"/>
                <aos:textfield name="djr" fieldLabel="登记人" columnWidth="0.49" value="${user}"/>
                <aos:datefield name="djsj" fieldLabel="登记时间" columnWidth="0.49"/>
                <aos:textfield name="bz" fieldLabel="备注" columnWidth="0.98"/>
                <aos:hiddenfield id="nd" name="nd" value="${nd}"/>
                <aos:docked dock="bottom">
                    <aos:dockeditem xtype="tbfill"/>
                    <aos:dockeditem text="保存" icon="add.png" onclick="_f_depotin_save"/>
                    <aos:dockeditem text="关闭" icon="close.png" onclick="#_w_depotIn.hide()"/>
                </aos:docked>
            </aos:formpanel>
        </aos:window>
        <aos:window id="_w_lyxg_u" title="修改">
            <aos:formpanel id="_f_depotIn_u" width="700" layout="column">
                <aos:hiddenfield  name="id_" />
                <aos:textfield name="sxh" fieldLabel="顺序号" columnWidth="0.49"/>
                <aos:textfield name="djbh" fieldLabel="登记编号" columnWidth="0.49"/>
                <aos:textfield name="cddw" fieldLabel="查档单位" columnWidth="0.49"/>
                <aos:textfield name="cdrxm" fieldLabel="查档人姓名" columnWidth="0.49"/>
                <aos:textfield name="tm" fieldLabel="题名" columnWidth="0.98"/>
                <aos:textareafield name="lyxgqk" fieldLabel="利用效果情况" columnWidth="0.98"/>
                <aos:textfield name="djr" fieldLabel="登记人" columnWidth="0.49"/>
                <aos:datefield name="djsj" fieldLabel="登记时间" columnWidth="0.49"/>
                <aos:textfield name="bz" fieldLabel="备注" columnWidth="0.98"/>
                <aos:hiddenfield id="nd" name="nd" value="${nd}"/>
                <aos:docked dock="bottom">
                    <aos:dockeditem xtype="tbfill"/>
                    <aos:dockeditem text="保存" icon="ok.png" onclick="_f_depotout_u_save"/>
                    <aos:dockeditem text="关闭" icon="close.png" onclick="#_w_lyxg_u.hide()"/>
                </aos:docked>
            </aos:formpanel>
        </aos:window>

        <script type="text/javascript">
            //导入excel窗口
            <%--function _w_import_show(){--%>
            <%--    window.parent.fnaddtab('rk','入库导入','/depot/safe/initimport.jhtml?tablename=depot_rk&lx='+Ext.getCmp("lx").getValue());--%>
            <%--}--%>
            <%--//生成XLS报表--%>
            <%--function _w_charts_export() {--%>
            <%--    AOS.ajax({--%>
            <%--        url : 'fillReport_rk.jhtml',--%>
            <%--        params:{lx:Ext.getCmp("lx").getValue()},--%>
            <%--        ok : function(data) {--%>
            <%--            AOS.file('${cxt}/report/xls2.jhtml');--%>
            <%--        }--%>
            <%--    });--%>
            <%--}--%>
            function _w_location_onshow(){
               var nd= Ext.getCmp("nd").getValue();
                AOS.ajax({
                    params:{name_:"出库流水号",nd:nd},
                    url:'getLyxgIndex.jhtml',
                    ok:function(data){
                        //设计一个随机数编号
                        _f_depotIn.form.findField("sxh").setValue("XG"+nd+data.index);
                    }
                });
            }
            //查询方法
            function _g_lyxg_query() {
                var params = {
                    nd: nd.getValue(),
                };
                _g_lyxg_store.getProxy().extraParams = params;
                _g_lyxg_store.load();
            }

            //弹出新增窗口
            function _w_depotIn_show() {
                _w_depotIn.show();
            }

            //新增方法
            function _f_depotin_save() {
                AOS.ajax({
                    forms: _f_depotIn,
                    url: 'saveDepotIn.jhtml',
                    ok: function (data) {
                        if (data.appcode == -1) {
                            AOS.err(data.appmsg);
                        } else {
                            _w_depotIn.hide();
                            _g_lyxg_store.reload();
                            AOS.tip(data.appmsg);
                        }
                    }
                })
            }

            //弹出修改窗口
            function _w_lyxg_u_show() {
                // var record = AOS.selectone(_g_lyxg);
                // if (record) {
                var record = AOS.selectone(_g_lyxg);
                if(record) {
                    //清空表单
                    AOS.reset(_f_depotIn_u);
                    _f_depotIn_u.loadRecord(record);
                    _w_lyxg_u.show();
                }
            }

            //修改方法
            function _f_depotout_u_save() {
                AOS.ajax({
                    forms: _f_depotIn_u,
                    url: 'updateDepotIn.jhtml',
                    ok: function (data) {
                        if (data.appcode === 1) {
                            _w_lyxg_u.hide();
                            _g_lyxg_store.reload();
                            AOS.tip(data.appmsg);
                        } else {
                            AOS.err(data.appmsg);
                        }
                    }
                })
            }

            //删除方法
            function _g_depotIn_del() {
                var selection = AOS.selection(_g_lyxg, 'id_');
                if (AOS.empty(selection)) {
                    AOS.tip("删除前请选中数据。。。");
                    return;
                }
                var rows = AOS.rows(_g_lyxg);
                var msg = AOS.merge('确定要删除选中的[{0}]条数据吗？', rows);
                AOS.confirm(msg, function (btn) {
                    if (btn === 'cancel') {
                        AOS.tip('删除被操作取消');
                        return;
                    }
                    AOS.ajax({
                        url: 'deleteDepotIn.jhtml',
                        params: {
                            aos_rows_: selection
                        },
                        ok: function (data) {
                            AOS.tip(data.appmsg);
                            _g_lyxg_store.reload();
                        }
                    })
                })
            }
        </script>
    </aos:onready>
</aos:html>
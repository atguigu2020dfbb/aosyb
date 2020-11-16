<%@page contentType="text/html; charset=utf-8" %>
<%@taglib prefix="aos" uri="/WEB-INF/tld/aos.tld" %>
<aos:html>
    <aos:head title="领导核批">
        <aos:include lib="ext"/>
        <aos:base href="make"/>
    </aos:head>
    <aos:body>
    </aos:body>
    <aos:onready>
        <aos:viewport layout="fit">
            <aos:gridpanel id="_g_make" url="listMake_ldhp.jhtml" onrender="_g_make_onrender">
                <aos:docked>
                    <aos:dockeditem xtype="tbtext" text="利用登记"/>
                    <aos:dockeditem text="审批办理" icon="icon66.png" onclick="_w_make_details"/>
                    <aos:dockeditem text="详细信息" icon="query.png" onclick="_w_advance_u_show"/>
                    <aos:dockeditem onclick="_g_make_del" text="删除" id="_f_del_task" icon="del.png" />
                </aos:docked>
                <aos:column header="id_" dataIndex="id_" hidden="true"  locked="true"/>
                <aos:column dataIndex="users" hidden="true"/>
                <aos:column header="登记编号" dataIndex="djbh" width="150" />
                <aos:column header="查档者姓名" dataIndex="xm"/>
                <aos:column header="身份证号" dataIndex="sfzh" width="200"/>
                <aos:column header="利用目的" dataIndex="lymd"/>
                <aos:column header="查阅内容" dataIndex="cdnr" width="200"/>
                <aos:column header="查旬结果" dataIndex="cxjg" rendererFn="fn_cxjg_render"/>
                <aos:column header="是否提供" dataIndex="nftg"/>
                <aos:column header="未提供原因" dataIndex="wtgyy"/>
                <aos:column header="审核状态" dataIndex="spzt" rendererFn="fn_spzt_render"/>
                <aos:column header="核批人" dataIndex="spr_cn" />
                <aos:column header="核批人" dataIndex="spr_en" hidden="true"/>
                <aos:column header="备注" dataIndex="bz"/>
                <aos:column header="" flex="1"/>
            </aos:gridpanel>
        </aos:viewport>

<%--        <aos:window id="_w_make_u" title="审核">--%>
<%--            <aos:formpanel id="_f_make_u" width="700" layout="anchor">--%>
<%--                <aos:hiddenfield name="id_" fieldLabel="id_" />--%>
<%--                <aos:textareafield name="sppz" fieldLabel="批注" allowBlank="false"/>--%>
<%--                <aos:docked dock="bottom" ui="footer">--%>
<%--                    <aos:dockeditem xtype="tbfill"/>--%>
<%--                    <aos:dockeditem text="同意" icon="agree.png" onclick="_f_make_agree"/>--%>
<%--                    <aos:dockeditem text="拒绝" icon="against.png" onclick="_f_make_disagree"/>--%>
<%--                    <aos:dockeditem text="关闭" icon="close.png" onclick="#_w_make_u.hide()"/>--%>
<%--                </aos:docked>--%>
<%--            </aos:formpanel>--%>
<%--        </aos:window>--%>

        <aos:window id="_w_make_u" title="登记详情">
            <aos:formpanel id="_f_make_u" width="500" layout="column">
                <aos:hiddenfield name="id_"  />
                <aos:hiddenfield name="users"/>
                <aos:textfield name="djbh" columnWidth="0.98" fieldLabel="登记编号" />
                <aos:textfield name="xm" columnWidth="0.98" fieldLabel="查档者姓名" />
                <aos:textfield name="dh" columnWidth="0.98" fieldLabel="介绍信" />
                <aos:textfield name="lymd" columnWidth="0.98" fieldLabel="利用目的" />
                <aos:textfield name="cdnr" columnWidth="0.98" fieldLabel="查阅内容" />
                <aos:textfield name="cxjg" columnWidth="0.98" fieldLabel="查询结果" />
                <aos:textfield name="wtgyy" columnWidth="0.98" fieldLabel="未提供原因" />
                <aos:textareafield name="spyj" columnWidth="0.98" fieldLabel="审批意见"/>
                <aos:textfield name="spr" columnWidth="0.98" fieldLabel="核批人"/>
                <aos:textfield name="bz" columnWidth="0.98" fieldLabel="备注" />
                <aos:docked dock="bottom" ui="footer">
                    <aos:dockeditem xtype="tbfill"/>
                    <aos:dockeditem text="同意" icon="agree.png" onclick="_f_make_agree"/>
                    <aos:dockeditem text="拒绝" icon="against.png" onclick="_f_make_disagree"/>
                    <aos:dockeditem text="关闭" icon="close.png" onclick="#_w_make_u.hide()"/>
                </aos:docked>
            </aos:formpanel>
        </aos:window>
        <aos:window id="_w_message_u" title="详细信息">
            <aos:formpanel id="_f_message_u" width="500" layout="column">
                <aos:hiddenfield name="id_" fieldLabel="id_" />
                <aos:textfield name="djbh" fieldLabel="登记编号" columnWidth="0.98"/>
                <aos:textfield name="xm" fieldLabel="查档者姓名" columnWidth="0.98"/>
                <aos:textfield name="sfzh" fieldLabel="身份证号" columnWidth="0.98"/>
                <aos:textfield name="dh" fieldLabel="介绍信" columnWidth="0.98"/>
                <aos:textfield name="lymd" fieldLabel="利用目的" columnWidth="0.98"/>
                <aos:textfield name="cdnr" fieldLabel="查阅内容" columnWidth="0.98"/>
                <aos:textfield name="cxjg" fieldLabel="查旬结果" columnWidth="0.98"/>
                <aos:textfield name="nftg" fieldLabel="是否提供" columnWidth="0.98"/>
                <aos:textfield name="wtgyy" fieldLabel="未提供原因" columnWidth="0.98"/>
                <aos:textfield name="spr" columnWidth="0.98" fieldLabel="核批人"/>
                <aos:textareafield name="bz" fieldLabel="备注" columnWidth="0.98"/>
                <aos:docked dock="bottom" ui="footer">
                    <aos:dockeditem xtype="tbfill"/>
                    <aos:dockeditem text="关闭" icon="close.png" onclick="#_w_message_u.hide()"/>
                </aos:docked>
            </aos:formpanel>
        </aos:window>
        <script type="text/javascript">
            function _w_advance_u_show(){
                var record = AOS.selectone(_g_make);
                if(record){
                    _w_message_u.show();
                    _f_message_u.loadRecord(record);
                }
            }
            function _g_make_onrender(){
                var params={
                    state:1
                };
                _g_make_store.getProxy().extraParams=params;
                _g_make_store.load();
            }
            function _w_make_show(){
                _w_make.show();
            }
            function _w_make_onshow() {
                var djbh =null;
                AOS.ajax({
                    url:'getAPPID.jhtml',
                    ok:function(data){
                        AOS.setValue('_f_make.djbh',data.djbh);
                    }

                })

            }
            function _w_make_u_show(){
                var record = AOS.selectone(_g_make);
                if(record){
                    _w_make_u.show();
                    //_f_make_u.loadRecord(record);
                }
            }
            function _f_make_save(){
                AOS.ajax({
                    forms:_f_make,
                    url:'saveMake.jhtml',
                    ok:function (data){
                        if(data.appcode == -1){
                            AOS.err(data.appmsg);
                        }else {
                            _w_make.hide();
                            _g_make_store.reload();
                            AOS.tip(data.appmsg);
                        }
                    }
                })
            }
            function _f_make_agree(){
                var record = AOS.selectone(_g_make);
                AOS.ajax({
                    params:{
                      'state':0,
                      'spzt':1,
                        users:record.data.users,
                      'id_':record.data.id_
                    },
                    url:'updateMakeAgree.jhtml',
                    ok:function(data){
                        if(data.appcode===1){
                            _w_make_u.hide();
                            _g_make_store.reload();
                            AOS.tip(data.appmsg);
                        }else {
                            AOS.err(AOS.appmsg);
                        }
                    }
                })
            }
            function _f_make_disagree(){

                AOS.ajax({
                    forms:_f_make_u,
                    params:{
                        'state':0,
                        users:record.data.users,
                        'spzt':0
                    },
                    url:'updateMakeAgree.jhtml',
                    ok:function(data){
                        if(data.appcode===1){
                            _w_make_u.hide();
                            _g_make_store.reload();
                            AOS.tip(data.appmsg);
                        }else {
                            AOS.err(AOS.appmsg);
                        }
                    }
                })
            }
            function _g_make_del(){
                var selection = AOS.selection(_g_make,'id_');
                if(AOS.empty(selection)){
                    AOS.tip("删除前请选中数据。。。");
                    return;
                }
                var rows = AOS.rows(_g_make);
                var msg = AOS.merge('确定要删除选中的[{0}]条数据吗？',rows);
                AOS.confirm(msg,function(btn){
                    if(btn==='cancel'){
                        AOS.tip('删除被操作取消');
                        return;
                    }
                    AOS.ajax({
                        url:'updateMake_spzt.jhtml',
                        params:{
                            aos_rows_: selection
                        },
                        ok:function(data){
                            AOS.tip(data.appmsg);
                            _g_make_store.reload();
                        }
                    })
                })
            }
            function fn_lyzt_render(value, metaData, record, rowIndex, colIndex,
                                    store) {
                if (value === '1') {
                    return '已查到';
                } else {
                    return '未查到';
                }
            }

            function fn_spzt_render(value, metaData, record, rowIndex, colIndex,
                                    store){
                if (value === '0') {
                    return '未通过';
                } if(value === '1'){
                    return "已通过";
                }
                else {
                    return '待审核';
                }
            }
            function fn_cxjg_render(value, metaData, record, rowIndex, colIndex,
                                    store){
                if (value === '1') {
                    return '已查到';
                } if(value === '0'){
                    return "未查到";
                }
            }

            function _w_make_details(){
                var record = AOS.selectone(_g_make);
                if(record){
                    _w_make_u.show();
                    _f_make_u.loadRecord(record);
                }

            }
            function _w_make_dd_show(){
                //alert('333');
                var record = AOS.selectone(_g_make);
                if(record){
                    _w_make_dd.show();
                    _f_make_dd.loadRecord(record)
                }
            }
        </script>
    </aos:onready>
</aos:html>
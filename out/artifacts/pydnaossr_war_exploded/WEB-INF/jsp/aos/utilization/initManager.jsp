<%@page contentType="text/html; charset=utf-8" %>
<%@taglib prefix="aos" uri="/WEB-INF/tld/aos.tld" %>
<aos:html>
    <aos:head title="利用登记">
        <aos:include lib="ext"/>
        <aos:base href="utilization"/>
    </aos:head>
    <aos:body>
    </aos:body>
    <aos:onready>
        <aos:viewport layout="fit">
            <aos:gridpanel id="_g_utilization" url="listUtilization.jhtml" onrender="_g_utilization_onrender">
                <aos:docked>
                    <aos:dockeditem xtype="tbtext" text="利用登记"/>
                    <aos:dockeditem text="登记详情" icon="icon66.png" onclick="_w_utilization_details"/>
                </aos:docked>
                <aos:column header="id_" dataIndex="id_" hidden="true"  locked="true"/>
                <aos:column header="登记编号" dataIndex="djbh"/>
                <aos:column header="姓名" dataIndex="xm"/>
                <aos:column header="登记日期" dataIndex="djrq"/>
                <aos:column header="档号" dataIndex="dh"/>
                <aos:column header="题名" dataIndex="tm"/>
                <aos:column header="是否归还" dataIndex="yngh"/>
                <aos:column header="利用状态" dataIndex="lyzt" rendererFn="fn_lyzt_render"/>
                <aos:column header="利用类型" dataIndex="lylx"/>
                <aos:column header="利用类别" dataIndex="lylb"/>
                <aos:column header="利用数量" dataIndex="lysl"/>
                <aos:column header="利用目的" dataIndex="lymd"/>
                <aos:column header="备注" dataIndex="bz"/>
                <aos:column header="" flex="1"/>
            </aos:gridpanel>

        </aos:viewport>
        <aos:window id="_w_utilization" title="新增" onshow="_w_utilization_onshow">
            <aos:formpanel id="_f_utilization" width="700" layout="anchor">
                <aos:textfield name="djbh" fieldLabel="登记编号" readOnly="true"/>
                <aos:textfield name="xm" fieldLabel="姓名"/>
                <aos:datefield name="djrq" fieldLabel="登记日期" editable="false"/>
                <aos:textfield name="lyfs" fieldLabel="利用方式"/>
                <aos:textfield name="lylx" fieldLabel="利用类型"/>
                <aos:textfield name="lylb" fieldLabel="利用类别"/>
                <aos:textfield name="lysl" fieldLabel="利用数量"/>
                <aos:textfield name="lymd" fieldLabel="利用目的"/>
                <aos:textfield name="bz" fieldLabel="备注"/>
                <aos:docked dock="bottom" ui="footer">
                    <aos:dockeditem xtype="tbfill"/>
                    <aos:dockeditem text="保存" icon="save.png" onclick="_f_utilization_save"/>
                    <aos:dockeditem text="关闭" icon="close.png" onclick="#_w_utilization.hide()"/>
                </aos:docked>
            </aos:formpanel>
        </aos:window>

        <aos:window id="_w_utilization_u" title="修改">
            <aos:formpanel id="_f_utilization_u" width="700" layout="anchor">
                <aos:hiddenfield name="id_" fieldLabel="id_" />
                <aos:textfield name="djbh" fieldLabel="登记编号" readOnly="true"/>
                <aos:textfield name="xm" fieldLabel="姓名"/>
                <aos:datefield name="djrq" fieldLabel="登记日期"/>
                <aos:textfield name="lyfs" fieldLabel="利用方式"/>
                <aos:textfield name="lylx" fieldLabel="利用类型"/>
                <aos:textfield name="lylb" fieldLabel="利用类别"/>
                <aos:textfield name="lysl" fieldLabel="利用数量"/>
                <aos:textfield name="lymd" fieldLabel="利用目的"/>
                <aos:textfield name="bz" fieldLabel="备注"/>
                <aos:docked dock="bottom" ui="footer">
                    <aos:dockeditem xtype="tbfill"/>
                    <aos:dockeditem text="保存" icon="save.png" onclick="_f_utilization_u_save"/>
                    <aos:dockeditem text="关闭" icon="close.png" onclick="#_w_utilization_u.hide()"/>
                </aos:docked>
            </aos:formpanel>
        </aos:window>

        <aos:window id="_w_utilization_dd" title="实体调档">
            <aos:formpanel id="_f_utilization_dd" width="700" layout="anchor">
                <aos:hiddenfield name="id_" fieldLabel="id_" />
                <aos:textfield name="djbh" fieldLabel="登记编号" readOnly="true"/>
                <aos:textfield name="xm" fieldLabel="姓名"/>
                <aos:datefield name="djrq" fieldLabel="登记日期" editable="false"/>
                <aos:textfield name="dh" fieldLabel="档号"/>
                <aos:textfield name="tm" fieldLabel="题名"/>
                <aos:textfield name="lyfs" fieldLabel="利用方式"/>
                <aos:textfield name="lylx" fieldLabel="利用类型"/>
                <aos:textfield name="lylb" fieldLabel="利用类别"/>
                <aos:textfield name="lysl" fieldLabel="利用数量"/>
                <aos:textfield name="lymd" fieldLabel="利用目的"/>
                <aos:textfield name="bz" fieldLabel="备注"/>
                <aos:docked dock="bottom" ui="footer">
                    <aos:dockeditem xtype="tbfill"/>
                    <aos:dockeditem text="领导审核" icon="share.png" onclick="_f_utilization_dd_save"/>
                    <aos:dockeditem text="库房申请" icon="icon5.png" onclick="_f_utilization_kf_save"/>
                    <aos:dockeditem text="关闭" icon="close.png" onclick="#_w_utilization_dd.hide()"/>
                </aos:docked>
            </aos:formpanel>
        </aos:window>


    <script type="text/javascript">
        function _g_utilization_onrender(){
            _g_utilization_store.load();
        }
        function _w_utilization_show(){
            _w_utilization.show();
        }
        function _w_utilization_onshow() {
            var djbh =null;
            AOS.ajax({
                url:'getAPPID.jhtml',
                ok:function(data){
                    AOS.setValue('_f_utilization.djbh',data.djbh);
                }
            })
        }
        function _w_utilization_u_show(){
            var record = AOS.selectone(_g_utilization);
            if(record){
                _w_utilization_u.show();
                _f_utilization_u.loadRecord(record);
            }
        }
        function _f_utilization_save(){
            AOS.ajax({
                forms:_f_utilization,
                url:'saveUtilization.jhtml',
                ok:function (data){
                    if(data.appcode == -1){
                        AOS.err(data.appmsg);
                    }else {
                        _w_utilization.hide();
                        _g_utilization_store.reload();
                        AOS.tip(data.appmsg);
                    }
                }
            })
        }
        function _f_utilization_u_save(){
            AOS.ajax({
                forms:_f_utilization_u,
                url:'updateUtilization.jhtml',
                ok:function(data){
                    if(data.appcode===1){
                        _w_utilization_u.hide();
                        _g_utilization_store.reload();
                        AOS.tip(data.appmsg);
                    }else {
                        AOS.err(AOS.appmsg);
                    }
                }
            })
        }
        function _g_utilization_del(){
            var selection = AOS.selection(_g_utilization,'id_');
            if(AOS.empty(selection)){
                AOS.tip("删除前请选中数据。。。");
                return;
            }
            var rows = AOS.rows(_g_utilization);
            var msg = AOS.merge('确定要删除选中的[{0}]条数据吗？',rows);
            AOS.confirm(msg,function(btn){
                if(btn==='cancel'){
                    AOS.tip('删除被操作取消');
                    return;
                }
                AOS.ajax({
                    url:'deleteUtilization.jhtml',
                    params:{
                        aos_rows_: selection
                    },
                    ok:function(data){
                        AOS.tip(data.appmsg);
                        _g_utilization_store.reload();
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
            }if(value === '2'){
                return "待审核";
            }
            else {
                return ' ';
            }
        }
        function fn_ckzt_render(value, metaData, record, rowIndex, colIndex,
                                store){
            if (value === '0') {
                return '出库中';
            } if(value === '1'){
                return "已出库";
            }if(value === '2'){
                return "已入库";
            }else {
                return ' ';
            }
        }
        function _w_utilization_details(){
            var record = AOS.selectone(_g_utilization);
            parent.fnaddtab('','登记详情','/utilization/initData.jhtml?formid='+record.data.id_);

        }
        function _w_utilization_dd_show(){
            //alert('333');
            var record = AOS.selectone(_g_utilization);
            if(record){
                _w_utilization_dd.show();
                _f_utilization_dd.loadRecord(record)
            }
        }
        function _f_utilization_dd_save(){
            AOS.ajax({
                forms:_f_utilization_dd,
                url:'updateUtilizationDd.jhtml',
                params:{
                    'state':1,
                    'spzt':2
                },
                ok:function(data){
                    if(data.appcode===1){
                        _w_utilization_dd.hide();
                        _g_utilization_store.reload();
                        AOS.tip(data.appmsg);
                    }else {
                        AOS.err(AOS.appmsg);
                    }
                }
            })
        }
        function _f_utilization_kf_save(){
            AOS.ajax({
                forms:_f_utilization_dd,
                url:'updateUtilizationDd.jhtml',
                params:{
                    'state':2,
                    'ckzt':'0'
                },
                ok:function(data){
                    if(data.appcode===1){
                        _w_utilization_dd.hide();
                        _g_utilization_store.reload();
                        AOS.tip(data.appmsg);
                    }else {
                        AOS.err(AOS.appmsg);
                    }
                }
            })
        }
        function _w_utilization_gh(){
            var record = AOS.selectone(_g_utilization);

            var id = record.data.id_;
            AOS.ajax({
                url:'updateUtilizationDd.jhtml',
                params:{
                    'id_':id,
                    'yngh':'已归还',
                    'ckzt':2
                },
                ok:function(data){
                    if(data.appcode===1){
                        _w_utilization_dd.hide();
                        _g_utilization_store.reload();
                        AOS.tip(data.appmsg);
                    }else {
                        AOS.err(AOS.appmsg);
                    }
                }
            })
        }
        function _g_utilization_read(){
            var record = AOS.selectone(_g_utilization);
            parent.fnaddtab('','登记详情','/utilization/loadData.jhtml?djbh='+record.data.djbh);
        }
    </script>
    </aos:onready>
</aos:html>
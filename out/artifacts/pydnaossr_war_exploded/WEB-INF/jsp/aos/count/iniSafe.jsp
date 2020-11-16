<%@page contentType="text/html; charset=utf-8" %>
<%@taglib prefix="aos" uri="/WEB-INF/tld/aos.tld" %>
<aos:html>
    <aos:head>
        <aos:include lib="ext" />
        <aos:base href="depot/safe"/>
    </aos:head>
    <aos:body>
    </aos:body>
    <aos:onready>
        <aos:viewport layout="fit">
            <aos:hiddenfield id="lx" value="${lx}"/>
            <aos:gridpanel id="_g_safe" onrender="_g_safe_query" url="listSafe.jhtml" pageSize="20">
                <aos:docked>
                    <aos:dockeditem text="新增" icon="add.png" onclick="_w_safe_show"/>
                    <aos:dockeditem text="修改" icon="edit.png" onclick="_w_safe_u_show"/>
                    <aos:dockeditem text="删除" icon="del.png" onclick="_g_safe_del"/>
                    <aos:dockeditem onclick="_w_charts_export" text="导出excel" icon="icon70.png" />
                    <aos:dockeditem onclick="_w_import_show" text="导入excel" icon="folder8.png" />
                </aos:docked>
                <aos:column header="流水号" dataIndex="id_" hidden="true"/>
                <aos:column header="库房编号" dataIndex="kfbh"/>
                <aos:column header="检查时间" dataIndex="jcsj" width="200"/>
                <aos:column header="检查部门" dataIndex="jcbm" width="200"/>
                <aos:column header="检查类型" dataIndex="jclx" width="200"/>
                <aos:column header="电源是否关闭" dataIndex="dysfgb" width="200"/>
                <aos:column header="口窗是否关闭" dataIndex="kcsfgb"  width="200"/>
                <aos:column header="暖气是否漏水" dataIndex="nqsfls" width="200"/>
                <aos:column header="设备是否正常" dataIndex="sbsfzcsy" width="200"/>
                <aos:column header="空调是否正常" dataIndex="ktsfzc"  width="200"/>
                <aos:column header="一体机是否正常" dataIndex="ytjsfzc"  width="200"/>
                <aos:column header="门禁是否正常" dataIndex="mjsfzc"  width="200"/>
                <aos:column header="监控是否正常" dataIndex="jksfzc"  width="200"/>
                <aos:column header="备注" dataIndex="bz"  width="200"/>
                <aos:column header="" flex="1" />
            </aos:gridpanel>
            <aos:window id="_w_safe" title="新增" onshow="_new_task">
                <aos:formpanel id="_f_safe" layout="column" width="700" >
                    <aos:textfield fieldLabel="库房编号" name="kfbh" columnWidth="0.49" readOnly="true"/>
                    <aos:datefield fieldLabel="检查时间" name="jcsj" columnWidth="0.49"/>
                    <aos:textfield fieldLabel="检查部门" name="jcbm" columnWidth="0.49"/>
                    <aos:textfield fieldLabel="检查人" name="jcr" columnWidth="0.49"/>
                        <aos:radioboxgroup fieldLabel="检查类型" id="jclx_add" columns="[100,100]" columnWidth="0.98">
                            <aos:radiobox name="r1"  boxLabel="例行检查"  checked="true"/>
                            <aos:radiobox name="r1"  boxLabel="节日检查" />
                        </aos:radioboxgroup>
                    <aos:radioboxgroup fieldLabel="电源是否关闭" id="dysfgb_add" columns="[100,100]" columnWidth="0.49">
                        <aos:radiobox name="r2" boxLabel="关闭"   checked="true"/>
                        <aos:radiobox name="r2" boxLabel="未关闭" />
                    </aos:radioboxgroup>
                    <aos:radioboxgroup fieldLabel="口窗是否关闭" id="kcsfgb_add" columns="[100,100]" columnWidth="0.49">
                        <aos:radiobox name="r3" boxLabel="关闭"  checked="true"/>
                        <aos:radiobox name="r3" boxLabel="未关闭" />
                    </aos:radioboxgroup>
                    <aos:radioboxgroup fieldLabel="暖气是否漏水" id="nqsfls_add" columns="[100,100]" columnWidth="0.49">
                        <aos:radiobox name="r4" boxLabel="是"  checked="true"/>
                        <aos:radiobox name="r4" boxLabel="否" />
                    </aos:radioboxgroup>
                    <aos:radioboxgroup fieldLabel="设备是否正常" id="sbsfzcsy_add" columns="[100,100]" columnWidth="0.49">
                        <aos:radiobox name="r5" boxLabel="是"  checked="true"/>
                        <aos:radiobox name="r5" boxLabel="否" />
                    </aos:radioboxgroup>
                    <aos:radioboxgroup fieldLabel="空调是否正常" id="ktsfzc_add" columns="[100,100]" columnWidth="0.49">
                        <aos:radiobox name="r6" boxLabel="是"  checked="true"/>
                        <aos:radiobox name="r6" boxLabel="否" />
                    </aos:radioboxgroup>
                    <aos:radioboxgroup fieldLabel="一体机是否正常" id="ytjsfzc_add" columns="[100,100]" columnWidth="0.49">
                        <aos:radiobox name="r7" boxLabel="是"  checked="true"/>
                        <aos:radiobox name="r7" boxLabel="否" />
                    </aos:radioboxgroup>
                    <aos:radioboxgroup fieldLabel="门禁是否正常" id="mjsfzc_add" columns="[100,100]" columnWidth="0.49">
                        <aos:radiobox name="r8" boxLabel="是"  checked="true"/>
                        <aos:radiobox name="r8" boxLabel="否" />
                    </aos:radioboxgroup>
                    <aos:radioboxgroup fieldLabel="监控是否正常" id="jksfzc_add" columns="[100,100]" columnWidth="0.49">
                        <aos:radiobox name="r9" boxLabel="是"  checked="true"/>
                        <aos:radiobox name="r9" boxLabel="否" />
                    </aos:radioboxgroup>
                    <aos:textfield fieldLabel="备注" name="bz" columnWidth="0.98"/>

                    <aos:docked dock="bottom">
                        <aos:dockeditem xtype="tbfill"/>
                        <aos:dockeditem text="添加" icon="add.png" onclick="_f_safe_save"/>
                        <aos:dockeditem text="关闭" icon="close.png" onclick="#_w_safe.hide()"/>
                    </aos:docked>
                </aos:formpanel>
            </aos:window>
        </aos:viewport>
               <script type="text/javascript">
            //生成XLS报表
            function _w_charts_export() {

                AOS.ajax({
                    url : 'fillReport_aq.jhtml',
                    params:{lx:Ext.getCmp("lx").getValue()},
                    ok : function(data) {
                        AOS.file('${cxt}/report/xls2.jhtml');
                    }
                });
            }
            function _g_safe_query(){
                var params={
                    lx:lx.getValue()
                };
                _g_safe_store.getProxy().extraParams=params;
                _g_safe_store.load();
            }
            function _w_safe_show(){
                _w_safe.show();
            }
            function _new_task(){
                //根据tree名称得到对应的部门名称
                var lx= Ext.getCmp("lx").getValue();
                if("x1"==lx){
                    lx="X1";
                }else if("x2"==lx){
                    lx="X2";
                }else if("x3"==lx){
                    lx="X3";
                }else if("ls"==lx){
                    lx="LS";
                }else if("zl"==lx){
                    lx="ZL";
                }else{
                }
                AOS.ajax({
                    params:{name_:"安全检查流水号"},
                    url:'calcId.jhtml',

                    ok:function(data){
                        //设计一个随机数编号
                        //年月日
                        var time = (new Date).getTime();
                        var yesday = new Date(time); // 获取的是前一天日期
                        yesday = yesday.getFullYear()  +""+(yesday.getMonth()> 9 ? (yesday.getMonth() + 1) : "0" +
                            (yesday.getMonth() + 1));
                        _f_safe.form.findField("kfbh").setValue(lx+yesday+data.index);
                    }
                });
            }
        </script>
    </aos:onready>
</aos:html>
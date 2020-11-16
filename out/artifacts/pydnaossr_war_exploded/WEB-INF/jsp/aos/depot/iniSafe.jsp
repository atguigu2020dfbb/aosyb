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
            <aos:hiddenfield id="jcr" value="${jcr}"/>
            <aos:gridpanel id="_g_safe" onrender="_g_safe_query" url="listSafe.jhtml" pageSize="20">
                <aos:docked>
                    <aos:dockeditem text="新增" icon="add.png" onclick="_w_safe_show"/>
                    <aos:dockeditem text="修改" icon="edit.png" onclick="_w_safe_u_show"/>
                    <aos:dockeditem text="删除" icon="del.png" onclick="_g_safe_del"/>
                    <aos:dockeditem onclick="_w_charts_export" text="导出excel" icon="icon70.png" />
                    <aos:dockeditem onclick="_w_import_show" text="导入excel" icon="folder8.png" />
                </aos:docked>
                <aos:column header="id_" dataIndex="id_" hidden="true" locked="true"/>
                <aos:column header="流水号" dataIndex="lsh"/>
                <aos:column header="检查时间" dataIndex="jcsj" width="200"/>
                <aos:column header="检查部门" dataIndex="jcbm" width="200"/>
                <aos:column header="检查人" dataIndex="jcr" width="200"/>
                <aos:column header="检查类型" dataIndex="jclx" width="200"/>
                <aos:column header="电源是否关闭" dataIndex="dysfgb" width="200"/>
                <aos:column header="门窗是否关闭" dataIndex="kcsfgb"  width="200"/>
                <aos:column header="暖气是否漏水" dataIndex="nqsfls" width="200"/>
                <aos:column header="监控是否正常" dataIndex="jksfzc"  width="200"/>
                <aos:column header="空调是否正常" dataIndex="ktsfzc"  width="200"/>
                <aos:column header="一体机是否正常" dataIndex="ytjsfzc"  width="200"/>
                <aos:column header="门禁是否正常" dataIndex="mjsfzc"  width="200"/>
                <aos:column header="其他问题描述" dataIndex="wtms"  width="200"/>
                <aos:column header="是否有虫害" dataIndex="sfych"  width="200"/>
                <aos:column header="虫害处理结果" dataIndex="chcljg"  width="200"/>
                <aos:column header="是否有霉变" dataIndex="sfymb"  width="200"/>
                <aos:column header="其他问题描述" dataIndex="wtms"  width="200"/>
                <aos:column header="备注" dataIndex="bz"  width="200"/>
                <aos:column header="" flex="1" />
            </aos:gridpanel>
        </aos:viewport>
        <aos:window id="_w_safe" title="新增" onshow="_new_task">
            <aos:formpanel id="_f_safe" layout="column" width="700" >
                <aos:textfield fieldLabel="流水号" name="lsh" columnWidth="0.49" />
                <aos:datefield fieldLabel="检查时间" name="jcsj" columnWidth="0.49"/>
                <aos:textfield name="jcbm" id="jcbm1" fieldLabel="检查部门" columnWidth="0.49"  />

                <aos:textfield fieldLabel="检查人" name="jcr" columnWidth="0.49" value="${user}"/>
                <aos:rowset>
                    <aos:radioboxgroup fieldLabel="检查类型" id="jclx_add" columns="[100,100]" columnWidth="0.98">
                        <aos:radiobox name="r1"  boxLabel="例行检查"  checked="true"/>
                        <aos:radiobox name="r1"  boxLabel="节日检查" />
                    </aos:radioboxgroup>
                </aos:rowset>

                <aos:radioboxgroup fieldLabel="电源是否关闭" id="dysfgb_add" columns="[100,100]" columnWidth="0.49">
                    <aos:radiobox name="r2" boxLabel="关闭"   checked="true"/>
                    <aos:radiobox name="r2" boxLabel="未关闭" />
                </aos:radioboxgroup>
                <aos:radioboxgroup fieldLabel="门窗是否关闭" id="mcsfgb_add" columns="[100,100]" columnWidth="0.49">
                    <aos:radiobox name="r3" boxLabel="关闭"  checked="true"/>
                    <aos:radiobox name="r3" boxLabel="未关闭" />
                </aos:radioboxgroup>
                <aos:radioboxgroup fieldLabel="暖气是否漏水" id="nqsfls_add" columns="[100,100]" columnWidth="0.49">
                    <aos:radiobox name="r4" boxLabel="是"  />
                    <aos:radiobox name="r4" boxLabel="否" checked="true"/>
                </aos:radioboxgroup>
                <aos:radioboxgroup fieldLabel="监控是否正常" id="jksfzc_add" columns="[100,100]" columnWidth="0.49">
                    <aos:radiobox name="r9" boxLabel="是"  checked="true"/>
                    <aos:radiobox name="r9" boxLabel="否" />
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

                <aos:textfield fieldLabel="其他问题描述" name="wtms" columnWidth="0.49"/>
                <aos:radioboxgroup fieldLabel="是否有虫害" id="sfych_add" columns="[100,100]" columnWidth="0.49">
                    <aos:radiobox name="r12" boxLabel="是"  />
                    <aos:radiobox name="r12" boxLabel="否" checked="true"/>
                </aos:radioboxgroup>
                <aos:textfield fieldLabel="虫害处理结果" name="chcljg" columnWidth="0.49"/>
                <aos:radioboxgroup fieldLabel="是否有霉变" id="sfymb_add" columns="[100,100]" columnWidth="0.49">
                    <aos:radiobox name="r10" boxLabel="是"  />
                    <aos:radiobox name="r10" boxLabel="否" checked="true"/>
                </aos:radioboxgroup>
                <aos:textfield fieldLabel="霉变处理结果" name="mbcljg" columnWidth="0.49"/>
                <aos:textfield fieldLabel="备注" name="bz" columnWidth="0.98"/>
                <aos:hiddenfield id="lx" name="lx" value="${lx}"/>
                <aos:docked dock="bottom">
                    <aos:dockeditem xtype="tbfill"/>
                    <aos:dockeditem text="新增" icon="add.png" onclick="_f_add_safe"/>
                    <aos:dockeditem text="保存" icon="save.png" onclick="_f_safe_save"/>

                    <aos:dockeditem text="关闭" icon="close.png" onclick="#_w_safe.hide()"/>
                </aos:docked>
            </aos:formpanel>
        </aos:window>
        <aos:window id="_w_safe_u" title="修改" onshow="edit_safe">
            <aos:formpanel id="_f_safe_u" layout="column" width="700">
                <aos:hiddenfield name="id_" fieldLabel="流水号" />
                <aos:textfield fieldLabel="流水号" name="lsh" columnWidth="0.49" />
                <aos:datefield fieldLabel="检查时间" name="jcsj" columnWidth="0.49"/>
                <aos:textfield name="jcbm" id="jcbm2" fieldLabel="检查部门" columnWidth="0.49"  />

                <aos:textfield fieldLabel="检查人" name="jcr" columnWidth="0.49" value="${user}"/>
                <aos:rowset>
                    <aos:radioboxgroup fieldLabel="检查类型" id="jclx_update" columns="[100,100]" columnWidth="0.98">
                        <aos:radiobox name="r1"  boxLabel="例行检查"  />
                        <aos:radiobox name="r1"  boxLabel="节日检查" />
                    </aos:radioboxgroup>
                </aos:rowset>

                <aos:radioboxgroup fieldLabel="电源是否关闭" id="dysfgb_update" columns="[100,100]" columnWidth="0.49">
                    <aos:radiobox name="r2" boxLabel="关闭"   />
                    <aos:radiobox name="r2" boxLabel="未关闭" />
                </aos:radioboxgroup>
                <aos:radioboxgroup fieldLabel="门窗是否关闭" id="mcsfgb_update" columns="[100,100]" columnWidth="0.49">
                    <aos:radiobox name="r3" boxLabel="关闭"  />
                    <aos:radiobox name="r3" boxLabel="未关闭" />
                </aos:radioboxgroup>
                <aos:radioboxgroup fieldLabel="暖气是否漏水" id="nqsfls_update" columns="[100,100]" columnWidth="0.49">
                    <aos:radiobox name="r4" boxLabel="是"  />
                    <aos:radiobox name="r4" boxLabel="否" />
                </aos:radioboxgroup>
                <aos:radioboxgroup fieldLabel="监控是否正常" id="jksfzc_update" columns="[100,100]" columnWidth="0.49">
                    <aos:radiobox name="r9" boxLabel="是"  />
                    <aos:radiobox name="r9" boxLabel="否" />
                </aos:radioboxgroup>
                <aos:radioboxgroup fieldLabel="空调是否正常" id="ktsfzc_update" columns="[100,100]" columnWidth="0.49">
                    <aos:radiobox name="r6" boxLabel="是"  />
                    <aos:radiobox name="r6" boxLabel="否" />
                </aos:radioboxgroup>
                <aos:radioboxgroup fieldLabel="一体机是否正常" id="ytjsfzc_update" columns="[100,100]" columnWidth="0.49">
                    <aos:radiobox name="r7" boxLabel="是"  />
                    <aos:radiobox name="r7" boxLabel="否" />
                </aos:radioboxgroup>
                <aos:radioboxgroup fieldLabel="门禁是否正常" id="mjsfzc_update" columns="[100,100]" columnWidth="0.49">
                    <aos:radiobox name="r8" boxLabel="是"  />
                    <aos:radiobox name="r8" boxLabel="否" />
                </aos:radioboxgroup>

                <aos:textfield fieldLabel="其他问题描述" name="wtms" columnWidth="0.49"/>
                <aos:radioboxgroup fieldLabel="是否有虫害" id="sfych_update" columns="[100,100]" columnWidth="0.49">
                    <aos:radiobox name="r10" boxLabel="是"  />
                    <aos:radiobox name="r10" boxLabel="否" />
                </aos:radioboxgroup>
                <aos:textfield fieldLabel="虫害处理结果" name="chcljg" columnWidth="0.49"/>
                <aos:radioboxgroup fieldLabel="是否有霉变" id="sfymb_update" columns="[100,100]" columnWidth="0.49">
                    <aos:radiobox name="r11" boxLabel="是"  />
                    <aos:radiobox name="r11" boxLabel="否" />
                </aos:radioboxgroup>
                <aos:textfield fieldLabel="霉变处理结果" name="mbcljg" columnWidth="0.49"/>
                <aos:textfield fieldLabel="备注" name="bz" columnWidth="0.98"/>
                <aos:hiddenfield id="lx" name="lx" value="${lx}"/>
                <aos:docked dock="bottom">
                    <aos:dockeditem xtype="tbfill"/>

                    <aos:dockeditem text="上一条" icon="more/go-previous.png"  onclick="_f_previous_data"/><%----%>
                    <aos:dockeditem text="下一条" icon="more/go-next.png" onclick="_f_next_data"/><%----%>

                    <aos:dockeditem text="保存" icon="save.png" onclick="_f_safe_u_save"/>

                    <aos:dockeditem text="关闭" icon="close.png" onclick="#_w_safe_u.hide()"/>
                </aos:docked>
            </aos:formpanel>
        </aos:window>
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
            //导入excel窗口
            function _w_import_show(){
                window.parent.fnaddtab('aq','安全检查导入','/depot/safe/initimport.jhtml?tablename=depot_aq&lx='+Ext.getCmp("lx").getValue());
            }
            function _f_add_safe(){
                var lx= Ext.getCmp("lx").getValue();
                //根据tree名称得到对应的部门名称
                AOS.ajax({
                    params:{
                        name_:"安全流水号",
                        lx:lx,
                        lsh:_f_safe.form.findField("lsh").getValue()
                    },
                    url:'getDepotAqIndex_nd.jhtml',
                    ok:function(data){
                        //设计一个随机数编号
                        //年
                        var time = (new Date).getTime() // 获取的是年
                        var yesday = _f_safe.form.findField("lsh").getValue().substring(3,7);
                        _f_safe.form.findField("lsh").setValue(lx+yesday+data.index);
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
            function edit_safe(){
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
                if(lx=="301"||lx=="302"){
                    Ext.getCmp("jcbm2").setValue("历史档案管理处");
                }
                if(lx!="301"&&lx!="302"){
                    Ext.getCmp("jcbm2").setValue("党政档案管理处");
                }
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
                if(lx=="301"||lx=="302"){
                    Ext.getCmp("jcbm1").setValue("历史档案管理处");
                }
                if(lx!="301"&&lx!="302"){
                    Ext.getCmp("jcbm1").setValue("党政档案管理处");
                }
                AOS.ajax({
                    params:{name_:"安全检查流水号",lx:lx},
                    url:'getDepotAqIndex.jhtml',
                    ok:function(data){
                        //设计一个随机数编号
                        //年月日
                        var time = (new Date).getTime();
                        var yesday = new Date(time); // 获取的是前一天日期
                        _f_safe.form.findField("lsh").setValue(lx+yesday.getFullYear()+data.index);
                        yesday = yesday.getFullYear()  +"-"+(yesday.getMonth()> 9 ? (yesday.getMonth() + 1) : "0" + (yesday.getMonth() + 1))+
                            "-"+(yesday.getDate()> 9 ? (yesday.getDate()) : "0" + yesday.getDate());
                        _f_safe.form.findField("jcr").setValue(jcr.getValue());
                        var time = (new Date).getTime();
                        var yesday = new Date(time); // 获取的是前一天日期
                        yesday = yesday.getFullYear()  +"-"+(yesday.getMonth()> 9 ? (yesday.getMonth() + 1) : "0" + (yesday.getMonth() + 1))+
                            "-"+(yesday.getDate()> 9 ? (yesday.getDate()) : "0" + yesday.getDate());
                        _f_safe.form.findField("jcsj").setValue(yesday);
                    }
                });
            }
            function _w_safe_show(){
                _w_safe.show();
            }
            function getUpdateRadio(){
                var jclx_update = Ext.getCmp('jclx_update').items;
                var jclx="";
                for (var i=0; i<jclx_update.length;i++){
                    if (jclx_update.items[i].checked){//此处获取到的是inputValue的值
                        jclx = jclx_update.items[i].boxLabel;
                    }
                }
                var dysfgb_update = Ext.getCmp('dysfgb_update').items;
                var dysfgb="";
                for (var i=0; i<dysfgb_update.length;i++){
                    if (dysfgb_update.items[i].checked){//此处获取到的是inputValue的值
                        dysfgb = dysfgb_update.items[i].boxLabel;
                    }
                }
                var kcsfgb_update = Ext.getCmp('mcsfgb_update').items;
                var kcsfgb="";
                for (var i=0; i<kcsfgb_update.length;i++){
                    if (kcsfgb_update.items[i].checked){//此处获取到的是inputValue的值
                        kcsfgb = kcsfgb_update.items[i].boxLabel;
                    }
                }
                var nqsfls_update = Ext.getCmp('nqsfls_update').items;
                var nqsfls="";
                for (var i=0; i<nqsfls_update.length;i++){
                    if (nqsfls_update.items[i].checked){//此处获取到的是inputValue的值
                        nqsfls = nqsfls_update.items[i].boxLabel;
                    }
                }
                var jksfzc_update = Ext.getCmp('jksfzc_update').items;
                var jksfzc="";
                for (var i=0; i<jksfzc_update.length;i++){
                    if (jksfzc_update.items[i].checked){//此处获取到的是inputValue的值
                        jksfzc = jksfzc_update.items[i].boxLabel;
                    }
                }
                var ktsfzc_update = Ext.getCmp('ktsfzc_update').items;
                var ktsfzc="";
                for (var i=0; i<ktsfzc_update.length;i++){
                    if (ktsfzc_update.items[i].checked){//此处获取到的是inputValue的值
                        ktsfzc = ktsfzc_update.items[i].boxLabel;
                    }
                }
                var ytjsfzc_update = Ext.getCmp('ytjsfzc_update').items;
                var ytjsfzc="";
                for (var i=0; i<ytjsfzc_update.length;i++){
                    if (ytjsfzc_update.items[i].checked){//此处获取到的是inputValue的值
                        ytjsfzc = ytjsfzc_update.items[i].boxLabel;
                    }
                }
                var mjsfzc_update = Ext.getCmp('mjsfzc_update').items;
                var mjsfzc="";
                for (var i=0; i<mjsfzc_update.length;i++){
                    if (mjsfzc_update.items[i].checked){//此处获取到的是inputValue的值
                        mjsfzc = mjsfzc_update.items[i].boxLabel;
                    }
                }
                var sfych_update = Ext.getCmp('sfych_update').items;
                var sfych="";
                for (var i=0; i<sfych_update.length;i++){
                    if (sfych_update.items[i].checked){//此处获取到的是inputValue的值
                        sfych = sfych_update.items[i].boxLabel;
                    }
                }
                var sfymb_update = Ext.getCmp('sfymb_update').items;
                var sfymb="";
                for (var i=0; i<sfymb_update.length;i++){
                    if (sfymb_update.items[i].checked){//此处获取到的是inputValue的值
                        sfymb = sfymb_update.items[i].boxLabel;
                    }
                }
                var params={
                    jclx:jclx,
                    dysfgb:dysfgb,
                    kcsfgb:kcsfgb,
                    nqsfls:nqsfls,
                    sfych:sfych,
                    sfymb:sfymb,
                    ktsfzc:ktsfzc,
                    ytjsfzc:ytjsfzc,
                    mjsfzc:mjsfzc,
                    jksfzc:jksfzc
                };
                return params;
            }
            function getAddRadio(){
                var jclx_add = Ext.getCmp('jclx_add').items;
                var jclx="";
                for (var i=0; i<jclx_add.length;i++){
                    if (jclx_add.items[i].checked){//此处获取到的是inputValue的值
                        jclx = jclx_add.items[i].boxLabel;
                    }
                }
                var dysfgb_add = Ext.getCmp('dysfgb_add').items;
                var dysfgb="";
                for (var i=0; i<dysfgb_add.length;i++){
                    if (dysfgb_add.items[i].checked){//此处获取到的是inputValue的值
                        dysfgb = dysfgb_add.items[i].boxLabel;
                    }
                }
                var kcsfgb_add = Ext.getCmp('mcsfgb_add').items;
                var kcsfgb="";
                for (var i=0; i<kcsfgb_add.length;i++){
                    if (kcsfgb_add.items[i].checked){//此处获取到的是inputValue的值
                        kcsfgb = kcsfgb_add.items[i].boxLabel;
                    }
                }
                var nqsfls_add = Ext.getCmp('nqsfls_add').items;
                var nqsfls="";
                for (var i=0; i<nqsfls_add.length;i++){
                    if (nqsfls_add.items[i].checked){//此处获取到的是inputValue的值
                        nqsfls = nqsfls_add.items[i].boxLabel;
                    }
                }
                var jksfzc_add = Ext.getCmp('jksfzc_add').items;
                var jksfzc="";
                for (var i=0; i<jksfzc_add.length;i++){
                    if (jksfzc_add.items[i].checked){//此处获取到的是inputValue的值
                        jksfzc = jksfzc_add.items[i].boxLabel;
                    }
                }
                var ktsfzc_add = Ext.getCmp('ktsfzc_add').items;
                var ktsfzc="";
                for (var i=0; i<ktsfzc_add.length;i++){
                    if (ktsfzc_add.items[i].checked){//此处获取到的是inputValue的值
                        ktsfzc = ktsfzc_add.items[i].boxLabel;
                    }
                }
                var ytjsfzc_add = Ext.getCmp('ytjsfzc_add').items;
                var ytjsfzc="";
                for (var i=0; i<ytjsfzc_add.length;i++){
                    if (ytjsfzc_add.items[i].checked){//此处获取到的是inputValue的值
                        ytjsfzc = ytjsfzc_add.items[i].boxLabel;
                    }
                }
                var mjsfzc_add = Ext.getCmp('mjsfzc_add').items;
                var mjsfzc="";
                for (var i=0; i<mjsfzc_add.length;i++){
                    if (mjsfzc_add.items[i].checked){//此处获取到的是inputValue的值
                        mjsfzc = mjsfzc_add.items[i].boxLabel;
                    }
                }
                var sfych_add = Ext.getCmp('sfych_add').items;
                var sfych="";
                for (var i=0; i<sfych_add.length;i++){
                    if (sfych_add.items[i].checked){//此处获取到的是inputValue的值
                        sfych = sfych_add.items[i].boxLabel;
                    }
                }
                var sfymb_add = Ext.getCmp('sfymb_add').items;
                var sfymb="";
                for (var i=0; i<sfymb_add.length;i++){
                    if (sfymb_add.items[i].checked){//此处获取到的是inputValue的值
                        sfymb = sfymb_add.items[i].boxLabel;
                    }
                }

                var params={
                    jclx:jclx,
                    dysfgb:dysfgb,
                    kcsfgb:kcsfgb,
                    nqsfls:nqsfls,
                    sfych:sfych,
                    sfymb:sfymb,
                    ktsfzc:ktsfzc,
                    ytjsfzc:ytjsfzc,
                    mjsfzc:mjsfzc,
                    jksfzc:jksfzc
                };
                return params;
            }
            function _f_safe_save(){
                var params=getAddRadio();
                AOS.ajax({
                    forms:_f_safe,
                    params:params,
                    url:'saveSafe.jhtml',
                    ok:function(data){
                        if(data.appcode === 1){
                            //_w_safe.hide();
                            _g_safe_store.reload();
                            AOS.tip(data.appmsg);
                        }else {
                            AOS.err(data.appmsg);
                        }
                    }
                })
            }
            function _w_safe_u_show(){
                var record = AOS.selectone(_g_safe);
                if(record){
                    _w_safe_u.show();
                    _f_safe_u.loadRecord(record);
                    var jclx = Ext.getCmp('jclx_update').items;
                    for(var i = 0; i < jclx.length; i++){
                        if(jclx.get(i).boxLabel==record.data.jclx){
                            jclx.items[i].setValue(true);
                        }else{
                            var zz= jclx_update.items[i];
                            jclx.items[i].setValue(false);
                        }
                    }
                    var dysfgb = Ext.getCmp('dysfgb_update').items;
                    for(var i = 0; i < dysfgb.length; i++){
                        if(dysfgb.get(i).boxLabel==record.data.dysfgb){
                            dysfgb.items[i].setValue(true);
                        }else{
                            var zz= dysfgb_update.items[i];
                            dysfgb.items[i].setValue(false);
                        }
                    }


                    var kcsfgb= Ext.getCmp('mcsfgb_update').items;
                    for(var i = 0; i < kcsfgb.length; i++){
                        if(kcsfgb.get(i).boxLabel==record.data.kcsfgb){
                            kcsfgb.items[i].setValue(true);
                        }else{
                            //var zz= kcsfgb_update.items[i];
                            kcsfgb.items[i].setValue(false);
                        }
                    }


                    var nqsfls= Ext.getCmp('nqsfls_update').items;
                    for(var i = 0; i < nqsfls.length; i++){
                        if(nqsfls.get(i).boxLabel==record.data.nqsfls){
                            nqsfls.items[i].setValue(true);
                        }else{
                            var zz= nqsfls_update.items[i];
                            nqsfls.items[i].setValue(false);
                        }
                    }
                    var ktsfzc = Ext.getCmp('ktsfzc_update').items;
                    for(var i = 0; i < ktsfzc.length; i++){
                        if(ktsfzc.get(i).boxLabel==record.data.ktsfzc){
                            ktsfzc.items[i].setValue(true);
                        }else{
                            var zz= ktsfzc_update.items[i];
                            ktsfzc.items[i].setValue(false);
                        }
                    }

                    var ytjsfzc= Ext.getCmp('ytjsfzc_update').items;
                    for(var i = 0; i < ytjsfzc.length; i++){
                        if(ytjsfzc.get(i).boxLabel==record.data.ytjsfzc){
                            ytjsfzc.items[i].setValue(true);
                        }else{
                            var zz= ytjsfzc_update.items[i];
                            ytjsfzc.items[i].setValue(false);
                        }
                    }

                    var mjsfzc= Ext.getCmp('mjsfzc_update').items;
                    for(var i = 0; i < mjsfzc.length; i++){
                        if(mjsfzc.get(i).boxLabel==record.data.mjsfzc){
                            mjsfzc.items[i].setValue(true);
                        }else{
                            var zz= mjsfzc_update.items[i];
                            mjsfzc.items[i].setValue(false);
                        }
                    }


                    var jksfzc= Ext.getCmp('jksfzc_update').items;
                    for(var i = 0; i < jksfzc.length; i++){
                        if(jksfzc.get(i).boxLabel==record.data.jksfzc){
                            jksfzc.items[i].setValue(true);
                        }else{
                            var zz= jksfzc_update.items[i];
                            jksfzc.items[i].setValue(false);
                        }
                    }
                    ///1111
                    var sfych= Ext.getCmp('sfych_update').items;
                    for(var i = 0; i < sfych.length; i++){
                        if(sfych.get(i).boxLabel==record.data.sfych){
                            sfych.items[i].setValue(true);
                        }else{
                            sfych.items[i].setValue(false);
                        }
                    }
                    var sfymb= Ext.getCmp('sfymb_update').items;
                    for(var i = 0; i < sfymb.length; i++){
                        if(sfymb.get(i).boxLabel==record.data.sfymb){
                            sfymb.items[i].setValue(true);
                        }else{
                            sfymb.items[i].setValue(false);
                        }
                    }
                }
            }
            function _f_safe_u_save(){
                var params=getUpdateRadio();
                AOS.ajax({
                    forms:_f_safe_u,
                    params:params,
                    url:'updateSafe.jhtml',
                    ok:function(data){
                        if(data.appcode===1){
                            _w_safe_u.hide();
                            _g_safe_store.reload();
                            AOS.tip(data.appmsg);
                        }else {
                            AOS.err(AOS.appmsg);
                        }
                    }
                })
            }
            function _g_safe_del(){
                var selection = AOS.selection(_g_safe,'id_');
                if(AOS.empty(selection)){
                    AOS.tip("删除前请选中数据。。。");
                    return;
                }
                var rows = AOS.rows(_g_safe);
                var msg = AOS.merge('确定要删除选中的[{0}]条数据吗？',rows);
                AOS.confirm(msg,function(btn){
                    if(btn==='cancel'){
                        AOS.tip('删除被操作取消');
                        return;
                    }
                    AOS.ajax({
                        url:'deleteSafe.jhtml',
                        params:{
                            aos_rows_: selection
                        },
                        ok:function(data){
                            AOS.tip(data.appmsg);
                            _g_safe_store.reload();
                        }
                    })
                })
            }




            //上一页
            function _f_previous_data(){
                var count=Ext.getCmp("_g_safe").getStore().getCount();
                var me=Ext.getCmp("_g_safe").getSelectionModel().getSelection();
                //var record = AOS.selectone(_g_safe);
                //得到执行行的坐标
                var rowIndex = Ext.getCmp("_g_safe").getStore().indexOf(me[0]);
                if(rowIndex==0){
                    AOS.err("当前第一条!");
                    return;
                }
                var s=Ext.getCmp("_g_safe").getStore().getAt(rowIndex-1);
                //原先行取消选中
                Ext.getCmp("_g_safe").getSelectionModel().deselect(rowIndex);
                //此时让光标选中上一行
                Ext.getCmp("_g_safe").getSelectionModel().select(rowIndex-1, true);
                //组件被显示后触发。
                Ext.getCmp("_f_safe_u").form.setValues(s.data);
            }
            //下一页
            function _f_next_data(){
                var count=Ext.getCmp("_g_safe").getStore().getCount();
                var me=Ext.getCmp("_g_safe").getSelectionModel().getSelection();
                //var record = AOS.selectone(_g_safe);
                //得到执行行的坐标
                var rowIndex = Ext.getCmp("_g_safe").getStore().indexOf(me[0]);
                if(rowIndex==count-1){
                    AOS.err("当前最后一条!");
                    return;
                }
                var s=Ext.getCmp("_g_safe").getStore().getAt(rowIndex+1);
                //原先行取消选中
                Ext.getCmp("_g_safe").getSelectionModel().deselect(rowIndex);
                //此时让光标选中下一行
                Ext.getCmp("_g_safe").getSelectionModel().select(rowIndex+1, true);
                //组件被显示后触发。
                Ext.getCmp("_f_safe_u").form.setValues(s.data);
            }
        </script>
    </aos:onready>
</aos:html>
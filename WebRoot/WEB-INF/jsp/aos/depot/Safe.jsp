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
                <aos:column header="流水号" dataIndex="id_" hidden="true" locked="true"/>
                <aos:column header="库房编号" dataIndex="kfbh"/>
                <aos:column header="检查时间" dataIndex="jcsj" width="200"/>
                <aos:column header="检查部门" dataIndex="jcbm" width="200"/>
                <aos:column header="检查类型" dataIndex="jclx" width="200"/>
                <aos:column header="电源是否关闭" dataIndex="dysfgb" width="200"/>
                <aos:column header="门窗是否关闭" dataIndex="kcsfgb"  width="200"/>
                <aos:column header="暖气是否漏水" dataIndex="nqsfls" width="200"/>
                <aos:column header="设备是否正常" dataIndex="sbsfzcsy" width="200"/>
                <aos:column header="空调是否正常" dataIndex="ktsfzc"  width="200"/>
                <aos:column header="一体机是否正常" dataIndex="ytjsfzc"  width="200"/>
                <aos:column header="门禁是否正常" dataIndex="mjsfzc"  width="200"/>
                <aos:column header="监控是否正常" dataIndex="jksfzc"  width="200"/>
                <aos:column header="备注" dataIndex="bz"  width="200"/>
                <aos:column header="" flex="1" />
            </aos:gridpanel>
        </aos:viewport>
        <aos:window id="_w_safe" title="新增" onshow="_new_task">
            <aos:formpanel id="_f_safe" layout="column" width="700" >
                <aos:textfield fieldLabel="库房编号" name="kfbh" columnWidth="0.49" />
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
                <aos:radioboxgroup fieldLabel="门窗是否关闭" id="kcsfgb_add" columns="[100,100]" columnWidth="0.49">
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
        <aos:window id="_w_safe_u" title="修改">
            <aos:formpanel id="_f_safe_u" layout="column" width="700" >
                <aos:hiddenfield  name="id_" id="id_"/>
                <aos:textfield fieldLabel="库房编号" name="kfbh" columnWidth="0.49" />
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
                <aos:radioboxgroup fieldLabel="门窗是否关闭" id="kcsfgb_add" columns="[100,100]" columnWidth="0.49">
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
                    <aos:dockeditem text="添加" icon="add.png" onclick="_f_safe_u_save"/>
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
            function _g_safe_query(){
                var params={
                    lx:lx.getValue()
                };
                _g_safe_store.getProxy().extraParams=params;
                _g_safe_store.load();
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
                    params:{name_:"安全检查流水号",lx:lx},
                    url:'getSafeIndex.jhtml',
                    ok:function(data){
                        //设计一个随机数编号
                        //年月日
                        var time = (new Date).getTime();
                        var yesday = new Date(time); // 获取的是前一天日期
                        _f_safe.form.findField("lsh").setValue(lx+yesday+data.index);
                        yesday = yesday.getFullYear()  +"-"+(yesday.getMonth()> 9 ? (yesday.getMonth() + 1) : "0" + (yesday.getMonth() + 1))+
                            "-"+(yesday.getDate()> 9 ? (yesday.getDate()) : "0" + yesday.getDate());

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
                var kcsfgb_update = Ext.getCmp('kcsfgb_update').items;
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
                var sbsfzcsy_update = Ext.getCmp('sbsfzcsy_update').items;
                var sbsfzcsy="";
                for (var i=0; i<sbsfzcsy_update.length;i++){
                    if (sbsfzcsy_update.items[i].checked){//此处获取到的是inputValue的值
                        sbsfzcsy = sbsfzcsy_update.items[i].boxLabel;
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
                var jksfzc_update = Ext.getCmp('jksfzc_update').items;
                var jksfzc="";
                for (var i=0; i<jksfzc_update.length;i++){
                    if (jksfzc_update.items[i].checked){//此处获取到的是inputValue的值
                        jksfzc = jksfzc_update.items[i].boxLabel;
                    }
                }
                var params={
                    jclx:jclx,
                    dysfgb:dysfgb,
                    kcsfgb:kcsfgb,
                    nqsfls:nqsfls,
                    sbsfzcsy:sbsfzcsy,
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
                var kcsfgb_add = Ext.getCmp('kcsfgb_add').items;
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
                var sbsfzcsy_add = Ext.getCmp('sbsfzcsy_add').items;
                var sbsfzcsy="";
                for (var i=0; i<sbsfzcsy_add.length;i++){
                    if (sbsfzcsy_add.items[i].checked){//此处获取到的是inputValue的值
                        sbsfzcsy = sbsfzcsy_add.items[i].boxLabel;
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
                var jksfzc_add = Ext.getCmp('jksfzc_add').items;
                var jksfzc="";
                for (var i=0; i<jksfzc_add.length;i++){
                    if (jksfzc_add.items[i].checked){//此处获取到的是inputValue的值
                        jksfzc = jksfzc_add.items[i].boxLabel;
                    }
                }
                var params={
                    jclx:jclx,
                    lx:lx.getValue(),
                    dysfgb:dysfgb,
                    kcsfgb:kcsfgb,
                    nqsfls:nqsfls,
                    sbsfzcsy:sbsfzcsy,
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
                            _w_safe.hide();
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


                    var kcsfgb= Ext.getCmp('kcsfgb_update').items;
                    for(var i = 0; i < kcsfgb.length; i++){
                        if(kcsfgb.get(i).boxLabel==record.data.kcsfgb){
                            kcsfgb.items[i].setValue(true);
                        }else{
                            var zz= kcsfgb_update.items[i];
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


                    var sbsfzcsy= Ext.getCmp('sbsfzcsy_update').items;
                    for(var i = 0; i < sbsfzcsy.length; i++){
                        if(sbsfzcsy.get(i).boxLabel==record.data.sbsfzcsy){
                            sbsfzcsy.items[i].setValue(true);
                        }else{
                            sbsfzcsy.items[i].setValue(false);
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
        </script>
    </aos:onready>
</aos:html>
<%@page contentType="text/html; charset=utf-8" %>
<%@taglib prefix="aos" uri="/WEB-INF/tld/aos.tld" %>
<aos:html>
    <aos:head title="自助登记">
        <aos:include lib="ext,swfupload"/>
        <aos:base href="make"/>
    </aos:head>
    <aos:body>
    </aos:body>
    <aos:onready>
        <aos:viewport layout="fit">
            <aos:gridpanel id="_g_apply" url="listApply.jhtml" onrender="_g_apply_onrender">
                <aos:docked>
                    <aos:hiddenfield id="user" name="user" value="${user}"/>
                    <aos:dockeditem xtype="tbtext" text="自助登记"/>
                    <aos:dockeditem  text="查询结果" icon="text_cap.png" >
                        <aos:menu plain="true">
                            <aos:menuitem onclick="_no_query" text="未查到"/>
                            <aos:menuitem onclick="_yes_query" text="已查到"/>
                        </aos:menu>
                    </aos:dockeditem>
                    <aos:dockeditem onclick="_del_zizhu_task" text="删除" id="_f_del_task" icon="del.png" />
                    <aos:dockeditem text="显示" icon="picture.png" id="_f_filename_message"
                                    onclick="_w_picture_show"/>
                    <aos:dockeditem text="能否提供" icon="icon134.png" >
                        <aos:menu plain="true">
                            <aos:menuitem text="可以提供" onclick="_yes_provide"/>
                            <aos:menuitem text="不可提供" onclick="_no_provide" />
                        </aos:menu>
                    </aos:dockeditem>
                    <aos:dockeditem text="利用日志" icon="text_list.png" onclick="_w_apply_log_show"/>
                </aos:docked>
                <aos:selmodel type="checkbox" mode="multi" />
                <aos:column header="id_" dataIndex="id_" hidden="true"  locked="true"/>
                <aos:column header="自助编号" dataIndex="zzbh" width="150"/>
                <aos:column header="查档者姓名" dataIndex="xm"/>
                <aos:column header="身份证号" dataIndex="sfzh" width="200"/>
                <aos:column header="扫描信" dataIndex="dh" hidden="true"/>
                <aos:column header="利用目的" dataIndex="lymd" hidden="true"/>
                <aos:column header="查阅内容" dataIndex="cdnr" hidden="true"/>
                <aos:column header="查旬结果" dataIndex="cxjg"   rendererFn="fn_cxjg_render"/>
                <aos:column header="是否提供" dataIndex="nftg"/>
                <aos:column header="未提供原因" dataIndex="wtgyy"/>
                <aos:column header="备注" dataIndex="bz"/>
                <aos:column header="表明" dataIndex="tablename" hidden="true"/>
                <aos:column header="条目id" dataIndex="tablename_id" hidden="true"/>

                <aos:column header="审核状态" dataIndex="shzt" rendererFn="fn_spzt_render"/>
                <aos:column header="" flex="1"/>
            </aos:gridpanel>
        </aos:viewport>

        <aos:window id="_w_apply_u" title="审核">
            <aos:formpanel id="_f_apply_u" width="700" layout="anchor">
                <aos:hiddenfield name="id_" fieldLabel="id_" />
                <aos:textareafield name="sppz" fieldLabel="批注" allowBlank="false"/>
                <aos:docked dock="bottom" ui="footer">
                    <aos:dockeditem xtype="tbfill"/>
                    <aos:dockeditem text="同意" icon="agree.png" onclick="_f_apply_agree"/>
                    <aos:dockeditem text="拒绝" icon="against.png" onclick="_f_apply_disagree"/>
                    <aos:dockeditem text="关闭" icon="close.png" onclick="#_w_apply_u.hide()"/>
                </aos:docked>
            </aos:formpanel>
        </aos:window>
        <aos:window title="不可提供" id="_w_provide" layout="fit"  modal="false" center="true" maximizable="true"
        >
            <aos:formpanel id="_f_provide" layout="fit" autoScroll="true" labelWidth="70" width="500" height="200">
                <aos:textareafield name="wtgyy" fieldLabel="原因"  />
            </aos:formpanel>
            <aos:docked dock="bottom" ui="footer">
                <aos:dockeditem xtype="tbfill" />
                <aos:dockeditem xtype="button" text="保存数据" icon="ok.png" onclick="_f_provide_save"/>
                <aos:dockeditem text="取消" icon="close.png" onclick="#_w_provide.hide()"/>
            </aos:docked>
        </aos:window>
        <aos:window id="_w_apply_log" title="利用日志" onshow="_w_apply_log_onshow" >
            <aos:gridpanel id="_g_apply_log" region="center" url="listMakeLog.jhtml" width="500" height="400" hidePagebar="true">
                <aos:column header="id_" dataIndex="id_" hidden="true"/>
                <aos:column header="操作时间" dataIndex="czsj" width="200"/>
                <aos:column header="操作人" dataIndex="czr"/>
                <aos:column header="内容" dataIndex="cznr"/>
                <aos:column header="" flex="1"/>
            </aos:gridpanel>
        </aos:window>
        <script type="text/javascript">
            function _no_query(){
                var record = AOS.selectone(_g_apply);
                AOS.ajax({
                    url:'insertApplyDetail.jhtml',
                    params:{
                        formid:record.data.id_,
                        cxjg:0
                    },
                    ok:function (data) {
                        AOS.tip(data.appmsg);
                        _g_apply_store.load();
                    }
                })
            }
            //删除信息
            function _del_zizhu_task() {
                var selection = AOS.selection(_g_apply, 'id_');
                var dataone = AOS.selectone(_g_apply);
                if (!AOS.selectone(_g_apply)) {
                    AOS.tip('删除前请先选中数据。');
                    return;
                }
                var msg = AOS.merge('确认要删除选中的数据吗？');
                AOS.confirm(msg, function (btn) {
                    if (btn === 'cancel') {
                        AOS.tip('删除操作被取消。');
                        return;
                    }
                    AOS.ajax({
                        url: 'deleteZiZhuTask.jhtml',
                        params: {
                            aos_rows_: selection
                        },
                        ok: function (data) {
                            AOS.tip(data.appmsg);
                            _g_apply_store.reload();
                        }
                    });
                });
            }
            function _w_apply_log_show(){
                _w_apply_log.show();
            }
            function _w_apply_log_onshow(){
                var record = AOS.selectone(_g_apply);
                var params={
                };
                if(record){
                    params={
                        tid:record.data.id_
                    };
                }

                _g_apply_log_store.getProxy().extraParams = params;
                _g_apply_log_store.load();
            }
            function _yes_query(){
                var record = AOS.selectone(_g_apply);
                AOS.ajax({
                    url:'insertApplyDetail.jhtml',
                    params:{
                        formid:record.data.id_,
                        cxjg:1
                    },
                    ok:function (data) {
                        AOS.tip(data.appmsg);
                        _g_apply_store.load();
                    }
                })
            }
            function _yes_provide(){
                var record = AOS.selectone(_g_apply);
                AOS.ajax({
                    url:'updatezz_Apply.jhtml',
                    params:{
                        id_:record.data.id_,
                        shzt:'1',
                        nftg:'可以提供'
                    },
                    ok:function (data) {
                        AOS.tip(data.appmsg);
                        _g_apply_store.reload();
                    }
                })
            }
            function _no_provide(){
                _w_provide.show();
            }
            function  _f_provide_save(){
                var record = AOS.selectone(_g_apply);
                AOS.ajax({
                    forms:_f_provide,
                    url:'updatezz_Apply.jhtml',
                    params:{
                        id_:record.data.id_,
                        shzt:'0',
                        nftg:'不可提供'
                    },
                    ok:function (data) {
                        AOS.tip(data.appmsg);
                        _w_provide.hide();
                        _g_apply_store.reload();
                    }
                })
            }
            function fn_cxjg_render(value, metaData, record, rowIndex, colIndex,
                                    store){
                if (value === '0') {
                    return '未查到';
                } if(value === '1'){
                    return "已查到";
                }
            }
            function _g_apply_onrender(){
                var params={
                    state:1
                };
                _g_apply_store.getProxy().extraParams=params;
                _g_apply_store.load();
            }
            function _w_apply_show(){
                _w_make.show();
            }
            function _w_apply_onshow() {
                var djbh =null;
                AOS.ajax({
                    url:'getAPPID.jhtml',
                    ok:function(data){
                        AOS.setValue('_f_apply.djbh',data.djbh);
                    }

                })

            }
            function _w_apply_u_show(){
                var record = AOS.selectone(_g_apply);
                if(record){
                    _w_apply_u.show();
                    _f_apply_u.loadRecord(record);
                }
            }
            function _f_apply_save(){
                AOS.ajax({
                    forms:_f_apply,
                    url:'saveApply.jhtml',
                    ok:function (data){
                        if(data.appcode == -1){
                            AOS.err(data.appmsg);
                        }else {
                            _w_apply.hide();
                            _g_apply_store.reload();
                            AOS.tip(data.appmsg);
                        }
                    }
                })
            }
            function _f_apply_agree(){
                AOS.ajax({
                    forms:_f_apply_u,
                    params:{
                      'state':0,
                      'spzt':1
                    },
                    url:'updateApplyAgree.jhtml',
                    ok:function(data){
                        if(data.appcode===1){
                            _w_apply_u.hide();
                            _g_apply_store.reload();
                            AOS.tip(data.appmsg);
                        }else {
                            AOS.err(AOS.appmsg);
                        }
                    }
                })
            }
            function _f_apply_disagree(){

                AOS.ajax({
                    forms:_f_apply_u,
                    params:{
                        'state':0,
                        'spzt':0
                    },
                    url:'updateApplyAgree.jhtml',
                    ok:function(data){
                        if(data.appcode===1){
                            _w_apply_u.hide();
                            _g_apply_store.reload();
                            AOS.tip(data.appmsg);
                        }else {
                            AOS.err(AOS.appmsg);
                        }
                    }
                })
            }
            function _g_apply_del(){
                var selection = AOS.selection(_g_apply,'id_');
                if(AOS.empty(selection)){
                    AOS.tip("删除前请选中数据。。。");
                    return;
                }
                var rows = AOS.rows(_g_apply);
                var msg = AOS.merge('确定要删除选中的[{0}]条数据吗？',rows);
                AOS.confirm(msg,function(btn){
                    if(btn==='cancel'){
                        AOS.tip('删除被操作取消');
                        return;
                    }
                    AOS.ajax({
                        url:'deleteApply.jhtml',
                        params:{
                            aos_rows_: selection
                        },
                        ok:function(data){
                            AOS.tip(data.appmsg);
                            _g_apply_store.reload();
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
            function _w_apply_details(){
                var record = AOS.selectone(_g_apply);
                parent.fnaddtab('','登记详情','/make/initData.jhtml?formid='+record.data.id_);
            }
            function _w_apply_dd_show(){
                //alert('333');
                var record = AOS.selectone(_g_apply);
                if(record){
                    _w_apply_dd.show();
                    _f_apply_dd.loadRecord(record)
                }
            }
            //显示上传面板
            function _w_picture_show() {
                var user = Ext.getCmp("user").getValue();
                var record = AOS.selectone(_g_apply);
                var uploadPanel = new Ext.ux.uploadPanel.UploadPanel({
                    addFileBtnText: '选择文件...',
                    uploadBtnText: '上传',
                    deleteBtnText: '移除',
                    downBtnText: '下载',
                    downAllBtnText: '批量下载',
                    removeBtnText: '移除所有',
                    cancelBtnText: '取消上传',
                    use_query_string: true,
                    listeners: {
                        //双击
                        itemdblclick: function (grid, row, kk, rowIndex) {
                            //parent.fnaddtab(row.data.id, '电子文件',
                            //					'archive/data/openFile.jhtml?id='+row.data.pid+'&tid='+row.data.tid+'&type='+row.data.type+'&tablename='+tablename.getValue());
                            //+'&dh='+record.data.dh
                            var type = row.data.type;
                            if (type == "mp4") {
                                open();
                            } else {
                                parent.fnaddtab(row.data.pid, '电子文件',
                                    'archive/data/openPdfFile.jhtml?id=' + row.data.pid + '&tid=' + row.data.tid + '&type=' + row.data.type + '&tablename=' + record.data.tablename);
                            }

                            /*parent.fnaddtab(row.data.id, '电子文件',
					'archive/data/openFile.jhtml?id='+row.data.pid+'&tid='+row.data.tid+'&type='+row.data.type+'&tablename='+tablename.getValue()+'&index='+rowIndex);*/
                            /*parent.fnaddtab(row.data.id, '电子文件',
                                 'archive/data1/initZpData.jhtml?id='+row.data.pid+'&tid='+row.data.tid+'&type='+row.data.type+'&tablename='+tablename.getValue());*/
                        }
                    },
                    onUpload: function () {
                        if (user != "root") {
                            AOS.err("没有权限!");
                            return;
                        }
                        var me = Ext.getCmp("uploadpanel");
                        if (this.swfupload && this.store.getCount() > 0) {
                            if (this.swfupload.getStats().files_queued > 0) {
                                this.showBtn(this, false);
                                this.swfupload.uploadStopped = false;
                                this.swfupload.startUpload();
                            }
                        }
                        // this.swfupload.destroy();
                    },
                    deletePath: function (grid, rowIndex, colIndex) {
                        if (user != "root") {
                            AOS.err("没有权限!");
                            return;
                        }
                        var me = Ext.getCmp("uploadpanel").getSelectionModel().getSelection();
                        var id = me[0].get('pid');
                        var tid = me[0].get('tid');
                        var rowIndex = Ext.getCmp("uploadpanel").getStore().indexOf(me[0]);
                        AOS.ajax({
                            params: {
                                id_: id,
                                tablename: record.data.tablename,
                                tid: tid,
                                tm: record.data.tm
                            }, // 提交参数,
                            url: 'deletePath.jhtml',
                            ok: function (data) {
                                var me = Ext.getCmp("uploadpanel");
                                //me.store.reload();
                                me.store.remove(me.store.getAt(rowIndex));
                                // me.store.load();
                                AOS.tip(data.appmsg);
                            }
                        });
                    },
                    onRemoveAll: function () {
                        if (user != "root") {
                            AOS.err("没有权限!");
                            return;
                        }
                        var selection = AOS.selection(_g_apply, 'id_');
                        AOS.ajax({
                            params: {
                                aos_rows_: selection,
                                tm: record.data.tm,
                                tablename: record.data.tablename
                            },
                            url: 'deletePathAll.jhtml',
                            ok: function (data) {
                                var me = Ext.getCmp("uploadpanel");
                                me.removeAll();
                                AOS.tip(data.appmsg);
                            }
                        });
                    },
                    //下载
                    onDownPath: function (grid, rowIndex, colIndex) {
                        if (user != "root") {
                            AOS.err("没有权限!");
                            return;
                        }
                        //得到选中的条目
                        var me = Ext.getCmp("uploadpanel").getSelectionModel().getSelection();
                        var id = me[0].get('pid');
                        AOS.file('downloadPath.jhtml?id_=' + id + '&tablename=' + record.data.tablename);
                    },
                    //批量下载
                    onDownAllPath: function () {
                        if (user != "root") {
                            AOS.err("没有权限!");
                            return;
                        }
                        //得到选中的条目
                        var grid_data = Ext.getCmp("uploadpanel").getStore();
                        for (var i = 0; i < grid_data.data.length; i++) {
                            var row = grid_data.getAt(i);
                            var id = row.get('pid');
                            AOS.file('downloadPath.jhtml?id_=' + id + '&tablename=' + record.data.tablename);
                        }
                    },
                    upload_complete_handler: function (file) {
                        if (user != "root") {
                            AOS.err("没有权限!");
                            return;
                        }
                        var me = Ext.getCmp("uploadpanel");

                        AOS.ajax({
                            params: {tid: record.data.id_, tablename: record.data.tablename},
                            url: 'getPath.jhtml',
                            ok: function (data) {
                                for (i in data) {
                                    me.store.getAt(file.index).set({
                                        "pid": data[i].id_,
                                        "tid": data[i].tid,
                                        "dirname": data[i].dirname
                                    });
                                }
                            }
                        });
                    },
                    post_params: {
                        tid: record.data.tablename_id,
                        tablename: record.data.tablename
                    },
                    file_size_limit: 10000,//MB

                    flash_url: "${cxt}/static/swfupload/swfupload.swf",
                    flash9_url: "${cxt}/static/swfupload/swfupload_f9.swf",
                    upload_url: "${cxt}/archive/upload/archiveUpload.jhtml"
                });

                var w_data_path = new Ext.Window({
                    title: '电子文件',
                    width: 700,
                    modal: true,
                    closeAction: 'destroy',
                    items: [uploadPanel]
                });
                w_data_path.on("show", w_data_path_onshow);
                //w_data_path.on("close",w_data_path_onclose);
                w_data_path.show();
            }
            function w_data_path_onshow() {
                //var me = this.settings.custom_settings.scope_handler;

                var record = AOS.selectone(Ext.getCmp('_g_apply'));
                var me = Ext.getCmp("uploadpanel");
                me.store.removeAll();
                AOS.ajax({
                    params: {tid: record.data.tablename_id, tablename: record.data.tablename},
                    url: 'getPath.jhtml',
                    ok: function (data) {
                        for (i in data) {
                            me.store.add({
                                pid: data[i].id_,
                                tid: data[i].tid,
                                name: data[i]._path,
                                dirname: data[i].dirname,
                                fileName: data[i].filename,
                                type: data[i].filetype,
                                percent: 100,
                                status: -4,
                            });
                        }
                    }
                });
                //在得到当前电子文件选框的状
            }
        </script>
    </aos:onready>
</aos:html>
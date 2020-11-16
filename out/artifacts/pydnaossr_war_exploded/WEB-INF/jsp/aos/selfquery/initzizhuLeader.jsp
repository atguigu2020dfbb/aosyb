<%@page contentType="text/html; charset=utf-8" %>
<%@taglib prefix="aos" uri="/WEB-INF/tld/aos.tld" %>
<aos:html>
    <aos:head title="自助审核">
        <aos:include lib="ext,swfupload" />
        <aos:base href="utilization/data"/>
        <style>
            .my_row_red .x-grid-cell {
                background-color: #99FF99;
            }
            .grid-one-column .x-grid-cell {
                background-color: #a6caf0;
            }

        </style>
    </aos:head>
    <aos:body>
    </aos:body>
    <aos:onready>
        <aos:viewport layout="border">
            <aos:gridpanel id="_g_make" url="listZiZhuMake.jhtml" region="north" onrender="_g_make_onrender"
                           height="300"  hidePagebar="false" pageSize="10" onitemclick="get_data">
                <aos:docked>
                    <aos:dockeditem xtype="tbtext" text="利用登记"/>
                    <aos:button text="是否提供" icon="icon_19.png" scale="small" margin="0 0 0 0" id="del_table_text">
                        <aos:menu plain="false">
                            <aos:menuitem text="可以提供" icon="agree.png" onclick="_yes_provide" />
                            <aos:menuitem text="不可提供" icon="against.png"
                                          onclick="_no_provide" />
                        </aos:menu>
                    </aos:button>
                    <aos:dockeditem  text="查询结果" icon="text_cap.png" >
                        <aos:menu plain="true">
                            <aos:menuitem onclick="_no_query" text="未查到"/>
                            <aos:menuitem onclick="_yes_query" text="已查到"/>
                        </aos:menu>
                    </aos:dockeditem>
                    <aos:dockeditem onclick="_del_zizhu_task" text="删除" id="_f_del_task" icon="del.png" />
                    <aos:dockeditem text="利用日志" icon="text_list.png" onclick="_w_make_log_show"/>




                </aos:docked>
                <aos:column header="id_" dataIndex="id_" hidden="true"  locked="true"/>
                <aos:column header="自助编号" dataIndex="zzbh" />
                <aos:column header="查档者姓名" dataIndex="xm"/>
                <aos:column header="性别" dataIndex="xb"/>
                <aos:column header="民族或国籍" dataIndex="mzgj"/>
                <aos:column header="出生" dataIndex="cs"/>
                <aos:column header="出生" dataIndex="dz"/>
                <aos:column header="身份证号" dataIndex="sfzh"/>
                <aos:column header="签发机关" dataIndex="qfjg"/>
                <aos:column header="查档者姓名" dataIndex="xm"/>
                <aos:column header="是否提供" dataIndex="nftg"/>
                <aos:column header="未提供原因" dataIndex="wtgyy"/>
                <aos:column header="查旬结果" dataIndex="cxjg"   rendererFn="fn_cxjg_render"/>
                <aos:column header="是否打印" dataIndex="nfday"/>
                <aos:column header="未打印原因" dataIndex="wdyyy"/>
                <aos:column header="备注" dataIndex="bz"/>
                <aos:column header="" flex="1"/>
            </aos:gridpanel>
            <aos:gridpanel id="_g_make_detail" region="center" rowclass="true" onitemclick="onclick_data"   url="listZiZhuMakedetail.jhtml"  hidePagebar="true" pageSize="5" >
                <aos:docked>
                    <aos:dockeditem xtype="tbtext" text="档案信息"/>
                    <aos:dockeditem text="电子文件" icon="picture.png"  id="_f_filename_message"
                                    onclick="_w_picture_show" />
                </aos:docked>
                <aos:hiddenfield id="rowmath" name="rowmath" value="0" />
                <aos:column header="id_" dataIndex="id_" hidden="true"  locked="true"/>
                <aos:column header="电子文件" dataIndex="_path" hidden="true"/>
                <aos:column header="全宗单位" dataIndex="qzmc" />
                <aos:column header="档号" dataIndex="dh" width="200"/>
                <aos:column header="题名" dataIndex="tm" width="300"/>
                <aos:column header="形成时间" dataIndex="xcrq"/>
                <aos:column header="档案门类" dataIndex="tablename" hidden="true"/>
                <aos:column header="" flex="1"/>
            </aos:gridpanel>
        </aos:viewport>

        <aos:window id="_w_make_u" title="审核">
            <aos:formpanel id="_f_make_u" width="700" layout="anchor">
                <aos:hiddenfield name="id_" fieldLabel="id_" />
                <aos:textareafield name="sppz" fieldLabel="批注" allowBlank="false"/>
                <aos:docked dock="bottom" ui="footer">
                    <aos:dockeditem xtype="tbfill"/>
                    <aos:dockeditem text="同意" icon="agree.png" onclick="_f_make_agree"/>
                    <aos:dockeditem text="拒绝" icon="against.png" onclick="_f_make_disagree"/>
                    <aos:dockeditem text="关闭" icon="close.png" onclick="#_w_make_u.hide()"/>
                </aos:docked>
            </aos:formpanel>
        </aos:window>

        <aos:window title="不可提供" id="_w_provide" layout="fit"  modal="false" center="true" maximizable="true">
            <aos:formpanel id="_f_provide" layout="fit" autoScroll="true" labelWidth="70" width="500" height="200">
                <aos:textareafield name="wtgyy" fieldLabel="原因"  />
            </aos:formpanel>
            <aos:docked dock="bottom" ui="footer">
                <aos:dockeditem xtype="tbfill" />
                <aos:dockeditem xtype="button" text="保存数据" icon="ok.png" onclick="_f_provide_save"/>
                <aos:dockeditem text="取消" icon="close.png" onclick="#_w_provide.hide()"/>

            </aos:docked>
        </aos:window>
        <aos:window id="_w_make_log" title="利用日志" onshow="_w_make_log_onshow" >
            <aos:gridpanel id="_g_make_log" region="center" url="listMakeLog_make.jhtml" width="500" height="400" hidePagebar="true">
                <aos:column header="id_" dataIndex="id_" hidden="true"/>
                <aos:column header="操作时间" dataIndex="czsj" width="200"/>
                <aos:column header="操作人" dataIndex="czr"/>
                <aos:column header="内容" dataIndex="cznr"/>
                <aos:column header="" flex="1"/>
            </aos:gridpanel>
        </aos:window>
        <script type="text/javascript">
            function _w_make_log_show(){
                _w_make_log.show();
            }
            function _w_make_log_onshow(){
                var record = AOS.selectone(_g_make);
                var params={
                };
                if(record){
                    params={
                        tid:record.data.id_
                    };
                }

                _g_make_log_store.getProxy().extraParams = params;
                _g_make_log_store.load();
            }
            //删除信息
            function _del_zizhu_task() {
                var selection = AOS.selection(_g_make, 'id_');
                var dataone = AOS.selectone(_g_make);
                if (!AOS.selectone(_g_make)) {
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
                        url: 'deleteZiZhuMake.jhtml',
                        params: {
                            aos_rows_: selection
                        },
                        ok: function (data) {
                            AOS.tip(data.appmsg);
                            _g_make_store.reload();
                        }
                    });
                });
            }
            function fn_cxjg_render(value, metaData, record, rowIndex, colIndex,
                                    store){
                if (value === '0') {
                    return '未查到';
                } if(value === '1'){
                    return "已查到";
                }
            }
            function _no_query(){
                var record = AOS.selectone(_g_make);
                AOS.ajax({
                    url:'insertMakeDetail.jhtml',
                    params:{
                        formid:record.data.id_,
                        cxjg:0
                    },
                    ok:function (data) {
                        AOS.tip(data.appmsg);
                        _g_make_store.load();
                    }
                });
            }
            function _yes_query(){
                var record = AOS.selectone(_g_make);
                AOS.ajax({
                    url:'insertMakeDetail.jhtml',
                    params:{
                        formid:record.data.id_,
                        cxjg:1
                    },
                    ok:function (data) {
                        AOS.tip(data.appmsg);
                        _g_make_store.load();
                    }
                });
            }
            function _yes_provide(){
                var make_record = AOS.selectone(_g_make);
                if(!make_record) {
                    AOS.tip("请选择任务!");
                    return;
                }
                AOS.ajax({
                    url:'updateZiZhuMake.jhtml',
                    params:{
                        user:make_record.data.xm,
                        id_:make_record.data.id_,
                        nftg:'可以提供'
                    },
                    ok:function(data){
                        AOS.tip("操作完成!");

                    }
                });
            }
            function  _f_provide_save(){
                var make_record = AOS.selectone(_g_make);
                AOS.ajax({
                    url:'updateZiZhuMake.jhtml',
                    params:{
                        user:make_record.data.xm,
                        id_:make_record.data.id_,
                        nftg:'不可提供'
                    },
                    ok:function(data){

                        AOS.tip("操作完成!");
                        _w_provide.hide();
                    }
                });
            }
            function _no_provide(){
                var make_record = AOS.selectone(_g_make);
                if(!make_record) {
                    AOS.tip("请选择任务!");
                    return;
                }else{
                    _w_provide.show();
                    AOS.reset(_f_provide);
                }
            }
            //显示上传面板
            function _w_picture_show() {
                var record = AOS.selectone(_g_make_detail);
                var uploadPanel= new Ext.ux.uploadPanel.UploadPanel({
                    addFileBtnText : '选择文件...',
                    uploadBtnText : '上传',
                    deleteBtnText : '移除',
                    downBtnText   : '下载',
                    downAllBtnText:'批量下载',
                    removeBtnText : '移除所有',
                    cancelBtnText : '取消上传',
                    use_query_string : true,
                    listeners:{
                        //双击
                        itemdblclick : function(grid,row,kk,rowIndex){
                            //parent.fnaddtab(row.data.id, '电子文件',
                            //					'archive/data/openFile.jhtml?id='+row.data.pid+'&tid='+row.data.tid+'&type='+row.data.type+'&tablename='+tablename.getValue());
                            //+'&dh='+record.data.dh
                            var type=row.data.type;
                            if(type=="mp4"){
                                open();
                            }else{
                                parent.fnaddtab(row.data.pid, '电子文件',
                                    'archive/data/openPdfFile.jhtml?id='+row.data.pid+'&tid='+row.data.tid+'&type='+row.data.type+'&tablename='+record.data.tablename);
                            }

                            /*parent.fnaddtab(row.data.id, '电子文件',
                                    'archive/data/openFile.jhtml?id='+row.data.pid+'&tid='+row.data.tid+'&type='+row.data.type+'&tablename='+tablename.getValue()+'&index='+rowIndex);*/
                            /*parent.fnaddtab(row.data.id, '电子文件',
                                                 'archive/data1/initZpData.jhtml?id='+row.data.pid+'&tid='+row.data.tid+'&type='+row.data.type+'&tablename='+tablename.getValue());*/
                        }
                    },
                    onUpload : function(){
                        if(user!="root"){
                            AOS.err("没有权限!");
                            return;
                        }
                        var me=Ext.getCmp("uploadpanel");
                        if (this.swfupload&&this.store.getCount()>0) {
                            if (this.swfupload.getStats().files_queued > 0){
                                this.showBtn(this,false);
                                this.swfupload.uploadStopped = false;
                                this.swfupload.startUpload();
                            }
                        }
                        // this.swfupload.destroy();
                    },
                    deletePath:	function(grid, rowIndex, colIndex) {
                        if(user!="root"){
                            AOS.err("没有权限!");
                            return;
                        }
                        var me=Ext.getCmp("uploadpanel").getSelectionModel().getSelection();
                        var id = me[0].get('pid');
                        var tid = me[0].get('tid');
                        var rowIndex = Ext.getCmp("uploadpanel").getStore().indexOf(me[0]);
                        AOS.ajax({
                            params: {id_:id,
                                tablename:record.data.tablename,
                                tid:tid,
                                tm:record.data.tm
                            }, // 提交参数,
                            url: 'deletePath.jhtml',
                            ok: function (data) {
                                var me=Ext.getCmp("uploadpanel");
                                //me.store.reload();
                                me.store.remove(me.store.getAt(rowIndex));
                                // me.store.load();
                                AOS.tip(data.appmsg);
                            }
                        });
                    },
                    onRemoveAll: function (){
                        if(user!="root"){
                            AOS.err("没有权限!");
                            return;
                        }
                        var selection = AOS.selection(_g_make_detail, 'id_');
                        AOS.ajax({
                            params: {aos_rows_: selection,
                                tm:record.data.tm,
                                tablename: record.data.tablename
                            },
                            url: 'deletePathAll.jhtml',
                            ok: function (data) {
                                var me=Ext.getCmp("uploadpanel");
                                me.removeAll();
                                AOS.tip(data.appmsg);
                            }
                        });
                    },
                    //下载
                    onDownPath:function(grid, rowIndex, colIndex){
                        if(user!="root"){
                            AOS.err("没有权限!");
                            return;
                        }
                        //得到选中的条目
                        var me=Ext.getCmp("uploadpanel").getSelectionModel().getSelection();
                        var id = me[0].get('pid');
                        AOS.file('downloadPath.jhtml?id_='+id+'&tablename='+record.data.tablename);
                    },
                    //批量下载
                    onDownAllPath:function(){
                        if(user!="root"){
                            AOS.err("没有权限!");
                            return;
                        }
                        //得到选中的条目
                        var grid_data=Ext.getCmp("uploadpanel").getStore();
                        for(var i= 0 ;i< grid_data.data.length;i++){
                            var row = grid_data.getAt(i);
                            var id=row.get('pid');
                            AOS.file('downloadPath.jhtml?id_='+id+'&tablename='+record.data.tablename);
                        }
                    },
                    upload_complete_handler : function(file){
                        if(user!="root"){
                            AOS.err("没有权限!");
                            return;
                        }
                        var me =Ext.getCmp("uploadpanel");

                        AOS.ajax({
                            params: {tid: record.data.id_,tablename:record.data.tablename},
                            url: 'getPath.jhtml',
                            ok: function (data) {
                                for(i in data){
                                    me.store.getAt(file.index).set({"pid":data[i].id_,"tid":data[i].tid,"dirname":data[i].dirname});
                                }
                            }
                        });
                    },
                    post_params:{tid:record.data.id_,
                        tablename:record.data.tablename
                    },
                    file_size_limit : 10000,//MB

                    flash_url : "${cxt}/static/swfupload/swfupload.swf",
                    flash9_url : "${cxt}/static/swfupload/swfupload_f9.swf",
                    upload_url : "${cxt}/archive/upload/archiveUpload.jhtml"
                });

                var w_data_path = new Ext.Window({
                    title : '电子文件',
                    width : 700,
                    modal:true,
                    closeAction : 'destroy',
                    items:[uploadPanel]
                });
                w_data_path.on("show",w_data_path_onshow);
                w_data_path.on("close",w_data_path_onclose);
                w_data_path.show();
            }
            function remember_load(tablename,type,value,name){
                AOS.ajax({
                    params: {
                        tablename:tablename,
                        type:type,
                        flag:value,
                        name:name
                    }, // 提交参数,
                    url: 'saveRemember.jhtml',
                    ok: function (data) {

                    }
                });
            }
            function w_data_path_onshow() {
                //var me = this.settings.custom_settings.scope_handler;

                var record = AOS.selectone(Ext.getCmp('_g_make_detail'));
                var me=Ext.getCmp("uploadpanel");
                me.store.removeAll();
                AOS.ajax({
                    params: {tid: record.data.id_,tablename:record.data.tablename},
                    url: 'getPath.jhtml',
                    ok: function (data) {
                        for(i in data){
                            me.store.add({
                                pid:data[i].id_,
                                tid:data[i].tid,
                                name:data[i]._path,
                                dirname:data[i].dirname,
                                fileName:data[i].filename,
                                type:data[i].filetype,
                                percent:100,
                                status:-4,
                            });
                        }
                    }
                });
                //在得到当前电子文件选框的状态
                AOS.ajax({
                    params: {
                        tablename:Ext.getCmp("tablename").getValue(),
                        type:"checkbox"
                    }, // 提交参数,
                    url: 'getRemember.jhtml',
                    ok: function (data) {
                        /*if(data.ocr==""||typeof(data.ocr) == "undefined"){
                            Ext.getCmp("ocr").setValue(false);
                        }else{
                            Ext.getCmp("ocr").setValue(data.ocr);
                        }
                        if(data.mark==""||typeof(data.mark) == "undefined"){
                            Ext.getCmp("mark").setValue(false);
                        }else{
                            Ext.getCmp("mark").setValue(data.mark);
                        }*/
                    }
                });
            }
            function w_data_path_onclose(){
                //_g_data_store.load();
            }
            function changeRowClass(record, rowIndex, rowParams, store){

                //得到当前行的指定的列的值
                if(record.get("_path")!=""&&record.get("_path")!=null){

                    return 'grid-one-column';
                }else{
                    return 'grid-zero-column';
                }

            }
            function onclick_data(grid, rowIndex, columnIndex, e){
                //点击之前获取当前行的初始颜色，
                //var beginColor=grid.all.elements[e].cells[i].style.backgroundColor;
                var k=Ext.getCmp("rowmath").getValue();
                for(var j=0;j<grid.all.elements[Ext.getCmp("rowmath").getValue()].cells.length;j++){
                    grid.all.elements[Ext.getCmp("rowmath").getValue()].cells[j].style="text-decoration:none";
                }
                var tt=grid.all.elements[e].cells.length;
                for(var i=0;i<tt;i++){
                    grid.all.elements[e].cells[i].style.backgroundColor="#68838B";
                    //此时把当前单元格存到缓冲中
                    Ext.getCmp("rowmath").setValue(e);
                }
            }
            function get_data(grid, rowIndex, columnIndex, e){


                    var params = {
                        table_desc_ : ""
                    };
                    var record = AOS.selectone(_g_make);
                    if (!AOS.empty(record)) {
                        params.formid = record.data.id_;
                    }
                    _g_make_detail_store.getProxy().extraParams = params;
                    _g_make_detail_store.load();
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
                        url:'deleteMake.jhtml',
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

            function _w_make_details(){
                var record = AOS.selectone(_g_make);


                parent.fnaddtab('','登记详情','/make/initData.jhtml?formid='+record.data.id_);

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
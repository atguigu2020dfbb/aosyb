<%@page contentType="text/html; charset=utf-8" %>
<%@taglib prefix="aos" uri="/WEB-INF/tld/aos.tld" %>
<aos:html>
    <aos:head title="利用出库">
        <aos:include lib="ext,swfupload"/>
        <aos:base href="depot/depotOut"/>
    </aos:head>
    <aos:body>
    </aos:body>
    <aos:onready>
        <aos:viewport layout="border">
            <aos:gridpanel id="_g_utilization" url="listDepotLy.jhtml" region="north" height="350" onrender="_g_utilization_onrender" onitemclick="_g_make_detail_query">
                <aos:docked>
                    <aos:hiddenfield id="rowmath" name="rowmath" value="0" />
                    <aos:hiddenfield id="user" name="user" value="${user}" />
                    <aos:dockeditem xtype="tbtext" text="利用登记"/>
                    <aos:dockeditem text="办理" icon="icon_19.png" onclick="_w_utilization_u_show"/>
                    <aos:dockeditem text="详细信息" icon="query.png" onclick="_w_advance_u_show"/>
                    <aos:dockeditem onclick="_g_make_del" text="删除" id="_f_del_task" icon="del.png" />
                    <aos:dockeditem text="入库" icon="left.png" onclick="_w_add_in_show"/>
                </aos:docked>
                <aos:column header="id_" dataIndex="id_" hidden="true" locked="true"/>
                <aos:column header="登记编号" dataIndex="djbh"/>
                <aos:column header="姓名" dataIndex="xm"/>
                <aos:column header="登记日期" dataIndex="djrq"/>
                <aos:column header="出库单号" dataIndex="ckdh"/>
                <aos:column header="出库人" dataIndex="ckr"/>
                <aos:column header="出库时间" dataIndex="cksj"/>
                <aos:column header="出库原因" dataIndex="ckyy"/>
                <aos:column header="拟归还时间" dataIndex="nghsj"/>
                <aos:column header="出库备注" dataIndex="ckbz"/>
                <aos:column header="出库状态" dataIndex="ckzt" rendererFn="fn_ckzt_render"/>
                <aos:column header="利用状态" dataIndex="lyzt" rendererFn="fn_lyzt_render"/>
                <aos:column header="利用方式" dataIndex="lyfs"/>
                <aos:column header="利用类型" dataIndex="lylx"/>
                <aos:column header="利用类别" dataIndex="lylb"/>
                <aos:column header="利用数量" dataIndex="lysl"/>
                <aos:column header="利用目的" dataIndex="lymd"/>
                <aos:column header="备注" dataIndex="bz"/>
                <aos:column header="审核状态" dataIndex="spzt" rendererFn="fn_spzt_render"/>

                <aos:column header="" flex="1"/>
            </aos:gridpanel>
            <aos:gridpanel id="_g_make_detail" region="center" url="listMakedetail.jhtml"  hidePagebar="false" pageSize="5" >
                <aos:docked>
                    <aos:dockeditem text="图片" icon="picture.png"  id="_f_filename_message"
                                    onclick="_w_picture_show" />
                </aos:docked>
                <aos:column header="id_" dataIndex="id_" hidden="true"  locked="true"/>
                <aos:column header="全宗单位" dataIndex="qzmc" locked="true"/>
                <aos:column header="档号" dataIndex="dh" width="200"/>
                <aos:column header="题名" dataIndex="tm" width="300"/>
                <aos:column header="形成时间" dataIndex="xcrq"/>
                <aos:column header="表明" dataIndex="tablename" hidden="true"/>
                <aos:column header="" flex="1"/>
            </aos:gridpanel>
        </aos:viewport>

        <aos:window id="_w_utilization_u" title="出库" >
            <aos:formpanel id="_f_utilization_u" width="700" layout="anchor">
                <aos:hiddenfield name="id_" fieldLabel="id_" />
                <aos:textfield name="ckdh" fieldLabel="出库单号"/>
                <aos:datefield name="nghsj" fieldLabel="拟归还时间" editable="false"/>
                <aos:combobox fieldLabel="库房名称" name="kfmc"   allowBlank="false" onselect="_w_uilization_onshow">
                    <aos:option value="历史库房301" display="历史库房301"/>
                    <aos:option value="资料库房302" display="资料库房302"/>
                    <aos:option value="现行库房401" display="现行库房401"/>
                    <aos:option value="现行库房402" display="现行库房402"/>
                </aos:combobox>
                <aos:textfield name="cksl" fieldLabel="出库数量"/>
                <aos:textareafield name="ckyy" fieldLabel="出库原因"/>
                <aos:textareafield name="ckbz" fieldLabel="备注"/>
                <aos:docked dock="bottom" ui="footer">
                    <aos:dockeditem xtype="tbfill"/>
                    <aos:dockeditem text="出库" icon="icon85.png" onclick="_f_utilization_ck"/>
                    <aos:dockeditem text="关闭" icon="close.png" onclick="#_w_utilization_u.hide()"/>
                </aos:docked>
            </aos:formpanel>
        </aos:window>

        <aos:window id="_w_make_u" title="登记详情">
            <aos:formpanel id="_f_make_u" width="500" layout="column">
                <aos:hiddenfield name="id_"  />
                <aos:hiddenfield name="users"/>
                <aos:textfield name="djbh" columnWidth="0.98" fieldLabel="登记编号" />
                <aos:textfield name="xm" columnWidth="0.98" fieldLabel="姓名" />
                <aos:textfield name="djrq" columnWidth="0.98" fieldLabel="登记日期" />
                <aos:textfield name="ckdh" columnWidth="0.98" fieldLabel="出库单号" />
                <aos:textfield name="ckr" columnWidth="0.98" fieldLabel="出库人" />
                <aos:textfield name="cksj" columnWidth="0.98" fieldLabel="出库时间" />
                <aos:textfield name="nghsj" columnWidth="0.98" fieldLabel="拟归还时间" />
                <aos:textfield name="ckzt" columnWidth="0.98" fieldLabel="出库状态" />
                <aos:textfield name="lyzt" columnWidth="0.98" fieldLabel="利用状态" />
                <aos:textareafield name="ckyy" columnWidth="0.98" fieldLabel="出库原因" />
                <aos:textareafield name="ckbz" columnWidth="0.98" fieldLabel="出库备注" />
                <aos:docked dock="bottom" ui="footer">
                    <aos:dockeditem xtype="tbfill"/>
                    <aos:dockeditem text="关闭" icon="close.png" onclick="#_w_make_u.hide()"/>
                </aos:docked>
            </aos:formpanel>
        </aos:window>

        <script type="text/javascript">

            //显示上传面板
            function _w_picture_show() {
                var user = Ext.getCmp("user").getValue();
                var record = AOS.selectone(_g_make_detail);
                var tablename=AOS.selectone(_g_make_detail).data.tablename;
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
                                    'archive/data/openPdfFile.jhtml?id=' + row.data.pid + '&tid=' + row.data.tid + '&type=' + row.data.type + '&tablename=' + tablename);
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
                                tablename: tablename,
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
                        var selection = AOS.selection(_g_data, 'id_');
                        AOS.ajax({
                            params: {
                                aos_rows_: selection,
                                tm: record.data.tm,
                                tablename: tablename
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
                        AOS.file('downloadPath.jhtml?id_=' + id + '&tablename=' + tablename);
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
                            AOS.file('downloadPath.jhtml?id_=' + id + '&tablename=' + tablename);
                        }
                    },
                    upload_complete_handler: function (file) {
                        if (user != "root") {
                            AOS.err("没有权限!");
                            return;
                        }
                        var me = Ext.getCmp("uploadpanel");

                        AOS.ajax({
                            params: {tid: record.data.id_, tablename: tablename},
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
                        tid: record.data.id_,
                        tablename: tablename
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

            function remember_load(tablename, type, value, name) {
                AOS.ajax({
                    params: {
                        tablename: tablename,
                        type: type,
                        flag: value,
                        name: name
                    }, // 提交参数,
                    url: 'saveRemember.jhtml',
                    ok: function (data) {

                    }
                });
            }

            function w_data_path_onshow() {
                //var me = this.settings.custom_settings.scope_handler;
                var tablename=AOS.selectone(_g_make_detail).data.tablename;
                var record = AOS.selectone(Ext.getCmp('_g_make_detail'));
                var me = Ext.getCmp("uploadpanel");
                me.store.removeAll();
                AOS.ajax({
                    params: {tid: record.data.id_, tablename: tablename},
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
                //在得到当前电子文件选框的状态
                AOS.ajax({
                    params: {
                        tablename: tablename,
                        type: "checkbox"
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

            function w_data_path_onclose() {
                _g_data_store.load();
            }




            //查询数据表列信息
            function _g_make_detail_query(grid, rowIndex, columnIndex, e){
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

                var params = {
                    table_desc_ : ""
                };
                var record = AOS.selectone(_g_utilization);
                if (!AOS.empty(record)) {
                    params.formid = record.data.id_;
                    params.ckzt=record.data.ckzt;
                    params.yngh=record.data.yngh;
                }
                _g_make_detail_store.getProxy().extraParams = params;
                _g_make_detail_store.load();

            }
            function _g_make_del(){
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
                        url:'deleteDepotLy.jhtml',
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
            function _w_advance_u_show(){
                var record = AOS.selectone(_g_utilization);
                if(record){
                    _w_make_u.show();
                    _f_make_u.loadRecord(record);
                }
            }
            function _g_utilization_onrender(){
                var params={
                    'state':'0'
                };
                _g_utilization_store.getProxy().extraParams=params;
                _g_utilization_store.load();
            }
            function _w_utilization_show(){
                _w_utilization.show();
            }
            function _w_utilization_u_show(){
                var record = AOS.selectone(_g_utilization);
                if(record){
                    var ckzt=record.data.ckzt;
                    if("1"==ckzt){
                        AOS.tip("已经出库!");
                        return;
                    }else if("2"==ckzt){
                        AOS.tip("已经入库!");
                        return;
                    }else{
                    _w_utilization_u.show();
                    AOS.reset(_f_utilization_u);
                    _f_utilization_u.loadRecord(record);
                    }
                }
            }
            function _w_add_in_show(){
                var record = AOS.selectone(_g_utilization);
                if(record){
                    var ckzt=record.data.ckzt;
                    var lx="";
                    var ckdh= record.data.ckdh;
                    if(ckdh!=null&&ckdh!=""){
                        lx=ckdh.substring(0,3);
                    }
                    if("0"==ckzt){
                        AOS.tip("未出库,无须入库!");
                        return;
                    }
                    if("2"==ckzt){
                        AOS.tip("已经入库了!");
                        return;
                    }
                    else{
                        AOS.ajax({
                            params:{id_:record.data.id_,
                            lx:lx},
                            url:'addDepotIn_ly.jhtml',
                            ok:function (data){
                                if(data.appcode == 1){
                                    AOS.err("操作成功！");
                                }else {
                                    _g_utilization_store.reload();
                                    AOS.err("操作失败！");
                                }
                            }
                        });
                    }
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
            function _f_utilization_ck(){
                var lx= _f_utilization_u.getForm().findField('kfmc').getValue();
                if("历史库房301"==lx){
                    lx="301";
                }else if("资料库房302"==lx){
                    lx="302";
                }else if("现行库房401"==lx){
                    lx="401";
                }else if("现行库房402"==lx){
                    lx="402";
                }else{

                }
                AOS.ajax({
                    forms:_f_utilization_u,
                    params:{
                        'ckbs':1,
                        'ckzt':1,
                        lx:lx
                    },
                    url:'updateUtilizationCk.jhtml',
                    ok:function(data){
                        if(data.appcode===1){
                            _w_utilization_u.hide();
                            _g_utilization_store.reload();
                            AOS.tip(data.appmsg);
                        }else {
                            AOS.err(AOS.appmsg);
                        }
                    }
                });
            }
            function _f_utilization_disagree(){

                AOS.ajax({
                    forms:_f_utilization_u,
                    params:{
                        'state':0,
                        'spzt':0
                    },
                    url:'updateUtilizationAgree.jhtml',
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
                }
                else {
                    return '待审核';
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
            function _w_uilization_onshow() {
                var lx= _f_utilization_u.getForm().findField('kfmc').getValue();
                if("历史库房301"==lx){
                    lx="301";
                }else if("资料库房302"==lx){
                    lx="302";
                }else if("现行库房401"==lx){
                    lx="401";
                }else if("现行库房402"==lx){
                    lx="402";
                }else{
                }
                AOS.ajax({
                    params:{name_:"出库流水号",lx:lx},
                    url:'getDepotOutIndex.jhtml',
                    ok:function(data){
                        //设计一个随机数编号
                        //年
                        var time = (new Date).getTime();
                        var yesday = new Date(time); // 获取的是年
                        yesday = yesday.getFullYear()  +"";
                        _f_utilization_u.form.findField("ckdh").setValue(lx+yesday+data.index);
                    }
                });
            }
        </script>
    </aos:onready>
</aos:html>
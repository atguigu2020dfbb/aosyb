<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="/WEB-INF/tld/aos.tld" prefix="aos"%>
<%@ taglib uri="/WEB-INF/tld/cll.tld" prefix="c"%>
<aos:html>
    <aos:head title="编研成果">
        <aos:include lib="ext,swfupload" />
        <aos:base href="compilation/result" />
        <aos:include js="${cxt}/static/pdfObject/pdfobject.js" />
        <aos:include css="${cxt}/static/weblib/bootstrap/css/bootstrap.min.css"/>
        <aos:include css="${cxt}/static/css/fileinput.min.css"/>
        <aos:include js="${cxt}/static/js/jquery-3.2.1.min.js"/>
        <aos:include js="${cxt}/static/weblib/bootstrap/js/bootstrap.min.js"/>
        <aos:include js="${cxt}/static/js/fileinput.min.js"/>
        <aos:include js="${cxt}/static/js/zh.js"/>
    </aos:head>
    <aos:body>
        <!--html代码  -->
        <div id="documentViewer" style="height: 600px;"></div>
        <div id="filediv">
            <input id="file" class="file-loading" name="file" type="file" multiple class="file-loading">
        </div>
        <video id="videolook" controls="controls" hidden="true" height="500" preload="autoplay"
               width="690">
        </video>
    </aos:body>
    <aos:onready>
        <aos:viewport layout="border" id="viewport">
            <aos:gridpanel id="_g_compilation" url="listCompilation.jhtml"  region="north"  splitterBorder="1 0 1 0" split="true"
                           onrender="_g_compilation_query" pageSize="10"  onitemclick="get_a_compilation"   enableLocking="true">
                <aos:docked>
                <aos:hiddenfield id="appraisal" name="appraisal"/>
                <aos:hiddenfield name="nd" id="nd"
                                 value="${nd}" />
                <aos:hiddenfield  name="_path" id="_path"/>
                <aos:hiddenfield name="_classtree" id="_classtree"
                                 value="${cascode_id_}" />
                <aos:hiddenfield name="aos_module_id_" id="aos_module_id_"
                                 value="${aos_module_id_}" />
                <aos:dockeditem text="撰稿详情" icon="edit.png" id="_zhuangao_operator"
                                onclick="zhuangao_message_" />
                <aos:dockeditem text="合稿详情" icon="more/arrow-merge.png" id="_first_operator"
                                onclick="hegao_message_" />
                    <aos:dockeditem text="上传附件" id="openExcel" icon="more/go-top-8.png" onclick="_fileupload_show"/>
                    <aos:dockeditem text="打开附件" id="openFile" icon="folder2.png" onclick="open_path_"/>
            </aos:docked>
                <aos:selmodel type="row" mode="multi" />
                <aos:column dataIndex="id_" header="流水号" hidden="true" />
                <aos:column dataIndex="zt_id" header="专题id" hidden="true" />
                <aos:column dataIndex="byrwbh" header="编研任务编号" width="150"/>
                <aos:column dataIndex="byrwmc" header="编研任务名称" />
                <aos:column dataIndex="ztyq" header="总体要求" />
                <aos:column dataIndex="bysj" header="编研时间" />
                <aos:column dataIndex="ckfw" header="参考范围"  />
                <aos:column dataIndex="bybz" header="编研步骤"  />
                <aos:column dataIndex="nd" header="年度"  />
                <aos:column dataIndex="zgr"  header="撰稿人"/>
                <aos:column dataIndex="zgr_cn"  header="撰稿人中文"/>
                <aos:column dataIndex="hgr"  header="合稿人"/>
                <aos:column dataIndex="hgr_cn"  header="合稿人中文"/>
                <aos:column dataIndex="jdr" header="校对人"  />
                <aos:column dataIndex="jdr_cn" header="校对人中文"  />
                <aos:column dataIndex="zbj" header="总编辑" />
                <aos:column dataIndex="zbj_cn" header="总编辑中文" />
                <aos:column dataIndex="qtsm" header="其他说明" />
                <aos:column dataIndex="shry" header="审核人"  />
                <aos:column dataIndex="shsj" header="审核时间"  />
                <aos:column dataIndex="shyj" header="审核意见"  />
                <aos:column dataIndex="bycg_path" header="编研成果"  />
                <aos:column dataIndex="hg_time" header="合稿时间" />
                <aos:column dataIndex="jd_time" header="校对时间" />
                <aos:column dataIndex="zbj_time" header="定稿时间" />
                <aos:column dataIndex="flag_submit" header="提交状态" />
                <aos:column dataIndex="flag_examine" header="审核状态" />
                <aos:column dataIndex="cjr" header="创建人" />

                <aos:column header="" flex="1" />
            </aos:gridpanel>
            <aos:gridpanel id="_g_data" url="listAccounts.jhtml"  region="center" split="true"
                            pageSize="${pagesize }"  enableLocking="true">
                <aos:docked>
                    <aos:dockeditem xtype="tbtext" text="编研数据库数据" />
                    <aos:dockeditem text="显示" icon="picture.png"  id="_f_filename_message"
                                    onclick="open_path_"   />
                </aos:docked>
                <aos:selmodel type="row" mode="multi" />
                <aos:column dataIndex="id_" header="流水号" hidden="true" />
                <aos:column dataIndex="_path" header="电子文件"
                            rendererFn="fn_path_render" />
                <aos:column header="全宗单位" dataIndex="qzdw" />
                <aos:column header="档号" dataIndex="dh" celltip="true" />
                <aos:column header="题名" dataIndex="tm"  width="80" />
                <aos:column header="年度" dataIndex="nd" />
                <aos:column header="保管期限" dataIndex="bgqx" />
                <aos:column header="形成时间" dataIndex="xcsj"  />
                <aos:column header="盒号" dataIndex="hh" />
                <aos:column header="备注" dataIndex="bz" flex="1" celltip="true" />
                <aos:column header="" flex="1" />
            </aos:gridpanel>




            <aos:window id="_w_operation" title="操作记录" autoScroll="true" height="500" width="800" onshow="_w_operation_show">
                <aos:gridpanel id="_g_operation"  url="listoperationlogin.jhtml"
                               hidePagebar="true"  width="5000" autoScroll="true">
                    <aos:docked>
                        <aos:dockeditem xtype="tbtext" text="列表" />
                        <aos:dockeditem onclick="_w_details_open" text="详情" icon="query.png" />
                    </aos:docked>
                    <aos:column type="rowno" />
                    <aos:column  dataIndex="id_" hidden="true" />
                    <aos:column header="操作人" dataIndex="users" width="200" />
                    <aos:column header="操作时间" dataIndex="operate_time" width="200" />
                    <aos:column header="操作描述" dataIndex="operate_description" width="200" />
                    <aos:column header="操作信息" dataIndex="compilation_message" width="200" />
                    <aos:column header="操作类型" dataIndex="type" width="200" />
                    <aos:column header="" flex="1" />
                </aos:gridpanel>
                <aos:docked dock="bottom" ui="footer">
                    <aos:dockeditem xtype="tbfill" />
                    <aos:dockeditem onclick="#_w_operation.close();" text="关闭" icon="close.png" />
                </aos:docked>
            </aos:window>

            <aos:window id="_w_open_path" title="全文信息" width="600" height="600" onshow="load_path_" >
                <aos:panel id="documentViewer3"   height="600" split="true" width="600" contentEl="documentViewer">

                </aos:panel>
            </aos:window>
            <aos:window id="_w_hegao_message" title="合稿信息" width="600" height="600" onshow="load_hegao_message" >
                <aos:formpanel id="_f_description_hegao"  layout="column"  width="590">
                    <aos:htmleditor fieldLabel="合稿信息" anchor="80%" height="500" labelAlign="top"  name="compilation_hegao_message" id="compilation_hegao_message" columnWidth="1" allowBlank="false"/>
                </aos:formpanel>
            </aos:window>
        <aos:window id="_w_zhuangao_grid" title="撰稿信息" width="600" height="400" autoScroll="true" onshow="_g_account_query">
            <aos:gridpanel id="_g_account" url="listZg_hegao.jhtml" region="north" split="true" pageSize="${pagesize }" hidePagebar="true"
                           enableLocking="false"  width="2000" >
                <aos:docked>
                    <aos:dockeditem xtype="tbtext" text="撰稿" />
                    <aos:dockeditem text="撰稿详情" icon="query.png" id="open_zhuangao_operator"
                                    onclick="open_zhuangao_message_" />
                </aos:docked>
                <aos:selmodel type="row" mode="multi" />
                <aos:column type="rowno" />
                <aos:column dataIndex="id_" header="流水号" hidden="true" />
                <aos:column dataIndex="rw_id_"  header="任务名称" hidden="true"  />
                <aos:column dataIndex="tablename"  header="表明"  />
                <aos:column dataIndex="username"  header="撰稿人"  />
                <aos:column dataIndex="zg_path"  header="撰稿路径" hidden="true"  />
                <aos:column dataIndex="zg_description"  header="撰稿描述"  width="400" />
                <aos:column dataIndex="hg_description"  header="合稿描述"  width="400" />
                <aos:column dataIndex="jd_description"  header="校对描述"  width="400" />
                <aos:column dataIndex="zbj_description"  header="总编辑描述"  width="400" />
                <aos:column dataIndex="operate_time"  header="操作时间"  />
                <aos:column header="" flex="1" />
            </aos:gridpanel>
        </aos:window>
            <%--上传文件--%>
            <aos:window id="_fileupload_add" title="上传文件" width="700" height="450" autoScroll="true">
                <aos:formpanel width="680" height="450" id="formpanell" layout="column" contentEl="filediv"
                               autoScroll="true">
                </aos:formpanel>
                <aos:docked dock="bottom" ui="footer">
                    <aos:dockeditem xtype="tbfill"/>
                    <aos:dockeditem onclick="#_fileupload_add.hide();" text="关闭" icon="close.png"/>
                </aos:docked>
            </aos:window>
            <aos:window id="_w_zhuangao_message" title="撰稿信息" width="600" height="600" onshow="load_zhuangao_message" >
                <aos:formpanel id="_f_description_zhuangao"  layout="column"  width="590">
                    <aos:htmleditor fieldLabel="合稿信息" anchor="80%" height="500" labelAlign="top"  name="compilation_zhuangao_message" id="compilation_zhuangao_message" columnWidth="1" allowBlank="false"/>
                </aos:formpanel>
            </aos:window>
        </aos:viewport>
        <script type="text/javascript">
            function load_path_(){
                document.getElementById('documentViewer3').innerHTML = "";
                var id_=AOS.selectone(_g_compilation).raw.id_;
                var filename=AOS.selectone(_g_compilation).raw.bycg_path;
                AOS.ajax({
                    params : {
                        id_ : id_,
                        filename:filename
                    },
                    url : 'getChengGuopath.jhtml',
                    ok : function(data) {
                        //将id值赋给hiddlen中
                        if (data.pdfpath == "" || data.pdfpath == null) {
                            Ext.getCmp("_path").setValue("");
                            document.getElementById('documentViewer3').innerHTML = "";
                        } else {
                            //new PDFObject({ url:data.pdfpath}).embed("documentViewer");

                            Ext.getCmp("_path").setValue(data.pdfpath);
                            PDFObject.embed(data.pdfpath, '#documentViewer3');
                        }
                    }
                });
            }
            //< onclick="_select_edit_archive" icon="more/select.png"   columnWidth="0.29"/>-->
            function open_path_(){
                var selection = AOS.selection(_g_compilation, 'id_');
                if (AOS.empty(selection)) {
                    AOS.tip('查看全文前请先选中数据。');
                    return;
                }

                _w_open_path.show();
            }
            //弹出上传文件窗口
            function _fileupload_show() {
                //我得加个重置
                var record = AOS.selection(_g_compilation, 'id_');
                if (AOS.empty(record)) {
                    AOS.tip('请选择要上传的条目!');
                    return;
                } else {
                    //初始化方法
                    $("#file").fileinput('destroy');
                    //edit_image();
                    Ext.getCmp("_fileupload_add").show();
                    $("#file").fileinput({
                        language: 'zh', //设置语言
                        uploadUrl: '${cxt}/archive/upload/uploadSubject_cg.jhtml', //上传的地址
                        uploadAsync: true,
                        uploadExtraData: {
                            id_: AOS.selectone(_g_compilation).data.id_
                        },
                        fileActionSettings: {
                            showZoom: false//显示预览按钮
                        },
                        allowedFileExtensions: ['pdf'],//接收的文件后缀
                        showRemove: true,//显示删除按钮
                        showUpload: true, //是否显示上传按钮
                        showCaption: true,//是否显示标题
                        browseClass: "btn btn-primary btn-xs", //按钮样式
                        removeClass: "btn btn-danger btn-xs",
                        uploadClass: "btn btn-success btn-xs",
                        dropZoneEnabled: true,//是否显示拖拽区域
                        //dropZoneTitle: "可以将文件拖放到这里",//拖拽区域显示文字
                        maxFileSize: 0,//单位为kb，如果为0表示不限制文件大小
                        minFileCount: 0,//表示允许同时上传的最小文件个数
                        maxFileCount: 1000,//表示允许同时上传的最大文件个数
                        hideThumbnailContent: true //在缩略图预览中隐藏图像，pdf，文本或其他内容---默认false-不隐藏
                    });
                }
            }
            function _w_details_open(){
                _w_zhuangao_compilation_message.show();
            }



            function _w_select_archive_onshow(){
                var params = {
                    tablename : Ext.getCmp("sjkmc").value
                };
                //这个Store的命名规则为：表格ID+"_store"。

                _g_select_archive_store.getProxy().extraParams = params;
                _g_select_archive_store.load();
            }

            function _w_select_edit_archive_onshow(){

                var id_ = AOS.selectone(_g_compilation).data.id_;
                var sjkmc = AOS.selectone(_g_compilation).data.sjkmc;
                var params = {
                    tablename : sjkmc,
                    id_ : id_
                };
                //这个Store的命名规则为：表格ID+"_store"。
                _g_select_edit_archive_store.getProxy().extraParams = params;
                _g_select_edit_archive_store.load();
            }
            function _g_add_data(){
                //1.判断是否选中了任务
                var selection = AOS.selection(_g_compilation, 'id_');
                if (AOS.empty(selection)) {
                    AOS.tip('请先选中任务。');
                    return;
                }
                //2.弹出选择数据页面
                //根据选择的名称
                var sjkmc_value = AOS.selection(_g_compilation, 'sjkmc');
                //弹出选择档案窗口
                _w_select_edit_archive.show();
            }
            //加载表格数据
            function _g_data_query() {
                var params = {
                    tablename : tablename.getValue(),
                    cascode_id_:cascode_id_.getValue()
                };
                //这个Store的命名规则为：表格ID+"_store"。
                _g_data_store.getProxy().extraParams = params;
                _g_data_store.load();
            }
            function get_a_compilation(){
                var record = AOS.selectone(_g_compilation);
                var params = {
                    zt_id:record.data.zt_id
                };
                //这个Store的命名规则为：表格ID+"_store"。
                _g_data_store.getProxy().extraParams = params;
                _g_data_store.load();
            }


            //查询窗口展开
            function _w_query_show() {
                //判断是不是
                _w_query_q.show();
            }
            function _w_query_from_show() {
                //判断是不是
                _w_query_q_from.show();
            }

            //_path列转换
            function fn_path_render(value, metaData, record, rowIndex, colIndex,
                                    store) {
                if (value >= 1) {
                    return '<img src="${cxt}/static/icon/picture.png" />';
                } else {
                    return '<img src="${cxt}/static/icon/picture-empty.png" />';
                }
            }
            function _select_edit_archive(){
                //根据选择的名称
                //弹出选择档案窗口
                _w_select_edit_archive.show();
            }
            function fn_button_render(){
                return '<input type="button" value="详情" class="cbtn" onclick="_w_details_show(this);" />';
            }

            function operation_login(){
                var selection = AOS.selection(_g_compilation, 'id_');
                if (AOS.empty(selection)) {
                    AOS.tip('请选择要查看操作记录的任务!');
                    return;
                }else {
                    _w_operation.show();
                }
            }
            function _w_operation_show(){
                //var selection = AOS.selectone(_g_compilation);
                var id_=AOS.selectone(_g_compilation).raw.id_;
                var params = {
                    pid: id_
                };
                //这个Store的命名规则为：表格ID+"_store"。
                _g_operation_store.getProxy().extraParams = params;
                _g_operation_store.load();
            }
            //加载数据,（批次号列表信息）
            function _g_compilation_query() {
                var params = {
                    aos_module_id_ : aos_module_id_.getValue(),
                    nd:nd.getValue()
                };
                //这个Store的命名规则为：表格ID+"_store"。
                _g_compilation_store.getProxy().extraParams = params;
                _g_compilation_store.load();
            }
            function zhuangao_message_(){
                var selection = AOS.selection(_g_compilation, 'id_');
                if (AOS.empty(selection)) {
                    AOS.tip('请选择要查看的任务!');
                    return;
                }
                _w_zhuangao_grid.show();
            }
            function hegao_message_(){
                var selection = AOS.selection(_g_compilation, 'id_');
                if (AOS.empty(selection)) {
                    AOS.tip('请选择要查看的任务!');
                    return;
                }
                _w_hegao_message.show();
            }
            function load_hegao_message(){
                var topic_id_=AOS.selectone(_g_compilation).raw.id_;
                AOS.ajax({
                    params: {
                        user:"<%=session.getAttribute("user")%>",
                        topic_id_: topic_id_
                    },
                    url: 'gethegaomessage.jhtml',
                    ok: function (data) {
                        Ext.getCmp("compilation_hegao_message").setValue(data.hegaomessage);
                    }
                });
            }
            function _g_account_query(){
                var topic_id_=AOS.selectone(_g_compilation).raw.id_;
                var tablename=AOS.selectone(_g_compilation).raw.tablename;
                var params = {
                    topic_id_: topic_id_,
                    tablename:tablename
                };
                //这个Store的命名规则为：表格ID+"_store"。
                _g_account_store.getProxy().extraParams = params;
                _g_account_store.load();
            }
            function open_zhuangao_message_(){
                var selection = AOS.selection(_g_account, 'id_');
                if (AOS.empty(selection)) {
                    AOS.tip('请选择要查看的撰稿!');
                    return;
                }
                _w_zhuangao_message.show();
            }
            function load_zhuangao_message(){
                var record = AOS.selectone(_g_account);
                var zg_path=record.data.zg_path;
                AOS.ajax({
                    params: {
                        zg_path: zg_path
                    },
                    url: 'getzhuangaomessage.jhtml',
                    ok: function (data) {
                        Ext.getCmp("compilation_zhuangao_message").setValue(data.appmsg);
                    }
                });
            }
            window.onload=function() {
                Ext.getCmp("_g_compilation").setHeight(document.body.scrollHeight * (1/ 2));
                Ext.getCmp("_g_data").setHeight(document.body.scrollHeight * (1/ 2));
            }
        </script>
    </aos:onready>
</aos:html>
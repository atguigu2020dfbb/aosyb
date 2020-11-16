<%@page contentType="text/html; charset=utf-8" %>
<%@taglib prefix="aos" uri="/WEB-INF/tld/aos.tld" %>
<aos:html>
    <aos:head title="利用登记">
        <aos:include lib="ext"/>
        <aos:base href="make"/>
        <aos:include js="${cxt}/static/js/jquery-3.2.1.min.js" />
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
        <div id="_div_photo" class="x-hidden" align="center">
            <img id="_img_photo" class="app_cursor_pointer"  width="200"
                 height="200">
        </div>
        <div id="_div_jieshaoxin" class="x-hidden" align="center">
            <img id="_img_jieshaoxin" class="app_cursor_pointer"  width="680"
                 height="500">
        </div>
    </aos:body>
    <aos:onready>
        <aos:viewport layout="border">
            <aos:gridpanel id="_g_make" url="listMake.jhtml" region="north" onrender="_g_make_onrender" split="true"
                           height="300" onitemclick="_g_make_detail_query" rowclass="true" hidePagebar="false" pageSize="10">
                <aos:docked>
                    <aos:hiddenfield  id="ipinput" value='127.0.0.1'/>
                    <aos:dockeditem xtype="tbtext" text="利用登记"/>
                    <aos:dockeditem text="新增登记" id="add" icon="add.png" onclick="_w_make_show"/>
                    <aos:dockeditem text="修改" icon="edit.png" onclick="_w_make_u_show"/>
                    <aos:dockeditem text="删除" icon="del.png" onclick="_g_make_del"/>
                    <aos:button text="介绍信" icon="add.png" scale="small" margin="0 0 0 0" id="jieshaoxin_table_text">
                        <aos:menu plain="false">
                            <aos:menuitem text="扫描介绍信" icon="add.png" onclick="_g_make_jieshaoxin" />
                            <aos:menuitem text="打开介绍信" icon="add.png"
                            onclick="_g_open_jieshaoxin" />
                        </aos:menu>
                    </aos:button>
                    <aos:dockeditem text="查档者管理" icon="user20.png" onclick="_g_user_manager"/>
                    <aos:dockeditem text="领导核批" icon="theme.png" onclick="_f_make_dd_save"/>
                    <aos:dockeditem text="问题标示" icon="icon134.png" onclick="_g_problem_show"/>
                    <aos:dockeditem id="cxjg" text="查询结果" icon="text_cap.png" >
                        <aos:menu plain="true">
                            <aos:menuitem onclick="_no_query" text="未查到"/>
                            <aos:menuitem onclick="_yes_query" text="已查到"/>
                        </aos:menu>
                    </aos:dockeditem>
                    <aos:dockeditem text="满意度评价" icon="icq.png" onclick="_w_satisfied_u_show"/>
                    <aos:dockeditem text="利用日志" icon="text_list.png" onclick="_w_make_log_show"/>
                </aos:docked>
                <aos:column header="id_" dataIndex="id_" hidden="true"  locked="true"/>
                <aos:hiddenfield id="rowmath" name="rowmath" value="0" />
                <aos:column dataIndex="img" hidden="true"/>
                <aos:column dataIndex="users" hidden="true"/>
                <aos:column header="登记编号" dataIndex="djbh" width="100"/>
                <aos:column header="预约编号" dataIndex="yybh" width="100"/>
                <aos:column header="查档者姓名" dataIndex="xm"/>
                <aos:column header="查档单位" dataIndex="cddw"/>
                <aos:column header="身份证号" dataIndex="sfzh"/>
                <aos:column header="介绍信" dataIndex="dh" hidden="true"/>
                <aos:column header="查阅方式" dataIndex="cyfs"/>
                <aos:column header="查阅目的" dataIndex="lymd"/>
                <aos:column header="登记人" dataIndex="djr" hidden="true"/>
                <aos:column header="查阅内容" dataIndex="cdnr" width="180"/>
                <aos:column header="接待人" dataIndex="jdr"/>
                <aos:column header="接待时间" dataIndex="jdsj"/>
                <aos:column header="查询结果" dataIndex="cxjg" rendererFn="fn_cxjg_render"/>
                <aos:column header="利用状态" dataIndex="spzt" rendererFn="fn_spzt_render" />
                <aos:column header="是否提供" dataIndex="nftg"/>
                <aos:column header="未提供原因" dataIndex="wtgyy"/>
                <aos:column header="问题标示" dataIndex="wtbs"/>
                <aos:column header="满意度" dataIndex="myd"/>
                <aos:column header="利用人次" dataIndex="lyrc"/>
                <aos:column header="利用卷次" dataIndex="lyjac"/>
                <aos:column header="利用件次" dataIndex="lyjc"/>
                <aos:column header="利用类别" dataIndex="lylbb"/>
                <aos:column header="是否归还" dataIndex="yngh"/>
                <aos:column header="介绍信" dataIndex="jsx" hidden="true"/>
                <aos:column header="出库状态" dataIndex="ckzt" hidden="true"/>
                <aos:column header="备注" dataIndex="bz"/>

                <aos:column header="" flex="1"/>
            </aos:gridpanel>
            <aos:gridpanel id="_g_make_detail" region="center" url="listMakedetail.jhtml" split="true" hidePagebar="false" pageSize="5" >
                <aos:docked>
                    <aos:dockeditem xtype="tbtext" text="档案信息"/>
                    <aos:dockeditem text="数据检索" icon="icon71.png" onclick="_g_make_read"/>
                    <aos:dockeditem text="删除" icon="del.png" onclick="_g_archive_del"/>
                    <aos:dockeditem text="转实体出库" icon="table2.png" onclick="_f_make_kf_save"/>
                    <aos:dockeditem text="已归还" icon="timeline.png" onclick="_w_make_gh"/>
                    <aos:dockeditem text="打印" icon="printer.png"  id="_f_filename_message"
                                    onclick="_w_picture_show"   />
                </aos:docked>
                <aos:column header="id_" dataIndex="id_" hidden="true"  locked="true"/>
                <aos:column header="全宗单位" dataIndex="qzmc" locked="true"/>
                <aos:column header="档号" dataIndex="dh" width="200"/>
                <aos:column header="题名" dataIndex="tm" width="300"/>
                <aos:column header="形成时间" dataIndex="xcrq"/>
                <aos:column header="是否开放" dataIndex="sfkf"/>
                <aos:column header="出库状态" dataIndex="ckzt" rendererFn="fn_ckzt_render"/>
                <aos:column header="表明" dataIndex="tablename" hidden="true"/>
                <aos:column header="" flex="1"/>
            </aos:gridpanel>
        </aos:viewport>
        <aos:window title="领导审批" id="_w_lingdao" width="500" layout="column" >
            <aos:formpanel id="_f_lingdao"  width="490" layout="column">
                <aos:combobox fieldLabel="审核人" name="spr" id="spr" columnWidth="0.98" value="处长审核" >
                    <aos:option value="hgc" display="黄光春"/>
                    <aos:option value="lfj" display="李范俊"/>
                    <aos:option value="qhy" display="秦洪叶"/>
                </aos:combobox>
            </aos:formpanel>
            <aos:docked dock="bottom" ui="footer">
                <aos:dockeditem xtype="tbfill" />
                <aos:dockeditem xtype="button" text="保存" icon="save.png" onclick="_f_make_ld_save"/>
                <aos:dockeditem text="关闭" icon="close.png" onclick="#_w_lingdao.hide()"/>
            </aos:docked>
        </aos:window>
        <aos:window title="登记信息" id="_w_make" width="850" layout="fit"  modal="false" center="true" maximizable="true"
                    onshow="_w_make_onshow">
            <aos:formpanel id="_f_make"  width="700" layout="column">
                <aos:fieldset title="基本信息" labelWidth="70" columnWidth="0.65" margin="0 10 0 0" height="280">
                    <aos:hiddenfield id="users" />
                    <aos:textfield name="djbh" fieldLabel="查档编号" readOnly="true" columnWidth="0.49"/>
                    <aos:textfield name="yybh" fieldLabel="预约编号" readOnly="true" columnWidth="0.49"/>
                    <aos:textfield name="cddw"  fieldLabel="查档单位" columnWidth="0.49"/>
                    <aos:textfield name="xm" id="xmm"  fieldLabel="查档者姓名" columnWidth="0.49"/>

                    <aos:textfield name="sfzh" id="sfzh" fieldLabel="身份证号"  maxLength="50" columnWidth="0.68" />
                    <aos:button text="读取身份证" margin="0 0 0 10"  icon="query.png" columnWidth="0.3" onclick="read_cardID"/>

                    <aos:combobox fieldLabel="查阅方式" name="cyfs"  value="现场查档" columnWidth="0.98" >
                        <aos:option value="现场查档" display="现场查档"/>
                        <aos:option value="电话预约" display="电话预约"/>
                        <aos:option value="网站预约" display="网站预约"/>
                        <aos:option value="微信预约" display="微信预约"/>
                    </aos:combobox>
                    <aos:combobox fieldLabel="查阅目的" name="lymd" dicField="lymd" columnWidth="0.98" />
                    <aos:textfield name="cdnr" fieldLabel="查阅内容"  columnWidth="0.98" />
                    <aos:textfield name="jdr" fieldLabel="接待人"  columnWidth="0.49" value="${user}"/>
                    <aos:datefield name="jdsj" id="jdsj" fieldLabel="接待时间"  columnWidth="0.49" readOnly="true" />
                    <aos:textfield name="bz" fieldLabel="备注"   columnWidth="0.98" />
                </aos:fieldset>
                <aos:fieldset title="图像" labelWidth="70" columnWidth="0.35" contentEl="_div_photo" height="280" >
                </aos:fieldset>
            </aos:formpanel>
            <aos:docked dock="bottom" ui="footer">
                <aos:dockeditem xtype="tbfill" />
                <aos:dockeditem xtype="button" text="保存数据" icon="ok.png" onclick="_f_make_save"/>
                <aos:dockeditem text="关闭" icon="close.png" onclick="#_w_make.hide()"/>
            </aos:docked>
        </aos:window>
        <aos:window id="_w_make_u" title="修改" width="850" layout="fit"  modal="false" center="true" maximizable="true">
            <aos:formpanel id="_f_make_u" width="700" layout="column">
                <aos:fieldset title="基本信息" labelWidth="70" columnWidth="0.65" margin="0 10 0 0" height="280">
                    <aos:hiddenfield name="id_" fieldLabel="主键" />
                    <aos:textfield name="djbh" fieldLabel="查档编号" readOnly="true" columnWidth="0.49"/>
                    <aos:textfield name="yybh" fieldLabel="预约编号" readOnly="true" columnWidth="0.49"/>
                    <aos:textfield name="cddw"  fieldLabel="查档单位" columnWidth="0.49"/>
                    <aos:textfield name="xm"  fieldLabel="查档者姓名" columnWidth="0.49"/>

                    <aos:textfield name="sfzh"  fieldLabel="身份证号"  maxLength="50" columnWidth="0.68" />
                    <aos:button text="读取身份证" margin="0 0 0 10"  icon="query.png" columnWidth="0.3" onclick="read_cardID"/>

                    <aos:combobox fieldLabel="查阅方式" name="cyfs"  columnWidth="0.98" >
                        <aos:option value="现场查档" display="现场查档"/>
                        <aos:option value="电话预约" display="电话预约"/>
                        <aos:option value="网站预约" display="网站预约"/>
                        <aos:option value="微信预约" display="微信预约"/>
                    </aos:combobox>


                    <aos:combobox fieldLabel="查阅目的" name="lymd" dicField="lymd" columnWidth="0.98" />

                    <aos:textfield name="cdnr" fieldLabel="查阅内容"  columnWidth="0.98" />
                    <aos:textfield name="jdr" fieldLabel="接待人"  columnWidth="0.49" value="${user}"/>
                    <aos:datefield name="jdsj" fieldLabel="接待时间"  columnWidth="0.49" />
                    <aos:textfield name="bz" fieldLabel="备注"   columnWidth="0.98" />
                </aos:fieldset>
                <aos:fieldset title="图像" labelWidth="70" columnWidth="0.35" contentEl="_div_photo" height="280" >
                </aos:fieldset>
                <aos:docked dock="bottom" ui="footer">
                    <aos:dockeditem xtype="tbfill"/>
                    <aos:dockeditem text="上一条" icon="more/go-previous.png"  onclick="_f_previous_data"/><%----%>
                    <aos:dockeditem text="下一条" icon="more/go-next.png" onclick="_f_next_data"/><%----%>

                    <aos:dockeditem text="保存" icon="save.png" onclick="_f_make_u_save"/>
                    <aos:dockeditem text="关闭" icon="close.png" onclick="#_w_make_u.hide()"/>
                </aos:docked>
            </aos:formpanel>
        </aos:window>
        <aos:window id="_w_jieshaoxin_u" title="介绍信">
            <aos:formpanel id="_f_jieshaoxin_u" width="700" layout="column">
                <aos:fieldset title="介绍信" labelWidth="70" columnWidth="0.99" contentEl="_div_jieshaoxin" >
                </aos:fieldset>
                <aos:docked dock="bottom" ui="footer">
                    <aos:dockeditem xtype="tbfill"/>
                    <aos:dockeditem text="关闭" icon="close.png" onclick="#_w_jieshaoxin_u.hide()"/>
                </aos:docked>
            </aos:formpanel>
        </aos:window>
        <aos:window id="_w_make_dd" title="实体调档">
            <aos:formpanel id="_f_make_dd" width="700" layout="anchor">
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
                    <aos:dockeditem text="领导审核" icon="share.png" onclick=""/>
                    <aos:dockeditem text="关闭" icon="close.png" onclick="#_w_make_dd.hide()"/>
                </aos:docked>
            </aos:formpanel>
        </aos:window>
        <aos:window id="_w_myd_u" title="满意度">
            <aos:formpanel id="_f_myd_u" width="300" layout="anchor">
                <aos:hiddenfield name="lyrc" fieldLabel="lyrc" value="1"/>
                <aos:textfield name="lyjac" fieldLabel="利用卷/册次" />
                <aos:textfield name="lyjc" fieldLabel="利用件/本次" />
                <aos:combobox fieldLabel="利用类别" name="lylbb" dicField="lylb" />
                <aos:docked dock="bottom" ui="footer" padding="0 0 5 0">
                    <aos:dockeditem xtype="tbfill"/>
                    <aos:dockeditem text="评价" icon="icq.png" onclick="_f_myd_u_save
"/>
                </aos:docked>
            </aos:formpanel>
        </aos:window>
        <aos:window id="_w_satisfied_u" title="满意度">
            <aos:formpanel id="_f_satisfied_u" width="700" layout="anchor">
                <aos:hiddenfield name="id_" fieldLabel="id_" />
                <aos:textfield name="djbh" fieldLabel="登记编号" readOnly="true"/>
                <aos:textfield name="xm" fieldLabel="姓名" readOnly="true"/>
                <aos:combobox name="myd" fieldLabel="满意度">
                    <aos:option value="满意" display="满意"/>
                    <aos:option value="基本满意" display="基本满意"/>
                    <aos:option value="不满意" display="不满意"/>
                </aos:combobox>
                <aos:docked dock="bottom" ui="footer">
                    <aos:dockeditem xtype="tbfill"/>
                    <aos:dockeditem text="保存" icon="save.png" onclick="_f_satisfied_u_save"/>
                    <aos:dockeditem text="关闭" icon="close.png" onclick="#_w_satisfied_u.hide()"/>
                </aos:docked>
            </aos:formpanel>
        </aos:window>
        <aos:window id="_w_problem" title="问题标示">
            <aos:formpanel id="_f_problem_u" width="700" layout="column">
                <aos:hiddenfield name="id_" fieldLabel="id_" />
                <aos:textfield name="djbh" fieldLabel="登记编号" readOnly="true" columnWidth="0.49"/>
                <aos:textfield name="cyfs" fieldLabel="查阅方式"  columnWidth="0.49"/>
                <aos:textfield name="jdr" fieldLabel="接待人员" columnWidth="0.49"/>
                <aos:textfield name="xm" fieldLabel="查档人员" columnWidth="0.49"/>
                <aos:textareafield name="wtbs" fieldLabel="问题标示" columnWidth="0.98"/>
                <aos:docked dock="bottom" ui="footer">
                    <aos:dockeditem xtype="tbfill"/>
                    <aos:dockeditem text="保存" icon="save.png" onclick="_f_problem_u_save"/>
                    <aos:dockeditem text="关闭" icon="close.png" onclick="#_w_problem.hide()"/>
                </aos:docked>
            </aos:formpanel>
        </aos:window>

        <aos:window id="_w_make_log" title="利用日志" onshow="_w_make_log_onshow" >
            <aos:gridpanel id="_g_make_log" region="center" url="listMakeLog.jhtml" width="500" height="400" hidePagebar="true">
                <aos:column header="id_" dataIndex="id_" hidden="true"/>
                <aos:column header="操作时间" dataIndex="czsj" width="200"/>
                <aos:column header="操作人" dataIndex="czr"/>
                <aos:column header="内容" dataIndex="cznr"/>
                <aos:column header="" flex="1"/>
            </aos:gridpanel>
        </aos:window>
        <aos:window id="_w_files" title="电子文件" autoScroll="true" width="800"
                    border="true" height="500" onshow="get_files">
            <aos:gridpanel id="_g_files" region="east" url="getFiles.jhtml"
                           hidePagebar="true" split="true" width="800">
                <aos:docked forceBoder="0 0 1 0">
                    <aos:dockeditem xtype="tbtext" text="电子文件列表" />
                    <%--Ricardo--%>
                    <aos:checkbox id="QRmark_show" boxLabel="显示二维码" />
                    <aos:checkbox id="watermark_show" boxLabel="显示水印"/>
                    <%--********--%>
                    <aos:dockeditem text="查看" icon="query.png" onclick="_w_files_look" />
                </aos:docked>
                <aos:column type="rowno" />
                <aos:column header="流水号" dataIndex="id_" hidden="true" />
                <aos:column header="档案号" dataIndex="tid" width="60" hidden="true" />
                <aos:column header="上传文件名" dataIndex="_path" width="90" />
                <aos:column header="路径" dataIndex="dirname" width="90" />
                <aos:column header="上传时间" dataIndex="sdatetime" width="90" />
                <aos:column header="文件名" dataIndex="_s_path" width="60" />
            </aos:gridpanel>
            <aos:docked dock="bottom" ui="footer">
                <aos:dockeditem xtype="tbfill" />
                <aos:dockeditem onclick="#_w_files.close();" text="关闭"
                                icon="close.png" />
            </aos:docked>
        </aos:window>
        <aos:window id="_w_make_manage" title="查档者管理" width="850" layout="fit"  modal="false" center="true" maximizable="true">
            <aos:formpanel id="_f_make_manage" width="700" layout="column">
                <aos:fieldset title="基本信息" labelWidth="70" columnWidth="0.65" margin="0 10 0 0" height="280">
                    <aos:hiddenfield name="id_" fieldLabel="主键" />
                    <aos:textfield name="djbh" fieldLabel="查档编号" readOnly="true" columnWidth="0.49"/>
                    <aos:textfield name="yybh" fieldLabel="预约编号" readOnly="true" columnWidth="0.49"/>
                    <aos:textfield name="cddw"  fieldLabel="查档单位" columnWidth="0.49"/>
                    <aos:textfield name="xm"  fieldLabel="查档者姓名" columnWidth="0.49"/>
                    <aos:textfield name="sfzh"  fieldLabel="身份证号"  maxLength="50" columnWidth="0.68" />
                    <aos:button text="读取身份证" margin="0 0 0 10"  icon="query.png" columnWidth="0.3" onclick="read_cardID"/>
                    <aos:combobox fieldLabel="查阅方式" name="cyfs"  columnWidth="0.98" >
                        <aos:option value="现场查档" display="现场查档"/>
                        <aos:option value="电话预约" display="电话预约"/>
                        <aos:option value="网站预约" display="网站预约"/>
                        <aos:option value="微信预约" display="微信预约"/>
                    </aos:combobox>
                    <aos:combobox fieldLabel="查阅目的" name="lymd" dicField="lymd" columnWidth="0.98" />
                    <aos:textfield name="cdnr" fieldLabel="查阅内容"  columnWidth="0.98" />
                    <aos:textfield name="jdr" fieldLabel="接待人"  columnWidth="0.49" value="${user}"/>
                    <aos:datefield name="jdsj" fieldLabel="接待时间"  columnWidth="0.49" />
                    <aos:textfield name="bz" fieldLabel="备注"   columnWidth="0.98" />
                </aos:fieldset>
                <aos:fieldset title="图像" labelWidth="70" columnWidth="0.35" contentEl="_div_photo" height="280" >
                </aos:fieldset>
                <aos:docked dock="bottom" ui="footer">
                    <aos:dockeditem xtype="tbfill"/>
                    <aos:dockeditem text="保存" icon="save.png" onclick="_f_make_manage_save"/>
                    <aos:dockeditem text="关闭" icon="close.png" onclick="#_w_make_manage.hide()"/>
                </aos:docked>
            </aos:formpanel>
        </aos:window>
        <script type="text/javascript">

            function changeRowClass(record, rowIndex, rowParams, store){

                //得到当前行的指定的列的值
                if(record.get("jsx")!=""&&record.get("jsx")!=null){

                    return 'grid-one-column';
                }else{
                    return 'grid-zero-column';
                }

            }
            function itemclick(grid, rowIndex, columnIndex, e){
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
            function _no_query(){
                var make_record = AOS.selectone(_g_make);
                if(!make_record) {
                    AOS.tip("请选择任务!");
                    return;
                }
                AOS.ajax({
                    url:'updateMakeDetail_select.jhtml',
                    params:{
                        id_: make_record.data.id_,
                        //tid:record.data.id_,
                        cxjg:'0'
                    },
                    ok:function (data) {
                        if(data.appcode===1){
                            AOS.tip("操作成功!");
                            _g_make_store.load();
                        }else{
                            AOS.tip("操作失败!");
                        }
                    }
                })
            }
            function _yes_query(){
                var make_record = AOS.selectone(_g_make);
                if(!make_record) {
                    AOS.tip("请选择任务!");
                    return;
                }
                AOS.ajax({
                    url:'updateMakeDetail_select.jhtml',
                    params:{
                        id_: make_record.data.id_,
                        //tid:record.data.id_,
                        cxjg:'1'
                    },
                    ok:function (data) {
                        if(data.appcode===1){
                            AOS.tip("操作成功!");
                            _g_make_store.load();
                        }else{
                            AOS.tip("操作失败!");
                        }
                    }
                })
            }

            //创建读卡控件
            var CertCtl = new IDCertCtl();
            //身份证读卡控件创建
            function IDCertCtl() {
                //创建用于与服务交换数据的对象
                this.xhr = createXmlHttp();
                this.type = "CertCtl";
                this.height = 0;
                this.width = 0;
                //连接
                this.connect = CertCtl_connect;
                //断开
                this.disconnect = CertCtl_disconnect;
                //获取状态
                this.getStatus = CertCtl_getStatus;
                //读卡
                this.readCert = CertCtl_readCert;
                //读IC卡序列号
                this.readICCardSN = CertCtl_readICCardSN;
                //读身份证物理卡号
                this.readIDCardSN = CertCtl_readIDCardSN;
            }

            //创建XMLHttpRequest 对象，用于在后台与服务器交换数据
            function createXmlHttp() {
                var xmlHttp = null;
                //根据window.XMLHttpRequest对象是否存在使用不同的创建方式
                if (window.XMLHttpRequest) {
                    xmlHttp = new XMLHttpRequest();                  //FireFox、Opera等浏览器支持的创建方式
                } else {
                    xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");//IE浏览器支持的创建方式
                }
                return xmlHttp;
            }
            //连接方法
            function CertCtl_connect() {
                var result = "";
                //创建请求 第一个参数是代表以post方式发送；第二个是请求端口和地址；第三个表示是否异步
                CertCtl.xhr.open("POST", "http://127.0.0.1:18889/api/connect", false);
                //发送请求
                try {
                    CertCtl.xhr.send();
                } catch (e) {
                }
                //返回值readyState   0: 请求未初始化
                //				    1: 服务器连接已建立
                //				    2：请求已接收
                //				    3: 请求处理中
                //				    4: 请求已完成，且响应已就绪
                //返回值status      200: "OK"
                //					404: 未找到页面
                //当返回值readyState为4且status为200时,为查询成功
                if (CertCtl.xhr.readyState == 4 && CertCtl.xhr.status == 200) {
                    result = CertCtl.xhr.responseText;
                    CertCtl.xhr.readyState = 1;
                }
                return result;
            }

            //断开方法
            function CertCtl_disconnect() {
                var result = "";
                //创建请求 第一个参数是代表以post方式发送；第二个是请求端口和地址；第三个表示是否异步
                CertCtl.xhr.open("POST", "http://127.0.0.1:18889/api/disconnect", false);
                //发送请求
                try {
                    CertCtl.xhr.send();
                } catch (e) {
                }
                if (CertCtl.xhr.readyState == 4 && CertCtl.xhr.status == 200) {
                    result = CertCtl.xhr.responseText;
                    CertCtl.xhr.readyState = 1;
                }

                //Ext.getCmp("CertCtl_disconnect1").getEl().dom.readOnly=true;
                return result;
            }
            //获取状态方法
            function CertCtl_getStatus() {
                var result = "";
                //创建请求 第一个参数是代表以post方式发送；第二个是请求端口和地址；第三个表示是否异步
                CertCtl.xhr.open("POST", "http://127.0.0.1:18889/api/getStatus", false);
                //发送请求
                try {
                    CertCtl.xhr.send();
                } catch (e) {
                }
                if (CertCtl.xhr.readyState == 4 && CertCtl.xhr.status == 200) {
                    result = CertCtl.xhr.responseText;
                    CertCtl.xhr.readyState = 1;
                }
                return result;
            }

            //执行读卡操作
            function CertCtl_readCert() {
                //清空之前的内容
                /*Ext.getCmp("xm").setValue("");
                Ext.getCmp("sfzh").setValue("");*/

                Ext.getCmp("users").setValue("<%=session.getAttribute("user")%>");
                _w_make_onshow();
                var result = "";
                try {
                    //创建请求 第一个参数是代表以post方式发送；第二个是请求端口和地址；第三个表示是否异步
                    CertCtl.xhr.open("POST", "http://127.0.0.1:18889/api/readCard", false);
                    //发送请求
                    CertCtl.xhr.send();
                    if (CertCtl.xhr.readyState == 4 && CertCtl.xhr.status == 200) {
                        result = CertCtl.xhr.responseText;
                        CertCtl.xhr.readyState = 1;
                    }
                } catch (e) {

                }
                return result;
            }

            //获取IC卡序列号
            function CertCtl_readICCardSN() {
                var result = "";
                //创建请求 第一个参数是代表以post方式发送；第二个是请求端口和地址；第三个表示是否异步
                CertCtl.xhr.open("POST", "http://127.0.0.1:18889/api/readICCardSN", false);
                //发送请求
                try {
                    CertCtl.xhr.send();
                } catch (e) {
                }
                if (CertCtl.xhr.readyState == 4 && CertCtl.xhr.status == 200) {
                    result = CertCtl.xhr.responseText;
                    CertCtl.xhr.readyState = 1;
                }
                return result;
            }

            //获取身份证物理卡号
            function CertCtl_readIDCardSN() {
                var result = "";
                //创建请求 第一个参数是代表以post方式发送；第二个是请求端口和地址；第三个表示是否异步
                CertCtl.xhr.open("POST", "http://127.0.0.1:18889/api/readIDCardSN", false);
                //发送请求
                try {
                    CertCtl.xhr.send();
                } catch (e) {
                }
                if (CertCtl.xhr.readyState == 4 && CertCtl.xhr.status == 200) {
                    result = CertCtl.xhr.responseText;
                    CertCtl.xhr.readyState = 1;
                }
                return result;
            }

            //转为Json格式
            function toJson(str) {
                //var obj = JSON.parse(str);
                //return obj;
                return eval('(' + str + ')');
            }
            //连接方法
            function connect() {
                //清空页面
                clearForm();

                try {
                    //调用对应的连接方法,并赋值给result
                    var result = CertCtl.connect();
                    //如果result为空,代表读卡插件未启动
                    if (result == "") {
                        alert("未启动读卡插件!")
                    } else {
                        //result页面回显
                        document.getElementById("result").value = result;
                    }
                } catch (e) {
                }
            }

            //断开连接方法
            function disconnect() {
                //清空页面
                clearForm();

                try {
                    //调用对应的断开连接方法,并赋值给result
                    var result = CertCtl.disconnect();
                    //如果result为空,代表读卡插件未启动
                    if (result == "") {
                        alert("未启动读卡插件!")
                    } else {
                        //result页面回显
                        document.getElementById("result").value = result;
                    }
                } catch (e) {
                }
            }

            //获取状态方法
            function getStatus() {
                //清空页面
                clearForm();

                try {
                    //调用对应的获取状态方法,并赋值给result
                    var result = CertCtl.getStatus();
                    //如果result为空,代表读卡插件未启动
                    if (result == "") {
                        alert("未启动读卡插件!")
                    } else {
                        //result页面回显
                        document.getElementById("result").value = result;
                    }
                } catch (e) {
                }
            }

            //读卡方法
            function readCert() {
                //清空页面
                //clearForm();
                //开始时间
                var startDt = new Date();
                //调用对应的读卡方法
                var result = CertCtl.readCert();
                //如果result为空,代表读卡插件未启动
                if (result == "") {
                    alert("未启动读卡插件!")
                } else {
                    //结束时间
                    var endDt = new Date();
                    //读卡时间回显
                    //document.getElementById("timeElapsed").value = (endDt.getTime() - startDt.getTime()) + "毫秒";
                    //document.getElementById("result").value = result;
                    //var resultObj = toJson(result);
                    //result = result.replace("\"resultFlag\":","\"resultFlag\":\"true\"");
                    //格式化result
                    //var resultObj = $.parseJSON(result);//windows10上面无法解析
                    var resultObj = eval('(' +result+')');
                    //resultFlag为0代表读卡成功
                    if (resultObj.resultFlag == "0") {
                        //回显相关数据

                        Ext.getCmp("xmm").setValue(resultObj.resultContent.partyName);
                        Ext.getCmp("sfzh").setValue(resultObj.resultContent.certNumber);
                        document.getElementById("_img_photo").src = "data:image/jpeg;base64," + resultObj.resultContent.identityPic;
                        //document.getElementById("PhotoDisplay").src = "data:image/jpeg;base64," + resultObj.resultContent.identityPrintPic;
                    } else if (resultObj.resultFlag == "-1") {
                        if (resultObj.errorMsg == "端口打开失败") {
                            alert("读卡器未连接");
                        } else {
                            alert(resultObj.errorMsg);
                        }
                    } else if (resultObj.resultFlag == "-2") {
                        alert(resultObj.errorMsg);
                    }
                }
            }

            function readICCardSN() {
                //清空页面
                clearForm();

                try {
                    //调用对应的获取状态方法,并赋值给result
                    var result = CertCtl.readICCardSN();
                    //如果result为空,代表读卡插件未启动
                    if (result == "") {
                        alert("未启动读卡插件!")
                    } else {
                        //result页面回显
                        document.getElementById("result").value = result;
                    }
                } catch (e) {
                }
            }

            function readIDCardSN() {
                //清空页面
                clearForm();

                try {
                    //调用对应的获取状态方法,并赋值给result
                    var result = CertCtl.readIDCardSN();
                    //如果result为空,代表读卡插件未启动
                    if (result == "") {
                        alert("未启动读卡插件!")
                    } else {
                        //result页面回显
                        document.getElementById("result").value = result;
                    }
                } catch (e) {

                }
            }
            //删除当前查到的档案
            function _g_archive_del(){
                var make_record = AOS.selectone(_g_make);
                var make_detail_record = AOS.selectone(_g_make_detail);
                if(!make_record) {
                    AOS.tip("请选择任务!");
                    return;
                }
                if(!make_detail_record) {
                    AOS.tip("请选择删除的数据!");
                    return;
                }
                var rw_id=make_record.data.id_;
                var archive_id=make_detail_record.data.id_;
                var tablename=make_detail_record.data.tablename;
                AOS.ajax({
                    params:{rw_id:rw_id,archive_id:archive_id,tablename:tablename},
                    url:'delArchive_make.jhtml',
                    ok:function(data){
                        if (data.appcode === -1) {
                            AOS.tip("删除失败");
                        } else {
                            _g_make_detail_store.reload();
                            AOS.tip("删除成功");
                        }

                    }
                })
            }























            function _g_make_onrender(){
                _g_make_store.load();
            }
            //打开介绍信窗口
            function _g_make_jieshaoxin(){
                var record = AOS.selectone(_g_make);
                if(record){
                    window.open('http://192.168.0.3/aosyb/make/openGpy.jhtml?id='+record.data.id_+'&djbh='+record.data.djbh,'_blank');
                    //window.location.href='http://127.0.0.1/aosyb/make/openGpy.jhtml?id='+record.data.id_+'&djbh='+record.data.djbh;
                    //先窗口弹出
                    //parent.parent.fnaddtab('', '介绍信','make/openGpy.jhtml?id='+record.data.id_+'&djbh='+record.data.djbh);
                }
            }
            function _w_make_show(){
                _w_make.show();
            }
            /*function _w_make_onshow() {
                var djbh =null;
                AOS.ajax({
                    url:'getAPPID.jhtml',
                    ok:function(data){
                        AOS.setValue('_f_make.djbh',data.djbh);
                    }
                })
            }*/
            function times(){
                //得到当前年月日
                var date = new Date();
                var year=date .getFullYear(); //获取完整的年份(4位)
                var month=date .getMonth(); //获取当前月份(0-11,0代表1月)
                if(month<10){
                    month='0'+(month+1);
                }else{
                    month='0'+(month+1);
                }
                var day=date .getDate(); //获取当前日(1-31)
                var times=year+month+day;
                return times;
            }
            function _w_make_onshow(){
                //清空表单
                AOS.reset(_f_make);
                document.getElementById("_img_photo").src="";
                var yesday=new Date();
                var nd=new Date().getFullYear();
                var yesday = yesday.getFullYear()  +"-"+(yesday.getMonth()> 8 ? (yesday.getMonth() + 1) : "0" + (yesday.getMonth() + 1))+
                    "-"+(yesday.getDate()> 9 ? (yesday.getDate()) : "0" + yesday.getDate());
                //根据tree名称得到对应的部门名称
                AOS.ajax({
                    params:{name_:'登记流水号',nd:"CD"+nd},
                    url:'getMakeIndex.jhtml',
                    ok:function(data){
                        //设计一个随机数编号
                        var date=new Date();
                        _f_make.form.findField("djbh").setValue("CD"+nd+data.index);
                        _f_make.form.findField("jdsj").setValue(yesday);
                    }
                });
            }
            function _w_make_u_show(){
                var record = AOS.selectone(_g_make);
                if(record){
                    //同时把当前条目的src字符串
                    AOS.ajax({
                        params:{imgpath:record.data.img},
                        url:'getImg.jhtml',
                        ok:function(data){
                            document.getElementById("_img_photo").src="data:image/jpeg;base64,"+data.appmsg;
                        }
                    });
                    _w_make_u.show();
                    _f_make_u.loadRecord(record);
                }

            }
            function _w_picture_show(){
                var selection = AOS.selection(_g_make_detail, 'id_');
                if (AOS.empty(selection)) {
                    AOS.tip('请选择需要查看电子文件的档案!');
                    return;
                }
                if(selection.substring(0,selection.length-1).split(",").length>1){
                    alert("请选择单个条目数据！");
                    return;
                }
                //此时判断是否有开放档案，审核操作
                var selection = AOS.selection(_g_make_detail, 'id_');

                var sfkf=AOS.selectone(_g_make_detail).data.sfkf;
                if(sfkf=='不开放'||sfkf=='控制'){
                    AOS.tip("此档案是未开放档案,需要审核！");
                    return;
                }
                //弹出一个窗口，展示电子文件列表
                _w_files.show();
            }
            function get_files(){
                var selection = AOS.selection(_g_make_detail, 'id_');
                var id_=selection.substring(0,selection.length-1);
                var listTablename = AOS.selection(_g_make_detail, 'tablename').substring(0,AOS.selection(_g_make_detail, 'tablename').length-1);
                var params ={
                    id_:id_,
                    tablename:listTablename
                };
                //这个Store的命名规则为：表格ID+"_store"。
                _g_files_store.getProxy().extraParams = params;
                _g_files_store.load();
            }
            function _w_files_look(){
                /*Ricardo*/
                var QRshow = Ext.getCmp("QRmark_show").getValue();
                var waterShow = Ext.getCmp("watermark_show").getValue();
                //*********************************************
                //得到当前用户,判断是不是档案室和admin用户
                var user="<%=session.getAttribute("user")%>";
                var tablename = AOS.selection(_g_make_detail, 'tablename').substring(0,AOS.selection(_g_make_detail, 'tablename').length-1);
                //得到当前选中的。
                var row = AOS.selectone(_g_files);
                var dh = AOS.selectone(_g_make_detail).data.dh;
                //alert(dh);
                //***********************************
                if(row.data._s_path.split(".")[1]=="jpg"||row.data._s_path.split(".")[1]=="JPG"||
                    row.data._s_path.split(".")[1]=="tif"||row.data._s_path.split(".")[1]=="TIF"||
                    row.data._s_path.split(".")[1]=="pdf"||row.data._s_path.split(".")[1]=="PDF"){
                    parent.fnaddtab(row.data.id_, '电子文件',
                        'make/openPdfFile.jhtml?id='+row.data.id_+'&tid='+row.data.tid+'&type='+row.data._s_path.split(".")[1]+'&tablename='+tablename+'&dh='+dh+'&QRshow='+QRshow+'&waterShow='+waterShow);
                }else if(row.data._s_path.split(".")[1]=="doc"||row.data._s_path.split(".")[1]=="DOC"){
                    parent.fnaddtab(row.data.id_,'电子文件','archive/data/openSwfFile.jhtml?id='+row.data.id_+'&tid='+row.data.tid+'&type='+row.data._s_path.split(".")[1]+'&tablename='+tablename+'&QRshow='+QRshow+'&waterShow='+waterShow);
                }
                //**************************************************
            }
            //_path列转换
            function fn_path_render(value, metaData, record, rowIndex, colIndex,
                                    store) {
                if (value >= 1) {
                    return '<img src="${cxt}/static/icon/picture.png" />';
                } else {
                    return '<img src="${cxt}/static/icon/picture_empty.png" />';
                }
            }
            function _f_make_save(){
                AOS.ajax({
                    forms:_f_make,
                    params:{
                        imagesrc:document.getElementById("_img_photo").src,
                        users:"<%=session.getAttribute("user")%>"
                    },
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
            function _f_make_u_save(){
                AOS.ajax({
                    forms:_f_make_u,
                    url:'updateMake.jhtml',
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
            function _f_make_manage_save(){
                AOS.ajax({
                    forms:_f_make_manage,
                    url:'updateMake.jhtml',
                    ok:function(data){
                        if(data.appcode===1){
                            _w_make_manage.hide();
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
                            aos_rows_: AOS.selection(_g_make,'id_')
                        },
                        ok:function(data){
                            AOS.tip(data.appmsg);
                            _g_make_store.reload();
                        }
                    })
                })
            }
            function fn_cxjg_render(value, metaData, record, rowIndex, colIndex,
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
                    return '审核中';
                } if(value === '1'){
                    return "已审核";
                }if(value === '2'){
                    return "未审核";
                }
                else {
                    return '未审核';
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
            function _f_make_dd_save() {
                var record = AOS.selectone(_g_make);
                if (record) {
                    _w_lingdao.show();
                }
            }
            function _f_make_ld_save() {
                var record = AOS.selectone(_g_make);
                var spr_cn=Ext.getCmp("spr").getRawValue();
                var spr_en=Ext.getCmp("spr").getValue();
                AOS.ajax({
                    forms:_f_lingdao,
                    url: 'updateMakeDd.jhtml',
                    params: {
                        'id_': record.data.id_,
                        'state': 1,
                        spr_cn:spr_cn,
                        spr_en:spr_en,
                        'spzt': 2
                    },
                    ok: function (data) {
                        if (data.appcode === 1) {
                            _w_lingdao.hide();
                            _g_make_store.reload();
                            AOS.tip(data.appmsg);
                        } else {
                            AOS.err(AOS.appmsg);
                        }
                    }
                });
            }
            function _f_make_kf_save(){
                var record=AOS.selectone(_g_make);
                AOS.ajax({
                    url:'updateMakeStck.jhtml',
                    params:{
                        'id_':record.data.id_,
                        'state':0,
                        'ckzt':'0',
                        'person':'root'
                    },
                    ok:function(data){
                        if(data.appcode===1){
                            _w_make_dd.hide();
                            _g_make_store.reload();
                            AOS.tip(data.appmsg);
                        }else {
                            AOS.err(AOS.appmsg);
                        }
                    }
                })
            }

            function _g_make_read(){
                var record = AOS.selectone(_g_make);
                parent.fnaddtab('','数据检索','/make/loadData.jhtml?djbh='+record.data.djbh+'&recordid='+record.data.id_);
            }

            //上一页
            function _f_previous_data(){
                var count=Ext.getCmp("_g_make").getStore().getCount();
                var me=Ext.getCmp("_g_make").getSelectionModel().getSelection();
                //var record = AOS.selectone(_g_make);
                //得到执行行的坐标
                var rowIndex = Ext.getCmp("_g_make").getStore().indexOf(me[0]);
                if(rowIndex==0){
                    AOS.err("当前第一条!");
                    return;
                }
                var s=Ext.getCmp("_g_make").getStore().getAt(rowIndex-1);
                //原先行取消选中
                Ext.getCmp("_g_make").getSelectionModel().deselect(rowIndex);
                //此时让光标选中上一行
                Ext.getCmp("_g_make").getSelectionModel().select(rowIndex-1, true);
                //组件被显示后触发。
                Ext.getCmp("_f_make_u").form.setValues(s.data);
            }
            //下一页
            function _f_next_data(){
                var count=Ext.getCmp("_g_make").getStore().getCount();
                var me=Ext.getCmp("_g_make").getSelectionModel().getSelection();
                //var record = AOS.selectone(_g_make);
                //得到执行行的坐标
                var rowIndex = Ext.getCmp("_g_make").getStore().indexOf(me[0]);
                if(rowIndex==count-1){
                    AOS.err("当前最后一条!");
                    return;
                }
                var s=Ext.getCmp("_g_make").getStore().getAt(rowIndex+1);
                //原先行取消选中
                Ext.getCmp("_g_make").getSelectionModel().deselect(rowIndex);
                //此时让光标选中下一行
                Ext.getCmp("_g_make").getSelectionModel().select(rowIndex+1, true);
                //组件被显示后触发。
                Ext.getCmp("_f_make_u").form.setValues(s.data);
            }



            //满意度评价


            function _w_satisfied_u_show(){

                //弹出3个利用人次，卷次，件次
                _w_myd_u.show();

            }
            function _f_myd_u_save(){
                var record = AOS.selectone(_g_make);
                if(record){
                    if(record.data.myd!=""){
                        AOS.tip("已作出评价了!");
                        return;
                    }
                    //为了把值传过去临时加的
                    /*AOS.ajax({
                        forms:_f_myd_u,
                        params:{
                            id_:record.data.id_},
                        url:'updateSatisfied.jhtml',
                        ok:function(data){
                            if(data.appcode===1){
                                _g_make_store.reload();
                                AOS.tip("评价完成!");
                            }else {
                                AOS.tip("评价失败!");
                            }
                        }
                    });*/


                    funCMD('ES',record.data.id_);
                }else{
                    AOS.tip("请选择需要评价的用户!");
                }

            }
            //问题标示
            function _w_make_error(){
                var record = AOS.selectone(_g_make);
                if(AOS.empty(record)){
                    AOS.tip("请选择需要标识的问题!");
                    return;
                }
                parent.fnaddtab('','登记详情','/make/loadData_error.jhtml?id_='+record.data.id_+'&djbh='+record.data.djbh);
                //parent.fnaddtab('','登记详情','/make/initData.jhtml?formid='+record.data.id_)
            }
            function _w_make_error_list(){
                parent.fnaddtab('','标记列表','/make/list_error.jhtml');
            }
            function read_cardID(){

                readCert();
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
                var record = AOS.selectone(_g_make);
                if (!AOS.empty(record)) {

                    params.formid = record.data.id_;
                    params.ckzt=record.data.ckzt;
                    params.yngh=record.data.yngh;
                }
                _g_make_detail_store.getProxy().extraParams = params;
                _g_make_detail_store.load();

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

            function _f_satisfied_u_save(){
                AOS.ajax({
                    forms:_f_satisfied_u,
                    url:'updateMake.jhtml',
                    ok:function(data){
                        if(data.appcode===1){
                            _w_satisfied_u.hide();
                            _g_make_store.reload();
                            AOS.tip(data.appmsg);
                        }else {
                            AOS.err(AOS.appmsg);
                        }
                    }
                })
            }

            function _g_problem_show(){
                var record = AOS.selectone(_g_make);
                if(record){
                    _w_problem.show();
                    _f_problem_u.loadRecord(record)
                }
            }
            function _f_problem_u_save(){
                AOS.ajax({
                    forms:_f_problem_u,
                    url:'updateMake.jhtml',
                    ok:function(data){
                        if(data.appcode===1){
                            _w_problem.hide();
                            _g_make_store.reload();
                            AOS.tip(data.appmsg);
                        }else {
                            AOS.err(AOS.appmsg);
                        }
                    }
                })
            }
            function _g_user_manager(){
                var record = AOS.selectone(_g_make);
                if(record){
                    _w_make_manage.show();
                    _f_make_manage.loadRecord(record);
                    //同时把当前条目的src字符串
                    AOS.ajax({
                        params:{imgpath:record.data.img},
                        url:'getImg.jhtml',
                        ok:function(data){
                            document.getElementById("_img_photo").src="data:image/jpeg;base64,"+data.appmsg;
                        }
                    });
                }
            }
            function _g_print(){
                AOS.tip("预留功能接口，暂未实现....");
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
            function _w_make_gh(){
                var record = AOS.selectone(_g_make);
                var yngh="已归还";
                var id = record.data.id_;
                AOS.ajax({
                    url:'updateMakeDd.jhtml',
                    params:{
                        'id_':id,
                        'yngh':yngh,
                        'ckzt':2
                    },
                    ok:function(data){
                        if(data.appcode===1){
                            _w_make_dd.hide();
                            _g_make_store.reload();
                            AOS.tip(data.appmsg);
                        }else {
                            AOS.err(AOS.appmsg);
                        }
                    }
                })
            }
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
            function _g_water(){
                AOS.ajax({
                    url:'updateMakeDd.jhtml',
                    params:{
                        'id_':id,
                        'yngh':yngh,
                        'ckzt':2
                    },
                    ok:function(data){
                        if(data.appcode===1){
                            _w_make_dd.hide();
                            _g_make_store.reload();
                            AOS.tip(data.appmsg);
                        }else {
                            AOS.err(AOS.appmsg);
                        }
                    }
                })
            }
            function funCMD(cmd,id_)
            {




                var address = Ext.getCmp("ipinput").getValue();
                var url = "";
                if(address != "")
                {
                    url = "http://"+address+":10791/inf/"+cmd;
                }
                if(url == "")
                {
                    alert("错误的IP地址");
                    return;
                }

                var prgs = $(".PRG_"+cmd).find("input");  //获取用户输入的参数
                var obj = {};	//准备参数
                switch(cmd)
                {//填写参数
                    case "SI": //签到A指令参数获取
                        obj.num = prgs.eq(0).val();
                        if(obj.num != "")
                        {

                        }
                        else{
                            alert("请输入工号")
                        }
                        break
                    case "SS":
                        obj.num = prgs.eq(0).val();
                        if(obj.num != "")
                        {
                            obj.name = prgs.eq(1).val();
                            obj.pos = prgs.eq(2).val();
                            obj.org = prgs.eq(3).val();
                            obj.equ = prgs.eq(4).val();
                            obj.star = prgs.eq(5).val();
                            obj.inf = prgs.eq(6).val();
                        }
                        else{
                            alert("请输入工号")
                        }
                        break;
                    case "MS":
                    case "VB":
                        obj.msg = prgs.eq(0).val();
                        if(obj.msg != "")
                        {
                        }
                        else{
                            alert("请输入内容")
                        }
                        break;
                    case "SD":
                    case "AD":
                        obj.file = prgs.eq(0).val();
                        if(obj.file != "")
                        {

                        }
                        else{
                            alert("请输入文件名")
                        }
                        break;
                    case "SO":
                    case "EV":
                    case "ES":
                    case "GS":
                    case "PS":
                    case "WT":
                    case "HN":
                    case "CK":
                        //无参数
                        break;
                }
                postCmd(url,JSON.stringify(obj),id_);  //执行指令发送
                //关闭弹窗
                _w_myd_u.hide();
            }

            function postCmd(url,prgstr,id_)
            {

                //注意，为简化起见，此处未使用timeout参数，客户程序中有关评价等需要较长时间的指令请自行配置该参数
                $.post(url,prgstr,function(data){
                    //alert("返回："+data)
                    //此时得到json字符串，对字符串进行截取操作,然后更新条目信息
                    AOS.ajax({
                        forms:_f_myd_u,
                        params:{data:data,
                            id_:id_},
                        url:'updateSatisfied.jhtml',
                        ok:function(data){
                            if(data.appcode===1){
                                _g_make_store.reload();
                                AOS.tip("评价完成!");
                            }else {
                                AOS.tip("评价失败!");
                            }
                        }
                    })
                });
            }
            //打开介绍信
            function _g_open_jieshaoxin(){
                var record = AOS.selectone(_g_make);
                if(record){
                    //同时把当前条目的src字符串
                    AOS.ajax({
                        params:{id_:record.data.id_},
                        url:'getJsx.jhtml',
                        ok:function(data){
                            document.getElementById("_img_jieshaoxin").src="data:image/jpeg;base64,"+data.appmsg;
                        }
                    });
                    _w_jieshaoxin_u.show();
                    _f_jieshaoxin_u.loadRecord(record);
                }
            }

        </script>
    </aos:onready>
    <script type="text/javascript">
        function read_photo(){
            alert("暂无扫描仪接口！");
        }
    </script>
</aos:html>
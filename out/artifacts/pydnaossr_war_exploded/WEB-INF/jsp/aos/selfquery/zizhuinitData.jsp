<%@page contentType="text/html; charset=UTF-8" isELIgnored="false"%>
<%@taglib prefix="aos" uri="/WEB-INF/tld/aos.tld"%>
<%@ taglib uri="/WEB-INF/tld/cll.tld" prefix="c"%>
<%
    String path = request.getContextPath();
    Object fieldDtos = request.getAttribute("fieldDtos");
%>
<aos:html>
    <aos:head title="开放档案">
        <aos:base href="utilization/data"/>
        <aos:include lib="ext,swfupload"/>
        <aos:include js="${cxt}/static/pdfObject/pdfobject.js" />
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
        <!--html代码  -->
        <div id="documentViewer" style="height: 600px;"></div>
    </aos:body>
    <aos:onready>
        <aos:viewport layout="border">
            <aos:gridpanel id="_g_param" url="listAccounts_wsda.jhtml"  features="true"
                           region="center" onitemclick="itemclick" onrender="_w_params_show" rowclass="true"  onitemdblclick="fn_g_data">
                <aos:docked forceBoder="0 0 1 0">
                    <aos:hiddenfield id="rowmath" name="rowmath" value="0" />
                    <aos:hiddenfield  id="tablename_id" name="tablename_id" value="${id_}"/>
                    <aos:hiddenfield  id="tablename" name="tablename" value="${tablename}"/>
                    <aos:hiddenfield  id="user" name="user" value="${user}"/>
                    <aos:hiddenfield  id="zz_id_" name="zz_id_" value="${zz_id_}"/>
                    <aos:hiddenfield  id="_path" name="_path"/>
                    <aos:combobox name="listTablename"
                                  fields="[ 'tablename', 'tabledesc']" fieldLabel="开放档案"
                                  id="listTablename" editable="false" columnWidth="0.24"
                                  url="listTablename.jhtml?flag=0" displayField="tabledesc"
                                  valueField="tablename"  allowBlank="false" onselect="fn_onselect"/>
                    <aos:textfield  fieldLabel="档案检索"  margin="0 0 0 0" emptyText="请输入检索内容" height="30" id="text" name="text" allowBlank="true"
                                   columnWidth="0.7" width="500"/>
                    <aos:button text="搜索" width="100" height="30" icon="query.png" margin="0 0 0 10" onclick="_w_query" />
                    <aos:button text="查看文件" width="100" height="30" icon="picture.png" margin="0 0 0 10" onclick="_w_picture_show" />
                    <aos:button text="申请查看" width="100" height="30" icon="icq.png" margin="0 0 0 10" onclick="_w_shenqing_show" />
                </aos:docked>
                <aos:column type="rowno"  width="100"/>
                <aos:selmodel type="row" mode="multi" />
                <aos:column dataIndex="id_" header="流水号" hidden="true"  locked="true"/>
                <aos:column dataIndex="_path" header="电子文件" hidden="true"  />
                <c:forEach var="field" items="${fieldDtos}">
                    <aos:column dataIndex="${field.fieldenname}" filter="true"
                                header="${field.fieldcnname}" width="${field.dislen}"  rendererField="field_type_" />
                </c:forEach>
                <aos:column header="" flex="1" />
            </aos:gridpanel>
        </aos:viewport>
        <aos:window id="_w_data_i" title="详情"   draggable="false"  closable="false" closeAction="hide"
                    autoScroll="true" x="50" width="860" height="500" >
            <aos:formpanel id="_f_data_i" labelWidth="100"   height="800"  width="750" margin="0 0 0 20">

            </aos:formpanel>
            <aos:docked dock="bottom" ui="footer">
                <aos:dockeditem xtype="tbfill" />
                <aos:dockeditem onclick="#_w_data_i.hide();" text="关闭"
                                icon="close.png" />
            </aos:docked>
        </aos:window>
        <aos:window id="_w_open_path" title="全文信息" width="600" height="600" onshow="load_path_" >
            <aos:panel id="documentViewer3"   height="600" split="true" width="600" contentEl="documentViewer">
            </aos:panel>
            <aos:docked dock="bottom" ui="footer">
                <aos:dockeditem xtype="tbfill" />
                <aos:dockeditem onclick="shenqing_print" text="申请打印"
                                icon="more/dialog-ok-apply-3.png" />
                <aos:dockeditem onclick="#_w_open_path.hide();" text="关闭"
                                icon="close.png" />
            </aos:docked>
        </aos:window>
        <script type="text/javascript">
            function _w_shenqing_show(){
                var user=Ext.getCmp("user").getValue();
                var record = AOS.selectone(_g_param);
                if(AOS.empty(record)){
                    AOS.tip("请选择申请的数据!");
                    return;
                }else{
                    AOS.ajax({
                        params: {tid: AOS.selectone1(_g_param).data.id_,
                            tablename:"<%=request.getAttribute("tablename")%>",
                            formid:"<%=request.getAttribute("zz_id_")%>"
                        },
                        url: 'shenqing_file.jhtml',
                        ok: function (data) {

                        }
                    });
                }
            }
            function _w_query(){
                //结合用户添加的信息进行文书档案的查询操作
                var text=Ext.getCmp("text").getValue();
                var listtablename= "<%=request.getAttribute("tablename")%>";
                var params = {
                    text: text,
                    flag:1,
                    tablename:listtablename
                };
                //这个Store的命名规则为：表格ID+"_store"。
                _g_param_store.getProxy().extraParams = params;
                _g_param_store.load();
            }
            _w_data_input('_f_data_i');
            //生成录入界面
            function _w_data_input(formid){
                var _panel = Ext.getCmp(formid);
                _panel.removeAll();
                //_panel.reload();
                AOS.ajax({
                    params: {tablename: tablename.getValue()},
                    url: 'getInputColumn.jhtml',
                    ok: function (data) {
                        //var _panel = Ext.getCmp(formid);
                        var strdh ='';
                        for (var j in data){
                            //档号设置
                            if(data[j].dh=='1'){
                                var strfieldname = data[j].fieldname.substring(0,data[j].fieldname.length-1);
                                if(typeof(data[j].dh1)!='undefined'){
                                    strdh = strfieldname+','+data[j].dh1;
                                    if(typeof(data[j].dh2)!='undefined'){
                                        strdh=strdh+','+data[j].dh2;
                                        if(typeof(data[j].dh3)!='undefined'){
                                            strdh=strdh+','+data[j].dh3;
                                            if(typeof(data[j].dh4)!='undefined'){
                                                strdh=strdh+','+data[j].dh4;
                                                if(typeof(data[j].dh5)!='undefined'){
                                                    strdh=strdh+','+data[j].dh5;
                                                    if(typeof(data[j].dh6)!='undefined'){
                                                        strdh=strdh+','+data[j].dh6;
                                                        if(typeof(data[j].dh7)!='undefined'){
                                                            strdh=strdh+','+data[j].dh7;
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }//判断dh
                        }
                        for(var i in data){
                            var items;
                            if(data[i].fieldname.charAt(data[i].fieldname.length - 1)=='L'){
                                //设置标签必录入项
                                if(data[i].ynnnull=='0'){
                                    /*  items=[{
                                          xtype : 'label',
                                          //value:data[i].displayname,
                                          text:data[i].displayname,
                                          style:'color:red',

                                          width : parseInt(data[i].cwidth),
                                          height : parseInt(data[i].cheight),
                                          x:parseInt(data[i].cleft)-200,
                                          y:parseInt(data[i].ctop)-50,
                                      }]*/
                                }else{
                                    /*items=[{
                                        xtype : 'label',
                                        //value:data[i].displayname,
                                        text:data[i].displayname,
                                        width : parseInt(data[i].cwidth),
                                        height : parseInt(data[i].cheight),
                                        x:parseInt(data[i].cleft)-200,
                                        y:parseInt(data[i].ctop)-50,
                                    }]*/
                                }
                            }else{
                                if(data[i].yndic=='1'){
                                    var ynnull;
                                    if(data[i].ynnnull==0){
                                        ynnull=false;
                                    }else{
                                        ynnull=true;
                                    }
                                    var strdicname=data[i].fieldname.substring(0,data[i].fieldname.length-1);
                                    items=[{
                                        name:data[i].fieldname.substring(0,data[i].fieldname.length-1),
                                        id:data[i].fieldname.substring(0,data[i].fieldname.length-1),
                                        //fieldLabel: 'ieldLabel',
                                        xtype: "combo",
                                        mode:'local',
                                        fieldLabel:data[i].displayname,
                                        //x:parseInt(data[i].cleft)-200,
                                        //y:parseInt(data[i].ctop)-50,
                                        maxWidth:parseInt(data[i].cwidth),
                                        width:parseInt(data[i].cwidth),
                                        //height:parseInt(data[i].cheight),
                                        margin:'2,0,0,0',
                                        allowBlank:ynnull,
                                        listeners:{
                                            select:function(e){
                                                if(strdh.indexOf(this.name)>-1){
                                                    var strarray=strdh.split(',');
                                                    var strtemp='';
                                                    for(var i=1;i<strarray.length; i++){
                                                        if(i==1){
                                                            strtemp =Ext.getCmp(strarray[i]).getValue();
                                                            continue;
                                                        }
                                                        strtemp = strtemp+'-'+Ext.getCmp(strarray[i]).getValue();
                                                    }
                                                    Ext.getCmp(strarray[0]).setValue(strtemp);
                                                }
                                            }
                                        },
                                        //labelWidth:80,
                                        store: new Ext.data.SimpleStore({
                                            fields: ["code_", "desc_"],
                                            proxy: {
                                                type: "ajax",
                                                //params:{"tablename":"3333"},
                                                url: "load_dic_index.jhtml?key_name_="+data[i].dic,
                                                actionMethods: {
                                                    read: "POST"  //解决传中文参数乱码问题，默认为“GET”提交
                                                },
                                                reader: {
                                                    type: "json",  //返回数据类型为json格式
                                                    root: "root"  //数据
                                                }
                                            },
                                            autoLoad: false  //自动加载数据
                                        }),
                                        emptyText:'请选择...',
                                        displayField: 'desc_',
                                        valueField :'desc_',
                                        //hiddenName: 'fieldenname',
                                    }]
                                }
                                else{

                                    if(data[i].fieldname=="tmD"){
                                        items = itemsGroup_textareafield(data[i],strdh);
                                    }else{
                                        items = itemsGroup(data[i],strdh);
                                    }

                                }
                            }
                            _panel.add(items);
                        }
                    }
                });
            }

            function itemsGroup_textareafield(data,strdh){
                var strx=parseInt(data.cleft)-200;
                var stry = parseInt(data.ctop)-50;
                var strwidth = parseInt(data.cwidth);
                var strheight=parseInt(data.cheight);
                var displayname=data.displayname;

                var fieldname = data.fieldname.substring(0,data.fieldname.length-1);
                var ynnull;
                var strdisplayname;
                if(data.ynnnull==0){
                    ynnull=false;
                    strdisplayname='<lable style="color: red;">'+displayname+'</lable>';
                }else{
                    ynnull=true;
                    strdisplayname=displayname;
                }
                //var ynnull=data.ynnnull=='0';
                var str =[{
                    xtype :'textareafield',
                    id:fieldname,
                    name:fieldname,
                    width:strwidth,
                    margin:'2,0,0,0',
                    //maxWidth :strwidth,
                    height :strheight,
                    //x:strx,
                    //y:stry,
                    //columnWidth: 0.5,

                    fieldLabel:strdisplayname,
                    //maxLength:data.edtmax,
                    allowBlank:ynnull,
                    listeners:{focus:function(e){},
                        blur:function(e){
                            if(data.ynpw=='1'){
                                var val=e.getValue();
                                var len=val.length;
                                while(len < data.edtmax) {
                                    val= "0" + val;
                                    len++;
                                }
                                e.setValue(val);
                            }
                            if(strdh.indexOf(this.name)>-1){
                                var strarray=strdh.split(',');
                                var strtemp='';
                                //alert(strdh);
                                for(var i=1;i<strarray.length; i++){
                                    //alert(strarray[i]);
                                    if(i==1){
                                        strtemp =Ext.getCmp(strarray[i]).getValue();
                                        continue;
                                    }
                                    strtemp=strtemp+'-'+Ext.getCmp(strarray[i]).getValue();

                                }
                                //alert(strtemp);
                                Ext.getCmp(strarray[0]).setValue(strtemp);

                            }
                        }
                        //离开鼠标事件结尾
                    }
                }];
                //alert(str);
                //var item = eval('(' + str + ')');
                return str;
            }
            function itemsGroup(data,strdh){
                var strx=parseInt(data.cleft)-200;
                var stry = parseInt(data.ctop)-50;
                var strwidth = parseInt(data.cwidth);
                var strheight=parseInt(data.cheight);
                var displayname=data.displayname;

                var fieldname = data.fieldname.substring(0,data.fieldname.length-1);
                var ynnull;
                var strdisplayname;
                if(data.ynnnull==0){
                    ynnull=false;
                    strdisplayname='<lable style="color: red;">'+displayname+'</lable>';
                }else{
                    ynnull=true;
                    strdisplayname=displayname;
                }
                //var ynnull=data.ynnnull=='0';
                var str =[{
                    xtype :'textfield',
                    id:fieldname,
                    name:fieldname,
                    width:strwidth,
                    margin:'2,0,0,0',
                    //maxWidth :strwidth,
                    height :strheight,
                    //x:strx,
                    //y:stry,
                    //columnWidth: 0.5,

                    fieldLabel:strdisplayname,
                    //maxLength:data.edtmax,
                    allowBlank:ynnull,
                    listeners:{focus:function(e){},
                        blur:function(e){
                            if(data.ynpw=='1'){
                                var val=e.getValue();
                                var len=val.length;
                                while(len < data.edtmax) {
                                    val= "0" + val;
                                    len++;
                                }
                                e.setValue(val);
                            }
                            if(strdh.indexOf(this.name)>-1){
                                var strarray=strdh.split(',');
                                var strtemp='';
                                //alert(strdh);
                                for(var i=1;i<strarray.length; i++){
                                    //alert(strarray[i]);
                                    if(i==1){
                                        strtemp =Ext.getCmp(strarray[i]).getValue();
                                        continue;
                                    }
                                    strtemp=strtemp+'-'+Ext.getCmp(strarray[i]).getValue();

                                }
                                //alert(strtemp);
                                Ext.getCmp(strarray[0]).setValue(strtemp);

                            }
                        }
                        //离开鼠标事件结尾
                    }
                }];
                //alert(str);
                //var item = eval('(' + str + ')');
                return str;
            }
            function changeRowClass(record, rowIndex, rowParams, store){

                //得到当前行的指定的列的值
                if(record.get("_path")>=1){
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
            function select_tree(){
                var record = Ext.getCmp("_t_catalog").getStore().getNodeById("4a6520adda08429d84bb00a96e990b3e");

                var ss = Ext.getCmp("_t_catalog").getSelectionModel();
                ss.select(record);

            }
            function _w_params_show(){
                var strtablename=Ext.getCmp("tablename").getValue();
                var params = {
                    tablename: strtablename
                };
                //这个Store的命名规则为：表格ID+"_store"。
                _g_param_store.getProxy().extraParams = params;
                _g_param_store.load();
            }
            function add_count(){
                var fieldtext=Ext.getCmp('_f_query').down("[name='fieldtext']").getValue();
                var fieldname=Ext.getCmp('_f_query').down("[name='filedname']").getValue();
                if(fieldname=="null"||fieldname==""||fieldname==null){
                    AOS.err("请选择字段");
                    return;
                }
                var _select_radio_check = Ext.getCmp('_select_radio').items;
                var xq = '';
                for(var i = 0; i < _select_radio_check.length; i++){
                    if(_select_radio_check.get(i).checked){
                        xq =_select_radio_check.get(i).boxLabel;
                    }
                }
                if(xq=="精确查询"){
                    var data=[{
                        'selectorder':"and "+fieldname+" = ",
                        'selectmath':fieldtext
                    }];
                    _g_count_store.loadData(data,true);
                }
                if(xq=="模糊查询"){
                    var data=[{
                        'selectorder':"and "+fieldname+" like ",
                        'selectmath':"%"+fieldtext+"%"
                    }];
                    _g_count_store.loadData(data,true);
                }
                if(xq=="高级检索"){
                    //条件
                    var and=Ext.getCmp('_f_query').down("[name='and']").getValue();
                    //连接符
                    var condition=Ext.getCmp('_f_query').down("[name='condition']").getValue();
                    var selectorder="";
                    var selectmath="";
                    if(condition=="like"){
                        condition="like";
                        selectorder=and+" "+fieldname+" like ";
                        selectmath="%"+fieldtext+"%";
                    }else if(condition=="left"){
                        selectorder=and+" "+fieldname+" like ";
                        selectmath="%"+fieldtext;
                    }else if(condition=="right"){
                        selectorder=and+" "+fieldname+" like ";
                        selectmath=fieldtext+"%";
                    }else if(condition=="null"){
                        selectorder=and+" "+fieldname+" is ";
                        selectmath="null";
                    }else{
                        selectorder=and+" "+fieldname+" "+condition;
                        selectmath=fieldtext;
                    }
                    var data=[{
                        'selectorder':selectorder,
                        'selectmath':selectmath
                    }];
                    _g_count_store.loadData(data,true);
                }
            }
            function delete_count(){
                var row = _g_count.getSelectionModel().getSelection();
                var rowid = _g_count_store.indexOf(row[0]);//获得选中的第一项在store内的行号
                //此时删除严肃，在他的上一行添加
                _g_count_store.removeAt(rowid);
            }
            //根据选择的门类名称，进行字段列表的查询操作
            function _load_fieldlists(){
                //默认把and隐藏
                Ext.getCmp("and").hide();
                Ext.getCmp("condition").hide();
                //把门类字段列表传递过去
                var params = {
                    id_ : Ext.getCmp("tablename_id").getValue(),
                };
                filedname_store.getProxy().extraParams = params;
                filedname_store.load();
            }
            function hidetoshow(){
                var _select_radio_check = Ext.getCmp('_select_radio').items;
                var xq = '';
                for(var i = 0; i < _select_radio_check.length; i++){
                    if(_select_radio_check.get(i).checked){
                        xq =_select_radio_check.get(i).boxLabel;
                    }
                }
                if(xq=="高级检索"){
                    Ext.getCmp("and").show();
                    Ext.getCmp("condition").show();
                }else{
                    Ext.getCmp("and").hide();
                    Ext.getCmp("condition").hide();
                }
            }
            function _w_query_show() {
                _w_query_q.show();
            }
            //查询参数列表
            function _g_param_query(view, record, item, index, e) {
                var url =  record.raw.url_;
                var record = AOS.selectone(_t_catalog);
                var djbh = _djbh.getValue();
                var recordid = _recordid.getValue();
                if (!Ext.isEmpty(url)) {
                    window.location.href="loadData.jhtml?id_="+record.raw.id+"&name_="+record.raw.name_+"&djbh="+djbh+"&recordid="+recordid;
                    //fnaddtab(record.raw.id, record.raw.text, url,root_id_,tablename,record.raw.b);
                }else{
                    if(record.raw.leaf){
                        AOS.tip('没有配置菜单的请求地址。');
                    }
                }
            }

            function _w_params_show(){
                var strtablename=Ext.getCmp("tablename").getValue();
                var params = {
                    tablename: strtablename
                };
                //这个Store的命名规则为：表格ID+"_store"。
                _g_param_store.getProxy().extraParams = params;
                _g_param_store.load();
            }

            //刷新分类树
            function _t_catalog_refresh() {
                var refreshnode = AOS.selectone(_t_catalog);
                if (refreshnode.isLeaf()) {
                    refreshnode = refreshnode.parentNode;
                }
                _t_catalog_store.load({
                    node: refreshnode,
                    callback: function () {
                        refreshnode.collapse();
                        refreshnode.expand();
                    }
                });
            }
            //确定执行统计设计
            function _f_data_query2(){
                //如果统计字段和统计方法没有选中直接不执行
                var s=Ext.getCmp('_g_count').getStore();
                var selectorders="";
                var selectmaths="";
                var query="";
                for(var i = 0 ;i< s.getCount(); i++){
                    query+=s.getAt(i).get('selectorder') +" '"+s.getAt(i).get('selectmath')+"'";
                }
                var params = AOS.getValue('_f_query');
                params["query"]=query;
                params["id_"]=Ext.getCmp("tablename_id").getValue();
                params["tablename"]="<%=request.getAttribute("tablename")%>";
                _g_param_store.getProxy().extraParams = params;
                _g_param_store.load();
                _w_query_q.hide();

            }
            function fn_g_data() {
                AOS.ajax({
                    params: {id_: AOS.selectone1(_g_param).data.id_,
                        tablename:tablename.getValue()
                    },
                    url: 'getData.jhtml',
                    ok: function (data) {
                        _f_data_i.form.setValues(data);
                        _w_data_i.show();
                    }
                });
            }
            //弹出选择卡号窗口
            function _w_account_find_show() {
                _w_account_find.show();
            }

            //加载表格数据
            function _g_djform_query() {
                var params = {
                    xm : _xm_.getValue()
                };
                //这个Store的命名规则为：表格ID+"_store"。
                _g_djform_store.getProxy().extraParams = params;
                _g_djform_store.load();
            }
            //账户表格双击事件
            function _g_djform_dbclick(obj, record) {
                _g_param.down('[name=djbh]').setValue(record.raw.djbh);
                _g_param.down('[name=recordid]').setValue(record.raw.id_);
                _w_account_find.hide();
            }

            //显示上传面板
            function _w_picture_show() {
                var user=Ext.getCmp("user").getValue();
                var record = AOS.selectone(_g_param);
                AOS.ajax({
                    params : {
                        user:user,
                        id_ : record.data.id_,
                        zz_id_:"<%=request.getAttribute("zz_id_")%>",
                        tablename:"<%=request.getAttribute("tablename")%>"
                    },
                    url : 'getPictureLook.jhtml',
                    ok : function(data) {
                        //将id值赋给hiddlen中
                        if (data.appcode===1) {
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
                                        var table=record.data.daml_en;
                                        //parent.fnaddtab(row.data.id, '电子文件',
                                        //					'archive/data/openFile.jhtml?id='+row.data.pid+'&tid='+row.data.tid+'&type='+row.data.type+'&tablename='+tablename.getValue());
                                        /*parent.fnaddtab(row.data.id, '电子文件',
                                                            'archive/data/openPdfFile.jhtml?id='+row.data.pid+'&tid='+row.data.tid+'&type='+row.data.type+'&tablename='+tablename.getValue());*/
                                        /* parent.parent.fnaddtab(row.data.id, '电子文件',
                                             'archive/data/openPdfFile.jhtml?id='+row.data.pid+'&tid='+row.data.tid+'&type='+row.data.type+'&tablename='+table+'&index='+rowIndex);*/
                                        /*parent.fnaddtab(row.data.id, '电子文件',
                                                             'archive/data1/initZpData.jhtml?id='+row.data.pid+'&tid='+row.data.tid+'&type='+row.data.type+'&tablename='+tablename.getValue());*/
                                        //window.open('openPdfFile.jhtml?id='+row.data.pid+'&tid='+row.data.tid+'&type='+row.data.type+'&tablename='+table+'&index='+rowIndex);
                                        _w_open_path.show();
                                        //此时关闭电子文件窗口
                                        w_data_path.hide();


                                    }
                                },
                                //单机ocr选框
                                ocrPath:function(){
                                    var record = AOS.selectone(_g_param);
                                    var value= Ext.getCmp("ocr").getValue();
                                    var type="checkbox";
                                    var name="ocr";
                                    var tablename=record.data.daml_en;
                                    //走后台存储当前用户选择的定义以便下次调用
                                    remember_load(tablename,type,value,name);
                                },
                                //单机mark选框
                                markPath:function(){
                                    var value= Ext.getCmp("mark").getValue();
                                    var type="checkbox";
                                    var name="mark";
                                    var tablename="<%=request.getAttribute("tablename")%>";
                                    //走后台存储当前用户选择的定义以便下次调用
                                    remember_load(tablename,type,value,name);
                                },
                                //单机info选框
                                infoPath:function(){
                                    var value= Ext.getCmp("info").getValue();
                                    var type="checkbox";
                                    var name="info";
                                    var tablename=Ext.getCmp("tablename").getValue();
                                    //走后台存储当前用户选择的定义以便下次调用
                                    remember_load(tablename,type,value,name);
                                },
                                onUpload : function(grid, rowIndex, colIndex){

                                    if(user!="root"){
                                        AOS.err("没有权限!");
                                        return;
                                    }
                                    var me=Ext.getCmp("uploadpanel").getSelectionModel().getSelection();
                                    var pid = me[0].get('pid');
                                    var tid = me[0].get('tid');
                                    var type=me[0].get('type');
                                    var record = AOS.selectone(_g_param);
                                    var tablename="<%=request.getAttribute("tablename")%>";
                                    parent.fnaddtab(pid, '电子文件',
                                        'archive/data/openFile.jhtml?id='+pid+'&tid='+tid+'&type='+type+'&tablename='+table+'&index='+rowIndex);
                                },
                                deletePath:	function(grid, rowIndex, colIndex) {
                                    //AOS.tip("请双击文件进行查看。");
                                    // return;
                                    if(user!="root"){
                                        AOS.err("没有权限!");
                                        return;
                                    }
                                    var me=Ext.getCmp("uploadpanel").getSelectionModel().getSelection();

                                    var id = me[0].get('pid');
                                    var tid = me[0].get('tid');
                                    var rowIndex = Ext.getCmp("uploadpanel").getStore().indexOf(me[0]);
                                    var record = AOS.selectone(_g_param);
                                    var tablename="<%=request.getAttribute("tablename")%>";
                                    AOS.ajax({
                                        params: {id_:id,
                                            tablename:tablename,
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
                                    var selection = AOS.selection(_g_data, 'id_');
                                    var record = AOS.selectone(_g_param);
                                    var tablename="<%=request.getAttribute("tablename")%>";
                                    AOS.ajax({
                                        params: {aos_rows_: selection,
                                            tm:record.data.tm,
                                            tablename: tablename
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
                                    var record = AOS.selectone(_g_param);
                                    var table="<%=request.getAttribute("tablename")%>";
                                    AOS.file('downloadPath.jhtml?id_='+id+'&tablename='+table);
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
                                        var record = AOS.selectone(_g_param);
                                        var table="<%=request.getAttribute("tablename")%>";
                                        AOS.file('downloadPath.jhtml?id_='+id+'&tablename='+table);
                                    }
                                },
                                upload_complete_handler : function(file){
                                    if(user!="root"){
                                        AOS.err("没有权限!");
                                        return;
                                    }
                                    var me =Ext.getCmp("uploadpanel");
                                    var record = AOS.selectone(_g_param);
                                    var table="<%=request.getAttribute("tablename")%>";
                                    AOS.ajax({
                                        params: {tid: record.data.id_,tablename:table},
                                        url: 'getPath.jhtml',
                                        ok: function (data) {
                                            for(i in data){
                                                me.store.getAt(file.index).set({"pid":data[i].id_,"tid":data[i].tid});
                                            }
                                        }
                                    });
                                },
                                post_params:{tid:record.data.id_,
                                    tablename:"<%=request.getAttribute("tablename")%>"
                                },
                                file_size_limit : 10000,//MB

                                flash_url : "${cxt}/static/swfupload/swfupload.swf",
                                flash9_url : "${cxt}/static/swfupload/swfupload_f9.swf",
                                upload_url : "${cxt}/archive/upload/archiveUpload.jhtml?tm="+record.data.tm
                            });

                            var w_data_path = new Ext.Window({
                                title : '电子文件',
                                width : 700,
                                modal:true,
                                closeAction : 'destroy',
                                items:[uploadPanel]
                            });
                            w_data_path.on("show",w_data_path_onshow);
                            //w_data_path.on("close",w_data_path_onclose);
                            w_data_path.show();
                        } else {
                            AOS.err("对不起你没有权限查看电子文件,请申请!");
                        }
                    }
                });






            }
            function load_path_(){
                var me=Ext.getCmp("uploadpanel").getSelectionModel().getSelection();
                document.getElementById('documentViewer3').innerHTML = "";
                AOS.ajax({
                    params : {
                        id_ : me[0].data.pid,
                        tablename:"<%=request.getAttribute("tablename")%>",
                        tid:me[0].data.tid
                    },
                    url : 'getpath.jhtml',
                    ok : function(data) {
                        //将id值赋给hiddlen中
                        if (data.urlpath == "" || data.urlpath == null) {
                            Ext.getCmp("_path").setValue("");
                            document.getElementById('documentViewer3').innerHTML = "";
                        } else {
                            //new PDFObject({ url:data.pdfpath}).embed("documentViewer");

                            Ext.getCmp("_path").setValue(data.pdfpath);
                            PDFObject.embed(data.urlpath, '#documentViewer3');
                        }
                    }
                });
            }
            window.onload=function(){
                Ext.getCmp("listTablename").setValue("<%=request.getAttribute("tablename")%>");
                Ext.getCmp("listTablename").setRawValue("<%=request.getAttribute("tabledesc")%>");
            }
            //先弹出申请打印窗口
            function shenqing_message(){
                _w_shenqing_u.show();
            }
            function close_shenqing_message(){
                AOS.reset(_f_shenqing_u);
            }
            function shenqing_print(){
                var me=Ext.getCmp("uploadpanel").getSelectionModel().getSelection();
                AOS.ajax({
                    params:{
                        id_:me[0].data.pid,
                        tablename:"<%=request.getAttribute("tablename")%>",
                        tid:me[0].data.tid
                    },
                    url : 'shenqing_print.jhtml',
                    ok : function(data) {
                        //将id值赋给hiddlen中
                        if (data.appcode ===1) {
                            AOS.tip("申请成功，请稍后");
                        } else {
                            AOS.tip("申请失败!");
                        }
                    }
                });
            }
            function w_data_path_onshow() {
                var record = AOS.selectone(_g_param);
                var tablenameId="<%=request.getAttribute("tablename")%>"
                var record = AOS.selectone(Ext.getCmp('_g_param'));
                var me=Ext.getCmp("uploadpanel");
                me.store.removeAll();
                AOS.ajax({
                    params: {tid: record.data.id_,tablename:tablenameId},
                    url: 'getPath.jhtml',
                    ok: function (data) {
                        for(i in data){
                            me.store.add({
                                pid:data[i].id_,
                                tid:data[i].tid,
                                name:data[i]._path,
                                fileName:data[i].filename,
                                type:data[i].filetype,
                                percent:100,
                                status:-4,
                            });
                        }
                    }
                });

            }
            function w_data_path_onclose(){
                _g_param_store.load();
            }

            function fn_lyzt_render(value, metaData, record, rowIndex, colIndex,
                                    store) {
                if (value === 1) {
                    return '已查到';
                } else {
                    return '未查到';
                }
            }

            function fn_path_render(value, metaData, record, rowIndex, colIndex,
                                    store) {
                if (value >= 1) {
                    return '<img src="${cxt}/static/icon/picture.png" />';
                } else {
                    return '<img src="${cxt}/static/icon/picture_empty.png" />';
                }
            }
            function _no_query(){
                var selection = AOS.selection(_g_param,'id_');
                AOS.ajax({
                    url:'insertMakeDetail.jhtml',
                    params:{
                        formid:_recordid.getValue(),
                        aos_rows_: selection,
                        //tid:record.data.id_,
                        tablename:Ext.getCmp("tablename").getValue(),
                        cxjg:0
                    },
                    ok:function (data) {
                        AOS.tip(data.appmsg);
                    }
                })
            }
            function _yes_query(){
                var selection = AOS.selection(_g_param,'id_');
                AOS.ajax({
                    url:'insertMakeDetail.jhtml',
                    params:{
                        formid:_recordid.getValue(),
                        aos_rows_: selection,
                        //tid:record.data.id_,
                        tablename:Ext.getCmp("tablename").getValue(),
                        cxjg:1
                    },
                    ok:function (data) {
                        AOS.tip(data.appmsg);

                    }
                })
            }
            function _yes_provide(){
                AOS.ajax({
                    url:'updateMake.jhtml',
                    params:{
                        id_:_recordid.getValue(),
                        nftg:'可以提供'
                    }
                })
            }
            function _no_provide(){
                _w_provide.show();
            }
            function  _f_provide_save(){
                AOS.ajax({
                    forms:_f_provide,
                    url:'updateMake.jhtml',
                    params:{
                        id_:_recordid.getValue(),
                        nftg:'不可提供'
                    }
                })
                var parenttab=parent.closetab();
                //针对纯的grid的刷新
                var parentframes=parent.frames["_id_tab_d98465c65f9644628552c5b6286d9b36.iframe-frame"];
                var aa=parentframes.Ext.getCmp('_g_make_detail');
                aa.store.reload();
                //刷新真个页面
                var curtab = parenttab.getActiveTab();
                parenttab.remove(curtab);
            }
            function fn_column_sub_onclick(){
                var record = AOS.selectone(Ext.getCmp('_g_data'));
                var msg = AOS.merge('您将要提交该单据！并且启动流程您将无法进行修改和删除！！！ 点击确认进行提交 , 点击取消取消该操作');
                AOS.confirm(msg,function (btn){
                    if(btn === 'cancel'){
                        AOS.tip('操作取消');
                        return;
                    }
                    AOS.ajax({
                        url:'startInstance.jhtml',
                        params:{
                            id_:record.data.id_,
                            state:1
                        },
                        ok:function(data){
                            Ext.getCmp('_g_data').store.load();
                            AOS.tip(data.appmsg);
                        }
                    })
                })
            }
            function fn_onselect(e){
                var user=Ext.getCmp("user").getValue();
                var zz_id_=Ext.getCmp("zz_id_").getValue();
                window.location.href="zizhu_initData.jhtml?user="+user+"&tablename="+e.value+"&tabledesc="+e.rawValue+"&zz_id_="+zz_id_;
            }
            function _f_data_query(){
                var params = AOS.getValue('_f_query2');
                var form = Ext.getCmp('_f_query2');
                var tmp = columnnum.getValue();
                for (var i = 1; i <= tmp; i++) {
                    var str = form.down("[name='filedname" + i + "']");
                    var filedname = str.getValue();
                    if (filedname == null) {
                        params["filedname" + i] = str.regexText;
                    }
                    var emptyfiledcnname = str.emptyText;
                    var filedcnname = Ext.getCmp("filedname" + i).getRawValue();
                    if (emptyfiledcnname == filedcnname && filedcnname != null && filedcnname != "") {
                        params["filedcnname" + i] = filedcnname;
                    } else if (filedcnname == null || filedcnname == "") {
                        params["filedcnname" + i] = emptyfiledcnname;
                    }
                }
                params["flag"]=1;
                _g_param_store.getProxy().extraParams = params;
                _g_param_store.load();
                _w_query_q.hide();
                AOS.reset(_f_query2);
            }
            //websocket
            //建立socket连接
            var sock;
            if ('WebSocket' in window) {
                sock = new WebSocket("ws://127.0.0.1:80/aosyb/websocketDemo/${user}");
            } else {
                alert('该浏览器不支持WebSocket!');
            }
            sock.onopen = function (e) {
                console.log(e);
            };
            sock.onmessage = function (e) {
                console.log(e);
                updateMsg();
                var loop =Ext.create(
                    'widget.uxNotification',
                    {
                        position : 'br',
                        title : '<span class="app-container-title-normal"><i class="fa fa-bell-o"></i> 通知</span>',
                        closable : false,
                        useXAxis : false,
                        slideInDuration : 150,
                        autoCloseDelay : 10000,
                        width : 300,
                        html : e.data
                    });
                loop.show();
            };
            sock.onerror = function (e) {
                console.log(e);
               //alert(e.data);
            };
            sock.onclose = function (e) {
                console.log(e);
            };
        </script>
    </aos:onready>
</aos:html>
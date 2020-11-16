<%@page contentType="text/html; charset=UTF-8" isELIgnored="false"%>
<%@taglib prefix="aos" uri="/WEB-INF/tld/aos.tld"%>
<%@ taglib uri="/WEB-INF/tld/cll.tld" prefix="c"%>
<%
    String path = request.getContextPath();
    Object fieldDtos = request.getAttribute("fieldDtos");
%>
<aos:html>
    <aos:head title="检索档案">
        <aos:base href="archive/lydepot"/>
        <aos:include lib="ext,swfupload"/>
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
            <aos:gridpanel id="_g_param" url="listAccounts_hlw.jhtml" region="center" onitemclick="itemclick"  rowclass="true" onrender="_w_params_show" onitemdblclick="fn_g_data">
                <aos:docked forceBoder="0 0 1 0">
                    <aos:hiddenfield id="rowmath" name="rowmath" value="0" />
                    <aos:hiddenfield  id="tablename_id" name="tablename_id" value="${id_}"/>
                    <aos:hiddenfield  id="tablename" name="tablename" value="${tablename}"/>
                    <aos:hiddenfield  id="_recordid" name="_recordid" value="${recordid}"/>
                    <aos:hiddenfield  id="query" name="query" value="${query}"/>
                    <aos:hiddenfield  id="user" name="user" value="${user}"/>
                    <aos:hiddenfield  id="ids" name="ids" />
                    <aos:combobox name="listTablename"
                                  fields="[ 'tablename', 'tabledesc']" fieldLabel="选择门类"
                                  id="listTablename" editable="false" columnWidth="0.24"
                                  url="listTablename.jhtml" displayField="tabledesc"
                                  valueField="tablename"  allowBlank="false" onselect="fn_onselect"/>
                    <aos:dockeditem onclick="_w_query_show" text="查询条件" icon="query.png" />
                    <aos:dockeditem onclick="_w_picture_show" text="显示" icon="picture.png" />
                    <aos:dockeditem text="删除" icon="del.png" onclick="_g_data_del" />

                </aos:docked>
                <aos:column type="rowno"  width="60"/>
                <aos:selmodel type="row" mode="multi" />
                <aos:column dataIndex="tablename_id"  hidden="true" />
                <aos:column dataIndex="_path" header="电子文件"
                            rendererFn="fn_path_render" hidden="true"/>
                <aos:column dataIndex="id_" header="流水号" hidden="true" />
                <c:forEach var="field" items="${fieldDtos}">
                    <aos:column dataIndex="${field.fieldenname}"
                                header="${field.fieldcnname}" width="${field.dislen}"  rendererField="field_type_" />
                </c:forEach>
                <aos:column header="" flex="1" />
            </aos:gridpanel>
            <aos:window id="_w_query_q" title="查询" width="720" autoScroll="true"
                        layout="fit" onshow="_load_fieldlists">
                <aos:tabpanel id="_tabpanel" region="center" activeTab="0"
                              bodyBorder="0 0 0 0" tabBarHeight="30">
                <aos:tab title="列表式搜索" id="_tab_org">
                    <aos:formpanel id="_f_query2" layout="column" columnWidth="1">
                        <aos:hiddenfield name="tablename" value="${tablename }" />
                        <aos:hiddenfield name="columnnum" id="columnnum" value="8" />
                        <aos:hiddenfield name="selectmark" id="selectmark" value="1" />
                        <aos:hiddenfield name="selectmath" id="selectmath" value="0" />
                        <c:forEach var="fieldss" items="${fieldDtos}" end="7"
                                   varStatus="listSearch">
                            <aos:combobox name="and${listSearch.count}" value="on"
                                          labelWidth="10" columnWidth="0.2">
                                <aos:option value="on" display="并且" />
                                <aos:option value="up" display="或者" />
                            </aos:combobox>
                            <aos:combobox id="filedname${listSearch.count}"
                                          name="filedname${listSearch.count}"
                                          emptyText="${fieldss.fieldcnname}" labelWidth="20"
                                          columnWidth="0.2" fields="['fieldenname','fieldcnname']"
                                          regexText="${fieldss.fieldenname}" displayField="fieldcnname"
                                          valueField="fieldenname"
                                          url="queryFields.jhtml?tablename=${tablename}">
                            </aos:combobox>
                            <aos:combobox name="condition${listSearch.count }" value="like"
                                          labelWidth="20" columnWidth="0.2">
                                <aos:option value="=" display="等于" />
                                <aos:option value=">" display="大于" />
                                <aos:option value=">=" display="大于等于" />
                                <aos:option value="<" display=" 小于" />
                                <aos:option value="<=" display="小于等于" />
                                <aos:option value="<>" display="不等于" />
                                <aos:option value="like" display="包含" />
                                <aos:option value="left" display="左包含" />
                                <aos:option value="right" display="右包含" />
                                <aos:option value="null" display="空值" />
                            </aos:combobox>
                            <aos:textfield name="content${listSearch.count }"
                                           allowBlank="true" columnWidth="0.39" />
                        </c:forEach>
                        <aos:docked dock="bottom" ui="footer">
                            <aos:dockeditem xtype="tbfill" />
                            <aos:dockeditem onclick="_f_data_query" text="确定" icon="ok.png" />
                            <aos:dockeditem onclick="#_w_query_q.hide();" text="关闭"
                                            icon="close.png" />
                        </aos:docked>
                    </aos:formpanel>
                </aos:tab>
                </aos:tabpanel>
            </aos:window>
        </aos:viewport>


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


        <aos:window id="_w_account_find" title="登记编号[双击选择]" height="-200" width="800" layout="fit" >
            <aos:gridpanel id="_g_djform" url="listDjform.jhtml" onrender="_g_djform_query"
                           onitemdblclick="_g_djform_dbclick">
                <aos:docked>
                    <aos:triggerfield emptyText="姓名" id="_xm_" onenterkey="_g_account_query" trigger1Cls="x-form-search-trigger"
                                      onTrigger1Click="_g_djform_query" width="250" />
                </aos:docked>
                <aos:column type="rowno" />
                <aos:column header="id_" dataIndex="id_"/>
                <aos:column header="登记编号" dataIndex="djbh"/>
                <aos:column header="姓名" dataIndex="xm"/>
                <aos:column header="登记日期" dataIndex="djrq"/>
                <aos:column header="档号" dataIndex="dh"/>
                <aos:column header="题名" dataIndex="tm"/>
                <aos:column header="利用状态" dataIndex="lyzt" rendererFn="fn_lyzt_render"/>
                <aos:column header="利用方式" dataIndex="lyfs"/>
                <aos:column header="利用类型" dataIndex="lylx"/>
                <aos:column header="利用类别" dataIndex="lylb"/>
                <aos:column header="利用数量" dataIndex="lysl"/>
                <aos:column header="利用目的" dataIndex="lymd"/>
                <aos:column header="备注" dataIndex="bz"/>
                <aos:column header="" flex="1"/>
            </aos:gridpanel>
        </aos:window>
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
        <aos:window id="_w_select_edit_archive" title="局域网数据选择"  width="800" height="500" onshow="load_archive">
            <aos:gridpanel id="_g_select_edit_archive" url="listdata_edit.jhtml" region="north" width="770" height="460"
                           autoScroll="true" pageSize="100" enableLocking="true" hidePagebar="false">
                <aos:docked forceBoder="0 0 1 0">
                    <aos:dockeditem xtype="tbtext" text="数据列表" />
                    <aos:dockeditem text="查询" icon="query.png"
                                    onclick="_w_select_edit_query_show" />
                    <aos:dockeditem  text="确定" icon="ok.png"
                                     onclick="ok_save_archive_edit" />
                    <aos:dockeditem  text="全选" icon="more/edit-select-all-4.png"
                                     onclick="ok_save_All_archive" />
                </aos:docked>
                <aos:selmodel type="checkbox" mode="multi" />
                <aos:column dataIndex="id_" header="流水号" hidden="true" />
                <aos:column header="_path" dataIndex="_path" hidden="true" />
                <aos:column header="全宗单位" dataIndex="qzdw" />
                <aos:column header="档号" dataIndex="dh"  width="200" />
                <aos:column header="责任者" dataIndex="zrz"  width="200" />
                <aos:column header="形成时间" dataIndex="xcsj" />
                <aos:column header="题名" dataIndex="tm"    width="500"/>
                <aos:column header="页号" dataIndex="yh" />
                <aos:column header="页数" dataIndex="ys"  />
                <aos:column header="是否开放" dataIndex="sfkf" />
                <aos:column header="全宗号" dataIndex="qzh" />
                <aos:column header="目录号" dataIndex="mlh"  />
                <aos:column header="保管期限" dataIndex="bgqx" />
                <aos:column header="案卷号" dataIndex="ajh" />
                <aos:column header="顺序号（件号）" dataIndex="sxh" />
                <aos:column header="" flex="1" />
            </aos:gridpanel>
            <aos:window id="_w_query_edit_select_q" title="查询" width="720" autoScroll="true"
                        layout="fit" onshow="combobox_tableFileldlist">
                <aos:tabpanel id="_tabpanel_edit_select" region="center" activeTab="0"
                              bodyBorder="0 0 0 0" tabBarHeight="30">
                    <aos:tab title="列表式搜索" id="_tab_select_edit_org">
                        <aos:formpanel id="_f_select_edit_query" layout="column" columnWidth="1">
                            <aos:hiddenfield name="cascode_id_" value="${cascode_id_ }" />
                            <aos:hiddenfield name="tablename" value="${tablename }" />
                            <aos:hiddenfield name="columnnum"  value="7" />
                            <aos:combobox name="and1" value="on"
                                          labelWidth="10" columnWidth="0.2">
                                <aos:option value="on" display="并且" />
                                <aos:option value="up" display="或者" />
                            </aos:combobox>
                            <aos:combobox name="filednames1" id="filednames1" url="getComboboxList.jhtml?tablename=${tablename}"  fields="['fieldenname','fieldcnname']"
                                          displayField="fieldcnname"
                                          valueField="fieldenname"  labelWidth="20" columnWidth="0.2"  >
                            </aos:combobox>

                            <aos:combobox name="condition1" value="like"
                                          labelWidth="20" columnWidth="0.2">
                                <aos:option value="=" display="等于" />
                                <aos:option value=">" display="大于" />
                                <aos:option value=">=" display="大于等于" />
                                <aos:option value="<" display="小于" />
                                <aos:option value="<=" display="小于等于" />
                                <aos:option value="<>" display="不等于" />
                                <aos:option value="like" display="包含" />
                                <aos:option value="left" display="左包含" />
                                <aos:option value="right" display="右包含" />
                                <aos:option value="null" display="空值" />
                            </aos:combobox>
                            <aos:textfield name="content1"
                                           allowBlank="true" columnWidth="0.39" />

                            <aos:combobox name="and2" value="on"
                                          labelWidth="10" columnWidth="0.2">
                                <aos:option value="on" display="并且" />
                                <aos:option value="up" display="或者" />
                            </aos:combobox>
                            <aos:combobox name="filednames2" id="filednames2" url="getComboboxList.jhtml?tablename=${tablename}"  fields="['fieldenname','fieldcnname']"
                                          displayField="fieldcnname"
                                          valueField="fieldenname"  labelWidth="20" columnWidth="0.2"  >
                            </aos:combobox>

                            <aos:combobox name="condition2" value="like"
                                          labelWidth="20" columnWidth="0.2">
                                <aos:option value="=" display="等于" />
                                <aos:option value=">" display="大于" />
                                <aos:option value=">=" display="大于等于" />
                                <aos:option value="<" display="小于" />
                                <aos:option value="<=" display="小于等于" />
                                <aos:option value="<>" display="不等于" />
                                <aos:option value="like" display="包含" />
                                <aos:option value="left" display="左包含" />
                                <aos:option value="right" display="右包含" />
                                <aos:option value="null" display="空值" />
                            </aos:combobox>
                            <aos:textfield name="content2"
                                           allowBlank="true" columnWidth="0.39" />

                            <aos:combobox name="and3" value="on"
                                          labelWidth="10" columnWidth="0.2">
                                <aos:option value="on" display="并且" />
                                <aos:option value="up" display="或者" />
                            </aos:combobox>
                            <aos:combobox name="filednames3" id="filednames3" url="getComboboxList.jhtml?tablename=${tablename}"  fields="['fieldenname','fieldcnname']"
                                          displayField="fieldcnname"
                                          valueField="fieldenname"  labelWidth="20" columnWidth="0.2"  >
                            </aos:combobox>

                            <aos:combobox name="condition3" value="like"
                                          labelWidth="20" columnWidth="0.2">
                                <aos:option value="=" display="等于" />
                                <aos:option value=">" display="大于" />
                                <aos:option value=">=" display="大于等于" />
                                <aos:option value="<" display="小于" />
                                <aos:option value="<=" display="小于等于" />
                                <aos:option value="<>" display="不等于" />
                                <aos:option value="like" display="包含" />
                                <aos:option value="left" display="左包含" />
                                <aos:option value="right" display="右包含" />
                                <aos:option value="null" display="空值" />
                            </aos:combobox>
                            <aos:textfield name="content3"
                                           allowBlank="true" columnWidth="0.39" />
                            <aos:combobox name="and4" value="on"
                                          labelWidth="10" columnWidth="0.2">
                                <aos:option value="on" display="并且" />
                                <aos:option value="up" display="或者" />
                            </aos:combobox>
                            <aos:combobox name="filednames4" id="filednames4" url="getComboboxList.jhtml?tablename=${tablename}"  fields="['fieldenname','fieldcnname']"
                                          displayField="fieldcnname"
                                          valueField="fieldenname"  labelWidth="20" columnWidth="0.2"  >
                            </aos:combobox>

                            <aos:combobox name="condition4" value="like"
                                          labelWidth="20" columnWidth="0.2">
                                <aos:option value="=" display="等于" />
                                <aos:option value=">" display="大于" />
                                <aos:option value=">=" display="大于等于" />
                                <aos:option value="<" display="小于" />
                                <aos:option value="<=" display="小于等于" />
                                <aos:option value="<>" display="不等于" />
                                <aos:option value="like" display="包含" />
                                <aos:option value="left" display="左包含" />
                                <aos:option value="right" display="右包含" />
                                <aos:option value="null" display="空值" />
                            </aos:combobox>
                            <aos:textfield name="content4"
                                           allowBlank="true" columnWidth="0.39" />

                            <aos:combobox name="and5" value="on"
                                          labelWidth="10" columnWidth="0.2">
                                <aos:option value="on" display="并且" />
                                <aos:option value="up" display="或者" />
                            </aos:combobox>
                            <aos:combobox name="filednames5" id="filednames5" url="getComboboxList.jhtml?tablename=${tablename}"  fields="['fieldenname','fieldcnname']"
                                          displayField="fieldcnname"
                                          valueField="fieldenname"  labelWidth="20" columnWidth="0.2"  >
                            </aos:combobox>

                            <aos:combobox name="condition5" value="like"
                                          labelWidth="20" columnWidth="0.2">
                                <aos:option value="=" display="等于" />
                                <aos:option value=">" display="大于" />
                                <aos:option value=">=" display="大于等于" />
                                <aos:option value="<" display="小于" />
                                <aos:option value="<=" display="小于等于" />
                                <aos:option value="<>" display="不等于" />
                                <aos:option value="like" display="包含" />
                                <aos:option value="left" display="左包含" />
                                <aos:option value="right" display="右包含" />
                                <aos:option value="null" display="空值" />
                            </aos:combobox>
                            <aos:textfield name="content5"
                                           allowBlank="true" columnWidth="0.39" />

                            <aos:docked dock="bottom" ui="footer">
                                <aos:dockeditem xtype="tbfill" />
                                <aos:dockeditem onclick="_f_select_edit_data_query" text="确定" icon="ok.png" />
                                <aos:dockeditem onclick="#_w_query_edit_select_q.hide();" text="关闭"
                                                icon="close.png" />
                            </aos:docked>
                        </aos:formpanel>
                    </aos:tab>
                </aos:tabpanel>
            </aos:window>

        </aos:window>
        <script type="text/javascript">
            window.onload=function(){
                Ext.getCmp("listTablename").setValue("<%=request.getAttribute("tablename")%>");
                Ext.getCmp("listTablename").setRawValue("<%=request.getAttribute("tabledesc")%>");

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
            //全部删除(指定条件的全部删除)
            function _g_data_del_term() {
                var query = Ext.getCmp("query").getValue();
                var msg = AOS.merge('确认要删除局域网数据吗？');
                AOS.confirm(msg, function (btn) {
                    if (btn === 'cancel') {
                        AOS.tip('删除操作被取消。');
                        return;
                    }
                    AOS.ajax({
                        url: 'deleteAllData_jyw.jhtml',
                        params: {
                            query: query,
                            tablename: tablename.getValue()
                        },
                        ok: function (data) {
                            AOS.tip(data.appmsg);
                            _g_param_store.reload();
                        }
                    });
                });
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
                var recordid = _recordid.getValue();
                if (!Ext.isEmpty(url)) {
                    window.location.href="loadData.jhtml?id_="+record.raw.id+"&name_="+record.raw.name_+"&recordid="+recordid;
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
                            var table="<%=request.getAttribute("tablename")%>";
                            //parent.fnaddtab(row.data.id, '电子文件',
                            //					'archive/data/openFile.jhtml?id='+row.data.pid+'&tid='+row.data.tid+'&type='+row.data.type+'&tablename='+tablename.getValue());
                            /*parent.fnaddtab(row.data.id, '电子文件',
                                                'archive/data/openPdfFile.jhtml?id='+row.data.pid+'&tid='+row.data.tid+'&type='+row.data.type+'&tablename='+tablename.getValue());*/
                            parent.parent.fnaddtab(row.data.id, '电子文件',
                                'archive/data/openPdfFile.jhtml?id='+row.data.pid+'&tid='+row.data.tid+'&type='+row.data.type+'&tablename='+table+'&index='+rowIndex);
                            /*parent.fnaddtab(row.data.id, '电子文件',
                                                 'archive/data1/initZpData.jhtml?id='+row.data.pid+'&tid='+row.data.tid+'&type='+row.data.type+'&tablename='+tablename.getValue());*/
                        }
                    },
                    //单机ocr选框
                    ocrPath:function(){
                        var value= Ext.getCmp("ocr").getValue();
                        var type="checkbox";
                        var name="ocr";
                        var tablename="<%=request.getAttribute("tablename")%>";;
                        //走后台存储当前用户选择的定义以便下次调用
                        remember_load(tablename,type,value,name);
                    },
                    //单机mark选框
                    markPath:function(){
                        var value= Ext.getCmp("mark").getValue();
                        var type="checkbox";
                        var name="mark";
                        var tablename="<%=request.getAttribute("tablename")%>";;
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
                        var table="<%=request.getAttribute("tablename")%>";
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
                        var table="<%=request.getAttribute("tablename")%>";
                        AOS.ajax({
                            params: {id_:id,
                                tablename:table,
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
                        var table="<%=request.getAttribute("tablename")%>";
                        AOS.ajax({
                            params: {aos_rows_: selection,
                                tm:record.data.tm,
                                tablename: table
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
                        tablename: "<%=request.getAttribute("tablename")%>"
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
            }
            function w_data_path_onshow() {
                var tablenameId="<%=request.getAttribute("tablename")%>";
                var record = AOS.selectone(Ext.getCmp('_g_param'));
                var me=Ext.getCmp("uploadpanel");
                me.store.removeAll();
                AOS.ajax({
                    params: {tid: record.data.tablename_id,tablename:tablenameId},
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
            //删除信息
            function _g_data_del() {
                var selection = AOS.selection(_g_param, 'id_');
                var msg = AOS.merge('确认要删除用户数据吗？', AOS.rows(_g_param));
                AOS.confirm(msg, function (btn) {
                    if (btn === 'cancel') {
                        AOS.tip('删除操作被取消。');
                        return;
                    }
                    AOS.ajax({
                        url: 'deleteDetails_hlw.jhtml',
                        params: {
                            tablename:Ext.getCmp("tablename").getValue(),
                            query: Ext.getCmp("query").getValue()
                        },
                        ok: function (data) {
                            if(data.appcode===1){
                                AOS.tip("删除成功!");
                                _g_param_store.reload();
                            }else if(data.appcode===-1){
                                AOS.tip("删除失败!");
                            }
                        }
                    });
                });
            }
            function fn_onselect(e){
                var user=Ext.getCmp("user").getValue();
                window.location.href="init.jhtml?lx=hlw&user="+user+"&tablename="+e.value+"&tabledesc="+e.rawValue+"&recordid="+_recordid.getValue();
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
                _g_param_store.getProxy().extraParams = params;
                _g_param_store.load();
                AOS.ajax({
                    params:params,
                    url: 'saveQueryData_hlw.jhtml',
                    ok: function (data) {
                        //此时在隐藏域中存入查询条件
                        Ext.getCmp("query").setValue(Ext.getCmp("query").getValue()+" "+data.appmsg);
                    }
                });
                _w_query_q.hide();
                AOS.reset(_f_query2);
            }
            function _g_add_data(){
                //弹出选择档案窗口
                _w_select_edit_archive.show();
                Ext.getCmp("_g_select_edit_archive").getStore().removeAll();
            }
            //查询窗口展开
            function _w_select_edit_query_show() {
                //判断是不是
                Ext.getCmp("query").setValue("");
                _w_query_edit_select_q.show();
            }
            //选择数据表后展开数据
            function by_select_sjkmc(){
                //根据选择的名称
                var sjkmc_value = Ext.getCmp("sjkmc").value;
                var params = {
                    tablename : sjkmc_value,
                    id_ : Ext.getCmp("aos_module_id_").getValue()
                };
                //这个Store的命名规则为：表格ID+"_store"。
                _g_select_edit_archive_store.getProxy().extraParams = params;
                _g_select_edit_archive_store.load();
                Ext.getCmp("ids").setValue("");
                Ext.getCmp("dhs").setValue("");
                Ext.getCmp("query").setValue("");
            }
            //获取字段数据列表
            function combobox_tableFileldlist(){
                var params = {
                    tablename : Ext.getCmp("listTablename").value
                };
                //这个Store的命名规则为：表格ID+"_store"。ok_save
                filednames1_store.getProxy().extraParams = params;
                filednames1_store.load();
                filednames2_store.getProxy().extraParams = params;
                filednames2_store.load();
                filednames3_store.getProxy().extraParams = params;
                filednames3_store.load();
                filednames4_store.getProxy().extraParams = params;
                filednames4_store.load();
                filednames5_store.getProxy().extraParams = params;
                filednames5_store.load();
            }
            //数据选择
            function load_archive(){
                var params = {
                    tablename : Ext.getCmp("tablename").getValue()
                };
                _g_select_edit_archive_store.getProxy().extraParams = params;
                _g_select_edit_archive_store.load();
            }
            //用于涉及到全部选择，排除分页的影响

            function ok_save_archive_edit(){
                var tablename=Ext.getCmp("tablename").getValue();
                var count=AOS.rows(_g_select_edit_archive);
                var ids=Ext.getCmp('ids').getValue();
                var params = {
                    ids:ids,
                    tablename:tablename
                };
                AOS.ajax({
                    url: 'updatejyw_data.jhtml',
                    params:params,
                    ok: function (data) {
                        if(data.appcode===1){
                            AOS.tip("添加成功!");
                            _g_param_store.load();
                            _w_select_edit_archive.hide();
                            //_g_receive_query();
                        }else if(data.appcode===-1){
                            AOS.tip("添加失败!");
                        }
                    }
                });
            }
            //保存当前选中的档案信息
            function ok_save_All_archive(){
                var model = _g_select_edit_archive.getSelectionModel();
                model.selectAll();//选择所有行
                var ids="";
                var dhs="";
                //走后台进行当前条件的所有条目的id集合
                AOS.ajax({
                    url: 'getQueryIds.jhtml',
                    params:{'tablename':Ext.getCmp("tablename").getValue(),
                        'query':Ext.getCmp("query").getValue()},
                    ok: function(data){
                        var ids="";
                        var dhs="";
                        for(k in data){
                            if(k==0){
                                ids=data[k].id_;
                            }else{
                                ids=ids+","+data[k].id_;
                            }
                        }
                        Ext.getCmp('ids').setValue(ids);
                        _w_select_archive.hide();
                    }
                });
            }
            function _f_select_edit_data_query(){
                var params = AOS.getValue('_f_select_edit_query');
                var form = Ext.getCmp('_f_select_edit_query');
                var tablename = Ext.getCmp("tablename").getValue();
                for(var i=1;i<=5;i++){
                    var str = form.down("[name='filednames"+i+"']");
                    var filedname =str.getValue();
                    if(filedname==null){
                        params["filednames"+i]=str.regexText;
                    }
                }
                params["query_old"]=Ext.getCmp("query").getValue();
                params["tablename"]=tablename;
                _g_select_edit_archive_store.getProxy().extraParams = params;
                _g_select_edit_archive_store.load();
                AOS.ajax({
                    params:params,
                    url: 'saveQueryData_hlw.jhtml',
                    ok: function (data) {
                        //此时在隐藏域中存入查询条件
                        Ext.getCmp("query").setValue(Ext.getCmp("query").getValue()+" "+data.appmsg);
                    }
                });
                _w_query_edit_select_q.hide();
                AOS.reset(_f_select_edit_query);
            }
        </script>
    </aos:onready>
</aos:html>
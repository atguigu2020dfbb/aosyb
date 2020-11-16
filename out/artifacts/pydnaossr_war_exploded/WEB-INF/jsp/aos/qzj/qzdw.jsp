<%@page contentType="text/html; charset=utf-8" %>
<%@taglib prefix="aos" uri="/WEB-INF/tld/aos.tld" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<aos:html>
    <aos:head title="全宗单位管理">
        <aos:include lib="ext,swfupload"/>
        <aos:include css="${cxt}/static/weblib/bootstrap/css/bootstrap.min.css"/>
        <aos:include css="${cxt}/static/css/fileinput.min.css"/>
        <aos:include js="${cxt}/static/js/jquery-3.2.1.min.js"/>
        <aos:include js="${cxt}/static/weblib/bootstrap/js/bootstrap.min.js"/>
        <aos:include js="${cxt}/static/js/fileinput.min.js"/>
        <aos:include js="${cxt}/static/js/zh.js"/>
        <aos:base href="archive/qzj"/>
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
        <div id="filediv">
            <input id="file" class="file-loading" name="file" type="file" multiple>
        </div>
        <div id="_div_yijiaoshu" class="x-hidden" align="center">
            <img id="_img_yijiaoshu" class="app_cursor_pointer"  width="680"
                 height="500">
        </div>
    </aos:body>
    <aos:onready>
        <aos:viewport layout="border">
            <aos:gridpanel id="_g_qzdw"   pageSize="1000" region="north" split="true" height="250" onitemclick="fn_data_qz" onrender="_g_qzdw_query" url="listQzdw.jhtml">
                <aos:docked>
                    <aos:hiddenfield  name="tablename" id="tablename" />
                    <aos:hiddenfield  name="user" id="user" value="${user}"/>
                    <aos:dockeditem xtype="tbtext" text="全宗信息"/>
                    <aos:dockeditem text="新增" icon="add.png" onclick="_w_qzdw_show"/>
                    <aos:dockeditem text="修改" icon="edit.png" onclick="_w_qzdw_u_show"/>
                    <aos:dockeditem text="删除" icon="del.png" onclick="_g_qzdw_del"/>
                    <aos:dockeditem text="查询" icon="query.png" onclick="_w_select_query_show" />
                    <aos:dockeditem text="更新" icon="icon5.png" onclick="_g_qzdw_update"/>
                    <aos:dockeditem text="导入" icon="folder8.png" onclick="_w_import_show"/>
                </aos:docked>
                <aos:selmodel type="checkbox" mode="multi" />
                <aos:column type="rowno" />
                <aos:column header="id_" dataIndex="id_" hidden="true"/>
                <aos:column header="全宗序号" dataIndex="qzxh"/>
                <aos:column header="全宗号" dataIndex="qzh"/>
                <aos:column header="全宗单位" dataIndex="qzdw"/>
                <aos:column header="类别" dataIndex="qzlb"/>
                <aos:column header="起止年度" dataIndex="qznd"/>
                <aos:column header="备注" dataIndex="bz"/>
            </aos:gridpanel>
            <aos:gridpanel id="_g_data" url="listAccounts.jhtml" region="center" features="true" split="true"
                              pageSize="${pagesize }" onitemclick="itemclick" rowclass="true"
                           autoScroll="true" >
                <aos:docked forceBoder="1 0 1 0">
                    <aos:hiddenfield id="rowmath" name="rowmath" value="0"/>
                    <aos:dockeditem xtype="tbtext" text="目录信息"/>
                    <aos:combobox name="listTablename" width="200" labelWidth="80"
                                   fieldLabel="目录号(年度)"  fields="['mlhenname']"
                                  id="mlhenname" editable="false" columnWidth="0.20"
                                  url="listMlh.jhtml" displayField="mlhenname"
                                  valueField="mlhenname"   onselect="fn_onselect_mlh"/>
                    <aos:combobox name="bgqxenname" width="200" labelWidth="60"
                                   fieldLabel="保管期限"
                                  id="bgqxenname" editable="false" columnWidth="0.20"
                                    onselect="fn_onselect_bgqx">
                        <aos:option value="永久" display="永久"></aos:option>
                        <aos:option value="长期" display="长期"></aos:option>
                        <aos:option value="定期" display="定期"></aos:option>
                        <aos:option value="短期" display="短期"></aos:option>
                        <aos:option value="30年" display="30年"></aos:option>
                    </aos:combobox>
                    <aos:combobox name="kfcdenname" width="200"
                                  fieldLabel="开放程度" labelWidth="60"
                                  id="kfcdenname" editable="false" columnWidth="0.20"
                                    onselect="fn_onselect_sfkf">
                        <aos:option value="开放" display="开放"></aos:option>
                        <aos:option value="控制" display="控制"></aos:option>
                        <aos:option value="不开放" display="不开放"></aos:option>
                    </aos:combobox>
                    <aos:dockeditem text="显示" icon="picture.png" id="_f_filename_message"
                                    onclick="_w_picture_show"/>
                    <aos:dockeditem text="详情" icon="text_col.png"
                                    onclick="_w_details_show" />
                    <aos:dockeditem xtype="tbfill"/>
                </aos:docked>
                <%--<aos:selmodel type="checkbox" mode="simple" />--%>
<%--                    <aos:column type="rowno" />--%>
                    <aos:column dataIndex="id_" header="流水号" hidden="true" locked="true"/>
                    <aos:column dataIndex="qzdw" header="全宗单位" width="100" />
                    <aos:column dataIndex="qzh" header="全宗号" width="80" />
                    <aos:column dataIndex="mlh" header="目录号" width="80" />
                    <aos:column dataIndex="ajh" header="案卷号" width="100" />
                    <aos:column dataIndex="sxh" header="顺序号" width="100" />
                    <aos:column dataIndex="tm" header="题名" width="150" />
                    <aos:column dataIndex="wjbh" header="文件编号" width="100" />
                    <aos:column dataIndex="cwrq" header="形成时间" width="80" />
                    <aos:column dataIndex="zrz" header="责任者" width="100" />
                    <aos:column dataIndex="ys" header="页数" width="80" />
                    <aos:column dataIndex="sfkf" header="是否开放" width="80" />
                <aos:column dataIndex="bgqx" header="保管期限" width="100" />
                <aos:column dataIndex="cz_bh" header="存址编号" width="100" />
                <aos:column dataIndex="_piccount" header="实扫页数" width="100" />
                <aos:column dataIndex="mj" header="密级" width="100" />
                <aos:column dataIndex="_path" header="附件" width="100" />
<%--                <aos:column header="" flex="1"/>--%>
<%--                <aos:column dataIndex="id_" header="流水号" hidden="true" locked="true"/>--%>
                <aos:column header="" flex="1"/>
            </aos:gridpanel>
        </aos:viewport>
        <aos:window id="_w_qzdw" title="新增" >
            <aos:formpanel id="_f_qzdw" layout="column" width="800">
                <aos:numberfield name="qzxh" fieldLabel="全宗序号" columnWidth="0.49" value="1" minWidth="0" maxValue="99999999" />
                <aos:textfield fieldLabel="全宗号" name="qzh" columnWidth="0.49"/>
                <aos:textfield fieldLabel="全宗单位" name="qzdw" columnWidth="0.49"/>
                <aos:combobox name="qzlb" fieldLabel="类别" dicField="qzlb" emptyText="请选择..." columnWidth="0.49" />
                <aos:textfield fieldLabel="起止年度" name="qznd" columnWidth="0.49"/>
                <aos:textfield fieldLabel="备注" name="bz" columnWidth="0.49"/>
                <aos:docked dock="bottom">
                    <aos:dockeditem xtype="tbfill"/>
                    <aos:dockeditem text="保存" icon="save.png" onclick="_f_qzdw_save"/>
                    <aos:dockeditem text="关闭" icon="close.png" onclick="#_w_qzdw.hide()"/>
                </aos:docked>
            </aos:formpanel>
        </aos:window>
        <aos:window id="_w_qzdw_u" title="修改"  >
            <aos:formpanel id="_f_qzdw_u" layout="column" width="800" onrender="_update_task">
                <aos:hiddenfield name="id_" fieldLabel="流水号" />
                <aos:numberfield name="qzxh" fieldLabel="全宗序号" columnWidth="0.49" value="1" minWidth="0" maxValue="99999999" />
                <aos:textfield fieldLabel="全宗号" name="qzh" columnWidth="0.49"/>
                <aos:textfield fieldLabel="全宗单位" name="qzdw" columnWidth="0.49"/>
                <aos:combobox name="qzlb" id="qzlb" fieldLabel="类别" margin="0 0 0 60"
                               labelWidth="40"
                              columnWidth="0.49" fields="['code_','desc_']"
                               displayField="desc_"
                              valueField="code_"
                              url="querytables.jhtml?qzlb=qzlb">
                </aos:combobox>
                <aos:textfield fieldLabel="起止年度" name="qznd" columnWidth="0.49"/>
                <aos:textfield fieldLabel="备注" name="bz" columnWidth="0.49"/>
                <aos:docked dock="bottom">
                    <aos:dockeditem xtype="tbfill"/>
                    <aos:dockeditem text="上一条" icon="more/go-previous.png" onclick="_f_previous_data"/><%----%>
                    <aos:dockeditem text="下一条" icon="more/go-next.png" onclick="_f_next_data"/><%----%>
                    <aos:dockeditem text="保存" icon="save.png" onclick="_f_qzdw_u_save"/>
                    <aos:dockeditem text="关闭" icon="close.png" onclick="#_w_qzdw_u.hide()"/>
                </aos:docked>
            </aos:formpanel>
        </aos:window>

        <aos:window id="_w_qzdw_q" title="查询">
            <aos:formpanel id="_f_qzdw_q" layout="column" width="800">
                <aos:hiddenfield name="id_" fieldLabel="流水号" />
                <aos:textfield fieldLabel="全宗号" name="qzh" columnWidth="0.99"/>
                <aos:textfield fieldLabel="全宗单位" name="qzdw" columnWidth="0.99"/>
                <aos:textfield fieldLabel="类别" name="qzlb" columnWidth="0.99"/>
                <aos:textfield fieldLabel="备注" name="bz" columnWidth="0.99"/>
                <aos:docked dock="bottom">
                    <aos:dockeditem xtype="tbfill"/>
                    <aos:dockeditem text="确定" icon="save.png" onclick="_f_qzdw_q_save"/>
                    <aos:dockeditem text="关闭" icon="close.png" onclick="#_w_qzdw_q.hide()"/>
                </aos:docked>
            </aos:formpanel>
        </aos:window>
        <aos:window id="_w_data_i" title="详情" draggable="false" closable="false" closeAction="hide"
                    autoScroll="true" x="50" width="860" height="500" >
            <aos:formpanel id="_f_data_i" labelWidth="100" height="800" width="750" margin="0 0 0 20">

            </aos:formpanel>
            <aos:docked dock="bottom" ui="footer">
                <aos:dockeditem xtype="tbfill"/>
                <aos:dockeditem onclick="#_w_data_i.hide();" text="关闭"
                                icon="close.png"/>
            </aos:docked>
        </aos:window>
        <aos:window id="_w_query_select" title="查询" width="720" autoScroll="true"
                    layout="fit" >
            <aos:tabpanel id="_tabpanel_edit_select" region="center" activeTab="0"
                          bodyBorder="0 0 0 0" tabBarHeight="30">
                <aos:tab title="列表式搜索" id="_tab_select_edit_org">
                    <aos:formpanel id="_f_select_query" layout="column" columnWidth="1">
                        <aos:hiddenfield name="columnnum"  value="7" />
                        <aos:combobox name="and1" value="on"
                                      labelWidth="10" columnWidth="0.2">
                            <aos:option value="on" display="并且" />
                            <aos:option value="up" display="或者" />
                        </aos:combobox>
                        <aos:combobox name="filedname1" id="filedname1" labelWidth="20" value="qzh" columnWidth="0.2">
                            <aos:option value="qzh" display="全宗号" />
                            <aos:option value="qzdw" display="全宗单位" />
                            <aos:option value="qzlb" display="类别" />
                            <aos:option value="bz" display="备注" />
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
                        <aos:combobox name="filedname2" id="filedname2"  labelWidth="20" columnWidth="0.2"  value="qzdw">
                            <aos:option value="qzh" display="全宗号" />
                            <aos:option value="qzdw" display="全宗单位" />
                            <aos:option value="qzlb" display="类别" />
                            <aos:option value="bz" display="备注" />
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
                        <aos:combobox name="filedname3" id="filedname3"  labelWidth="20" columnWidth="0.2"  value="qzlb">
                            <aos:option value="qzh" display="全宗号" />
                            <aos:option value="qzdw" display="全宗单位" />
                            <aos:option value="qzlb" display="类别" />
                            <aos:option value="bz" display="备注" />
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
                        <aos:combobox name="filedname4" id="filedname4"   labelWidth="20" columnWidth="0.2"  value="bz" >
                            <aos:option value="qzh" display="全宗号" />
                            <aos:option value="qzdw" display="全宗单位" />
                            <aos:option value="qzlb" display="类别" />
                            <aos:option value="bz" display="备注" />
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
                        <aos:docked dock="bottom" ui="footer">
                            <aos:dockeditem xtype="tbfill" />
                            <aos:dockeditem onclick="_f_select_data_query" text="确定" icon="ok.png" />
                            <aos:dockeditem onclick="#_w_query_select.hide();" text="关闭"
                                            icon="close.png" />
                        </aos:docked>
                    </aos:formpanel>
                </aos:tab>
            </aos:tabpanel>


            <script type="text/javascript">

                //生成录入界面
                function _w_data_input(formid) {
                    var _panel = Ext.getCmp(formid);
                    _panel.removeAll();
                    //_panel.reload();
                    AOS.ajax({
                        params: {tablename: Ext.getCmp("tablename").getValue()},
                        url: 'getInputColumn.jhtml',
                        ok: function (data) {
                            //var _panel = Ext.getCmp(formid);
                            var strdh = '';
                            for (var j in data) {
                                //档号设置
                                if (data[j].dh == '1') {
                                    var strfieldname = data[j].fieldname.substring(0, data[j].fieldname.length - 1);
                                    if (typeof (data[j].dh1) != 'undefined') {
                                        strdh = strfieldname + ',' + data[j].dh1;
                                        if (typeof (data[j].dh2) != 'undefined') {
                                            strdh = strdh + ',' + data[j].dh2;
                                            if (typeof (data[j].dh3) != 'undefined') {
                                                strdh = strdh + ',' + data[j].dh3;
                                                if (typeof (data[j].dh4) != 'undefined') {
                                                    strdh = strdh + ',' + data[j].dh4;
                                                    if (typeof (data[j].dh5) != 'undefined') {
                                                        strdh = strdh + ',' + data[j].dh5;
                                                        if (typeof (data[j].dh6) != 'undefined') {
                                                            strdh = strdh + ',' + data[j].dh6;
                                                            if (typeof (data[j].dh7) != 'undefined') {
                                                                strdh = strdh + ',' + data[j].dh7;
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }//判断dh
                            }
                            for (var i in data) {
                                var items;
                                if (data[i].fieldname.charAt(data[i].fieldname.length - 1) == 'L') {
                                    //设置标签必录入项
                                    if (data[i].ynnnull == '0') {
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
                                    } else {
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
                                } else {
                                    if (data[i].yndic == '1') {
                                        var ynnull;
                                        if (data[i].ynnnull == 0) {
                                            ynnull = false;
                                        } else {
                                            ynnull = true;
                                        }
                                        var strdicname = data[i].fieldname.substring(0, data[i].fieldname.length - 1);
                                        items = [{
                                            name: data[i].fieldname.substring(0, data[i].fieldname.length - 1),
                                            id: data[i].fieldname.substring(0, data[i].fieldname.length - 1),
                                            //fieldLabel: 'ieldLabel',
                                            xtype: "combo",
                                            mode: 'local',
                                            fieldLabel: data[i].displayname,
                                            //x:parseInt(data[i].cleft)-200,
                                            //y:parseInt(data[i].ctop)-50,
                                            maxWidth: parseInt(data[i].cwidth),
                                            width: parseInt(data[i].cwidth),
                                            //height:parseInt(data[i].cheight),
                                            margin: '2,0,0,0',
                                            allowBlank: ynnull,
                                            listeners: {
                                                select: function (e) {
                                                    if (strdh.indexOf(this.name) > -1) {
                                                        var strarray = strdh.split(',');
                                                        var strtemp = '';
                                                        for (var i = 1; i < strarray.length; i++) {
                                                            if (i == 1) {
                                                                //strtemp =Ext.getCmp(strarray[i]).getValue();
                                                                strtemp = _f_data_i.form.findField(strarray[i]).getValue();
                                                                continue;
                                                            }
                                                            //strtemp = strtemp+'-'+Ext.getCmp(strarray[i]).getValue();
                                                            strtemp = strtemp + '-' + _f_data_i.form.findField(strarray[i]).getValue();
                                                        }

                                                        //Ext.getCmp(strarray[0]).setValue(strtemp);
                                                        _f_data_i.form.findField(strarray[0]).setValue(strtemp)
                                                    }
                                                }
                                            },
                                            //labelWidth:80,
                                            store: new Ext.data.SimpleStore({
                                                fields: ["code_", "desc_"],
                                                proxy: {
                                                    type: "ajax",
                                                    //params:{"tablename":"3333"},
                                                    url: "load_dic_index.jhtml?key_name_=" + data[i].dic,
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
                                            emptyText: '请选择...',
                                            displayField: 'desc_',
                                            valueField: 'desc_',
                                            //hiddenName: 'fieldenname',
                                        }]
                                        _panel.add(items);
                                    } else {

                                        if (data[i].fieldname == "tmD") {
                                            items = itemsGroup_textareafield(data[i], strdh);
                                            _panel.add(items);
                                        } else {
                                            //此时是时长
                                            if ("时长" == data[i].displayname) {
                                                items = itemsGroup(data[i], strdh);
                                                _panel.add(items);
                                                items = itemsGroup_label(data[i], "分钟", "35");
                                                _panel.add(items);
                                            } else if ("容量大小" == data[i].displayname) {
                                                items = itemsGroup(data[i], strdh);
                                                _panel.add(items);
                                                items = itemsGroup_label(data[i], "GB", "25");
                                                _panel.add(items);
                                            } else if ("视频大小" == data[i].displayname) {
                                                items = itemsGroup(data[i], strdh);
                                                _panel.add(items);
                                                items = itemsGroup_label(data[i], "GB", "25");
                                                _panel.add(items);
                                            } else if ("音频大小" == data[i].displayname) {
                                                items = itemsGroup(data[i], strdh);
                                                _panel.add(items);
                                                items = itemsGroup_label(data[i], "GB", "25");
                                                _panel.add(items);
                                            } else {
                                                items = itemsGroup(data[i], strdh);
                                                _panel.add(items);
                                            }
                                        }
                                    }
                                }

                            }
                        }
                    });
                }

                function itemsGroup_textareafield(data, strdh) {
                    var strx = parseInt(data.cleft) - 200;
                    var stry = parseInt(data.ctop) - 50;
                    var strwidth = parseInt(data.cwidth);
                    var strheight = parseInt(data.cheight);
                    var displayname = data.displayname;

                    var fieldname = data.fieldname.substring(0, data.fieldname.length - 1);
                    var ynnull;
                    var strdisplayname;
                    if (data.ynnnull == 0) {
                        ynnull = false;
                        strdisplayname = '<lable style="color: red;">' + displayname + '</lable>';
                    } else {
                        ynnull = true;
                        strdisplayname = displayname;
                    }
                    //var ynnull=data.ynnnull=='0';
                    var str = [{
                        xtype: 'textareafield',
                        id: fieldname,
                        name: fieldname,
                        width: strwidth,
                        margin: '2,0,0,0',
                        //maxWidth :strwidth,
                        height: strheight,
                        //x:strx,
                        //y:stry,
                        //columnWidth: 0.5,

                        fieldLabel: strdisplayname,
                        //maxLength:data.edtmax,
                        allowBlank: ynnull,
                        listeners: {
                            focus: function (e) {
                            },
                            blur: function (e) {
                                if (data.ynpw == '1') {
                                    var val = e.getValue();
                                    var len = val.length;
                                    while (len < data.edtmax) {
                                        val = "0" + val;
                                        len++;
                                    }
                                    e.setValue(val);
                                }
                                if (strdh.indexOf(this.name) > -1) {
                                    var strarray = strdh.split(',');
                                    var strtemp = '';
                                    //alert(strdh);
                                    for (var i = 1; i < strarray.length; i++) {
                                        //alert(strarray[i]);
                                        if (i == 1) {
                                            //strtemp =Ext.getCmp(strarray[i]).getValue();
                                            strtemp = _f_data_i.form.findField(strarray[i]).getValue();
                                            continue;
                                        }
                                        //strtemp=strtemp+'-'+Ext.getCmp(strarray[i]).getValue();
                                        strtemp = strtemp + '-' + _f_data_i.form.findField(strarray[i]).getValue();

                                    }
                                    //alert(strtemp);
                                    //Ext.getCmp(strarray[0]).setValue(strtemp);
                                    _f_data_i.form.findField(strarray[0]).setValue(strtemp);

                                }
                            }
                            //离开鼠标事件结尾
                        }
                    }];
                    //alert(str);
                    //var item = eval('(' + str + ')');
                    return str;
                }

                function itemsGroup(data, strdh) {
                    var strx = parseInt(data.cleft) - 200;
                    var stry = parseInt(data.ctop) - 50;
                    var strwidth = parseInt(data.cwidth);
                    var strheight = parseInt(data.cheight);
                    var displayname = data.displayname;

                    var fieldname = data.fieldname.substring(0, data.fieldname.length - 1);
                    var ynnull;
                    var strdisplayname;
                    if (data.ynnnull == 0) {
                        ynnull = false;
                        strdisplayname = '<lable style="color: red;">' + displayname + '</lable>';
                    } else {
                        ynnull = true;
                        strdisplayname = displayname;
                    }
                    //var ynnull=data.ynnnull=='0';
                    var str = [{
                        xtype: 'textfield',
                        id: fieldname,
                        name: fieldname,
                        width: strwidth,
                        margin: '2,0,0,0',
                        //maxWidth :strwidth,
                        height: strheight,
                        //x:strx,
                        //y:stry,
                        //columnWidth: 0.5,

                        fieldLabel: strdisplayname,
                        //maxLength:data.edtmax,
                        allowBlank: ynnull,
                        listeners: {
                            focus: function (e) {
                            },
                            blur: function (e) {
                                if (data.ynpw == '1') {
                                    var val = e.getValue();
                                    var len = val.length;
                                    while (len < data.edtmax) {
                                        val = "0" + val;
                                        len++;
                                    }
                                    e.setValue(val);
                                }
                                if (strdh.indexOf(this.name) > -1) {
                                    var strarray = strdh.split(',');
                                    var strtemp = '';
                                    //alert(strdh);
                                    for (var i = 1; i < strarray.length; i++) {
                                        //alert(strarray[i]);
                                        if (i == 1) {
                                            //strtemp =Ext.getCmp(strarray[i]).getValue();
                                            strtemp = _f_data_i.form.findField(strarray[i]).getValue();
                                            continue;
                                        }
                                        //strtemp=strtemp+'-'+Ext.getCmp(strarray[i]).getValue();
                                        strtemp = strtemp + '-' + _f_data_i.form.findField(strarray[i]).getValue();

                                    }
                                    //alert(strtemp);
                                    //Ext.getCmp(strarray[0]).setValue(strtemp);
                                    _f_data_i.form.findField(strarray[0]).setValue(strtemp);

                                }
                            }
                            //离开鼠标事件结尾
                        }
                    }];
                    //alert(str);
                    //var item = eval('(' + str + ')');
                    return str;
                }

                function itemsGroup_label(data, strdh, width) {
                    var _id_1c8417852637_cfg = {
                        name: '_id_1c8417852637',
                        xtype: 'displayfield',
                        width: width,
                        height: 25,
                        fieldCls: 'app-form-fieldcls',
                        value: '<div >' + strdh + '</div>',
                        margin: "2,0,0,0",
                        app: 169
                    };
                    var str = Ext.create('Ext.form.field.Display', _id_1c8417852637_cfg);
                    //var
                    //alert(str);
                    //var item = eval('(' + str + ')');
                    return str;
                }
                function _w_details_show(a) {
                    var record_qzdw=AOS.selectone(_g_qzdw);
                    var record_data=AOS.selectone(_g_data);
                    if(AOS.empty(record_qzdw)||AOS.empty(record_data)){
                        AOS.tip("请选择全宗单位和条目数据!");
                        return;
                    }else{
                        var tablename = Ext.getCmp("tablename").getValue();
                        AOS.ajax({
                            params: {
                                id_: record_data.data.id_,
                                tablename: tablename
                            },
                            url: 'getData.jhtml',
                            ok: function (data) {
                                Ext.getCmp("_f_data_i").form.setValues(data);
                                Ext.getCmp("_w_data_i").show();
                            }
                        });
                    }

                }
                //显示上传面板
                function _w_picture_show() {
                    var user = Ext.getCmp("user").getValue();
                    var record = AOS.selectone(_g_data);
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
                                        'archive/data/openPdfFile.jhtml?id=' + row.data.pid + '&tid=' + row.data.tid + '&type=' + row.data.type + '&tablename=' + tablename.getValue());
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
                                    tablename: tablename.getValue(),
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
                                    tablename: tablename.getValue()
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
                            AOS.file('downloadPath.jhtml?id_=' + id + '&tablename=' + tablename.getValue());
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
                                AOS.file('downloadPath.jhtml?id_=' + id + '&tablename=' + tablename.getValue());
                            }
                        },
                        upload_complete_handler: function (file) {
                            if (user != "root") {
                                AOS.err("没有权限!");
                                return;
                            }
                            var me = Ext.getCmp("uploadpanel");

                            AOS.ajax({
                                params: {tid: record.data.id_, tablename: tablename.getValue()},
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
                            tablename: tablename.getValue()
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

                    var record = AOS.selectone(Ext.getCmp('_g_data'));
                    var me = Ext.getCmp("uploadpanel");
                    me.store.removeAll();
                    AOS.ajax({
                        params: {tid: record.data.id_, tablename: tablename.getValue()},
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
                            tablename: Ext.getCmp("tablename").getValue(),
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
                function fn_data_qz(){
                    //鼠标单击单位名称，下方出现对应的数据列表
                    var record=AOS.selectone(_g_qzdw);
                    AOS.ajax({
                        params: {
                            tabledesc: record.data.qzlb
                        }, // 提交参数,
                    url: 'getTablename.jhtml',
                    ok: function (data) {
                            Ext.getCmp("tablename").setValue(data.tablename);
                        _w_data_input('_f_data_i');
                        }
                    });
                    var params = {
                        qzh : record.data.qzh,
                        tabledesc : record.data.qzlb
                    };
                    //这个Store的命名规则为：表格ID+"_store"。
                    _g_data_store.getProxy().extraParams = params;
                    _g_data_store.load();
                    //此时结合门类和在更新combobox
                    mlhenname_store.getProxy().extraParams = params;
                    mlhenname_store.load();


                }
                function fn_onselect_mlh(){
                    //鼠标单击单位名称，下方出现对应的数据列表
                    var record=AOS.selectone(_g_qzdw);
                    if(!AOS.empty(record)){
                        var params = {
                            qzh : record.data.qzh,
                            mlh:Ext.getCmp("mlhenname").getValue(),
                            tabledesc : record.data.qzlb
                        };
                        //这个Store的命名规则为：表格ID+"_store"。
                        _g_data_store.getProxy().extraParams = params;
                        _g_data_store.load();
                    }
                }
                function fn_onselect_bgqx(){
                    //鼠标单击单位名称，下方出现对应的数据列表
                    var record=AOS.selectone(_g_qzdw);
                    if(!AOS.empty(record)){
                        var params = {
                            qzh : record.data.qzh,
                            bgqx:Ext.getCmp("bgqxenname" +
                                "").getValue(),
                            tabledesc : record.data.qzlb
                        };
                        //这个Store的命名规则为：表格ID+"_store"。
                        _g_data_store.getProxy().extraParams = params;
                        _g_data_store.load();
                    }
                }
                function fn_onselect_sfkf(){
                    //鼠标单击单位名称，下方出现对应的数据列表
                    var record=AOS.selectone(_g_qzdw);
                    if(!AOS.empty(record)){
                        var params = {
                            qzh : record.data.qzh,
                            sfkf:Ext.getCmp("kfcdenname").getValue(),
                            tabledesc : record.data.qzlb
                        };
                        //这个Store的命名规则为：表格ID+"_store"。
                        _g_data_store.getProxy().extraParams = params;
                        _g_data_store.load();
                    }
                }
                function changeRowClass(record, rowIndex, rowParams, store) {

                    //得到当前行的指定的列的值
                    if (record.get("_path") >= 1) {
                        return 'grid-one-column';
                    } else {
                        return 'grid-zero-column';
                    }

                }
                function itemclick(grid, rowIndex, columnIndex, e) {
                    //点击之前获取当前行的初始颜色，
                    //var beginColor=grid.all.elements[e].cells[i].style.backgroundColor;
                    var k = Ext.getCmp("rowmath").getValue();
                    for (var j = 0; j < grid.all.elements[Ext.getCmp("rowmath").getValue()].cells.length; j++) {
                        grid.all.elements[Ext.getCmp("rowmath").getValue()].cells[j].style = "text-decoration:none";
                    }
                    var tt = grid.all.elements[e].cells.length;
                    for (var i = 0; i < tt; i++) {
                        grid.all.elements[e].cells[i].style.backgroundColor = "#68838B";
                        //此时把当前单元格存到缓冲中
                    }
                    var g = grid.getSelectionModel();
                    //让k行取消选择

                    //原先行取消选中
                    grid.getSelectionModel().deselect(k);
                    //此时让光标选中上一行
                    grid.getSelectionModel().select(e, true);

                    Ext.getCmp("rowmath").setValue(e);
                }

                //查询窗口展开
                function _w_select_query_show() {
                    //判断是不是
                    _w_qzdw_q.show();
                }
                //生成XLS报表
                function _w_charts_export() {
                    AOS.ajax({
                        url : 'fillReport_ry.jhtml',
                        ok : function(data) {
                            AOS.file('${cxt}/report/xls2.jhtml');
                        }
                    });
                }
                //导入excel窗口
                function _w_import_show(){
                    window.parent.fnaddtab('ry','人员出入库导入','/depot/qzdw/initImport.jhtml?tablename=depot_ry');
                }
                function _g_qzdw_query(){
                    //这个Store的命名规则为：表格ID+"_store"。
                    _g_qzdw_store.load();
                }

                function _w_qzdw_show(){
                    _w_qzdw.show();
                }
                function _f_add_person(){
                    var lx= Ext.getCmp("lx").getValue();
                    //根据tree名称得到对应的部门名称
                    AOS.ajax({
                        params:{
                            name_:"保管状况流水号",
                            lx:lx,
                            lsh:_f_qzdw.form.findField("lsh").getValue()
                        },
                        url:'getDepotRyIndex_nd.jhtml',
                        ok:function(data){
                            //设计一个随机数编号
                            //年
                            var time = (new Date).getTime();
                            var yesday = _f_qzdw.form.findField("lsh").getValue().substring(3,7);
                            _f_qzdw.form.findField("lsh").setValue(lx+yesday+data.index);
                        }
                    });
                }
                /*function _new_task(){
                    AOS.ajax({
                        params:{name_:"设备流水号",lx:lx.getValue()},
                        url:'getPersonIndex.jhtml',
                        ok:function(data){
                            //设计一个随机数编号
                            //年
                            var time = (new Date).getTime();
                            var yesday = new Date(time);
                            _f_qzdw.form.findField("lsh").setValue(lx.getValue()+yesday.getFullYear()+data.index);
                            _f_qzdw.form.findField("xm").setValue(Ext.getCmp("xm").getValue());
                            yesday = yesday.getFullYear()  +"-"+(yesday.getMonth()> 9 ? (yesday.getMonth() + 1) : "0" + (yesday.getMonth() + 1))+
                                "-"+(yesday.getDate()> 9 ? (yesday.getDate()) : "0" + yesday.getDate());
                            _f_qzdw.form.findField("f_logdatetime").setValue(yesday);
                            _f_qzdw.form.findField("glnd").setValue(new Date(time).getFullYear());

                        }
                    });
                }*/
                function _update_task(){

                }
                function _f_qzdw_save(){
                    var qzlb_cn=Ext.getCmp('_f_qzdw').down("[name='qzlb']").getRawValue();
                    AOS.ajax({
                        forms:_f_qzdw,
                        params:{'qzlb_cn':qzlb_cn},
                        url:'saveQzdw.jhtml',
                        ok:function(data){
                            if(data.appcode === 1){
                                _w_qzdw.hide();
                                _g_qzdw_store.reload();
                                AOS.tip(data.appmsg);
                            }else {
                                AOS.err(data.appmsg);
                            }
                        }
                    });
                }
                //上一页
                function _f_previous_data() {
                    var count = Ext.getCmp("_g_qzdw").getStore().getCount();
                    var me = Ext.getCmp("_g_qzdw").getSelectionModel().getSelection();
                    //var record = AOS.selectone(_g_data);
                    //得到执行行的坐标
                    var rowIndex = Ext.getCmp("_g_qzdw").getStore().indexOf(me[0]);
                    if (rowIndex == 0) {
                        AOS.err("当前第一条!");
                        return;
                    }
                    var s = Ext.getCmp("_g_qzdw").getStore().getAt(rowIndex - 1);
                    //原先行取消选中
                    Ext.getCmp("_g_qzdw").getSelectionModel().deselect(rowIndex);
                    //此时让光标选中上一行
                    Ext.getCmp("_g_qzdw").getSelectionModel().select(rowIndex - 1, true);
                    //组件被显示后触发。
                    Ext.getCmp("_f_qzdw_u").form.setValues(s.data);
                }

                //下一页
                function _f_next_data() {
                    var count = Ext.getCmp("_g_qzdw").getStore().getCount();
                    var me = Ext.getCmp("_g_qzdw").getSelectionModel().getSelection();
                    //var record = AOS.selectone(_g_data);
                    //得到执行行的坐标
                    var rowIndex = Ext.getCmp("_g_qzdw").getStore().indexOf(me[0]);
                    if (rowIndex == count - 1) {
                        AOS.err("当前最后一条!");
                        return;
                    }
                    var s = Ext.getCmp("_g_qzdw").getStore().getAt(rowIndex + 1);
                    //原先行取消选中
                    Ext.getCmp("_g_qzdw").getSelectionModel().deselect(rowIndex);
                    //此时让光标选中下一行
                    Ext.getCmp("_g_qzdw").getSelectionModel().select(rowIndex + 1, true);
                    //组件被显示后触发。
                    Ext.getCmp("_f_qzdw_u").form.setValues(s.data);
                }
                function _w_qzdw_u_show(){
                    var record = AOS.selectone(_g_qzdw);
                    if(record){
                        _w_qzdw_u.show();
                        _f_qzdw_u.loadRecord(record);
                        Ext.getCmp('_f_qzdw_u').down("[name='qzlb']").setValue(AOS.selectone(_g_qzdw).data.qzlb);
                        Ext.getCmp('_f_qzdw_u').down("[name='qzlb']").setRawValue(AOS.selectone(_g_qzdw).data.qzlb);
                    }
                }
                function _f_qzdw_u_save(){

                    var qzlb_cn=_f_qzdw_u.form.findField("qzlb").getRawValue();
                    var qzlb_en=_f_qzdw_u.form.findField("qzlb").getValue();
                    AOS.ajax({
                        forms:_f_qzdw_u,
                        params:{'qzlb_cn':qzlb_cn},
                        url:'updateQzdw.jhtml',
                        ok:function(data){
                            if(data.appcode===1){
                                _w_qzdw_u.hide();
                                _g_qzdw_store.reload();
                                AOS.tip(data.appmsg);
                            }else {
                                AOS.err(AOS.appmsg);
                            }
                        }
                    })
                }
                function _g_qzdw_del(){
                    var selection = AOS.selection(_g_qzdw,'id_');
                    if(AOS.empty(selection)){
                        AOS.tip("删除前请选中数据。。。");
                        return;
                    }
                    var rows = AOS.rows(_g_qzdw);
                    var msg = AOS.merge('确定要删除选中的[{0}]条数据吗？',rows);
                    AOS.confirm(msg,function(btn){
                        if(btn==='cancel'){
                            AOS.tip('删除被操作取消');
                            return;
                        }
                        AOS.ajax({
                            url:'deleteQzdw.jhtml',
                            params:{
                                aos_rows_: selection
                            },
                            ok:function(data){
                                AOS.tip(data.appmsg);
                                _g_qzdw_store.reload();
                            }
                        })
                    })
                }
                function _f_select_data_query(){

                    var params = AOS.getValue('_f_select_query');

                    var form = Ext.getCmp('_f_select_query');
                    for(var i=1;i<=4;i++){
                        var str = form.down("[name='filedname"+i+"']");
                        var filedname =str.getValue();
                        if(filedname==null){
                            params["filedname"+i]=str.regexText;
                        }
                    }
                    _g_qzdw_store.getProxy().extraParams = params;
                    _g_qzdw_store.load();
                    _w_query_select.hide();
                    AOS.reset(_f_select_query);
                }
                function _f_qzdw_q_save(){
                    var params=AOS.getValue('_f_qzdw_q');
                    _g_qzdw_store.getProxy().extraParams = params;
                    _g_qzdw_store.load();
                    _w_qzdw_q.hide();
                }
                function _g_qzdw_update(){
                    var selection = AOS.selection(_g_qzdw,'id_');
                    if(AOS.empty(selection)){
                        AOS.tip("更新前请选中数据。。。");
                        return;
                    }
                    var rows = AOS.rows(_g_qzdw);
                    var msg = AOS.merge('确定要更新选中的[{0}]条全宗数据吗？',rows);
                    AOS.confirm(msg,function(btn){
                        if(btn==='cancel'){
                            AOS.tip('更新被操作取消');
                            return;
                        }
                        AOS.ajax({
                            url:'updateFiled.jhtml',
                            params:{
                                aos_rows_: selection
                            },
                            ok:function(data){
                                AOS.tip(data.appmsg);
                            }
                        })
                    })
                }
                //导入窗口
                function _w_import_show() {
                    //我得加个重置
                    Ext.getCmp("_fileupload_add").show();
                    $(document).ready(function() {
                        $("#file").fileinput({
                            language: 'zh', //设置语言
                            uploadUrl: '${cxt}/archive/upload/uploadImport_excel.jhtml', //上传的地址
                            uploadExtraData:{
                                tablename: "aos_sys_qzdw"
                            },
                            fileActionSettings:{
                                showZoom:false//显示预览按钮
                            },
                            allowedFileExtensions: ['xls','xlsx','jpg', 'gif', 'png','pdf','tif'],//接收的文件后缀
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
                    });
                    /**/
                    //window.parent.fnaddtab('','数据导入','/archive/data_relevance/initImport.jhtml?tablename='+tablename.getValue()+'&pch='+pch);
                }
                function _import_data(){
                    var file123=$("#filename");
                    var file=$("#filename").attr("title");
                    var tablename="aos_sys_qzdw";
                    window.parent.fnaddtab('417','数据导入','archive/qzj/initImport_qzdw.jhtml?tablename='+tablename+'&file='+file);
                }
            </script>
        </aos:window>
        <%--上传文件--%>
        <aos:window id="_fileupload_add" title="上传文件"  width="700" height="450" autoScroll="true"  >
            <aos:formpanel width="680" height="450" id="formpanell" layout="column" contentEl="filediv" autoScroll="true">
            </aos:formpanel>
            <aos:docked dock="bottom" ui="footer">
                <aos:dockeditem xtype="tbfill" />
                <aos:dockeditem text="导入数据" onclick="_import_data" icon="add.png" />
                <aos:dockeditem onclick="#_fileupload_add.hide();" text="关闭" icon="close.png" />
            </aos:docked>
        </aos:window>
    </aos:onready>
</aos:html>
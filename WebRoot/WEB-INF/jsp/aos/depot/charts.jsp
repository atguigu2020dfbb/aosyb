<%@page contentType="text/html; charset=utf-8" %>
<%@taglib prefix="aos" uri="/WEB-INF/tld/aos.tld" %>
<%@ taglib prefix="ao" uri="http://www.osworks.cn/tag/aos" %>
<%
    String path =  request.getContextPath();
%>
<aos:html>
    <aos:head>
        <aos:include lib="ext"/>
        <aos:base href="depot/location"/>
        <script type="text/javascript" src="<%=path%>/static/weblib/jquery/jquery-1.10.2.js"></script>
        <script type="text/javascript" src="<%=path%>/static/weblib/jquery/jquery.min-1.10.2.js"></script>
    </aos:head>
    <aos:body>
    </aos:body>
    <aos:onready>
        <aos:viewport layout="border">
                     <aos:hiddenfield name="archive_storehouse" id="archive_storehouse" value="${archive_storehouse}"/>
                     <aos:hiddenfield name="archive_liehao" id="archive_liehao"/>
                    <aos:hiddenfield name="archive_jh" id="archive_jh"/>
                    <aos:hiddenfield name="archive_gh" id="archive_gh"/>
                    <aos:hiddenfield name="chart_historycolor" id="chart_historycolor"/>
                    <aos:hiddenfield name="chart_historyid" id="chart_historyid"/>
                    <aos:hiddenfield name="plan_historycolor" id="plan_historycolor"/>
                    <aos:hiddenfield name="plan_historyid" id="plan_historyid"/>
                    <aos:hiddenfield name="ids" id="ids" />
                    <aos:hiddenfield name="query" id="query" />
                     <aos:panel id="chartsPanel" region="center" title="柱状图"  border="true" collapsible="true" closable="true">
                         <aos:docked>
                             <aos:textfield name="dql" id="dql" fieldLabel="当前列" labelWidth="50" width="100"  readOnly="true"/>
                             <aos:textfield name="dqj" id="dqj" fieldLabel="当前节" labelWidth="50" width="100"  readOnly="true"/>
                             <aos:textfield name="dqg" id="dqg" fieldLabel="当前格" labelWidth="50" width="100"  readOnly="true"/>
                         </aos:docked>
                     </aos:panel>
                     <aos:panel id="planPanel"  region="east" margin="0 -150 0 0"  title="平面图"   border="true" collapsible="true" closable="true">
                        <aos:panel id="planPanel1"  layout="absolute" margin="-30 0 0 0" >
                        </aos:panel>
                     </aos:panel>
                     <aos:gridpanel id="_g_detail" url="listDetail.jhtml" region="south" pageSize="10" split="true"  splitterBorder="0 1 0 1">
                         <aos:docked forceBoder="0 0 1 0">
                             <aos:dockeditem onclick="_w_detail_show" text="新增" icon="add.png" />
                             <aos:dockeditem onclick="_g_detail_del" text="删除" icon="del.png" />
                             <aos:dockeditem xtype="tbseparator" />
                         </aos:docked>
                            <aos:column type="rowno" />
                            <aos:column header="流水号" dataIndex="id_" hidden="true"  locked="true"/>
                            <aos:column header="存址编号" dataIndex="cz_bh" />
                            <aos:column header="全宗单位" dataIndex="qzdw" />
                            <aos:column header="档号" dataIndex="dh" celltip="true" />
                            <aos:column header="题名" dataIndex="tm"  width="200" />
                            <aos:column header="形成时间" dataIndex="xcsj"  />
                            <aos:column header="盒号" dataIndex="hh" />
                            <aos:column header="备注" dataIndex="bz" width="200" celltip="true" />
                             <aos:column header="表名" dataIndex="tablename" hidden="true"  />
                             <aos:column header="表名" dataIndex="tabledesc" />

                        </aos:gridpanel>
            <aos:window id="_w_detail" title="新增登记信息" >
                <aos:formpanel id="_f_detail" layout="anchor" width="700">
                    <aos:textfield name="qzdw" fieldLabel="全宗单位"/>
                    <aos:textfield name="dh" fieldLabel="档号"/>
                    <aos:textfield name="tm" fieldLabel="题名"/>
                    <aos:textfield name="xcsj" fieldLabel="形成时间"/>
                    <aos:textfield name="hh" fieldLabel="盒号"/>
                    <aos:textfield name="bz" fieldLabel="备注"/>
                    <aos:docked dock="bottom">
                        <aos:dockeditem xtype="tbfill"/>
                        <aos:dockeditem text="添加" icon="add.png" onclick="_f_detail_save"/>
                        <aos:dockeditem text="关闭" icon="close.png" onclick="#_w_detail.hide()"/>
                    </aos:docked>
                </aos:formpanel>
            </aos:window>
            <aos:window id="_w_detail_u" title="修改登记信息">
                <aos:formpanel id="_f_detail_u" layout="anchor" width="700">
                    <aos:hiddenfield name="id_" fieldLabel="流水号" />
                    <aos:hiddenfield name="cz_bh" fieldLabel="存址编号" />
                    <aos:textfield name="qzdw" fieldLabel="全宗单位"/>
                    <aos:textfield name="dh" fieldLabel="档号"/>
                    <aos:textfield name="tm" fieldLabel="题名"/>
                    <aos:textfield name="xcsj" fieldLabel="形成时间"/>
                    <aos:textfield name="hh" fieldLabel="盒号"/>
                    <aos:textfield name="bz" fieldLabel="备注"/>
                    <aos:docked dock="bottom">
                        <aos:dockeditem xtype="tbfill"/>
                        <aos:dockeditem text="保存" icon="save.png" onclick="_f_detail_u_save"/>
                        <aos:dockeditem text="关闭" icon="close.png" onclick="#_w_detail_u.hide()"/>
                    </aos:docked>
                </aos:formpanel>
            </aos:window>
            <aos:window id="_w_select_kf" title="库房数据选择"  width="800" height="500" onshow="kf_archive_reset">
                <aos:gridpanel id="_g_select_kf" url="listArchive.jhtml" region="center" width="770" height="460"
                               autoScroll="true" pageSize="20" enableLocking="true" >
                    <aos:docked forceBoder="0 0 1 0">
                        <aos:dockeditem xtype="tbtext" text="数据列表" />
                        <aos:combobox fieldLabel="数据表" name="sjkmc"
                                      fields="['tablename','tabledesc']" id="sjkmc" displayField="tabledesc" valueField="tablename"
                                      editable="false" columnWidth="0.7" emptyText="请选择数据表" url="listTablename_by.jhtml" onselect="select_tablename" width="300"/>
                        <aos:dockeditem text="查询" icon="query.png"
                                        onclick="_w_select_query_show" />
                        <aos:dockeditem  text="确定" icon="ok.png"
                                         onclick="ok_save_kf" />
                        <aos:dockeditem  text="全选" icon="more/edit-select-all-4.png"
                                         onclick="ok_save_All_archive" />
                    </aos:docked>
                    <aos:selmodel type="checkbox" mode="multi" />
                    <aos:column type="rowno" />
                    <aos:column dataIndex="id_" header="流水号" hidden="true" />
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

            </aos:window>
            <aos:window id="_w_query_select_q" title="查询" width="720" autoScroll="true"
                        layout="fit">
                <aos:tabpanel id="_tabpanel_select" region="center" activeTab="0"
                              bodyBorder="0 0 0 0" tabBarHeight="30">
                    <aos:tab title="列表式搜索" id="_tab_select_org">
                        <aos:formpanel id="_f_select_query" layout="column" columnWidth="1">
                            <aos:hiddenfield name="cascode_id_" value="${cascode_id_ }" />
                            <aos:hiddenfield name="tablename" value="${tablename }" />
                            <aos:hiddenfield name="columnnum" id="columnnum" value="7" />
                            <aos:combobox name="and1" value="on"
                                          labelWidth="10" columnWidth="0.2">
                                <aos:option value="on" display="并且" />
                                <aos:option value="up" display="或者" />
                            </aos:combobox>
                            <aos:combobox name="filedname1"  labelWidth="20" columnWidth="0.2" value="qzdw">
                                <aos:option value="qzdw" display="全宗单位"/>
                                <aos:option value="dh" display="档号"/>
                                <aos:option value="tm" display="题名"/>
                                <aos:option value="nd" display="年度"/>
                                <aos:option value="bgqx" display="保管期限"/>
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
                            <aos:combobox name="filedname2"  labelWidth="20" columnWidth="0.2" value="dh">
                                <aos:option value="qzdw" display="全宗单位"/>
                                <aos:option value="dh" display="档号"/>
                                <aos:option value="tm" display="题名"/>
                                <aos:option value="nd" display="年度"/>
                                <aos:option value="bgqx" display="保管期限"/>
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
                            <aos:combobox name="filedname3"  labelWidth="20" columnWidth="0.2" value="tm">
                                <aos:option value="qzdw" display="全宗单位"/>
                                <aos:option value="dh" display="档号"/>
                                <aos:option value="tm" display="题名"/>
                                <aos:option value="nd" display="年度"/>
                                <aos:option value="bgqx" display="保管期限"/>
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
                            <aos:combobox name="filedname4"  labelWidth="20" columnWidth="0.2" value="nd">
                                <aos:option value="qzdw" display="全宗单位"/>
                                <aos:option value="dh" display="档号"/>
                                <aos:option value="tm" display="题名"/>
                                <aos:option value="nd" display="年度"/>
                                <aos:option value="bgqx" display="保管期限"/>
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
                            <aos:combobox name="filedname5"  labelWidth="20" columnWidth="0.2" value="bgqx">
                                <aos:option value="qzdw" display="全宗单位" />
                                <aos:option value="dh" display="档号"  />
                                <aos:option value="tm" display="题名"/>
                                <aos:option value="nd" display="年度" />
                                <aos:option value="bgqx" display="保管期限" />
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
                                <aos:dockeditem onclick="_f_select_data_query" text="确定" icon="ok.png" />
                                <aos:dockeditem onclick="#_w_query_select_q.hide();" text="关闭"
                                                icon="close.png" />
                            </aos:docked>
                        </aos:formpanel>
                    </aos:tab>
                </aos:tabpanel>
            </aos:window>
        </aos:viewport>
        <script type="text/javascript">
            //进来默认执行

            window.onload = function(){
                //Ext.getCmp("chartsPanel").setWidth(document.body.scrollWidth*(2/5));
                Ext.getCmp("planPanel").setWidth(document.body.scrollWidth*(1/2));
                Ext.getCmp("planPanel").setHeight(document.body.scrollHeight*(3/5));

                Ext.getCmp("chartsPanel").setHeight(document.body.scrollHeight*(4/5));
                Ext.getCmp("chartsPanel").setWidth(document.body.scrollWidth*(2/3));

                Ext.getCmp("_g_detail").setHeight(document.body.scrollHeight*(3/10));
                var archive_storehouse ="<%=request.getParameter("archive_storehouse")%>";
                //根据用户名得到条目数，求出总页数
                var myMask = new Ext.LoadMask(Ext.getBody(), {
                    msg: '正在获取数据，请稍后！'
                });
                myMask.show();

                Ext.Ajax.request({
                    url:'getKfJson.jhtml',
                    waitMsg : '正在获取数据,请稍后... ...',
                    params:{'archive_storehouse':archive_storehouse},
                    method : 'post',
                    success:function(response,config){
                        myMask.hide();
                        json=Ext.decode(response.responseText);
                        //得到总条目数
                        var store1 = Ext.create('Ext.data.JsonStore', {
                            fields: [ 'data1','name'],
                            data:json
                        });
                        var chart = Ext.create('Ext.chart.Chart', {
                            style: 'background:#fff',
                            height:600,
                            animate: true,
                            shadow: true,
                            store: store1,
                            axes: [ {
                                type: 'Numeric',
                                position: 'left',
                                fields: ['data1'],
                                title: '被占格数（个）',
                                //majorTickSteps:48,
                                majorTickSteps: 24,
                                minorTickSteps: 0,
                                //data:['0','1','2','3','4','5','6','7','8','9','10','11','12','13','14','15','16','17','18','19','20','21','22','23','24','25'],
                                maximum : 48,
                                grid: true,
                            }, {
                                type: 'Category',
                                position: 'bottom',
                                //data:['0','1','2','3','4','5','6','7','8','9','10','11','12','13','14','15','16','17','18','19','20','21','22','23','24','25'],
                                fields: ['name'],
                                title: '列数',
                                grid: true,
                                label: {
                                    rotate: {
                                        degrees: 270
                                    }
                                }
                            }],
                            series: [
                                {
                                    type: 'column',
                                    axis: 'left',
                                    highlight: true,
                                    listeners: {
                                        'itemmousedown': function(item) {
                                            var tt=document.getElementById("ext-sprite-1128 ");
                                            var chartcolor= item.attr.fill;
                                            Ext.getCmp("archive_liehao").setValue(chartcolor);
                                            _g_detail_store.removeAll();
                                            //此时单击统计图，grid清空重置
                                            var liehao=item.value[0];
                                            //截取字符串
                                            Ext.getCmp("archive_liehao").setValue(liehao);
                                            Ext.getCmp("archive_jh").setValue(0);
                                            Ext.getCmp("archive_gh").setValue(0);
                                            Ext.getCmp("plan_historycolor").setValue("");
                                            Ext.getCmp("plan_historyid").setValue("");
                                            //存入到隐藏域中
                                            //得到当前点击的列数
                                           AOS.ajax({
                                                url: 'getgeSum.jhtml',
                                                wait:"正在加载中",
                                                params:{
                                                    liehao:liehao,
                                                    archive_storehouse:"<%=request.getParameter("archive_storehouse")%>"
                                                },
                                                ok : function(data) {
                                                    Ext.getCmp("dql").setValue(liehao);
                                                    for(k in data){
                                                        if(data[k]==0){
                                                            Ext.getCmp(k).setFieldStyle('background-color:#D1D1D1;');
                                                        }else{
                                                            Ext.getCmp(k).setFieldStyle('background-color:#B22222;');
                                                        }
                                                    }
                                                }
                                            });
                                        }
                                    },
                                    tips: {
                                        trackMouse: true,
                                        width: 140,
                                        height: 28,
                                        renderer: function (storeItem, item) {
                                            this.setTitle(storeItem.get('name') + ': ' + storeItem.get('data1'));
                                        },

                                    },
                                    label: {
                                        display: 'insideEnd',
                                        'text-anchor': 'middle',
                                        field: 'data1',
                                        renderer: Ext.util.Format.numberRenderer('0'),
                                        orientation: 'vertical',
                                        color: '#333'
                                    },
                                    style: {    //柱子显示样式
//                 opacity: 0,    //透明度
//                  fill: '#2562a1',    //填充颜色
                                        fill: '#333',    //填充颜色
//                  'foreground-color': '#2562a1'
                                    },
//              style: { 'foreground-color': '#00f' },  
                                    showMarkers:true,
                                    markerConfig: {// 由Ext.Draw.Sprite配置项决定  
                                        type: 'circle',
                                        size: 4,
                                        radius: 4,
                                        fill: '#00f',
                                        'stroke-width': 0
                                    },
                                    xField: 'name',
                                    yField: 'data1'
                                }
                            ]
                        });
                        var _panel = Ext.getCmp("chartsPanel");
                        _panel.add(chart);
                        addLabel();
                    }
                });


            }


            function addLabel(){
                var _panel = Ext.getCmp("planPanel1");
                /*var stritems = addTextfield(10,10,'#B22222');
                var stritems1 = addTextfield(10,50,'#D1D1D1');
                _panel.add(stritems);
                _panel.add(stritems1);
                return;*/
                var itemsfull={
                    xtype : 'textfield',
                    fieldLabel : '满',
                    labelWidth:20,
                    value:'',
                    readOnly:true,
                    //offsetWidth:'20',
                    //fieldStyle:'background-color: #B22222;',
                    fieldStyle:'background-color:#B22222;',
                    grow:true,
                    growMin:'40',
                    x:450,
                    y:50,
                };

                var itemsempty={
                    xtype : 'textfield',
                    fieldLabel : '空',
                    labelWidth:20,
                    value:'',
                    readOnly:true,
                    //offsetWidth:'20',
                    //fieldStyle:'background-color: #B22222;',
                    fieldStyle:'background-color:#D1D1D1;',
                    grow:true,
                    growMin:'40',
                    x:450,
                    y:100,
                };
                _panel.add(itemsfull);
                _panel.add(itemsempty);
                for(var i=1;i<9;i++){
                    for(var j=1;j<7;j++){
                        //alert(i*10+'******'+j*10);
                        var stritems = addTextfield(i+'-'+j,i*50,j*50,'#B22222');
                        _panel.add(stritems);
                    }
                }

                //var stritems = addTextfield(10,10,'#B22222');
                //_panel.add(stritems)
            }

            function addTextfield(id,x,y,cssstyle){
                //红色 #B22222;
                // 灰色 #D1D1D1
                var items={
                    id:id,
                    xtype : 'textfield',
                    value:id,
                    readOnly:true,
                    //offsetWidth:'20',
                    //fieldStyle:'background-color: #B22222;',
                    fieldStyle:'background-color:'+cssstyle,
                    grow:true,
                    growMin:'40',
                    x:x,
                    y:y,
                    listeners:{
                        render: function(p) {
                            // Append the Panel to the click handler's argument list.
                            p.getEl().on('click', function(p,m,e){
                                //处理点击事件代码
                                //alert('11111');

                                var liehao=Ext.getCmp("archive_liehao").getValue();
                                var archive_storehouse=Ext.getCmp("archive_storehouse").getValue();

                                if(liehao==""){
                                    alert("请选择列号!");
                                    return;
                                }
                                var plan_historycolor=Ext.getCmp("plan_historycolor").getValue();
                                var plan_historyid=Ext.getCmp("plan_historyid").getValue();
                                if(plan_historycolor!=null&&plan_historycolor!=null&&plan_historycolor!=""&&plan_historycolor!=""){
                                    Ext.getCmp(plan_historyid).setFieldStyle(plan_historycolor);
                                }
                                Ext.getCmp("plan_historycolor").setValue(Ext.getCmp(id).getFieldStyle());
                                Ext.getCmp("plan_historyid").setValue(id);
                                //此时把选中的变成绿色，
                                Ext.getCmp(id).setFieldStyle('background-color:#00FF00;');
                                //把节号隔号传递给隐藏域
                                if(id.length==3){
                                    Ext.getCmp("archive_jh").setValue(id.substr(0,1));
                                    Ext.getCmp("archive_gh").setValue(id.substr(id.length-1,1));
                                }
                                Ext.getCmp("dqj").setValue(id.substr(0,1));
                                Ext.getCmp("dqg").setValue(id.substr(id.length-1,1));
                                _g_detail_query(archive_storehouse,liehao,id);

                            });
                        }
                    }
                };
                return items;
            }
            function changecolor(){
                //background-color:#D1D1D1
                var _panel = Ext.getCmp("Mainpanel");
                _panel.add(stritems);
                return;
                var aa=11;
                Ext.getCmp('4-1').setFieldStyle('background-color:#D1D1D1;');
                //Ext.getCmp('alarmsLevelVal').setFieldStyle('background-color:#ff0000;background-p_w_picpath: none; ');


               /* Ext.getCmp('btn2').setStyle({
                    backgroundColor: '#26adaf',
                    color: 'white',
                    borderRadius: '4px'
                });*/
            }
            function _g_detail_query(archive_storehouse,liehao,id){
               // var record = AOS.selectone(_g_location);
                _g_detail_store.getProxy().extraParams = {
                    archive_storehouse:Ext.getCmp("archive_storehouse").getValue(),
                    cz_liehao:liehao,
                    cz_jiehaoandgehao:id
                };
                _g_detail_store.load();
            }
            function _w_detail_show(){
                var archive_liehao=Ext.getCmp("archive_liehao").getValue();
                var archive_storehouse=Ext.getCmp("archive_storehouse").getValue();
                var archive_jh=Ext.getCmp("archive_jh").getValue();
                var archive_gh=Ext.getCmp("archive_gh").getValue();
                if(archive_liehao==""){
                    AOS.tip("请选择列号");
                    return;
                }
                if(archive_jh==0||archive_gh==0){
                    AOS.tip("请选择对应列位置");
                    return;
                }
                _w_select_kf.show();
            }
            /*function _w_detail_show(){
                //得到选择的列号
                var archive_liehao=Ext.getCmp("archive_liehao").getValue();
                var mian=Ext.getCmp("archive_mian").getValue();
                var archive_storehouse=Ext.getCmp("archive_storehouse").getValue();
                var archive_jh=Ext.getCmp("archive_jh").getValue();
                var archive_gh=Ext.getCmp("archive_gh").getValue();
                if(archive_liehao==""){
                    AOS.tip("请选择列号");
                    return;
                }
                if(archive_jh==0||archive_gh==0){
                    AOS.tip("请选择对应列位置");
                    return;
                }
                //判断
                _w_detail.show();
            }*/
            //添加
            function _f_detail_save(){
                var archive_liehao=Ext.getCmp("archive_liehao").getValue();
                var archive_storehouse=Ext.getCmp("archive_storehouse").getValue();
                var archive_jh=Ext.getCmp("archive_jh").getValue();
                var archive_gh=Ext.getCmp("archive_gh").getValue();
                AOS.ajax({
                    forms:_f_detail,
                    params:{cz_liehao:archive_liehao,archive_storehouse:archive_storehouse,cz_jiehao:archive_jh,cz_gehao:archive_gh},
                    url:'saveDetail_charts.jhtml',
                    ok:function(data){
                        if(data.appcode === 1){
                            _w_detail.hide();
                            _g_detail_store.load();
                            //_g_detail_store.reload();
                            AOS.tip(data.appmsg);
                        }else {
                            AOS.err(data.appmsg);
                        }
                    }
                })
            }
            function _f_detail_u_save(){
                AOS.ajax({
                    forms:_f_detail_u,
                    url:'updateDetail.jhtml',
                    ok:function(data){
                        if(data.appcode===1){
                            _w_detail_u.hide();
                            _g_detail_store.load();
                            AOS.tip(data.appmsg);
                        }else {
                            AOS.err(AOS.appmsg);
                        }
                    }
                })
            }
            //保存当前选中的档案信息
            function ok_save_All_archive(){
                var model = _g_select_kf.getSelectionModel();
                model.selectAll();//选择所有行
                var ids="";
                var dhs="";
                //走后台进行当前条件的所有条目的id集合
                AOS.ajax({
                    url: 'getQueryIds.jhtml',
                    params:{'tablename':Ext.getCmp("sjkmc").getValue(),
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
                    }
                });
            }
            function _w_detail_u_show(){
                var record = AOS.selectone(_g_detail);
                //alert(record.data.cz_bh);
                if(record){
                    _w_detail_u.show();
                    _f_detail_u.loadRecord(record);
                }
            }
            /*function _g_detail_del(){
                var selection = AOS.selection(_g_detail,'id_');
                if(AOS.empty(selection)){
                    AOS.tip("删除前请选中数据。。。");
                    return;
                }
                var rows = AOS.rows(_g_detail);
                var msg = AOS.merge('确定要删除选中的[{0}]条数据吗？',rows);
                AOS.confirm(msg,function(btn){
                    if(btn==='cancel'){
                        AOS.tip('删除被操作取消');
                        return;
                    }
                    AOS.ajax({
                        url:'deleteDetail.jhtml',
                        params:{
                            aos_rows_: selection
                        },
                        ok:function(data){
                            AOS.tip(data.appmsg);
                            _g_detail_store.reload();
                        }
                    })
                })
            }*/
            function kf_archive_reset(){
                Ext.getCmp("sjkmc").setRawValue("");
                Ext.getCmp("sjkmc").setValue("");
                //AOS.reset(sjkmc);
                _g_select_kf_store.removeAll();
            }
            //查询窗口展开
            function _w_select_query_show() {
                //判断是不是
                //判断是否选择了数据表
                Ext.getCmp("ids").setValue("");
                var sjkmc=Ext.getCmp("sjkmc").value;
                if(sjkmc==""||sjkmc==null){
                    AOS.tip("请选择数据表!");
                    return;
                }
                _w_query_select_q.show();
            }
            function _f_select_data_query(){
                var params = AOS.getValue('_f_select_query');
                var form = Ext.getCmp('_f_select_query');
                for(var i=1;i<=5;i++){
                    var str = form.down("[name='filedname"+i+"']");
                    var filedname =str.getValue();
                    if(filedname==null){
                        params["filedname"+i]=str.regexText;
                    }
                }
                params["tablename"]=Ext.getCmp("sjkmc").value;
                _g_select_kf_store.getProxy().extraParams = params;
                _g_select_kf_store.load();
                AOS.ajax({
                    params:params,
                    url: 'saveQueryData.jhtml',
                    ok: function (data) {
                        //此时在隐藏域中存入查询条件
                        Ext.getCmp("query").setValue(Ext.getCmp("query").getValue()+" "+data.appmsg);
                    }
                });
                _w_query_select_q.hide();
                AOS.reset(_f_select_query);
            }
            //选择了数据表，并加载下方表格数据
            function select_tablename(){
                var params = {
                    tablename : Ext.getCmp("sjkmc").value
                };
                //这个Store的命名规则为：表格ID+"_store"。
                _g_select_kf_store.getProxy().extraParams = params;
                _g_select_kf_store.load();
            }
            function ok_save_kf(){
                var count=AOS.rows(_g_select_kf);
                if(count<=0){
                    AOS.tip("请选择数据");
                    return;
                }
                var ids=Ext.getCmp("ids").getValue();
                var archive_liehao=Ext.getCmp("archive_liehao").getValue();
                var archive_storehouse=Ext.getCmp("archive_storehouse").getValue();
                var archive_jh=Ext.getCmp("archive_jh").getValue();
                var archive_gh=Ext.getCmp("archive_gh").getValue();
                AOS.ajax({
                    url : 'addKf_archive_charts.jhtml',
                    params:{cz_liehao:archive_liehao,archive_storehouse:archive_storehouse,cz_jiehao:archive_jh,cz_gehao:archive_gh,ids:ids,
                        tablename:Ext.getCmp("sjkmc").value},
                    ok : function(data){
                        if(data.appcode===1){
                            _w_select_kf.hide();
                            _g_detail_store.reload();
                        }else{
                            AOS.err("添加失败,存址可能不存在");
                        }
                    }
                });
            }
            function _g_detail_del(){
                var selection_ids = AOS.selection(_g_detail,'id_');
                var selection_tablenames = AOS.selection(_g_detail,'tablename');

                var archive_liehao=Ext.getCmp("archive_liehao").getValue();
                var archive_storehouse=Ext.getCmp("archive_storehouse").getValue();
                var archive_jh=Ext.getCmp("archive_jh").getValue();
                var archive_gh=Ext.getCmp("archive_gh").getValue();
                if(AOS.empty(selection_ids)){
                    AOS.tip("删除前请选中数据。。。");
                    return;
                }
                    AOS.ajax({
                        url:'deleteDetail_charts.jhtml',
                        params:{
                            cz_liehao:archive_liehao,archive_storehouse:archive_storehouse,cz_jiehao:archive_jh,cz_gehao:archive_gh,
                            id_: AOS.selectone(_g_detail).raw.id_,
                            tablename:AOS.selectone(_g_detail).raw.tablename
                        },
                        ok:function(data){
                            AOS.tip(data.appmsg);
                            _g_detail_store.reload();
                        }
                    })
            }
        </script>
    </aos:onready>
</aos:html>
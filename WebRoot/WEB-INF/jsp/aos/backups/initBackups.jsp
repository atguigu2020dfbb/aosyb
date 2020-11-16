<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="/WEB-INF/tld/aos.tld" prefix="aos"%>
<aos:html>
    <aos:head title="数据备份">
        <aos:include lib="ext" />
        <aos:base href="backups/strategy" />
    </aos:head>
    <aos:onready>
        <aos:viewport layout="border">
            <aos:gridpanel id="_g_table" region="center" url="listAccounts.jhtml" width="200"
                           onitemclick="_g_backups_query" onrender="_g_table_query">

                <aos:docked>
                    <aos:dockeditem xtype="tbtext" text="数据表信息" />
                    <aos:dockeditem xtype="tbfill" />
                    <aos:dockeditem text="全库备份" icon="c_key.png" onclick="_w_backups_all_show"  />
                    <aos:dockeditem text="单表备份" icon="c_key.png" onclick="_w_backups_show"  />
                </aos:docked>
                <aos:column header="表流水号" dataIndex="id_" hidden="true" />
                <aos:column header="数据表名称" dataIndex="tablename"  />
                <aos:column header="数据表描述" dataIndex="tabledesc"  />
            </aos:gridpanel>
            <aos:gridpanel id="_g_backups"  url="listBackups.jhtml" region="east" width="600"
                           split="true" splitterBorder="0 1 0 1"  pageSize="60">
                <aos:docked>
                    <aos:dockeditem xtype="tbtext" text="数据表字段操作" />
                    <aos:dockeditem xtype="tbfill" />
                    <aos:dockeditem text="还原"  icon="undo.png" onclick="_w_returntable_show" />
                    <aos:dockeditem text="删除" icon="del2.png" onclick="_g_backups_del" />
                </aos:docked>
                <aos:selmodel type="checkbox" mode="multi" />
                <aos:column type="rowno" />
                <aos:column header="流水号" dataIndex="id_" hidden="true" />
                <aos:column header="备份时间" dataIndex="bfsj" width="90" />
                <aos:column header="备份人" dataIndex="bfr" width="90" />
                <aos:column header="备份类型" dataIndex="bflx" width="90" />
                <aos:column header="备份表名" dataIndex="bfbm" width="90" hidden="true"/>
                <aos:column header="备份名称" dataIndex="bfmc" width="90" hidden="true"/>
            </aos:gridpanel>
            <aos:window id="_w_config" title="全库备份"
                        autoScroll="true" layout="column" width="310" border="false">
                <aos:formpanel id="_form1" width="300" layout="column" labelWidth="80" bodyBorder="1 0 0 0">
                    <aos:combobox name="condition" columnWidth="0.91" margin="10 0 0 0" center="true"
                                  editable="true" fieldLabel="数据库类型" value="sql server">
                        <aos:option value="sql server" display="sql server"/>
                        <aos:option value="mysql" display="mysql"/>
                        <aos:option value="oracle" display="oracle"/>
                        <aos:option value="DB" display="DB"/>
                        <aos:option value="ACCESS" display="ACCESS"/>
                    </aos:combobox>
                    <aos:textfield name="condition" center="true" columnWidth="0.91" fieldLabel="IP地址"
                                   value="192.168.0.3" margin="20 0 0 0"/>
                    <aos:textfield name="condition" center="true" columnWidth="0.91" fieldLabel="端口"
                                   value="1433" margin="20 0 0 0"/>
                    <aos:textfield name="condition" center="true" columnWidth="0.91" fieldLabel="数据库名称"
                                   value="aosyb" margin="20 0 0 0"/>
                    <aos:textfield name="condition" center="true" columnWidth="0.91" fieldLabel="用户名" value="sa"
                                   margin="20 0 0 0"/>
                    <aos:textfield name="condition" center="true" columnWidth="0.91" fieldLabel="密码"
                                   value="*****" margin="20 0 0 0"/>
                    <aos:docked dock="bottom" ui="footer" padding="0 0 5 0">
                        <aos:dockeditem xtype="tbfill" />
                        <aos:dockeditem xtype="button" text="备份" onclick="backup_record" icon="ok.png" />
                        <aos:dockeditem onclick="#_w_config.hide();" xtype="button" text="取消" icon="del.png"/>
                        <%--					<aos:dockeditem xtype="button" text="还原" onclick="restore_record" icon="refresh.png" />--%>
                        <aos:dockeditem xtype="tbfill" />
                    </aos:docked>
                </aos:formpanel>
            </aos:window>
        </aos:viewport>
        <script type="text/javascript">
            //数据库备份
            function backup_record(){
                // AOS.file('backupData.jhtml');
                AOS.ajax({
                    url: 'backupData.jhtml',
                    ok: function (data) {
                        if(data.appcode===1){
                            AOS.file("download.jhtml?filepath="+data.filepath+"&name="+data.name+"");
                        }
                    }
                });
            }
            //弹出
            function _w_backups_all_show(){
                _w_config.show();
            }
            function _g_table_query(){
                _g_table_store.load();
            }
            function _g_backups_query(){
                var params={};
                var record = AOS.selectone(_g_table);
                if (!AOS.empty(record)) {
                    params.tid = record.data.id_;
                }
                _g_backups_store.getProxy().extraParams = params;
                _g_backups_store.load();
            }
            function _w_backups_show(){
                var record = AOS.selectone(_g_table);
               // if (AOS.empty(record)) {
                    //params.tid = record.data.id_;
                 //   AOS.tip("备份前请选中数据。。。");
                //    return;
              //  }
                AOS.ajax({
                    url:'saveBackups.jhtml',
                    params:{
                        tid:record.data.id_,
                        bfbm:record.data.tablename
                    },
                    ok:function(data){
                            _g_backups_store.reload();
                            AOS.tip(data.appmsg);

                    }
                })
            }

            function _g_backups_del(){
                var selection = AOS.selection(_g_backups,'id_');
                if(AOS.empty(selection)){
                    AOS.tip("删除前请选中数据。。。");
                    return;
                }
                var rows = AOS.rows(_g_backups);
                var msg = AOS.merge('确定要删除选中的[{0}]条数据吗？',rows);
                AOS.confirm(msg,function(btn){
                    if(btn==='cancel'){
                        AOS.tip('删除被操作取消');
                        return;
                    }
                    AOS.ajax({
                        url:'deleteBackups.jhtml',
                        params:{
                            aos_rows_: selection
                        },
                        ok:function(data){
                            AOS.tip(data.appmsg);
                            _g_backups_store.reload();
                        }
                    })
                })
            }

            function _w_returntable_show(){
                var selection = AOS.selection(_g_backups,'id_');

                var record = AOS.selectone(_g_backups);
                if(AOS.empty(selection)){
                    AOS.tip("还原前请选中数据。。。");
                    return;
                }
                var rows = AOS.rows(_g_backups);
                var msg = AOS.merge('确定要还原选中的[{0}]条数据吗？',rows);
                AOS.confirm(msg,function(btn){
                    if(btn==='cancel'){
                        AOS.tip('还原被操作取消');
                        return;
                    }
                    AOS.ajax({
                        url:'returnTable.jhtml',
                        params:{
                            aos_rows_: selection,
                            bfmc:record.data.bfmc,
                            bfbm:record.data.bfbm
                        },
                        ok:function(data){
                            AOS.tip(data.appmsg);
                            _g_backups_store.reload();
                        }
                    })
                })

            }
        </script>
    </aos:onready>
</aos:html>






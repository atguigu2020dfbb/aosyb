<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="/WEB-INF/tld/aos.tld" prefix="aos"%>
<%@ taglib uri="/WEB-INF/tld/cll.tld" prefix="c"%>
<aos:html>
    <aos:head title="登记详情">
        <aos:include lib="ext" />
        <aos:base href="utilization"/>
    </aos:head>
    <aos:body>

    </aos:body>
    <aos:onready>
        <aos:viewport layout="border">
            <aos:gridpanel id="_g_param" url="listDjxq.jhtml" pageSize="${pagesize }" enableLocking="false" region="center" split="true" onrender="_w_params_show">
                <aos:docked forceBoder="0 0 1 0">
                    <aos:hiddenfield  id="tablename" name="tablename" value="${tablename}"/>
                    <aos:hiddenfield  id="formid" name="formid" value="${formid}"/>
                    <aos:dockeditem text="显示" icon="picture.png"  id="_f_filename_message"
                                    onclick="_w_picture_show"   />
                </aos:docked>
                <aos:column type="rowno"  />
                <aos:column dataIndex="id_" header="流水号" hidden="true"  locked="true"/>
                <c:forEach var="field" items="${fieldDtos}">
                    <aos:column dataIndex="${field.fieldenname}"
                                header="${field.fieldcnname}" width="${field.dislen}"  rendererField="field_type_" />
                </c:forEach>
                <aos:column header="表明" dataIndex="tablename" hidden="true"/>
                <aos:column header="" flex="1" />
            </aos:gridpanel>
            <aos:window id="_w_files" title="电子文件" autoScroll="true" width="800"
                        border="true" height="500" onshow="get_files">
                <aos:gridpanel id="_g_files" region="east" url="getFiles.jhtml"
                               hidePagebar="true" split="true" width="800">
                    <aos:docked forceBoder="0 0 1 0">
                        <aos:dockeditem xtype="tbtext" text="电子文件列表" />
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
        </aos:viewport>

        <script type="text/javascript">
            function _w_params_show(){
                var params={
                    tablename:tablename.getValue(),
                    formid:formid.getValue()
                }
                //这个Store的命名规则为：表格ID+"_store"。
                _g_param_store.getProxy().extraParams = params;
                _g_param_store.load();
            }
            function _w_picture_show(){
                var selection = AOS.selection(_g_param, 'id_');
                if (AOS.empty(selection)) {
                    AOS.tip('请选择需要查看电子文件的档案!');
                    return;
                }
                if(selection.substring(0,selection.length-1).split(",").length>1){
                    alert("请选择单个条目数据！");
                    return;
                }
                //弹出一个窗口，展示电子文件列表
                _w_files.show();
            }
            function get_files(){
                var selection = AOS.selection(_g_param, 'id_');
                var id_=selection.substring(0,selection.length-1);
                var listTablename = AOS.selection(_g_param, 'tablename').substring(0,AOS.selection(_g_param, 'tablename').length-1);
                var params ={
                    id_:id_,
                    tablename:listTablename
                };
                //这个Store的命名规则为：表格ID+"_store"。
                _g_files_store.getProxy().extraParams = params;
                _g_files_store.load();
            }
            //查看电子文件
            function _w_files_look(){
                //得到当前用户,判断是不是档案室和admin用户
                var user="<%=session.getAttribute("user")%>";
                var tablename = AOS.selection(_g_param, 'tablename').substring(0,AOS.selection(_g_param, 'tablename').length-1);
                //得到当前选中的。
                var row = AOS.selectone(_g_files);
                if(row.data._s_path.split(".")[1]=="pdf"||row.data._s_path.split(".")[1]=="PDF"){
                    parent.fnaddtab(row.data.id_, '电子文件',
                        'archive/data/openPdfFile.jhtml?id='+row.data.id_+'&tid='+row.data.tid+'&type='+row.data._s_path.split(".")[1]+'&tablename='+tablename);
                }else if(row.data._s_path.split(".")[1]=="jpg"||row.data._s_path.split(".")[1]=="JPG"||row.data._s_path.split(".")[1]=="doc"||row.data._s_path.split(".")[1]=="DOC"){
                    parent.fnaddtab(row.data.id_,'电子文件','archive/data/openSwfFile.jhtml?id='+row.data.id_+'&tid='+row.data.tid+'&type='+row.data._s_path.split(".")[1]+'&tablename='+tablename);
                }
            }
        </script>

    </aos:onready>
</aos:html>
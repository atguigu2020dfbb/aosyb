<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="/WEB-INF/tld/aos.tld" prefix="aos"%>
<%@ taglib uri="/WEB-INF/tld/cll.tld" prefix="c"%>
<aos:html>
    <aos:head title="详细查看">
        <aos:include lib="ext" />
        <aos:base href="utilization/data" />
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
        <div id="_div_photo" class="x-hidden" align="center">
            <img id="PhotoID" name="PhotoID" class="app_cursor_pointer"  style="width:96px; height:118px;margin-top:30px">
        </div>
    </aos:body>
    <aos:onready>
        <aos:viewport layout="border">
            <aos:hiddenfield name="tablename" id="tablename" value="${tablename}"/>
            <aos:hiddenfield  id="formid" name="formid" value="${formid}"/>
            <aos:panel region="west" width="250" bodyBorder="0 1 0 0">
                <aos:layout type="vbox" align="stretch" />
                <aos:panel height="500" layout="fit">
                    <aos:docked forceBoder="1 0 1 0">
                        <aos:dockeditem xtype="tbtext" text="申请人信息" />
                    </aos:docked>
                    <aos:formpanel layout="anchor" labelWidth="60" header="false" icon="user8.png" id="_f_info">
                        <aos:button text="111" />
                        <aos:textfield name="xm" id="xm" fieldLabel="姓名" columnWidth="0.99"/>
                        <aos:textfield name="xb" id="xb" fieldLabel="性别" columnWidth="0.99"/>
                        <aos:textfield name="mzgj" id="mzgj" fieldLabel="民族或国籍" columnWidth="0.99"/>
                        <aos:textfield name="cs" id="cs" fieldLabel="出生" columnWidth="0.99"/>
                        <aos:textfield name="dz" id="dz" fieldLabel="地址" columnWidth="0.99"/>
                        <aos:textfield name="sfzh" id="sfzh" fieldLabel="身份证号" columnWidth="0.99"/>
                        <aos:textfield name="qfjg" id="qfjg" fieldLabel="签发机关" columnWidth="0.99"/>
                        <aos:fieldset title="图像" labelWidth="70" columnWidth="0.35" contentEl="_div_photo" height="250" >
                        </aos:fieldset>
                    </aos:formpanel>
                </aos:panel>
            </aos:panel>

            <aos:panel region="center" border="false" layout="border">


                <aos:gridpanel height="120" id="_g_center" hidePagebar="true" url="listAccounts.jhtml" onrender="_g_center_query" region="north" onitemclick="fn_click_center">
                    <aos:column type="rowno" />
                    <aos:column dataIndex="id_" header="流水号" hidden="true" />
                    <c:forEach var="field" items="${fieldDtos}">
                        <aos:column dataIndex="${field.fieldenname}"
                                    header="${field.fieldcnname }" width="${field.dislen }"
                                    rendererField="field_type_" >
                            <aos:textfield />
                        </aos:column>
                    </c:forEach>
                    <aos:column header="" flex="1" />
                </aos:gridpanel>
                <aos:panel  region="center" layout="hbox">
                    <aos:panel id="documentViewer2"  region="center" height="400" autoScroll="true" split="true" width="650"  contentEl="documentViewer">

                    </aos:panel>

                    <aos:panel layout="hbox">

                        <aos:gridpanel height="200" id="_g_documnet" url="listDocument.jhtml" hidePagebar="true" onitemdblclick="fn_dbcclick_document" onrender="_g_document_query" region="north">
                            <aos:column type="rowno" />
                            <aos:column dataIndex="id_" header="流水号" hidden="true" />
                            <aos:column dataIndex="tid" header="主表ID" hidden="true" />
                            <aos:column dataIndex="relpath" header="文件路径"  hidden="true"/>
                            <aos:column dataIndex="_path" header="文件名称"  />
                        </aos:gridpanel>
                    </aos:panel>

                </aos:panel>
            </aos:panel>

        </aos:viewport>
        <script type="text/javascript">
            window.onload=function(){
                AOS.ajax({
                    url:"getform.jhtml",
                    params:{
                        id_:formid.getValue()
                    },
                    ok:function (data) {
                        AOS.setValue('_f_info.xm',data.xm);
                        AOS.setValue('_f_info.xb',data.xb);
                        AOS.setValue('_f_info.mzgj',data.mzgj);
                        AOS.setValue('_f_info.cs',data.cs);
                        AOS.setValue('_f_info.dz',data.dz);
                        AOS.setValue('_f_info.sfzh',data.sfzh);
                        AOS.setValue('_f_info.qfjg',data.qfjg);
                        document.all('PhotoID').src = 'data:image/jpeg;base64,' +data.tpsj;
                    }
                })
                //document.all('PhotoID').src = 'data:image/jpeg;base64,' + "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";
            }

            function _g_center_query(){
                var params = {
                    tablename : tablename.getValue(),
                    formid:formid.getValue()
                };
                _g_center_store.getProxy().extraParams = params;
                _g_center_store.reload();
            }
            function fn_click_center(){
                var record = AOS.selectone(_g_center);
                //alert(record.data.id_);
                var params = {
                    tablename : tablename.getValue(),
                    tid:record.data.id_
                };
                _g_documnet_store.getProxy().extraParams = params;
                _g_documnet_store.load();
            }
            function _g_document_query(){

            }
            function fn_dbcclick_document(){
                var record = AOS.selectone(_g_documnet);
                AOS.ajax({
                    url:"getDocumentPath.jhtml",
                    params:{
                        id:record.data.id_,
                        tid:record.data.tid,
                        tablename : tablename.getValue(),
                    },
                    ok:function(data){
                        if(data.appcode==1){
                            var url="http://192.168.0.3/Data/";
                            PDFObject.embed(url+data.pdfpath, '#documentViewer');
                        }
                    }
                })
               // var urlpath="http://192.168.0.3/Data/GDWJ_WSWJ/328/5319299/65601/11-44-005.TIF"
                //PDFObject.embed(urlpath, '#documentViewer');
            }
        </script>
    </aos:onready>
</aos:html>
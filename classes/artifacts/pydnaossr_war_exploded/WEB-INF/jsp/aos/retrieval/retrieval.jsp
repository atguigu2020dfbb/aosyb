<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="/WEB-INF/tld/aos.tld" prefix="aos"%>
<aos:html>
<aos:head title="全文检索">
	<aos:include lib="ext,preview"/>
	<aos:include css="${cxt}/static/css/previewPlugin.css" />
	<aos:base href="retrieval/retrieval" />
</aos:head>
    
<aos:body>
</aos:body>
<aos:onready>
	<aos:viewport layout="border">
		<aos:formpanel id="_f_info" layout="column" autoScroll="true" region="north" border="false"
			labelWidth="70" padding="20 0 0 200">
				<aos:textfield fieldLabel="" height="30" id="contentId" name="content" allowBlank="true"
					columnWidth="0.7" />
					<aos:button text="搜索" width="100" height="30" margin="0 0 0 10" onclick="_w_query" />
		<aos:button text="初始化" width="100" height="30" margin="0 0 0 10" onclick="_w_initIndex" />
		</aos:formpanel>
		<aos:gridpanel id="_g_data" url="getDataList.jhtml" region="center" previewPlugin="true" bodyField="nrzy" any="hideHeaders:true">
			<aos:column dataIndex="id_" header="流水号" hidden="true"/>
			<aos:column id="tm"  dataIndex="tm" header="题名" rendererFn="fn_tm_render" />
			<aos:column dataIndex="nrzy" header="内容摘要" hidden="true"/>
			<aos:column dataIndex="dh" header="档号" hidden="true"/>
			<aos:column  dataIndex="zrz" header="责任者" hidden="true" />
			<aos:column  dataIndex="bgqx" header="保管期限" hidden="true"  />
			<aos:column  dataIndex="nd" header="日期" hidden="true" />
			<aos:column  dataIndex="dalb" header="档案类别" hidden="true" />
			<aos:column  dataIndex="tablename" header="表名" hidden="false" rendererFn="fn_tablename_render"/>
			<aos:column  dataIndex="pathid" header="电子表主ID" hidden="true" />
		</aos:gridpanel>
	</aos:viewport>
	<script>
		function _w_create(){		
		AOS.ajax({
                    forms:_f_info,
                    url: 'listAccounts.jhtml',
                    ok: function (data) {
                        _f_print_u.form.setValues(data);
                    }
                });
		
		}
		function _w_query(){
		var content=contentId.getValue();
		var params = {
				tablename : "all_table",
				content:content
			};
			//这个Store的命名规则为：表格ID+"_store"。
			_g_data_store.getProxy().extraParams = params;
			_g_data_store.load();
		}
        function _w_picture_dbclick(grid,row){
        parent.fnaddtab(row.data.id, '电子文件',
							'archive/data/openPdfFile.jhtml?id='+row.data.id_+'&tablename=wsgdwj');
        }
        function fn_tm_render(value, metaData, record, rowIndex, colIndex,store){
	        return Ext.String.format(
	       // '<b><a class="title" href="openFile.jhtml?id={6}&tid={7}&type={8}&tablename={9}">{0}</a></b>'+  
	       '<b><a class="title" href="javascript:void(0);">{0}</a></b>'+  
	        '<b class="title2" >档号：{1}&nbsp&nbsp&nbsp&nbsp&nbsp责任者：{2}&nbsp&nbsp&nbsp&nbsp&nbsp保管期限：{3}&nbsp&nbsp&nbsp&nbsp&nbsp日期：{4}&nbsp&nbsp&nbsp&nbsp&nbsp档案类别：{5}</b>',
	        record.data.tm,
	        record.data.dh,
	        record.data.zrz,
	        record.data.bgqx,
	        record.data.nd,
	        record.data.dalb
	        );
        }
        function fntmonclick(){
        //id='+row.data.pid+'&tid='+row.data.tid+'&type='+row.data.type+'&tablename='+tablename.getValue()
        alert('111');
        return;
        
        }
        
        _g_data.on("cellclick", function(pGrid, rowIndex, columnIndex, e) {
        var record = AOS.selectone(_g_data);
        var id = record.data.pathid;
        var tid= record.data.id_;
        var tablename = record.data.tablename;
		if(columnIndex === 0){
			parent.fnaddtab('111', '电子文件','archive/data/openPdfFile.jhtml?id='+id+'&tid='+tid+'&tablename='+tablename+'&type=pdf');
			}
		});
        function _w_initIndex(){
        AOS.ajax({
                    //forms:_f_info,
                    url: 'addIndex.jhtml',
                    ok: function (data) {
                    
                    AOS.tip(data.appmsg);
                      //  _f_print_u.form.setValues(data);
                    }
                });
        
        }
	</script>
</aos:onready>
</aos:html>
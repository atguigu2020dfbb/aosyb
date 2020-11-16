Ext.define("Ext.ux.grid.DPrinter",{

    requires: 'Ext.XTemplate',

    statics: {
        print: function(grid) {
            var me=this;
            me.initColumns(grid.columns);
            var columns = this.columns;
            me.headings=me.initHeader(grid.columns);
            var data = [];
            grid.store.data.each(function(item) {
                var convertedData = [];
                for (var key in item.data) {
                    var value = item.data[key];

                    Ext.each(columns, function(column) {
                        if (column.dataIndex == key) {
                            convertedData[key] = column.renderer ? column.renderer(value) : value;
                        }
                    }, this);
                }

                data.push(convertedData);
            });
            var body     = Ext.create('Ext.XTemplate', this.bodyTpl).apply(columns);

            var htmlMarkup = [
                '<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">',
                '<html>',
                '<head>',
                '<meta content="text/html; charset=UTF-8" http-equiv="Content-Type" />',
                '<link href="' + this.stylesheetPath + '" rel="stylesheet" type="text/css" media="screen,print" />',
                '<title></title>',
                '</head>',
                '<body>',
                '<table>'+this.headings+
                '<tpl for=".">',
                body,
                '</tpl>',
                '</table>',
                '</body>',
                '</html>'
            ];
            var html = Ext.create('Ext.XTemplate', htmlMarkup).apply(data);
            var win = window.open('', 'printgrid');
            win.document.write(html);
            if (this.printAutomatically){
                win.print();
                win.close();
            }
        },

        initColumns:function(columns){
            var me=this;
            Ext.each(columns,function(column){
                if(column.dataIndex!=null){
                    me.columns.push(column);
                }
                else{
                    me.initColumns(column.items.items);
                }
            });
        },

        initHeaderCtl:function(columns,index,maxrow){
            var me=this;
            var html='<tr align=center>';
            var colspan=0;
            Ext.each(columns,function(column){
                if(column.dataIndex!=null){
                    if(maxrow-index>1)html+='<th rowspan='+eval(maxrow-index)+'>'+column.text+'</th>';
                    else html+='<th>'+column.text+'</th>';
                    colspan++;
                }
                else{
                    var temp=me.initHeaderCtl(column.items.items,index+1,maxrow);
                    html+='<th colspan='+temp+'>'+column.text+'</th>';
                    colspan+=temp;
                }

            });
            html+='</tr>';
            var temp=this.headerCtl[index];
            if(temp!=null && temp!=''){
                html=temp+html;
            }
            this.headerCtl[index]=html;
            return colspan;
        },

        initHeader:function(columns){
            var me=this;
            me.getMaxRows(columns,1);
            me.initHeaderCtl(columns,0,me.maxrow);
            var headerCtls=me.headerCtl;
            var headings='';
            Ext.each(headerCtls,function(headerCtl){
                headings+=headerCtl;
            });
            return headings;
        },
        getMaxRows:function(columns,rowIndex){
            var me=this;
            Ext.each(columns,function(column){
                if(column.dataIndex==null){
                    var temp=me.getMaxRows(column.items.items,rowIndex+1);
                }
            });
            if(rowIndex>me.maxrow){
                me.maxrow=rowIndex;
            }

            return me.maxrow;
        },
        columns:[],
        headerCtl:[],
        maxrow:0,
        headings:'',
        stylesheetPath: 'ux/grid/gridPrinterCss/print.css',
        printAutomatically: true,

        bodyTpl: [
            '<tr>',
            '<tpl for=".">',
            '<td>\{{dataIndex}\}</td>',
            '</tpl>',
            '</tr>'
        ]
    }
}); 
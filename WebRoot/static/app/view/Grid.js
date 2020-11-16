 Ext.define('GridFilterDemo.view.Grid', {
	    extend : "Ext.grid.Panel",
		xtype : "app-gridpanel",
		//important ,required lib declaration
		requires :[
		   "Ext.ux.grid.FiltersFeature",
		   "Ext.toolbar.Toolbar",
		   "Ext.ux.grid.filter.*",
		   "Ext.ux.grid.menu.*"
		],
        border: false,
        store: "ProductStore",
		loadMask: true,
        features: {
			ftype: 'filters',
            local: true,
			encode : false,
			filters: [{
				type: 'boolean',
				dataIndex: 'visible'
            }]
        },
        columns: [{
            dataIndex: 'id',
            text: 'Id',
            // instead of specifying filter config just specify filterable=true
            // to use store's field's type property (if type property not
            // explicitly specified in store config it will be 'auto' which
            // GridFilters will assume to be 'StringFilter'
            filterable: true,
            width: 30
            //filter: {type: 'numeric'}
        }, {
            dataIndex: 'company',
            text: 'Company',
            id: 'company',
            flex: 1,
            filter: {
                type: 'string'
            }
        }, {
            dataIndex: 'price',
            text: 'Price',
            filter: {
                type: 'numeric'  // specify type here or in store fields config
            },
            width: 70
        }, {
            dataIndex: 'size',
            text: 'Size',
            filter: {
                type: 'list',
                phpMode: true,
				// options will be used as data to implicitly creates an ArrayStore
				options: ['extra small', 'small', 'medium',
				'large', 'extra large']
            }
        }, {
            dataIndex: 'date',
            text: 'Date',
            filter: true,
            renderer: Ext.util.Format.dateRenderer('m/d/Y')
        }, {
            dataIndex: 'visible',
            text: 'Visible'
            // this column's filter is defined in the filters feature config
        }],
        emptyText: 'No Matching Records'
    });
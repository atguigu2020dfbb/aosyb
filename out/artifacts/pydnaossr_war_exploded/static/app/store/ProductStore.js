
Ext.define('GridFilterDemo.store.ProductStore', {
	extend : "Ext.data.Store",
	// store configs
	autoDestroy: true,
	autoLoad :true,
	model: 'GridFilterDemo.model.Product',
	proxy: {
		type: 'ajax',
		url: "grid-filter.json",
		reader: {
			type: 'json',
			root: 'data',
			idProperty: 'id',
			totalProperty: 'total'
		}
	},
	sorters: [{
		property: 'company',
		direction: 'ASC'
	}],
	pageSize: 50
});
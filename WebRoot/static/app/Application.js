Ext.define('GridFilterDemo.Application', {
    name: 'GridFilterDemo',

    extend: 'Ext.app.Application',
    
    views: [
        'Grid','Main'
    ],

    controllers: [
        
    ],

    stores: [
		'ProductStore'
    ]
});

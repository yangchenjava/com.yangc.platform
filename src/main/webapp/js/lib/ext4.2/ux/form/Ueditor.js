/**
 * Extjs集成百度编辑器
 */
Ext.define('Ext.ux.form.Ueditor', {
	extend : 'Ext.form.FieldContainer',
	mixins : {
		field : 'Ext.form.field.Field'
	},
	alias : 'widget.ueditor',
	alternateClassName : 'Ext.ux.Ueditor',
	ueditorInstance : null,
	initialized : false,
	initComponent : function() {
		var me = this;
		me.addEvents('initialize', 'change');
		var id = me.id + '-ueditor';
		me.html = '<script id="' + id + '" name="' + me.name + '" type="text/plain"></script>';
		me.callParent(arguments);
		me.initField();
		me.on('render', function() {
			var width = me.width - 75;
			var height = me.height - 109;
			var config = {
				initialFrameWidth : width,
				initialFrameHeight : height
			};
			me.ueditorInstance = UE.getEditor(id, config);
			me.ueditorInstance.ready(function() {
				me.initialized = true;
				me.fireEvent('initialize', me);
				me.ueditorInstance.addListener('contentChange', function() {
					me.fireEvent('change', me);
				});
			});
		});
	},
	getValue : function() {
		var me = this, value = '';
		if (me.initialized) {
			value = me.ueditorInstance.getContent();
		}
		me.value = value;
		return value;
	},
	setValue : function(value) {
		var me = this;
		if (value === null || value === undefined) {
			value = '';
		}
		if (me.initialized) {
			me.ueditorInstance.setContent(value);
		}
		return me;
	},
	onDestroy : function() {
		this.ueditorInstance.destroy();
	}
});

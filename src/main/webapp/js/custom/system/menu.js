Ext.define("MenuTree", {
    extend: "Ext.data.Model",
    fields: [
        {name: "id",           type: "int"},
        {name: "text",         type: "string"},
        {name: "leaf",         type: "boolean"},
        {name: "menuId",       type: "int"},
        {name: "menuName",     type: "string"},
        {name: "menuUrl",      type: "string"},
        {name: "parentMenuId", type: "int"},
        {name: "serialNum",    type: "int"},
        {name: "isshow",       type: "int"},
        {name: "description",  type: "string"}
    ]
});

Ext.onReady(function(){
	/** ------------------------------------- store ------------------------------------- */
	var store_menuTree = Ext.create("Ext.data.TreeStore", {
        model: "MenuTree",
        nodeParam: "parentMenuId",
        proxy: {
            type: "ajax",
            actionMethods: {
				create: "POST", read: "POST", update: "POST", destroy: "POST"
			},
            url: basePath + "resource/menu/getMenuTreeList"
        },
        root: {
        	id: "0",
	        text: "系统菜单",
	        menuId: "0",
	        menuName: "系统菜单",
	        expanded: true
	    },
        autoLoad: true
    });
	
	/** ------------------------------------- view ------------------------------------- */
	var tree_menu = Ext.create("Ext.tree.Panel", {
        store: store_menuTree,
        width: 300,
        height: document.documentElement.clientHeight,
        border: false,
        style: {
			borderWidth: "0 2px 0 0",
			borderStyle: "solid"
		},
        rootVisible: true,
        listeners: {
        	itemclick: showMenuHandler
        },
        viewConfig: {
        	stripeRows: true,
        	plugins: {
        		ptype: "treeviewdragdrop",
        		containerScroll: true
        	},
        	listeners: {
        		beforedrop: function(node, data, overModel, dropPosition, dropHandlers){
        			var srcModel = data.records[0];
        			if (srcModel.get("leaf") && hasPermission("menu" + permission.UPD)) {
        				var parentMenuId = dropPosition == "append" ? overModel.get("id") : overModel.get("parentMenuId");
        				$.post(basePath + "resource/menu/updateParentMenuId", {
        					id: srcModel.get("id"),
        					parentMenuId: parentMenuId
        				}, function(data){
        					if (data.success) {
        						message.info(data.message);
        					} else {
        						message.error(data.message);
        					}
        				});
        			} else {
        				return false;
        			}
        		},
        		itemcontextmenu: function(thiz, record, item, index, e, eOpts){
        			if (record.get("depth") >= 3) {
        				Ext.getCmp("createMenu").setDisabled(true);
        			} else {
        				Ext.getCmp("createMenu").setDisabled(!hasPermission("menu" + permission.ADD));
        			}
        			if (record.get("id") == 0) {
        				Ext.getCmp("updateMenu").setDisabled(true);
        				Ext.getCmp("deleteMenu").setDisabled(true);
        			} else {
        				Ext.getCmp("updateMenu").setDisabled(!hasPermission("menu" + permission.UPD));
        				Ext.getCmp("deleteMenu").setDisabled(!hasPermission("menu" + permission.DEL));
        			}
        			e.stopEvent();
        			menu_menu.showAt(e.getXY());
                    return false;
        		}
        	}
        }
    });
	
	var menu_menu = Ext.create("Ext.menu.Menu", {
        items: [
            {id: "createMenu", text: "创建子菜单", icon: basePath + "js/lib/ext4.2/icons/add.gif", handler: createMenu},
            {id: "updateMenu", text: "修改菜单", icon: basePath + "js/lib/ext4.2/icons/edit_task.png", handler: updateMenu},
            {id: "deleteMenu", text: "删除菜单", icon: basePath + "js/lib/ext4.2/icons/delete.gif", handler: deleteMenu}
        ]
    });
	
	var fieldSet_menu = Ext.create("Ext.form.FieldSet", {
		title: "菜单详情",
		columnWidth: .6,
		padding: 20,
		margin: "10 0 0 20",
		defaults: {
			labelAlign: "right",
            labelWidth: 60,
            anchor: "100%"
		},
		items: [
		    {id: "addOrUpdate_menuId", name: "id", xtype: "hidden"},
		    {id: "addOrUpdate_parentMenuId", name: "parentMenuId", xtype: "hidden"},
			{id: "addOrUpdate_menuName", name: "menuName", xtype: "textfield", fieldLabel: "菜单名称", vtype: "basic_chinese"},
			{id: "addOrUpdate_menuUrl", name: "menuUrl", xtype: "textfield", fieldLabel: "菜单地址", vtype: "basic"},
			{id: "addOrUpdate_serialNum", name: "serialNum", xtype: "numberfield", fieldLabel: "顺序", minValue: 1},
			{xtype: "radiogroup", fieldLabel: "是否显示", items: [
                {id: "addOrUpdate_isshow_yes", boxLabel: "是", name: "isshow", inputValue: 1, checked: true},
                {id: "addOrUpdate_isshow_no", boxLabel: "否", name: "isshow", inputValue: 0}
            ]},
            {id: "addOrUpdate_description", name: "description", xtype: "textarea", fieldLabel: "描述", vtype: "basic_chinese"},
            {id: "addOrUpdate_button", xtype: "button", margin: "0 0 0 300", maxWidth: 70, text: "确定", handler: addOrUpdateMenuHandler}
		]
	});
	
	Ext.create("Ext.form.Panel", {
		renderTo: "menu",
		width: "100%",
		height: document.documentElement.clientHeight - 127,
        border: false,
		header: false,
		layout: "column",
        items: [tree_menu, fieldSet_menu]
	});
	
    /** ------------------------------------- handler ------------------------------------- */
	function refreshMenuTree(){
		tree_menu.up("form").getForm().reset();
		store_menuTree.load();
    }
	
	function fieldSetReadOnly(isReadOnly){
		Ext.getCmp("addOrUpdate_menuName").setReadOnly(isReadOnly);
		Ext.getCmp("addOrUpdate_menuUrl").setReadOnly(isReadOnly);
		Ext.getCmp("addOrUpdate_serialNum").setReadOnly(isReadOnly);
		Ext.getCmp("addOrUpdate_isshow_yes").setReadOnly(isReadOnly);
		Ext.getCmp("addOrUpdate_isshow_no").setReadOnly(isReadOnly);
		Ext.getCmp("addOrUpdate_description").setReadOnly(isReadOnly);
		Ext.getCmp("addOrUpdate_button").setDisabled(isReadOnly);
	}
	fieldSetReadOnly(true);
	
	function showMenuHandler(thiz, record, item, index, e, eOpts){
		fieldSetReadOnly(true);
		thiz.up("form").getForm().loadRecord(record);
	}
	
	function createMenu(){
		tree_menu.up("form").getForm().reset();
		var record = tree_menu.getSelectionModel().getSelection()[0];
		Ext.getCmp("addOrUpdate_parentMenuId").setValue(record.get("id"));
		fieldSetReadOnly(false);
	}
	
	function updateMenu(){
		tree_menu.up("form").getForm().reset();
		var record = tree_menu.getSelectionModel().getSelection()[0];
		tree_menu.up("form").getForm().loadRecord(record);
		fieldSetReadOnly(false);
	}
	
	function deleteMenu(){
		var record = tree_menu.getSelectionModel().getSelection()[0];
		if (record.get("leaf")) {
			message.confirm("是否删除记录？", function(){
				$.post(basePath + "resource/menu/delMenu", {
					id: record.get("id"),
				}, function(data){
					if (data.success) {
						fieldSetReadOnly(true);
						message.info(data.message);
						refreshMenuTree();
					} else {
						message.error(data.message);
					}
				});
			});
		} else {
			message.info("该节点下存在子节点，无法删除！");
		}
	}
	
	function addOrUpdateMenuHandler(){
		var menuName = Ext.getCmp("addOrUpdate_menuName");
		var serialNum = Ext.getCmp("addOrUpdate_serialNum");
		if (!menuName.getValue().trim()) {
			message.error("请输入菜单名称！");
		} else if (!serialNum.getValue()) {
			message.error("请输入顺序！");
		} else {
			var url;
			if (Ext.getCmp("addOrUpdate_menuId").getValue()) {
				url = basePath + "resource/menu/updateMenu";
			} else {
				url = basePath + "resource/menu/addMenu";
			}
			tree_menu.up("form").getForm().submit({
				url: url,
				method: "POST",
				success: function(form, action){
					fieldSetReadOnly(true);
					message.info(action.result.msg);
					refreshMenuTree();
				},
				failure: function(form, action){
					message.error(action.result.msg);
				}
			});
		}
	}
});

Ext.define("Role", {
    extend: "Ext.data.Model",
    fields: [
		{name: "id",   	        type: "int"},
		{name: "roleName",   	type: "string"},
		{name: "createTimeStr", type: "string"}
    ]
});

Ext.define("AuthTree", {
    extend: "Ext.data.Model",
    fields: [
        {name: "id",       type: "int"},
        {name: "text",     type: "string"},
        {name: "leaf",     type: "boolean"},
        {name: "menuId",   type: "int"},
        {name: "menuName", type: "string"},
		{name: "all",      type: "boolean"},
		{name: "sel",      type: "boolean"},
		{name: "add",      type: "boolean"},
		{name: "upd",      type: "boolean"},
		{name: "del",      type: "boolean"}
    ]
});

Ext.onReady(function(){
	/** ------------------------------------- store ------------------------------------- */
	var store_roleGrid = Ext.create("Ext.data.Store", {
		model: "Role",
		pageSize: 20,
		proxy: {
			type: "ajax",
			actionMethods: {
				create: "POST", read: "POST", update: "POST", destroy: "POST"
			},
			url: basePath + "resource/role/getRoleList_page",
			reader: {
            	root: "dataGrid",
                totalProperty: "totalCount"
            }
		},
		autoLoad: true
	});
	
	var store_authTree = Ext.create("Ext.data.TreeStore", {
        model: "AuthTree",
        nodeParam: "parentMenuId",
        proxy: {
            type: "ajax",
            actionMethods: {
				create: "POST", read: "POST", update: "POST", destroy: "POST"
			},
            url: basePath + "resource/acl/getAuthTreeList"
        },
        root: {
        	id: "0",
	        text: "系统菜单",
	        menuId: "0",
	        menuName: "系统菜单",
	    },
        autoLoad: false
    });
	
	/** ------------------------------------- view ------------------------------------- */
	var grid_role = Ext.create("Ext.grid.Panel", {
        renderTo: "role",
		store: store_roleGrid,
		width: "100%",
		height: document.documentElement.clientHeight - 127,
		border: false,
        collapsible: false,
        multiSelect: false,
        scroll: false,
        viewConfig: {
            stripeRows: true,
            enableTextSelection: true
        },
        columns: [
            {text: "序号",    width: 50, align: "center", xtype: "rownumberer"},
            {text: "角色名称", flex: 1,   align: "center", dataIndex: "roleName"},
            {text: "创建时间", flex: 1,   align: "center", dataIndex: "createTimeStr"}
        ],
        tbar: new Ext.Toolbar({
        	height: 30,
			items: [
		        {width: 5,  disabled: true},
		        {width: 55, text: "创建", handler: createRole, disabled: !hasPermission("role" + permission.ADD), icon: basePath + "js/lib/ext4.2/icons/add.gif"}, "-",
		        {width: 55, text: "修改", handler: updateRole, disabled: !hasPermission("role" + permission.UPD), icon: basePath + "js/lib/ext4.2/icons/edit_task.png"}, "-",
		        {width: 55, text: "删除", handler: deleteRole, disabled: !hasPermission("role" + permission.DEL), icon: basePath + "js/lib/ext4.2/icons/delete.gif"}, "-",
		        {width: 55, text: "授权", handler: authorizeRole, disabled: !hasPermission("role" + permission.ADD), icon: basePath + "js/lib/ext4.2/icons/user_suit.png"}
		    ]
        }),
        bbar: Ext.create("Ext.PagingToolbar", {
        	store: store_roleGrid,
            displayInfo: true,
            displayMsg: "当前显示{0} - {1}条，共 {2} 条记录",
            emptyMsg: "当前没有任何记录"
        })
    });
	
	Ext.define("panel_addOrUpdate_role", {
		extend: "Ext.form.Panel",
        bodyPadding: 20,
        bodyBorder: false,
        frame: false,
		header: false,
        fieldDefaults: {
            labelAlign: "right",
            labelWidth: 70,
            anchor: "100%"
        },
        items: [
			{id: "addOrUpdate_roleId", name: "id", xtype: "hidden"},
			{id: "addOrUpdate_roleName", name: "roleName", xtype: "textfield", fieldLabel: "角色名称", allowBlank: false, invalidText: "请输入角色名称！", vtype: "basic_chinese"}
		]
	});
    Ext.define("window_addOrUpdate_role", {
		extend: "Ext.window.Window",
    	layout: "fit",
		width: 500,
		bodyMargin: 10,
		border: false,
		closable: true,
		modal: true,
		plain: true,
		resizable: false,
		items: [],
		buttonAlign: "right",
        buttons: [
            {text: "确定", handler: addOrUpdateRoleHandler}, "-",
			{text: "取消", handler: function(){this.up("window").close();}}
        ]
	});
    
    Ext.define("panel_addOrUpdate_acl", {
    	extend: "Ext.tree.Panel",
        bodyBorder: false,
        frame: false,
		header: false,
		useArrows: true,
        rootVisible: false,
        store: store_authTree,
        columns: [
			{xtype: "treecolumn", text: "菜单", dataIndex: "menuName", flex: 2, draggable: false, sortable: false, menuDisabled: true},
			{xtype: "checkcolumn", text: "全选", dataIndex: "all", flex: 1, draggable: false, sortable: false, menuDisabled: true},
			{xtype: "checkcolumn", text: "查询", dataIndex: "sel", flex: 1, draggable: false, sortable: false, menuDisabled: true},
			{xtype: "checkcolumn", text: "添加", dataIndex: "add", flex: 1, draggable: false, sortable: false, menuDisabled: true},
			{xtype: "checkcolumn", text: "修改", dataIndex: "upd", flex: 1, draggable: false, sortable: false, menuDisabled: true},
			{xtype: "checkcolumn", text: "删除", dataIndex: "del", flex: 1, draggable: false, sortable: false, menuDisabled: true}
        ],
		listeners: {
			itemmouseup: addOrUpdateAclHandler
		}
	});
    Ext.define("window_addOrUpdate_acl", {
    	extend: "Ext.window.Window",
		layout: "fit",
		title: "授权",
		width: 750,
		height: 500,
		bodyMargin: 10,
		border: false,
		closable: true,
		modal: true,
		plain: true,
		resizable: false,
		items: [],
		buttonAlign: "right",
        buttons: [
			{text: "关闭", handler: function(){this.up("window").close();}}
        ]
	});
    
    /** ------------------------------------- handler ------------------------------------- */
    function refreshRoleGrid(){
    	grid_role.getSelectionModel().deselectAll();
    	store_roleGrid.currentPage = 1;
		store_roleGrid.load();
    }
    
	function createRole(){
		var panel_addOrUpdate_role = Ext.create("panel_addOrUpdate_role");
		
		var window_addOrUpdate_role = Ext.create("window_addOrUpdate_role");
		window_addOrUpdate_role.add(panel_addOrUpdate_role);
		window_addOrUpdate_role.setTitle("创建");
		window_addOrUpdate_role.show();
	}
	
	function updateRole(){
		if (grid_role.getSelectionModel().hasSelection()) {
			var record = grid_role.getSelectionModel().getSelection()[0];
			
			var panel_addOrUpdate_role = Ext.create("panel_addOrUpdate_role");
			panel_addOrUpdate_role.getForm().loadRecord(record);
			
			var window_addOrUpdate_role = Ext.create("window_addOrUpdate_role");
			window_addOrUpdate_role.add(panel_addOrUpdate_role);
			window_addOrUpdate_role.setTitle("修改");
			window_addOrUpdate_role.show();
		} else {
			message.info("请先选择数据再操作！");
		}
	}
	
	function deleteRole(){
		if (grid_role.getSelectionModel().hasSelection()) {
			message.confirm("是否删除记录？", function(){
				var record = grid_role.getSelectionModel().getSelection()[0];
				$.post(basePath + "resource/role/delRole", {
					id: record.get("id"),
				}, function(data){
					if (data.success) {
						message.info(data.message);
						refreshRoleGrid();
					} else {
						message.error(data.message);
					}
				});
			});
		} else {
			message.info("请先选择数据再操作！");
		}
	}
	
	function authorizeRole(){
		if (grid_role.getSelectionModel().hasSelection()) {
			var record = grid_role.getSelectionModel().getSelection()[0];
			
			var panel_addOrUpdate_acl = Ext.create("panel_addOrUpdate_acl");
			store_authTree.proxy.extraParams = {"roleId": record.get("id")};
			store_authTree.load({
				scope: this,
		   	    callback: function(records, operation, success){
		   	    	store_authTree.getRootNode().expand();
		   	    }
			});
			
			var window_addOrUpdate_acl = Ext.create("window_addOrUpdate_acl");
			window_addOrUpdate_acl.add(panel_addOrUpdate_acl);
			window_addOrUpdate_acl.show();
		} else {
			message.info("请先选择数据再操作！");
		}
	}
	
	function addOrUpdateRoleHandler(){
		var roleName = Ext.getCmp("addOrUpdate_roleName");
		if (!roleName.isValid()) {
			message.error(roleName.invalidText);
		} else {
			var url;
			if (Ext.getCmp("addOrUpdate_roleId").getValue()) {
				url = basePath + "resource/role/updateRole";
			} else {
				url = basePath + "resource/role/addRole";
			}
			var window_addOrUpdate_role = this.up("window");
			window_addOrUpdate_role.items.items[0].getForm().submit({
				url: url,
				method: "POST",
				success: function(form, action){
					window_addOrUpdate_role.close();
					message.info(action.result.msg);
					refreshRoleGrid();
				},
				failure: function(form, action){
					message.error(action.result.msg);
				}
			});
		}
	}
	
	function addOrUpdateAclHandler(thiz, record, item, index, e, eOpts){
		var permission = -1, allow = -1, cellIndex = e.target.offsetParent.cellIndex;
		switch (cellIndex) {
		case 1:
			permission = 4;
			if (record.get("all")) {
				record.set("sel", true);
				record.set("add", true);
				record.set("upd", true);
				record.set("del", true);
				allow = 1;
			} else {
				record.set("sel", false);
				record.set("add", false);
				record.set("upd", false);
				record.set("del", false);
				allow = 0;
			}
			break;
		case 2:
			permission = 3;
			if (record.get("sel")) {
				allow = 1;
			} else {
				record.set("all", false);
				allow = 0;
			}
			break;
		case 3:
			permission = 2;
			if (record.get("add")) {
				allow = 1;
			} else {
				record.set("all", false);
				allow = 0;
			}
			break;
		case 4:
			permission = 1;
			if (record.get("upd")) {
				allow = 1;
			} else {
				record.set("all", false);
				allow = 0;
			}
			break;
		case 5:
			permission = 0;
			if (record.get("del")) {
				allow = 1;
			} else {
				record.set("all", false);
				allow = 0;
			}
			break;
		}
		
		if (permission != -1 && allow != -1) {
			$.post(basePath + "resource/acl/addOrUpdateAcl", {
				roleId: grid_role.getSelectionModel().getSelection()[0].get("id"),
				menuId: record.get("menuId"),
				permission: permission,
				allow: allow
			}, function(data){
				if (!data.success) {
					message.error(data.message);
				}
			});
		}
	}
});

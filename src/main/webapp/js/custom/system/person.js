Ext.define("Person", {
    extend: "Ext.data.Model",
    fields: [
		{name: "id",   	   type: "int"},
		{name: "name",     type: "string"},
		{name: "sex",      type: "int"},
		{name: "phone",    type: "string"},
		{name: "spell",    type: "string"},
		
		{name: "deptId",   type: "int"},
		{name: "deptName", type: "string"},
		
		{name: "userId",   type: "int"},
		{name: "username", type: "string"},
		
		{name: "roleIds",  type: "string"}
    ]
});

Ext.define("Dept", {
	extend: "Ext.data.Model",
	fields: [
        {name: "id",   	   type: "int"},
        {name: "deptName", type: "string"}
    ]
});

Ext.define("Role", {
	extend: "Ext.data.Model",
	fields: [
	    {name: "id",   	   type: "int"},
	    {name: "roleName", type: "string"}
	]
});

Ext.onReady(function(){
	/** ------------------------------------- store ------------------------------------- */
	var store_personGrid = Ext.create("Ext.data.Store", {
		model: "Person",
		pageSize: 20,
		proxy: {
			type: "ajax",
			actionMethods: {
				create: "POST", read: "POST", update: "POST", destroy: "POST"
			},
			url: basePath + "resource/person/getPersonListByPersonNameAndDeptId_page",
			reader: {
            	root: "dataGrid",
                totalProperty: "totalCount"
            }
		},
		autoLoad: true
	});
	
	var store_deptList = Ext.create("Ext.data.Store", {
		model: "Dept",
		proxy: {
			type: "ajax",
			actionMethods: {
				create: "POST", read: "POST", update: "POST", destroy: "POST"
			},
			url: basePath + "resource/dept/getDeptList"
		},
		autoLoad: true
	});

	var store_roleList = Ext.create("Ext.data.Store", {
		model: "Role",
		proxy: {
			type: "ajax",
			actionMethods: {
				create: "POST", read: "POST", update: "POST", destroy: "POST"
			},
			url: basePath + "resource/role/getRoleList"
		},
		autoLoad: false
	});

	var store_roleIdsList = Ext.create("Ext.data.Store", {
		model: "Person",
		proxy: {
			type: "ajax",
			actionMethods: {
				create: "POST", read: "POST", update: "POST", destroy: "POST"
			},
			url: basePath + "resource/person/getRoleIdsByUserId"
		},
		autoLoad: false
	});
	
	var store_spellList = Ext.create("Ext.data.Store", {
		model: "Person",
		proxy: {
			type: "ajax",
			actionMethods: {
				create: "POST", read: "POST", update: "POST", destroy: "POST"
			},
			url: basePath + "resource/person/getPersonList"
		},
		autoLoad: true
	});
	
	/** ------------------------------------- view ------------------------------------- */
	var grid_person = Ext.create("Ext.grid.Panel", {
        renderTo: "person",
		store: store_personGrid,
		width: "100%",
		height: document.documentElement.clientHeight - 127,
		border: 0,
        collapsible: false,
        multiSelect: false,
        scroll: false,
        viewConfig: {
            stripeRows: true,
            enableTextSelection: true
        },
        columns: [
            {text: "序号", width: 50, align: "center", xtype: "rownumberer"},
            {text: "用户名", flex: 1, align: "center", dataIndex: "username"},
            {text: "昵称", flex: 1, align: "center", dataIndex: "name"},
            {text: "性别", flex: 1, align: "center", dataIndex: "sex", renderer: function(value){
            	return value == 0 ? "女" : "男";
            }},
            {text: "电话", flex: 1, align: "center", dataIndex: "phone"},
            {text: "部门", flex: 1, align: "center", dataIndex: "deptName"}
        ],
        tbar: new Ext.Toolbar({
        	height: 30,
			items: [
		        {width: 5,  disabled: true},
		        {width: 55, text: "创建", handler: createPerson, disabled: !hasPermission("person" + permission.ADD), icon: basePath + "js/lib/ext4.2/icons/add.gif"}, "-",
		        {width: 55, text: "修改", handler: updatePerson, disabled: !hasPermission("person" + permission.UPD), icon: basePath + "js/lib/ext4.2/icons/edit_task.png"}, "-",
		        {width: 55, text: "删除", handler: deletePerson, disabled: !hasPermission("person" + permission.DEL), icon: basePath + "js/lib/ext4.2/icons/delete.gif"},
		        {width: 200,  disabled: true},
		        {width: 180, id: "search_name", xtype: "combobox", emptyText: "昵称", store: store_spellList, forceSelection: true, editable: true, valueField: "name", displayField: "name", hideTrigger: true, queryMode: "local",
		        	listConfig: {
		    			getInnerTpl: function(){
		    				return "{name} ({spell})";
		    			}
		    		},
		        	listeners: {
			        	beforequery: function(queryPlan, eOpts){
			        		if (!queryPlan.forceAll) {
			        			var combo = queryPlan.combo, content = queryPlan.query.trim();
			        			if (content) {
			        				combo.store.filterBy(function(record, id){
			        					var nameSpell = record.get("name") + record.get("spell");
			        					return nameSpell.indexOf(content) != -1;
			        				});
			        				combo.expand();
			        			} else {
			        				combo.collapse();
			        			}
			        			return false;
			        		}
			        	}
			        }
		        },
		        {width: 150, id: "search_dept", xtype: "combobox", emptyText: "部门", store: store_deptList, forceSelection: true, editable: false, valueField: "id", displayField: "deptName"},
		        {width: 55, text: "搜索", handler: refreshPersonGrid, icon: basePath + "js/lib/ext4.2/icons/search.png"},
		        {width: 55, text: "清空", handler: resetPersonGrid, icon: basePath + "js/lib/ext4.2/icons/refresh.png"}
		    ]
        }),
        bbar: Ext.create("Ext.PagingToolbar", {
        	store: store_personGrid,
            displayInfo: true,
            displayMsg: "当前显示{0} - {1}条，共 {2} 条记录",
            emptyMsg: "当前没有任何记录"
        })
    });
	
	Ext.define("panel_addOrUpdate_person", {
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
			{id: "addOrUpdate_personId", name: "id", xtype: "hidden"},
			{id: "addOrUpdate_userId", name: "userId", xtype: "hidden"},
			{xtype: "container", layout:"column", items: [
                {xtype: "container", columnWidth:.5, layout: "anchor", items: [
                    {id: "addOrUpdate_username", name: "username", xtype: "textfield", fieldLabel: "用户名", allowBlank: false, invalidText: "请输入用户名！", vtype: "basic"}
                ]},
                {xtype: "container", columnWidth:.5, layout: "anchor", items: [
                    {id: "addOrUpdate_name", name: "name", xtype: "textfield", fieldLabel: "昵称", allowBlank: false, invalidText: "请输入昵称！", vtype: "basic_chinese"}
                ]}
            ]},
            {id: "addOrUpdate_sex", xtype: "radiogroup", fieldLabel: "性别", allowBlank: false, invalidText: "请选择性别！", items: [
                {boxLabel: "男", name: "sex", inputValue: 1, checked: true},
                {boxLabel: "女", name: "sex", inputValue: 0}
            ]},
            {xtype: "container", layout:"column", items: [
                {xtype: "container", columnWidth:.5, layout: "anchor", items: [
                    {id: "addOrUpdate_phone", name: "phone", xtype: "textfield", fieldLabel: "手机", allowBlank: false, invalidText: "请输入手机！", vtype: "mobile"}
                ]},
                {xtype: "container", columnWidth:.5, layout: "anchor", items: [
                    {id: "addOrUpdate_dept", name: "deptId", xtype: "combobox", fieldLabel: "部门", allowBlank: false, invalidText: "请选择部门！", store: store_deptList, forceSelection: true, editable: false, valueField: "id", displayField: "deptName"}
                ]}
            ]},
            {id: "addOrUpdate_role", name: "roleIds", xtype: "itemselector", fieldLabel: "角色", allowBlank: false, labelSeparator: "", invalidText: "请选择角色！",
            	height: 120, imagePath: basePath + "js/lib/ext4.2/ux/css/images/", buttons: ["add", "remove"], store: store_roleList, valueField: "id", displayField: "roleName"
        	}
		]
	});
    Ext.define("window_addOrUpdate_person", {
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
            {text: "确定", handler: addOrUpdatePersonHandler}, "-",
			{text: "取消", handler: function(){this.up("window").close();}}
        ]
	});
    
    /** ------------------------------------- handler ------------------------------------- */
    function refreshPersonGrid(){
    	var name = Ext.getCmp("search_name").getValue() ? Ext.getCmp("search_name").getValue() : "";
    	var deptId = Ext.getCmp("search_dept").getValue() ? Ext.getCmp("search_dept").getValue() : 0;
    	
    	grid_person.getSelectionModel().deselectAll();
    	store_personGrid.currentPage = 1;
    	store_personGrid.proxy.extraParams = {"name": encodeURIComponent(name), "deptId": deptId};
		store_personGrid.load();
    }
    
    function resetPersonGrid(){
    	Ext.getCmp("search_name").reset();
    	Ext.getCmp("search_dept").reset();
    	refreshPersonGrid();
    }
    
	function createPerson(){
		var panel_addOrUpdate_person = Ext.create("panel_addOrUpdate_person");
		store_roleList.load();
		
		var window_addOrUpdate_person = Ext.create("window_addOrUpdate_person");
		window_addOrUpdate_person.add(panel_addOrUpdate_person);
		window_addOrUpdate_person.setTitle("创建<label style='color: #FF7256;'>（初始密码为123456）</label>");
		window_addOrUpdate_person.show();
	}
	
	function updatePerson(){
		if (grid_person.getSelectionModel().hasSelection()) {
			var record = grid_person.getSelectionModel().getSelection()[0];
			
			var panel_addOrUpdate_person = Ext.create("panel_addOrUpdate_person");
			store_roleList.load();
			panel_addOrUpdate_person.getForm().loadRecord(record);
			store_roleIdsList.load({
				params: {"userId": record.get("userId")},
		   	 	scope: this,
		   	    callback: function(records, operation, success){
		   	    	Ext.getCmp("addOrUpdate_role").setValue(records[0].get("roleIds"));
		   	    }
	   	 	});
			
			var window_addOrUpdate_person = Ext.create("window_addOrUpdate_person");
			window_addOrUpdate_person.add(panel_addOrUpdate_person);
			window_addOrUpdate_person.setTitle("修改");
			window_addOrUpdate_person.show();
		} else {
			message.info("请先选择数据再操作！");
		}
	}
	
	function deletePerson(){
		if (grid_person.getSelectionModel().hasSelection()) {
			message.confirm("是否删除记录？", function(){
				var record = grid_person.getSelectionModel().getSelection()[0];
				$.post(basePath + "resource/person/delPerson", {
					id: record.get("id"),
				}, function(data){
					if (data.success) {
						message.info(data.message);
						refreshPersonGrid();
					} else {
						message.error(data.message);
					}
				});
			});
		} else {
			message.info("请先选择数据再操作！");
		}
	}
	
	function addOrUpdatePersonHandler(){
		var username = Ext.getCmp("addOrUpdate_username");
		var name = Ext.getCmp("addOrUpdate_name");
		var sex = Ext.getCmp("addOrUpdate_sex");
		var phone = Ext.getCmp("addOrUpdate_phone");
		var dept = Ext.getCmp("addOrUpdate_dept");
		var role = Ext.getCmp("addOrUpdate_role");
		if (!username.isValid()) {
			message.error(username.invalidText);
		} else if (!name.isValid()) {
			message.error(name.invalidText);
		} else if (!sex.isValid()) {
			message.error(sex.invalidText);
		} else if (!phone.isValid()) {
			message.error(phone.invalidText);
		} else if (!dept.isValid()) {
			message.error(dept.invalidText);
		} else if (!role.isValid()) {
			message.error(role.invalidText);
		} else {
			var url;
			if (Ext.getCmp("addOrUpdate_personId").getValue()) {
				url = basePath + "resource/person/updatePerson";
			} else {
				url = basePath + "resource/person/addPerson";
			}
			var window_addOrUpdate_person = this.up("window");
			window_addOrUpdate_person.items.items[0].getForm().submit({
				url: url,
				method: "POST",
				success: function(form, action){
					window_addOrUpdate_person.close();
					message.info(action.result.msg);
					refreshPersonGrid();
				},
				failure: function(form, action){
					message.error(action.result.msg);
				}
			});
		}
	}
});

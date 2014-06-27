Ext.define("Dept", {
    extend: "Ext.data.Model",
    fields: [
		{name: "id",   	        type: "int"},
		{name: "deptName",      type: "string"},
		{name: "serialNum",     type: "int"},
		{name: "createTimeStr", type: "string"}
    ]
});

Ext.onReady(function(){
	/** ------------------------------------- store ------------------------------------- */
	var store_deptGrid = Ext.create("Ext.data.Store", {
		model: "Dept",
		pageSize: 20,
		proxy: {
			type: "ajax",
			actionMethods: {
				create: "POST", read: "POST", update: "POST", destroy: "POST"
			},
			url: basePath + "resource/dept/getDeptList_page",
			reader: {
            	root: "dataGrid",
                totalProperty: "totalCount"
            }
		},
		autoLoad: true
	});
	
	/** ------------------------------------- view ------------------------------------- */
	var grid_dept = Ext.create("Ext.grid.Panel", {
        renderTo: "dept",
		store: store_deptGrid,
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
            {text: "序号", width: 50, align: "center", xtype: "rownumberer"},
            {text: "部门名称", flex: 2, align: "center", dataIndex: "deptName"},
            {text: "排序", flex: 1, align: "center", dataIndex: "serialNum"},
            {text: "创建时间", flex: 2, align: "center", dataIndex: "createTimeStr"}
        ],
        tbar: new Ext.Toolbar({
        	height: 30,
			items: [
		        {width: 5,  disabled: true},
		        {width: 55, text: "创建", handler: createDept, disabled: !hasPermission("dept" + permission.ADD), icon: basePath + "js/lib/ext4.2/icons/add.gif"}, "-",
		        {width: 55, text: "修改", handler: updateDept, disabled: !hasPermission("dept" + permission.UPD), icon: basePath + "js/lib/ext4.2/icons/edit_task.png"}, "-",
		        {width: 55, text: "删除", handler: deleteDept, disabled: !hasPermission("dept" + permission.DEL), icon: basePath + "js/lib/ext4.2/icons/delete.gif"}, "-",
		        {width: 55, text: "打印", handler: printDept,  disabled: !hasPermission("dept" + permission.SEL), icon: basePath + "js/lib/ext4.2/icons/printer.png"}
		    ]
        }),
        bbar: Ext.create("Ext.PagingToolbar", {
        	store: store_deptGrid,
            displayInfo: true,
            displayMsg: "当前显示{0} - {1}条，共 {2} 条记录",
            emptyMsg: "当前没有任何记录"
        })
    });
	
	Ext.define("panel_addOrUpdate_dept", {
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
			{id: "addOrUpdate_deptId", name: "id", xtype: "hidden"},
			{id: "addOrUpdate_deptName", name: "deptName", xtype: "textfield", fieldLabel: "部门名称", allowBlank: false, invalidText: "请输入部门名称！", vtype: "basic_chinese"},
			{id: "addOrUpdate_serialNum", name: "serialNum", xtype: "numberfield", fieldLabel: "顺序", allowBlank: false, invalidText: "请输入顺序！", minValue: 1}
		]
	});
    Ext.define("window_addOrUpdate_dept", {
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
            {text: "确定", handler: addOrUpdateDeptHandler}, "-",
			{text: "取消", handler: function(){this.up("window").close();}}
        ]
	});
    
    /** ------------------------------------- handler ------------------------------------- */
    function refreshDeptGrid(){
    	grid_dept.getSelectionModel().deselectAll();
    	store_deptGrid.currentPage = 1;
		store_deptGrid.load();
    }
    
	function createDept(){
		var panel_addOrUpdate_dept = Ext.create("panel_addOrUpdate_dept");
		
		var window_addOrUpdate_dept = Ext.create("window_addOrUpdate_dept");
		window_addOrUpdate_dept.add(panel_addOrUpdate_dept);
		window_addOrUpdate_dept.setTitle("创建");
		window_addOrUpdate_dept.show();
	}
	
	function updateDept(){
		if (grid_dept.getSelectionModel().hasSelection()) {
			var record = grid_dept.getSelectionModel().getSelection()[0];
			
			var panel_addOrUpdate_dept = Ext.create("panel_addOrUpdate_dept");
			panel_addOrUpdate_dept.getForm().loadRecord(record);
			
			var window_addOrUpdate_dept = Ext.create("window_addOrUpdate_dept");
			window_addOrUpdate_dept.add(panel_addOrUpdate_dept);
			window_addOrUpdate_dept.setTitle("修改");
			window_addOrUpdate_dept.show();
		} else {
			message.info("请先选择数据再操作！");
		}
	}
	
	function deleteDept(){
		if (grid_dept.getSelectionModel().hasSelection()) {
			message.confirm("是否删除记录？", function(){
				var record = grid_dept.getSelectionModel().getSelection()[0];
				$.post(basePath + "resource/dept/delDept", {
					id: record.get("id"),
				}, function(data){
					if (data.success) {
						message.info(data.message);
						refreshDeptGrid();
					} else {
						message.error(data.message);
					}
				});
			});
		} else {
			message.info("请先选择数据再操作！");
		}
	}
	
	function printDept(){
		if (store_deptGrid.getCount() > 0) {
			Ext.ux.grid.Printer.printAutomatically = false;
			Ext.ux.grid.Printer.print(grid_dept);
		} else {
			message.info("没有发现要打印的数据！");
		}
	}
	
	function addOrUpdateDeptHandler(){
		var deptName = Ext.getCmp("addOrUpdate_deptName");
		var serialNum = Ext.getCmp("addOrUpdate_serialNum");
		if (!deptName.isValid()) {
			message.error(deptName.invalidText);
		} else if (!serialNum.isValid()) {
			message.error(serialNum.invalidText);
		} else {
			var url;
			if (Ext.getCmp("addOrUpdate_deptId").getValue()) {
				url = basePath + "resource/dept/updateDept";
			} else {
				url = basePath + "resource/dept/addDept";
			}
			var window_addOrUpdate_dept = this.up("window");
			window_addOrUpdate_dept.items.items[0].getForm().submit({
				url: url,
				method: "POST",
				success: function(form, action){
					window_addOrUpdate_dept.close();
					message.info(action.result.msg);
					refreshDeptGrid();
				},
				failure: function(form, action){
					message.error(action.result.msg);
				}
			});
		}
	}
});

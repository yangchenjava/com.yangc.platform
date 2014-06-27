Ext.Loader.setConfig({enabled: true});
Ext.Loader.setPath("Ext.ux", basePath + "js/lib/ext4.2/ux");
Ext.require([
    "*",
    "Ext.ux.form.MultiSelect",
    "Ext.ux.form.ItemSelector",
    "Ext.ux.grid.Printer",
    "Ext.ux.window.Notification"
]);

Ext.define("TopFrame", {
    extend: "Ext.data.Model",
    fields: [
		{name: "id",   	   type: "int"},
		{name: "menuName", type: "string"},
		{name: "menuUrl",  type: "string"}
    ]
});

Ext.onReady(function(){
	/** ------------------------------------- store ------------------------------------- */
	Ext.create("Ext.data.Store", {
		model: "TopFrame",
		proxy: {
			type: "ajax",
			actionMethods: {
				create: "POST", read: "POST", update: "POST", destroy: "POST"
			},
			url: basePath + "resource/menu/showTopFrame"
		},
		autoLoad: true,
		listeners: {
    		load: function(thiz, records, successful, eOpts){
				for (var i = 0, len = records.length; i < len; i++) {
					body.add({
						id: records[i].get("id"),
						title: records[i].get("menuName"),
						border: 0,
						margin: "-1 0 0 0",
						loader: {
							url: basePath + records[i].get("menuUrl"),
							autoLoad: false,
							scripts: true
						}
					});
				}
				body.setActiveTab(0);
    		}
    	}
	});
	
	/** ------------------------------------- view ------------------------------------- */
	var head = Ext.create("Ext.panel.Panel", {
		region: "north",
		layout: "anchor",
		border: 0,
		height: 35,
		tbar: new Ext.Toolbar({
			style: {
				"background": "#B3DFDA",
				"background-image": "none !important"
			},
			height: 35,
			padding: "0 20 0 0",
			items: [
			    "->", {height: 20, text: "修改密码", handler: changePassword}, "-",
			    {height: 20, text: "注销", handler: logout}
			]
		})
	});
	
	var body = Ext.create("Ext.tab.Panel", {
		region: "center",
		layout: "anchor",
		border: 0,
		xtype: "tabpanel",
		minTabWidth: 150,
		plain: true,
		cls: "ui-tab-bar",
		bodyCls: "ui-tab-body",
		style: {
			"background": "#B3DFDA"
		},
		items: [],
		listeners: {
    		tabchange: function(tabPanel, newCard, oldCard, eOpts){
    			$.post(basePath + "resource/ping/system", function(data){
    				if (data.success) {
    					parentMenuId = newCard.id;
    					newCard.loader.load();
    				} else {
    					message.error(data.message);
    				}
    			});
			}
		}
	});
	
	var foot = Ext.create("Ext.panel.Panel", {
		region: "south",
		layout: "anchor",
		border: 0,
        height: 25,
		tbar: new Ext.Toolbar({
			style: {
				"background": "#B3DFDA",
				"background-image": "none !important"
			},
			height: 25,
			items: ["->", "版权所有人: yangc", "->"]
		})
	});
	
	Ext.create("Ext.Viewport", {
		layout: {
            type: "border",
            padding: 1
        },
        items: [head, body, foot]
    });
	
	var panel_changePassword = Ext.create("Ext.form.Panel", {
        bodyPadding: 20,
        bodyBorder: false,
        frame: false,
		header: false,
        fieldDefaults: {
            labelAlign: "right",
            labelWidth: 60,
            anchor: "100%"
        },
        items: [
			{id: "password", xtype: "textfield", inputType:"password", fieldLabel: "原密码", allowBlank: false, invalidText: "请输入原密码！", vtype: "password"},
			{id: "newPassword_1", xtype: "textfield", inputType:"password", fieldLabel: "新密码", allowBlank: false, invalidText: "请输入新密码！", vtype: "password"},
			{id: "newPassword_2", xtype: "textfield", inputType:"password", fieldLabel: "确认密码", allowBlank: false, invalidText: "请输入确认密码！", vtype: "password", validator: validatorPasswordRepeatHandler}
		]
	});
    var window_changePassword = Ext.create("Ext.window.Window", {
		title: "修改密码",
		layout: "fit",
		width: 350,
		bodyMargin: 10,
		border: false,
		closable: true,
		closeAction: "hide",
		draggable: false,
		modal: true,
		plain: true,
		resizable: false,
		items: [panel_changePassword],
		buttonAlign: "right",
        buttons: [
            {text: "确定", handler: changePasswordHandler}, "-",
			{text: "取消", handler: function(){window_changePassword.hide();}}
        ]
	});
    
    /** ------------------------------------- handler ------------------------------------- */
	function changePassword(){
		panel_changePassword.getForm().reset();
		window_changePassword.show();
	}
	
	function logout(){
		message.confirm("是否确定注销用户？", function(){
			window.location.href = basePath + "resource/user/logout";
		});
	}
	
	function changePasswordHandler(){
		if (!Ext.getCmp("password").isValid()) {
			message.error(Ext.getCmp("password").invalidText);
		} else if (!Ext.getCmp("newPassword_1").isValid()) {
			message.error(Ext.getCmp("newPassword_1").invalidText);
		} else if (!Ext.getCmp("newPassword_2").isValid()) {
			message.error(Ext.getCmp("newPassword_2").invalidText);
		} else {
			$.post(basePath + "resource/user/changePassword", {
				password: $.md5(Ext.getCmp("password").getValue()),
				newPassword: $.md5(Ext.getCmp("newPassword_1").getValue())
			}, function(data){
				if (data.success) {
					window_changePassword.hide();
					message.info(data.message, function(){
						window.location.href = basePath + "resource/user/logout";
					});
				} else {
					message.error(data.message);
				}
			});
		}
	}
	
	function validatorPasswordRepeatHandler(){
		var newPassword_1 = Ext.getCmp("newPassword_1");
		var newPassword_2 = Ext.getCmp("newPassword_2");
    	if (newPassword_2.getValue().length > 0 && newPassword_1.getValue() != newPassword_2.getValue()) {
    		newPassword_2.invalidText = "两次输入的密码不相同！";
			return newPassword_2.invalidText;
		}
    	return true;
    }
	
	// 校验当前用户密码是否为初始密码
	$.post(basePath + "resource/user/checkPassword", function(data){
		if (data.success) {
			Ext.create("widget.uxNotification", {
				title: "提示",
				width: 280,
				height: 200,
				position: "br",
				stickOnClick: false,
				iconCls: "ux-notification-icon-information",
				autoCloseDelay: 10000,
				html: data.message
			}).show();
		}
	});
});

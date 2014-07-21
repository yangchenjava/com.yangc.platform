Ext.define("MainFrame", {
    extend: "Ext.data.Model",
    fields: [
		{name: "id",   	       type: "int"},
		{name: "menuName",     type: "string"},
		{name: "menuAlias",    type: "string"},
		{name: "menuUrl",  	   type: "string"},
		{name: "childRenMenu", type: "object"}
    ]
});

Ext.onReady(function(){
	/** ------------------------------------- store ------------------------------------- */
	Ext.create("Ext.data.Store", {
		model: "MainFrame",
		proxy: {
			type: "ajax",
			actionMethods: {
				create: "POST", read: "POST", update: "POST", destroy: "POST"
			},
			url: basePath + "resource/menu/showMainFrame",
			extraParams: {"parentMenuId": index.parentMenuId}
		},
		autoLoad: true,
		listeners: {
    		load: function(thiz, records, successful, eOpts){
				for (var i = 0, recordsLen = records.length; i < recordsLen; i++) {
					var html = "";
					var childRenMenu = records[i].get("childRenMenu");
					for (var j = 0, childRenMenuLen = childRenMenu.length; j < childRenMenuLen; j++) {
						var id = childRenMenu[j].id;
						var title = childRenMenu[j].menuName;
						var menuAlias = childRenMenu[j].menuAlias;
						if (menuAlias) {
							index.menuAliasMap[menuAlias] = childRenMenu[j];
						}
						var url = basePath + childRenMenu[j].menuUrl;
						html += "<div class='menu' onclick='addTab(\"" + id + "\", \"" + title + "\", \"" + url + "\")'>" + title + "</div>";
					}
					left.add({
						title: records[i].get("menuName"),
						html: html
					});
				}
    		}
    	}
	});
	
	/** ------------------------------------- view ------------------------------------- */
	var left = Ext.create("Ext.panel.Panel", {
		title: "你好，" + index.personName,
		region: "west",
		layout: "accordion",
		width: "20%",
		minWidth: 200,
		collapsible: true,
		split: true,
		items: [],
		listeners: {
			resize: function(thiz, width, height, oldWidth, oldHeight, eOpts){
				doRefresh();
			},
			collapse: function(p, eOpts){
				doRefresh();
			},
			expand: function(p, eOpts){
				doRefresh();
			}
		}
	});
	
	var right = Ext.create("Ext.tab.Panel", {
		region: "center",
		layout: "anchor",
		xtype: "tabpanel",
		minTabWidth: 80,
		plain: true,
		cls: "ui-tab-bar",
		plugins: [
			Ext.create("Ext.ux.TabReorderer"),
			Ext.create("Ext.ux.TabCloseMenu", {
				closeTabText: "关闭当前",
				closeOthersTabsText: "关闭其他",
				closeAllTabsText: "关闭所有"
			})
		],
		items: []
	});

    Ext.create("Ext.panel.Panel", {
    	renderTo: "main_" + index.parentMenuId,
		layout: "border",
		border: 0,
		width: "100%",
		height: document.documentElement.clientHeight - 92,
        items: [left, right]
    });
    
    /** ------------------------------------- handler ------------------------------------- */
    function doRefresh(){
    	var tabs = right.items.items;
		Ext.Array.each(tabs, function(tab){
			tab.loader.load();
		});
    }
    
    getMainTabWidth = function(){
    	return right.getWidth();
    };
    
    addTab = function(id, title, url){
		if (!right.queryById(id)) {
			if (right.items.length == 5) {
				right.remove(right.items.items[4]);
			}
			right.add({
				id: id,
				title: title,
				closable: true,
				loader: {
					url: url,
					autoLoad: true,
					scripts: true
				}
			});
		}
		right.setActiveTab(id);
	};
	
	addTabByMenuAlias = function(menuAlias){
		var menu = index.menuAliasMap[menuAlias];
		if (menu) {
			addTab("" + menu.id, menu.menuName, basePath + menu.menuUrl);
		}
	};
	
	removeCurrentTab = function(){
		var currentTab = right.getActiveTab();
		if (currentTab) {
			right.remove(currentTab);
		}
	};
	
	removeTabByMenuAlias = function(menuAlias){
		var menu = index.menuAliasMap[menuAlias];
		if (menu) {
			var tab = right.queryById("" + menu.id);
			if (tab) {
				right.remove(tab);
			}
		}
	};
});

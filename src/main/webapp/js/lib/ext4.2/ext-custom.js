var message = {};

// 消息
message.info = function(msg, resultHandler){
	if (window.self != window.top) {
		window.top.message.info(msg, resultHandler);
	} else {
		Ext.MessageBox.show({
			title: "提示消息",
			width: 300,
			msg: msg,
			icon: Ext.MessageBox.INFO,
			buttons: Ext.MessageBox.OK,
			fn: resultHandler
		});
	}
};

// 警告
message.warn = function(msg, resultHandler){
	if (window.self != window.top) {
		window.top.message.warn(msg, resultHandler);
	} else {
		Ext.MessageBox.show({
			title: "危险警告",
			width: 300,
			msg: msg,
			icon: Ext.MessageBox.WARNING,
			buttons: Ext.MessageBox.OK,
			fn: resultHandler
		});
	}
};

// 错误
message.error = function(msg, resultHandler){
	if (window.self != window.top) {
		window.top.message.error(msg, resultHandler);
	} else {
		Ext.MessageBox.show({
			title: "操作错误",
			width: 300,
			msg: msg,
			icon: Ext.MessageBox.ERROR,
			buttons: Ext.MessageBox.OK,
			fn: resultHandler
		});
	}
};

// 对话框
message.confirm = function(msg, yesHandler, noHandler){
	if (window.self != window.top) {
		window.top.message.confirm(msg, yesHandler, noHandler);
	} else {
		Ext.MessageBox.show({
			title: "操作确认",
			width: 300,
			msg: msg,
			icon: Ext.MessageBox.QUESTION,
			buttons: Ext.MessageBox.YESNO,
			fn: function(val){
				if (val == "yes" && typeof(yesHandler) == "function") {
					yesHandler();
				} else if (val == "no" && typeof(noHandler) == "function") {
					noHandler();
				}
			}
		});
	}
};

// 进度
message.progress = function(msg){
	if (window.self != window.top) {
		window.top.message.progress(msg);
	} else {
		Ext.MessageBox.show({
			width: 300,
			msg: msg,
			progress: true,
			progressText: "请稍等...",
			wait: true,
			waitConfig: {interval: 200}
		});
	}
};
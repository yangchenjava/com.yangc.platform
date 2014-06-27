Ext.Loader.setConfig({enabled: true});
Ext.Loader.setPath("Ext.ux", basePath + "js/lib/ext4.2/ux");
Ext.require(["*"]);

Ext.onReady(function(){
	/** ------------------------------------- view ------------------------------------- */
	var panel = Ext.create("Ext.form.Panel", {
        bodyPadding: 30,
        bodyBorder: false,
        frame: false,
		header: false,
        fieldDefaults: {
            labelAlign: "right",
            labelWidth: 80,
            labelStyle: "margin-top: 6px;",
            anchor: "100%"
        },
        items: [
			{id: "username", xtype: "textfield", minWidth: 300, maxWidth: 300, minHeight: 28, maxHeight: 28, fieldLabel: "用户名", allowBlank: false, invalidText: "请输入用户名！", enableKeyEvents: true, listeners: {keyup: keyupHandler}},
			{id: "password", xtype: "textfield", minWidth: 300, maxWidth: 300, minHeight: 28, maxHeight: 28, fieldLabel: "密&nbsp;&nbsp;&nbsp;码", allowBlank: false, invalidText: "请输入密码！", inputType:"password", enableKeyEvents: true, listeners: {keyup: keyupHandler}},
			{id: "captcha", xtype: "container", layout:"column", hidden: true, items: [
                {xtype: "container", columnWidth:.5, layout: "anchor", items: [
                    {id: "captcha_code", xtype: "textfield", minWidth: 190, maxWidth: 190, minHeight: 28, maxHeight: 28, fieldLabel: "验证码", allowBlank: false, invalidText: "请输入验证码！", enableKeyEvents: true, listeners: {keyup: keyupHandler}}
                ]},
                {xtype: "container", columnWidth:.5, layout: "anchor", items: [
                    {id: "captcha_image", xtype: "image", margin: "0 0 0 36", src: "", style: "cursor: pointer;"}
                ]}
            ]},
			{id: "remember", xtype: "checkbox", boxLabel: "记住我", padding: "0 0 0 39"}
		]
	});
    Ext.create("Ext.window.Window", {
		title: "xxx管理系统",
		layout: "fit",
		width: 400,
		border: false,
		closable: false,
		draggable: false,
		plain: true,
		resizable: false,
		items: [panel],
		buttonAlign: "right",
        buttons: [
            {text: "登录", handler: login}
        ]
	}).show();
    
    /** ------------------------------------- handler ------------------------------------- */
    var COOKIE_USERNAME = "app_yangc_username";
	var COOKIE_PASSWORD = "app_yangc_password";
	
	function init(){
		var username = $.cookie(COOKIE_USERNAME);
		var password = $.cookie(COOKIE_PASSWORD);
		if (username && password) {
			Ext.getCmp("username").setValue(username);
			Ext.getCmp("password").setValue(password);
			Ext.getCmp("remember").setValue(true);
		}
		
		Ext.getCmp("username").focus();
		
		if (needCaptcha) {
			refreshCaptcha();
			Ext.getCmp("captcha").show();
		}
		Ext.get("captcha_image").on("click", refreshCaptcha);
	}
	init();
	
	function refreshCaptcha(){
		Ext.get("captcha_image").dom.src = basePath + "resource/user/captcha?q=" + Math.random();
	}
	
	function keyupHandler(thiz, e, eOpts){
		if (e.keyCode == 13) {
			if (thiz.id == "username") {
				Ext.getCmp("password").focus();
			} else if (thiz.id == "password" && Ext.getCmp("captcha").isHidden()) {
				login();
			} else if (thiz.id == "password" && !Ext.getCmp("captcha").isHidden()) {
				Ext.getCmp("captcha_code").focus();
			} else {
				login();
			}
		}
	}

	function login(){
		if (!Ext.getCmp("username").isValid()) {
			message.error(Ext.getCmp("username").invalidText);
		} else if (!Ext.getCmp("password").isValid()) {
			message.error(Ext.getCmp("password").invalidText);
		} else if (!Ext.getCmp("captcha").isHidden() && !Ext.getCmp("captcha_code").isValid()) {
			message.error(Ext.getCmp("captcha_code").invalidText);
		} else {
			var username = Ext.getCmp("username").getValue();
			var password = Ext.getCmp("password").getValue() == $.cookie(COOKIE_PASSWORD) ? Ext.getCmp("password").getValue() : $.md5(Ext.getCmp("password").getValue());
			var captcha_code = Ext.getCmp("captcha_code").getValue();
			// 登录
			$.post(basePath + "resource/user/login?captcha=" + encodeURIComponent(captcha_code), {
				username: username,
				password: password
			}, function(data){
				if (data.success) {
					// 保存cookie
					if (Ext.getCmp("remember").getValue()) {
						$.cookie(COOKIE_USERNAME, username, {expires: 5, path: "/"});
						$.cookie(COOKIE_PASSWORD, password, {expires: 5, path: "/"});
					}
					window.location.href = basePath + data.message;
				} else {
					if (data.other) {
						refreshCaptcha();
						Ext.getCmp("captcha").show();
					}
					message.error(data.message);
				}
			});
		}
    }
});

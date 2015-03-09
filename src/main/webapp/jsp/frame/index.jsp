<%@ page language="java" pageEncoding="UTF-8"%>
<jsp:directive.include file="/jsp/frame/head.jspf" />
<link rel="stylesheet" type="text/css" href="<%=css_custom%>frame/index.css" />
<script type="text/javascript">
$LAB.runQueue()
.script("<%=basePath%>ueditor/ueditor.config.js")
.script("<%=basePath%>ueditor/ueditor.all.js")
.script("<%=basePath%>ueditor/lang/zh-cn/zh-cn.js")
.script("<%=js_lib%>jquery_plugin/jquery.md5.js")
.script("<%=js_custom%>frame/status_code.js")
.script("<%=js_custom%>frame/permission.js").wait()
.script("<%=js_custom%>frame/index.js").wait(function(){
	index.nickname = "${sessionScope.CURRENT_USER.nickname}";
});
</script>
</head>
<body>
</body>
</html>

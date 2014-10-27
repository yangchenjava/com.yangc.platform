<%@ page language="java" pageEncoding="UTF-8"%>
<jsp:directive.include file="/jsp/frame/head.jspf" />
<link rel="stylesheet" type="text/css" href="<%=css_custom%>frame/index.css" />
<script type="text/javascript" src="<%=basePath%>ueditor/ueditor.config.js"></script>
<script type="text/javascript" src="<%=basePath%>ueditor/ueditor.all.js"></script>
<script type="text/javascript" src="<%=basePath%>ueditor/lang/zh-cn/zh-cn.js"></script>
<script type="text/javascript" src="<%=js_lib%>jquery_plugin/jquery.md5.js"></script>
<script type="text/javascript" src="<%=js_custom%>frame/permission.js"></script>
<script type="text/javascript" src="<%=js_custom%>frame/index.js"></script>
<script type="text/javascript">
index.nickname = "${sessionScope.CURRENT_USER.nickname}";
</script>
</head>
<body>
</body>
</html>

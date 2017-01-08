<%@ page language="java" pageEncoding="UTF-8"%>
<jsp:directive.include file="/jsp/frame/head.jspf" />
<script type="text/javascript">
var needCaptcha = "${sessionScope.NEED_CAPTCHA}";
$LAB.runQueue()
.script("<%=js_lib%>jquery_plugin/jquery.cookie.js")
.script("<%=js_lib%>jquery_plugin/jquery.md5.js")
.script("<%=js_custom%>frame/status_code.js").wait()
.script("<%=js_custom%>login.js").wait();
</script>
</head>
<body>
</body>
</html>

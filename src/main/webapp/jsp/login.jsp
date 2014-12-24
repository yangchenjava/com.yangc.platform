<%@ page language="java" pageEncoding="UTF-8"%>
<jsp:directive.include file="/jsp/frame/head.jspf" />
<script type="text/javascript" src="<%=js_lib%>jquery_plugin/jquery.cookie.js"></script>
<script type="text/javascript" src="<%=js_lib%>jquery_plugin/jquery.md5.js"></script>
<script type="text/javascript" src="<%=js_custom%>frame/status_code.js"></script>
<script type="text/javascript" src="<%=js_custom%>login.js"></script>
<script type="text/javascript">
var needCaptcha = "${sessionScope.NEED_CAPTCHA}";
</script>
</head>
<body>
</body>
</html>

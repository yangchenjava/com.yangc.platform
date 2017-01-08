<%@ page language="java" pageEncoding="UTF-8"%>
<jsp:directive.include file="/jsp/frame/head.jspf" />
</head>
<body>
	<div>
		<form action="<%=basePath%>resource/file/upload" method="post" enctype="multipart/form-data">
			<input type="text" name="test" />
			<input type="file" name="file" />
			<input type="submit" value="Upload It" />
		</form>
	</div>
</body>
</html>

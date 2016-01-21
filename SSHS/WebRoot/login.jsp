<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>   
<html>
<head>
<title>Sign On</title>
</head>
<span>${requestScope.emsg}</span>
<body>
	<form action="userAction!login" method="post">
		<input type="text" name="username">
		<input type="password" name="password">
		<input type="submit" value="登录">
	<form>
</body>
</html>

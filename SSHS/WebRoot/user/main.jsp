<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>shiro</title>
</head>
<body>
<h1>Shiro</h1>
<span>
<shiro:authenticated>
欢迎 <shiro:principal property="nickname"/> 使用我们的系统!
</shiro:authenticated>
<shiro:hasPermission name="/user/res/list">
	<a href="<%=request.getContextPath() %>/user/res/list.jsp" class="admin_link">资源管理</a>
</shiro:hasPermission>
<shiro:hasPermission name="/user/role/list">
	<a href="<%=request.getContextPath() %>/user/role/list.jsp" class="admin_link">角色管理</a>
</shiro:hasPermission>
<shiro:hasPermission name="/user/emp/list">
	<a href="<%=request.getContextPath() %>/user/role/list.jsp" class="admin_link">用户管理</a>
</shiro:hasPermission>
<a href="<%=request.getContextPath() %>/logout">退出系统</a>



</span>
</body>
</html>
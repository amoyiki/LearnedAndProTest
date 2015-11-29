<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    
    <title>显示日志</title>
    
	<link rel="stylesheet" type="text/css" href="./themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="./themes/icon.css">
	<script type="text/javascript" src="./js/jquery.min.js"></script>
	<script type="text/javascript" src="./js/jquery.easyui.min.js"></script>

  </head>
  
  <body>
    <div align="center">
    	<div style="margin:20px 0;"></div>
		<div class="easyui-panel" title="用户登录" style="width:502px;height:300px;padding:30px 60px">
    	<div align="center" style="color:red">
    	${msg}    	
    	</div>
    	<form method="post" name="form1" action="user!login.action">
    	<div style="margin-bottom:20px">
			<div>用户名:</div>
			<input name="username" class="easyui-textbox" data-options="prompt:'请输入用户名'" style="width:90%;height:32px">
		</div>
		<div style="margin-bottom:20px">
			<div>密码:</div>
			<input name="password" type="password" class="easyui-textbox" style="width:90%;height:32px">
		</div>
		<div>
			<a href="#" onClick="form1.submit();" class="easyui-linkbutton" iconCls="icon-lock" style="width:45%;height:32px">登录</a>
			<a href="register.jsp" class="easyui-linkbutton" iconCls="icon-man" style="width:45%;height:32px">注册新用户</a>
		</div>
    	</form>
    	</div>    	
    </div>
  </body>
</html>
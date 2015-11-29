<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    
    <title>新用户注册</title>
    
	<link rel="stylesheet" type="text/css" href="./themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="./themes/icon.css">
	<script type="text/javascript" src="./js/jquery.min.js"></script>
	<script type="text/javascript" src="./js/jquery.easyui.min.js"></script>


  </head>
  
  <body>
    <div align="center">
    	<div style="margin:20px 0;"></div>
		<div class="easyui-panel" title="注册新用户" style="width:500px;height:400px;padding:30px 60px">
    	<div align="center" style="color:red">
    	${msg}    	
    	</div>
    	<form method="post" name="form1" action="user!register.action">
    	<div style="margin-bottom:20px">
			<div>用户名:</div>
			<input name="username" class="easyui-textbox" data-options="prompt:'请输入用户名'" style="width:90%;height:32px">
		</div>
		<div style="margin-bottom:20px">
			<div>密码:</div>
			<input name="password" type="password" class="easyui-textbox" style="width:90%;height:32px">
		</div>
		<div style="margin-bottom:20px">
			<div>确认密码:</div>
			<input name="confirm" type="password" class="easyui-textbox" style="width:90%;height:32px">
		</div>
		<div>
			<a href="#" onClick="form1.submit();" class="easyui-linkbutton" iconCls="icon-ok" style="width:90%;height:32px">注册</a>
		</div>
    	</form>
    	</div>    	
    </div>
  </body>
</html>

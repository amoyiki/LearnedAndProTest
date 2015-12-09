<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>欢迎使用OA系统</title>
	<link rel="stylesheet" type="text/css" href="../themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="../themes/icon.css">
	<script type="text/javascript" src="../js/jquery.min.js"></script>
	<script type="text/javascript" src="../js/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="../js/easyui-lang-zh_CN.js"></script>
	<!-- 自定义js文件 -->
	<script type="text/javascript" src="../js/main.js"></script>
	
</head>
<body class="easyui-layout">
<!-- egion="north" 指明高度，宽度可以自适应-->
<div region="north" style="height:80px;background-color:#ADEAEA; background-image: url('../themes/logo.png');">
<%@include file="/layout/header.jsp"%>
</div>
 <!-- region="west" 必须指明宽度 -->
<div region="west" title="导航栏" collapsible="false" style="width: 220px;">
<%@include file="/layout/catalog.jsp"%>
</div>
<!-- region="center"宽高由周围模块决定，不用设置 -->
<div region="center" style="background: #EEE;">
	<div class="easyui-tabs" fit="true" border="false" id="tabs">
 		<div  title="首页" data-options="iconCls:'icon-home',closable:true"></div>
	</div>
</div>
 <!-- region="west" 必须指明宽度 -->
<div region="east" title="通用栏" collapsible="true" style="width: 220px;">
<%@include file="/layout/common.jsp"%>
</div>
<!-- region="south" 底部高度要设 -->
<div region="south" style="height:30px;">
<%@include file="/layout/footer.jsp"%>
</div>

</body>
</html>
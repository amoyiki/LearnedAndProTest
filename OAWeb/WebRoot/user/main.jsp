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
<script type="text/javascript">
$(function(){
    $('#mytree').tree({   
        url:'treeAction!treeLoad.action',
        lines: true,
    }); 
});
</script>
</head>
<body class="easyui-layout">
<!-- egion="north" 指明高度，可以自适应-->
<div region="north" style="height:80px;">
<center> <h1>OA办公系统</h1></center>

</div>
 <!-- region="west" 必须指明宽度 -->
<div region="west" title="导航栏" collapsible="false" style="width: 220px;">
<ul id="mytree"></ul>
</div>
<!-- region="center"宽高由周围模块决定，不用设置 -->
<div region="center" style="background: #EEE;">
</div>
<div region="south" style="height:50px;"><center> <h3>页面底部</h3></center></div>

</body>
</html>
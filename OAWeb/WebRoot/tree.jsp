<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'tree.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<link rel="stylesheet" type="text/css" href="themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="themes/icon.css">
	<script type="text/javascript" src="js/jquery.min.js"></script>
	<script type="text/javascript" src="js/jquery.easyui.min.js"></script>
	
<script type="text/javascript">
$(function(){

    $('#mytree').tree({   
        url:'treeAction!treeLoad.action',
        lines: true,
        onLoadSuccess : function(node, data) {
        	var t = $(this); 
        	if (data) {  
                $(data).each(function(index, d) {  
                    if (this.state == 'closed') {  
                        t.tree('expandAll');  
                    }  
                });  
            }  
        }
    }); 
});
</script>	
	
  </head>
  
  <body>
<ul id="mytree"></ul>
  </body>
</html>

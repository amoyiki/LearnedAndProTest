$(function(){
	//创建tree
    $('#mytree').tree({   
        url:'treeAction!treeLoad.action',
        lines: true,
        animate: true,
        onClick: function (node) {
            if (node.attributes != null) {
                Open(node.text, node.attributes.url);
            }
        }
    }); 
    //在右边center区域打开菜单，新增tab
    function Open(text, url) {
        if ($("#tabs").tabs('exists', text)) {
            $('#tabs').tabs('select', text);
        } else {
            $('#tabs').tabs('add', {
                title : text,
                closable : true,
                content : text
            });
        }
    }    
    
    
    
});
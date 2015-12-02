package org.amoyiki.oa.actions;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.amoyiki.oa.dao.TreeDao;
import org.amoyiki.oa.entities.TreeNode;
import org.amoyiki.oa.utils.JacksonUtil;
import org.amoyiki.oa.viewModel.EasyUITree;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;

/**
 * 
 * <p>Title:TreeAction</p>
 * <p>Description: 树形菜单从数据库转换成Json</p>
 * @author amoyiki
 * @date 2015年12月2日 下午11:39:58
 *
 */
public class TreeAction extends ActionSupport{
	
	
	private static final long serialVersionUID = 1L;
	private String id="";//tree的组件id
	private TreeDao treeDao;

	/**
	 * 从数据库中取得tree节点值转换成Json返回前台js
	 */
	public void treeLoad(){
		//防止Json中文传值乱码
		HttpServletResponse httpServletResponse = ServletActionContext.getResponse();
		httpServletResponse.setContentType("text/plain");
		httpServletResponse.setCharacterEncoding("utf-8");
		//用于前台显示的tree的属性、比如id、state、text、checked等等  
        List<EasyUITree> eList = new ArrayList<EasyUITree>();  
		List<TreeNode> treeNodes = treeDao.findAllNodeById(id);
		if (treeNodes.size() != 0) {
			for (TreeNode t : treeNodes) {
				EasyUITree e = new EasyUITree();
				System.out.println(t.getText());
				e.setId(t.getId());
				e.setText(t.getText());
				Map<String, Object> attributes = new HashMap<String, Object>();
				attributes.put("url", t.getUrl());
				e.setAttributes(attributes);
				int count = treeDao.countChildrens(t.getId());
				if (count > 0) {
					e.setState("closed");
				}
				eList.add(e);
			}
		}
		id = null;
		try {
			PrintWriter pw = ServletActionContext.getResponse().getWriter();
			pw.print(JacksonUtil.toJson(eList));  
            pw.flush();  
            pw.close();  
		} catch (IOException e) {
			e.printStackTrace();
		}  
		
	}	
	
	
	
	public TreeDao getTreeDao() {
		return treeDao;
	}


	public void setTreeDao(TreeDao treeDao) {
		this.treeDao = treeDao;
	}


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}


}

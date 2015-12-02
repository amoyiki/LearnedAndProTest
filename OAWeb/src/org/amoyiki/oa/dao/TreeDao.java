package org.amoyiki.oa.dao;

import java.util.List;
import org.amoyiki.oa.entities.TreeNode;

public interface TreeDao {

	public Integer countChildrens(String id); 
	public List<TreeNode> findAllNodeById(String pid);

}

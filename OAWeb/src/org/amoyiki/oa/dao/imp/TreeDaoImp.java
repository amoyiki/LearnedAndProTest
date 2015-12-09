package org.amoyiki.oa.dao.imp;

import java.util.List;

import org.amoyiki.oa.dao.TreeDao;
import org.amoyiki.oa.entities.TreeNode;
import org.hibernate.Query;
import org.springframework.orm.hibernate4.support.HibernateDaoSupport;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TreeDaoImp extends HibernateDaoSupport implements TreeDao{


	@Override
	@Transactional
	public List<TreeNode> findAllNodeById(String pid) {
		Query query = null;
		if(pid == null || "".equals(pid)){
			query = this.getSessionFactory().getCurrentSession().
					createQuery("select t from TreeNode t where t.pid is null");
		}else {
			
			query = this.getSessionFactory().getCurrentSession().
					createQuery("select t from TreeNode t where t.pid=:pid");
			query.setParameter("pid", pid);
		}
		
		List<TreeNode> list = query.list();
		if (list == null || list.size() == 0)
			return null;
		return list;
		
	}

	@Override
	@Transactional
	public Integer countChildrens(String id) {
		Query query = this.getSessionFactory().getCurrentSession().
				createQuery("select count(*)  from TreeNode t where t.pid=:id");
		query.setParameter("id", id);
		return Integer.parseInt( String.valueOf(query.list().get(0).toString()));
		
	}

}

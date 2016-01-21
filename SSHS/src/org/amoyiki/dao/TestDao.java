package org.amoyiki.dao;

import java.util.List;

import org.amoyiki.entity.Resource;
import org.amoyiki.entity.Role;
import org.amoyiki.entity.User;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("testDao")  
public class TestDao {

	@Autowired
	private SessionFactory sessionFactory;
	
	public User findByName(String username) {
		Query query = this.sessionFactory.getCurrentSession().
				createQuery("select user from User user where user.username=:username");
		query.setParameter("username", username);
		
		List<User> list = query.list();
		if (list == null || list.size() == 0)
			return null;
		return list.get(0);
	}
	public void add(User user){
		this.sessionFactory.getCurrentSession().save(user);
	}
	public List<String> listRoleSnByUser(int uid){
		String hql = "select r.sn from UserRole ur,Role r,User u where u.id=ur.userId and r.id=ur.roleId and u.id=:uid";
		Query query = this.sessionFactory.getCurrentSession().
				createQuery(hql);
		query.setParameter("uid", uid);
		List<String> list = query.list();
		if (list == null || list.size() == 0)
			return null;
		return list;
		
	}
	public List<Role> listUserRole(int uid) {
		String hql = "select r from UserRole ur,Role r,User u where u.id=ur.userId and r.id=ur.roleId and u.id=:uid";
		Query query = this.sessionFactory.getCurrentSession().
				createQuery(hql);
		query.setParameter("uid", uid);
		List<Role> list = query.list();
		if (list == null || list.size() == 0)
			return null;
		return list;
	}
	public List<Resource> listAllResource(int uid) {
		String hql = "select res from User u,Resource res,UserRole ur,RoleResource rr where " +
				"u.id=ur.userId and ur.roleId=rr.roleId  and rr.resId=res.id and u.id=:uid";
		Query query = this.sessionFactory.getCurrentSession().
				createQuery(hql);
		query.setParameter("uid", uid);
		List<Resource> list = query.list();
		if (list == null || list.size() == 0)
			return null;
		return list;
	}
	
}

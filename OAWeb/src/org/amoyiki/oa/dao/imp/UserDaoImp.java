package org.amoyiki.oa.dao.imp;

import java.util.List;

import org.amoyiki.oa.dao.UserDao;
import org.amoyiki.oa.entities.User;
import org.hibernate.Query;
import org.springframework.orm.hibernate4.support.HibernateDaoSupport;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class UserDaoImp extends HibernateDaoSupport implements UserDao{

	@Override
	@Transactional
	public Integer save(User user) {

		return (Integer)this.getSessionFactory().getCurrentSession().save(user);
	}

	@Override
	@Transactional
	public User findByName(String username) {
		Query query = this.getSessionFactory().getCurrentSession().
				createQuery("select user from User user where user.username=:username");
		query.setParameter("username", username);
		
		List<User> list = query.list();
		if (list == null || list.size() == 0)
			return null;
		return list.get(0);
	}

}

package org.amoyiki.dao;

import java.util.List;

import javax.annotation.Resource;

import org.amoyiki.entity.User;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;


@Repository("userDao")
public class UserDaoImpl  implements UserDao{

    /**
     * 使用@Autowired注解将sessionFactory注入到UserDaoImpl中
     */
	@Resource(name="sessionFactory")
    private SessionFactory sessionFactory;
    
    public Session getSession() {
    	/* 在hibernate4中不用getCurrentSession() */
        return sessionFactory.openSession();
    }
	@Override
	public User query(String username, String password) {
		Query query = this.getSession().
				createQuery("select user from User user where user.username=:username");
		query.setParameter("username", username);
		List<User> list = query.list();
		if (list == null || list.size() == 0)
			return null;
		return list.get(0);
	}

}

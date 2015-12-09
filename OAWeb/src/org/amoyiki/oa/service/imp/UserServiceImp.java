package org.amoyiki.oa.service.imp;



import org.amoyiki.oa.dao.UserDao;
import org.amoyiki.oa.entities.User;
import org.amoyiki.oa.service.UserService;
import org.springframework.stereotype.Service;
import com.opensymphony.xwork2.inject.Inject;

@Service
public class UserServiceImp implements UserService {

	@Inject
	private UserDao userDao;
	
	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	@Override
	public boolean regisiter(User user) {

		if (userDao.findByName(user.getUsername()) != null) {
			return false;
		}
		userDao.save(user);
		return true;
	}

	@Override
	public User login(String username, String password) {
		User user = userDao.findByName(username);
		if(user == null){
			return null;
		}
		if(!user.getPassword().equals(password)){
			return null;
		}
		return user;
	}

}

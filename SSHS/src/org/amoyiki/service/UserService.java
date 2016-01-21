package org.amoyiki.service;

import java.util.List;

import org.amoyiki.dao.IRoleDao;
import org.amoyiki.dao.IUserDao;
import org.amoyiki.dao.TestDao;
import org.amoyiki.entity.Resource;
import org.amoyiki.entity.Role;
import org.amoyiki.entity.User;
import org.amoyiki.util.ShiroKit;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("userService")
public class UserService implements IUserService {

	@Autowired
	private IUserDao userDao;
	@Autowired
	private TestDao testDao;
	@Autowired
	private IRoleDao roleDao;
	
	public void add(User user) {
		if(ShiroKit.isEmpty(user.getUsername())||ShiroKit.isEmpty(user.getPassword())) {
			throw new RuntimeException("username or password is error！");
		}
		user.setPassword(ShiroKit.md5(user.getPassword(), user.getUsername()));
		testDao.add(user);
	}

	public void delete(int id) {
		userDao.delete(id);
	}

	public void update(User user,List<Integer> rids) {
		roleDao.deleteUserRoles(user.getId());
		for(int rid:rids) {
			roleDao.addUserRole(user.getId(), rid);
		}
		userDao.update(user);
	}
	
	public void update(User user) {
		userDao.update(user);
	}

	public User load(int id) {
		return userDao.load(id);
	}

	public User loadByUsername(String username) {
		return userDao.loadByUsername(username);
	}

	public User login(String username, String password) {
		System.out.println("+++++++++");
		User u = testDao.findByName(username);
		if(u==null) throw new UnknownAccountException("用户名或密码错误！");
		if(!u.getPassword().equals(ShiroKit.md5(password, username)))
			throw new IncorrectCredentialsException("用户名或密码错误！");
		if(u.getStatus()==0) throw new LockedAccountException("用户已经被锁定");
		return u;
	}

	public List<User> list() {
		return userDao.listUser();
	}

	public List<User> listByRole(int id) {
		return userDao.listByRole(id);
	}

	public List<Resource> listAllResource(int uid) {
		return testDao.listAllResource(uid);
	}

	public void add(User user, List<Integer> rids) {
		this.add(user);
		for(int rid:rids) {
			roleDao.addUserRole(user.getId(), rid);
		}
	}

	@Override
	public List<String> listRoleSnByUser(int uid) {
		return testDao.listRoleSnByUser(uid);
	}

	@Override
	public List<Role> listUserRole(int uid) {
		return userDao.listUserRole(uid);
	}

}

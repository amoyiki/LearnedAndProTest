package org.amoyiki.dao;

import java.util.List;

import org.amoyiki.entity.Resource;
import org.amoyiki.entity.Role;
import org.amoyiki.entity.User;
import org.konghao.basic.dao.IBaseDao;
public interface IUserDao extends IBaseDao<User> {
	public List<User> listUser();
	public void addUser(User user); 
	public User loadByUsername(String username);
	
	public List<User> listByRole(int id);
	
	public List<Resource> listAllResource(int uid);
	
	public List<String> listRoleSnByUser(int uid);
	
	public List<Role> listUserRole(int uid);
	
}

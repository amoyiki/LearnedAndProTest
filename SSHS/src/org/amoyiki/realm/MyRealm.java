package org.amoyiki.realm;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.amoyiki.entity.Resource;
import org.amoyiki.entity.User;
import org.amoyiki.service.UserService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;

public class MyRealm extends AuthorizingRealm{

	@Autowired
	@Lazy(value=false)
	@Qualifier(value="userService")
	private UserService userService;
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection arg0) {
		User user = ((User)arg0.getPrimaryPrincipal());
		int uid = user.getId();
		System.out.println("进入授权====");
		List<String> roles = userService.listRoleSnByUser(uid);
		List<Resource> reses = userService.listAllResource(uid);
		List<String> permissions = new ArrayList<String>();
		for(Resource r:reses) {
			permissions.add(r.getUrl());
		}
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		info.setRoles(new HashSet<String>(roles));
		info.setStringPermissions(new HashSet<String>(permissions));
		
		return info;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken arg0) throws AuthenticationException {
		//验证
		System.out.println("进入验证");
		UsernamePasswordToken token = (UsernamePasswordToken)arg0;
		String username = token.getPrincipal().toString();
		String password = new String((char[])token.getCredentials());
		User u = userService.login(username, password);
		SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(u,u.getPassword(), this.getName());
		info.setCredentialsSalt(ByteSource.Util.bytes(username));
		return info;

		
	}

}

package org.amoyiki.service;

import java.util.List;

import org.amoyiki.dao.IResourceDao;
import org.amoyiki.entity.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("resourceService")
public class ResourceService implements IResourceService {
	
	@Autowired
	private IResourceDao resourceDao;
	
	public void add(Resource res) {
		resourceDao.add(res);
	}

	public void update(Resource res) {
		resourceDao.update(res);
	}

	public void delete(int id) {
		resourceDao.delete(id);
	}

	public Resource load(int id) {
		return resourceDao.load(id);
	}

	public List<Resource> listResource() {
		return resourceDao.listResource();
	}
}

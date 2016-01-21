package org.amoyiki.dao;

import java.util.List;

import org.amoyiki.entity.Resource;
import org.konghao.basic.dao.IBaseDao;

public interface IResourceDao extends IBaseDao<Resource>{
	public List<Resource> listResource();
}

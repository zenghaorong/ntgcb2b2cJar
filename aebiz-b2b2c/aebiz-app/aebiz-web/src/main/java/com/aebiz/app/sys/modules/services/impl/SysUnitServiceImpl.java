package com.aebiz.app.sys.modules.services.impl;

import javax.annotation.Resource;

import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.lang.Strings;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aebiz.app.sys.modules.models.Sys_unit;
import com.aebiz.app.sys.modules.services.SysUnitService;
import com.aebiz.baseframework.base.service.BaseServiceImpl;

/**
 * Created by wizzer on 2016/12/22.
 */
@Service
public class SysUnitServiceImpl extends BaseServiceImpl<Sys_unit> implements SysUnitService {
	@Resource(name = "nutDao", type = Dao.class)
	public void init(Dao dao) {
		super.setDao(dao);
	}

	/**
	 * 新增单位
	 *
	 * @param unit
	 * @param pid
	 */
	@Transactional
	public void save(Sys_unit unit, String pid) {
		String path = "";
		if (!Strings.isEmpty(pid)) {
			Sys_unit pp = this.fetch(pid);
			path = pp.getPath();
		}
		unit.setPath(getSubPath("Sys_unit", "path", path));
		unit.setParentId(pid);
		dao().insert(unit);
		if (!Strings.isEmpty(pid)) {
			this.update(Chain.make("hasChildren", true), Cnd.where("id", "=", pid));
		}
	}

	/**
	 * 级联删除单位
	 *
	 * @param unit
	 */
	@Transactional
	public void deleteAndChild(Sys_unit unit) {
		dao().execute(Sqls.create("delete from Sys_unit where path like @path").setParam("path", unit.getPath() + "%"));
		if (!Strings.isEmpty(unit.getParentId())) {
			int count = count(Cnd.where("parentId", "=", unit.getParentId()));
			if (count < 1) {
				dao().execute(Sqls.create("update Sys_unit set hasChildren=0 where id=@pid").setParam("pid",
						unit.getParentId()));
			}
		}
	}

}

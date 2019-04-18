package com.aebiz.app.web.modules.controllers.platform.sys;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.aebiz.baseframework.page.datatable.DataTable;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.dao.Cnd;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.quartz.JobKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.aebiz.app.sys.modules.models.Sys_task;
import com.aebiz.app.sys.modules.services.SysTaskService;
import com.aebiz.app.web.commons.log.annotation.SLog;
import com.aebiz.app.web.commons.quartz.QuartzJob;
import com.aebiz.app.web.commons.quartz.QuartzManager;
import com.aebiz.baseframework.base.Result;
import com.aebiz.baseframework.view.annotation.SJson;
import com.aebiz.commons.utils.StringUtil;

/**
 * Created by 王怀先 on 2017/2/3
 */
@Controller
@RequestMapping("/platform/sys/task")
public class SysTaskController {
	private static final Log log = Logs.get();
	@Autowired
	private SysTaskService taskService;

	@Autowired
	private QuartzManager quartzManager;

	@RequestMapping("")
	@RequiresPermissions("sys.manager.task")
	public String index() {
		return "pages/platform/sys/task/index";
	}

	@RequestMapping("/add")
	@RequiresPermissions("sys.manager.task")
	public String add() {
		return "pages/platform/sys/task/add";
	}

	@RequestMapping("/data")
	@SJson("full")
	@RequiresPermissions("sys.manager.task")
	public Object data(DataTable dataTable) {
		Cnd cnd = Cnd.NEW();
		return taskService.data(dataTable.getLength(), dataTable.getStart(), dataTable.getDraw(), dataTable.getOrders(), dataTable.getColumns(), cnd, null);
	}

	/*
	 * 添加任务
	 */
	@RequestMapping("/addDo")
	@SJson
	@SLog(description = "新增定时任务")
	@RequiresPermissions("sys.manager.task.add")
	public Object addDo(Sys_task task, HttpServletRequest req) {
		try {
			if (null != task.getStatus()) {
				task.setStatus(1);
			} else {
				task.setStatus(0);
			}
			try {
				task.setExeResult("暂未执行");
				Sys_task sys_task = taskService.insert(task);
				if (0 == task.getStatus()) {
					add(sys_task);
				}
			} catch (Exception e) {
				log.error(e.getMessage());
				return Result.error("globals.result.error");
			}
			return Result.success("globals.result.success");
		} catch (Exception e) {
			return Result.error("globals.result.error");
		}
	}

	@RequestMapping("/edit/{id}")
	@RequiresPermissions("sys.manager.task")
	public String edit(@PathVariable String id, HttpServletRequest req) {
		req.setAttribute("obj", taskService.fetch(id));
		return "pages/platform/sys/task/edit";
	}

	@RequestMapping("/editDo")
	@SJson
	@SLog(description = "编辑定时任务")
	@RequiresPermissions("sys.manager.task.edit")
	public Object editDo(Sys_task sysTask, HttpServletRequest req) {
		try {
			try {
				boolean isExist = quartzManager.exist(new JobKey(sysTask.getId(), sysTask.getId()));
				if (null != sysTask.getStatus()) {
					sysTask.setStatus(1);
				} else {
					sysTask.setStatus(0);
				}
				if (isExist) {
					delete(sysTask);
				}
				if (0 == sysTask.getStatus()) {
					add(sysTask);
				}
			} catch (Exception e) {
				log.error(e.getMessage());
				taskService.update(org.nutz.dao.Chain.make("status", 1), Cnd.where("id", "=", sysTask.getId()));
				return Result.error("globals.result.error");
			}
			sysTask.setOpBy(StringUtil.getUid());
			sysTask.setOpAt((int) (System.currentTimeMillis() / 1000));
			taskService.updateIgnoreNull(sysTask);
			return Result.success("globals.result.success");
		} catch (Exception e) {
			return Result.error("globals.result.error");
		}
	}

	/**
	 * 删除一个定时任务
	 *
	 * @param sysTask
	 */
	public void delete(Sys_task sysTask) {
		QuartzJob qj = new QuartzJob();
		qj.setJobName(sysTask.getId());
		qj.setJobGroup(sysTask.getId());
		quartzManager.delete(qj);
	}

	/**
	 * 添加一个定时任务
	 *
	 * @param sysTask
	 */
	public void add(Sys_task sysTask) {
		QuartzJob qj = new QuartzJob();
		qj.setJobName(sysTask.getId());
		qj.setJobGroup(sysTask.getId());
		qj.setClassName(sysTask.getJobClass());
		qj.setCron(sysTask.getCron());
		qj.setComment(sysTask.getNote());
		qj.setDataMap(sysTask.getData());
		quartzManager.add(qj);
	}

	/**
	 * 暂停一个定时任务
	 *
	 * @param sysTask
	 */
	public void pause(Sys_task sysTask) {
		QuartzJob qj = new QuartzJob();
		qj.setJobName(sysTask.getId());
		qj.setJobGroup(sysTask.getId());
		quartzManager.pause(qj);
	}

	/**
	 * 恢复一个定时任务
	 *
	 * @param sysTask
	 */
	public void resume(Sys_task sysTask) {
		QuartzJob qj = new QuartzJob();
		qj.setJobName(sysTask.getId());
		qj.setJobGroup(sysTask.getId());
		quartzManager.resume(qj);
	}

	/**
	 * 删除任务
	 *
	 * @param id
	 * @param ids
	 * @return
	 */
	@RequestMapping({ "/delete/{id}", "/delete" })
	@SJson
	@SLog(description = "删除任务")
	@RequiresPermissions("sys.manager.task.delete")
	public Object delete(@PathVariable(value = "id", required = false) String id,
			@RequestParam(value = "ids", required = false) String[] ids) {
		try {
			if (ids != null && ids.length > 0) {
				List<Sys_task> taskList = taskService.query(Cnd.where("id", "in", ids));
				for (Sys_task sysTask : taskList) {
					try {
						delete(sysTask);
					} catch (Exception e) {
						log.error(e.getMessage());
						return Result.error("globals.result.error");
					}
				}
				String[] ssids = ids[0].split(",");
				for (int i = 0; i < ssids.length; i++) {
					taskService.delete(ssids[i]);
				}
			} else {
				Sys_task sysTask = taskService.fetch(id);
				try {
					boolean isExist = quartzManager.exist(new JobKey(sysTask.getId(), sysTask.getId()));
					if (isExist) {
						// 删除参数错误会抛异常
						delete(sysTask);
					}
					taskService.delete(id);
				} catch (Exception e) {
					log.error(e.getMessage());
					return Result.error("globals.result.error");
				}
			}
			return Result.success("globals.result.success");
		} catch (Exception e) {
			return Result.error("globals.result.error");
		}
	}

	/**
	 * 启用任务
	 *
	 * @param id
	 * @return
	 */
	@RequestMapping("/enable/{id}")
	@SJson
	@RequiresPermissions("sys.manager.task.edit")
	@SLog(description = "启用任务")
	public Object enable(@PathVariable String id) {
		try {
			Sys_task sysTask = taskService.fetch(id);
			try {
				boolean isExist = quartzManager.exist(new JobKey(sysTask.getId(), sysTask.getId()));
				if (!isExist && !"2".equals(sysTask.getStatus().toString())) {
					add(sysTask);
					taskService.update(org.nutz.dao.Chain.make("status", 0), Cnd.where("id", "=", id));
				} else {
					return Result.error("globals.result.error");
				}
			} catch (Exception e) {
				log.error(e.getMessage());
				return Result.error("globals.result.error");
			}
			return Result.success("globals.result.success");
		} catch (Exception e) {
			return Result.error("globals.result.error");
		}
	}

	/**
	 * 禁用
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("/disable/{id}")
	@SJson
	@RequiresPermissions("sys.manager.task.edit")
	@SLog(description = "禁用任务")
	public Object disable(@PathVariable String id) {
		try {
			Sys_task sysTask = taskService.fetch(id);
			try {
				boolean isExist = quartzManager.exist(new JobKey(sysTask.getId(), sysTask.getId()));
				if (isExist) {
					delete(sysTask);
					taskService.update(org.nutz.dao.Chain.make("status", 1), Cnd.where("id", "=", id));
				} else {
					return Result.error("globals.result.error");
				}
			} catch (Exception e) {
				log.error(e.getMessage());
				return Result.error("globals.result.error");
			}
			return Result.success("globals.result.success");
		} catch (Exception e) {
			return Result.error("globals.result.error");
		}
	}

	/*
	 * 新增暂停任务功能
	 */
	@RequestMapping("/suspend/{id}")
	@SJson
	@RequiresPermissions("sys.manager.task.edit")
	@SLog(description = "暂停任务")
	public Object suspend(@PathVariable String id) {
		try {
			Sys_task sysTask = taskService.fetch(id);
			try {
				boolean isExist = quartzManager.exist(new JobKey(sysTask.getId(), sysTask.getId()));
				if (isExist) {
					pause(sysTask);
					taskService.update(org.nutz.dao.Chain.make("status", 2), Cnd.where("id", "=", id));
				} else {
					return Result.error("globals.result.error");
				}
			} catch (Exception e) {
				log.error(e.getMessage());
				return Result.error("globals.result.error");
			}
			return Result.success("globals.result.success");
		} catch (Exception e) {
			return Result.error("globals.result.error");
		}
	}

	/**
	 * 新增恢复任务功能
	 * 
	 * @param
	 * @return
	 */
	@RequestMapping("/restore/{id}")
	@SJson
	@RequiresPermissions("sys.manager.task.edit")
	@SLog(description = "恢复任务")
	public Object restore(@PathVariable String id) {
		try {
			Sys_task sysTask = taskService.fetch(id);
			try {
				boolean isExist = quartzManager.exist(new JobKey(sysTask.getId(), sysTask.getId()));
				if (isExist && sysTask.getStatus().equals(2)) {
					resume(sysTask);
					taskService.update(org.nutz.dao.Chain.make("status", 0), Cnd.where("id", "=", id));
				} else {
					return Result.error("globals.result.error");
				}
			} catch (Exception e) {
				log.error(e.getMessage());
				return Result.error("globals.result.error");
			}
			return Result.success("globals.result.success");
		} catch (Exception e) {
			return Result.error("globals.result.error");
		}
	}

}

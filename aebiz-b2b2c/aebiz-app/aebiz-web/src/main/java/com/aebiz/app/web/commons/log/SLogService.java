package com.aebiz.app.web.commons.log;

import com.aebiz.commons.utils.DateUtil;
import com.aebiz.commons.utils.SpringUtil;
import com.aebiz.commons.utils.StringUtil;
import com.aebiz.app.sys.modules.models.Sys_log;
import com.aebiz.app.sys.modules.models.Sys_user;
import com.aebiz.app.sys.modules.services.SysLogService;
import org.apache.shiro.SecurityUtils;
import org.nutz.dao.Dao;
import org.nutz.dao.util.Daos;
import org.nutz.lang.Lang;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * SLog异步插入数据库
 * Created by wizzer on 2016/6/22.
 */
@Service
public class SLogService implements Runnable {

    private static final Log log = Logs.get();

    ExecutorService es;

    LinkedBlockingQueue<Sys_log> queue;

    @Autowired
    protected SysLogService sysLogService;

    /**
     * 按月分表的dao实例
     */
    protected Map<String, Dao> ymDaos = new HashMap<String, Dao>();

    /**
     * 获取按月分表的Dao实例,即当前日期的dao实例
     *
     * @return
     */
    public Dao dao() {
        Calendar cal = Calendar.getInstance();
        String key = String.format("%d%02d", cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1);
        return dao(key);
    }

    /**
     * 获取特定月份的Dao实例
     *
     * @param key
     * @return
     */
    public Dao dao(String key) {
        Dao dao = ymDaos.get(key);
        if (dao == null) {
            synchronized (this) {
                dao = ymDaos.get(key);
                if (dao == null) {
                    dao = Daos.ext(sysLogService.dao(), key);
                    dao.create(Sys_log.class, false);
                    ymDaos.put(key, dao);
                    try {
                        Daos.migration(dao, Sys_log.class, true, false);
                    } catch (Throwable e) {
                    }
                }
            }
        }
        return dao;
    }

    /**
     * 异步
     *
     * @param syslog
     */
    public void async(Sys_log syslog) {
        LinkedBlockingQueue<Sys_log> queue = this.queue;
        if (queue != null)
            try {
                boolean re = queue.offer(syslog, 50, TimeUnit.MILLISECONDS);
                if (!re) {
                    log.info("syslog queue is full, drop it ...");
                }
            } catch (InterruptedException e) {
            }
    }

    /**
     * 同步
     *
     * @param syslog
     */
    public void sync(Sys_log syslog) {
        try {
            dao().insert(syslog);
        } catch (Throwable e) {
            log.info("insert syslog sync fail", e);
        }
    }

    public void run() {
        while (true) {
            LinkedBlockingQueue<Sys_log> queue = this.queue;
            if (queue == null)
                break;
            try {
                Sys_log sysLog = queue.poll(1, TimeUnit.SECONDS);
                if (sysLog != null) {
                    sync(sysLog);
                }
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    @PostConstruct
    public void init() {
        queue = new LinkedBlockingQueue<>();
        int c = Runtime.getRuntime().availableProcessors();
        es = Executors.newFixedThreadPool(c);
        for (int i = 0; i < c; i++) {
            es.submit(this);
        }
    }

    @PreDestroy
    public void close() throws InterruptedException {
        queue = null; // 触发关闭
        if (es != null && !es.isShutdown()) {
            es.shutdown();
            es.awaitTermination(5, TimeUnit.SECONDS);
        }
    }

    /**
     * 手动添加日志(异步入库)
     *
     * @param type
     * @param description
     * @param module
     */
    public void create(Sys_log.TypeEnum type, String description, String module) {
        Sys_log sysLog = new Sys_log();
        if (type == null || description == null) {
            throw new RuntimeException("type/tag/msg can't null");
        }
        if (module == null) {
            StackTraceElement[] tmp = Thread.currentThread().getStackTrace();
            if (tmp.length > 2) {
                module = tmp[2].getClassName() + "#" + tmp[2].getMethodName();
            } else {
                module = "main";
            }

        }
        sysLog.setType(type);
        sysLog.setModule(module);
        sysLog.setIp(Lang.getIP(SpringUtil.getRequest()));
        sysLog.setDescription(description);
        Object u = SecurityUtils.getSubject().getPrincipal();
        String uid = "";
        String username = "";
        if (u != null) {
            if (u instanceof Sys_user) {
                username = ((Sys_user) u).getUsername();
                uid = ((Sys_user) u).getId();
            } else if (u instanceof String) {
                username = ((String) u);
            }
        }
        sysLog.setOpBy(uid);
        sysLog.setOpAt((int) (System.currentTimeMillis() / 1000));
        sysLog.setUsername(username);
        this.async(sysLog);
    }
}
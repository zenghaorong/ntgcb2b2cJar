package com.aebiz.app.web.commons.quartz.job;

import com.aebiz.app.goods.modules.models.Goods_main;
import com.aebiz.app.goods.modules.services.GoodsService;
import com.aebiz.app.goods.modules.services.impl.GoodsServiceImpl;
import com.aebiz.app.shop.modules.models.Shop_estemp;
import com.aebiz.app.shop.modules.services.ShopEstempService;
import com.aebiz.app.shop.modules.services.impl.ShopEstempServiceImpl;
import com.aebiz.app.sys.modules.services.SysTaskService;
import com.aebiz.app.sys.modules.services.impl.SysTaskServiceImpl;
import com.aebiz.app.web.commons.es.EsService;
import com.aebiz.commons.utils.DateUtil;
import com.aebiz.commons.utils.SpringUtil;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.pager.Pager;
import org.nutz.ioc.impl.PropertiesProxy;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.List;

/**
 * Created by Wizzer.cn on 2015/6/27.
 */
public class EsSyncGoodsJob implements Job {

    private static final Log log = Logs.get();
    private SysTaskService sysTaskService = SpringUtil.getBean("sysTaskServiceImpl", SysTaskServiceImpl.class);
    private GoodsService goodsService = SpringUtil.getBean("goodsServiceImpl", GoodsServiceImpl.class);
    private ShopEstempService shopEstempService = SpringUtil.getBean("shopEstempServiceImpl", ShopEstempServiceImpl.class);
    private EsService esService = SpringUtil.getBean("esService", EsService.class);
    private PropertiesProxy config = SpringUtil.getBean("config", PropertiesProxy.class);


    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDataMap data = context.getJobDetail().getJobDataMap();
        String taskId = context.getJobDetail().getKey().getName();
        try {
            int pageSize = data.getInt("pageSize");
            log.info("EsGoodsJob Job pageSize::" + pageSize);
            Pager pager = new Pager();
            pager.setPageNumber(1);
            pager.setPageSize(pageSize);
            List<Shop_estemp> list = shopEstempService.query(Cnd.orderBy().asc("opAt"), "goods", pager);
            if (list.size() > 0) {
                for (Shop_estemp estemp : list) {
                    Goods_main goods = estemp.getGoods();
                    goodsService.fetchLinks(goods, null, Cnd.orderBy().asc("location"));
                    if ("delete".equals(estemp.getAction())) {
                        if (esService.deleteData(config.get("es.index.name"), "goods", goods.getId())) {
                            shopEstempService.delete(estemp.getId());
                        }
                    } else {
                        if (esService.createOrUpdateData(config.get("es.index.name"), "goods", goods.getId(), goods)) {
                            shopEstempService.delete(estemp.getId());
                        }
                    }
                }
            }
            sysTaskService.update(Chain.make("exeAt", (int) (System.currentTimeMillis() / 1000)).add("exeResult", "执行成功").add("nextAt", DateUtil.getTime(context.getNextFireTime())), Cnd.where("id", "=", taskId));
        } catch (Exception e) {
            sysTaskService.update(Chain.make("exeAt", (int) (System.currentTimeMillis() / 1000)).add("exeResult", "执行失败").add("nextAt", DateUtil.getTime(context.getNextFireTime())), Cnd.where("id", "=", taskId));
        }
    }
}

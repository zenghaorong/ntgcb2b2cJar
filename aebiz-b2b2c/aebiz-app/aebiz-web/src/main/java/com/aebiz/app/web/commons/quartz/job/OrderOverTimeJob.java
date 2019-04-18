package com.aebiz.app.web.commons.quartz.job;

import com.aebiz.app.order.modules.models.Order_main;
import com.aebiz.app.order.modules.services.OrderLogService;
import com.aebiz.app.order.modules.services.OrderMainService;
import com.aebiz.app.order.modules.services.impl.OrderLogServiceImpl;
import com.aebiz.app.order.modules.services.impl.OrderMainServiceImpl;
import com.aebiz.app.sys.modules.services.SysTaskService;
import com.aebiz.app.sys.modules.services.impl.SysTaskServiceImpl;
import com.aebiz.commons.utils.DateUtil;
import com.aebiz.commons.utils.SpringUtil;
import com.aebiz.commons.utils.StringUtil;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.Date;
import java.util.List;

/**
 * 订单等待支付时间过期检查JOB
 * Created by ThinkPad on 2017/7/21.
 */
public class OrderOverTimeJob implements Job{

    private static final Log log = Logs.get();

    private SysTaskService sysTaskService = SpringUtil.getBean("sysTaskServiceImpl", SysTaskServiceImpl.class);

    private OrderMainService orderMainService = SpringUtil.getBean("orderMainServiceImpl", OrderMainServiceImpl.class);

    private OrderLogService orderLogService = SpringUtil.getBean("orderLogServiceImpl", OrderLogServiceImpl.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        log.debug("订单支付超时作业检查开始---------------");
        String taskId = context.getJobDetail().getKey().getName();
        try{
            //处理超时未支付的订单
            List<Order_main> orderMainList = orderMainService.query(Cnd.where("orderStatus","=", 1).and("payStatus","=", 0));
            if(orderMainList != null){
                for(Order_main orderMain:orderMainList){
                    //判断订单是否超时两小时
                    Integer orderTime =  orderMain.getOrderAt();
                    Integer nowTime = DateUtil.getTime(new Date());
                    if((nowTime - orderTime) > 60*60*2){
                        orderMainService.update(Chain.make("orderStatus",4),Cnd.where("id","=",orderMain.getId()));
                        StringBuilder note = new StringBuilder("订单号："+orderMain.getId() +" 支付时间已经过期了");
                        //创建订单日志
                        orderLogService.createLog(orderMain, StringUtil.getUsername(),note.toString(), 1);
                    }
                }
            }

            sysTaskService.update(Chain.make("exeAt", (int) (System.currentTimeMillis() / 1000)).add("exeResult", "执行成功").add("nextAt", DateUtil.getTime(context.getNextFireTime())), Cnd.where("id", "=", taskId));
        }catch (Exception e){
            sysTaskService.update(Chain.make("exeAt", (int) (System.currentTimeMillis() / 1000)).add("exeResult", "执行失败").add("nextAt", DateUtil.getTime(context.getNextFireTime())), Cnd.where("id", "=", taskId));
        }finally {
            log.debug("订单支付超时作业检查结束---------------");
        }

    }
}

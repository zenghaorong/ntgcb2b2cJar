package com.aebiz.app.sys.modules.services.impl;

import com.aebiz.app.sys.modules.models.Sys_log;
import com.aebiz.app.sys.modules.services.SysLogService;
import com.aebiz.baseframework.base.service.BaseServiceImpl;
import com.aebiz.baseframework.page.OffsetPager;
import com.aebiz.baseframework.page.datatable.DataTableColumn;
import com.aebiz.baseframework.page.datatable.DataTableOrder;
import com.aebiz.commons.utils.DateUtil;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.entity.Record;
import org.nutz.dao.pager.Pager;
import org.nutz.dao.sql.Sql;
import org.nutz.lang.util.NutMap;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by wizzer on 2016/12/22.
 */
@Service
public class SysLogServiceImpl extends BaseServiceImpl<Sys_log> implements SysLogService {
    @Resource(name = "nutDao", type = Dao.class)
    public void init(Dao dao) {
        super.setDao(dao);
    }

    public void dropLogTable() {
        List<Record> existTableList = getTableList();
        List<String> dropTableList = new ArrayList<>();
        String thisMonth = "sys_log_" + DateUtil.format(new Date(), "yyyyMM");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH, -1);
        String lastMonth = "sys_log_" + DateUtil.format(calendar.getTime(), "yyyyMM");

        for (Record table : existTableList) {
            String tempTableName = table.get("table_name").toString();
            if (tempTableName.equals(thisMonth) || tempTableName.equals(lastMonth)) {
                continue;
            }

            dropTableList.add(tempTableName);
        }

        for (String dropTable : dropTableList) {
            this.clear(dropTable);
        }

    }

    /**
     * 根据时间 返回这两个时间之间的月份List
     *
     * @param date1
     * @param date2
     * @return
     * @throws ParseException
     */
    private List<String> getMonthList(String date1, String date2)
            throws ParseException {
        List<String> tempList = new ArrayList<>();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMM");
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        Calendar c3 = Calendar.getInstance();
        Calendar c4 = Calendar.getInstance();
        c1.setTime(sdf.parse(date1));
        c2.setTime(sdf.parse(date2));
        //为什么要搞这么多日历类型的参数呢？
        //因为要解决一个特殊情况，也不算特殊情况啦，
        //就是当data1的日大于data2的日的 时候data1月份一直加的话 就会在月份相同的情况下，整体时间比data2大 导致data2那个月份的表漏掉
        //肯定有更便捷的写法啦~ 但是我现在有点懵逼 暂时就不优化啦。啦啦啦
        c3.setTime(sdf2.parse(DateUtil.format(c1.getTime(), "yyyyMM")));
        c4.setTime(sdf2.parse(DateUtil.format(c2.getTime(), "yyyyMM")));
        c4.add(Calendar.DAY_OF_MONTH,1);//为毛c4.set(Calendar.DAY_OF_MONTH,1);不好使？要不然省的上面定义一大堆。

        while (c4.after(c3)) {
            //若时间超过当前时间则跳出
            if (c3.getTime().getTime() > new Date().getTime()) {
                break;
            }

            tempList.add("sys_log_" + DateUtil.format(c3.getTime(), "yyyyMM"));
            c3.add(Calendar.MONTH, 1);
        }

        //根据现有的表进行过滤
        List<Record> existTableList = getTableList();
        List<String> resList = new ArrayList<>();

        for (Record table : existTableList) {
            String tempTableName = table.get("table_name").toString();
            for (String tempTableName2 : tempList) {
                if (tempTableName2.equals(tempTableName)) {
                    resList.add(tempTableName);
                }

            }

        }

        return resList;

    }

    private boolean theSameMonth(String date1, String date2)
            throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.setTime(sdf.parse(date1));
        c2.setTime(sdf.parse(date2));

        if (c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH)) {
            return true;
        }

        return false;
    }

    private List<Record> getTableList() {
        return this.list(Sqls.create("SELECT table_name FROM information_schema.TABLES WHERE table_name like 'sys_log_2%';"));
    }


}

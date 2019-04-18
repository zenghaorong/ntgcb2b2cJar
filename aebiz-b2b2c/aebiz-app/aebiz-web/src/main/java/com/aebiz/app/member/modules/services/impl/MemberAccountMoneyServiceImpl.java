package com.aebiz.app.member.modules.services.impl;

import com.aebiz.app.member.modules.models.Member_account_money;
import com.aebiz.app.member.modules.services.MemberAccountMoneyService;
import com.aebiz.app.member.modules.services.MemberAccountService;
import com.aebiz.app.sys.modules.models.Sys_user;
import com.aebiz.app.sys.modules.services.SysUserService;
import com.aebiz.baseframework.base.service.BaseServiceImpl;
import com.aebiz.baseframework.page.OffsetPager;
import com.aebiz.baseframework.page.datatable.DataTableColumn;
import com.aebiz.baseframework.page.datatable.DataTableOrder;
import com.aebiz.commons.utils.MoneyUtil;
import com.aebiz.commons.utils.StringUtil;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.pager.Pager;
import org.nutz.lang.Strings;
import org.nutz.lang.util.NutMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class MemberAccountMoneyServiceImpl extends BaseServiceImpl<Member_account_money> implements MemberAccountMoneyService {
    @Resource(name = "nutDao", type = Dao.class)
    public void init(Dao dao) {
        super.setDao(dao);
    }

    @Autowired
    private MemberAccountService memberAccountService;
    @Autowired
    private SysUserService sysUserService;

    /**
     * 显示会员账户余额变动的记录
     * @param length   每页显示的数据条数
     * @param start    起始条数
     * @param draw     请求次数
     * @param orders   排序
     * @param columns  字段
     * @param cnd      查询条件
     * @param linkName 关联查询的字段名称，支持正则表达式
     * @return
     */
    public NutMap moneyInfo(int length, int start, int draw, List<DataTableOrder> orders, List<DataTableColumn> columns, Cnd cnd, String linkName) {
        if (orders != null && orders.size() > 0) {
            for (DataTableOrder order : orders) {
                DataTableColumn col = columns.get(order.getColumn());
                cnd.orderBy(Sqls.escapeSqlFieldValue(col.getData()).toString(), order.getDir());
            }
        }

        Pager pager = new OffsetPager(start, length);
        List<Member_account_money> moneyList = this.query(cnd, pager);
        if (!Strings.isBlank(linkName)) {
            this.fetchLinks(moneyList, linkName);
        }

        List<Map<String, String>> recordList = new ArrayList<>();
        if (moneyList != null && moneyList.size() > 0) {
            // 如果有记录，把记录的相关信息组合成字符串放到recordList中
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            for (Member_account_money money : moneyList) {
                Map<String,String> map = new HashMap<>();
                Integer time = money.getCreatAt();
                if (time == null || time < 0){
                    time = money.getOpAt();
                }
                String formatTime = sdf.format(new Date((long)time * 1000));
                StringBuffer record = new StringBuffer(formatTime).append(", ");
                int diff = money.getDiffMoney();
                if (diff > 0) {
                    record.append("+");
                }
                record.append(MoneyUtil.fenToYuan(diff));
                String username = "";
                Sys_user sysUser = sysUserService.fetch(money.getOpBy());
                if (sysUser != null) {
                    username = sysUser.getUsername();
                }

                record.append(", 操作人：").append(username);
                record.append(", 备注：").append(money.getNote());
                map.put("record", record.toString());
                recordList.add(map);
            }
        }

        NutMap map = new NutMap();
        map.put("draw", draw);
        map.put("data", recordList);
        map.put("recordsTotal", length);
        map.put("recordsFiltered", this.count(cnd));
        return map;
    }

    /**
     * 更新余额并添加一条余额变动记录
     * @param memberAccountMoney
     * @param accountId 账户id
     */
    @Transactional
    public void updateMoney(Member_account_money memberAccountMoney, String accountId) {
        int opeTime = (int) (System.currentTimeMillis() / 1000);
        String opeManUid = StringUtil.getUid();
        // 更新账户余额,操作时间和操作人
        Integer money = memberAccountMoney.getOldMoney() + memberAccountMoney.getDiffMoney();
        memberAccountService.update(Chain.make("money",money).add("opAt",opeTime).add("opBy",opeManUid),Cnd.where("accountId","=",accountId));
        // 余额变动表中添加一条记录
        memberAccountMoney.setNewMoney(money);
        memberAccountMoney.setCreatAt(opeTime);
        this.insert(memberAccountMoney);
    }
}

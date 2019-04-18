package com.aebiz.app.member.modules.services.impl;


import com.aebiz.app.member.modules.models.Member_account;
import com.aebiz.app.member.modules.models.Member_account_score;
import com.aebiz.app.member.modules.services.MemberAccountScoreService;
import com.aebiz.app.member.modules.services.MemberAccountService;
import com.aebiz.app.sys.modules.models.Sys_user;
import com.aebiz.app.sys.modules.services.SysUserService;
import com.aebiz.baseframework.base.service.BaseServiceImpl;
import com.aebiz.baseframework.page.OffsetPager;
import com.aebiz.baseframework.page.datatable.DataTableColumn;
import com.aebiz.baseframework.page.datatable.DataTableOrder;
import com.aebiz.commons.utils.StringUtil;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.pager.Pager;
import org.nutz.dao.sql.Sql;
import org.nutz.lang.Strings;
import org.nutz.lang.util.NutMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class MemberAccountScoreServiceImpl extends BaseServiceImpl<Member_account_score> implements MemberAccountScoreService {
    @Resource(name = "nutDao", type = Dao.class)
    public void init(Dao dao) {
        super.setDao(dao);
    }

    @Autowired
    private MemberAccountService memberAccountService;
    @Autowired
    private SysUserService sysUserService;

    /**
     * 显示会员账户积分变动的记录
     * @param length   每页显示的数据条数
     * @param start    起始条数
     * @param draw     请求次数
     * @param orders   排序
     * @param columns  字段
     * @param cnd      查询条件
     * @param linkName 关联查询的字段名称，支持正则表达式
     * @return
     */
    public NutMap viewScoreInfo(int length, int start, int draw, List<DataTableOrder> orders, List<DataTableColumn> columns, Cnd cnd, String linkName) {
            if (orders != null && orders.size() > 0) {
                for (DataTableOrder order : orders) {
                    DataTableColumn col = columns.get(order.getColumn());
                    cnd.orderBy(Sqls.escapeSqlFieldValue(col.getData()).toString(), order.getDir());
                }
            }

            Pager pager = new OffsetPager(start, length);
            // 根据查询条件和分页查询积分变动记录
            List<Member_account_score> moneyList = this.query(cnd, pager);
            // 关联查询
            if (!Strings.isBlank(linkName)) {
                this.fetchLinks(moneyList, linkName);
            }

            List<Map<String, String>> recordList = new ArrayList<>();
            if (moneyList != null && moneyList.size() > 0) {
                // 如果有记录，把记录的相关信息组合成字符串放到recordList中
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                for (Member_account_score score : moneyList) {
                    Map<String,String> map = new HashMap<>();
                    Integer time = score.getCreatAt();
                    if (time == null || time < 0){
                        time = score.getOpAt();
                    }
                    String formatTime = sdf.format(new Date((long)time * 1000));
                    StringBuffer record = new StringBuffer(formatTime).append(", ");
                    int diff = score.getDiffScore();
                    if (diff > 0) {
                        record.append("+");
                    }
                    record.append(diff);
                    String username = "";
                    Sys_user sysUser = sysUserService.fetch(score.getOpBy());
                    if (sysUser != null) {
                        username = sysUser.getUsername();
                    }

                    record.append(", 操作人：").append(username);
                    record.append(", 备注：").append(score.getNote());
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

    @Transactional
    public void updateScore(Member_account_score memberAccountScore, String accountId) {
        int opeTime = (int) (System.currentTimeMillis() / 1000);
        String opeManUid = StringUtil.getUid();
        // 更新账户积分,操作时间和操作人
        Integer score = memberAccountScore.getOldScore() + memberAccountScore.getDiffScore();
        memberAccountService.update(Chain.make("score", score).add("opAt", opeTime).add("opBy", opeManUid), Cnd.where("accountId", "=", accountId));
        // 积分变动表添加一条记录
        memberAccountScore.setNewScore(score);
        memberAccountScore.setCreatAt(opeTime);
        this.insert(memberAccountScore);
    }

    @Override
    @Transactional
    public Map<String, Object> selectDataAll(String accountId, Integer page, Integer rows, Integer status) {
        String sq="SELECT ma.*,gi.imgAlbum,gp.name FROM member_account_score ma\n" +
                "LEFT JOIN goods_image gi ON ma.goodsId=gi.goodsId\n" +
                "LEFT JOIN goods_product gp ON ma.goodsId=gp.goodsId " +
                "WHERE ma.accountId='"+accountId+"'and gi.defaultValue=1 and gp.defaultValue=1\n"+
                "order by creatAt desc";
        Sql sql1 = Sqls.queryRecord(sq+" limit "+(page-1)*rows+","+rows);//
        dao().execute(sql1);
        int totle = dao().count("member_account_score ma \n" +
                "LEFT JOIN goods_image gi ON ma.goodsId=gi.goodsId\n" +
                "LEFT JOIN goods_product gp ON ma.goodsId=gp.goodsId " +
                "WHERE ma.accountId='"+accountId+"' and gi.defaultValue=1 and gp.defaultValue=1");
        List<Member_account_score> list = sql1.getList(Member_account_score.class);
        Map<String,Object> map = new HashMap<String,Object>();
        int total = (totle+rows-1)/rows;
        map.put("totalPage", total);
        map.put("page", page);
        map.put("status", status);
        map.put("records", totle);
        map.put("count", list.size());
        map.put("rowList", list);
        return map;
    }

}

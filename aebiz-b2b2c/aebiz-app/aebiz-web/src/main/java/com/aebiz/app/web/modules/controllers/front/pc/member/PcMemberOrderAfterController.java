package com.aebiz.app.web.modules.controllers.front.pc.member;

import com.aebiz.app.goods.modules.services.GoodsProductService;
import com.aebiz.app.order.modules.models.*;
import com.aebiz.app.order.modules.models.em.OrderAfterStateEnum;
import com.aebiz.app.order.modules.services.*;
import com.aebiz.app.sys.modules.services.SysDictService;
import com.aebiz.baseframework.base.Result;
import com.aebiz.baseframework.page.datatable.DataTableColumn;
import com.aebiz.baseframework.page.datatable.DataTableOrder;
import com.aebiz.baseframework.view.annotation.SJson;
import com.aebiz.commons.utils.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.nutz.dao.Cnd;
import org.nutz.lang.Lang;
import org.nutz.lang.Strings;
import org.nutz.lang.util.NutMap;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * Created by zhangyi on 2017/6/16.
 * 个人中心订单售后，主要是用户提出订单售后申请，查看售后申请等
 */
@Controller
@RequestMapping("/member/orderAfter")
public class PcMemberOrderAfterController {
    private static final Log log = Logs.get();

    @Autowired
    private OrderAfterMainService orderAfterMainService;

    @Autowired
    private OrderAfterReturnsService orderAfterReturnsService;

    @Autowired
    private OrderMainService orderMainService;

    @Autowired
    private OrderAfterRefundmentService orderAfterRefundmentService;

    @Autowired
    private OrderAfterDetailService orderAfterDetailService;

    @Autowired
    private OrderGoodsService orderGoodsService;

    @Autowired
    private SysDictService sysDictService;

    @Autowired
    private GoodsProductService goodsProductService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index(HttpServletRequest req, HttpSession session) {
        return "pages/front/pc/member/orderAfter";
    }

    @RequestMapping(value= "/data",method = RequestMethod.POST)
    public String orderAfterData(@RequestParam("pageNo")Integer pageNo,
                                 @RequestParam("pageSize")Integer pageSize,
                                 @RequestParam("applyType")String applyType,
                                 @RequestParam(value = "id",required = false)String id,
                                 ArrayList<DataTableOrder> order,
                                 ArrayList<DataTableColumn> columns,
                                 HttpServletRequest req){
        //获取当前会员的Id
        String accountId = StringUtil.getMemberUid();
        Cnd cnd = Cnd.NEW();
        cnd.and("applyManId","=", accountId).and("delFlag","=",false).desc("applyTime");
        if(Strings.isNotBlank(id)){
            cnd.and("id", "=", id);
        }
        if(Strings.isNotBlank(applyType)){
            cnd.and("applyType", "=", Integer.parseInt(applyType));
        }
        int start =  (pageNo - 1) * pageSize;
        NutMap map = orderAfterMainService.data(pageSize, start, 0, order, columns, cnd, null);
        int totalCount = (int)map.get("recordsFiltered");//count总数
        int pageTotal = (int)Math.ceil((double)totalCount / (double)pageSize);
        //查询售后商品信息
        List<Order_after_main> list = (List<Order_after_main>) map.get("data");
        List<String> ids = new LinkedList<>();
        for(Order_after_main oam : list){
            ids.add(oam.getId());
        }
        Map<String,Order_after_detail> detailMap = new HashMap<>();
        if(ids.size() > 0){
            Cnd cnd1 = Cnd.where("afterSaleId", "in", ids);
            List<Order_after_detail> orderAfterDetails = orderAfterDetailService.query(cnd1, "^(goodsMain|orderGoods)$");
            for(Order_after_detail detail : orderAfterDetails){
                detail.getOrderGoods().setImgUrl(goodsProductService.getProductImage(detail.getSku()));
                detailMap.put(detail.getAfterSaleId(), detail);
            }
        }
        for(Order_after_main main : list){
            main.setOrderAfterDetail(detailMap.get(main.getId()));
        }
        req.setAttribute("obj",map);
        req.setAttribute("pageTotal", pageTotal);
        return "pages/front/pc/member/orderAfter_data";
    }

    @RequestMapping(value= "/detail/{afterSaleId}",method = RequestMethod.GET)
    public String orderAfterDetail(@PathVariable(value = "afterSaleId") String afterSaleId, HttpServletRequest req){
        //获取当前会员的Id
        String accountId = StringUtil.getMemberUid();
        //售后主表信息
        Order_after_main orderAfterMain = orderAfterMainService.fetch(Cnd.where("id", "=", afterSaleId).and("delFlag", "=", false).and("applyManId", "=", accountId));
        orderAfterMainService.fetchLinks(orderAfterMain, "orderMain");
        //售后详情信息
        Order_after_detail orderAfterDetail = orderAfterDetailService.fetch(Cnd.where("afterSaleId", "=", afterSaleId));
        orderAfterDetailService.fetchLinks(orderAfterDetail,"^(goodsMain|orderGoods)$");
        orderAfterDetail.getOrderGoods().setImgUrl(goodsProductService.getProductImage(orderAfterDetail.getSku()));
        //售后退货物流
        Order_after_returns orderAfterReturns = orderAfterReturnsService.fetch(Cnd.where("afterSaleId", "=", afterSaleId));
        if(!Lang.isEmpty(orderAfterReturns)){
            req.setAttribute("orderAfterReturns", orderAfterReturns);
        }
        //售后退款信息
        Order_after_refundment orderAfterRefundment = orderAfterRefundmentService.fetch(Cnd.where("afterSaleId", "=", afterSaleId));
        if(!Lang.isEmpty(orderAfterRefundment)){
            req.setAttribute("orderAfterRefundment",orderAfterRefundment);
        }
        req.setAttribute("orderAfterMain", orderAfterMain);
        req.setAttribute("orderAfterDetail", orderAfterDetail);
        return "pages/front/pc/member/orderAfter_detail";
    }

    @RequestMapping(value = "/applyOrderAfter")
    public String applyOrderAfter(@RequestParam(value = "orderGoodsId") String orderGoodsId,
                                  @RequestParam(value = "orderId") String orderId,
                                  HttpServletRequest req){
        //获取当前会员的Id
        String accountId = StringUtil.getMemberUid();
        Order_goods orderGoods = orderGoodsService.fetch(Cnd.where("id", "=", orderGoodsId).and("orderId", "=", orderId).and("accountId", "=", accountId));
        orderGoodsService.fetchLinks(orderGoods, "^(orderMain|goodsMain)$");
        orderGoods.setImgUrl(goodsProductService.getProductImage(orderGoods.getSku()));
        // 获取售后原因到页面
        req.setAttribute("reasons", sysDictService.getSubListByPath("0003"));
        req.setAttribute("orderGoods", orderGoods);
        return "pages/front/pc/member/orderAfter_apply";
    }

    /**
     * 申请售后
     *
     * @param applyType 售后类型（如仅退款）
     * @param orderId   订单Id
     * @param orderGoodsId 订单明细Id
     * @param returnReason  售后原因
     * @param description   售后说明描述
     * @param evidence  凭证，买家上传的凭证
     * @param req
     * @return
     */
    @RequestMapping(value = "/addOrderAfterDo", method = RequestMethod.POST)
    @SJson
    public Object addOrderAfterDo(@RequestParam(value = "applyType") String applyType,
                               @RequestParam(value = "orderId") String orderId,
                               @RequestParam(value = "orderGoodsId") String orderGoodsId,
                               @RequestParam(value = "afterSaleNum") Integer afterSaleNum,
                               @RequestParam(value = "returnReason") String returnReason,
                               @RequestParam(value = "description", required = false) String description,
                               @RequestParam(value = "evidence",required = false) String evidence,
                               HttpServletRequest req){
        try{
            Order_goods fetch = orderGoodsService.fetch(Cnd.where("id", "=", orderGoodsId).and("orderId", "=", orderId).and("accountId", "=", StringUtil.getMemberUid()));
            Integer buyNum =  fetch.getBuyNum();
            if(afterSaleNum < 1 || afterSaleNum > buyNum){
                return Result.error("order.after.result.afterSaleNumApplyError");
            }
            NutMap nutMap = orderAfterMainService.checkCanApply(orderId, orderGoodsId);
            if(1 == (Integer) nutMap.get("code")){
                return (Result)nutMap.get("result");
            }
            //通过校验，则创建售后申请单
            Order_after_main orderAfterMain = new Order_after_main();
            orderAfterMain.setOrderId(orderId);
            orderAfterMain.setApplyType(Integer.valueOf(applyType));
            orderAfterMain.setReturnReason(returnReason);
            orderAfterMain.setDescription(Strings.isNotBlank(description)?description:"");
            if(Strings.isNotBlank(evidence)){
                String[] evidences = evidence.split(",");
                for (int i = 0; i < evidences.length; i++) {
                    if (i > 2) {
                        break;
                    }
                    switch (i + 1) {
                        case 1:
                            orderAfterMain.setEvidence1(evidences[i]);
                            break;
                        case 2:
                            orderAfterMain.setEvidence2(evidences[i]);
                            break;
                        case 3:
                            orderAfterMain.setEvidence3(evidences[i]);
                            break;
                        default:
                            break;
                    }
                }
            }

            //创建售后申请详情
            Order_after_detail orderAfterDetail = new Order_after_detail();
            orderAfterDetail.setOrderGoodsId(orderGoodsId);
            orderAfterDetail.setAfterSaleNum(afterSaleNum);

            //申请售后创建
            orderAfterMainService.addApplyOrderAfter(orderAfterMain, orderAfterDetail);
            return Result.success("globals.result.success");
        }catch (Exception e){
            log.debug(e.getMessage(),e);
            return Result.error("globals.result.error");
        }
    }

    /**
     * 买家取消售后
     *
     * @param afterSaleId 售后单号
     * @param cancelReason 取消原因
     * @param req
     */
    @RequestMapping(value = "cancelOrderAfter", method = RequestMethod.POST)
    @SJson
    public Object cancelOrderAfter(@RequestParam(value = "afterSaleId") String afterSaleId,
                                 @RequestParam(value = "cancelReason", required = false, defaultValue = "") String cancelReason,
                                 HttpServletRequest req
                                 ){
        try {
            //获取当前会员的Id
            String accountId = StringUtil.getMemberUid();
            Order_after_main orderAfterMain = orderAfterMainService.fetch(Cnd.where("applyManId", "=", accountId).and("id", "=", afterSaleId));
            if(null == orderAfterMain){
                return Result.error("order.after.result.inexistence");
            }
            orderAfterMainService.cancelOrderAfter(afterSaleId, cancelReason);
            return Result.success();
        }catch (Exception e){
            log.debug(e.getMessage(),e);
            return Result.error("globals.result.error");
        }
    }


    /**
     * 上传会员寄回商品物流信息
     *
     * @param afterSaleId 售后单号
     * @param logisticsCompany 物流公司
     * @param logisticsSheetId 物流单号
     * @param note 备注说明
     * @param voucher 凭证图片凭借字符串
     * @param req
     * @return
     */
    @RequestMapping(value = "addRefundLogisticsDo", method = RequestMethod.POST)
    @SJson
    public Object addRefundLogisticsDo(@RequestParam(value = "afterSaleId") String afterSaleId,
                                       @RequestParam(value = "logisticsCompany") String logisticsCompany,
                                       @RequestParam(value = "logisticsSheetId") String logisticsSheetId,
                                       @RequestParam(value = "note", required = false, defaultValue = "") String note,
                                       @RequestParam(value = "voucher", required = false) String voucher,
                                       HttpServletRequest req
                                       ){
        try {
            //获取当前会员的Id
            String accountId = StringUtil.getMemberUid();
            Order_after_main orderAfterMain = orderAfterMainService.fetch(Cnd.where("applyManId", "=", accountId).and("id", "=", afterSaleId));
            if(null == orderAfterMain){
                return Result.error("order.after.result.inexistence");
            }
            if(orderAfterMain.getAfterSaleState() != 4){
                return  Result.error("order.after.result.stateError");
            }
            //凭证最多三张
            String[] vouchers = StringUtils.split(Strings.sNull(voucher), ",");
            orderAfterReturnsService.addRefundLogistics(afterSaleId, logisticsCompany, logisticsSheetId, note, vouchers);
            return Result.success();
        }catch (Exception e){
            log.debug(e.getMessage(),e);
            return Result.error();
        }
    }

    /**
     * 填写售后退款银行账号
     *
     * @param afterSaleId 售后单号
     * @param bankCard 银行卡号
     * @param bankName 银行名称
     * @param name 人名
     * @param req
     * @return
     */
    @RequestMapping(value = "addBankCardDo", method = RequestMethod.POST)
    @SJson
    public Object addBankCardDo(@RequestParam(value = "afterSaleId") String afterSaleId,
                                @RequestParam(value = "bankCard") String bankCard,
                                @RequestParam(value = "bankName") String bankName,
                                @RequestParam(value = "name") String name,
                                HttpServletRequest req){
        try {
            //获取当前会员的Id
            String accountId = StringUtil.getMemberUid();
            Integer afterSaleState = orderAfterMainService.fetch(Cnd.where("applyManId", "=", accountId).and("id", "=", afterSaleId)).getAfterSaleState();
            if(OrderAfterStateEnum.WAIT_BUYER_ACCOUNT.getKey() != afterSaleState){
                return Result.error("order.after.result.stateErro");
            }
            orderAfterRefundmentService.addBankCardDo(afterSaleId, bankCard, bankName, name);
            return Result.success();
        }catch (Exception e){
            log.debug(e.getMessage(), e);
            return Result.error();
        }
    }

}

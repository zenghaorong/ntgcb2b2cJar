package com.aebiz.app.web.modules.controllers.platform.self.order;

import com.aebiz.app.acc.modules.models.Account_info;
import com.aebiz.app.acc.modules.models.Account_user;
import com.aebiz.app.acc.modules.services.AccountInfoService;
import com.aebiz.app.acc.modules.services.AccountUserService;
import com.aebiz.app.goods.modules.models.Goods_main;
import com.aebiz.app.goods.modules.models.Goods_product;
import com.aebiz.app.goods.modules.services.GoodsProductService;
import com.aebiz.app.member.modules.models.Member_account;
import com.aebiz.app.member.modules.models.Member_address;
import com.aebiz.app.member.modules.models.Member_user;
import com.aebiz.app.member.modules.services.MemberAccountService;
import com.aebiz.app.member.modules.services.MemberAddressService;
import com.aebiz.app.member.modules.services.MemberUserService;
import com.aebiz.app.order.commons.vo.OrderCheckBoxStatus;
import com.aebiz.app.order.modules.models.*;
import com.aebiz.app.order.modules.models.em.OrderDeliveryStatusEnum;
import com.aebiz.app.order.modules.models.em.OrderPayStatusEnum;
import com.aebiz.app.order.modules.models.em.OrderPayTypeEnum;
import com.aebiz.app.order.modules.models.em.OrderStatusEnum;
import com.aebiz.app.order.modules.services.*;
import com.aebiz.app.shop.modules.models.Shop_account;
import com.aebiz.app.shop.modules.models.Shop_express;
import com.aebiz.app.shop.modules.services.ShopAccountService;
import com.aebiz.app.shop.modules.services.ShopAreaService;
import com.aebiz.app.shop.modules.services.ShopExpressService;
import com.aebiz.app.shop.modules.services.impl.ShopAreaServiceImpl;
import com.aebiz.app.sys.modules.models.Sys_dict;
import com.aebiz.app.sys.modules.services.SysDictService;
import com.aebiz.app.web.commons.log.annotation.SLog;
import com.aebiz.baseframework.base.Result;
import com.aebiz.baseframework.page.OffsetPager;
import com.aebiz.baseframework.page.datatable.DataTableColumn;
import com.aebiz.baseframework.page.datatable.DataTableOrder;
import com.aebiz.baseframework.view.annotation.SJson;
import com.aebiz.commons.utils.SpringUtil;
import com.aebiz.commons.utils.StringUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.dao.Cnd;
import org.nutz.dao.Sqls;
import org.nutz.dao.pager.Pager;
import org.nutz.dao.sql.Sql;
import org.nutz.dao.util.Daos;
import org.nutz.dao.util.cri.SqlExpressionGroup;
import org.nutz.json.Json;
import org.nutz.lang.Lang;
import org.nutz.lang.Strings;
import org.nutz.lang.util.NutMap;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/platform/self/order/main")
public class SelfOrderMainController {

    private static final Log log = Logs.get();
    
    @Autowired
	private OrderMainService orderMainService;

    @Autowired
    private OrderGoodsService orderGoodsService;

    @Autowired
    private OrderPayTransferService orderPayTransferService;

    @Autowired
    private OrderPayPaymentService orderPayPaymentService;

    @Autowired
    private OrderLogService orderLogService;

    @Autowired
    private AccountUserService accountUserService;

    @Autowired
    private MemberUserService memberUserService;

    @Autowired
    private ShopExpressService shopExpressService;

    @Autowired
    private MemberAddressService memberAddressService;

    @Autowired
    private MemberAccountService memberAccountService;

    @Autowired
    private ShopAccountService shopAccountService;

    @Autowired
    private GoodsProductService goodsProductService;

    @Autowired
    private OrderAfterMainService orderAfterMainService;

    @Autowired
    private OrderDeliveryDetailService orderDeliveryDetailService;

    @Autowired
    private SysDictService sysDictService;

    @Autowired
    private ShopAreaService shopAreaService;

    @Autowired
    private AccountInfoService accountInfoService;

    @RequestMapping("")
    @RequiresPermissions("self.order.main")
	public String index(HttpServletRequest req) {
        NutMap map = new NutMap();
        //查询待发货的订单数量
        Integer waitDeliveryNum = orderMainService.count(Cnd.where("deliveryStatus","<",OrderDeliveryStatusEnum.ALL.getKey())
                .and("payStatus","=",OrderPayStatusEnum.PAYALL.getKey()).and("payType"," in",OrderPayTypeEnum.ONLINE.getKey()+","+OrderPayTypeEnum.TRANSFER.getKey())
                .and("orderStatus","=",OrderStatusEnum.ACTIVE.getKey()).and("delFlag","=",false).and("storeId","=","2017060000000001"));
        //查询待审核的订单数量
        Integer waitVerifyNum = orderMainService.count(Cnd.where("orderStatus","=",OrderStatusEnum.WAITVERIFY.getKey()).and("delFlag","=",false).and("storeId","=","2017060000000001"));
        //查询待支付的数量
        Integer waitPayNum = orderMainService.count(Cnd.where("payStatus","<",OrderPayStatusEnum.PAYALL.getKey()).and("payType"," in",OrderPayTypeEnum.ONLINE.getKey()+","+OrderPayTypeEnum.TRANSFER.getKey()).and("orderStatus","=",OrderStatusEnum.ACTIVE.getKey()).and("delFlag","=",false).and("storeId","=","2017060000000001"));
        //货到付款的订单数量
        Integer deliveryPayNum = orderMainService.count(Cnd.where("payType","in", OrderPayTypeEnum.CASH.getKey()+","+OrderPayTypeEnum.POS.getKey()+","+OrderPayTypeEnum.ALIQRCODE.getKey()).and("orderStatus","=",OrderStatusEnum.ACTIVE.getKey()).and("delFlag","=",false).and("storeId","=","2017060000000001"));
        //关闭订单原因
        List<Sys_dict> sysDictList = sysDictService.query(Cnd.where("delFlag","=",false).and("code","=","order_close_reason"));
        if(sysDictList != null && sysDictList.size() > 0){
            Sys_dict sysDict = sysDictList.get(0);
            List<Sys_dict> reasonList = sysDictService.query(Cnd.where("delFlag","=",false).and("parentId","=",sysDict.getId()));
            req.setAttribute("reasonList",reasonList);
        }
        map.put("waitDeliveryNum",waitDeliveryNum);
        map.put("waitVerifyNum",waitVerifyNum);
        map.put("waitPayNum",waitPayNum);
        map.put("deliveryPayNum",deliveryPayNum);
        // TODO: 2017/5/25 超时订单和预售订单未知
        req.setAttribute("obj",map);
        return "pages/platform/self/order/main/index";
	}

    /**
     * 获取会号列表
     * @param loginname
     * @param mobile
     * @param length
     * @param start
     * @param draw
     * @param order
     * @param columns
     * @return
     */
    @RequestMapping("/account")
    @SJson("full")
    @RequiresPermissions("self.order.main")
    public Object accountData(@RequestParam(value = "loginname",required = false) String loginname,@RequestParam(value = "mobile",required = false) String mobile,@RequestParam("length")int length, @RequestParam("start") int start, @RequestParam("draw") int draw, ArrayList<DataTableOrder> order, ArrayList<DataTableColumn> columns) {
        Cnd cnd = Cnd.NEW();
        cnd.and("delFlag","=",false);
        Map<String,String> accountMap = new HashMap<>();
        StringBuilder  sqlBuilder  = new StringBuilder("select member.* from member_user member,account_info info,account_user user where info.id = user.accountId and member.accountId = info.id and info.delFlag = 0 and user.disabled = 0 ");
        if(Strings.isNotBlank(loginname)){
            accountMap.put("loginname",loginname.trim());
            sqlBuilder.append( " and user.loginname like @loginname ");
        }
        if(Strings.isNotBlank(mobile)){
            accountMap.put("mobile",mobile.trim());
            sqlBuilder.append("and user.mobile like @mobile ");
        }
        Sql countSql = Sqls.create(sqlBuilder.toString());
        Sql orderSql = countSql.duplicate();
        for(String key : accountMap.keySet()){
            countSql.setParam(key,accountMap.get(key)+"%");
            orderSql.setParam(key,accountMap.get(key)+"%");
        }
        NutMap re = new NutMap();
        Pager pager = new OffsetPager(start, length);
        pager.setRecordCount((int) Daos.queryCount(memberUserService.dao(), countSql));
        orderSql.setPager(pager);
        orderSql.setCallback(Sqls.callback.entities());
        orderSql.setEntity(memberUserService.dao().getEntity(Member_user.class));
        memberUserService.dao().execute(orderSql);
        List<Member_user> list = orderSql.getList(Member_user.class);
        memberUserService.dao().fetchLinks(list, "^(accountInfo|accountUser|memberLevel|memberType)$");
        re.put("recordsFiltered", pager.getRecordCount());
        re.put("data", list);
        re.put("draw", draw);
        re.put("recordsTotal", length);
        return re;
    }

    /**
     * 获取货品列表
     * @param length
     * @param start
     * @param draw
     * @param order
     * @param columns
     * @return
     */
    @RequestMapping("/product")
    @SJson("full")
    @RequiresPermissions("self.order.main")
    public Object productData(@ModelAttribute("goods") Goods_main goods,@RequestParam("length")int length, @RequestParam("start") int start, @RequestParam("draw") int draw, ArrayList<DataTableOrder> order, ArrayList<DataTableColumn> columns) {
        //查询货品与商品的关联查询
        NutMap map = goodsProductService.data(length, start, draw, order, columns, goods,"2017060000000001", "goodsMain");
        return map;
    }

    /**
     * 获取会员的默认地址
     * @param accountId
     * @return
     */
    @RequestMapping("/address")
    @SJson
    @RequiresPermissions("self.order.main")
    public Object getAddressData(@RequestParam("accountId") String accountId){
        Cnd cnd = Cnd.NEW();
        cnd.and("accountId","=",accountId);
        cnd.and("defaultValue","=",true);
        cnd.and("delFlag","=",false);
        /*查询会员的默认收货地址*/
        Member_address memberAddress = memberAddressService.fetch(cnd);
        if(memberAddress != null){
            memberAddress.setProvince(shopAreaService.getNameByCode(memberAddress.getProvince()));
            memberAddress.setCity(shopAreaService.getNameByCode(memberAddress.getCity()));
            memberAddress.setCounty(shopAreaService.getNameByCode(memberAddress.getCounty()));
        }
        return memberAddress;
    }

    /**
     * 订单列表查询
     * @param status
     * @param length
     * @param start
     * @param draw
     * @param order
     * @param columns
     * @return
     */
	@RequestMapping("/data")
    @SJson("full")
    @RequiresPermissions("self.order.main")
    public Object data(@RequestParam(value = "id",required = false) String id,
                       @RequestParam(value = "loginname",required = false) String loginname,
                       @RequestParam(value = "checkedStatus",required = false) String checkedStatus,
                       @RequestParam("status") Integer status,
                       @RequestParam("length") int length,
                       @RequestParam("start") int start,
                       @RequestParam("draw") int draw, ArrayList<DataTableOrder> order, ArrayList<DataTableColumn> columns) {
		Cnd cnd = Cnd.NEW();
		//传入平台ID
        cnd.and("storeId","=","2017060000000001");
		if(Strings.isNotBlank(id)){
		    cnd.and("id","=",id.trim());
		}
		if(Strings.isNotBlank(loginname)){
		    String accId="";
		    List<Account_user> accountUserList = accountUserService.query("accountId",Cnd.where("loginname","=",loginname));
		    if(accountUserList != null && accountUserList.size() > 0){
		        for(Account_user accountUser:accountUserList){
                   accId = accountUser.getAccountId();
                }
                cnd.and("accountId","=",accId);
            }else{
                cnd.and("accountId","=","-1");
            }
        }
        if(Strings.isNotBlank(checkedStatus)){
		    //选中的状态
		    List<OrderCheckBoxStatus> checkBoxStatusList = Json.fromJsonAsList(OrderCheckBoxStatus.class,checkedStatus);
            SqlExpressionGroup el = new SqlExpressionGroup();
		    for(int i = 0;i<checkBoxStatusList.size();i++){
		        el.or(checkBoxStatusList.get(i).getName(),"=",checkBoxStatusList.get(i).getVal());
            }
            cnd.and(el);
            if(status == 0 ){
                cnd.and("orderStatus","=",OrderStatusEnum.ACTIVE.getKey());
            }
        }
        cnd.and("delFlag","=",false);
        switch (status){
            case 0:
                cnd.desc("opAt");
                break;
            case 1:
                cnd.and("deliveryStatus","<",OrderDeliveryStatusEnum.ALL.getKey())
                        .and("payStatus","=",OrderPayStatusEnum.PAYALL.getKey()).and("payType"," in",OrderPayTypeEnum.ONLINE.getKey()+","+OrderPayTypeEnum.TRANSFER.getKey())
                        .and("orderStatus","=",OrderStatusEnum.ACTIVE.getKey()).desc("payAt");
                break;
            case 2:
                cnd.and("orderStatus","=",OrderStatusEnum.WAITVERIFY.getKey());
                break;
            case 3:
                cnd.and("payStatus","<",OrderPayStatusEnum.PAYALL.getKey()).and("payType"," in",OrderPayTypeEnum.ONLINE.getKey()+","+OrderPayTypeEnum.TRANSFER.getKey()).and("orderStatus","=",OrderStatusEnum.ACTIVE.getKey());
                break;
            case 4:
                cnd.and("payType","in", OrderPayTypeEnum.CASH.getKey()+","+OrderPayTypeEnum.POS.getKey()+","+OrderPayTypeEnum.ALIQRCODE.getKey()).and("orderStatus","=",OrderStatusEnum.ACTIVE.getKey());
                break;
            default:
        }
        //订单时间降序
        cnd.desc("orderAt");
        NutMap map = orderMainService.data(length, start, draw, order, columns, cnd, "^(accountInfo|storeMain|goodsList)$");
        List<Order_main> orderMainList = (List<Order_main>) map.get("data");
        if(orderMainList != null){
            for(Order_main orderMain :orderMainList){
                Account_info accountInfo = orderMain.getAccountInfo();
                if(accountInfo != null){
                    orderMain.setAccountUser(accountUserService.getField("^(loginname|mobile)$",Cnd.where("accountId","=",accountInfo.getId())));
                }
                List<Order_goods> orderGoodsList = orderMain.getGoodsList();
                if(orderGoodsList != null){
                    for(Order_goods orderGoods:orderGoodsList){
                        orderGoods.setImgUrl(goodsProductService.getProductImage(orderGoods.getSku()));
                    }
                }

            }
        }
    	return map;
    }

    /**
     * 订单查看功能
     * @param id
     * @param req
     * @return
     */
    @RequestMapping("/detail/{id}")
    public String detail(@PathVariable String id, HttpServletRequest req) {
        if (!Strings.isBlank(id)) {
            Order_main orderMain = orderMainService.fetch(id);
            if(orderMain == null){
                return "redirect:/404";
            }
            orderMainService.fetchLinks(orderMain,"accountInfo");
            Account_user accountUser = accountUserService.fetch(Cnd.where("accountId","=",orderMain.getAccountId()));
            orderMain.setAccountUser(accountUser);
            req.setAttribute("obj", orderMain);
            List<Order_goods> orderGoodsList = orderGoodsService.query(Cnd.where("delFlag","=",false).and("orderId","=",id));
            //加载订单商品信息
            req.setAttribute("orderGoods",orderGoodsList);
            //物流信息
            List<Order_delivery_detail> orderDeliveryDetailList = orderDeliveryDetailService.query(Cnd.where("delFlag","=",false).and("orderId","=",id));
            String expressId ="";
            String expressName = "";
            if(orderDeliveryDetailList != null && orderDeliveryDetailList.size() > 0){
                for(Order_delivery_detail detail :orderDeliveryDetailList){
                    expressId += (expressId + ",");
                    expressName += (expressName + ",");
                }
                req.setAttribute("expressId",expressId.substring(0,expressId.lastIndexOf(",")));
                req.setAttribute("expressName",expressName.substring(0,expressName.lastIndexOf(",")));
            }
            //关闭订单原因
            List<Sys_dict> sysDictList = sysDictService.query(Cnd.where("delFlag","=",false).and("code","=","order_close_reason"));
            if(sysDictList != null && sysDictList.size() > 0){
                Sys_dict sysDict = sysDictList.get(0);
                List<Sys_dict> reasonList = sysDictService.query(Cnd.where("delFlag","=",false).and("parentId","=",sysDict.getId()));
                req.setAttribute("reasonList",reasonList);
            }
            //支付凭证信息
            List<Order_pay_transfer> orderPayTransferList = orderPayTransferService.query(Cnd.where("delFlag","=",false).and("orderId","=",id));
            req.setAttribute("payTransfers",orderPayTransferList);
            //售后信息
            List<Order_after_main> orderAfterMainList = orderAfterMainService.query(Cnd.where("orderId","=",id).and("delFlag","=",false));
            req.setAttribute("orderAfterMainList",orderAfterMainList);
            //订单日志信息
            List<Order_log> orderLogList = orderLogService.query(Cnd.where("delFlag","=",false).and("orderId","=",id).desc("opAt"));
            orderLogService.fetchLinks(orderLogList,"accountInfo");
            req.setAttribute("orderLogList",orderLogList);
            req.setAttribute("area",shopAreaService);
        }else{
            req.setAttribute("obj", null);
        }
        return "pages/platform/self/order/main/detail";
    }

    /**
     * 手工录单页面
     * @param req
     * @return
     */
    @RequestMapping("/add")
    @RequiresPermissions("self.order.main")
    public String add(HttpServletRequest req) {
        Cnd cnd = Cnd.NEW();
        cnd.and("delFlag","=",false);
        //查询物流信息
        List<Shop_express> shopExpressList = shopExpressService.query(cnd);
        //查询商城账号信息
        List<Shop_account> shopAccountList =  shopAccountService.query(cnd);
        req.setAttribute("expressList",shopExpressList);
        req.setAttribute("shopAccountList",shopAccountList);
    	return "pages/platform/self/order/main/add";
    }

    /**
     * 手工录单保存
     * @param orderMain
     * @param req
     * @return
     */
    @RequestMapping("/addDo")
    @SJson
    @SLog(description = "手工录单保存")
    @RequiresPermissions("self.order.main.add")
    public Object addDo(Order_main orderMain,
                        @RequestParam("goods")String goods,
                        @RequestParam("payments")String payments,
                        @RequestParam("uploadInfo")String uploadInfo, HttpServletRequest req) {
		try {
            //所选商品存订单商品表
            List<Goods_product> goodsProductList = Json.fromJsonAsList(Goods_product.class,goods);
            if(goodsProductList == null){
               return  Result.error("order.main.result.goodsError");
            }
            for(Goods_product goodsProduct:goodsProductList){
                if(goodsProduct.getSaleNumAll() > goodsProduct.getBuyMax() || goodsProduct.getSaleNumAll() < goodsProduct.getBuyMin()){
                    return  Result.error("order.main.result.buyNumError");
                }
            }
            //平台商户ID存入
            orderMain.setStoreId("2017060000000001");
            orderMainService.insertOrderByManual(orderMain,goodsProductList,payments,uploadInfo);
            return Result.success("globals.result.success");
		} catch (Exception e) {
            log.debug(e.getMessage(),e);
			return Result.error("globals.result.error");
		}
    }

    /**
     *上传凭证页面
     * @param req
     * @return
     */
    @RequestMapping("/upload/{id}")
    @RequiresPermissions("self.order.main")
    public String upload(@PathVariable String id,HttpServletRequest req) {
        if (!Strings.isBlank(id)) {
            req.setAttribute("obj", orderMainService.fetch(id));
            //加载支付凭证的信息
            List<Order_pay_transfer> orderPayTransferList = orderPayTransferService.query(Cnd.where("delFlag","=",false).and("orderId","=",id));
            req.setAttribute("payTransfers",orderPayTransferList);
        }else{
            req.setAttribute("obj", null);
        }
        return "pages/platform/self/order/main/upload";
    }

    /**
     * 上传支付凭证或者跳过
     * @param id
     * @param uploadInfo
     * @return
     */
    @RequestMapping("/uploadProof")
    @SJson
    @SLog(description = "上传支付凭证")
    @RequiresPermissions("self.order.main")
    public Object uploadProof(@RequestParam("id") String id,@RequestParam("uploadInfo") String uploadInfo){
        try {
            Order_main orderMain =  orderMainService.fetch(Cnd.where("id","=",id).and("delFlag","=",false));
            if(orderMain == null ){
                return  Result.error("订单不存在,请检查订单号");
            }
            //凭证录入[若凭证为空，则跳过，不为空，则录入]
            orderMainService.uploadProof(orderMain,uploadInfo);
            return  Result.success("globals.result.success");
        }catch (Exception e){
            log.debug(e.getMessage(),e);
            return Result.error("globals.result.error");
        }
    }

    /**
     *确认收款页面
     * @param req
     * @return
     */
    @RequestMapping("/receipt/{id}")
    @RequiresPermissions("self.order.main")
    public String receipt(@PathVariable String id,HttpServletRequest req) {
        if (!Strings.isBlank(id)) {
            //查询订单主表信息
            Order_main orderMain =  orderMainService.fetch(id);
            req.setAttribute("obj", orderMain);
            Cnd cnd = Cnd.NEW().and("delFlag","=",false);
            List<Order_goods> orderGoodsList = orderGoodsService.query(Cnd.where("delFlag","=",false).and("orderId","=",id));
            //加载订单商品信息
            req.setAttribute("orderGoods",orderGoodsList);
            //加载支付凭证的信息
            List<Order_pay_transfer> orderPayTransferList = orderPayTransferService.query(Cnd.where("delFlag","=",false).and("orderId","=",id));
            req.setAttribute("payTransfers",orderPayTransferList);
            //加载商城账号信息
            List<Shop_account> shopAccountList =  shopAccountService.query(cnd);
            req.setAttribute("shopAccountList",shopAccountList);
            //加载订单付款账号详情
            List<Order_pay_payment> orderPayPaymentList = orderPayPaymentService.query(Cnd.where("delFlag","=",false).and("orderId","=",id));
            req.setAttribute("orderPayPaymentList",orderPayPaymentList);
            //加载会员账户余额信息
            Member_account memberAccount = memberAccountService.fetch(Cnd.where("delFlag","=",false).and("accountId","=",orderMain.getAccountId()));
            req.setAttribute("memberAccount",memberAccount);
        }else{
            req.setAttribute("obj", null);
        }
        return "pages/platform/self/order/main/receipt";
    }

    @RequestMapping(value = {"/receiptSave"})
    @SJson
    @SLog(description = "确认收款")
    public Object receiptSave(@RequestParam("id") String id, @RequestParam("accountInfo") String  accountInfo,@RequestParam("uploadInfo") String uploadInfo,HttpServletRequest req){
        //查询当前订单主表
        Order_main orderMain = orderMainService.fetch(id);
        if(orderMain == null){
            return  Result.error("订单不存在");
        }
        orderMainService.confirmReceive(orderMain,accountInfo,uploadInfo);
        return  Result.success("globals.result.success");
    }

    /**
     *订单原价信息
     * @param req
     * @return
     */
    @RequestMapping("/costPrice/{id}")
    @RequiresPermissions("self.order.main")
    public String costPrice(@PathVariable String id,HttpServletRequest req) {
        if (!Strings.isBlank(id)) {
            //查询订单主表信息
            Order_main orderMain =  orderMainService.fetch(id);
            req.setAttribute("obj", orderMain);
            List<Order_goods> orderGoodsList = orderGoodsService.query(Cnd.where("delFlag","=",false).and("orderId","=",id));
            //加载订单商品信息
            req.setAttribute("orderGoods",orderGoodsList);
            //加载订单日志信息
            List<Order_log> orderLogList = orderLogService.query(Cnd.where("delFlag","=",false).and("orderId","=",id).desc("opAt"));
            req.setAttribute("orderLogList",orderLogList);
            req.setAttribute("area",shopAreaService);
        }else{
            req.setAttribute("obj", null);
        }
        return "pages/platform/self/order/main/costPrice";
    }

    /**
     * 原价恢复
     * @param id
     * @param req
     * @return
     */
    @RequestMapping("/costPriceRecover")
    @SJson
    @SLog(description = "原价恢复")
    public Object costPriceRecover(@RequestParam("id")String id,HttpServletRequest req){
            try{
                Order_main orderMain = orderMainService.fetch(Cnd.where("id","=",id).and("delFlag","=",false));
                orderMain.setOrderStatus(OrderStatusEnum.ACTIVE.getKey());
                orderMainService.update(orderMain);
                return Result.success("globals.result.success");
            }catch (Exception e){
                log.debug(e.getMessage(),e);
                return Result.error("globals.result.error");
            }
    }

    /**
     *订单现价信息
     * @param req
     * @return
     */
    @RequestMapping("/currentPrice/{id}")
    @RequiresPermissions("self.order.main")
    public String currentPrice(@PathVariable String id,HttpServletRequest req) {
        if (!Strings.isBlank(id)) {
            //查询订单主表信息
            Order_main orderMain =  orderMainService.fetch(id);
            req.setAttribute("obj", orderMain);
            List<Order_goods> orderGoodsList = orderGoodsService.query(Cnd.where("delFlag","=",false).and("orderId","=",id));
            //加载订单商品信息
            req.setAttribute("orderGoods",orderGoodsList);
            //加载订单现价商品信息
            List<Order_goods>  newOrderGoodsList = new ArrayList<>();
            for(Order_goods orderGoods: orderGoodsList){
                //根据sku查询
                Goods_product goodsProduct = goodsProductService.fetch(Cnd.where("delFlag","=",false).and("sku","=",orderGoods.getSku()));
                //变更购买价
                // TODO: 2017/5/23 购买价取货品的销售价
                orderGoods.setBuyPrice(goodsProduct.getSalePrice());
                newOrderGoodsList.add(orderGoods);
            }
            req.setAttribute("newOrderGoodsList",newOrderGoodsList);
            //加载订单日志信息
            List<Order_log> orderLogList = orderLogService.query(Cnd.where("delFlag","=",false).and("orderId","=",id).desc("opAt"));
            req.setAttribute("orderLogList",orderLogList);
            req.setAttribute("area",shopAreaService);
        }else{
            req.setAttribute("obj", null);
        }
        return "pages/platform/self/order/main/currentPrice";
    }

    /**
     * 现价恢复
     * @param id
     * @param req
     * @return
     */
    @RequestMapping("/currentPriceRecover")
    @SJson
    @SLog(description = "现价恢复")
    public Object currentPriceRecover(@RequestParam("id")String id,HttpServletRequest req){
        try{
            Order_main orderMain = orderMainService.fetch(Cnd.where("id","=",id).and("delFlag","=",false));
            orderMain.setOrderStatus(OrderStatusEnum.ACTIVE.getKey());
            orderMainService.update(orderMain);
            List<Order_goods> orderGoodsList = orderGoodsService.query(Cnd.where("delFlag","=",false).and("orderId","=",id));
            for(Order_goods orderGoods: orderGoodsList){
                //根据sku查询
                Goods_product goodsProduct = goodsProductService.fetch(Cnd.where("delFlag","=",false).and("sku","=",orderGoods.getSku()));
                //变更购买价
                orderGoods.setBuyPrice(goodsProduct.getSalePrice());
                //价格变更
                orderGoodsService.update(orderGoods);
            }
            return Result.success("globals.result.success");
        }catch (Exception e){
            log.debug(e.getMessage(),e);
            return Result.error("globals.result.error");
        }
    }


    /**
     * 删除订单（逻辑删除）
     * @param id
     * @param req
     * @return
     */
    @RequestMapping(value = {"/delOrder/{id}", "/delOrder"})
    @SJson
    @SLog(description = "删除订单")
    public Object delOrder(@PathVariable(required = false) String id,
                           @RequestParam(value = "ids",required = false)  String[] ids,
                           HttpServletRequest req) {
        try {
            if (ids != null && ids.length > 0) {
                // ids转化为字符串存进数组 作用：区分单删还是多选
                String[] ssids = ids[0].split(",");
                for (int i = 0; i < ssids.length; i++) {
                    String orderId = ssids[i];
                    //根据订单id，查询订单信息
                    Order_main orderMain = orderMainService.fetch(orderId);

                    //判断订单是否为空，为空则直接进行下一条数据
                    if(Lang.isEmpty(orderMain)){
                        continue;
                    }

                    //如果不为空，则判断订单是否符合删除的条件
                    if(orderMainService.checkDelOrder(orderMain)){
                        //符合删除的条件，则调用service的delOrder方法
                        orderMainService.delOrder(orderMain);
                    }else{
                        //如果不符合，则直接进行下一条数据
                        continue;
                    }
                }
            } else {
                //根据订单id，查询订单信息
                Order_main orderMain = orderMainService.fetch(id);

                //判断订单是否为空，为空则直接返回错误信息
                if(Lang.isEmpty(orderMain)){
                    return Result.error("order.main.noOrder.notice");
                }

                //如果不为空，则判断订单是否符合删除的条件
                if(orderMainService.checkDelOrder(orderMain)){
                    //符合删除的条件，则调用service的delOrder方法
                    orderMainService.delOrder(orderMain);
                }else{
                    //如果不符合，则直接返回错误信息
                    return Result.error("order.main.noDelOrder.notice");
                }
            }
            orderMainService.clearCache();
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    /**
     * 关闭订单
     * @param id
     * @param req
     * @return
     */
    @RequestMapping(value = {"/closeOrder/{id}", "/closeOrder"})
    @SJson
    @SLog(description = "关闭订单")
    public Object closeOrder(@PathVariable(required = false) String id,
                             @RequestParam("mark") String mark,
                             @RequestParam(value = "ids[]",required = false)  String[] ids,
                             HttpServletRequest req) {
        try {
            if (ids != null && ids.length > 0) {
                // ids转化为字符串存进数组 作用：区分单删还是多选
                String[] ssids = ids[0].split(",");
                for (int i = 0; i < ssids.length; i++) {
                    String orderId = ssids[i];
                    //根据订单id，查询订单信息
                    Order_main orderMain = orderMainService.fetch(orderId);

                    //订单是否为空，为空则直接进行下一条数据
                    if(orderMain == null){
                        continue;
                    }

                    //如果不为空，则判断订单是否符合关闭的条件
                    if(orderMainService.checkCloseOrder(orderMain)){
                        //符合关闭的条件，则调用service的closeOrder方法
                        orderMain.setMark(mark);
                        orderMainService.closeOrder(orderMain);
                    }else{
                        //如果不符合，则直接进行下一条数据
                        continue;
                    }
                }
            } else {
                //根据订单id，查询订单信息
                Order_main orderMain = orderMainService.fetch(id);

                //判断订单是否为空，为空则直接返回错误信息
                if(orderMain == null){
                    return Result.error("order.main.noOrder.notice");
                }

                //如果不为空，则判断订单是否符合关闭的条件
                if(orderMainService.checkCloseOrder(orderMain)){
                    //符合关闭的条件，则调用service的closeOrder方法
                    orderMain.setMark(mark);
                    orderMainService.closeOrder(orderMain);
                }else{
                    //如果不符合，则直接返回错误信息
                    return Result.error("order.main.noCloseOrder.notice");
                }
            }
            orderMainService.clearCache();
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    /**
     * 保存订单审核
     * @param id
     * @param ids
     * @param checkStatus
     * @param comment
     * @return
     */
    @RequestMapping(value = {"/audit", "/audit/{id}"})
    @SJson
    @SLog(description = "订单审核")
    public Object audit(@PathVariable(required = false) String id, @RequestParam("ids") String[] ids,
                        @RequestParam("checkStatus") Integer checkStatus, @RequestParam("comment") String comment) {
        try {
            if (ids != null && ids.length > 0) {
                // ids转化为字符串存进数组 作用：区分单删还是多选
                String[] ssids = ids[0].split(",");
                for (int i = 0; i < ssids.length; i++) {
                    String orderId = ssids[i];
                    //根据订单id，查询订单信息
                    Order_main orderMain = orderMainService.fetch(orderId);

                    //订单是否为空，为空则直接进行下一条数据
                    if(Lang.isEmpty(orderMain)){
                        continue;
                    }

                    //如果不为空，则判断订单是否符合审核的条件
                    if(orderMainService.checkAuditOrder(orderMain)){
                        //符合关闭的条件，则调用service的audit方法
                        orderMainService.audit(orderMain,checkStatus,comment);
                    }else{
                        //如果不符合，则直接进行下一条数据
                        continue;
                    }
                }
            } else {
                //根据订单id，查询订单信息
                Order_main orderMain = orderMainService.fetch(id);

                //判断订单是否为空，为空则直接返回错误信息
                if(Lang.isEmpty(orderMain)){
                    return Result.error("order.main.noOrder.notice");
                }

                //如果不为空，则判断订单是否符合审核的条件
                if(orderMainService.checkAuditOrder(orderMain)){
                    //符合关闭的条件，则调用service的audit方法
                    orderMainService.audit(orderMain,checkStatus,comment);
                }else{
                    //如果不符合，则直接返回错误信息
                    return Result.error("order.main.noAuditOrder.notice");
                }
            }
            orderMainService.clearCache();
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    /**
     * 到订单改地址页面
     * @param id
     * @param req
     * @return
     */
    @RequestMapping("/changeAddress/{id}")
    @RequiresPermissions("self.order.main")
    public String changeAddress(@PathVariable String id,HttpServletRequest req) {
        Order_main obj = orderMainService.fetch(id);
        req.setAttribute("obj", obj);
        req.setAttribute("area",shopAreaService);
        return "pages/platform/self/order/main/changeAddress";
    }

    /**
     * 保存订单改地址
     * @param orderMain
     * @param req
     * @return
     */
    @RequestMapping("/changeAddressDo")
    @SJson
    @SLog(description = "订单改地址")
    public Object changeAddressDo(Order_main orderMain, HttpServletRequest req) {
        try {
            orderMain.setOpBy(StringUtil.getUid());
            orderMain.setOpAt((int) (System.currentTimeMillis() / 1000));
            orderMainService.changeAddress(orderMain);
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    /**
     * 到订单改价页面
     * @param id
     * @param req
     * @return
     */
    @RequestMapping("/changePrice/{id}")
    public String changePrice(@PathVariable String id, HttpServletRequest req) {
        Order_main obj = orderMainService.fetch(id);
        orderMainService.fetchLinks(obj,"goodsList");
        Account_user accountUser = accountUserService.fetch(Cnd.where("accountId","=",obj.getAccountId()));
        accountUserService.fetchLinks(accountUser,"accountInfo");
        req.setAttribute("obj", obj);
        //加载会员信息
        req.setAttribute("accountUser",accountUser);
        req.setAttribute("area",shopAreaService);
        return "pages/platform/self/order/main/changePrice";
    }

    /**
     * 保存订单改价
     * @param orderMain
     * @param req
     * @return
     */
    @RequestMapping("/changePriceDo")
    @SJson
    @SLog(description = "订单改价")
    public Object changePriceDo(Order_main orderMain, @RequestParam("goods") String goods,HttpServletRequest req) {
        try {
            orderMain.setOpBy(StringUtil.getUid());
            orderMain.setOpAt((int) (System.currentTimeMillis() / 1000));
            List<Order_goods> orderGoodsList = Json.fromJsonAsList(Order_goods.class,goods);
            //这里提示改价的商品为空判断
            if(orderGoodsList == null){
                return Result.error("order.main.noChangePriceGoods.tip");
            }
            orderMainService.changePrice(orderMain,orderGoodsList);
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    /**
     * 创建配货单页面
     * @param id
     * @param req
     * @return
     */
    @RequestMapping("/createDelivery/{id}")
    public String delivery(@PathVariable String id, HttpServletRequest req){
        //获取订单信息，并传值到页面
        Order_main obj = orderMainService.fetch(id);
        req.setAttribute("obj", obj);
        //根据条件获取符合创建配货单的订单信息
        SqlExpressionGroup e1 = Cnd.exps("deliveryStatus", "=", OrderDeliveryStatusEnum.NONE.getKey()).or("deliveryStatus","=",OrderDeliveryStatusEnum.SOME.getKey());
        SqlExpressionGroup e2 = Cnd.exps("payStatus","=",OrderPayStatusEnum.PAYALL.getKey()).or("payType","in",OrderPayTypeEnum.CASH.getKey()+","+OrderPayTypeEnum.POS.getKey()+","+OrderPayTypeEnum.ALIQRCODE.getKey());
        List<Order_main> orderList = orderMainService.query(
                Cnd.where("accountId", "=", obj.getAccountId()) //同一会员
                        .and(e1)//发货状态为未发货和部分发货
                        .and(e2)
                        .and("storeId","=","2017060000000001")
                        .and("orderStatus","=",OrderStatusEnum.ACTIVE.getKey())
                        .and("delFlag","=",false)
                        .asc("id"));
        //todo 是否可以发同一个物流
        //当前可以配货的订单
        List<Order_main> newOrderList = new ArrayList<>();
        if(orderList != null){
            for(Order_main orderMain : orderList){
                orderMainService.fetchLinks(orderMain,"goodsList");
                List<Order_goods> orderGoodsList = orderMain.getGoodsList();
                //这里需判断发货数量是否小于购买数量
                if(orderGoodsList != null){
                    for(Order_goods orderGoods : orderGoodsList){
                        Goods_product goodsProduct = goodsProductService.getField("deliveryTime",Cnd.where("sku","=",orderGoods.getSku()));
                        if(goodsProduct != null){
                            orderGoods.setDeliveryTime(goodsProduct.getDeliveryTime());
                        }
                        //这个判断是当前订单还有数量未配货
                        if(orderGoods.getBuyNum() > orderGoods.getSendNum()){
                            newOrderList.add(orderMain);
                            break;
                        }
                    }
                }
            }
        }
        req.setAttribute("orderList", newOrderList);
        return  "pages/platform/self/order/main/createDelivery";
    }

}

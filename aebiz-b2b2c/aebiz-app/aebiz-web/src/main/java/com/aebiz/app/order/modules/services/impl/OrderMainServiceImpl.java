package com.aebiz.app.order.modules.services.impl;

import com.aebiz.app.goods.modules.models.Goods_main;
import com.aebiz.app.goods.modules.models.Goods_product;
import com.aebiz.app.goods.modules.models.em.GoodsSaleClientEnum;
import com.aebiz.app.goods.modules.models.em.GoodsStockOffTypeEnum;
import com.aebiz.app.goods.modules.services.GoodsPriceService;
import com.aebiz.app.goods.modules.services.GoodsProductService;
import com.aebiz.app.goods.modules.services.GoodsService;
import com.aebiz.app.member.modules.models.Member_account;
import com.aebiz.app.member.modules.models.Member_address;
import com.aebiz.app.member.modules.services.MemberAccountService;
import com.aebiz.app.member.modules.services.MemberCartService;
import com.aebiz.app.order.commons.vo.OrderGoodsVO;
import com.aebiz.app.order.commons.vo.OrderStoreVO;
import com.aebiz.app.order.modules.models.*;
import com.aebiz.app.order.modules.models.em.*;
import com.aebiz.app.order.modules.services.*;
import com.aebiz.app.sales.modules.commons.vo.StoreSalesVO;
import com.aebiz.app.sales.modules.services.SalesCouponService;
import com.aebiz.app.sales.modules.services.SalesRuleGoodsService;
import com.aebiz.app.sales.modules.services.SalesRuleOrderService;
import com.aebiz.app.shop.modules.models.Shop_express;
import com.aebiz.app.shop.modules.services.ShopExpressService;
import com.aebiz.app.store.modules.commons.vo.StoreFreightProduct;
import com.aebiz.app.store.modules.services.StoreFreightService;
import com.aebiz.baseframework.base.service.BaseServiceImpl;
import com.aebiz.baseframework.rabbit.RabbitMessage;
import com.aebiz.baseframework.rabbit.RabbitProducer;
import com.aebiz.baseframework.redis.RedisService;
import com.aebiz.commons.utils.SpringUtil;
import com.aebiz.commons.utils.StringUtil;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.json.Json;
import org.nutz.lang.Lang;
import org.nutz.lang.Strings;
import org.nutz.lang.util.NutMap;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderMainServiceImpl extends BaseServiceImpl<Order_main> implements OrderMainService {

    private static final Log log = Logs.get();

    @Resource(name = "nutDao", type = Dao.class)
    public void init(Dao dao) {
        super.setDao(dao);
    }

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private OrderGoodsService orderGoodsService;

    @Autowired
    private OrderGroupService orderGroupService;

    @Autowired
    private OrderPayPaymentService orderPayPaymentService;

    @Autowired
    private OrderPayTransferService orderPayTransferService;

    @Autowired
    private GoodsProductService goodsProductService;

    @Autowired
    private OrderLogService orderLogService;

    @Autowired
    private MemberAccountService  memberAccountService;

    @Autowired
    private MemberCartService memberCartService;

    @Autowired
    private GoodsPriceService goodsPriceService;

    @Autowired
    private StoreFreightService storeFreightService;

    @Autowired
    private ShopExpressService shopExpressService;

    @Autowired
    private SalesRuleGoodsService salesRuleGoodsService;

    @Autowired
    private SalesRuleOrderService salesRuleOrderService;

    @Autowired
    private SalesCouponService salesCouponService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private RabbitProducer rabbitProducer;

    /**
     * 清除缓存
     */
    public void clearCache(){

    }

    /**
     * 前台下单
     * @param memberAddress 会员地址
     * @param storeVOList 下单信息
     * @param payType 支付方式
     * @return
     */
    @Transactional
    public String createOrder(Member_address memberAddress, List<OrderStoreVO> storeVOList, Integer payType, String memberLevelId,boolean isNowBuy){
        //获取当前会员账号Id
        String accountId = StringUtil.getMemberUid();
        //对购买的商品进行分组
        //Map<String,List<Member_cart>> map = OrderUtil.storeCart(memberCartList);
        //创建一个订单组
        Order_group orderGroup = new Order_group();
        orderGroup.setAccountId(accountId);
        orderGroup.setDelFlag(false);
        orderGroup.setOpBy(accountId);
        orderGroup.setOpAt((int)(System.currentTimeMillis()/1000));
        orderGroup =  orderGroupService.insert(orderGroup);
        List<String> cart_ids = new ArrayList<>();
        for(OrderStoreVO storeVO: storeVOList){
            //根据不同的店铺生成不同的订单
            Order_main orderMain = new Order_main();
            orderMain.setGroupId(orderGroup.getId());
            orderMain.setAccountId(accountId);
            orderMain.setStoreId(storeVO.getId());
            orderMain.setNote(storeVO.getOrderNote());
            orderMain = this.insert(orderMain);
            List<OrderGoodsVO> orderGoodsVOList = storeVO.getGoodsList();
            //初始化商品总金额
            int goodsMoney = 0;
            //初始化商品应付金额
            int goodsPayMoney = 0;
            //初始化商品总重量
            int goodsWeight = 0;
            //初始化运费
            int freightMoney = 0;
            //初始化积分
            int goodsScore = 0;

            List<StoreFreightProduct> storeFreightProductList = new ArrayList<>();
            for(OrderGoodsVO orderGoodsVO : orderGoodsVOList){
                if(!isNowBuy){
                    cart_ids.add(orderGoodsVO.getSku());
                }
                cart_ids.add(orderGoodsVO.getSku());
                Order_goods orderGoods = new Order_goods();
                orderGoods.setOrderId(orderMain.getId());
                orderGoods.setAccountId(accountId);
                orderGoods.setStoreId(storeVO.getId());
                Goods_product goodsProduct =  goodsProductService.getBySku(orderGoodsVO.getSku());
                 orderGoods.setGoodsId(goodsProduct.getGoodsId());
                orderGoods.setGoodsName(goodsProduct.getGoodsMain().getName());
                orderGoods.setGoodsVersion(goodsProduct.getGoodsMain().getGoodsVersion());
                orderGoods.setProductId(goodsProduct.getId());
                orderGoods.setSku(orderGoodsVO.getSku());
                orderGoods.setName(goodsProduct.getName());
                orderGoods.setSpec(goodsProduct.getSpec());
                orderGoods.setProductVersion(goodsProduct.getProductVersion());
                orderGoods.setBuyNum(orderGoodsVO.getNum());
                orderGoods.setSalePrice(goodsProduct.getSalePrice());
                int buyPrice;
                if(Strings.isNotBlank(orderGoodsVO.getGoodsSalesId())){
                    buyPrice =  salesRuleGoodsService.price(orderGoodsVO.getSku(),memberAddress.getCounty(),memberLevelId,orderGoodsVO.getGoodsSalesId());
                }else{
                    buyPrice =  goodsPriceService.price(orderGoodsVO.getSku(),null, GoodsSaleClientEnum.PC.getKey(),null);
                }
                orderGoods.setBuyPrice(buyPrice);
                orderGoods.setTotalWeight(goodsProduct.getWeight()*orderGoodsVO.getNum());
                goodsWeight += orderGoods.getTotalWeight();
                orderGoods.setTotalMoney(goodsProduct.getSalePrice()*orderGoodsVO.getNum());
                goodsMoney += orderGoods.getTotalMoney();
                int freeMoney = (goodsProduct.getSalePrice()-buyPrice)*orderGoodsVO.getNum();
                orderGoods.setFreeMoney(freeMoney);
                orderGoods.setPayMoney(orderGoods.getTotalMoney() - freeMoney);
                goodsPayMoney += orderGoods.getPayMoney();
                orderGoods.setOpBy(accountId);
                orderGoods.setOpAt((int)(System.currentTimeMillis()/1000));
                orderGoods.setDelFlag(false);
                orderGoodsService.insert(orderGoods);
                //运费
                StoreFreightProduct storeFreightProduct = new StoreFreightProduct();
                storeFreightProduct.setSku(orderGoodsVO.getSku());
                storeFreightProduct.setNum(orderGoodsVO.getNum());
                storeFreightProductList.add(storeFreightProduct);
            }
            orderMain.setGoodsMoney(goodsMoney);
            orderMain.setGoodsPayMoney(goodsPayMoney);
            orderMain.setGoodsWeight(goodsWeight);
            //计算运费
            freightMoney = storeFreightService.countFreight(storeFreightProductList,memberAddress.getProvince(),null,null,storeVO.getId());

            //获取店铺优惠
            //判断店铺促销是否存在
            if(Lang.isEmpty(storeVO.getStoreSales())){
                orderMain.setFreightMoney(freightMoney);
                orderMain.setFreeMoney(0);
            }else{
                StoreSalesVO storeSalesVO = salesRuleOrderService.storeSales(storeVO.getStoreSales().getSalesId(),goodsScore,goodsPayMoney,freightMoney);
                orderMain.setFreightMoney(storeSalesVO.getFreightMoney());
                orderMain.setFreeMoney(storeSalesVO.getFreeMoney());
            }
            orderMain.setPayMoney(goodsPayMoney +orderMain.getFreightMoney() - orderMain.getFreeMoney());
            orderMain.setPayType(payType);
            //orderMain.setCurrency();
            orderMain.setPayStatus(OrderPayStatusEnum.NO.getKey());
            orderMain.setOrderAt((int)(System.currentTimeMillis()/1000));
            orderMain.setOrderSrc(OrderSourceEnum.PC.getKey());
            orderMain.setOrderStatus(OrderStatusEnum.ACTIVE.getKey());
            orderMain.setDeliveryName(memberAddress.getFullName());
            orderMain.setDeliveryProvince(memberAddress.getProvince());
            orderMain.setDeliveryCity(memberAddress.getCity());
            orderMain.setDeliveryCounty(memberAddress.getCounty());
            orderMain.setDeliveryTown(memberAddress.getTown());
            orderMain.setDeliveryAddress(memberAddress.getAddress());
            orderMain.setDeliveryMobile(memberAddress.getMobile());
            orderMain.setDeliveryPhone(memberAddress.getPhone());
            orderMain.setDeliveryPostcode(memberAddress.getPostCode());
            //orderMain.setDeliveryTime("当日发货");
            orderMain.setDeliveryNeed(true);
            orderMain.setOpBy(accountId);
            orderMain.setDelFlag(false);
            orderMain.setOrderAt((int)(System.currentTimeMillis()/1000));
            this.update(orderMain);
            //记录订单创建日志
            StringBuilder note = new StringBuilder("PC端创建订单");
            orderLogService.createLog(orderMain,StringUtil.getMemberUsername(),note.toString(),OrderLogBehaviorEnum.CREATE.getKey());
        }
        //redis存入订单信息,并设置有效时间2小时
        try (Jedis jedis = redisService.jedis()) {
            jedis.set("aebiz-order:"+orderGroup.getId(), Json.toJson(storeVOList));
            jedis.expire("aebiz-order:"+orderGroup.getId(), 60*60*2);
        }
        //向mq里发消息
        if (SpringUtil.isRabbitEnabled()) {
            String exchange = "topicExchange";
            String routeKey = "topic.order.create";
            RabbitMessage msg = new RabbitMessage(exchange, routeKey, NutMap.NEW().addv("orderId",orderGroup.getId()));
            rabbitProducer.sendMessage(msg);
        } else {
            this.updateOrderGoodsStock(orderGroup.getId());
        }

        if(cart_ids.size() > 0){
            //删除购物车数据
            memberCartService.clear(Cnd.where("sku","in",cart_ids.toArray(new String[0])));
        }
        return orderGroup.getId();
    }

    /**
     * 手工录单
     * @param orderMain 主订单
     * @param goodsProductList  货品详情
     * @param payments 支付详情
     * @param uploadInfo 凭证详情
     */
    @Transactional
    public void insertOrderByManual(Order_main orderMain,List<Goods_product> goodsProductList, String payments, String uploadInfo)  {
        StringBuilder note = new StringBuilder("手动录单");
        //创建订单组,目前只有一个自营的订单组
        Order_group orderGroup = new Order_group();
        orderGroup.setAccountId(orderMain.getAccountId());
        orderGroup.setOpBy(StringUtil.getUsername());
        orderGroup.setOpAt((int)(System.currentTimeMillis()/1000));
        orderGroup.setDelFlag(false);
        //订单组入库
        orderGroup = orderGroupService.insert(orderGroup);
        orderMain.setGroupId(orderGroup.getId());
        orderMain.setOrderSrc(OrderSourceEnum.HAND.getKey());//手动录单
        orderMain.setOrderStatus(OrderStatusEnum.ACTIVE.getKey());//待审核订单
        //orderMain.setGoodsMoney(orderMain.getGoodsMoney());
        Integer goodsMoney = 0;//商品总金额
        //orderMain.setGoodsPayMoney(orderMain.getGoodsPayMoney());
        Integer goodsPayMoney = 0;//商品应付金额
        //orderMain.setFreightMoney(orderMain.getFreightMoney());
        Integer freightMoney = 0;//运费
        orderMain.setFreeMoney(orderMain.getFreeMoney());
        orderMain.setPayMoney(orderMain.getPayMoney());
        orderMain.setOrderAt((int)(System.currentTimeMillis()/1000));
        orderMain.setOpBy(StringUtil.getUid());
        //插入库中后获取当前订单对象
        orderMain =  this.insert(orderMain);
        List<StoreFreightProduct> storeFreightProductList = new ArrayList<>();
        if(goodsProductList != null){
            for(Goods_product goodsProduct :goodsProductList){
                //创建运费计算的货品VO对象
                StoreFreightProduct storeFreightProduct = new StoreFreightProduct();
                Order_goods orderGoods = new Order_goods();
                orderGoods.setOrderId(orderMain.getId());
                orderGoods.setAccountId(orderMain.getAccountId());
                orderGoods.setStoreId(goodsProduct.getStoreId());
                orderGoods.setGoodsId(goodsProduct.getGoodsId());
                Goods_main goodsMain = goodsService.fetch(goodsProduct.getGoodsId());
                if(goodsMain != null){
                    orderGoods.setGoodsVersion(goodsMain.getGoodsVersion());
                }
                orderGoods.setGoodsName(goodsMain.getName());
                orderGoods.setProductId(goodsProduct.getId());
                orderGoods.setSku(goodsProduct.getSku());
                storeFreightProduct.setSku(orderGoods.getSku());
                orderGoods.setName(goodsProduct.getName());
                orderGoods.setSpec(goodsProduct.getSpec());
                orderGoods.setProductVersion(goodsProduct.getProductVersion());
                //以后从页面进行传购买数量
                Integer buyNum = goodsProduct.getSaleNumAll();
                orderGoods.setBuyNum(buyNum);
                storeFreightProduct.setNum(buyNum);
                //加入
                storeFreightProductList.add(storeFreightProduct);
                orderGoods.setSalePrice(goodsProduct.getSalePrice());
                //购买从价格接口中取
                orderGoods.setBuyPrice(goodsPriceService.getSalePrice(goodsProduct.getSku(),null,null,null, GoodsSaleClientEnum.PC.getKey(),0,null));
                Integer totalWeight = goodsProduct.getWeight()*buyNum;
                orderGoods.setTotalWeight(totalWeight);
                Integer totalMoney = orderGoods.getBuyPrice()*buyNum;
                goodsMoney += totalMoney;//合计
                orderGoods.setTotalMoney(totalMoney);
                // TODO: 2017/5/12  以后要从优惠渠道取数,暂时设置为0
                Integer freeMoney = 0;
                orderGoods.setFreeMoney(freeMoney);
                orderGoods.setPayMoney(totalMoney-freeMoney);
                goodsPayMoney += orderGoods.getPayMoney();
                //// TODO: 2017/5/12 积分暂时设置为0,以后从积分系统取数
                Integer score = 0;
                orderGoods.setScore(score);
                orderGoods.setOpBy(StringUtil.getUid());
                orderGoods.setOpAt((int)(System.currentTimeMillis()/1000));
                orderGoods.setDelFlag(false);
                //订单商品录入
                orderGoodsService.insert(orderGoods);
                //1.判断该货品的商品配置是否为空 2.如果该商品是下单时减库存,则更新库存
                if(goodsMain != null && goodsMain.getStockOffType() != null){
                    if(GoodsStockOffTypeEnum.ORDER.getKey() == goodsMain.getStockOffType()){
                        goodsProductService.updateStock(orderGoods.getSku(),buyNum,true);
                    }
                }
            }
        }
        //计算运费
        Shop_express shopExpress = shopExpressService.getField("code",orderMain.getId());
        String logisticsCode = null;
        if(shopExpress != null && Strings.isNotBlank(shopExpress.getCode())){
            logisticsCode = shopExpress.getCode();
        }
        freightMoney = storeFreightService.countFreight(storeFreightProductList,orderMain.getDeliveryProvince(),logisticsCode,null,orderMain.getStoreId());

        //更新订单商品总金额 商品应付金额 运费
        this.update(Chain.make("goodsMoney",goodsMoney).add("goodsPayMoney",goodsPayMoney).add("freightMoney",freightMoney),Cnd.where("id","=",orderMain.getId()));

        //订单支付表
        List<Order_pay_payment> orderPayPaymentList = Json.fromJsonAsList(Order_pay_payment.class,payments);
        if(orderPayPaymentList != null){
           orderPayPaymentService.add(orderPayPaymentList,orderMain,false);
        }
        //订单支付凭证
        this.saveUploadInfo(orderGroup.getId(),orderMain.getId(),orderMain.getAccountId(),orderMain.getStoreId(),uploadInfo);
        //2、记录手动录单日志
        orderLogService.createLog(orderMain,StringUtil.getUsername(),note.toString(),OrderLogBehaviorEnum.CREATE.getKey());
    }

    /**
     * 上传凭证
     * @param orderMain
     * @param uploadInfo
     */
    @Transactional
    public void uploadProof(Order_main orderMain,String uploadInfo) {
        StringBuilder note = new StringBuilder("上传凭证");
        //先删除旧的凭证数据
        List<Order_pay_transfer> transferListr = orderPayTransferService.query(Cnd.NEW().and("orderId","=",orderMain.getId()));
        for(Order_pay_transfer orderPayTransfer:transferListr){
            orderPayTransfer.setDelFlag(true);
            orderPayTransfer.setOpBy(StringUtil.getUid());
            orderPayTransfer.setOpAt((int)(System.currentTimeMillis()/1000));
            orderPayTransferService.update(orderPayTransfer);
        }
        //保存支付凭证
        this.saveUploadInfo(orderMain.getGroupId(),orderMain.getId(),orderMain.getAccountId(),orderMain.getStoreId(),uploadInfo);
        //更新状态
        orderMain.setPayStatus(OrderPayStatusEnum.WAITVERIFY.getKey());
        orderMain.setOpBy(StringUtil.getUid());
        orderMain.setOpAt((int) (System.currentTimeMillis() / 1000));
        this.updateIgnoreNull(orderMain);
        //2、记录手动录单日志
        orderLogService.createLog(orderMain,StringUtil.getUsername(),note.toString(),OrderLogBehaviorEnum.UPDATE.getKey());
    }


    /**
     * 批量保存支付凭证
     */
    public void saveUploadInfo(String groupId,String mainId,String accountId,String storeId,String uploadInfo){
        //订单支付凭证
        if(Strings.isNotBlank(uploadInfo)) {
            List<String> uploadImageList = Json.fromJsonAsList(String.class, uploadInfo);
            for (String uploadImage : uploadImageList) {
                Order_pay_transfer orderPayTransfer = new Order_pay_transfer();
                orderPayTransfer.setGroupId(groupId);
                orderPayTransfer.setOrderId(mainId);
                // TODO: 2017/5/12 上传凭证的账号 ID是取收款人的ID还是付款人的ID
                orderPayTransfer.setAccountId(accountId);
                orderPayTransfer.setStoreId(storeId);
                orderPayTransfer.setUploadImage(uploadImage);
                orderPayTransfer.setUploadAt((int) (System.currentTimeMillis() / 1000));
                orderPayTransfer.setOpBy(StringUtil.getUsername());
                orderPayTransfer.setOpAt((int) (System.currentTimeMillis() / 1000));
                orderPayTransfer.setDelFlag(false);
                orderPayTransferService.insert(orderPayTransfer);
            }
        }
    }

    /**
     * 删除订单（逻辑删除）
     * @param orderMain
     * @return
     */
    @Transactional
    public void delOrder(Order_main orderMain){
        //1、更新订单状态(delFlag)
        orderMain.setDelFlag(true);
        orderMain.setOpAt((int) (System.currentTimeMillis() / 1000));
        orderMain.setOpBy(StringUtil.getUid());
        this.dao().update(orderMain);

        //2、记录订单删除日志
        orderLogService.createLog(orderMain,StringUtil.getUsername(),"删除订单", OrderLogBehaviorEnum.CANCEL.getKey());
    }

    /**
     * 关闭订单
     * @param orderMain
     * @return
     */
    @Transactional
    public void closeOrder(Order_main orderMain){
        //1、更新订单状态(订单状态字段)
        orderMain.setOrderStatus(OrderStatusEnum.DEAD.getKey()); //订单状态（关闭的订单）
        orderMain.setOpAt((int) (System.currentTimeMillis() / 1000));
        orderMain.setOpBy(StringUtil.getUid());
        this.dao().update(orderMain);

        //2、记录订单删除日志
        orderLogService.createLog(orderMain,StringUtil.getUsername(),"关闭订单", OrderLogBehaviorEnum.UPDATE.getKey());

        //3、遍历订单明细表，如果商品是下单时锁定库存，则更新货品表中的库存信息
        //TODO 如果是下单时锁定库存，则更新货品表中的库存信息
        List<Order_goods> orderGoodsList = orderGoodsService.query(Cnd.NEW().where("delFlag","=",false).and("orderId","=",orderMain.getId()));
        if(orderGoodsList != null){
            for(Order_goods orderGoods : orderGoodsList){
                try {
                    goodsProductService.updateStock(orderGoods.getSku(),orderGoods.getBuyNum(),false);
                } catch (Exception e) {
                   log.debug(e.getMessage(),e);
                }

            }
        }
    }

    /**
     * 订单审核流程（根据审核的状态进行操作）：
     * 1、审核通过：
     *  （1）将订单待审核状态 改为 有效订单
     *  （2）记录订单的审核意见
     *  （3）记录审核日志
     * 2、审核不通过：
     *  （1）将订单待审核状态 改为 无效订单
     *  （2）记录订单的审核意见
     *  （3）记录审核日志
     * @param orderMain
     */
    @Transactional
    public void audit(Order_main orderMain,Integer checkStatus, String comment){
        StringBuffer noteBuffer = new StringBuffer("审核订单");
        if(OrderCheckStatusEnum.PASS.getKey() == checkStatus){
            //订单审核通过
            //1、更新订单状态(订单状态字段)--将待支审核状态 改为 有效订单
            orderMain.setOrderStatus(OrderStatusEnum.ACTIVE.getKey()); //订单状态（有效订单）

            //构造note
            noteBuffer.append("-").append(OrderCheckStatusEnum.PASS.getValue());
        }else{
            //订单审核通过
            //1、更新订单状态(订单状态字段)--将待支审核状态 改为 无效订单
            orderMain.setOrderStatus(OrderStatusEnum.DEAD.getKey()); //订单状态（关闭的订单）

            //构造note
            noteBuffer.append("-").append(OrderCheckStatusEnum.NOPASS.getValue());
        }

        if(Strings.isNotBlank(comment)){
            noteBuffer.append("<br/>").append(comment);
            orderMain.setMark(comment);
        }

        orderMain.setOpAt((int) (System.currentTimeMillis() / 1000));
        orderMain.setOpBy(StringUtil.getUid());
        this.dao().update(orderMain);

        //2、记录订单审核日志
        orderLogService.createLog(orderMain,StringUtil.getUsername(),noteBuffer.toString(),OrderLogBehaviorEnum.UPDATE.getKey());
    }

    /**
     * 订单改地址
     * 1）修改主表的地址信息
     * 2）记录修改地址日志
     * @param orderMain
     */
    @Transactional
    public void changeAddress(Order_main orderMain){
        StringBuilder note = new StringBuilder("订单改地址");
        //1、更新订单(订单收货地址相关字段)
        this.updateIgnoreNull(orderMain);
        //2、记录订单审核日志
        orderLogService.createLog(orderMain,StringUtil.getUsername(),note.toString(),OrderLogBehaviorEnum.UPDATE.getKey());
    }

    /**
     * 订单改价
     * 1）更新订单主表
     * 2）更新订单货品价格
     * 3）记录订单日志
     * @param orderMain
     */
    @Transactional
    public void changePrice(Order_main orderMain, List<Order_goods> orderGoodsLists){
            List<Order_goods> goods=orderGoodsService.query(Cnd.where("orderId","=",orderMain.getId()));
            StringBuilder note = new StringBuilder("订单号"+orderMain.getId()+"订单改价");
            //更新订单主表
            this.updateIgnoreNull(orderMain);
            //更新订单货品价格
            for(Order_goods orderGoods:orderGoodsLists){
                orderGoodsService.updateIgnoreNull(orderGoods);
            }
            //2、记录订单改价日志
            orderLogService.createLog(orderMain,StringUtil.getUsername(),note.toString(),OrderLogBehaviorEnum.UPDATE.getKey());
    }

    /**
     * 确认收款
     * @param orderMain
     * @param accountInfo
     * @param uploadInfo
     */
    @Override
    @Transactional
    public void confirmReceive(Order_main orderMain, String accountInfo, String uploadInfo) {
        StringBuilder note = new StringBuilder("确认收款");
        String id = orderMain.getId();
        //初始化支付金额
        int hasPayMoney = 0;
        //查询当前已支付信息
        List<Order_pay_payment> orderPayPaymentList =  orderPayPaymentService.query(Cnd.NEW().where("orderId","=",id).and("delFlag","=",false));
        if(orderPayPaymentList != null){
            for(Order_pay_payment orderPayPayment :orderPayPaymentList){
                //已支付的数据不为空,则更新支付状态
                hasPayMoney += orderPayPayment.getPayMoney();
                orderPayPayment.setPaySucess(true);
                orderPayPaymentService.update(orderPayPayment);
            }
        }
        //新填写的订单支付信息
        List<Order_pay_payment> newOrderPayPaymentList = Json.fromJsonAsList(Order_pay_payment.class,accountInfo);
        if(newOrderPayPaymentList != null){
            orderPayPaymentService.add(newOrderPayPaymentList,orderMain,true);
            for(Order_pay_payment newPay: newOrderPayPaymentList){
                hasPayMoney += newPay.getPayMoney();
            }
        }
        //计算当前商品应付金额与确认收款金额差值
        int diffMoney = hasPayMoney - orderMain.getPayMoney();
        //查询当前会员的余额
        Member_account memberAccount =  memberAccountService.fetch(Cnd.NEW().where("accountId","=",orderMain.getAccountId()).and("delFlag","=",false));
        //初始化余额
        int balance = 0;
        if(memberAccount != null){
            balance = memberAccount.getMoney();
        }
       /* if(hasPayMoney == 0 && balance ==0){
            return Result.error("当前会员没有金额可以支付");
        }*/
        if(diffMoney >= 0){
            //更新订单状态
            orderMain.setPayStatus(OrderPayStatusEnum.PAYALL.getKey());
            orderMain.setPayAt((int) (System.currentTimeMillis() / 1000));
            //若支付金额大于订单应付金额，更新会员账户余额
            if(diffMoney > 0){
                memberAccount.setMoney(balance+diffMoney);
            }
            //当确认收款完全支付完,则更新库存
            //1.查询当前订单购买的货品
            List<Order_goods> orderGoodsList = orderGoodsService.query(Cnd.NEW().where("delFlag","=",false).and("orderId","=",id));
            if(orderGoodsList != null){
                for(Order_goods orderGoods: orderGoodsList){
                    //获取商品的信息
                    Goods_main goodsMain =  goodsService.fetch(orderGoods.getGoodsId());
                    //若商品不为空,则判断货品的支付类型，若是支付减库存方式，则更新库存
                    if(goodsMain != null){
                        Goods_product product = goodsProductService.fetch(orderGoods.getProductId());
                        if(product != null){
                            if(GoodsStockOffTypeEnum.PAY.getKey() ==  goodsMain.getStockOffType()){
                                Integer buyNum = orderGoods.getBuyNum();
                                //更新库存
                                goodsProductService.updateStock(orderGoods.getSku(),buyNum,true);
                            }
                        }
                    }
                }
            }
        }else{
            //若支付的金额不足以支付订单,则启动余额支付
            int millMoney = balance + diffMoney; //扣除余额计算后的金额
            if(millMoney >= 0){ //余额足够支付订单
                orderMain.setPayStatus(OrderPayStatusEnum.PAYALL.getKey());
                memberAccount.setMoney(millMoney);
            }else{
                orderMain.setPayStatus(OrderPayStatusEnum.PAYSOME.getKey());
                //余额支付完也不足以支付订单
                memberAccount.setMoney(0);
            }
        }

        if(orderMain.getPayType()==OrderPayTypeEnum.ONLINE.getKey()){
            orderMain.setDeliveryStatus(OrderDeliveryStatusEnum.NONE.getKey());
        }else{
            orderMain.setDeliveryStatus(OrderDeliveryStatusEnum.ALL.getKey());
        }

        orderMain.setOpBy(StringUtil.getUid());
        orderMain.setOpAt((int) (System.currentTimeMillis() / 1000));
        this.update(orderMain);
        //凭证录入[若凭证为空，则跳过，不为空，则录入]
        //先删除旧的凭证数据
        List<Order_pay_transfer> transferListr = orderPayTransferService.query(Cnd.NEW().and("orderId","=",orderMain.getId()));
        for(Order_pay_transfer orderPayTransfer:transferListr){
            orderPayTransfer.setDelFlag(true);
            orderPayTransfer.setOpBy(StringUtil.getUid());
            orderPayTransfer.setOpAt((int)(System.currentTimeMillis()/1000));
            orderPayTransferService.update(orderPayTransfer);
        }
        //保存支付凭证
        this.saveUploadInfo(orderMain.getGroupId(),orderMain.getId(),orderMain.getAccountId(),orderMain.getStoreId(),uploadInfo);
        memberAccountService.update(memberAccount);
        //记录订单确认收款日志
        orderLogService.createLog(orderMain,StringUtil.getUsername(),note.toString(),OrderLogBehaviorEnum.UPDATE.getKey());
    }

    @Transactional
    public void updateOrderGoodsStock(String orderGroupId){
        try (Jedis jedis = redisService.jedis()) {
            String orderInfo = jedis.get("aebiz-order:"+orderGroupId);
            if(Strings.isNotBlank(orderInfo)){
                List<OrderStoreVO> orderStoreVOList = Json.fromJsonAsList(OrderStoreVO.class,orderInfo);
                for(OrderStoreVO orderStoreVO:orderStoreVOList){
                    List<OrderGoodsVO> orderGoodsVOList  = orderStoreVO.getGoodsList();
                    for(OrderGoodsVO orderGoodsVO : orderGoodsVOList){
                        goodsProductService.updateStock(orderGoodsVO.getSku(),orderGoodsVO.getNum(),true);
                    }
                }
            }
        }
    }

    /**
     * 验证订单是否能删除
     * @param orderMain
     * @return
     */
    public boolean checkDelOrder(Order_main orderMain){
        return true;
    }

    /**
     * 验证订单是否能关闭
     * @param orderMain
     * @return
     */
    public boolean checkCloseOrder(Order_main orderMain){
        return true;
    }

    /**
     * 验证订单是否能审核
     * @param orderMain
     * @return
     */
    public boolean checkAuditOrder(Order_main orderMain){
        return true;
    }

}

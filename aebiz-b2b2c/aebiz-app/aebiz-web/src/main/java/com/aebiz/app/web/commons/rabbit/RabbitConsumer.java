package com.aebiz.app.web.commons.rabbit;

import com.aebiz.app.goods.modules.models.Goods_main;
import com.aebiz.app.goods.modules.services.GoodsService;
import com.aebiz.app.order.modules.services.OrderMainService;
import com.aebiz.app.web.commons.base.Globals;
import com.aebiz.app.web.commons.es.EsService;
import com.aebiz.baseframework.rabbit.RabbitMessage;
import com.aebiz.commons.utils.SpringUtil;
import org.nutz.dao.Dao;
import org.nutz.ioc.impl.PropertiesProxy;
import org.nutz.lang.util.NutMap;
import org.nutz.log.Log;
import org.nutz.log.Logs;

import javax.annotation.Resource;

/**
 * Created by wizzer on 2016/12/29.
 */
public class RabbitConsumer {
    private Log log = Logs.get();
    @Resource(type = Dao.class)
    private Dao dao;

    @Resource(type = OrderMainService.class)
    private OrderMainService orderMainService;

    @Resource(type = GoodsService.class)
    private GoodsService goodsService;

    @Resource(type = EsService.class)
    private EsService esService;

    @Resource(type = PropertiesProxy.class)
    private PropertiesProxy config;

    public void handleMessage(Object object) {
        log.debug("接收到消息了");
        RabbitMessage rabbitMessage = (RabbitMessage) object;
        switch (rabbitMessage.getExchange()) {
            case "topicExchange":
                switch (rabbitMessage.getRouteKey()){
                    case "topic.order.create":
                        log.debug("下单的topic消息参数：" + rabbitMessage.getParams());
                        String orderGroupId = ((NutMap) rabbitMessage.getParams()[0]).getString("orderId");
                        orderMainService.updateOrderGoodsStock(orderGroupId);
                        break;
                    case "topic.es.goods":
                        log.debug("商品同步es的topic消息参数：" + rabbitMessage.getParams());
                        String goodsId = ((NutMap) rabbitMessage.getParams()[0]).getString("goodsId");//商品Id
                        String action = ((NutMap) rabbitMessage.getParams()[0]).getString("action");//操作类型
                        if ("delete".equals(action)) {
                            esService.deleteData(config.get("es.index.name"), "goods", goodsId);
                        } else {
                            Goods_main goods = goodsService.fetch(goodsId);
                            esService.createOrUpdateData(config.get("es.index.name"), "goods", goodsId, goods);
                        }
                        break;
                    default:
                        break;
                }

                break;
            case "fanoutExchange":
                switch (rabbitMessage.getRouteKey()) {
                    case "sysconfig":
                        Globals.initSysConfig(dao);
                        break;
                    case "sysroute":
                        Globals.initRoute(dao);
                        break;
                    case "wxtoken":
                        Globals.WxMap.clear();
                        break;
                    case "top.order.create":
                        log.debug("下单的广播的消息参数：" + rabbitMessage.getParams());
                        break;
                    default:
                        break;
                }
                break;
        }
    }
}

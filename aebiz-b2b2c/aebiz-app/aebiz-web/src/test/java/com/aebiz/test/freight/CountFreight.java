//package com.aebiz.test.freight;
//
//import com.aebiz.app.cms.modules.models.Cms_channel;
//import com.aebiz.app.cms.modules.services.CmsChannelService;
//import com.aebiz.app.store.modules.commons.vo.StoreFreightProduct;
//import com.aebiz.app.store.modules.models.Store_freight;
//import com.aebiz.app.store.modules.services.StoreFreightService;
//import com.aebiz.test.base.BaseTest;
//import org.junit.Test;
//import org.nutz.dao.Cnd;
//import org.nutz.json.Json;
//import org.nutz.log.Log;
//import org.nutz.log.Logs;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.junit.Assert.assertTrue;
//
///**
// * Created by wizzer on 2017/6/19.
// */
//public class CountFreight extends BaseTest {
//    private final Log log= Logs.get();
//
//    @Autowired
//    private StoreFreightService storeFreightService;
//    @Test
//    public void testOne(){
//        List<StoreFreightProduct> productsList=new ArrayList<>();
//        StoreFreightProduct storeFreightProduct=new StoreFreightProduct();
//        storeFreightProduct.setSku("42073205255655567045");
//        storeFreightProduct.setNum(2);
//        productsList.add(storeFreightProduct);
//        String provinceCode="340000";
//        String  logisticsCode="SF";
//        StoreFreightProduct storeFreightProduct1=new StoreFreightProduct();
//        storeFreightProduct1.setSku("52524744542024525655");
//        storeFreightProduct1.setNum(3);
//        productsList.add(storeFreightProduct1);
//        int sum=storeFreightService.countFreight(productsList,provinceCode,logisticsCode,"0010","2017060000000001");
//        log.debug("list:::"+ sum);
//        assertTrue(sum>0);
//    }
//}

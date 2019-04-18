//package com.aebiz.test.cms;
//
//import com.aebiz.app.cms.modules.models.Cms_channel;
//import com.aebiz.app.cms.modules.services.CmsChannelService;
//import com.aebiz.test.base.BaseTest;
//import org.junit.Test;
//import org.nutz.dao.Cnd;
//import org.nutz.json.Json;
//import org.nutz.log.Log;
//import org.nutz.log.Logs;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import java.util.List;
//
//import static org.junit.Assert.assertTrue;
//
///**
// * Created by wizzer on 2017/6/19.
// */
//public class CmsChannelTest extends BaseTest {
//    private final Log log= Logs.get();
//
//    @Autowired
//    private CmsChannelService cmsChannelService;
//    @Test
//    public void testOne(){
//        List<Cms_channel> list=cmsChannelService.query(Cnd.NEW());
//        log.debug("list:::"+ Json.toJson(list));
//        assertTrue(list.size()>0);
//    }
//}

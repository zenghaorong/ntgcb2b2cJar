package com.aebiz.app.web.modules.controllers.platform.sys;

import com.aebiz.app.shop.modules.services.ShopEstempService;
import com.aebiz.app.sys.modules.models.Sys_api;
import com.aebiz.app.sys.modules.services.SysApiService;
import com.aebiz.app.web.commons.es.EsService;
import com.aebiz.app.web.commons.log.annotation.SLog;
import com.aebiz.baseframework.base.Result;
import com.aebiz.baseframework.page.datatable.DataTableColumn;
import com.aebiz.baseframework.page.datatable.DataTableOrder;
import com.aebiz.baseframework.view.annotation.SJson;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.dao.Cnd;
import org.nutz.dao.Sqls;
import org.nutz.ioc.impl.PropertiesProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by wizzer on 2017/4/4.
 */
@Controller
@RequestMapping("/platform/sys/es")
public class SysEsController {
    @Autowired
    private ShopEstempService shopEstempService;
    @Autowired
    private EsService esService;
    @Autowired
    private PropertiesProxy config;

    @RequestMapping("")
    public String index(HttpServletRequest req) {
        req.setAttribute("indexName", config.get("es.index.name", "aebiz"));
        return "pages/platform/sys/es/index";
    }


    @RequestMapping("/status/{indexName}")
    @SJson
    public Object es(@PathVariable String indexName) {
        return Result.success("globals.result.success", esService.isExistsIndex(indexName));
    }

    @RequestMapping("/index/{indexName}")
    @SJson
    public Object esindex(@PathVariable String indexName) {
        try {
            if (esService.isExistsIndex(indexName)) {
                esService.deleteIndex(indexName);
            }
            esService.createIndex(indexName);
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping("/data")
    @SJson
    public Object esdata() {
        try {
            shopEstempService.clear();
            shopEstempService.dao().execute(Sqls.create("INSERT INTO shop_estemp(id,goodsid,ACTION,opAt) SELECT id,id,'create',opAt FROM goods_main WHERE delFlag=@f").setParam("f", false));
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping("/count")
    @SJson
    public Object count() {
        try {
            return Result.success("globals.result.success", shopEstempService.count());
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }
}

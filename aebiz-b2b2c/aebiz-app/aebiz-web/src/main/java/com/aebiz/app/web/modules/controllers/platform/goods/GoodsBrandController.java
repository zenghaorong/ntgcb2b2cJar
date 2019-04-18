package com.aebiz.app.web.modules.controllers.platform.goods;

import com.aebiz.app.goods.modules.services.GoodsTypeService;
import com.aebiz.app.web.commons.log.annotation.SLog;
import com.aebiz.baseframework.base.Result;
import com.aebiz.baseframework.page.datatable.DataTable;
import com.aebiz.baseframework.page.datatable.DataTableColumn;
import com.aebiz.baseframework.page.datatable.DataTableOrder;
import com.aebiz.baseframework.redis.RedisService;
import com.aebiz.commons.utils.StringUtil;
import com.aebiz.app.goods.modules.models.Goods_brand;
import com.aebiz.app.goods.modules.services.GoodsBrandService;
import com.aebiz.baseframework.view.annotation.SJson;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.dao.Cnd;
import org.nutz.dao.Sqls;
import org.nutz.lang.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

@Controller
@RequestMapping("/platform/goods/brand")
public class GoodsBrandController {

    @Autowired
	private GoodsBrandService goodsBrandService;

    @Autowired
    private GoodsTypeService goodsTypeService;

    @Autowired
    private RedisService redisService;

    @RequestMapping("")
	@RequiresPermissions("goods.conf.brand")
	public String index() {
		return "pages/platform/goods/brand/index";
	}

	@RequestMapping("/data")
    @SJson("full")
    @RequiresPermissions("goods.conf.brand")
    public Object data(@RequestParam(value = "name",required = false) String name,
                       @RequestParam(value = "url",required = false) String url,
                       DataTable dataTable) {
		Cnd cnd = Cnd.NEW();
        if (Strings.isNotBlank(name)) {
            cnd.and("name", "like", Sqls.escapeSqlFieldValue("%" + Strings.trim(name) + "%"));
        }
        if (Strings.isNotBlank(url)) {
            cnd.and("url", "like", Sqls.escapeSqlFieldValue("%" + Strings.trim(url) + "%"));
        }
    	return goodsBrandService.data(dataTable.getLength(), dataTable.getStart(),
                dataTable.getDraw(), dataTable.getOrders(), dataTable.getColumns(), cnd, null);
    }

    @RequestMapping("/add")
    @RequiresPermissions("goods.conf.brand")
    public String add(HttpServletRequest req) {
        req.setAttribute("typeList", goodsTypeService.query(Cnd.orderBy().asc("opAt")));
        return "pages/platform/goods/brand/add";
    }

    @RequestMapping("/addDo")
    @SJson
    @SLog(description = "Goods_brand")
    @RequiresPermissions("goods.conf.brand.add")
    public Object addDo(Goods_brand goodsBrand, @RequestParam(value = "type", required = false) String[] type, HttpServletRequest req) {
		try {
            goodsBrandService.add(goodsBrand,type);
			return Result.success("globals.result.success");
		} catch (Exception e) {
			return Result.error("globals.result.error");
		}
    }

    @RequestMapping("/edit/{id}")
    @RequiresPermissions("goods.conf.brand")
    public String edit(@PathVariable String id,HttpServletRequest req) {
        Goods_brand obj = goodsBrandService.fetch(id);
        req.setAttribute("typeList", goodsTypeService.query(Cnd.orderBy().asc("opAt")));
        goodsTypeService.fetchLinks(obj, null, Cnd.orderBy().asc("opAt"));
        req.setAttribute("obj", obj);

        return "pages/platform/goods/brand/edit";
    }

    @RequestMapping("/editDo")
    @SJson
    @SLog(description = "Goods_brand")
    @RequiresPermissions("goods.conf.brand.edit")
    public Object editDo(Goods_brand goodsBrand, @RequestParam(value = "type", required = false) String[] type, HttpServletRequest req) {
		try {
            goodsBrand.setOpBy(StringUtil.getUid());
			goodsBrand.setOpAt((int) (System.currentTimeMillis() / 1000));
//			goodsBrandService.updateIgnoreNull(goodsBrand);
            goodsBrandService.edit(goodsBrand,type);
			return Result.success("globals.result.success");
		} catch (Exception e) {
			return Result.error("globals.result.error");
		}
    }

    @RequestMapping(value = {"/delete/{id}", "/delete"})
    @SJson
    @SLog(description = "Goods_brand")
    @RequiresPermissions("goods.conf.brand.delete")
    public Object delete(@PathVariable(required = false) String id, @RequestParam(value = "ids[]",required = false)  String[] ids, HttpServletRequest req) {
		try {
			if(ids != null && ids.length > 0){
				goodsBrandService.delete(ids);
			}else{
				goodsBrandService.delete(id);
			}
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }


}

package com.aebiz.app.web.modules.controllers.platform.goods;

import com.aebiz.app.web.commons.log.annotation.SLog;
import com.aebiz.baseframework.base.Result;
import com.aebiz.baseframework.page.datatable.DataTable;
import com.aebiz.baseframework.page.datatable.DataTableColumn;
import com.aebiz.baseframework.page.datatable.DataTableOrder;
import com.aebiz.commons.utils.StringUtil;
import com.aebiz.app.goods.modules.models.Goods_tag;
import com.aebiz.app.goods.modules.services.GoodsTagService;
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
@RequestMapping("/platform/goods/tag")
public class GoodsTagController {

    @Autowired
	private GoodsTagService goodsTagService;

    @RequestMapping("")
	@RequiresPermissions("goods.conf.tag")
	public String index() {
		return "pages/platform/goods/tag/index";
	}

	@RequestMapping("/data")
    @SJson("full")
    @RequiresPermissions("goods.conf.tag")
    public Object data(@RequestParam(value = "name",required = false) String name, DataTable dataTable) {
		Cnd cnd = Cnd.NEW();
        if (Strings.isNotBlank(name)) {
            cnd.and("name", "like", Sqls.escapeSqlFieldValue("%" + Strings.trim(name) + "%"));
        }
    	return goodsTagService.data(dataTable.getLength(), dataTable.getStart(),
                dataTable.getDraw(), dataTable.getOrders(), dataTable.getColumns(), cnd, null);
    }

    @RequestMapping("/add")
    @RequiresPermissions("goods.conf.tag")
    public String add() {
    	return "pages/platform/goods/tag/add";
    }

    @RequestMapping("/addDo")
    @SJson
    @SLog(description = "Goods_tag")
    @RequiresPermissions("goods.conf.tag.add")
    public Object addDo(Goods_tag goodsTag, HttpServletRequest req) {
		try {
			goodsTagService.insert(goodsTag);
			return Result.success("globals.result.success");
		} catch (Exception e) {
			return Result.error("globals.result.error");
		}
    }

    @RequestMapping("/edit/{id}")
    @RequiresPermissions("goods.conf.tag")
    public String edit(@PathVariable String id,HttpServletRequest req) {
		req.setAttribute("obj", goodsTagService.fetch(id));
		return "pages/platform/goods/tag/edit";
    }

    @RequestMapping("/editDo")
    @SJson
    @SLog(description = "Goods_tag")
    @RequiresPermissions("goods.conf.tag.edit")
    public Object editDo(Goods_tag goodsTag, HttpServletRequest req) {
		try {
            goodsTag.setOpBy(StringUtil.getUid());
			goodsTag.setOpAt((int) (System.currentTimeMillis() / 1000));
			goodsTagService.updateIgnoreNull(goodsTag);
			return Result.success("globals.result.success");
		} catch (Exception e) {
			return Result.error("globals.result.error");
		}
    }

    @RequestMapping(value = {"/delete/{id}", "/delete"})
    @SJson
    @SLog(description = "Goods_tag")
    @RequiresPermissions("goods.conf.tag.delete")
    public Object delete(@PathVariable(required = false) String id, @RequestParam(value = "ids[]",required = false)  String[] ids, HttpServletRequest req) {
		try {
			if(ids!=null&&ids.length>0){
				goodsTagService.delete(ids);
			}else{
				goodsTagService.delete(id);
			}
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping("/detail/{id}")
    @RequiresPermissions("goods.conf.tag")
	public String detail(@PathVariable String id, HttpServletRequest req) {
		if (!Strings.isBlank(id)) {
            req.setAttribute("obj", goodsTagService.fetch(id));
		}else{
            req.setAttribute("obj", null);
        }
        return "pages/platform/goods/tag/detail";
    }

}

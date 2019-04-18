package com.aebiz.app.web.modules.controllers.platform.self.store;

import com.aebiz.app.web.commons.log.annotation.SLog;
import com.aebiz.baseframework.base.Result;
import com.aebiz.baseframework.page.datatable.DataTableColumn;
import com.aebiz.baseframework.page.datatable.DataTableOrder;
import com.aebiz.commons.utils.StringUtil;
import com.aebiz.app.store.modules.models.Store_goodsclass;
import com.aebiz.app.store.modules.services.StoreGoodsclassService;
import com.aebiz.baseframework.view.annotation.SJson;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Sqls;
import org.nutz.lang.Lang;
import org.nutz.lang.Strings;
import org.nutz.lang.util.NutMap;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 自营前台分类
 */
@Controller
@RequestMapping("/platform/self/store/goodsclass")
public class SelfStoreGoodsclassController {
    private static final Log log = Logs.get();
    @Autowired
	private StoreGoodsclassService storeGoodsclassService;

    @RequestMapping("")
    @RequiresPermissions("self.store.goodsclass")
	public String index(HttpServletRequest req) {
        req.setAttribute("storeGoodsclassList", storeGoodsclassService.query(Cnd.where("storeId", "=", "2017060000000001").and("parentId", "=", "").asc("location").asc("path")));
        return "pages/platform/self/store/goodsclass/index";
	}

	@RequestMapping("/data")
    @SJson("full")
    @RequiresPermissions("self.store.goodsclass")
    public Object data(@RequestParam("length") int length, @RequestParam("start") int start, @RequestParam("draw") int draw, ArrayList<DataTableOrder> order, ArrayList<DataTableColumn> columns) {
		Cnd cnd = Cnd.NEW();
        cnd.and("storeId", "=", "2017060000000001");
    	return storeGoodsclassService.data(length, start, draw, order, columns, cnd, null);
    }

    @RequestMapping("/add")
    @RequiresPermissions("self.store.goodsclass")
    public String add(@RequestParam(value = "pid", required = false) String pid, HttpServletRequest req) {
        if (!Strings.isEmpty(pid)) {
            req.setAttribute("pobj", storeGoodsclassService.fetch(pid));
        }
        return "pages/platform/self/store/goodsclass/add";
    }

    @RequestMapping("/addDo")
    @SJson
    @SLog(description = "保存前台分类")
    @RequiresPermissions("self.store.goodsclass.add")
    public Object addDo(Store_goodsclass storeGoodsclass, @RequestParam("parentId") String parentId, HttpServletRequest req) {
		try {
			storeGoodsclassService.save(storeGoodsclass, parentId, "2017060000000001");
            storeGoodsclassService.clearCache();
			return Result.success("globals.result.success");
		} catch (Exception e) {
			return Result.error("globals.result.error");
		}
    }

    @RequestMapping("/edit/{id}")
    @RequiresPermissions("self.store.goodsclass")
    public String edit(@PathVariable("id") String id, HttpServletRequest req) {
        Store_goodsclass obj = storeGoodsclassService.fetch(id);
        if (Strings.isNotBlank(obj.getParentId())) {
            req.setAttribute("pobj", storeGoodsclassService.fetch(obj.getParentId()));
        }
		req.setAttribute("obj", obj);
		return "pages/platform/self/store/goodsclass/edit";
    }

    @RequestMapping("/editDo")
    @SJson
    @SLog(description = "修改前台分类")
    @RequiresPermissions("self.store.goodsclass.edit")
    public Object editDo(Store_goodsclass storeGoodsclass, HttpServletRequest req) {
		try {
            storeGoodsclass.setOpBy(StringUtil.getUid());
			storeGoodsclass.setOpAt((int) (System.currentTimeMillis() / 1000));
			storeGoodsclassService.updateIgnoreNull(storeGoodsclass);
            storeGoodsclassService.clearCache();
			return Result.success("globals.result.success");
		} catch (Exception e) {
			return Result.error("globals.result.error");
		}
    }

    @RequestMapping(value = {"/delete/{id}", "/delete"})
    @SJson
    @SLog(description = "删除前台分类")
    @RequiresPermissions("self.store.goodsclass.delete")
    public Object delete(@PathVariable(required = false) String id, @RequestParam(value = "ids",required = false)  String[] ids, HttpServletRequest req) {
		try {
			if(Lang.isEmpty(ids)){
                storeGoodsclassService.delete(id);
            }else{
                storeGoodsclassService.delete(ids);
			}
            storeGoodsclassService.clearCache();
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping("/detail/{id}")
    @RequiresPermissions("self.store.goodsclass")
	public String detail(@PathVariable String id, HttpServletRequest req) {
		if (!Strings.isBlank(id)) {
            req.setAttribute("obj", storeGoodsclassService.fetch(id));
		}else{
            req.setAttribute("obj", null);
        }
        return "pages/platform/self/store/goodsclass/detail";
    }

    @RequestMapping("/enable/{id}")
    @SJson
    @RequiresPermissions("self.store.goodsclass.edit")
    @SLog(description = "启用商品分类")
    public Object enable(@PathVariable String id, HttpServletRequest req) {
        try {
            storeGoodsclassService.update(Chain.make("disabled", false), Cnd.where("id", "=", id));
            storeGoodsclassService.clearCache();
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping("/disable/{id}")
    @SJson
    @RequiresPermissions("self.store.goodsclass.edit")
    @SLog(description = "禁用商品分类")
    public Object disable(@PathVariable(required = false) String id, HttpServletRequest req) {
        try {
            storeGoodsclassService.update(Chain.make("disabled", true), Cnd.where("id", "=", id));
            storeGoodsclassService.clearCache();
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping("/tree")
    @SJson
    @RequiresAuthentication
    public Object tree(@RequestParam(value = "pid", required = false) String pid) {
        return storeGoodsclassService.query(Cnd.where("storeId", "=", "2017060000000001").and("parentId", "=", Strings.sBlank(pid)).asc("location").asc("path"));
    }

    @RequestMapping("/child/{id}")
    @RequiresPermissions("self.store.goodsclass")
    public Object child(@PathVariable String id, HttpServletRequest req) {
        req.setAttribute("storeGoodsclassList", storeGoodsclassService.query(Cnd.where("parentId", "=", id).asc("location").asc("path")));
        return "pages/platform/self/store/goodsclass/child";
    }

    @RequestMapping("/sort")
    @RequiresPermissions("self.store.goodsclass")
    public String sort(HttpServletRequest req) {
        List<Store_goodsclass> list = storeGoodsclassService.query(Cnd.where("storeId", "=", "2017060000000001").asc("location").asc("path"));
        List<Store_goodsclass> firstClasses = new LinkedList<>();
        LinkedHashMap<String, List<Store_goodsclass>> secondClasses = new LinkedHashMap<>();
        List<Store_goodsclass> tmpList;
        for (Store_goodsclass sgc : list) {
            if (sgc.getPath().length() > 4) {
                String parentPath = StringUtil.getParentId(sgc.getPath());
                tmpList = secondClasses.get(parentPath);
                if (Lang.isEmpty(tmpList)) {
                    tmpList = new LinkedList<>();
                }
                tmpList.add(sgc);
                secondClasses.put(parentPath, tmpList);
            } else if (sgc.getPath().length() == 4) {
                firstClasses.add(sgc);
            }
        }
        req.setAttribute("firstClasses", firstClasses);
        req.setAttribute("secondClasses", secondClasses);
        return "pages/platform/self/store/goodsclass/sort";
    }

    @RequestMapping("/sortDo")
    @SJson
    @RequiresPermissions("self.store.goodsclass.edit")
    public Object sortDo(@RequestParam("ids") String ids, HttpServletRequest req) {
        try {
            String[] menuIds = StringUtils.split(ids, ",");
            storeGoodsclassService.sort(menuIds, "2017060000000001");
            storeGoodsclassService.clearCache();
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

}

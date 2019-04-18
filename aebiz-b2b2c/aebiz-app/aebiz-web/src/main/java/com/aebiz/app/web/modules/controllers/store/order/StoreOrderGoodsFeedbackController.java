package com.aebiz.app.web.modules.controllers.store.order;

import com.aebiz.app.order.modules.models.Order_goods_feedback;
import com.aebiz.app.order.modules.services.OrderGoodsFeedbackService;
import com.aebiz.app.web.commons.log.annotation.SLog;
import com.aebiz.baseframework.base.Result;
import com.aebiz.baseframework.page.datatable.DataTable;
import com.aebiz.baseframework.view.annotation.SJson;
import com.aebiz.commons.utils.DateUtil;
import com.aebiz.commons.utils.StringUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.dao.Chain;
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
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;

@Controller
@RequestMapping("/store/order/goods/feedback")
public class StoreOrderGoodsFeedbackController {

    private static final Log log = Logs.get();

    @Autowired
	private OrderGoodsFeedbackService orderGoodsFeedbackService;

    @RequestMapping("")
    @RequiresPermissions("store.order.score.list")
	public String index() {
		return "pages/store/order/feedback/index";
	}

	@RequestMapping("/data")
    @SJson("full")
    @RequiresPermissions("store.order.score.list")
    public Object data(@RequestParam(value = "name",required = false) String name,
                       @RequestParam(value = "startAt",required = false) String startAt,
                       @RequestParam(value = "endAt",required = false) String endAt,
                       @RequestParam(value = "feedOpen",required = false) String feedOpen,
                       DataTable dataTable) {
        //商家id
        String storeId=StringUtil.getStoreId();
        NutMap fieldMap = NutMap.NEW();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        if(Strings.isNotBlank(feedOpen)){
            fieldMap.put("feedOpen", "1".equals(feedOpen));
        }
        fieldMap.put("storeId",storeId);
        if (Strings.isNotBlank(name)) {
            fieldMap.put("name", name);
        }
        if (Strings.isNotBlank(startAt)) {
            fieldMap.put("startAt", DateUtil.getTime(startAt+" 00:00:00"));
        }
        if (Strings.isNotBlank(endAt)) {
            fieldMap.put("endAt", DateUtil.getTime(endAt+" 23:59:59"));
        }
        return orderGoodsFeedbackService.selfOrderGoodsFeedbackData(dataTable.getLength(), dataTable.getStart(), dataTable.getDraw(),
                dataTable.getOrders(), dataTable.getColumns(), fieldMap);
    }

    @RequestMapping("/add")
    @RequiresPermissions("store.order.score.list")
    public String add() {
    	return "pages/store/order/feedback/add";
    }

    @RequestMapping("/addDo")
    @SJson
    @SLog(description = "Order_goods_feedback")
    @RequiresPermissions("store.order.score.list.add")
    public Object addDo(Order_goods_feedback orderGoodsFeedback, HttpServletRequest req) {
		try {
			orderGoodsFeedbackService.insert(orderGoodsFeedback);
			return Result.success("globals.result.success");
		} catch (Exception e) {
			return Result.error("globals.result.error");
		}
    }

    @RequestMapping("/edit/{id}")
    @RequiresPermissions("store.order.score.list")
    public String edit(@PathVariable String id,HttpServletRequest req) {
		req.setAttribute("obj", orderGoodsFeedbackService.fetch(id));
		return "pages/store/order/feedback/edit";
    }

    @RequestMapping("/editDo")
    @SJson
    @SLog(description = "Order_goods_feedback")
    @RequiresPermissions("store.order.score.list.edit")
    public Object editDo(Order_goods_feedback orderGoodsFeedback, HttpServletRequest req) {
		try {
            orderGoodsFeedback.setOpBy(StringUtil.getUid());
			orderGoodsFeedback.setOpAt((int) (System.currentTimeMillis() / 1000));
			orderGoodsFeedbackService.updateIgnoreNull(orderGoodsFeedback);
			return Result.success("globals.result.success");
		} catch (Exception e) {
			return Result.error("globals.result.error");
		}
    }

    /**
     * 评价的删除功能（单个、批量、逻辑删除）
     * @param id
     * @param ids
     * @param req
     * @return
     */
    @RequestMapping(value = {"/delete/{id}", "/delete"})
    @SJson
    @SLog(description = "Order_goods_feedback")
    @RequiresPermissions("store.order.score.list.delete")
    public Object delete(@PathVariable(required = false) String id, @RequestParam(value = "ids",required = false)  String[] ids, HttpServletRequest req) {
		try {
			if(!Lang.isEmptyArray(ids)){
                orderGoodsFeedbackService.update(Chain.make("delFlag", true)
                                .add("opBy", StringUtil.getUid())
                                .add("opAt", (int) (System.currentTimeMillis() / 1000)), Cnd.where("id", "in", ids));
                req.setAttribute("id", org.apache.shiro.util.StringUtils.toString(ids));
			}else{
                orderGoodsFeedbackService.update(Chain.make("delFlag", true)
                        .add("opBy", StringUtil.getUid()).add("opAt", (int) (System.currentTimeMillis() / 1000)),
                        Cnd.where("id", "=", id));
    			req.setAttribute("id", id);
			}
            return Result.success("globals.result.success");
        } catch (Exception e) {
            log.error(e.getMessage());
            return Result.error("globals.result.error");
        }
    }

    /**
     * 评论是否公开方法
     * @param id
     * @param isOpen
     * @param req
     * @return
     */
    @RequestMapping(value = "/changeFeedOpen/{id}/{isOpen}")
    @SJson
    @SLog(description = "Order_goods_feedback")
    @RequiresPermissions("store.order.score.list.delete")
    public Object changeFeedOpen(@PathVariable String id, @PathVariable boolean isOpen, HttpServletRequest req) {
        try {
            orderGoodsFeedbackService.update(Chain.make("feedOpen", isOpen)
                    .add("opBy", StringUtil.getUid()).add("opAt", (int) (System.currentTimeMillis() / 1000)),
                    Cnd.where("id", "=", id));
            req.setAttribute("id", id);
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping("/detail/{id}")
    @RequiresPermissions("store.order.score.list")
	public String detail(@PathVariable String id, HttpServletRequest req) {
		if (!Strings.isBlank(id)) {
            req.setAttribute("obj", orderGoodsFeedbackService.fetchLinks(orderGoodsFeedbackService.fetch(id),
                    "^(orderMain|goodsMain|goodsProduct|accountUser|accountInfo)$"));
		}else{
            req.setAttribute("obj", null);
        }
        return "pages/store/order/feedback/detail";
    }

    /**
     * 到回复评论页面
     * @param id
     * @param req
     * @return
     */
    @RequestMapping("/replay/{id}")
    @RequiresPermissions("store.order.score.list")
    public String replay(@PathVariable String id, HttpServletRequest req) {
        if (!Strings.isBlank(id)) {
            req.setAttribute("obj", orderGoodsFeedbackService.fetchLinks(orderGoodsFeedbackService.fetch(id),
                    "^(orderMain|goodsMain|goodsProduct|accountUser|accountInfo)$"));
        }else{
            req.setAttribute("obj", null);
        }
        return "pages/store/order/feedback/replay";
    }

    /**
     * 保存评论回复信息
     * @param id
     * @param storeReplyNote
     * @param req
     * @return
     */
    @RequestMapping("/replayDo/{id}")
    @SJson
    @SLog(description = "Order_goods_feedback")
    @RequiresPermissions("store.order.score.list.edit")
    public Object replayDo(@PathVariable String id, @RequestParam String storeReplyNote,HttpServletRequest req) {
        try {
            orderGoodsFeedbackService.update(Chain.make("storeReplyNote", storeReplyNote)
                    .add("storeReplyAt", (int) (System.currentTimeMillis() / 1000))
                    .add("opBy", StringUtil.getUid()).add("opAt", (int) (System.currentTimeMillis() / 1000)),
                    Cnd.where("id", "=", id));
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

}

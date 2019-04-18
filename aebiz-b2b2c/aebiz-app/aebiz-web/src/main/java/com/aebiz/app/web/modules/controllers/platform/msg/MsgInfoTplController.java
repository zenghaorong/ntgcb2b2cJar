package com.aebiz.app.web.modules.controllers.platform.msg;

import com.aebiz.app.msg.modules.models.Msg_conf_sms_tpl;
import com.aebiz.app.msg.modules.services.MsgConfSmsTplService;
import com.aebiz.app.web.commons.log.annotation.SLog;
import com.aebiz.baseframework.base.Result;
import com.aebiz.baseframework.page.datatable.DataTableColumn;
import com.aebiz.baseframework.page.datatable.DataTableOrder;
import com.aebiz.commons.utils.StringUtil;
import com.aebiz.app.msg.modules.models.Msg_info_tpl;
import com.aebiz.app.msg.modules.services.MsgInfoTplService;
import com.aebiz.baseframework.view.annotation.SJson;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.dao.Cnd;
import org.nutz.lang.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/platform/msg/info/tpl")
public class MsgInfoTplController {
    @Autowired
	private MsgInfoTplService msgInfoTplService;
    @Autowired
    private MsgConfSmsTplService msgConfSmsTplService;

    @RequestMapping("")
	@RequiresPermissions("mall.msgconfig.sysset")
	public String index() {
		return "pages/platform/msg/info/tpl/index";
	}

	@RequestMapping("/data")
    @SJson("full")
    @RequiresPermissions(value = {"mall.msgconfig.sysset", "mall.msgconfig.sysmsgsetting"}, logical = Logical.OR)
    public Object data(@RequestParam("length") int length, @RequestParam("start") int start, @RequestParam("draw") int draw, ArrayList<DataTableOrder> order, ArrayList<DataTableColumn> columns) {
		Cnd cnd = Cnd.NEW();
    	return msgInfoTplService.data(length, start, draw, order, columns, cnd, null);
    }

    @RequestMapping("/add")
    @RequiresPermissions(value = {"mall.msgconfig.sysset", "mall.msgconfig.sysmsgsetting"}, logical = Logical.OR)
    public String add() {
    	return "pages/platform/msg/info/tpl/add";
    }

    @RequestMapping("/addDo")
    @SJson
    @SLog(description = "Msg_info_tpl")
    @RequiresPermissions(value = {"mall.msgconfig.sysset.add", "mall.msgconfig.sysmsgsetting"}, logical = Logical.OR)
    public Object addDo(Msg_info_tpl msgInfoTpl, HttpServletRequest req) {
		try {
			msgInfoTplService.insert(msgInfoTpl);
			return Result.success("globals.result.success");
		} catch (Exception e) {
			return Result.error("globals.result.error");
		}
    }

    @RequestMapping("/edit/{id}")
    @RequiresPermissions(value = {"mall.msgconfig.sysset", "mall.msgconfig.sysmsgsetting"}, logical = Logical.OR)
    public String edit(@PathVariable String id,HttpServletRequest req) {
		req.setAttribute("obj", msgInfoTplService.fetch(id));
		return "pages/platform/msg/info/tpl/edit";
    }

    @RequestMapping("/editDo")
    @SJson
    @SLog(description = "Msg_info_tpl")
    @RequiresPermissions(value = {"mall.msgconfig.sysset.edit", "mall.msgconfig.sysmsgsetting"}, logical = Logical.OR)
    public Object editDo(Msg_info_tpl msgInfoTpl, HttpServletRequest req) {
		try {
            msgInfoTpl.setOpBy(StringUtil.getUid());
			msgInfoTpl.setOpAt((int) (System.currentTimeMillis() / 1000));
			msgInfoTplService.updateIgnoreNull(msgInfoTpl);
			return Result.success("globals.result.success");
		} catch (Exception e) {
			return Result.error("globals.result.error");
		}
    }

    @RequestMapping(value = {"/delete/{id}", "/delete"})
    @SJson
    @SLog(description = "Msg_info_tpl")
    @RequiresPermissions("mall.msgconfig.sysset.delete")
    public Object delete(@PathVariable(required = false) String id, @RequestParam(value = "ids",required = false)  String[] ids, HttpServletRequest req) {
		try {
			if(ids!=null&&ids.length>0){
				msgInfoTplService.delete(ids);
			}else{
				msgInfoTplService.delete(id);
			}
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping("/detail/{id}")
    @RequiresPermissions("mall.msgconfig.sysset")
	public String detail(@PathVariable String id, HttpServletRequest req) {
		if (!Strings.isBlank(id)) {
            req.setAttribute("obj", msgInfoTplService.fetch(id));
		}else{
            req.setAttribute("obj", null);
        }
        return "pages/platform/msg/info/tpl/detail";
    }

    /**
     * 保存模板
     * @param infoId
     * @param infoType
     * @param uecontent
     * @param req
     * @return
     */
    @RequestMapping("/saveTpl")
    @SJson("full")
    @RequiresPermissions(value = {"mall.msgconfig.sysset", "mall.msgconfig.sysmsgsetting"}, logical = Logical.OR)
    public Object saveTpl(@RequestParam String infoId, @RequestParam String infoType, @RequestParam String uecontent, @RequestParam(value = "smsPlatformTplId", required = false) String smsPlatformTplId, HttpServletRequest req) {
        try {
            List<Msg_info_tpl> msgInfotpllsit = msgInfoTplService.dao().query(Msg_info_tpl.class, Cnd.where("infoId", "=", infoId));
            if(msgInfotpllsit != null && msgInfotpllsit.size()>0){
                //更新
                Msg_info_tpl msgInfotpl = msgInfotpllsit.get(0);
                if(infoType.equals("sms")){
                    msgInfotpl.setTplSMS(uecontent);
                }else if(infoType.equals("email")){
                    msgInfotpl.setTplEmail(uecontent);
                }else{
                    msgInfotpl.setTplMSG(uecontent);
                }
                msgInfotpl.setOpBy(StringUtil.getUid());
                msgInfotpl.setOpAt((int) (System.currentTimeMillis() / 1000));
                msgInfoTplService.updateIgnoreNull(msgInfotpl);

                if (Strings.isNotBlank(smsPlatformTplId)) {
                    Msg_conf_sms_tpl smsTpl = msgConfSmsTplService.fetch(smsPlatformTplId);
                    smsTpl.setInfoId(infoId);
                    msgConfSmsTplService.update(smsTpl);
                }
            }else{
                //插入
                Msg_info_tpl obj = new Msg_info_tpl();
                obj.setInfoId(infoId);
                if(infoType.equals("sms")){
                    obj.setTplSMS(uecontent);
                }else if(infoType.equals("email")){
                    obj.setTplEmail(uecontent);
                }else{
                    obj.setTplMSG(uecontent);
                }
                msgInfoTplService.insert(obj);

                if (Strings.isNotBlank(smsPlatformTplId)) {
                    Msg_conf_sms_tpl smsTpl = msgConfSmsTplService.fetch(smsPlatformTplId);
                    smsTpl.setInfoId(infoId);
                    msgConfSmsTplService.update(smsTpl);
                }
            }
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    /**
     * 查询模板内容
     */
    @RequestMapping("/getMsgTplInfo")
    @SJson("full")
    @RequiresPermissions(value = {"mall.msgconfig.sysset", "mall.msgconfig.sysmsgsetting"}, logical = Logical.OR)
    public Object getMsgTplInfo(@RequestParam String infoId, HttpServletRequest req) {
        try {
            List<Msg_info_tpl> msgInfotpllsit = msgInfoTplService.dao().query(Msg_info_tpl.class, Cnd.where("infoId", "=", infoId));
            Msg_info_tpl tempInfo = null;
            if(msgInfotpllsit != null && msgInfotpllsit.size()>0){
                tempInfo = msgInfotpllsit.get(0);
            }
            return Result.success("globals.result.success",tempInfo);
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }


}

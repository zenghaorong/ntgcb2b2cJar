package com.aebiz.app.web.modules.controllers.platform.msg;

import com.aebiz.app.msg.modules.models.Msg_receive_msg;
import com.aebiz.app.msg.modules.models.Msg_send;
import com.aebiz.app.msg.modules.models.Msg_type;
import com.aebiz.app.msg.modules.services.MsgReceiveMsgService;
import com.aebiz.app.msg.modules.services.MsgSendService;
import com.aebiz.app.msg.modules.services.MsgTypeService;
import com.aebiz.app.web.commons.log.annotation.SLog;
import com.aebiz.baseframework.base.Result;
import com.aebiz.baseframework.page.datatable.DataTableColumn;
import com.aebiz.baseframework.page.datatable.DataTableOrder;
import com.aebiz.commons.utils.StringUtil;
import com.aebiz.app.msg.modules.models.Msg_send_msg;
import com.aebiz.app.msg.modules.services.MsgSendMsgService;
import com.aebiz.baseframework.view.annotation.SJson;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.nutz.dao.Cnd;
import org.nutz.dao.Sqls;
import org.nutz.lang.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/platform/msg/send/msg")
public class MsgSendMsgController {
    @Autowired
	private MsgSendMsgService msgSendMsgService;
    @Autowired
    private MsgTypeService msgTypeService;
    @Autowired
    private MsgSendService msgSendService;
    @Autowired
    private MsgReceiveMsgService msgReceiveMsgService;

    @RequestMapping("")
	@RequiresAuthentication
	public String index() {
        return "pages/platform/msg/send/msg/index";
	}

	@RequestMapping("/data")
    @SJson("full")
    public Object data(@RequestParam("length") int length, @RequestParam("start") int start, @RequestParam("draw") int draw, ArrayList<DataTableOrder> order, ArrayList<DataTableColumn> columns) {
        Cnd cnd = Cnd.NEW();
        return msgSendMsgService.data(length, start, draw, order, columns, cnd.where("delFlag","=","1"), "msgSend");
    }

    @RequestMapping("/add")
    public String add(HttpServletRequest req) {
        //消息类型
        Cnd cnd = Cnd.NEW();
        List<Msg_type> msgTypeList = msgTypeService.query(cnd);
        req.setAttribute("msgTypeList",msgTypeList);
        return "pages/platform/msg/send/msg/add";
    }

    @RequestMapping("/addDo")
    @SJson
    @SLog(description = "Msg_send_msg")
    public Object addDo(Msg_send_msg msgSendMsg, HttpServletRequest req) {
		try {
            //保存信息
            msgSendMsgService.saveInfoMsg(msgSendMsg);
			return Result.success("globals.result.success");
		} catch (Exception e) {
			return Result.error("globals.result.error");
		}
    }

    @RequestMapping("/edit/{id}")
    @RequiresAuthentication
    public String edit(@PathVariable String id,HttpServletRequest req) {
        Msg_send_msg msgSendMsg = msgSendMsgService.fetch(id);
        msgSendMsg.setMsgSend(msgSendService.fetch(msgSendMsg.getSendId()));
		req.setAttribute("obj",msgSendMsg);
		return "pages/platform/msg/send/msg/edit";
    }

    @RequestMapping("/editDo")
    @SJson
    @SLog(description = "Msg_send_msg")
    public Object editDo(Msg_send_msg msgSendMsg, HttpServletRequest req) {
		try {
            msgSendMsg.setOpBy(StringUtil.getUid());
			msgSendMsg.setOpAt((int) (System.currentTimeMillis() / 1000));
			msgSendMsgService.updateIgnoreNull(msgSendMsg);
			return Result.success("globals.result.success");
		} catch (Exception e) {
			return Result.error("globals.result.error");
		}
    }

    @RequestMapping(value = {"/delete/{id}", "/delete"})
    @SJson
    @SLog(description = "Msg_send_msg")
    public Object delete(@PathVariable(required = false) String id, @RequestParam(value = "ids",required = false)  String[] ids, HttpServletRequest req) {
		try {
			if(ids!=null&&ids.length>0){
				msgSendMsgService.delete(ids);
                for(int i=0;i<ids.length;i++){
                    Msg_send_msg msgSendMsg = msgSendMsgService.fetch(ids[i]);
                    msgSendService.delete(msgSendMsg.getSendId());
                    msgSendMsgService.delete(ids[i]);
                    List<Msg_receive_msg>  lsitre = msgReceiveMsgService.query(Cnd.NEW().where("sendId","=",msgSendMsg.getSendId()));
                    if(lsitre != null && lsitre.size()>0){
                        for (Msg_receive_msg msgReceiveMsg : lsitre){
                            msgReceiveMsgService.delete(msgReceiveMsg.getId());
                        }
                    }
                }
			}else{
                //撤回时需对系统已经发送邮件进行撤回
                Msg_send_msg msgSendMsg = msgSendMsgService.fetch(id);
                Msg_send msgSend = msgSendService.fetch(msgSendMsg.getSendId());
                msgSend.setDelFlag(true);
                msgSendService.update(msgSend);
               // msgSendMsgService.delete(id);

                msgSendMsg.setDelFlag(true);
                msgSendMsgService.update(msgSendMsg);

                List<Msg_receive_msg>  lsitre = msgReceiveMsgService.query(Cnd.NEW().where("sendId","=",msgSendMsg.getSendId()));
                if(lsitre != null && lsitre.size()>0){
                   for (Msg_receive_msg msgReceiveMsg : lsitre){
                        if(!msgReceiveMsg.isHasRead()) { //未查看可以删除,查看了就不行删除了
                          //  msgReceiveMsgService.delete(msgReceiveMsg.getId());
                            msgReceiveMsg.setDelFlag(true);
                            msgReceiveMsgService.update(msgReceiveMsg);
                        }
                   }
                }
			}
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping("/detail/{id}")
	public String detail(@PathVariable String id, HttpServletRequest req) {
		if (!Strings.isBlank(id)) {
            req.setAttribute("obj", msgSendMsgService.fetch(id));
		}else{
            req.setAttribute("obj", null);
        }
        return "pages/platform/msg/send/msg/detail";
    }

    @RequestMapping("/selectUser")
    @RequiresAuthentication
    public String selectUser() {
        return "pages/platform/msg/send/msg/selectUser";
    }

    @RequestMapping("/selectData")
    @SJson("full")
    @RequiresAuthentication
    public Object selectData(@RequestParam(value = "sendTo", required = false) String sendTo,
                             @RequestParam(value = "name", required = false) String name, @RequestParam("length") int length,
                             @RequestParam("start") int start, @RequestParam("draw") int draw,
                             ArrayList<DataTableOrder> order, ArrayList<DataTableColumn> columns) {
        String sql = "SELECT a.* FROM sys_user a WHERE 1=1 ";
        if (!Strings.isBlank(sendTo)) {
            //TODO 获取相应平台（平台，商户，会员）下用户
        }
        if (!Strings.isBlank(name)) {
            sql += " and (a.loginname like '%" + name + "%' or a.username like '%" + name + "%') ";
        }
        String s = sql;
        if (order != null && order.size() > 0) {
            for (DataTableOrder o : order) {
                DataTableColumn col = columns.get(o.getColumn());
                s += " order by a." + Sqls.escapeSqlFieldValue(col.getData()).toString() + " " + o.getDir();
            }
        }
        return msgSendMsgService.data(length, start, draw, Sqls.create(sql), Sqls.create(s));
    }

}

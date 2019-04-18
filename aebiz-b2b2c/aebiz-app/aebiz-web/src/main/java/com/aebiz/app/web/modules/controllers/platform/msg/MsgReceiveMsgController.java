package com.aebiz.app.web.modules.controllers.platform.msg;

import com.aebiz.app.msg.modules.models.Msg_send;
import com.aebiz.app.msg.modules.models.Msg_send_msg;
import com.aebiz.app.msg.modules.models.Msg_type;
import com.aebiz.app.msg.modules.services.MsgSendMsgService;
import com.aebiz.app.msg.modules.services.MsgSendService;
import com.aebiz.app.msg.modules.services.MsgTypeService;
import com.aebiz.app.web.commons.log.annotation.SLog;
import com.aebiz.baseframework.base.Result;
import com.aebiz.baseframework.page.datatable.DataTableColumn;
import com.aebiz.baseframework.page.datatable.DataTableOrder;
import com.aebiz.commons.utils.StringUtil;
import com.aebiz.app.msg.modules.models.Msg_receive_msg;
import com.aebiz.app.msg.modules.services.MsgReceiveMsgService;
import com.aebiz.baseframework.view.annotation.SJson;
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
@RequestMapping("/platform/msg/receive/msg")
public class MsgReceiveMsgController {
    @Autowired
	private MsgReceiveMsgService msgReceiveMsgService;
    @Autowired
    private MsgSendMsgService msgSendMsgService;
    @Autowired
    private MsgSendService msgSendService;
    @Autowired
    private MsgTypeService msgTypeService;


   @RequestMapping("")
   @RequiresPermissions("msg.info.manager")
   public String index(@RequestParam String msgType, HttpServletRequest req) {
       req.setAttribute("menuMsgType", msgType);

       List<Msg_type> msgtypeList = msgTypeService.query(Cnd.NEW());
       req.setAttribute("msgtypeList",msgtypeList);
       return "pages/platform/msg/receive/msg/index";
   }

	@RequestMapping("/data")
    @SJson("full")
    @RequiresPermissions("msg.info.manager")
    public Object data(@RequestParam(value = "msgType",required = false) String msgType,@RequestParam(value = "type",required = false) String type,@RequestParam("length") int length, @RequestParam("start") int start, @RequestParam("draw") int draw, ArrayList<DataTableOrder> order, ArrayList<DataTableColumn> columns) {
        Cnd cnd = Cnd.NEW();
        if("wd".equals(msgType)){
            cnd.and("readAt","is", null);
        }else if("hasread".equals(msgType)){
            cnd.and("readAt","is not",null);
        }
        if(!"".equals(type) && type!=null ){
            if("msgall".equals(msgType)){
                cnd.and("type","=",type);
            }else{
                cnd.and("type","=",type);
            }
        }
    	return msgReceiveMsgService.data(length, start, draw, order, columns, cnd, "^(msgSend|msgSendMsg)$");
    }

    @RequestMapping("/add")
    @RequiresPermissions("msg.info.manager")
    public String add() {
    	return "pages/platform/msg/receive/msg/add";
    }

    @RequestMapping("/addDo")
    @SJson
    @SLog(description = "Msg_receive_msg")
    @RequiresPermissions(value={"msg.info.manager.msgall.add","msg.info.manager.hasread.add","msg.info.manager.wd.add"})
    public Object addDo(Msg_receive_msg msgReceiveMsg, HttpServletRequest req) {
		try {
			msgReceiveMsgService.insert(msgReceiveMsg);
			return Result.success("globals.result.success");
		} catch (Exception e) {
			return Result.error("globals.result.error");
		}
    }

    @RequestMapping("/edit/{id}")
    @RequiresPermissions("msg.info.manager")
    public String edit(@PathVariable String id,HttpServletRequest req) {
		req.setAttribute("obj", msgReceiveMsgService.fetch(id));
		return "pages/platform/msg/receive/msg/edit";
    }

    @RequestMapping("/editDo")
    @SJson
    @SLog(description = "Msg_receive_msg")
    @RequiresPermissions(value={"msg.info.manager.msgall.edit","msg.info.manager.hasread.edit","msg.info.manager.wd.edit"})
    public Object editDo(Msg_receive_msg msgReceiveMsg, HttpServletRequest req) {
		try {
            msgReceiveMsg.setOpBy(StringUtil.getUid());
			msgReceiveMsg.setOpAt((int) (System.currentTimeMillis() / 1000));
			msgReceiveMsgService.updateIgnoreNull(msgReceiveMsg);
			return Result.success("globals.result.success");
		} catch (Exception e) {
			return Result.error("globals.result.error");
		}
    }

    @RequestMapping(value = {"/delete/{id}", "/delete"})
    @SJson
    @SLog(description = "Msg_receive_msg")
    @RequiresPermissions(value={"msg.info.manager.msgall.delete","msg.info.manager.hasread.delete","msg.info.manager.wd.delete"})
    public Object delete(@PathVariable(required = false) String id, @RequestParam(value = "ids",required = false)  String[] ids, HttpServletRequest req) {
		try {
			if(ids!=null&&ids.length>0){
				msgReceiveMsgService.delete(ids);
			}else{
				msgReceiveMsgService.delete(id);
			}
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }


    @RequestMapping(value = "/changeReadType/{id}")
    @SJson
    @SLog(description = "Msg_receive_msg")
    @RequiresPermissions(value={"msg.info.manager.msgall.delete","msg.info.manager.hasread.delete","msg.info.manager.wd.delete"})
    public Object changeReadType(@PathVariable(required = false) String id,  HttpServletRequest req) {
        try {
            Msg_receive_msg msgReceiveMsg = msgReceiveMsgService.fetch(id);
            msgReceiveMsg.setHasRead(true);
            msgReceiveMsg.setReadAt((int) (System.currentTimeMillis() / 1000));
            msgReceiveMsgService.update(msgReceiveMsg);
            req.setAttribute("id", id);
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping("/detail/{id}")
    @RequiresPermissions("msg.info.manager")
	public String detail(@PathVariable String id, HttpServletRequest req) {
		if (!Strings.isBlank(id)) {
            //更新消息接收者的状态
            Msg_receive_msg msgReceiveMsg = msgReceiveMsgService.fetch(id);
            msgReceiveMsg.setHasRead(true);
            msgReceiveMsg.setReadAt((int) (System.currentTimeMillis() / 1000));
            msgReceiveMsgService.update(msgReceiveMsg);
            Msg_send  msgSend = msgSendService.fetch(msgReceiveMsg.getSendId());
            Msg_send_msg msgSendMsg = msgSendMsgService.fetch(msgReceiveMsg.getMsgId());
            req.setAttribute("msgSend",msgSend);
            req.setAttribute("msgSendMsg",msgSendMsg);
            req.setAttribute("obj",msgReceiveMsg);
		}else{
            req.setAttribute("obj", null);
        }
        return "pages/platform/msg/receive/msg/detail";
    }

}

package com.aebiz.app.web.modules.controllers.platform.dec;

import com.aebiz.app.dec.commons.vo.BaseCompModel;
import com.aebiz.app.dec.commons.utils.DecorateCommonConstant;
import com.aebiz.app.dec.modules.models.*;
import com.aebiz.app.dec.modules.services.DecComponentClassService;
import com.aebiz.app.dec.modules.services.DecComponentResourceService;
import com.aebiz.app.web.commons.log.annotation.SLog;
import com.aebiz.baseframework.base.Result;
import com.aebiz.baseframework.page.datatable.DataTableColumn;
import com.aebiz.baseframework.page.datatable.DataTableOrder;
import com.aebiz.commons.utils.StringUtil;
import com.aebiz.app.dec.modules.services.DecComponentService;
import com.aebiz.app.dec.modules.models.Dec_component_class;
import com.aebiz.baseframework.view.annotation.SJson;
import com.alibaba.fastjson.JSON;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.dao.Cnd;
import org.nutz.json.Json;
import org.nutz.lang.Files;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/platform/dec/component")
public class DecComponentController {
    private static final Log log = Logs.get();
    @Autowired
	private DecComponentService decComponentService;

    @Autowired
    private DecComponentClassService decComponentClassService;

    @Autowired
    private DecComponentResourceService decComponentResourceService;

    @RequestMapping("")
    @RequiresPermissions("dec.component")
	public String index(HttpServletRequest req) {
        req.setAttribute("versionType",req.getParameter("versionType"));
        req.setAttribute("ClassList",decComponentClassService.query(Cnd.NEW()));
		return "pages/platform/dec/component/index";
	}

	@RequestMapping("/data")
    @SJson("full")
    @RequiresPermissions("dec.component")
    public Object data(@RequestParam("versionType") String  versionType,@RequestParam("compId") String  compId,@RequestParam("compName") String  compName,@RequestParam("compType") String  compType,
            @RequestParam("length") int length, @RequestParam("start") int start, @RequestParam("draw") int draw, ArrayList<DataTableOrder> order, ArrayList<DataTableColumn> columns) {
        Cnd cnd = Cnd.NEW();
    	return decComponentService.data(versionType,compId,compName,compType,length, start, draw, order, columns, cnd, null);
    }

    @RequestMapping("/add")
    @RequiresPermissions("dec.component")
    public String add(HttpServletRequest req) {
        req.setAttribute("versionType",req.getParameter("versionType"));
        List<Dec_component_class> list=(List<Dec_component_class>)decComponentClassService.query(Cnd.where("1", "=", "1"));
        req.setAttribute("list",list);
        return "pages/platform/dec/component/add";
    }

    @RequestMapping("/addDo")
    @SJson
    @SLog(description = "Dec_component")
    @RequiresPermissions("dec.component.add")
    public Object addDo(Dec_component decComponent, HttpServletRequest req) {
		try {
            boolean flag=decComponentService.isCompIdExist(decComponent.getCompId());
            if(flag){
                decComponentService.insert(decComponent);
                return Result.success("globals.result.success");
            }else{
                return Result.error("该组件编号已经有了");
            }
		} catch (Exception e) {
			return Result.error("globals.result.error");
		}
    }

    @RequestMapping("/edit/{id}")
    @RequiresPermissions("dec.component")
    public String edit(@PathVariable String id,HttpServletRequest req) {
        List<Dec_component_class> list=(List<Dec_component_class>)decComponentClassService.query(Cnd.where("1", "=", "1"));
        req.setAttribute("list",list);
		req.setAttribute("obj", decComponentService.fetch(id));
		return "pages/platform/dec/component/edit";
    }

    @RequestMapping("/editDo")
    @SJson
    @SLog(description = "Dec_component")
    @RequiresPermissions("dec.component.edit")
    public Object editDo(Dec_component decComponent, HttpServletRequest req) {
		try {
            decComponent.setOpBy(StringUtil.getUid());
			decComponent.setOpAt((int) (System.currentTimeMillis() / 1000));
			decComponentService.updateIgnoreNull(decComponent);
			return Result.success("globals.result.success");
		} catch (Exception e) {
			return Result.error("globals.result.error");
		}
    }

    @RequestMapping(value = {"/delete/{id}", "/delete"})
    @SJson
    @SLog(description = "Dec_component")
    @RequiresPermissions("dec.component.del")
    public Object delete(@PathVariable(required = false) String id, @RequestParam(value = "ids",required = false)  String[] ids, HttpServletRequest req) {
		try {
			if(ids!=null&&ids.length>0){
				decComponentService.deleteComps(ids);
    			req.setAttribute("id", org.apache.shiro.util.StringUtils.toString(ids));
			}else{
				decComponentService.deleteComps(id);
    			req.setAttribute("id", id);
			}
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping("/goComponentClass")
    @RequiresPermissions("dec.component.class")
    public String goComponentClass() {
        return "pages/platform/dec/class/index";
    }

    @RequestMapping("/dataComponentClass")
    @SJson("full")
    @RequiresPermissions("dec.component.class")
    public Object dataComponentClass(@RequestParam("length") int length, @RequestParam("start") int start, @RequestParam("draw") int draw, ArrayList<DataTableOrder> order, ArrayList<DataTableColumn> columns) {
        Cnd cnd = Cnd.NEW();
        return decComponentClassService.data(length, start, draw, order, columns, cnd, null);
    }

    @RequestMapping("/addComponentClass")
    @RequiresPermissions("dec.component.class")
    public String addComponentClass() {
        return "pages/platform/dec/class/add";
    }
    @RequestMapping("/addComponentClassDo")
    @SJson
    @SLog(description = "Dec_component_class")
    @RequiresPermissions("dec.component.class")
    public Object addComponentClassDo(Dec_component_class decComponentClass, HttpServletRequest req) {
        try {
            decComponentClassService.insert(decComponentClass);
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping("/editComponentClass/{id}")
    @RequiresPermissions("dec.component.class")
    public String editComponentClass(@PathVariable String id,HttpServletRequest req) {
        req.setAttribute("obj", decComponentClassService.fetch(id));
        return "pages/platform/dec/class/edit";
    }

    @RequestMapping("/editComponentClassDo")
    @SJson
    @SLog(description = "Dec_component_class")
    @RequiresPermissions("dec.component.class")
    public Object editComponentClassDo(Dec_component_class decComponentClass, HttpServletRequest req) {
        try {
            decComponentClass.setOpBy(StringUtil.getUid());
            decComponentClass.setOpAt((int) (System.currentTimeMillis() / 1000));
            decComponentClassService.updateIgnoreNull(decComponentClass);
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping(value = {"/deleteComponentClass/{id}", "/deleteComponentClass"})
    @SJson
    @SLog(description = "Dec_component_class")
    @RequiresPermissions("dec.component.class")
    public Object deleteComponentClass(@PathVariable(required = false) String id, @RequestParam(value = "ids",required = false)  String[] ids, HttpServletRequest req) {
        try {
            if(ids!=null&&ids.length>0){
                decComponentClassService.delete(ids);
                req.setAttribute("id", org.apache.shiro.util.StringUtils.toString(ids));
            }else{
                decComponentClassService.delete(id);
                req.setAttribute("id", id);
            }
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    /**
     * 跳转到组件注册页面
     *
     * @param id
     * @param req
     * @return
     */
    @RequestMapping(value = { "/toRegister/{id}" })
    @RequiresPermissions("dec.component.Register")
    public String toRegister(@PathVariable String id, HttpServletRequest req) {
        Cnd cnd=Cnd.NEW();
        Dec_component m =decComponentService.fetch(id);
        req.setAttribute("m",m);

        // 查询组件是否已经注册过
        Dec_component_resource jspResourceModel = decComponentResourceService.getComponentResource(id, DecorateCommonConstant.COMPONENT_RESOURCETYE_JSP);

        if (jspResourceModel != null) {
            req.setAttribute("whetherRegister", "true");
        }else {
            req.setAttribute("whetherRegister", "false");
        }
        req.setAttribute("Result","");
        return "pages/platform/dec/component/addResouce";
    }

    /**
     * 将显示的页面、设置参数页面和引用的js注册到后端
     *
     * @param compUuid
     * @param myHtmlFiles
     * @param myJsFiles
     * @param response
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/register")
    @Transactional
    public Object register(@RequestParam("compUuid") String compUuid,
                           @RequestParam(value = "myHtmlFile") MultipartFile[] myHtmlFiles,
                           @RequestParam(value = "myJspFile") MultipartFile[] myJspFiles,
                           @RequestParam(value = "myJsFile") MultipartFile[] myJsFiles,
                           HttpServletResponse response, HttpServletRequest request) throws Exception {
        Dec_component m = decComponentService.fetch(compUuid);
        request.setAttribute("m",m);
        try {
            // Html文件
            MultipartFile htmlFile = null;
            // Jsp文件
            MultipartFile jspFile = null;
            // Js文件
            MultipartFile jsFile = null;

            if (myHtmlFiles != null && myHtmlFiles.length > 0) {
                htmlFile = myHtmlFiles[0];
            }
            if (myJspFiles != null && myJspFiles.length > 0) {
                jspFile = myJspFiles[0];
            }
            if (myJsFiles != null && myJsFiles.length > 0) {
                jsFile = myJsFiles[0];
            }

            String compId = m.getCompId();

            if (htmlFile != null) {
                decComponentResourceService.saveComponentHtmlResource(compUuid, compId, htmlFile.getBytes());
            }
            if (jsFile != null) {
                decComponentResourceService.saveComponentJsResource(compUuid, compId, jsFile.getBytes());
            }
            if (jspFile != null) {
                decComponentResourceService.saveComponentJspResource(compUuid, compId, jspFile.getBytes());
            }
            request.setAttribute("whetherRegister", "true");
            request.setAttribute("Result","success");
        }catch (Exception e){
            request.setAttribute("whetherRegister", "false");
            request.setAttribute("Result","error");
            log.error(e);
        }
        return "pages/platform/dec/component/addResouce";
    }

    /**
     * 获取所有组件列表
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value = { "getComps" }, method = { RequestMethod.GET })
    @SJson
    public Object getComps(@RequestParam String versionType,HttpServletRequest request,
                           HttpServletResponse response) throws Exception {
       String contextPath = request.getContextPath();
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        try {
            List<CompGroupModel> components=decComponentService.getUsableComps(contextPath,versionType);
            response.getWriter().print(JSON.toJSONString(components));
           // response.getWriter().print(JSON.toJSONString(decComponentService.getUsableComps(contextPath,versionType)));
        } catch (Exception e) {
            log.error(e);
        }
        return null;
    }

    /**
     * 获取某个组件的参数页面
     *
     * @param compId
     * @param response
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping({ "/getCompParamsDefine" })
    @SJson
    public Object getCompParamsDefine(@RequestParam String compId,
                                      @RequestParam String pageJson, HttpServletResponse response,
                                      HttpServletRequest request) throws Exception {
        Dec_component componentsModel = decComponentService.getComponentsModelByCompId(compId);

        if(DecorateCommonConstant.COMPONENTS_USETYPE_CUSTOM==componentsModel.getCompType()){
            String content = decComponentResourceService.getUserDefinedParamHtml(componentsModel.getCompId());
            //把页面需要的参数${webPageModelJsonStr}替换掉
            content = content.replace("${webPageModelJsonStr}", pageJson);
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/html;charset=UTF-8");
            try {
                response.getWriter().print(content);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }


        Dec_component_resource componentResource = decComponentResourceService
                .getComponentResource(componentsModel.getId(), DecorateCommonConstant.COMPONENT_RESOURCETYE_JSP);

        String versionNo = componentResource.getVersionNo();

        // 获取要生成的JSP文件夹所在路径
        String jspDistPath = request.getServletContext().getRealPath(
                "/WEB-INF/views/pages/platform/dec/syscomps/" + compId + "/");

        File destDir = new File(jspDistPath);
        if (!destDir.exists()) {
            destDir.mkdirs();
        }

        // JSP文件生成路径
        String jspFilePath = jspDistPath + "/paramsDefine" + "_" + versionNo + ".html";

        File jspFile = new File(jspFilePath);
       if (!jspFile.exists()) {
            // 先删除原有版本的jsp文件
            File[] files = destDir.listFiles();
            for (int i = 0; i < files.length; i++) {
                files[i].delete();
            }
            String jspResourceKey = componentResource.getResourceKey();
            String jspFileContent = decComponentResourceService.getRedisResourceByKey(jspResourceKey);
            File file=new File(jspFilePath);
            if(file.exists()) {
               Files.deleteFile(file);
            } else if(!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            DataOutputStream dout = null;

           try {
               file.createNewFile();
               dout = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
               if(true) {
                   dout.write(jspFileContent.getBytes("utf-8"));
               } else {
                   dout.write(jspFileContent.getBytes());
               }
           } catch (Exception var14) {
               var14.printStackTrace();
           } finally {
               try {
                   dout.close();
               } catch (IOException var13) {
                   var13.printStackTrace();
               }

           }
        }
        String toUrl = "pages/platform/dec/syscomps/" + compId + "/paramsDefine" + "_" + versionNo;

        String classFullName = componentsModel.getClassFullName();
        BaseCompModel bcm = (BaseCompModel)Class.forName(classFullName).newInstance();

        // 组件参数页面指向的文件路径
        request.setAttribute(DecorateCommonConstant.COMPONENT_TOPARAMSJSP_URL, toUrl);
        // WebPageModel Json字符串
        request.setAttribute(DecorateCommonConstant.COMPONENT_WEBPAGEMODEL_JSONSTR, pageJson);
        String uurl=bcm.getUrlToParamsJsp();
        request.getServletContext().getRequestDispatcher(bcm.getUrlToParamsJsp()).forward(request, response);

        return null;
    }
}

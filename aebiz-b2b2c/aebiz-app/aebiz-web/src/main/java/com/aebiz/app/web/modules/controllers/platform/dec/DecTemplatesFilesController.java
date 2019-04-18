package com.aebiz.app.web.modules.controllers.platform.dec;

import com.aebiz.app.dec.commons.utils.DecorateCommonConstant;
import com.aebiz.app.dec.commons.utils.TreeElement;
import com.aebiz.app.dec.modules.models.Dec_templates_style;
import com.aebiz.app.dec.modules.services.*;
import com.aebiz.app.dec.modules.services.impl.DecComponentResourceServiceImpl;
import com.aebiz.app.web.commons.base.Globals;
import com.aebiz.app.web.commons.log.annotation.SLog;
import com.aebiz.baseframework.base.Result;
import com.aebiz.baseframework.page.datatable.DataTableColumn;
import com.aebiz.baseframework.page.datatable.DataTableOrder;
import com.aebiz.baseframework.redis.RedisService;
import com.aebiz.baseframework.view.annotation.SFile;
import com.aebiz.commons.utils.StringUtil;
import com.aebiz.app.dec.modules.models.Dec_templates_files;
import com.aebiz.baseframework.view.annotation.SJson;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.dao.Cnd;
import org.nutz.lang.Files;
import org.nutz.lang.Strings;
import org.nutz.lang.util.NutMap;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/platform/dec/templates/files")
public class DecTemplatesFilesController {
    private static final Log log = Logs.get();
    @Autowired
	private DecTemplatesFilesService decTemplatesFilesService;
    @Autowired
    private DecComponentResourceServiceImpl decComponentResourceService;

    @Autowired
    private DecTemplatesStyleService decTemplatesStyleService;

    @Autowired
    private DecTemplatesResourceService decTemplatesResourceService;

    @Autowired
    private RedisService redisService;

    @RequestMapping("")
    @RequiresPermissions("platform.dec.templates.files")
	public String index() {
		return "pages/platform/dec/templates/files/index";
	}

	@RequestMapping("/data")
    @SJson("full")
    @RequiresPermissions("platform.dec.templates.files")
    public Object data(@RequestParam("length") int length, @RequestParam("start") int start, @RequestParam("draw") int draw, ArrayList<DataTableOrder> order, ArrayList<DataTableColumn> columns) {
		Cnd cnd = Cnd.NEW();
    	return decTemplatesFilesService.data(length, start, draw, order, columns, cnd, null);
    }

    @RequestMapping("/add")
    @RequiresPermissions("platform.dec.templates.files")
    public String add() {
    	return "pages/platform/dec/templates/files/add";
    }

    @RequestMapping("/addDo")
    @SJson
    @SLog(description = "Dec_templates_files")
    @RequiresPermissions("platform.dec.templates.files.add")
    public Object addDo(Dec_templates_files decTemplatesFiles, HttpServletRequest req) {
		try {
			decTemplatesFilesService.insert(decTemplatesFiles);
			return Result.success("globals.result.success");
		} catch (Exception e) {
			return Result.error("globals.result.error");
		}
    }

    @RequestMapping("/edit/{id}")
    @RequiresPermissions("platform.dec.templates.files")
    public String edit(@PathVariable String id,HttpServletRequest req) {
		req.setAttribute("obj", decTemplatesFilesService.fetch(id));
		return "pages/platform/dec/templates/files/edit";
    }

    @RequestMapping("/editDo")
    @SJson
    @SLog(description = "Dec_templates_files")
    @RequiresPermissions("platform.dec.templates.files.edit")
    public Object editDo(Dec_templates_files decTemplatesFiles, HttpServletRequest req) {
		try {
            decTemplatesFiles.setOpBy(StringUtil.getUid());
			decTemplatesFiles.setOpAt((int) (System.currentTimeMillis() / 1000));
			decTemplatesFilesService.updateIgnoreNull(decTemplatesFiles);
			return Result.success("globals.result.success");
		} catch (Exception e) {
			return Result.error("globals.result.error");
		}
    }

    @RequestMapping(value = {"/delete/{id}", "/delete"})
    @SJson
    @SLog(description = "Dec_templates_files")
    @RequiresPermissions("platform.dec.templates.files.delete")
    public Object delete(@PathVariable(required = false) String id, @RequestParam(value = "ids",required = false)  String[] ids, HttpServletRequest req) {
		try {
			if(ids!=null&&ids.length>0){
				decTemplatesFilesService.delete(ids);
    			req.setAttribute("id", org.apache.shiro.util.StringUtils.toString(ids));
			}else{
				decTemplatesFilesService.delete(id);

    			req.setAttribute("id", id);
			}
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping("/detail/{id}")
    @RequiresPermissions("platform.dec.templates.files")
	public String detail(@PathVariable String id, HttpServletRequest req) {
		if (!Strings.isBlank(id)) {
            req.setAttribute("obj", decTemplatesFilesService.fetch(id));
		}else{
            req.setAttribute("obj", null);
        }
        return "pages/platform/dec/templates/files/detail";
    }
    /**
     * 根据文件保存的key下载对应好的文件
     *
     * @param key
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    @RequestMapping(value="/getFile", method=RequestMethod.GET)
    @SFile("css")
    public Object getFile(@RequestParam("key") String key,
                                          HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        byte[] fileBytes = decTemplatesResourceService.getRedisResoureBytesByKey(key);

        if (fileBytes == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        String fileType = Files.getSuffix(key);
        String fileName = new String(key.getBytes("UTF-8"), "iso-8859-1");// 为了解决中文名称乱码问题
        if (".css".equalsIgnoreCase(fileType)) {
            String templateUuid = key.substring(0,key.indexOf("_"));
            String fileStr = new String(fileBytes);
            fileStr = fileStr.replaceAll("\\$\\{base!\\}", Globals.APP_BASE);
            fileStr = fileStr.replaceAll("\\$\\{templateUuid\\}", templateUuid);
            response.setContentType("text/css");
            fileBytes = fileStr.getBytes();

        } else if (".js".equals(fileType)) {
            response.setContentType("text/javascript");
        } else {
            response.setContentType("application/octet-stream");
        }
        response.addHeader("attachment",fileName);

        return fileBytes;
    }

    /**
     * 获取模板当前使用的皮肤
     *
     * @param templateUuid
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/getSkin", method = RequestMethod.GET, produces = { "text/html;charset=UTF-8" })
    @SFile("css")
    public Object getSkin(
            @RequestParam("templateUuid") String templateUuid,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        Dec_templates_style styleModel = decTemplatesStyleService.getCurrentStyleOfTemplate(templateUuid);

        if (styleModel == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        String cssFileName = styleModel.getStyleFileName();
        String cssResourceKey = templateUuid + "_css_" + cssFileName;
        byte[] fileBytes = decTemplatesResourceService.getRedisResoureBytesByKey(cssResourceKey);
        if(fileBytes !=null){
            String fileStr = new String(fileBytes);
            fileStr = fileStr.replaceAll("\\$\\{base!\\}", request.getServletContext().getContextPath());
            fileStr = fileStr.replaceAll("\\$\\{templateUuid\\}", templateUuid);
            String fileName =  new String(cssFileName.getBytes("UTF-8"), "iso-8859-1");// 为了解决中文名称乱码问题
            try (Jedis jedis = redisService.jedis()) {
                fileBytes = fileStr.getBytes();
                jedis.get(fileBytes);
            }
            return fileBytes;
        }
        return fileBytes;
    }


    /**
     * 构造模板文件夹树
     *
     * @param key
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value = "/getNodes", method = RequestMethod.GET)
    @ResponseBody
    public List<TreeElement> getNodes(@RequestParam(value = "key") String key,
                                      @RequestParam(value = "templateUuid") String templateUuid,
                                      Model model, HttpServletRequest request) {

        List<TreeElement> list = decTemplatesFilesService.getRootList(key, templateUuid);
        return list;
    }
    /**
     * 修改文件夹名称
     *
     * @param showName
     * @param selectUuid
     * @param request
     * @return
     */
    @RequestMapping(value = "/updateFolder", method = RequestMethod.GET)
    @SJson
    public Object updateFolder(@RequestParam String showName, @RequestParam String selectUuid,
                                            @RequestParam String note, HttpServletRequest request) {

        Dec_templates_files m = decTemplatesFilesService.fetch(selectUuid);
        m.setShowName(showName);
        m.setNote(note);
        decTemplatesFilesService.update(m);
        return NutMap.NEW().put("rsp", true);
    }
    /**
     * 删除页面
     *
     * @param pageUuid
     * @param model
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value = { "/deletePage" }, method = { RequestMethod.GET })
    @ResponseBody
    @SJson
    public String deletePage(@RequestParam("pageUuid") String pageUuid,
                             Model model, HttpServletRequest request,
                             HttpServletResponse response) throws Exception {

        decTemplatesFilesService.deletePageFileByPageUuid(pageUuid);

        return "success";
    }

    /**
     * 删除文件夹
     *
     * @param selectUuid
     * @param model
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value = { "/deleteFolder" }, method = { RequestMethod.GET })
    @ResponseBody
    public String deleteFolder(@RequestParam("selectUuid") String selectUuid,
                               Model model, HttpServletRequest request,
                               HttpServletResponse response) throws Exception {

        Dec_templates_files m = decTemplatesFilesService.fetch(selectUuid);

        if (DecorateCommonConstant.TEMPLATE_FOLDER_DEFAULT_YES==(m.getIsDefault())) {
            return "isSystemFolder";
        }

        decTemplatesFilesService.deleteFolder(selectUuid);

        return "success";
    }

    @RequestMapping("/templateFileResource/{id}")
    public String templateFileResource(@PathVariable String id, HttpServletRequest req){
       req.setAttribute("templateUuid",id);
        return "pages/platform/dec/templates/files/TemplateFileList";
    }
    /**
     * 上传模板资源文件
     *
     * @param folderId
     * @param resourceFiles
     * @param response
     * @param request
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("/uploadFiles")
    public String uploadFiles(
            @RequestParam String folderId,
            @RequestParam String templateUuid,
            @RequestParam(value = "resourceFiles") MultipartFile[] resourceFiles,
            HttpServletResponse response, HttpServletRequest request,
            Model model) throws Exception {

        String nowSelectKey = request.getParameter("nowSelectKey");

        if (resourceFiles != null && resourceFiles.length > 0) {
            decTemplatesResourceService.uploadFileResources(folderId, resourceFiles);
        }

        return "redirect:/platform/dec/templates/files/resourceIndex?folderUuid="+nowSelectKey+"&templateUuid=" + templateUuid;
    }

    /**
     * 新建子文件夹
     *
     * @param model
     * @param showName
     * @param parentUuid
     * @param request
     * @return
     */
    @RequestMapping(value = "/createFolder", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> createFolder(Model model,
                                            @RequestParam String templateUuid, @RequestParam String showName,
                                            @RequestParam String note, @RequestParam String parentUuid,
                                            HttpServletRequest request) {
        Map<String, Object> reJsonMap = new HashMap<String, Object>();

        boolean subFolderNameExisted = decTemplatesFilesService.checkSubFolderNameExisted(parentUuid, showName);

        if (subFolderNameExisted) {
            reJsonMap.put("rsp", Boolean.valueOf(false));
            reJsonMap.put("errorMsg", "文件夹已存在！");
            return reJsonMap;
        }

        Dec_templates_files m = new Dec_templates_files();
        m.setTemplateUuid(templateUuid);
        // 文件夹名称
        m.setShowName(showName);
        // 上级uuid
        m.setParentUuid(parentUuid);
        m.setNote(note);

        // 创建文件夹
        decTemplatesFilesService.createTemplateFolder(m);
        model.addAttribute("m", m);

        reJsonMap.put("rsp", Boolean.valueOf(true));
        return reJsonMap;
    }

    @RequestMapping("/resourceIndex")
    @RequiresPermissions("dec.templates.resource")
    public String resourceIndex(@RequestParam("folderUuid")String folderUuid,HttpServletRequest req) {
        req.setAttribute("folderUuid",folderUuid);
        return "pages/platform/dec/templates/files/templatesResource";
    }
}

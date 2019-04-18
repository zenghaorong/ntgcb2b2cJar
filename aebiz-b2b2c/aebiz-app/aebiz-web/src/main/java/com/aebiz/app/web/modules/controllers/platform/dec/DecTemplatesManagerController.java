package com.aebiz.app.web.modules.controllers.platform.dec;

import com.aebiz.app.dec.commons.vo.BaseCompModel;
import com.aebiz.app.dec.commons.vo.Datatable;
import com.aebiz.app.dec.commons.vo.WebPageModel;
import com.aebiz.app.dec.commons.utils.DecorateCacheConstant;
import com.aebiz.app.dec.commons.utils.DecorateCommonConstant;
import com.aebiz.app.dec.commons.utils.ReflectUtil;
import com.aebiz.app.dec.commons.comps.userdefined.vo.UserDefinedModel;
import com.aebiz.app.dec.modules.models.Dec_component;
import com.aebiz.app.dec.modules.models.Dec_templates_pages;
import com.aebiz.app.dec.modules.models.Dec_templates_sub;
import com.aebiz.app.dec.modules.models.em.PageTypeEnum;
import com.aebiz.app.dec.modules.services.*;
import com.aebiz.app.web.commons.log.annotation.SLog;
import com.aebiz.baseframework.base.Result;
import com.aebiz.baseframework.page.datatable.DataTableColumn;
import com.aebiz.baseframework.page.datatable.DataTableOrder;
import com.aebiz.commons.utils.StringUtil;
import com.aebiz.app.dec.modules.models.Dec_templates_manager;
import com.aebiz.baseframework.view.annotation.SJson;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.dao.Cnd;
import org.nutz.json.Json;
import org.nutz.lang.Lang;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Controller
@RequestMapping("/platform/dec/templates/manager")
public class DecTemplatesManagerController {
    private static final Log log = Logs.get();
    @Autowired
    private DecTemplatesManagerService decTemplatesManagerService;

    @Autowired
    private DecTemplatesPagesService decTemplatesPagesService;

    @Autowired
    private DecTemplatesSubService decTemplatesSubService;

    @Autowired
    private DecTemplatesResourceService decTemplatesResourceService;

    @Autowired
    private DecTemplatesFilesService decTemplatesFilesService;
    @Autowired
    private DecComponentService decComponentService;

    @Autowired
    private BeanFactory bf = null;

    @Autowired
    private DecTemplatesStyleService decTemplatesStyleService;

    @RequestMapping("")
    @RequiresPermissions("dec.templates")
    public String index(HttpServletRequest req) {
        //判断是pc端还是手机端
        String versionType = req.getParameter("versionType");
        //获取商户的id
        String storeId = StringUtil.getStoreId();
        if (!Strings.isEmpty(versionType)) {
            req.setAttribute("versionType", versionType);
            req.setAttribute("templateList", decTemplatesManagerService.query(Cnd.where("versionType", "=", versionType)));
        } else {
            req.setAttribute("versionType", 1);
            req.setAttribute("templateList", null);
        }
        return "pages/platform/dec/templates/manager/templatesList";
    }

    @RequestMapping("/data")
    @SJson("full")
    @RequiresPermissions("dec.templates")
    public Object data(@RequestParam("length") int length, @RequestParam("start") int start, @RequestParam("draw") int draw, ArrayList<DataTableOrder> order, ArrayList<DataTableColumn> columns) {
        Cnd cnd = Cnd.NEW();
        return decTemplatesManagerService.data(length, start, draw, order, columns, cnd, null);
    }

    @RequestMapping("/add")
    @RequiresPermissions("dec.templates")
    public String add(@RequestParam(value = "versionType") String versionType, HttpServletRequest req) {
        req.setAttribute("versionType", versionType);
        return "pages/platform/dec/templates/manager/add";
    }

    @RequestMapping("/addDo")
    @SJson
    @SLog(description = "Dec_templates_manager")
    @RequiresPermissions("dec.templates.add")
    public Object addDo(Dec_templates_manager decTemplatesManager, HttpServletRequest req) {
        boolean flag = decTemplatesManagerService.createAll(decTemplatesManager);
        if (flag) {
            return Result.success("globals.result.success");
        } else {
            return Result.error("globals.result.error");
        }


    }

    @RequestMapping("/edit/{id}")
    @RequiresPermissions("dec.templates")
    public String edit(@PathVariable String id, HttpServletRequest req) {
        req.setAttribute("obj", decTemplatesManagerService.fetch(id));
        return "pages/platform/dec/templates/manager/edit";
    }

    @RequestMapping("/editDo")
    @SJson
    @SLog(description = "Dec_templates_manager")
    @RequiresPermissions("dec.templates.edit")
    public Object editDo(Dec_templates_manager decTemplatesManager, HttpServletRequest req) {
        try {
            decTemplatesManager.setOpBy(StringUtil.getUid());
            decTemplatesManager.setOpAt((int) (System.currentTimeMillis() / 1000));
            decTemplatesManagerService.updateIgnoreNull(decTemplatesManager);
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping(value = {"/delete/{id}", "/delete"})
    @SJson
    @SLog(description = "Dec_templates_manager")
    @RequiresPermissions("dec.templates.del")
    public Object delete(@PathVariable(required = false) String id, @RequestParam(value = "ids", required = false) String[] ids, HttpServletRequest req) {
        boolean flag = decTemplatesManagerService.deleteAllByTemplateUuid(id);
        if (flag) {
            return Result.success("globals.result.success");
        } else {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping("/detail/{id}")
    @RequiresPermissions("dec.templates")
    public String detail(@PathVariable String id, HttpServletRequest req) {
        if (!Strings.isBlank(id)) {
            req.setAttribute("obj", decTemplatesManagerService.fetch(id));
        } else {
            req.setAttribute("obj", null);
        }
        return "pages/platform/dec/templates/manager/detail";
    }

    /**
     * 启用模板
     *
     * @param id
     * @return
     */
    @RequestMapping("/enable/{id}")
    @SJson
    public Object enable(@PathVariable String id) {
        try {
            Cnd cnd = Cnd.NEW();
            Dec_templates_manager m = decTemplatesManagerService.fetch(id);
            List<Dec_templates_manager> listModel = (List<Dec_templates_manager>) decTemplatesManagerService.query(cnd);
            if (listModel != null && listModel.size() > 0) {
                for (int i = 0; i < listModel.size(); i++) {
                    listModel.get(i).setDisabled(false);
                    decTemplatesManagerService.update(listModel.get(i));
                }
            }
            m.setDisabled(true);
            decTemplatesManagerService.update(m);
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    /**
     * 进入页面的自定义页面
     *
     * @param request
     * @return
     */
    @RequestMapping("/toPageDefine")
    public String toPageDefine(@RequestParam("templateUuid") String templateUuid,
                               @RequestParam("pageUuid") String pageUuid, HttpServletRequest request) {
        Cnd cnd = Cnd.NEW();
        request.setAttribute("templateUuid", templateUuid);
        request.setAttribute("pageUuid", pageUuid);

        // 获取当前模板名称
        Dec_templates_manager templatesModel = decTemplatesManagerService.fetch(templateUuid);
        //判断是pc/手机/微信/tv(1:pc 2:手机 3：微信 4 tv)
        String versionType = templatesModel.getVersionType();
        request.setAttribute("versionType", versionType);
        request.setAttribute("templateName", templatesModel.getTemplateZhName());

        // 获取当前模板下的所有页面
        List<Dec_templates_pages> pageList = decTemplatesPagesService.query(cnd.where("templateUuid", "=", templateUuid));
        request.setAttribute("pageList", pageList);

        // 获取当前页面的版本号
        Dec_templates_sub subPagesModel = decTemplatesSubService.fetch(cnd.where("pageUuid", "=", pageUuid));
        request.setAttribute("pageCurrentVersion", subPagesModel.getVersionNo());

        String[] preKeyArr = {DecorateCacheConstant.DESIGNER_PAGEVEIWHTML, DecorateCacheConstant.DESIGNER_PAGEMODELJSON};
        String[] pageResource = decTemplatesPagesService.getPageRedisResourceByPageUuid(pageUuid, preKeyArr);
        if (pageResource != null && pageResource.length > 0) {
            request.setAttribute("pageViewHtml", pageResource[0]);
            request.setAttribute("pageModelJson", pageResource[1]);
        }
        if (!Strings.isEmpty(versionType)) {
            if ("1".equals(versionType)) {
                return "pages/platform/dec/designer/PageDefine";
            } else if ("2".equals(versionType) || "3".equals(versionType)) {
                return "pages/platform/dec/designer/PageMobileDefine";
            }
        }
        return "pages/platform/dec/designer/PageDefine";
    }

    /**
     * 保存页面的自定义页面
     *
     * @param pageUuid
     * @param saveHtml
     * @param pageJson
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/save")
    public String save(@RequestParam("pageUuid") String pageUuid,
                       @RequestParam("saveHtml") String saveHtml,
                       @RequestParam("pageJson") String pageJson,
                       HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        List<String[]> list = new ArrayList<String[]>();

        WebPageModel wpm = this.parseJsonStrToWebPageModel(pageJson, request);

        Dec_templates_sub subPagesModel = decTemplatesSubService.getResourceOfCurrentPage(pageUuid);

        String pageJs = "";
        if (subPagesModel != null) {
            String resourceKey = subPagesModel.getResourceKey();

            String pageViewHtmlKey = DecorateCacheConstant.DESIGNER_PAGEVEIWHTML + resourceKey;
            String pageModelJsonKey = DecorateCacheConstant.DESIGNER_PAGEMODELJSON + resourceKey;
            String[] pageViewHtmlArr = {pageViewHtmlKey, saveHtml};
            String[] pageModelJsonArr = {pageModelJsonKey, pageJson};

            list.add(pageViewHtmlArr);
            list.add(pageModelJsonArr);

            String pageModelKey = DecorateCacheConstant.DESIGNER_PAGEMODEL + resourceKey;

            decTemplatesPagesService.savePageModelBytesToRedis(pageModelKey.getBytes(), Lang.toBytes(wpm));

            if (wpm != null && !wpm.getMapComps().isEmpty()) {
                for (String compId : wpm.getMapComps().keySet()) {
                    String compType = compId.split("_")[1];
                    String compJsKey = DecorateCacheConstant.COMPONENTS_JS_REDIS_KEY + compType;
                    String designJs = decTemplatesPagesService.getRedisResourceByKey(compJsKey, true);
                    pageJs += this.executePageDesignJs(compId, designJs, wpm) + "\r\n";
                }
            }
            if (!Strings.isEmpty(pageJs)) {
                String pageJsKey = DecorateCacheConstant.DESIGNER_PAGEJS + resourceKey;
                String[] pageJsArr = {pageJsKey, pageJs};
                list.add(pageJsArr);
            }
        }

        decTemplatesPagesService.saveDesingerPage(list);

        response.getWriter().print("true");

        return null;
    }

    /**
     * 根据传递过来的JSON字符串转换成WebPageModel对象
     *
     * @param jsonStr
     * @return
     */
    @SuppressWarnings("rawtypes")
    private WebPageModel parseJsonStrToWebPageModel(String jsonStr, HttpServletRequest request) {
        WebPageModel model = Json.fromJson(WebPageModel.class, jsonStr);
        Map<String, Object> map = (Map<String, Object>) Json.fromJson(jsonStr);
        Object compsObj = map.get("comps");
        String js = Json.toJson(compsObj);
        List<HashMap> list = Json.fromJsonAsList(HashMap.class, js);
        try {
            for (Iterator<HashMap> iterator = list.iterator(); iterator.hasNext(); ) {
                Map<String, Object> hashMap = (HashMap<String, Object>) iterator.next();
                String compIdStr = (String) hashMap.get("compId");
                // 默认首屏加载
                String needAsyncInitStr = "true";
                Object needAsyncInitObj = hashMap.get("needAsyncInit");
                if (needAsyncInitObj != null) {
                    needAsyncInitStr = needAsyncInitObj.toString();
                }
                if (!Strings.isEmpty(compIdStr)) {
                    String compId = compIdStr.split("_")[1];

                    // 根据compId获取组件对象
                    Dec_component componentsModel = decComponentService.getComponentsModelByCompId(compId);
                    if (componentsModel != null) {
                        String classFullName = componentsModel.getClassFullName();
                        Class compClass = Class.forName(classFullName);

                        if (DecorateCommonConstant.COMPONENTS_USETYPE_SYSTEM == componentsModel.getCompType()) {
                            // 此处转换的BaseCompModel对象拿不到父类属性的值,需要将compId等父类属性重新赋值
                            BaseCompModel compModel = ReflectUtil.getBean(hashMap, compClass);
                            compModel.setCompId(compIdStr);
                            compModel.setNeedAsyncInit(Boolean.valueOf(needAsyncInitStr));
                            model.getMapComps().put(compIdStr, compModel);
                        } else {
                            //说明是用户自定义的组件
                            UserDefinedModel compModel = new UserDefinedModel();
                            compModel.setCompId(compId);
                            compModel.setNeedAsyncInit(true);
                            compModel.setMapProps(hashMap);
                            model.getMapComps().put(compIdStr, compModel);
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error(e);
        }

        return model;
    }

    /**
     * 合并JS
     *
     * @param designJs
     * @param wpm
     * @param
     * @param
     * @return
     */
    private String executePageDesignJs(String compId, String designJs, WebPageModel wpm) {
        BaseCompModel bcm = wpm.getMapComps().get(compId);
        BaseCompController bcc = this.getCompController(bcm);
        designJs = bcc.genJs(designJs, wpm, bcm);
        return designJs;
    }

    /**
     * 合并页面
     *
     * @param pageViewHtml
     * @param wpm
     * @param alreadyExeComps
     * @param request
     * @return
     */
    private String executePageViewHtml(String pageViewHtml, WebPageModel wpm,
                                       List<String> alreadyExeComps, HttpServletRequest request) {

        for (String compUuid : wpm.getMapComps().keySet()) {
            if (alreadyExeComps.contains(compUuid)) {
                continue;
            }
             BaseCompModel bcm = wpm.getMapComps().get(compUuid);
            if (bcm.isNeedAsyncInit()) {
                alreadyExeComps.add(compUuid);
                continue;
            }
            BaseCompController bcc = this.getCompController(bcm);
            String retStr = bcc.executeCompViewHtml(pageViewHtml, wpm, bcm);
            if (!"false".equals(retStr.trim())) {
                pageViewHtml = retStr;
                alreadyExeComps.add(compUuid);
            }
        }
        if (wpm.getMapComps().size() != alreadyExeComps.size()) {
            pageViewHtml = this.executePageViewHtml(pageViewHtml, wpm,
                    alreadyExeComps, request);
        }

        return pageViewHtml;
    }

    /**
     * 获取BaseCompController对象
     *
     * @param bcm
     * @return
     */
    private BaseCompController getCompController(BaseCompModel bcm) {
        return (BaseCompController) bf.getBean(bcm.getCompControllerClass());
    }

    /**
     * 预览运行自定义的页面
     *
     * @param pageUuid
     * @param request
     * @param response
     */
    @RequestMapping("/run")
    public String runPage(@RequestParam String templateUuid,
                          @RequestParam String pageUuid, HttpServletRequest request,
                          HttpServletResponse response) throws Exception {
        String[] preKeyArr = {DecorateCacheConstant.DESIGNER_PAGEVEIWHTML, DecorateCacheConstant.DESIGNER_PAGEJS};
        //从缓存中获取当前页面使用的版本
        Dec_templates_sub subPagesModel = decTemplatesSubService.getCurrentUsingPageFromCache(pageUuid);
        if (subPagesModel == null) {
            return null;
        }
        String key = subPagesModel.getResourceKey();
        String[] pageResource = decTemplatesPagesService.getPageRedisResourceByResourceKey(key, preKeyArr);
        WebPageModel wpm = getPageModelByKey(key, request);

        //获取资源文件版本号
        String resourceVersion = decTemplatesResourceService.getTemplateResourceVersion(templateUuid);
        request.setAttribute("version", resourceVersion);
        List<String> alreadyExeComps = new ArrayList<String>();
        String str = this.executePageViewHtml(pageResource[0], wpm, alreadyExeComps, request);
        String pageJs = pageResource[1];
        StringBuffer pageStr =new StringBuffer(Strings.isNotBlank(str) ? str : "");
        pageStr.append("\r\n<script>" + pageJs + "</script>");
        request.setAttribute("templateUuid", templateUuid);
        request.setAttribute("pageViewHtml", pageStr);
        String templateType = decTemplatesManagerService.fetch(templateUuid).getVersionType();
        if (templateType.equals("1")) {
            return "pages/platform/dec/designer/RunPage";
        } else if (templateType.equals("2") || templateType.equals("3")) {
            return "pages/platform/dec/designer/RunPageMobile";
        }
        return "pages/platform/dec/designer/RunPage";
    }

    /**
     * 获取运行页面的WebPageModel对象
     *
     * @return
     */
    private WebPageModel getPageModelByKey(String key, HttpServletRequest request) throws Exception {
        WebPageModel wpm = decTemplatesPagesService.getPageModelByKey(key);

        // 获取可读的参数Map
        Map map = this.getParameterMap(request);
        wpm.setMapPageParams(map);

        return wpm;
    }


    /**
     * 从request中获得参数Map，并返回可读的Map
     *
     * @param request
     * @return
     */
    private Map getParameterMap(HttpServletRequest request) {
        // 参数Map
        Map properties = request.getParameterMap();
        // 返回值Map
        Map returnMap = new HashMap();
        Iterator entries = properties.entrySet().iterator();
        Map.Entry entry;
        String name = "";
        String value = "";
        while (entries.hasNext()) {
            entry = (Map.Entry) entries.next();
            name = (String) entry.getKey();
            Object valueObj = entry.getValue();
            if (null == valueObj) {
                value = "";
            } else if (valueObj instanceof String[]) {
                String[] values = (String[]) valueObj;
                for (int i = 0; i < values.length; i++) {
                    value = values[i] + ",";
                }
                value = value.substring(0, value.length() - 1);
            } else {
                value = valueObj.toString();
            }
            returnMap.put(name, value);
        }
        returnMap.put(DecorateCommonConstant.COMPONENT_REQUEST_COOKIES, request.getCookies());
        returnMap.put(DecorateCommonConstant.COMPONENT_REQUEST_CONTEXTPATH, request.getContextPath());
        return returnMap;
    }

    @RequestMapping("/getPageVersions")
    @SJson
    public List<Dec_templates_sub> getPageVersions(@RequestParam String pageUuid, HttpServletResponse response) {
        List<Dec_templates_sub> subPageList = decTemplatesSubService.getSubPagesByPageUuid(pageUuid);
        return subPageList;
    }

    /**
     * 切换页面版本
     *
     * @param templateUuid
     * @param pageUuid
     * @param subPageUuid
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/switchPageVersion")
    public String switchPageVersion(@RequestParam String templateUuid,
                                    @RequestParam String pageUuid, @RequestParam String subPageUuid,
                                    HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        decTemplatesSubService.switchPageVersion(pageUuid, subPageUuid);

        String str = "/platform/dec/templates/manager/toPageDefine?templateUuid="
                + templateUuid + "&pageUuid=" + pageUuid;
        return str;
    }

    /**
     * 删除子页面
     *
     * @param subPageUuid
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value = {"/deletePageVersion"}, method = {RequestMethod.POST})
    public String deletePageVersion(@RequestParam String subPageUuid,
                                    HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        decTemplatesSubService.delete(subPageUuid);

        response.getWriter().print("true");

        return null;
    }


    /**
     * 保存为新版本
     *
     * @param templateUuid
     * @param pageUuid
     * @param versionDescribe
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/saveAsNewVersion")
    public String saveAsNewVersion(@RequestParam String templateUuid,
                                   @RequestParam String pageUuid,
                                   @RequestParam String versionDescribe, HttpServletRequest request,
                                   HttpServletResponse response) throws Exception {
        decTemplatesSubService.saveAsNewVersion(templateUuid, pageUuid, versionDescribe);

        return "redirect:toPageDefine?templateUuid=" + templateUuid + "&pageUuid=" + pageUuid;
    }

    /**
     * 获取页面所有类型
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value = {"getPageTypes"}, method = {RequestMethod.GET})
    @SJson
    public Object getPageTypes(HttpServletRequest request,
                               HttpServletResponse response) throws Exception {
        List<Datatable> datatableList = new ArrayList<Datatable>();
        for (PageTypeEnum pt : PageTypeEnum.values()) {
            Datatable datatable = new Datatable();
            datatable.setName(pt.getKey() + "");
            datatable.setValue(pt.getValue());
            datatableList.add(datatable);
        }
        return datatableList;
    }

    /**
     * 进入模板管理页面
     *
     * @param uuid
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value = {"/toManage"}, method = {RequestMethod.GET})
    public String toManage(@RequestParam String uuid, Model model,
                           HttpServletRequest request) {
        model.addAttribute("templateUuid", uuid);

        return "decoration/templates/TemplateManage";
    }

    /**
     * 新增页面
     *
     * @param templateUuid
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/addNewPage")
    public String addNewPage(@RequestParam String templateUuid,
                             @RequestParam String pageName,
                             @RequestParam String pageFileName,
                             @RequestParam String pageType,
                             @RequestParam String description,
                             HttpServletRequest request,
                             HttpServletResponse response) throws Exception {
        Dec_templates_pages m = new Dec_templates_pages();
        m.setTemplateUuid(templateUuid);
        m.setPageName(pageName);
        m.setPageFileName(pageFileName);
        m.setPageType(Integer.parseInt(pageType));
        m.setDescription(description);

        Dec_templates_sub subPagesModel = decTemplatesPagesService.addPage(m);

        String str = "pages/platform/dec/designer/PageDefine?templateUuid="
                + templateUuid + "&pageUuid=" + subPagesModel.getPageUuid();
        return str;
    }

    /**
     * 套用模板
     */
    @RequestMapping("/setting/{id}")
    @RequiresPermissions("dec.templates.manager")
    public String setting(@PathVariable String id, HttpServletRequest req) {
        req.setAttribute("obj", decTemplatesManagerService.fetch(id));
        return "pages/platform/dec/templates/manager/setting";
    }


    /**
     * 弹出套用现有模板也main
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/adopt")
    public String adopt(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Dec_templates_manager m = new Dec_templates_manager();
        String templateImage = request.getParameter("templateImage");
        String templateZhName = request.getParameter("templateZhName");
        String templateUuid = request.getParameter("templateUuid");
        String templateEnName = request.getParameter("templateEnName");
        String description = request.getParameter("description");
        m.setTemplateZhName(templateZhName);
        m.setTemplateEnName(templateEnName);
        m.setDescription(description);
        m.setDisabled(false);
        m.setTemplateImage(templateImage);
        decTemplatesManagerService.adoptTemplate(templateUuid, m);

        return "redirect:toList";
    }


    /**
     * 进入源码编辑页面
     *
     * @param pageUuid
     * @param request
     * @return
     * @date 2017-05-12
     */
    @RequestMapping(value = {"/toPageDefineByCode"}, method = {RequestMethod.GET})
    public String toPageDefineByCode(@RequestParam("templateUuid") String templateUuid,
                                     @RequestParam("pageUuid") String pageUuid,
                                     HttpServletRequest request) {

        request.setAttribute("templateUuid", templateUuid);
        request.setAttribute("pageUuid", pageUuid);

        // 获取当前模板名称
        Dec_templates_manager templatesModel = decTemplatesManagerService.fetch(templateUuid);
        request.setAttribute("templateName", templatesModel.getTemplateZhName());

        // 获取当前模板下的所有页面
        List<Dec_templates_pages> pageList = decTemplatesPagesService.query(Cnd.where("templateUuid", "=", templateUuid));
        request.setAttribute("pageList", pageList);

        // 获取当前页面的版本号
        Dec_templates_sub subPagesModel = decTemplatesSubService.getResourceOfCurrentPage(pageUuid);
        request.setAttribute("pageCurrentVersion", subPagesModel.getVersionNo());

        String[] preKeyArr = {DecorateCacheConstant.DESIGNER_PAGEVEIWHTML, DecorateCacheConstant.DESIGNER_PAGEMODELJSON};
        String[] pageResource = decTemplatesPagesService.getPageRedisResourceByPageUuid(pageUuid, preKeyArr);
        if (pageResource != null && pageResource.length > 0) {
            request.setAttribute("pageViewHtml", pageResource[0]);
            request.setAttribute("pageModelJson", pageResource[1]);
        }

        //模板应用的终端类型
        String versionType = templatesModel.getVersionType();
        request.setAttribute("versionType", versionType);


        return "pages/platform/dec/designer/PageDefineByCode";
    }

}

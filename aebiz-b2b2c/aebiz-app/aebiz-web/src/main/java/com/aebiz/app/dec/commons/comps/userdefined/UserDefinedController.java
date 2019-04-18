//import_begin
package com.aebiz.app.dec.commons.comps.userdefined;

import com.aebiz.app.dec.commons.comps.userdefined.vo.UserDefinedModel;
import com.aebiz.app.dec.commons.service.PlatInfoForCompService;
import com.aebiz.app.dec.commons.vo.BaseCompModel;
import com.aebiz.app.dec.commons.vo.WebPageModel;
import com.aebiz.app.web.modules.controllers.open.dec.dto.platinfo.PlatImageCategoryDTO;
import com.aebiz.app.web.modules.controllers.platform.dec.BaseCompController;
import org.nutz.json.Json;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户自定义组件访问的控制器，通用的
 * 主要要使用的是genJs，复用父类的功能
 * @author cc
 */
@Controller
@RequestMapping("/userDefined")
public class UserDefinedController extends BaseCompController {
    @Autowired
    private PlatInfoForCompService platInfoForCompService;

    /**
     * 重写替换js
     * @param designJs
     * @param wpm
     * @param bcm
     * @return
     */
    @Override
    public String genJs(String designJs, WebPageModel wpm, BaseCompModel bcm){
        UserDefinedModel uc = (UserDefinedModel) bcm;
        String compId = uc.getCompId();

        designJs = designJs.replaceAll("\\$_compId",compId);
        String js = designJs.replaceAll("\\$_compConfig_\\$", Json.toJson(bcm));
        return js;
    }

    /**
     * 初始化属性编辑页面
     * @param request
     * @return
     */
    @RequestMapping(value="toParamsDesign")
    public String toParamsDesign(HttpServletResponse response, HttpServletRequest request){
        String toUrl = (String) request.getAttribute("toParamsJspURL");
        return toUrl;
    }

    /**
     * 获取广告位，图片分类
     * @param request
     * @return
     */
    @RequestMapping("/getImgLib")
    @ResponseBody
    public String getImgLib(HttpServletResponse response,HttpServletRequest request){
        Map<String, Object> map = new HashMap<>();
        List<PlatImageCategoryDTO> imageLibCategoryList = platInfoForCompService.getImageLibCategoryById();
        map.put("list", imageLibCategoryList);
        return Json.toJson(map);
    }


}

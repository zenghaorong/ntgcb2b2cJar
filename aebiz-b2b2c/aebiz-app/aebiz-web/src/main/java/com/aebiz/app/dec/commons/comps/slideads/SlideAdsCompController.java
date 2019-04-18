package com.aebiz.app.dec.commons.comps.slideads;

import com.aebiz.app.dec.commons.comps.slideads.vo.SlideAdsCompModel;
import com.aebiz.app.dec.commons.service.AdForCompService;
import com.aebiz.app.dec.commons.vo.BaseCompModel;
import com.aebiz.app.dec.commons.vo.WebPageModel;
import com.aebiz.app.web.modules.controllers.open.dec.dto.advertisement.AdDTO;
import com.aebiz.app.web.modules.controllers.platform.dec.BaseCompController;
import com.aebiz.baseframework.view.annotation.SJson;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.nutz.json.Json;
import org.nutz.lang.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 轮播广告组件controller
 *
 * Created by Aebiz_yjq on 2016/12/28.
 */
@Controller
@RequestMapping("/slideAds")
public class SlideAdsCompController extends BaseCompController {

    @Autowired
    AdForCompService adForCompService;

    /**
     * 页面初始化方法
     *
     * @param pageViewHtml 模板页面
     * @param wpm   页面model
     * @param sam   组件model
     * @return
     */
    public String executeCompViewHtml(String pageViewHtml, WebPageModel wpm, BaseCompModel sam){
        SlideAdsCompModel saModel = (SlideAdsCompModel)sam;
        String adUuid = saModel.getAdUuid();

        /*1、获取组件关联的广告*/
        AdDTO adDTO = adForCompService.getAdByUuid(adUuid);

        /*2、解析设计页面*/
        Document doc = Jsoup.parse(pageViewHtml);
        
        delCompLoading(doc, sam);

        /*3、初始化页面*/
        this.initPage(doc, adDTO);

        /*4、返回初始化之后的页面*/
        return doc.html();

    }

    /**
     * 后台配置页面数据初始化方法
     *
     * @param response 响应信息
     * @param request   请求信息
     * @return  返回包含广告列表
     */
    @RequestMapping("/toParamsDesign")
    public String toParamsDesign(HttpServletResponse response, HttpServletRequest request){

        /*1、获取并转换分页相关数据*/
        String pageShowStr = request.getParameter("pageShow");
        String nowPageStr = request.getParameter("nowPage");
        int pageShow;
        int nowPage;

        //获取每页显示数量，默认为9条
        if(Strings.isEmpty(pageShowStr)){
            pageShow = 9;
        }else{
            pageShow = Integer.parseInt(pageShowStr);
        }

        //获取当前页面，默认为第一页
        if(Strings.isEmpty(nowPageStr)){
            nowPage = 1;
        }else{
            nowPage = Integer.parseInt(nowPageStr);
        }

        /*2、通过接口获取平台广告列表及广告数量*/
        List<AdDTO> adList = adForCompService.getAdList(pageShow, nowPage);
        int totalNum = adForCompService.getAdCount();

        /*3、将获取到的信息返回给页面*/
        request.setAttribute("pageShow", pageShow);
        request.setAttribute("nowPage", nowPage);
        request.setAttribute("totalNum", totalNum);
        request.setAttribute("platAdMap", adList);
        return Strings.sNull( request.getAttribute("toParamsJspURL"));
    }

    /**
     * 替换JS中变量，及关联的广告uuid。
     *
     * @param designJs 原js文件
     * @param wpm 配置页面Model
     * @param bcm 基础组件Model
     * @return 返回替换之后的JS文件
     */
    public String genJs(String designJs, WebPageModel wpm, BaseCompModel bcm) {

//        SlideAdsCompModel hcm = (SlideAdsCompModel)bcm;
//        String compId = hcm.getCompId();
//        String adUuid = hcm.getAdUuid();
//
//        AdDTO adDto = adForCompService.getAdByUuid(adUuid);
//
//        designJs = designJs.replace("$_compId", compId);
//        designJs = designJs.replace("$_adUuid", adDto.getAdUuid());
//
//        designJs = designJs.replace("$_needAsyncInit", hcm.isNeedAsyncInit()+"");
        
    	String compId = bcm.getCompId();
		String js = designJs.replaceAll("\\$_compConfig_\\$", Json.toJson(bcm));
		js = js.replaceAll("\\$_compId", compId);
        
        return js;
    }

    /**
     * 异步加载返回页面数据
     * @return 以Json字符串的形式返回广告及图片的链接
     */
    @RequestMapping("/ajaxLoadData")
    @SJson
    public String ajaxLoadData(String adUuid){

        /*1、根据广告uuid获取广告信息*/
        AdDTO adDTO = adForCompService.getAdByUuid(adUuid);

        /*2、取得广告中的图片地址及广告的地址*/
        List adUrlList = adDTO.getAdUrlAndPicList();

        /*3、将取得的广告及图片地址转为Json格式并返回给前台*/
        return Json.toJson(adUrlList);
    }

    /**
     * 页面初始化方法
     *
     * @param doc 页面文件
     * @param adDTO 关联的广告
     */
    private void initPage(Document doc, AdDTO adDTO) {

        /*1、获取锚点所在ul,并创建节点的克隆*/
        Element fgUl = doc.select("#slideBox_fgUl").first();
        Element fgUlClone = fgUl.clone();
        Element fgLi = fgUlClone.child(0).clone();
        fgUlClone.children().remove();

        /*2、获取广告图片所在ul,并创建节点的克隆*/
        Element bgUl = doc.select("#slideBox_bgUl").first();
        Element bgUlClone = bgUl.clone();
        Element bgLi = bgUlClone.child(0).clone();
        bgUlClone.children().remove();

        /*3、生成广告图片节点*/
        List<String> adStrList = adDTO.getAdUrlAndPicList();
        /*链接数组，返回的链接是以 "图片地址;广告地址"的形式拼接的字符串，需要进行分割提取，分割后保存到该数组中*/
        String[] adUrlStrArray ;
        String adUrl = "";
        String picUrl = "";
        String urlStr;
        if(adStrList !=null && adStrList.size()>0){
            for(int i = 0; i<adStrList.size();i++){
                if(i!=0){
                    /*第一个锚点要处于选中中台，如果不是第一个节点需要去掉锚点的class属性。*/
                    fgLi.removeAttr("class");
                }
                urlStr = adStrList.get(i);
                fgUlClone.append(fgLi.outerHtml());

                /*分割并提取广告/图片链接，并保存到数组中*/
                adUrlStrArray = urlStr.split(";");

                /*如果广告没有配置链接的话字符串中只保存了图片地址，并没有广告地址，所以分割之后需要判断数组长度，
                    如果长度是2代表既有图片地址又有广告地址；如果是1代表只有图片地址没有广告地址。*/
                if(adUrlStrArray.length==2){
                    adUrl = adUrlStrArray[1];
                    picUrl = adUrlStrArray[0];
                }else if(adUrlStrArray.length==1){
                    picUrl = adUrlStrArray[0];
                }
                bgLi.child(0).attr("href", adUrl);
                bgLi.child(0).child(0).attr("src", picUrl);
                bgUlClone.append(bgLi.outerHtml());
            }
        }

        /*4、替换原有节点*/
        fgUl.replaceWith(fgUlClone);
        bgUl.replaceWith(bgUlClone);
    }

    /**
     * 异步加载返回页面数据
     * @return 以Json字符串的形式返回广告及图片的链接
     */
    @RequestMapping("/getPlatAdList")
    @SJson
    public  Map<String, Object> getPlatAdList(@RequestParam(value="nowPage",defaultValue="0")int nowPage, @RequestParam("pageShow")int pageShow , HttpServletRequest request){

        /*2、通过接口获取广告列表*/
        List<AdDTO> adDTOs = adForCompService.getAdList(pageShow, nowPage);

        /*3、通过接口获取广告数量*/
        int totalNum = adForCompService.getAdCount();

        /*4、将获取到的信息返回给页面*/
        Map<String, Object> returnMap = new HashMap<>();
        if(pageShow != 0){
            returnMap.put("totalPage", totalNum/pageShow);
        }else{
            returnMap.put("totalPage", 1);
        }
        returnMap.put("adList", adDTOs);
        return returnMap;
    }

}

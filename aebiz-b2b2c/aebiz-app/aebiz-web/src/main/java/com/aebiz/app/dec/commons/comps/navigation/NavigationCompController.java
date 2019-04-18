package com.aebiz.app.dec.commons.comps.navigation;

import com.aebiz.app.dec.commons.service.NavigationForCompService;
import com.aebiz.app.dec.commons.utils.DecorateCommonConstant;
import com.aebiz.app.dec.commons.comps.navigation.vo.NavigationCompModel;
import com.aebiz.app.dec.commons.comps.navigation.vo.NavigationModel;
import com.aebiz.app.dec.commons.vo.BaseCompModel;
import com.aebiz.app.dec.commons.vo.WebPageModel;
import com.aebiz.app.web.modules.controllers.open.dec.dto.navigation.NavigationDTO;
import com.aebiz.app.web.modules.controllers.platform.dec.BaseCompController;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.nutz.json.Json;
import org.nutz.lang.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/navigationComp")
public class NavigationCompController extends BaseCompController {

    private String contextPath="";
    @Autowired
    private NavigationForCompService navigationForCompService;
    @Override
    public String executeCompViewHtml(String pageViewHtml, WebPageModel wpm, BaseCompModel bcm) {

        //读取模板页面
        Document doc = Jsoup.parse(pageViewHtml);
        //获取组件模块element
        Element compEle = doc.getElementById(bcm.getCompId());
        NavigationCompModel ncm = (NavigationCompModel) bcm;
        Element ulEle = compEle.getElementById(ncm.getCompId()+"_channelUl");
        contextPath= (String) wpm.getMapPageParams().get(DecorateCommonConstant.COMPONENT_REQUEST_CONTEXTPATH);
        render(ulEle,ncm);
        super.delCompLoading(doc,ncm);
        return doc.html();
    }

    @RequestMapping("/ajaxLoadData")
    @ResponseBody
    public Map ajaxLoadData(@RequestParam("compId")String compId, HttpServletRequest request){
        Map initData = new HashMap();
        //调用接口获取数据
        List<NavigationDTO> channelList = navigationForCompService.getPublishedChannel();
//        List<ChannelModel> channelList = (List<ChannelModel>) data.get("channelList");
        List modelList = new ArrayList();
        for(NavigationDTO m:channelList) {
            NavigationModel navigationModel = new NavigationModel();
            navigationModel.setChannelName(m.getChannelName());
            navigationModel.setChannelUrl(m.getChannelUrl());
            navigationModel.setPageUuid(m.getPageUuid());
            modelList.add(navigationModel);
        }
        initData.put("data", modelList);
        return initData;
    }

    @Override
    public String genJs(String designJs, WebPageModel wpm, BaseCompModel bcm) {
        NavigationCompModel ccm = (NavigationCompModel) bcm;
        designJs = designJs.replaceAll("\\$_compConfig_\\$", Json.toJson(ccm));
        designJs = designJs.replaceAll("\\$_compId", ccm.getCompId());
        return designJs;
    }

    @RequestMapping("/toParamsDesign")
    @Override
    public String toParamsDesign(HttpServletResponse response, HttpServletRequest request) {
        String toUrl = (String) request.getAttribute("toParamsJspURL");
        return toUrl;
    }
    /**
     * 渲染导航栏
     * @param ulEle
     */
    public void render(Element ulEle, NavigationCompModel ncm){
//        List channelList = new ArrayList();
        //调用接口获取频道数据
        List<NavigationDTO> channelList = navigationForCompService.getPublishedChannel();
        Element modelEle = ulEle.select(".j_channel").first();
        if(channelList==null||channelList.size()<1){
            //删除循环模板
            modelEle.remove();
            return ;
        }
        int size = channelList.size();
        if(size>ncm.getShowNum()){
            size=ncm.getShowNum();
        }
        //渲染导航栏
        for(int i=0;i<size;++i){
            NavigationDTO channel = channelList.get(i);
            String channelUrl;
            if(Strings.isEmpty(channel.getChannelUrl())){
                channelUrl=ncm.getCustomUrl() + channel.getPageUuid();
            }else{
                channelUrl=channel.getChannelUrl();
            }
            Element newEle = modelEle.clone();
            Element aEle = newEle.getElementsByTag("a").first();
            aEle.attr("href", contextPath+channelUrl);
            aEle.html(Strings.isEmpty(channel.getChannelName()) ? "" : channel.getChannelName());
            ulEle.appendChild(newEle);
        }
        //删除循环模板
        modelEle.remove();
    }
}

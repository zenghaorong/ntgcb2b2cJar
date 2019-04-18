package com.aebiz.app.web.modules.controllers.open.H5;

import com.aebiz.app.acc.modules.models.Account_user;
import com.aebiz.app.cms.modules.models.Cms_article;
import com.aebiz.app.cms.modules.models.Cms_channel;
import com.aebiz.app.cms.modules.models.Cms_video;
import com.aebiz.app.cms.modules.services.CmsArticleService;
import com.aebiz.app.cms.modules.services.CmsChannelService;
import com.aebiz.app.cms.modules.services.CmsVideoService;
import com.aebiz.app.shop.modules.models.Shop_adv_main;
import com.aebiz.baseframework.base.Result;
import com.aebiz.baseframework.page.Pagination;
import com.aebiz.baseframework.view.annotation.SJson;
import com.aebiz.commons.utils.StringUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.nutz.dao.Cnd;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Auther: zenghaorong
 * @Date: 2019/3/23  20:54
 * @Description:
 */
@Controller
@RequestMapping("/open/h5/cms")
public class CmsVideoController {
    private static final Log log = Logs.get();

    @Autowired
    private CmsVideoService cmsVideoService;

    @Autowired
    private CmsChannelService cmsChannelService;

    @Autowired
    private CmsArticleService cmsArticleService;

    /**
     * 进入视频列表页
     */
    @RequestMapping("/video.html")
    public String index() {
        return "pages/front/h5/niantu/videoList";
    }

    /**
     * 获得视频列表
     * @param pageNumber 页码
     * @return
     */
    @RequestMapping("videoList.html")
    @SJson
    public Result getVideoList(Integer pageNumber){
        try {
            if(pageNumber == null){
                pageNumber = 0;
            }
            Cnd cnd = Cnd.NEW();
            cnd.and("delFlag", "=", 0 );
            cnd.desc("sort");

            Pagination pagination = cmsVideoService.listPage(pageNumber,15,cnd,"^(id|videoTitle|imageUrl|price)$");
            return Result.success("ok",pagination.getList());
        } catch (Exception e) {
            log.error("获取视频列表异常",e);
            return Result.error("fail");
        }
    }

    /**
     * 获得视频详情信息
     * @return
     */
    @RequestMapping("videoDetail.html")
    @SJson
    public Result getVideoDetail(String id){
        try {

            Cms_video cms_video = cmsVideoService.fetch(id);
//            cms_video.setVideoUrl("");
            return Result.success("ok",cms_video);
        } catch (Exception e) {
            log.error("获取视频详情异常",e);
            return Result.error("fail");
        }
    }


    /**
     * 获得 黏土教程 ，达人秀场 ，创意美术 列表
     * @param pageNumber 页码
     * @return
     */
    @RequestMapping("cmsListByType.html")
    @SJson
    public Result cmsListByType(Integer pageNumber,String typeName){
        try {
            //根据名称查询对应的栏目编号
            String str="";
            if("cyms".equals(typeName)){
                str = "创意美术";
            }
            if("drxc".equals(typeName)){
                str = "达人秀场";
            }
            if("ntjc".equals(typeName)){
                str = "黏土教程";
            }
            Cnd c = Cnd.NEW();
            c.and("name", "=", str );
            Cms_channel cms_channel = cmsChannelService.fetch(c);

            if(pageNumber == null){
                pageNumber = 0;
            }
            Cnd cnd = Cnd.NEW();
            cnd.and("delFlag", "=", 0 );
            cnd.and("channelId","=",cms_channel.getId());
            cnd.desc("location");

            Pagination pagination = cmsArticleService.listPage(pageNumber,15,cnd);
            return Result.success("ok",pagination.getList());
        } catch (Exception e) {
            log.error("获取图文列表异常",e);
            return Result.error("fail");
        }
    }


    /**
     * 获得 黏土教程 ，达人秀场 ，创意美术 详情信息
     * @return
     */
    @RequestMapping("cmsArticle.html")
    @SJson
    public Result cmsArticle(String id){
        try {
            Cms_article cms_article = cmsArticleService.fetch(id);
            return Result.success("ok",cms_article);
        } catch (Exception e) {
            log.error("获取图文详情异常",e);
            return Result.error("fail");
        }
    }


    /**
     * 进入视频购买详情页
     */
    @RequestMapping("videoBuyPage.html")
    public String videoBuyPage(String id, HttpServletRequest req){
        req.setAttribute("id",id);
        Subject subject = SecurityUtils.getSubject();
        Account_user accountUser = (Account_user) subject.getPrincipal();

        //判断是否购买 从而判断进入购买页还是详情页

        return "pages/front/h5/niantu/videoBuyPage";
    }

    /**
     * 进入视频详情页
     */
    @RequestMapping("goVideoDetail.html")
    public String goVideoDetail(String id, HttpServletRequest req){
        req.setAttribute("id",id);
        return "pages/front/h5/niantu/videoDetail";
    }

    /**
     * 获取视频购买详情页
     */
    @RequestMapping("getVideoDesc.html")
    @SJson
    public Result getVideoDesc(String id){
        try {
            Subject subject = SecurityUtils.getSubject();
            Account_user accountUser = (Account_user) subject.getPrincipal();

            Cms_video cms_video = new Cms_video();

            cms_video=cmsVideoService.getField("^(id|videoTitle|videoDetails|imageUrl|price|opAt)$",id);

            //这里做一个判断 如果当前账号有此视频订单就把url地址返回
            cms_video=cmsVideoService.getField("^(id|videoTitle|videoDetails|imageUrl|price|opAt|videoUrl)$",id);
            return Result.success("ok",cms_video);
        } catch (Exception e) {
            log.error("获取图文详情异常",e);
            return Result.error("fail");
        }
    }





}

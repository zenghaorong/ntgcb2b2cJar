package com.aebiz.app.web.modules.controllers.open.H5;

import com.aebiz.app.acc.modules.models.Account_user;
import com.aebiz.app.cms.modules.models.Cms_article;
import com.aebiz.app.cms.modules.models.Cms_review;
import com.aebiz.app.cms.modules.models.ReplyVO;
import com.aebiz.app.cms.modules.services.CmsReviewService;
import com.aebiz.baseframework.base.Result;
import com.aebiz.baseframework.view.annotation.SJson;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.nutz.dao.Cnd;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: zenghaorong
 * @Date: 2019/3/31  12:23
 * @Description:  社区模块
  */
@Controller
@RequestMapping("/open/h5/cms")
public class CmsH5ReviewController {

    private static final Log log = Logs.get();

    @Autowired
    private CmsReviewService cmsReviewService;


    /**
     * 获取当前内容下的评论和回复
     */
    @RequestMapping("getReview.html")
    @SJson
    public Result getReview(String cmsId){
        try {
            //查询评论
            Cnd cnd = Cnd.NEW();
            cnd.and("delFlag", "=", 0 );
            cnd.and("type", "=", "1" ); // 类型 1评论 2回复
            cnd.and("cmsId", "=", cmsId );
            List<Cms_review> list = cmsReviewService.query(cnd);
            List<Cms_review> cms_reviewList = new ArrayList<>();
            for (Cms_review cms_review : list) {
                //查看回复
                Cnd replyCnd = Cnd.NEW();
                replyCnd.and("delFlag", "=", 0 );
                replyCnd.and("type", "=", "2" ); // 类型 1评论 2回复
                replyCnd.and("cmsId", "=", cmsId );
                replyCnd.and("reviewId", "=", cms_review.getId());
                List<Cms_review> replyList = cmsReviewService.query(replyCnd);
                List<ReplyVO> replyVOList = new ArrayList<>();
                for (Cms_review r:replyList) {
                    ReplyVO replyVO = new ReplyVO();
                    replyVO.setContent(r.getContent());
                    replyVO.setReviewFatherName(r.getReviewFatherName());
                    replyVO.setReviewOpName(r.getReviewOpName());
                    replyVO.setReviewFatherId(r.getReviewFatherId());
                    replyVOList.add(replyVO);
                }
                cms_review.setReplyVOList(replyVOList);
                cms_reviewList.add(cms_review);
            }
            return Result.success("ok",cms_reviewList);
        } catch (Exception e) {
            log.error("获取评论回复信息异常",e);
            return Result.error("fail");
        }
    }


    /**
     * 评论接口
     */
    @RequestMapping("review.html")
    @SJson
    public Result review(String cmsId,String cmsTitle,String content){
        try {
            Subject subject = SecurityUtils.getSubject();
            Account_user accountUser = (Account_user) subject.getPrincipal();
            if(accountUser == null){
                return Result.error(2,"请先登录");
            }
            Cms_review cms_review = new Cms_review();
            cms_review.setCmsId(cmsId);
            cms_review.setIsStore("2");
            cms_review.setType("1");
            cms_review.setCmsTitle(cmsTitle);
            cms_review.setContent(content);
            cms_review.setReviewOpName(accountUser.getMobile());
            cms_review.setReviewOpId(accountUser.getId());
            cmsReviewService.insert(cms_review);
            return Result.success("ok");
        } catch (Exception e) {
            log.error("发布评论异常",e);
            return Result.error("fail");
        }
    }

    /**
     * 回复接口
     */
    @RequestMapping("reply.html")
    @SJson
    public Result reply(String cmsId,String cmsTitle,String content,
                        String reviewId,String reviewReplyId,String reviewFatherName,String reviewFatherId){
        try {
            Subject subject = SecurityUtils.getSubject();
            Account_user accountUser = (Account_user) subject.getPrincipal();
            if(accountUser == null){
                return Result.error(2,"请先登录");
            }
            Cms_review cms_review = new Cms_review();
            cms_review.setCmsId(cmsId);
            cms_review.setIsStore("2");
            cms_review.setType("2");
            cms_review.setCmsTitle(cmsTitle);
            cms_review.setContent(content);
            cms_review.setReviewOpName(accountUser.getMobile());
            cms_review.setReviewOpId(accountUser.getId());
            cms_review.setReviewId(reviewId);
            cms_review.setReviewFatherName(reviewFatherName);
            cms_review.setReviewFatherId(reviewFatherId);
            cms_review.setReviewReplyId(reviewReplyId);
            cmsReviewService.insert(cms_review);
            return Result.success("ok");
        } catch (Exception e) {
            log.error("发布评论异常",e);
            return Result.error("fail");
        }
    }
}

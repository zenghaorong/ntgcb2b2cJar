package com.aebiz.app.dec.commons.comps.productdetail.service.impl;


import com.aebiz.app.dec.commons.comps.productdetail.service.ProductDetailCompService;
import com.aebiz.app.dec.commons.comps.productdetail.vo.ProductDetailCommentModel;
import com.aebiz.app.dec.commons.comps.productdetail.vo.ProductDetailCompModel;
import com.aebiz.app.dec.commons.comps.productdetail.vo.ProductDetailInfoModel;
import com.aebiz.app.dec.commons.service.ProductForCompService;
import com.aebiz.app.dec.commons.utils.DecorateCommonConstant;
import com.aebiz.app.dec.commons.vo.BaseCompModel;
import com.aebiz.app.dec.commons.vo.WebPageModel;
import com.aebiz.app.web.modules.controllers.open.dec.dto.product.ProductAppraiseContentDTO;
import com.aebiz.app.web.modules.controllers.open.dec.dto.product.ProductAppraiseScoreDTO;
import com.aebiz.app.web.modules.controllers.open.dec.dto.product.ProductDescDTO;
import com.aebiz.baseframework.page.Pagination;
import com.aebiz.app.dec.commons.vo.Datatable;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.nutz.lang.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductDetailCompServiceImpl implements ProductDetailCompService {
	
	@Autowired
	ProductForCompService productForCompService;


	
	private String contextPath;
	

	@Override
	public ProductDetailInfoModel getProductDetailInfo(String sku, String pageShow) {
		ProductDetailInfoModel detainInfo = new ProductDetailInfoModel();
		ProductDescDTO productDesc = productForCompService.getProductDetail(sku);
		if(productDesc != null){
			detainInfo.setDesc(productDesc.getDescription());
			detainInfo.setAfterService( productDesc.getSaleAfterNote());
			detainInfo.setProdAttr(productDesc.getProdAttrs());
			detainInfo.setProductUuid(sku);
			//detainInfo.setSkuNo(productMainInfo.getSkuNo());
			//detainInfo.setSmallImg(productMainInfo.getSmallImageUrl());
			//detainInfo.setPrice(productMainInfo.getShopPrice()+"");
		}

		
		//获取商品的评价，评价数，已经评价率
		ProductDetailCommentModel comments = getProductComments(sku,pageShow);
		detainInfo.setComments(comments);
		return detainInfo;
	}
	
	
	
	/**
	 * 获取商品的评论
	 * @param sku
	 * @return
	 */
	private ProductDetailCommentModel getProductComments(String sku,String defaultPageShow){
		ProductDetailCommentModel comments = new ProductDetailCommentModel();
		Pagination commentsMap = getProductComment(sku, null, "1", defaultPageShow);
		
		List<ProductAppraiseContentDTO> list =(List<ProductAppraiseContentDTO>) commentsMap.getList();
	    comments.setList(list);
		comments.setPg((commentsMap));
		
		//获取各种评价的数量【有缓存】
		ProductAppraiseScoreDTO score = productForCompService.getAppraiseCount(sku);
		comments.setAllCount(score.getTotalCount());
		comments.setGoodCount(score.getgAppraiseCount());
		comments.setMiddleCount(score.getmAppraiseCount());
		comments.setBadCount(score.getbAppraiseCount());
		comments.setPicCount(score.getOrderShowCount());
		//计算好评率，中评率
		comments.computeRate();
		return comments;
		
	}
	

	/**
	 * type(0：全部评价，1：好评，2：中评，3:差评，4：晒图)，type为空，则返回 总评价，好评数。等。。
	 */
	@Override
	public Pagination getProductComment(String sku, String type, String nowPage, String pageShow) {
		Pagination map = productForCompService.getCommentsNew(sku,type,Integer.parseInt(nowPage),Integer.parseInt(pageShow));
		return map;
	}


	
	@Override
	public Document genCompHtml(String pageViewHtml, WebPageModel wpm, BaseCompModel bcm) {
		ProductDetailCompModel compModel = (ProductDetailCompModel) bcm;
		//同步运行期 商品id通过 url的参数获取
		String sku = (String) wpm.getMapPageParams().get("sku");

		if(Strings.isEmpty(sku)){
			//预览时 URL没有 商品ID,配置中给一个默认填写的的商品ID
			sku = compModel.getProductUuid();
		}
		
		Document doc = Jsoup.parse(pageViewHtml);
		
		contextPath = (String) wpm.getMapPageParams().get(DecorateCommonConstant.COMPONENT_REQUEST_CONTEXTPATH);
		
		ProductDetailInfoModel info = getProductDetailInfo(sku,compModel.getPageShow()+"");
		
		if(info != null){
			String compId = compModel.getCompId();
			doc.select("#"+compId+"_comsloading").remove();
			
			Element root = doc.select("#"+compId).first();
			
			//表头内购物车
			//root.select(".j_addtocart").select(".j_product-detail-img").attr("src",info.getSmallImg());
			//root.select(".j_addtocart").select(".j_smallPrice").html(info.getPrice());
			
			//生成商品详情
			genDetailHtml(doc, info,wpm,compModel);
			
			genProdAttrs(doc, info,wpm,compModel);
			//生成评价
			genCommentsHtml(doc, info,wpm,compModel);
			//生成售后服务
			genAfterServiceHtml(doc, info,wpm,compModel);
		}
		
		return doc;
	}
	
	
	private void genDetailHtml(Document doc, ProductDetailInfoModel info , WebPageModel wpm, ProductDetailCompModel compModel){
		String compId = compModel.getCompId();
		Element root = doc.select("#"+compId).first();
		root.select("#"+compId+"_detailimg").html(info.getDesc());
	}
	
	//生成规格与包装
	private void genProdAttrs(Document doc, ProductDetailInfoModel info , WebPageModel wpm, ProductDetailCompModel compModel){
		String compId = compModel.getCompId();
		if(compModel.isShowAttribute()){
			if(info.getProdAttr() !=null){
				doc.select("#"+compId+"_prodAttrs-content").html(info.getProdAttr());
			}else{
				doc.select("#"+compId+"_prodAttrs-content").select(".j_content").html("");
			}
			
		}else{
			//删除标签页
			doc.select("#"+compId+"_prodAttrs").remove();
			//删除标签页对应的内容
			doc.select("#"+compId+"_prodAttrs-content").remove();
		}
	}
	
	
	private void genCommentsHtml(Document doc, ProductDetailInfoModel info, WebPageModel wpm, ProductDetailCompModel compModel ){
		String compId = compModel.getCompId();
		ProductDetailCommentModel comments = info.getComments();
		//商品评价页签评价数
		doc.select("#"+compId+"_appCount").html("("+comments.getAllCount()+")");
	
		//好评率
		doc.select("#"+compId+"_goodAppraiseRate").html(comments.getGoodAppraiseRate()+"");
		//总评价人数
		doc.select("#"+compId+"_allAppCount").html(comments.getAllCount()+"");
		//好评率
		doc.select("#"+compId+"_goodAppraiseRate2").html(comments.getGoodAppraiseRate()+"%");
		doc.select("#"+compId+"_goodProgress").attr("style", "width:"+comments.getGoodAppraiseRate()+"%");
		
		//中评率
		doc.select("#"+compId+"_middleAppraiseRate").html(comments.getMiddleAppraiseRate()+"%");
		doc.select("#"+compId+"_middleProgress").attr("style", "width:"+comments.getMiddleAppraiseRate()+"%");
		//差评率
		doc.select("#"+compId+"_badAppraiseRate").html(comments.getBadAppraiseRate()+"%");
		doc.select("#"+compId+"_badProgress").attr("style", "width:"+comments.getBadAppraiseRate()+"%");
		
		//标签页各种评价数
		Element tab = doc.select("#"+compId+"_apptab").first();
		tab.select(".j_allAppCount").html(comments.getAllCount()+"");
		//设置类型，用于点击tab事件，重新渲染评价
		tab.select("#"+compId+"_allAppraise").attr("data-type","0");
		//设置总页数，用于分页组件初始化
		tab.select("#"+compId+"_allAppraise").attr("data-totalPage",comments.getPg().getTotalPage()+"");
		
		//好评页签
		tab.select(".j_goodAppCount").html(comments.getGoodCount()+"");
		tab.select("#"+compId+"_goodAppraise").attr("data-type","1");
		//中评页签
		tab.select(".j_middleAppCount").html(comments.getMiddleCount()+"");
		tab.select("#"+compId+"_normalAppraise").attr("data-type","2");
		//差评页签
		tab.select(".j_badAppCount").html(comments.getBadCount()+"");
		tab.select("#"+compId+"_lowAppraise").attr("data-type","3");
		//晒图页签
		tab.select(".j_picCount").html(comments.getPicCount()+"");
		tab.select("#"+compId+"_showpicAppraise").attr("data-type","4");
		
		//评论内容
		genCommentContent(doc, info, wpm, compModel);
	}

	//生成评论内容
	private void genCommentContent(Document doc, ProductDetailInfoModel info, WebPageModel wpm, ProductDetailCompModel compModel ){
		String compId = compModel.getCompId();
		Element divEle = doc.select("#"+compId+"_evaluall").first();
		//重新渲染此部分需要移除之前的节点（点击好评，中评，差评，需要重新渲染内容）
		divEle.select(".j_remove").remove();
		ProductDetailCommentModel comments = info.getComments();
		
		if(comments != null && comments.getList() != null && comments.getList().size() > 0){
			Element itemEle = divEle.select(".j_item.j_template").first();
			for(ProductAppraiseContentDTO comment :comments.getList()){
				Element tempItem = itemEle.clone();
				//模板本身隐藏，克隆的 展示出来
				tempItem.attr("style","display:block");
				//添加样式j_remove 以便于重新渲染时 删除这些节点
				tempItem.removeClass("j_template").addClass("j_remove");
				
				// 生成评论人信息
				//评论人头衔
				if(!Strings.isEmpty(comment.getCustomerImg())){
					
					if(comment.getCustomerImg().substring(0,4).equals("http")){
						tempItem.select(".j_customerImg").attr("src",comment.getCustomerImg());
					}else{
						tempItem.select(".j_customerImg").attr("src",contextPath+"/static/usercenter/img/default.png");
					}
				}else{
					tempItem.select(".j_customerImg").attr("src",contextPath+"/static/usercenter/img/default.png");
				}
				//会员名称
				tempItem.select(".j_customerName").html(comment.getCustomerName());
				tempItem.select(".j_appTime").html(comment.getAppraiseTime());
				//会员评分
				String score = (comment.getScore()/5)*100+"%";
				tempItem.select(".j_appScore").attr("style","width:"+score);
				//评论内容
				tempItem.select(".j_appContent").html(comment.getAppraiseContent());
				
				//购买的规格属性
				genSpecAttribute(tempItem, comment);
				
				//会员的晒单信息
				genCommentPic(comment,tempItem);
				
				divEle.append(tempItem.outerHtml());
				
			}
		}else{
			Element nothing = divEle.select(".j_nothing").first().clone().attr("style","display:block").removeClass("j_template").addClass("j_remove");
			divEle.append(nothing.outerHtml());
		}
	
	}
	//生成购买的规格属性1
	private void genSpecAttribute(Element tempItem, ProductAppraiseContentDTO comment){
		//会员购买的规格
		Element parent = tempItem.select(".j_attr").first();
		Element attrName = parent.select(".j_attrName").first();
		Element attrValue = parent.select(".j_attrValue").first();
		attrName.remove();
		attrValue.remove();
		List<Datatable> list =  comment.getSpec();
		if(!list.isEmpty()){
			for(Datatable attr: list){
				 genAppContentBuyAttr(attr,attrName,attrValue,parent);
			}
		}
	}
	
	//生成购买的规格属性2
	private void genAppContentBuyAttr(Datatable attr, Element attrName, Element attrValue, Element parent){
		Element tmpAttrName = attrName.clone();
		Element tmpAttrValue = attrValue.clone();
		tmpAttrName.html(attr.getName()+":");
		tmpAttrValue.html(attr.getValue().toString());
		parent.append(tmpAttrName.outerHtml());
		parent.append(tmpAttrValue.outerHtml());
	}
	
	//生成晒图
	private void genCommentPic(ProductAppraiseContentDTO comment,Element tempItem){
		Element wrap = tempItem.select(".j_pics").first();
		if(comment!=null && comment.getPicList()!=null && comment.getPicList().size() > 0){
			Element ulEle = wrap.select(".j_photos-thumb").first();
			Element liEle = ulEle.select(".j_photos-thumb-item").first();
			liEle.remove();
			for(String pic :comment.getPicList()){
				Element tmpLi = liEle.clone();
				tmpLi.attr("data-src", pic);
				tmpLi.select(".j_img").attr("src",pic);
				ulEle.append(tmpLi.outerHtml());
			}
		}else{
			wrap.remove();
		}
	}
	
	private void genAfterServiceHtml(Document doc, ProductDetailInfoModel info , WebPageModel wpm, ProductDetailCompModel compModel){
		String compId = compModel.getCompId();
		//售后服务
		doc.select("#"+compId+"_detailname").html(info.getAfterService());
	}

}

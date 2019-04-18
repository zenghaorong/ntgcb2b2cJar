package com.aebiz.app.dec.commons.comps.productmain;

import com.aebiz.app.dec.commons.comps.productmain.vo.ProductImgModel;
import com.aebiz.app.dec.commons.comps.productmain.vo.ProductMainCompModel;
import com.aebiz.app.dec.commons.comps.productmain.vo.ProductMainInfoModel;
import com.aebiz.app.dec.commons.service.ProductForCompService;
import com.aebiz.app.dec.commons.utils.CommonUtil;
import com.aebiz.app.dec.commons.utils.DecorateCommonConstant;
import com.aebiz.app.dec.commons.vo.BaseCompModel;
import com.aebiz.app.dec.commons.vo.WebPageModel;
import com.aebiz.app.web.modules.controllers.open.dec.dto.product.*;
import com.aebiz.app.web.modules.controllers.open.dec.dto.promotion.ProductGiftDTO;
import com.aebiz.app.web.modules.controllers.open.dec.dto.promotion.PromotionDetailDTO;
import com.aebiz.app.web.modules.controllers.platform.dec.BaseCompController;
import com.aebiz.commons.utils.MoneyUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.nutz.json.Json;
import org.nutz.lang.Lang;
import org.nutz.lang.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Controller
@RequestMapping("productmain")
public class ProductMainController extends BaseCompController {
	
	@Autowired
	ProductForCompService productForCompService;
	
	
	//===========================================生成页面HTML START========================================================//	
	
	public String executeCompViewHtml(String pageViewHtml, WebPageModel wpm, BaseCompModel bcm){
		ProductMainCompModel model = (ProductMainCompModel)bcm;
		Document doc = Jsoup.parse(pageViewHtml);
		
		delCompLoading(doc, bcm);
		
		//同步加载 在运行期 商品id通过 url的参数获取
		String sku = (String) wpm.getMapPageParams().get("sku");
		
		//预览时 URL没有 商品ID,配置中给一个默认填写的的商品ID
		if(Strings.isEmpty(sku)){
			sku = model.getProductUuid();
		}
		
		//如果没有取到默认配置，可能是没有点开属性配置页面
		if(!Strings.isEmpty(sku)){
			ProductMainInfoModel productMainInfo = null;
			if(wpm != null){
				//先从 内存中取，取不到再调用服务查询
				Object main =  wpm.getMapPageParams().get("productmain"+sku);
				if(main!=null){
					productMainInfo =  (ProductMainInfoModel)main;
				}else{
					//后台获取商品详情
					ProductDetailInteractiveDTO productInfo = getProductDetailInfo(sku);
					productMainInfo = convertoProductMainInfo(productInfo);
					wpm.getMapPageParams().put("productmain", productMainInfo);
				}
			}
			
		
			if(productMainInfo != null){
				
				//生成左侧图片预览
				doc = genLeftImgPreview(doc, wpm, bcm,productMainInfo);
				//生成右侧商品基本信息
				doc = genProductMain(doc, wpm, bcm,productMainInfo);
				
				//生成浏览足迹数据，保存到页面
				genViewRecord(productMainInfo, bcm.getCompId(), doc);
				
			}else{
				//配置了商品UUID，但是后台没有取到 该商品
				doc.select("#"+bcm.getCompId()).first().html("<p style='text-align: center;color: red'>商品无效</p>");
			}
		
		}else{
			//商品UUID 还是为空
			doc.select("#"+bcm.getCompId()).first().html("<p style='text-align: center;color: red'>请配置 预览参数[商品UUID]</p>");
		}
		
		
		
		
		return doc.html();
	}
	
	
	private void genViewRecord(ProductMainInfoModel productMainInfo,String compId,Document doc){
		Map<String,Object> viewRcord = new HashMap<String,Object>();
		viewRcord.put("productUuid", productMainInfo.getProductUuid());
		viewRcord.put("productName", productMainInfo.getProductName());
		viewRcord.put("bigImageUrl", productMainInfo.getBigImageUrl());
		viewRcord.put("shopPrice", productMainInfo.getShopPrice());
		viewRcord.put("saleCount", productMainInfo.getSaleCount());
		doc.select("#"+compId).first().attr("data-viewRecord", Json.toJson(viewRcord));
	}
	
	
	
	/**
	 * 生成左侧图片预览HTML
	 * @param doc
	 * @param wpm
	 * @param bcm
	 * @return
	 */
	private Document genLeftImgPreview(Document doc, WebPageModel wpm, BaseCompModel bcm, ProductMainInfoModel productMainInfo){
		ProductMainCompModel scm = (ProductMainCompModel)bcm;
		Element compEle = doc.select("#"+bcm.getCompId()).first();
		
		//第一张图片的 中图src和大图url
		String fsmall = productMainInfo.getSmallImageUrl();
		String fmid = productMainInfo.getCenterImageUrl();
		String fbig = productMainInfo.getBigImageUrl();
		
		//添加小图列表
		Elements ulEle = doc.select("#"+bcm.getCompId()+"_img-items ul");
		Element liEle = ulEle.select("li").first();
		liEle.remove();//移除模板
		
		//先添加主图
		Elements midImgEle = doc.select("#"+bcm.getCompId()+"_mid");
		midImgEle.attr("src",fmid);
		midImgEle.attr("data-big", fbig);
		Element firstLi = liEle.clone();
		firstLi.select("img")
				.attr("src",fsmall)
				.attr("data-mid-url", fmid)
				.attr("data-big-url", fbig);
		ulEle.append(firstLi.outerHtml());
		
		//添加 其他多视角小图列表
		List<ProductImgModel> imgList = productMainInfo.getImgList();
		if(imgList!=null && !imgList.isEmpty()){
			for(ProductImgModel img : imgList){
				Element tmpLi = liEle.clone();
				tmpLi.select("img")
						.attr("src",img.getSmallImageUrl())
						.attr("data-mid-url", img.getCenterImageUrl())
						.attr("data-big-url", img.getBigImageUrl());
				
				ulEle.append(tmpLi.outerHtml());
			}
		}
		
		return doc;
		
	}
	
	
	
	/**
	 * 生成右侧主信息
	 * @param doc
	 * @param wpm
	 * @param bcm
	 * @return
	 */
	private Document genProductMain(Document doc, WebPageModel wpm, BaseCompModel bcm, ProductMainInfoModel mainInfo){
		
		ProductMainCompModel scm = (ProductMainCompModel)bcm;
		if(!mainInfo.isSelfStore()){
			//不是自营，删除自营节点
			doc.select("#"+bcm.getCompId()+"_selfStore").remove();
		}
		
		doc.select("#"+bcm.getCompId()+"_productName").html(mainInfo.getProductName());
		doc.select("#"+bcm.getCompId()+"_note").html(mainInfo.getAdviceNote());
		
		//是否显示市场价
		if(scm.isShowMarketPrice()){
			System.out.println(mainInfo.getMarketPrice());
			System.out.println(Integer.parseInt(mainInfo.getMarketPrice()));
			System.out.println(MoneyUtil.fenToYuan(Integer.parseInt(mainInfo.getMarketPrice())));
			doc.select("#"+bcm.getCompId()+"_marketPriceValue").html(MoneyUtil.fenToYuan(Integer.parseInt(mainInfo.getMarketPrice())));
		}else{
			doc.select("#"+bcm.getCompId()+"_marketPrice").remove();
		}
		
		//商城价格
		doc.select("#"+bcm.getCompId()+"_shopPrice").html(MoneyUtil.fenToYuan(Integer.parseInt(mainInfo.getShopPrice())));
		
		//设置促销名称
		/*if(mainInfo.getPromotionName()!= null){
			doc.select("#"+bcm.getCompId()+"_promoname").html(mainInfo.getPromotionName());
		}else{
			doc.select("#"+bcm.getCompId()+"_promonameContainer").remove();
		}*/
		
		//购买积分
		if(mainInfo.getLeastIntegral() > 0){
			doc.select("#"+bcm.getCompId()+"_buyPoints").html(mainInfo.getLeastIntegral()+"");
		}else{
			doc.select("#"+bcm.getCompId()+"_buyPointsEl").remove();
		}
		
		//生成单品促销信息
		genProductPromotions(doc, wpm, scm, mainInfo);
		
		//商品评价
		if(scm.isShowAppraise()){
			doc.select("#"+bcm.getCompId()+"_appraise").attr("style", "width:"+mainInfo.getProductScore()*20 + "%");
			doc.select("#"+bcm.getCompId()+"_appCount").html(mainInfo.getAppCount()+"");
		}else{
			doc.select("#"+bcm.getCompId()+"_productAppCount").remove();
		}
		
	
		//生成库存
		genStock(doc, wpm, scm, mainInfo);
		
		//生成购买按钮和加入购物车
		genFastBuy(doc, wpm, scm, mainInfo);
				
		//生成选择规格属性
		genAttributeValues(doc, wpm, bcm, mainInfo);
		
		return doc; 
	}
	
	
	//生成单品促销信息
	private void genProductPromotions(Document doc, WebPageModel wpm, ProductMainCompModel scm, ProductMainInfoModel mainInfo){
		String compId = scm.getCompId();
		List<String> promotionNames=mainInfo.getPromotionName();
		if(!Lang.isEmpty(promotionNames)){
			Element parentContainer = doc.select("#"+compId+"_promotionTmp").first();
			Element promotionTmp = doc.select("#"+compId+"_promotionTmp").select(".j_tmp_promotion").first();
			promotionTmp.remove();
			Element onePromotionTmp  = promotionTmp.clone();
			for(int i=0;i<promotionNames.size();i++){
				onePromotionTmp.select(".promotionName").html(promotionNames.get(i));
				if(i!=0){
					onePromotionTmp.select(".promotionName").addClass("hideClass");
				}
				parentContainer.append(onePromotionTmp.outerHtml());
			}
			parentContainer.append("<a class='open'>展开促销</a>");
		}
	}
	
	
	//生成库存
	private void genStock(Document doc, WebPageModel wpm, ProductMainCompModel scm, ProductMainInfoModel mainInfo){
		String compId = scm.getCompId();
		doc.select("#"+compId +"_number").attr("data-stock",mainInfo.getStock()+"");
		//显示库存
		if(scm.isShowStock()){
			doc.select("#"+compId+"_stock").html(mainInfo.getStock()+"");
		}else{
			doc.select("#"+compId+"_stockNode").remove();
		}
		
		if(mainInfo.getStock() < 1){
			//无库存，置灰购买按钮和加入购物车 
			doc.select("#"+compId+"_fastBuy").attr("disabled","true");
			doc.select("#"+compId+"_addtocar").attr("disabled","true");
		}
	}
	
	//生成购买按钮和加入购物车
	private void genFastBuy(Document doc, WebPageModel wpm, ProductMainCompModel scm, ProductMainInfoModel mainInfo){
		//显示立即购买按钮
		if(scm.isShowBuy()){
			//交易商品状态，可购买状态才显示理解购买
		}else{
			doc.select("#"+scm.getCompId()+"_fastBuy").remove();
		}
	}
	
	
	
	//生成可选择规格属性信息
	private Document genAttributeValues(Document doc, WebPageModel wpm, BaseCompModel bcm, ProductMainInfoModel mainInfo){
		//先初始化 表单数据
		initFormData(doc, wpm, bcm, mainInfo);
		String compId = bcm.getCompId();
		Elements ulELe = doc.select("#"+compId+"_productAttributes");
		//普通规格属性 模板
		Element normalLiEle  = ulELe.select("li.j_normalAttr").first();
		//颜色规格属性 模板
		Element colorLiEle  = ulELe.select("li.j_colorAttr").first();
		normalLiEle.remove();//移除模板
		colorLiEle.remove();
		//循环列出 属性
		List<SelectedAttributeInValueJsonDTO> productAttributes = mainInfo.getProductAttributes();
		if(productAttributes!=null && !productAttributes.isEmpty()){
			for(SelectedAttributeInValueJsonDTO it : productAttributes){
				//普通规格属性
				if(it.getCanColor().equals(ChoosedAttributeInValueJson.NO)){
					Element tmpLi = normalLiEle.clone();//克隆模板，填充数据，然后追加到ul下
					Element attrValsDiv = tmpLi.select(".j_attributesValues").first();
					Element spanEle = tmpLi.select(".j_valuespan").first();
					spanEle.remove();
					//属性名称
					tmpLi.select(".j_attributeName").html("选择"+it.getAttributeName());
					//循环列出 属性可选值
					List<AttributeValueDTO>  values = it.getValues();
					for(AttributeValueDTO that : values){
						Element tmpSpan = spanEle.clone();
						//判断改属性值 是否 默认选择的，如果是 则给 加上active 样式
						if(isDefaultSelectValue( that.getValueUuid(), mainInfo)){
							tmpSpan.addClass("active");
							//父类加上已选择的uuid,以便事件处理
							tmpLi.attr("data-selectValueUuid", that.getValueUuid());
						}
						tmpSpan.select(".j_event_attributeValue").attr("data-valueUuid", that.getValueUuid()).html(that.getValue());
						attrValsDiv.append(tmpSpan.outerHtml());
					}
					ulELe.append(tmpLi.outerHtml());
				}else{
					//颜色的规格属性
					Element tmpLi = colorLiEle.clone();//克隆模板，填充数据，然后追加到ul下
					Element attrValsDiv = tmpLi.select(".j_attributesValues").first();
					Element spanEle = tmpLi.select(".j_valuespan").first();
					spanEle.remove();
					//颜色属性名称
					tmpLi.select(".j_attributeName").html("选择"+it.getAttributeName());
					List<AttributeValueDTO>  values = it.getValues();
					for(AttributeValueDTO that : values){
						Element tmpSpan = spanEle.clone();
						if(isDefaultSelectValue( that.getValueUuid(), mainInfo)){
							tmpSpan.addClass("active");
							//父类加上已选择的uuid,以便事件处理
							tmpLi.attr("data-selectValueUuid", that.getValueUuid());
						}
						//颜色属性与一般属性不同的 是 多了一个  图片
						if(false){
							tmpSpan.select(".j_color_img").attr("src","");
						}else{
							tmpSpan.select(".j_color_img").remove();
						}
						//设置点击事件所需要的 数据
						tmpSpan.select(".j_event_attributeValue").attr("data-valueUuid",that.getValueUuid());
						//颜色属性与一般属性不同的 是 多了一个  图片，模板结构稍微有些差别
						//设置 颜色名称
						tmpSpan.select(".j_attr_color").html(that.getValue());
						attrValsDiv.append(tmpSpan.outerHtml());
					}
					ulELe.append(tmpLi.outerHtml());
					
					
				};
			 }
		}
		return doc;
	}
	
	/**
	 * 初始化 表单数据
	 * @param doc
	 * @param wpm
	 * @param bcm
	 * @param mainInfo
	 */
	private void  initFormData(Document doc, WebPageModel wpm, BaseCompModel bcm, ProductMainInfoModel mainInfo){
		String compId = bcm.getCompId();
		
		Element form = doc.select("#"+compId+"_pForm").first();
		int specSize = 0;
		if(mainInfo.getInitSelectSpecUuids()!=null){
			specSize  =  mainInfo.getInitSelectSpecUuids().size();
		}
		form.select("#"+compId+"_skuNo").val(mainInfo.getSkuNo());
		form.select("#"+compId+"_specSize").val(specSize+"");
		if(specSize > 0){
			for(int i = 0 ;i < specSize ; i++){
				form.select("#"+compId+"_specUuid"+i).val(mainInfo.getInitSelectSpecUuids().get(i));
			}
		}
		form.select("#"+compId+"_productUuid").val(mainInfo.getProductUuid());
		form.select("#"+compId+"_productType").val(mainInfo.getProductType());
	}
	
	
	/**
	 * 判断某个属性值是否是 默认的规格属性
	 * @param valueUuid
	 * @param mainInfo
	 * @return
	 */
	private boolean isDefaultSelectValue(String valueUuid,ProductMainInfoModel mainInfo){
		List<String> initSpecUuids = mainInfo.getInitSelectSpecUuids();
		if(initSpecUuids !=null){
			return initSpecUuids.contains(valueUuid);
		}
		return false;
	}
	
	
	/**
	 * 获取商品详情
	 * @param sku
	 * @return
	 */
	private ProductDetailInteractiveDTO getProductDetailInfo(String sku){
		ProductDetailInteractiveDTO productInfo = productForCompService.getProductMain(sku);
		return productInfo;
	}
	
	
	/**
	 * 对象转换
	 * @param productInfo
	 * @return
	 */
	public ProductMainInfoModel convertoProductMainInfo(ProductDetailInteractiveDTO productInfo){
		ProductMainInfoModel model = new ProductMainInfoModel();
		if(productInfo != null){
			model.setProductName(productInfo.getProductName());
			model.setAdviceNote(productInfo.getAdviceNote());
			model.setAppCount(productInfo.getAppCount());
			model.setMarketPrice(productInfo.getMarketPrice()+"");
			model.setProductAttributes(productInfo.getSelectAttrValues());
			double score = productForCompService.getProductAvgScore(productInfo.getSkuNo());
			model.setProductScore(score);//需要计算
			model.setSelfStore(false);
			model.setShopPrice(productInfo.getShopPrice()+"");
			model.setSkuNo(productInfo.getSkuNo());
			model.setStock(productInfo.getStock());
			model.setSmallImageUrl(productInfo.getSmallImageUrl());
			model.setCenterImageUrl(productInfo.getCenterImageUrl());
			model.setBigImageUrl(productInfo.getBigImageUrl());
			model.setCanShowBuy("1");//目前所有商品都是1
			model.setProductType("01");//目前商品都是普通商品
			model.setProductUuid(productInfo.getProductUuid());
			model.setSaleCount(productInfo.getSaleCount());
			List<ProductImgModel>  imgs = new ArrayList<ProductImgModel>();
			for(ProductMainMultiImageDTO img : productInfo.getProductMultiImage()){
				ProductImgModel proImg = new ProductImgModel();
				proImg.setBigImage(img.getBasicImageUrl());
				proImg.setBigImageUrl(img.getBigImageUrl());
				proImg.setCenterImageUrl(img.getCenterImageUrl());
				proImg.setSmallImageUrl(img.getSmallImageUrl());
				imgs.add(proImg);
			}
			model.setImgList(imgs);
			
			//如果是规格商品
			if((productInfo.getSpec()=="0")){
				//设置默认规格属性
				String attrStock = productInfo.getAttrValueId();
				if(attrStock != null){
					String[] skus = attrStock.split("-");
					List<String> selectedSpec = Arrays.asList(skus);
					model.setInitSelectSpecUuids(selectedSpec);
				}
			}
			
			model.setAttrValueId(productInfo.getAttrValueId());
			model.setProductPromotions(productInfo.getProductPromotions());
			model.setStorePromotions(productInfo.getStorePromotions());
			model.setPromotionName(productInfo.getPromotionName());
			model.setLeastIntegral(productInfo.getLeastIntegral());
		}
		return model;
		
	}
	
	
 //===========================================生成页面HTML END========================================================//	
	
	
	
	
	/**
	 * 根据商品UUID获取商品主信息
	 * @param sku
	 * @return
	 */
	@ResponseBody
	@RequestMapping("getInitData")
	public ProductMainInfoModel getInitData(@RequestParam("sku")String sku){
		//后台获取商品详情
		ProductDetailInteractiveDTO productInfo = getProductDetailInfo(sku);
		ProductMainInfoModel productMainInfo = convertoProductMainInfo(productInfo);
		return productMainInfo;
	}
	
	
	/**
	 * 根据指定规格获取商品信息
	 * @param sku
	 * @param selectUuids
	 * @return
	 */
	@ResponseBody
	@RequestMapping("selectSpec")
	public SpecChooseDTO selectSpec(@RequestParam("sku")String sku, @RequestParam("selectUuids")String selectUuids){
		Map<String,Object> mapParams = new HashMap<String,Object>();
		SpecChooseDTO str = productForCompService.selectSpec(sku,selectUuids);
		return str;
	}
	
	
	@Override
	@RequestMapping("/toParamsDesign")
	public String toParamsDesign(HttpServletResponse response, HttpServletRequest request) {
		//做一些  进入组件配置页面前的 初始化操作
		//不需要操作则直接返回
		String toUrl = (String) request.getAttribute("toParamsJspURL");
		return toUrl;
	}
	
	
	@Override
	public String genJs(String designJs, WebPageModel wpm, BaseCompModel bcm) {
		String compId = bcm.getCompId();
		String js = designJs.replaceAll("\\$_compConfig_\\$", Json.toJson(bcm));
		js = js.replaceAll("\\$_compId", compId);
		return js;
	}



	
}

package com.aebiz.app.dec.commons.comps.storerelcategory;

import com.aebiz.app.dec.commons.comps.storerelcategory.vo.StoreRelCategoryCompModel;
import com.aebiz.app.dec.commons.service.ProductForCompService;
import com.aebiz.app.dec.commons.utils.DecorateCommonConstant;
import com.aebiz.app.dec.commons.vo.BaseCompModel;
import com.aebiz.app.dec.commons.vo.WebPageModel;
import com.aebiz.app.web.modules.controllers.open.dec.dto.product.ProductCategoryDTO;
import com.aebiz.app.web.modules.controllers.platform.dec.BaseCompController;
import org.apache.logging.log4j.util.Strings;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.nutz.json.Json;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("/storeRelCategoryComp")
public class StoreRelCategoryCompController extends BaseCompController {

    @Autowired
    public ProductForCompService productForCompService;
    
    @Override
    public String executeCompViewHtml(String pageViewHtml, WebPageModel wpm, BaseCompModel bcm) {
//    	System.out.println("storeRelCategoryComp同步");
        Document doc = Jsoup.parse(pageViewHtml);
        StoreRelCategoryCompModel compModel = (StoreRelCategoryCompModel) bcm;
        String contextPath = (String) wpm.getMapPageParams().get(DecorateCommonConstant.COMPONENT_REQUEST_CONTEXTPATH);
        
        //商品UUID来自上级页面
        String sku = (String)wpm.getMapPageParams().get("sku");
        
        //优先使用上级页面提供的，其次使用属性页面提供的
        if(Strings.isNotEmpty(sku)) {
        	//替换掉属性页面提供的
        	compModel.setProductUuid(sku);
        }
        
        //用compModel和contextPath填充doc
        renderBelow(doc, compModel, contextPath);
        
        super.delCompLoading(doc, compModel);
        return doc.html();
    }

    @RequestMapping("/toParamsDesign")
    @Override
    public String toParamsDesign(HttpServletResponse response, HttpServletRequest request) {
        String toUrl = (String) request.getAttribute("toParamsJspURL");
        
        return toUrl;

    }

    @Override
    public String genJs(String designJs, WebPageModel wpm, BaseCompModel bcm) {
        StoreRelCategoryCompModel compModel = (StoreRelCategoryCompModel) bcm;
        designJs = designJs.replaceAll("\\$_compConfig_\\$", Json.toJson(compModel));
        designJs = designJs.replaceAll("\\$_compId",compModel.getCompId());
        return designJs;
    }

    @RequestMapping(value="/ajaxLoadData")
	public void ajaxLoadData(HttpServletRequest request, HttpServletResponse response) {
    	String sku =request.getParameter("sku");
    	if(Strings.isBlank(sku)) {
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=UTF-8");
			try {
				response.getWriter().print("请提供商品UUID");
			} catch (Exception e) {
				e.printStackTrace();
			}
    		return;
    	}
    	
    	String compId = request.getParameter("compId");
    	
    	StringBuffer sb = new StringBuffer();
    	sb.append("<div class=\"m-comsloading\" id=\"" + compId + "_comsloading\"></div>");
    	sb.append("<div class=\"shop_relcategorey\">");
    	sb.append("<div class=\"shop_box_hd\">");
    	sb.append("<div class=\"relh3\">相关分类");
    	
    	String relcategory = request.getParameter("relcategory");
    	sb.append(relcategory);
    	sb.append("</div></div>");
    	sb.append("<div class=\"shop_box_bd\" id=\"" + compId + "_list\">");
    	
    	//取得商户
    	String storeUuid = productForCompService.getStoreUuidByProductUuid(sku);
    	
    	//查询商户某一分类对应的同级分类
    	List<ProductCategoryDTO> retList = productForCompService.getSameLevelProductCategorysByStoreUuid(storeUuid);
    	if(retList==null || retList.size()<=0) {
    		sb.append("相关分类不存在");
    		return;
    	}
    	
    	//循环填充
    	String contextPath = request.getParameter("contextPath");
    	String relUrl = request.getParameter("relUrl");
        for(ProductCategoryDTO dto : retList) {
			String href = contextPath + relUrl + "?storeUuid=" + storeUuid + "&categoryUuid=" + dto.getCategoryUuid();
			sb.append("<div class=\"all_cate_y\">");
			sb.append("<div class=\"first_cate_y\"><a  href=\"" + href + "\">"+dto.getCategoryName()+"</a></div>");
        	sb.append("<ul class=\"w_relcalist clearfix\">");
			List<ProductCategoryDTO> dtos= dto.getSubCategoryList();
			if(dtos !=null && dtos.size()>0){
				for(ProductCategoryDTO  dto2: dtos){
					String href2 = contextPath + relUrl + "?storeUuid=" + storeUuid + "&categoryUuid=" + dto2.getCategoryUuid();
					sb.append("<li><a class=\"a_href\" href=\"" + href2 + "\">");
					sb.append("<span class=\"span_html\">");
					sb.append(dto2.getCategoryName());
					sb.append("</span>");
					sb.append("</a></li>");
				}
			}
        	//String href = contextPath + relUrl + "?storeUuid=" + storeUuid + "&categoryUuid=" + dto.getCategoryUuid();
        	String src = "";
        	/*if(StringUtils.isNotBlank(dto.getCategoryPicUrl())) {
        		src = dto.getCategoryPicUrl();
        	}*/
        	//sb.append("<img class=\"img_src\" src=\"" + src + "\" width=\"24\" height=\"24\" />");
        	sb.append("</ul>");
			sb.append("</div>");
        }
        sb.append("");
    	sb.append("</div>"); //shop_box_bd
    	sb.append("</div>"); //shop_relcategorey

		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		try {
			response.getWriter().print(sb.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    private void renderBelow(Element doc, StoreRelCategoryCompModel compModel, String contextPath){
    	//替换标题
    	Element ele_h3 = doc.select(".shop_box_hd .relh3").first();
    	ele_h3.html("相关分类"+compModel.getRelcategory());
    	
    	//清空
    	Element listEle = doc.getElementById(compModel.getCompId() + "_list");
    	Element templateEle = listEle.select(".w_relcalist").first().clone();
    	listEle.children().remove();
    	
    	if(Strings.isBlank(compModel.getProductUuid())) {
    		listEle.append("请提供商品UUID");
    		return;
    	}
    	
    	//取得商户
    	String storeUuid = productForCompService.getStoreUuidByProductUuid(compModel.getProductUuid());
    	
    	//查询商户某一分类对应的同级分类
    	List<ProductCategoryDTO> retList = productForCompService.getSameLevelProductCategorysByStoreUuid(storeUuid);

    	if(retList==null || retList.size()<=0) {
    		listEle.append("相关分类不存在");
    		return;
    	}
        
        //循环填充
        for(ProductCategoryDTO dto : retList) {
        	Element ele = templateEle.clone();
        	
        	Element ele_a_href = ele.select(".a_href").first();
        	ele_a_href.attr("href", contextPath + compModel.getRelUrl() + "?storeUuid=" + storeUuid + "&categoryUuid=" + dto.getCategoryUuid());
        	
        	Element ele_span_html = ele.select(".span_html").first();
        	ele_span_html.html(dto.getCategoryName());
        	
        	/*if(StringUtils.isNotBlank(dto.getCategoryPicUrl())) {
        		Element ele_a_src = ele.select(".img_src").first();
        		ele_a_src.attr("src", dto.getCategoryPicUrl());
        	}*/
        	
        	listEle.appendChild(ele);
        }
        
    }

}

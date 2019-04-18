package com.aebiz.app.dec.commons.comps.productdetail;

import com.aebiz.app.dec.commons.comps.productdetail.service.ProductDetailCompService;
import com.aebiz.app.dec.commons.comps.productdetail.vo.ProductDetailCommentModel;
import com.aebiz.app.dec.commons.comps.productdetail.vo.ProductDetailInfoModel;
import com.aebiz.app.dec.commons.vo.BaseCompModel;
import com.aebiz.app.dec.commons.vo.WebPageModel;
import com.aebiz.app.web.modules.controllers.open.dec.dto.product.ProductAppraiseContentDTO;
import com.aebiz.app.web.modules.controllers.platform.dec.BaseCompController;
import com.aebiz.baseframework.page.Pagination;
import org.jsoup.nodes.Document;
import org.nutz.json.Json;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("productdetail")
public class ProductDetailCompController extends BaseCompController {

	@Autowired
	ProductDetailCompService productDetailCompService;
	
	@Override
	public String executeCompViewHtml(String pageViewHtml, WebPageModel wpm, BaseCompModel bcm) {
		Document doc = productDetailCompService.genCompHtml(pageViewHtml, wpm, bcm);
		return doc.html();
	}
	
	
	@Override
	@RequestMapping("/toParamsDesign")
	public String toParamsDesign(HttpServletResponse response, HttpServletRequest request) {
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
	
	
	
	/**
	 *通过懒加载方式获商品详情选项卡取初始化数据
	 * @param sku
	 * @param req
	 * @return
	 */
	@ResponseBody
	@RequestMapping("getInitData")
	public ProductDetailInfoModel ajaxLoadData(@RequestParam("sku")String sku,
											   @RequestParam("pageShow")String pageShow,
											   HttpServletRequest req){
		return productDetailCompService.getProductDetailInfo(sku,pageShow);
	}


	/**
	 * 分页获取商品评价
	 * @param sku 商品Uuid
	 * @param type 评价类型
	 * @param nowPage 当前页
	 * @param pageShow 每页条数
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value={"getComment"}, method={RequestMethod.GET})
	public ProductDetailCommentModel getComment(@RequestParam("sku")String sku,
												@RequestParam("type")String type,
												@RequestParam("nowPage")String nowPage,
												@RequestParam("pageShow")String pageShow ){
		ProductDetailCommentModel comments = new ProductDetailCommentModel();
		Pagination commentsMap =  productDetailCompService.getProductComment(sku, type, nowPage, pageShow);
		List<ProductAppraiseContentDTO> list = (List<ProductAppraiseContentDTO>)commentsMap.getList();
	    comments.setList(list);
		comments.setPg((commentsMap));
		return  comments;
	}
}

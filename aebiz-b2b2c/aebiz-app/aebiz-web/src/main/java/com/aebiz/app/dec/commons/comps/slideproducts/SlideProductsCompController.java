package com.aebiz.app.dec.commons.comps.slideproducts;

import com.aebiz.app.dec.commons.comps.producttab.vo.ProductTabDetailModel;
import com.aebiz.app.dec.commons.comps.slideproducts.vo.SlideProductsCompModel;
import com.aebiz.app.dec.commons.service.ProductForCompService;
import com.aebiz.app.dec.commons.utils.DecorateCommonConstant;
import com.aebiz.app.dec.commons.vo.BaseCompModel;
import com.aebiz.app.dec.commons.vo.WebPageModel;
import com.aebiz.app.web.modules.controllers.open.dec.dto.product.ProductCategoryDTO;
import com.aebiz.app.web.modules.controllers.open.dec.dto.product.ProductDetailDTO;
import com.aebiz.app.web.modules.controllers.platform.dec.BaseCompController;
import com.aebiz.baseframework.page.Pagination;
import com.aebiz.baseframework.view.annotation.SJson;
import com.aebiz.commons.utils.MoneyUtil;
import com.alibaba.fastjson.JSON;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.nutz.lang.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * 轮播商品组件controller
 *
 * Created by Aebiz_yjq on 2016/12/28.
 */
@Controller
@RequestMapping("/slideproducts")
public class SlideProductsCompController extends BaseCompController {

    @Autowired
	ProductForCompService productForCompService;
    
    private  String contextPath;

    /**
     * 页面初始化方法
     *
     * @param pageViewHtml 模板页面
     * @param wpm   页面model
     * @param sam   组件model
     * @return
     */
    public String executeCompViewHtml(String pageViewHtml, WebPageModel wpm, BaseCompModel sam){
        SlideProductsCompModel saModel = (SlideProductsCompModel)sam;
        Document doc = Jsoup.parse(pageViewHtml);
        delCompLoading(doc, sam);
        String productUuids = saModel.getProductUuids();
        contextPath = (String) wpm.getMapPageParams().get(DecorateCommonConstant.COMPONENT_REQUEST_CONTEXTPATH);
    	List<String> uuids = Arrays.asList(productUuids.split(","));
    	List<ProductTabDetailModel> products =  getProductDetailByUuids(uuids);
    	if(!products.isEmpty()){
    		Map<String,Element> template = initTemplate(doc, wpm, saModel);
    		genHtml(doc, wpm, saModel, products, template);
    	}
        return doc.html();

    }

    private Map<String,Element> initTemplate(Document doc, WebPageModel wpm, SlideProductsCompModel sam){
    	Map<String,Element>  tmp = new HashMap<String,Element>();
    	String compId = sam.getCompId();
    	//保存 详情模板，防止在嵌套循环中模板被删除掉
    	Element root = doc.select("#"+compId+"-products").first();
    	Element productDeail = root.select(".j_product-detail").first();
    	Element productTmplate = productDeail;
    	productDeail.remove();
    	
    	//商品组 模板，8个商品一组，产生轮播效果（8个商品在一个LI内）,轮播LI
    	Element productLi = root.select(".j_tab-product-page-group").first();
    	Element productLiTmplate = productLi;
    	productLi.remove();
    	tmp.put("productTmplate", productTmplate);
    	tmp.put("productLiTmplate", productLiTmplate);
    	
		return tmp;
    }
    
    //生成HTML
    private void genHtml(Document doc, WebPageModel wpm, SlideProductsCompModel sam, List<ProductTabDetailModel> products, Map<String,Element> template){
    	String compId = sam.getCompId();
		Element pagingContainerUl = doc.select("#"+compId+"-paging-container").first();
		Element productGroupLi = template.get("productLiTmplate");
		List<List<ProductTabDetailModel>> groups = this.groupProducts(products);
		for(int j = 0 ; j < groups.size() ; j++){
			//循环每个分组
			List<ProductTabDetailModel> groupProducts = groups.get(j);//每个分组内的商品
			//创建分组
			Element tmpGroupLi = productGroupLi.clone();
			//往分组内添加 商品数据
			genOneGroupHtml(groupProducts,tmpGroupLi,template,sam);
			//将新生成的li追加到Ul下
			pagingContainerUl.append(tmpGroupLi.outerHtml());
		}
    }
    
    //生成一组商品
    private void genOneGroupHtml(List<ProductTabDetailModel> products, Element tmpGroupLi, Map<String,Element> template, SlideProductsCompModel sam){
    	//商品模板
    	Element productDetail = template.get("productTmplate");
    	for(int i = 0 ,len = products.size() ; i < len ; i++){
    		ProductTabDetailModel product = products.get(i);
    		Element tmpProduct = productDetail.clone();
    		tmpProduct.select(".j_product-name").html(product.getProductName());
    		tmpProduct.select(".j_product-price").html(MoneyUtil.fenToYuan(Integer.parseInt(product.getPrice())) +"");
    		//================连接 待定===================//
    		//String link = contextPath + sam.getProductDetailUrl()+"&productUuid="+product.getProductUuid();
    		//String link = CommonUtil.getProductDetailUrl(contextPath, sam.getProductDetailUrl(), product.getProductUuid());
			String link=contextPath+sam.getProductDetailUrl()+"?sku="+product.getProductUuid();
    		tmpProduct.select("a.j_product-link").attr("href",link);
    		tmpProduct.select("a.j_product-collect").attr("data-id",product.getProductUuid());
    		tmpProduct.select(".j_product-photo").attr("src",product.getPic());
    		tmpGroupLi.append(tmpProduct.outerHtml());
    	}
    }
    
	
	//将 商品分组，8个商品为一组，获得一个 商品组 数组
	private List<List<ProductTabDetailModel>> groupProducts(List<ProductTabDetailModel> products){
		List<List<ProductTabDetailModel>> res = new ArrayList<List<ProductTabDetailModel>>();
		for(int i = 0 ,len = products.size() ; i < len ; i++){
			List<ProductTabDetailModel> arr = null;
			if((i+1) % 8 == 1){
				arr  = new ArrayList<ProductTabDetailModel>();
				res.add(arr);
			}else{
				arr = res.get(res.size()-1);
			}
			arr.add(products.get(i));
		}
		return res;
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
      
    	String toUrl = (String) request.getAttribute("toParamsJspURL");
		// 获取分类
	//	Map<String, Object> firstLevelCategory = productForCompService.getFirstLevelCategory();
		List<ProductCategoryDTO> firstLevelCategory2 = productForCompService.getFirstLevelCategory();
			request.setAttribute("prodList", firstLevelCategory2);
		
    	
        return toUrl;
    }
	@RequestMapping("/getPageData")
	@SJson
	public String getPageData(HttpServletRequest request) {


		String nowPage = request.getParameter("nowPage");
		String pageShow = request.getParameter("pageShow");
		String searchKey = request.getParameter("searchKey");
		String categoryUuid = request.getParameter("categoryUuid");
		String startPrice = request.getParameter("startPrice");
		String endPrice = request.getParameter("endPrice");
		String param_inProductUuids = request.getParameter("param_inProductUuids");

		// 获取所有商品列表
		Pagination scm = productForCompService.search("", searchKey, categoryUuid, Integer.parseInt(pageShow), Integer.parseInt(nowPage), "",
				"", startPrice, endPrice);
		request.setAttribute("param_inProductUuids",param_inProductUuids);
		String jsonString = JSON.toJSONString(scm);

		return jsonString;
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
    	String compId = bcm.getCompId();
		String js = designJs.replaceAll("\\$_compConfig_\\$", JSON.toJSONString(bcm));
		js = js.replaceAll("\\$_compId", compId);
        return js;
    }

    /**
     * 异步加载返回页面数据
     * @return 以Json字符串的形式返回广告及图片的链接
     */
    @RequestMapping(value="/ajaxLoadData", produces = "application/json; charset=utf-8")
    @ResponseBody
    public List<ProductTabDetailModel> ajaxLoadData(String productUuids,HttpServletRequest req){
    	
    	System.out.println("CCCccccc");
    	List<ProductTabDetailModel>  res = new ArrayList<ProductTabDetailModel>();
    	if(!Strings.isEmpty(productUuids)){
    		List<String> uuids = Arrays.asList(productUuids.split(","));
    		res =  getProductDetailByUuids(uuids);
    	}
    	return res;
      
    }


    private List<ProductTabDetailModel> getProductDetailByUuids(List<String> productUuids){
    	
    	long currentTimeMillis = System.currentTimeMillis();
    	List<ProductTabDetailModel>  res = new ArrayList<ProductTabDetailModel>();
    	
    	
    	
    	String[] arr = (String[])productUuids.toArray(new String[productUuids.size()]);
    	
    	
    	
    	
    	List<ProductDetailDTO> list =productForCompService.getBatchProductSample(arr);
    	
    	for(ProductDetailDTO dto:list){
    		
    		
			ProductTabDetailModel  product = new ProductTabDetailModel();
			product.setProductName(dto.getProductName());
			product.setProductUuid(dto.getProductUuid());
			product.setPrice((long)dto.getShopPrice()+"");
			product.setPic(dto.getCenterImageUrl());
    		
			res.add(product);

    	}
    	
    	
    	
    	/*for(String uuid:productUuids){
			ProductTabDetailModel  product = new ProductTabDetailModel();
			Map<String,Object>  mapParams = new HashMap<String,Object>();
			mapParams.put("productUuid", uuid);
			CompProductDetailDTO map = productForCompService.getProductDetailInfo(uuid);
			
			ProductDetailDTO mainModel = map.getProductDetailDTO();
			product.setProductName(mainModel.getProductName());
			product.setProductUuid(uuid);
			product.setPrice(mainModel.getShopPrice()+"");
			product.setPic(mainModel.getCenterImageUrl());
			res.add(product);
		}*/
    	
    	
    	System.out.println(System.currentTimeMillis()-currentTimeMillis+"ffffff");
    	return res;
    }
    
}

package com.aebiz.app.dec.commons.comps.reputation;

import com.aebiz.app.dec.commons.comps.reputation.vo.ReputationCompModel;
import com.aebiz.app.dec.commons.service.ProductForCompService;
import com.aebiz.app.dec.commons.service.StoreInfoForCompService;
import com.aebiz.app.dec.commons.vo.BaseCompModel;
import com.aebiz.app.dec.commons.vo.WebPageModel;
import com.aebiz.app.store.modules.models.Store_favorite;
import com.aebiz.app.store.modules.models.Store_main;
import com.aebiz.app.store.modules.services.StoreFavoriteService;
import com.aebiz.app.store.modules.services.StoreMainService;
import com.aebiz.app.web.modules.controllers.open.dec.dto.store.StoreInfoDTO;
import com.aebiz.app.web.modules.controllers.platform.dec.BaseCompController;
import com.aebiz.baseframework.view.annotation.SJson;
import com.aebiz.commons.utils.StringUtil;
import com.alibaba.dubbo.config.support.Parameter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.nutz.dao.Cnd;
import org.nutz.json.Json;
import org.nutz.lang.Lang;
import org.nutz.lang.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/reputationComp")
public class ReputationController extends BaseCompController {

	@Autowired
	private StoreInfoForCompService storeInfoForCompService;

	@Autowired
	private ProductForCompService productForCompService;
	@Autowired
	private StoreFavoriteService storeFavoriteService;
	@Autowired
	private StoreMainService storeMainService;

	@Override
	public String executeCompViewHtml(String pageViewHtml, WebPageModel wpm, BaseCompModel bcm) {

		Document doc = Jsoup.parse(pageViewHtml);
		this.delCompLoading(doc, bcm);


		Element compEle = doc.select("#" + bcm.getCompId()).first();
		
		String sku=(String)wpm.getMapPageParams().get("sku");
		
		if(!Strings.isEmpty(sku)){
		ReputationCompModel scm = getData(sku);

		//设置店铺logo
		compEle.select("#" + bcm.getCompId() + "_reputation_img").attr("src", scm.getShopUrl());

		//设置店铺跳转链接
		compEle.select("#" + bcm.getCompId() + "_reputation_img_a").attr("href",
				"/dfFront/toShopPage/" + scm.getShopUuid() + "/pc");
		compEle.select("#" + bcm.getCompId() + "_reputation_name").attr("href",
				"/dfFront/toShopPage/" + scm.getShopUuid() + "/pc");
		//店铺名称
		compEle.select("#" + bcm.getCompId() + "_reputation_name_span").first().text(scm.getShopName());
		
		//店铺uuid
		compEle.select("#" + bcm.getCompId() + "_reputation_i").attr("uuid", scm.getShopUuid());
		
		//店铺信誉得分
		compEle.select("#" + bcm.getCompId() + "_reputation_descritionScore").attr("style",
				"width:" + (scm.getProdDescScore() * 20) + "%");
			compEle.select("#" + bcm.getCompId() + "_reputation_serviceScore").attr("style",
					"width:" + (scm.getServiceAttitudeScore() * 20) + "%");
			compEle.select("#" + bcm.getCompId() + "_reputation_speedScore").attr("style",
					"width:" + (scm.getLogisticSpeedScore() * 20) + "%");
		
		}

		return doc.html();

	}


	@RequestMapping("/toParamsDesign")
	public String toParamsDesign(HttpServletResponse response, HttpServletRequest request) {
		String toUrl = (String) request.getAttribute("toParamsJspURL");
	
		return toUrl;

	}

	public String genJs(String designJs, WebPageModel wpm, BaseCompModel bcm) {
		ReputationCompModel sc = (ReputationCompModel) bcm;
	        String compId = sc.getCompId();
	     
	       designJs = designJs.replaceAll("\\$_compId", compId);

			designJs = designJs.replaceAll("\\$_compConfig_\\$", Json.toJson(sc));
	        
	        
	        return designJs;
	}

	@RequestMapping(value = "/ajaxLoadData", produces = "application/json; charset=utf-8")
	@ResponseBody
	public String ajaxLoadData(String compId, @RequestParam("sku") String sku, String lableName) {
		
			if(Strings.isEmpty(sku)){
				sku="50760337367003536301";
			}
		
		
		String jsonString="";
		if(!Strings.isEmpty(sku)){
			ReputationCompModel data = getData(sku);
			
			data.setLableName(lableName);

			jsonString = Json.toJson(data);
		}
		return jsonString;
	}

	/**
	 * 根据商品的uuid获取店铺的详细信息
	 * @param sku
	 * @return
	 */
	public ReputationCompModel getData(String sku) {
		
		
		
		long currentTimeMillis = System.currentTimeMillis();


		ReputationCompModel reputationCompModel = new ReputationCompModel();
		
		//=========start lsl 原方法 2017-2-13
	//	CompProductDetailDTO productDetailInfo = productForCompService.getProductDetailInfo(productUuid);
		
	//	String storeUuid = productDetailInfo.getProductDetailDTO().getStoreUuid();
		//=========start end 原方法 2017-2-13
		
		
		//=========start lsl  2017-2-13
		
		String storeUuid =productForCompService.getStoreUuidByProductUuid(sku);
		
		//=========end lsl  2017-2-13

		
		if (!Strings.isEmpty(storeUuid)) {


			StoreInfoDTO storeInfo = storeInfoForCompService.getStoreInfoForReputation(storeUuid);



			String name =storeInfo.getStoreName();

			Double storeScore = storeInfo.getStoreScore();

			DecimalFormat df = new DecimalFormat("######0.00");

			String uuid = storeInfo.getStoreUuid();

			String logo = storeInfo.getLogo();

			reputationCompModel.setShopUuid(uuid);
			if(!Strings.isEmpty(logo)){
				reputationCompModel.setShopUrl(logo);
			}else{
				reputationCompModel.setShopUrl("/assets/platform/images/logoa.png");
			}
			reputationCompModel.setServiceAttitudeScore(storeInfo.getServiceAttitudeScore());
			reputationCompModel.setLogisticSpeedScore(storeInfo.getLogisticSpeedScore());
			reputationCompModel.setProdDescScore(storeInfo.getProdDescScore());
			reputationCompModel.setScoreTotal(df.format(storeScore));

			reputationCompModel.setShopUuid(storeUuid);

			reputationCompModel.setShopName(name);

		}
		
		System.out.println(System.currentTimeMillis()-currentTimeMillis+"reputation");


		return reputationCompModel;
	}
	//收藏店铺
	@RequestMapping("/collectStore")
	@SJson
	public Map collectStore(@RequestParam("storeId") String storeId){
		//获取会员id
		Map map=new HashMap<>();
		String accountId= StringUtil.getMemberUid();
		Store_favorite storeFavorite= storeFavoriteService.fetch(Cnd.where("accountId","=",accountId).and("storeId","=",storeId));
		if(Lang.isEmpty(storeFavorite)){
			//说明该商铺未收藏
			Store_main storeMain=storeMainService.fetch(storeId);
			if(!Lang.isEmpty(storeMain)){
				storeFavoriteService.saveData(accountId,storeMain);
				map.put("state","0");
			}
			int count=storeFavoriteService.count(Cnd.where("accountId","=",accountId));
			map.put("count",count);
			return map;
		}else{
			int count=storeFavoriteService.count(Cnd.where("accountId","=",accountId));
			//说明该商铺已收藏
			map.put("state","1");
			map.put("count",count);
			return map;
		}
	}
}

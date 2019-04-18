package com.aebiz.app.dec.commons.comps.columnnavigation;

import com.aebiz.app.dec.commons.comps.columnnavigation.vo.ColumnNavigationCompModel;
import com.aebiz.app.dec.commons.comps.columnnavigation.vo.ColumnNavigationModel;
import com.aebiz.app.dec.commons.service.ContentForCompService;
import com.aebiz.app.dec.commons.vo.BaseCompModel;
import com.aebiz.app.dec.commons.vo.WebPageModel;
import com.aebiz.app.web.modules.controllers.open.dec.dto.content.ContentCategoryDTO;
import com.aebiz.app.web.modules.controllers.open.dec.dto.content.ContentDTO;
import com.aebiz.app.web.modules.controllers.platform.dec.BaseCompController;
import com.aebiz.baseframework.view.annotation.SJson;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.nutz.json.Json;
import org.nutz.lang.util.NutMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/columnNavigationComp")
public class ColumnNavigationController extends BaseCompController {

	@Autowired
	private ContentForCompService contentForCompService;

	@Override
	public String executeCompViewHtml(String pageViewHtml, WebPageModel wpm, BaseCompModel bcm) {

		Document doc = Jsoup.parse(pageViewHtml);
		
		this.delCompLoading(doc, bcm);


		Element compEle = doc.select("#" + bcm.getCompId()).first();
		
		//清空内容
		compEle.select("#help-breadcrumb").html(" ");
	

		Elements clonetEle = compEle.select("#" + bcm.getCompId() + "_columnnavigation_panel").clone();

		Elements parentEle = compEle.select("#" + bcm.getCompId() + "_columnnavigation_panelgroup");

		clonetEle.select("#" + bcm.getCompId() + "_columnnavigation_li").remove();

		// 删除模板节点
		compEle.select("#" + bcm.getCompId() + "_columnnavigation_panel").remove();

		ColumnNavigationCompModel scm = (ColumnNavigationCompModel) bcm;

		String categoryUuid = scm.getCategoryUuid();
		
		
		ColumnNavigationModel data = this.getData(categoryUuid);
		

		HashMap<ColumnNavigationModel, List<ColumnNavigationModel>> map = data.getMap();

		int i = 1;

		for (Map.Entry<ColumnNavigationModel, List<ColumnNavigationModel>> m : map.entrySet()) {

			// 改变id的值好用于ul append
			clonetEle.attr("id", bcm.getCompId() + "_columnnavigation_panel" + i);

			ColumnNavigationModel key = m.getKey();

			// 设置一级菜单

			// 1 菜单名称
			clonetEle.select(".columnnavigation_sencondtitle").first().text(key.getCategoryName());

			// 2 toggle

			clonetEle.select(".columnnavigation_span_toggle").attr("data-target", "#panel" + i);
			clonetEle.select(".columnnavigation_toggle").attr("id", "panel" + i);

			parentEle.append(clonetEle.outerHtml());

			List<ColumnNavigationModel> value = m.getValue();

			// 设置二级菜单

			Elements newEle = compEle.select("#" + bcm.getCompId() + "_columnnavigation_panel" + i);

			Elements secondParentEle = newEle.select(".columnnavigation_ul");

			Elements secondClonetEle = newEle.select(".columnnavigation_li").clone();

			setSecondEle(value, secondClonetEle, secondParentEle, bcm.getCompId(),i);

			i = i + 1;

		}

		return doc.html();

	}
	
	/**
	 * 设置二级菜单
	 * @param value
	 * @param secondClonetEle
	 * @param secondParentEle
	 * @param compId
	 * @param index
	 */
	private void setSecondEle(List<ColumnNavigationModel> value, Elements secondClonetEle,
                              Elements secondParentEle, String compId, int index) {

		if (value != null) {

			// 清空数据
			secondParentEle.html("");
			
			int index2=1;

			for (ColumnNavigationModel model : value) {

				secondClonetEle.select(".columnnavigation_thirdtitle").first().text(model.getCategoryName()+"");
				
				if(StringUtils.isNoneBlank(model.getContent())){
					secondClonetEle.select(".columnnavigation_thirdtitle").attr("content",model.getContent()+"");

				}
				
				
				
				secondClonetEle.select(".columnnavigation_thirdtitle").attr("id",compId+"columnnavigation_thirdtitle"+index+index2);
				
				secondClonetEle.select(".columnnavigation_thirdtitle").attr("uuid",model.getCategoryUuid());


				secondParentEle.append(secondClonetEle.outerHtml());
				index2=index2+1;

			}

		}

	}

	@RequestMapping("/toParamsDesign")
	public String toParamsDesign(HttpServletResponse response, HttpServletRequest request) {
		String toUrl = (String) request.getAttribute("toParamsJspURL");
			
		

		List<ColumnNavigationCompModel> list = new ArrayList<>();

		 List<ContentCategoryDTO> subCategoryByUuid = contentForCompService.getSubCategoryByUuid("");
		 
	
		 if(subCategoryByUuid!=null&&subCategoryByUuid.size()>0){
			 for(ContentCategoryDTO ca:subCategoryByUuid){
				 
					ColumnNavigationCompModel comp = new ColumnNavigationCompModel();
					
					
					
					comp.setCategoryUuid(ca.getCategoryUuid());
					comp.setCategoryName(ca.getCategoryName());
					list.add(comp);
				 
				 
				 
				 
			 }
			 
		 }
		 
	

		request.setAttribute("list", list);

		return toUrl;

	}

	public String genJs(String designJs, WebPageModel wpm, BaseCompModel bcm) {
		ColumnNavigationCompModel sc = (ColumnNavigationCompModel) bcm;
		String compId = sc.getCompId();
	
		
		designJs = designJs.replaceAll("\\$_compId", compId);

		designJs = designJs.replaceAll("\\$_compConfig_\\$", Json.toJson(sc));
		
		

		return designJs;
	}

	@RequestMapping("/ajaxLoadData")
	@SJson
	public List ajaxLoadData(String compid, String categoryUuid) {
		
		
		//JSONArray array= new JSONArray();
		List list=new ArrayList();
		Map<ContentCategoryDTO, List<ContentDTO>> contentCategoryByUuid = contentForCompService.getContentCategoryByUuid(categoryUuid);

		for (Map.Entry<ContentCategoryDTO, List<ContentDTO>> m : contentCategoryByUuid.entrySet()) {
			NutMap map=new NutMap();
			map.put("key", Json.fromJson(Json.toJson(m.getKey())));

			map.put("value", Json.fromJson(Json.toJson(m.getValue())));
			list.add(map);
		}

		return list;
	}
	
	/**
	 * 通过分类查询数据
	 * @param contentCateUuid
	 * @return
	 */
	@RequestMapping(value = "/getData")
	public ColumnNavigationModel getData(String contentCateUuid) {
		
	

		ColumnNavigationModel all = new ColumnNavigationModel();

		HashMap<ColumnNavigationModel, List<ColumnNavigationModel>> map = new HashMap<>();


		
		Map<ContentCategoryDTO, List<ContentDTO>> contentCategoryByUuid = contentForCompService.getContentCategoryByUuid(contentCateUuid);
		


				for (Map.Entry<ContentCategoryDTO, List<ContentDTO>> m : contentCategoryByUuid.entrySet()) {
					
					
					ContentCategoryDTO key = m.getKey();
					
					List<ContentDTO> value = m.getValue();

					ColumnNavigationModel keyModel = new ColumnNavigationModel();

					keyModel.setCategoryUuid(key.getCategoryUuid());
					keyModel.setCategoryName(key.getCategoryName());

					
					
					

					List<ColumnNavigationModel> list = new ArrayList<>();

					for (ContentDTO o2 : value) {


						ColumnNavigationModel valueModel = new ColumnNavigationModel();
						
						
						
						
						
						

						valueModel.setCategoryName(o2.getArticlTitle());

						valueModel.setCategoryUuid(o2.getArticlUuid());
						String introduction = o2.getArticlValue();
						
						
						
						if(introduction!=null&& StringUtils.isNoneBlank(introduction)){
							
							
							valueModel.setContent(introduction);
							
						}

						list.add(valueModel);

					}

					map.put(keyModel, list);

				}

			




		all.setMap(map);

		return all;
	}

}

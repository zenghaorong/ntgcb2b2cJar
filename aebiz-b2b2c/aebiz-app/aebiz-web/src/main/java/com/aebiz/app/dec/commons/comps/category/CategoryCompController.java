package com.aebiz.app.dec.commons.comps.category;

import com.aebiz.app.dec.commons.comps.category.vo.CategoryCompModel;
import com.aebiz.app.dec.commons.comps.category.vo.CategoryModel;
import com.aebiz.app.dec.commons.service.ProductForCompService;
import com.aebiz.app.dec.commons.vo.BaseCompModel;
import com.aebiz.app.dec.commons.vo.WebPageModel;
import com.aebiz.app.web.modules.controllers.open.dec.dto.product.ProductCategoryDTO;
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

/**
 * Created by 金辉 on 2016/12/15.
 */
@Controller
@RequestMapping("/categoryComp")
public class CategoryCompController extends BaseCompController {

    @Autowired
    public ProductForCompService productForCompService;
    @Override
    public String executeCompViewHtml(String pageViewHtml, WebPageModel wpm, BaseCompModel bcm) {
        Document doc = Jsoup.parse(pageViewHtml);
        String compId = bcm.getCompId();
        Element compEle = doc.getElementById(compId);
        CategoryCompModel ccm = (CategoryCompModel) bcm;
        //判断分类显示类型：固定、下拉
        if(ccm.getShowType()==ccm.SHOWTYPE_FIXED){
            Element dropEle = compEle.getElementById(ccm.getCompId()+"_dropDiv");
            dropEle.attr("style","display:block;");
        }
        //渲染分类
        renderBelow(doc, ccm);
        super.delCompLoading(doc,ccm);
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
        CategoryCompModel compModel = (CategoryCompModel) bcm;
        designJs = designJs.replaceAll("\\$_compConfig_\\$", Json.toJson(compModel));
        designJs = designJs.replaceAll("\\$_compId",compModel.getCompId());
        System.err.println(designJs);
        return designJs;
    }

    @RequestMapping("/ajaxLoadData")
    @ResponseBody
    public Map ajaxLoadData(@RequestParam("compId")String compId, HttpServletRequest request){
        Map initData = new HashMap();
        List<ProductCategoryDTO> categoryList = productForCompService.getFrontCategory();
        if(categoryList!=null&&categoryList.size()>0){
            initData.put("data",CastCategoryList(categoryList));
        }else{
            initData.put("data","无数据");
        }
        return initData;
    }
    private List CastCategoryList(List categoryList){
        List categoryModelList = new ArrayList();
        for(Object obj :categoryList){
            CategoryModel m = castToCategoryModel((ProductCategoryDTO) obj);
            if(m.getSubCategory()!=null&&m.getSubCategory().size()>0){
                m.setSubCategory(CastCategoryList(m.getSubCategory()));
            }
            categoryModelList.add(m);
        }
        return categoryModelList;
    }

    /**
     * 渲染下方的一级分类（根据判断是否显示二级）
     * @param doc
     * @param ccm
     */
    private void renderBelow(Element doc, CategoryCompModel ccm){
        //调用接口获取数据
        List<ProductCategoryDTO> categoryList= productForCompService.getFrontCategory();
        Element loopEle = doc.getElementById(ccm.getCompId() + "_loop");
        Element liEle = loopEle.select("li").first();
        int size = categoryList.size();
        //循环最多展示
        if(size>ccm.getFirstLevelNum()){
            size=ccm.getFirstLevelNum();
        }
        for(int i=0;i<size;++i) {
            ProductCategoryDTO pcm  = (ProductCategoryDTO) categoryList.get(i);
            CategoryModel categoryModel = castToCategoryModel(pcm);
            Element newLiEle = liEle.clone();
            //渲染一级分类
            renderFirst(newLiEle,ccm,categoryModel);
            //渲染二级分类
            renderBelowSecond(newLiEle, ccm,categoryModel.getSubCategory());
            //渲染右侧分类
            renderRight(newLiEle,categoryModel.getSubCategory());
            loopEle.appendChild(newLiEle);
        }
        liEle.remove();
    }

    /**
     * 渲染一级分类
     * @param newLiEle
     * @param ccm
     * @param categoryModel
     */
    private void renderFirst(Element newLiEle, CategoryCompModel ccm, CategoryModel categoryModel){
        //图片element
        Element imgEle = newLiEle.select(".j_catImg").first();
        //判断是否显示分类图片
        if (ccm.getIsShowImage() == ccm.IS_SHOW_IMAGE) {
            imgEle.attr("src", Strings.isEmpty(categoryModel.getImg())?"":categoryModel.getImg());
        } else {
            imgEle.parent().remove();
        }
        //渲染第一级分类
        Element firstCatEle = newLiEle.select(".j_firstCat").first();
        firstCatEle.attr("href", "/front/list?categoryUuid="+categoryModel.getCategoryUuid());
        firstCatEle.html(categoryModel.getCategoryName());
    }

    /**
     * 将map转换为category实体
     * @param data
     * @return
     */
    private CategoryModel castToCategoryModel(ProductCategoryDTO data){
        CategoryModel categoryModel = new CategoryModel();
        if(!Strings.isEmpty(data.getCategoryName())) {
            categoryModel.setCategoryName(data.getCategoryName());
        }
        categoryModel.setCategoryUuid(data.getCategoryUuid());
        categoryModel.setSubCategory(data.getSubCategoryList());
        if(!Strings.isEmpty(data.getCategoryUrl())) {
            categoryModel.setUrl(data.getCategoryUrl());
        }
      /*  if(!Strings.isEmpty(data.getCategoryPicUrl())) {
            categoryModel.setImg(data.getCategoryPicUrl());
        }*/
        return categoryModel;
    }

    /**
     * 循环渲染二级分类
     * @param newLiEle
     * @param ccm
     */
    private void renderBelowSecond(Element newLiEle, CategoryCompModel ccm, List subCategoryList){
        //循环渲染第二级分类
        Element secCatLoopEle = newLiEle.select(".j_secCatLoop").first();
        Element secCatEle = newLiEle.select(".j_secCat").first();
        //判断是否显示第二级分类
        if(ccm.getShowLevel()==ccm.SHOWLEVEL_ONE){
            secCatLoopEle.remove();
        }else if(ccm.getShowLevel()==ccm.SHOWLEVEL_TWO){
            int size = subCategoryList.size();
            if(size>ccm.getSecondLevelNum()){
                size=ccm.getSecondLevelNum();
            }
            for(int i=0;i<size;++i){
                CategoryModel categoryModel = castToCategoryModel((ProductCategoryDTO) subCategoryList.get(i));
                Element newSecEle = secCatEle.clone();
                newSecEle.attr("href","/front/list?categoryUuid="+categoryModel.getCategoryUuid());
                newSecEle.html(categoryModel.getCategoryName());
                secCatLoopEle.appendChild(newSecEle);
            }
        }
        secCatEle.remove();
    }
    /**
     * 渲染右侧二三级分类页面
     * @param newLiEle
     * @return
     */
    private void renderRight(Element newLiEle, List subCategoryList){
        Element rightLoopEle = newLiEle.select(".j_rightLoop").first();
        Element rightDlEle = newLiEle.select(".j_rightDl").first();
        int size = subCategoryList.size();
        for(int i=0;i<size;++i){
            CategoryModel categoryModel = castToCategoryModel((ProductCategoryDTO) subCategoryList.get(i));
            //克隆循环模板
            Element newDlEle = rightDlEle.clone();
            //右侧第二级分类标签
            Element rightSecEle = newDlEle.select(".j_rightSecCat").first();
            //右侧循环标签
            Element thdCatLoopEle = newDlEle.select(".j_rightThdCatLoop").first();
            //第三级循环模板标签
            Element thdCatEle = thdCatLoopEle.select(".j_rightThdCat").first();
            //设置属性
            rightSecEle.html(categoryModel.getCategoryName());
            rightSecEle.attr("href","/front/list?categoryUuid="+categoryModel.getCategoryUuid());
            List subList = categoryModel.getSubCategory();
            if(subList!=null&&subList.size()>0) {
                for (int j = 0; j < subList.size(); ++j) {
                    CategoryModel catModel = castToCategoryModel((ProductCategoryDTO) subList.get(j));
                    Element newThdEle = thdCatEle.clone();
                    newThdEle.attr("href", "/front/list?categoryUuid="+catModel.getCategoryUuid());
                    newThdEle.html(catModel.getCategoryName());
                    thdCatLoopEle.appendChild(newThdEle);
                }
            }
            rightLoopEle.appendChild(newDlEle);
            thdCatEle.remove();
        }
        //删除模板
        rightDlEle.remove();
    }
}

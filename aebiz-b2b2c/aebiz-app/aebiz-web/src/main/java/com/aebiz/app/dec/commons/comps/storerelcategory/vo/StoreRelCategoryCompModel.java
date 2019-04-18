package com.aebiz.app.dec.commons.comps.storerelcategory.vo;


import com.aebiz.app.dec.commons.comps.storerelcategory.StoreRelCategoryCompController;
import com.aebiz.app.dec.commons.vo.BaseCompModel;

public class StoreRelCategoryCompModel extends BaseCompModel {
    private static final long serialVersionUID = -1;

    private String compName = "店铺相关分类";
	private String compType = "StoreRelCategoryComp";
    
    /**
     * 相关分类，用户可以自定义名称
     */
    private String relcategory = "";
    /**
     * 商品UUID，属性配置页设置的话，仅用于预览
     * 实际使用的话，该值应该由上级页面提供，也就是说，谁调用我，要给我传值
     */
    private String productUuid = "";
    
    /**
     * 点击分类，链接到哪里
     * 后台会在其后追加storeUuid、categoryUuid
     */
    private String relUrl = "";

    public StoreRelCategoryCompModel() {
        super(StoreRelCategoryCompController.class, "/storeRelCategoryComp/toParamsDesign");
    }

	public String getRelcategory() {
		return relcategory;
	}

	public void setRelcategory(String relcategory) {
		this.relcategory = relcategory;
	}

	public String getProductUuid() {
		return productUuid;
	}

	public void setProductUuid(String productUuid) {
		this.productUuid = productUuid;
	}

	public String getCompName() {
		return compName;
	}

	public void setCompName(String compName) {
		this.compName = compName;
	}

	public String getCompType() {
		return compType;
	}

	public void setCompType(String compType) {
		this.compType = compType;
	}

	public String getRelUrl() {
		return relUrl;
	}

	public void setRelUrl(String relUrl) {
		this.relUrl = relUrl;
	}
	
}

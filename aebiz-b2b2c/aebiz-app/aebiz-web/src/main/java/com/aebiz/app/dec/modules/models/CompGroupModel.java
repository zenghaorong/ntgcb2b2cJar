package com.aebiz.app.dec.modules.models;
import java.util.ArrayList;
import java.util.List;

/**
 * 组件组合实体类 <br/>
 * 供前台可用组件列表使用
 * 
 * @author linq
 *
 */
public class CompGroupModel {
	/* 组件分类名称*/
	private String categoryName;
	
	/* 组件分类下对应的组件集合*/
	private List<Dec_component> components = new ArrayList<Dec_component>();

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public List<Dec_component> getComponents() {
		return components;
	}

	public void setComponents(List<Dec_component> components) {
		this.components = components;
	}

	@Override
	public String toString() {
		return "CompGroupModel [categoryName=" + categoryName + ", components="
				+ components + "]";
	}
}

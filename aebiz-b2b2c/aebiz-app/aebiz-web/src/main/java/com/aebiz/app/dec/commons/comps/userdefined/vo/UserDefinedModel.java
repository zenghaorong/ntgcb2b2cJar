package com.aebiz.app.dec.commons.comps.userdefined.vo;


import com.aebiz.app.dec.commons.vo.BaseCompModel;
import com.aebiz.app.dec.commons.utils.DecorateCommonConstant;
import com.aebiz.app.dec.commons.comps.userdefined.UserDefinedController;

import java.util.HashMap;
import java.util.Map;


/**
 * 用户自定义组件的通用属性模型
 * @author cc
 */
public class UserDefinedModel extends BaseCompModel {
	private static final long serialVersionUID = 4260838622018964123L;
	/**
	 * 组件的类型
	 */
	private int compType = DecorateCommonConstant.COMPONENTS_USETYPE_CUSTOM;
	/**
	 * 所有的属性都封装到这个Map里面，key就是属性名称，value是属性对应的值 
	 */
	private Map<String,Object> mapProps = new HashMap<String,Object>();
	/**
	 * 给父类传入参数：对应的控制器的类，转向参数设计页面的路径
	 */
	public UserDefinedModel() {
		super(UserDefinedController.class,"/userDefined/toParamsDefine");
	}
	public int getCompType() {
		return compType;
	}
	public void setCompType(int compType) {
		this.compType = compType;
	}
	public Map<String, Object> getMapProps() {
		return mapProps;
	}
	public void setMapProps(Map<String, Object> mapProps) {
		this.mapProps = mapProps;
	}
	
	
}

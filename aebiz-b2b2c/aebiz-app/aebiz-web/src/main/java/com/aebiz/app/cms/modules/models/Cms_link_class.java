package com.aebiz.app.cms.modules.models;

import java.io.Serializable;
import java.util.List;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.ColType;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.EL;
import org.nutz.dao.entity.annotation.ManyMany;
import org.nutz.dao.entity.annotation.Name;
import org.nutz.dao.entity.annotation.Prev;
import org.nutz.dao.entity.annotation.Table;
import com.aebiz.baseframework.base.model.BaseModel;

/**
 * Created by Wizzer on 2016/7/18.
 */
@Table("cms_link_class")
public class Cms_link_class extends BaseModel implements Serializable {
	private static final long serialVersionUID = 1L;
	@Column
	@Name
	@Comment("ID")
	@ColDefine(type = ColType.VARCHAR, width = 32)
	@Prev(els = { @EL("ig(view.tableName,'')") })
	private String id;

	@Column
	@Comment("分类名称")
	@ColDefine(type = ColType.VARCHAR, width = 120)
	private String name;

	@ManyMany(from = "classId", relation = "cms_class_link", to = "linkId")
	private List<Cms_link> links;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}

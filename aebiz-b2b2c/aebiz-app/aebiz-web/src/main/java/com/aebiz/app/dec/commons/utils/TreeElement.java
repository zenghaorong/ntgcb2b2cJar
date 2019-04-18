package com.aebiz.app.dec.commons.utils;

import java.util.List;

public class TreeElement {

	private String title = "";

	private String key = "";
	
	private String note = "";

	private List<TreeElement> children;

	private boolean expand = false;

	private boolean isFolder = true;

	private boolean isLazy = false;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public List<TreeElement> getChildren() {
		return children;
	}

	public void setChildren(List<TreeElement> children) {
		this.children = children;
	}

	public boolean getIsFolder() {
		return isFolder;
	}

	public void setIsFolder(boolean isFolder) {
		this.isFolder = isFolder;
	}

	public boolean isExpand() {
		return expand;
	}

	public void setExpand(boolean expand) {
		this.expand = expand;
	}

	public boolean getIsLazy() {
		return isLazy;
	}

	public void setIsLazy(boolean isLazy) {
		this.isLazy = isLazy;
	}
}

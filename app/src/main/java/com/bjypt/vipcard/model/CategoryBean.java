package com.bjypt.vipcard.model;

import java.io.Serializable;
import java.util.List;

/****
 * 分类用的数据
 */
public class CategoryBean implements Serializable{

private String twoName;
	private String oneName;
	private String twoSum;
	private String parentpk;
	private List<CategoryBean> list;

	public CategoryBean(String oneName) {
		this.oneName = oneName;
	}

	public CategoryBean(String twoName, String twoSum) {
		this.twoName = twoName;
		this.twoSum = twoSum;
	}

	public String getParentpk() {
		return parentpk;
	}

	public void setParentpk(String parentpk) {
		this.parentpk = parentpk;
	}

	public CategoryBean(List<CategoryBean> list, String oneName,String parentpk) {
		this.list = list;
		this.oneName = oneName;
		this.parentpk = parentpk;

	}

	public String getTwoName() {
		return twoName;
	}

	public void setTwoName(String twoName) {
		this.twoName = twoName;
	}

	public String getOneName() {
		return oneName;
	}

	public void setOneName(String oneName) {
		this.oneName = oneName;
	}

	public String getTwoSum() {
		return twoSum;
	}

	public void setTwoSum(String twoSum) {
		this.twoSum = twoSum;
	}

	public List<CategoryBean> getList() {
		return list;
	}

	public void setList(List<CategoryBean> list) {
		this.list = list;
	}
}

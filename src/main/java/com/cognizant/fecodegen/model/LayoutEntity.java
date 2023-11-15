/**
 * 
 */
package com.cognizant.fecodegen.model;

import java.math.BigInteger;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author 238209
 *
 */
@Document(collection="layout")
public class LayoutEntity {

	@Id
	private BigInteger id;
	
	private String layoutName;
	
	private String layout;
	
	private String type;
	
	private List<Params> params;

	/**
	 * @return the id
	 */
	public BigInteger getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(BigInteger id) {
		this.id = id;
	}

	/**
	 * @return the layoutName
	 */
	public String getLayoutName() {
		return layoutName;
	}

	/**
	 * @param layoutName the layoutName to set
	 */
	public void setLayoutName(String layoutName) {
		this.layoutName = layoutName;
	}

	/**
	 * @return the layout
	 */
	public String getLayout() {
		return layout;
	}

	/**
	 * @param layout the layout to set
	 */
	public void setLayout(String layout) {
		this.layout = layout;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the params
	 */
	public List<Params> getParams() {
		return params;
	}

	/**
	 * @param params the params to set
	 */
	public void setParams(List<Params> params) {
		this.params = params;
	} 
}

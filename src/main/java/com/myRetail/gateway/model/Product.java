/**
 * 
 */
package com.myRetail.gateway.model;

/**
 * @author
 *
 */
public class Product {

	private long id;

	private String name;

	private ProductPriceInfo current_price;

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the current_price
	 */
	public ProductPriceInfo getCurrent_price() {
		return current_price;
	}

	/**
	 * @param current_price the current_price to set
	 */
	public void setCurrent_price(ProductPriceInfo current_price) {
		this.current_price = current_price;
	}

}

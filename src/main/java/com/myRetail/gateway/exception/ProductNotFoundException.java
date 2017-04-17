/**
 * 
 */
package com.myRetail.gateway.exception;

/**
 * @author owner
 *
 */
public class ProductNotFoundException extends Exception {

	private static final long serialVersionUID = 1600114180760225210L;

	public ProductNotFoundException( String message ) {
		super( message );
	}

}

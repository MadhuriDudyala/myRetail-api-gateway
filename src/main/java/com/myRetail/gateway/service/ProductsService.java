/**
 * 
 */
package com.myRetail.gateway.service;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Future;

import com.myRetail.gateway.model.Product;
import com.myRetail.gateway.model.ProductPriceInfo;

/**
 * @author owner
 *
 */
public interface ProductsService {
	
	public Future<Optional<Product>> getProductDetails( Long id );
	
	public Future<ProductPriceInfo> getProductPrice( Long id );
	
	public ProductPriceInfo updateProductInfo( Long id,  Map<String, Object> productMap );

}

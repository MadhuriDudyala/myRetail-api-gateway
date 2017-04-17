/**
 * 
 */
package com.myRetail.gateway.controller;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.myRetail.gateway.constants.ErrorConstants;
import com.myRetail.gateway.exception.ProductNotFoundException;
import com.myRetail.gateway.model.Product;
import com.myRetail.gateway.model.ProductPriceInfo;
import com.myRetail.gateway.service.ProductsService;

/**
 * @author
 *
 */
@RestController
public class ProductsController {

	public static final Logger logger = LoggerFactory.getLogger(ProductsController.class);

	@Autowired
	private ProductsService productService;

	@RequestMapping(value = "/products/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getProductInfo( @PathVariable("id") long id ) throws ProductNotFoundException, InterruptedException, ExecutionException {

		logger.info(" fetching the product information with id ", id );
		
		Future<Optional<Product>> futureProduct = productService.getProductDetails( id );
		Future<ProductPriceInfo> futureProductPriceInfo = productService.getProductPrice( id );

		Optional<Product> optionalProduct = futureProduct.get();

		if ( !optionalProduct.isPresent() ) {
			throw new ProductNotFoundException( ErrorConstants.PRODUCT_NOT_FOUND );
		}

		Product product = optionalProduct.get();
		ProductPriceInfo productPriceInfo = futureProductPriceInfo.get();

		product.setId(id);
		product.setCurrent_price(productPriceInfo);

		return new ResponseEntity<Product>(product, HttpStatus.OK);
	}
	
	@ExceptionHandler(value = ProductNotFoundException.class)
	public ResponseEntity<Object> handleProductNotFoundException( ProductNotFoundException productNotFoundException ) {
		return new ResponseEntity<Object>( productNotFoundException.getMessage(), HttpStatus.NOT_FOUND);
	}
	
	@RequestMapping(value="/products/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateProductInfo(@PathVariable("id") long id, @RequestBody Map<String, Object> productMap) { 		
		ProductPriceInfo productPriceInfo = productService.updateProductInfo(id, productMap);
		return new ResponseEntity<ProductPriceInfo>(productPriceInfo, HttpStatus.OK);
	}

	@ExceptionHandler(Throwable.class)
	public ResponseEntity<Object> handleThrowable(final Throwable ex) {
		ex.printStackTrace();
		return new ResponseEntity<Object>("Unable to process request.", HttpStatus.INTERNAL_SERVER_ERROR);
	}
}

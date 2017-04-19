/**
 * 
 */
package com.myRetail.gateway.service.impl;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.myRetail.gateway.model.Product;
import com.myRetail.gateway.model.ProductPriceInfo;
import com.myRetail.gateway.service.ProductsService;

/**
 * @author owner
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class ProductsServiceImplTest {

	@InjectMocks
	private ProductsService productsService = new ProductsServiceImpl();
	
	@Mock
	private RestTemplate restTemplate;
	
	@Test
	public void testGetProductDetails() throws InterruptedException, ExecutionException
	{
		Future<Optional<Product>> future = productsService.getProductDetails(12L);
		Optional<Product> op = future.get();
		Product product = op.get();
		Assert.assertEquals("The Big Lebowski (Blue-ray) (Widescreen)", product.getName());	
	}
	
	@Test
	public void testGetProductPrice() throws InterruptedException, ExecutionException
	{
		ProductPriceInfo productPriceInfo = new ProductPriceInfo();
		productPriceInfo.setValue(13.49);
		Mockito.when(restTemplate.getForObject(anyString(), any())).thenReturn(productPriceInfo);
		
		Future<ProductPriceInfo> fpp = productsService.getProductPrice(12L);
		ProductPriceInfo fp = fpp.get();
		Assert.assertEquals(new Double( 13.49 ), new Double ( fp.getValue() ));
	}
	
	@Test
	public void testUpdateProductInfo()
	{
		Map<String, Object> hm = new HashMap<String, Object>();
		HttpHeaders requestHeaders = new HttpHeaders();
		HttpEntity<?> requestEntity = new HttpEntity<Object>(hm, requestHeaders);		
		ProductPriceInfo productPriceInfo = new ProductPriceInfo();
		productPriceInfo.setValue(13.49);
		ResponseEntity<ProductPriceInfo> rp = new ResponseEntity<ProductPriceInfo>(productPriceInfo, HttpStatus.OK); 
		Mockito.when(restTemplate.exchange("http://localhost:9001/price/{id}", HttpMethod.PUT, requestEntity, ProductPriceInfo.class, 12L)).thenReturn(rp);		
		Assert.assertEquals(new Double( 13.49 ), new Double ( rp.getBody().getValue() ));		
	}
}

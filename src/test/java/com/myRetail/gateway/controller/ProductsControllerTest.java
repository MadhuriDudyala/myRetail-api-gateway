/**
 * 
 */
package com.myRetail.gateway.controller;

import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyMap;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;
import java.util.concurrent.Future;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myRetail.gateway.model.Product;
import com.myRetail.gateway.model.ProductPriceInfo;
import com.myRetail.gateway.service.ProductsService;

/**
 * @author owner
 *
 */
@RunWith(SpringRunner.class)
@WebMvcTest(ProductsController.class)
public class ProductsControllerTest {

    @Autowired
    private MockMvc mvc;
    
	@MockBean
	private ProductsService productService;
	
	@MockBean
	private RestTemplate restTemplate;
    
    @Mock
    private Future<Optional<Product>> futureProduct;

    @Mock
    private Future<ProductPriceInfo> futureProductPriceInfo;
    
	@Autowired 
	private ObjectMapper mapper;

	@Test
	public void testGetProductInfo() throws Exception
	{	
		Product p = new Product();
		p.setId(12L);
		
		Optional<Product> optionalProduct = Optional.ofNullable(p);		
		ProductPriceInfo productPriceInfo = new ProductPriceInfo();		
		
		when(this.productService.getProductDetails(1L)).thenReturn(futureProduct);
		when(this.productService.getProductPrice(1L)).thenReturn(futureProductPriceInfo);
				
		when(futureProduct.get()).thenReturn(optionalProduct);
		when(futureProductPriceInfo.get()).thenReturn(productPriceInfo);
		
		this.mvc.perform(get("/products/1").accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk());
	}

	
	@SuppressWarnings("unchecked")
	@Test
	public void testUpdateProductInfo() throws Exception
	{
		ProductPriceInfo productPriceInfo = new ProductPriceInfo(); 
		when(this.productService.updateProductInfo(anyLong(), anyMap())).thenReturn(productPriceInfo);
		
		String json = mapper.writeValueAsString(productPriceInfo);
		
		this.mvc.perform(put("/products/12")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}
	
}

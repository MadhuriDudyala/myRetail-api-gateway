package com.myRetail.gateway.service.impl;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import com.myRetail.gateway.model.Product;
import com.myRetail.gateway.model.ProductPriceInfo;
import com.myRetail.gateway.service.ProductsService;

@Service
public class ProductsServiceImpl implements ProductsService {

	@Autowired
	private RestTemplate restTemplate;

	private MockRestServiceServer mockServer;

	@Override
	@Async
	public Future<Optional<Product>> getProductDetails(Long id) {

		String responseBody = "{\"name\" : \"The Big Lebowski (Blue-ray) (Widescreen)\"}";

		RestTemplate template = new RestTemplate();
		mockServer = MockRestServiceServer.createServer(template);

		String targetUrl = "http://redsky.target.com/v2/pdp/tcin/" + id + "?excludes=taxonomy,price,promotion,bulk_ship,rating_and_review_reviews,rating_and_review_statistics,question_answer_statistics";

		mockServer.expect(requestTo( targetUrl ))
		.andExpect(method(HttpMethod.GET)).andRespond(withSuccess( responseBody , MediaType.APPLICATION_JSON));

		Product product = template.getForObject(targetUrl, Product.class, id);

		Optional<Product> result = Optional.ofNullable(product);
		return new AsyncResult<>(result);
	}

	@Override
	@Async
	public Future<ProductPriceInfo> getProductPrice(Long id) {		
		ProductPriceInfo productPriceInfo = restTemplate.getForObject("http://localhost:9001/price/" + id , ProductPriceInfo.class);		
		return new AsyncResult<>( productPriceInfo );	
	}

	@Override
	public ProductPriceInfo updateProductInfo(Long id,  Map<String, Object> productMap) {

		HttpHeaders requestHeaders = new HttpHeaders();
		List <MediaType> mediaTypeList = new ArrayList<MediaType>();
		mediaTypeList.add(MediaType.APPLICATION_JSON);
		requestHeaders.setAccept(mediaTypeList);
		requestHeaders.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<?> requestEntity = new HttpEntity<Object>(productMap, requestHeaders);

		ResponseEntity<ProductPriceInfo> response =restTemplate.exchange("http://localhost:9001/price/{id}", HttpMethod.PUT, requestEntity, ProductPriceInfo.class, id );
		
		return response.getBody();
	}
}

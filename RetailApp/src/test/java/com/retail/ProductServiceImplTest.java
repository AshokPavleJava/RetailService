package com.retail;

import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.retail.dto.ProductDTO;
import com.retail.entity.Product;
import com.retail.repository.ProductRepository;
import com.retail.serviceimpl.ProductServiceImpl;

public class ProductServiceImplTest {

	@InjectMocks
	ProductServiceImpl productService;
	
	@Mock
	ProductRepository productRepository;
	
	@BeforeEach
	void init() {
		MockitoAnnotations.openMocks(this);
	}
	
	@Test
	void getAllProducts() {
		when(productRepository.findAll()).thenReturn(getProducts());
		List<ProductDTO> actualProducts = productService.getAllProducts();
		Assertions.assertEquals(2, actualProducts.size());
	}
	
	private List<Product> getProducts() {
		List<Product> products = new ArrayList<>();
		products.add(new Product(101, "Dressing Table", 4000.0, "Created", LocalDateTime.now()));
		products.add(new Product(102, "Dining Table", 3000.0, "Updated", LocalDateTime.now()));
		return products;
	}
}

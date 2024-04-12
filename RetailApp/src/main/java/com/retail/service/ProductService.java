package com.retail.service;

import java.time.LocalDateTime;
import java.util.List;

import com.retail.dto.ApprovalQueueDTO;
import com.retail.dto.ProductDTO;

public interface ProductService {

	List<ProductDTO> getAllProductsWithActiveStatus();

	List<ProductDTO> searchProducts(String productName, Double minPrice, Double maxPrice, LocalDateTime minPostedDate,
			LocalDateTime maxPostedDate);

	ProductDTO createProduct(ProductDTO product);

	ProductDTO updateProduct(ProductDTO product, int productId);

	void delete(int productId);
	
	/*
	 * Approval Queue methods
	 */
	List<ApprovalQueueDTO> getProductsInApprovalQueue();
	void approveProduct(int approvalId);
	void rejectProduct(int approvalId);
}

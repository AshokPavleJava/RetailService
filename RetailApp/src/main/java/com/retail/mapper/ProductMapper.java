package com.retail.mapper;

import com.retail.dto.ProductDTO;
import com.retail.entity.Product;

public class ProductMapper {
	public static Product convertToEntity(ProductDTO productDTO) {
		return new Product(productDTO.getId(), productDTO.getProductName(), productDTO.getPrice(),
				productDTO.getStatus(), productDTO.getPostedDate());
	}

	public static ProductDTO convertToDTO(Product product) {
		return new ProductDTO(product.getId(), product.getProductName(), product.getPrice(), product.getStatus(),
				product.getPostedDate());
	}
}

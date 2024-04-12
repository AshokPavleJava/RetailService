package com.retail.mapper;

import com.retail.constants.ProductConstants;
import com.retail.dto.ApprovalQueueDTO;
import com.retail.dto.ProductDTO;
import com.retail.entity.ApprovalQueue;
import com.retail.entity.Product;

public class ApprovalQueueMapper {
	public static ApprovalQueue convertToEntity(ApprovalQueueDTO approvalQueueDTO) {
		return null;
	}
	
	public static ApprovalQueueDTO convertToDTO(ApprovalQueue approvalQueue) {
		return new ApprovalQueueDTO(approvalQueue.getId(), approvalQueue.getProductId(),
				approvalQueue.getProductName(), approvalQueue.getPrice(), approvalQueue.getStatus(),
				approvalQueue.getPostedDate());
	}
	
	public static ApprovalQueue convertProductDTOToApprovalQueueEntity(ProductDTO productDTO) {
		ApprovalQueue approvalQueue = new ApprovalQueue();
		approvalQueue.setProductName(productDTO.getProductName());
		approvalQueue.setPrice(productDTO.getPrice());
		approvalQueue.setStatus(productDTO.getStatus());
		approvalQueue.setPreviousStatus(productDTO.getStatus());
		approvalQueue.setPostedDate(productDTO.getPostedDate());
		return approvalQueue;
	}
	
	public static ApprovalQueue convertProductEntityToAprovalQueueEntity(Product product) {
		ApprovalQueue approvalQueue = new ApprovalQueue();
		approvalQueue.setProductId(product.getId());
		approvalQueue.setProductName(product.getProductName());
		approvalQueue.setPrice(product.getPrice());
		approvalQueue.setStatus(ProductConstants.DELETED);
		approvalQueue.setPostedDate(product.getPostedDate());
		return approvalQueue;
	}
	
	public static Product convertApprovalQueueEntityToProductEntity(ApprovalQueue approvalQueue) {
		Product product = new Product();
		product.setProductName(approvalQueue.getProductName());
		product.setPrice(approvalQueue.getPrice());
		product.setStatus(approvalQueue.getStatus());
		product.setPostedDate(approvalQueue.getPostedDate());
		return product;
	}
}

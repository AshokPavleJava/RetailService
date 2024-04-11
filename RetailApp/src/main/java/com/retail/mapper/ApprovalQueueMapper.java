package com.retail.mapper;

import com.retail.dto.ApprovalQueueDTO;
import com.retail.entity.ApprovalQueue;

public class ApprovalQueueMapper {
	public static ApprovalQueue convertToEntity(ApprovalQueueDTO approvalQueueDTO) {
		return null;
	}
	
	public static ApprovalQueueDTO convertToDTO(ApprovalQueue approvalQueue) {
		return new ApprovalQueueDTO(approvalQueue.getId(), approvalQueue.getProductId(),
				approvalQueue.getProductName(), approvalQueue.getPrice(), approvalQueue.getStatus(),
				approvalQueue.getPostedDate());
	}
}

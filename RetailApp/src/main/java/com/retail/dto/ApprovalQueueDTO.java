package com.retail.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApprovalQueueDTO {
	private int id;
	private int productId;
	private String productName;
	private Double price;
	private String status;
	private LocalDateTime postedDate;
}

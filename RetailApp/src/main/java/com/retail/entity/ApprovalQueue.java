package com.retail.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "APPROVAL_QUEUE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApprovalQueue {

	@Id
	@Column(name = "APPROVAL_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column(name = "PRODUCT_ID")
	private int productId;
	@Column(name = "PRODUCT_NAME")
	private String productName;
	private Double price;
	private String status;
	@Column(name = "POSTED_DATE")
	private LocalDateTime postedDate;
	
}

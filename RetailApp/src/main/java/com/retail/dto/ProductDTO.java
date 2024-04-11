package com.retail.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO{
	
	private int id;
	private String productName;
	private Double price;
	private String status;
	private LocalDateTime postedDate;
}

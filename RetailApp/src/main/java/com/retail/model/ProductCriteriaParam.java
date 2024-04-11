package com.retail.model;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductCriteriaParam {
	private String productName;
	private Double minPrice;
	private Double maxPrice;
	private LocalDateTime minPostedDate;
	private LocalDateTime maxPostedDate;
}

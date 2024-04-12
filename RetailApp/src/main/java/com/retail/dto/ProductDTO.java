package com.retail.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
	@NotBlank(message = "Product name can not be blank")
	private String productName;
	@NotNull(message = "Product price can not be null")
	private Double price;
	@NotBlank(message = "Product status can not be blank")
	private String status;
	private LocalDateTime postedDate;
}

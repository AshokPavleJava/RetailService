package com.retail.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.retail.dto.ApprovalQueueDTO;
import com.retail.dto.ProductDTO;
import com.retail.service.ProductService;

@RestController
@RequestMapping("/api/products")
public class ProductController {
	@Autowired
	private ProductService productService;

	@GetMapping
	public ResponseEntity<List<ProductDTO>> getAllProducts() {
		List<ProductDTO> productDTOList = productService.getAllProducts();
		return new ResponseEntity<List<ProductDTO>>(productDTOList, HttpStatus.OK);
	}

	@GetMapping("/search")
	public ResponseEntity<List<ProductDTO>> searchProducts(@RequestParam(required = false) String productName,
			@RequestParam(required = false) Double minPrice, @RequestParam(required = false) Double maxPrice,
			@RequestParam(required = false) LocalDateTime minPostedDate,
			@RequestParam(required = false) LocalDateTime maxPostedDate) {
		List<ProductDTO> productDTOList = productService.searchProducts(productName, minPrice, maxPrice, minPostedDate, maxPostedDate);
		return new ResponseEntity<List<ProductDTO>>(productDTOList, HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductDTO product) {
		ProductDTO persistedProduct = productService.createProduct(product);
		return new ResponseEntity<ProductDTO>(persistedProduct, HttpStatus.CREATED);
	}

	@PutMapping("/{productId}")
	public ResponseEntity<ProductDTO> updateProduct(@RequestBody ProductDTO productDTO,
			@PathVariable("productId") Integer productId) {
		ProductDTO updatedProductDTO = productService.updateProduct(productDTO, productId);
		return new ResponseEntity<ProductDTO>(updatedProductDTO, HttpStatus.OK);
	}

	@DeleteMapping("/{productId}")
	public ResponseEntity<Void> deleteProduct(@PathVariable("productId") int productId) {
		productService.delete(productId);
		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
	}

	@GetMapping("/approval-queue")
	public ResponseEntity<List<ApprovalQueueDTO>> getProductsInApprovalQueue() {
		List<ApprovalQueueDTO> productsInApprovalQueue = productService.getProductsInApprovalQueue();
		return new ResponseEntity<List<ApprovalQueueDTO>>(productsInApprovalQueue, HttpStatus.OK);
	}

	@PutMapping("/approval-queue/{approvalId}/approve")
	public ResponseEntity<Void> approveProduct(@PathVariable("approvalId") int approvalId) {
		productService.approveProduct(approvalId);
		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
	}

	@PutMapping("/approval-queue/{approvalId}/reject")
	public ResponseEntity<Void> rejectProduct(@PathVariable("approvalId") int approvalId) {
		productService.rejectProduct(approvalId);
		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
	}
}

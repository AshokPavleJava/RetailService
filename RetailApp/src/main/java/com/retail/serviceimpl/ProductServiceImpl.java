package com.retail.serviceimpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.retail.constants.ProductConstants;
import com.retail.dto.ApprovalQueueDTO;
import com.retail.dto.ProductDTO;
import com.retail.entity.ApprovalQueue;
import com.retail.entity.Product;
import com.retail.mapper.ApprovalQueueMapper;
import com.retail.mapper.ProductMapper;
import com.retail.model.ProductCriteriaParam;
import com.retail.repository.ApprovalQueueRepository;
import com.retail.repository.ProductRepository;
import com.retail.service.ProductService;
import com.retail.specification.ProductSpecification;

@Service
public class ProductServiceImpl implements ProductService {
	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private ApprovalQueueRepository approvalQueueRepository;
	
	@Override
	public List<ProductDTO> getAllProductsWithActiveStatus() {
		return productRepository.findByStatus(ProductConstants.ACTIVE).stream()
		.sorted((product1, product2) -> product2.getPostedDate().compareTo(product1.getPostedDate()))
		.map(product -> ProductMapper.convertToDTO(product)).collect(Collectors.toList());
	}

	@Override
	public List<ProductDTO> searchProducts(String productName, Double minPrice, Double maxPrice,
			LocalDateTime minPostedDate, LocalDateTime maxPostedDate) {
		List<Product> products = null;
		List<ProductDTO> productDTOList = null;
		Specification<Product> specification = ProductSpecification.getProductCriteria(
				new ProductCriteriaParam(productName, minPrice, maxPrice, minPostedDate, maxPostedDate));
		products = productRepository.findAll(specification);
		productDTOList = products.stream().map(product -> ProductMapper.convertToDTO(product)).collect(Collectors.toList());
		return productDTOList;
	}

	@Override
	public ProductDTO createProduct(ProductDTO productDTO) {
		LocalDateTime postedDate = LocalDateTime.now();
		Product persistedProduct = null;
		ApprovalQueue approvalQueue = null;
		ProductDTO persistedProductDTO = null;
		productDTO.setPostedDate(postedDate);
		Product product = null;
		if (productDTO.getPrice() > ProductConstants.TEN_THOUSAND) {
			throw new IllegalArgumentException(ProductConstants.PRODUCT_PRICE_SHOULD_NOT_EXCEED_MAX_LIMIT);
		} else if (productDTO.getPrice() > ProductConstants.FIVE_THOUSAND) {
			approvalQueue = ApprovalQueueMapper.convertProductDTOToApprovalQueueEntity(productDTO);
			approvalQueueRepository.save(approvalQueue);
		} else {
			product = ProductMapper.convertToEntity(productDTO);
			persistedProduct = productRepository.save(product);
			persistedProductDTO = ProductMapper.convertToDTO(persistedProduct);
		}
		return persistedProductDTO;
	}

	@Override
	public ProductDTO updateProduct(ProductDTO productDTO, int productId) {
		Product persistedProduct = productRepository.getReferenceById(productId);
		Product updatedProduct = null;
		ProductDTO updatedProductDTO = null;
		Product product = null;
		ApprovalQueue approvalQueue = null;
		double newPrice = persistedProduct.getPrice() * 50 / 100;
		if (productDTO.getPrice() > (persistedProduct.getPrice() + newPrice)) {
			approvalQueue = ApprovalQueueMapper.convertProductDTOToApprovalQueueEntity(productDTO);
			approvalQueue.setStatus(ProductConstants.UPDATED);
			approvalQueue.setProductId(productDTO.getId());
			approvalQueueRepository.save(approvalQueue);
		} else {
			product = ProductMapper.convertToEntity(productDTO);
			updatedProduct = productRepository.save(product);
			updatedProductDTO = ProductMapper.convertToDTO(updatedProduct);
		}
		return updatedProductDTO;
	}

	@Override
	public void delete(int productId) {
		Product product = productRepository.getReferenceById(productId);
		ApprovalQueue approvalQueue = ApprovalQueueMapper.convertProductEntityToAprovalQueueEntity(product);
		approvalQueue.setPreviousStatus(product.getStatus());
		productRepository.deleteById(productId);
		approvalQueueRepository.save(approvalQueue);
	}

	@Override
	public List<ApprovalQueueDTO> getProductsInApprovalQueue() {
		return approvalQueueRepository.findAll().stream()
				.sorted((approvalQueue1, approvalQueue2) -> approvalQueue1.getPostedDate()
						.compareTo(approvalQueue2.getPostedDate()))
				.map(approvalQueue -> ApprovalQueueMapper.convertToDTO(approvalQueue)).collect(Collectors.toList());
	}

	@Override
	public void approveProduct(int approvalId) {
		Product product = null;
		ApprovalQueue approvalQueue = null;
		approvalQueue = approvalQueueRepository.getReferenceById(approvalId);
		if(!approvalQueue.getStatus().equalsIgnoreCase(ProductConstants.DELETED)) {
			product = ApprovalQueueMapper.convertApprovalQueueEntityToProductEntity(approvalQueue);
			product.setStatus(ProductConstants.UPDATED);
		}
		if (approvalQueue.getStatus().equalsIgnoreCase(ProductConstants.ACTIVE)) {
			productRepository.save(product); // price > 5000 sent to approval queue
		} else if(approvalQueue.getStatus().equalsIgnoreCase(ProductConstants.UPDATED)) {
			product.setId(approvalQueue.getProductId()); // price > 50% previous sent to approval queue
			productRepository.save(product);
		}
		approvalQueueRepository.deleteById(approvalId);
	}

	@Override
	public void rejectProduct(int approvalId) {
		ApprovalQueue approvalQueue = approvalQueueRepository.getReferenceById(approvalId);
		Product product = null;
		if(approvalQueue.getStatus().equalsIgnoreCase(ProductConstants.DELETED)) {
			product = ApprovalQueueMapper.convertApprovalQueueEntityToProductEntity(approvalQueue);
			product.setStatus(approvalQueue.getPreviousStatus());
			productRepository.save(product);
		}
		approvalQueueRepository.deleteById(approvalId);
	}

	

}

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
	public List<ProductDTO> getAllProducts() {
		return productRepository.findAll().stream()
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
		ApprovalQueue approvalQueue =  null;
		ProductDTO persistedProductDTO = null;
		productDTO.setPostedDate(postedDate);
		Product product = ProductMapper.convertToEntity(productDTO);
		if(product.getPrice() > ProductConstants.TEN_THOUSAND) {
			throw new IllegalArgumentException(ProductConstants.PRODUCT_PRICE_SHOULD_NOT_EXCEED_MAX_LIMIT);
		} else if(product.getPrice() > ProductConstants.FIVE_THOUSAND) {
			approvalQueue = new ApprovalQueue();
			approvalQueue.setProductName(product.getProductName());
			approvalQueue.setPrice(product.getPrice());
			approvalQueue.setStatus(product.getStatus());
			approvalQueue.setPostedDate(product.getPostedDate());
			approvalQueueRepository.save(approvalQueue);
		} else {
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
		Product product = ProductMapper.convertToEntity(productDTO);
		double newPrice = persistedProduct.getPrice() * 50 / 100;
		if (product.getPrice() > (persistedProduct.getPrice() + newPrice)) {
			ApprovalQueue approvalQueue = new ApprovalQueue();
			approvalQueue.setProductId(product.getId());
			approvalQueue.setProductName(product.getProductName());
			approvalQueue.setPrice(product.getPrice());
			approvalQueue.setStatus(product.getStatus());
			approvalQueue.setPostedDate(product.getPostedDate());
			approvalQueueRepository.save(approvalQueue);
		} else {
			updatedProduct = productRepository.save(product);
			updatedProductDTO = ProductMapper.convertToDTO(updatedProduct);
		}
		
		return updatedProductDTO;
	}

	@Override
	public void delete(int productId) {
		Product product = productRepository.getReferenceById(productId);
		ApprovalQueue approvalQueue = new ApprovalQueue();
		approvalQueue.setProductId(productId);
		approvalQueue.setProductName(product.getProductName());
		approvalQueue.setPrice(product.getPrice());
		approvalQueue.setStatus("Deleted");
		approvalQueue.setPostedDate(product.getPostedDate());
		productRepository.deleteById(productId);
		approvalQueueRepository.save(approvalQueue);
	}

	@Override
	public List<ApprovalQueueDTO> getProductsInApprovalQueue() {
		return approvalQueueRepository.findAll().stream().sorted((approvalQueue1, approvalQueue2) -> approvalQueue1
				.getPostedDate().compareTo(approvalQueue2.getPostedDate()))
				.map(approvalQueue -> ApprovalQueueMapper.convertToDTO(approvalQueue))
				.collect(Collectors.toList());
	}

	@Override
	public void approveProduct(int approvalId) {
		Product product = null;
		ApprovalQueue approvalQueue = null;
		approvalQueue = approvalQueueRepository.getReferenceById(approvalId);
		if(!approvalQueue.getStatus().equalsIgnoreCase(ProductConstants.DELETED)) {
			product = new Product();
			product.setProductName(approvalQueue.getProductName());
			product.setPrice(approvalQueue.getPrice());
			product.setStatus(ProductConstants.UPDATED);
			product.setPostedDate(approvalQueue.getPostedDate());
		}
		if (approvalQueue.getStatus().equalsIgnoreCase(ProductConstants.CREATED)) {
			product = productRepository.save(product); // price > 5000 send to approval queue
		} else if(approvalQueue.getStatus().equalsIgnoreCase(ProductConstants.UPDATED)) {
			product.setId(approvalQueue.getProductId()); // price > 50% previous send to approval queue
			product = productRepository.save(product);
		}
		approvalQueueRepository.deleteById(approvalId);
	}

	@Override
	public void rejectProduct(int approvalId) {
		ApprovalQueue approvalQueue = approvalQueueRepository.getReferenceById(approvalId);
		Product product = null;
		if(approvalQueue.getStatus().equalsIgnoreCase(ProductConstants.DELETED)) {
			product = new Product();
			product.setProductName(approvalQueue.getProductName());
			product.setPrice(approvalQueue.getPrice());
			product.setStatus(ProductConstants.UPDATED);
			product.setPostedDate(approvalQueue.getPostedDate());
			productRepository.save(product);
		}
		approvalQueueRepository.deleteById(approvalId);
	}

}

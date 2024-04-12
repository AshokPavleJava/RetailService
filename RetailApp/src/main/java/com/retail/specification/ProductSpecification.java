package com.retail.specification;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import com.retail.entity.Product;
import com.retail.model.ProductCriteriaParam;

import jakarta.persistence.criteria.Predicate;

public class ProductSpecification {
	private static final String PRODUCT_NAME = "productName";
	private static final String PRICE = "price";
	private static final String POSTED_DATE = "postedDate";
	public static Specification<Product> getProductCriteria(ProductCriteriaParam param) {
		return (root, query, criteriaBuilder) -> {
			List<Predicate> predicates = new ArrayList<>();
			if (StringUtils.hasText(param.getProductName())) {
				predicates.add(criteriaBuilder.equal(root.get(PRODUCT_NAME), param.getProductName()));
			}
			if (param.getMinPrice() != null) {
				predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get(PRICE), param.getMinPrice()));
			}
			if (param.getMaxPrice() != null) {
				predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get(PRICE), param.getMaxPrice()));
			}
			if (param.getMinPostedDate() != null) {
				predicates
						.add(criteriaBuilder.greaterThanOrEqualTo(root.get(POSTED_DATE), param.getMinPostedDate()));
			}
			if (param.getMaxPostedDate() != null) {
				predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get(POSTED_DATE), param.getMaxPostedDate()));
			}
			return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
		};
	}
}

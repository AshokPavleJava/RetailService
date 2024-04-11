package com.retail.specification;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import com.retail.entity.Product;
import com.retail.model.ProductCriteriaParam;

import jakarta.persistence.criteria.Predicate;

public class ProductSpecification {
	public static Specification<Product> getProductCriteria(ProductCriteriaParam param) {
		return (root, query, criteriaBuilder) -> {
			List<Predicate> predicates = new ArrayList<>();
			if (StringUtils.hasText(param.getProductName())) {
				predicates.add(criteriaBuilder.equal(root.get("productName"), param.getProductName()));
			}
			if (param.getMinPrice() != null) {
				predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("price"), param.getMinPrice()));
			}
			if (param.getMaxPrice() != null) {
				predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("price"), param.getMaxPrice()));
			}
			if (param.getMinPostedDate() != null) {
				predicates
						.add(criteriaBuilder.greaterThanOrEqualTo(root.get("postedDate"), param.getMinPostedDate()));
			}
			if (param.getMaxPostedDate() != null) {
				predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("postedDate"), param.getMaxPostedDate()));
			}
			return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
		};
	}
}

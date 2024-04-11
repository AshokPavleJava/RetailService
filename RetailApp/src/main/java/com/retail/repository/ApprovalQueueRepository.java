package com.retail.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.retail.entity.ApprovalQueue;

public interface ApprovalQueueRepository extends JpaRepository<ApprovalQueue, Integer> {
	
}

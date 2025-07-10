package com.app.app_customers.repo;

import com.app.app_customers.db.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Integer> {
    @Query("SELECT p FROM PurchaseOrder p WHERE p.customer.id = ?1")
    List<PurchaseOrder> findByCustomerId(Integer customerId);
}

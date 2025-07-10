package com.app.app_customers.repo;

import com.app.app_customers.db.LineItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LineItemRepository extends JpaRepository<LineItem, Integer> {
    @Query("SELECT l FROM LineItem l WHERE l.id = ?1")
    Optional<LineItem> findById(Integer id);
}

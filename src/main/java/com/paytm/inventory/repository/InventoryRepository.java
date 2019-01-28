package com.paytm.inventory.repository;

import com.paytm.inventory.models.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<Item,Long>{
    Optional<Item> findByName(String name);
}

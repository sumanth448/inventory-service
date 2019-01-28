package com.paytm.inventory.repository;

import com.paytm.inventory.models.Item;
import com.paytm.inventory.models.Variant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VariantRepository extends JpaRepository<Variant,Long> {
    List<Variant> getVariantByItemAndNameIn(Item item, List<String> name);
}

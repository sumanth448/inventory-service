package com.paytm.inventory.models;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "item_details")
@Data
public class Item extends BaseEntity {
    private String name;
    private String itemId;
    private String category;
    private String productCode;
    private String brand;
}

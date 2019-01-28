package com.paytm.inventory.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import com.paytm.inventory.models.dto.Properties;
import org.hibernate.annotations.Type;

import javax.persistence.*;

@Entity
@Table(name = "variant_details")
@Data
public class Variant extends BaseEntity {
    private String name;
    private Double sellingPrice;
    private Double costPrice;
    private String variantId;

    @Type(type = "json")
    @Column(columnDefinition = "json")
    private Properties properties;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Item.class)
    Item item;

    private int quantity;

}

package com.paytm.inventory.models.dto;

import com.paytm.inventory.enums.OperationType;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Map;

@Data
public class VariantRequestDTO {
    @NotNull
    private String name;
    @NotNull
    private OperationType operationType;
    @NotBlank
    private Double costPrice;
    @NotBlank
    private Double sellingPrice;
    @NotBlank
    private int quantity;
    @NotNull
    private Map<String,Object> properties;
}

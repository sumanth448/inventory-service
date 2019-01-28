package com.paytm.inventory.models.dto;

import com.paytm.inventory.enums.OperationType;
import lombok.Data;
import com.paytm.inventory.models.Variant;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class ItemRequestDTO {
    @NotNull
    private OperationType operationType;
    @NotBlank
    private String name;
    @NotBlank
    private String brand;
    @NotBlank
    private String category;
    @NotBlank
    private String productCode;
    @NotNull
    private List<VariantRequestDTO> variants;
}

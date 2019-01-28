package com.paytm.inventory.models.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class GetUserDetailsRequestDTO {
    @NotBlank
    private String startTime;
    @NotBlank
    private String endTime;
    private String userName;
}

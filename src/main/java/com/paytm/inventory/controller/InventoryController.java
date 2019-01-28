package com.paytm.inventory.controller;

import com.paytm.inventory.constants.InventoryConstants;
import com.paytm.inventory.models.User;
import com.paytm.inventory.models.dto.GetUserDetailsRequestDTO;
import com.paytm.inventory.models.dto.ItemRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.paytm.inventory.service.InventoryService;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@RestController
@RequestMapping("paytm/inventory")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    @PostMapping("add_edit_remove")
    public ResponseEntity addDeleteModify(@NotBlank @RequestHeader(InventoryConstants.USERNAME) String userName,
                                          @ApiIgnore @RequestAttribute(InventoryConstants.USER) User user,
                                          @Valid @RequestBody ItemRequestDTO itemRequestDTO){

     return ResponseEntity.ok(inventoryService.addUpdateInventory(user,itemRequestDTO));
    }

    @PostMapping("get_details")
    public ResponseEntity getActionDetails(@NotBlank @RequestHeader(InventoryConstants.USERNAME) String userName,
                                           @ApiIgnore @RequestAttribute(InventoryConstants.USER) User user,
                                           @Valid @RequestBody GetUserDetailsRequestDTO getUserDetailsRequestDTO){
      return ResponseEntity.ok(inventoryService.getUserDetails(user,getUserDetailsRequestDTO));
    }

}

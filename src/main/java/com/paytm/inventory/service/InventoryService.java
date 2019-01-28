package com.paytm.inventory.service;

import com.paytm.inventory.models.User;
import com.paytm.inventory.models.dto.GetUserDetailsRequestDTO;
import com.paytm.inventory.models.dto.ItemRequestDTO;

import java.util.List;
import java.util.Map;

public interface InventoryService {
    public String addUpdateInventory(User user, ItemRequestDTO itemRequestDTO);
    public Map<String,List<String>> getUserDetails(User user, GetUserDetailsRequestDTO getUserDetailsRequestDTO);
}

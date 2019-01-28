package com.paytm.inventory.service.impl;

import com.paytm.inventory.constants.InventoryConstants;
import com.paytm.inventory.enums.OperationType;
import com.paytm.inventory.enums.PropertyType;
import com.paytm.inventory.exception.InventoryServiceException;
import com.paytm.inventory.models.Item;
import com.paytm.inventory.models.User;
import com.paytm.inventory.models.UserActions;
import com.paytm.inventory.models.Variant;
import com.paytm.inventory.models.dto.GetUserDetailsRequestDTO;
import com.paytm.inventory.models.dto.ItemRequestDTO;
import com.paytm.inventory.models.dto.Properties;
import com.paytm.inventory.models.dto.VariantRequestDTO;
import com.paytm.inventory.repository.UserActionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.paytm.inventory.repository.InventoryRepository;
import com.paytm.inventory.repository.VariantRepository;
import com.paytm.inventory.service.InventoryService;

import javax.validation.constraints.NotBlank;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class InventoryServiceImpl implements InventoryService {

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private VariantRepository variantRepository;

    @Autowired
    private UserActionRepository userActionRepository;

    @Override
    @Transactional
    public String addUpdateInventory(User user, ItemRequestDTO itemRequestDTO) {
        List<UserActions> userActionsList = new ArrayList<>();
        Date date = new Date();
        Item item = processItemDetails(user,itemRequestDTO,userActionsList,date);
        processVariantDetails(user,itemRequestDTO,userActionsList,date,item);
        userActionRepository.saveAll(userActionsList);
        return InventoryConstants.SUCCESS;
    }

    @Override
    public Map<String, List<String>> getUserDetails(User user, GetUserDetailsRequestDTO getUserDetailsRequestDTO) {
        List<UserActions> userActionsList = new ArrayList<>();
        Date startDate = null;
        Date endDate = null;
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        try {
            startDate = dateFormat.parse(getUserDetailsRequestDTO.getStartTime());
            endDate = dateFormat.parse(getUserDetailsRequestDTO.getEndTime());
        } catch (ParseException e) {
            throw new InventoryServiceException("INV-500","Error in proceesing the request");
        }
        if(getUserDetailsRequestDTO.getUserName() != null && getUserDetailsRequestDTO.getUserName().length() > 0){
            userActionsList = userActionRepository.findByTimeBetweenAndUserName(startDate,endDate,user.getUserName());
        }else{
            userActionsList = userActionRepository.findByTimeBetween(startDate,endDate);
        }
        return parseResponse(userActionsList);
    }

    private Map<String,List<String>> parseResponse(List<UserActions> userActionsList) {
        Map<String,List<String>> resultMap = new HashMap<>();
        userActionsList.stream().filter(Objects::nonNull).forEach(userActions -> {
            if(resultMap.containsKey(userActions.getUserName())){
                List<String> actions = resultMap.get(userActions.getUserName());
                actions.add(generateAction(userActions));
                resultMap.put(userActions.getUserName(),actions);
            }else{
                List<String> list = new ArrayList<>();
                list.add(generateAction(userActions));
                resultMap.put(userActions.getUserName(),list);
            }
        });
        return resultMap;
    }

    private String generateAction(UserActions userActions) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(userActions.getUserName());
        stringBuilder.append(" has ");
        stringBuilder.append(userActions.getOperationType().toString());
        stringBuilder.append(" ");
        stringBuilder.append(String.join(",",userActions.getAttributeName()));
        stringBuilder.append(" of ");
        stringBuilder.append(userActions.getItemName());
        return stringBuilder.toString();
    }

    private void processVariantDetails(User user, ItemRequestDTO itemRequestDTO, List<UserActions> userActionsList,
                                       Date date, Item item) {
        List<String> variantNames = itemRequestDTO.getVariants().stream().map(x-> x.getName()).collect(Collectors.toList());
        List<Variant> variantList = variantRepository.getVariantByItemAndNameIn(item,variantNames);
        List<String> attributes = new ArrayList<>();
        Map<String,List<Variant>> variantMap = variantList.stream().collect(Collectors.groupingBy(Variant::getName));
        List<Variant> variantInsertList = new ArrayList<>();
        itemRequestDTO.getVariants().stream().filter(Objects::nonNull).forEach(variant-> {
           if(OperationType.ADD.equals(variant.getOperationType())){
             if(!variantMap.containsKey(variant.getName())) {
                 Variant newVariant = generateVariant(item, variant);
                 variantInsertList.add(newVariant);
                 if (!OperationType.ADD.equals(itemRequestDTO.getOperationType())) {
                     attributes.add(variant.getName());
                     UserActions userActions = generateUserActions(user, OperationType.ADD, PropertyType.VARIANT, attributes,
                             itemRequestDTO.getName(), date);
                     userActionsList.add(userActions);
                 }
              }
             }else if(OperationType.MODIFY.equals(variant.getOperationType())){
                if(variantMap.containsKey(variant.getName())){
                    Variant newVariant = null;
                    UserActions userActions = compareVariantValues(variant,variantMap.get(variant.getName()),user,date,item);
                    if(userActions != null){
                        userActionsList.add(userActions);
                        newVariant = generateVariant(item,variant);
                        newVariant.setId(variantMap.get(variant.getName()).get(0).getId());
                        variantInsertList.add(newVariant);
                    }
                }
             }else{
                 throw new InventoryServiceException("INV-401","Currently deletion of item is not supported");
             }
        });
        variantRepository.saveAll(variantInsertList);
    }


    private Item processItemDetails(User user, ItemRequestDTO itemRequestDTO, List<UserActions> userActionsList,
                                    Date date) {
        Optional<Item> item = inventoryRepository.findByName(itemRequestDTO.getName());
        List<String> attributes = new ArrayList<>();
        switch (itemRequestDTO.getOperationType()){
            case ADD:
                if(!item.isPresent()){
                  Item newItem = generateItem(itemRequestDTO);
                  //Save in Inventory
                  newItem  = inventoryRepository.save(newItem);
                  attributes.add(itemRequestDTO.getName());
                  UserActions userActions = generateUserActions(user,OperationType.ADD, PropertyType.ITEM,attributes,
                          itemRequestDTO.getName(),date);
                  userActionsList.add(userActions);
                  return newItem;
                }else{
                    throw new InventoryServiceException("INV-401","Already product exists");
                }
            case MODIFY:
                if(item.isPresent()){
                    Item newItem = null;
                    UserActions userActions = compareItemValues(item.get(),itemRequestDTO,user,date);
                    if(userActions != null){
                        userActionsList.add(userActions);
                        newItem = generateItem(itemRequestDTO);
                        newItem.setId(item.get().getId());
                        //Save in Inventory
                        newItem = inventoryRepository.save(newItem);
                    }
                    return newItem!= null ? newItem : item.get();
                }else{
                    throw new InventoryServiceException("INV-401","Item not present so can't update");
                }
            case DELETE:
                throw new InventoryServiceException("INV-401","Currently deletion of item is not supported");
            default:
                return null;
        }

    }

    private UserActions compareItemValues(Item item, ItemRequestDTO itemRequestDTO, User user, Date date) {

        List<String> attributes = new ArrayList<>();
        if(!item.getCategory().equalsIgnoreCase(itemRequestDTO.getCategory())){
          attributes.add("category");
        }
        if(!item.getProductCode().equalsIgnoreCase(itemRequestDTO.getProductCode())){
           attributes.add("productCode");
        }
        if(!item.getBrand().equalsIgnoreCase(itemRequestDTO.getBrand())){
            attributes.add("brand");
        }
        if(attributes != null && attributes.size() > 0){
         UserActions userActions = new UserActions();
         userActions.setUserName(user.getUserName());
         userActions.setPropertyType(PropertyType.ITEM);
         userActions.setOperationType(itemRequestDTO.getOperationType());
         userActions.setTime(date);
         userActions.setItemName(itemRequestDTO.getName());
         userActions.setAttributeName(attributes);
         return userActions;
        }
        return null;
    }


    private UserActions generateUserActions(User user, OperationType operationType, PropertyType propertyType,
                                            List<String> attributes, @NotBlank String name, Date date) {
        UserActions userActions = new UserActions();
        userActions.setTime(date);
        userActions.setAttributeName(attributes);
        userActions.setItemName(name);
        userActions.setOperationType(operationType);
        userActions.setPropertyType(propertyType);
        userActions.setUserName(user.getUserName());
        return userActions;
    }

    private UserActions compareVariantValues(VariantRequestDTO variant, List<Variant> variants, User user, Date date, Item item) {
        List<String> attributes = new ArrayList<>();
        Variant existingVariant = variants.get(0);
        if(!variant.getCostPrice().equals(existingVariant.getCostPrice())){
            attributes.add("CostPrice");
        }
        if(!variant.getSellingPrice().equals(existingVariant.getSellingPrice())){
            attributes.add("SellingPrice");
        }
        if(variant.getQuantity() != existingVariant.getQuantity()){
            attributes.add("Quantity");
        }
        variant.getProperties().entrySet().stream().filter(Objects::nonNull).forEach(x-> {
          if(existingVariant.getProperties() != null && existingVariant.getProperties().getProperties() != null &&
                  existingVariant.getProperties().getProperties().containsKey(x.getKey())){
              if(x.getValue() != existingVariant.getProperties().getProperties().get(x.getKey())){
                attributes.add("modified " + x.getKey());
              }
          }else{
              attributes.add("added " + x.getKey());
          }
        });
        UserActions userActions = new UserActions();
        userActions.setTime(date);
        userActions.setItemName(item.getName());
        userActions.setAttributeName(attributes);
        userActions.setOperationType(variant.getOperationType());
        userActions.setPropertyType(PropertyType.VARIANT);
        userActions.setUserName(user.getUserName());
        return userActions;
    }

    private Item generateItem(ItemRequestDTO itemRequestDTO) {
        Item item = new Item();
        item.setCategory(itemRequestDTO.getCategory());
        item.setItemId(UUID.randomUUID().toString());
        item.setName(itemRequestDTO.getName());
        item.setProductCode(itemRequestDTO.getProductCode());
        item.setBrand(itemRequestDTO.getBrand());
        return item;
    }

    private Variant generateVariant(Item item, VariantRequestDTO variant) {
        Variant newVaraint = new Variant();
        Properties properties = new Properties();
        newVaraint.setCostPrice(variant.getCostPrice());
        newVaraint.setItem(item);
        newVaraint.setName(variant.getName());
        newVaraint.setSellingPrice(variant.getSellingPrice());
        newVaraint.setQuantity(variant.getQuantity());
        properties.setProperties(variant.getProperties());
        newVaraint.setProperties(properties);
        return newVaraint;
    }

}

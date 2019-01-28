package com.paytm.inventory.models;

import com.paytm.inventory.enums.OperationType;
import com.paytm.inventory.enums.PropertyType;
import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Data
@Table(name = "user_actions")
public class UserActions extends BaseEntity{
    private String userName;

    private String itemName;

    @Enumerated(EnumType.STRING)
    private OperationType operationType;

    @Enumerated(EnumType.STRING)
    private PropertyType propertyType;

    private Date time;

    @Type(type = "json")
    @Column(columnDefinition = "json")
    private List<String> attributeName;


}

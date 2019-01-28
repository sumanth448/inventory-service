package com.paytm.inventory.models;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "user_details")
@Data
@EqualsAndHashCode(callSuper = false)
public class User extends BaseEntity {
    private String userId;
    private String userName;
    private String emailId;
    private String phoneNo;
}

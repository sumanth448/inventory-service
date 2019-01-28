package com.paytm.inventory.repository;

import com.paytm.inventory.models.UserActions;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface UserActionRepository extends JpaRepository<UserActions,Long>{
    List<UserActions> findByTimeBetween(Date start, Date end);
    List<UserActions> findByTimeBetweenAndUserName(Date start, Date end, String userName);
}

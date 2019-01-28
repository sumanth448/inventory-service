package com.paytm.inventory.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.TypeDef;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@MappedSuperclass
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
@TypeDef(name = "json", typeClass = JsonStringType.class)
public abstract class BaseEntity {

    @JsonIgnore
    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    Date createdAt;

    @JsonIgnore
    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    Date updatedAt;

    @JsonIgnore
    @Id
    @GenericGenerator(name = "native", strategy = "native")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    private Long id;

}
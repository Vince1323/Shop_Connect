package com.projet.ShopConnect.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Where(clause = "deleted = FALSE")
@Entity
@Table(name = "address")
public class Address extends Identified {
    private String street;
    private String streetNumber;
    private String boxNumber;
    private String zipCode;
    private String city;
    private String country;
}
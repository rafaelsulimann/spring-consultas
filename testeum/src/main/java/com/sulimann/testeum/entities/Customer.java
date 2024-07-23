package com.sulimann.testeum.entities;

import java.math.BigDecimal;

import com.sulimann.testeum.dtos.CustomerResponse;

import jakarta.persistence.ColumnResult;
import jakarta.persistence.ConstructorResult;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SqlResultSetMapping;
import jakarta.persistence.SqlResultSetMappings;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Table(name = "tb_customer")
@Getter
@SqlResultSetMappings({
  @SqlResultSetMapping(name = "CustomerResponseMapping", classes = @ConstructorResult(targetClass = CustomerResponse.class, columns = {
      @ColumnResult(name = "name", type = String.class) })) })
public class Customer {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;
  private String street;
  private String city;
  private String state;
  private BigDecimal creditLimit;

  public Customer() {
  }

  public Customer(String name, String street, String city, String state, BigDecimal creditLimit) {
    this.name = name;
    this.street = street;
    this.city = city;
    this.state = state;
    this.creditLimit = creditLimit;
  }

  @Override
  public String toString() {
    return "Customer [id=" + id + ", name=" + name + ", street=" + street + ", city=" + city + ", state=" + state
        + ", creditLimit=" + creditLimit + "]";
  }

}

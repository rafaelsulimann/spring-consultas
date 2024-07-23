package com.sulimann.testeum.dtos;

import com.sulimann.testeum.entities.Customer;
import com.sulimann.testeum.projections.CustomerResponseProjection;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerResponse {

  private String name;

  public CustomerResponse(CustomerResponseProjection projection) {
    this.name = projection.getName();
  }

  public CustomerResponse(Customer entity){
    this.name = entity.getName();
  }

  @Override
  public String toString() {
    return "CustomerResponse [name=" + name + "]";
  }

}

package com.sulimann.testeum.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sulimann.testeum.dtos.CustomerResponse;
import com.sulimann.testeum.services.CustomerService;
import com.sulimann.testeum.specs.CustomerSpec;

@RestController
@RequestMapping(value = "/customers")
public class CustomerController {

  private final CustomerService customerService;

  public CustomerController(CustomerService customerService) {
    this.customerService = customerService;
  }

  @GetMapping(value = "/spec")
  public ResponseEntity<Page<CustomerResponse>> findAllCustomersByState(CustomerSpec spec, @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable){
    return ResponseEntity.ok(this.customerService.findAllCustomersByStateWithSpecificationAndCriteriaQuery(spec, pageable));
  }
}

package com.sulimann.testeum.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sulimann.testeum.dtos.CustomerResponse;
import com.sulimann.testeum.entities.Customer;
import com.sulimann.testeum.projections.CustomerResponseProjection;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

  @Query("SELECT new com.sulimann.testeum.dtos.CustomerResponse(c.name) FROM Customer c WHERE c.state = :state")
  Page<CustomerResponse> findAllCustomersByStateJpql(@Param("state") String state, Pageable pageable);

  @Query(nativeQuery = true, value = "SELECT c.name FROM tb_customer as c WHERE c.state = :state")
  Page<CustomerResponseProjection> findAllCustomersByStateNative(@Param("state") String state, Pageable pageable);

}

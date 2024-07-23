package com.sulimann.testeum.services;

import java.util.List;

import org.hibernate.query.NativeQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.sulimann.testeum.dtos.CustomerResponse;
import com.sulimann.testeum.entities.Customer;
import com.sulimann.testeum.entities.Customer_;
import com.sulimann.testeum.repositories.CustomerRepository;
import com.sulimann.testeum.specs.CustomerSpec;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

@Service
public class CustomerService {

  private final CustomerRepository customerRepository;
  private final EntityManager entityManager;

  public CustomerService(CustomerRepository customerRepository, EntityManager entityManager) {
    this.customerRepository = customerRepository;
    this.entityManager = entityManager;
  }

  /*
   * Sua empresa está fazendo um levantamento de quantos clientes estão
   * cadastrados nos estados,
   * porém, faltou levantar os dados do estado do Rio Grande do Sul.
   * Então você deve Exibir o nome de todos os clientes cujo estado seja ‘RS’.
   */

  // 1 - Spring Repository -> JPQL + DTO
  public Page<CustomerResponse> findAllCustomersByStateWithJpql(String state, Pageable pageable) {
    return this.customerRepository.findAllCustomersByStateJpql(state, pageable);
  }

  // 2 - Spring Repository -> Native Query + Projection
  public Page<CustomerResponse> findAllCustomersByStateWithNative(String state, Pageable pageable) {
    return this.customerRepository.findAllCustomersByStateNative(state, pageable).map(CustomerResponse::new);
  }

  // 3 - EntityManager -> JPQL + DTO
  public Page<CustomerResponse> findAllCustomersByStateWithJpqlUsingEntityManager(String state, Pageable pageable) {
    var query = this.entityManager.createQuery(
        "SELECT new com.sulimann.testeum.dtos.CustomerResponse(c.name) FROM Customer c WHERE c.state = :state",
        CustomerResponse.class);
    query.setParameter("state", state);

    query.setFirstResult((int) pageable.getOffset());
    query.setMaxResults(pageable.getPageSize());

    List<CustomerResponse> resultList = query.getResultList();

    var countQuery = this.entityManager.createQuery("SELECT COUNT(c) FROM Customer c WHERE c.state = :state");
    countQuery.setParameter("state", state);
    Long total = (Long) countQuery.getSingleResult();

    return new PageImpl<>(resultList, pageable, total);
  }

  // 4 - EntityManager -> Native Query + DTO
  @SuppressWarnings("unchecked")
  public Page<CustomerResponse> findAllCustomersByStateWithNativeUsingEntityManager(String state, Pageable pageable) {
    // Construção da consulta nativa para buscar os clientes
    Query query = this.entityManager.createNativeQuery("""
        SELECT c.name as name
        FROM tb_customer c
        WHERE c.state = :state
        """);
    query.setParameter("state", state);
    query.setFirstResult((int) pageable.getOffset());
    query.setMaxResults(pageable.getPageSize());

    // Definindo o mapeamento do resultado
    query.unwrap(NativeQuery.class).setTupleTransformer((tuples, aliases) -> {
      var result = new CustomerResponse();
      result.setName((String) tuples[0]);
      return result;
    });

    List<CustomerResponse> resultList = query.getResultList();

    // Construção da consulta nativa para contar o total de clientes
    Query countQuery = this.entityManager
        .createNativeQuery("SELECT COUNT(c.id) FROM tb_customer c WHERE c.state = :state");
    countQuery.setParameter("state", state);
    Long total = ((Number) countQuery.getSingleResult()).longValue();

    // Retorno do resultado paginado
    return new PageImpl<>(resultList, pageable, total);
  }

  public Page<CustomerResponse> findAll(Pageable pageable) {
    return this.customerRepository.findAll(pageable).map(CustomerResponse::new);
  }

  public Page<CustomerResponse> findAllCustomersByStateWithCriteriaQueryUsingEntityManager(String state,
      Pageable pageable) {
    CriteriaBuilder cb = this.entityManager.getCriteriaBuilder();

    // Consulta para buscar os clientes
    CriteriaQuery<CustomerResponse> query = cb.createQuery(CustomerResponse.class);
    Root<Customer> root = query.from(Customer.class);
    query.select(cb.construct(CustomerResponse.class, root.get(Customer_.name)));
    query.where(cb.equal(root.get(Customer_.state), state));

    List<CustomerResponse> resultList = this.entityManager.createQuery(query)
        .setFirstResult((int) pageable.getOffset())
        .setMaxResults(pageable.getPageSize())
        .getResultList();

    // Consulta para contar o total de clientes
    CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
    Root<Customer> countRoot = countQuery.from(Customer.class);
    countQuery.select(cb.count(countRoot));
    countQuery.where(cb.equal(countRoot.get(Customer_.state), state));
    Long total = this.entityManager.createQuery(countQuery).getSingleResult();

    // Retorno do resultado paginado
    return new PageImpl<>(resultList, pageable, total);
  }

  public Page<CustomerResponse> findAllCustomersByStateWithNativeQueryUsingEntityManagerAndSqlResultMapping(
      String state,
      Pageable pageable) {
    Query query = this.entityManager.createNativeQuery("""
        SELECT c.name name
        FROM tb_customer c
        WHERE c.state = :state
        """, "CustomerResponseMapping");
    query.setParameter("state", state);

    List<CustomerResponse> resultList = query
        .setFirstResult((int) pageable.getOffset())
        .setMaxResults(pageable.getPageSize())
        .getResultList();

    // Consulta para contar o total de clientes
    Query countQuery = this.entityManager
        .createNativeQuery("SELECT COUNT(c.id) FROM tb_customer c WHERE c.state = :state");
    countQuery.setParameter("state", state);
    Long total = ((Number) countQuery.getSingleResult()).longValue();

    // Retorno do resultado paginado
    return new PageImpl<>(resultList, pageable, total);
  }

  public Page<CustomerResponse> findAllCustomersByStateWithSpecificationAndCriteriaQuery(CustomerSpec spec, Pageable pageable) {
    CriteriaBuilder cb = this.entityManager.getCriteriaBuilder();
    CriteriaQuery<CustomerResponse> query = cb.createQuery(CustomerResponse.class);
    Root<Customer> root = query.from(Customer.class);
    query.select(cb.construct(CustomerResponse.class, root.get(Customer_.name)));

    if(spec != null){
      query.where(spec.toPredicate(root, query, cb));
    }

    List<CustomerResponse> resultList = this.entityManager.createQuery(query)
        .setFirstResult((int) pageable.getOffset())
        .setMaxResults(pageable.getPageSize())
        .getResultList();

    CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
    Root<Customer> countRoot = countQuery.from(Customer.class);
    countQuery.select(cb.count(countRoot));

    if(spec != null){
      countQuery.where(spec.toPredicate(countRoot, countQuery, cb));
    }

    Long total = this.entityManager.createQuery(countQuery).getSingleResult();

    return new PageImpl<>(resultList, pageable, total);
  }



}

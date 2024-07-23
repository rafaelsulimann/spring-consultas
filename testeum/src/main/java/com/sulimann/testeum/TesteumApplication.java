package com.sulimann.testeum;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.sulimann.testeum.dtos.CustomerResponse;
import com.sulimann.testeum.entities.Customer;
import com.sulimann.testeum.repositories.CustomerRepository;
import com.sulimann.testeum.services.CustomerService;


@SpringBootApplication
public class TesteumApplication implements CommandLineRunner{

	@Autowired
	private CustomerService customerService;

	@Autowired CustomerRepository customerRepository;

	public static void main(String[] args) {
		SpringApplication.run(TesteumApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		var customer1 = new Customer("Pedro Augusto da Rocha", "Rua Pedro Carlos Hoffman", "Porto Alegre", "RS", new BigDecimal("700.00"));
    var customer2 = new Customer("Antonio Carlos Mamel", "Av. Pinheiros", "Belo Horizonte", "MG", new BigDecimal("3500.50"));
    var customer3 = new Customer("Luiza Augusta Mhor", "Rua Salto Grande", "Niteroi", "RJ", new BigDecimal("4000.00"));
    var customer4 = new Customer("Jane Ester", "Av 7 de setembro", "Erechim", "RS", new BigDecimal("800.00"));
    var customer5 = new Customer("Marcos Antônio dos Santos", "Av Farrapos", "Porto Alegre", "RS", new BigDecimal("4250.25"));
    customerRepository.saveAll(List.of(customer1, customer2, customer3, customer4, customer5));

		System.out.println("\n----- ALL DATA ------");
		Page<CustomerResponse> pageEntity = this.customerService.findAll(PageRequest.of(0, 10));
		pageEntity.forEach(System.out::println);

		System.out.println("\n----- Spring Repository -> JPQL + DTO ------");
		Page<CustomerResponse> customers = customerService.findAllCustomersByStateWithJpql("RS", PageRequest.of(0, 10));
		customers.forEach(System.out::println);

		System.out.println("\n----- Spring Repository -> Native Query + Projection ------");
		Page<CustomerResponse> customers2 = customerService.findAllCustomersByStateWithNative("RS", PageRequest.of(0, 10));
		customers2.forEach(System.out::println);

		System.out.println("\n----- EntityManager -> JPQL + DTO ------");
		Page<CustomerResponse> customers3 = customerService.findAllCustomersByStateWithJpqlUsingEntityManager("RS", PageRequest.of(0, 10));
		customers3.forEach(System.out::println);

		System.out.println("\n----- EntityManager -> Native Query + Projection ------");
		Page<CustomerResponse> customers4 = customerService.findAllCustomersByStateWithNativeUsingEntityManager("RS", PageRequest.of(0, 10));
		customers4.forEach(System.out::println);

		System.out.println("\n----- EntityManager -> Criteria Query ------");
		Page<CustomerResponse> customers5 = customerService.findAllCustomersByStateWithCriteriaQueryUsingEntityManager("RS", PageRequest.of(0, 10));
		customers5.forEach(System.out::println);

		System.out.println("\n----- EntityManager -> Native Query + SqlResultMapping ------");
		Page<CustomerResponse> customers6 = customerService.findAllCustomersByStateWithNativeQueryUsingEntityManagerAndSqlResultMapping("RS", PageRequest.of(0, 10));
		customers6.forEach(System.out::println);

	}

}

package com.sulimann.testeum.specs;

import org.springframework.data.jpa.domain.Specification;

import com.sulimann.testeum.entities.Customer;

import net.kaczmarzyk.spring.data.jpa.domain.LikeIgnoreCase;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;

@And({
    @Spec(path = "state", spec = LikeIgnoreCase.class)
})
public interface CustomerSpec extends Specification<Customer> {

}

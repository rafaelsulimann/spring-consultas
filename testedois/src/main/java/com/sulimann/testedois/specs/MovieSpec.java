package com.sulimann.testedois.specs;

import org.springframework.data.jpa.domain.Specification;

import com.sulimann.testedois.entities.Movie;

import net.kaczmarzyk.spring.data.jpa.domain.LikeIgnoreCase;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;

@And({
  @Spec(path = "name", params = "name", spec = LikeIgnoreCase.class),
  @Spec(path = "genre.description", params = "genre", spec = LikeIgnoreCase.class)
})
public interface MovieSpec extends Specification<Movie>{

}

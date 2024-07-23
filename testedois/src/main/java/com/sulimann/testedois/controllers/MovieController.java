package com.sulimann.testedois.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sulimann.testedois.dtos.MovieResponse;
import com.sulimann.testedois.services.MovieService;
import com.sulimann.testedois.specs.MovieSpec;

@RestController
@RequestMapping(value = "/movies")
public class MovieController {

  private final MovieService movieService;

  public MovieController(MovieService movieService) {
    this.movieService = movieService;
  }

  @GetMapping(value = "/spec")
  public ResponseEntity<Page<MovieResponse>> findAllMoviesByGenreWithSpecificationAndCriteriaQuery(
      MovieSpec spec, @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
    return ResponseEntity.ok(movieService.findAllMoviesByGenreWithSpecificationAndCriteriaQuery(spec, pageable));
  }
}

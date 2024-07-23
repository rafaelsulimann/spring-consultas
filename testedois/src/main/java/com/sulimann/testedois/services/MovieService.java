package com.sulimann.testedois.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.sulimann.testedois.dtos.MovieResponse;
import com.sulimann.testedois.repositories.MovieRepository;
import com.sulimann.testedois.specs.MovieSpec;

import jakarta.persistence.EntityManager;

@Service
public class MovieService {

  private final MovieRepository movieRepository;

  public MovieService(MovieRepository movieRepository, EntityManager entityManager) {
    this.movieRepository = movieRepository;
  }

  /*
   * Uma Vídeo locadora contratou seus serviços para catalogar os filmes dela.
   * Eles precisam que você selecione o código e o nome dos filmes cuja descrição
   * do gênero seja 'Action'.
   */

  // 1 - Spring Repository -> JPQL + DTO
  public Page<MovieResponse> findAllMoviesByGenreWithJpql(String genre, Pageable pageable) {
    return this.movieRepository.findAllMoviesByGenreWithJpql(genre, pageable);
  }

  // 2 - Spring Repository -> Native Query + Projection
  public Page<MovieResponse> findAllMoviesByGenreWithNative(String genre, Pageable pageable) {
    return this.movieRepository.findAllMoviesByGenreWithNative(genre, pageable);
  }

  // 3 - EntityManager -> JPQL + DTO
  public Page<MovieResponse> findAllMoviesByGenreWithJpqlUsingEntityManager(String genre, Pageable pageable) {
    return this.movieRepository.findAllMoviesByGenreWithJpqlUsingEntityManager(genre, pageable);
  }

  // 4 - EntityManager -> Native Query + DTO
  @SuppressWarnings("unchecked")
  public Page<MovieResponse> findAllMoviesByGenreWithNativeUsingEntityManager(String genre, Pageable pageable) {
    return this.movieRepository.findAllMoviesByGenreWithNativeUsingEntityManager(genre, pageable);
  }

  public Page<MovieResponse> findAll(Pageable pageable) {
    return this.movieRepository.findAll(pageable).map(MovieResponse::new);
  }

  public Page<MovieResponse> findAllMoviesByGenreWithCriteriaQueryUsingEntityManager(String genre,
      Pageable pageable) {
    return this.movieRepository.findAllMoviesByGenreWithCriteriaQueryUsingEntityManager(genre, pageable);
  }

  public Page<MovieResponse> findAllMoviesByGenreWithNativeQueryUsingEntityManagerAndSqlResultMapping(
      String genre,
      Pageable pageable) {
    return this.movieRepository.findAllMoviesByGenreWithNativeQueryUsingEntityManagerAndSqlResultMapping(genre, pageable);
  }

  public Page<MovieResponse> findAllMoviesByGenreWithSpecificationAndCriteriaQuery(MovieSpec spec, Pageable pageable) {
    return this.movieRepository.findAllMoviesByGenreWithSpecificationAndCriteriaQuery(spec, pageable);
  }

}

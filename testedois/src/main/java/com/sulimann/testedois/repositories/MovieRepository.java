package com.sulimann.testedois.repositories;

import java.util.List;

import org.hibernate.query.NativeQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.sulimann.testedois.dtos.MovieResponse;
import com.sulimann.testedois.entities.Genre_;
import com.sulimann.testedois.entities.Movie;
import com.sulimann.testedois.entities.Movie_;
import com.sulimann.testedois.specs.MovieSpec;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

@Component
public class MovieRepository {

  private final IMovieRepository repository;
  private final EntityManager entityManager;

  public MovieRepository(IMovieRepository repository, EntityManager entityManager) {
    this.repository = repository;
    this.entityManager = entityManager;
  }

  public Page<MovieResponse> findAllMoviesByGenreWithJpql(String genre, Pageable pageable) {
    return this.repository.findAllMoviesByGenreWithJpql(genre, pageable);
  }

  public Page<Movie> findAll(Pageable pageable) {
    return this.repository.findAll(pageable);
  }

  public Page<MovieResponse> findAllMoviesByGenreWithNative(String genre, Pageable pageable) {
    return this.repository.findAllMoviesByGenreWithNative(genre, pageable).map(MovieResponse::new);
  }

  @SuppressWarnings("unchecked")
  public Page<MovieResponse> findAllMoviesByGenreWithJpqlUsingEntityManager(String genre, Pageable pageable) {
    Query query = this.entityManager.createQuery("""
        SELECT new com.sulimann.testedois.dtos.MovieResponse(m.id, m.name)
        FROM Movie m
        WHERE UPPER(m.genre.description) LIKE CONCAT('%', UPPER(:genre), '%')
        """, MovieResponse.class);

    query.setParameter("genre", genre);
    query.setFirstResult((int) pageable.getOffset());
    query.setMaxResults(pageable.getPageSize());

    List<MovieResponse> movies = query.getResultList();

    Query countQuery = this.entityManager.createQuery("""
        SELECT COUNT(m)
        FROM Movie m
        WHERE UPPER(m.genre.description) LIKE CONCAT('%', UPPER(:genre), '%')
        """, Long.class);
    countQuery.setParameter("genre", genre);
    Long total = (Long) countQuery.getSingleResult();

    return new PageImpl<>(movies, pageable, total);
  }

  @SuppressWarnings("unchecked")
  public Page<MovieResponse> findAllMoviesByGenreWithNativeUsingEntityManager(String genre, Pageable pageable) {
    Query query = this.entityManager.createNativeQuery("""
        SELECT m.id, m.name
        FROM tb_movies m
        JOIN tb_genres g ON m.genre_id = g.id
        WHERE UPPER(g.description) LIKE CONCAT('%', UPPER(:genre), '%')
        """);

    query.setParameter("genre", genre);
    query.setFirstResult((int) pageable.getOffset());
    query.setMaxResults(pageable.getPageSize());
    query.unwrap(NativeQuery.class)
        .setTupleTransformer((tuple, aliases) -> {
          return new MovieResponse((Long) tuple[0], (String) tuple[1]);
        });

    List<MovieResponse> movies = query.getResultList();

    Query countQuery = this.entityManager.createNativeQuery("""
        SELECT COUNT(m.id)
        FROM tb_movies m
        JOIN tb_genres g ON m.genre_id = g.id
        WHERE UPPER(g.description) LIKE CONCAT('%', UPPER(:genre), '%')
        """);
    countQuery.setParameter("genre", genre);
    Long total = ((Number) countQuery.getSingleResult()).longValue();

    return new PageImpl<>(movies, pageable, total);
  }

  public Page<MovieResponse> findAllMoviesByGenreWithCriteriaQueryUsingEntityManager(String genre, Pageable pageable) {
    CriteriaBuilder cb = this.entityManager.getCriteriaBuilder();
    CriteriaQuery<MovieResponse> cq = cb.createQuery(MovieResponse.class);
    Root<Movie> movieRoot = cq.from(Movie.class);

    movieRoot.join(Movie_.genre);

    cq.select(cb.construct(MovieResponse.class, movieRoot.get(Movie_.id), movieRoot.get(Movie_.name)));
    cq.where(cb.like(cb.upper(movieRoot.get(Movie_.genre).get(Genre_.description)), "%" + genre.toUpperCase() + "%"));

    List<MovieResponse> movies = this.entityManager.createQuery(cq)
        .setFirstResult((int) pageable.getOffset())
        .setMaxResults(pageable.getPageSize())
        .getResultList();

    CriteriaQuery<Long> countCq = cb.createQuery(Long.class);
    Root<Movie> countRoot = countCq.from(Movie.class);
    countRoot.join(Movie_.genre);

    countCq.select(cb.count(countRoot));
    countCq.where(cb.like(cb.upper(countRoot.get(Movie_.genre).get(Genre_.description)), "%" + genre.toUpperCase() + "%"));
    Long total = this.entityManager.createQuery(countCq).getSingleResult();

    return new PageImpl<>(movies, pageable, total);
  }

  @SuppressWarnings("unchecked")
  public Page<MovieResponse> findAllMoviesByGenreWithNativeQueryUsingEntityManagerAndSqlResultMapping(String genre,
      Pageable pageable) {
    Query query = this.entityManager.createNativeQuery("""
        SELECT m.id, m.name
        FROM tb_movies m
        JOIN tb_genres g ON m.genre_id = g.id
        WHERE UPPER(g.description) LIKE CONCAT('%', UPPER(:genre), '%')
        """, "MovieResponseMapping");
    query.setParameter("genre", genre);
    query.setFirstResult((int) pageable.getOffset());
    query.setMaxResults(pageable.getPageSize());

    List<MovieResponse> movies = query.getResultList();

    Query countQuery = this.entityManager.createNativeQuery("""
        SELECT COUNT(m.id)
        FROM tb_movies m
        JOIN tb_genres g ON m.genre_id = g.id
        WHERE UPPER(g.description) LIKE CONCAT('%', UPPER(:genre), '%')
        """, Long.class);
    countQuery.setParameter("genre", genre);
    Long total = ((Number) countQuery.getSingleResult()).longValue();

    return new PageImpl<>(movies, pageable, total);
  }

  public Page<MovieResponse> findAllMoviesByGenreWithSpecificationAndCriteriaQuery(MovieSpec spec, Pageable pageable) {
    CriteriaBuilder cb = this.entityManager.getCriteriaBuilder();
    CriteriaQuery<MovieResponse> cq = cb.createQuery(MovieResponse.class);
    Root<Movie> movieRoot = cq.from(Movie.class);

    movieRoot.join(Movie_.genre);

    cq.select(cb.construct(MovieResponse.class, movieRoot.get(Movie_.id), movieRoot.get(Movie_.name)));
    if(spec != null){
      cq.where(spec.toPredicate(movieRoot, cq, cb));
    }

    List<MovieResponse> movies = this.entityManager.createQuery(cq)
        .setFirstResult((int) pageable.getOffset())
        .setMaxResults(pageable.getPageSize())
        .getResultList();

    CriteriaQuery<Long> countCq = cb.createQuery(Long.class);
    Root<Movie> countRoot = countCq.from(Movie.class);
    countRoot.join(Movie_.genre);

    countCq.select(cb.count(countRoot));
    if(spec != null){
      countCq.where(spec.toPredicate(countRoot, countCq, cb));
    }
    Long total = this.entityManager.createQuery(countCq).getSingleResult();

    return new PageImpl<>(movies, pageable, total);
  }

}

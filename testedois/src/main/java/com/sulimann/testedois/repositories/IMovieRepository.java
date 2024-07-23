package com.sulimann.testedois.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sulimann.testedois.dtos.MovieResponse;
import com.sulimann.testedois.entities.Movie;
import com.sulimann.testedois.projections.MovieResponseProjection;

public interface IMovieRepository extends JpaRepository<Movie, Long> {

  @Query("""
      SELECT new com.sulimann.testedois.dtos.MovieResponse(m.id, m.name)
      FROM Movie m
      WHERE UPPER(m.genre.description) LIKE CONCAT('%', UPPER(:genre), '%')
      """)
  Page<MovieResponse> findAllMoviesByGenreWithJpql(@Param("genre") String genre, Pageable pageable);

  @Query(nativeQuery = true, value = """
      SELECT m.id, m.name
      FROM tb_movies m
      JOIN tb_genres g ON m.genre_id = g.id
      WHERE UPPER(g.description) LIKE CONCAT('%', UPPER(:genre), '%')
      """)
  Page<MovieResponseProjection> findAllMoviesByGenreWithNative(@Param("genre") String genre, Pageable pageable);

}

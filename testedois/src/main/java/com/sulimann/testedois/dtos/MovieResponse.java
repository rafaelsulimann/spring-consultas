package com.sulimann.testedois.dtos;

import com.sulimann.testedois.entities.Movie;
import com.sulimann.testedois.projections.MovieResponseProjection;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieResponse {

  private Long id;
  private String name;

  public MovieResponse(Movie entity){
    id = entity.getId();
    name = entity.getName();
  }

  public MovieResponse(MovieResponseProjection projection){
    id = projection.getId();
    name = projection.getName();
  }

}

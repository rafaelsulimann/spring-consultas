package com.sulimann.testedois.entities;

import com.sulimann.testedois.dtos.MovieResponse;

import jakarta.persistence.ColumnResult;
import jakarta.persistence.ConstructorResult;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SqlResultSetMapping;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_movies")
@Getter
@NoArgsConstructor
@SqlResultSetMapping(name = "MovieResponseMapping", classes = {
    @ConstructorResult(targetClass = MovieResponse.class, columns = {
        @ColumnResult(name = "id", type = Long.class),
        @ColumnResult(name = "name", type = String.class)
    })
})
public class Movie {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;

  @ManyToOne
  @JoinColumn(name = "genre_id")
  private Genre genre;

  public Movie(String name, Genre genre) {
    this.name = name;
    this.genre = genre;
  }

  @Override
  public String toString() {
    return "Movie [id=" + id + ", name=" + name + ", genre=" + genre + "]";
  }

}

package com.sulimann.testedois.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_genres")
@Getter
@NoArgsConstructor
public class Genre {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String description;

  public Genre(String description) {
    this.description = description;
  }

  @Override
  public String toString() {
    return "Genre [id=" + id + ", description=" + description + "]";
  }

}

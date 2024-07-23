package com.sulimann.testedois.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sulimann.testedois.entities.Genre;

public interface IGenreRepository extends JpaRepository<Genre, Long> {

}

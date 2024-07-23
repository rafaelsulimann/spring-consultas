package com.sulimann.testedois;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.sulimann.testedois.dtos.MovieResponse;
import com.sulimann.testedois.entities.Genre;
import com.sulimann.testedois.entities.Movie;
import com.sulimann.testedois.repositories.IGenreRepository;
import com.sulimann.testedois.repositories.IMovieRepository;
import com.sulimann.testedois.services.MovieService;

@SpringBootApplication
public class TestedoisApplication implements CommandLineRunner {

	@Autowired
	private IGenreRepository genreRepository;

	@Autowired
	private IMovieRepository movieRepository;

	@Autowired
	private MovieService movieService;

	public static void main(String[] args) {
		SpringApplication.run(TestedoisApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		var genre1 = genreRepository.save(new Genre("Action"));
		var genre2 = genreRepository.save(new Genre("Comedy"));
		var genre3 = genreRepository.save(new Genre("Drama"));
		var genre4 = genreRepository.save(new Genre("Horror"));
		var genre5 = genreRepository.save(new Genre("Romance"));
		var genre6 = genreRepository.save(new Genre("Thriller"));
		this.genreRepository.saveAll(List.of(genre1, genre2, genre3, genre4, genre5, genre6));

		var movie1 = movieRepository.save(new Movie("The Dark Knight", genre1));
		var movie2 = movieRepository.save(new Movie("The Hangover", genre2));
		var movie3 = movieRepository.save(new Movie("The Shawshank Redemption", genre3));
		var movie4 = movieRepository.save(new Movie("The Exorcist", genre4));
		var movie5 = movieRepository.save(new Movie("Titanic", genre5));
		var movie6 = movieRepository.save(new Movie("Se7en", genre6));
		var movie7 = movieRepository.save(new Movie("The Godfather", genre3));
		var movie8 = movieRepository.save(new Movie("The Shining", genre4));
		var movie9 = movieRepository.save(new Movie("The Silence of the Lambs", genre6));
		var movie10 = movieRepository.save(new Movie("The Notebook", genre5));
		var movie11 = movieRepository.save(new Movie("The Matrix", genre1));
		var movie12 = movieRepository.save(new Movie("The Hangover Part II", genre2));
		var movie13 = movieRepository.save(new Movie("The Dark Knight Rises", genre1));
		var movie14 = movieRepository.save(new Movie("The Godfather Part II", genre3));
		this.movieRepository.saveAll(List.of(movie1, movie2, movie3, movie4, movie5, movie6, movie7, movie8, movie9, movie10, movie11, movie12, movie13, movie14));

		System.out.println("----- ALL DATA -----");
		this.genreRepository.findAll().forEach(System.out::println);
		this.movieRepository.findAll().forEach(System.out::println);

		System.out.println("\n----- Spring Repository -> JPQL + DTO ------");
		Page<MovieResponse> movies = movieService.findAllMoviesByGenreWithJpql("Action", PageRequest.of(0, 10));
		movies.forEach(System.out::println);

		System.out.println("\n----- Spring Repository -> Native Query + Projection ------");
		Page<MovieResponse> movies2 = movieService.findAllMoviesByGenreWithNative("Action", PageRequest.of(0, 10));
		movies2.forEach(System.out::println);

		System.out.println("\n----- EntityManager -> JPQL + DTO ------");
		Page<MovieResponse> movies3 = movieService.findAllMoviesByGenreWithJpqlUsingEntityManager("ACTION", PageRequest.of(0, 10));
		movies3.forEach(System.out::println);

		System.out.println("\n----- EntityManager -> Native Query + Projection ------");
		Page<MovieResponse> movies4 = movieService.findAllMoviesByGenreWithNativeUsingEntityManager("ACTION", PageRequest.of(0, 10));
		movies4.forEach(System.out::println);

		System.out.println("\n----- EntityManager -> Criteria Query ------");
		Page<MovieResponse> movies5 = movieService.findAllMoviesByGenreWithCriteriaQueryUsingEntityManager("ACTION", PageRequest.of(0, 10));
		movies5.forEach(System.out::println);

		System.out.println("\n----- EntityManager -> Native Query + SqlResultMapping ------");
		Page<MovieResponse> movies6 = movieService.findAllMoviesByGenreWithNativeQueryUsingEntityManagerAndSqlResultMapping("ACTION", PageRequest.of(0, 10));
		movies6.forEach(System.out::println);
	}

}

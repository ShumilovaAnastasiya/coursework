package com.example.backendcinema.services;

import com.example.backendcinema.pojoes.Movie;
import com.example.backendcinema.repositories.MovieRepository;
import com.example.backendcinema.utils.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Service
@Transactional
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

    public List<Movie> findAll(String sortBy, String sortValue, String filterBy, String filterValue) {
        List<Movie> movies = movieRepository.findAll();
        Utility.filterAndSort(movies, sortBy, sortValue, filterBy, filterValue);
        return movies;
    }

    public ResponseEntity save(Movie movie) {
        if (Utility.atLeastOneNullField(movie)) return new ResponseEntity("Укажите все параметры (budget, title, duration, publishDate, price)", HttpStatus.BAD_REQUEST);
        if (existsByTitle(movie.getTitle())) return new ResponseEntity("Такой фильм уже есть в БД", HttpStatus.BAD_REQUEST);
        movieRepository.save(movie);

        return new ResponseEntity("Фильм добавлен в базу данных", HttpStatus.OK);
    }

    public ResponseEntity delete(Movie movie) {
        int c = 0;
        if (movie.getId() != null) {
            c = movieRepository.deleteById(movie.getId().intValue());
        } else if (movie.getTitle() != null) {
            c = movieRepository.deleteByTitle(movie.getTitle());
        } else if (movie.getId() == null && movie.getTitle() == null) {
            return new ResponseEntity("Укажите либо id, либо title", HttpStatus.BAD_REQUEST);
        }

        if (c == 0) return new ResponseEntity("Такого фильма нет", HttpStatus.BAD_REQUEST);
        return new ResponseEntity("Фильм удалён", HttpStatus.OK);
    }

    public ResponseEntity update(Movie movie) {
        if (movie.getId() == null) return new ResponseEntity("Укажите id фильма", HttpStatus.BAD_REQUEST);
        if (Utility.allFieldsAreNull(movie)) return new ResponseEntity("Укажите хотя бы один параметр (title, budget, duration, price, publishDate)", HttpStatus.BAD_REQUEST);

        int c = movieRepository.update(movie.getId(), movie.getBudget(), movie.getPrice(), movie.getPublishDate(), movie.getTitle(), movie.getDuration());
        if (c == 0) return new ResponseEntity("Такого фильма нет", HttpStatus.BAD_REQUEST);
        return new ResponseEntity("Данные фильма изменены", HttpStatus.OK);
    }

    public boolean existsByTitle(String movieTitle) {
        Movie movie = movieRepository.findByTitle(movieTitle);
        return movie != null;
    }

    public Movie findByTitle(String movieTitle) {
        return movieRepository.findByTitle(movieTitle);
    }

    public Map<String, Object> getStat() {
        Map<String, Object> result = new TreeMap<>();
        List<Movie> movies = findAll(null, null, null, null);

        result.put("labels", movies.stream().map(f -> f.getTitle()).collect(Collectors.toSet()));
        result.put("type", "bar");
        result.put("datasets", List.of(
                Map.of("label", "Бюджет фильма", "data", getPricesForFilms(movies))
        ));

        return result;
    }

    private Map<String, Integer> getPricesForFilms(List<Movie> movies) {
        Map<String, Integer> result = new HashMap<>();
        for (Movie movie: movies) {
            result.put( movie.getTitle(), movie.getBudget() );
        }

        return result;
    }
}

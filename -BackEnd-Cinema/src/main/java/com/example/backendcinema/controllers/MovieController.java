package com.example.backendcinema.controllers;

import com.example.backendcinema.pojoes.Movie;
import com.example.backendcinema.services.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/movie")
@CrossOrigin
public class MovieController {

    @Autowired
    private MovieService movieService;

    @GetMapping
    public List<Movie> findAll(@RequestParam(value = "sortBy", required = false) String sortBy, @RequestParam(value = "sortValue", required = false) String sortValue,
                               @RequestParam(value = "filterBy", required = false) String filterBy, @RequestParam(value = "filterValue", required = false) String filterValue) {
        return movieService.findAll(sortBy, sortValue, filterBy, filterValue);
    }

    @PostMapping
    public ResponseEntity save(Movie movie) {
        return movieService.save(movie);
    }

    @DeleteMapping
    public ResponseEntity delete(Movie movie) {
        return movieService.delete(movie);
    }

    @PutMapping
    public ResponseEntity update(Movie movie) {
        return movieService.update(movie);
    }

    @GetMapping("stat")
    public Map<String, Object> statistic() {
        return movieService.getStat();
    }

}

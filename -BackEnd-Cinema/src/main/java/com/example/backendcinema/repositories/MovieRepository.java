package com.example.backendcinema.repositories;

import com.example.backendcinema.pojoes.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Date;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Integer> {

    @Query(value = "DELETE FROM Movie WHERE id = ?1")
    @Modifying
    int deleteById(int id);

    int deleteByTitle(String title);

    @Query(nativeQuery = true, value = "UPDATE movie\n" +
            "    SET budget = ifnull(:budget, budget),\n" +
            "        title = ifnull(:title, title),\n" +
            "        duration = ifnull(:duration, duration),\n" +
            "        publish_date = ifnull(:publishDate, publish_date),\n" +
            "        price = ifnull(:price, price)\n" +
            "WHERE id = :id")
    @Modifying
    int update(Integer id, Integer budget, Integer price, Date publishDate, String title, Integer duration);

    Movie findByTitle(String title);
}

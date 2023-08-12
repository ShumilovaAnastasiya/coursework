package com.example.backendcinema.pojoes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "movie")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String title;
    private Integer budget;
    private Integer duration;
    private Date publishDate;
    private Integer price;

    public Movie(String title, Integer budget, Integer duration, Date publishDate, Integer price) {
        this.title = title;
        this.budget = budget;
        this.duration = duration;
        this.publishDate = publishDate;
        this.price = price;
    }
}

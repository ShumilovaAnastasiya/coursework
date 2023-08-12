package com.example.backendcinema.pojoes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "viewer")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Viewer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private Date birthDate;

    public Viewer(String name, Date birthDate) {
        this.name = name;
        this.birthDate = birthDate;
    }

    public Integer getBirthYear() {
        return getBirthDate().toLocalDate().getYear();
    }
}

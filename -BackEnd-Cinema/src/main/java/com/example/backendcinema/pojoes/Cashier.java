package com.example.backendcinema.pojoes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Date;
import java.time.LocalDate;

@Entity
@Table(name = "cashier")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Cashier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private Date birthDate;

    public Cashier(String name, Date birthDate) {
        this.name = name;
        this.birthDate = birthDate;
    }

    public Integer getBirthYear() {
        return getBirthDate().toLocalDate().getYear();
    }
}

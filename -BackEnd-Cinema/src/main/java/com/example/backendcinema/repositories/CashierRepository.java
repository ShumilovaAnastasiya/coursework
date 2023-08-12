package com.example.backendcinema.repositories;

import com.example.backendcinema.pojoes.Cashier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Date;

@Repository
public interface CashierRepository extends JpaRepository<Cashier, Integer> {
    Cashier findByName(String name);

    @Query(value = "DELETE FROM Cashier WHERE id = ?1")
    @Modifying
    int deleteById(int id);

    int deleteByName(String name);


    @Query(nativeQuery = true, value = "UPDATE cashier\n" +
            "SET name = ifnull(:name, name),\n" +
            "    birth_date = ifnull(:birthDate, birth_date)\n" +
            "WHERE id = :id")
    @Modifying
    int update(Integer id, String name, Date birthDate);
}

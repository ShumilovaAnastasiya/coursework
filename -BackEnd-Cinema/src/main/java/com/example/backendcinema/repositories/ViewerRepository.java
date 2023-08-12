package com.example.backendcinema.repositories;

import com.example.backendcinema.pojoes.Viewer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Date;

@Repository
public interface ViewerRepository extends JpaRepository<Viewer, Integer> {

    @Query("DELETE FROM Viewer WHERE id = ?1")
    @Modifying
    int deleteById(int id);

    int deleteByName(String name);

    @Query(nativeQuery = true, value = "UPDATE viewer\n" +
            "    SET name = ifnull(:name, name),\n" +
            "    birth_date = ifnull(:birthDate, birth_date)\n" +
            "WHERE id = :id")
    @Modifying
    int update(Integer id, String name, Date birthDate);

    Viewer findByName(String name);
}

package com.example.backendcinema.repositories;

import com.example.backendcinema.pojoes.Cashier;
import com.example.backendcinema.pojoes.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;
import java.util.Map;

@Repository
public interface SaleRepository extends JpaRepository<Cashier, Integer> {

    @Query(nativeQuery = true, value = "SELECT\n" +
            "    sale.date AS \"date\",\n" +
            "    c.name AS \"cashierName\",\n" +
            "    v.name AS \"viewerName\",\n" +
            "    m.title AS \"movieTitle\",\n" +
            "    m.price AS \"price\",\n" +
            "    sale.id AS \"id\"\n" +
            "FROM sale JOIN cashier c on sale.cashier_id = c.id\n" +
            "JOIN movie m on m.id = sale.movie_id\n" +
            "JOIN viewer v on v.id = sale.viewer_id")
    List<Map<String, Object>> findAllSales();

    @Query(nativeQuery = true, value = "INSERT INTO sale(cashier_id, viewer_id, movie_id, date) VALUES (:cashierId, :viewerId, :movieId, :date)")
    @Modifying
    void save(Integer cashierId, Integer viewerId, Integer movieId, Date date);

    @Query(nativeQuery = true, value = "SELECT * FROM sale where viewer_id = :viewerId AND movie_id = :movieId AND date = :date")
    boolean recordAlreadyExists(Integer viewerId, Integer movieId, Date date);

    @Query(nativeQuery = true, value = "SELECT\n" +
            "    sale.date AS \"date\",\n" +
            "    c.name AS \"cashierName\",\n" +
            "    v.name AS \"viewerName\",\n" +
            "    m.title AS \"movieTitle\",\n" +
            "    m.price AS \"price\",\n" +
            "    sale.id AS \"id\"\n" +
            "FROM sale JOIN cashier c on sale.cashier_id = c.id\n" +
            "JOIN movie m on m.id = sale.movie_id\n" +
            "JOIN viewer v on v.id = sale.viewer_id WHERE viewer_id = :viewerId AND movie_id = :movieId AND date = :date")
    List<Map<String, Object>> findSaleByRecord(Integer viewerId, Integer movieId, Date date);

    @Query(nativeQuery = true, value = "DELETE FROM sale WHERE id = :id")
    @Modifying
    int deleteById(int id);
}

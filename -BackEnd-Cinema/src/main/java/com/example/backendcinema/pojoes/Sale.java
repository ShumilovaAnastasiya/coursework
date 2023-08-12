package com.example.backendcinema.pojoes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Sale {
    private Integer id;
    private String cashierName;
    private String viewerName;
    private String movieTitle;
    private Date date;
    private Integer price;

    public Sale(String cashierName, String viewerName, String movieTitle, Date date, Integer price) {
        this.cashierName = cashierName;
        this.viewerName = viewerName;
        this.movieTitle = movieTitle;
        this.date = date;
        this.price = price;
    }

    public static Sale getSale(Map<String, Object> sale) {
        return new Sale(
            Integer.parseInt(sale.get("id").toString()),
            (String) sale.get("cashierName"),
            (String) sale.get("viewerName"),
            (String) sale.get("movieTitle"),
            (Date) sale.get("date"),
            (Integer) sale.get("price")
        );
    }

    public Integer getYear() {
        return getDate().toLocalDate().getYear();
    }
}

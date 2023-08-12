package com.example.backendcinema.services;

import com.example.backendcinema.pojoes.Cashier;
import com.example.backendcinema.pojoes.Movie;
import com.example.backendcinema.pojoes.Sale;
import com.example.backendcinema.pojoes.Viewer;
import com.example.backendcinema.repositories.SaleRepository;
import com.example.backendcinema.utils.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class SaleService {

    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private ViewerService viewerService;

    @Autowired
    private CashierService cashierService;

    @Autowired
    private MovieService movieService;

    public List<Sale> findAll(String sortBy, String sortValue, String filterBy, String filterValue) {
        List<Sale> sales = new ArrayList<>(saleRepository.findAllSales().stream().map(Sale::getSale).toList());
        Utility.filterAndSort(sales, sortBy, sortValue, filterBy, filterValue);
        return sales;
    }

    public ResponseEntity save(Sale sale) {
        if (Utility.atLeastOneNullField(sale)) return new ResponseEntity("Укажите все параметры (cashierName, viewerName, movieTitle, date, price)", HttpStatus.BAD_REQUEST);

        Cashier cashier = cashierService.findByName(sale.getCashierName());
        Movie movie = movieService.findByTitle(sale.getMovieTitle());
        Viewer viewer = viewerService.findByName(sale.getViewerName());
        if (cashier == null) return new ResponseEntity("Такой кассир не существует", HttpStatus.BAD_REQUEST);
        if (movie == null) return new ResponseEntity("Такого фильма нет в сеансах", HttpStatus.BAD_REQUEST);
        if (viewer == null) return new ResponseEntity("Такого зрителя не существует", HttpStatus.BAD_REQUEST);

        if (recordAlreadyExists(viewer.getId(), movie.getId(), sale.getDate())) {
            return new ResponseEntity("На эту дату уже куплены билеты для '"+viewer.getName()+"'", HttpStatus.BAD_REQUEST);
        }
        saleRepository.save(cashier.getId(), viewer.getId(), movie.getId(), sale.getDate());
        return new ResponseEntity("Место на сеанс куплено", HttpStatus.OK);
    }

    private boolean recordAlreadyExists(Integer viewerId, Integer movieId, Date date) {
        List<Map<String, Object>> sales = saleRepository.findSaleByRecord(viewerId, movieId, date);
        return sales.size() > 0;
    }

    public ResponseEntity delete(Integer id) {
        if (id == null) return new ResponseEntity("Укажите ID записи на сеанс для удаления", HttpStatus.BAD_REQUEST);
        int c = saleRepository.deleteById(id.intValue());
        if (c == 0) return new ResponseEntity("Такой записи на сеанс нет", HttpStatus.BAD_REQUEST);
        return new ResponseEntity("Запись на сеанс удалена", HttpStatus.OK);
    }

    public Map<String, Object> getStat() {
        Map<String, Object> result = new TreeMap<>();
        List<Sale> sales = findAll(null, null, null, null);
        result.put("labels", new TreeSet<>(sales.stream().map(s -> s.getDate()).collect(Collectors.toSet()))
                );
        result.put("type", "line");
        result.put("datasets", List.of(
                Map.of("label", "Общая выручка в этот день", "data", getTotalCost(sales))
        ));

        return result;
    }

    private Map<String, Integer> getTotalCost(List<Sale> sales) {
        Map<String, Integer> result = new TreeMap<>();
        for (Sale sale: sales) {
            result.put( sale.getDate().toString(), result.getOrDefault( sale.getDate().toString(), 0 )+sale.getPrice() );
        }

        return result;
    }
}

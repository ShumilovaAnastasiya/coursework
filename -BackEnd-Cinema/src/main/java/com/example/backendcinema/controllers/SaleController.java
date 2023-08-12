package com.example.backendcinema.controllers;

import com.example.backendcinema.pojoes.Sale;
import com.example.backendcinema.services.SaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/sale")
@CrossOrigin
public class SaleController {

    @Autowired
    private SaleService saleService;

    @GetMapping
    public List<Sale> findAll(@RequestParam(value = "sortBy", required = false) String sortBy, @RequestParam(value = "sortValue", required = false) String sortValue,
                              @RequestParam(value = "filterBy", required = false) String filterBy, @RequestParam(value = "filterValue", required = false) String filterValue) {
        return saleService.findAll(sortBy, sortValue, filterBy, filterValue);
    }

    @PostMapping
    public ResponseEntity save(Sale sale) {
        return saleService.save(sale);
    }

    @DeleteMapping
    public ResponseEntity delete(Integer id) {
        return saleService.delete(id);
    }

    @GetMapping("stat")
    public Map<String, Object> statistic() {
        return saleService.getStat();
    }

}

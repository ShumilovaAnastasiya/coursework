package com.example.backendcinema.controllers;

import com.example.backendcinema.pojoes.Cashier;
import com.example.backendcinema.services.CashierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/cashier")
@CrossOrigin
public class CashierController {

    @Autowired
    private CashierService cashierService;

    @GetMapping
    public List<Cashier> findAll(@RequestParam(value = "sortBy", required = false) String sortBy, @RequestParam(value = "sortValue", required = false) String sortValue,
                                 @RequestParam(value = "filterBy", required = false) String filterBy, @RequestParam(value = "filterValue", required = false) String filterValue) {
        return cashierService.findAll(sortBy, sortValue, filterBy, filterValue);
    }

    @PostMapping
    public ResponseEntity save(Cashier cashier) {
        return cashierService.save(cashier);
    }

    @DeleteMapping
    public ResponseEntity delete(@RequestParam(value = "id", required = false) Integer id, @RequestParam(value = "name", required = false) String name) {
        return cashierService.delete(id, name);
    }

    @PutMapping
    public ResponseEntity update(Cashier cashier) {
        return cashierService.update(cashier);
    }

    @GetMapping("stat")
    public Map<String, Object> statistic() {
        return cashierService.getStat();
    }

}

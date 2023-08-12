package com.example.backendcinema.services;

import com.example.backendcinema.pojoes.Cashier;
import com.example.backendcinema.repositories.CashierRepository;
import com.example.backendcinema.utils.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class CashierService {

    @Autowired
    private CashierRepository cashierRepository;

    public List<Cashier> findAll(String sortBy, String sortValue, String filterBy, String filterValue) {
        List<Cashier> cashiers = cashierRepository.findAll();
        Utility.filterAndSort(cashiers, sortBy, sortValue, filterBy, filterValue);
        return cashiers;
    }

    public ResponseEntity save(Cashier cashier) {
        if (Utility.atLeastOneNullField(cashier)) {
            return new ResponseEntity("Укажите все параметры (name, birthDate)", HttpStatus.BAD_REQUEST);
        } else if (existsByName(cashier.getName())) {
            return new ResponseEntity("Такой кассир уже есть", HttpStatus.BAD_REQUEST);
        }

        cashierRepository.save(cashier);
        return new ResponseEntity("Кассир добавлен в базу данных", HttpStatus.OK);
    }

    public boolean existsByName(String name) {
        Cashier cashier = cashierRepository.findByName(name);
        return cashier != null;
    }

    public Cashier findByName(String name) {
        return cashierRepository.findByName(name);
    }

    public ResponseEntity delete(Integer id, String name) {
        int c = 0;
        if (id != null) {
            c = cashierRepository.deleteById(id.intValue());
        } else if (name != null) {
            c = cashierRepository.deleteByName(name);
        } else if (id == null && name == null) return new ResponseEntity("Укажите либо id, либо name", HttpStatus.BAD_REQUEST);

        if (c == 0) return new ResponseEntity("Такого кассира нет", HttpStatus.BAD_REQUEST);
        return new ResponseEntity("Кассир удалён", HttpStatus.OK);
    }

    public ResponseEntity update(Cashier cashier) {
        if (cashier.getId() == null) return new ResponseEntity("Укажите ID кассира", HttpStatus.BAD_REQUEST);
        if (cashier.getName() == null && cashier.getBirthDate() == null) return new ResponseEntity("Укажите хотя бы один параметр (name, birthDate)", HttpStatus.BAD_REQUEST);

        if (cashier.getName() != null) {
            Cashier anotherCashier = cashierRepository.findByName(cashier.getName());
            if (anotherCashier != null && !cashier.getId().equals(anotherCashier.getId())) {
                return new ResponseEntity("Такой кассир уже есть", HttpStatus.BAD_REQUEST);
            }
        }
        int c = cashierRepository.update(cashier.getId(), cashier.getName(), cashier.getBirthDate());
        if (c == 0) return new ResponseEntity("Такого кассира нет", HttpStatus.BAD_REQUEST);
        return new ResponseEntity("Данные кассира изменены", HttpStatus.OK);
    }

    public Map<String, Object> getStat() {
        Map<String, Object> result = new HashMap<>();

        List<Cashier> cashiers = findAll(null, null, null, null);
        result.put("type", "bar"); result.put("labels", new TreeSet<>(
                cashiers.stream()
                        .map(c -> c.getBirthDate().toLocalDate().getYear()+"").collect(Collectors.toSet())
        ));

        result.put("datasets", List.of(
                Map.of("label", "Кассиров, рождённых в этот год", "data", getCashierYears(cashiers))
        ));

        return result;
    }

    private Map<Integer, Integer> getCashierYears(List<Cashier> cashiers) {
        Map<Integer, Integer> result = new TreeMap<>();
        for (Cashier cashier: cashiers) {
            result.put( cashier.getBirthYear(), result.getOrDefault( cashier.getBirthYear(), 0 )+1 );
        }

        return result;
    }
}

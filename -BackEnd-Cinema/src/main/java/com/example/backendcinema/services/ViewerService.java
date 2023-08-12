package com.example.backendcinema.services;

import com.example.backendcinema.pojoes.Cashier;
import com.example.backendcinema.pojoes.Viewer;
import com.example.backendcinema.repositories.ViewerRepository;
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
public class ViewerService {

    @Autowired
    private ViewerRepository viewerRepository;

    public List<Viewer> findAll(String sortBy, String sortValue, String filterBy, String filterValue) {
        List<Viewer> viewers = viewerRepository.findAll();
        Utility.filterAndSort(viewers, sortBy, sortValue, filterBy, filterValue);
        return viewers;
    }

    public ResponseEntity save(Viewer viewer) {
        if (Utility.atLeastOneNullField(viewer)) return new ResponseEntity("Укажите все параметры (name, birthDate)", HttpStatus.BAD_REQUEST);
        viewerRepository.save(viewer);

        return new ResponseEntity("Зритель добавлен в базу данных", HttpStatus.OK);
    }

    public ResponseEntity delete(Viewer viewer) {
        int c = 0;
        if (viewer.getId() != null) {
            c = viewerRepository.deleteById(viewer.getId().intValue());
        } else if (viewer.getName() != null) {
            c = viewerRepository.deleteByName(viewer.getName());
        } else if (viewer.getId() == null && viewer.getName() == null) {
            return new ResponseEntity("Укажите либо id, либо name", HttpStatus.BAD_REQUEST);
        }

        if (c == 0) return new ResponseEntity("Такого зрителя нет", HttpStatus.BAD_REQUEST);
        return new ResponseEntity("Зритель удалён", HttpStatus.OK);
    }

    public ResponseEntity update(Viewer viewer) {
        if (viewer.getId() == null) return new ResponseEntity("Укажите ID зрителя", HttpStatus.BAD_REQUEST);
        if (Utility.allFieldsAreNull(viewer)) return new ResponseEntity("Укажите хотя бы один параметр (name, birthDate)", HttpStatus.BAD_REQUEST);

        int c = viewerRepository.update(viewer.getId(), viewer.getName(), viewer.getBirthDate());
        if (c == 0) return new ResponseEntity("Такого зрителя нет", HttpStatus.BAD_REQUEST);
        return new ResponseEntity("Данные зрителя изменены", HttpStatus.OK);
    }

    public boolean existsByName(String name) {
        Viewer viewer = viewerRepository.findByName(name);
        return viewer != null;
    }

    public Viewer findByName(String viewerName) {
        return viewerRepository.findByName(viewerName);
    }

    public Map<String, Object> getStat() {
        Map<String, Object> result = new HashMap<>();

        List<Viewer> viewers = findAll(null, null, null, null);
        result.put("type", "bar"); result.put("labels", new TreeSet<>(
                viewers.stream()
                        .map(c -> c.getBirthDate().toLocalDate().getYear()+"").collect(Collectors.toSet())
        ));

        result.put("datasets", List.of(
                Map.of("label", "Зрителей, рождённых в этот год", "data", getViewerYears(viewers))
        ));

        return result;
    }

    private Map<Integer, Integer> getViewerYears(List<Viewer> viewers) {
        Map<Integer, Integer> result = new TreeMap<>();
        for (Viewer viewer: viewers) {
            result.put( viewer.getBirthYear(), result.getOrDefault( viewer.getBirthYear(), 0 )+1 );
        }

        return result;
    }
}

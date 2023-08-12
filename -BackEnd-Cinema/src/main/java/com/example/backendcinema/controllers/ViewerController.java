package com.example.backendcinema.controllers;

import com.example.backendcinema.pojoes.Viewer;
import com.example.backendcinema.repositories.ViewerRepository;
import com.example.backendcinema.services.ViewerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/viewer")
@CrossOrigin
public class ViewerController {

    @Autowired
    private ViewerService viewerService;

    @GetMapping
    public List<Viewer> findAll(@RequestParam(value = "sortBy", required = false) String sortBy, @RequestParam(value = "sortValue", required = false) String sortValue,
                                @RequestParam(value = "filterBy", required = false) String filterBy, @RequestParam(value = "filterValue", required = false) String filterValue) {
        return viewerService.findAll(sortBy, sortValue, filterBy, filterValue);
    }

    @PostMapping
    public ResponseEntity save(Viewer viewer) {
        return viewerService.save(viewer);
    }

    @DeleteMapping
    public ResponseEntity delete(Viewer viewer) {
        return viewerService.delete(viewer);
    }

    @PutMapping
    public ResponseEntity update(Viewer viewer) {
        return viewerService.update(viewer);
    }

    @GetMapping("stat")
    public Map<String, Object> statistic() {
        return viewerService.getStat();
    }

}

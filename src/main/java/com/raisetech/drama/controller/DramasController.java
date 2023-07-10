package com.raisetech.drama.controller;

import com.raisetech.drama.dto.DramaDto;
import com.raisetech.drama.entity.Drama;
import com.raisetech.drama.form.InsertForm;
import com.raisetech.drama.service.DramasService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.http.ResponseEntity;


import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/dramas")
public class DramasController {
    private final DramasService dramasService;

    public DramasController(DramasService dramasService){
        this.dramasService = dramasService;
    }

//    @GetMapping
    /*public List<Drama> getDramas(@RequestParam(value = "priority", required = false) String priority)
            throws Exception {
        return dramasService.getDramas(priority);
    }*/

    @GetMapping
    public ResponseEntity<List<Drama>> getDramas(@RequestParam(value = "priority", required = false) String priority) {
        if (priority != null && !isValidPriority(priority)) {
            return ResponseEntity.badRequest().build();
        }
        List<Drama> dramas;
        if (priority != null) {
            dramas = dramasService.getDramas(priority);
            if (dramas.isEmpty()) {
                return ResponseEntity.ok().body(Collections.emptyList());
            }
        } else {
            dramas = dramasService.getAllDramas();
        }
        return ResponseEntity.ok(dramas);
    }

    private boolean isValidPriority(String priority) {
        List<String> validPriorities = new ArrayList<>();
        validPriorities.add("A");
        validPriorities.add("B");
        validPriorities.add("C");
        return validPriorities.contains(priority);
    }

    @PostMapping
    public ResponseEntity create(@Validated @RequestBody InsertForm insertForm) {
        DramaDto dramaDto = new DramaDto(
                insertForm.getTitle(),
                insertForm.getYear(),
                insertForm.getPriority());
        int newDramaId = dramasService.save(dramaDto);
        URI location = UriComponentsBuilder.fromUriString("http://localohost:8080/create/" + newDramaId)
                .build().toUri();
        return ResponseEntity.created(location).build();
    }
}

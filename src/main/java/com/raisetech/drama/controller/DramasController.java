package com.raisetech.drama.controller;

import com.raisetech.drama.dto.DramaDto;
import com.raisetech.drama.entity.Drama;
import com.raisetech.drama.form.InsertForm;
import com.raisetech.drama.form.UpdateForm;
import com.raisetech.drama.service.DramasService;
import com.raisetech.drama.validation.PriorityValidation;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import java.net.URI;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/dramas")
@Validated
public class DramasController {
    private final DramasService dramasService;

    public DramasController(DramasService dramasService) {
        this.dramasService = dramasService;
    }

    @GetMapping
    public ResponseEntity<List<Drama>> getDramas(
        @Valid @PriorityValidation @RequestParam(value = "priority", required = false)
        String priority) {
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

    @PostMapping
    public ResponseEntity create(@Validated @RequestBody InsertForm insertForm) {
        DramaDto dramaDto = new DramaDto(
                insertForm.getTitle(),
                insertForm.getYear(),
                insertForm.getPriority());
        int newDramaId = dramasService.save(dramaDto);
        URI location = UriComponentsBuilder.fromUriString("http://localhost:8080/create/" + newDramaId)
                .build().toUri();
        return ResponseEntity.created(location).build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity update(@PathVariable("id") int id,
                                 @Validated @RequestBody UpdateForm updateForm) {
        DramaDto dramaDto = new DramaDto(
                id,
                updateForm.getTitle(),
                updateForm.getYear(),
                updateForm.getPriority());
        Drama drama = dramasService.update(dramaDto);
        return ResponseEntity.ok(drama);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable("id") int id) {
        dramasService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}

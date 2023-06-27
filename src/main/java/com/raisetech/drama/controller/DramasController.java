package com.raisetech.drama.controller;

import com.raisetech.drama.entity.Drama;
import com.raisetech.drama.service.DramasService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/dramas")
public class DramasController {
    private final DramasService dramasService;

    public DramasController(DramasService dramasService){
        this.dramasService = dramasService;
    }

    @GetMapping
    public List<Drama> getDramas(@RequestParam(value = "priority", required = false) String priority)
            throws Exception {
        return dramasService.getDramas(priority);
    }
}

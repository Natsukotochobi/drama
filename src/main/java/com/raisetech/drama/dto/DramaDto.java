package com.raisetech.drama.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DramaDto {
    @Setter(AccessLevel.NONE)
    private int id;
    private String title;
    private String year;
    private String priority;

    public DramaDto(String title, String year, String priority) {
        this.id = 0;
        this.title = title;
        this.year = year;
        this.priority = priority;
    }
}

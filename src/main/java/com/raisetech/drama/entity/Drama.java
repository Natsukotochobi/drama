package com.raisetech.drama.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Drama {
    private int id;
    private String title;
    private String year;
    private String priority;
}

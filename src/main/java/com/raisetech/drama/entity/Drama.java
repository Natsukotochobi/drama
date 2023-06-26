package com.raisetech.drama.entity;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Drama {
    @Setter(AccessLevel.NONE)
    private int id;
    private String title;
    private String year;
    private String priority;
}

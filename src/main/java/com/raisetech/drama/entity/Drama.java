package com.raisetech.drama.entity;

import com.raisetech.drama.dto.DramaDto;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Drama {
    @Setter(AccessLevel.NONE)
    private int id;
    private String title;
    private String year;
    private String priority;

    public void updateDrama(DramaDto dramaDto) {
        if (dramaDto.getTitle() != null) {
            setTitle(dramaDto.getTitle());
        }
        if (dramaDto.getYear() != null) {
            setYear(dramaDto.getYear());
        }
        if (dramaDto.getPriority() != null) {
            setPriority(dramaDto.getPriority());
        }
    }
}

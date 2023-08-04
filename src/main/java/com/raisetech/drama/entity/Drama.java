package com.raisetech.drama.entity;

import com.raisetech.drama.dto.DramaDto;
import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Drama)) return false;
        Drama drama = (Drama) o;
        return Objects.equals(getId(), drama.getId()) &&
                Objects.equals(getTitle(), drama.getTitle()) &&
                Objects.equals(getYear(), drama.getYear()) &&
                Objects.equals(getPriority(), drama.getPriority());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getTitle(), getYear(), getPriority());
    }
}

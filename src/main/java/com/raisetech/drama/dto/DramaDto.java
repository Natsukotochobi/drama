package com.raisetech.drama.dto;

import com.raisetech.drama.entity.Drama;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import java.util.Objects;

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

    public DramaDto(int id, String title, String year, String priority) {
        this.id = id;
        this.title = title;
        this.year = year;
        this.priority = priority;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {return true;}
        if (!(o instanceof Drama)) {return false;}
        Drama drama = (Drama) o;
        return Objects.equals(getId(), drama.getId())
                && Objects.equals(getTitle(), drama.getTitle())
                && Objects.equals(getYear(), drama.getYear())
                && Objects.equals(getPriority(), drama.getPriority());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getTitle(), getYear(), getPriority());
    }
}

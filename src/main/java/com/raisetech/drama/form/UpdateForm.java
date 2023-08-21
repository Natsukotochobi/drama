package com.raisetech.drama.form;

import com.raisetech.drama.entity.Drama;
import com.raisetech.drama.validation.PriorityValidation;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateForm {
    @Size(max = 100)
    private String title;

    @Pattern(regexp = "^\\d{4}$")
    private String year;

    //@Size(max = 1)
    @PriorityValidation
    private String priority;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Drama)) return false;
        Drama drama = (Drama) o;
        return Objects.equals(getTitle(), drama.getTitle()) &&
                Objects.equals(getYear(), drama.getYear()) &&
                Objects.equals(getPriority(), drama.getPriority());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTitle(), getYear(), getPriority());
    }
}

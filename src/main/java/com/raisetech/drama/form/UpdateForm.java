package com.raisetech.drama.form;

import com.raisetech.drama.validation.PriorityValidation;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateForm {
    @Size(max = 100)
    private String title;

    @Pattern(regexp = "^\\d{4}$")
    private String year;

    @PriorityValidation
    private String priority;
}

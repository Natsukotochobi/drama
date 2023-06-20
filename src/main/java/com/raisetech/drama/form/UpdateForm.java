package com.raisetech.drama.form;

import com.raisetech.drama.Validation.PriorityValidation;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateForm {
    @Size(max = 100)
    private String title;

    @Pattern(regexp = "^\\d{4}$")
    private String year;

    @Size(max = 1)
    @PriorityValidation
    private String priority;
}

package com.raisetech.drama.form;

import com.raisetech.drama.validation.PriorityValidation;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@AllArgsConstructor
@NoArgsConstructor
public class InsertForm {
    @NotBlank
    @Size(max = 100)
    private String title;

    @Pattern(regexp = "^\\d{4}$")
    private String year;

    @NotBlank
    @Size(max = 1)
    @PriorityValidation
    private String priority;
}

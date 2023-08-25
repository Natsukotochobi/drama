package com.raisetech.drama.validation;

import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class PriorityValidatorTest {

    @Test
    void 正常な値の場合はtrueが返ってくること() {
        PriorityValidator validator = new PriorityValidator();
        String validPriority = "A";
        boolean isValid = validator.isValid(validPriority, mockContext());

        assertThat(isValid).isTrue();
    }

    @Test
    void ABC以外の場合はfalseを返すこと() {
        PriorityValidator validator = new PriorityValidator();
        String invalidPriority = "D";
        boolean isValid = validator.isValid(invalidPriority, mockContext());

        assertThat(isValid).isFalse();
    }

    @Test
    void nullの場合はtrueが返ってくること() {
        PriorityValidator validator = new PriorityValidator();
        String nullPriority = null;
        boolean isValid = validator.isValid(nullPriority, mockContext());

        assertThat(isValid).isTrue();
    }

    private ConstraintValidatorContext mockContext() {
        ConstraintValidatorContext context = Mockito.mock(ConstraintValidatorContext.class);
        ConstraintValidatorContext.ConstraintViolationBuilder builder =
                Mockito.mock(ConstraintValidatorContext.ConstraintViolationBuilder.class);
        when(context.buildConstraintViolationWithTemplate(Mockito.anyString())).thenReturn(builder);
        when(builder.addConstraintViolation()).thenReturn(context);
        return context;
    }

}
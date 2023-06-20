package com.raisetech.drama.Validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PriorityValidator.class)
@Documented
public @interface PriorityValidation {
    String message() default "Priority入力の指定に沿っていません。";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

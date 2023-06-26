package com.raisetech.drama.validation;

import com.raisetech.drama.entity.Priority;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;


public class PriorityValidator implements ConstraintValidator<PriorityValidation, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // 自作バリデーションのロジックを実装する
        // 例: valueがA、B、Cのいずれかであるかチェックする
        // バリデーションが成功した場合はtrue、失敗した場合はfalseを返す
        return Priority.A.name().equals(value) ||
                Priority.B.name().equals(value) ||
                Priority.C.name().equals(value);
    }
}

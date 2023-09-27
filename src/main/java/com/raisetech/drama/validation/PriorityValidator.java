package com.raisetech.drama.validation;

import com.raisetech.drama.entity.Priority;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;


public class PriorityValidator implements ConstraintValidator<PriorityValidation, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        // 自作バリデーションのロジックを実装する
        // 例: valueがA、B、Cのいずれかであるかチェックする
        // バリデーションが成功した場合はtrue、失敗した場合はfalseを返す
        return Arrays.stream(Priority.values()).anyMatch(priority -> priority.name().equals(value));
    }
}

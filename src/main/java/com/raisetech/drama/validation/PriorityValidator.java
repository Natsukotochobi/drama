package com.raisetech.drama.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PriorityValidator implements ConstraintValidator<PriorityValidation, String> {
    public enum Letter{
        //視聴優先度を表す文字を定義する
        A,B,C
    }
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // 自作バリデーションのロジックを実装する
        // 例: valueがA、B、Cのいずれかであるかチェックする
        // バリデーションが成功した場合はtrue、失敗した場合はfalseを返す
        return Letter.A.name().equals(value) ||
                Letter.B.name().equals(value) ||
                Letter.C.name().equals(value);
    }
}

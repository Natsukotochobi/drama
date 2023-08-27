package com.raisetech.drama.form;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class UpdateFormTest {
    @Test
    void 正しい値を入力した時にバリデーションエラーとならないこと() {
        UpdateForm updateForm = new UpdateForm();
        updateForm.setTitle("テスト用タイトル");
        updateForm.setYear("2023");
        updateForm.setPriority("A");

        Set<ConstraintViolation<UpdateForm>> result =
                Validation
                        .buildDefaultValidatorFactory()
                        .getValidator()
                        .validate(updateForm);
        assertThat(result.size()).isEqualTo(0);
    }

    @Test
    void タイトルに100文字を超えて入力するとバリデーションエラーになること() {
        UpdateForm updateForm = new UpdateForm();
        //「あ」を101文字入力する
        updateForm.setTitle("あああああああああああああああああああああああ" +
                "ああああああああああああああああああああああああああああああ" +
                "ああああああああああああああああああああああああああああああ" +
                "ああああああああああああああああああ");
        updateForm.setYear("2023");
        updateForm.setPriority("A");

        Set<ConstraintViolation<UpdateForm>> violations =
                Validation
                        .buildDefaultValidatorFactory()
                        .getValidator()
                        .validate(updateForm);
        assertThat(violations.size()).isEqualTo(1);
        assertThat(violations).extracting("message")
                .containsOnly("0 から 100 の間のサイズにしてください");
    }

    @Test
    void 発売年に整数4桁以外を入力するとバリデーションエラーになること() {
        UpdateForm updateForm = new UpdateForm();
        updateForm.setTitle("テスト用タイトル");
        updateForm.setYear("abcd");
        updateForm.setPriority("A");

        Set<ConstraintViolation<UpdateForm>> violations =
                Validation
                        .buildDefaultValidatorFactory()
                        .getValidator()
                        .validate(updateForm);
        assertThat(violations.size()).isEqualTo(1);
        assertThat(violations).extracting("message")
                .containsOnly("正規表現 \"^\\d{4}$\" にマッチさせてください");
    }

    @Test
    void 優先度の入力が1文字以外の場合にバリデーションエラーになること() {
        UpdateForm updateForm = new UpdateForm();
        updateForm.setTitle("テスト用タイトル");
        updateForm.setYear("2023");
        updateForm.setPriority("AB");

        Set<ConstraintViolation<UpdateForm>> violations =
                Validation
                        .buildDefaultValidatorFactory()
                        .getValidator()
                        .validate(updateForm);
        assertThat(violations.size()).isEqualTo(1);
        assertThat(violations).extracting("message")
                .containsOnly("Priority入力の指定に沿っていません。");
    }

    @Test
    void 優先度の入力がABC以外の場合にバリデーションエラーになること() {
        UpdateForm updateForm = new UpdateForm();
        updateForm.setTitle("テスト用タイトル");
        updateForm.setYear("2023");
        updateForm.setPriority("D");

        Set<ConstraintViolation<UpdateForm>> violations =
                Validation
                        .buildDefaultValidatorFactory()
                        .getValidator()
                        .validate(updateForm);
        assertThat(violations.size()).isEqualTo(1);
        assertThat(violations).extracting("message")
                .containsOnly("Priority入力の指定に沿っていません。");
    }

    @Test
    void すべてのフィールドにnullを入れてもバリデーションエラーにならないこと() {
        UpdateForm updateForm = new UpdateForm();
        updateForm.setTitle(null);
        updateForm.setYear(null);
        updateForm.setPriority(null);

        Set<ConstraintViolation<UpdateForm>> result =
                Validation
                        .buildDefaultValidatorFactory()
                        .getValidator()
                        .validate(updateForm);
        assertThat(result.size()).isEqualTo(0);
    }
}
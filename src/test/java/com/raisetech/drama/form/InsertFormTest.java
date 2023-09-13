package com.raisetech.drama.form;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Locale;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class InsertFormTest {

    @BeforeEach
    void setUp() {
        // テストケース内でのローカライズを設定（ここでは日本語を指定）
        Locale.setDefault(Locale.JAPANESE);
    }

    @Test
    void 正しい値を入力した時にバリデーションエラーとならないこと() {
        InsertForm insertForm = new InsertForm();
        insertForm.setTitle("テスト用タイトル");
        insertForm.setYear("2023");
        insertForm.setPriority("A");

        Set<ConstraintViolation<InsertForm>> result =
                Validation
                        .buildDefaultValidatorFactory()
                        .getValidator()
                        .validate(insertForm);
        assertThat(result.size()).isEqualTo(0);
    }

    @Test
    void タイトルに空文字を入力するとバリデーションエラーになること() {
        InsertForm insertForm = new InsertForm();
        insertForm.setTitle("");
        insertForm.setYear("2023");
        insertForm.setPriority("A");

        Set<ConstraintViolation<InsertForm>> violations =
                Validation
                        .buildDefaultValidatorFactory()
                        .getValidator()
                        .validate(insertForm);

        assertThat(violations.size()).isEqualTo(1);
        assertThat(violations).extracting("message").containsOnly("空白は許可されていません");
    }

    @Test
    void タイトルに100文字を超えて入力するとバリデーションエラーになること() {
        InsertForm insertForm = new InsertForm();
        //「あ」を101文字入力する
        insertForm.setTitle("あああああああああああああああああああああああ" +
                "ああああああああああああああああああああああああああああああ" +
                "ああああああああああああああああああああああああああああああ" +
                "ああああああああああああああああああ");
        insertForm.setYear("2023");
        insertForm.setPriority("A");

        Set<ConstraintViolation<InsertForm>> violations =
                Validation
                        .buildDefaultValidatorFactory()
                        .getValidator()
                        .validate(insertForm);

        assertThat(violations.size()).isEqualTo(1);
        assertThat(violations).extracting("message")
                .containsOnly("0 から 100 の間のサイズにしてください"); //あとでチェック
    }

    @Test
    void タイトルと優先度にnullを入力するとバリデーションエラーになること() {
        InsertForm insertForm = new InsertForm();
        insertForm.setTitle(null);
        insertForm.setYear("2023");
        insertForm.setPriority(null);

        Set<ConstraintViolation<InsertForm>> violations =
                Validation
                        .buildDefaultValidatorFactory()
                        .getValidator()
                        .validate(insertForm);

        assertThat(violations.size()).isEqualTo(2);
        assertThat(violations).extracting("message")
                .containsOnly("空白は許可されていません", "null は許可されていません");
    }

    @Test
    void 発売年に整数4桁以外を入力するとバリデーションエラーになること() {
        InsertForm insertForm = new InsertForm();
        insertForm.setTitle("テスト用タイトル");
        insertForm.setYear("abcd");
        insertForm.setPriority("A");

        Set<ConstraintViolation<InsertForm>> violations =
                Validation
                        .buildDefaultValidatorFactory()
                        .getValidator()
                        .validate(insertForm);

        assertThat(violations.size()).isEqualTo(1);
        assertThat(violations).extracting("message")
                .containsOnly("正規表現 \"^\\d{4}$\" にマッチさせてください"); //あとでチェック
    }

    @Test
    void 優先度に空文字を入力するとバリデーションエラーになること() {
        InsertForm insertForm = new InsertForm();
        insertForm.setTitle("テスト用タイトル");
        insertForm.setYear("2023");
        insertForm.setPriority("");

        Set<ConstraintViolation<InsertForm>> violations =
                Validation
                        .buildDefaultValidatorFactory()
                        .getValidator()
                        .validate(insertForm);

        assertThat(violations.size()).isEqualTo(1);
        assertThat(violations).extracting("message")
                .containsOnly("Priority入力の指定に沿っていません。");
    }

    @Test
    void 優先度の入力が1文字以外の場合にバリデーションエラーになること() {
        InsertForm insertForm = new InsertForm();
        insertForm.setTitle("テスト用タイトル");
        insertForm.setYear("2023");
        insertForm.setPriority("AB");

        Set<ConstraintViolation<InsertForm>> violations =
                Validation
                        .buildDefaultValidatorFactory()
                        .getValidator()
                        .validate(insertForm);

        assertThat(violations.size()).isEqualTo(1);
        assertThat(violations).extracting("message")
                .containsOnly("Priority入力の指定に沿っていません。");
    }

    @Test
    void 優先度の入力がABC以外の場合にバリデーションエラーになること() {
        InsertForm insertForm = new InsertForm();
        insertForm.setTitle("テスト用タイトル");
        insertForm.setYear("2023");
        insertForm.setPriority("D");

        Set<ConstraintViolation<InsertForm>> violations =
                Validation
                        .buildDefaultValidatorFactory()
                        .getValidator()
                        .validate(insertForm);

        assertThat(violations.size()).isEqualTo(1);
        assertThat(violations).extracting("message")
                .containsOnly("Priority入力の指定に沿っていません。");
    }
    }


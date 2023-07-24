package com.raisetech.drama.service;

import com.raisetech.drama.entity.Drama;
import com.raisetech.drama.mapper.DramasMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class DramasServiceImplTest {

    @InjectMocks
    DramasServiceImpl dramasServiceImpl;

    @Mock
    DramasMapper dramasMapper;

    @BeforeEach
    void setUp() {
        // テスト実行前にモックを初期化
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void ドラマ全件を正常に返すかテストする() {
        // モックの設定
        List<Drama> dramas = Arrays.asList(
                new Drama(1, "Drama1", "2022", "A"),
                new Drama(2, "Drama2", "2023", "B")
        );
        doReturn(dramas).when(dramasMapper).findAll();

        // テスト対象メソッドを呼び出す
        List<Drama> actualDramas = dramasServiceImpl.getAllDramas();

        assertThat(actualDramas).isEqualTo(dramas);

        verify(dramasMapper, times(1)).findAll();
    }

    
}

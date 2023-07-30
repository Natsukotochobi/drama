package com.raisetech.drama.service;

import com.raisetech.drama.dto.DramaDto;
import com.raisetech.drama.entity.Drama;
import com.raisetech.drama.exception.DuplicateTitleException;
import com.raisetech.drama.mapper.DramasMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DuplicateKeyException;

import java.util.Arrays;
import java.util.List;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;

import static org.mockito.Mockito.doThrow;
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

    @Test
    void priorityの絞り込みを正常に返すかテストする() {
        List<Drama> dramas = Arrays.asList(new Drama(1, "Drama1", "2022", "A"));
        doReturn(dramas).when(dramasMapper).findByPriority("A");

        // テスト対象メソッドを呼び出す
        List<Drama> actualDramas = dramasServiceImpl.getDramas("A");

        assertThat(actualDramas).isEqualTo(dramas);
        verify(dramasMapper, times(1)).findByPriority("A");
    }

    @Test
    void 新しいドラマを登録できるかテストする() {
        DramaDto dramaDto = new DramaDto("drama1", "2023", "A");
        doNothing().when(dramasMapper).save(dramaDto);
        dramasServiceImpl.save(dramaDto);
        verify(dramasMapper, times(1)).save(dramaDto);
    }

    @Test
    public void DuplicateTitleExceptionがスローされるかテストする() {
        DramaDto dramaDto = new DramaDto("Duplicate Title", "2023", "A");
        doThrow(new DuplicateKeyException("重複したタイトル")).when(dramasMapper).save(dramaDto);
        assertThrows(DuplicateTitleException.class, () -> {
            dramasServiceImpl.save(dramaDto);
        });
        verify(dramasMapper, times(1)).save(dramaDto);
    }


}

package com.raisetech.drama.service;

import com.raisetech.drama.dto.DramaDto;
import com.raisetech.drama.entity.Drama;
import com.raisetech.drama.exception.DuplicateTitleException;
import com.raisetech.drama.exception.ResourceNotFoundException;
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
import java.util.Optional;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
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
        DramaDto dramaDto = new DramaDto("Drama1", "2023", "A");
        doNothing().when(dramasMapper).save(dramaDto);
        dramasServiceImpl.save(dramaDto);
        verify(dramasMapper, times(1)).save(dramaDto);
    }

    @Test
    void saveメソッドでDuplicateTitleExceptionがスローされるかテストする() {
        //saveメソッドの重複テスト
        DramaDto dramaDto = new DramaDto("Duplicate Title", "2023", "A");
        doThrow(new DuplicateKeyException("重複したタイトル")).when(dramasMapper).save(dramaDto);
        assertThrows(DuplicateTitleException.class, () -> {
            dramasServiceImpl.save(dramaDto);
        });
        verify(dramasMapper, times(1)).save(dramaDto);
    }

    @Test
    void 更新が成功した場合をテストする() {
        DramaDto dramaDto = new DramaDto(1, "Updated Title", "2023", "A");
        Drama drama = new Drama(1, "Existing Title", "2022", "B");
        //findById()のモック挙動を定義
        doReturn(Optional.of(drama)).when(dramasMapper).findById(dramaDto.getId());
        //update()のモック挙動を定義
        doNothing().when(dramasMapper).update(drama);
        // テスト対象メソッドを呼び出す
        Drama updatedDrama = dramasServiceImpl.update(dramaDto);

        assertThat(updatedDrama.getId()).isEqualTo(dramaDto.getId());
        assertThat(updatedDrama.getTitle()).isEqualTo(dramaDto.getTitle());
        assertThat(updatedDrama.getYear()).isEqualTo(dramaDto.getYear());
        assertThat(updatedDrama.getPriority()).isEqualTo(dramaDto.getPriority());

        verify(dramasMapper, times(1)).findById(dramaDto.getId());
        verify(dramasMapper, times(1)).update(drama);
    }

    @Test
    void updateメソッドで存在しないidが指定された時例外がスローされるかテストする() {
        //update()のfindById()テスト
        DramaDto dramaDto = new DramaDto(999, "Updated Title", "2023", "A");
        doReturn(Optional.empty()).when(dramasMapper).findById(999);

        assertThrows(ResourceNotFoundException.class, () -> dramasServiceImpl.update(dramaDto));
        verify(dramasMapper, times(1)).findById(999);
    }

    @Test
    void updateメソッドでDuplicateTitleExceptionがスローされるかテストする() {
        DramaDto dramaDto = new DramaDto(1, "Updated Title", "2023", "A");
        Drama drama = new Drama(1, "Existing Title", "2022", "B");
        //findById()のモック挙動を定義
        doReturn(Optional.of(drama)).when(dramasMapper).findById(dramaDto.getId());
        //update()のモック挙動を定義
        doThrow(new DuplicateKeyException("重複したタイトル")).when(dramasMapper).update(drama);

        assertThrows(DuplicateTitleException.class, () -> dramasServiceImpl.update(dramaDto));

        verify(dramasMapper, times(1)).findById(dramaDto.getId());
        verify(dramasMapper, times(1)).update(drama);
    }

    @Test
    void 削除が成功した場合をテストする() {
        int idToDelete = 1;
        Drama existingDrama = new Drama(1, "Existing Title", "2022", "A");

        doReturn(Optional.of(existingDrama)).when(dramasMapper).findById(idToDelete);
        doNothing().when(dramasMapper).deleteById(idToDelete);

        dramasServiceImpl.deleteById(idToDelete);
        verify(dramasMapper, times(1)).findById(idToDelete);
        verify(dramasMapper, times(1)).deleteById(idToDelete);
    }

    @Test
    void deleteメソッドで存在しないidが指定された時例外がスローされるかテストする() {
        int idToDelete = 999;
        doReturn(Optional.empty()).when(dramasMapper).findById(idToDelete);

        assertThrows(ResourceNotFoundException.class, () -> dramasServiceImpl.deleteById(idToDelete));
        verify(dramasMapper, times(1)).findById(idToDelete);
        verify(dramasMapper, never()).deleteById(anyInt());
    }
}

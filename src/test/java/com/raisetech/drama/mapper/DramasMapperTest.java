package com.raisetech.drama.mapper;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.spring.api.DBRider;
import com.raisetech.drama.dto.DramaDto;
import com.raisetech.drama.entity.Drama;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DBRider
@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class DramasMapperTest {

    @Autowired
    DramasMapper dramasMapper;

    @Test
    @DataSet(value = "datasets/dramas.yml")
    @Transactional
    void すべてのドラマが取得できること() {
        List<Drama> dramas = dramasMapper.findAll();
        assertThat(dramas)
                .hasSize(3)
                .contains(
                        new Drama(1, "MIMICS", "2022", "A"),
                        new Drama(2, "偶然見つけたハル", "2019", "C"),
                        new Drama(3, "A-TEEN", "2018", "A")
                );
    }
    @Test
    @DataSet(value = "datasets/dramas.yml")
    @Transactional
    void Priorityで絞り込みできること() {
        List<Drama> result = dramasMapper.findByPriority("A");

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getTitle()).isEqualTo("MIMICS");
        assertThat(result.get(1).getTitle()).isEqualTo("A-TEEN");
    }

    @Test
    @DataSet(value = "datasets/dramas.yml")
    @Transactional
    void Priorityに該当するデータがないとき空のリストが返されること() {
        List<Drama> result = dramasMapper.findByPriority("B");
        assertThat(result).hasSize(0);
        assertThat(result).isEmpty();
    }

    @Test
    @DataSet(value = "datasets/dramas.yml")
    @Transactional
    void idで指定したドラマを取得できること() {
        Optional<Drama> drama = dramasMapper.findById(1);
        assertThat(drama).isEqualTo(Optional.of(new Drama(1, "MIMICS", "2022", "A")));
    }

    @Test
    @DataSet(value = "datasets/dramas.yml")
    @Transactional
    void 指定したidが存在しない場合に返ってくるデータが空であること() {
        int targetId = 100;
        Optional<Drama> drama = dramasMapper.findById(targetId);
        assertThat(drama).isEmpty();
    }

    @Test
    @DataSet(value = "datasets/dramas.yml")
    @ExpectedDataSet(value = "datasets/expectedDramaDataAfterInsert.yml", ignoreCols = "id")
    @Transactional
    void 新しいドラマが登録できること() {
        DramaDto dramaDto = new DramaDto("追加したドラマ", "2023", "A");
        dramasMapper.save(dramaDto);
    }

    @Test
    @DataSet(value = "datasets/dramas.yml")
    @Transactional
    void 重複したタイトルが新規登録で渡された場合にDuplicateKeyExceptionをスローすること() {
        DramaDto dramaDto = new DramaDto("偶然見つけたハル", "2023", "A");
        assertThrows(DuplicateKeyException.class, () -> dramasMapper.save(dramaDto));
    }

    @Test
    @DataSet(value = "datasets/dramas.yml")
    @ExpectedDataSet(value = "datasets/expectedDramaDataAfterUpdate.yml")
    @Transactional
    void ドラマの情報を更新できること() {
        Drama drama = new Drama(1, "更新されたドラマ", "2023", "C");
        dramasMapper.update(drama);
    }

    @Test
    @DataSet(value = "datasets/dramas.yml")
    @Transactional
    void 重複したタイトルが更新で渡された場合にDuplicateKeyExceptionをスローすること() {
        Drama drama = new Drama(1, "偶然見つけたハル", "2022", "A");
        assertThrows(DuplicateKeyException.class, () -> dramasMapper.update(drama));
    }

    @Test
    @DataSet(value = "datasets/dramas.yml")
    @ExpectedDataSet(value = "datasets/expectedDramaDataAfterDelete.yml")
    @Transactional
    void ドラマを削除できること() {
        int targetId = 3;
        dramasMapper.deleteById(targetId);
    }


}

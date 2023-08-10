package com.raisetech.drama.mapper;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;
import com.raisetech.drama.entity.Drama;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

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
    void 指定したidが存在しない場合に空のOptionalが返ってくること() {
        int targetId = 100;
        Optional<Drama> drama = dramasMapper.findById(targetId);
        assertThat(drama).isEmpty();
    }

}

package com.raisetech.drama.entity;

import com.raisetech.drama.dto.DramaDto;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class DramaTest {
    @Test
    void inputの値が存在するときに更新ができること() {
        Drama drama = new Drama(1, "title", "year", "priority");
        DramaDto dramaDto = new DramaDto("title2", "year2", "priority2");
        drama.updateDrama(dramaDto);

            assertThat(drama).isEqualTo(new Drama(1, "title2", "year2", "priority2"));
    }

    @Test
    void inputの値がnullであるときに更新ができること() {
        Drama drama = new Drama(1, "title", "year", "priority");
        DramaDto dramaDto = new DramaDto(null, null, null);
        drama.updateDrama(dramaDto);
        assertThat(drama).isEqualTo(new Drama(1, "title", "year", "priority"));
    }
}

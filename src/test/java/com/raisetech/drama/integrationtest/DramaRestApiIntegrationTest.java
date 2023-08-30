package com.raisetech.drama.integrationtest;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.Customization;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.comparator.CustomComparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@AutoConfigureMockMvc
@DBRider
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class DramaRestApiIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    @DataSet(value = "datasets/dramas.yml")
    @Transactional
    void ドラマが全件取得できること() throws Exception {
        String response = mockMvc.perform(MockMvcRequestBuilders.get("/dramas"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        JSONAssert.assertEquals("""
            [
                {
                    "id": 1,
                    "title": "MIMICS",
                    "year": "2022",
                    "priority": "A"
                },
                {
                    "id": 2,
                    "title": "偶然見つけたハル",
                    "year": "2019",
                    "priority": "C"
                },
                {
                    "id": 3,
                    "title": "A-TEEN",
                    "year": "2018",
                    "priority": "A"
                }
            ]
            """, response, JSONCompareMode.STRICT);
    }

    @Test
    @DataSet(value = "datasets/dramas.yml")
    @Transactional
    void priorityで指定したドラマのみ取得できること() throws Exception {
        String response = mockMvc.perform(MockMvcRequestBuilders.get("/dramas")
                    .param("priority", "A"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        JSONAssert.assertEquals("""
            [
                {
                    "id": 1,
                    "title": "MIMICS",
                    "year": "2022",
                    "priority": "A"
                },
                {
                    "id": 3,
                    "title": "A-TEEN",
                    "year": "2018",
                    "priority": "A"
                }
            ]
            """, response, JSONCompareMode.STRICT);
    }

    @Test
    @DataSet(value = "datasets/dramas.yml")
    @Transactional
    void priorityで指定したドラマがDBに登録されていないとき空のデータが返されること() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/dramas")
                        .param("priority", "B"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    @DataSet(value = "datasets/dramas.yml")
    @Transactional
    void priorityでABC以外を指定したときBadRequestが返ってくること() throws Exception {
        String response = mockMvc.perform(MockMvcRequestBuilders.get("/dramas")
                        .param("priority", "D"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        JSONAssert.assertEquals("""
                {
                    "error": "Bad Request",
                    "timeStamp": "2023-08-30T10:50:29.604145900+09:00[Asia/Tokyo]",
                    "status": "400"
                }
                """, response,
                new CustomComparator(JSONCompareMode.LENIENT,
                        new Customization("timeStamp", (o1, o2) -> true))); // タイムスタンプを無視
    }

}

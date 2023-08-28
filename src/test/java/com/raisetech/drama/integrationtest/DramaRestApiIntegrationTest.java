package com.raisetech.drama.integrationtest;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;

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

}

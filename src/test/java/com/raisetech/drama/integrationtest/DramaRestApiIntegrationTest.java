package com.raisetech.drama.integrationtest;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
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
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;

import static org.hamcrest.text.MatchesPattern.matchesPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;

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
                            "status": "400",
                            "messages": [
                                {
                                   "field": "priority",
                                   "message": "Priority入力の指定に沿っていません。"
                                }
                                        ]
                        }
                        """, response,
                new CustomComparator(JSONCompareMode.LENIENT,
                        new Customization("timeStamp", (o1, o2) -> true))); // タイムスタンプを無視
    }

    @Test
    @DataSet(value = "datasets/dramas.yml")
    @ExpectedDataSet(value = "datasets/expectedDramaDataAfterInsert.yml", ignoreCols = "id")
    @Transactional
    void ドラマを新規登録できること() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.post("/dramas")
                .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                    {
                                        "title": "追加したドラマ",
                                        "year": "2023",
                                        "priority": "A"
                                    }
                                """))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(header().string(
                        "Location",  matchesPattern("http://localhost:8080/create/\\d+")));
    }

    @Test
    @Transactional
    void すべてnullで登録するとtitleとpriorityがエラーになりBadRequestが返ってくること() throws Exception{
        String response = mockMvc.perform(MockMvcRequestBuilders.post("/dramas")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                                    {
                                        "title": null,
                                        "year": null,
                                        "priority": null
                                    }
                                """)
                .header(HttpHeaders.ACCEPT_LANGUAGE, "ja-JP"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        JSONAssert.assertEquals("""
                        {
                            "message": {
                                "title": "空白は許可されていません",
                                "priority": "null は許可されていません"
                                        },
                            "error": "Bad Request",
                            "timeStamp": "2023-08-30T10:50:29.604145900+09:00[Asia/Tokyo]",
                            "status": "400"
                        }
                        """, response,
                new CustomComparator(JSONCompareMode.LENIENT,
                        new Customization("timeStamp", (o1, o2) -> true))); // タイムスタンプを無視

    }

    @Test
    @Transactional
    void すべて空文字で登録するとtitleとpriorityがエラーになりBadRequestが返ってくること() throws Exception{
        String response = mockMvc.perform(MockMvcRequestBuilders.post("/dramas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                    {
                                        "title": "",
                                        "year": "",
                                        "priority": ""
                                    }
                                """)
                        .header(HttpHeaders.ACCEPT_LANGUAGE, "ja-JP"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        JSONAssert.assertEquals("""
                        {
                            "message": {
                                "title": "空白は許可されていません",
                                "priority": "Priority入力の指定に沿っていません。"
                                        },
                            "error": "Bad Request",
                            "timeStamp": "2023-08-30T10:50:29.604145900+09:00[Asia/Tokyo]",
                            "status": "400"
                        }
                        """, response,
                new CustomComparator(JSONCompareMode.LENIENT,
                        new Customization("timeStamp", (o1, o2) -> true))); // タイムスタンプを無視

    }

    @Test
    @Transactional
    void titleに101文字以上入力すると新規登録されずBadRequestが返ってくること() throws Exception{
        //titleに101文字入力する
        String response = mockMvc.perform(MockMvcRequestBuilders.post("/dramas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                     {
                                        "title": "あああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああ",
                                        "year": "2023",
                                        "priority": "A"
                                     }
                                """)
                        .header(HttpHeaders.ACCEPT_LANGUAGE, "ja-JP"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        JSONAssert.assertEquals("""
                        {
                            "message": {
                                "title": "0 から 100 の間のサイズにしてください"
                                        },
                            "error": "Bad Request",
                            "timeStamp": "2023-08-30T10:50:29.604145900+09:00[Asia/Tokyo]",
                            "status": "400"
                        }
                        """, response,
                new CustomComparator(JSONCompareMode.LENIENT,
                        new Customization("timeStamp", (o1, o2) -> true))); // タイムスタンプを無視
    }

    @Test
    @Transactional
    void yearに整数4桁以外のものを入力すると新規登録されずBadRequestが返ってくること() throws Exception{
        String response = mockMvc.perform(MockMvcRequestBuilders.post("/dramas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                    {
                                        "title": "追加したドラマ",
                                        "year": "abcd",
                                        "priority": "A"
                                    }
                                """)
                        .header(HttpHeaders.ACCEPT_LANGUAGE, "ja-JP"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        JSONAssert.assertEquals("""
                        {
                            "status": "400",
                            "error": "Bad Request",
                            "message": {
                                "year": "正規表現 \\"^\\\\d{4}$\\" にマッチさせてください"
                                        },
                            "timeStamp": "2023-09-01T16:12:47.452803800+09:00[Asia/Tokyo]"
                        }
                        """, response,
                new CustomComparator(JSONCompareMode.LENIENT,
                        new Customization("timeStamp", (o1, o2) -> true))); // タイムスタンプを無視
    }

    @Test
    @Transactional
    void priorityにABC以外のものを入力すると新規登録されずBadRequestが返ってくること() throws Exception{
        String response = mockMvc.perform(MockMvcRequestBuilders.post("/dramas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                    {
                                        "title": "追加したドラマ",
                                        "year": "2023",
                                        "priority": "D"
                                    }
                                """)
                        .header(HttpHeaders.ACCEPT_LANGUAGE, "ja-JP"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        JSONAssert.assertEquals("""
                        {
                            "status": "400",
                            "error": "Bad Request",
                            "message": {
                                "priority": "Priority入力の指定に沿っていません。"
                                        },
                            "timeStamp": "2023-09-01T16:12:47.452803800+09:00[Asia/Tokyo]"
                        }
                        """, response,
                new CustomComparator(JSONCompareMode.LENIENT,
                        new Customization("timeStamp", (o1, o2) -> true))); // タイムスタンプを無視
    }

    @Test
    @DataSet(value = "datasets/dramas.yml")
    @Transactional
    void 登録するタイトルがすでにDBに存在するときエラーになりConflictが返ってくること() throws Exception{
        //タイトルのみがconflictの原因であるか調べるため他の項目は登録にない内容にする
        String response = mockMvc.perform(MockMvcRequestBuilders.post("/dramas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                    {
                                        "title": "MIMICS",
                                        "year": "2000",
                                        "priority": "B"
                                    }
                                """)
                        .header(HttpHeaders.ACCEPT_LANGUAGE, "ja-JP"))
                .andExpect(MockMvcResultMatchers.status().isConflict())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        JSONAssert.assertEquals("""
                        {
                            "status": "409",
                            "error": "Conflict",
                            "message": "MIMICSは、すでに登録されています。",
                            "timeStamp": "2023-09-01T16:12:47.452803800+09:00[Asia/Tokyo]"
                        }
                        """, response,
                new CustomComparator(JSONCompareMode.LENIENT,
                        new Customization("timeStamp", (o1, o2) -> true))); // タイムスタンプを無視
    }



    @Test
    @DataSet(value = "datasets/dramas.yml")
    @ExpectedDataSet(value = "datasets/expectedDramaDataAfterUpdate.yml")
    @Transactional
    void 指定されたidのドラマが存在するとき内容が更新されること() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.patch("/dramas/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("""
                            {
                                "title": "更新されたドラマ",
                                "year": "2023",
                                "priority": "C"
                            }
                            """))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }

    @Test
    @DataSet(value = "datasets/dramas.yml")
    @Transactional
    void 指定されたidがDBに存在しないとき更新されずNotFoundが返ってくること() throws Exception{
       String response = mockMvc.perform(MockMvcRequestBuilders.patch("/dramas/{id}", 99)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("""
                            {
                                "title": "更新されたドラマ",
                                "year": "2023",
                                "priority": "C"
                            }
                            """))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        JSONAssert.assertEquals("""
                        {
                            "status": "404",
                            "error": "Not Found",
                            "message": "id:99番のタイトルが見つかりません。",
                            "timeStamp": "2023-09-01T16:12:47.452803800+09:00[Asia/Tokyo]",
                            "path": "/dramas/99"
                        }
                        """, response,
                new CustomComparator(JSONCompareMode.LENIENT,
                        new Customization("timeStamp", (o1, o2) -> true))); // タイムスタンプを無視

    }

    @Test
    @DataSet(value = "datasets/dramas.yml")
    @Transactional
    void titleに101文字以上入力すると更新処理されずBadRequestが返ってくること() throws Exception{
        //titleに101文字入力する
        String response = mockMvc.perform(MockMvcRequestBuilders.patch("/dramas/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                     {
                                        "title": "あああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああ",
                                        "year": "2023",
                                        "priority": "A"
                                     }
                                """)
                        .header(HttpHeaders.ACCEPT_LANGUAGE, "ja-JP"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        JSONAssert.assertEquals("""
                        {
                            "message": {
                                "title": "0 から 100 の間のサイズにしてください"
                                        },
                            "error": "Bad Request",
                            "timeStamp": "2023-08-30T10:50:29.604145900+09:00[Asia/Tokyo]",
                            "status": "400"
                        }
                        """, response,
                new CustomComparator(JSONCompareMode.LENIENT,
                        new Customization("timeStamp", (o1, o2) -> true))); // タイムスタンプを無視
    }

    @Test
    @DataSet(value = "datasets/dramas.yml")
    @Transactional
    void yearに整数4桁以外のものを入力すると更新処理されずBadRequestが返ってくること() throws Exception{
        String response = mockMvc.perform(MockMvcRequestBuilders.patch("/dramas/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                    {
                                        "title": "追加したドラマ",
                                        "year": "abcd",
                                        "priority": "A"
                                    }
                                """)
                        .header(HttpHeaders.ACCEPT_LANGUAGE, "ja-JP"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        JSONAssert.assertEquals("""
                        {
                            "status": "400",
                            "error": "Bad Request",
                            "message": {
                                "year": "正規表現 \\"^\\\\d{4}$\\" にマッチさせてください"
                                        },
                            "timeStamp": "2023-09-01T16:12:47.452803800+09:00[Asia/Tokyo]"
                        }
                        """, response,
                new CustomComparator(JSONCompareMode.LENIENT,
                        new Customization("timeStamp", (o1, o2) -> true))); // タイムスタンプを無視
    }

    @Test
    @DataSet(value = "datasets/dramas.yml")
    @Transactional
    void priorityにABC以外のものを入力すると更新処理されずBadRequestが返ってくること() throws Exception{
        String response = mockMvc.perform(MockMvcRequestBuilders.patch("/dramas/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                    {
                                        "title": "追加したドラマ",
                                        "year": "2023",
                                        "priority": "D"
                                    }
                                """))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        JSONAssert.assertEquals("""
                        {
                            "status": "400",
                            "error": "Bad Request",
                            "message": {
                                "priority": "Priority入力の指定に沿っていません。"
                                        },
                            "timeStamp": "2023-09-01T16:12:47.452803800+09:00[Asia/Tokyo]"
                        }
                        """, response,
                new CustomComparator(JSONCompareMode.LENIENT,
                        new Customization("timeStamp", (o1, o2) -> true))); // タイムスタンプを無視
    }

    @Test
    @DataSet(value = "datasets/dramas.yml")
    @Transactional
    void 更新するタイトルがすでにDBに存在するときエラーになりConflictが返ってくること() throws Exception{
        //タイトルのみがconflictの原因であるか調べるため他の項目は登録にない内容にする
        String response = mockMvc.perform(MockMvcRequestBuilders.patch("/dramas/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                    {
                                        "title": "偶然見つけたハル",
                                        "year": "2000",
                                        "priority": "B"
                                    }
                                """)
                        .header(HttpHeaders.ACCEPT_LANGUAGE, "ja-JP"))
                .andExpect(MockMvcResultMatchers.status().isConflict())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        JSONAssert.assertEquals("""
                        {
                            "status": "409",
                            "error": "Conflict",
                            "message": "偶然見つけたハルは、すでに登録されています。",
                            "timeStamp": "2023-09-01T16:12:47.452803800+09:00[Asia/Tokyo]"
                        }
                        """, response,
                new CustomComparator(JSONCompareMode.LENIENT,
                        new Customization("timeStamp", (o1, o2) -> true))); // タイムスタンプを無視
    }

    @Test
    @DataSet(value = "datasets/dramas.yml")
    @ExpectedDataSet(value = "datasets/dramas.yml")
    @Transactional
    void nullで更新すると更新前の情報のまま据え置かれエラーにならないこと() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.patch("/dramas/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                    {
                                        "title": null,
                                        "year": null,
                                        "priority": null
                                    }
                                """))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DataSet(value = "datasets/dramas.yml")
    @ExpectedDataSet(value = "datasets/expectedDramaDataAfterDelete.yml")
    @Transactional
    void 指定されたidのドラマが存在するときDBから削除されること() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.delete("/dramas/{id}", 3))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @DataSet(value = "datasets/dramas.yml")
    @Transactional
    void 指定されたidがDBに存在しないとき削除されずNotFoundが返ってくること() throws Exception{
        String response = mockMvc.perform(MockMvcRequestBuilders.delete("/dramas/{id}", 99))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        JSONAssert.assertEquals("""
                        {
                            "status": "404",
                            "error": "Not Found",
                            "message": "id:99番のタイトルが見つかりません。",
                            "timeStamp": "2023-09-01T16:12:47.452803800+09:00[Asia/Tokyo]",
                            "path": "/dramas/99"
                        }
                        """, response,
                new CustomComparator(JSONCompareMode.LENIENT,
                        new Customization("timeStamp", (o1, o2) -> true))); // タイムスタンプを無視
    }

}

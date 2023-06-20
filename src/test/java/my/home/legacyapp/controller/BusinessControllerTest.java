package my.home.legacyapp.controller;

import my.home.legacyapp.LegacyAppTestConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql(scripts={"classpath:init.sql"})
@SpringBootTest(classes = LegacyAppTestConfiguration.class)
@AutoConfigureMockMvc
class BusinessControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void whenGetByIdThenReturnsDto() throws Exception {
        this.mockMvc.perform(get("/api/value/{id}", 1))
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.id").value("1"),
                        jsonPath("$.type").value("TYPE1"),
                        jsonPath("$.businessValue").value("Value1"),
                        jsonPath("$.createdAt").value("10.06.2023 13:01:19"),
                        jsonPath("$.updatedAt").value("10.06.2023 13:01:19")
                );
    }

    @Test
    void whenFindByTypeThenReturnsPageList() throws Exception {
        this.mockMvc.perform(get("/api/values/types").param("types", "TYPE4", "TYPE5"))
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.content", hasSize(2)),
                        jsonPath("$.content[0].id").value("4"),
                        jsonPath("$.content[0].type").value("TYPE4"),
                        jsonPath("$.content[0].businessValue").value("Value4"),
                        jsonPath("$.content[0].createdAt").value("15.06.2023 18:50:32"),
                        jsonPath("$.content[0].updatedAt").value("15.06.2023 18:50:32"),
                        jsonPath("$.content[1].id").value("5"),
                        jsonPath("$.content[1].type").value("TYPE5"),
                        jsonPath("$.content[1].businessValue").value("Value5"),
                        jsonPath("$.content[1].createdAt").value("17.06.2023 11:05:14"),
                        jsonPath("$.content[1].updatedAt").value("17.06.2023 11:05:14")
                );
    }
}
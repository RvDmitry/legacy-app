package my.home.legacyapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import my.home.legacyapp.dto.BusinessDto;
import my.home.legacyapp.entity.BusinessType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class BusinessControllerTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest");
    @Container
    static KafkaContainer kafkaContainer = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:latest"));

    @DynamicPropertySource
    static void kafkaProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers", kafkaContainer::getBootstrapServers);
    }

    @Autowired
    private MockMvc mockMvc;

    @Sql(scripts={"classpath:init.sql"})
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

    @Sql(scripts={"classpath:init.sql"})
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

    @Sql(scripts={"classpath:init.sql"})
    @Test
    void whenFindByCreationDateThenReturnsPageList() throws Exception {
        this.mockMvc.perform(get("/api/values/created")
                        .param("dateFrom", "2023-06-12")
                        .param("dateTo", "2023-06-13"))
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.content", hasSize(2)),
                        jsonPath("$.content[0].id").value("2"),
                        jsonPath("$.content[0].type").value("TYPE2"),
                        jsonPath("$.content[0].businessValue").value("Value2"),
                        jsonPath("$.content[0].createdAt").value("12.06.2023 12:10:45"),
                        jsonPath("$.content[0].updatedAt").value("12.06.2023 12:10:45"),
                        jsonPath("$.content[1].id").value("3"),
                        jsonPath("$.content[1].type").value("TYPE3"),
                        jsonPath("$.content[1].businessValue").value("Value3"),
                        jsonPath("$.content[1].createdAt").value("13.06.2023 14:20:00"),
                        jsonPath("$.content[1].updatedAt").value("13.06.2023 14:20:00")
                );
    }

    @Test
    void whenAddValueThenReturnsDto() throws Exception {
        var dto = new BusinessDto(BusinessType.TYPE3, "Value3");
        this.mockMvc.perform(post("/api/value")
                .content(asJsonString(dto))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpectAll(
                        status().isCreated(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.id").exists(),
                        jsonPath("$.type").value("TYPE3"),
                        jsonPath("$.businessValue").value("Value3"),
                        jsonPath("$.createdAt").exists(),
                        jsonPath("$.updatedAt").exists()
                );
    }

    @Sql(scripts={"classpath:init.sql"})
    @Test
    void whenEditValueThenReturnsDto() throws Exception {
        var dto = new BusinessDto(3L, BusinessType.TYPE5, "Value5");
        this.mockMvc.perform(patch("/api/value")
                        .content(asJsonString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.id").value("3"),
                        jsonPath("$.type").value("TYPE5"),
                        jsonPath("$.businessValue").value("Value5"),
                        jsonPath("$.createdAt").exists(),
                        jsonPath("$.updatedAt").exists()
                );
    }

    @Sql(scripts={"classpath:init.sql"})
    @Test
    void whenDeleteByIdThenReturnsString() throws Exception {
        this.mockMvc.perform(delete("/api/value/{id}", 1))
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().contentType("text/plain;charset=UTF-8"),
                        content().string("Value with id [1] was delete")
                );
    }

    private String asJsonString(final Object obj) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
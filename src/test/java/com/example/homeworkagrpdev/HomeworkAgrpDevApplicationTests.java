package com.example.homeworkagrpdev;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.UUID;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
class HomeworkAgrpDevApplicationTests {

    @Container
    private static MySQLContainer container = new MySQLContainer<>("mysql:latest");

    @Autowired
    private MockMvc mock;
    @Autowired
    JdbcTemplate jdbcTemplate;


    @DynamicPropertySource
    public static void setProperties(DynamicPropertyRegistry registry){
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.username", container::getUsername);
        registry.add("spring.datasource.password", container::getPassword);
    }

    @Test
    void checkMostOpenedUnique() throws Exception {
        mock.perform(MockMvcRequestBuilders
                        .get("/topUniqueLastWeekTest?endOfTheWeek=2022-04-14 00:00:00")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(10)))
                .andExpect(jsonPath("$[0].documentUuid").value("8fb1ce0d-5dfc-4425-a06e-ac216f2356af"))
                .andExpect(jsonPath("$[0].views").value("19"))
                .andExpect(jsonPath("$[1].documentUuid").value("0693ae78-f1e6-44ac-9359-19a600b8921b"))
                .andExpect(jsonPath("$[1].views").value("16"));
    }

    @Test
    void checkMostOpenedOverall() throws Exception {
        mock.perform(MockMvcRequestBuilders
                        .get("/topOverallLastWeekTest?endOfTheWeek=2022-04-20 00:00:00")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(10)))
                .andExpect(jsonPath("$[0].documentUuid").value("d2ca9c37-3017-466f-9318-36a76e16f280"))
                .andExpect(jsonPath("$[0].views").value("123"))
                .andExpect(jsonPath("$[9].documentUuid").value("dbb72190-484d-4312-afea-35bd077be1ad"))
                .andExpect(jsonPath("$[9].views").value("97"));
    }

    @Test
    void checkTrendingForWeek() throws Exception {
        mock.perform(MockMvcRequestBuilders
                        .get("/mostTrendingLastWeekTest?endOfTheWeek=2022-04-20 00:00:00")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(10)))
                .andExpect(jsonPath("$[0].documentUuid").value("86d83acf-46de-4c47-b161-eec791121867"))
                .andExpect(jsonPath("$[0].score").value("2.4899442"))
                .andExpect(jsonPath("$[9].documentUuid").value("f2663b8d-6e79-4e89-bc0e-0b06d810b87e"))
                .andExpect(jsonPath("$[9].score").value("0.372572"));
    }

    @Test
    void checkTrendingFor24Hours() throws Exception {
        mock.perform(MockMvcRequestBuilders
                        .get("/mostTrendingFromTest?from=2022-04-19 00:00:00&to=2022-04-20 00:00:00")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(10)))
                .andExpect(jsonPath("$[0].documentUuid").value("b058119d-d859-4eb2-a47a-8da052fbe63b"))
                .andExpect(jsonPath("$[0].score").value("2.3794491"))
                .andExpect(jsonPath("$[9].documentUuid").value("f2663b8d-6e79-4e89-bc0e-0b06d810b87e"))
                .andExpect(jsonPath("$[9].score").value("0.2565583"));
    }

    @Test
    void checkTrendingForLastMonth() throws Exception {
        mock.perform(MockMvcRequestBuilders
                        .get("/mostTrendingFromTest?from=2022-03-20 00:00:00&to=2022-04-20 00:00:00")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(10)))
                .andExpect(jsonPath("$[0].documentUuid").value("252a0dba-b4cc-4070-bb4e-912fcbbb81bf"))
                .andExpect(jsonPath("$[0].score").value("4.6922584"))
                .andExpect(jsonPath("$[9].documentUuid").value("44c77775-7dd7-4846-9d24-c69ced4d9180"))
                .andExpect(jsonPath("$[9].score").value("3.5766375"));
    }

    @Test
    void checkAddingRecord() throws Exception {
        mock.perform(MockMvcRequestBuilders
                        .post(String.format("/addRecord?bookUuid=%s&userUuid=%s", UUID.randomUUID(), UUID.randomUUID())))
                .andExpect(status().isOk());
        mock.perform(MockMvcRequestBuilders
                        .post(String.format("/addRecord?bookUuid=%s&userUuid=%s", UUID.randomUUID(), UUID.randomUUID())))
                .andExpect(status().isOk());
        int result = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM document_record;",
                Integer.TYPE);
        Assertions.assertEquals(result, 2002);
    }

}

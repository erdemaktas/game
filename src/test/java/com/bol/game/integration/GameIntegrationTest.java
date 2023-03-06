package com.bol.game.integration;

import com.bol.game.controller.GameController;
import com.bol.game.service.BoardService;
import com.bol.game.service.rules.GameRule;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mongodb.client.MongoClients;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class GameIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private String createBoard() throws Exception {
        ResultActions response = mockMvc.perform(post("/games")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        MvcResult result = response.andReturn();
        String contentAsString = result.getResponse().getContentAsString();

        JsonParser parser = new JsonParser();
        JsonElement gson = parser.parse(contentAsString);
        return gson.getAsJsonObject().get("id").getAsString();
    }

    @Test
    void test_01_create_board_success() throws Exception {
        mockMvc.perform(post("/games")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty());
    }

    @Test
    void test_02_play_board_success() throws Exception {
        String id = createBoard();
        Integer index = 1;

        mockMvc.perform(put("/games/" + id + "/" + index)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.board[0].stones").value(0))
                .andExpect(jsonPath("$.board[0].index").value(index))
                .andExpect(jsonPath("$.board[6].stones").value(1));
    }

    @Test
    void test_03_wrong_board_id_failed() throws Exception {
        mockMvc.perform(put("/games/4c7254c9-fc6d-4440-bc7d-30218df4660a/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail", is("Board can not found")));
    }

    @Test
    void test_04_over_pit_count_id_failed() throws Exception {
        String id = createBoard();
        mockMvc.perform(put("/games/" + id + "/18")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail", Matchers.containsString("Selected pit (18) must be between 1 and")));
    }

    @Test
    void test_05_wrong_pit_selected_failed() throws Exception {
        String id = createBoard();
        mockMvc.perform(put("/games/" + id + "/10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail", is("Selected pit is not suitable for the selected user. pit id:10")));
    }

    @Test
    void test_06_get_board_success() throws Exception {
        String id = createBoard();

        mockMvc.perform(get("/games/" + id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.board").isArray())
                .andExpect(jsonPath("$.board[0].stones").value(6))
                .andExpect(jsonPath("$.board[6].stones").value(0))
                .andExpect(jsonPath("$.board[6].index").value(7))
                .andExpect(jsonPath("$.board[12].stones").value(6))
                .andExpect(jsonPath("$.board[13].stones").value(0))
                .andExpect(jsonPath("$.board[13].index").value(14));


    }

    @Test
    void test_07_player_get_another_round_success() throws Exception {
        String id = createBoard();
        Integer index = 1;

        mockMvc.perform(put("/games/" + id + "/" + index)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.board[0].stones").value(0))
                .andExpect(jsonPath("$.board[0].index").value(index))
                .andExpect(jsonPath("$.board[6].stones").value(1))
                .andExpect(jsonPath("$.board[6].index").value(7))
                .andExpect(jsonPath("$.board[13].stones").value(0))
                .andExpect(jsonPath("$.board[13].index").value(14));
        ;
    }
}


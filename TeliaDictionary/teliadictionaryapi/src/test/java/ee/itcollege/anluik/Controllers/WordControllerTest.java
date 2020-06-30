package ee.itcollege.anluik.Controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ee.itcollege.anluik.Domain.Word;
import ee.itcollege.anluik.Services.WordService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@WebMvcTest
class WordControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WordService wordService;

    private String baseURI = "/words/";

    /**
     *
     * I tried testing :) doesn't work :) I have no idea currently how to test this correctly.
     *
     */

    @Test
    void getWords() throws Exception {
        Word mockWord = new Word("test", "est");
        Word mockWord2 = new Word("testtwo", "est");

        List<Word> words = new ArrayList<>();
        words.add(mockWord);
        words.add(mockWord2);
        Mockito.when(this.wordService.getAll("est")).thenReturn(words);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(this.baseURI)
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        String expectedJson = this.mapToJson(words);
        String outputInJson = result.getResponse().getContentAsString();
        assertThat(outputInJson).isEqualTo(expectedJson);
    }

    @Test
    void getWord() {
        Word mockWord = new Word("test", "est");
        // String inputInJson = this.mapToJson(mockWord);
        OngoingStubbing<Word> words = Mockito.when(this.wordService.getOneById(Mockito.any(UUID.class)));
    }

    @Test
    void createWord() throws Exception {
        Word mockWord = new Word("test", "est");
        String inputInJson = this.mapToJson(mockWord);
        // Mockito.when(this.wordService.create(Mockito.anyList())).thenReturn(mockWord);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(this.baseURI)
                .accept(MediaType.APPLICATION_JSON).content(inputInJson)
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();

        String outputInJson =  response.getContentAsString();

        assertThat(outputInJson).isEqualTo(inputInJson);
        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
    }

    @Test
    void putWord() {
    }

    @Test
    void deleteWord() {
    }

    /**
     * Maps an object into a JSON String. Uses a JacksonObjectMapper
     */
    private String mapToJson(Object object) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(object);
    }
}
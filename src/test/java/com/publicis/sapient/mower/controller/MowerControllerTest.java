package com.publicis.sapient.mower.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
@SpringBootTest
class MowerControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Test
	void initializeMowers_should_be_ok() throws Exception {
		MockMultipartFile multipartFile = new MockMultipartFile("file", "test.txt", MediaType.TEXT_PLAIN_VALUE,
				("5 5\n" + "1 2 N\n" + "GAGAGAGAA\n" + "3 3 E\n" + "AADAADADDA").getBytes());
		
		mockMvc.perform(multipart("/mower").file(multipartFile))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.[0].coordinate.x").value(1))
				.andExpect(jsonPath("$.[0].coordinate.y").value(3))
				.andExpect(jsonPath("$.[0].orientation").value("N"))
				.andExpect(jsonPath("$.[1].coordinate.x").value(5))
				.andExpect(jsonPath("$.[1].coordinate.y").value(1))
				.andExpect(jsonPath("$.[1].orientation").value("E"));
	}
	
	@Test
	void initializeMowers_should_be_parsing_exception() throws Exception {
		MockMultipartFile multipartFile = new MockMultipartFile("file", "test.txt", MediaType.TEXT_PLAIN_VALUE, ("5 5").getBytes());
		
		mockMvc.perform(multipart("/mower").file(multipartFile)).andDo(print()).andExpect(status().is4xxClientError());
	}
}
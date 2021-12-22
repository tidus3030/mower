package com.publicis.sapient.mower.service;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import com.publicis.sapient.mower.exception.ParsingException;
import com.publicis.sapient.mower.model.Coordinate;
import com.publicis.sapient.mower.model.Movement;
import com.publicis.sapient.mower.model.Mower;
import com.publicis.sapient.mower.model.Orientation;
import com.publicis.sapient.mower.model.Plan;

@ExtendWith(MockitoExtension.class)
class FileServiceTest {
	
	@InjectMocks
	FileService fileService;
	
	@Test
	void parsePlan_should_return_plan() throws IOException {
		// GIVEN
		MockMultipartFile multipartFile = new MockMultipartFile("file", "test.txt", MediaType.TEXT_PLAIN_VALUE, "5 5".getBytes());
		
		// WHEN
		Optional<Plan> plan = fileService.parsePlan(multipartFile);
		
		// THEN
		assertTrue(plan.isPresent());
		Plan expectedPlan = Plan.builder()
				.cornerBottomLeft(Coordinate.builder().x(0).y(0).build())
				.cornerTopRight(Coordinate.builder().x(5).y(5).build())
				.build();
		assertEquals(expectedPlan, plan.get());
	}
	
	@Test
	void parsePlan_should_throw_incorrect_arguments() {
		// GIVEN
		MockMultipartFile multipartFile = new MockMultipartFile("file", "test.txt", MediaType.TEXT_PLAIN_VALUE, "5".getBytes());
		
		// WHEN - THEN
		ParsingException parsingException = assertThrows(ParsingException.class, () -> fileService.parsePlan(multipartFile));
		assertEquals("Incorrect number of arguments in plan", parsingException.getMessage());
	}
	
	@Test
	void parsePlan_should_throw_wrong_arguments() {
		// GIVEN
		MockMultipartFile multipartFile = new MockMultipartFile("file", "test.txt", MediaType.TEXT_PLAIN_VALUE, "5 A".getBytes());
		
		// WHEN - THEN
		ParsingException parsingException = assertThrows(ParsingException.class, () -> fileService.parsePlan(multipartFile));
		assertEquals("Invalid argument type in plan", parsingException.getMessage());
	}
	
	@Test
	void parsePlan_should_be_empty() throws IOException {
		// GIVEN
		MockMultipartFile multipartFile = new MockMultipartFile("file", "test.txt", MediaType.TEXT_PLAIN_VALUE, InputStream.nullInputStream());
		
		// WHEN
		Optional<Plan> plan = fileService.parsePlan(multipartFile);
		
		// THEN
		assertTrue(plan.isEmpty());
	}
	
	@Test
	void parseMowersAndMovements_should_return_map() throws IOException {
		// GIVEN
		MockMultipartFile multipartFile = new MockMultipartFile("file", "test.txt", MediaType.TEXT_PLAIN_VALUE,
				("5 5\n" + "1 2 N\n" + "GAGAGAGAA\n" + "3 3 E\n" + "AADAADADDA").getBytes());
		
		// WHEN
		Map<Mower, List<Movement>> mapMowersMovements = fileService.parseMowersAndMovements(multipartFile);
		
		// THEN
		assertFalse(mapMowersMovements.isEmpty());
		
		Map<Mower, List<Movement>> mapMowersMovementsExpected = Map.of(
				Mower.builder().id(1).coordinate(Coordinate.builder().x(1).y(2).build()).orientation(Orientation.N).build(),
				List.of(Movement.G, Movement.A, Movement.G, Movement.A, Movement.G, Movement.A, Movement.G, Movement.A, Movement.A),
				Mower.builder().id(2).coordinate(Coordinate.builder().x(3).y(3).build()).orientation(Orientation.E).build(),
				List.of(Movement.A, Movement.A, Movement.D, Movement.A, Movement.A, Movement.D, Movement.A, Movement.D, Movement.D, Movement.A));
		assertEquals(mapMowersMovementsExpected, mapMowersMovements);
	}
	
	@Test
	void parseMowersAndMovements_should_throw_incorrect_arguments() {
		// GIVEN
		MockMultipartFile multipartFile = new MockMultipartFile("file", "test.txt", MediaType.TEXT_PLAIN_VALUE,
				("5 5\n" + "1 N\n" + "GAGAGAGAA").getBytes());
		
		// WHEN - THEN
		ParsingException parsingException = assertThrows(ParsingException.class, () -> fileService.parseMowersAndMovements(multipartFile));
		assertEquals("Incorrect number of arguments in mower", parsingException.getMessage());
	}
	
	@Test
	void parseMowersAndMovements_should_throw_wrong_arguments() {
		// GIVEN
		MockMultipartFile multipartFile = new MockMultipartFile("file", "test.txt", MediaType.TEXT_PLAIN_VALUE,
				("5 5\n" + "1 2 Q\n" + "GAGAGAGAA").getBytes());
		
		// WHEN - THEN
		ParsingException parsingException = assertThrows(ParsingException.class, () -> fileService.parseMowersAndMovements(multipartFile));
		assertEquals("Invalid argument type in mower", parsingException.getMessage());
	}
	
	@Test
	void parseMowersAndMovements_should_throw_incorrect_arguments_movements() {
		// GIVEN
		MockMultipartFile multipartFile = new MockMultipartFile("file", "test.txt", MediaType.TEXT_PLAIN_VALUE,
				("5 5\n" + "1 2 N\n" + "GACAGAGAA").getBytes());
		
		// WHEN - THEN
		ParsingException parsingException = assertThrows(ParsingException.class, () -> fileService.parseMowersAndMovements(multipartFile));
		assertEquals("Invalid argument type in movements", parsingException.getMessage());
	}
	
}
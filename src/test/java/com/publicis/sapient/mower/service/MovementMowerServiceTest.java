package com.publicis.sapient.mower.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import com.publicis.sapient.mower.model.Coordinate;
import com.publicis.sapient.mower.model.Movement;
import com.publicis.sapient.mower.model.Mower;
import com.publicis.sapient.mower.model.Orientation;
import com.publicis.sapient.mower.model.Plan;

@ExtendWith(MockitoExtension.class)
class MovementMowerServiceTest {
	
	@InjectMocks
	private MovementMowerService movementMowerService;
	
	@Test
	void moveMower_all_directions() {
		// GIVEN
		Plan plan = Plan.builder()
				.cornerBottomLeft(Coordinate.builder().x(0).y(0).build())
				.cornerTopRight(Coordinate.builder().x(2).y(2).build())
				.build();
		Mower mower = Mower.builder().coordinate(Coordinate.builder().x(0).y(1).build()).orientation(Orientation.E).build();
		List<Movement> movements = List.of(Movement.A, Movement.G, Movement.A, Movement.D, Movement.D, Movement.A, Movement.A, Movement.A, Movement.D,
				Movement.A);
		
		// WHEN
		movementMowerService.moveMower(plan, mower, movements);
		
		// THEN
		Mower expectedMower = Mower.builder().coordinate(Coordinate.builder().x(0).y(0).build()).orientation(Orientation.W).build();
		assertEquals(expectedMower, mower);
	}
}
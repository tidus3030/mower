package com.publicis.sapient.mower.controller;

import java.io.IOException;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.publicis.sapient.mower.exception.ParsingException;
import com.publicis.sapient.mower.model.Movement;
import com.publicis.sapient.mower.model.Mower;
import com.publicis.sapient.mower.model.Plan;
import com.publicis.sapient.mower.service.FileService;
import com.publicis.sapient.mower.service.MovementMowerService;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/mower", produces = MediaType.APPLICATION_JSON_VALUE)
public class MowerController {
	
	private final FileService fileService;
	private final MovementMowerService movementMowerService;
	
	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@ApiOperation(value = "Initialize mowers with file")
	public ResponseEntity<Set<Mower>> initializeMowers(@RequestPart("file") MultipartFile file) throws IOException {
		try {
			// parse plan
			Plan plan = fileService.parsePlan(file).orElseThrow(() -> new RuntimeException("Error when parsing plan"));
			
			// parse mowers and movements
			Map<Mower, List<Movement>> mowerWithMovements = fileService.parseMowersAndMovements(file);
			if (mowerWithMovements.isEmpty()) {
				throw new ParsingException("Error when parsing mowers and their movements");
			}
			
			// execute movements
			mowerWithMovements.forEach((mower, movements) -> movementMowerService.moveMower(plan, mower, movements));
			
			// sort mowers
			Comparator<Mower> comparator = Comparator.comparingInt((Mower mower) -> mower.getCoordinate().getX());
			
			return ResponseEntity.ok(mowerWithMovements.keySet().stream()
					.sorted(comparator)
					.collect(Collectors.toCollection(LinkedHashSet::new)));
		} catch (ParsingException pE) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, pE.getMessage(), pE);
		}
	}
	
}

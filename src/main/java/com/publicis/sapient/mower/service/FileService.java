package com.publicis.sapient.mower.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.publicis.sapient.mower.exception.ParsingException;
import com.publicis.sapient.mower.model.Coordinate;
import com.publicis.sapient.mower.model.Movement;
import com.publicis.sapient.mower.model.Mower;
import com.publicis.sapient.mower.model.Orientation;
import com.publicis.sapient.mower.model.Plan;

@Service
public class FileService {
	
	private Integer id = 1;
	
	/**
	 * Parse plan in first line of file
	 *
	 * @param multipartFile the file
	 * @return plan extracted
	 * @throws IOException error in file
	 */
	public Optional<Plan> parsePlan(MultipartFile multipartFile) throws IOException {
		InputStream inputStream = multipartFile.getInputStream();
		return new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8)).lines()
				.findFirst().stream()
				.map(line -> {
					String[] s = line.split(" ");
					if (s.length != 2) {
						throw new ParsingException("Incorrect number of arguments in plan");
					}
					try {
						return Plan.builder()
								.cornerBottomLeft(Coordinate.builder().x(0).y(0).build())
								.cornerTopRight(Coordinate.builder().x(Integer.parseInt(s[0])).y(Integer.parseInt(s[1])).build())
								.build();
					} catch (NumberFormatException nFE) {
						throw new ParsingException("Invalid argument type in plan", nFE);
					}
				})
				.findAny();
	}
	
	/**
	 * Parse mowers and movements in file
	 *
	 * @param multipartFile the file
	 * @return map which contains mower and their movements
	 * @throws IOException error in file
	 */
	public Map<Mower, List<Movement>> parseMowersAndMovements(MultipartFile multipartFile) throws IOException {
		InputStream inputStream = multipartFile.getInputStream();
		AtomicInteger cpt = new AtomicInteger(1);
		Map<Mower, List<Movement>> mowerWithMovements = new HashMap<>();
		new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8)).lines().skip(1)
				.forEach(line -> handleLine(cpt.getAndIncrement(), line, mowerWithMovements));
		return mowerWithMovements;
	}
	
	private void handleLine(Integer cpt, String line, Map<Mower, List<Movement>> mowerWithMovements) {
		if (cpt % 2 != 0) {
			String[] s = line.split(" ");
			if (s.length != 3) {
				throw new ParsingException("Incorrect number of arguments in mower");
			}
			try {
				mowerWithMovements.put(Mower.builder()
						.id(id++)
						.coordinate(Coordinate.builder().x(Integer.parseInt(s[0])).y(Integer.parseInt(s[1])).build())
						.orientation(Orientation.valueOf(s[2]))
						.build(), new ArrayList<>());
			} catch (IllegalArgumentException iAE) {
				throw new ParsingException("Invalid argument type in mower", iAE);
			}
		} else {
			String[] s = line.split("(?!^)");
			try {
				List<Movement> movements = Arrays.stream(s)
						.map(Movement::valueOf)
						.collect(Collectors.toList());
				mowerWithMovements.entrySet().stream()
						.filter(mowerListEntry -> mowerListEntry.getValue().isEmpty())
						.forEach(mowerListEntry -> mowerListEntry.setValue(movements));
			} catch (IllegalArgumentException iAE) {
				throw new ParsingException("Invalid argument type in movements", iAE);
			}
		}
	}
	
}

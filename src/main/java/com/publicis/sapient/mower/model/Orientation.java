package com.publicis.sapient.mower.model;

import java.util.Arrays;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Orientation {
	
	N(0),
	E(90),
	S(180),
	W(270);
	
	public final Integer degree;
	
	public static Orientation valueOfDegree(Integer degree) {
		return Arrays.stream(Orientation.values())
				.filter(orientation -> orientation.getDegree().equals(degree))
				.findFirst()
				.orElse(null);
	}
}

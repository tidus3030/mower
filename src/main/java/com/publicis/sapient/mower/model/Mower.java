package com.publicis.sapient.mower.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Mower {
	
	private Coordinate coordinate;
	private Orientation orientation;
	
}

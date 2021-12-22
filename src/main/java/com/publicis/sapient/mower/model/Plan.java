package com.publicis.sapient.mower.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Plan {
	
	private Coordinate cornerBottomLeft;
	private Coordinate cornerTopRight;
}

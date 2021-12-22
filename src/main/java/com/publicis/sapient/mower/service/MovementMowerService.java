package com.publicis.sapient.mower.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.publicis.sapient.mower.model.Coordinate;
import com.publicis.sapient.mower.model.Movement;
import com.publicis.sapient.mower.model.Mower;
import com.publicis.sapient.mower.model.Orientation;
import com.publicis.sapient.mower.model.Plan;

@Service
public class MovementMowerService {
	
	/**
	 * Move mower on a plan with list of movements
	 *
	 * @param plan      the plan
	 * @param mower     the mower to move
	 * @param movements the movements
	 */
	public void moveMower(Plan plan, Mower mower, List<Movement> movements) {
		movements.forEach(movement -> moveMower(plan, mower, movement));
	}
	
	private void moveMower(Plan plan, Mower mower, Movement movement) {
		switch (movement) {
			case D:
				turnMowerToRight(mower);
				break;
			case G:
				turnMowerToLeft(mower);
				break;
			case A:
				moveMowerForward(mower, plan);
				break;
		}
	}
	
	private void turnMowerToRight(Mower mower) {
		int newDegree = mower.getOrientation().degree + 90;
		mower.setOrientation(Orientation.valueOfDegree(newDegree == 360 ? 0 : newDegree));
	}
	
	private void turnMowerToLeft(Mower mower) {
		int newDegree = mower.getOrientation().degree - 90;
		mower.setOrientation(Orientation.valueOfDegree(newDegree == -90 ? 270 : newDegree));
	}
	
	private void moveMowerForward(Mower mower, Plan plan) {
		// compute new coordinate
		Coordinate newCoordinate = computeNewCoordinate(mower.getCoordinate(), mower.getOrientation());
		
		// check new coordinate is not offside, if not don't move mower
		if (isInsidePlan(newCoordinate, plan)) {
			mower.setCoordinate(newCoordinate);
		}
	}
	
	private Coordinate computeNewCoordinate(Coordinate coordinate, Orientation orientation) {
		int actualX = coordinate.getX();
		int actualY = coordinate.getY();
		
		switch (orientation) {
			case N:
				actualY++;
				break;
			case E:
				actualX++;
				break;
			case S:
				actualY--;
				break;
			case W:
				actualX--;
				break;
		}
		return Coordinate.builder().x(actualX).y(actualY).build();
	}
	
	private boolean isInsidePlan(Coordinate coordinate, Plan plan) {
		return plan.getCornerBottomLeft().getX() <= coordinate.getX() && coordinate.getX() <= plan.getCornerTopRight().getX()
				&& plan.getCornerBottomLeft().getY() <= coordinate.getY() && coordinate.getY() <= plan.getCornerTopRight().getY();
	}
	
}

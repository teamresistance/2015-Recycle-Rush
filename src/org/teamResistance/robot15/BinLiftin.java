package org.teamResistance.robot15;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class BinLiftin {

	private State state = State.IDLE;

	private int numTotes = 0;

	private static final double[] IDLE_LIFTIN_SPEEDS = new double[] { -0.0, -0.0, -0.0, -0.0, -0.1, -0.1, -0.2, -0.2, -0.2, -0.2 };

	private static final double PICKUP_SPEED = -0.75;
	private boolean previousIndexerState = false;
	private boolean currentIndexerState = false;
	
	private double setDownTimer = 0.0;
	private static final double SET_DOWN_TIME = 10.0;
	private static final double LOWER_SPEED = 0.5;
	private static final double HOME_SPEED = 0.5;

	private boolean currentHomeState = false;
	private boolean previousHomeState = false;
	
	public BinLiftin() {
		previousIndexerState = IO.binLiftIndexer.get();
	}

	public void update(double deltaTime) {
		currentHomeState = IO.binLiftHome.get();
		currentIndexerState  = IO.binLiftIndexer.get();
		SmartDashboard.putNumber("Num totes", numTotes);

		switch (state) {
		case IDLE:
			idleUpdate(deltaTime);
			break;
		case PICKUP:
			pickupUpdate(deltaTime);
			break;
		case SET_DOWN:
			setDownStackUpdate(deltaTime);
			break;
		case HOME:
			homeUpdate(deltaTime);
			break;
		case ZERO:
			zeroUpdate(deltaTime);
			break;
		case INDEX_DOWN:
			indexDownUpdate(deltaTime);
			break;
		default:
			idleUpdate(deltaTime);
			break;
		}
		previousHomeState = currentHomeState;
		previousIndexerState = currentIndexerState;
	}

	private void idleUpdate(double deltaTime) {
		setIdleMotorSpeed();
	}

	private void pickupUpdate(double deltaTime) {
		if (IO.binLiftHome.get() || (currentIndexerState && !previousIndexerState)) {
			state = State.IDLE;
			numTotes++;
			setIdleMotorSpeed();
		} else {
			IO.binLiftinMotor.set(PICKUP_SPEED);
		}
	}
	
	private void indexDownUpdate(double deltaTime) {
		boolean currentIndexerState = IO.binLiftIndexer.get();
		if (currentIndexerState && !previousIndexerState) {
			state = State.IDLE;
			numTotes--;
			setIdleMotorSpeed();
		} else {
			IO.binLiftinMotor.set(-PICKUP_SPEED);
		}
		previousIndexerState = currentIndexerState;
	}

	private void homeUpdate(double deltaTime) {
		if (currentHomeState && !previousHomeState) {
			numTotes = 0;
			setIdleMotorSpeed();
			state = State.ZERO;
		} else {
			IO.binLiftinMotor.set(HOME_SPEED);
		}
	}

	private void zeroUpdate(double deltaTime) {
		if (currentIndexerState && !previousIndexerState) {
			state = State.IDLE;
			setIdleMotorSpeed();
		} else {
			IO.binLiftinMotor.set(-HOME_SPEED);
			// IO.binLiftinMotor.set(0);
		}
	}

	private void setDownStackUpdate(double deltaTime) {
		setDownTimer += deltaTime;
		if (setDownTimer >= SET_DOWN_TIME || (currentHomeState && !previousHomeState)) {
			state = State.IDLE;
			numTotes = 0;
			setIdleMotorSpeed();
			setDownTimer = 0.0;
		} else {
			IO.binLiftinMotor.set(LOWER_SPEED);
		}
	}

	private void setIdleMotorSpeed() {
		IO.binLiftinMotor.set(IDLE_LIFTIN_SPEEDS[numTotes]);
	}

	public void home() {
		if (state == State.IDLE && (!IO.binLiftHome.get() || numTotes > 0)) {
			state = State.HOME;
		}
	}

	public void pickup() {
		if (state == State.IDLE && !IO.binLiftHome.get()) {
			state = State.PICKUP;
		}
	}
	
	public void setDown(){
		if (state == State.IDLE && numTotes > 0) {
			state = State.SET_DOWN;
		}
	}
	
	public void indexDown() {
		if (state == State.IDLE && numTotes > 0) {
			state = State.INDEX_DOWN;
		}
	}
	
	private enum State {
		IDLE, PICKUP, SET_DOWN, HOME, ZERO, INDEX_DOWN
	}
}
package org.teamResistance.robot15;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Terms: - Chain link: chains running vertically along the robot
 * 		  - Shuttle: box on the chains (vertical movement)
 * 		  - Arm: static extension connecting the claw to the shuttle
 * 		  - Wrist: swivels the claw (horizontal rotation)
 * 		  - Claw: grabbing mechanism
 */

public class ContainerLiftin {
	private static final double CLAW_OPEN_SPEED = 0.75;
	
	private State state = State.IDLE;
	
	private PID wristPID;
	private PID shuttlePID;
	
	private double clawDelay = 0.0;
	
	public ContainerLiftin() {
		wristPID = new PID(IO.wristPot, IO.wristMotor);
		topWrist();
		wristPID.setPID(-0.1, 0.0, 0.0);
		wristPID.setMin(.338);
		wristPID.setMax(.666);
		wristPID.setDeadband(0.0);
		wristPID.setDefaultSpeed(0.0);
		
		shuttlePID = new PID(IO.shuttlePot, IO.shuttleMotor);
		binLiftinBack();
		shuttlePID.setPID(0.1, 0.0, 0.0);
		shuttlePID.setMax(.88);
		shuttlePID.setMin(.07);
		shuttlePID.setDeadband(0);
		shuttlePID.setDefaultSpeed(0);
		shuttlePID.setInverted(true);
		shuttlePID.setMaxOutput(.8);
		shuttlePID.setMinOutput(-.8);
	}

	public void update(double deltaTime) {
		SmartDashboard.putNumber("Claw State", state.ordinal());
		switch(state) {
		case IDLE:
			break;
		case TOGGLE_CLAW:
			toggleClawUpdate();
			break;
		case CLAW_DELAY:
			delayUpdate(deltaTime);
			break;
		case CLAW_WAIT:
			clawWaitUpdate();
			break;
		}
		
		double p = SmartDashboard.getNumber("P");
		double i = SmartDashboard.getNumber("I");
		double d = SmartDashboard.getNumber("D");
		wristPID.setPID(p, i, d);
		
		wristUpdate(deltaTime);
		
		shuttlePID.setP(SmartDashboard.getNumber("Shuttle P"));
		shuttlePID.setI(SmartDashboard.getNumber("Shuttle I"));
		shuttlePID.setD(SmartDashboard.getNumber("Shuttle D"));
		shuttleUpdate(deltaTime);
	}
	
	/**
	 * Toggle the claw if another process is not occuring.
	 */
	public void toggleClaw() {
		if(state == State.IDLE) {
			state = State.TOGGLE_CLAW;
		}
	}

	/**
	 * Toggle the claw and wait for the process to finish.
	 */
	private void toggleClawUpdate() {
		if (IO.clawOpenedLimit.get()) {
			IO.clawMotor.set(-CLAW_OPEN_SPEED);
		} else if (IO.clawClosedLimit.get()) {
			IO.clawMotor.set(CLAW_OPEN_SPEED);
		} else {
			IO.clawMotor.set(CLAW_OPEN_SPEED);
		}
		state = State.CLAW_DELAY;
	}
	
	/**
	 * Stops opening or closing the claw if the limit switch is hit 
	 */
	private void clawWaitUpdate() {
		if(IO.clawOpenedLimit.get() || IO.clawClosedLimit.get()) {
			IO.clawMotor.set(0);
			state = State.IDLE;
		}
	}
	
	private void delayUpdate(double deltaTime) {
		clawDelay += deltaTime;
		if(clawDelay >= 1) {
			state = State.CLAW_WAIT;
			clawDelay = 0.0;
		}
	}
	
	/**
	 * Moves the shuttle to a set position
	 */
	private void shuttleUpdate(double deltaTime) {
		shuttlePID.update(deltaTime);
	}
	
	/**
	 * Moves the wrist to a set position
	 */
	private void wristUpdate(double deltaTime) {
		wristPID.update(deltaTime);
	}
	
	public void centerWrist() {
		wristPID.setSetpoint(.49);
	}
	
	public void topWrist() {
		wristPID.setSetpoint(.33);
	}
	
	public void topShuttle() {
		shuttlePID.setSetpoint(.08);
		centerWrist();
	}
	
	public void bottomShuttle() {
		shuttlePID.setSetpoint(.89);
		if(IO.wristPot.get() < .49) {
			centerWrist();
		}
	}
	
	public void binLiftnForward() {
		shuttlePID.setSetpoint(.46);
		topWrist();
	}
	
	public void binLiftinBack() {
		shuttlePID.setSetpoint(.716);
		topWrist();
	}
	
	public void setShuttleSetpoint(double setpoint) {
		shuttlePID.setSetpoint(setpoint);
	}
	
	public double getShuttleSetpoint() {
		return shuttlePID.getSetpoint();
	}
	
	public void setWristSetpoint(double setpoint) {
		wristPID.setSetpoint(setpoint);
	}
	
	public double getWristSetpoint() {
		return wristPID.getSetpoint();
	}
	
	private enum State {
		IDLE,
		TOGGLE_CLAW,
		CLAW_DELAY,
		CLAW_WAIT
	}
}

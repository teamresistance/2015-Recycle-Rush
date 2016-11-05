package org.teamResistance.robot15;

import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class PID {
	
	private double kP, kI, kD, kF;
	
	private AnalogPotentiometer input;
	private SpeedController output;
	
	private double totalError = 0.0;
	private double previousError = 0.0;
	
	private double setpoint = 0.0;

	private double maxOutput = 1.0;
	private double minOutput = -1.0;
	
	private double min = 0.0;
	private double max = 1.0;
	
	private boolean inverted = false;
	
	private double deadband = 0.0;
	private double defaultSpeed = 0.0;
	
	public PID(AnalogPotentiometer input, SpeedController output) {
		this.input = input;
		this.output = output;
		this.kP = 0.0;
		this.kI = 0.0;
		this.kD = 0.0;
		this.kF = 0.0;
	}
	
	public double update(double deltaTime) {
		double pos = input.get();
		double error = setpoint - pos;
		
		double result = 0;
		
		if(onTarget(error)) {
			output.set(defaultSpeed);
			
		} else {
		
			totalError += error;
				
			result = (error * kP) + (totalError * kI * deltaTime) + ((error - previousError) * kD / deltaTime) + setpoint * kF;
			previousError = error;
			
			if(result > maxOutput) result = maxOutput;
			else if(result < minOutput) result = minOutput;
			
			if((pos <= min && result <= 0) || (pos >= max && result >= 0)) {
				output.set(0);
				return 0;
			} else {
				output.set(result * (inverted ? -1 : 1));
				return result * (inverted ? -1 : 1);
			}
		}
		return 0;
	}
	
	private boolean onTarget(double value) {
		return Math.abs(value) < deadband;
	}
	
	public void setPID(double kP, double kI, double kD) {
		this.kP = kP;
		this.kI = kI;
		this.kD = kD;
		this.kF = 0.0;
	}
	
	public void setPIDF(double kP, double kI, double kD, double kF) {
		this.kP = kP;
		this.kI = kI;
		this.kD = kD;
		this.kF = kF;
	}
	
	public double getP() {
		return kP;
	}

	public void setP(double kP) {
		this.kP = kP;
	}

	public double getI() {
		return kI;
	}

	public void setI(double kI) {
		this.kI = kI;
	}

	public double getD() {
		return kD;
	}

	public void setD(double kD) {
		this.kD = kD;
	}

	public double getF() {
		return kF;
	}

	public void setF(double kF) {
		this.kF = kF;
	}

	public AnalogPotentiometer getInput() {
		return input;
	}

	public void setInput(AnalogPotentiometer input) {
		this.input = input;
	}

	public SpeedController getOutput() {
		return output;
	}

	public void setOutput(SpeedController output) {
		this.output = output;
	}
	
	public double getSetpoint() {
		return setpoint;
	}

	public void setSetpoint(double setpoint) {
		if(setpoint >= max)
			this.setpoint = max;
		else if(setpoint <= min)
			this.setpoint = min;
		else
			this.setpoint = setpoint;
	}

	public double getMaxOutput() {
		return maxOutput;
	}

	public void setMaxOutput(double maxOutput) {
		this.maxOutput = maxOutput;
	}

	public double getMinOutput() {
		return minOutput;
	}

	public void setMinOutput(double minOutput) {
		this.minOutput = minOutput;
	}

	public double getMin() {
		return min;
	}

	public void setMin(double min) {
		this.min = min;
	}

	public double getMax() {
		return max;
	}

	public void setMax(double max) {
		this.max = max;
	}

	public boolean isInverted() {
		return inverted;
	}

	public void setInverted(boolean inverted) {
		this.inverted = inverted;
	}

	public double getDeadband() {
		return deadband;
	}

	public void setDeadband(double deadband) {
		this.deadband = deadband;
	}

	public double getDefaultSpeed() {
		return defaultSpeed;
	}

	public void setDefaultSpeed(double defaultSpeed) {
		this.defaultSpeed = defaultSpeed;
	}
}

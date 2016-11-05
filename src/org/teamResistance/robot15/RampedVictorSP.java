package org.teamResistance.robot15;

import edu.wpi.first.wpilibj.VictorSP;

public class RampedVictorSP extends VictorSP {

	private double maxMotorChange = 0.01;
	private double currentMotorOutput = 0.0;
	
	public RampedVictorSP(int channel) {
		super(channel);
	}

	@Override
	public void set(double speed) {
		double requestedChange = currentMotorOutput - speed;
		if(Math.abs(requestedChange) > maxMotorChange) {
			if(requestedChange < 0) {
				currentMotorOutput += maxMotorChange;
			} else {
				currentMotorOutput -= maxMotorChange;
			}
		} else {
			currentMotorOutput += requestedChange;
		}
		super.set(currentMotorOutput);
	}

	public double getMaxMotorChange() {
		return maxMotorChange;
	}

	public void setMaxMotorChange(double maxMotorChange) {
		this.maxMotorChange = maxMotorChange;
	}

	public double getCurrentMotorOutput() {
		return currentMotorOutput;
	}
}

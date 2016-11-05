package org.teamResistance.robot15;

public class DigitalInput extends edu.wpi.first.wpilibj.DigitalInput {

	private boolean inverted = true;
	
	public DigitalInput(int channel) {
		super(channel);
	}

	@Override
	public boolean get() {
		return super.get() ^ inverted; // Conditionally inverts the output
	}

	public boolean isInverted() {
		return inverted;
	}

	public void setInverted(boolean inverted) {
		this.inverted = inverted;
	}
}

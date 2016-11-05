package org.teamResistance.robot15;

import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.SPI.Port;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class OpticalFlowSensor {
	
	private SPI spi;
	private byte[] dataReceived;
	private byte[] register = new byte[] {0};
	
	private double x = 0;
	private double y = 0;
	
	private double ticksPerFootX = 268;
	private double ticksPerFootY = 275;
	
	public OpticalFlowSensor() {
		spi = new SPI(Port.kOnboardCS0);
		spi.setChipSelectActiveLow();
		spi.setClockActiveHigh();
		spi.setClockRate(500000);
		
		dataReceived = new byte[1];
		for(int i = 0; i < dataReceived.length; i++) {
			dataReceived[i] = 0;
		}
	}
	
	public void init() {
		int motionRegister = readRegister((byte)2);
		SmartDashboard.putNumber("Motion Register", motionRegister);
		if((motionRegister & 0x80) != 0) {
			readRegister((byte)3);
			readRegister((byte)4);
		}
		x = 0;
		y = 0;
	}
	
	public void update() {
		SmartDashboard.putNumber("Product ID", readRegister((byte)0));
//		Timer.delay(0.05);
//		SmartDashboard.putNumber("Squal", readRegister((byte)5));
//		
		int rawDx = 0;
		int rawDy = 0;
		int motionRegister = readRegister((byte)2);
		SmartDashboard.putNumber("Motion Register", motionRegister);
		if((motionRegister & 0x80) != 0) {
			rawDy = readRegister((byte)3);
			rawDx = -readRegister((byte)4);
		}
		double rad = Math.toRadians(IO.gyro.getAngle());
		double cos = Math.cos(rad);
		double sin = Math.sin(rad);
		double dx = rawDx / ticksPerFootX;
		double dy = rawDy / ticksPerFootY;
		x += dx * cos - dy * sin;
		y += dx * sin + dy * cos;
		SmartDashboard.putNumber("Delta X", (double)rawDx);
		SmartDashboard.putNumber("Delta Y", (double)rawDy);
		SmartDashboard.putNumber("X", x);
		SmartDashboard.putNumber("Y", y);
	}
	
	private int readRegister(byte register) {
		this.register[0] = register;
		spi.write(this.register, 1); // Writes the register to be read
		spi.read(true, dataReceived, 1); // Reads the garbage
		spi.read(false, dataReceived, 1); // Reads the real register value
		return dataReceived[0];
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}
	
}

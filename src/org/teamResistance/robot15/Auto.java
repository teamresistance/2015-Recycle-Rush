package org.teamResistance.robot15;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Auto {
	
	private int state = 0;
	
	private double distance = 5;
	private double speedY = 0.5;
	private double speedX = 0.5;
	
	private double time = 0;
	private double delay = 1;
	
	private double previousTime = System.currentTimeMillis();
	
	private RawPID xPID;
	private RawPID yPID;
	
	public void init() {
		previousTime = System.currentTimeMillis() / 1000;
		IO.opticalFlow.setX(0);
		IO.opticalFlow.setY(0);
		
		xPID = new RawPID();
		yPID = new RawPID();
		SmartDashboard.putNumber("auto drive x p", 0.01);
		xPID.setP(0.01);
		SmartDashboard.putNumber("auto drive y p", 0.01);
		yPID.setP(0.01);
		xPID.setSetpoint(0);
		yPID.setSetpoint(0);
		xPID.setMax(speedX);
		xPID.setMin(-speedX);
		yPID.setMax(speedY);
		yPID.setMin(-speedY);
		yPID.setInverted(true);
	}
	
	public void update() {
		double currentTime = System.currentTimeMillis() / 1000;
		double deltaTime = currentTime - previousTime;
		previousTime = currentTime;
		
		
		xPID.setP(SmartDashboard.getNumber("auto drive x p", 0.01));
		yPID.setP(SmartDashboard.getNumber("auto drive y p", 0.01));
		
		IO.opticalFlow.update();
		switch(state) {
		case 0:
			if(Math.abs(yPID.getError()) > 0.2) {
				xPID.setSetpoint(0);
				yPID.setSetpoint(5);
			} else {
				state++;
			}
			break;
		case 1:
			xPID.setSetpoint(0);
			yPID.setSetpoint(5);
			time += deltaTime;
			if(time > delay) {
				state++;
				time = 0;
			}
			break;
		case 2:
			if(Math.abs(xPID.getError()) > 0.2) {
				xPID.setSetpoint(5);
				yPID.setSetpoint(5);
			} else {
				state++;
			}
			break;
		case 3:
			xPID.setSetpoint(5);
			yPID.setSetpoint(5);
			time += deltaTime;
			if(time > delay) {
				state++;
				time = 0;
			}
			break;
		case 4:
			if(Math.abs(yPID.getError()) > 0.2) {
				xPID.setSetpoint(5);
				yPID.setSetpoint(0);
			} else {
				state++;
			}
			break;
		case 5:
			xPID.setSetpoint(5);
			yPID.setSetpoint(0);
			time += deltaTime;
			if(time > delay) {
				state++;
				time = 0;
			} 
			break;
		case 6:
			if(Math.abs(xPID.getError()) > 0.2) {
				xPID.setSetpoint(0);
				yPID.setSetpoint(0);
			} else {
				state++;
			}
			break;
		case 7:
			xPID.setSetpoint(0);
			yPID.setSetpoint(0);
			time += deltaTime;
			if(time > delay) {
				state++;
				time = 0;
			} 
			break;
		default:
			xPID.setSetpoint(0);
			yPID.setSetpoint(0);
			//IO.mecanumDrive.drive(0, 0, 0, 0);
			break;
		}
		double x = xPID.update(deltaTime, IO.opticalFlow.getX());
		double y = yPID.update(deltaTime, IO.opticalFlow.getY());
		SmartDashboard.putNumber("Auto x", x);
		SmartDashboard.putNumber("Auto y", y);
		IO.mecanumDrive.drive(x, y, 0, 0);
	}
	
}

package org.teamResistance.robot15;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class PIDWrist {
	
	private PIDController controller;
	
	public void init() {
		controller = new PIDController(0.7, 0, 0, 0, IO.wristPot, IO.wristMotor);
    	controller.setOutputRange(-0.3, 0.3);
    	controller.setAbsoluteTolerance(0.002);
    	controller.setInputRange(0.2, 0.8);
//    	
    	controller.setSetpoint(0.5);
//    	
    	IO.wristMotor.setInverted(true);
    	controller.reset();
    	controller.enable();
	}
	
	public void update() {
    	double kP = SmartDashboard.getNumber("PID P", 0.7);
    	double kI = SmartDashboard.getNumber("PID I", 0.0);
    	double kD = SmartDashboard.getNumber("PID D", 0.0);
    	double kF = SmartDashboard.getNumber("PID F", 0.0);
    	double tolerance = SmartDashboard.getNumber("PID Tolerance", 0.002);
    	double min = SmartDashboard.getNumber("PID Output Min", -0.3);
    	double max = SmartDashboard.getNumber("PID Output Max", 0.3);
    	
    	SmartDashboard.putNumber("Setpoint", controller.getSetpoint());
    	SmartDashboard.putNumber("Error", controller.getError());
    	SmartDashboard.putNumber("Error Graph", controller.getError());
    	
    	SmartDashboard.putNumber("-Motor Output", controller.get());
    	SmartDashboard.putNumber("-Motor Output Graph", controller.get());
    	
    	double angle = IO.wristPot.get();
    	if(angle < 0.185 || angle > 0.785) {
    		controller.disable();
    		IO.wristMotor.set(0);
    	}
    	
//    	if(Math.abs(controller.getError()) < 0.01){
//    		controller.reset();
//    		controller.enable();
//    	}
    	
    	controller.setPID(kP, kI, kD, kF);
    	
//    	if(Math.abs(controller.getError()) < SmartDashboard.getNumber("I Deadband", 0.02)) {
//    		controller.setPID(kP / 2, kI, kD, kF);
//    		hold = true;
//    	} else {
//    		if(hold) {
//	    		controller.reset();
//	    		controller.enable();
//	    		hold = false;
//    		}
//    		controller.setPID(kP, 0, kD, kF);
//    	}
    	
    	//controller.setPID(kP, kI, kD, kF);
    	controller.setAbsoluteTolerance(tolerance);
    	controller.setOutputRange(min, max);
    	//teleOp.update();
    	SmartDashboard.putNumber("Wrist Angle", angle);
    	SmartDashboard.putNumber("Wrist", angle);
    	
    	if(IO.leftJoystick.getRawButton(3)) {
    		if(IO.clawClosedLimit.get()) {
    			controller.setSetpoint(0.472);
    		} else {
    			controller.setSetpoint(0.5);
    		}
//    		controller.reset();
//    		controller.enable();
    	} else if(IO.leftJoystick.getRawButton(4)) {
    		controller.setSetpoint(0.755);
//    		controller.reset();
//    		controller.enable();
    	} else if(IO.leftJoystick.getRawButton(5)) {
    		controller.setSetpoint(0.254);
//    		controller.reset();
//    		controller.enable();
    	}
	}
	
}

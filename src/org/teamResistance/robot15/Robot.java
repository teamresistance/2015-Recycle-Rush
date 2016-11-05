package org.teamResistance.robot15;

import edu.wpi.first.wpilibj.IterativeRobot;

public class Robot extends IterativeRobot {

	private TeleOp teleOp;
//	private Autonomous auto;
	
	private Auto auto;
	
//	private Wrist wrist;
	
//	private PIDWrist wrist;
	
//	
//	private boolean hold = false;
	
	
	public Robot() {
		
	}
	
    public void robotInit() {
//    	wrist = new Wrist();
    	IO.init();
    	
    	teleOp = new TeleOp();
//    	auto = new Autonomous();
    	auto = new Auto();
    	
//    	wrist = new PIDWrist();
    }   

    public void autonomousInit() {
    	auto.init();
    }

    public void autonomousPeriodic() {
    	auto.update();
    }

    public void teleopInit() {
    	IO.opticalFlow.init();
//    	wrist.init();
    	teleOp.init();
//    	wrist.init();

    }

    public void teleopPeriodic() {
    	IO.opticalFlow.update();
//    	wrist.update();
//    	double error = 0.51 - IO.wristPot.get();
//    	if(hold) {
//    		IO.wristMotor.set(SmartDashboard.getNumber("Holding", 0.1));
//    	} else {
//    		if(Math.abs(error) < SmartDashboard.getNumber("PID Tolerance", 0.01)) {
//        		hold = true;
//        	} else if(error < 0) {
//        		IO.wristMotor.set(SmartDashboard.getNumber("Speed"));
//        	} else if(error > 0) {
//        		IO.wristMotor.set(-SmartDashboard.getNumber("Speed"));
//        	} else {
//        		IO.wristMotor.set(0);
//        	}
//    	}
//    	
    	teleOp.update();
    	
    }

	@Override
	public void testInit() {
		
	}

	@Override
	public void testPeriodic() {
		
	}
    
}

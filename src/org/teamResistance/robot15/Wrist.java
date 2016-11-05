package org.teamResistance.robot15;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


public class Wrist {
	
    private static final int HOLD_CURRENT = 0;
    private static final int MOVE_SPEED = 1;
    private static final int RELOCATE_SPEED = 2;
    private static final int HOLD_OFFSET = 3;
	
	private double zoneParams[][][] = 
			new double[][][]
			{ { { 17.0, 45.0, 10.0, 0.0 },		//Zone0, No Can
				{ 20.0, 50.0, 10.0, 0.0 },	//Zone1, No Can
				{ 22.0, 70.0, 10.0, 0.0 },	//Zone2, No Can (Mid)
				{ 20.0, 50.0, 10.0, 0.0 },	//Zone3, No Can
				{ 17.0, 45.0, 10.0, 0.0 } },	//Zone4, No Can
				{
				{ 22.0, 50.0, 10.0, 0.0 },	//Zone0, Can
				{ 25.0, 65.0, 10.0, 0.0 },	//Zone1, Can
				{ 28.0, 72.0, 10.0, 0.0 },	//Zone2, Can (Mid)
				{ 25.0, 65.0, 10.0, 0.0 },	//Zone3, Can
				{ 22.0, 50.0, 10.0, 0.0 } } }; //Zone4, Can
	
	private double position;
	private double setpoint;
	
	private int wristZone;
	private int canState;
	
	private boolean holdPosition = false;
	
	private double previousErrorSign = 0;
	private double wristOut = 0;
	
	public void init() {
		//setpoint = Util.span(IO.wristPot.get(), 0.325, 0.702, 100.0, -100.0);
		setpoint = 0;
	}
	
	public void update() {
		position = Util.span(IO.wristPot.get(), 0.325, 0.702, 100.0, -100.0);
		
		double error = position - setpoint;
		double errorSign = error / Math.abs(error);
		
		SmartDashboard.putNumber("Position", position);
		SmartDashboard.putNumber("Setpoint", setpoint);
		SmartDashboard.putNumber("error", error);
		SmartDashboard.putNumber("error sign", errorSign);
		
		if ( position < -60 ) {
        	wristZone = 0;
        } else if ( position < -30 ) {
        	wristZone = 1;
        } else if ( position < 30 ) {
        	wristZone = 2;
        } else if ( position < 60 ) {
        	wristZone = 3;
        } else {
        	wristZone = 4;
        }
		SmartDashboard.putNumber("Zone", wristZone);
		zoneParams[0][2][HOLD_CURRENT] = SmartDashboard.getNumber("Hold Current", 10.0);
		
		if(IO.clawClosedLimit.get()) {
			canState = 1;
		} else  {
			canState = 0;
		}
		
		if (!holdPosition) {
			holdPosition = previousErrorSign != errorSign;
    	}
		
		
		double relocateDeadband = SmartDashboard.getNumber("Relocate Deadband", 10.0);
		double holdDeadband = SmartDashboard.getNumber("Hold Deadband", 5.0);
		wristOut = 0;
    	if ( !holdPosition ) {
    		wristOut = -errorSign * zoneParams[canState][wristZone][MOVE_SPEED];
    	} else if ( Math.abs( error ) > relocateDeadband ) {
    		wristOut = -errorSign * zoneParams[canState][wristZone][RELOCATE_SPEED];
    	} else if ( Math.abs( error ) > holdDeadband ) {
    			if ( error < 0.0 ) {
    				zoneParams[canState][wristZone][HOLD_OFFSET] += 0.3;	//Low add some
    				if ( zoneParams[canState][wristZone][HOLD_OFFSET] > holdDeadband )	//Limit to +2%
    					zoneParams[canState][wristZone][HOLD_OFFSET] = 2.0;
    			} else {
    				zoneParams[canState][wristZone][HOLD_OFFSET] += -0.2;	//Hi ease off some
    				if ( zoneParams[canState][wristZone][HOLD_OFFSET] < holdDeadband )	//Limit to -2%
    					zoneParams[canState][wristZone][HOLD_OFFSET] = -2.0;
    			}
    	}
    	SmartDashboard.putBoolean("Hold position", holdPosition);
    	SmartDashboard.putNumber("out", wristOut);
    	wristOut += zoneParams[canState][wristZone][HOLD_CURRENT] + zoneParams[canState][wristZone][HOLD_OFFSET];
    	
    	SmartDashboard.putNumber("Hold offset", zoneParams[canState][wristZone][HOLD_OFFSET]);
    	
    	if(Math.abs(position) > 95.0)
    		wristOut = 0.0;
    	SmartDashboard.putNumber("Pre wrist out", wristOut);
    	SmartDashboard.putNumber("Post wrist out", Util.span(wristOut, -100.0, 100.0, -1.0, 1.0));
    	IO.wristMotor.set(Util.span(wristOut, -100.0, 100.0, -1.0, 1.0));
    	
    	previousErrorSign = errorSign;
	}
	
	public void setSetpoint(double setpoint) {
		holdPosition = false;
		
	}
	
}

package org.teamResistance.robot15;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class TeleOp {
	
	private long previousTime;
	
	boolean prevState = false;
	
	boolean fieldOrientedState = false;
	
	boolean onBumperDown = false;
	
	private boolean firstIteration;
	
	public TeleOp() {
		firstIteration = true;
	}
	
	/**
	 * Called once at the beginning of teleop.
	 */
	public void init() {
		previousTime = System.currentTimeMillis();

		SmartDashboard.putNumber("Wrist RoC", 0.01);
		SmartDashboard.putNumber("Max Motor Out", 0.8);
		SmartDashboard.putNumber("Min Motor Out", -0.8);
	}
	
	/** 
	 * Called periodically during teleop.
	 */
	public void update() {
		long currentTime = System.currentTimeMillis();
		double deltaTime = (currentTime - previousTime) / 1000.0;
		previousTime = currentTime;
		
//		IO.wristMotor.setMaxMotorChange(SmartDashboard.getNumber("Wrist RoC"));
		
		boolean is_calibrating = IO.imu.isCalibrating();
        if ( firstIteration && !is_calibrating ) {
            Timer.delay( 0.3 );
            IO.imu.zeroYaw();
            firstIteration = false;
        }
        
        // Update the dashboard with status and orientation
        // data from the nav6 IMU
        
        SmartDashboard.putBoolean(  "IMU_Connected",        IO.imu.isConnected());
        SmartDashboard.putBoolean(  "IMU_IsCalibrating",    IO.imu.isCalibrating());
        SmartDashboard.putNumber(   "IMU_Yaw",              IO.imu.getYaw());
        SmartDashboard.putNumber(   "IMU_Pitch",            IO.imu.getPitch());
        SmartDashboard.putNumber(   "IMU_Roll",             IO.imu.getRoll());
        SmartDashboard.putNumber(   "IMU_CompassHeading",   IO.imu.getCompassHeading());

        // If you are using the IMUAdvanced class, you can also access the following
        // additional functions, at the expense of some extra processing
        // that occurs on the CRio processor
        
        SmartDashboard.putNumber(   "IMU_Accel_X",         IO.imu.getWorldLinearAccelX());
        SmartDashboard.putNumber(   "IMU_Accel_Y",         IO.imu.getWorldLinearAccelY());
        SmartDashboard.putBoolean(  "IMU_IsMoving",        IO.imu.isMoving());
        SmartDashboard.putNumber(   "IMU_Temp_C",          IO.imu.getTempC());
                                                          
        SmartDashboard.putNumber(   "Velocity_X",          IO.imu.getVelocityX() );
        SmartDashboard.putNumber(   "Velocity_Y",          IO.imu.getVelocityY() );
        SmartDashboard.putNumber(   "Displacement_X",      IO.imu.getDisplacementX() * 3.280 );
        SmartDashboard.putNumber(   "Displacement_Y",      IO.imu.getDisplacementY() * 3.280 );
		
		IO.gyro.smartdashboardPut();
		
		IO.mecanumDrive.setkP(SmartDashboard.getNumber("Drive P", 1));
		IO.mecanumDrive.setkI(SmartDashboard.getNumber("Drive I", 0));
		IO.mecanumDrive.setkD(SmartDashboard.getNumber("Drive D", 0));
		IO.mecanumDrive.setMaxOutput(SmartDashboard.getNumber("Max Motor Out", 0.8));
		IO.mecanumDrive.setMinOutput(SmartDashboard.getNumber("Min Motor Out", -0.8));
		IO.mecanumDrive.drive(IO.leftJoystick.getX(), IO.leftJoystick.getY(), IO.rightJoystick.getX(), IO.codriverBox.getRotation());
		
		IO.codriverBox.update(deltaTime);
		
		if(IO.leftJoystick.getRawButton(11)) { 
    		IO.opticalFlow.setX(0);
        	IO.opticalFlow.setY(0);
        	IO.imu.resetDisplacement();
    	}
		
//		if(IO.codriverBox.getButton(CodriverBox.BIN_LIFTIN_HOME)) {
//			IO.binLiftin.home();
//			System.out.println("Home Pressed");
//		} 
//		
//		boolean currentBumper = IO.binBumperLeft.get() && IO.binBumperRight.get();
//		if(IO.codriverBox.getButton(CodriverBox.BIN_LIFTIN_PICKUP)|| (currentBumper && !onBumperDown)) {
//			IO.binLiftin.pickup();
//			System.out.println("Pick Up Pressed");
//		} 
//		onBumperDown = currentBumper;
//		
//		if(IO.codriverBox.getButton(CodriverBox.BIN_LIFTIN_DROP)) {
//			IO.binLiftin.setDown();
//			System.out.println("Drop Pressed");
//		} 
//		if(IO.codriverBox.getButton(CodriverBox.BIN_LIFTIN_INDEX_DOWN)) {
//			IO.binLiftin.indexDown();
//			System.out.println("Index Down Pressed");
//		}
//		
//		if(IO.codriverBox.getButton(CodriverBox.SHUTTLE_HIGH)) {
//			IO.containerLiftin.topShuttle();
//		}
//		if(IO.codriverBox.getButton(CodriverBox.SHUTTLE_LOW)) {
//			IO.containerLiftin.bottomShuttle();
//		}
//		
//		if(IO.codriverBox.getButton(CodriverBox.CLAW_TOP)) {
//			IO.containerLiftin.topWrist();
//		}
//		if(IO.codriverBox.getButton(CodriverBox.CLAW_CENTER)) {
//			IO.containerLiftin.centerWrist();
//		}
//		if(IO.codriverBox.getButton(CodriverBox.BIN_LIFTIN_FORWARD)) {
//			IO.containerLiftin.binLiftnForward();
//		} 
//		if(IO.codriverBox.getButton(CodriverBox.BIN_LIFTIN_BACK)) {
//			IO.containerLiftin.binLiftinBack();
//		}
//		
//		if(IO.codriverBox.getButton(CodriverBox.CLAW_TOGGLE)) {
//			IO.containerLiftin.toggleClaw();
//		}
		
		if(IO.leftJoystick.getRawButton(6)) {
			IO.gyro.reset();
		}
		
		boolean currentOrientationState = IO.leftJoystick.getRawButton(7);
		if(!fieldOrientedState && currentOrientationState) {
			IO.mecanumDrive.nextState();
		}
		fieldOrientedState = !currentOrientationState;
		
//		SmartDashboard.putBoolean("Left", IO.binBumperLeft.get());
//		SmartDashboard.putBoolean("Right", IO.binBumperRight.get());
//		
//		SmartDashboard.putNumber("Shuttle Pot", IO.shuttlePot.get());
//		SmartDashboard.putNumber("Wrist Pot", IO.wristPot.get());
//		
//		IO.containerLiftin.update(deltaTime);
//		IO.binLiftin.update(deltaTime);
		
		SmartDashboard.putNumber("Rotation knob Angle", IO.codriverBox.getRotation());
		
//		IO.containerLiftin.setShuttleSetpoint(IO.containerLiftin.getShuttleSetpoint() + (SmartDashboard.getNumber("Shuttle Sensitivity") * IO.codriverBox.getShuttleSpeed()));
//		IO.containerLiftin.setWristSetpoint(IO.containerLiftin.getWristSetpoint() + (SmartDashboard.getNumber("Wrist Sensitivity") * IO.codriverBox.getWristSpeed()));
	}
	
}

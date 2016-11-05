package org.teamResistance.robot15;

import com.kauailabs.navx_mxp.AHRS;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.RobotDrive.MotorType;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;

public class IO {

	public static BinLiftin binLiftin;
	public static ContainerLiftin containerLiftin;

	public static Victor rightFront;
	public static Victor leftFront;
	public static Victor leftRear;
	public static Victor rightRear;

	public static VictorSP binLiftinMotor;
	public static VictorSP shuttleMotor;

	public static InvertableVictor wristMotor;
	public static VictorSP clawMotor;

	public static RobotDrive drive;

	public static Joystick leftJoystick;
	public static Joystick rightJoystick;
	public static CodriverBox codriverBox;

	public static JoystickButton clawToggleButton;

	public static OpticalFlowSensor opticalFlow;
	public static DualGyro gyro;
	public static AHRS imu;
	public static SerialPort serial_port;

	public static MecanumDrive mecanumDrive;

	public static DigitalInput binLiftHome;
	public static DigitalInput binLiftIndexer;

	public static DigitalInput binBumperLeft;
	public static DigitalInput binBumperRight;

	public static DigitalInput clawOpenedLimit;
	public static DigitalInput clawClosedLimit;

	public static AnalogPotentiometer shuttlePot;
	public static AnalogPotentiometer wristPot;

	public static void init() {
		leftFront = new Victor(0); // TODO: Change to victorSP
		rightFront = new Victor(2);
		leftRear = new Victor(1);
		rightRear = new Victor(3);

		wristMotor = new InvertableVictor(5);
		clawMotor = new VictorSP(6);
		shuttleMotor = new VictorSP(8);
		binLiftinMotor = new VictorSP(7);

		clawOpenedLimit = new DigitalInput(4);
		clawClosedLimit = new DigitalInput(5);

		leftJoystick = new Joystick(0);
		rightJoystick = new Joystick(1);
		codriverBox = new CodriverBox(2);

		binLiftHome = new DigitalInput(2); // Check to see if these are correct
		binLiftIndexer = new DigitalInput(3);

		shuttlePot = new AnalogPotentiometer(2); // Need to figure out what the
													// offset and the full range
													// is
		wristPot = new AnalogPotentiometer(3);

		clawToggleButton = new JoystickButton(leftJoystick, 3);

		// binLiftHome = new DigitalInput(3);
		// binLiftIndexer = new DigitalInput(4);

		binBumperLeft = new DigitalInput(0);
		binBumperRight = new DigitalInput(1);

		drive = new RobotDrive(leftFront, leftRear, rightFront, rightRear);
		drive.setInvertedMotor(MotorType.kFrontRight, true);
		drive.setInvertedMotor(MotorType.kRearRight, true);

		try {
			serial_port = new SerialPort(57600, SerialPort.Port.kMXP);

			byte update_rate_hz = 50;
			// imu = new IMU(serial_port,update_rate_hz);
			// imu = new IMUAdvanced(serial_port,update_rate_hz);
			imu = new AHRS(serial_port, update_rate_hz);
		} catch (Exception ex) {

		}
		if (imu != null) {
			LiveWindow.addSensor("IMU", "Gyro", imu);
		}
		
		opticalFlow = new OpticalFlowSensor();
		gyro = new DualGyro();
		gyro.setInvertedGyro(1, true);
		mecanumDrive = new MecanumDrive(drive, gyro);
		mecanumDrive.init(0, 0.015, 0, 0);

		binLiftin = new BinLiftin();
		containerLiftin = new ContainerLiftin();
	}
}

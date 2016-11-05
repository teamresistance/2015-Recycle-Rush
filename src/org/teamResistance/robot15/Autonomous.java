package org.teamResistance.robot15;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Autonomous {
	

	public void init() {
		
		long previousTime = System.currentTimeMillis();
		double distanceBack; // negative number - robot picks up tote and moves back this much
		double secondToteDistance = 0; // distance to second tote after first is picked up
		double thirdToteDistance = 0; //distance to third tote after second is picked up
		double errorDistance = 0; //if bumpers aren't pressed, robot moves this minimal distance until they are
		double xAutoDistance = 0; //the x distance robot needs to drive to be in Auto Zone
		double yAutoDistance = 0; //the y distance robot needs to drive to be in Auto Zone
		double rotationAngleAuto = 0.0;
		/* comment for rotationAngleAuto variable
		 * 
		 * set as 0 as default but we might want to turn it so we set down
		 * the stack while our entire robot is in the Auto Zone (for the robot set)
		 */
		
		IO.binLiftin.pickup();
		IO.binLiftin.pickup();
//		IO.binLiftin.pickup(); 	(if we need it)
		
		double distanceBack1 = 0;
		IO.mecanumDrive.drive(0, distanceBack1, 0, 0);
		IO.mecanumDrive.drive(secondToteDistance, 0, 0, 0);
		
		/*
		 * indexDown method in BinLiftin needs to be edited so that it says:
		 * 
		 * state = state.INDEX_DOWN;
		 * 
		 * rather than:
		 * 
		 * state = state.SET_DOWN;
		 * 
		 * (possibly just a typo but i dont want to mess with frank's code)
		 */
		IO.binLiftin.indexDown();
		IO.binLiftin.indexDown();
//		IO.binLiftin.indexDown();	(if we need it)
		
		IO.mecanumDrive.drive(0, -distanceBack1, 0, 0);
		
		while ((IO.binBumperLeft.get() && IO.binBumperRight.get()) == false) {
				IO.mecanumDrive.drive(0, errorDistance, 0, 0);
		}
		
			IO.binLiftin.pickup();
		
		IO.binLiftin.pickup();
		IO.binLiftin.pickup();
//		IO.binLiftin.pickup(); 	(if we need it)
		
		IO.mecanumDrive.drive(0, distanceBack1, 0, 0);
		IO.mecanumDrive.drive(thirdToteDistance, 0, 0, 0);
		
		IO.binLiftin.indexDown();
		IO.binLiftin.indexDown();
//		IO.binLiftin.indexDown();	(if we need it)
		
		IO.mecanumDrive.drive(0, -distanceBack1, 0, 0);
		
		while ((IO.binBumperLeft.get() && IO.binBumperRight.get()) == false) {
				IO.mecanumDrive.drive(0, errorDistance, 0, 0);
		}
		
		IO.binLiftin.pickup();
		
		IO.mecanumDrive.drive(xAutoDistance, yAutoDistance, rotationAngleAuto, 0);
		
		IO.binLiftin.setDown();
		
	}
	
	public void update() {
		
		long currentTime = System.currentTimeMillis();
		long previousTime = 0;
		double deltaTime = (currentTime - previousTime) / 1000.0;
		SmartDashboard.putNumber("Delta Time", deltaTime);
		previousTime = currentTime;
		
		IO.binLiftin.update(deltaTime);
		
		
		/**
		 * Option 1 for Autonomous
		 * 
		 * This is what I coded for - its all in init
		 * but idk what should go in update and what should go in init so
		 * ill let one of yall fix that
		 * 
		 * also check the comment made in init about indexDown() in BinLiftin
		 */
		
		// Step 1: when robot is turned on, the 2 bumpers for BinLiftin will be hit
		//			^-- (the first yellow tote will be picked up)
		// Step 2: index up 2 (or 3 -- im not sure) times
		// Step 3: move back x amount of inches (we'll figure this out)
		// Step 4: drive sideways until it gets to the next yellow tote
		// Step 5: index down 2 (or 3 -- im not sure) times
		// Step 6: drive forward so that the 2 bumpers for BinLifin will be hit
		// Step 7: repeat Steps 2, 3, 4 , and 5
		// Step 8: drive back so that robot is in Auto Zone
		// Step 9: set stack down in Auto Zone
		// Step 10: make sure entire robot is in Auto Zone for robot set
		
		/**
		 * This is me trying to write the code for Option 1
		 */
		
		/**
		 * Option 2 for Autonomous
		 * 
		 * I didn't code for this yet -- maybe not as practical...
		 */
		
		// start with first container ready to be picked up (position - wise)
		// Step 1: close claw
		// Step 2: move shuttle up (just so it goes over scoring platform)
		// Step 3: rotate robot so that container can be set down in Auto Zone
		// Step 4: do same for rest of the the containers (you get the idea)
		// Step whatever: move ourselves into the Auto Zone for robot set
		
	}
	
}

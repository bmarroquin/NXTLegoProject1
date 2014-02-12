import lejos.nxt.Button;
import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.nxt.TouchSensor;
import lejos.nxt.UltrasonicSensor;
import lejos.nxt.addon.AccelMindSensor;
import lejos.nxt.LightSensor;
import lejos.robotics.Color;

public class Project1 {
	TouchSensor frontBumber;
	NXTRegulatedMotor left;
	NXTRegulatedMotor right;
	NXTRegulatedMotor arm;
	UltrasonicSensor eye;
	AccelMindSensor tilt;

	public Project1() {
		frontBumber = new TouchSensor(SensorPort.S4);
		eye = new UltrasonicSensor(SensorPort.S1);
		left = Motor.C;
		right = Motor.A;
		arm = Motor.B;
		tilt = new AccelMindSensor(SensorPort.S2);
	}

	/**
	 * Start moving the robot forward
	 */
	private void start() {
		forward();
	}

	/**
	 * Makes both tires move forward
	 */
	private void forward() {
		left.forward();
		right.forward();

	}

	/**
	 * Makes both tires move backwards for a specific amount of time
	 * @param ms The time to move backwards
	 */
	private void backward(long ms) {
		left.backward();
		right.backward();
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Makes the robot turn in a certain direction for a specific amount of time.
	 * @param ms The duration for the turn
	 * @param direction The direction of the turn. right or left
	 */
	private void turn(long ms, String direction) {
		if (direction.equalsIgnoreCase("left")) {
			left.backward();
			right.forward();
		} else if (direction.equalsIgnoreCase("right")) {
			left.forward();
			right.backward();
		} else {
			System.out.println("Invalid turn direction");
		}
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * What the robot does on every update beside move forward.
	 * @return Return false to cancel the movement.
	 */
	public boolean run() {
		checkTilt();
		
		checkBumper();

		checkEyes();

		return checkTurnOff();
	}
	


	/**
	 * Checks the tilt on the x axis and if it is within a threshold it reverses and changes direction
	 */
	private void checkTilt() {
		int dst []= new int[3];
		tilt.getAllTilt(dst, 0);
		if(dst[0]<-90 && dst[0]>-100){
			backward(2000);
			turn(1000, "left");
			forward();
		}
	}

	/**
	 * Handles bumper logic
	 */
	private void checkBumper() {
		if (frontBumber.isPressed()) {
			arm.rotate(100);
			arm.rotate(-100);
		}
	}

	/**
	 * Handles all logic with the UltrasonicSensor. 
	 */
	private void checkEyes() {
		if (eye.getDistance() < 20) {
			backward(1500);
			turn(1000, "right");
			forward();
		}
	}

	/**
	 * Checks whether or not to get out of the program
	 * @return Returns true to stay in or false to get out.
	 */
	private boolean checkTurnOff() {
		if(Button.ESCAPE.isDown()){
			return false;
		}
		return true;
	}

	public static void main(String args[]) {
		System.out.println("Starting Program");
		Project1 p1 = new Project1();
		p1.start();
		while (p1.run()) {}
		System.out.println("Program Done");
		System.exit(0);

	}
}

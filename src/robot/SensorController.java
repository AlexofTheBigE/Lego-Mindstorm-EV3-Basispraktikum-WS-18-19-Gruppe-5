package robot;

import lejos.hardware.port.Port;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.hardware.sensor.SensorMode;
import lejos.robotics.SampleProvider;

/**
 * Encapsulates sensor access.
 *
 */
public class SensorController {
	private static SensorController instance;
	
	private final Port COLOR_SENSOR_PORT = SensorPort.S1;
	private final Port DISTANCE_SENSOR_PORT = SensorPort.S4;
	private final Port LEFT_TOUCH_SENSOR_PORT = SensorPort.S3;
	private final Port RIGHT_TOUCH_SENSOR_PORT = SensorPort.S2;
	
	private int colorId;
	private float redValue;
	private float distance;
	private boolean leftTouching;
	private boolean rightTouching;
	
	private EV3ColorSensor colorSensor;
	private EV3UltrasonicSensor distanceSensor;
	private EV3TouchSensor leftTouchSensor;
	private EV3TouchSensor rightTouchSensor;
	
	private SampleProvider redValueSampler;

	private SensorController() {
		colorId = 0;
		distance = 0;
		colorSensor = new EV3ColorSensor(COLOR_SENSOR_PORT);
		redValueSampler = colorSensor.getRedMode();
		distanceSensor = new EV3UltrasonicSensor(DISTANCE_SENSOR_PORT);
		leftTouchSensor = new EV3TouchSensor(LEFT_TOUCH_SENSOR_PORT);
		rightTouchSensor = new EV3TouchSensor(RIGHT_TOUCH_SENSOR_PORT);
	}

	/**
	 * Get the singleton instance.
	 * @return
	 */
	public static SensorController get() {
		if (SensorController.instance == null) {
			SensorController.instance = new SensorController();
		}
		return SensorController.instance;
	}
	
	/**
	 * Update all sensor values.
	 * Used in the main loop.
	 */
	public void tick() {
		//updateColorId();
		updateRedValue();
		updateDistance();
		updateTouch();
	}
	
	/*
	private void updateColorId()
	{
		colorId = colorSensor.getColorID();
	}*/
	
	private void updateRedValue()
	{
        float[] sample = new float[redValueSampler.sampleSize()];
        redValueSampler.fetchSample(sample, 0);
        redValue = sample[0];
	}
	
	private void updateDistance()
	{
		SensorMode distanceSampler = distanceSensor.getMode("Distance");
        float[] sample = new float[distanceSampler.sampleSize()];
        distanceSampler.fetchSample(sample, 0);
        distance = sample[0];
	}
	
	private void updateTouch() {
		float[] sampleLeft = new float[leftTouchSensor.sampleSize()];
		sampleLeft[0] = 0;
        leftTouchSensor.fetchSample(sampleLeft, 0);
        if(sampleLeft[0] == 1) {
        	// TODO
            //Sound.playTone(800, 20);
        }
        rightTouching = (sampleLeft[0] == 1);
        
        float[] sampleRight = new float[rightTouchSensor.sampleSize()];
        sampleRight[0] = 0;
        rightTouchSensor.fetchSample(sampleRight, 0);
        if(sampleRight[0] == 1) {
        	// TODO
            //Sound.playTone(800, 20);
        }
        leftTouching = (sampleRight[0] == 1);
    }
	
	/**
	 * Get the measured distance of the ultrasonic sensor.
	 * @return
	 */
	public float getDistance() {
		return distance;
	}
	
	/**
	 * Get the measured color id of the color sensor.
	 * @return
	 */
	public int getColorId() {
		return colorId;
	}
	
	/**
	 * Get the measured red value of the color sensor.
	 * @return
	 */
	public float getRedValue() {
		return redValue;
	}
	
	/**
	 * Get whether the touch sensor is pressed.
	 * @return
	 */
	public boolean isLeftTouching() {
		return leftTouching;
	}
	
	/**
	 * Get whether the touch sensor is pressed.
	 * @return
	 */
	public boolean isRightTouching() {
		return rightTouching;
	}
}

package linefollower;

import lejos.hardware.motor.Motor;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.SampleProvider;

public class Straightline {

    // Motors
    private final RegulatedMotor leftMotor = Motor.A;
    private final RegulatedMotor rightMotor = Motor.B;

    // Sensors
    private final EV3ColorSensor colorSensor = new EV3ColorSensor(SensorPort.S1); // Line sensor
    private final EV3UltrasonicSensor ultrasonicSensor = new EV3UltrasonicSensor(SensorPort.S2); // Obstacle sensor

    public static void main(String[] args) {
        Straightline robot = new Straightline();
        robot.run(); // Start the robot
    }

    public void run() {
        // Main loop (runs forever until manually stopped)
        while (true) {
            // 1. Follow the line
            followLine();

            // 2. Check for obstacles
            if (isObstacleDetected()) {
                avoidObstacle();
            }
        }
    }

    // Basic line-following logic
    private void followLine() {
        float intensity = readColorSensor();

        // On the line (dark)? Turn left slightly
        if (intensity < 0.3) {
            leftMotor.setSpeed(100); // Slow left
            rightMotor.setSpeed(200); // Fast right
        }
        // Off the line (light)? Turn right slightly
        else {
            leftMotor.setSpeed(200); // Fast left
            rightMotor.setSpeed(100); // Slow right
        }

        // Keep moving forward
        leftMotor.forward();
        rightMotor.forward();
    }

    // Check if obstacle is near (distance < 25 cm)
    private boolean isObstacleDetected() {
        float distance = readUltrasonicSensor();
        return distance < 0.10f;
    }

    // Avoid obstacle: Turn left, move forward, then turn right
    private void avoidObstacle() {
        // 1. Stop
        leftMotor.stop();
        rightMotor.stop();

        // 2. Turn left (adjust degrees if needed)
        leftMotor.rotate(-180, true); // Left motor backward
        rightMotor.rotate(180); // Right motor forward

        // 3. Move forward for 1 second
        leftMotor.setSpeed(300);
        rightMotor.setSpeed(300);
        leftMotor.forward();
        rightMotor.forward();
        sleep(3000);

        // 4. Turn right to realign
        leftMotor.rotate(180, true);
        rightMotor.rotate(-180);
        searchForLine();
    }

    private void searchForLine() {
        // Move forward slowly while checking for the line
        leftMotor.setSpeed(150);
        rightMotor.setSpeed(150);
        leftMotor.forward();
        rightMotor.forward();

        // Keep checking until the line is found
        while (readColorSensor() > 0.3) { // While not on the line
            sleep(50); // Small delay to avoid CPU overload
        }

        // Line found! Resume normal following
    }

    // Read the color sensor (0 = black, 1 = white)
    private float readColorSensor() {
        SampleProvider color = colorSensor.getRedMode();
        float[] sample = new float[color.sampleSize()];
        color.fetchSample(sample, 0);
        return sample[0];
    }

    // Read distance from ultrasonic sensor (in meters)
    private float readUltrasonicSensor() {
        SampleProvider distance = ultrasonicSensor.getDistanceMode();
        float[] sample = new float[distance.sampleSize()];
        distance.fetchSample(sample, 0);
        return sample[0];
    }

    // Pause execution (milliseconds)
    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
        }
    }
}
// This annotation indicates that the class is a TeleOp (Teleoperated) mode. In FTC, TeleOp is when drivers control the robot using game controllers. The comment block at the top is a simple header indicating that this is a base OpMode template.
@TeleOp 

// This is the FTC Tutorial for Controlling Servos
public class MyFIRSTJavaOpMode extends LinearOpMode { 
    
    private Gyroscope imu; //  Gyroscope sensor for orientation and movement data.
    private DcMotor motorTest; // A DC motor that can be controlled to move parts of the robot.
    private DigitalChannel digitalTouch; // A distance sensor that can also detect color, useful for line following or object detection.
    private DistanceSensor sensorColorRange; // A digital touch sensor, often used to detect whether something is pressed or not.
    private Servo servoTest; //A servo motor for precise positioning.
 
    @Override
    public void runOpMode() { 
        
        //The hardware components are initialized by retrieving them from the hardwareMap, which contains the robot’s hardware configuration as defined in the FTC app.
        imu = hardwareMap.get(Gyroscope.class, "imu");
        motorTest = hardwareMap.get(DcMotor.class, "motorTest");
        digitalTouch = hardwareMap.get(DigitalChannel.class, "digitalTouch");
        sensorColorRange = hardwareMap.get(DistanceSensor.class, "sensorColorRange");
        servoTest = hardwareMap.get(Servo.class, "servoTest");

        // This updates the driver station's telemetry to display that the robot has been initialized.
        telemetry.addData("Status", "Initialized");
        telemetry.update();
        
        // This method halts execution until the driver presses the play button on the driver station, indicating that the match has started.
        waitForStart();
        
        // Run until the end of the match (driver presses STOP)
        // This loop runs as long as the OpMode is active. 

        double tgtPower = 0;
        while (opModeIsActive()) {
            // Inside the loop, it updates the telemetry to indicate that the robot is currently running. 
            telemetry.addData("Status", "Running");
            telemetry.update();

            
            tgtPower = -this.gamepad1.left_stick_y;
            motorTest.setPower(tgtPower);
            // check to see if we need to move the servo (if any buttons on the gamepad have been pressed)
            // This indicates the servo (180 degree range) must be moved
            if(gamepad1.y) {
                // move to 0 degrees.
                servoTest.setPosition(0);
            } else if (gamepad1.x || gamepad1.b) {
                // move to 90 degrees.
                servoTest.setPosition(0.5);
            } else if (gamepad1.a) {
                // move to 180 degrees.
                servoTest.setPosition(1);
            }

            // Update Driver Station To Include More Data
            telemetry.addData("Servo Position", servoTest.getPosition());
            telemetry.addData("Target Power", tgtPower);
            telemetry.addData("Motor Power", motorTest.getPower());
            telemetry.addData("Status", "Running");
            telemetry.update();

        }
    }
}

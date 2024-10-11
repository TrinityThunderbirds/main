
/*
This code sets up a basic structure for a TeleOp mode in an FTC robot. 
It initializes several hardware components, waits for the start of the match, and enters a loop that continues until the match ends. 
You would typically add additional logic within the main loop to control the robot’s behavior based on sensor input and driver commands.
/* 

@TeleOp 
// This annotation indicates that the class is a TeleOp (Teleoperated) mode. In FTC, TeleOp is when drivers control the robot using game controllers. The comment block at the top is a simple header indicating that this is a base OpMode template.

/* 
Classes are Object Oriented Programming (OOP) code.
They are a blueprint, and you can make specific instance(s) of 
the 'blueprint'.

Think of it like this: Car() is the class,
and you can make different instances of the class,
like a red car, green car, etc.
*/

    
public class MyFIRSTJavaOpMode extends LinearOpMode { //This is the name of the class, and it extends LinearOpMode, which is a base class for OpModes that allows for sequential execution of code.
    /*
    Private attributes are class-specific variables that
    cannot be accessed outside of the class.

    In Java, we define/create private attributes
    and then use public methods (which can be accessed outside)
    to alter/access the private attirbutes
    */

    private Gyroscope imu; //  Gyroscope sensor for orientation and movement data.
    private DcMotor motorTest; // A DC motor that can be controlled to move parts of the robot.
    private DigitalChannel digitalTouch; // A distance sensor that can also detect color, useful for line following or object detection.
    private DistanceSensor sensorColorRange; // A digital touch sensor, often used to detect whether something is pressed or not.
    private Servo servoTest; //A servo motor for precise positioning.

    /*
    This a public function (accessible outside of the
    class).
    Void means it doesn't return anything
    */
    
    @Override
    public void runOpMode() { //This is the main method that will be called when the OpMode starts. It contains the initialization and main loop for the robot's operation. 
        
        //The hardware components are initialized by retrieving them from the hardwareMap, which contains the robot’s hardware configuration as defined in the FTC app.
        imu = hardwareMap.get(Gyroscope.class, "imu");
        motorTest = hardwareMap.get(DcMotor.class, "motorTest");
        digitalTouch = hardwareMap.get(DigitalChannel.class, "digitalTouch");
        sensorColorRange = hardwareMap.get(DistanceSensor.class, "sensorColorRange");
        servoTest = hardwareMap.get(Servo.class, "servoTest");

        // This updates the driver station's telemetry to display that the robot has been initialized.
        telemetry.addData("Status", "Initialized");
        telemetry.update();
        
        // Wait for the game to start (driver presses PLAY)
        // This method halts execution until the driver presses the play button on the driver station, indicating that the match has started.
        waitForStart();
        
        // Run until the end of the match (driver presses STOP)
        // This loop runs as long as the OpMode is active. 
        //  Inside the loop, it updates the telemetry to indicate that the robot is currently running. You can add more functionality here, such as controlling motors or reading sensor values.
        while (opModeIsActive()) {
            telemetry.addData("Status", "Running");
            telemetry.update();

        }
    }
}

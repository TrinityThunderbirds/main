@TeleOp

/*

This is the base OPMODE template.

*/


/* 
Classes are Object Oriented Programming (OOP) code.
They are a blueprint, and you can make specific instance(s) of 
the 'blueprint'.

Think of it like this: Car() is the class,
and you can make different instances of the class,
like a red car, green car, etc.
*/
public class MyFIRSTJavaOpMode extends LinearOpMode {
    /*
    Private attributes are class-specific variables that
    cannot be accessed outside of the class.

    In Java, we define/create private attributes
    and then use public methods (which can be accessed outside)
    to alter/access the private attirbutes
    */
    private Gyroscope imu;
    private DcMotor motorTest;
    private DigitalChannel digitalTouch;
    private DistanceSensor sensorColorRange;
    private Servo servoTest;


    /*
    This a public function (accessible outside of the
    class).
    Void means it doesn't return anything
    */
    @Override
    public void runOpMode() {
        imu = hardwareMap.get(Gyroscope.class, "imu");
        motorTest = hardwareMap.get(DcMotor.class, "motorTest");
        digitalTouch = hardwareMap.get(DigitalChannel.class, "digitalTouch");
        sensorColorRange = hardwareMap.get(DistanceSensor.class, "sensorColorRange");
        servoTest = hardwareMap.get(Servo.class, "servoTest");

        telemetry.addData("Status", "Initialized");
        telemetry.update();
        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            telemetry.addData("Status", "Running");
            telemetry.update();

        }
    }
}

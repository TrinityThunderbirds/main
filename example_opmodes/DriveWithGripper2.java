package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name="Drive Gripper 2", group="Exercises")
//@Disabled
public class DriveWithGripper2 extends LinearOpMode
{
    DcMotor leftDrive, rightDrive, armMotor;
    // Servo intakeServo;   // Commenting out the servo for intake
    // Servo wristServo;    // Commenting out the wrist servo
    float leftX, leftY, rightY;
    // double intakePower; // Commenting out intake variables
    boolean intakeOn = false; // Track intake toggle state
    boolean lastXPress = false; // To prevent multiple toggles from a single button press
    boolean lastYPress = false;

    @Override
    public void runOpMode() throws InterruptedException
    {
        leftDrive = hardwareMap.dcMotor.get("left_drive");
        rightDrive = hardwareMap.dcMotor.get("right_drive");
        armMotor = hardwareMap.dcMotor.get("arm_motor");
        // intakeServo = hardwareMap.servo.get("intake_servo"); // Commented out intake servo
        // wristServo = hardwareMap.servo.get("wrist_servo");   // Commented out wrist servo

        leftDrive.setDirection(DcMotor.Direction.FORWARD);
        rightDrive.setDirection(DcMotor.Direction.REVERSE);

        telemetry.addData("Mode", "waiting");
        telemetry.update();

        // wait for start button.
        waitForStart();

        while (opModeIsActive())
        {
            // Left joystick for driving
            leftY = -gamepad1.left_stick_y; // Inverted to match standard forward/backward
            leftX = gamepad1.left_stick_x;

            // Right joystick for arm control (up/down)
            rightY = -gamepad1.right_stick_y; // Inverted for natural up/down motion

            // Set driving power (basic tank drive)
            double leftPower = Range.clip((leftY + leftX) * 0.8, -1.0, 1.0); // Forward/backward and left/right
            double rightPower = Range.clip((leftY - leftX) * 0.8, -1.0, 1.0); // Forward/backward and left/right
            leftDrive.setPower(leftPower);
            rightDrive.setPower(rightPower);

            // Set arm power based on right joystick Y-axis
            armMotor.setPower(Range.clip(rightY * 0.8, -1.0, 1.0));

            // Commenting out the intake and wrist controls below:

            // // Intake Toggle Control using X and Y buttons
            // if (gamepad1.x && !lastXPress) {
            //     intakeOn = !intakeOn; // Toggle the intake state
            //     lastXPress = true; // Mark X as pressed to prevent double toggling
            // } else if (!gamepad1.x) {
            //     lastXPress = false; // Reset X press status once button is released
            // }

            // if (gamepad1.y && !lastYPress) {
            //     intakeOn = false; // Ensure intake is off when Y is pressed
            //     lastYPress = true;
            // } else if (!gamepad1.y) {
            //     lastYPress = false;
            // }

            // // Intake Servo Control using triggers
            // if (intakeOn) {
            //     if (gamepad1.left_trigger > 0) {
            //         intakeServo.setPosition(Range.clip(intakeServo.getPosition() + 0.01, 0, 1)); // Move up
            //     } else if (gamepad1.right_trigger > 0) {
            //         intakeServo.setPosition(Range.clip(intakeServo.getPosition() - 0.01, 0, 1)); // Move down
            //     }
            // }

            // Telemetry feedback
            telemetry.addData("left power", leftPower);
            telemetry.addData("right power", rightPower);
            telemetry.addData("arm power", rightY);
            // telemetry.addData("intake servo", "position=%.2f", intakeServo.getPosition()); // Commented out
            telemetry.addData("intake", intakeOn ? "On" : "Off"); // Will show "Off" always since intake is off
            telemetry.update();

            idle(); // Always call idle() at the end of each loop iteration
        }
    }
}

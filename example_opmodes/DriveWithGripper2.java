package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name="Drive Gripper 2", group="Exercises")
//@Disabled
public class DriveWithGripper2 extends LinearOpMode {
public class DriveWithGripper2 extends LinearOpMode {
    DcMotor leftDrive, rightDrive, armMotor;
    Servo intakeServo, wristServo;
    float leftX, leftY, rightY;

    boolean isTurning = false; // Flag to indicate if the robot is currently turning
    long turnDuration = 350; // Duration in milliseconds to turn 90 degrees

    @Override
    public void runOpMode() throws InterruptedException {
    public void runOpMode() throws InterruptedException {
        leftDrive = hardwareMap.dcMotor.get("left_drive");
        rightDrive = hardwareMap.dcMotor.get("right_drive");
        armMotor = hardwareMap.dcMotor.get("arm_motor");
        wristServo = hardwareMap.servo.get("wrist_servo");
        intakeServo = hardwareMap.servo.get("intake_servo");

        // Reverse motor directions
        leftDrive.setDirection(DcMotor.Direction.REVERSE);
        rightDrive.setDirection(DcMotor.Direction.FORWARD);
        // Reverse motor directions
        leftDrive.setDirection(DcMotor.Direction.REVERSE);
        rightDrive.setDirection(DcMotor.Direction.FORWARD);

        telemetry.addData("Mode", "waiting");
        telemetry.update();

        // Wait for start button
        // Wait for start button
        waitForStart();

        while (opModeIsActive()) {
            // Reverse control for driving
            leftY = -gamepad1.left_stick_y; // Reversed for forward/backward
            leftX = gamepad1.left_stick_x; // Reversed for left/right

            // Right joystick for arm control (up/down)
            rightY = -gamepad1.right_stick_y; // Inverted for natural up/down motion

            double wristServoPos = 0;

            // D-pad control for forward and backward
            if (gamepad1.dpad_up) {
                leftDrive.setPower(0.4);
                rightDrive.setPower(0.4);
            } else if (gamepad1.dpad_down) {
                leftDrive.setPower(-0.4);
                rightDrive.setPower(-0.4);
            } else if (gamepad1.dpad_right && !isTurning) {
                turnRight();
            } else if (gamepad1.dpad_left && !isTurning) {
                turnLeft();
            } else {
                // Set driving power based on joystick input
                double leftPower = Range.clip((leftY + leftX) * 0.6, -1.0, 1.0);
                double rightPower = Range.clip((leftY - leftX) * 0.6, -1.0, 1.0);
                leftDrive.setPower(leftPower);
                rightDrive.setPower(rightPower);
            }

            // Arm control using triggers
            if (gamepad1.left_trigger > 0) {
                armMotor.setPower(Range.clip(gamepad1.left_trigger, 0, 1.0)); // Move arm up
            } else if (gamepad1.right_trigger > 0) {
                armMotor.setPower(-Range.clip(gamepad1.right_trigger, 0, 1.0)); // Move arm down
            } else {
                armMotor.setPower(0); // Stop arm if no triggers are pressed
            }

            if (gamepad1.left_bumper && wristServoPos != 1) {
                wristServoPos += 0.1;
                wristServo.setPosition(wristServoPos);
            }
            if (gamepad1.right_bumper && wristServoPos != 0) {
                wristServoPos -= 0.1;
                wristServo.setPosition(wristServoPos);
            }

            if (gamepad1.y) {
                intakeServo.setPosition(1);
            }
            if (gamepad1.a) {
                intakeServo.setPosition(0);
            }

            // Telemetry feedback
            telemetry.addData("left power", leftDrive.getPower());
            telemetry.addData("right power", rightDrive.getPower());
            telemetry.addData("arm power", armMotor.getPower());
            telemetry.update();

            idle(); // Always call idle() at the end of each loop iteration

            idle(); // Always call idle() at the end of each loop iteration
        }
    }

    private void turnRight() {
        leftDrive.setPower(0.4);
        rightDrive.setPower(-0.4);
        isTurning = true;
        sleep(turnDuration);
        leftDrive.setPower(0);
        rightDrive.setPower(0);
        isTurning = false;
    }

    private void turnLeft() {
        leftDrive.setPower(-0.4);
        rightDrive.setPower(0.4);
        isTurning = true;
        sleep(turnDuration);
        leftDrive.setPower(0);
        rightDrive.setPower(0);
        isTurning = false;
    }
}
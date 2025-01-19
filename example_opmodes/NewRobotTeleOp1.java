package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name = "NewRobotTeleOp1", group = "TeleOp")
public class NewRobotTeleOp1 extends LinearOpMode {

    // Declare hardware components
    DcMotor leftFront, rightFront, leftBack, rightBack;
    DcMotor armMotor, linearSlide;
    Servo clawServo;

    final double ARM_TICKS_PER_DEGREE = 
            28 * 250047.0 / 4913.0 * 100.0 / 20.0 * 1 / 360.0; 
    double targetArmPosition = 0;
    final long TURN_TIME_MS = 500; // Approximate time to turn 90 degrees

    @Override
    public void runOpMode() throws InterruptedException {
        // Initialize hardware from the hardware map
        leftFront = hardwareMap.dcMotor.get("leftFront");
        rightFront = hardwareMap.dcMotor.get("rightFront");
        leftBack = hardwareMap.dcMotor.get("leftBack");
        rightBack = hardwareMap.dcMotor.get("rightBack");
        armMotor = hardwareMap.dcMotor.get("armMotor");
        //linearSlide = hardwareMap.dcMotor.get("linear_slide");
        clawServo = hardwareMap.servo.get("clawServo");

        // Reverse the left motors so it doesn't just spin
        leftFront.setDirection(DcMotorSimple.Direction.REVERSE);
        leftBack.setDirection(DcMotorSimple.Direction.REVERSE);

        // Set the arm motor to run using encoders
        armMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        armMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        waitForStart();

        while (opModeIsActive()) {
            // Mecanum drive control
            double y = -gamepad1.left_stick_y; // Forward/Back
            double x = gamepad1.right_stick_x; // Left/Right
            double rx = 0.0; // No rotation unless turning is requested

            // Timed 90-degree turn
            if (gamepad1.dpad_right || gamepad1.dpad_left) {
                double turnDirection = gamepad1.dpad_right ? 1 : -1; // 1 for right, -1 for left

                // Start turning
                leftFront.setPower(turnDirection * 0.6);
                leftBack.setPower(turnDirection * 0.6);
                rightFront.setPower(-turnDirection * 0.6);
                rightBack.setPower(-turnDirection * 0.6);

                // Sleep for the duration of the turn
                sleep(TURN_TIME_MS);

                // Stop turning
                leftFront.setPower(0);
                leftBack.setPower(0);
                rightFront.setPower(0);
                rightBack.setPower(0);
            } else {
                // Standard mecanum drive controls
                double denominator = Math.max(Math.abs(y) + Math.abs(x), 1);
                double leftFrontPower = (y + x + rx) / denominator;
                double leftBackPower = (y - x + rx) / denominator;
                double rightFrontPower = (y - x - rx) / denominator;
                double rightBackPower = (y + x - rx) / denominator;

                leftFront.setPower(leftFrontPower * 0.6);
                leftBack.setPower(leftBackPower * 0.6);
                rightFront.setPower(rightFrontPower * 0.6);
                rightBack.setPower(rightBackPower * 0.6);
            }

            // Claw control
            if (gamepad1.x) {
                clawServo.setPosition(1.0); // Open claw
            } else if (gamepad1.b) {
                clawServo.setPosition(0.0); // Close claw
            }

            // Arm control
            if (gamepad1.y) {
                targetArmPosition += ARM_TICKS_PER_DEGREE; // Arm up
            } else if (gamepad1.a) {
                targetArmPosition -= ARM_TICKS_PER_DEGREE; // Arm down
            }

            // Clamp the target arm position within safe bounds
            targetArmPosition = Range.clip(targetArmPosition, 0, 5000); 

            // Move arm to the target position
            armMotor.setTargetPosition((int) targetArmPosition);
            armMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            armMotor.setPower(0.6);

            // Telemetry for debugging
            telemetry.addData("LeftFront Power", leftFront.getPower());
            telemetry.addData("RightFront Power", rightFront.getPower());
            telemetry.addData("LeftBack Power", leftBack.getPower());
            telemetry.addData("RightBack Power", rightBack.getPower());
            telemetry.addData("Arm Position", targetArmPosition);
            telemetry.update();
        }
    }
}

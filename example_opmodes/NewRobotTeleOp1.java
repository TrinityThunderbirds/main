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

    // Encoder information gotten from goBilda codes
    final double ARM_TICKS_PER_DEGREE =
            28 // number of encoder ticks per rotation of the bare motor
                    * 250047.0 / 4913.0 // This is the exact gear ratio of the 50.9:1 Yellow Jacket gearbox
                    * 100.0 / 20.0 // This is the external gear reduction, a 20T pinion gear that drives a 100T hub-mount gear
                    * 1 / 360.0; // we want ticks per degree, not per rotation

    double targetArmPosition = 0;

    @Override
    public void runOpMode() throws InterruptedException {
        // Initialize hardware from the hardware map
        leftFront = hardwareMap.dcMotor.get("leftFront");
        rightFront = hardwareMap.dcMotor.get("rightFront");
        leftBack = hardwareMap.dcMotor.get("leftBack");
        rightBack = hardwareMap.dcMotor.get("rightBack");
        armMotor = hardwareMap.dcMotor.get("armMotor");
        //linearSlide = hardwareMap.dcMotor.get("linear_slide");
        //clawServo = hardwareMap.servo.get("claw_servo");

        // Reverse the left motors so it doesn't just spin
        leftFront.setDirection(DcMotorSimple.Direction.REVERSE);
        leftBack.setDirection(DcMotorSimple.Direction.REVERSE);

        // Set the arm motor to run using encoders
        armMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        armMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        waitForStart();

        while (opModeIsActive()) {
            // Mecanum drive control
            double y = -gamepad1.left_stick_y; // Remember, Y stick value is reversed
            double x = gamepad1.left_stick_x * 1.1; // Counteract imperfect strafing
            double rx = gamepad1.right_stick_x;

            // Denominator is the largest motor power (absolute value) or 1
            // This ensures all the powers maintain the same ratio,
            // but only if at least one is out of the range [-1, 1]
            double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
            double leftFrontPower = (y + x + rx) / denominator;
            double leftBackPower = (y - x + rx) / denominator;
            double rightFrontPower = (y - x - rx) / denominator;
            double rightBackPower = (y + x - rx) / denominator;

            leftFront.setPower(leftFrontPower * 0.6);
            leftBack.setPower(leftBackPower * 0.6);
            rightFront.setPower(rightFrontPower * 0.6);
            rightBack.setPower(rightBackPower * 0.6);

            // Linear slide control
            // double slidePower = -gamepad1.right_stick_y; // Extend/retract using right joystick
            // linearSlide.setPower(Range.clip(slidePower, -0.6, 0.6));

            // Claw control
            //if (gamepad1.x) {
            //    clawServo.setPosition(1.0); // Open claw
            //} else if (gamepad1.b) {
            //    clawServo.setPosition(0.0); // Close claw
            //}

            // Arm control
            if (gamepad1.y) {
                targetArmPosition += ARM_TICKS_PER_DEGREE; // Arm up
            } else if (gamepad1.a) {
                targetArmPosition -= ARM_TICKS_PER_DEGREE; // Arm down
            }

            // Clamp the target arm position within safe bounds
            targetArmPosition = Range.clip(targetArmPosition, 0, 5000); // Adjust max range as needed

            // Move arm to the target position
            armMotor.setTargetPosition((int) targetArmPosition);
            armMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            armMotor.setPower(0.6); // Adjust power as needed

            // Telemetry for debugging
            telemetry.addData("LeftFront Power", leftFrontPower);
            telemetry.addData("RightFront Power", rightFrontPower);
            telemetry.addData("LeftBack Power", leftBackPower);
            telemetry.addData("RightBack Power", rightBackPower);
            //telemetry.addData("Linear Slide Power", linearSlide.getPower());
            //telemetry.addData("Claw Position", clawServo.getPosition());
            telemetry.update();
        }
    }
}

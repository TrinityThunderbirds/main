package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name = "Thunderbirds New Robot TeleOp 1", group = "TeleOp")
public class NewRobotTeleOp1 extends LinearOpMode {

    // Declare hardware components
    DcMotor frontLeft, frontRight, backLeft, backRight;
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
        frontLeft = hardwareMap.dcMotor.get("frontLeft");
        frontRight = hardwareMap.dcMotor.get("front_Right");
        backLeft = hardwareMap.dcMotor.get("backLeft");
        backRight = hardwareMap.dcMotor.get("backRight");
        armMotor = hardwareMap.dcMotor.get("armMotor");
        //linearSlide = hardwareMap.dcMotor.get("linear_slide");
        //clawServo = hardwareMap.servo.get("claw_servo");

        // Reverse the left motors so it doesn't just spin
        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);

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
            double frontLeftPower = (y + x + rx) / denominator;
            double backLeftPower = (y - x + rx) / denominator;
            double frontRightPower = (y - x - rx) / denominator;
            double backRightPower = (y + x - rx) / denominator;

            frontLeftMotor.setPower(frontLeftPower * 0.6);
            backLeftMotor.setPower(backLeftPower * 0.6);
            frontRightMotor.setPower(frontRightPower * 0.6);
            backRightMotor.setPower(backRightPower * 0.6);

            // Linear slide control
            double slidePower = -gamepad1.right_stick_y; // Extend/retract using right joystick
            linearSlide.setPower(Range.clip(slidePower, -0.6, 0.6));

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
            telemetry.addData("Front Left Power", frontLeftPower);
            telemetry.addData("Front Right Power", frontRightPower);
            telemetry.addData("Back Left Power", backLeftPower);
            telemetry.addData("Back Right Power", backRightPower);
            //telemetry.addData("Linear Slide Power", linearSlide.getPower());
            //telemetry.addData("Claw Position", clawServo.getPosition());
            telemetry.update();
        }
    }
}

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "NewRobotTeleOp1", group = "TeleOp")
public class NewRobotTeleOp1 extends LinearOpMode {

    // Declare hardware components
    DcMotor leftFront, rightFront, leftBack, rightBack;
    DcMotor armMotor;
    Servo clawServo;

    final long TURN_TIME_MS = 500; // Approximate time to turn 90 degrees

    @Override
    public void runOpMode() throws InterruptedException {
        // Initialize hardware from the hardware map
        leftFront = hardwareMap.dcMotor.get("leftFront");
        rightFront = hardwareMap.dcMotor.get("rightFront");
        leftBack = hardwareMap.dcMotor.get("leftBack");
        rightBack = hardwareMap.dcMotor.get("rightBack");
        armMotor = hardwareMap.dcMotor.get("armMotor");
        clawServo = hardwareMap.servo.get("clawServo");

        // Reverse the left motors so it doesn't just spin
        leftFront.setDirection(DcMotorSimple.Direction.REVERSE);
        leftBack.setDirection(DcMotorSimple.Direction.REVERSE);

        waitForStart();

        while (opModeIsActive()) {
            // Mecanum drive control
            double y = -gamepad1.left_stick_y; // Forward/Backward
            double rx = gamepad1.right_stick_x; // Rotation

            // Combine forward/backward movement and rotation
            double denominator = Math.max(Math.abs(y) + Math.abs(rx), 1);
            double leftFrontPower = (y + rx) / denominator;
            double leftBackPower = (y + rx) / denominator;
            double rightFrontPower = (y - rx) / denominator;
            double rightBackPower = (y - rx) / denominator;

            leftFront.setPower(leftFrontPower * 0.6);
            leftBack.setPower(leftBackPower * 0.6);
            rightFront.setPower(rightFrontPower * 0.6);
            rightBack.setPower(rightBackPower * 0.6);

            // Claw control
            if (gamepad1.x) {
                clawServo.setPosition(1.0); // Open claw
            } else if (gamepad1.b) {
                clawServo.setPosition(0.0); // Close claw
            }

            // Arm control
            if (gamepad1.y) {
                armMotor.setPower(0.6); // Move arm up
            } else if (gamepad1.a) {
                armMotor.setPower(-0.6); // Move arm down
            } else {
                armMotor.setPower(0); // Stop arm motor
            }

            // Telemetry for debugging
            telemetry.addData("LeftFront Power", leftFront.getPower());
            telemetry.addData("RightFront Power", rightFront.getPower());
            telemetry.addData("LeftBack Power", leftBack.getPower());
            telemetry.addData("RightBack Power", rightBack.getPower());
            telemetry.update();
        }
    }
}

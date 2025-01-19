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

    // Arm control variables
    private int armTargetPosition = 0;
    private final int ARM_INCREMENT = 50; // Encoder counts per button press

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

        // Configure arm motor
        armMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        armMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        armMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        waitForStart();

        while (opModeIsActive()) {
            // Mecanum drive control
            double y = -gamepad1.left_stick_y; // Forward/Backward
            double x = gamepad1.left_stick_x; // Rotation

            // Combine forward/backward movement and rotation
            double denominator = Math.max(Math.abs(y) + Math.abs(x), 1);
            double leftFrontPower = (y + x) / denominator;
            double leftBackPower = (y + x) / denominator;
            double rightFrontPower = (y - x) / denominator;
            double rightBackPower = (y - x) / denominator;

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
                armTargetPosition += ARM_INCREMENT;
            } else if (gamepad1.a) {
                armTargetPosition -= ARM_INCREMENT;
            }

            // Clamp arm position to prevent going out of range
            armTargetPosition = Math.max(0, Math.min(armTargetPosition, armMotor.getCurrentPosition() + 1000)); // Example max range

            armMotor.setTargetPosition(armTargetPosition);
            armMotor.setPower(0.6); // Set constant power to maintain position

            // Telemetry for debugging
            telemetry.addData("LeftFront Power", leftFront.getPower());
            telemetry.addData("RightFront Power", rightFront.getPower());
            telemetry.addData("LeftBack Power", leftBack.getPower());
            telemetry.addData("RightBack Power", rightBack.getPower());
            telemetry.addData("Arm Target Position", armTargetPosition);
            telemetry.addData("Arm Current Position", armMotor.getCurrentPosition());
            telemetry.update();
        }
    }
}

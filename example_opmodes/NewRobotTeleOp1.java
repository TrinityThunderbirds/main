// CONTROLS:

// LEFT JOYSTICK = FORWARD/BACK/LEFT/RIGHT
// RIGHT JOYSTICK = ROTATION
// LEFT TRIGGER = UP ARM
// RIGHT TRIGGER = DOWN ARM
// Y = EXTEND LINEAR SLIDE
// A = RETRACT LINEAR SLIDE
// X = OPEN CLAW
// B = CLOSE CLAW


package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name = "Mecanum Drive with Claw and Linear Slide", group = "TeleOp")
public class DriveWithGripper2 extends LinearOpMode {

    // Declare hardware components
    DcMotor frontLeft, frontRight, backLeft, backRight;
    DcMotor armMotor, linearSlide;
    Servo clawServo;

    // Encoder information gotten from goBilda codes
    final double ARM_TICKS_PER_DEGREE =
            28 // number of encoder ticks per rotation of the bare motor
                    * 250047.0 / 4913.0 // This is the exact gear ratio of the 50.9:1 Yellow Jacket gearbox
                    * 100.0 / 20.0 // This is the external gear reduction, a 20T pinion gear that drives a 100T hub-mount gear
                    * 1/360.0; // we want ticks per degree, not per rotation
    
    double targetArmPosition = 0;
    
    @Override
    public void runOpMode() throws InterruptedException {
        // Initialize hardware from the hardware map
        frontLeft = hardwareMap.dcMotor.get("front_left");
        frontRight = hardwareMap.dcMotor.get("front_right");
        backLeft = hardwareMap.dcMotor.get("back_left");
        backRight = hardwareMap.dcMotor.get("back_right");
        armMotor = hardwareMap.dcMotor.get("arm_motor");
        linearSlide = hardwareMap.dcMotor.get("linear_slide");
        clawServo = hardwareMap.servo.get("claw_servo");

        // Reverse the left motors so it doesnt just spin
        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);

        // Set the arm motor to run using encoders
        armMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        armMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        
        waitForStart();

        while (opModeIsActive()) {
            // Mecanum drive control
            double y = -gamepad1.left_stick_y; // Forward/backward
            double x = gamepad1.left_stick_x * 1.1; // Strafe
            double rx = gamepad1.right_stick_x; // Rotation

            // How different wheel powers are calculated for intended movement
            double frontLeftPower = y + x + rx;
            double frontRightPower = y - x - rx;
            double backLeftPower = y - x + rx;
            double backRightPower = y + x - rx;

            // Stops wheel powers from being over 1 and causing errors
            double maxPower = Math.max(1.0, Math.abs(frontLeftPower));
            frontLeftPower /= maxPower;
            frontRightPower /= maxPower;
            backLeftPower /= maxPower;
            backRightPower /= maxPower;

            // Set power to the motors
            frontLeft.setPower(frontLeftPower);
            frontRight.setPower(frontRightPower);
            backLeft.setPower(backLeftPower);
            backRight.setPower(backRightPower);

            // Linear slide control
            if (gamepad1.y) {
                linearSlide.setPower(0.6); // Extend
            } else if (gamepad1.a) {
                linearSlide.setPower(-0.6); // Retract
            } else {
                linearSlide.setPower(0); // Stop
            }

            // Claw control
            if (gamepad1.x) {
                clawServo.setPosition(1.0); // Open
            } else if (gamepad1.b) {
                clawServo.setPosition(0.0); // Close
            }

            // Arm motor control with triggers
             if (gamepad2.left_trigger > 0) {
                targetArmPosition += gamepad2.left_trigger * ARM_TICKS_PER_DEGREE; // Move arm up
            } else if (gamepad2.right_trigger > 0) {
                targetArmPosition -= gamepad2.right_trigger * ARM_TICKS_PER_DEGREE; // Move arm down
            }

            // Make target position in certain bounds
            targetArmPosition = Range.clip(targetArmPosition, 0, 5000); // Adjust max range as needed

            // Set arm motor to move to the target position
            armMotor.setTargetPosition((int) targetArmPosition);
            armMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            armMotor.setPower(0.6); // Adjust power as needed for smooth movement
            
            // Telemetry for debugging
            telemetry.addData("Front Left Power", frontLeftPower);
            telemetry.addData("Front Right Power", frontRightPower);
            telemetry.addData("Back Left Power", backLeftPower);
            telemetry.addData("Back Right Power", backRightPower);
            telemetry.addData("Linear Slide Power", linearSlide.getPower());
            telemetry.addData("Claw Position", clawServo.getPosition());
            telemetry.update();
        }
    }
}

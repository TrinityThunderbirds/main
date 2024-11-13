package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name="Drive Gripper 2", group="Exercises")
public class DriveWithGripper2 extends LinearOpMode {
    DcMotor leftDrive, rightDrive, armMotor;
    CRServo intakeServo;
    Servo wristServo;

    @Override
    public void runOpMode() {
        // Initialize hardware
        leftDrive = hardwareMap.dcMotor.get("left_drive");
        rightDrive = hardwareMap.dcMotor.get("right_drive");
        armMotor = hardwareMap.dcMotor.get("arm_motor");
        intakeServo = hardwareMap.get(CRServo.class, "intake_servo");
        wristServo = hardwareMap.servo.get("wrist_servo");

        // Set motor directions
        leftDrive.setDirection(DcMotor.Direction.REVERSE);
        rightDrive.setDirection(DcMotor.Direction.FORWARD);

        telemetry.addData("Mode", "waiting");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            // **Driving Controls**

            // Forward and backward movement with left joystick (y-axis)
            double drivePower = -gamepad1.left_stick_y;
            leftDrive.setPower(drivePower);
            rightDrive.setPower(drivePower);

            // Rotation with right joystick (x-axis)
            double rotatePower = gamepad1.right_stick_x;
            leftDrive.setPower(rotatePower);
            rightDrive.setPower(-rotatePower);

            // **Arm Control with Left Joystick on Gamepad 2**
            double armPower = -gamepad2.left_stick_y;
            armMotor.setPower(Range.clip(armPower, -1.0, 1.0));

            // **Intake Servo (CRServo) Control**
            if (gamepad2.left_trigger > 0) {
                intakeServo.setPower(1.0); // Collect
                telemetry.addData("Intake", "Collecting");
            } else if (gamepad2.right_trigger > 0) {
                intakeServo.setPower(-1.0); // Deposit
                telemetry.addData("Intake", "Depositing");
            } else {
                intakeServo.setPower(0); // Stop intake if no triggers are pressed
                telemetry.addData("Intake", "Stopped");
            }

            // **Wrist Servo Control**
            if (gamepad2.dpad_left) {
                wristServo.setPosition(0.0); // Rotate wrist left
                telemetry.addData("Wrist", "Left Position (0.0)");
            } else if (gamepad2.dpad_right) {
                wristServo.setPosition(1.0); // Rotate wrist right
                telemetry.addData("Wrist", "Right Position (1.0)");
            } else if (gamepad2.dpad_up) {
                wristServo.setPosition(0.5); // Set wrist straight
                telemetry.addData("Wrist", "Straight Position (0.5)");
            }

            // **Telemetry Feedback**
            telemetry.addData("Drive Power", drivePower);
            telemetry.addData("Rotate Power", rotatePower);
            telemetry.addData("Arm Power", armMotor.getPower());
            telemetry.addData("Intake Servo Power", intakeServo.getPower());
            telemetry.addData("Wrist Servo Position", wristServo.getPosition());
            telemetry.update();

            idle(); // Always call idle() at the end of each loop iteration
        }
    }
}

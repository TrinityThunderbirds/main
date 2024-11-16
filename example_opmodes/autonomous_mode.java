package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name="auton", group="Exercises")
public class DriveWithGripper2 extends LinearOpMode {
    DcMotor leftDrive, rightDrive, armMotor;
    CRServo intakeServo;
    Servo wristServo;

    private static final double TPR = 537.7;
    private static final double gearReduction = 1/3;
    private static final double wheelDiameterMM = 96;
    private static final double countsPerMM = (TPR * gearReduction) / (wheelDiameterMM * Math.PI);

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
        
        while (opModeIsActive){

        }

    }
    
    // This should THEORETICALLY make it so that both motors move to the same encoder position, hence driving forward.
    private void driveForward(double inches, double power) {
        // Calculate target position in encoder ticks
        double targetPosition = (inches * 25.4 * countsPerMM);

        // Set target position
        leftMotor.setTargetPosition(leftMotor.getCurrentPosition() + targetPosition);
        rightMotor.setTargetPosition(rightMotor.getCurrentPosition() + targetPosition);

        // Set motors to RUN_TO_POSITION mode
        leftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        // Set motor power
        leftMotor.setPower(power);
        rightMotor.setPower(power);

        // Wait for the motors to reach the target position
        while (opModeIsActive() && (leftMotor.isBusy() || rightMotor.isBusy())) {
            telemetry.addData("Target", targetPosition);
            telemetry.addData("Left Current", leftMotor.getCurrentPosition());
            telemetry.addData("Right Current", rightMotor.getCurrentPosition());
            telemetry.update();
        }

        // Stop motors
        leftMotor.setPower(0);
        rightMotor.setPower(0);

        // Reset to RUN_USING_ENCODER mode
        leftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
}



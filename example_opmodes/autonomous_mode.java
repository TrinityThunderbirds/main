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
    private static final double gearReduction = 1.0/3.0;
    private static final double wheelDiameterMM = 96;
    private static final double countsPerMM = (TPR * gearReduction) / (wheelDiameterMM * Math.PI);
    private double initialPos = 48;

    @Override
    public void runOpMode() {
        // Initialize hardware
        leftDrive = hardwareMap.dcMotor.get("left_drive");
        rightDrive = hardwareMap.dcMotor.get("right_drive");
        armMotor = hardwareMap.dcMotor.get("arm_motor");
        intakeServo = hardwareMap.get(CRServo.class, "intake_servo");

        // Set motor directions
        leftDrive.setDirection(DcMotor.Direction.REVERSE);
        rightDrive.setDirection(DcMotor.Direction.FORWARD);

        telemetry.addData("Mode", "waiting");
        telemetry.update();

        waitForStart();
        
        intakeServo.setPower(1);
        driveForward(30, 1);
        armRotate(150, 1);
        intakeServo.setDirection(DcMotor.Direction.REVERSE);
        sleep(1000);
        intakeServo.setPower(0);
        armRotate(-150, 1);
        driveForward(-120, 1);

        // Log that mode in finished
        telemetry.addData("Mode", "Finished");
        telemetry.update();

    }

    // This should THEORETICALLY make it so that both motors move to the same encoder position, hence driving forward.
    private void driveForward(double inches, double power) {
        // Calculate target position in encoder ticks
        double targetPosition = (inches * 25.4 * countsPerMM);

        // Set target position
        leftDrive.setTargetPosition(leftDrive.getCurrentPosition() + targetPosition);
        rightDrive.setTargetPosition(rightDrive.getCurrentPosition() + targetPosition);

        // Set motors to RUN_TO_POSITION mode
        leftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        // Set motor power
        leftDrive.setPower(power);
        rightDrive.setPower(power);

        // Wait for the motors to reach the target position
        while (opModeIsActive() && (leftDrive.isBusy() || rightDrive.isBusy())) {
            telemetry.addData("Target", targetPosition);
            telemetry.addData("Left Current", leftDrive.getCurrentPosition());
            telemetry.addData("Right Current", rightDrive.getCurrentPosition());
            telemetry.update();
        }

        // Stop motors
        
        leftDrive.setPower(0);
        rightDrive.setPower(0);
        
        // Reset to RUN_USING_ENCODER mode
        leftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

      
    }

    private void armRotate(double degrees, double power){
        // Set target position
        double armTargetPosition = ((degrees * 1/5)/360) * 1425.1;

        double targetPositionMotor = armMotor.getCurrentPosition() + targetPosition;
        
        armMotor.setTargetPosition(armMotor.getCurrentPosition() + targetPosition);

        armMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        // Set motor power
        armMotor.setPower(power);

        // Wait for the motors to reach the target position
        while (opModeIsActive() && (armMotor.isBusy())) {
            telemetry.addData("Arm Target", targetPosition);
            telemetry.addData("Arm Current", armMotor.getCurrentPosition());
            telemetry.update();
        }

        // Stop motor
        
        armMotor.setPower(0);
        
        // Reset to RUN_USING_ENCODER mode
        armMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

    }
}



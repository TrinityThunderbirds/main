package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name="Drive Gripper 2", group="Exercises")
//@Disabled
public class DriveWithGripper2 extends LinearOpMode
{
    DcMotor leftDrive, rightDrive, armMotor;
    Servo   intakeServo;
    CRServo contServo;
    float   leftY, rightY;
    double  gripPosition, contPower;
    double  aLastTime, bLastTime;
    final double MIN_POSITION = 0, MAX_POSITION = 1;
    final double ARM_POWER_INCREMENT = 0.05;

    boolean intakeOn = false; // Track intake toggle state
    boolean lastXPress = false; // To prevent multiple toggles from a single button press
    boolean lastYPress = false;

    @Override
    public void runOpMode() throws InterruptedException
    {
        leftDrive = hardwareMap.dcMotor.get("left_drive");
        rightDrive = hardwareMap.dcMotor.get("right_drive");
        armMotor = hardwareMap.dcMotor.get("arm_motor");  // Now correctly defined as a DcMotor
        intakeServo = hardwareMap.servo.get("intake_servo");
        contServo = hardwareMap.crservo.get("cont_servo");

        leftDrive.setDirection(DcMotor.Direction.FORWARD);
        rightDrive.setDirection(DcMotor.Direction.REVERSE); // Usually one motor needs to be reversed

        telemetry.addData("Mode", "waiting");
        telemetry.update();

        // wait for start button.
        waitForStart();

        gripPosition = MAX_POSITION; // set grip to full open

        while (opModeIsActive())
        {
            leftY = gamepad1.left_stick_y * 1;
            rightY = gamepad1.right_stick_y * 1;

            leftDrive.setPower(Range.clip(leftY, -1.0, 1.0));
            rightDrive.setPower(Range.clip(rightY, -1.0, 1.0));

            telemetry.addData("Mode", "running");
            telemetry.addData("sticks", "  left: " + leftY + "  right: " + rightY);

            // Control the arm motor with A and B buttons (increment or decrement motor power)
            if (gamepad1.a)
            {
                if (getRuntime() - aLastTime > .075)
                {
                    armMotor.setPower(Range.clip(-ARM_POWER_INCREMENT, -1.0, 1.0)); // Move arm down
                    aLastTime = getRuntime();
                }
            }
            else if (gamepad1.b)
            {
                if (getRuntime() - bLastTime > .075)
                {
                    armMotor.setPower(Range.clip(ARM_POWER_INCREMENT, -1.0, 1.0)); // Move arm up
                    bLastTime = getRuntime();
                }
            }
            else
            {
                armMotor.setPower(0); // Stop the arm motor when no button is pressed
            }

            // Intake Toggle Control using X and Y buttons
            if (gamepad1.x && !lastXPress) // Check if X is pressed and was not previously pressed
            {
                intakeOn = !intakeOn; // Toggle the intake state
                lastXPress = true; // Mark X as pressed to prevent double toggling
            }
            else if (!gamepad1.x)
            {
                lastXPress = false; // Reset X press status once button is released
            }

            if (gamepad1.y && !lastYPress) // Y button to turn off the intake
            {
                intakeOn = false; // Ensure intake is off when Y is pressed
                lastYPress = true;
            }
            else if (!gamepad1.y)
            {
                lastYPress = false;
            }

            // Set intake servo position based on intake toggle state
            if (intakeOn)
            {
                intakeServo.setPosition(MAX_POSITION); // Set intake servo to open position
            }
            else
            {
                intakeServo.setPosition(MIN_POSITION); // Set intake servo to closed position
            }

            // Set continuous servo power level and direction
            if (gamepad1.dpad_left)
                contPower = .20;
            else if (gamepad1.dpad_right)
                contPower = -.20;
            else
                contPower = 0.0;

            // Set the continuous servo power
            contServo.setPower(contPower);

            telemetry.addData("intake servo", "position=%.2f", intakeServo.getPosition());
            telemetry.addData("intake", intakeOn ? "On" : "Off");
            telemetry.update();
            idle();
        }
    }
}

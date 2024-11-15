package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

@Autonomous
public class FreightFenzy_REDAuton1 extends LinearOpMode {

  private DcMotor RightDrive;
  private DcMotor LeftDrive;
  private DcMotor Arm;
  private DcMotor Intake;
  
  //Convert from the counts per revolution of the encoder to counts per inch
  static final double HD_COUNTS_PER_REV = 28;
  static final double DRIVE_GEAR_REDUCTION = 20.15293;
  static final double WHEEL_CIRCUMFERENCE_MM = 90 * Math.PI;
  static final double DRIVE_COUNTS_PER_MM = (HD_COUNTS_PER_REV * DRIVE_GEAR_REDUCTION) / WHEEL_CIRCUMFERENCE_MM;
  static final double DRIVE_COUNTS_PER_IN = DRIVE_COUNTS_PER_MM * 25.4;
    
  @Override
  public void runOpMode() {

    RightDrive = hardwareMap.get(DcMotor.class, "RightDrive");
    LeftDrive = hardwareMap.get(DcMotor.class, "LeftDrive");
    Arm = hardwareMap.get(DcMotor.class, "Arm");
    Intake = hardwareMap.get(DcMotor.class, "Intake");

   // reverse left drive motor direciton
    LeftDrive.setDirection(DcMotorSimple.Direction.REVERSE);
    
    waitForStart();
    if (opModeIsActive()) {
      // Create target positions
      rightTarget = RightDrive.getCurrentPosition() + (int)(30 * DRIVE_COUNTS_PER_IN);
      leftTarget = LeftDrive.getCurrentPosition() + (int)(15 * DRIVE_COUNTS_PER_IN);
      
      // set target position
      LeftDrive.setTargetPosition(leftTarget);
      RightDrive.setTargetPosition(rightTarget);
      
      //switch to run to position mode
      LeftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
      RightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
      
      //run to position at the desiginated power
      LeftDrive.setPower(0.7);
      RightDrive.setPower(0.7);
      
      // wait until both motors are no longer busy running to position
      while (opModeIsActive() && (LeftDrive.isBusy() || RightDrive.isBusy())) {
      }
      
      // set motor power back to 0 
      LeftDrive.setPower(0);
      RightDrive.setPower(0);
        }
     }
 }

package com.qualcomm.ftcrobotcontroller.opmodes;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;



public class BlueTimeAuto extends LinearOpMode {

    DcMotor motorRight;
    DcMotor motorLeft;
    DcMotor DebrisMotor;
    DcMotor RollerMotor;
    Servo LeftZipline;
    Servo RightZipline;
    Servo ClimberServo;
    Servo HopperDoor;
    Servo LiftServo;
    Servo RightTail;
    Servo LeftTail;
    AnalogInput rollerPhotogate;
    AnalogInput elevatorPhotogate;
    final double turnPower = 0.01;
    final double arcOffset = .9;
    final double movePower = .25;
    final double rollerPower = -.9;
    public void stopMotors() throws InterruptedException{
        motorLeft.setPower(0);
        motorRight.setPower(0);
        waitOneFullHardwareCycle();
    }
    @Override
    public void runOpMode() throws InterruptedException {

        map();
        waitForStart();
        waitForNextHardwareCycle();
        // RollerStop();
        //RaiseHopper();
        RollerMotor.setPower(rollerPower);
        //forward(movePower, 2000);
        //waitOneFullHardwareCycle();
        //turnLeft(turnPower, 3000);
        //waitOneFullHardwareCycle();
        //forward(movePower, 35000);
        //waitOneFullHardwareCycle();
        //turnLeft(.69, .85);
        //waitOneFullHardwareCycle();
        //LastForward();
        //waitOneFullHardwareCycle();
        //AutoFlip();
        //waitOneFullHardwareCycle();
        waitOneFullHardwareCycle();
        motorLeft.setPower(turnPower);
        motorRight.setPower(turnPower +arcOffset);
        Thread.sleep(2000);
        waitOneFullHardwareCycle();
        stopMotors();
        motorLeft.setPower(movePower);
        motorRight.setPower(movePower);
        Thread.sleep(10000);
        stopMotors();
    }


    public void map() throws InterruptedException {
        waitOneFullHardwareCycle();
        motorRight = hardwareMap.dcMotor.get("motor_2");
        motorLeft = hardwareMap.dcMotor.get("motor_1");
        motorLeft.setDirection(DcMotor.Direction.REVERSE);
        RollerMotor = hardwareMap.dcMotor.get("RollerMotor");
        RollerMotor.setDirection(DcMotor.Direction.REVERSE);
        DebrisMotor = hardwareMap.dcMotor.get("DebrisMotor");
        LeftZipline = hardwareMap.servo.get("LeftZipline");
        RightZipline = hardwareMap.servo.get("RightZipline");
        RightZipline.setDirection(Servo.Direction.REVERSE);
        waitOneFullHardwareCycle();
        motorLeft.setMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
        motorRight.setMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
        DebrisMotor.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        //may need a wait
        DebrisMotor.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
        DebrisMotor.setTargetPosition(0);
        DebrisMotor.setPower(.015);
        LiftServo = hardwareMap.servo.get("LiftServo");
        LiftServo.setPosition(.5);
        ClimberServo = hardwareMap.servo.get("ClimberServo");
        HopperDoor = hardwareMap.servo.get("HopperDoor");
        HopperDoor.setPosition(.5);
        //ClimberServo.setPosition(0.5);
        rollerPhotogate = hardwareMap.analogInput.get("rollerPhotogate");
        elevatorPhotogate = hardwareMap.analogInput.get("elevatorPhotogate");
        RightTail=hardwareMap.servo.get("RightTail");
        LeftTail =hardwareMap.servo.get("LeftTail");
        LeftTail.setDirection(Servo.Direction.REVERSE);
        waitOneFullHardwareCycle();
        ClimberServo.setPosition(0);
        LeftZipline.setPosition(.5);
        RightZipline.setPosition(.5);
        RightTail.setPosition(.1);
        LeftTail.setPosition(.1);
        waitOneFullHardwareCycle();
    }





}
package com.qualcomm.ftcrobotcontroller.opmodes;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.hardware.Servo;



public class FellowshipAuto extends LinearOpMode {

    DcMotor motorRight;
    DcMotor motorLeft;
    DcMotor DebrisMotor;
    DcMotor RollerMotor;
    Servo LeftZipline;
    Servo RightZipline;
    Servo LiftServo;
    Servo ClimberServo;
    Servo HopperDoor;
    AnalogInput rollerPhotogate;
    AnalogInput elevatorPhotogate;
    final float EncoderPerRotation = 1680;
    final float EncoderPerRotation40 = 1120;
    final float maxAngle = 35;
    final float minAngle = 28;
    final double triggerCutoff = .2;
    final double power = .9;
    final double searchingPower = 0.1;
    final double miniPower = 0.5;
    boolean RightDown = false;
    boolean LeftDown = false;
    boolean Braked = false;

    float smallPower = (float) miniPower;


    @Override
    public void runOpMode() {
        map();
        forward();
        turn();
    }


    public void map() {

        motorRight = hardwareMap.dcMotor.get("motor_2");
        motorLeft = hardwareMap.dcMotor.get("motor_1");
        motorLeft.setDirection(DcMotor.Direction.REVERSE);
        RollerMotor = hardwareMap.dcMotor.get("RollerMotor");
        RollerMotor.setDirection(DcMotor.Direction.REVERSE);
        DebrisMotor = hardwareMap.dcMotor.get("DebrisMotor");
        DebrisMotor.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        motorLeft.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        motorRight.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        DebrisMotor.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        //may need a wait
        DebrisMotor.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
        DebrisMotor.setTargetPosition(0);
        DebrisMotor.setPower(.015);
        LeftZipline = hardwareMap.servo.get("LeftZipline");
        RightZipline = hardwareMap.servo.get("RightZipline");
        RightZipline.setDirection(Servo.Direction.REVERSE);
        LiftServo = hardwareMap.servo.get("LiftServo");
        //LeftZipline.setPosition(0.5);
        //RightZipline.setPosition(0.5);
        ClimberServo = hardwareMap.servo.get("ClimberServo");
        //ClimberServo.setPosition(0.5);
        rollerPhotogate = hardwareMap.analogInput.get("rollerPhotogate");
        elevatorPhotogate = hardwareMap.analogInput.get("elevatorPhotogate");
        HopperDoor = hardwareMap.servo.get("HopperDoor");
        HopperDoor.setPosition(.5);
        LeftZipline.setPosition(.5);
        RightZipline.setPosition(.5);
        ClimberServo.setPosition(0);
        LiftServo.setPosition(.5);
    }

    public void forward() {
        motorLeft.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
        motorLeft.setPower(0.22);
        motorLeft.setTargetPosition(55);
        motorRight.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
        motorRight.setPower(0.22);
        motorRight.setTargetPosition(55);
    }

    public void turn() {
        motorLeft.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
        motorLeft.setPower(-0.1);
        motorLeft.setTargetPosition(10);
        motorRight.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
        motorRight.setPower(0.1);
        motorRight.setTargetPosition(10);
    }

}
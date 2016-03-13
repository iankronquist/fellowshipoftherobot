package com.qualcomm.ftcrobotcontroller.opmodes;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;



public class HangerReset extends OpMode {

    DcMotor motorRight;
    DcMotor motorLeft;
    DcMotor DebrisMotor;
    DcMotor RollerMotor;
    DcMotor RightHangMotor;
    DcMotor LeftHangMotor;
    Servo LeftZipline;
    Servo RightZipline;
    Servo LiftServo;
    Servo ClimberServo;
    Servo HopperDoor;
    Servo RightHangServo;
    Servo LeftHangServo;
    AnalogInput rollerPhotogate;
    AnalogInput elevatorPhotogate;

    final double power = .3;


    final double zero = .63;
    final double high = .66;
    final double offset = 0.035;






    public HangerReset() {

    }

    @Override
    public void init() {

        motorRight = hardwareMap.dcMotor.get("motor_2");
        motorLeft = hardwareMap.dcMotor.get("motor_1");
        motorLeft.setDirection(DcMotor.Direction.REVERSE);
        RollerMotor = hardwareMap.dcMotor.get("RollerMotor");
        RollerMotor.setDirection(DcMotor.Direction.REVERSE);
        DebrisMotor = hardwareMap.dcMotor.get("DebrisMotor");
        DebrisMotor.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        //may need a wait
        DebrisMotor.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
        DebrisMotor.setTargetPosition(0);
        DebrisMotor.setPower(0);
        LeftHangMotor = hardwareMap.dcMotor.get("LeftHangMotor");
        RightHangMotor = hardwareMap.dcMotor.get("RightHangMotor");
        LeftHangMotor.setDirection(DcMotor.Direction.REVERSE);
        LeftZipline = hardwareMap.servo.get("LeftZipline");
        RightZipline = hardwareMap.servo.get("RightZipline");
        RightZipline.setDirection(Servo.Direction.REVERSE);
        LiftServo = hardwareMap.servo.get("LiftServo");
        ClimberServo = hardwareMap.servo.get("ClimberServo");
        rollerPhotogate = hardwareMap.analogInput.get("rollerPhotogate");
        elevatorPhotogate = hardwareMap.analogInput.get("elevatorPhotogate");
        RightHangServo=hardwareMap.servo.get("RightHangServo");
        LeftHangServo =hardwareMap.servo.get("LeftHangServo");
        LeftHangServo.setDirection(Servo.Direction.REVERSE);
        HopperDoor = hardwareMap.servo.get("HopperDoor");
        HopperDoor.setPosition(.5);
        LeftZipline.setPosition(.5);
        RightZipline.setPosition(.5);
        ClimberServo.setPosition(0);
        LiftServo.setPosition(.5);
        RightHangServo.setPosition(high);
        LeftHangServo.setPosition(high+offset);
    }

    @Override
    public void loop() {
RightHangServo.setPosition(high);
        LeftHangServo.setPosition(high+offset);
        if(Math.abs(gamepad1.right_stick_y)>.1) {
            RightHangMotor.setPower(gamepad1.right_stick_y);
        }else if(Math.abs(gamepad1.left_stick_y)>.1) {
            LeftHangMotor.setPower(gamepad1.left_stick_y);
        }else{
            RightHangMotor.setPower(0);
            LeftHangMotor.setPower(0);
        }
    }
    @Override
    public void stop(){

    }




}
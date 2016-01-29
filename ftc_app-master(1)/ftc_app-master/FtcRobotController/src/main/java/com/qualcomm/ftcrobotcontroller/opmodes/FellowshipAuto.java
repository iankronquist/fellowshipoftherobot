package com.qualcomm.ftcrobotcontroller.opmodes;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
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
    Servo ClimberServo;
    Servo HopperDoor;
    Servo LiftServo;
    AnalogInput rollerPhotogate;
    AnalogInput elevatorPhotogate;
    final float EncoderPerRotation = 1680;
    final float EncoderPerRotation40 = 1120;
    final float maxAngle = 35;
    final float minAngle = 28;
    final double triggerCutoff = .2;
    final double power = .9;
    final double searchingPower = 0.1;
    boolean OpenFound = false;
    boolean done = false;

    public void forward(double ForwardPower, int Target) {
        while(motorLeft.getCurrentPosition()>-Target||motorRight.getCurrentPosition()>-Target){
            motorLeft.setPower(ForwardPower);
            motorRight.setPower(ForwardPower);}
        motorLeft.setPower(0);
        motorRight.setPower(0);
        telemetry.addData("LeftEncoder", motorLeft.getCurrentPosition());
    }

    public void turnRight(double TurnPower, int Target) {
        while(motorLeft.getCurrentPosition()>-Target||motorRight.getCurrentPosition()<Target){
            motorLeft.setPower(TurnPower);
            motorRight.setPower(-TurnPower);
        }
    }
    public void turnLeft(double TurnPower, int Target) {
        while(motorLeft.getCurrentPosition()<Target||motorRight.getCurrentPosition()>-Target){
            motorLeft.setPower(-TurnPower);
            motorRight.setPower(TurnPower);
        }
    }

    public void RollerStop() {
        if(rollerPhotogate.getValue() >= 500)//photogate blocked?
        {
            RollerMotor.setPower(0);//stop motor
        } else {
            RollerMotor.setPower(-searchingPower);
        }
    }


    public void LookForClip(){
        if(elevatorPhotogate.getValue()>=100) {
            ElevatorStop();
            done =true;
        }
    }
    public void FindOpen(){
        if(elevatorPhotogate.getValue() <= 30){
            OpenFound = true;
        }
    }

    public void RaiseHopper(){
        while(!OpenFound&&!done){
            LiftServo.setPosition(0);
            FindOpen();
        }
        while(OpenFound&&!done){
            LookForClip();
        }
    }


    public void ElevatorStop() {
        LiftServo.setPosition(.5);
    }
    public void resetEncoders(){
        motorLeft.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        motorRight.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        motorLeft.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        motorRight.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);

    }

    @Override
    public void runOpMode() throws InterruptedException {

        map();
        waitForStart();
        RollerStop();
        RaiseHopper();
        /*forward(1, 100);
        resetEncoders();
        turnRight(1, 100);
        resetEncoders();
        turnLeft(1, 100);
        resetEncoders();*/
    }


    public void map() {

        motorRight = hardwareMap.dcMotor.get("motor_2");
        motorLeft = hardwareMap.dcMotor.get("motor_1");
        motorLeft.setDirection(DcMotor.Direction.REVERSE);
        RollerMotor = hardwareMap.dcMotor.get("RollerMotor");
        RollerMotor.setDirection(DcMotor.Direction.REVERSE);
        DebrisMotor = hardwareMap.dcMotor.get("DebrisMotor");
        LeftZipline = hardwareMap.servo.get("LeftZipline");
        RightZipline = hardwareMap.servo.get("RightZipline");
        RightZipline.setDirection(Servo.Direction.REVERSE);
        motorLeft.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        motorRight.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        telemetry.addData("LeftEncoder", motorLeft.getCurrentPosition());
        motorLeft.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        motorRight.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
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
        ClimberServo.setPosition(0);
        LeftZipline.setPosition(.5);
        RightZipline.setPosition(.5);
    }





}
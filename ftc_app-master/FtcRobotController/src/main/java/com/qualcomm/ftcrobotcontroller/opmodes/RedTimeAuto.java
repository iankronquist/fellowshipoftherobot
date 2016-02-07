package com.qualcomm.ftcrobotcontroller.opmodes;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;



public class RedTimeAuto extends LinearOpMode {

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
    final double searchingPower = 0.1;
    final double movePower = .2;
    final double turnPower =.5;
    boolean OpenFound = false;
    boolean OpenFound2  = false;
    boolean OpenFound3 = false;
    boolean done = false;
    boolean done2 = false;
    boolean done3 = false;

    public void forward(double ForwardPower, long time) throws InterruptedException{
            motorLeft.setPower(ForwardPower);
            motorRight.setPower(ForwardPower);
            Thread.sleep(time);
            stopMotors();
            waitOneFullHardwareCycle();
    }

    public void turnLeft(double TurnPower, long time) throws InterruptedException{
            motorLeft.setPower(TurnPower);
            motorRight.setPower(-TurnPower);
            Thread.sleep(time);
            stopMotors();
            waitOneFullHardwareCycle();
    }
    public void turnRight(double TurnPower, long time) throws InterruptedException{
            motorLeft.setPower(-TurnPower);
            motorRight.setPower(TurnPower);
            Thread.sleep(time);
            stopMotors();
            waitOneFullHardwareCycle();

    }

    public void stopMotors(){
        motorLeft.setPower(0);
        motorRight.setPower(0);
    }

    public void RollerStop() throws InterruptedException {
        while(rollerPhotogate.getValue() < 500){
            RollerMotor.setPower(-searchingPower);
            waitOneFullHardwareCycle();
        }
        RollerMotor.setPower(0);
    }


    public void LookForClip(){
        if(elevatorPhotogate.getValue()>=100) {
            ElevatorStop();
            done =true;
        }
    }
    public void LookForClip2(){
        if(elevatorPhotogate.getValue()>=100) {
            ElevatorStop();
            done2 =true;
        }
    }
    public void LookForClip3(){
        if(elevatorPhotogate.getValue()>=100) {
            ElevatorStop();
            done3 =true;
        }
    }
    public void FindOpen(){
        if(elevatorPhotogate.getValue() <= 30){
            OpenFound = true;
        }
    }
    public void FindOpen2(){
        if(elevatorPhotogate.getValue() <= 30){
            OpenFound2 = true;
        }
    }
    public void FindOpen3(){
        if(elevatorPhotogate.getValue() <= 30){
            OpenFound3 = true;
        }
    }
    public void RaiseHopper() throws InterruptedException{
        while(!OpenFound && !done) {
            LiftServo.setPosition(0);
            FindOpen();
            waitOneFullHardwareCycle();
        }
        while(OpenFound && !done) {
            LookForClip();
            waitOneFullHardwareCycle();
        }

        while (done && !OpenFound2) {
            LiftServo.setPosition(0);
            FindOpen2();
            waitOneFullHardwareCycle();
        }
        while(OpenFound2&&!done2){
            LookForClip2();
            waitOneFullHardwareCycle();
        }
        while(done2&&!OpenFound3){
            LiftServo.setPosition(0);
            FindOpen3();
            waitOneFullHardwareCycle();
        }
        while(OpenFound3&&!done3){
            LookForClip3();
            waitOneFullHardwareCycle();
        }







    }

    public void AutoFlip() throws InterruptedException{
        ClimberServo.setPosition(1);
        waitOneFullHardwareCycle();
    }



    public void ElevatorStop() {
        LiftServo.setPosition(.5);
    }

    public void LastForward() throws InterruptedException{
        motorLeft.setPower(.69);
        motorRight.setPower(.69);
        Thread.sleep(1000);
        stopMotors();
    }





    @Override
    public void runOpMode() throws InterruptedException {

        map();
        waitForStart();
        waitForNextHardwareCycle();
        RollerStop();
        RaiseHopper();

        RollerMotor.setPower(-.9);
        forward(movePower, 2000);
        waitOneFullHardwareCycle();
        turnLeft(turnPower, 3000);
        waitOneFullHardwareCycle();
        forward(movePower, 15000);
        waitOneFullHardwareCycle();
        //turnLeft(.69, .85);
        waitOneFullHardwareCycle();
        //LastForward();
        waitOneFullHardwareCycle();
        //AutoFlip();
        //waitOneFullHardwareCycle();
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
        ClimberServo.setPosition(0);
        LeftZipline.setPosition(.5);
        RightZipline.setPosition(.5);
        waitOneFullHardwareCycle();
    }





}
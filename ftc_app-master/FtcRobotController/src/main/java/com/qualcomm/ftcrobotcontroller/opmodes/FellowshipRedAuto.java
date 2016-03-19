package com.qualcomm.ftcrobotcontroller.opmodes;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.Servo;



public class FellowshipRedAuto extends LinearOpMode {

    DcMotor motorRight;
    DcMotor motorLeft;
    DcMotor DebrisMotor;
    DcMotor RollerMotor;
    Servo LeftZipline;
    Servo RightZipline;
    Servo ClimberServo;
    Servo HopperDoor;
    Servo LiftServo;
    Servo RightHangServo;
    Servo LeftHangServo;
    Servo beaconServo;
    Servo LeftCowcatcher;
    Servo RightCowcatcher;
    Servo RightTail;
    Servo LeftTail;
    AnalogInput rollerPhotogate;
    AnalogInput elevatorPhotogate;
    ColorSensor floorSeeker;
    ColorSensor beaconSeeker;
    GyroSensor gyro;
    final double turnPower = 0;
    final double arcOffset = .9;
    final double movePower = .35;
    final double rollerPower = -.9;
    final double zero = .53;
    final double cowcatcherOffset = .03;
    final double higherPower = .3;
    final double lowerPower = .2;
    final double rightHit =.3;
    final double leftHit = .7;
    final double tailOffset = .16;


    public void stopMotors() throws InterruptedException{
        motorLeft.setPower(0);
        waitOneFullHardwareCycle();
        motorRight.setPower(0);
        waitOneFullHardwareCycle();
    }


    public void findRedStripe() throws InterruptedException {
        while (floorSeeker.red() < 7) {
            motorLeft.setPower(.8);
            motorRight.setPower(.8);
            waitOneFullHardwareCycle();
        }
    }
    public void findWhiteStripe() throws InterruptedException {
        while (floorSeeker.green() < 10) {
            motorLeft.setPower(.6);
            motorRight.setPower(.6);
            waitOneFullHardwareCycle();
        }
    }
    public void dumpClimbers() throws InterruptedException{
        ClimberServo.setPosition(1);
        Thread.sleep(500);
        ClimberServo.setPosition(.8);
        Thread.sleep(500);
        ClimberServo.setPosition(1);
    }
    public void followLine(double meme) throws InterruptedException{
        resetStartTime();
        while (getRuntime() < meme) {
            if (floorSeeker.green()>=10) {
                motorLeft.setPower(lowerPower);
                motorRight.setPower(higherPower);
                waitOneFullHardwareCycle();
            }

            if (floorSeeker.green()<10) {
                motorLeft.setPower(higherPower);
                motorRight.setPower(lowerPower);
                waitOneFullHardwareCycle();
            }
            waitOneFullHardwareCycle();
        }

    }
    public void BackAway() throws InterruptedException{
        motorLeft.setPower(-.3);
        motorRight.setPower(-.8);
        Thread.sleep(1000);
        gyroTurn(45, .9, "right");
        motorRight.setPower(.6);
        motorLeft.setPower(.6);
        Thread.sleep(3000);
        gyroTurn(195,.8, "right");
    }

    public void hitBeacon() throws InterruptedException {
        if (beaconSeeker.red() > 8) {
            beaconServo.setPosition(rightHit);
            telemetry.addData("red", arcOffset);
        } else if (beaconSeeker.blue() > 8) {
            beaconServo.setPosition(leftHit);
            telemetry.addData("blue", arcOffset);
        } else {
            waitOneFullHardwareCycle();
        }

    }

    public void gyroTurn(int desiredAngle,double power, String direction) throws InterruptedException{

        if (direction == "left") {
            while (Math.abs(desiredAngle - gyro.getHeading())>5) {
                motorLeft.setPower(power);
                motorRight.setPower(-power);
                telemetry.addData("gyro", gyro.getHeading());
                waitOneFullHardwareCycle();
            }
        }
        if (direction == "right") {
            while (Math.abs(desiredAngle -gyro.getHeading())>5) {

                motorLeft.setPower(-power);
                motorRight.setPower(power);
                telemetry.addData("gyro", gyro.getHeading());
                waitOneFullHardwareCycle();
            }
        }
    }



    @Override
    public void runOpMode() throws InterruptedException {

        map();
        waitForStart();
        waitForNextHardwareCycle();
        RollerMotor.setPower(rollerPower);
        motorLeft.setPower(.8);
        motorRight.setPower(.8);
        Thread.sleep(500);
        waitOneFullHardwareCycle();
        stopMotors();
        waitOneFullHardwareCycle();
        gyroTurn(322, .8, "left");
        waitOneFullHardwareCycle();
        findRedStripe();
        waitOneFullHardwareCycle();
        stopMotors();
        waitOneFullHardwareCycle();
        telemetry.addData("angle", gyro.getHeading());
        gyroTurn(355,.8, "right");
        waitOneFullHardwareCycle();
        stopMotors();
        waitOneFullHardwareCycle();
        findWhiteStripe();
        waitOneFullHardwareCycle();
        stopMotors();
        waitOneFullHardwareCycle();
        motorLeft.setPower(-.8);
        motorRight.setPower(-.8);
        Thread.sleep(500);
        waitOneFullHardwareCycle();
        stopMotors();
        waitOneFullHardwareCycle();
        Thread.sleep(200);
        waitOneFullHardwareCycle();
        gyroTurn(277,.8, "left");
        waitOneFullHardwareCycle();
        stopMotors();
        waitOneFullHardwareCycle();
        followLine(1.8);
       // RightCowcatcher.setPosition(.38 +cowcatcherOffset);
       // LeftCowcatcher.setPosition(.38);
        waitOneFullHardwareCycle();
        RollerMotor.setPower(0);
        waitOneFullHardwareCycle();
        motorRight.setPower(.6);
        motorLeft.setPower(.6);
        waitOneFullHardwareCycle();
        Thread.sleep(500);
        waitOneFullHardwareCycle();
        stopMotors();
        waitOneFullHardwareCycle();
        Thread.sleep(200);
        dumpClimbers();
        waitOneFullHardwareCycle();
        //hitBeacon();
        waitOneFullHardwareCycle();
        waitOneFullHardwareCycle();
        BackAway();
        waitOneFullHardwareCycle();
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
        beaconServo = hardwareMap.servo.get("beaconServo");
        beaconServo.setPosition(.5);
        floorSeeker = hardwareMap.colorSensor.get("floorSeeker");
        beaconSeeker = hardwareMap.colorSensor.get("beaconSeeker");
        beaconSeeker.setI2cAddress(0x70);
        beaconSeeker.enableLed(true);
        waitOneFullHardwareCycle();
        beaconSeeker.enableLed(false);
        gyro = hardwareMap.gyroSensor.get("gyro");
        gyro.calibrate();

        //ClimberServo.setPosition(0.5);
        RightHangServo = hardwareMap.servo.get("RightHangServo");
        LeftHangServo  = hardwareMap.servo.get("LeftHangServo");
        LeftHangServo.setDirection(Servo.Direction.REVERSE);
        rollerPhotogate = hardwareMap.analogInput.get("rollerPhotogate");
        elevatorPhotogate = hardwareMap.analogInput.get("elevatorPhotogate");
        RightTail=hardwareMap.servo.get("RightTail");
        LeftTail =hardwareMap.servo.get("LeftTail");
        LeftTail.setDirection(Servo.Direction.REVERSE);
        RightCowcatcher = hardwareMap.servo.get("RightCowcatcher");
        LeftCowcatcher = hardwareMap.servo.get("LeftCowcatcher");
        LeftCowcatcher.setDirection(Servo.Direction.REVERSE);
        waitOneFullHardwareCycle();
        ClimberServo.setPosition(0);
        LeftZipline.setPosition(.5);
        RightZipline.setPosition(.5);
        waitOneFullHardwareCycle();
        RightTail.setPosition(0.07);
        LeftTail.setPosition(0.07+tailOffset);
        waitOneFullHardwareCycle();
        RightHangServo.setPosition(zero+.01);
        LeftHangServo.setPosition(zero+.035);
        waitOneFullHardwareCycle();
        RightCowcatcher.setPosition(.77+cowcatcherOffset);
        LeftCowcatcher.setPosition(.77);
        waitOneFullHardwareCycle();
    }





}
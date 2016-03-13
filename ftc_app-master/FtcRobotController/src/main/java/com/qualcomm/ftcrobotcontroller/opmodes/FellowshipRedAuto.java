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
    Servo LeftCowcatcher;
    Servo RightCowcatcher;
    Servo beaconServo;
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
    final double higherPower = .6;
    final double lowerPower = .4;
    final double rightHit = .3;
    final double leftHit = .7;


    public void stopMotors() throws InterruptedException{
        motorLeft.setPower(0);
        waitOneFullHardwareCycle();
        motorRight.setPower(0);
        waitOneFullHardwareCycle();
    }
public void followLine() throws InterruptedException{
    resetStartTime();
    while (getRuntime() < 5) {
        if (floorSeeker.green()>=10) {
            motorLeft.setPower(higherPower);
            motorRight.setPower(lowerPower);
            waitOneFullHardwareCycle();
        }

        if (floorSeeker.green()<10) {
            motorLeft.setPower(lowerPower);
            motorRight.setPower(higherPower);
            waitOneFullHardwareCycle();
        }
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

    public void hitBeacon() throws InterruptedException {
        if (beaconSeeker.red() > 8) {
            beaconServo.setPosition(rightHit);
        } else if (beaconSeeker.blue() > 8) {
            beaconServo.setPosition(leftHit);
        } else {
            waitOneFullHardwareCycle();
        }

    }
        public void BackAway() throws InterruptedException{
        motorLeft.setPower(-.8);
            motorRight.setPower(-.5);
            Thread.sleep(1000);
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
            motorLeft.setPower(.5);
            motorRight.setPower(.5);
            waitOneFullHardwareCycle();
        }
    }
    public void gyroTurn(int desiredAngle, String direction) throws InterruptedException{

        if (direction == "left") {
            while (Math.abs(desiredAngle-gyro.getHeading())>5) {
                motorLeft.setPower(.8);
                motorRight.setPower(-.8);
                telemetry.addData("gyro", gyro.getHeading());
                waitOneFullHardwareCycle();
            }
        }
        if (direction == "right") {
            while (Math.abs(desiredAngle-gyro.getHeading())>5) {

                motorLeft.setPower(-.8);
                motorRight.setPower(.8);
                telemetry.addData("gyro", gyro.getHeading());
                waitOneFullHardwareCycle();
            }
        }
    }



    @Override
    public void runOpMode() throws InterruptedException {
Thread.sleep(2000);
        map();
        waitForStart();
        RollerMotor.setPower(rollerPower);
        motorLeft.setPower(.8);
        motorRight.setPower(.8);
        Thread.sleep(500);
        waitOneFullHardwareCycle();
        stopMotors();
        waitOneFullHardwareCycle();
        gyroTurn(315, "left");
        waitForNextHardwareCycle();
        findRedStripe();
        waitOneFullHardwareCycle();
        stopMotors();
        waitOneFullHardwareCycle();
        telemetry.addData("angle", gyro.getHeading());
        gyroTurn(0, "right");
        waitOneFullHardwareCycle();
        stopMotors();
        waitOneFullHardwareCycle();
        findWhiteStripe();
        waitOneFullHardwareCycle();
        stopMotors();
        waitOneFullHardwareCycle();
        motorLeft.setPower(-.8);
        motorRight.setPower(-.8);
        Thread.sleep(250);
        waitOneFullHardwareCycle();
        stopMotors();
        gyroTurn(270, "left");
        waitOneFullHardwareCycle();
        followLine();
        waitOneFullHardwareCycle();
        Thread.sleep(500);
        dumpClimbers();
        waitOneFullHardwareCycle();
        hitBeacon();
        waitOneFullHardwareCycle();
        Thread.sleep(500);
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
        LeftCowcatcher = hardwareMap.servo.get("LeftCowcatcher");
        RightCowcatcher = hardwareMap.servo.get("RightCowcatcher");
        LeftCowcatcher.setDirection(Servo.Direction.REVERSE);
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
        gyro = hardwareMap.gyroSensor.get("gyro");
        gyro.calibrate();
        //ClimberServo.setPosition(0.5);
        RightHangServo = hardwareMap.servo.get("RightHangServo");
        LeftHangServo  = hardwareMap.servo.get("LeftHangServo");
        LeftHangServo.setDirection(Servo.Direction.REVERSE);
        rollerPhotogate = hardwareMap.analogInput.get("rollerPhotogate");
        elevatorPhotogate = hardwareMap.analogInput.get("elevatorPhotogate");
        waitOneFullHardwareCycle();
        ClimberServo.setPosition(0);
        LeftZipline.setPosition(.5);
        RightZipline.setPosition(.5);
        waitOneFullHardwareCycle();
        RightHangServo.setPosition(zero+.01);
        LeftHangServo.setPosition(zero+.035);
        RightCowcatcher.setPosition(.7+cowcatcherOffset);
        LeftCowcatcher.setPosition(.7);
        waitOneFullHardwareCycle();
    }





}
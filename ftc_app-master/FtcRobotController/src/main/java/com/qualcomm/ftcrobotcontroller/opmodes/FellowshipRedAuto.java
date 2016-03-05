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
    Servo RightTail;
    Servo LeftTail;
    Servo RightHangServo;
    Servo LeftHangServo;
    AnalogInput rollerPhotogate;
    AnalogInput elevatorPhotogate;
    ColorSensor floorSeeker;
    GyroSensor gyro;
    final double turnPower = 0;
    final double arcOffset = .9;
    final double movePower = .35;
    final double rollerPower = -.9;
    final double zero = .53;


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
            motorLeft.setPower(.8);
            motorRight.setPower(.8);
            waitOneFullHardwareCycle();
        }
    }
    public void gyroTurn(int desiredAngle, String direction) throws InterruptedException{

        if (direction == "left") {
            while (gyro.getHeading() > desiredAngle) {
                double proportionalSpeed = .7+.3*((desiredAngle-gyro.getHeading())/desiredAngle);
                motorLeft.setPower(proportionalSpeed);
                motorRight.setPower(-proportionalSpeed);
                telemetry.addData("gyro", gyro.getHeading());
                waitOneFullHardwareCycle();
            }
        }
        if (direction == "right") {
            while (gyro.getHeading() < desiredAngle) {
                double proportionalSpeed = .7+.3*((desiredAngle-gyro.getHeading())/desiredAngle);
                motorLeft.setPower(-proportionalSpeed);
                motorRight.setPower(proportionalSpeed);
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
        findRedStripe();
        waitOneFullHardwareCycle();
        stopMotors();
        waitOneFullHardwareCycle();
        gyroTurn(45,"right");
        waitOneFullHardwareCycle();
        stopMotors();
        waitOneFullHardwareCycle();
        findWhiteStripe();
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
        floorSeeker = hardwareMap.colorSensor.get("floorSeeker");
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
        waitOneFullHardwareCycle();
        ClimberServo.setPosition(0);
        LeftZipline.setPosition(.5);
        RightZipline.setPosition(.5);
        waitOneFullHardwareCycle();
        RightTail.setPosition(0);
        LeftTail.setPosition(0);
        waitOneFullHardwareCycle();
        RightHangServo.setPosition(zero+.01);
        LeftHangServo.setPosition(zero+.035);
        waitOneFullHardwareCycle();
    }





}
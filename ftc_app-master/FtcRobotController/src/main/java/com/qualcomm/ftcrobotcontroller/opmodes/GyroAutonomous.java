package com.qualcomm.ftcrobotcontroller.opmodes;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;


public class GyroAutonomous extends LinearOpMode {

    DcMotor motorRight;
    DcMotor motorLeft;
    AnalogInput gyroSensor;
    final float EncoderPerRotation = 1680;
    final float EncoderPerRotation40 = 1120;
    final double movePower = .2;


    public void map() throws InterruptedException {
        waitOneFullHardwareCycle();
        motorRight = hardwareMap.dcMotor.get("motor_2");
        motorLeft = hardwareMap.dcMotor.get("motor_1");
        motorLeft.setDirection(DcMotor.Direction.REVERSE);
        gyroSensor = hardwareMap.analogInput.get("gyroSensor");
        motorLeft.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        motorRight.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        motorLeft.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        motorRight.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
    }

    public void runOpMode() throws InterruptedException {

        map();
        resetEncoders();
        waitForStart();
        arc(movePower, 1);
    }

    public void arc(double ForwardPower, double Rot) throws InterruptedException
    {
        resetEncoders();
        double Targetfloat = Rot*EncoderPerRotation40;
        while(motorLeft.getCurrentPosition()>-Targetfloat||motorRight.getCurrentPosition()>-Targetfloat)
       // while(gyroSensor.getValue() < 500){
<<<<<<< HEAD
        {   //telemetry.addData("AngleX", gyroSensor.rawX());
            //telemetry.addData("AngleY", gyroSensor.rawY());
            //telemetry.addData("AngleZ", gyroSensor.rawZ());
=======
        {   telemetry.addData("AngleX", gyroSensor.rawX());
            telemetry.addData("AngleY", gyroSensor.rawY());
            telemetry.addData("AngleZ", gyroSensor.rawZ());
>>>>>>> origin/master
            motorLeft.setPower(0.25);
            motorRight.setPower(0.50);
        }
        waitOneFullHardwareCycle();
        }
    //}

    public void resetEncoders() throws InterruptedException{
        waitOneFullHardwareCycle();
        motorLeft.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        motorRight.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        waitOneFullHardwareCycle();
        motorLeft.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        motorRight.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        waitOneFullHardwareCycle();
    }
}








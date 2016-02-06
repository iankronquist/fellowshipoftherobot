package com.qualcomm.ftcrobotcontroller.opmodes;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.hardware.Servo;



public class FellowshipTele extends OpMode {

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
    final float EncoderPerRotation60 = 1680;
    final float maxAngle = 35;
    final float minAngle = 32;
    final double triggerCutoff = .2;
    final double power = .3;
    final double searchingPower = 0.1;
    final double miniPower = 0.35;
    boolean SearchingUp = false;
    boolean SearchingDown = false;
    boolean OpenFound = false;
    boolean climbersFlipped = false;
    float smallPower = (float)miniPower;

    public void RollerStop() {
        if(rollerPhotogate.getValue() >= 500)//photogate blocked?
        {
            RollerMotor.setPower(0);//stop motor
        } else {
            RollerMotor.setPower(searchingPower);
        }
    }

    public void ElevatorStop() {
        LiftServo.setPosition(.5);
    }

    public void flipClimbers(){
        ClimberServo.setPosition(1);
    }


        public void LookForClip(){
        if(elevatorPhotogate.getValue()>=100) {
            ElevatorStop();
            SearchingUp = false;
            SearchingDown = false;
            OpenFound = false;
        }
        }

    public void OpenDoors(){
    HopperDoor.setPosition(.9);
    }
//Braking cosde
    public void Brake(){
        RightZipline.setPosition(0);
        LeftZipline.setPosition(0);
    }

    public void UnBrake(){
        RightZipline.setPosition(.5);
        LeftZipline.setPosition(.5);
    }
    public void FindOpen(){
        if(elevatorPhotogate.getValue() <= 30){
            OpenFound = true;
        }
    }



    public FellowshipTele() {

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
        DebrisMotor.setPower(.035);
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

    @Override
    public void loop() {

        //drive code
        //may need change, this is from FTC
        if (gamepad1.left_stick_button) {
            float throttle = -gamepad1.left_stick_y;
            float direction = gamepad1.left_stick_x;
            float right = throttle - direction;
            float left = throttle + direction;
            right = Range.clip(right, -1, 1);
            left = Range.clip(left, -1, 1);
            right = smallPower * (float) scaleInput(right);
            left = smallPower * (float) scaleInput(left);
            motorRight.setPower(left);
            motorLeft.setPower(right);

        } else {
            float throttle = -gamepad1.left_stick_y;
            float direction = gamepad1.left_stick_x;
            float right = throttle - direction;
            float left = throttle + direction;
            right = Range.clip(right, -1, 1);
            left = Range.clip(left, -1, 1);
            right = (float) scaleInput(right);
            left = (float) scaleInput(left);
            motorRight.setPower(left);
            motorLeft.setPower(right);
        }

        //lift servo code
        //this is a continuous rotation servo
        //for a continuous rotation servo, 0 is spin one direction full power, 1 is the other direction full power
        //and 0.5 is stopped.
        /*if (gamepad2.dpad_up) {
            LiftServo.setPosition(0);
        } else if (gamepad2.dpad_down) {
            LiftServo.setPosition(1);
        } else {
            LiftServo.setPosition(0.5);
        }*/

            if (gamepad2.dpad_up) {
                    SearchingUp = true;
            }
            else if (gamepad2.dpad_down) {
                    SearchingDown = true;
            }




        //Bucket Tilting code
        //joystick reads from -1 to 1 on each axis
        float position = gamepad2.right_stick_x;
        //changing max angle allowed from degrees to encoder units
        float EncoderMax = (maxAngle / 360) * EncoderPerRotation60;
        //scaling position to have the max value as the max angle
        int TargetEncoderValue = (int) (position * EncoderMax);
        //DebrisMotor.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
        if (gamepad2.right_stick_x > .1 || gamepad2.right_stick_x < -.1) {
            DebrisMotor.setTargetPosition(TargetEncoderValue);
        }
        if (gamepad2.right_stick_x <= .1 && gamepad2.right_stick_x >= -.1) {
            DebrisMotor.setTargetPosition(0);
        }


        //Roller Code
        //when trigger is pressed, power is set
        if (gamepad2.right_trigger > triggerCutoff) {
            RollerMotor.setPower(power);
        } else if (gamepad2.left_trigger > triggerCutoff) {
            RollerMotor.setPower(-power);
        } else {
            RollerStop();
        }


        //Climber code
        if (gamepad1.right_bumper) {
            flipClimbers();
            climbersFlipped = true;
        } else if(climbersFlipped) {
            ClimberServo.setPosition(0.5);
        }else {
            ClimberServo.setPosition(0);
        }

//Hopper Door code
        if ((Math.abs(DebrisMotor.getCurrentPosition()) * 360 / EncoderPerRotation60) >= minAngle) {
            OpenDoors();
        } else {
            HopperDoor.setPosition(.5);
        }

        //Braking code
        if (gamepad1.y) {
            Brake();

        } else if(gamepad2.left_stick_x>0){
            RightZipline.setPosition((gamepad2.left_stick_x/2)+.5);
        }else if(gamepad2.left_stick_x<0){
            LeftZipline.setPosition((-gamepad2.left_stick_x/2)+.5);
        }else if (gamepad2.left_stick_x==0){
            LeftZipline.setPosition(.5);
            RightZipline.setPosition(.5);
        }else {
            UnBrake();
    }


if(SearchingUp&&!OpenFound){
     LiftServo.setPosition(0);
    FindOpen();

}
        if(SearchingUp&&OpenFound) {
            LiftServo.setPosition(0);
            LookForClip();
        }

        if(SearchingDown&&!OpenFound){
            LiftServo.setPosition(1);
            FindOpen();
        }
        if(SearchingDown&&OpenFound){
            LiftServo.setPosition(1);
            LookForClip();
        }
if(SearchingDown&&SearchingUp){
    SearchingDown = false;
}

        //telemetry section
        telemetry.addData("elevatorGate", elevatorPhotogate.getValue());
        telemetry.addData("elevatorEncoder", DebrisMotor.getCurrentPosition());

    }
    @Override
    public void stop(){

    }


    /*
     * This method scales the joystick input so for low joystick values, the
     * scaled value is less than linear.  This is to make it easier to drive
     * the robot more precisely at slower speeds.
     */
    double scaleInput(double dVal)  {
        double[] scaleArray = { 0.0, 0.05, 0.09, 0.10, 0.12, 0.15, 0.18, 0.24,
                0.30, 0.36, 0.43, 0.50, 0.60, 0.72, 0.85, 1.00, 1.00 };

        // get the corresponding index for the scaleInput array.
        int index = (int) (dVal * 16.0);

        // index should be positive.
        if (index < 0) {
            index = -index;
        }

        // index cannot exceed size of array minus 1.
        if (index > 16) {
            index = 16;
        }

        // get value from the array.
        double dScale = 0.0;
        if (dVal < 0) {
            dScale = -scaleArray[index];
        } else {
            dScale = scaleArray[index];
        }

        // return scaled value.
        return dScale;
    }

}
package com.qualcomm.ftcrobotcontroller.opmodes;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.hardware.Servo;



public class FellowshipTele extends OpMode {

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
    Servo beaconServo;
    Servo LeftTail;
    Servo RightTail;
    Servo LeftCowcatcher;
    Servo RightCowcatcher;
    AnalogInput rollerPhotogate;
    AnalogInput elevatorPhotogate;
    ColorSensor floorSeeker;
    ColorSensor beaconSeeker;
    final float EncoderPerRotation60 = 1680;
    final float maxAngle = 35;
    final float minAngle = 32;
    final double triggerCutoff = .2;
    final double power = .3;
    final double searchingPower = 0.1;
    final double miniPower = 1;
    final double cowcatcherOffset = .03;
    final double tailOffset = .16;
    boolean SearchingUp = false;
    boolean SearchingDown = false;
    boolean OpenFound = false;
    boolean climbersFlipped = false;
    boolean mountainMode = false;
    boolean hanging = false;
    boolean allGood = false;
    int hangerPosition = 0;
    float smallPower = (float)miniPower;
    final double zero = .60;
    final double mid = .64;
    final double high = .67;
    final double offset = 0.035;
    final float mountainAngle = 107;
    double desiredPosition;




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
/*public void setTailPosition(float angle){
    int encoderPosition = (int) ((angle/360)*EncoderPerRotation60);
    RightTail.setTargetPosition(encoderPosition);
    LeftTail.setTargetPosition(encoderPosition);


}*/

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
        DebrisMotor.setPower(0);
        RightTail=hardwareMap.servo.get("RightTail");
        LeftTail =hardwareMap.servo.get("LeftTail");
        LeftTail.setDirection(Servo.Direction.REVERSE);
        LeftHangMotor = hardwareMap.dcMotor.get("LeftHangMotor");
        RightHangMotor = hardwareMap.dcMotor.get("RightHangMotor");
        LeftHangMotor.setDirection(DcMotor.Direction.REVERSE);
        LeftZipline = hardwareMap.servo.get("LeftZipline");
        RightZipline = hardwareMap.servo.get("RightZipline");
        RightZipline.setDirection(Servo.Direction.REVERSE);
        LeftCowcatcher = hardwareMap.servo.get("LeftCowcatcher");
        RightCowcatcher = hardwareMap.servo.get("RightCowcatcher");
        LeftCowcatcher.setDirection(Servo.Direction.REVERSE);
        LiftServo = hardwareMap.servo.get("LiftServo");
        ClimberServo = hardwareMap.servo.get("ClimberServo");
        beaconServo = hardwareMap.servo.get("beaconServo");
        beaconServo.setPosition(.5);
        beaconSeeker=hardwareMap.colorSensor.get("beaconSeeker");
        beaconSeeker.setI2cAddress(0x70);
        rollerPhotogate = hardwareMap.analogInput.get("rollerPhotogate");
        elevatorPhotogate = hardwareMap.analogInput.get("elevatorPhotogate");
        floorSeeker = hardwareMap.colorSensor.get("floorSeeker");
        floorSeeker.enableLed(false);
        floorSeeker.enableLed(true);

        /*RightTail=hardwareMap.dcMotor.get("RightTail");
        LeftTail =hardwareMap.dcMotor.get("LeftTail");
        LeftTail.setDirection(DcMotor.Direction.REVERSE);*/
        RightHangServo=hardwareMap.servo.get("RightHangServo");
        LeftHangServo =hardwareMap.servo.get("LeftHangServo");
        LeftHangServo.setDirection(Servo.Direction.REVERSE);
        HopperDoor = hardwareMap.servo.get("HopperDoor");
        HopperDoor.setPosition(.5);
        LeftZipline.setPosition(.5);
        RightZipline.setPosition(.5);
        ClimberServo.setPosition(0);
        LiftServo.setPosition(.5);
        LeftTail.setPosition(.15);
        RightTail.setPosition(.15);

        RightCowcatcher.setPosition(.38+cowcatcherOffset);
        LeftCowcatcher.setPosition(.38);

        RightHangServo.setPosition(zero+.01);
        LeftHangServo.setPosition(zero+offset);
    }

    @Override
    public void loop() {
        DebrisMotor.setPower(.035);
        //RightTail.setPower(.25);
        //LeftTail.setPower(.25);
        boolean ready = (RightHangServo.getPosition() == desiredPosition||LeftHangServo.getPosition()==desiredPosition);
        //drive code
        //may need change, this is from FTC
        if (gamepad1.left_stick_button||mountainMode) {
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
            ClimberServo.setPosition(0.3);
        }else {
            ClimberServo.setPosition(0.3);
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

if(Math.abs(gamepad2.left_stick_x)>.2){
    LeftCowcatcher.setPosition(.6);
    RightCowcatcher.setPosition(.6+cowcatcherOffset);
}else if(allGood) {

    LeftCowcatcher.setPosition(.6);
    RightCowcatcher.setPosition(.6+cowcatcherOffset);
}else {

        RightCowcatcher.setPosition(.38 + cowcatcherOffset);
        LeftCowcatcher.setPosition(.38);

    }



        if(gamepad2.back){
            allGood=true;
        }

        if(allGood){
            RightHangServo.setPosition(mid);
            LeftHangServo.setPosition(mid);



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


        //tails
        if(gamepad1.a){
            mountainMode = true;
        }
        if(gamepad1.x){
            mountainMode = false;
        }

if(gamepad1.dpad_left) {
    beaconServo.setPosition(.3);
} else if(gamepad1.dpad_right){
            beaconServo.setPosition(.7);
        }else{
    beaconServo.setPosition(.5);
        }
if(mountainMode) {

    RightTail.setPosition(1);
    LeftTail.setPosition(.93);


} else{
    RightTail.setPosition(.15);
    LeftTail.setPosition(.15+tailOffset);
}
        //hangers
        if(gamepad1.dpad_up&&hangerPosition !=2){
            hangerPosition++;
        }
        if(gamepad1.dpad_down&&hangerPosition !=0){
            hangerPosition--;
        }
        if(hangerPosition == 0){
            desiredPosition = zero;
        }
        if(hangerPosition == 1){
            desiredPosition = mid;
        }
        if(hangerPosition == 2){
            desiredPosition = high;
        }
        RightHangServo.setPosition(desiredPosition + .01);
        LeftHangServo.setPosition(desiredPosition + offset);
        RightHangMotor.setPower(-gamepad1.right_stick_y);
        LeftHangMotor.setPower(-gamepad1.right_stick_y);
        if(Math.abs(gamepad1.right_stick_y)<.1){
            RightHangMotor.setPower(0);
            LeftHangMotor.setPower(0);
        }
        beaconSeeker.enableLed(true);
        beaconSeeker.enableLed(false);

        //telemetry section
        telemetry.addData("elevatorGate", elevatorPhotogate.getValue());
        telemetry.addData("elevatorEncoder", DebrisMotor.getCurrentPosition());
        telemetry.addData("red", beaconSeeker.red());
        telemetry.addData("green", beaconSeeker.green());
        telemetry.addData("blue", beaconSeeker.blue());




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
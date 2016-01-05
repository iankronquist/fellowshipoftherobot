package com.qualcomm.ftcrobotcontroller.opmodes;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.hardware.Servo;


/**
 * TeleOp Mode
 * <p>
 * Enables control of the robot via the gamepad
 */
public class FellowshipTele extends OpMode {

    DcMotor motorRight;
    DcMotor motorLeft;
    DcMotor DebrisMotor;
    DcMotor RollerMotor;
    //Servo LeftZipline;
    //Servo RightZipline;
    Servo LiftServo;
<<<<<<< HEAD
<<<<<<< HEAD
    AnalogInput rollerPhotogate;
    AnalogInput elevatorPhotogate;
    int HopperPosition = 0;
=======
>>>>>>> parent of ac80adf... added photogate code. edited elevator servo
=======
>>>>>>> parent of ac80adf... added photogate code. edited elevator servo
    final float EncoderPerRotation = 1680;
    final float maxAngle = 35;
    final double triggerCutoff = .2;
    final double servoIncrement = .001;
    final double power = .9;
<<<<<<< HEAD
<<<<<<< HEAD
    boolean RightDown = false;
    boolean LeftDown = false;




    public void RollerStop() {
        if (rollerPhotogate.getValue() >= 1000)//photogate blocked?
        {
            RollerMotor.setPower(0); //stop motor
        } else if (RollerMotor.getPower() > 0) //motor turning with positive power?
        {
            RollerMotor.setPower(.1); //set to low positive power to find flag
        } else {
            RollerMotor.setPower(-.1);//motor must be turning with negative power, so it is set to low negative power to search for flag.
        }
    }

    public void ElevatorStop() {
        if (ElevatorPhotogate.getValue() > 0)
        {
            LiftServo.setPower(0);
        }
    }

    public void HopperRaise(){
        LiftServo.setPosition(0);
    }



=======
    /**
     * Constructor
     */
>>>>>>> parent of ac80adf... added photogate code. edited elevator servo
=======
    /**
     * Constructor
     */
>>>>>>> parent of ac80adf... added photogate code. edited elevator servo
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
        DebrisMotor.setPower(.005);
        //LeftZipline = hardwareMap.servo.get("LeftZipline");
        //RightZipline = hardwareMap.servo.get("RightZipline");
        //RightZipline.setDirection(Servo.Direction.REVERSE);
        LiftServo = hardwareMap.servo.get("LiftServo");
    }
    @Override
    public void loop() {
        //drive code
        //may need change. this is from K9TeleOp
        if(gamepad1.left_stick_button){
            float throttle = -gamepad1.left_stick_y;
            float direction = gamepad1.left_stick_x;
            float right = throttle - direction;
            float left = throttle + direction;
            // clip the right/left values so that the values never exceed +/- 1
            right = Range.clip(right, -.2, .2);
            left = Range.clip(left, -.2, .2);
            // scale the joystick value to make it easier to control
            // the robot more precisely at slower speeds.
            right = (float)scaleInput(right);
            left =  (float)scaleInput(left);
            // write the values to the motors
            motorRight.setPower(left);
            motorLeft.setPower(right);}
    }
        else if(gamepad1.right_stick_button){
            float throttle = -gamepad1.left_stick_y;
            float direction = gamepad1.left_stick_x;
            float right = throttle - direction;
            float left = throttle + direction;
            // clip the right/left values so that the values never exceed +/- 1
            right = Range.clip(right, -1, 1);
            left = Range.clip(left, -1, 1);
            // scale the joystick value to make it easier to control
            // the robot more precisely at slower speeds.
            right = (float)scaleInput(right);
            left =  (float)scaleInput(left);
            // write the values to the motors
            motorRight.setPower(left);
            motorLeft.setPower(right);}



        //lift servo code
        if(gamepad1.dpad_up){
            LiftServo.setPosition(LiftServo.getPosition()+servoIncrement);
        }else if(gamepad1.dpad_down){
            LiftServo.setPosition(LiftServo.getPosition()-servoIncrement);
        }else{
<<<<<<< HEAD
<<<<<<< HEAD
            LiftServo.setPosition(0.48);
        }*/

        if(gamepad1.dpad_up){
            if(HopperPosition != 2){
                HopperPosition++;
            }
        }
        if(gamepad1.dpad_down){
            if (HopperPosition != 0){
                HopperPosition--;
            }
=======
            LiftServo.setPosition(LiftServo.getPosition());
>>>>>>> parent of ac80adf... added photogate code. edited elevator servo
        }
        else
        {
            ElevatorStop();
=======
            LiftServo.setPosition(LiftServo.getPosition());
>>>>>>> parent of ac80adf... added photogate code. edited elevator servo
        }





        //zipline servo code
        /*if(gamepad1.x){
           LeftZipline.setPosition(.5);
        }else{
            LeftZipline.setPosition(0);
        }
        if(gamepad1.b){
            RightZipline.setPosition(.5);
        }else{
            RightZipline.setPosition(0);
        }*/





        //Bucket tilting code
        //joystick reads from -1 to 1 on each axis
        float position = gamepad1.right_stick_x;
        //changing max angle allowed from degrees to encoder units
        float EncoderMax = (maxAngle/360)*EncoderPerRotation;
        //scaling position to have the max value as the max angle
        int TargetEncoderValue = (int)(position*EncoderMax);
        //DebrisMotor.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
        DebrisMotor.setTargetPosition(TargetEncoderValue);
        telemetry.addData("target", TargetEncoderValue);
        telemetry.addData("encoder position", DebrisMotor.getCurrentPosition());
        telemetry.addData("trigger value", gamepad1.right_trigger);



        //Roller Code
        //when trigger is pressed, power is set
        if(gamepad1.right_trigger>triggerCutoff){
            RollerMotor.setPower(power);
        }else if(gamepad1.left_trigger>triggerCutoff){
            RollerMotor.setPower(-power);
        }else{
            RollerMotor.setPower(0);
        }
    }

    /*
     * Code to run when the op mode is first disabled goes here
     *
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#stop()
     */
    @Override
    public void stop() {

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
            index = -index;}
        // index cannot exceed size of array minus 1.
        if (index > 16) {
            index = 16;}
        // get value from the array.
        double dScale = 0.0;
        if (dVal < 0) {
            dScale = -scaleArray[index];
        } else {
            dScale = scaleArray[index];}
        // return scaled value.
        return dScale;
    }
}


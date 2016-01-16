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
    AnalogInput rollerPhotogate;
    AnalogInput elevatorPhotogate;
    final float EncoderPerRotation = 1680;
    final float maxAngle = 35;
    final double triggerCutoff = .2;
    final double power = .9;
    final double searchingPower = 0.1;
    float smallPower = (1/5);
    boolean RightDown = false;
    boolean LeftDown = false;
    //boolean climbersFlipped=false;

    public void RollerStop() {
        if(rollerPhotogate.getValue() >= 500)//photogate blocked?
        {
            RollerMotor.setPower(0);//stop motor
        } else if(RollerMotor.getPower() > 0) //motor turning with positive power?
        {
            RollerMotor.setPower(searchingPower); //set to low power to find flag
        } else {
            RollerMotor.setPower(-searchingPower);
        }
    }

    public void ElevatorStop() {
        LiftServo.setPosition(.5);
    }
    public void HopperRaise() {
        LiftServo.setPosition(.3);
     if(elevatorPhotogate.getValue()<26){
         ElevatorStop();
         LiftServo.setPosition(.3);
         if(elevatorPhotogate.getValue()>=24){
             ElevatorStop();
         }
     }
    }
    public void HopperLower() {
        LiftServo.setPosition(.7);
        if(elevatorPhotogate.getValue()<26){
            ElevatorStop();
            LiftServo.setPosition(.7);
            if(elevatorPhotogate.getValue()>=24){
                ElevatorStop();
            }
        }
    }

   public void flipClimbers(){
        ClimberServo.setPosition(1);
        for(int i =0; i<25; i++){
            i+=.5;
        }
        ClimberServo.setPosition(.5);
    }






    public FellowshipTele() {

    }

    @Override
    public void init() {

        motorRight = hardwareMap.dcMotor.get("motor_2");
        motorLeft = hardwareMap.dcMotor.get("motor_1");
        motorLeft.setDirection(DcMotor.Direction.REVERSE);
        RollerMotor = hardwareMap.dcMotor.get("RollerMotor");
        DebrisMotor = hardwareMap.dcMotor.get("DebrisMotor");
        DebrisMotor.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        //may need a wait
        DebrisMotor.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
        DebrisMotor.setTargetPosition(0);
        DebrisMotor.setPower(.05);
        LeftZipline = hardwareMap.servo.get("LeftZipline");
        RightZipline = hardwareMap.servo.get("RightZipline");
        RightZipline.setDirection(Servo.Direction.REVERSE);
        LiftServo = hardwareMap.servo.get("LiftServo");
        LeftZipline.setPosition(0.5);
        RightZipline.setPosition(0.5);
        ClimberServo = hardwareMap.servo.get("ClimberServo");
        ClimberServo.setPosition(0.5);
        rollerPhotogate = hardwareMap.analogInput.get("rollerPhotogate");
        elevatorPhotogate = hardwareMap.analogInput.get("elevatorPhotogate");
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
            right = smallPower*(float)scaleInput(right);
            left = smallPower*(float)scaleInput(left);
            motorRight.setPower(left);
            motorLeft.setPower(right);

        }else {
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
        /* if(gamepad1.dpad_up){
            LiftServo.setPosition(0);
        } else if(gamepad1.dpad_down){
        LiftServo.setPosition(1);
        }else{
        LiftServo.setPosition(0.5);
         */
            if (gamepad1.dpad_up) {

                    HopperRaise();

            }
            else if (gamepad1.dpad_down) {

                    HopperLower();


            }


        //zipline servo code
        if (gamepad1.x)
            if (!LeftDown) {
                LeftZipline.setPosition(1);
                LeftDown = true;
            } else {
                LeftZipline.setPosition(0.5);
                LeftDown = false;
            }
        if (gamepad1.b)
            if (!RightDown) {
                RightZipline.setPosition(1);
                RightDown = true;
            } else {
                RightZipline.setPosition(.5);
                RightDown = false;
            }


        //Bucket Tilting code
        //joystick reads from -1 to 1 on each axis
        float position = gamepad1.right_stick_x;
        //changing max angle allowed from degrees to encoder units
        float EncoderMax = (maxAngle / 360) * EncoderPerRotation;
        //scaling position to have the max value as the max angle
        int TargetEncoderValue = (int)(position * EncoderMax);
        //DebrisMotor.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
        DebrisMotor.setTargetPosition(TargetEncoderValue);


        //Roller Code
        //when trigger is pressed, power is set
        if (gamepad1.right_trigger > triggerCutoff) {
            RollerMotor.setPower(power);
        } else if (gamepad1.left_trigger > triggerCutoff) {
            RollerMotor.setPower(-power);
        } else {
            RollerStop();
        }

        //Climber code
        /*if(climbersFlipped&&gamepad1.left_bumper)
        {
            flipClimbers();
            climbersFlipped = true;
        }*/





        //telemetry section
telemetry.addData("elevatorGate", elevatorPhotogate.getValue());

    }
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
            index = -index;
        }

        // index cannot exceed size of array qqQSQQqsQSqsQSsminus 1.
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
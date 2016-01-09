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
    Servo ZiplineLeft;
    Servo ZiplineRight;
    final float EncoderPerRotation = 1680;
    final float maxAngle = 30;
    /**
     * Constructor
     */
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
        DebrisMotor.setPower(.1);
        ZiplineLeft.setPosition(0.5);
        ZiplineRight.setPosition(0.5);
    }
    @Override
    public void loop() {

        float throttle = -gamepad1.left_stick_y;
        float direction = gamepad1.left_stick_x;
        float right = throttle - direction;
        float left = throttle + direction;
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

        if(gamepad1.right_trigger>5){
            RollerMotor.setPower(1);
        }else{
            RollerMotor.setPower(0);

        }

        if(gamepad1.x=true)
        {
            ZiplineLeft.setPosition(1);
        }
        if(gamepad1.b=true)
        {
            ZiplineRight.setPosition(1);
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


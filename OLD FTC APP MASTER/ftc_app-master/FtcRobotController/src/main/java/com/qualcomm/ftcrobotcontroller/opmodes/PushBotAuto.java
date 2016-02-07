package com.qualcomm.ftcrobotcontroller.opmodes;

//------------------------------------------------------------------------------
//
// PushBotAuto
//

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Provide a basic autonomous operational mode that uses the left and right
 * drive motors and associated encoders implemented using a state machine for
 * the Push Bot.
 *
 * @author SSI Robotics
 * @version 2015-08-01-06-01
 */
public class PushBotAuto extends OpMode

{
  DcMotor DebrisMotor;

    public PushBotAuto ()

    {}
@Override public void init(){
    DebrisMotor = hardwareMap.dcMotor.get("DebrisMotor");
}

    @Override public void start ()

    {}


    @Override public void loop ()

    {

        DebrisMotor.setPower(1);
    }


}

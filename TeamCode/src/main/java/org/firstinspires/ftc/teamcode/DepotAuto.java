package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name = "DepotAuto", group = "Autonomous")
public class DepotAuto extends StartAuto {

    @Override
    //go to crater from depot side lander
    public void runOpMode() throws InterruptedException {
        super.runOpMode();
        goToCrater();
    }

    private void goToCrater(){
        //go straight to crater
        turnRelative(90, turnSpeed);
        move(30, moveSpeed);
    }
}

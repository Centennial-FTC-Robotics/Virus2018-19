package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

//@Autonomous(name = "CraterAuto2", group = "Autonomous")

public class CraterAuto2 extends StartAuto {

    @Override
    //go to crater from crater side lander
    public void runOpMode() throws InterruptedException {
        super.runOpMode();
        goToCrater();
    }

    private void goToCrater(){
        //go straight to crater
        turnRelative(-90, turnSpeed);
        move(9, moveSpeed);
    }
}
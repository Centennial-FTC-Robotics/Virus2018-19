package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "DepotAuto2", group = "Autonomous")
public class DepotAuto2 extends EndAuto2{

    @Override
    //go to depot, drop off marker, return to lander, knock gold, extend slides into crater
    public void runOpMode() throws InterruptedException {
        holdHang();
        dehang();
        initializeIMU();
        goToWall();
        //go to depot
        turnRelative(-90, turnSpeed);
        move(48, moveSpeed);
        //drop marker
        turnRelative(90, turnSpeed);
        dropMarker();
        turnRelative(90, turnSpeed);
        //go back to lander
        move(48, moveSpeed);
        turnRelative(90, turnSpeed);
        move(48, moveSpeed);
        turnAbsolute(0, turnSpeed);

        knockGold();

        //slides into crater
        super.runOpMode();
    }

    private void goToWall(){
        //realign self
        turnAbsolute(0, turnSpeed);
        move(-5, moveSpeed);
        //go to wall (REPLACE WITH PROX SENSOR CODE)
        intakePivot(false, true);
        turnAbsolute(45, turnSpeed);
        move(48, moveSpeed);
    }
}

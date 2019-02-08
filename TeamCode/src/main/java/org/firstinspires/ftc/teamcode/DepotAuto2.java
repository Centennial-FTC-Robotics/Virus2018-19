package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "DepotAuto2", group = "Autonomous")
public class DepotAuto2 extends VirusMethods{

    @Override
    //go to depot, drop off marker, return to lander, knock gold, extend slides into crater
    public void runOpMode() throws InterruptedException {
        super.runOpMode();
        holdHang();
        dehang();
        initializeIMU();
        goToWall();
        //go to depot
        turnRelative(-90, turnSpeed);
        move(72, moveSpeed);
        //drop marker
        turnRelative(90, turnSpeed);
        dropMarker();
        turnRelative(135, turnSpeed);

        knockGold();
        slides(0);
        intakePivot(true,true);
        turnAbsolute(135,turnSpeed);
        move(72,moveSpeed);
        //do we have clearance when intake up

        //slides into crater
        hinge(30);
        slides(2000);
        hinge(0);
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

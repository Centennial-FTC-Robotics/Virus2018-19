package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "DepotAuto", group = "Autonomous")
public class DepotAuto extends VirusMethods{

    @Override
    //go to depot, drop off marker, return to lander, knock gold, extend slides into crater
    public void runOpMode() throws InterruptedException {
        super.runOpMode();
        holdHang();
        dehang(8);

        //realign self
        //go to wall
        intakePivot(false, true);
        turnAbsolute(45, turnSpeed);
        move(48, moveSpeed);

        //go to depot
        turnAbsolute(-45, turnSpeed);
        move(60, moveSpeed);

        //drop marker (NEED TO EDIT)
        turnAbsolute(45, turnSpeed);
        dropMarker();
        turnAbsolute(180, turnSpeed);

        knockGold();

        slides(0);
        intakePivot(true,true);
        turnAbsolute(135,turnSpeed);
        move(72,moveSpeed);
        //do we have clearance when intake up????

        //slides into crater
        hinge(30);
        slides(2000);
        hinge(0);
    }
}

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "CraterAuto2", group = "Autonomous")
public class CraterAuto2 extends VirusMethods {
    @Override
    //go to depot, drop off marker, return to lander, knock gold, extend slides into crater
    public void runOpMode() throws InterruptedException {
        super.runOpMode();
        holdHang();
        dehang(13f);
        initializeIMU();
        //turn towards wall
//        turnAbsolute(90, turnSpeed);
        turnRelative(90, turnSpeed);
        //go to wall
        move(47, moveSpeed);
        //turn toward depot
//        turnRelative(45, turnSpeed);
        turnAbsolute(135,turnSpeed);
        //go to depot
        move(36, moveSpeed);
        //drop marker
//        turnRelative(90, turnSpeed);
        turnAbsolute(-135,turnSpeed);
        dropMarker();
//        turnRelative(90, turnSpeed);
        turnAbsolute(-45, turnSpeed);
        //go back to lander
        move(34, moveSpeed);
//        turnRelative(-45, turnSpeed);
        turnAbsolute(-90,turnSpeed);
        move(50, moveSpeed);
        turnAbsolute(0, turnSpeed);

        knockGold();

        //slides into crater
        autoExtendIntoCrater();
    }

}

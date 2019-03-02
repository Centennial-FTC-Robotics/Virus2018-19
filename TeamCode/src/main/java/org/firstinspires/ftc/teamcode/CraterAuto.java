package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "CraterAuto", group = "Autonomous")
public class CraterAuto extends VirusMethods {
    @Override
    //go to depot, drop off marker, return to lander, knock gold, extend slides into crater
    public void runOpMode() throws InterruptedException {
        super.runOpMode();
        holdHang();
        dehang(14f);
        zeroAngle();

        //turn towards wall
        turnAbsolute(90, turnSpeed);

        //go to wall
        move(50, moveSpeed);

        //turn toward depot
        turnAbsolute(135,turnSpeed);

        //go to depot
        move(36, moveSpeed);

        //drop marker
        turnAbsolute(-165,turnSpeed);
        dropMarker();
        turnAbsolute(-45, turnSpeed);

        //go back to lander
        move(36, moveSpeed);
        turnAbsolute(-90,turnSpeed);
        move(50, moveSpeed);
        turnAbsolute(0, turnSpeed);

        knockGold();

        //slides into crater
        autoExtendIntoCrater();
    }

}

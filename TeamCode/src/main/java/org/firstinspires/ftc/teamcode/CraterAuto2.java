package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "CraterAuto2", group = "Autonomous")
public class CraterAuto2 extends EndAuto2 {
    @Override
    //go to depot, drop off marker, return to lander, knock gold, extend slides into crater
    public void runOpMode() throws InterruptedException {
        holdHang();
        dehang();
        initializeIMU();
        //turn towards wall
        turnAbsolute(90, turnSpeed);
        //go to wall
        move(50, moveSpeed);
        //turn toward depot
        turnRelative(45, turnSpeed);
        //go to depot
        move(36, moveSpeed);
        //drop marker
        turnRelative(90, turnSpeed);
        dropMarker();
        turnRelative(90, turnSpeed);
        //go back to lander
        move(36, moveSpeed);
        turnRelative(-45, turnSpeed);
        move(50, moveSpeed);
        turnAbsolute(90, turnSpeed);

        knockGold();

        //slides into crater
        super.runOpMode();
    }

}

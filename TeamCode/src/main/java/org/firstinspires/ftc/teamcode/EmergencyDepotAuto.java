package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by keert on 1/25/2019.
 */

//@Autonomous(name="EmergencyDepotAuto", group="Autonomous")
//just dehangs and goes to crater
public class EmergencyDepotAuto extends VirusMethods {
    public void runOpMode() throws InterruptedException {
        super.runOpMode();

//        while(!isStarted()) {
//            intakePivot(false, false);
//            hinge(0, false);
//            slides(0, false);
//        }
        initializeIMU();
        waitForStart();
        double turnSpeed = 0.5;
        ElapsedTime timer = new ElapsedTime();
        //dehang();
        move(5, .5f);
        turnAbsolute(45, turnSpeed);
        move(40, (float) 0.5);
        turnRelative(90, 0.5f);
        move(30,0.5f);
    }
}

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by keert on 12/8/2018.
 */
@Autonomous(name="ServoTest", group="Autonomous")
public class ServoTest extends VirusMethods {
    public void runOpMode()throws InterruptedException {
        super.runOpMode();
        waitForStart();
        move(24, (float) 1);
        telemetry.addData("Point", "1");
        telemetry.update();
//        waitTime(500);
//
//        telemetry.addData("Point", "2");
//        telemetry.update();
    }
}

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by keert on 12/8/2018.
 */
//@Autonomous(name="ServoTest", group="Autonomous")
public class ServoTest extends VirusMethods {
    String goldPos = "bad";
    public void runOpMode()throws InterruptedException {
        super.runOpMode();
        initVision();
        waitForStart();
        while (opModeIsActive()){
            goldPos = autoFindGold();
            telemetry.addData("Gold Position" , goldPos);
            telemetry.update();
            waitTime(500);
        }
    }
}

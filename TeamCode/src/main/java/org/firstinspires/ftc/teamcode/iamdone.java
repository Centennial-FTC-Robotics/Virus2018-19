package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name="iamdone", group="Autonomous")

/**
 * Created by keert on 1/25/2019.
 */

public class iamdone extends VirusMethods{
    @Override
    public void runOpMode() throws InterruptedException {
        super.runOpMode();
        initializeIMU();
        initVision();
        while (!isStarted()) {
            intakePivot(true, false);
            hinge(0, false);
            slides(0, false);
        }
        waitForStart();

    }
}

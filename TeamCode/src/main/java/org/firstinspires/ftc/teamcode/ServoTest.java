package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by keert on 12/8/2018.
 */
@Autonomous(name="ServoTest", group="Autonomous")
public class ServoTest extends VirusMethods {
    public void runOpMode()throws InterruptedException {
        super.runOpMode();
        waitForStart();
        while(opModeIsActive()){

            telemetry.addData("Red",colorSensor1.red());
            telemetry.addData("Green",colorSensor1.green());
            telemetry.addData("Blue",colorSensor1.blue());
            telemetry.update();
        }
    }
}

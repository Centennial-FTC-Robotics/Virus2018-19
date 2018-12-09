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
            if (gamepad2.right_bumper){
                marker.setPosition(marker.getPosition()+0.05);
                while (gamepad2.right_bumper);
            }
            if (gamepad2.left_bumper){
                marker.setPosition(marker.getPosition()-0.05);
                while (gamepad2.left_bumper);
            }
            if (gamepad2.a){
                dropMarker();
            }
            telemetry.addData("marker",marker.getPosition());
            telemetry.update();
        }
    }
}

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="CraterBackup", group="Autonomous")
//Qualifier 1 bad
//goes straight forward
public class CraterBackup extends VirusMethods {
    public void runOpMode() throws InterruptedException {
        super.runOpMode();
        runDriveMotors(-0.7f,-0.7f);
        waitTime(1500);
        runDriveMotors(0,0);
    }
}

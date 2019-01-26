package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="DepotBackup", group="Autonomous")
//WE SHOULD HOPE TO NEVER USE THIS
public class DepotBackup extends VirusMethods {
    public void runOpMode() throws InterruptedException{
        super.runOpMode();
        runDriveMotors(-0.7f,-0.7f);
        waitTime(500);
        runDriveMotors(0.7f,-0.7f);
        waitTime(500);
        runDriveMotors(-0.7f,-0.7f);
        waitTime(1500);
        runDriveMotors(0.7f,-0.7f);
        waitTime(250);
        runDriveMotors(-0.7f,-0.7f);
        intakePivot(true, false);
        waitTime(1000);
        runDriveMotors(0,0);
    }
}

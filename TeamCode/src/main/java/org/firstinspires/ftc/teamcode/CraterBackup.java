package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="CraterBackup", group="Autonomous")
//Qualifier 1 bad
//goes straight forward
public class CraterBackup extends VirusMethods {
    public void runOpMode() throws InterruptedException {
        super.runOpMode();
        holdHang();
        dehang(33);
        hinge(45);
        slides(5000);
        hinge(0);
    }
}

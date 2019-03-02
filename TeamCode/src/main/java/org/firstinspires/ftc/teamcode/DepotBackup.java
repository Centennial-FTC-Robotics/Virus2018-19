package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="DepotBackup", group="Autonomous")
//WE SHOULD HOPE TO NEVER USE THIS
//goes to crater
public class DepotBackup extends VirusMethods {
    public void runOpMode() throws InterruptedException{
        super.runOpMode();
        holdHang();
        dehang(33);
    }
}

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name="iamdone", group="Autonomous")

/**
 * Created by keerts on 1/25/2019.
 */

public class iamdone extends VirusMethods{
    @Override
    public void runOpMode() throws InterruptedException {
        super.runOpMode();
        holdHang();
        dehang(12);
        zeroAngle();
        turnAbsolute(45, 0.4);
    }
}

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name="CraterAuto", group="Autonomous")

public class CraterAuto extends VirusMethods {

    @Override
    public void runOpMode() throws InterruptedException {
        super.runOpMode();
        //initVision();
//        slideLock.setPosition(0.5);

        waitForStart();

        ElapsedTime timer = new ElapsedTime();

        int scanAngle = 12; //positive angle is left turn
        int knockAngle = 26;
        double turnSpeed = 0.3;
        float moveSpeed = 0.4f;
        String goldPos = "bad";

        dehang();
        initializeIMU();
        turnRelative(10,0.5);

        //figure out gold position
        while (opModeIsActive() && timer.seconds() < 5 && goldPos.equals("bad")) {
            telemetry.addData("Timer" , timer.seconds());
            goldPos = autoFindGold();
            telemetry.addData("Gold Position" , goldPos);
            telemetry.update();
        }
        closeTfod();
        hinge(0);
        //turn and move to hit (if no detect just move on)
        //extends slides 2.45 ft to hit mineral
        if (!goldPos.equals("bad")){
            if (goldPos.equals("Left")) {
                turnAbsolute(knockAngle, turnSpeed);
            }
            else if (goldPos.equals("Center")) {
                turnAbsolute(0, turnSpeed);
            }
            else if (goldPos.equals("Right")) {
                turnAbsolute(-knockAngle, turnSpeed);
            }
            slides(5960);
            waitTime(500);
            slides(0);
        }
        //go to wall (REPLACE WITH PROX SENSOR CODE)
        turnAbsolute(45, turnSpeed);
        move(40, (float) 0.5);

        //go to depot, drop marker (prox sensor too)
        turnRelative(90, turnSpeed);
        move(54, (float) 0.5);
        turnRelative(90,0.5);
        dropMarker();

        //turn around, go to crater
        turnRelative(90,0.5);
        move(72, (float) 0.5);
        intoCrater();
    }
}
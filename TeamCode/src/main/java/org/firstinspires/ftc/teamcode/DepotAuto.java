package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name="DepotAuto", group="Autonomous")
public class DepotAuto extends VirusMethods {
    @Override
    public void runOpMode() throws InterruptedException {
        super.runOpMode();
        //initVision();
//        slideLock.setPosition(0.5);

        waitForStart();

        ElapsedTime timer = new ElapsedTime();
        String action = "initializing";
        telemetry.addData("current task", action);

        int scanAngle = 12; //positive angle is left turn
        int knockAngle = 26;
        double turnSpeed = 0.3;
        float moveSpeed = 0.4f;
        String goldPos = "bad";
        action = "dehanging";
        telemetry.update();
        dehang();
        action = "initializing IMU";
        telemetry.update();
        initializeIMU();
        action = "turning left 10 degrees";
        telemetry.update();
        turnRelative(10,0.5);

        //figure out gold position
        action = "finding gold";
        telemetry.update();
        while (opModeIsActive() && timer.seconds() < 5 && goldPos.equals("bad")) {
            telemetry.addData("Timer" , timer.seconds());
            goldPos = autoFindGold();
            telemetry.addData("Gold Position" , goldPos);
            telemetry.update();
        }
        closeTfod();
        action = "raising hinge to 0 degrees, finding gold";
        telemetry.update();
        hinge(0);
        //turn and move to hit (if no detect just move on)
        //extends slides 2.45 ft to hit mineral
        if (!goldPos.equals("bad")){
            action = "found gold: " +goldPos;
            telemetry.update();
            if (goldPos.equals("Left")) {
                action = "turning absolute left " + knockAngle +" degrees";
                telemetry.update();
                turnAbsolute(knockAngle, turnSpeed);
            }
            else if (goldPos.equals("Center")) {
                action = "turning absolute left 0 degrees";
                telemetry.update();
                turnAbsolute(0, turnSpeed);
            }
            else if (goldPos.equals("Right")) {
                action = "turning absolute right " + knockAngle +" degrees";
                telemetry.update();
                turnAbsolute(-knockAngle, turnSpeed);
            }
            action = "extending slides to knock gold";
            telemetry.update();
            slides(5960);
            waitTime(500);
            action = "retracting slides";
            telemetry.update();
            slides(0);


        }
        //go to wall
        action = "turning absolute left 45 degrees";
        telemetry.update();
        turnAbsolute(45, turnSpeed);
        action = "going forward 40 inches";
        telemetry.update();
        move(40, (float) 0.5);

        //turn to depot, drop marker, turn to crater(prox sensor too)
        action = "turning relative right 90 degrees";
        telemetry.update();
        turnRelative(-90, turnSpeed);
        action = "going forward 48 inches";
        telemetry.update();
        move(48, 0.5f); //two blocks
        action = "turning relative left 90 degrees";
        telemetry.update();
        turnRelative(90, turnSpeed);
        action = "dropping marker";
        telemetry.update();
        dropMarker();
        action = "turning relative left 90 degrees";
        telemetry.update();
        turnRelative(90, turnSpeed);

        //go to crater
        action = "going forward 72 inches";
        telemetry.update();
        move(72, (float) 0.5);
        action = "going into crater";
        telemetry.update();
        intoCrater();
        action = "finish autonomous";
        telemetry.update();
    }
}

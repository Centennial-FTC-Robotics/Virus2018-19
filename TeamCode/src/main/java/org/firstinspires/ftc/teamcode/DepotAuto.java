package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name="DepotAuto", group="Autonomous")
public class DepotAuto extends VirusMethods {

    private String action = "";
    private boolean needTelemetry = true;

    @Override
    public void runOpMode() throws InterruptedException {
        super.runOpMode();
        initVision();
//        telemetry.addData("task", "before while");
//        while(!isStarted()) {
//            intakePivot(false, false);
//            hinge(0, false);
//            slides(0, false);
//        }
//        telemetry.addData("task", "after while");
//        slideLock.setPosition(0.5);

        waitForStart();

        ElapsedTime timer = new ElapsedTime();
        showTelemetry("initializing");

        int scanAngle = 12; //positive angle is left turn
        int knockAngle = 26;
        double turnSpeed = 0.3;
        float moveSpeed = 0.4f;
        String goldPos = "bad";
//        showTelemetry("dehanging");
//        dehang();
        showTelemetry("initializing imu");
        initializeIMU();
        slides(100, false);
        hinge(90, false);
        showTelemetry("turning left 10 degrees");
        turnRelative(10,0.5);

        //figure out gold position
        showTelemetry("finding gold");
        timer.reset();
        while (opModeIsActive() && timer.seconds() < 5 && goldPos.equals("bad")) {
            telemetry.addData("Timer" , timer.seconds());
            goldPos = autoFindGold();
            telemetry.addData("Gold Position" , goldPos);
            telemetry.update();
        }
        closeTfod();
        showTelemetry("raising hinge to 0 degrees, finding gold");
        slides(500, false);
        hinge(0, false);
        //turn and move to hit (if no detect just move on)
        //extends slides 2.45 ft to hit mineral
        if (!goldPos.equals("bad")){
            action = "found gold: " +goldPos;
            telemetry.update();
            if (goldPos.equals("Left")) {
                showTelemetry("turning absolute left " + knockAngle +" degrees");
                telemetry.update();
                turnAbsolute(knockAngle, turnSpeed);
                move(24, 0.5f);
                waitTime(500);
                move(-24, 0.5f);
            }
            else if (goldPos.equals("Center")) {
                showTelemetry("turning absolute 0 degrees");
                turnAbsolute(0, turnSpeed);
                move(18, 0.5f);
                waitTime(500);
                move(-18, 0.5f);
            }
            else if (goldPos.equals("Right")) {
                showTelemetry("turning absolute right " + knockAngle +" degrees");
                turnAbsolute(-knockAngle, turnSpeed);
                move(24, 0.5f);
                waitTime(500);
                move(-24, 0.5f);
            }
            //showTelemetry("extending slide to knock gold");
            //slides(5960, false);
            showTelemetry("retracting slides");
            //slides(0, false);


        }
        turnAbsolute(0, turnSpeed);
        move(-3, 0.5f);
        //go to wall
        showTelemetry("turning absolute left 45 degrees");
        turnAbsolute(45, turnSpeed);
        showTelemetry("going forward 40 inches");
        move(40, (float) 0.5);

//        //turn to depot, drop marker, turn to crater(prox sensor too)
//        showTelemetry("turning relative right 90 degrees");
//        turnRelative(-90, turnSpeed);
//        showTelemetry("going forward 48 inches");
//        move(48, 0.5f); //two blocks
//        showTelemetry("turning relative left 90 degrees");
//        turnRelative(90, turnSpeed);
//        showTelemetry("dropping marker");
//        telemetry.update();
//        dropMarker();
//        showTelemetry("turning relative left 90 degrees");
//        turnRelative(90, turnSpeed);
//
//        //go to crater
//        showTelemetry("going forward 72 inches");
//        move(63, (float) 0.5);
//        showTelemetry("going into crater");
//        telemetry.update();
//        intoCrater();
//        showTelemetry("finish autonomous");
//        telemetry.update();

        //go straight to crater
        turnRelative(90, turnSpeed);
        move(15,0.5f);
    }
    private void showTelemetry(String action){
        this.action = action;
        telemetry.update();
    }
}

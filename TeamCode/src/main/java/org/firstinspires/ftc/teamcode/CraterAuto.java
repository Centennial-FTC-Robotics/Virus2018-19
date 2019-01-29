package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name="CraterAuto", group="Autonomous")

public class CraterAuto extends VirusMethods {

    private String action = "";
    private boolean needTelemetry = true;

    @Override
    public void runOpMode() throws InterruptedException {
        super.runOpMode();
        initVision();
//        telemetry.addData("is started", isStarted());
//        telemetry.update();
//        while(!isStarted()) {
//            telemetry.addData("while not started", "");
//            telemetry.update();
//            intakePivot(false, false);
//            slides(100, false);
//            hinge(45, false);
//
//        }
//        telemetry.addData("task", "after while");
//        slideLock.setPosition(0.5);
        initializeIMU();
        waitForStart();

        int scanAngle = 12; //positive angle is left turn
        int knockAngle = 30;
        double turnSpeed = 0.5;
        float moveSpeed = 0.4f;
        String goldPos = "";
//        showTelemetry("dehanging");
//        dehang();
        move(5, .5f);
        slides(100, false);
        hinge(90, false);
        //showTelemetry("turning left 10 degrees");
        turnRelative(10,0.5);

        //figure out gold position
        //showTelemetry("finding gold");
        ElapsedTime timer = new ElapsedTime();
        telemetry.addData("Timer", timer.seconds());
        timer.reset();
        while (opModeIsActive() && timer.seconds() < 5 && goldPos.equals("")) {
            telemetry.addData("Timer" , timer.seconds());
            goldPos = autoFindGold();
            telemetry.addData("Gold Position" , goldPos);
            telemetry.update();
        }
        closeTfod();
        //showTelemetry("raising hinge to 0 degrees, finding gold");
        intakePivot(true, false);
        slides(500, false);
        hinge(0, false);
        //turn and move to hit (if no detect just move on)
        //extends slides 2.45 ft to hit mineral
        if (!goldPos.equals("")){
            //showTelemetry("found gold: " + goldPos);
            telemetry.addData("Turning to", goldPos);
            if (goldPos.equals("Left")) {
                //showTelemetry("turning absolute left " + knockAngle +" degrees");
                telemetry.update();
                turnAbsolute(knockAngle, turnSpeed);
                intakePivot(false, false);
                move(24, 0.5f);
                waitTime(500);
                move(-24, 0.5f);
            }
            else if (goldPos.equals("Center")) {
                //showTelemetry("turning absolute 0 degrees");
                turnAbsolute(0, turnSpeed);
                intakePivot(false, false);
                move(18, 0.5f);
                waitTime(500);
                move(-18, 0.5f);
            }
            else if (goldPos.equals("Right")) {
                //showTelemetry("turning absolute right " + knockAngle +" degrees");
                turnAbsolute(-knockAngle, turnSpeed);
                intakePivot(false, false);
                move(24, 0.5f);
                waitTime(500);
                move(-24, 0.5f);
            }
//            showTelemetry("extending slide to knock gold");
//            slides(5960, false);
//            waitTime(500);
//            showTelemetry("retracting slides");
//            slides(0, false);
        }
        if(goldPos.equals("")){
            telemetry.addData("Did not find gold", "nicht gut");
            intakePivot(false, false);
            move(24, .5f);
            move(-24, 5.f);
        }
            //realign self
            turnAbsolute(0, turnSpeed);
            move(-3, 0.5f);
            //go to wall (REPLACE WITH PROX SENSOR CODE)
            intakePivot(false, false);
            //showTelemetry("turning absolute left 45 degrees");
            turnAbsolute(45, turnSpeed);
            //showTelemetry("going forward 40 inches");
            move(46, (float) 0.5);

//        //go to depot, drop marker (prox sensor too)
//        showTelemetry("turning relative left 90 degrees");
//        turnRelative(90, turnSpeed);
//        showTelemetry("going forward 54 inches");
//        move(54, (float) 0.5);
//        action = "turning relative left 90 degrees";
//        telemetry.update();
//        turnRelative(90,0.5);
//        showTelemetry("dropping marker");
//        dropMarker();
//
//        //turn around, go to crater
//        showTelemetry("turning relative left 90 degrees");
//        turnRelative(90,0.5);
//        showTelemetry("going forward 72 inches");
//        move(63, (float) 0.5);
//        showTelemetry("going into crater");
//        intoCrater();
//        showTelemetry("finish autonomous");

            //go straight to crater
            turnRelative(-90, turnSpeed);
            move(9, 0.5f);

    }
//    private void showTelemetry(String action){
//        this.action = action;
//        telemetry.update();
//    }
}
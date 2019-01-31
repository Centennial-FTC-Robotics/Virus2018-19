package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name = "CraterAuto", group = "Autonomous")

public class CraterAuto extends VirusMethods {

    private String action = "";
    private boolean needTelemetry = true;

    @Override
    public void runOpMode() throws InterruptedException {
        super.runOpMode();
        slides(0);
        hinge(0);
        intakePivot(true);
        initVision();
        initializeIMU();
        waitForStart();

        int scanAngle = 12; //positive angle is left turn
        int knockAngle = 30;
        double turnSpeed = 0.6;
        float moveSpeed = 0.6f;
        String goldPos = "";
//        showTelemetry("dehanging");
        dehang();
        move(5, moveSpeed);
        slides(100);
        hinge(90);
        //showTelemetry("turning left 10 degrees");
        turnRelative(13, turnSpeed);

        //figure out gold position
        //showTelemetry("finding gold");
        ElapsedTime timer = new ElapsedTime();
        telemetry.addData("Timer", timer.seconds());
        timer.reset();
        while (opModeIsActive() && timer.seconds() < 3 && goldPos.equals("")) {
            telemetry.addData("Timer", timer.seconds());
            goldPos = autoFindGold();
            telemetry.addData("Gold Position", goldPos);
            telemetry.update();
        }
        closeTfod();
        //showTelemetry("raising hinge to 0 degrees, finding gold");
        intakePivot(true);
        slides(500);
        hinge(0);
        //turn and move to hit (if no detect just move on)
        //extends slides 2.45 ft to hit mineral
        int sideDist = 24;
        int centerDist = 20;
        if (!goldPos.equals("")) {
            //showTelemetry("found gold: " + goldPos);
            telemetry.addData("Turning to", goldPos);
            if (goldPos.equals("Left")) {
                //showTelemetry("turning absolute left " + knockAngle +" degrees");
                telemetry.update();
                turnAbsolute(knockAngle, turnSpeed);
                intakePivot(false);
                move(sideDist, moveSpeed);
                move(-sideDist, moveSpeed);
            } else if (goldPos.equals("Center")) {
                //showTelemetry("turning absolute 0 degrees");
                turnAbsolute(0, turnSpeed);
                intakePivot(false);
                move(centerDist, moveSpeed);
                move(-centerDist, moveSpeed);
            } else if (goldPos.equals("Right")) {
                //showTelemetry("turning absolute right " + knockAngle +" degrees");
                turnAbsolute(-knockAngle, turnSpeed);
                intakePivot(false);
                move(sideDist, moveSpeed);
                move(-sideDist, moveSpeed);
            }
//            showTelemetry("extending slide to knock gold");
//            slides(5960, false);
//            waitTime(500);
//            showTelemetry("retracting slides");
//            slides(0, false);
        }
        //deafult, ram the right mineral
        if (goldPos.equals("")) {
            telemetry.addData("Did not find gold", "nicht gut");
            turnAbsolute(-knockAngle, turnSpeed);
            intakePivot(false);
            move(sideDist, moveSpeed);
            move(-sideDist, moveSpeed);
        }
        //realign self
        turnAbsolute(0, turnSpeed);
        move(-5, moveSpeed);
        //go to wall (REPLACE WITH PROX SENSOR CODE)
        intakePivot(false);
        //showTelemetry("turning absolute left 45 degrees");
        turnAbsolute(45, turnSpeed);
        //showTelemetry("going forward 40 inches");
        move(48, moveSpeed);

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
        move(9, moveSpeed);

    }
//    private void showTelemetry(String action){
//        this.action = action;
//        telemetry.update();
//    }
}
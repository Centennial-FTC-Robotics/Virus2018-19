package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name = "DepotAuto", group = "Autonomous")
public class DepotAuto extends VirusMethods {

    private String action = "";
    private boolean needTelemetry = true;

    @Override
    public void runOpMode() throws InterruptedException {
        super.runOpMode();
        slides(0);
        hinge(0);
        intakePivot(false, false);
        initVision();
        waitForStart();
        
        int knockAngle = 30;
        double turnSpeed = 0.6;
        float moveSpeed = 0.6f;
        boolean haveGold = false;
        int sideDist = 24;
        int centerDist = 20;
        double lookTime = 1.5;

        dehang();
        initializeIMU();

        //figure out gold position
        //look at left mineral
        turnAbsolute(knockAngle, turnSpeed);
        ElapsedTime timer = new ElapsedTime();
        telemetry.addData("Timer", timer.seconds());
        timer.reset();
        while (opModeIsActive() && timer.seconds() < lookTime && !haveGold) {
            telemetry.addData("Timer", timer.seconds());
            haveGold = isGold();
            telemetry.addData("Gold?", haveGold);
            telemetry.update();
        }
        if (haveGold) {
            //knock left mineral
            hinge(0);
            intakePivot(false,true);
            move(sideDist, moveSpeed);
            move(-sideDist, moveSpeed);
        } else {
            //look at center mineral
            turnAbsolute(0, turnSpeed);
            timer.reset();
            while (opModeIsActive() && timer.seconds() < lookTime && !haveGold) {
                telemetry.addData("Timer", timer.seconds());
                haveGold = isGold();
                telemetry.addData("Gold?", haveGold);
                telemetry.update();
            }
            if (haveGold) {
                //knock center mineral
                hinge(0);
                intakePivot(false,true);
                move(centerDist, moveSpeed);
                move(-centerDist, moveSpeed);
            } else {
                //turn to right mineral and knock
                turnAbsolute(-knockAngle, turnSpeed);
                hinge(0);
                intakePivot(false,true);
                move(sideDist, moveSpeed);
                move(-sideDist, moveSpeed);
            }
        }
        closeTfod();
        //realign self
        turnAbsolute(0, turnSpeed);
        move(-5, moveSpeed);
        //go to wall (REPLACE WITH PROX SENSOR CODE)
        intakePivot(false,true);
        //showTelemetry("turning absolute left 45 degrees");
        turnAbsolute(45, turnSpeed);
        //showTelemetry("going forward 40 inches");
        move(48, moveSpeed);

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
        move(30, moveSpeed);

    }
//    private void showTelemetry(String action){
//        this.action = action;
//        telemetry.update();
//    }
}

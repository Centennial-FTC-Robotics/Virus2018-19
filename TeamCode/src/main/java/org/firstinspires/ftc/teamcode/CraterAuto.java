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
        if(needTelemetry){
            action = "starting crater auto opmode";
            telemetry.addData("task", action);
        }
        super.runOpMode();
        //initVision();
//        slideLock.setPosition(0.5);

        waitForStart();

        ElapsedTime timer = new ElapsedTime();
        showTelemetry("initializing");

        int scanAngle = 12; //positive angle is left turn
        int knockAngle = 26;
        double turnSpeed = 0.3;
        float moveSpeed = 0.4f;
        String goldPos = "bad";
        showTelemetry("dehanging");
        dehang();
        showTelemetry("initializing imu");
        initializeIMU();
        showTelemetry("turning left 10 degrees");
        turnRelative(10,0.5);

        //figure out gold position
        showTelemetry("finding gold");
        while (opModeIsActive() && timer.seconds() < 5 && goldPos.equals("bad")) {
            telemetry.addData("Timer" , timer.seconds());
            goldPos = autoFindGold();
            telemetry.addData("Gold Position" , goldPos);
            telemetry.update();
        }
        closeTfod();
        showTelemetry("raising hinge to 0 degrees, finding gold");
        hinge(0);
        //turn and move to hit (if no detect just move on)
        //extends slides 2.45 ft to hit mineral
        if (!goldPos.equals("bad")){
            showTelemetry("found gold: " + goldPos);
            if (goldPos.equals("Left")) {
                showTelemetry("turning absolute left " + knockAngle +" degrees");
                turnAbsolute(knockAngle, turnSpeed);
            }
            else if (goldPos.equals("Center")) {
                showTelemetry("turning absolute 0 degrees");
                turnAbsolute(0, turnSpeed);
            }
            else if (goldPos.equals("Right")) {
                showTelemetry("turning absolute right " + knockAngle +" degrees");
                turnAbsolute(-knockAngle, turnSpeed);
            }
            showTelemetry("extending slide to knock gold");
            slides(5960);
            waitTime(500);
            showTelemetry("retracting slides");
            slides(0);
        }
        //go to wall (REPLACE WITH PROX SENSOR CODE)
        showTelemetry("turning absolute left 45 degrees");
        turnAbsolute(45, turnSpeed);
        showTelemetry("going forward 40 inches");
        move(40, (float) 0.5);

        //go to depot, drop marker (prox sensor too)
        showTelemetry("turning relative left 90 degrees");
        turnRelative(90, turnSpeed);
        showTelemetry("going forward 54 inches");
        move(54, (float) 0.5);
        action = "turning relative left 90 degrees";
        telemetry.update();
        turnRelative(90,0.5);
        showTelemetry("dropping marker");
        dropMarker();

        //turn around, go to crater
        showTelemetry("turning relative left 90 degrees");
        turnRelative(90,0.5);
        showTelemetry("going forward 72 inches");
        move(72, (float) 0.5);
        showTelemetry("going into crater");
        intoCrater();
        showTelemetry("finish autonomous");
    }
    private void showTelemetry(String action){
        this.action = action;
        telemetry.update();
    }
}
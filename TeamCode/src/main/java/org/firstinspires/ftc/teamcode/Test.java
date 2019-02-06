package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

/**
 * Created by keert on 12/8/2018.
 */
@Autonomous(name="Test", group="Autonomous")
public class Test extends VirusMethods {

    public static String action = "";
    private boolean needTelemetry = true;
    private boolean havedrive = true;
    private boolean havehinge = true;
    private boolean haveslides = true;
    private boolean haveslidelock = false;
    private boolean haveintake = false;
    private boolean havesweeprs = false;
    private boolean havesifter = false;

    @Override
    public void runOpMode()throws InterruptedException {
        if(needTelemetry){
            action = "starting test auto opmode";
            telemetry.addData("task", action);
        }
        super.runOpMode();
        showTelemetry("initializing imu");
        initializeIMU();
        showTelemetry("waiting for start");
        waitForStart();
        if(havedrive) {
            //go forward a little
            showTelemetry("moving forward 12 inches");
            move(12, (float) .4);
            waitTime(500);

            //go backward a little
            showTelemetry("moving backward 12 inches");
            move(-12, (float) .4);
            waitTime(500);

            //rotate 360 degrees right
            showTelemetry("turning relative left 180 degrees");
            turnRelative(179, .4);
            waitTime(500);

            //rotate 360 degrees left
            showTelemetry("turning relative right 180  degrees");
            turnRelative(-179, .4);
            waitTime(500);
        }
        if(haveslidelock) {
        //disable slide lcck
        showTelemetry("disabling slide lock");
        //slideLock.setPosition(0);
        waitTime(500);

        //anable slide lcok
        showTelemetry("enabling slide lock");
        //slideLock.setPosition(0.5);
        waitTime(500);

        //disable slide lock for next tests
        showTelemetry("disabling slide lock");
        //slideLock.setPosition(0);
        waitTime(500);
        }
        if(haveslides) {
            //extend slides to max
            showTelemetry("extending slide to max");
            slides(slideMax);
            waitTime(500);

            //retract slides
            showTelemetry("retracting slides");
            slides(0);
            waitTime(500);
        }
        if(havehinge) {
            //hinge all the way up
            showTelemetry("raising hinge to 90 degrees");
            hinge(90);
            waitTime(500);
        }
        if(haveslides) {
            //hinge to height of lander
            showTelemetry("extending slides to lander");
            slides(-3500);
            waitTime(500);

            //retract slides for next tests
            showTelemetry("retracting slides");
            slides(0);
            waitTime(500);
        }
        if(havehinge) {
            //hinge all the way down
            showTelemetry("lowering hinge to 0 degrees");
            hinge(0);
            waitTime(500);

            //hinge slightly up for next tests
            showTelemetry("raising hinge to 30 degrees");
            hinge(30);
            waitTime(500);
        }
        if(haveintake) {
            //claw up
            showTelemetry("raising intake");
            intakePivot(true, false);
            waitTime(500);

            //claw down
            showTelemetry("lowering intake");
            intakePivot(false, true);
            waitTime(500);
        }
        if(havesweeprs) {
            //sweep in
            showTelemetry("sweeping in");
            //sweeper.setPower(-1);
            waitTime(500);

            //stop
            showTelemetry("stop sweeping");
            //sweeper.setPower(0);
            waitTime(500);

            //sweep out
            showTelemetry("sweeping out");
            //sweeper.setPower(1);
            waitTime(500);

            //stop
            showTelemetry("stop sweeping");
            //sweeper.setPower(0);
            waitTime(500);
        }
        if(havesifter) {
            //set to ball modes
            showTelemetry("switching to ball mode");
            //sifter.setPosition(1);
            waitTime(500);

            //cube mode
            showTelemetry("switching to cube mode");
            //sifter.setPosition(0);
            waitTime(500);
        }

    }
    public void showTelemetry(String action){
        this.action = action;
        telemetry.update();
    }
}

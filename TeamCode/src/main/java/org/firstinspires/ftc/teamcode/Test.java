package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

/**
 * Created by keert on 12/8/2018.
 */
@Autonomous(name="Test", group="Autonomous")
public class Test extends VirusMethods {
    @Override
    public void runOpMode()throws InterruptedException {
        super.runOpMode();
        initializeIMU();
        waitForStart();
        //go forward a little
        move(12, (float) .4);
        waitTime(500);

        //go backward a little
        move(-12, (float) .4);
        waitTime(500);

        //rottate 360 degrees right
        turnRelative(179,.4);
        waitTime(500);

        //rotate 360 degrees left
        turnRelative(-179,.4);
        waitTime(500);

        //disable slide lcck
        slideLock.setPosition(0);
        waitTime(500);

        //anable slide lcok
        slideLock.setPosition(0.5);
        waitTime(500);

        //disable slide lock for next tests
        slideLock.setPosition(0);
        waitTime(500);

        //extend slides to max
        slides(-7300);
        waitTime(500);

        //retract slides
        slides(0);
        waitTime(500);

        //hinge all the way up
        hinge(90);
        waitTime(500);

        //hinge to height of lander
        slides(-3500);
        waitTime(500);

        //retract slides for next tests
        slides(0);
        waitTime(500);

        //hinge all the way down
        hinge(0);
        waitTime(500);

        //hinge slightly up for next tests
        hinge(30);
        waitTime(500);

        //claw up
        intakePivot(true);
        waitTime(500);

        //claw down
        intakePivot(false);
        waitTime(500);

        //sweep in
        sweeper.setPower(-1);
        waitTime(500);

        //stop
        sweeper.setPower(0);
        waitTime(500);

        //sweep out
        sweeper.setPower(1);
        waitTime(500);

        //stop
        sweeper.setPower(0);
        waitTime(500);

        //set to ball modes
        sifter.setPosition(1);
        waitTime(500);

        //cube mode
        sifter.setPosition(0);
        waitTime(500);

    }
}

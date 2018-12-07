package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name="DepotAuto", group="Autonomous")
public class DepotAuto extends VirusMethods {
    @Override
    public void runOpMode() throws InterruptedException {
        super.runOpMode();
        //hanging:
        // initAutoMotors(); //keep motors running for hang
        //moveHinge(0);
        //add code to drop down
        initializeIMU();
        initVision();

        waitForStart();
        ElapsedTime timer = new ElapsedTime();

        int scanAngle = 12; //positive angle is left turn
        int knockAngle = 26;
        double turnSpeed = 0.3;
        float moveSpeed = 0.4f;
        String goldPos = "bad";

        hinge(90);

        //turn left to look at 2 minerals
        turnRelative(scanAngle, turnSpeed);

        //figure out gold position
        while (opModeIsActive() && timer.seconds() < 5 && goldPos.equals("bad")) {
            telemetry.addData("Timer" , timer.seconds());
            goldPos = autoFindGold();
            telemetry.addData("Gold Position" , goldPos);
            telemetry.update();
        }
        closeTfod();

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
            slides(-5960);
            waitTime(1000);
            slides(0);
        }

        //turn towards wall and go 48 in to wall
        turnAbsolute(45, turnSpeed);
        move(48,moveSpeed);

        //turn to depot, go there (4.5 ft)
        turnAbsolute(-45, turnSpeed);
        move(54,moveSpeed);

        //turn right, drop marker, turn back
        turnRelative(90,turnSpeed);
        marker.setPosition(1);
        waitTime(2000);
        marker.setPosition(0);
        turnAbsolute(135,turnSpeed);

        //go to crater (4.5 ft), extend slides in
        move(54, moveSpeed);
        //IMPORTANT move the intake up
        slides(-3600);
    }
}

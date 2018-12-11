package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name="CraterAuto", group="Autonomous")

public class CraterAuto extends VirusMethods {

    @Override
    public void runOpMode() throws InterruptedException {
        super.runOpMode();
        //initVision();
        slideLock.setPosition(0.5);

        waitForStart();
        initializeIMU();
        ElapsedTime timer = new ElapsedTime();

        int scanAngle = 12; //positive angle is left turn
        int knockAngle = 26;
        double turnSpeed = 0.3;
        float moveSpeed = 0.4f;
        String goldPos = "bad";
        //turn towards wall and go 48 in to wall
        //turnRelative(45, turnSpeed);
        //move(12,moveSpeed);
       // hinge(30);
        intakePivot(true);
        //slides(-2000);
        runDriveMotors(.7f,.7f);
        waitTime(1000);
        //turn to depot, go there (4.5 ft)
//        turnRelative(-90, turnSpeed);
        runDriveMotors(0,0);
//        hinge(90);
//        slides(-3500);
//        move((float)6.0, (float)0.5);
//        slides(0);
//        //turn left to look at 2 minerals
//        turnRelative(scanAngle, turnSpeed);
//
//        //figure out gold position
//        while (opModeIsActive() && timer.seconds() < 5 && goldPos.equals("bad")) {
//            telemetry.addData("Timer" , timer.seconds());
//            goldPos = autoFindGold();
//            telemetry.addData("Gold Position" , goldPos);
//            telemetry.update();
//        }
//        closeTfod();
//        hinge(0);
//        //turn and move to hit (if no detect just move on)
//        //extends slides 2.45 ft to hit mineral
//        if (!goldPos.equals("bad")){
//            if (goldPos.equals("Left")) {
//                turnAbsolute(knockAngle, turnSpeed);
//            }
//            else if (goldPos.equals("Center")) {
//                turnAbsolute(0, turnSpeed);
//            }
//            else if (goldPos.equals("Right")) {
//                turnAbsolute(-knockAngle, turnSpeed);
//            }
//            slides(-5960);
//            waitTime(1000);
//            slides(0);
//        }
//
//        //turn towards wall and go 48 in to wall
//        turnAbsolute(45, turnSpeed);
//        move(48,moveSpeed);
//
//        //turn to depot, go there (4.5 ft)
//        turnAbsolute(135, turnSpeed);
//        move(54,moveSpeed);
//
//        //turn right, drop marker, turn back
//        turnRelative(90,turnSpeed);
//        dropMarker();
//        turnAbsolute(-45,turnSpeed);
//
//        //go to crater (4.5 ft), extend slides in
//        move(54, moveSpeed);
//        //IMPORTANT move the intake up
//        intakePivot(true);
//        waitTime(1000);
//        slides(-3600);
    }
}
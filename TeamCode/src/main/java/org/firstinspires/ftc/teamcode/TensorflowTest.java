package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.ElapsedTime;

//@Autonomous(name = "TensorflowTest", group = "Autonomous")
public class TensorflowTest extends VirusMethods {
    @Override
    public void runOpMode() throws InterruptedException {
        super.runOpMode();
        initializeIMU();
        initVision();
        waitForStart();

        hinge(0);
        slides(100);
        hinge(90);
        turnRelative(13, 0.6);
        ElapsedTime timer = new ElapsedTime();
        String goldPos = "";
        int knockAngle = 30;
        double turnSpeed = 0.6;
        telemetry.addData("Timer", timer.seconds());
        timer.reset();
        while (opModeIsActive() && timer.seconds() < 7 && goldPos.equals("")) {
            telemetry.addData("Timer", timer.seconds());
            goldPos = autoFindGold();
            telemetry.addData("Gold Position", goldPos);
            telemetry.update();
        }
        closeTfod();
        intakePivot(true, false);
        slides(500);
        hinge(0);
        if (!goldPos.equals("")) {
            //showTelemetry("found gold: " + goldPos);
            telemetry.addData("Turning to", goldPos);
            if (goldPos.equals("Left")) {
                //showTelemetry("turning absolute left " + knockAngle +" degrees");
                telemetry.update();
                turnAbsolute(knockAngle, turnSpeed);
                intakePivot(false, false);

            } else if (goldPos.equals("Center")) {
                //showTelemetry("turning absolute 0 degrees");
                turnAbsolute(0, turnSpeed);
                intakePivot(false,false);

            } else if (goldPos.equals("Right")) {
                //showTelemetry("turning absolute right " + knockAngle +" degrees");
                turnAbsolute(-knockAngle, turnSpeed);
                intakePivot(false, false);

            }
        }
        //deafult, ram the right mineral
        if (goldPos.equals("")) {
            telemetry.addData("Did not find gold", "nicht gut");
            turnAbsolute(90, turnSpeed);
            intakePivot(false, false);
        }
    }
}

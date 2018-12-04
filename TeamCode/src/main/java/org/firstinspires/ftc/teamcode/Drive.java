package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name="TeleOp", group="TeleOp")
//0 is front, 1 is back
//l means left, r means right
public class Drive extends VirusMethods {
    double theta;
    double x;
    double y;
    double x2;
    double y2;
    String goldPos = "none";
    public void runOpMode() throws InterruptedException {
        super.runOpMode();
        waitForStart();
        while(opModeIsActive()){
            //original drive system
            lmotor0.setPower(Range.clip(Math.abs(gamepad1.left_stick_y)*gamepad1.left_stick_y-Math.abs(gamepad1.right_stick_x)*gamepad1.right_stick_x,-1,1));
            rmotor0.setPower(Range.clip(Math.abs(gamepad1.left_stick_y)*gamepad1.left_stick_y+Math.abs(gamepad1.right_stick_x)*gamepad1.right_stick_x, -1,1));
            lmotor1.setPower(Range.clip(Math.abs(gamepad1.left_stick_y)*gamepad1.left_stick_y-Math.abs(gamepad1.right_stick_x)*gamepad1.right_stick_x,-1,1));
            rmotor1.setPower(Range.clip(Math.abs(gamepad1.left_stick_y)*gamepad1.left_stick_y+Math.abs(gamepad1.right_stick_x)*gamepad1.right_stick_x, -1,1));
//            x = gamepad1.left_stick_x;
//            y = gamepad1.left_stick_y;
//            theta = -Math.atan(y/x);
//            x2 = x*Math.cos(theta)-y*Math.sin(theta);
//            y2 = x*Math.sin(theta)+y*Math.cos(theta);
//            runDriveMotors((float)x2,(float)y2);
            if(!gamepad2.a && !gamepad2.b){
                slidePower(gamepad2.left_stick_y);
            }

            hingePower(-0.75* gamepad2.right_stick_y);
            //lift up to height of lander (to put minerals in)
            if(gamepad2.b){
                slides(-3500);
            }
            //retract slides
            if(gamepad2.a){
                slides(0);
            }
            goldPos = autoFindGold();
            telemetry.addData("Gold",  goldPos);
            telemetry.update();
            idle();
        }
    }
}
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name="TeleOp", group="TeleOp")
//0 is front, 1 is back
//l means left, r means right
public class Drive extends VirusMethods {
    double fullSpeed = 1;
    double normalSpeed = 0.7;
    double slowSpeed = 0.4;
    float leftSpeed;
    float rightSpeed;
    public void runOpMode() throws InterruptedException {
        super.runOpMode();
        waitForStart();
        while(opModeIsActive()){
            //original drive system
            leftSpeed = Range.clip(Math.abs(gamepad1.left_stick_y)*gamepad1.left_stick_y-Math.abs(gamepad1.right_stick_x)*gamepad1.right_stick_x,-1,1);
            rightSpeed = Range.clip(Math.abs(gamepad1.left_stick_y)*gamepad1.left_stick_y+Math.abs(gamepad1.right_stick_x)*gamepad1.right_stick_x, -1,1);
            float addLeft = 0;
            float addRight = 0;
            if(leftSpeed > 0){
                addLeft = (float)(-(0.3*gamepad1.left_trigger)+(0.3*gamepad1.right_trigger));
            }else if(leftSpeed < 0){
                addLeft = (float)((0.3*gamepad1.left_trigger)+(-0.3*gamepad1.right_trigger));
            }
            if(rightSpeed > 0){
                addRight = (float)(-(0.3*gamepad1.left_trigger)+(0.3*gamepad1.right_trigger));
            }else if(rightSpeed < 0){
                addRight = (float)((0.3*gamepad1.left_trigger)+(-0.3*gamepad1.right_trigger));
            }
            runDriveMotors( leftSpeed + addLeft, rightSpeed + addRight);
//            lmotor0.setPower(Range.clip(Math.abs(gamepad1.left_stick_y)*gamepad1.left_stick_y-Math.abs(gamepad1.right_stick_x)*gamepad1.right_stick_x,-1,1));
//            rmotor0.setPower(Range.clip(Math.abs(gamepad1.left_stick_y)*gamepad1.left_stick_y+Math.abs(gamepad1.right_stick_x)*gamepad1.right_stick_x, -1,1));
//            lmotor1.setPower(Range.clip(Math.abs(gamepad1.left_stick_y)*gamepad1.left_stick_y-Math.abs(gamepad1.right_stick_x)*gamepad1.right_stick_x,-1,1));
//            rmotor1.setPower(Range.clip(Math.abs(gamepad1.left_stick_y)*gamepad1.left_stick_y+Math.abs(gamepad1.right_stick_x)*gamepad1.right_stick_x, -1,1));
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
            telemetry.addData("Rotation X", getRawX());
            telemetry.addData("Rotation Y", getRawY());
            telemetry.addData("Rotation Z", getRawZ());
            telemetry.update();
            idle();

        }
    }
}
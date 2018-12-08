package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name="TeleOp", group="TeleOp")
//0 is front, 1 is back
//l means left, r means right
public class Drive extends VirusMethods {
    float leftSpeed;
    float rightSpeed;
    float factor;
    public void runOpMode() throws InterruptedException {
        super.runOpMode();
        waitForStart();
        while(opModeIsActive()){
            //drive system
            leftSpeed = Range.clip(Math.abs(gamepad1.left_stick_y)*gamepad1.left_stick_y-Math.abs(gamepad1.right_stick_x)*gamepad1.right_stick_x,-1,1);
            rightSpeed = Range.clip(Math.abs(gamepad1.left_stick_y)*gamepad1.left_stick_y+Math.abs(gamepad1.right_stick_x)*gamepad1.right_stick_x, -1,1);
            factor = (float) (0.7 + 0.3*gamepad1.right_trigger -0.5*gamepad2.left_trigger);
            runDriveMotors(leftSpeed, rightSpeed);
            if(!gamepad2.a && !gamepad2.b){
                slidePower(gamepad2.left_stick_y);
            }
            //hinge follows joystick
            hingePower(-0.75* gamepad2.right_stick_y);
            //lift up to height of lander (to put minerals in)
            if(gamepad2.b){
                slides(-3500);
            }
            //retract slides
            if(gamepad2.a){
                slides(0);
            }
            //claw down
            if(gamepad2.dpad_down){
                intakePivot(false);
            }
            //claw up
            if(gamepad2.dpad_up){
                intakePivot(true);
            }
            //enable slide lock
            if(gamepad2.right_trigger > 0){
                slideLock.setPosition(0.5);
            }
            //disable slide lock
            if(gamepad2.left_trigger > 0){
                slideLock.setPosition(0);
            }
            //sweep in
//            if(gamepad2.right_bumper){
//                sweeper.setPower(-1);
//            }else if(gamepad2.left_bumper){
//                sweeper.setPower( 1);
//            } else{
//                sweeper.setPower(0);
//            }
            if (gamepad2.right_bumper){
                sweeper.setPower(-1);
            }
            if (gamepad2.left_bumper){
                sweeper.setPower(1);
            }
            if (!gamepad2.right_bumper && !gamepad2.left_bumper){
                sweeper.setPower(0);
            }
            //ball mode
            if(gamepad2.start){
                sifter.setPosition(0);
            }
            //cube mode
            if(gamepad2.back){
                sifter.setPosition(1);
            }
//            telemetry.addData("Rotation X", getRotationinDimension('X'));
//            telemetry.addData("Rotation Y", getRotationinDimension('Y'));
//            telemetry.addData("Rotation Z", getRotationinDimension('Z'));
//            telemetry.addData("Left slide", slideLeft.getCurrentPosition());
//            telemetry.addData("Right slide", slideRight.getCurrentPosition());
//            telemetry.addData("Left motor", lmotor0.getCurrentPosition());
//            telemetry.addData("Right motor", rmotor0.getCurrentPosition());
            telemetry.update();
            idle();

        }
    }
}
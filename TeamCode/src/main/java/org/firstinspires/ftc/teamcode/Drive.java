package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name="TeleOp", group="TeleOp")
//0 is front, 1 is back
//l means left, r means right
public class Drive extends VirusMethods {

    public void runOpMode() throws InterruptedException {
        super.runOpMode();
        while(opModeIsActive()){
            telemetry.update();
//            lmotor0.setPower(Range.clip(Math.abs(gamepad1.left_stick_y)*gamepad1.left_stick_y-Math.abs(gamepad1.right_stick_x)*gamepad1.right_stick_x,-1,1));
//            rmotor0.setPower(Range.clip(Math.abs(gamepad1.left_stick_y)*gamepad1.left_stick_y+Math.abs(gamepad1.right_stick_x)*gamepad1.right_stick_x, -1,1));
//            lmotor1.setPower(Range.clip(Math.abs(gamepad1.left_stick_y)*gamepad1.left_stick_y-Math.abs(gamepad1.right_stick_x)*gamepad1.right_stick_x,-1,1));
//            rmotor1.setPower(Range.clip(Math.abs(gamepad1.left_stick_y)*gamepad1.left_stick_y+Math.abs(gamepad1.right_stick_x)*gamepad1.right_stick_x, -1,1));
            slidePower(gamepad2.left_stick_y);
            hingePower(-gamepad2.right_stick_y);
            //lift up to height of lander (to put minerals in)
            if(gamepad2.b){
                slides(-3500);
            }
            //retract slides
            if(gamepad2.a){
                slides(0);
            }
            telemetry.addData("Left slide",  slideLeft.getCurrentPosition());
            telemetry.addData("Right slide",  slideRight.getCurrentPosition());
            telemetry.addData("Hinge",  hinge.getCurrentPosition());

            idle();
        }
    }
}
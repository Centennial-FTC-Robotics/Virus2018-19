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
            slideLeft.setPower(gamepad2.left_stick_y);
            slideRight.setPower(gamepad2.left_stick_y);

            if (0 != gamepad2.left_stick_y) {
                if (slideLeft.getCurrentPosition()<-7300) {
                    slides(-7290);
                } else if(slideLeft.getCurrentPosition()>10 ){
                    slides(0);
                } else {
                    slidePower(gamepad2.left_stick_y);
                }
            }
            hinge.setPower(gamepad2.right_stick_y);
            //max -7300 min 0
            telemetry.addData("Left slide",  slideLeft.getCurrentPosition());
            telemetry.addData("Right slide",  slideRight.getCurrentPosition());

            idle();
        }
    }
}
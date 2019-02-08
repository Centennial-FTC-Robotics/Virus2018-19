package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name = "Controller", group = "TeleOp")
public class Controller extends LinearOpMode {
    float leftSpeed;
    float rightSpeed;
    double stickAngle;
    double steerSpeed;
    double stickX;
    double stickY;
    public void runOpMode() throws InterruptedException{
        waitForStart();
        while(opModeIsActive()){
            stickX = gamepad1.right_stick_x;
            stickY = Range.clip(-gamepad1.right_stick_y,0,1);

            stickAngle = -(Math.toDegrees(Math.acos(stickX/(Math.sqrt(Math.pow(stickX,2)+Math.pow(stickY,2)))))-90);
            if (stickX == 0 && stickY == 0){
                stickAngle = 0;
            }
            steerSpeed = stickAngle/90.0;
            leftSpeed = Range.clip(Math.abs(gamepad1.left_stick_y) * gamepad1.left_stick_y + (float)steerSpeed, -1, 1);
            rightSpeed = Range.clip(Math.abs(gamepad1.left_stick_y) * gamepad1.left_stick_y - (float)steerSpeed, -1, 1);
            telemetry.addData("Angle",stickAngle);
            telemetry.addData("Turn speed",steerSpeed);
            telemetry.addData("left speed",leftSpeed);
            telemetry.addData("right speed",rightSpeed);
            telemetry.update();
        }
    }
}

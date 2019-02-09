package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name = "Controller", group = "TeleOp")
public class Controller extends LinearOpMode {
    float leftSpeed;
    float rightSpeed;
    double stickAngle = 0;
    double steerSpeed;
    double stickX;
    double stickY;
    //-1 is left, 0 is none, 1 is right
    int direction = 0;
    public void runOpMode() throws InterruptedException{
        waitForStart();
        while(opModeIsActive()){
            stickX = gamepad1.right_stick_x;
            stickY = -gamepad1.right_stick_y;
            //if stick is on the edge (is Andrew trying to turn)
            //otherwise stick angle and direction stay at default values of zero
            if(Math.sqrt(Math.pow(stickX,2)+Math.pow(stickY,2)) > 0.9){
                stickAngle = -(Math.toDegrees(Math.acos(stickX/(Math.sqrt(Math.pow(stickX,2)+Math.pow(stickY,2)))))-90);
                //makes the angles below x axis correct
                if(stickY < 0){
                    if(stickX < 0){
                        stickAngle = -180-stickAngle;
                    }else{
                        stickAngle = 180-stickAngle;
                    }
                }
            }
            //if at zero degrees, then make direction of turning zero
            //otherwise, only updates direction when within -20 and 20 deg, so that it doesn't change direction between 180 and -180
            if(stickAngle == 0){
                direction = 0;
            }else if(stickAngle > -20 && stickAngle < 20){
                direction = (int)(stickAngle/Math.abs(stickAngle));
            }
            //if stick angle is in turning direction, then turn as normal
            //if Andrew crosses between -180 and 180, keeps going in original direction
            if(stickAngle*direction > 0){
                steerSpeed = stickAngle/180.0;
            }else{
                steerSpeed = direction;
            }

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

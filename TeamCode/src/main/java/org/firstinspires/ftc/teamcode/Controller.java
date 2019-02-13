package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name = "Controller", group = "TeleOp")
public class Controller extends LinearOpMode {
    float leftSpeed;
    float rightSpeed;
    double stickAngle = 0;
    double steerSpeed;
    double stickX;
    double stickY;
    int intakeStep = 0;
    //-1 is left, 0 is none, 1 is right
    int direction = 0;
    ElapsedTime timer = new ElapsedTime();
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
            if (gamepad2.a){
                intoCraterSimul();
            }
            leftSpeed = Range.clip(Math.abs(gamepad1.left_stick_y) * gamepad1.left_stick_y + (float)steerSpeed, -1, 1);
            rightSpeed = Range.clip(Math.abs(gamepad1.left_stick_y) * gamepad1.left_stick_y - (float)steerSpeed, -1, 1);
            telemetry.addData("Angle",stickAngle);
            telemetry.addData("left speed",leftSpeed);
            telemetry.addData("right speed",rightSpeed);
            telemetry.addData("intake step",intakeStep);
            telemetry.update();
        }

    }
    public boolean slidesSimul(int time){

        if (timer.seconds() > time) {
            return true;
        }
        return false;
    }
    public void intoCraterSimul() {
        switch (intakeStep) {
            case 0:
                if (slidesSimul(3)){
                    intakeStep++;
                }
                break;
            case 1:
                if (slidesSimul(4)){
                    intakeStep++;
                }
                break;
            case 2:
                if (slidesSimul(2)){
                    intakeStep++;
                }
                break;
            case 3:
                if (slidesSimul(5)){
                    intakeStep = 0;
                }
                break;
        }

    }
}

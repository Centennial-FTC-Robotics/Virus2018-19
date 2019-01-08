package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;

@TeleOp(name="TeleOp", group="TeleOp")
//0 is front, 1 is back
//l means left, r means right
public class Drive extends VirusMethods {
    float leftSpeed;
    float rightSpeed;
    float factor;
    int red1;
    int green1;
    int blue1;
        public void runOpMode() throws InterruptedException {
        super.runOpMode();
        if (slideLeft.getCurrentPosition()>2500){
            intakeState = intakeState.crater;
        }else{
            intakeState = intakeState.retracted;
        }
        waitForStart();
        while(opModeIsActive()){
            red1 = colorSensor1.red();
            green1 = colorSensor1.green();
            blue1 = colorSensor1.blue();
            angles  = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
            gravity = imu.getGravity();
            //drive system
            leftSpeed = Range.clip(Math.abs(gamepad1.left_stick_y)*gamepad1.left_stick_y-Math.abs(gamepad1.right_stick_x)*gamepad1.right_stick_x,-1,1);
            rightSpeed = Range.clip(Math.abs(gamepad1.left_stick_y)*gamepad1.left_stick_y+Math.abs(gamepad1.right_stick_x)*gamepad1.right_stick_x, -1,1);
            factor = (float) (0.7 + 0.3*gamepad1.right_trigger -0.5*gamepad1.left_trigger);
            runDriveMotors(factor*leftSpeed, factor*rightSpeed);
            if(!gamepad2.a && !gamepad2.b && !gamepad2.x && !gamepad2.y){

                slidePower(gamepad2.left_stick_y);
            }
            //hinge follows joystick
            hingePower(-1.0* gamepad2.right_stick_y);
            //hinge.setPower(gamepad2.right_stick_y);
            //lift up to height of lander (to put minerals in)

//            if(gamepad2.a){
//                retract();
//            }
//            if(gamepad2.b && !gamepad2.start){
//                standby();
//            }
//            if(gamepad2.y){
//                intoCrater();
//            }
//            if(gamepad2.x){
//                score();
//            }
//            //claw down
//            if(gamepad2.dpad_down){
//                intakePivot(false);
//            }
//            //claw up
//            if(gamepad2.dpad_up){
//                intakePivot(true);
//            }
//            //enable slide lock
//            if(gamepad2.right_trigger > 0){
//                slideLock.setPosition(0.5);
//            }
//            //disable slide lock
//            if(gamepad2.left_trigger > 0){
//                slideLock.setPosition(0);
//            }
//            //sweepers
//            if(gamepad2.right_bumper){
//                //sweep in
//                sweeperVex.setPower(-0.5);
//            }else if(gamepad2.left_bumper){
//                //sweep out
//                sweeperVex.setPower(0.3);
//            } else{
//                //idle, sweep in slowly
//                sweeperVex.setPower(0);
//            }
//            //ball mode
//            if(gamepad2.dpad_right){
//                sifter.setPosition(1);
//            }
//            //cube mode
//            if(gamepad2.dpad_left){
//                sifter.setPosition(0);
//            }
//            if(intakeState == intakeState.crater){
//                if (gamepad1.left_stick_y == 0 && gamepad1.right_stick_x == 0){
//                    double turnFactor = 0.3 - 0.207*(slideLeft.getCurrentPosition()/7300);
//                    runDriveMotors((float) (turnFactor*gamepad2.left_stick_x), (float) (-turnFactor*gamepad2.left_stick_x));
//                }
//            }
            updateIntakes();
//            telemetry.addData("Left slide", slideLeft.getCurrentPosition());
//            telemetry.addData("Right slide", slideRight.getCurrentPosition());
            telemetry.addData("Hinge Encoder", hinge.getCurrentPosition());
            telemetry.addData("Hinge Angle", hingeAngle());
//            telemetry.addData("Slide Left", slideLeft.getCurrentPosition());
//            telemetry.addData("Slide Right", slideRight.getCurrentPosition());
//            telemetry.addData("Gamepad 2 Right Joystick y", gamepad2.right_stick_y);
//            telemetry.addData("Gamepad 2 Right Joystick x", gamepad2.right_stick_x);
//            telemetry.addData("Gamepad 2 Left Joystick y", gamepad2.left_stick_y);
//            telemetry.addData("Gamepad 2 Left Joystick x", gamepad2.left_stick_x);
//            telemetry.addData("Intake 1", slot1);
//            telemetry.addData("Intake 2", slot2);
            telemetry.update();
            idle();

        }
    }
}

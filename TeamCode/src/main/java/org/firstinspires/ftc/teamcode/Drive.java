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
    String intake;
    int red;
    int green;
    int blue;
        public void runOpMode() throws InterruptedException {
        super.runOpMode();
        if (slideLeft.getCurrentPosition()>2500){
            intakeState = intakeState.crater;
        }else{
            intakeState = intakeState.retracted;
        }
        waitForStart();
        while(opModeIsActive()){
            red = colorSensor.red();
            green = colorSensor.green();
            blue = colorSensor.blue();
            angles  = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
            gravity = imu.getGravity();
            //drive system
            leftSpeed = Range.clip(Math.abs(gamepad1.left_stick_y)*gamepad1.left_stick_y-Math.abs(gamepad1.right_stick_x)*gamepad1.right_stick_x,-1,1);
            rightSpeed = Range.clip(Math.abs(gamepad1.left_stick_y)*gamepad1.left_stick_y+Math.abs(gamepad1.right_stick_x)*gamepad1.right_stick_x, -1,1);
            factor = (float) (0.7 + 0.3*gamepad1.right_trigger -0.5*gamepad1.left_trigger);
            runDriveMotors(factor*leftSpeed, factor*rightSpeed);
            if(!gamepad2.a && !gamepad2.b){

                slidePower(gamepad2.left_stick_y);
            }
            //hinge follows joystick
            hingePower(-0.75* gamepad2.right_stick_y);
            //lift up to height of lander (to put minerals in)
            if(gamepad2.b && !gamepad2.start){
                slides(3500);
            }

            if(gamepad2.a){
                retract();
            }
            if(gamepad2.b && !gamepad2.start){
                intoCrater();
            }
            if(gamepad2.y){
                score();
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
            //sweepers
            if(gamepad2.right_bumper){
                //sweep in
                sweeperVex.setPower(-0.5);
            }else if(gamepad2.left_bumper){
                //sweep out
                sweeperVex.setPower(0.3);
            } else{
                //idle, sweep in slowly
                sweeperVex.setPower(0);
            }
            //ball mode
            if(gamepad2.dpad_right){
                sifter.setPosition(1);
            }
            //cube mode
            if(gamepad2.dpad_left){
                sifter.setPosition(0);
            }
            //enter rake crater
            if (gamepad2.x){
                hinge(45);
                intakePivot(true);
                slides(3000);
                intakePivot(false);
                hinge(0);
            }
            if (colorSensor.red()<30 && colorSensor.green()<30 && colorSensor.blue()<30){
                intake = "Nothing";
            }
            if (Math.abs((red - blue)/((red+blue)/2)) < .2){
                intake = "Ball";
            }
            if ((Math.abs((red - blue)/((red+blue)/2)) > .2) && (Math.abs((red - green)/((red+green)/2)) > .2)){
                intake = "Cube";
            }
//            telemetry.addData("Left slide", slideLeft.getCurrentPosition());
//            telemetry.addData("Right slide", slideRight.getCurrentPosition());
//            telemetry.addData("Hinge Angle", hingeAngle());
//            telemetry.addData("Joystick x", gamepad2.left_stick_x);
            telemetry.addData("Intake", intake);
            telemetry.update();
            idle();

        }
    }
}

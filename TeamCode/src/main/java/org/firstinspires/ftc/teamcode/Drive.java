package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;
import com.sun.tools.javac.util.Position;

@TeleOp(name = "TeleOp", group = "TeleOp")
//0 is front, 1 is back
//l means left, r means right
public class Drive extends VirusMethods {
    float leftSpeed;
    float rightSpeed;
    float factor;
    float moveSpeed;
    float turnSpeed;
    ElapsedTime time = new ElapsedTime();


    public void runOpMode() throws InterruptedException {
        super.runOpMode();
        time.reset();
        sifter.setPosition(0);
        waitForStart();

        while (opModeIsActive()) {
            updateOrientation();
            usingOutrigger = (gamepad2.right_trigger > 0) || (gamepad2.left_trigger > 0);
            //drive system
            moveSpeed = (float) Math.pow(gamepad1.left_stick_y, 3);
            turnSpeed = (float) ((float) -(-Math.abs(-0.3*moveSpeed) + 1)*Math.pow(gamepad1.right_stick_x, 3));
            if (moveSpeed != 0) {
                leftSpeed = moveSpeed + (turnSpeed);
                rightSpeed = moveSpeed - (turnSpeed);
            } else {
                turnSpeed = (float) -Math.pow(gamepad1.right_stick_x, 3);
                leftSpeed = turnSpeed;
                rightSpeed = -turnSpeed;
            }
            factor = (float) (0.7 + 0.3 * gamepad1.right_trigger - 0.5 * gamepad1.left_trigger);
//            if (!(intakeState == intakeState.crater && leftSpeed == 0 && rightSpeed == 0 && gamepad2.left_stick_x != 0)) {
//
//            }
            runDriveMotors(Range.clip(factor * leftSpeed,-1, 1), Range.clip(factor * rightSpeed, -1, 1));
            if (!gamepad2.a && !gamepad2.b && !gamepad2.x && !gamepad2.y) {
                slidePower(-gamepad2.left_stick_y);
                hingePower(-1.0 * gamepad2.right_stick_y);
            }
            //hinge follows joystick

//            boolean usingPivot = (gamepad2.dpad_down) || (gamepad2.dpad_up);


            //hinge.setPower(gamepad2.right_stick_y);
            //lift up to height of lander (to put minerals in)

            if (gamepad2.a) {
                //retract
                slidePower(-1);
            }
            if (!gamepad2.start && gamepad2.b) {
                //score spheres
                int lower = 3250;
                int upper = 3400;
                hingePower(1);
                int pos = slideLeft.getCurrentPosition();

                if(pos >= lower && pos <= upper){
                    slidePower(0);
                }else if (slideLeft.getCurrentPosition() < lower) {
                    slidePower(1);
                } else if(slideLeft.getCurrentPosition() > upper) {
                    slidePower(-1);
                }
            }
            if(gamepad2.y){
                //standby
                hinge(10);
            }

            if(gamepad2.x){
                //hanging height
                if (slideLeft.getCurrentPosition() < 1700) {
                    slidePower(0.5);
                } else {
                    slidePower(0);
                }
            }
            //claw down
            if (gamepad2.dpad_down) {
                intakePivot(false, true);
            }
            //claw up
            if (gamepad2.dpad_up) {
                intakePivot(true, true);
            }
            //enable usingOutrigger
            if (gamepad2.right_trigger > 0) {
                outrigger.setPosition(1);
            }
            //disable usingOutrigger
            if (gamepad2.left_trigger > 0) {
                outrigger.setPosition(0);
            }
            //sweepers
            if (gamepad2.right_bumper) {
                //sweep in
                sweeperVex.setPower(-0.5);
                updateIntakes();
            } else if (gamepad2.left_bumper) {
                //sweep out
                sweeperVex.setPower(0.5);
                updateIntakes();
            } else {
                //idle, sweep in slowly
                //sweeperVex.setPower(-0.3);
                //update intake every 3 seconds
                sweeperVex.setPower(0);
                if (time.time() % 3 == 0) {
                    updateIntakes();
                }
            }
//            //ball mode
            if (gamepad2.dpad_right) {
                sifter.setPosition(1);
            }
//            //cube mode
            if (gamepad2.dpad_left) {
                sifter.setPosition(0);
            }

            //endgame mode
            if (gamepad1.x && gamepad1.y) {
                hingeMin = (int) (-20 * (3700 / 90));
            }
            if (gamepad1.a && gamepad1.b) {
                hingeMin = 0;
            }
//            if ((intakeState == intakeState.crater) || (slideLeft.getCurrentPosition() > 1000 && hingeAngle() < 45)) {
//                if (gamepad1.left_stick_y == 0 && gamepad1.right_stick_x == 0) {
//                    double turnFactor = 0.3 - 0.207 * (slideLeft.getCurrentPosition() / 7300);
//                    runDriveMotors((float) (-turnFactor * gamepad2.left_stick_x), (float) (turnFactor * gamepad2.left_stick_x));
//                }
//            }
//            telemetry.addData("Left slide", slideLeft.getCurrentPosition());
//            telemetry.addData("Right slide", slideRight.getCurrentPosition());
            telemetry.addData("Hinge Encoder", hinge.getCurrentPosition());
            telemetry.addData("Hinge Angle", hingeAngle());
//            telemetry.addData("Intake state", intakeState);
//            telemetry.addData("Left Slide Encoder", slideLeft.getCurrentPosition());
//            telemetry.addData("Right Slide Encoder", slideRight.getCurrentPosition());
            telemetry.addData("Heading", getHeading());
//            telemetry.addData("X pos", imu.getPosition().x);
//            telemetry.addData("Y pos", imu.getPosition().y);
//            telemetry.addData("Gamepad 2 Right Joystick y", gamepad2.right_stick_y);
//            telemetry.addData("Gamepad 2 Right Joystick x", gamepad2.right_stick_x);
//            telemetry.addData("Gamepad 2 Left Joystick y", gamepad2.left_stick_y);
//            telemetry.addData("Gamepad 2 Left Joystick x", gamepad2.left_stick_x);
//            telemetry.addData("Gamepad 1 Right Joystick x", gamepad1.right_stick_x);
//            telemetry.addData("Gamepad 1 Left Joystick y", gamepad1.left_stick_y);
//            telemetry.addData("Turn speed", turnSpeed);
//            telemetry.addData("Move speed", moveSpeed);
//            telemetry.addData("Left speed", leftSpeed);
//            telemetry.addData("Right speed", rightSpeed);
            telemetry.update();
            idle();

        }

    }

}

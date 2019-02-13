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
    ElapsedTime time = new ElapsedTime();
    private String action = "";
    public static boolean needTelemetry = false;


    public void runOpMode() throws InterruptedException {
        if (needTelemetry) {
            action = "starting drive opmode";
            telemetry.addData("task", action);
        }
        super.runOpMode();
        time.reset();
        sifter.setPosition(0);
        waitForStart();

        while (opModeIsActive()) {
            //drive system
            leftSpeed = Range.clip(Math.abs(gamepad1.left_stick_y) * gamepad1.left_stick_y - Math.abs(gamepad1.right_stick_x) * gamepad1.right_stick_x, -1, 1);
            rightSpeed = Range.clip(Math.abs(gamepad1.left_stick_y) * gamepad1.left_stick_y + Math.abs(gamepad1.right_stick_x) * gamepad1.right_stick_x, -1, 1);
            factor = (float) (0.7 + 0.3 * gamepad1.right_trigger - 0.5 * gamepad1.left_trigger);
            if (!(intakeState == intakeState.crater && leftSpeed == 0 && rightSpeed == 0 && gamepad2.left_stick_x != 0)) {
                runDriveMotors(factor * leftSpeed, factor * rightSpeed);
            }
            if (!gamepad2.a && !gamepad2.b && !gamepad2.x && !gamepad2.y) {
                slidePower(-gamepad2.left_stick_y);
                hingePower(-1.0 * gamepad2.right_stick_y, false);
            }
            //hinge follows joystick
//            boolean usingOutrigger = (gamepad2.right_trigger > 0) || (gamepad2.left_trigger > 0);
//            boolean usingPivot = (gamepad2.dpad_down) || (gamepad2.dpad_up);


            //hinge.setPower(gamepad2.right_stick_y);
            //lift up to height of lander (to put minerals in)

//            if(gamepad2.a){
//                showTelemetry("retracting slides )button a)");
//                retractSimul();
//            }
//            if(gamepad2.start && gamepad2.b){
//                while(gamepad2.start || gamepad2.b){
//                    showTelemetry("waiting while initializing");
//                    //do nothing
//                }
//            }
//            if(gamepad2.b && !gamepad2.start){
//                showTelemetry("going into standby (button b)");
//                standbySimul();
//            }
//            if(gamepad2.y){
//                showTelemetry("going into crater (button y)");
//                intoCraterSimul();
//            }
//            if(gamepad2.x){
//                showTelemetry("scoring (button x)");
//                scoreSimul();
//            }
            //claw down
            if (gamepad2.dpad_down) {
                intakePivot(false,true);
            }
            //claw up
            if (gamepad2.dpad_up) {
                intakePivot(true,true);
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
            if ((intakeState == intakeState.crater) || (slideLeft.getCurrentPosition() > 1000 && hingeAngle() < 45)) {
                if (gamepad1.left_stick_y == 0 && gamepad1.right_stick_x == 0) {
                    double turnFactor = 0.3 - 0.207 * (slideLeft.getCurrentPosition() / 7300);
                    runDriveMotors((float) (-turnFactor * gamepad2.left_stick_x), (float) (turnFactor * gamepad2.left_stick_x));
                }
            }
            telemetry.addData("Left slide", slideLeft.getCurrentPosition());
            telemetry.addData("Right slide", slideRight.getCurrentPosition());
//            telemetry.addData("Hinge Encoder", hinge.getCurrentPosition());
//            telemetry.addData("Hinge Angle", hingeAngle());
            telemetry.addData("Intake state", intakeState);
//            telemetry.addData("Left Slide Encoder", slideLeft.getCurrentPosition());
//            telemetry.addData("Right Slide Encoder", slideRight.getCurrentPosition());
            telemetry.addData("Heading", getRotationinDimension(('Z')));
            telemetry.addData("X pos", imu.getPosition().x);
            telemetry.addData("Y pos", imu.getPosition().y);
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

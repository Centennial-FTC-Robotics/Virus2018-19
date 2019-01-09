package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

//with drive motors, lag: 2
//with hinge, lag: 2
//with color sensors, lag: 5
//with slides, lag: 5
@TeleOp(name="Basic", group="TeleOp")
public class Basic extends LinearOpMode {

    DcMotor lmotor0;
    DcMotor lmotor1;
    DcMotor rmotor0;
    DcMotor rmotor1;
    DcMotor hinge;
    ColorSensor colorSensor1, colorSensor2;
    DcMotor slideLeft;
    DcMotor slideRight;

    float leftSpeed;
    float rightSpeed;
    float factor;
    int red1;
    int green1;
    int blue1;

    enum intake {Ball, Cube, None}
    VirusMethods.intake slot1;
    VirusMethods.intake slot2;
    VirusMethods.intake[] intakeSlots = {slot1, slot2};
    ColorSensor[] colorSensors = {colorSensor1, colorSensor2};

    public void runOpMode() throws InterruptedException {
        lmotor0 = hardwareMap.dcMotor.get("lmotor0");
        lmotor1 = hardwareMap.dcMotor.get("lmotor1");
        rmotor0 = hardwareMap.dcMotor.get("rmotor0");
        rmotor1 = hardwareMap.dcMotor.get("rmotor1");
        hinge = hardwareMap.dcMotor.get("hinge");
        colorSensor1 = hardwareMap.colorSensor.get("colorSensor1");
        colorSensor2 = hardwareMap.colorSensor.get("colorSensor2");
        slideLeft = hardwareMap.dcMotor.get("slideLeft");
        slideRight = hardwareMap.dcMotor.get("slideRight");

        rmotor0.setDirection(DcMotor.Direction.REVERSE);
        rmotor1.setDirection(DcMotor.Direction.REVERSE);
        hinge.setDirection(DcMotor.Direction.REVERSE);
        slideRight.setDirection(DcMotor.Direction.REVERSE);

        lmotor0.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        lmotor1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rmotor0.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rmotor1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        hinge.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        slideLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        slideRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        lmotor0.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rmotor0.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        lmotor1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rmotor1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        hinge.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        slideLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        slideRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        waitForStart();

        while(opModeIsActive()){
            red1 = colorSensor1.red();
            green1 = colorSensor1.green();
            blue1 = colorSensor1.blue();
            leftSpeed = Range.clip(Math.abs(gamepad1.left_stick_y)*gamepad1.left_stick_y-Math.abs(gamepad1.right_stick_x)*gamepad1.right_stick_x,-1,1);
            rightSpeed = Range.clip(Math.abs(gamepad1.left_stick_y)*gamepad1.left_stick_y+Math.abs(gamepad1.right_stick_x)*gamepad1.right_stick_x, -1,1);
            factor = (float) (0.7 + 0.3*gamepad1.right_trigger -0.5*gamepad1.left_trigger);
            runDriveMotors(factor*leftSpeed, factor*rightSpeed);
            //hinge.setPower(gamepad2.right_stick_y);
            hingePower(-1.0* gamepad2.right_stick_y);
            slidePower(gamepad2.left_stick_y);

            updateIntakes();

            telemetry.addData("Hinge Encoder", hinge.getCurrentPosition());
            telemetry.addData("Hinge Angle", hingeAngle());

            telemetry.update();
        }
    }

    public double percentDiff(int num1, int num2) {
        double diff = (((double)num1-(double)num2)/(((double)num1+(double)num2)/2))*100;
        return diff;
    }

    public void updateIntakes(){
        int red1 = colorSensor1.red();
        int blue1 = colorSensor1.blue();
        int green1 = colorSensor1.green();
        int red2 = colorSensor1.red();
        int blue2 = colorSensor1.blue();
        int green2 = colorSensor1.green();

        if (percentDiff(red1,blue1) > 70){
            slot1 = VirusMethods.intake.Cube;
        }else if ((red1 + green1 + blue1)/3 > 30){
            slot1 = VirusMethods.intake.Ball;
        }else{
            slot1 = VirusMethods.intake.None;
        }
        if (percentDiff(red2,blue2) > 70){
            slot2 = VirusMethods.intake.Cube;
        }else if ((red2 + green2 + blue2)/3 > 30){
            slot2 = VirusMethods.intake.Ball;
        }else{
            slot2 = VirusMethods.intake.None;
        }
    }

    public void runDriveMotors(float leftSpeed, float rightSpeed) {
        lmotor0.setPower(Range.clip(leftSpeed, -1, 1));
        lmotor1.setPower(Range.clip(leftSpeed, -1, 1));
        rmotor0.setPower(Range.clip(rightSpeed, -1, 1));
        rmotor1.setPower(Range.clip(rightSpeed, -1, 1));
    }

    //get hinge position
    public int hingeAngle(){
        return (int) ((hinge.getCurrentPosition()*90)/1124);
    }

    //set hinge power
    public void hingePower(double power){
//        if (power > 0){
//            slideLock.setPosition(0);
//            outrigger.setPosition(1);
//        }else{
//            outrigger.setPosition(0);
//        }
        hinge.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        //if at 90 degrees, only move if decreasing angle
        if (hinge.getCurrentPosition() >= 3700) {
            if (power < 0) {
                hinge.setPower(power);
            }
            else {
                hinge.setPower(0);
            }
        }
        //if at 0 degrees, only move if increasing angle
        else if (hinge.getCurrentPosition() <= 0){
            if (power > 0) {
                hinge.setPower(power);
            }
            else {
                hinge.setPower(0);
            }
        }
        //if in between 0 and 90 degrees, move however
        else {
            hinge.setPower(power);
        }
    }

    public void slidePower(double power){
        slideLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        slideRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        //if at max extension, only move if retracting
        if (slideLeft.getCurrentPosition() <= 0 || slideRight.getCurrentPosition() <= 0) {
            if (power > 0) {
                slideLeft.setPower(power);
                slideRight.setPower(power);
            }
            else {
                slideLeft.setPower(0);
                slideRight.setPower(0);
            }
        }
        //if completely retracted, only move if extending
        else if (slideLeft.getCurrentPosition() >= 7300 || slideRight.getCurrentPosition() >= 7300){
            if (power < 0) {
                slideLeft.setPower(power);
                slideRight.setPower(power);
            }
            else {
                slideLeft.setPower(0);
                slideRight.setPower(0);
            }
        }
        //if in between, move however
        else {
            slideLeft.setPower(power);
            slideRight.setPower(power);
        }
    }
}

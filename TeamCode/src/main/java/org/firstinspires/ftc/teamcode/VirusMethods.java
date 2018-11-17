package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;

import java.util.List;

/**
 * Created by keert on 10/26/2018.
 */

public class VirusMethods extends VirusHardware {
    //motors
//    private final DcMotor[] leftDriveMotors = {lmotor0, lmotor1};
//    private final DcMotor[] rightDriveMotors = {rmotor0, rmotor1};
//    private final DcMotor[] driveMotors = {lmotor0, lmotor1, rmotor0, rmotor1};

    //for reference
    private final int driveTicksPerRev = 560; //change this to value of our motor
    private final int driveSprocket = 24;
    private final int wheelSprocket = 22;
    private final int wheelDiameterIn = 4;

    //tensor flow and vuforia stuff
    private static final String TFOD_MODEL_ASSET = "RoverRuckus.tflite";
    private static final String LABEL_GOLD_MINERAL = "Gold Mineral";
    private static final String LABEL_SILVER_MINERAL = "Silver Mineral";
    private static final String VUFORIA_KEY = "AQmuIUP/////AAAAGR6dNDzwEU07h7tcmZJ6YVoz5iaF8njoWsXQT5HnCiI/oFwiFmt4HHTLtLcEhHCU5ynokJgYSvbI32dfC2rOvqmw81MMzknAwxKxMitf8moiK62jdqxNGADODm/SUvu5a5XrAnzc7seCtD2/d5bAIv1ZuseHcK+oInFHZTi+3BvhbUyYNvnVb0tQEAv8oimzjiQW18dSUcEcB/d6QNGDvaDHpxuRCJXt8U3ShJfBWWQEex0Vp6rrb011z8KxU+dRMvGjaIy+P2p5GbWXGJn/yJS9oxuwDn3zU6kcQoAwI7mUgAw5zBGxxM+P35DoDqiOja6ST6HzDszHxClBm2dvTRP7C4DEj0gPkhX3LtBgdolt";
    private VuforiaLocalizer vuforia; //Vuforia localization engine
    private TFObjectDetector tfod; //Tensor Flow Object Detection engine

    /* -------------- Initialization -------------- */

    private void initVuforia() {
        //create parameter object and pass it to create Vuforia engine
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();
        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        vuforia = ClassFactory.getInstance().createVuforia(parameters);
    }

    private void initTfod() {
        //create parameter object and pass it to create Tensor Flow object detector
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_GOLD_MINERAL, LABEL_SILVER_MINERAL);
    }

    public void autoInit() {
        initVuforia();
        if (ClassFactory.getInstance().canCreateTFObjectDetector()) {
            initTfod();
        } else {
            telemetry.addData("Sorry!", "This device is not compatible with TFOD");
        }
        //wait for game to start
        telemetry.addData(">", "Press Play to start tracking");
        telemetry.update();
        waitForStart();
    }

    public void initializeIMU(){
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "AdafruitIMUCalibration.json"; // see the calibration sample opmode
        parameters.loggingEnabled      = true;
        parameters.loggingTag          = "IMU";
        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();
        imu.initialize(parameters);
    }

    public void initVision() {
        initVuforia();
        if (ClassFactory.getInstance().canCreateTFObjectDetector()) {
            initTfod();
        } else {
            telemetry.addData("Sorry!", "This device is not compatible with TFOD");
        }
        //wait for game to start
        telemetry.addData(">", "Press Play to start tracking");
        telemetry.update();
    }

    public void initAutoMotors() {

        slidePower(1);
    }

     /* -------------- Status Methods -------------- */

//    public boolean motorsBusy() {
//        boolean busy = false;
//        for (DcMotor motor : driveMotors) {
//            if (motor.isBusy()) {
//                busy = true;
//            }
//        }
//        return busy;
//    }

    public double getRawZ() {

        return orientation.firstAngle;
    }

    public double getRawY() {

        return orientation.thirdAngle;
    }

    public double getRawX() {

        return orientation.secondAngle;
    }

    public OpenGLMatrix getRotation() {

        return orientation.getRotationMatrix();
    }

    public double getRotationinDimension(char dimension) {

        switch (Character.toUpperCase(dimension)) {
            case 'X':

                return AngleUnit.normalizeDegrees(getRawX() - initialPitch);
            case 'Y':

                return AngleUnit.normalizeDegrees(getRawY() - initialRoll);
            case 'Z':

                return AngleUnit.normalizeDegrees(getRawZ() - initialHeading);
        }

        return 0;
    }

    /* -------------- Processing -------------- */

    private double getAngleDist(double angle, double currentAngle) {

        double referenceAngle = (currentAngle + 180 > 360) ? (currentAngle - 180) : (currentAngle + 180);
        double distance = 180 - Math.abs(referenceAngle - angle);

        return distance;
    }

    private int convertInchToEncoder(float dist) {

        float wheelRotations = (float)(dist / (wheelDiameterIn * Math.PI));
        float motorRotations = (22/24) * (wheelRotations);
        float encoderCounts = 560 * motorRotations;
        int position = Math.round(encoderCounts);

        return position;
    }

    private double convertEncoderToInch(int encoders) {

        float motorRotations = encoders / 560;
        float wheelRotations = motorRotations * (24/22);
        double distance = wheelRotations * (wheelDiameterIn * Math.PI);

        return distance;
    }

    /* -------------- Movement -------------- */

    //movement based on speeds
//    public void runDriveMotors(float leftSpeed, float rightSpeed) {
//        lmotor0.setPower(Range.clip(leftSpeed, -1, 1));
//        lmotor1.setPower(Range.clip(leftSpeed, -1, 1));
//        rmotor0.setPower(Range.clip(rightSpeed, -1, 1));
//        rmotor1.setPower(Range.clip(rightSpeed, -1, 1));
//    }

    //sets side to certain position
    public void slides(int position){
        slideLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        slideRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        position = Range.clip(position, -7300, 0);
        slideLeft.setTargetPosition(position);
        slideRight.setTargetPosition(position);
        slideLeft.setPower(1);
        slideRight.setPower(1);
    }
    //set slide power
    public void slidePower(double power){
        slideLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        slideRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        //if at max extension, only move if retracting
        if (slideLeft.getCurrentPosition() <= -7300 || slideRight.getCurrentPosition() <= -7300) {
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
        else if (slideLeft.getCurrentPosition() >= 0 || slideRight.getCurrentPosition() >= 0){
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
    //set hinge position
    public void hinge(double angle) {
        hinge.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        angle = Range.clip(angle, 0, 90);
        int position = (int)(angle * (1200/90));
        hinge.setTargetPosition(position);
        hinge.setPower(1);
    }
    //set hinge power
    public void hingePower(double power){
        hinge.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        //if at 90 degrees, only move if decreasing angle
        if (hinge.getCurrentPosition() >= 1100) {
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
    //currently in inches
//    public void move(float distance) {
//        //converting from linear distance -> wheel rotations ->
//        // motor rotations -> encoder counts, then round
//
//        int position = convertInchToEncoder(distance);
//
//        for (DcMotor motor : driveMotors) {
//            motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//            motor.setTargetPosition(position);
//        }
//        waitForMotors();
//        for (DcMotor motor : driveMotors) {
//            motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        }
//    }
//
//    public void turn(double angle, double speed) {
//
//        // normalize the angle
//        angle = AngleUnit.normalizeDegrees(angle);
//
//        double currentAngle = getRotationinDimension('Z');
//        double referenceAngle = (currentAngle + 180 > 360) ? (currentAngle - 180) : (currentAngle + 180);
//        double distance = 180 - Math.abs(referenceAngle - angle);
//        int direction =(int) (((referenceAngle - angle) == 0) ? 1 : (referenceAngle - angle) / Math.abs(referenceAngle - angle)); // -1 is right, 1 is left
//        double turnRate = (distance * speed) / 90;
//
//        while (Math.abs(getRotationinDimension('Z') - angle) > 1) {
//
//            runDriveMotors((float) (-direction * (turnRate)), (float) (direction * (turnRate)));
//            distance = getAngleDist(angle, getRotationinDimension('Z'));
//            turnRate = (distance * speed) / 90;
//
//        }
//
//        runDriveMotors(0,0);
//    }
    /* -------------- Procedure -------------- */

//    public void waitForMotors() {
//        while (motorsBusy()) {
//        }
//    }
    public void updateOrientation (){
        orientation = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZXY, AngleUnit.DEGREES);
    }
        /* -------------- Technical Innovation -------------- */

    //returns left, right, or center based on position of gold
    public String autoFindGold() {
        //added:
        String goldPosition = "";

        if (opModeIsActive()) {
            if (tfod != null) {
                tfod.activate();
            }

            while (opModeIsActive()) {
                if (tfod != null) {
                    // getUpdatedRecognitions() will return null if no new information is available since
                    // the last time that call was made.
                    List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
                    if (updatedRecognitions != null) {
                        telemetry.addData("# Object Detected", updatedRecognitions.size());
                        if (updatedRecognitions.size() == 3) {
                            int goldMineralX = -1;
                            int silverMineral1X = -1;
                            int silverMineral2X = -1;

                            //gets x positions for each mineral detected
                            for (Recognition recognition : updatedRecognitions) {
                                if (recognition.getLabel().equals(LABEL_GOLD_MINERAL)) {
                                    goldMineralX = (int) recognition.getLeft();
                                } else if (silverMineral1X == -1) {
                                    silverMineral1X = (int) recognition.getLeft();
                                } else {
                                    silverMineral2X = (int) recognition.getLeft();
                                }
                            }

                            //determines position of gold mineral
                            if (goldMineralX != -1 && silverMineral1X != -1 && silverMineral2X != -1) {
                                if (goldMineralX < silverMineral1X && goldMineralX < silverMineral2X) {
                                    telemetry.addData("Gold Mineral Position", "Left");
                                    //added:
                                    goldPosition = "Left";

                                } else if (goldMineralX > silverMineral1X && goldMineralX > silverMineral2X) {
                                    telemetry.addData("Gold Mineral Position", "Right");
                                    //added:
                                    goldPosition = "Right";
                                } else {
                                    telemetry.addData("Gold Mineral Position", "Center");
                                    //added:
                                    goldPosition = "Center";
                                }
                            }
                        }
                        telemetry.update();
                    }
                }
            }
        }

        if (tfod != null) {
            tfod.shutdown();
        }

        //added:
        return goldPosition;
    }
}

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;

import java.util.List;
import java.util.Locale;

/**
 * Created by keert on 10/26/2018.
 */

public class VirusMethods extends VirusHardware {

    //motors
    private final DcMotor[] leftDriveMotors = {lmotor0, lmotor1};
    private final DcMotor[] rightDriveMotors = {rmotor0, rmotor1};
    private DcMotor[] driveMotors = new DcMotor[4];

    // motor movement
    private final int driveTicksPerRev = 560;
    private final int driveSprocket = 24;
    private final int wheelSprocket = 22;
    private final int wheelDiameterIn = 4;

    double stickAngle;
    double steerSpeed;
    double stickX;
    double stickY;
    int direction = 0;

    // slides, full = 7300
    int slideMax = 6900;

    int hingeMin = 0;
    private int encodersMovedSpeed;

    enum intakeState {
        retracted, standby, crater, lander
    }
    int intakeStep = 0;
    intakeState intakeState;

    //intake
    enum intake {
        Ball, Cube, None
    }
    intake slot1;
    intake slot2;
    intake[] intakeSlots = {slot1, slot2};
    boolean intakePivotUp;
    boolean usingOutrigger = false;
    // simple conversion
    private static final float mmPerInch = 25.4f;

    //    ColorSensor[] colorSensors = {colorSensor1, colorSensor2};

    // turn
    public static final int RIGHT = 1;
    public static final int LEFT = -1;

    //tensor flow and vuforia stuff
    private static final String TFOD_MODEL_ASSET = "RoverRuckus.tflite";
    private static final String LABEL_GOLD_MINERAL = "Gold Mineral";
    private static final String LABEL_SILVER_MINERAL = "Silver Mineral";
    private static final String VUFORIA_KEY = "AQmuIUP/////AAAAGR6dNDzwEU07h7tcmZJ6YVoz5iaF8njoWsXQT5HnCiI/oFwiFmt4HHTLtLcEhHCU5ynokJgYSvbI32dfC2rOvqmw81MMzknAwxKxMitf8moiK62jdqxNGADODm/SUvu5a5XrAnzc7seCtD2/d5bAIv1ZuseHcK+oInFHZTi+3BvhbUyYNvnVb0tQEAv8oimzjiQW18dSUcEcB/d6QNGDvaDHpxuRCJXt8U3ShJfBWWQEex0Vp6rrb011z8KxU+dRMvGjaIy+P2p5GbWXGJn/yJS9oxuwDn3zU6kcQoAwI7mUgAw5zBGxxM+P35DoDqiOja6ST6HzDszHxClBm2dvTRP7C4DEj0gPkhX3LtBgdolt";
    private VuforiaLocalizer vuforia; //Vuforia localization engine
    private TFObjectDetector tfod; //Tensor Flow Object Detection engine
    private int cameraMonitorViewId;

    private VuforiaTrackables targetsRoverRuckus;
    private VuforiaLocalizer.Parameters parameters;
    private List<VuforiaTrackable> allTrackables;

    protected int knockAngle = 30;
    protected double turnSpeed = 0.6;
    protected float moveSpeed = 0.6f;
    protected boolean haveGold = false;
    protected int sideDist = 24;
    protected int centerDist = 20;
    protected double lookTime = 1.5;

    //distance calculation
    @Override
    public void runOpMode() throws InterruptedException {
        super.runOpMode();
        intakePivotUp = (pivot1.getPosition() != 0);
        driveMotors[0] = lmotor0;
        driveMotors[1] = lmotor1;
        driveMotors[2] = rmotor0;
        driveMotors[3] = rmotor1;
         initializeIMU();
        initialHeading = orientation.firstAngle;
        initialRoll = orientation.secondAngle;
        initialPitch = orientation.thirdAngle;
        encodersMovedSpeed = 0;
        encodersMovedSpeed = 0;
        intakeState = intakeState.retracted;
        //all servo starting positions go here
//        marker.setPosition(0);
        intakePivot(false, true);
//        sifter.setPosition(0); //ball mode
        outrigger.setPosition(1);
        //need to run initVuforia and initTfod
    }

    /* -------------- Initialization -------------- */

    private void initVuforia() {
        //create parameter object and pass it to create Vuforia engine
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();
        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.FRONT;
        vuforia = ClassFactory.getInstance().createVuforia(parameters);
    }

        private void initTfod() {
        //create parameter object and pass it to create Tensor Flow object detector
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_GOLD_MINERAL, LABEL_SILVER_MINERAL);
    }
    public void initVision() {
        initVuforia();
        if (ClassFactory.getInstance().canCreateTFObjectDetector()) {
            initTfod();
        } else {
            telemetry.addData("Sorry!", "This device is not compatible with TFOD");
        }
        //navTargetInit();
        //wait for game to start
        telemetry.addData(">", "Press Play to start tracking");
        telemetry.update();
    }

    public void initializeIMU() {
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();

        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;

        parameters.mode = BNO055IMU.SensorMode.IMU;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "AdafruitIMUCalibration.json"; // see the calibration sample opmode
        parameters.loggingEnabled = true;
        parameters.loggingTag = "IMU";
        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();

        imu.initialize(parameters);
        while (opModeIsActive() && !imu.isGyroCalibrated()) ;
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            telemetry.addData("oof", "oof");
            telemetry.update();
        }
        updateOrientation();
        initialHeading = orientation.firstAngle;
        initialRoll = orientation.secondAngle;
        initialPitch = orientation.thirdAngle;

    }

    /* -------------- Status Methods -------------- */

    public boolean motorsBusy() {
        boolean busy = false;
        for (int i = 0; i < 4; i++) {
            if (driveMotors[i].isBusy()) {
                busy = true;
            }
        }
        return busy;
    }

    public double getRawZ() {
        updateOrientation();
        return orientation.firstAngle;
    }

    public double getRawY() {
        updateOrientation();
        return orientation.thirdAngle;
    }

    public double getRawX() {
        updateOrientation();
        return orientation.secondAngle;
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

    String formatAngle(AngleUnit angleUnit, double angle) {

        return formatDegrees(AngleUnit.DEGREES.fromUnit(angleUnit, angle));
    }

    String formatDegrees(double degrees) {

        return String.format(Locale.getDefault(), "%.1f", AngleUnit.DEGREES.normalize(degrees));
    }

    public double getHeading() {


        return Double.parseDouble(formatAngle(orientation.angleUnit, orientation.firstAngle));
    }
    /* -------------- Processing -------------- */

    public double getAngleDist(double targetAngle, double currentAngle) {

        double angleDifference = currentAngle - targetAngle;
        if (Math.abs(angleDifference) > 180) {
            angleDifference = 360 - Math.abs(angleDifference);
        } else {
            angleDifference = Math.abs(angleDifference);
        }

        return angleDifference;
    }

    public int getAngleDir(double targetAngle, double currentAngle) {

        double angleDifference = currentAngle - targetAngle;
        int angleDir = (int) (angleDifference / Math.abs(angleDifference));
        if (Math.abs(angleDifference) > 180) {
            angleDir *= -1;
        }
        return angleDir;
    }

    private int convertInchToEncoder(float dist) {

        float wheelRotations = (float) (dist / (wheelDiameterIn * Math.PI));
        float motorRotations = (22.0f / 24.0f) * (wheelRotations);
        float encoderCounts = (float) (537.6 * motorRotations);
        int position = Math.round(encoderCounts);
        return position;
    }

    public double percentDiff(int num1, int num2) {
        double diff = (((double) num1 - (double) num2) / (((double) num1 + (double) num2) / 2)) * 100;
        return diff;
    }

    public void updateIntakes() {
//        int red1 = colorSensor1.red();
//        int blue1 = colorSensor1.blue();
//        int green1 = colorSensor1.green();
//        int red2 = colorSensor1.red();
//        int blue2 = colorSensor1.blue();
//        int green2 = colorSensor1.green();
//
//        if (percentDiff(red1, blue1) > 70) {
//            slot1 = intake.Cube;
//        } else if ((red1 + green1 + blue1) / 3 > 30) {
//            slot1 = intake.Ball;
//        } else {
//            slot1 = intake.None;
//        }
//        if (percentDiff(red2, blue2) > 70) {
//            slot2 = intake.Cube;
//        } else if ((red2 + green2 + blue2) / 3 > 30) {
//            slot2 = intake.Ball;
//        } else {
//            slot2 = intake.None;
//        }
    }
    /* -------------- Movement -------------- */

    //movement based on speeds
    public void runDriveMotors(float leftSpeed, float rightSpeed) {
        lmotor0.setPower(Range.clip(leftSpeed, -1, 1));
        lmotor1.setPower(Range.clip(leftSpeed, -1, 1));
        rmotor0.setPower(Range.clip(rightSpeed, -1, 1));
        rmotor1.setPower(Range.clip(rightSpeed, -1, 1));
    }

    //sets slide to certain position
    public void slides(int position) {
        slideLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        slideRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        position = Range.clip(position, 0, slideMax);
        slideLeft.setTargetPosition(position);
        slideRight.setTargetPosition(position);
        slideLeft.setPower(0.8);
        slideRight.setPower(0.8);
        while (Math.abs(slideLeft.getCurrentPosition()-position) > 50);
        slideLeft.setPower(0);
        slideRight.setPower(0);
//        while(slideLeft.isBusy() || slideRight.isBusy());
    }
    public boolean slidesSimul(int position){
        slideLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        slideRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        position = Range.clip(position, 0, slideMax);
        slideLeft.setTargetPosition(position);
        slideRight.setTargetPosition(position);
        slideLeft.setPower(0.7);
        slideRight.setPower(0.7);
        int posDifference = Math.abs(slideLeft.getCurrentPosition()-position);
        if (posDifference < 50) {
            slideLeft.setPower(0);
            slideRight.setPower(0);
            return true;
        }
        return false;
    }
    //sets slide to specified INCH position
//    public void slidesINCH(double inch) {
//        int position = (int) (inch * (double) slideMax / 36.0);
//        slides(position);
//    }

    //set slide power
    public void slidePower(double power) {
        slideLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        slideRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        //if at max extension, only move if retracting
        if (slideRight.getCurrentPosition() <= 0) {
            if (power > 0) {
                slideLeft.setPower(power);
                slideRight.setPower(power);
            } else {
                slideLeft.setPower(0);
                slideRight.setPower(0);
            }
        }
//        if (slideLeft.getCurrentPosition() <= 0 || slideRight.getCurrentPosition() <= 0) {
//            if (power > 0) {
//                slideLeft.setPower(power);
//                slideRight.setPower(power);
//            } else {
//                slideLeft.setPower(0);
//                slideRight.setPower(0);
//            }
//        }
        //if completely retracted, only move if extending
        else if (slideRight.getCurrentPosition() >= slideMax) {
            if (power < 0) {
                slideLeft.setPower(power);
                slideRight.setPower(power);
            } else {
                slideLeft.setPower(0);
                slideRight.setPower(0);
            }
        }
//        else if (slideLeft.getCurrentPosition() >= slideMax || slideRight.getCurrentPosition() >= slideMax) {
//            if (power < 0) {
//                slideLeft.setPower(power);
//                slideRight.setPower(power);
//            } else {
//                slideLeft.setPower(0);
//                slideRight.setPower(0);
//            }
//        }
        //if in between, move however
        else {
            slideLeft.setPower(power);
            slideRight.setPower(power);
        }
    }

    //get hinge position
    public int hingeAngle() {
        return (int) ((hinge.getCurrentPosition() * 90) / 3700);
    }

    //set hinge position
    public void hinge(double angle) {
//        slideLock.setPosition(0);
        hinge.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        angle = Range.clip(angle, 0, 90);
        int position = (int) (angle * (3700 / 90));
        if (hinge.getCurrentPosition() < position){
            outrigger.setPosition(0);
        }
        hinge.setTargetPosition(position);
        hinge.setPower(1);
        while (Math.abs(hinge.getCurrentPosition()-position) > 50);
        hinge.setPower(0);
//        while (hinge.isBusy());
        outrigger.setPosition(1);
    }
    public boolean hingeSimul(double angle){
        hinge.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        angle = Range.clip(angle, 0, 90);
        int position = (int) (angle * (3700 / 90));
        if (hinge.getCurrentPosition() < position){
            outrigger.setPosition(0);
        }
        hinge.setTargetPosition(position);
        hinge.setPower(1);

        double posDifference = Math.abs(hinge.getCurrentPosition()-position);
        if (posDifference < 50) {
            hinge.setPower(0);
            outrigger.setPosition(1);
            return true;
        }
        return false;
    }

    //set hinge power
    public void hingePower(double power) {
        if (!usingOutrigger) {
            if (power > 0){
                outrigger.setPosition(0);
            }else {
                outrigger.setPosition(1);
            }
        }
        if (power != 0){
            if (hingeAngle() > 80){
                intakePivot(true, false);
            } else {
                intakePivot(false, true);
            }
        }
        hinge.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        //if at 90 degrees, only move if decreasing angle
        if (hinge.getCurrentPosition() >= 3700) {
            if (power < 0) {
                hinge.setPower(power);
            } else {
                hinge.setPower(0);
            }
        }
        //if at 0 degrees, only move if increasing angle
        else if (hinge.getCurrentPosition() <= hingeMin) {
            if (power > 0) {
                hinge.setPower(power);
            } else {
                hinge.setPower(0);
            }
        }
        //if in between 0 and 90 degrees, move however
        else {
            hinge.setPower(power);
        }
    }

    //if true, intake pivots up, if false, then pivots down
    public void intakePivot(boolean up, boolean adjust) {
        lmotor0.setPower(0);
        lmotor1.setPower(0);
        rmotor0.setPower(0);
        rmotor1.setPower(0);
        if (intakePivotUp != up) {
            double originalHinge = hingeAngle();
            if (originalHinge < 10 && adjust) {
                hinge(10);
                telemetry.addData("Hinge", "Moving Up");
            }
            if (up) {
                pivot1.setPosition(.65);
                pivot2.setPosition(.35);
                intakePivotUp = true;
            } else {
                pivot1.setPosition(0);
                pivot2.setPosition(1);
                intakePivotUp = false;
            }
            if (originalHinge < 10 && adjust) {
                hinge(originalHinge);
            }
        }
    }

    public void intakePivotSimul(boolean up) {
        if (intakePivotUp != up) {
            double originalHinge = hingeAngle();
            if (originalHinge < 10) {
                if (hinge.getCurrentPosition() < 10) {
                    hingePower(1);
                } else {
                    hingePower(0);
                }
                telemetry.addData("Hinge", "Moving Up");
            }
            if (up) {
                pivot1.setPosition(.65);
                pivot2.setPosition(.35);
                intakePivotUp = true;
            } else {
                pivot1.setPosition(0);
                pivot2.setPosition(1);
                intakePivotUp = false;
            }
            if (originalHinge < 10) {
                hinge(originalHinge);
            }
        }
    }

    //drop marker
    public void dropMarker() {
//        marker.setPosition(0.65);
//        waitTime(500);
//        marker.setPosition(.35);
//        marker.setPosition(0.65);
//        waitTime(500);
//        marker.setPosition(.35);
//        marker.setPosition(0.65);
//        waitTime(500);
//        marker.setPosition(0);
    }

    public void dehang(float distance) {
        //get on ground
        intakePivot(true,false);
        hinge(90);
        slides(3500);
        //move forward and retract slides
        move(distance,0.4f);
        slides(0);
    }

    public void intoCrater() {
        sweeperVex.setPower(-1);
        outrigger.setPosition(1);
        slides(0);
        hinge(30);
        intakePivot(true, true);
        slides(3000);
        intakePivot(false, true);
        hinge(0);

        intakeState = intakeState.crater;
    }
    public void intoCraterSimul() {
        if(intakeState != intakeState.crater){
            outrigger.setPosition(1);
            switch (intakeStep) {
                case 0:
                    if (slidesSimul(0)){
                        intakeStep++;
                    }
                    break;
                case 1:
                    if (hingeSimul(30)){
                        intakePivot(true, true);
                        intakeStep++;
                    }
                    break;
                case 2:
                    if (slidesSimul(3000)){
                        intakePivot(false, true);
                        intakeStep++;
                    }
                    break;
                case 3:
                    if (hingeSimul(0)){
                        intakeState = intakeState.crater;
                        intakeStep = 0;
                    }
                    break;
            }
        }
    }

    public void retract() {
        outrigger.setPosition(1);
        sweeperVex.setPower(0);
        if (intakeState == intakeState.crater) {
            hinge(30);
            intakePivot(true, true);
        }
        slides(0);
        intakePivot(false, true);
        hinge(0);
        intakeState = intakeState.retracted;
    }
    public void retractSimul(){
        if(intakeState != intakeState.retracted){
            outrigger.setPosition(1);
            sweeperVex.setPower(0);
            switch (intakeStep) {
                case 0:
                    if (intakeState == intakeState.crater){
                        if (hingeSimul(30)){
                            intakePivot(true, true);
                            intakeStep++;
                        }
                    }else{
                        intakeStep++;
                    }
                    break;
                case 1:
                    if (slidesSimul(0)){
                        intakePivot(false, true);
                        intakeStep++;
                    }
                    break;
                case 2:
                    if (hingeSimul(0)){
                        intakeState = intakeState.retracted;
                        intakeStep = 0;
                    }
                    break;
            }
        }
    }
    public void score() {
        if (intakeState == intakeState.crater) {
            hinge(30);
            slides(0);
        }
        outrigger.setPosition(0);
        hinge(90);
        intakePivot(true, true);
        slides(3500);
        sweeperVex.setPower(0.4);
        intakeState = intakeState.lander;
    }
    public void scoreSimul(){
        if(intakeState != intakeState.lander){
            switch (intakeStep) {
                case 0:
                    if (intakeState == intakeState.crater){
                        if (hingeSimul(30)){
                            intakeStep++;
                        }
                    }else{
                        intakeStep+=2;
                    }
                    break;
                case 1:
                    if (slidesSimul(0)){
                        outrigger.setPosition(0);
                        intakeStep++;
                    }
                    break;
                case 2:
                    if (hingeSimul(90)){
                        intakePivot(true, true);
                        intakeStep++;
                    }
                    break;
                case 3:
                    if (slidesSimul(3500)){
                        intakeState = intakeState.lander;
                        intakeStep = 0;
                    }
                    break;
            }
        }
    }
    public void standby() {
        hinge(30);
        slides(0);
        intakeState = intakeState.standby;
    }
    public void standbySimul(){
        if(intakeState != intakeState.standby){
            switch (intakeStep) {
                case 0:
                    if (hingeSimul(30)){
                        intakeStep++;
                    }
                    break;
                case 1:
                    if (slidesSimul(0)){
                        intakeState = intakeState.standby;
                        intakeStep = 0;
                    }
                    break;
            }
        }
    }
    //currently in inches
    public void move(float distance) {
        //converting from linear distance -> wheel rotations ->
        // motor rotations -> encoder counts, then round
        int position = convertInchToEncoder(distance);

        for (DcMotor motor : driveMotors) {
            motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            motor.setTargetPosition(position);
        }
        waitForMotors();
        for (DcMotor motor : driveMotors) {
            motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
    }

    public void move(float distance, float speed) {
        //converting from linear distance -> wheel rotations ->
        // motor rotations -> encoder counts, then round
//        for (int i = 0; i < driveMotors.length; i++) {
//            driveMotors[i].setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        }
        lmotor0.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        lmotor1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rmotor0.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rmotor1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        waitForMotors();

        int position = convertInchToEncoder(distance);

//        for (int i = 0; i < driveMotors.length; i++) {
//            driveMotors[i].setMode(DcMotor.RunMode.RUN_TO_POSITION);
//            driveMotors[i].setTargetPosition(-position);
//            driveMotors[i].setPower(speed);
//        }
        lmotor0.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        lmotor1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rmotor0.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rmotor1.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        lmotor0.setTargetPosition(-position);
        lmotor1.setTargetPosition(-position);
        rmotor0.setTargetPosition(-position);
        rmotor1.setTargetPosition(-position);

        lmotor0.setPower(speed);
        lmotor1.setPower(speed);
        rmotor0.setPower(speed);
        rmotor1.setPower(speed);

        waitForMotors();
        runDriveMotors(0, 0);
//        for (int i = 0; i < driveMotors.length; i++) {
//            driveMotors[i].setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        }
        lmotor0.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        lmotor1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rmotor0.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rmotor1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }


    public void turnRelative(double targetChange, double speed) {

        turnAbsolute(AngleUnit.normalizeDegrees(getRotationinDimension('Z') + targetChange), speed);
    }

    public void turnAbsolute(double targetAngle, double speed) {
//        double currentAngle = getHeading();
//
//        int direction;
//        if (targetAngle != currentAngle) {
//
//            double angleDifference = getAngleDist(targetAngle, currentAngle);
//            direction = getAngleDir(targetAngle, currentAngle);
//            double turnRate = (angleDifference * speed) / 90;
//
//            while (opModeIsActive() && angleDifference > 5) {
//                runDriveMotors((float) -(turnRate * direction), (float) (turnRate * direction));
//                angleDifference = getAngleDist(targetAngle, getRotationinDimension('Z'));
//                direction = getAngleDir(targetAngle, getRotationinDimension(('Z')));
//                turnRate = (angleDifference * speed) / 90;
//                telemetry.addData("Current Angle", getRotationinDimension(('Z')));
//                telemetry.update();
//            }
//
//            runDriveMotors(0, 0);
//        }
        double currentAngle = getHeading();
        int direction;
        double turnRate = 0;
        double P = 1d / 60d;
        double minSpeed = 0;
        double maxSpeed = 0.2d;
        double tolerance = 0.5;

        double error = getAngleDist(targetAngle, currentAngle);
        while (opModeIsActive() && error > tolerance) {
            currentAngle = getRotationinDimension('Z');
            direction = getAngleDir(targetAngle, currentAngle);
            error = getAngleDist(targetAngle, currentAngle);
            turnRate = Range.clip(P * error, minSpeed, maxSpeed);
            runDriveMotors((float) -(turnRate * direction), (float) (turnRate * direction));
            /*telemetry.addData("error: ", error);
            telemetry.addData("currentAngle: ", getRotationinDimension('Z'));
            telemetry.update();*/
        }
        runDriveMotors(0, 0);
    }

    /* -------------- Procedure -------------- */

    public void waitForMotors() {
        while (opModeIsActive() && motorsBusy()) {
        }
    }

    public void waitTime(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void updateOrientation() {
        orientation = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
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

            if (tfod != null) {
                // getUpdatedRecognitions() will return null if no new information is available since
                // the last time that call was made.
                List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
                if (updatedRecognitions != null) {
                    telemetry.addData("# Object Detected", updatedRecognitions.size());

                    if (updatedRecognitions.size() == 2) {
                        int goldMineralX = -1;
                        int goldMineralY = -1;
                        int silverMineral1X = -1;
                        int silverMineral1Y = -1;
                        int silverMineral2X = -1;
                        int silverMineral2Y = -1;
                        telemetry.addData("Point",3);
                        telemetry.update();
                        //gets x positions for each mineral detected
                        for (Recognition recognition : updatedRecognitions) {
                            if (recognition.getLabel().equals(LABEL_GOLD_MINERAL)) {
                                goldMineralX = (int) recognition.getLeft();
                                goldMineralY = (int) recognition.getTop();
                            } else if (silverMineral1X == -1) {
                                silverMineral1X = (int) recognition.getLeft();
                                silverMineral1Y = (int) recognition.getTop();
                            } else {
                                silverMineral2X = (int) recognition.getLeft();
                                silverMineral2Y = (int)recognition.getTop();
                            }
                        }
                        telemetry.addData("G Mineral Position X", goldMineralX);
                        telemetry.addData("G Mineral Position Y", goldMineralY);
                        telemetry.addData("S1 Mineral Position X", silverMineral1X);
                        telemetry.addData("S1 Mineral Position Y", silverMineral1Y);
                        telemetry.addData("S2 Mineral Position X", silverMineral2X);
                        telemetry.addData("S2 Mineral Position Y", silverMineral2Y);
                        //determines position of gold mineral
                        if (goldMineralX == -1) {
                            goldPosition = "Right";
                        } else if (goldMineralX > silverMineral1X) {
                            goldPosition = "Center";
                        } else if (goldMineralX < silverMineral1X) {
                            goldPosition = "Left";
                        }
                    }
                    telemetry.update();
                }
            }
        }
        //added:
        return goldPosition;
    }
    public boolean isGold(){
        boolean gold = false;
        if (opModeIsActive()) {
            if (tfod != null) {
                tfod.activate();
            }

            if (tfod != null) {
                // getUpdatedRecognitions() will return null if no new information is available since
                // the last time that call was made.
                List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
                if (updatedRecognitions != null) {
                    telemetry.addData("# Object Detected", updatedRecognitions.size());

                    if (updatedRecognitions.size() > 0) {
                        int goldMineralX = -1;
                        int goldMineralY = -1;
                        int silverMineral1X = -1;
                        int silverMineral1Y = -1;
                        int silverMineral2X = -1;
                        int silverMineral2Y = -1;
                        telemetry.addData("Point",3);
                        telemetry.update();
                        //gets x positions for each mineral detected
                        for (Recognition recognition : updatedRecognitions) {
                            if (recognition.getLabel().equals(LABEL_GOLD_MINERAL)) {
                                goldMineralX = (int) recognition.getLeft();
                                goldMineralY = (int) recognition.getTop();
                            } else if (silverMineral1X == -1) {
                                silverMineral1X = (int) recognition.getLeft();
                                silverMineral1Y = (int) recognition.getTop();
                            } else {
                                silverMineral2X = (int) recognition.getLeft();
                                silverMineral2Y = (int)recognition.getTop();
                            }
                        }
                        telemetry.addData("G Mineral Position X", goldMineralX);
                        telemetry.addData("G Mineral Position Y", goldMineralY);
                        //determines position of gold mineral
                        if (goldMineralX > 625 && goldMineralX < 825){
                            gold = true;
                        }
                    }
                    telemetry.update();
                }
            }
        }
        //added:
        return gold;
    }

    public void closeTfod() {
        if (tfod != null) {
            tfod.shutdown();
        }
    }

    public void holdHang(){
        slides(0);
        hinge(0);
        intakePivot(false, false);
        initVision();
        waitForStart();
    }

    //knocks gold with movement
    public void moveKnockGold(){
        //figure out gold position
        //look at left mineral
        turnAbsolute(knockAngle, turnSpeed);
        ElapsedTime timer = new ElapsedTime();
        telemetry.addData("Timer", timer.seconds());
        timer.reset();
        while (opModeIsActive() && timer.seconds() < lookTime && !haveGold) {
            telemetry.addData("Timer", timer.seconds());
            haveGold = isGold();
            telemetry.addData("Gold?", haveGold);
            telemetry.update();
        }
        if (haveGold) {
            //knock left mineral
            hinge(0);
            intakePivot(false, true);
            move(sideDist, moveSpeed);
            move(-sideDist, moveSpeed);
        } else {
            //look at center mineral
            turnAbsolute(0, turnSpeed);
            timer.reset();
            while (opModeIsActive() && timer.seconds() < lookTime && !haveGold) {
                telemetry.addData("Timer", timer.seconds());
                haveGold = isGold();
                telemetry.addData("Gold?", haveGold);
                telemetry.update();
            }
            if (haveGold) {
                //knock center mineral
                hinge(0);
                intakePivot(false, true);
                move(centerDist, moveSpeed);
                move(-centerDist, moveSpeed);
            } else {
                //turn to right mineral and knock
                turnAbsolute(-knockAngle, turnSpeed);
                hinge(0);
                intakePivot(false, true);
                move(sideDist, moveSpeed);
                move(-sideDist, moveSpeed);
            }
        }
        closeTfod();
    }

    //knocks gold with slides
    public void knockGold(){
        //figure out gold position
        //look at left mineral
        turnAbsolute(knockAngle, turnSpeed);
        ElapsedTime timer = new ElapsedTime();
        telemetry.addData("Timer", timer.seconds());
        timer.reset();
        while (opModeIsActive() && timer.seconds() < lookTime && !haveGold) {
            telemetry.addData("Timer", timer.seconds());
            haveGold = isGold();
            telemetry.addData("Gold?", haveGold);
            telemetry.update();
        }
        if (haveGold) {
            //knock left mineral
            hinge(0);
            intakePivot(false, true);
            slides(5000);
        } else {
            //look at center mineral
            turnAbsolute(0, turnSpeed);
            timer.reset();
            while (opModeIsActive() && timer.seconds() < lookTime && !haveGold) {
                telemetry.addData("Timer", timer.seconds());
                haveGold = isGold();
                telemetry.addData("Gold?", haveGold);
                telemetry.update();
            }
            if (haveGold) {
                //knock center mineral
                hinge(0);
                intakePivot(false, true);
                slides(5000);
            } else {
                //turn to right mineral and knock
                turnAbsolute(-knockAngle, turnSpeed);
                hinge(0);
                intakePivot(false, true);
                slides(5000);
            }
        }
        closeTfod();
    }

    public void autoExtendIntoCrater(){
        hinge(30);
        turnAbsolute(0, turnSpeed);
        slides(6000);
        hinge(0);
    }
    public void steerSpeed90(){
        stickX = gamepad1.right_stick_x;
        stickY = Range.clip(-gamepad1.right_stick_y,0,1);

        stickAngle = -(Math.toDegrees(Math.acos(stickX/(Math.sqrt(Math.pow(stickX,2)+Math.pow(stickY,2)))))-90);
        if (stickX == 0 && stickY == 0){
            stickAngle = 0;
        }
        steerSpeed = stickAngle/90.0;
    }

    public void steerSpeed(){
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
    }
}
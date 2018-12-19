package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.firstinspires.ftc.robotcore.external.navigation.AngleUnit.DEGREES;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.XYZ;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesReference.EXTRINSIC;

/**
 * Created by keert on 10/26/2018.
 */

public class VirusMethods extends VirusHardware {
    //motors
    private final DcMotor[] leftDriveMotors = {lmotor0, lmotor1};
    private final DcMotor[] rightDriveMotors = {rmotor0, rmotor1};
    private final DcMotor[] driveMotors = {lmotor0, lmotor1, rmotor0, rmotor1};

    // motor movement
    private final int driveTicksPerRev = 560;
    private final int driveSprocket = 24;
    private final int wheelSprocket = 22;
    private final int wheelDiameterIn = 4;

    // slides
    private int encodersMovedStronk;
    private int encodersMovedSpeed;
    private double inchesPerEncoderStronk = (Math.PI * 1.5) / 840;
    private double inchesPerEncoderSpeed = (Math.PI * 1.5) / 280;
    private double slideInchPerStrInch = 1.0; // replace w/ actual value
    enum intakeState {retracted, crater, lander}
    intakeState intakeState;

    //intake
    enum intake {Ball, Cube, None}
    intake slot1;
    intake slot2;
    intake[] intakeSlots = {slot1, slot2};
    ColorSensor[] colorSensors = {colorSensor1, colorSensor2};

    // simple conversion
    private static final float mmPerInch        = 25.4f;
    private static final float mmFTCFieldWidth  = (12*6) * mmPerInch;       // the width of the FTC field (from the center point to the outer panels)
    private static final float mmTargetHeight   = (6) * mmPerInch;

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
    private OpenGLMatrix lastLocation = null;
    private boolean targetVisible = false;
    private List<VuforiaTrackable> allTrackables;

    //distance calculation
    @Override
    public void runOpMode() throws InterruptedException {
        super.runOpMode();
        initializeIMU();
        Thread.sleep(1000);
        updateOrientation();
        initialHeading = orientation.firstAngle;
        initialRoll = orientation.secondAngle;
        initialPitch = orientation.thirdAngle;
        encodersMovedSpeed = 0;
        encodersMovedSpeed = 0;
        intakeState = intakeState.retracted;
        //all servo starting positions go here
        marker.setPosition(0);
        intakePivot(false);
        sifter.setPosition(0); //ball mode
        outrigger.setPosition(0);
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

    private void navTargetInit() {

        cameraMonitorViewId =  hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        targetsRoverRuckus = this.vuforia.loadTrackablesFromAsset("RoverRuckus");
        parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);
        parameters.useExtendedTracking = true;
        allTrackables = new ArrayList<VuforiaTrackable>();
        allTrackables.addAll(targetsRoverRuckus);
        targetsRoverRuckus.activate();
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

    public void initializeIMU() {
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit            = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit            = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.loggingEnabled       = true;
        parameters.useExternalCrystal   = true;
        parameters.mode                 = BNO055IMU.SensorMode.IMU;
        parameters.loggingTag           = "IMU";
        imu = hardwareMap.get(BNO055IMU.class, "imu");
        parameters.calibrationDataFile = "AdafruitIMUCalibration.json"; // see the calibration sample opmode
        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();
        imu.initialize(parameters);
        while (opModeIsActive()&&!imu.isGyroCalibrated());
        updateOrientation();
        initialHeading = orientation.firstAngle;
        initialRoll = orientation.secondAngle;
        initialPitch = orientation.thirdAngle;
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

    public boolean motorsBusy() {
        boolean busy = false;
        for (DcMotor motor : driveMotors) {
            if (motor.isBusy()) {
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

    public OpenGLMatrix getRotation() {
        updateOrientation();
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

    public int getHingeAngle() {

        return hinge.getCurrentPosition() * (2240 / 90);
    }

    public double getSlideExtendInch() {

        double strInches = (encodersMovedSpeed * inchesPerEncoderSpeed) + (encodersMovedStronk * inchesPerEncoderStronk);

        return (strInches * slideInchPerStrInch);
    }
    String formatAngle(AngleUnit angleUnit, double angle)
    {
        return formatDegrees(AngleUnit.DEGREES.fromUnit(angleUnit, angle));
    }

    String formatDegrees(double degrees)
    {
        return String.format(Locale.getDefault(), "%.1f", AngleUnit.DEGREES.normalize(degrees));
    }
    public double getHeading(){
        return Double.parseDouble(formatAngle(angles.angleUnit, angles.firstAngle));
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

        float wheelRotations = (float)(dist / (wheelDiameterIn * Math.PI));
        float motorRotations = (22/24) * (wheelRotations);
        float encoderCounts = (float) (537.6 * motorRotations);
        int position = Math.round(encoderCounts);

        return position;
    }

    private double convertEncoderToInch(int encoders) {

        float motorRotations = (float) (encoders / 537.6);
        float wheelRotations = motorRotations * (24/22);
        double distance = wheelRotations * (wheelDiameterIn * Math.PI);

        return distance;
    }
    public double percentDiff(int num1, int num2) {
        double diff = (((double)num1-(double)num2)/(((double)num1+(double)num2)/2))*100;
        return diff;
    }
    public void updateIntakes(){
        if (percentDiff(colorSensor1.red(),colorSensor1.blue()) > 70){
            slot1 = intake.Cube;
        }else if ((colorSensor1.red()+colorSensor1.green()+colorSensor1.blue())/3 < 30){
            slot1 = intake.None;
        }else{
            slot1 = intake.Ball;
        }
        if (percentDiff(colorSensor2.red(),colorSensor2.blue()) > 70){
            slot2 = intake.Cube;
        }else if ((colorSensor2.red()+colorSensor2.green()+colorSensor2.blue())/3 < 30){
            slot2 = intake.None;
        }else{
            slot2 = intake.Ball;
        }
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
        position = Range.clip(position, 0, 7300);
        slideLeft.setTargetPosition(position);
        slideRight.setTargetPosition(position);
        slideLeft.setPower(0.7);
        slideRight.setPower(0.7);
    }

    //set slide power
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
    //get hinge position
    public int hingeAngle(){
        return (int) ((hinge.getCurrentPosition()*90)/1124);
    }
    //set hinge position
    public void hinge(double angle) {
        slideLock.setPosition(0);
        hinge.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        angle = Range.clip(angle, 0, 90);
        int position = (int)(angle * (1124/90));
        hinge.setTargetPosition(position);
        hinge.setPower(1);
    }
    //set hinge power
    public void hingePower(double power){
        if (power > 0){
            slideLock.setPosition(0);
        }
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
    //if true, intake pivots up, if false, then pivots down
    public void intakePivot(boolean up){
        double originalHinge = hingeAngle();
        if (originalHinge < 10){
            hinge(10);
            telemetry.addData("Hinge","Moving Up");
        }
        int originalSlide = slideLeft.getCurrentPosition();
        if (slideLeft.getCurrentPosition()>-50 || slideRight.getCurrentPosition()>-50){
            slides(-50);
        }
        if (up){
            pivot1.setPosition(.5);
            pivot2.setPosition(.5);
        } else {
            pivot1.setPosition(0);
            pivot2.setPosition(1);
        }
        if (originalHinge < 10){
            hinge(originalHinge);
        }
        if (originalSlide > -50) {
            slides(originalSlide);
        }
    }
    //drop marker
    public void dropMarker(){
        marker.setPosition(0.65);
        waitTime(1000);
        marker.setPosition(.35);
        marker.setPosition(0.65);
        waitTime(1000);
        marker.setPosition(.35);
        marker.setPosition(0.65);
        waitTime(1000);
        marker.setPosition(0);
    }
    public void dehang(){
        //get on ground
        slides(3500);
        hinge(90);
        //move forward and retract slides
        move(3,0.3f);
        slides(0);
    }
    public void intoCrater(){
        sweeper.setPower(-1);
        outrigger.setPosition(0);
        if (intakeState == intakeState.retracted){
            hinge(45);
            intakePivot(true);
        }
        if (intakeState == intakeState.lander){
            slides(0);
            hinge(45);
        }
        slides(3000);
        intakePivot(false);
        hinge(0);
        intakeState = intakeState.crater;
    }
    public void retract(){
        outrigger.setPosition(0);
        sweeper.setPower(0);
        if (intakeState == intakeState.crater){
            hinge(45);
            intakePivot(true);
        }
        slides(0);
        intakePivot(false);
        hinge(0);
        intakeState = intakeState.retracted;
    }
    public void score(){
        if (intakeState == intakeState.crater){
            hinge(45);
            slides(0);
        }
        outrigger.setPosition(1);
        hinge(90);
        intakePivot(true);
        slides(3500);
        sweeper.setPower(0.4);
        intakeState = intakeState.lander;
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
        for (int i = 0; i < driveMotors.length; i++) {
            driveMotors[i].setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        }

        waitForMotors();

        int position = convertInchToEncoder(distance);

        for (int i = 0; i < driveMotors.length; i++) {
            driveMotors[i].setMode(DcMotor.RunMode.RUN_TO_POSITION);
            driveMotors[i].setTargetPosition(-position);
            driveMotors[i].setPower(speed);
        }
        waitForMotors();
        runDriveMotors(0, 0);
        for (int i = 0; i < driveMotors.length; i++) {
            driveMotors[i].setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
    }


    public void turnRelative(double targetChange, double speed) {

        turnAbsolute(AngleUnit.normalizeDegrees(getRotationinDimension('Z') + targetChange), speed);
    }

    public void turnAbsolute(double targetAngle, double speed) {
        double currentAngle = getHeading();
        int direction;
        if (targetAngle != currentAngle) {

            double angleDifference = getAngleDist(targetAngle, currentAngle);
            direction = getAngleDir(targetAngle, currentAngle);

            double turnRate = (angleDifference * speed) / 90;

            while (opModeIsActive() && angleDifference > 1) {
                runDriveMotors((float) -(turnRate * direction), (float) (turnRate * direction));
                angleDifference = getAngleDist(targetAngle, getRotationinDimension('Z'));
                direction = getAngleDir(targetAngle, getRotationinDimension(('Z')));
                turnRate = (angleDifference * speed) / 90;
                telemetry.addData("angleDifference: ", angleDifference);
                telemetry.addData("currentAngle: ", getRotationinDimension('Z'));
                telemetry.update();
            }

            runDriveMotors(0, 0);
        }
    }


//    public void turn2(vdouble targetAngle, double speed) {
//        double currentAngle = getRotationinDimension('Z');
//        double angleDifference = currentAngle - targetAngle;
//
//    }

    /* -------------- Procedure -------------- */

    public void waitForMotors() {
        while (opModeIsActive() && motorsBusy()) {
        }
    }

    public void waitTime(int time){
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void updateOrientation (){
        orientation = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZXY, AngleUnit.DEGREES);
    }

    public void ejectTeamMarker() {


    }

    public void slidesBrake() {

        slideLeft.setPower(0);
        slideRight.setPower(0);
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
                        int silverMineral1X = -1;
                        int silverMineral2X = -1;

                        //gets x positions for each mineral detected
                        for (Recognition recognition : updatedRecognitions) {
                            if (recognition.getLabel().equals(LABEL_GOLD_MINERAL)) {
                                goldMineralX = (int) recognition.getBottom();
                            } else if (silverMineral1X == -1) {
                                silverMineral1X = (int) recognition.getBottom();
                            } else {
                                silverMineral2X = (int) recognition.getBottom();
                            }
                        }

                        //determines position of gold mineral
                        if (goldMineralX==-1){
                            goldPosition = "Right";
                        }else if(goldMineralX < silverMineral1X){
                            goldPosition = "Left";
                        }else if(goldMineralX > silverMineral1X){
                            goldPosition = "Center";
                        }
                        telemetry.addData("Gold Position", goldPosition);
                    }
                    telemetry.update();
                }
            }
        }
        //added:
        return goldPosition;
    }
    public void closeTfod(){
        if (tfod != null) {
            tfod.shutdown();
        }
    }
    public void updateNavTargets() {

        // check all the trackable target to see which one (if any) is visible.
        targetVisible = false;
        for (VuforiaTrackable trackable : allTrackables) {
            if (((VuforiaTrackableDefaultListener)trackable.getListener()).isVisible()) {
                telemetry.addData("Visible Target", trackable.getName());
                targetVisible = true;

                // getUpdatedRobotLocation() will return null if no new information is available since
                // the last time that call was made, or if the trackable is not currently visible.
                OpenGLMatrix robotLocationTransform = ((VuforiaTrackableDefaultListener)trackable.getListener()).getUpdatedRobotLocation();
                if (robotLocationTransform != null) {
                    lastLocation = robotLocationTransform;
                }
                break;
            }
        }

        // Provide feedback as to where the robot is located (if we know).
        if (targetVisible) {
            // express position (translation) of robot in inches.
            VectorF translation = lastLocation.getTranslation();
            telemetry.addData("Pos (in)", "{X, Y, Z} = %.1f, %.1f, %.1f",
                    translation.get(0) / mmPerInch, translation.get(1) / mmPerInch, translation.get(2) / mmPerInch);

            // express the rotation of the robot in degrees.
            Orientation rotation = Orientation.getOrientation(lastLocation, EXTRINSIC, XYZ, DEGREES);
            telemetry.addData("Rot (deg)", "{Roll, Pitch, Heading} = %.0f, %.0f, %.0f", rotation.firstAngle, rotation.secondAngle, rotation.thirdAngle);
        } else {
            telemetry.addData("Visible Target", "none");
        }
        telemetry.update();
    }
}
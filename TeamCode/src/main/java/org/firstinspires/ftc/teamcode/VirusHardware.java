package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

// hardware
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.GyroSensor;

// sensors & sensorSetup
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

/**
 * Created by keert on 10/26/2018.
 */

public abstract class VirusHardware extends LinearOpMode {
    DcMotorEx lmotor0;
    DcMotorEx lmotor1;
    DcMotorEx rmotor0;
    DcMotorEx rmotor1;
    DcMotor slideLeft;
    DcMotor slideRight;
    DcMotor hinge;
    //DcMotor sweeper; //
    CRServo sweeperVex;
    Servo  slideLock; //
    Servo pivot1, pivot2;
    Servo marker;
    Servo sifter;
    Servo outrigger;
//    ColorSensor colorSensor1, colorSensor2;

    Orientation orientation = new Orientation(AxesReference.EXTRINSIC, AxesOrder.ZXY, AngleUnit.DEGREES,0,0,0,0);
    BNO055IMU imu;
    Orientation angles;
    Acceleration gravity;
    double initialHeading;
    double initialPitch;
    double initialRoll;

    @Override
    public void runOpMode() throws InterruptedException {
        lmotor0 = (DcMotorEx) hardwareMap.get(DcMotor.class, "lmotor0");
        lmotor1 = (DcMotorEx) hardwareMap.get(DcMotor.class, "lmotor1");
        rmotor0 = (DcMotorEx) hardwareMap.get(DcMotor.class, "rmotor0");
        rmotor1 = (DcMotorEx) hardwareMap.get(DcMotor.class, "rmotor1");
//        lmotor0 = (DcMotorEx)hardwareMap.dcMotor.get("lmotor0");
//        lmotor1 = (DcMotorEx)hardwareMap.dcMotor.get("lmotor1");
//        rmotor0 = (DcMotorEx)hardwareMap.dcMotor.get("rmotor0");
//        rmotor1 = (DcMotorEx)hardwareMap.dcMotor.get("rmotor1");
        hinge = hardwareMap.dcMotor.get("hinge");
        slideLock = hardwareMap.servo.get("slideLock");
        pivot1 = hardwareMap.servo.get("pivot1");
        pivot2 = hardwareMap.servo.get("pivot2");
        marker = hardwareMap.servo.get("marker");

//        //sweeper = hardwareMap.dcMotor.get("sweeper");
        sweeperVex = hardwareMap.crservo.get("sweeperVex");
//        marker = hardwareMap.servo.get("marker");
        sifter = hardwareMap.servo.get("sifter");
        slideLeft = hardwareMap.dcMotor.get("slideLeft");
        slideRight = hardwareMap.dcMotor.get("slideRight");
        outrigger = hardwareMap.servo.get("outrigger");
        imu = hardwareMap.get(BNO055IMU.class, "imu");
//        colorSensor1 = hardwareMap.colorSensor.get("colorSensor1");
//        colorSensor2 = hardwareMap.colorSensor.get("colorSensor2");

        rmotor0.setDirection(DcMotor.Direction.REVERSE);
        rmotor1.setDirection(DcMotor.Direction.REVERSE);
        hinge.setDirection(DcMotor.Direction.REVERSE);

        slideRight.setDirection(DcMotor.Direction.REVERSE);

        lmotor0.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        lmotor1.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        rmotor0.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        rmotor1.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);

        slideLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        slideRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        hinge.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        lmotor0.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        rmotor0.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        lmotor1.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        rmotor1.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);

        hinge.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        slideLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        slideRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        //sweeper.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        initialHeading = orientation.firstAngle;
        initialPitch = orientation.secondAngle;
        initialRoll = orientation.thirdAngle;


    }
}
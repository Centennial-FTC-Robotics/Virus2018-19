package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

// hardware
import com.qualcomm.robotcore.hardware.DcMotor;
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
    DcMotor lmotor0;
    DcMotor lmotor1;
    DcMotor rmotor0;
    DcMotor rmotor1;
    DcMotor slideLeft;
    DcMotor slideRight;
    DcMotor hinge;
    DcMotor sweeper;
    Servo slideLock;
    Servo pivot1, pivot2;
    Servo marker;
    Servo sifter;

    Orientation orientation = new Orientation(AxesReference.EXTRINSIC, AxesOrder.ZXY, AngleUnit.DEGREES,0,0,0,0);
    BNO055IMU imu;
    Orientation angles;
    Acceleration gravity;
    double initialHeading;
    double initialPitch;
    double initialRoll;

    @Override
    public void runOpMode() throws InterruptedException {
        lmotor0 = hardwareMap.dcMotor.get("lmotor0");
        lmotor1 = hardwareMap.dcMotor.get("lmotor1");
        rmotor0 = hardwareMap.dcMotor.get("rmotor0");
        rmotor1 = hardwareMap.dcMotor.get("rmotor1");
        hinge = hardwareMap.dcMotor.get("hinge");
        slideLock = hardwareMap.servo.get("slideLock");
        pivot1 = hardwareMap.servo.get("pivot1");
        pivot2 = hardwareMap.servo.get("pivot2");
        sweeper = hardwareMap.dcMotor.get("sweeper");
        marker = hardwareMap.servo.get("marker");
        sifter = hardwareMap.servo.get("sifter");
        slideLeft = hardwareMap.dcMotor.get("slideLeft");
        slideRight = hardwareMap.dcMotor.get("slideRight");
        imu = hardwareMap.get(BNO055IMU.class, "imu");


        rmotor0.setDirection(DcMotor.Direction.REVERSE);
        rmotor1.setDirection(DcMotor.Direction.REVERSE);

        slideRight.setDirection(DcMotor.Direction.REVERSE);

        lmotor0.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        lmotor1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rmotor0.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rmotor1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        slideLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        slideRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        hinge.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        lmotor0.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rmotor0.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        lmotor1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rmotor1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        hinge.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        slideLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        slideRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        sweeper.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        initialHeading = orientation.firstAngle;
        initialPitch = orientation.secondAngle;
        initialRoll = orientation.thirdAngle;


        waitForStart();
    }
}
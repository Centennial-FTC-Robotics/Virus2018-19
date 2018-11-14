package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by keert on 10/26/2018.
 */

public abstract class VirusHardware extends LinearOpMode {
//    DcMotor lmotor0;
//    DcMotor lmotor1;
//    DcMotor rmotor0;
//    DcMotor rmotor1;
    DcMotor slideLeft;
    DcMotor slideRight;
    DcMotor hinge;
    @Override
    public void runOpMode() throws InterruptedException {
//        lmotor0 = hardwareMap.dcMotor.get("lmotor0");
//        lmotor1 = hardwareMap.dcMotor.get("lmotor1");
//        rmotor0 = hardwareMap.dcMotor.get("rmotor0");
//        rmotor1 = hardwareMap.dcMotor.get("rmotor1");
        hinge = hardwareMap.dcMotor.get("hinge");

        slideLeft = hardwareMap.dcMotor.get("slideLeft");
        slideRight = hardwareMap.dcMotor.get("slideRight");

//        rmotor0.setDirection(DcMotor.Direction.REVERSE);
//        rmotor1.setDirection(DcMotor.Direction.REVERSE);

        slideLeft.setDirection(DcMotor.Direction.REVERSE);

//        lmotor0.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//        lmotor1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//        rmotor0.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//        rmotor1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        slideLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        slideRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        hinge.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

//        lmotor0.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        rmotor0.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        lmotor1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        rmotor1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        hinge.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        slideLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        slideRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


        waitForStart();
    }
}

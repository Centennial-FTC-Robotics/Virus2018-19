package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by keert on 10/26/2018.
 */

public abstract class TrainingHardware extends LinearOpMode {
    DcMotor lmotor0;
    DcMotor rmotor0;

    @Override
    public void runOpMode() throws InterruptedException {
        lmotor0 = hardwareMap.dcMotor.get("lmotor0");
        rmotor0 = hardwareMap.dcMotor.get("rmotor0");

        rmotor0.setDirection(DcMotor.Direction.REVERSE);

        lmotor0.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rmotor0.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        lmotor0.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rmotor0.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        waitForStart();
    }
}
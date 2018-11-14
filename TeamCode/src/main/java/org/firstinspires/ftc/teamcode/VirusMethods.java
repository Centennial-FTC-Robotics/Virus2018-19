package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by keert on 10/26/2018.
 */

public class VirusMethods extends VirusHardware {
    public void slides(int position){
        slideLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        slideRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        slideLeft.setPower(-1);
        slideRight.setPower(-1);
        slideLeft.setTargetPosition(position);
        slideRight.setTargetPosition(position);
        if (!slideLeft.isBusy() || !slideRight.isBusy()) {
            slideLeft.setPower(0);
            slideRight.setPower(0);
        }
    }
    public void slidePower(double power){
        slideLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        slideRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        slideLeft.setPower(power);
        slideRight.setPower(power);
    }
}

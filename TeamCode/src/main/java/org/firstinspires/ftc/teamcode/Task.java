package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.Servo;

public class Task {

    private HardwareDevice motor;

    private double power;
    private double time;

    Task(DcMotor newMotor, double newPower, double newTime) {

        motor = newMotor;
        power = newPower;
        time = newTime;
    }

    Task(Servo newServo, double newPower, double newTime) {

        motor = newServo;
        power = newPower;
        time = newTime;
    }

    public HardwareDevice getMotor() {
        return motor;
    }

    public String motorType() {

        boolean isDcMotor = true;
        try {

            motor = (DcMotor) motor;
        } catch(ClassCastException e) {

            isDcMotor = false;
        }

        String motorType = (isDcMotor) ? "DcMotor":"Servo";
        //motorType = ((DcMotor) motor).getMotorType().toString();
        return motorType;
    }

    public double getPower() {

        return power;
    }

    public double getTime() {

        return time;
    }
}

package org.firstinspires.ftc.teamcode;

public class EndAuto2 extends VirusMethods {

    @Override
    //knocks gold, extends into crater
    public void runOpMode() throws InterruptedException {
        knockGold();
        extendIntoCrater();
    }

    private void extendIntoCrater(){
        hinge(45);
        turnAbsolute(0, turnSpeed);
        hinge(0);
    }
}
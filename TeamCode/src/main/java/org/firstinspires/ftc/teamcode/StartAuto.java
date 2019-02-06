package org.firstinspires.ftc.teamcode;

public class StartAuto extends VirusMethods {

    @Override
    //dehangs, knock gold, go to wall
    public void runOpMode() throws InterruptedException {
        super.runOpMode();
        holdHang();
        dehang();
        initializeIMU();
        moveKnockGold();
        goToWall();
    }

    private void goToWall(){
        //realign self
        turnAbsolute(0, turnSpeed);
        move(-5, moveSpeed);
        //go to wall (REPLACE WITH PROX SENSOR CODE)
        intakePivot(false, true);
        turnAbsolute(45, turnSpeed);
        move(48, moveSpeed);
    }
}

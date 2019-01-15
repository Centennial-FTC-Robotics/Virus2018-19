package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.util.ArrayList;

public class TaskManager {

    private ArrayList<Task> tasks;
    private ArrayList<ElapsedTime> times;

    TaskManager() {
        tasks = new ArrayList<Task>();

    }

    TaskManager(TaskManager taskSet) {
        this();
        tasks.addAll(taskSet.getTasks());

    }

    TaskManager(ArrayList<Task> newTasks) {
        this();
        tasks.addAll(newTasks);

    }

    private void checkTasks() {


    }

    public ArrayList<Task> getTasks() {

        return tasks;
    }
}

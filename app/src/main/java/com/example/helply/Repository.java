package com.example.helply;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

public class Repository {
    private List<Task> tasks = new CopyOnWriteArrayList<>();

    private boolean addTask(Task task) {
        if (task != null) {
            this.tasks.add(task);
            return true;
        }
        return false;
    }

    private boolean removeTask(Task task) {
        if (task != null && this.tasks.contains(task)) {
            this.tasks.remove(task);
            return true;
        }
        return false;
    }

    private boolean removeTask(UUID taskUuid) {
        for (Task t : this.tasks) {
            if (t.getUuid().equals(taskUuid)) {
                this.tasks.remove(t);
                return true;
            }
        }
        return false;
    }

    private boolean updateTask(UUID taskUuid, Task task) {
        for (int i = 0; i < this.tasks.size(); i++) {
            if (tasks.get(i).getUuid().equals(taskUuid)) {
                this.tasks.remove(tasks.get(i));
                this.tasks.add(task);
                return true;
            }
        }
        return false;
    }

    private Task getTask(UUID taskUuid) {
        for (int i = 0; i < this.tasks.size(); i++) {
            if (tasks.get(i).getUuid().equals(taskUuid)) {
                return tasks.get(i);
            }
        }
        return null;
    }


    public void writeAllTasks() {
        System.out.println(Arrays.toString(this.tasks.toArray()));
    }

}

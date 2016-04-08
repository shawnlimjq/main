package application.logic;
//@@author A0132632R

import java.util.ArrayList;

import application.storage.Task;

public class ViewChange implements Command {

    private static final String MESSAGE_LIST = "Here are your tasks.";
    
    @Override
    public Feedback execute(StorageConnector storageConnector, ArrayList<Task> tasksOnScreen) {
        Feedback feedback = new Feedback(MESSAGE_LIST, tasksOnScreen ,null);
        feedback.setViewChangeFlag();
        return feedback;
    }

}

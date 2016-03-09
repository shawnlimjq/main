package application.parser;

import application.storage.Storage;
import application.storage.Task;
import java.io.IOException;
import java.util.ArrayList;

public class DoneByName implements Command {
     private static final int FIRST_INDEX = 0;
    private static final String FEEDBACK_CLOSE = "CLOSE Task: %1$s";
    private static final String MESSAGE_CLOSE_ERROR = "We encountered some "
            + "problem while closing this task. We apologise for the inconvenience.";
    
    
  
    String taskToClose;
    
    DoneByName(String taskToClose){
        this.taskToClose = taskToClose;
        
        
    }
     
    //NEED TO IMPLEMENT THIS
    public String execute(Storage storage){
         try{
            ArrayList<Task> taskList = storage.searchByTask(taskToClose);
            String feedback = takeAction(taskList, storage);
            return feedback;
        }catch(IOException e){
            return MESSAGE_CLOSE_ERROR;
        }
    }
    
    public String takeAction(ArrayList<Task> taskList, Storage storage) throws IOException{
        assert(taskList.size() > 0);
        if (taskList.size() ==  1){
            return closeSingleTask(taskList, storage);
        } else {
            return listTasks(taskList);
        }
    }

    private String closeSingleTask(ArrayList<Task> taskList, Storage storage) throws IOException {
        String feedback = String.format(FEEDBACK_CLOSE, 
                taskList.get(FIRST_INDEX).getTaskDescription());
        storage.closeTaskInList(FIRST_INDEX);
        return feedback;
    }
    
    private String listTasks(ArrayList<Task> taskList){
        String listed = "\nWhich task would you like to close?";
        int i = 1;
        for (Task task: taskList){
            listed = listed + "\n" + i + ") " + task.getTaskDescription();
            i++;
        }
        return listed;
    }
    
    
    
}

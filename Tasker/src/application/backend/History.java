package application.backend;
//@@author A0132632R

import java.util.Stack;

public class History {

    private Stack<UndoableCommand> executedCommands;
        private static History instance;
    
    private History(){
        this.executedCommands = new Stack<UndoableCommand>();
    }
    
    public static History getInstance(){
        if (instance == null){
            instance = new History();
        }
        return instance;
    }
    
    
    
    public void add(Command cmd){
        if(cmd instanceof UndoableCommand){
            UndoableCommand cmd_undoable = (UndoableCommand) cmd;
            executedCommands.add(cmd_undoable);
        }
    }
    
    public Feedback undo(){
        try{
            UndoableCommand cmd = executedCommands.pop();
            Feedback feedback = cmd.undo();
            return feedback;
        } catch (NothingToUndoException e){
            return undo();
        }
    }
    
    
    
}
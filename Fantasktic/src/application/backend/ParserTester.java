package application.backend;
//import application.storage.Storage;

import java.util.ArrayList;

import org.junit.Test;

import application.storage.Task;

public class ParserTester {

        StorageConnector storageConnector = new StorageConnector();
        Parser parser = new Parser();
    
      /*  @Test
    public void deleteTest(){
        try{
            storage.initialise();
            
            ArrayList<Task> tasks = storage.getFileList();
            int numb = tasks.size() + 2;
            Command cmd = parser.interpretCommand("delete " + numb);
            Feedback fb = cmd.execute(storage, tasks);
            assertEquals("Please enter a valid number.",fb.getMessage());
            
            Command cmd2 = parser.interpretCommand("delete 0");
            Feedback fb2 = cmd2.execute(storage, tasks);
            assertEquals("Please enter a valid number.",fb2.getMessage());
            
            Command cmd3 = parser.interpretCommand("delete 1");
            System.out.println(tasks.size());
            String expected = "Deleted Task: " + tasks.get(0).toString();
            Feedback fb3 = cmd3.execute(storage, tasks);
            System.out.println(fb3.getMessage());
            assertEquals(expected,fb3.getMessage());
            
        }catch(Exception e){
            System.out.println("Not working delete");
       }
    } */
        
        @Test
        public void updateTest(){
            try{
                storageConnector.initialise();
                System.out.println("IM HEREE");
                
                ArrayList<Task> tasks = storageConnector.getOpenList();
                Command cmd = parser.interpretCommand("update 2 tennis");
                Feedback fb = cmd.execute(storageConnector, tasks);
                System.out.println(fb.getMessage());
                /*
                Command cmd2 = parser.interpretCommand("delete -1");
                Feedback fb2 = cmd2.execute(storage, tasks);
                assertEquals("Please enter a valid number.",fb2.getMessage());
                
                Command cmd3 = parser.interpretCommand("delete 1");
                System.out.println(tasks.size());
                String expected = "Deleted Task: " + tasks.get(0).toString();
                Feedback fb3 = cmd3.execute(storage, tasks);
                System.out.println(fb3.getMessage());
                assertEquals(expected,fb3.getMessage());
        */
            }catch(Exception e){
                e.printStackTrace(System.out);
            }
        }

}

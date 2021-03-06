//@@author A0125522R

package application.storage;

import static org.junit.Assert.*;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * StorageTest is used to test all task manipulation functions.
 * 
 * TAKE NOTE :
 * --------------
 * Before running all tests, do take note of variables such as CUSTOM_DIRECTORY1 & CUSTOM_DIRECTORY2.
 * - Adjust the designated directory path according to your own local computer.
 * Also to ensure reliability and consistency, remove any data files in the folders you'll be testing with.
 */
public class StorageTest {
	Storage storageController;
    private static final int EMPTY = 1;
	static final String CUSTOM_DIRECTORY1 = "E:\\eclipse\\workspace\\";
	static final String CUSTOM_DIRECTORY2 = "E:\\eclipse\\workspace\\CS2103_Tasker\\";
	static final String FILE_CLOSED_NAME = "FantaskticHistory.txt";
	static final String FILE_DATA_NAME = "FantaskticData.txt";
	static final String FILE_DIRECTORY_NAME = "FantaskticDirectory.txt";
	static final SimpleDateFormat FORMAT_TIME = new SimpleDateFormat("h:mm a");
	static final SimpleDateFormat FORMAT_DATE = new SimpleDateFormat("d MMM yyyy");
	static Calendar cal1 = Calendar.getInstance();
	static Calendar cal2 = Calendar.getInstance();
	static Calendar cal3 = Calendar.getInstance();
	static Calendar time1 = Calendar.getInstance();
	static Calendar noDate = Calendar.getInstance();
	static Calendar noTime = Calendar.getInstance();
	static Calendar noDateTime = Calendar.getInstance();
	
    @Before
	public void before() throws IOException {
		storageController = new Storage();
		if(!storageController.directoryExists())	
			storageController.setDirectory("");
		storageController.initialise();
		cal1.set(2020, Calendar.JUNE, 30);
		cal2.set(2030, Calendar.DECEMBER, 25);
		cal3.set(2040, Calendar.APRIL, 1);
		noDate.set(Calendar.YEAR, EMPTY);
		noTime.set(Calendar.MILLISECOND, EMPTY);
		noTime.set(Calendar.HOUR_OF_DAY, 0);
		noTime.set(Calendar.MINUTE, 0);
		noTime.set(Calendar.SECOND, 0);
		time1.set(Calendar.YEAR, EMPTY);
		time1.set(Calendar.HOUR_OF_DAY, 17);
		time1.set(Calendar.MINUTE, 17);
		noDateTime.set(Calendar.YEAR, EMPTY);
		noDateTime.set(Calendar.MILLISECOND, EMPTY);
		noDateTime.set(Calendar.HOUR_OF_DAY, 0);
		noDateTime.set(Calendar.MINUTE, 0);
		noDateTime.set(Calendar.SECOND, 0);
	}
	
	@Test
	public void checkDirectoryExists() throws IOException {
		assertTrue(storageController.directoryExists());	
	}
	
	@Test
	public void checkInitialise() throws IOException {
		storageController.directoryExists();	
		storageController.setDirectory("");
		assertTrue(storageController.initialise());
	}
	
	@Test
	public void setDirectory() throws IOException {
		storageController.addTaskInList("Go to hell", cal1, noDate, "Doom", noDate, "high");
		storageController.addTaskInList("Do homework", noDate, cal1, "Home", cal2, "low");
		assertEquals(CUSTOM_DIRECTORY2, storageController.setDirectory(CUSTOM_DIRECTORY1));
		assertEquals(CUSTOM_DIRECTORY1, storageController.setDirectory(""));
		assertEquals(CUSTOM_DIRECTORY1, storageController.setDirectory(CUSTOM_DIRECTORY2));
		assertEquals(CUSTOM_DIRECTORY2, storageController.setDirectory(""));
	}

	@Test
	public void addTask() throws IOException {
		storageController.addTaskInList("Go to hell", null, null, "Doom", noDate, "high");
		assertEquals("FloatingTask",storageController.getOpenList().get(0).getClass().getSimpleName());
		storageController.addTaskInList("Do homework", null, cal1, "Home", cal2, "low");
		assertEquals("DeadlineTask",storageController.getOpenList().get(0).getClass().getSimpleName());
		storageController.addTaskInList("Sign up for homework", cal1, cal2, "Home", noDate, "low");
		assertEquals("EventTask",storageController.getOpenList().get(1).getClass().getSimpleName());
		storageController.addTaskInList("Finish CS2103", null, null, "School", noDate, "high");
		assertEquals("FloatingTask",storageController.getOpenList().get(3).getClass().getSimpleName());
	}
	
	@Test
	public void closeTask() throws IOException {
		storageController.addTaskInList("Do homework", null, cal2, "Home", cal2, "low");
		storageController.addTaskInList("Go to hell", cal1, cal2, "Doom", noDate, "high");
		assertEquals("Do homework",storageController.closeTask(1).getTaskDescription());
	}
	
	@Test
	public void deleteTask() throws IOException {
		storageController.addTaskInList("Do homework", null, cal2, "Home", cal2, "low");
		storageController.addTaskInList("Go to hell", cal1, cal2, "Doom", noDate, "high");
		assertEquals("Do homework",storageController.deleteTask(1).getTaskDescription());
	}
	
	@Test
	public void searchName() throws IOException {
		storageController.addTaskInList("Go to hell", null, null, "Doom", noDate, "high");
		storageController.addTaskInList("Do homework", null, cal1, "Home", cal2, "low");
		storageController.addTaskInList("Sign up for homework", cal1, cal2, "Home", noDate, "low");
		storageController.addTaskInList("Finish CS2103", null, null, "School", noDate, "high");
		ArrayList<Task> searchList = storageController.searchTaskByName("home");
		assertEquals(2,searchList.size());
	}
	
	@Test
	public void searchByDate() throws IOException {
		storageController.addTaskInList("Go to hell", null, null, "Doom", noDate, "high");
		storageController.addTaskInList("Do homework", null, cal1, "Home", cal2, "low");
		storageController.addTaskInList("Sign up for homework", cal1, cal2, "Home", noDate, "low");
		storageController.addTaskInList("Finish CS2103", null, null, "School", noDate, "high");
		Calendar searchDate = Calendar.getInstance();
		searchDate.set(2035, Calendar.JUNE, 30);
		ArrayList<Task> searchList = storageController.searchTaskByDate(searchDate);
		assertEquals(2,searchList.size());
	}
	
	@Test
	public void searchPriority() throws IOException {
		storageController.addTaskInList("Go to hell", null, null, "Doom", noDate, "high");
		storageController.addTaskInList("Do homework", null, cal1, "Home", cal2, "low");
		storageController.addTaskInList("Sign up for homework", cal1, cal2, "Home", noDate, "low");
		storageController.addTaskInList("Finish CS2103", null, null, "School", noDate, "high");
		ArrayList<Task> searchList = storageController.searchTaskByPriority("low");	
		assertEquals(2,searchList.size());
	}
	
	@Test
	public void sortName() throws IOException {
		storageController.addTaskInList("Go to hell", null, null, "Doom", noDate, "high");
		storageController.addTaskInList("Do homework", null, cal1, "Home", cal2, "low");
		storageController.addTaskInList("Sign up for homework", cal1, cal2, "Home", noDate, "low");
		storageController.addTaskInList("Finish CS2103", null, null, "School", noDate, "high");
		ArrayList<Task> searchList = storageController.sortByName();
		assertEquals("Go to hell",searchList.get(2).getTaskDescription());
		assertEquals("Finish CS2103",searchList.get(1).getTaskDescription());
	}
	
//	@Test
	public void sortDate() throws IOException {
		storageController.addTaskInList("Go to hell", null, null, "Doom", noDate, "high");
		storageController.addTaskInList("Do homework", null, cal1, "Home", cal2, "low");
		storageController.addTaskInList("Sign up for homework", cal1, cal2, "Home", noDate, "low");
		storageController.addTaskInList("Finish CS2103", null, null, "School", noDate, "high");
		ArrayList<Task> searchList = storageController.sortByDate();
		assertEquals("Do homework",searchList.get(0).getTaskDescription());
		assertEquals("Finish CS2103",searchList.get(3).getTaskDescription());
	}
	
	@Test
	public void sortPriority() throws IOException {
		storageController.addTaskInList("Go to hell", null, null, "Doom", noDate, "high");
		storageController.addTaskInList("Do homework", null, cal1, "Home", cal2, "low");
		storageController.addTaskInList("Sign up for homework", cal1, cal2, "Home", noDate, "medium");
		storageController.addTaskInList("Finish CS2103", null, null, "School", noDate, "high");
		ArrayList<Task> searchList = storageController.sortByPriority();
		assertEquals("Do homework",searchList.get(3).getTaskDescription());
		assertEquals("Finish CS2103",searchList.get(1).getTaskDescription());
	}
	
	@Test
	public void updateTask() throws IOException, CloneNotSupportedException {
		storageController.addTaskInList("Go to hell", null, null, "Doom", noDate, "high");
		storageController.addTaskInList("Do homework", null, cal1, "Home", cal2, "low");
		storageController.addTaskInList("Sign up for homework", cal1, cal2, "Home", noDate, "low");
		storageController.addTaskInList("Finish CS2103", null, null, "School", noDate, "high");
		Calendar newDate = Calendar.getInstance();
		newDate.set(2035, Calendar.JUNE, 30);
		ArrayList<Task> list = new ArrayList<Task>();
		EventTask eventTask = new EventTask("Finish CS2103 and CS3230", cal1, newDate, "Home", noDate, "highest",3);
		list = storageController.updateTask(3, "Finish CS2103 and CS3230", noDate, newDate, "Home", noDate, "highest");
		assertEquals(eventTask.getTaskDescription(), list.get(1).getTaskDescription());
		assertEquals(eventTask.durationToString(), list.get(1).durationToString());
	}	

	@Test
	public void updateTaskWithTime() throws IOException, CloneNotSupportedException {
		storageController.addTaskInList("Go to hell", null, null, "Doom", noDate, "high");
		storageController.addTaskInList("Do homework", null, cal1, "Home", cal2, "low");
		storageController.addTaskInList("Sign up for homework", cal1, cal2, "Home", noDate, "low");
		storageController.addTaskInList("Finish CS2103", null, null, "School", noDate, "high");
		ArrayList<Task> list = new ArrayList<Task>();
		list = storageController.updateTask(3, "", noDate, time1, "Home", noDate, "highest");
		assertEquals(FORMAT_TIME.format(time1.getTime()), FORMAT_TIME.format(list.get(1).getEndTime().getTime()));
	}
	
	@Test
	public void searchOnDate() throws IOException {
		storageController.addTaskInList("Go to hell", null, null, "Doom", noDate, "high");
		storageController.addTaskInList("Do homework", null, cal1, "Home", cal2, "low");
		storageController.addTaskInList("Sign up for homework", cal1, cal2, "Home", noDate, "low");
		storageController.addTaskInList("Finish CS2103", null, null, "School", noDate, "high");
		Calendar searchDate = Calendar.getInstance();
		searchDate.set(2030, Calendar.DECEMBER, 25);
		ArrayList<Task> searchList = storageController.searchTaskOnDate(searchDate);
		assertEquals("Sign up for homework",searchList.get(0).getTaskDescription());
	}
	
	@Test 
	public void checkCloseList() throws IOException {
		storageController.addTaskInList("Go to hell", null, null, "Doom", noDate, "high");
		storageController.addTaskInList("Do homework", null, cal1, "Home", cal2, "low");
		storageController.addTaskInList("Sign up for homework", cal1, cal2, "Home", noDate, "low");
		storageController.addTaskInList("Finish CS2103", null, null, "School", noDate, "high");
		storageController.closeTask(1);
		storageController.closeTask(4);
		ArrayList<Task> list = storageController.getCloseList();
		assertEquals("Go to hell",list.get(0).getTaskDescription());
		assertEquals("Finish CS2103",list.get(1).getTaskDescription());
	}
	
	@Test	// event task ---> deadline task
	public void updateTaskType1() throws IOException, CloneNotSupportedException {
		storageController.addTaskInList("Go to hell", null, null, "Doom", noDate, "high");
		storageController.addTaskInList("Do homework", null, cal1, "Home", cal2, "low");
		storageController.addTaskInList("Sign up for homework", cal1, cal2, "Home", noDate, "low");
		storageController.addTaskInList("Finish CS2103", null, null, "School", noDate, "high");
		Calendar newDate = Calendar.getInstance();
		newDate.set(2035, Calendar.JUNE, 30);
		ArrayList<Task> list = new ArrayList<Task>();
		list = storageController.updateTask(3, "", null, cal3, "School", noDate, "high");
		DeadlineTask deadlineTask = new DeadlineTask("Sign up for homework", cal3, "School", noDate, "high",3);
		assertEquals(deadlineTask.toString(), list.get(1).toString());
	}
	
	@Test	
	// event task ---> floating task
	public void updateTaskType2() throws IOException, CloneNotSupportedException {
		storageController.addTaskInList("Go to hell", null, null, "Doom", noDate, "high");
		storageController.addTaskInList("Do homework", null, cal1, "Home", cal2, "low");
		storageController.addTaskInList("Sign up for homework", cal1, cal2, "Home", noDate, "low");
		storageController.addTaskInList("Finish CS2103", null, null, "School", noDate, "high");
		Calendar newDate = Calendar.getInstance();
		newDate.set(2035, Calendar.JUNE, 30);
		ArrayList<Task> list = new ArrayList<Task>();
		list = storageController.updateTask(3, "Bee CS2103", null, null, "School", noDate, "high");
		FloatingTask floatingTask = new FloatingTask("Bee CS2103", "School", noDate, "high", 3);
		assertEquals(floatingTask.toString(), list.get(1).toString());
	}
	
	@Test	
	// event task ---> event task
	public void updateTaskType3() throws IOException, CloneNotSupportedException {
		storageController.addTaskInList("Go to hell", null, null, "Doom", noDate, "high");
		storageController.addTaskInList("Do homework", null, cal1, "Home", cal2, "low");
		storageController.addTaskInList("Sign up for homework", cal1, cal2, "Home", noDate, "low");
		storageController.addTaskInList("Finish CS2103", null, null, "School", noDate, "high");
		Calendar newDate = Calendar.getInstance();
		newDate.set(2035, Calendar.JUNE, 30);
		ArrayList<Task> list = new ArrayList<Task>();
		list = storageController.updateTask(3, "", cal2, cal3, "School", noDate, "high");
		Calendar cal = (Calendar) cal2.clone();
		cal.set(2030, Calendar.DECEMBER, 25);
		EventTask eventTask = new EventTask("Sign up for homework", cal, cal3, "School", noDate, "high", 3);
		assertEquals(eventTask.toString(), list.get(1).toString());
	}
	
	@Test 
	// floating task ---> event task
	public void updateTaskType4() throws IOException, CloneNotSupportedException {
		storageController.addTaskInList("Go to hell", null, null, "Doom", noDate, "high");
		storageController.addTaskInList("Do homework", null, cal1, "Home", cal2, "low");
		storageController.addTaskInList("Sign up for homework", cal1, cal2, "Home", noDate, "low");
		storageController.addTaskInList("Finish CS2103", null, null, "School", noDate, "high");
		Calendar newDate = Calendar.getInstance();
		newDate.set(2035, Calendar.JUNE, 30);
		ArrayList<Task> list = new ArrayList<Task>();
		list = storageController.updateTask(1, "", cal1, cal2, "Hell", noDate, "high");
		EventTask eventTask = new EventTask("Go to hell", cal1, cal2, "Hell", noDate, "high", 1);
		assertEquals(eventTask.toString(), list.get(1).toString());
	}
	
	@Test 
	// floating task ---> deadline task
	public void updateTaskType5() throws IOException, CloneNotSupportedException {
		storageController.addTaskInList("Go to hell", null, null, "Doom", noDate, "high");
		storageController.addTaskInList("Do homework", null, cal1, "Home", cal2, "low");
		storageController.addTaskInList("Sign up for homework", cal1, cal2, "Home", noDate, "low");
		storageController.addTaskInList("Finish CS2103", null, null, "School", noDate, "high");
		Calendar newDate = Calendar.getInstance();
		newDate.set(2035, Calendar.JUNE, 30);
		ArrayList<Task> list = new ArrayList<Task>();
		list = storageController.updateTask(1, "", null, cal2, "Heaven", noDate, "high");
		DeadlineTask deadlineTask = new DeadlineTask("Go to hell", cal2, "Heaven", noDate, "high",1);
		assertEquals(deadlineTask.toString(), list.get(1).toString());
	}
	
	@Test 
	// floating task ---> floating task
	public void updateTaskType6() throws IOException, CloneNotSupportedException {
		storageController.addTaskInList("Go to hell", null, null, "Doom", noDate, "high");
		storageController.addTaskInList("Do homework", null, cal1, "Home", cal2, "low");
		storageController.addTaskInList("Sign up for homework", cal1, cal2, "Home", noDate, "low");
		storageController.addTaskInList("Finish CS2103", null, null, "School", noDate, "high");
		Calendar newDate = Calendar.getInstance();
		newDate.set(2035, Calendar.JUNE, 30);
		ArrayList<Task> list = new ArrayList<Task>();
		list = storageController.updateTask(1, "", null, null, "LT33", noDate, "high");
		FloatingTask floatingTask = new FloatingTask("Go to hell", "LT33", noDate, "high", 3);
		assertEquals(floatingTask.toString(), list.get(1).toString());
	}
	
	@Test 
	// deadline task ---> event task	
	public void updateTaskType7() throws IOException, CloneNotSupportedException {
		storageController.addTaskInList("Go to hell", null, null, "Doom", noDate, "high");
		storageController.addTaskInList("Do homework", null, cal1, "Home", cal2, "low");
		storageController.addTaskInList("Sign up for homework", cal1, cal2, "Home", noDate, "low");
		storageController.addTaskInList("Finish CS2103", null, null, "School", noDate, "high");
		Calendar newDate = Calendar.getInstance();
		newDate.set(2035, Calendar.JUNE, 30);
		ArrayList<Task> list = new ArrayList<Task>();
		list = storageController.updateTask(2, "", cal1, cal2, "Hell", noDate, "high");
		Calendar cal = (Calendar) cal1.clone();
		cal.set(2020, Calendar.JUNE, 30);
		EventTask eventTask = new EventTask("Do homework", cal, cal2, "Hell", noDate, "high", 2);
		assertEquals(eventTask.toString(), list.get(1).toString());
	}
	
	@Test 
	// deadline task --> floating task
	public void updateTaskType8() throws IOException, CloneNotSupportedException {
		storageController.addTaskInList("Go to hell", null, null, "Doom", noDate, "high");
		storageController.addTaskInList("Do homework", null, cal1, "Home", cal2, "low");
		storageController.addTaskInList("Sign up for homework", cal1, cal2, "Home", noDate, "low");
		storageController.addTaskInList("Finish CS2103", null, null, "School", noDate, "high");
		Calendar newDate = Calendar.getInstance();
		newDate.set(2035, Calendar.JUNE, 30);
		ArrayList<Task> list = new ArrayList<Task>();
		list = storageController.updateTask(2, "", null, null, "Hell", noDate, "high");
		FloatingTask floatingTask = new FloatingTask("Do homework", "Hell", noDate, "high", 3);
		assertEquals(floatingTask.toString(), list.get(1).toString());
	}
	
	@Test 
	// deadline task ---> deadline task
	public void updateTaskType9() throws IOException, CloneNotSupportedException {
		storageController.addTaskInList("Go to hell", null, null, "Doom", noDate, "high");
		storageController.addTaskInList("Do homework", null, cal1, "Home", cal2, "low");
		storageController.addTaskInList("Sign up for homework", cal1, cal2, "Home", noDate, "low");
		storageController.addTaskInList("Finish CS2103", null, null, "School", noDate, "high");
		Calendar newDate = Calendar.getInstance();
		newDate.set(2035, Calendar.JUNE, 30);
		ArrayList<Task> list = new ArrayList<Task>();
		list = storageController.updateTask(2, "", null, cal3, "Hell", noDate, "high");
		DeadlineTask deadlineTask = new DeadlineTask("Do homework", cal3, "Hell", noDate, "high",1);
		assertEquals(deadlineTask.toString(), list.get(1).toString());
	}

	// @@author A0110422E	
	@Test
// 	Test for Deadline Task	
	public void testGetPriority() {
		DeadlineTask deadlineTask = new DeadlineTask();
		Calendar endDate = Calendar.getInstance();
		endDate.add(Calendar.SECOND, -1);
		deadlineTask.setEndDate(endDate);
		deadlineTask.setEndTime(endDate);
		assertTrue(deadlineTask.getPriority()=="high");
		deadlineTask.setPriority("");
		
		Calendar a = Calendar.getInstance();
		a.add(Calendar.MINUTE, 1);
		deadlineTask.setEndDate(a);
		deadlineTask.setEndTime(a);
		assertTrue(deadlineTask.getPriority()=="high");
		deadlineTask.setPriority("");
		
		Calendar b = Calendar.getInstance();
		b.add(Calendar.HOUR, 1);
		deadlineTask.setEndDate(b);
		deadlineTask.setEndTime(b);
		assertTrue(deadlineTask.getPriority()=="high");
		deadlineTask.setPriority("");
		
		Calendar c = Calendar.getInstance();
		c.add(Calendar.HOUR, 3);
		deadlineTask.setEndDate(c);
		deadlineTask.setEndTime(c);
		assertTrue(deadlineTask.getPriority()=="medium");
		deadlineTask.setPriority("");
		
		Calendar d = Calendar.getInstance();
		d.add(Calendar.HOUR, 23);
		deadlineTask.setEndDate(d);
		deadlineTask.setEndTime(d);
		assertTrue(deadlineTask.getPriority()=="medium");
		deadlineTask.setPriority("");
		
		Calendar e = Calendar.getInstance();
		e.add(Calendar.HOUR, 25);
		deadlineTask.setEndDate(e);
		deadlineTask.setEndTime(e);
		assertTrue(deadlineTask.getPriority()=="low");
		deadlineTask.setPriority("");
		
		deadlineTask.setPriority("low");
		assertTrue(deadlineTask.getPriority()=="low");
		
		deadlineTask.setPriority("medium");
		assertTrue(deadlineTask.getPriority()=="medium");
		
		deadlineTask.setPriority("high");
		assertTrue(deadlineTask.getPriority()=="high");

		
// 	Test for Event Task	
		EventTask eventTask = new EventTask();
		Calendar startDate = Calendar.getInstance();
		startDate.add(Calendar.SECOND, -1);
		eventTask.setStartDate(startDate);
		eventTask.setStartDate(startDate);
		assertTrue(eventTask.getPriority()=="high");
		eventTask.setPriority("");
		
		Calendar f = Calendar.getInstance();
		f.add(Calendar.MINUTE, 1);
		eventTask.setStartDate(f);
		eventTask.setStartDate(f);
		assertTrue(eventTask.getPriority()=="high");
		eventTask.setPriority("");
		
		Calendar g = Calendar.getInstance();
		g.add(Calendar.HOUR, 1);
		eventTask.setStartDate(g);
		eventTask.setStartTime(g);
		assertTrue(eventTask.getPriority()=="high");
		eventTask.setPriority("");
		
		Calendar h = Calendar.getInstance();
		h.add(Calendar.HOUR, 3);
		eventTask.setStartDate(h);
		eventTask.setStartTime(h);
		assertTrue(eventTask.getPriority()=="medium");
		eventTask.setPriority("");
		
		Calendar i = Calendar.getInstance();
		i.add(Calendar.HOUR, 23);
		eventTask.setStartDate(i);
		eventTask.setStartTime(i);
		assertTrue(eventTask.getPriority()=="medium");
		eventTask.setPriority("");
		
		Calendar j = Calendar.getInstance();
		j.add(Calendar.HOUR, 25);
		eventTask.setStartDate(j);
		eventTask.setStartTime(j);
		assertTrue(eventTask.getPriority()=="low");
		eventTask.setPriority("");
		
		eventTask.setPriority("low");
		assertTrue(eventTask.getPriority()=="low");
		
		eventTask.setPriority("medium");
		assertTrue(eventTask.getPriority()=="medium");
		
		eventTask.setPriority("high");
		assertTrue(eventTask.getPriority()=="high");
		
//	 	Test for floating Task	
		FloatingTask floatingTask = new FloatingTask();
		
		floatingTask.setPriority("");
		assertTrue(floatingTask.getPriority()=="low");
		
		floatingTask.setPriority("low");
		assertTrue(floatingTask.getPriority()=="low");
		
		floatingTask.setPriority("medium");
		assertTrue(floatingTask.getPriority()=="medium");
		
		floatingTask.setPriority("high");
		assertTrue(floatingTask.getPriority()=="high");
	}
		
	@After
	public void after() {
		File a = new File(FILE_DIRECTORY_NAME);
		File b = new File(FILE_CLOSED_NAME);
		File c = new File(FILE_DATA_NAME);
		a.delete();
		b.delete();
		c.delete();
	}

}

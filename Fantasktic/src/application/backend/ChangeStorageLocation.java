// @@author A0125417L
package application.backend;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;
import application.gui.ExceptionHandler;
import application.logger.LoggerHandler;
import application.storage.Task;

/*
 * Checks if URL exists and change directory or sends back a flag for gui to prompt user with a directory prompt
 */

public class ChangeStorageLocation implements UndoableCommand {

	// Initialization
	private static Logger logger = LoggerHandler.getLog();

	// Logger Messages
	private static final String ENTER_CHANGE_STORAGE_LOGGER_MSG = "Executing change storage function";

	// Exception Messages
	private static final String CHANGE_DIRECTORY_EXCEPTION_MESSAGE = "Failed to change directory";

	// Formatting
	private static final String BACKSLASHS = "\\\\";
	private static final String SLASH = "/";
	private static final String EMPTY_STRING = "";
	private static final String BACKSLASH = "\\";

	// Messages
	private static final String MESSAGE_STORAGE_URL_NOT_FOUND = "Storage Location Invalid: Opening Directory Chooser";
	private static final String MESSAGE_STORAGE_NO_INPUT = "No Location Input: Opening Directory Chooser";
	private static final String MESSAGE_STORAGE_URL_FOUND = "Storage Location Changed To: ";

	private static final String MESSAGE_UNDO_CHANGE = "Reverted Storage Location To: ";
	private static final String MESSAGE_UNDO_ERROR = "We encountered some problem undoing the storage change.";

	// Variables
	private String arguments;
	private String prevLocation;
	private StorageConnector storageConnector;

	ChangeStorageLocation(String arguments) {
		this.arguments = arguments;
	}

	@Override
	public Feedback execute(StorageConnector storageConnector, ArrayList<Task> tasksOnScreen) {
		logger.info(ENTER_CHANGE_STORAGE_LOGGER_MSG);
		this.storageConnector = storageConnector;
		Feedback feedback = null;
		feedback = checkUrlInput(storageConnector, tasksOnScreen, feedback);
		return feedback;
	}

	/*
	 * Check if user has input a directory or not
	 */
	private Feedback checkUrlInput(StorageConnector storageConnector, ArrayList<Task> tasksOnScreen,
			Feedback feedback) {
		if (arguments != EMPTY_STRING) {
			arguments = arguments.replaceAll(SLASH, BACKSLASHS);
			feedback = checkUrlExist(storageConnector, tasksOnScreen, feedback);
		} else {
			assert (arguments == EMPTY_STRING);
			feedback = noInput(tasksOnScreen);
		}
		return feedback;
	}

	/*
	 * Checks if the input is an actual file directory
	 */
	private Feedback checkUrlExist(StorageConnector storageConnector, ArrayList<Task> tasksOnScreen,
			Feedback feedback) {
		if (new File(arguments).isDirectory()) {
			feedback = validDirectoryFound(storageConnector, tasksOnScreen, feedback);
		} else {
			assert (!new File(arguments).isDirectory());
			feedback = invalidDirectoryFound(tasksOnScreen);
		}
		return feedback;
	}

	/*
	 * If input is a valid directory, execute the change storage functions
	 */
	private Feedback validDirectoryFound(StorageConnector storageConnector, ArrayList<Task> tasksOnScreen,
			Feedback feedback) {
		try {
			prevLocation = storageConnector.setDirectory(arguments + BACKSLASH);
			feedback = validDirectoryFound(tasksOnScreen);
		} catch (IOException e) {
			try {
				logger.severe(CHANGE_DIRECTORY_EXCEPTION_MESSAGE);
				throw new ExceptionHandler(CHANGE_DIRECTORY_EXCEPTION_MESSAGE);
			} catch (ExceptionHandler e1) {
				e1.printStackTrace();
			}
		}
		return feedback;
	}

	/*
	 * Create feedback when user did not input a directory path
	 */
	private Feedback noInput(ArrayList<Task> tasksOnScreen) {
		Feedback feedback;
		feedback = new Feedback(MESSAGE_STORAGE_NO_INPUT, tasksOnScreen, null);
		feedback.setStorageFlag();
		return feedback;
	}

	/*
	 * Create feedback when user inputs an invalid directory path
	 */
	private Feedback invalidDirectoryFound(ArrayList<Task> tasksOnScreen) {
		Feedback feedback;
		feedback = new Feedback(MESSAGE_STORAGE_URL_NOT_FOUND, tasksOnScreen, null);
		feedback.setStorageFlag();
		return feedback;
	}

	/*
	 * Create feedback when user inputs a valid directory path
	 */
	private Feedback validDirectoryFound(ArrayList<Task> tasksOnScreen) {
		Feedback feedback;
		feedback = new Feedback(MESSAGE_STORAGE_URL_FOUND + arguments + BACKSLASH, tasksOnScreen, null);
		feedback.setCalFlag();
		return feedback;
	}

	// @@author A0132632R
	public Feedback undo() throws NothingToUndoException {
		if (prevLocation == null) {
			throw new NothingToUndoException();
		} else {
			return revertStorageLocation();
		}
	}

	private Feedback revertStorageLocation() {
		try {
			return setStorageLocationToPrevious();
		} catch (IOException e) {
			Feedback feedback = new Feedback(MESSAGE_UNDO_ERROR, storageConnector.getOpenList(), null);
			feedback.setCalFlag();
			return feedback;
		}
	}

	private Feedback setStorageLocationToPrevious() throws IOException {
		storageConnector.setDirectory(prevLocation);
		Feedback feedback = new Feedback(MESSAGE_UNDO_CHANGE + prevLocation, storageConnector.getOpenList(), null);
		feedback.setCalFlag();
		return feedback;
	}
}
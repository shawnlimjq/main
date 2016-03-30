package application.gui;

import java.awt.AWTException;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

import application.logic.Logic;
import application.logic.NoDescriptionException;
import application.logic.Feedback;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * 
 * @author Shawn
 *
 */

public class GUIHandler {
	Logic logic;
	private TrayIcon trayIcon;

	private static final String LOGO_URL = "files/robot.jpg";
	private static final String APPLICATION_NAME = "Tasker";
	private static String LOGGER_NAME = "logfile";
	private static Logger logger = Logger.getLogger(LOGGER_NAME);

	private static final String EMPTY_STRING = "";
	private static final String SPACE = "\\s+";
	private static final String BACKSLASH = "\\";

	// Messages
	private static final String ADD_HINT_MESSAGE = "To add: [task description] from [start] to [end] at [location]";
	private static final String HELP_HINT_MESSAGE = "To get help: help";
	private static final String DELETE_HINT_MESSAGE = "To delete: delete [task description/number]";
	private static final String SEARCH_HINT_MESSAGE = "To search: search [task decription/priority [level]/[task description] by [date]]";
	private static final String EXIT_HINT_MESSAGE = "To exit: exit";
	private static final String UPDATE_HINT_MESSAGE = "To update: update [task number] [new task desc]";
	private static final String UNDO_HINT_MESSAGE = "To undo: undo";
	private static final String STORAGE_HINT_MESSAGE = "To change storage: storage";
	private static final String DONE_HINT_MESSAGE = "To mark task as complete: done [task number]";
	private static final String TEXT_FIELD_PROMPT = "What would you like us to do for you?";
	private static final String SYSTEM_TRAY_HINT = "Tasker is still running in the background.";

	private static final String SHOW_MENU_TEXT = "Show";
	private static final String EXIT_MENU_TEXT = "Exit";
	private static final String TXTFIELD_CSS = "txtField";

	String text = EMPTY_STRING;
	private static ArrayList<String> commands = new ArrayList<String>();
	private static int pointer = 0;

	public GUIHandler(Logic logic) {
		this.logic = logic;
	}

	public Feedback executeCommands(String cmd) throws NoDescriptionException {
		Feedback feedBack = logic.executeCommand(cmd);
		return feedBack;
	}

	public void startDirectoryPrompt(String file) {
		try {
			logic.startDirectoryPrompt(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void textFieldSetUp(TextField txtField, TaskListView taskList, GUIHandler guiH, Label helpLabel,
			Label feedbackLabel, Stage primaryStage, DirectoryChooser dirChooser) {
		txtField.setPromptText(TEXT_FIELD_PROMPT);
		txtField.getStyleClass().add(TXTFIELD_CSS);
		txtField.setMaxWidth(1066);
		txtField.setPrefSize(1066, 59);
		txtField.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				getHints(oldValue, newValue, helpLabel);
			}
		});
		txtField.setOnKeyPressed(new EventHandler<KeyEvent>() {

			public void handle(KeyEvent ke) {
				if (ke.getCode().equals(KeyCode.ENTER)) {
					try {
						text = txtField.getText();
						commands.add(text);
						if (text.equalsIgnoreCase("storage")) {
							directoryPrompt(primaryStage, dirChooser, guiH);
						} else {
							Feedback feedback = guiH.executeCommands(text);
							System.out.println(feedback.getMessage());
							taskList.updateList(feedback.getTasks());
							feedbackLabel.setText(feedback.getMessage());
						}
						txtField.clear();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				if (ke.getCode().equals(KeyCode.UP)) {
					// if used commands list is not empty
					if (!commands.isEmpty()) {
						if (!commands.contains(txtField.getText())) {
							pointer = commands.size();
						} else {
							pointer = commands.indexOf(txtField.getText());
						}
						// if pointer is not at the front end
						if (pointer != 0) {
							txtField.setText(commands.get(pointer - 1));
						}
					}
				}
				if (ke.getCode().equals(KeyCode.DOWN)) {
					if (!commands.isEmpty()) {
						if (!commands.contains(txtField.getText())) {
							pointer = commands.size();
						} else {
							pointer = commands.indexOf(txtField.getText());
						}
						// if pointer is not at the end
						if (pointer != commands.size() - 1) {
							txtField.setText(commands.get(pointer + 1));
						}
					}
				}
			}
		});
	}

	private void getHints(String oldValue, String newValue, Label helpLabel) {
		String newLetter = EMPTY_STRING;
		String oldWord = EMPTY_STRING;
		String newWord = EMPTY_STRING;

		if (!oldValue.isEmpty() && oldValue != null) {
			oldWord = getFirstWord(oldValue);
		}
		if (!newValue.isEmpty() && newValue != null) {
			newWord = getFirstWord(newValue);
			newLetter = getFirstLetter(newValue);
		}

		if (newWord == null) {
			return;
		}

		if (newWord.equals(oldWord)) {
			return;
		} else {
			switch (newLetter.toLowerCase()) {
			case "a":
				helpLabel.setText(ADD_HINT_MESSAGE);
				break;
			case "h":
				helpLabel.setText(HELP_HINT_MESSAGE);
				break;
			case "d":
				if (!newValue.isEmpty() && newValue.length() > 1) {
					if (getSecondLetter(newValue).equalsIgnoreCase("do")) {
						helpLabel.setText(DONE_HINT_MESSAGE);
					}
				} else {
					helpLabel.setText(DELETE_HINT_MESSAGE);
				}
				break;
			case "u":
				if (!newValue.isEmpty() && newValue.length() > 1) {
					if (getSecondLetter(newValue).equalsIgnoreCase("un")) {
						helpLabel.setText(UNDO_HINT_MESSAGE);
					}
				} else {
					helpLabel.setText(UPDATE_HINT_MESSAGE);
				}
				break;
			case "e":
				helpLabel.setText(EXIT_HINT_MESSAGE);
				break;
			case "s":
				if (!newValue.isEmpty() && newValue.length() > 1) {
					if (getSecondLetter(newValue).equalsIgnoreCase("st")) {
						helpLabel.setText(STORAGE_HINT_MESSAGE);
					}
				} else {
					helpLabel.setText(SEARCH_HINT_MESSAGE);
				}
				break;
			default:
				helpLabel.setText(ADD_HINT_MESSAGE);
				break;
			}
		}
	}

	private String getFirstLetter(String input) {
		String firstLetter = input.substring(0, 1);
		return firstLetter;
	}

	private String getFirstWord(String input) {
		String[] inputArgs = input.trim().split(SPACE);
		String firstWord = inputArgs[0];
		return firstWord;
	}

	private String getSecondLetter(String input) {
		String secondLetter = input.substring(0, 2);
		return secondLetter;
	}

	/*
	 * Checks if save location already exists If exists - use that location If
	 * does not exist then prompt for new location
	 */
	public void firstLaunchDirectoryPrompt(Stage primaryStage, DirectoryChooser dirChooser, GUIHandler guiH,
			TaskListView taskList) throws IOException {
		if (!logic.checkIfFileExists()) {
			directoryPrompt(primaryStage, dirChooser, guiH);
		} else {
			taskList.updateList(logic.loadDataFile());
		}
	}

	// Change directory
	public void directoryPrompt(Stage primaryStage, DirectoryChooser dirChooser, GUIHandler guiH) {
		final File selectedDirectory = dirChooser.showDialog(primaryStage);
		if (selectedDirectory != null) {
			guiH.startDirectoryPrompt(selectedDirectory.getPath().toString() + BACKSLASH);
		} else {
			guiH.startDirectoryPrompt(EMPTY_STRING);
		}
	}

	// Create Tray Icon
	public void createTrayIcon(Stage primaryStage) {
		if (SystemTray.isSupported()) {
			SystemTray tray = SystemTray.getSystemTray();
			setToTray(primaryStage);

			ActionListener showListener = showApplication(primaryStage);
			ActionListener closeListener = closeTray();

			PopupMenu popup = popupMenuConfiguration(closeListener, showListener);

			trayIconConfiguration(showListener, popup);

			try {
				tray.add(trayIcon);
			} catch (AWTException e) {
				System.err.println(e);
			}

		}
	}

	// Configuration for Tray Icon
	private void trayIconConfiguration(ActionListener showListener, PopupMenu popup) {
		java.awt.Image image = Toolkit.getDefaultToolkit().getImage(LOGO_URL);
		trayIcon = new TrayIcon(image, APPLICATION_NAME, popup);
		trayIcon.setImageAutoSize(false);
		trayIcon.addActionListener(showListener);
	}

	// Show Application Menu
	private ActionListener showApplication(Stage primaryStage) {
		ActionListener showListener = new ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent e) {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						logger.info("Clicked show on system tray icon");
						primaryStage.show();
						primaryStage.setIconified(false);
					}
				});
			}
		};
		return showListener;
	}

	// Close Menu
	private ActionListener closeTray() {
		ActionListener closeListener = new ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent e) {
				logger.info("Clicked close on system tray icon");
				System.exit(0);
			}
		};
		return closeListener;
	}

	// Minimize to tray
	private void setToTray(Stage primaryStage) {
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent t) {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						if (SystemTray.isSupported()) {
							primaryStage.hide();
							showProgramIsMinimizedMsg();
						} else {
							System.exit(0);
						}
					}
				});
			}
		});
	}

	// create a popup menu for right clicking system tray icon
	private PopupMenu popupMenuConfiguration(final ActionListener closeListener, ActionListener showListener) {
		PopupMenu popup = new PopupMenu();

		MenuItem showItem = new MenuItem(SHOW_MENU_TEXT);
		showItem.addActionListener(showListener);
		popup.add(showItem);

		MenuItem closeItem = new MenuItem(EXIT_MENU_TEXT);
		closeItem.addActionListener(closeListener);
		popup.add(closeItem);
		return popup;
	}

	// Message when minimized
	public void showProgramIsMinimizedMsg() {
		trayIcon.displayMessage(APPLICATION_NAME, SYSTEM_TRAY_HINT, TrayIcon.MessageType.INFO);
	}

}

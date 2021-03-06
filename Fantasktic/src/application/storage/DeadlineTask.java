//@@author A0125522R

package application.storage;

import java.util.Calendar;

/**
 * DeadlineTask is a subclass of Task class. This class is used when the user
 * creates a task with a deadline. It has an extra parameter, endDate, to denote
 * the task deadline.
 */
public class DeadlineTask extends Task implements Cloneable {
	private Calendar endDate;

	/**
	 * Creates a DeadlineTask object with "empty" variables initalised.
	 */
	public DeadlineTask() {
		setTaskDescription(EMPTY_STRING);
		endDate = Calendar.getInstance();
		setLocation(EMPTY_STRING);
		setRemindDate(Calendar.getInstance());
		setPriority(EMPTY_STRING);
		setTaskIndex(EMPTY_TASK);
	}

	/**
	 * Creates a DeadlineTask object with specified variables.
	 */
	public DeadlineTask(String taskDescription, Calendar endDate, String location, Calendar remindDate, String priority,
			int taskIndex) {
		setTaskDescription(taskDescription);
		this.endDate = endDate;
		setEndTime(endDate);
		setLocation(location);
		setRemindDate(remindDate);
		setPriority(priority);
		setTaskIndex(taskIndex);
	}

	/**
	 * Set the deadline date (year, month, date).
	 */
	public void setEndDate(Calendar endDate) {
		this.endDate.set(endDate.get(Calendar.YEAR), endDate.get(Calendar.MONTH), endDate.get(Calendar.DATE));
	}

	/**
	 * Set the deadline time (hour, minute, second, milliseconds).
	 */
	public void setEndTime(Calendar endTime) {
		this.endDate.set(Calendar.HOUR_OF_DAY, endTime.get(Calendar.HOUR_OF_DAY));
		this.endDate.set(Calendar.MINUTE, endTime.get(Calendar.MINUTE));
		this.endDate.set(Calendar.SECOND, endTime.get(Calendar.SECOND));
		this.endDate.set(Calendar.MILLISECOND, endTime.get(Calendar.MILLISECOND));
	}

	/**
	 * Returns null as no start date for DeadlineTask.
	 */
	public Calendar getStartDate() {
		return NO_DATE;
	}

	/**
	 * Returns null as no start time for DeadlineTask.
	 */
	public Calendar getStartTime() {
		return NO_TIME;
	}

	/**
	 * Returns the deadline date.
	 */
	public Calendar getEndDate() {
		return endDate;
	}

	/**
	 * Returns the deadline time.
	 */
	public Calendar getEndTime() {
		return endDate;
	}

	/**
	 * Returns the deadline in String.
	 */
	public String durationToString() {
		String durationMessage = EMPTY_STRING;
		String endingDate = dateToString(endDate);
		String endingTime = timeToString(endDate);
		if (!endingDate.equals(EMPTY_DATE_STRING)) {
			durationMessage += KEYWORD_BY + endingDate;
			if (!endingTime.equals(EMPTY_TIME_STRING)) {
				durationMessage += KEYWORD_SPACE + endingTime;
			}
		}
		return durationMessage;
	}

	/**
	 * Returns the task details in String.
	 */
	public String toString() {
		String taskDetails = KEYWORD_QUOTE;
		taskDetails += getTaskDescription();
		taskDetails += KEYWORD_SPACE + durationToString();
		if (!getLocation().equalsIgnoreCase(EMPTY_STRING)) {
			taskDetails += KEYWORD_AT + getLocation();
		}
		
		if (!getPriority().equalsIgnoreCase(EMPTY_STRING)) {
			taskDetails += KEYWORD_FULLSTOP + KEYWORD_PRIORITY + getPriority();
		}
		
		taskDetails += KEYWORD_QUOTE;
		return taskDetails;
	}

	/**
	 * Clones a DeadlineTask.
	 */
	protected Object clone() throws CloneNotSupportedException {
		DeadlineTask newTask = (DeadlineTask) super.clone();
		newTask.endDate = (Calendar) endDate.clone();
		newTask.remindDate = (Calendar) remindDate.clone();
		return newTask;
	}

	// @@author A0110422E
	/**
	 * Return checked priority parameter
	 */
	public String getPriority() {
		Calendar currentTime = Calendar.getInstance();
		Calendar endTime = getEndDate();
		String tempPriority = LOW;
		if (endTime.before(currentTime)) {
			setPriority(HIGH);
		}
		if (priority.equalsIgnoreCase(EMPTY_STRING)) {
			// End time is in less than two hours
			if (timeDifference(currentTime, endTime) < TWO_HOUR) {
				tempPriority = HIGH;
			} else {
				// End time is between two hours and one day
				if (timeDifference(currentTime, endTime) < ONE_DAY) {
					tempPriority = MEDIUM;
				}
			}
		} else {
			tempPriority = priority;
		}
		return tempPriority;
	}

	/**
	 * Return the time difference between two Calendar objects, in miliseconds
	 */
	private long timeDifference(Calendar currentTime, Calendar endTime) {
		return endTime.getTimeInMillis() - currentTime.getTimeInMillis();
	}

}

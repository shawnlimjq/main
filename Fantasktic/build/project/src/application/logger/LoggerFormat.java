package application.logger;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

/**
 * 
 * @author Shawn
 *
 */

public class LoggerFormat extends Formatter {

	private static final String HEADER = " Start ";
	private static final String TAIL = "  End  ";
	private static final String NEXT_LINE = "\n";
	private static final String PADDING = " ---------------------- ";
	private static final String SEPARATOR = " - ";

	private static final String DATE_FORMAT = "dd.MM.yyyy hh:mm:ss";
	private static final DateFormat DF = new SimpleDateFormat(DATE_FORMAT);

	@Override
	public String format(LogRecord log) {
		String date = DF.format(new Date(log.getMillis()));
		String className = log.getSourceClassName();
		String methodName = log.getSourceMethodName();
		String level = String.format("%-6s", log.getLevel().toString());
		String message = formatMessage(log);
		String logMessage = date + SEPARATOR + level + SEPARATOR + className + SEPARATOR + methodName + SEPARATOR
				+ message + NEXT_LINE;
		return logMessage;
	}

	public String getHead(Handler handler) {
		String logHead = PADDING + HEADER + new Date() + PADDING + NEXT_LINE;
		return logHead;
	}

	public String getTail(Handler handler) {
		String logTail = PADDING + TAIL + new Date() + PADDING + NEXT_LINE;
		return logTail;
	}

}

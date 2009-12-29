package ro.mihaiparaschiv.sac.client;

import com.google.gwt.core.client.GWT.UncaughtExceptionHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.DialogBox;

public class ExceptionHandler implements UncaughtExceptionHandler {
	public void onUncaughtException(Throwable throwable) {
		String text = "Uncaught exception: ";
		while (throwable != null) {
			StackTraceElement[] stackTraceElements = throwable.getStackTrace();
			text += throwable.toString() + "\n";
			for (int i = 0; i < stackTraceElements.length; i++) {
				text += "    at " + stackTraceElements[i] + "\n";
			}
			throwable = throwable.getCause();
			if (throwable != null) {
				text += "Caused by: ";
			}
		}
		DialogBox dialogBox = new DialogBox(true);
		DOM.setStyleAttribute(dialogBox.getElement(), "backgroundColor",
				"#ABCDEF");
		System.err.print(text);
		text = text.replaceAll(" ", " ");
		dialogBox.setHTML("<pre>" + text + "</pre>");
		dialogBox.center();
	}
}

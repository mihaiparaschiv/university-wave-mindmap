package ro.mihaiparaschiv.sac.client.view.dnd;

import pl.balon.gwt.diagrams.client.connection.Connection;
import pl.balon.gwt.diagrams.client.connection.RectilinearTwoEndedConnection;
import pl.balon.gwt.diagrams.client.connector.Connector;
import pl.balon.gwt.diagrams.client.connector.UIObjectConnector;
import ro.mihaiparaschiv.sac.client.view.widget.AdditionHandle;

import com.google.gwt.user.client.ui.AbsolutePanel;

/**
 * Handles the operations for an active connection between a concept widget and
 * it's addition handle.
 */
public class ConnectionHandler {
	private final AbsolutePanel panel;
	private Connection connection;
	private AdditionHandle additionHandle;

	public ConnectionHandler(AbsolutePanel panel) {
		this.panel = panel;
	}
	
	/**
	 * Builds the connection.
	 * 
	 * @param additionHandle
	 */
	public void build(AdditionHandle additionHandle) {
		reset();
		this.additionHandle = additionHandle;

		Connector start = UIObjectConnector.wrap(additionHandle
				.getConceptWidget());
		Connector end = UIObjectConnector.wrap(additionHandle);
		connection = new RectilinearTwoEndedConnection(start, end);
		connection.appendTo(panel);
	}

	public boolean isBuilt() {
		return connection != null;
	}
	
	/**
	 * Called when the ends of the connection move.
	 */
	public void update() {
		if (isBuilt()) {
			connection.update();
		}
	}

	/**
	 * Removes the connection from the view and retrieves the addition handle to
	 * the parent concept widget.
	 */
	public void reset() {
		if (isBuilt()) {
			connection.remove();
			connection = null;
			additionHandle.getConceptWidget().retrieveAdditionHandler();
		}
	}
}

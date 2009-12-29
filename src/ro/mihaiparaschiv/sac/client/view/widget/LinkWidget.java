package ro.mihaiparaschiv.sac.client.view.widget;

import pl.balon.gwt.diagrams.client.connection.RectilinearTwoEndedConnection;
import pl.balon.gwt.diagrams.client.connector.Connector;
import ro.mihaiparaschiv.sac.client.model.Link;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;

public class LinkWidget extends RectilinearTwoEndedConnection implements
		HasClickHandlers {

	private final Link link;

	public LinkWidget(Link link, Connector c1, Connector c2) {
		super(c1, c2);
		this.link = link;
	}

	public Link getLink() {
		return link;
	}

	@Override
	public HandlerRegistration addClickHandler(ClickHandler handler) {
		return addDomHandler(handler, ClickEvent.getType());
	}
}

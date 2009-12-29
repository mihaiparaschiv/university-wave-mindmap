package ro.mihaiparaschiv.sac.client.event.model;

import ro.mihaiparaschiv.sac.client.model.Link;

public class LinkAddEvent extends ModelEvent {
	public static Type<ModelEventHandler> TYPE = new Type<ModelEventHandler>();

	private final Link link;

	public LinkAddEvent(Link link) {
		this.link = link;
	}
	
	public Link getLink() {
		return link;
	}

	@Override
	public Type<ModelEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(final ModelEventHandler handler) {
		handler.onLinkAdd(this);
	}
}

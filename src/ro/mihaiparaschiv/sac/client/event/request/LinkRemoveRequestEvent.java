package ro.mihaiparaschiv.sac.client.event.request;

import ro.mihaiparaschiv.sac.client.model.Link;

public class LinkRemoveRequestEvent extends RequestEvent {
	public static Type<RequestEventHandler> TYPE = new Type<RequestEventHandler>();

	private final Link link;

	public LinkRemoveRequestEvent(Link link) {
		this.link = link;
	}
	
	public Link getLink() {
		return link;
	}

	@Override
	public Type<RequestEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(final RequestEventHandler handler) {
		handler.onLinkRemoveRequest(this);
	}
}

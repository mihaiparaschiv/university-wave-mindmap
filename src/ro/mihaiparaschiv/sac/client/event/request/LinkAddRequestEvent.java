package ro.mihaiparaschiv.sac.client.event.request;

import ro.mihaiparaschiv.sac.client.model.Concept;

public class LinkAddRequestEvent extends RequestEvent {
	public static Type<RequestEventHandler> TYPE = new Type<RequestEventHandler>();
	private Concept startConcept;
	private Concept endConcept;

	public LinkAddRequestEvent(Concept startConcept, Concept endConcept) {
		this.startConcept = startConcept;
		this.endConcept = endConcept;
	}
	
	public Concept getStartConcept() {
		return startConcept;
	}
	
	public Concept getEndConcept() {
		return endConcept;
	}

	@Override
	public Type<RequestEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(final RequestEventHandler handler) {
		handler.onLinkAddRequest(this);
	}
}

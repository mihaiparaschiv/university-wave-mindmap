package ro.mihaiparaschiv.sac.client.event.request;

import ro.mihaiparaschiv.sac.client.model.Concept;

public class ConceptRemoveRequestEvent extends RequestEvent {
	public static Type<RequestEventHandler> TYPE = new Type<RequestEventHandler>();

	private final Concept concept;

	public ConceptRemoveRequestEvent(Concept concept) {
		this.concept = concept;
	}
	
	public Concept getConcept() {
		return concept;
	}

	@Override
	public Type<RequestEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(final RequestEventHandler handler) {
		handler.onConceptRemoveRequest(this);
	}
}

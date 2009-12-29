package ro.mihaiparaschiv.sac.client.event.request;

import ro.mihaiparaschiv.sac.client.model.Concept;
import ro.mihaiparaschiv.sac.client.model.ConceptProperty;

public class ConceptChangeRequestEvent extends RequestEvent {
	public static Type<RequestEventHandler> TYPE = new Type<RequestEventHandler>();

	private final Concept concept;
	private final ConceptProperty property;

	public ConceptChangeRequestEvent(Concept concept, ConceptProperty property) {
		this.concept = concept;
		this.property = property;
	}
	
	public Concept getConcept() {
		return concept;
	}
	
	public ConceptProperty getProperty() {
		return property;
	}

	@Override
	public Type<RequestEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(final RequestEventHandler handler) {
		handler.onConceptChangeRequest(this);
	}
}

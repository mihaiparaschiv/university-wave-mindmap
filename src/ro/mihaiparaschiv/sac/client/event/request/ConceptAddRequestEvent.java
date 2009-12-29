package ro.mihaiparaschiv.sac.client.event.request;

import ro.mihaiparaschiv.sac.client.model.Concept;
import ro.mihaiparaschiv.sac.client.model.ConceptName;
import ro.mihaiparaschiv.sac.client.model.ConceptPosition;

public class ConceptAddRequestEvent extends RequestEvent {
	public static Type<RequestEventHandler> TYPE = new Type<RequestEventHandler>();
	
	private final Concept parentConcept;
	private final ConceptName name;
	private final ConceptPosition position;

	public ConceptAddRequestEvent(Concept parentConcept, ConceptName name, ConceptPosition position) {
		this.parentConcept = parentConcept;
		this.name = name;
		this.position = position;
	}
	
	public Concept getParentConcept() {
		return parentConcept;
	}
	
	public ConceptName getName() {
		return name;
	}
	
	public ConceptPosition getPosition() {
		return position;
	}

	@Override
	public Type<RequestEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(final RequestEventHandler handler) {
		handler.onConceptAddRequest(this);
	}
}

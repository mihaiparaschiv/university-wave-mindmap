package ro.mihaiparaschiv.sac.client.event.model;

import ro.mihaiparaschiv.sac.client.model.Concept;

public class ConceptRemoveEvent extends ModelEvent {
	public static Type<ModelEventHandler> TYPE = new Type<ModelEventHandler>();

	private final Concept concept;

	public ConceptRemoveEvent(Concept concept) {
		this.concept = concept;
	}
	
	public Concept getConcept() {
		return concept;
	}

	@Override
	public Type<ModelEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(final ModelEventHandler handler) {
		handler.onConceptRemove(this);
	}
}

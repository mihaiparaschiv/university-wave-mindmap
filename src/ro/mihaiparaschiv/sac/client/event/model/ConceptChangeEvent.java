package ro.mihaiparaschiv.sac.client.event.model;

import ro.mihaiparaschiv.sac.client.model.Concept;
import ro.mihaiparaschiv.sac.client.model.ConceptProperty;

public class ConceptChangeEvent extends ModelEvent {
	public static Type<ModelEventHandler> TYPE = new Type<ModelEventHandler>();

	private final Concept concept;
	private ConceptProperty property;

	public ConceptChangeEvent(Concept concept, ConceptProperty property) {
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
	public Type<ModelEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(final ModelEventHandler handler) {
		handler.onConceptChange(this);
	}
}

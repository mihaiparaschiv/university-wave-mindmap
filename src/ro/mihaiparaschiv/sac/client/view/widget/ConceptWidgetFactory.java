package ro.mihaiparaschiv.sac.client.view.widget;

import ro.mihaiparaschiv.sac.client.model.Concept;

public class ConceptWidgetFactory {
	public ConceptWidget build(Concept concept) {
		ConceptWidget widget = new ConceptWidget(concept);
		return widget;
	}
}

package ro.mihaiparaschiv.sac.client.view.widget;

import com.google.gwt.user.client.ui.Label;

public class ConceptRemovalWidget extends Label {
	private ConceptWidget conceptWidget;

	public ConceptRemovalWidget(ConceptWidget conceptWidget) {
		super("del");
		this.conceptWidget = conceptWidget;
	}

	public ConceptWidget getConceptWidget() {
		return conceptWidget;
	}
}

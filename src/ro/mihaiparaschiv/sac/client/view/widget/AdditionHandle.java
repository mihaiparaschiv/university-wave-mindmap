package ro.mihaiparaschiv.sac.client.view.widget;

import com.google.gwt.user.client.ui.Label;

public class AdditionHandle extends Label {
	private ConceptWidget conceptWidget;

	public AdditionHandle(ConceptWidget conceptWidget) {
		super("+");
		this.conceptWidget = conceptWidget;
	}

	public ConceptWidget getConceptWidget() {
		return conceptWidget;
	}
}

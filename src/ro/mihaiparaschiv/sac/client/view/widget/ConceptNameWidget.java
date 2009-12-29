package ro.mihaiparaschiv.sac.client.view.widget;

import com.google.gwt.user.client.ui.TextBox;

public class ConceptNameWidget extends TextBox {
	private ConceptWidget conceptWidget;

	public ConceptNameWidget(ConceptWidget conceptWidget) {
		this.conceptWidget = conceptWidget;
	}

	public ConceptWidget getConceptWidget() {
		return conceptWidget;
	}
}

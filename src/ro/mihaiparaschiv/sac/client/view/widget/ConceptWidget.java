package ro.mihaiparaschiv.sac.client.view.widget;

import ro.mihaiparaschiv.sac.client.model.Concept;

import com.allen_sauer.gwt.dnd.client.HasDragHandle;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ConceptWidget extends Composite implements HasDragHandle {
	private final Concept concept;
	private final Label dragHandle;
	private final ConceptNameWidget nameWidget;
	private final AdditionHandle additionHandle;
	private final ConceptRemovalWidget removalWidget;
	private final VerticalPanel panel;

	public ConceptWidget(Concept concept) {
		this.concept = concept;

		nameWidget = new ConceptNameWidget(this);
		setText(concept.getName().getValue());

		dragHandle = new Label(":::::::");
		additionHandle = new AdditionHandle(this);

		removalWidget = new ConceptRemovalWidget(this);

		panel = new VerticalPanel();
		panel.add(dragHandle);
		panel.add(nameWidget);
		panel.add(additionHandle);
		panel.add(removalWidget);

		initWidget(panel);
	}

	public void setText(String value) {
		nameWidget.setValue(value);
	}

	public Concept getConcept() {
		return concept;
	}

	public ConceptNameWidget getNameWidget() {
		return nameWidget;
	}

	public ConceptRemovalWidget getRemovalWidget() {
		return removalWidget;
	}

	@Override
	public Widget getDragHandle() {
		return dragHandle;
	}

	public AdditionHandle getAdditionHandle() {
		return additionHandle;
	}

	public void retrieveAdditionHandler() {
		panel.add(additionHandle);
	}
}

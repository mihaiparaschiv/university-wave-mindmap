package ro.mihaiparaschiv.sac.client.view.widget;

import ro.mihaiparaschiv.sac.client.model.Concept;

import com.allen_sauer.gwt.dnd.client.HasDragHandle;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ConceptWidget extends Composite implements HasDragHandle {
	private final Concept concept;
	private final Label dragHandle;
	private final ConceptNameWidget nameWidget;
	private final AdditionHandle additionHandle;
	private final ConceptRemovalWidget removalWidget;
	private final VerticalPanel mainContainer;
	private final HorizontalPanel toolsContainer;

	public ConceptWidget(Concept concept) {
		this.concept = concept;

		nameWidget = new ConceptNameWidget(this);
		setText(concept.getName().getValue());

		dragHandle = new Label(":::::::");
		dragHandle.addStyleName("concept-drag-handle");
		
		removalWidget = new ConceptRemovalWidget(this);
		removalWidget.addStyleName("concept-removal-widget");
		
		additionHandle = new AdditionHandle(this);
		additionHandle.addStyleName("concept-addition-handle");

		toolsContainer = new HorizontalPanel();
		toolsContainer.add(dragHandle);
		toolsContainer.add(removalWidget);
		toolsContainer.add(additionHandle);
		
		mainContainer = new VerticalPanel();
		mainContainer.addStyleName("concept-container");
		mainContainer.add(nameWidget);
		mainContainer.add(toolsContainer);

		initWidget(mainContainer);
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
		toolsContainer.add(additionHandle);
	}
}

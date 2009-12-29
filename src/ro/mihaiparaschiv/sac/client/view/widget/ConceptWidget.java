package ro.mihaiparaschiv.sac.client.view.widget;

import ro.mihaiparaschiv.sac.client.model.Concept;

import com.allen_sauer.gwt.dnd.client.HasDragHandle;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ConceptWidget extends Composite implements HasDragHandle {
	private Concept concept;
	private Label dragHandle;
	private TextBox textBox;
	private AdditionHandle additionHandle;
	private VerticalPanel panel;

	public ConceptWidget(Concept concept) {
		this.concept = concept;

		dragHandle = new Label(":::::::");
		textBox = new TextBox();
		textBox.setValue(concept.getName().getValue());
		
		additionHandle = new AdditionHandle(this);

		panel = new VerticalPanel();
		panel.add(dragHandle);
		panel.add(textBox);
		panel.add(additionHandle);

		initWidget(panel);
	}

	public Concept getConcept() {
		return concept;
	}
	
	public void setText(String value) {
		textBox.setValue(value);
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

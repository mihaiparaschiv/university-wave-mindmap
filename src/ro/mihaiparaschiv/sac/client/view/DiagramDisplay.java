package ro.mihaiparaschiv.sac.client.view;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class DiagramDisplay extends Composite implements
		DiagramPresenter.Display {
	private final DiagramPanel diagramPanel;

	public DiagramDisplay(int width, int height) {
		diagramPanel = new DiagramPanel();
		diagramPanel.addStyleName("diagram-container");
		diagramPanel.setPixelSize(width, height);
		initWidget(diagramPanel);
	}

	@Override
	public DiagramPanel getDiagramPanel() {
		return diagramPanel;
	}

	@Override
	public Widget asWidget() {
		return this;
	}

	@Override
	public void startProcessing() {
	}

	@Override
	public void stopProcessing() {
	}
}

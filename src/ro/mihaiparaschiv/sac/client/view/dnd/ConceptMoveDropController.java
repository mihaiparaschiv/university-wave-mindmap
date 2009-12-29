package ro.mihaiparaschiv.sac.client.view.dnd;

import java.util.List;

import net.customware.gwt.presenter.client.EventBus;
import ro.mihaiparaschiv.sac.client.event.request.ConceptChangeRequestEvent;
import ro.mihaiparaschiv.sac.client.model.ConceptPosition;
import ro.mihaiparaschiv.sac.client.view.DiagramPresenter;
import ro.mihaiparaschiv.sac.client.view.widget.ConceptWidget;

import com.allen_sauer.gwt.dnd.client.DragContext;
import com.allen_sauer.gwt.dnd.client.drop.AbsolutePositionDropController;
import com.google.gwt.user.client.ui.Widget;

/**
 * Handles concept position changes resulting from drag and drop operations.
 */
public class ConceptMoveDropController extends AbsolutePositionDropController {
	private DiagramPresenter presenter;
	private EventBus eventBus;

	public ConceptMoveDropController(DiagramPresenter presenter,
			EventBus eventBus) {
		super(presenter.getDisplay().getDiagramPanel());
		this.presenter = presenter;
		this.eventBus = eventBus;
	}

	@Override
	public void onMove(DragContext context) {
		super.onMove(context);

		updateConnections(context.selectedWidgets);
	}

	@Override
	public void onDrop(DragContext context) {
		super.onDrop(context);

		updateConnections(context.selectedWidgets);
		ConceptWidget cw = (ConceptWidget) context.selectedWidgets.get(0);
		int x = cw.getAbsoluteLeft() - cw.getParent().getAbsoluteLeft();
		int y = cw.getAbsoluteTop() - cw.getParent().getAbsoluteTop();
		ConceptPosition pos = new ConceptPosition(x, y);
		eventBus.fireEvent(new ConceptChangeRequestEvent(cw.getConcept(), pos));
	}

	private void updateConnections(List<Widget> widgets) {
		for (Widget w : widgets) {
			presenter.updateLinks(((ConceptWidget) w).getConcept());
		}
	}
}

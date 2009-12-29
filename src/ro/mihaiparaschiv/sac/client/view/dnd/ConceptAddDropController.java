package ro.mihaiparaschiv.sac.client.view.dnd;

import net.customware.gwt.presenter.client.EventBus;
import ro.mihaiparaschiv.sac.client.event.request.ConceptAddRequestEvent;
import ro.mihaiparaschiv.sac.client.model.Concept;
import ro.mihaiparaschiv.sac.client.model.ConceptName;
import ro.mihaiparaschiv.sac.client.model.ConceptPosition;
import ro.mihaiparaschiv.sac.client.view.widget.AdditionHandle;

import com.allen_sauer.gwt.dnd.client.DragContext;
import com.allen_sauer.gwt.dnd.client.drop.AbsolutePositionDropController;
import com.google.gwt.user.client.ui.AbsolutePanel;

/**
 * Handles concept creation by drag and drop of addition handles.
 */
public class ConceptAddDropController extends AbsolutePositionDropController {
	private EventBus eventBus;
	private ConnectionController connectionController;

	public ConceptAddDropController(AbsolutePanel panel, EventBus eventBus,
			ConnectionController connectionController) {
		super(panel);
		this.eventBus = eventBus;
		this.connectionController = connectionController;
	}

	@Override
	public void onMove(DragContext context) {
		super.onMove(context);

		if (connectionController.isBuilt()) {
			connectionController.update();
		} else {
			AdditionHandle ah = (AdditionHandle) context.selectedWidgets.get(0);
			connectionController.build(ah);
		}
	}

	@Override
	public void onDrop(DragContext context) {
		super.onDrop(context);

		AdditionHandle ah = (AdditionHandle) context.selectedWidgets.get(0);
		Concept concept = ah.getConceptWidget().getConcept();

		// add a new concept
		int x = ah.getAbsoluteLeft() - ah.getParent().getAbsoluteLeft();
		int y = ah.getAbsoluteTop() - ah.getParent().getAbsoluteTop();
		eventBus.fireEvent(new ConceptAddRequestEvent(concept, new ConceptName("concept name"), //
				new ConceptPosition(x, y)));

		connectionController.reset();
	}
}

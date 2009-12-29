package ro.mihaiparaschiv.sac.client.view.dnd;

import net.customware.gwt.presenter.client.EventBus;
import ro.mihaiparaschiv.sac.client.event.request.LinkAddRequestEvent;
import ro.mihaiparaschiv.sac.client.model.Concept;
import ro.mihaiparaschiv.sac.client.view.widget.AdditionHandle;
import ro.mihaiparaschiv.sac.client.view.widget.ConceptWidget;

import com.allen_sauer.gwt.dnd.client.DragContext;
import com.allen_sauer.gwt.dnd.client.drop.SimpleDropController;

/**
 * Handles the dropping of addition handles over concepts. The drop target is a
 * concept widget.
 */
public class LinkAddDropController extends SimpleDropController {
	private EventBus eventBus;
	private ConnectionHandler connectionController;

	public LinkAddDropController(ConceptWidget conceptWidget,
			EventBus eventBus, ConnectionHandler connectionController) {
		super(conceptWidget);
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
		Concept c1 = ah.getConceptWidget().getConcept();
		Concept c2 = ((ConceptWidget) getDropTarget()).getConcept();

		if (!c1.equals(c2) && c1.getLink(c2) == null) {
			eventBus.fireEvent(new LinkAddRequestEvent(c1, c2));
		}

		connectionController.reset();
	}
}

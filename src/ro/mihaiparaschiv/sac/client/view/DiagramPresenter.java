package ro.mihaiparaschiv.sac.client.view;

import java.util.HashMap;

import net.customware.gwt.presenter.client.EventBus;
import net.customware.gwt.presenter.client.place.Place;
import net.customware.gwt.presenter.client.place.PlaceRequest;
import net.customware.gwt.presenter.client.widget.WidgetDisplay;
import net.customware.gwt.presenter.client.widget.WidgetPresenter;
import ro.mihaiparaschiv.sac.client.event.model.ConceptAddEvent;
import ro.mihaiparaschiv.sac.client.event.model.ConceptChangeEvent;
import ro.mihaiparaschiv.sac.client.event.model.ConceptRemoveEvent;
import ro.mihaiparaschiv.sac.client.event.model.LinkAddEvent;
import ro.mihaiparaschiv.sac.client.event.model.LinkRemoveEvent;
import ro.mihaiparaschiv.sac.client.event.model.ModelEventHandler;
import ro.mihaiparaschiv.sac.client.event.request.ConceptAddRequestEvent;
import ro.mihaiparaschiv.sac.client.event.request.LinkRemoveRequestEvent;
import ro.mihaiparaschiv.sac.client.model.Concept;
import ro.mihaiparaschiv.sac.client.model.ConceptName;
import ro.mihaiparaschiv.sac.client.model.ConceptPosition;
import ro.mihaiparaschiv.sac.client.model.ConceptProperty;
import ro.mihaiparaschiv.sac.client.model.Link;
import ro.mihaiparaschiv.sac.client.view.dnd.ConceptAddDropController;
import ro.mihaiparaschiv.sac.client.view.dnd.ConceptMoveDropController;
import ro.mihaiparaschiv.sac.client.view.dnd.ConnectionController;
import ro.mihaiparaschiv.sac.client.view.dnd.LinkAddDropController;
import ro.mihaiparaschiv.sac.client.view.widget.ConceptWidget;
import ro.mihaiparaschiv.sac.client.view.widget.ConceptWidgetFactory;
import ro.mihaiparaschiv.sac.client.view.widget.LinkWidget;
import ro.mihaiparaschiv.sac.client.view.widget.LinkWidgetFactory;

import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.user.client.ui.AbsolutePanel;

public class DiagramPresenter extends WidgetPresenter<DiagramPresenter.Display> {
	private final HashMap<Concept, ConceptWidget> conceptWidgets;
	private final HashMap<Link, LinkWidget> linkWidgets;
	private final ConceptWidgetFactory conceptWidgetFactory;
	private final LinkWidgetFactory linkWidgetFactory;

	private PickupDragController conceptDragController;
	private ConceptMoveDropController conceptDropController;
	private PickupDragController actionDragController;
	private ConceptAddDropController conceptAddDropController;
	private EventHandler eventHandler;
	private ConnectionController connectionController;

	public DiagramPresenter(Display display, EventBus eventBus) {
		super(display, eventBus);

		conceptWidgets = new HashMap<Concept, ConceptWidget>();
		linkWidgets = new HashMap<Link, LinkWidget>();
		conceptWidgetFactory = new ConceptWidgetFactory();
		linkWidgetFactory = new LinkWidgetFactory();

		eventHandler = new EventHandler();
		eventBus.addHandler(ConceptAddEvent.TYPE, eventHandler);
		eventBus.addHandler(ConceptChangeEvent.TYPE, eventHandler);
		eventBus.addHandler(ConceptRemoveEvent.TYPE, eventHandler);
		eventBus.addHandler(LinkAddEvent.TYPE, eventHandler);
		eventBus.addHandler(LinkRemoveEvent.TYPE, eventHandler);
	}

	private DiagramPanel getDiagramPanel() {
		return display.getDiagramPanel();
	}

	public ConceptWidget getConceptWidget(Concept concept) {
		return conceptWidgets.get(concept);
	}

	public LinkWidget getLinkWidget(Link link) {
		return linkWidgets.get(link);
	}

	public void updateLinks(Concept concept) {
		// TODO Update only if we need to.
		connectionController.reset();

		for (Link l : concept.getLinks()) {
			getLinkWidget(l).update();
		}
	}

	/* MODEL METHODS ******************************************************** */

	protected void addConcept(Concept concept) {
		ConceptWidget cw = conceptWidgetFactory.build(concept);
		conceptWidgets.put(concept, cw);
		getDiagramPanel().add(cw, concept.getPosition().getX(),
				concept.getPosition().getY());
		conceptDragController.makeDraggable(cw);
		actionDragController.makeDraggable(cw.getAdditionHandle());
		actionDragController.registerDropController(new LinkAddDropController(
				cw, eventBus, connectionController));
	}

	protected void changeConcept(Concept concept, ConceptName name) {
		ConceptWidget cw = getConceptWidget(concept);
		cw.setText(name.getValue());
	}

	protected void changeConcept(Concept concept, ConceptPosition position) {
		ConceptWidget cw = getConceptWidget(concept);
		int x = position.getX();
		int y = position.getY();
		getDiagramPanel().setWidgetPosition(cw, x, y);
		updateLinks(concept);
	}

	protected void removeConcept(Concept concept) {
		connectionController.reset();
		ConceptWidget cw = conceptWidgets.remove(concept);
		getDiagramPanel().remove(cw);
	}

	protected void addLink(Link link) {
		ConceptWidget cw1 = conceptWidgets.get(link.getStartConcept());
		ConceptWidget cw2 = conceptWidgets.get(link.getEndConcept());
		LinkWidget lw = linkWidgetFactory.build(link, cw1, cw2);
		linkWidgets.put(link, lw);
		lw.appendTo(getDiagramPanel());
		lw.addClickHandler(eventHandler);
	}

	protected void removeLink(Link link) {
		LinkWidget lw = linkWidgets.remove(link);
		getDiagramPanel().remove(lw);
	}

	/* PRESENTER METHODS **************************************************** */

	protected void onBind() {
		DiagramPanel panel = getDiagramPanel();
		panel.addDoubleClickHandler(eventHandler);

		conceptDropController = new ConceptMoveDropController(this, eventBus);
		conceptDragController = new PickupDragController(panel, true);
		conceptDragController.setBehaviorConstrainedToBoundaryPanel(false);
		conceptDragController.setBehaviorMultipleSelection(true);
		conceptDragController.registerDropController(conceptDropController);

		connectionController = new ConnectionController(getDiagramPanel());
		conceptAddDropController = new ConceptAddDropController(panel,
				eventBus, connectionController);
		actionDragController = new PickupDragController(panel, true);
		actionDragController.setBehaviorConstrainedToBoundaryPanel(false);
		actionDragController.setBehaviorMultipleSelection(false);
		actionDragController.registerDropController(conceptAddDropController);
	}

	@Override
	protected void onUnbind() {
	}

	@Override
	public void refreshDisplay() {
	}

	@Override
	public void revealDisplay() {
	}

	@Override
	public Place getPlace() {
		return null;
	}

	@Override
	protected void onPlaceRequest(PlaceRequest request) {
	}

	private class EventHandler implements ModelEventHandler, ClickHandler,
			DoubleClickHandler {
		@Override
		public void onConceptAdd(ConceptAddEvent event) {
			addConcept(event.getConcept());
		}

		@Override
		public void onConceptChange(ConceptChangeEvent conceptChangeEvent) {
			Concept c = conceptChangeEvent.getConcept();
			ConceptProperty p = conceptChangeEvent.getProperty();
			if (p instanceof ConceptName) {
				changeConcept(c, (ConceptName) p);
			} else if (p instanceof ConceptPosition) {
				changeConcept(c, (ConceptPosition) p);
			}
		}

		@Override
		public void onConceptRemove(ConceptRemoveEvent conceptRemoveEvent) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onLinkAdd(LinkAddEvent event) {
			addLink(event.getLink());
		}

		@Override
		public void onLinkRemove(LinkRemoveEvent event) {
			removeLink(event.getLink());
		}

		@Override
		public void onClick(ClickEvent event) {
			Object s = event.getSource();

			if (s instanceof LinkWidget) {
				LinkWidget lw = (LinkWidget) s;
				eventBus.fireEvent(new LinkRemoveRequestEvent(lw.getLink()));
			}
		}

		@Override
		public void onDoubleClick(DoubleClickEvent event) {
			AbsolutePanel p = getDiagramPanel();
			if (event.getSource() == p) {
				NativeEvent e = event.getNativeEvent();
				int x = e.getClientX() - p.getAbsoluteLeft();
				int y = e.getClientY() - p.getAbsoluteTop();

				ConceptName name = new ConceptName("concept");
				ConceptPosition position = new ConceptPosition(x, y);
				eventBus.fireEvent(new ConceptAddRequestEvent(null, name,
						position));
			}
		}
	}

	public interface Display extends WidgetDisplay {
		DiagramPanel getDiagramPanel();
	}
}
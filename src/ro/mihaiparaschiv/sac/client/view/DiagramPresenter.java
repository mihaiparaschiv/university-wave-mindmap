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
import ro.mihaiparaschiv.sac.client.event.request.ConceptChangeRequestEvent;
import ro.mihaiparaschiv.sac.client.event.request.ConceptRemoveRequestEvent;
import ro.mihaiparaschiv.sac.client.event.request.LinkRemoveRequestEvent;
import ro.mihaiparaschiv.sac.client.model.Concept;
import ro.mihaiparaschiv.sac.client.model.ConceptName;
import ro.mihaiparaschiv.sac.client.model.ConceptPosition;
import ro.mihaiparaschiv.sac.client.model.ConceptProperty;
import ro.mihaiparaschiv.sac.client.model.Link;
import ro.mihaiparaschiv.sac.client.view.dnd.ConceptAddDropController;
import ro.mihaiparaschiv.sac.client.view.dnd.ConceptMoveDropController;
import ro.mihaiparaschiv.sac.client.view.dnd.ConnectionHandler;
import ro.mihaiparaschiv.sac.client.view.dnd.LinkAddDropController;
import ro.mihaiparaschiv.sac.client.view.widget.ConceptNameWidget;
import ro.mihaiparaschiv.sac.client.view.widget.ConceptRemovalWidget;
import ro.mihaiparaschiv.sac.client.view.widget.ConceptWidget;
import ro.mihaiparaschiv.sac.client.view.widget.ConceptWidgetFactory;
import ro.mihaiparaschiv.sac.client.view.widget.LinkWidget;
import ro.mihaiparaschiv.sac.client.view.widget.LinkWidgetFactory;

import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.dnd.client.drop.DropController;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.AbsolutePanel;

public class DiagramPresenter extends WidgetPresenter<DiagramPresenter.Display> {
	private final HashMap<Concept, ConceptWidget> conceptWidgets;
	private final HashMap<Link, LinkWidget> linkWidgets;
	private final ConceptWidgetFactory conceptWidgetFactory;
	private final LinkWidgetFactory linkWidgetFactory;

	private PickupDragController conceptDragController;
	private ConceptMoveDropController conceptMoveDropController;
	private PickupDragController actionDragController;
	private ConceptAddDropController conceptAddDropController;
	private HashMap<ConceptWidget, LinkAddDropController> linkAddDropControllers;
	private EventHandler eventHandler;
	private ConnectionHandler connectionHandler;

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
		connectionHandler.reset();

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

		// prepare for drag and drop
		conceptDragController.makeDraggable(cw);
		actionDragController.makeDraggable(cw.getAdditionHandle());

		// make the concept widget a drop target
		LinkAddDropController dc = new LinkAddDropController(cw, eventBus,
				connectionHandler);
		linkAddDropControllers.put(cw, dc);
		actionDragController.registerDropController(dc);

		cw.getNameWidget().addValueChangeHandler(eventHandler);
		cw.getRemovalWidget().addClickHandler(eventHandler);
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
		ConceptWidget cw = conceptWidgets.get(concept);
		
		// unregister the widget
		DropController dc = linkAddDropControllers.get(cw);
		actionDragController.unregisterDropController(dc);
		
		// remove the widget
		connectionHandler.reset();
		conceptWidgets.remove(concept);
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

		// concept moving - target: diagram panel
		conceptMoveDropController = new ConceptMoveDropController(this, eventBus);
		conceptDragController = new PickupDragController(panel, true);
		conceptDragController.setBehaviorConstrainedToBoundaryPanel(false);
		conceptDragController.setBehaviorMultipleSelection(true);
		conceptDragController.registerDropController(conceptMoveDropController);

		// concept building - target: diagram panel
		connectionHandler = new ConnectionHandler(getDiagramPanel());
		conceptAddDropController = new ConceptAddDropController(panel,
				eventBus, connectionHandler);
		actionDragController = new PickupDragController(panel, true);
		actionDragController.setBehaviorConstrainedToBoundaryPanel(false);
		actionDragController.setBehaviorMultipleSelection(false);
		actionDragController.registerDropController(conceptAddDropController);
		
		// link / concept building - target: concept widget
		linkAddDropControllers = new HashMap<ConceptWidget, LinkAddDropController>();
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
			DoubleClickHandler, ValueChangeHandler<String> {
		@Override
		public void onConceptAdd(ConceptAddEvent event) {
			addConcept(event.getConcept());
		}

		@Override
		public void onConceptChange(ConceptChangeEvent event) {
			Concept c = event.getConcept();
			ConceptProperty p = event.getProperty();
			if (p instanceof ConceptName) {
				changeConcept(c, (ConceptName) p);
			} else if (p instanceof ConceptPosition) {
				changeConcept(c, (ConceptPosition) p);
			}
		}

		@Override
		public void onConceptRemove(ConceptRemoveEvent event) {
			removeConcept(event.getConcept());
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
			} else if (s instanceof ConceptRemovalWidget) {
				ConceptWidget cw = ((ConceptRemovalWidget) s)
						.getConceptWidget();
				eventBus.fireEvent(new ConceptRemoveRequestEvent(cw
						.getConcept()));
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

		@Override
		public void onValueChange(ValueChangeEvent<String> event) {
			Object s = event.getSource();

			if (s instanceof ConceptNameWidget) {
				ConceptWidget cw = ((ConceptNameWidget) s).getConceptWidget();
				eventBus.fireEvent(new ConceptChangeRequestEvent(cw
						.getConcept(), new ConceptName(event.getValue())));
			}
		}
	}

	public interface Display extends WidgetDisplay {
		DiagramPanel getDiagramPanel();
	}
}
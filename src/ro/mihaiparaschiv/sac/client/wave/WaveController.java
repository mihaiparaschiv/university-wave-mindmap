package ro.mihaiparaschiv.sac.client.wave;

import java.util.HashMap;

import net.customware.gwt.presenter.client.EventBus;

import org.cobogw.gwt.waveapi.gadget.client.State;
import org.cobogw.gwt.waveapi.gadget.client.StateUpdateEvent;
import org.cobogw.gwt.waveapi.gadget.client.StateUpdateEventHandler;
import org.cobogw.gwt.waveapi.gadget.client.WaveFeature;

import ro.mihaiparaschiv.sac.client.event.model.ConceptAddEvent;
import ro.mihaiparaschiv.sac.client.event.model.ConceptChangeEvent;
import ro.mihaiparaschiv.sac.client.event.model.ConceptRemoveEvent;
import ro.mihaiparaschiv.sac.client.event.model.LinkAddEvent;
import ro.mihaiparaschiv.sac.client.event.model.LinkRemoveEvent;
import ro.mihaiparaschiv.sac.client.event.request.ConceptAddRequestEvent;
import ro.mihaiparaschiv.sac.client.event.request.ConceptChangeRequestEvent;
import ro.mihaiparaschiv.sac.client.event.request.ConceptRemoveRequestEvent;
import ro.mihaiparaschiv.sac.client.event.request.LinkAddRequestEvent;
import ro.mihaiparaschiv.sac.client.event.request.LinkRemoveRequestEvent;
import ro.mihaiparaschiv.sac.client.event.request.RequestEventHandler;
import ro.mihaiparaschiv.sac.client.model.Concept;
import ro.mihaiparaschiv.sac.client.model.ConceptId;
import ro.mihaiparaschiv.sac.client.model.ConceptMap;
import ro.mihaiparaschiv.sac.client.model.ConceptName;
import ro.mihaiparaschiv.sac.client.model.ConceptPosition;
import ro.mihaiparaschiv.sac.client.model.ConceptProperty;
import ro.mihaiparaschiv.sac.client.model.Link;
import ro.mihaiparaschiv.sac.client.model.LinkId;
import ro.mihaiparaschiv.sac.client.model.User;
import ro.mihaiparaschiv.sac.client.model.UserRegistry;

import com.google.gwt.core.client.JsArrayString;

/**
 * This is the main controller. It handles: wave state updates (parsing and
 * novelty detection), delta submit operations and internal model building.
 * 
 * TODO: Split the class.
 */
public class WaveController implements StateUpdateEventHandler {
	private static final String MODEL_CONCEPT = "concept";
	private static final String MODEL_LINK = "link";
	private static final String MODEL_USER = "user";

	public static final String PROPERTY_NAME = "name";
	public static final String PROPERTY_POSITION = "position";
	public static final String PROPERTY_COUNTER = "counter";

	private final WaveFeature wave;
	private final ConceptMap conceptMap;
	private final EventBus eventBus;
	private final UserRegistry userRegistry;
	private final WaveValueTransformer valueTransformer;
	private final EventHandler eventHandler;
	private final AccumulatorHandler accumulatorHandler;
	private HashMap<String, String> oldState;
	private int userConceptCounter = 0;

	public WaveController(WaveFeature wave, ConceptMap conceptMap,
			EventBus eventBus, UserRegistry userRegistry) {
		this.wave = wave;
		this.conceptMap = conceptMap;
		this.eventBus = eventBus;
		this.userRegistry = userRegistry;
		this.valueTransformer = new WaveValueTransformer();
		this.eventHandler = new EventHandler();
		this.oldState = new HashMap<String, String>();
		this.accumulatorHandler = new AccumulatorHandler(conceptMap);

		wave.addStateUpdateEventHandler(this);
		eventBus.addHandler(ConceptAddRequestEvent.TYPE, eventHandler);
		eventBus.addHandler(ConceptChangeRequestEvent.TYPE, eventHandler);
		eventBus.addHandler(ConceptRemoveRequestEvent.TYPE, eventHandler);
		eventBus.addHandler(LinkAddRequestEvent.TYPE, eventHandler);
		eventBus.addHandler(LinkRemoveRequestEvent.TYPE, eventHandler);
	}

	/* PARSER METHODS ******************************************************* */

	/**
	 * Parses properties and builds internal models.
	 * 
	 * @param key
	 * @param value
	 */
	private void processConceptProperty(String key, String value) {
		String[] a = key.split(":");
		ConceptId id = new ConceptId(userRegistry.get(a[0]), a[1]);
		String type = a[2];

		Concept concept = conceptMap.getConcept(id);

		if (type.equals(PROPERTY_NAME)) {
			ConceptName name = valueTransformer.parseName(value);

			if (concept != null) {
				eventBus.fireEvent(new ConceptChangeEvent(concept, name));
			} else {
				accumulatorHandler.assureConcept(id).setName(name);
			}
		} else if (type.equals(PROPERTY_POSITION)) {
			ConceptPosition position = valueTransformer.parsePosition(value);

			if (concept != null) {
				eventBus.fireEvent(new ConceptChangeEvent(concept, position));
			} else {
				accumulatorHandler.assureConcept(id).setPosition(position);
			}
		}

		if (concept == null) {
			ConceptAccumulator ca = accumulatorHandler.assureConcept(id);
			if (ca.isComplete()) {
				accumulatorHandler.removeConcept(id);
				concept = new Concept(conceptMap, id, ca.getName(), ca
						.getPosition());
				eventBus.fireEvent(new ConceptAddEvent(concept));
			}
		}
	}

	private void processMissingConceptKey(String key) {
		String[] a = key.split(":");
		ConceptId id = new ConceptId(userRegistry.get(a[0]), a[1]);

		accumulatorHandler.removeConcept(id);

		Concept concept = conceptMap.getConcept(id);
		if (concept != null) {
			// TODO: remove links based on wave updates
			for (Link link : concept.getLinks()) {
				link.remove();
				eventBus.fireEvent(new LinkRemoveEvent(link));
			}
			concept.remove();
			eventBus.fireEvent(new ConceptRemoveEvent(concept));
		}
	}

	private void processLink(String key) {
		String[] a = key.split(":");
		ConceptId startId = new ConceptId(userRegistry.get(a[0]), a[1]);
		ConceptId endId = new ConceptId(userRegistry.get(a[2]), a[3]);
		accumulatorHandler.assureLink(new LinkId(startId, endId));
	}

	private void processMissingLinkKey(String key) {
		String[] a = key.split(":");
		ConceptId startId = new ConceptId(userRegistry.get(a[0]), a[1]);
		ConceptId endId = new ConceptId(userRegistry.get(a[2]), a[3]);
		LinkId id = new LinkId(startId, endId);

		accumulatorHandler.removeLink(id);

		Link link = conceptMap.getLink(id);
		if (link != null) {
			link.remove();
			eventBus.fireEvent(new LinkRemoveEvent(link));
		}
	}

	private void processUserProperty(String key, String value) {
		String[] a = key.split(":");
		User user = userRegistry.get(a[0]);
		String type = a[1];

		if (user.equals(userRegistry.getCurrentUser())
				&& type == PROPERTY_COUNTER) {
			userConceptCounter = Integer.parseInt(value);
		}
	}

	/* UTILITY METHODS ****************************************************** */

	/**
	 * Builds the key for a concept variable.
	 * 
	 * @param id
	 * @param propertyType
	 * @return
	 */
	private String buildConceptKey(ConceptId id, String propertyType) {
		return MODEL_CONCEPT + "::" + id.getUser().getName() + ":"
				+ id.getUserDomainId() + ":" + propertyType;
	}

	/**
	 * Builds the key for a user variable.
	 * 
	 * @param user
	 * @param propertyCounter
	 * @return
	 */
	private String buildUserKey(User user, String propertyType) {
		return MODEL_USER + "::" + user.getName() + ":" + propertyType;
	}

	private String buildLinkKey(LinkId id) {
		String sn = id.getStartConceptId().getUser().getName();
		String sid = id.getStartConceptId().getUserDomainId();
		String en = id.getEndConceptId().getUser().getName();
		String eid = id.getEndConceptId().getUserDomainId();

		// sort the fields so that we have only one link between two concepts
		String c1 = sn + ":" + sid;
		String c2 = en + ":" + eid;
		if (c1.compareTo(c2) > 0) {
			String aux = c1;
			c1 = c2;
			c2 = aux;
		}

		return MODEL_LINK + "::" + c1 + ":" + c2;
	}

	/**
	 * Generates a concept id and updates the wave.
	 * 
	 * @param state
	 * @return
	 */
	private ConceptId generateConceptId(State state) {
		User user = userRegistry.getCurrentUser();
		String idString = Integer.toString(++userConceptCounter);
		state.submitValue(buildUserKey(user, PROPERTY_COUNTER), idString);
		return new ConceptId(user, idString);
	}

	/**
	 * Creates the internal link models.
	 */
	private void processCompleteLinks() {
		for (LinkAccumulator la : accumulatorHandler.getCompleteLinks()) {
			accumulatorHandler.removeLink(la.getId());
			Link link = new Link(conceptMap, la.getId());
			eventBus.fireEvent(new LinkAddEvent(link));
		}
	}

	/* EVENT HANDLING ******************************************************* */

	@Override
	public void onUpdate(StateUpdateEvent event) {
		State state = event.getState();
		HashMap<String, String> newState = new HashMap<String, String>();

		JsArrayString keys = event.getState().getKeys();
		for (int i = 0; i < keys.length(); i++) {
			String key = keys.get(i);
			String newValue = state.get(key);

			String oldValue = oldState.get(key);
			if (!newValue.equals(oldValue)) {
				// update the model
				String[] a = key.split("::");
				String modelType = a[0];
				String modelKey = a[1];

				if (modelType.equals(MODEL_CONCEPT)) {
					processConceptProperty(modelKey, newValue);
				} else if (modelType.equals(MODEL_LINK)) {
					processLink(modelKey);
				} else if (modelType.equals(MODEL_USER)) {
					processUserProperty(modelKey, newValue);
				}
			}

			newState.put(key, newValue);

			// remove the used key from the old state
			oldState.remove(key);
		}

		// remove the models corresponding to keys not found in the new state
		for (String key : oldState.keySet()) {
			String[] a = key.split("::");
			String modelType = a[0];
			String modelKey = a[1];

			if (modelType.equals(MODEL_CONCEPT)) {
				processMissingConceptKey(modelKey);
			} else if (modelType.equals(MODEL_LINK)) {
				processMissingLinkKey(modelKey);
			}
		}

		// build the links
		processCompleteLinks();

		oldState = newState;
	}

	private class EventHandler implements RequestEventHandler {
		@Override
		public void onConceptAddRequest(ConceptAddRequestEvent event) {
			ConceptName name = event.getName();
			ConceptPosition position = event.getPosition();
			ConceptId id = generateConceptId(wave.getState());

			HashMap<String, String> delta = new HashMap<String, String>();
			delta.put(buildConceptKey(id, PROPERTY_NAME), //
					valueTransformer.build(name));
			delta.put(buildConceptKey(id, PROPERTY_POSITION), //
					valueTransformer.build(position));

			Concept parentConcept = event.getParentConcept();
			if (parentConcept != null) {
				ConceptId startConceptId = parentConcept.getId();
				ConceptId endConceptId = id;
				String key = buildLinkKey(new LinkId(startConceptId,
						endConceptId));
				delta.put(key, "value");
			}

			wave.getState().submitDelta(delta);
		}

		@Override
		public void onConceptChangeRequest(ConceptChangeRequestEvent event) {
			Concept concept = event.getConcept();
			ConceptProperty property = event.getProperty();

			String propertyName = null;
			String value = null;

			if (property instanceof ConceptName) {
				propertyName = PROPERTY_NAME;
				value = valueTransformer.build((ConceptName) property);
			} else if (property instanceof ConceptPosition) {
				propertyName = PROPERTY_POSITION;
				value = valueTransformer.build((ConceptPosition) property);
			}
			String key = buildConceptKey(concept.getId(), propertyName);

			wave.getState().submitValue(key, value);
		}

		@Override
		public void onConceptRemoveRequest(ConceptRemoveRequestEvent event) {
			Concept concept = event.getConcept();
			ConceptId id = concept.getId();

			HashMap<String, String> delta = new HashMap<String, String>();
			delta.put(buildConceptKey(id, PROPERTY_NAME), null);
			delta.put(buildConceptKey(id, PROPERTY_POSITION), null);

			for (Link link : concept.getLinks()) {
				delta.put(buildLinkKey(link.getId()), null);
			}

			wave.getState().submitDelta(delta);
		}

		@Override
		public void onLinkAddRequest(LinkAddRequestEvent event) {
			Concept startConcept = event.getStartConcept();
			Concept endConcept = event.getEndConcept();
			String key = buildLinkKey(new LinkId(startConcept, endConcept));
			wave.getState().submitValue(key, "value");
		}

		@Override
		public void onLinkRemoveRequest(LinkRemoveRequestEvent event) {
			String key = buildLinkKey(event.getLink().getId());
			wave.getState().submitValue(key, null);
		}
	}
}

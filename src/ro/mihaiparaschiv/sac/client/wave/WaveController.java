package ro.mihaiparaschiv.sac.client.wave;

import java.util.HashMap;

import net.customware.gwt.presenter.client.EventBus;

import org.cobogw.gwt.waveapi.gadget.client.State;
import org.cobogw.gwt.waveapi.gadget.client.StateUpdateEvent;
import org.cobogw.gwt.waveapi.gadget.client.StateUpdateEventHandler;
import org.cobogw.gwt.waveapi.gadget.client.WaveFeature;

import ro.mihaiparaschiv.sac.client.event.model.ConceptAddEvent;
import ro.mihaiparaschiv.sac.client.event.model.ConceptChangeEvent;
import ro.mihaiparaschiv.sac.client.event.request.ConceptAddRequestEvent;
import ro.mihaiparaschiv.sac.client.event.request.ConceptChangeRequestEvent;
import ro.mihaiparaschiv.sac.client.event.request.ConceptRemoveRequestEvent;
import ro.mihaiparaschiv.sac.client.event.request.LinkAddRequestEvent;
import ro.mihaiparaschiv.sac.client.event.request.LinkRemoveRequestEvent;
import ro.mihaiparaschiv.sac.client.event.request.RequestEventHandler;
import ro.mihaiparaschiv.sac.client.model.Concept;
import ro.mihaiparaschiv.sac.client.model.ConceptMap;
import ro.mihaiparaschiv.sac.client.model.ConceptName;
import ro.mihaiparaschiv.sac.client.model.ConceptPosition;
import ro.mihaiparaschiv.sac.client.model.ConceptProperty;
import ro.mihaiparaschiv.sac.client.model.User;
import ro.mihaiparaschiv.sac.client.model.UserRegistry;

import com.google.gwt.core.client.JsArrayString;

public class WaveController implements StateUpdateEventHandler {
	private static final String MODEL_CONCEPT = "concept";
	private static final String MODEL_LINK = "link";

	public static final String PROPERTY_REMOVED = "removed";
	public static final String PROPERTY_NAME = "name";
	public static final String PROPERTY_POSITION = "position";

	private final WaveFeature wave;
	private final ConceptMap conceptMap;
	private final EventBus eventBus;
	private final UserRegistry userRegistry;
	private final WaveValueTransformer valueTransformer;
	private final HashMap<User, HashMap<String, WaveConceptModel>> concepts;
	private final EventHandler eventHandler;

	public WaveController(WaveFeature wave, ConceptMap conceptMap,
			EventBus eventBus, UserRegistry userRegistry) {
		this.wave = wave;
		this.conceptMap = conceptMap;
		this.eventBus = eventBus;
		this.userRegistry = userRegistry;
		this.valueTransformer = new WaveValueTransformer();
		this.concepts = new HashMap<User, HashMap<String, WaveConceptModel>>();
		this.eventHandler = new EventHandler();

		wave.addStateUpdateEventHandler(this);
		eventBus.addHandler(ConceptAddRequestEvent.TYPE, eventHandler);
		eventBus.addHandler(ConceptChangeRequestEvent.TYPE, eventHandler);
		eventBus.addHandler(ConceptRemoveRequestEvent.TYPE, eventHandler);
		eventBus.addHandler(LinkAddRequestEvent.TYPE, eventHandler);
		eventBus.addHandler(LinkRemoveRequestEvent.TYPE, eventHandler);
	}

	/**
	 * Parses properties and builds internal models.
	 * 
	 * @param key
	 * @param value
	 */
	private void processProperty(String key, String value) {
		String[] a = key.split(":");
		User user = userRegistry.get(a[0]);
		String modelType = a[1];
		String modelId = a[2];
		String propertyType = a[3];
		String propertyValue = value;

		if (modelType.equals(MODEL_CONCEPT)) {
			WaveConceptModel wcm = assureWaveConcept(user, modelId);

			Concept concept = null;
			if (wcm.isComplete()) {
				concept = conceptMap.getConcept(user, modelId);
			}

			if (propertyType.equals(PROPERTY_REMOVED)) {
				wcm.remove();

				// dont't delete incomplete models
				if (concept != null) {
					// delete model
				}
			} else if (propertyType.equals(PROPERTY_NAME)) {
				ConceptName name = valueTransformer.parseName(propertyValue);
				wcm.setName(name);

				if (concept != null) {
					eventBus.fireEvent(new ConceptChangeEvent(concept,
							name));
				}
			} else if (propertyType.equals(PROPERTY_POSITION)) {
				ConceptPosition position = valueTransformer
						.parsePosition(propertyValue);
				wcm.setPosition(position);

				if (concept != null) {
					eventBus.fireEvent(new ConceptChangeEvent(concept,
							position));
				}
			}

			// build only models that have not been marked as removed
			if (concept == null && wcm.isComplete() && !wcm.isRemoved()) {
				concept = new Concept(conceptMap, modelId, user, wcm.getName(),
						wcm.getPosition());
				eventBus.fireEvent(new ConceptAddEvent(concept));
			}
		}
	}

	/* UTILITY METHODS ****************************************************** */

	/**
	 * Make sure we have the desired wave concept.
	 * 
	 * @param user
	 * @param modelId
	 * @return
	 */
	private WaveConceptModel assureWaveConcept(User user, String modelId) {
		HashMap<String, WaveConceptModel> cmap = concepts.get(user);
		if (cmap == null) {
			cmap = new HashMap<String, WaveConceptModel>();
			concepts.put(user, cmap);
		}

		WaveConceptModel wcm = cmap.get(modelId);
		if (wcm == null) {
			wcm = new WaveConceptModel();
			cmap.put(modelId, wcm);
		}

		return wcm;
	}

	/**
	 * Builds the key for a concept wave variable.
	 * 
	 * @param user
	 * @param id
	 * @param propertyType
	 * @return
	 */
	private String buildConceptKey(User user, String id, String propertyType) {
		return user.getName() + ":" + MODEL_CONCEPT + ":" + id + ":"
				+ propertyType;
	}

	private int currentId = 1;

	/**
	 * Generates a concept id.
	 * 
	 * @return
	 */
	private String generateConceptId() {
		return "" + currentId++;
	}

	/* EVENT HANDLING ******************************************************* */

	@Override
	public void onUpdate(StateUpdateEvent event) {
		State state = event.getState();
		JsArrayString keys = event.getState().getKeys();
		for (int i = 0; i < keys.length(); i++) {
			String key = keys.get(i);
			String value = state.get(key);
			processProperty(key, value);
		}
	}

	private class EventHandler implements RequestEventHandler {
		@Override
		public void onConceptAddRequest(ConceptAddRequestEvent event) {
			ConceptName name = event.getName();
			ConceptPosition position = event.getPosition();
			User user = userRegistry.getCurrentUser();
			String id = generateConceptId();
			
			HashMap<String, String> delta = new HashMap<String, String>();
			delta.put(buildConceptKey(user, id, PROPERTY_NAME), //
					valueTransformer.build(name));
			delta.put(buildConceptKey(user, id, PROPERTY_POSITION), //
					valueTransformer.build(position));

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
			String key = buildConceptKey(concept.getUser(), concept.getId(),
					propertyName);
			
			wave.getState().submitValue(key, value);
		}

		@Override
		public void onConceptRemoveRequest(ConceptRemoveRequestEvent event) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onLinkAddRequest(LinkAddRequestEvent event) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onLinkRemoveRequest(LinkRemoveRequestEvent event) {
			// TODO Auto-generated method stub

		}
	}
}

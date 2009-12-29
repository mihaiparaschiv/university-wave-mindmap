package ro.mihaiparaschiv.sac.client.wave;

import java.util.HashMap;

import ro.mihaiparaschiv.sac.client.model.ConceptId;

public class AccumulatorHandler {
	private final HashMap<ConceptId, ConceptAccumulator> concepts;

	public AccumulatorHandler() {
		concepts = new HashMap<ConceptId, ConceptAccumulator>();
	}

	/**
	 * Makes sure we have the desired wave concept.
	 * 
	 * @param user
	 * @param id
	 * @return
	 */
	public ConceptAccumulator getConcept(ConceptId id) {
		ConceptAccumulator ca = concepts.get(id);
		if (ca == null) {
			ca = new ConceptAccumulator();
			concepts.put(id, ca);
		}

		return ca;
	}

	public void removeConcept(ConceptId id) {
		concepts.remove(id);
	}
}

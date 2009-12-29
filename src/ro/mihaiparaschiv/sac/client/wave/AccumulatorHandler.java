package ro.mihaiparaschiv.sac.client.wave;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import ro.mihaiparaschiv.sac.client.model.ConceptId;
import ro.mihaiparaschiv.sac.client.model.ConceptMap;
import ro.mihaiparaschiv.sac.client.model.LinkId;

/**
 * Assures the accumulators for model building exist. The accumulators should be
 * removed after the internal models have been built.
 */
public class AccumulatorHandler {
	private final ConceptMap conceptMap;
	private final HashMap<ConceptId, ConceptAccumulator> concepts;
	private final HashMap<LinkId, LinkAccumulator> links;

	public AccumulatorHandler(ConceptMap conceptMap) {
		this.conceptMap = conceptMap;
		concepts = new HashMap<ConceptId, ConceptAccumulator>();
		links = new HashMap<LinkId, LinkAccumulator>();
	}

	public ConceptAccumulator assureConcept(ConceptId id) {
		ConceptAccumulator ca = concepts.get(id);
		if (ca == null) {
			ca = new ConceptAccumulator(id);
			concepts.put(id, ca);
		}
		return ca;
	}

	public void removeConcept(ConceptId id) {
		concepts.remove(id);
	}

	public LinkAccumulator assureLink(LinkId id) {
		LinkAccumulator la = links.get(id);
		if (la == null) {
			la = new LinkAccumulator(conceptMap, id);
			links.put(id, la);
		}
		return la;
	}

	public void removeLink(LinkId id) {
		links.remove(id);
	}

	public List<LinkAccumulator> getCompleteLinks() {
		List<LinkAccumulator> las = new LinkedList<LinkAccumulator>();
		for (LinkAccumulator la : links.values()) {
			if (la.isComplete()) {
				las.add(la);
			}
		}
		return las;
	}
}

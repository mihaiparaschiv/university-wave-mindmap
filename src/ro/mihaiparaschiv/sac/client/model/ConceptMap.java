package ro.mihaiparaschiv.sac.client.model;

import java.util.HashMap;

public class ConceptMap {
	private final HashMap<ConceptId, Concept> concepts;
	private final HashMap<Concept, HashMap<Concept, Link>> links;

	public ConceptMap() {
		concepts = new HashMap<ConceptId, Concept>();
		links = new HashMap<Concept, HashMap<Concept, Link>>();
	}

	protected void addConcept(Concept concept) {
		concepts.put(concept.getId(), concept);
		links.put(concept, new HashMap<Concept, Link>());
	}

	protected void removeConcept(Concept concept) {
		concepts.remove(concept.getId());
		// TODO cleanup user concepts

		HashMap<Concept, Link> map = links.remove(concept);
		for (Concept c : map.keySet()) {
			links.get(c).remove(concept);
		}
	}

	protected void addLink(Link link) {
		Concept c1 = link.getStartConcept();
		Concept c2 = link.getEndConcept();
		links.get(c1).put(c2, link);
		links.get(c2).put(c1, link);
	}

	protected void removeLink(Link link) {
		Concept c1 = link.getStartConcept();
		Concept c2 = link.getEndConcept();
		links.get(c1).remove(c2);
		links.get(c2).remove(c1);
	}

	protected HashMap<Concept, Link> getLinkMap(Concept concept) {
		return links.get(concept);
	}
	
	/* PUBLIC METHODS ********************************************************/

	public Concept getConcept(ConceptId id) {
		return concepts.get(id);
	}

	public Link getLink(LinkId id) {
		Concept startConcept = getConcept(id.getStartConceptId());
		Concept endConcept = getConcept(id.getEndConceptId());
		return links.get(startConcept).get(endConcept);
	}
}

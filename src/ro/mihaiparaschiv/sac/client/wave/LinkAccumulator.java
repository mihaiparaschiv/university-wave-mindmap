package ro.mihaiparaschiv.sac.client.wave;

import ro.mihaiparaschiv.sac.client.model.ConceptMap;
import ro.mihaiparaschiv.sac.client.model.LinkId;

class LinkAccumulator extends WaveModelAccumulator {
	private final ConceptMap conceptMap;
	private final LinkId id;

	public LinkAccumulator(ConceptMap conceptMap, LinkId id) {
		this.conceptMap = conceptMap;
		this.id = id;
	}

	@Override
	public boolean isComplete() {
		return conceptMap.getConcept(id.getStartConceptId()) != null
				&& conceptMap.getConcept(id.getEndConceptId()) != null;
	}
	
	public LinkId getId() {
		return id;
	}
}

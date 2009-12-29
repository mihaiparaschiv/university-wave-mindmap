package ro.mihaiparaschiv.sac.client.wave;

import ro.mihaiparaschiv.sac.client.model.ConceptId;
import ro.mihaiparaschiv.sac.client.model.ConceptMap;

class LinkAccumulator extends WaveModelAccumulator {
	private final ConceptMap conceptMap;
	private ConceptId startId;
	private ConceptId endId;

	public LinkAccumulator(ConceptMap conceptMap) {
		this.conceptMap = conceptMap;
	}

	@Override
	public boolean isComplete() {
		return conceptMap.getConcept(startId) != null
				&& conceptMap.getConcept(endId) != null;
	}
	
	public void setStartId(ConceptId id) {
		this.endId = id;
	}
	
	public void setEndId(ConceptId id) {
		this.endId = id;
	}
}

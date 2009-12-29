package ro.mihaiparaschiv.sac.client.model;

public class Link {
	private final ConceptMap conceptMap;
	private final LinkId id;

	public Link(ConceptMap conceptMap, LinkId id) {
		this.conceptMap = conceptMap;
		this.id = id;
		
		conceptMap.addLink(this);
	}
	
	public void remove() {
		conceptMap.removeLink(this);
	}
	
	public LinkId getId() {
		return id;
	}
	
	public Concept getStartConcept() {
		return conceptMap.getConcept(id.getStartConceptId());
	}

	public Concept getEndConcept() {
		return conceptMap.getConcept(id.getEndConceptId());
	}
}

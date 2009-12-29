package ro.mihaiparaschiv.sac.client.model;

public class LinkId {
	private ConceptId startConceptId;
	private ConceptId endConceptId;

	public LinkId(ConceptId startConceptId, ConceptId endConceptId) {
		this.startConceptId = startConceptId;
		this.endConceptId = endConceptId;
	}

	public LinkId(Concept startConcept, Concept endConcept) {
		this(startConcept.getId(), endConcept.getId());
	}

	public ConceptId getStartConceptId() {
		return startConceptId;
	}

	public ConceptId getEndConceptId() {
		return endConceptId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((endConceptId == null) ? 0 : endConceptId.hashCode());
		result = prime * result
				+ ((startConceptId == null) ? 0 : startConceptId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LinkId other = (LinkId) obj;
		if (endConceptId == null) {
			if (other.endConceptId != null)
				return false;
		} else if (!endConceptId.equals(other.endConceptId))
			return false;
		if (startConceptId == null) {
			if (other.startConceptId != null)
				return false;
		} else if (!startConceptId.equals(other.startConceptId))
			return false;
		return true;
	}
}

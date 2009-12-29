package ro.mihaiparaschiv.sac.client.model;

public class Link {
	private final ConceptMap conceptMap;
	private final String id;
	private final User user;
	private final Concept startConcept;
	private final Concept endConcept;

	public Link(ConceptMap conceptMap, String id, User user, Concept startConcept, Concept endConcept) {
		this.conceptMap = conceptMap;
		this.id = id;
		this.user = user;
		this.startConcept = startConcept;
		this.endConcept = endConcept;
		
		conceptMap.addLink(this);
	}
	
	public void remove() {
		conceptMap.removeLink(this);
	}
	
	public User getUser() {
		return user;
	}

	public Concept getStartConcept() {
		return startConcept;
	}

	public Concept getEndConcept() {
		return endConcept;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		Link other = (Link) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}

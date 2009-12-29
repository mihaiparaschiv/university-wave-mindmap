package ro.mihaiparaschiv.sac.client.model;

import java.util.Collection;

public class Concept {
	private final ConceptMap conceptMap;
	private final String id;
	private final User user;
	private ConceptName name;
	private ConceptPosition position;

	public Concept(ConceptMap conceptMap, String id, User user,
			ConceptName name, ConceptPosition position) {
		this.conceptMap = conceptMap;
		this.id = id;
		this.user = user;
		this.name = name;
		this.position = position;

		conceptMap.addConcept(this);
	}

	public void remove() {
		conceptMap.removeConcept(this);
	}

	public Link getLink(Concept concept) {
		return conceptMap.getLinkMap(this).get(concept);
	}

	public Collection<Link> getLinks() {
		return conceptMap.getLinkMap(this).values();
	}

	public String getId() {
		return id;
	}

	public User getUser() {
		return user;
	}

	public ConceptName getName() {
		return name;
	}

	public void setName(ConceptName name) {
		this.name = name;
	}

	public ConceptPosition getPosition() {
		return position;
	}

	public void setPosition(ConceptPosition position) {
		this.position = position;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((user == null) ? 0 : user.hashCode());
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
		Concept other = (Concept) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return user + ":" + id;
	}
}

package ro.mihaiparaschiv.sac.client.model;

public class ConceptId {
	private final User user;
	private final String userDomainId;

	public ConceptId(User user, String id) {
		this.user = user;
		this.userDomainId = id;
	}

	public User getUser() {
		return user;
	}

	public String getUserDomainId() {
		return userDomainId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((userDomainId == null) ? 0 : userDomainId.hashCode());
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
		ConceptId other = (ConceptId) obj;
		if (userDomainId == null) {
			if (other.userDomainId != null)
				return false;
		} else if (!userDomainId.equals(other.userDomainId))
			return false;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		return true;
	}
}

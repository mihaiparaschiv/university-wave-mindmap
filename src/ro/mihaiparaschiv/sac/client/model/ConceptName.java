package ro.mihaiparaschiv.sac.client.model;

public class ConceptName extends ConceptProperty {
	private final String value;

	public ConceptName(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
	
	@Override
	public String toString() {
		return value;
	}
}

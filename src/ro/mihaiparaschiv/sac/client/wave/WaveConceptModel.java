package ro.mihaiparaschiv.sac.client.wave;

import ro.mihaiparaschiv.sac.client.model.ConceptName;
import ro.mihaiparaschiv.sac.client.model.ConceptPosition;

class WaveConceptModel extends WaveModel {
	private ConceptName name = null;
	private ConceptPosition position = null;
	private boolean removed = false;

	public boolean isComplete() {
		return (name != null) && (position != null);
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
	
	public boolean isRemoved() {
		return removed;
	}
	
	public void remove() {
		this.removed = true;
	}
	
	@Override
	public String toString() {
		return name + ":" + position;
	}
}

package ro.mihaiparaschiv.sac.client.wave;

import ro.mihaiparaschiv.sac.client.model.ConceptName;
import ro.mihaiparaschiv.sac.client.model.ConceptPosition;

public class WaveValueTransformer {
	public ConceptName parseName(String s) {
		return new ConceptName(s);
	}

	public String build(ConceptName name) {
		return name.getValue();
	}

	public ConceptPosition parsePosition(String s) {
		String[] a = s.split(",");
		int x = Integer.parseInt(a[0]);
		int y = Integer.parseInt(a[1]);
		return new ConceptPosition(x, y);
	}

	public String build(ConceptPosition position) {
		return position.getX() + "," + position.getY();
	}
}

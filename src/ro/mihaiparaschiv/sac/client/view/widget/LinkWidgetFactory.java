package ro.mihaiparaschiv.sac.client.view.widget;

import pl.balon.gwt.diagrams.client.connector.Connector;
import pl.balon.gwt.diagrams.client.connector.UIObjectConnector;
import ro.mihaiparaschiv.sac.client.model.Link;

public class LinkWidgetFactory {
	public LinkWidget build(Link link, ConceptWidget cw1, ConceptWidget cw2) {
		Connector c1 = UIObjectConnector.wrap(cw1);
		Connector c2 = UIObjectConnector.wrap(cw2);
		LinkWidget c = new LinkWidget(link, c1, c2);
		return c;
	}
}

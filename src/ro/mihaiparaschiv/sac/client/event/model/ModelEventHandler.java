package ro.mihaiparaschiv.sac.client.event.model;

import com.google.gwt.event.shared.EventHandler;

public interface ModelEventHandler extends EventHandler {
	public void onConceptAdd(ConceptAddEvent event);
	
	public void onConceptChange(ConceptChangeEvent event);

	public void onConceptRemove(ConceptRemoveEvent event);

	public void onLinkAdd(LinkAddEvent event);

	public void onLinkRemove(LinkRemoveEvent event);
}

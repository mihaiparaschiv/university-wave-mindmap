package ro.mihaiparaschiv.sac.client.event.request;

import com.google.gwt.event.shared.EventHandler;

public interface RequestEventHandler extends EventHandler {
	public void onConceptAddRequest(ConceptAddRequestEvent event);

	public void onConceptChangeRequest(ConceptChangeRequestEvent event);

	public void onConceptRemoveRequest(ConceptRemoveRequestEvent event);

	public void onLinkRemoveRequest(LinkRemoveRequestEvent event);

	public void onLinkAddRequest(LinkAddRequestEvent event);
}

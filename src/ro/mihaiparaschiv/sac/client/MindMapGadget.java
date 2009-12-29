package ro.mihaiparaschiv.sac.client;

import net.customware.gwt.presenter.client.DefaultEventBus;
import net.customware.gwt.presenter.client.EventBus;

import org.cobogw.gwt.waveapi.gadget.client.WaveGadget;

import ro.mihaiparaschiv.sac.client.model.ConceptMap;
import ro.mihaiparaschiv.sac.client.model.UserRegistry;
import ro.mihaiparaschiv.sac.client.view.DiagramDisplay;
import ro.mihaiparaschiv.sac.client.view.DiagramPresenter;
import ro.mihaiparaschiv.sac.client.wave.WaveController;

import com.google.gwt.gadgets.client.DynamicHeightFeature;
import com.google.gwt.gadgets.client.NeedsDynamicHeight;
import com.google.gwt.gadgets.client.UserPreferences;
import com.google.gwt.gadgets.client.Gadget.ModulePrefs;
import com.google.gwt.user.client.ui.RootPanel;

@ModulePrefs(//
title = "Mind Map Gadget", //
author = "Mihai Paraschiv", //
author_email = "mihai.paraschiv@gmail.com")
public class MindMapGadget extends WaveGadget<UserPreferences> implements
		NeedsDynamicHeight {
	private static final int WIDTH = 400;
	private static final int HEIGHT = 400;

	private DynamicHeightFeature dynamicHeightFeature;

	protected void init(UserPreferences preferences) {
		EventBus eventBus = new DefaultEventBus();
		ConceptMap conceptMap = new ConceptMap();
		UserRegistry userRegistry = new UserRegistry(getWave());

		DiagramDisplay display = new DiagramDisplay(WIDTH, HEIGHT);
		DiagramPresenter presenter = new DiagramPresenter(display, eventBus);
		presenter.bind();
		RootPanel.get().add(presenter.getDisplay().asWidget());

		new WaveController(getWave(), conceptMap, eventBus, userRegistry);

		dynamicHeightFeature.adjustHeight();
	}

	public void initializeFeature(DynamicHeightFeature feature) {
		dynamicHeightFeature = feature;
	}
}
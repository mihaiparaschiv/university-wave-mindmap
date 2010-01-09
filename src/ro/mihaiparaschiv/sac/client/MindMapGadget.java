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
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

@ModulePrefs(//
title = "Mind Map Gadget", //
author = "Mihai Paraschiv", //
author_email = "mihai.paraschiv@gmail.com")
public class MindMapGadget extends WaveGadget<UserPreferences> implements
		NeedsDynamicHeight {
	private static final int WIDTH = 700;
	private static final int HEIGHT = 500;
	private static final String INFO = "Create a concept: " +
			"double click on the map or drag the \"add\" handle of an existing concept.<br/>" +
			"Create a link: use the \"add\" handle to join two concepts.<br/>" +
			"Remove a link: click on the link.";

	private DynamicHeightFeature dynamicHeightFeature;

	protected void init(UserPreferences preferences) {
		EventBus eventBus = new DefaultEventBus();
		ConceptMap conceptMap = new ConceptMap();
		UserRegistry userRegistry = new UserRegistry(getWave());

		VerticalPanel panel = new VerticalPanel();
		HTML info = new HTML(INFO);
		panel.add(info);

		DiagramDisplay display = new DiagramDisplay(WIDTH, HEIGHT);
		DiagramPresenter presenter = new DiagramPresenter(display, eventBus);
		presenter.bind();
		panel.add(presenter.getDisplay().asWidget());
		RootPanel.get().add(panel);

		new WaveController(getWave(), conceptMap, eventBus, userRegistry);

		dynamicHeightFeature.adjustHeight();
	}

	public void initializeFeature(DynamicHeightFeature feature) {
		dynamicHeightFeature = feature;
	}
}

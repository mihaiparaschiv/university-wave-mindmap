package ro.mihaiparaschiv.sac.client.model;

import java.util.HashMap;

import org.cobogw.gwt.waveapi.gadget.client.StateUpdateEvent;
import org.cobogw.gwt.waveapi.gadget.client.StateUpdateEventHandler;
import org.cobogw.gwt.waveapi.gadget.client.WaveFeature;

public class UserRegistry implements StateUpdateEventHandler {
	private final HashMap<String, User> users;
	private final WaveFeature wave;
	private User currentUser;

	public UserRegistry(WaveFeature wave) {
		this.users = new HashMap<String, User>();
		this.wave = wave;

		wave.addStateUpdateEventHandler(this);
	}

	/**
	 * Builds a user with the given name if one does not exist.
	 * 
	 * @param name
	 * @return
	 */
	public User get(String name) {
		User user = users.get(name);
		
		if (user == null) {
			user = new User(name);
			users.put(name, user);
		}

		return user;
	}

	public User getCurrentUser() {
		return get(wave.getViewer().getId());
	}

	@Override
	public void onUpdate(StateUpdateEvent event) {
		// TODO check future wave api versions to see if this is necessary
		if (currentUser == null) {
			currentUser = get(wave.getViewer().getId());
		}
	}
}

package net.suntrans.haipopeiwang.mdns.api;

import org.json.JSONArray;

public interface JmdnsListener {
	public void onJmdnsFind(int code, JSONArray deviceJsonString);
}

package com.brotherlogic.ditr.datatypes;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public class User {
    private final String name;
    private State currentState;
    private final Long lastUpdate;
    private final String id;
    private final String accessToken;

    public User(String id, String nme, State state, Long update, String accessToken) {
        this.id = id;
        this.name = nme;
        this.currentState = state;
        this.lastUpdate = update;
        this.accessToken = accessToken;
    }

    public void forceVenue(Venue v) {
        currentState = new State(v.getVenueId(), System.currentTimeMillis() + 60 * 1000);
        System.out.println("NEW STATE = " + currentState.convertToJson());
    }

    public JsonObject convertToJson() {
        System.out.println("HERE = " + id);

        JsonObject obj = new JsonObject();
        obj.add("name", new JsonPrimitive(name));
        obj.add("last_update", new JsonPrimitive(lastUpdate));
        obj.add("id", new JsonPrimitive(id));
        obj.add("state", currentState.convertToJson());

        return obj;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getId() {
        return id;
    }

    public Long getLastUpdate() {
        return lastUpdate;
    }

    public String getName() {
        return name;
    }

    public State getState() {
        return currentState;
    }

}

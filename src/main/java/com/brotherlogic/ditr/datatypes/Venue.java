package com.brotherlogic.ditr.datatypes;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public class Venue {
    String venueId;

    public Venue(String venueId) {
        this.venueId = venueId;
    }

    public JsonObject convertToJson() {
        JsonObject obj = new JsonObject();
        obj.add("venue_id", new JsonPrimitive(venueId));
        return obj;
    }

    public String getVenueId() {
        return venueId;
    }
}

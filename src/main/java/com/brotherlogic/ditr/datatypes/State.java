package com.brotherlogic.ditr.datatypes;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public class State {
    String venueID;
    long timestamp;

    public State(String venue, Long timestamp) {
        this.venueID = venue;
        this.timestamp = timestamp;
        System.out.println(venue + " and also " + timestamp);
    }

    public State(String venId) {
        Thread.dumpStack();
        venueID = venId;
        if (venId.length() > 0)
            timestamp = System.currentTimeMillis();
        else
            timestamp = -1;
    }

    public State() {
        Thread.dumpStack();
        venueID = "";
        timestamp = -1L;
    }

    public JsonObject convertToJson() {
        JsonObject obj = new JsonObject();
        obj.add("venue_id", new JsonPrimitive(venueID));
        obj.add("timestamp", new JsonPrimitive(timestamp));

        return obj;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getVenueID() {
        return venueID;
    }

    public void unlock() {
        venueID = "";
        timestamp = -1L;
    }
}

package com.brotherlogic.ditr.services.foursquare;

import java.io.IOException;

import com.brotherlogic.ditr.datatypes.State;
import com.brotherlogic.ditr.datatypes.User;
import com.brotherlogic.ditr.datatypes.Venue;
import com.brotherlogic.ditr.services.RemoteInformation;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public abstract class FoursquareRemote implements RemoteInformation {
    String oauthCode;

    public FoursquareRemote(String oauth) {
        oauthCode = oauth;
    }

    public boolean hasVisitedVenue(User user, String venueID) throws IOException {
        JsonObject venObj = get(oauthCode, "venues/" + venueID);

        JsonElement count = venObj.get("response").getAsJsonObject().get("venue").getAsJsonObject().get("beenHere");

        System.out.println("VCOUNT = " + count);
        return count != null;
    }

    public User login() throws IOException {
        // Get the self
        JsonObject self = get(oauthCode, "users/self");
        JsonObject userObj = self.get("response").getAsJsonObject().get("user").getAsJsonObject();

        // Build out the components for the user
        System.out.println("OBHJ = " + userObj);
        String id = userObj.get("id").getAsString();
        String name = userObj.get("firstName").getAsString() + " " + userObj.get("lastName").getAsString();
        State currentState = new State();
        Long lastUpdate = -1L;

        User user = new User(id, name, currentState, lastUpdate, oauthCode);
        return user;
    }

    public Venue runQuery(String cuisine, boolean newPlace, double radiusInMeters, double lat, double lon) throws IOException {
        JsonObject resp = get(oauthCode, "venues/search?ll=" + lat + "," + lon + "&limit=50&radius=" + radiusInMeters + "&query=" + cuisine);

        JsonArray venues = resp.get("response").getAsJsonObject().get("venues").getAsJsonArray();
        if (venues.size() == 0)
            return null;
        else {
            int rIndex = (int) Math.floor(Math.random() * venues.size());
            JsonObject chosenVenue = venues.get(rIndex).getAsJsonObject();
            Venue v = new Venue(chosenVenue.get("id").getAsString());
            return v;
        }
    }

    protected abstract JsonObject get(String oauthCode, String path) throws IOException;

}

package com.brotherlogic.ditr.services;

import java.io.IOException;

import com.brotherlogic.ditr.datatypes.User;
import com.brotherlogic.ditr.datatypes.Venue;

public interface RemoteInformation {
    boolean hasVisitedVenue(User user, String venueID) throws IOException;

    User login() throws IOException;

    Venue runQuery(String cuisine, boolean newPlace, double radiusInMiles, double lat, double lon) throws IOException;
}

package com.brotherlogic.ditr.services;

import java.io.IOException;

import com.brotherlogic.ditr.datatypes.User;
import com.brotherlogic.ditr.services.foursquare.FoursquareRemoteProduction;
import com.brotherlogic.ditr.services.mongo.MongoBridge;

public class SystemManager {
    public User getUser(String accessCode) throws IOException {
        User u = null;

        // Retrieve the locally stored user
        MongoBridge local = new MongoBridge();
        FoursquareRemoteProduction remote = new FoursquareRemoteProduction(accessCode);
        u = local.getUserFromToken(accessCode);

        System.out.println("GOT " + u);

        if (u != null) {
            // Refresh the user lock
            if (u.getState().getVenueID().length() > 0) {
                if (remote.hasVisitedVenue(u, u.getState().getVenueID()) || System.currentTimeMillis() > u.getState().getTimestamp()) {
                    System.out.println(System.currentTimeMillis());
                    System.out.println(u.getState().getTimestamp());
                    u.getState().unlock();
                    local.storeUser(u);
                    System.out.println("Unlocked user");

                }
            }
        } else {
            // Grab the user and store
            u = remote.login();
            local.storeUser(u);
        }

        return u;
    }
}

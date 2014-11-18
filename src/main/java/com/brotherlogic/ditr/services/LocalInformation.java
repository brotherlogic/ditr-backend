package com.brotherlogic.ditr.services;

import com.brotherlogic.ditr.datatypes.User;
import com.brotherlogic.ditr.datatypes.Venue;

public interface LocalInformation {
    User getUser(String ident);

    User getUserFromToken(String token);

    boolean lockUser(User u, Venue v);

    boolean storeUser(User u);

    boolean unlockUser(User u);
}

package com.brotherlogic.ditr.api;

import java.io.IOException;
import java.io.PrintStream;

import javax.servlet.GenericServlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.brotherlogic.ditr.datatypes.User;
import com.brotherlogic.ditr.datatypes.Venue;
import com.brotherlogic.ditr.services.RemoteInformation;
import com.brotherlogic.ditr.services.SystemManager;
import com.brotherlogic.ditr.services.foursquare.FoursquareRemoteProduction;
import com.brotherlogic.ditr.services.mongo.MongoBridge;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;

public class MainHandler extends GenericServlet {

    @Override
    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
        HttpServletRequest hReq = (HttpServletRequest) req;
        final HttpServletResponse hResp = (HttpServletResponse) res;

        String requestEndpoint = hReq.getRequestURI().substring(hReq.getRequestURI().indexOf("ditrapi") + "ditrapi".length() + 1);

        JsonElement response = JsonNull.INSTANCE;
        if (requestEndpoint.startsWith("User"))
            response = getUser(requestEndpoint.split("/")[1]).convertToJson();
        else if (requestEndpoint.startsWith("Venue")) {
            String[] elems = requestEndpoint.split("/");
            System.out.println("FIRST = " + elems[1]);
            response = search(elems[2], elems[3], elems[4], elems[5], elems[6], elems[7]);
        }

        Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
        String prettyJsonString = gson.toJson(response);

        final PrintStream ps = new PrintStream(hResp.getOutputStream());
        res.setContentType("application/json");
        ps.print(prettyJsonString);
        ps.flush();
        ps.close();
    }

    private User getUser(String accessToken) throws IOException {
        return new SystemManager().getUser(accessToken);
    }

    private JsonObject search(String token, String cuisine, String been, String radius, String lat, String lon) throws IOException {
        JsonObject ret;
        RemoteInformation ri = new FoursquareRemoteProduction(token);
        boolean beenB = false;
        if (been.equals("true"))
            beenB = true;
        Venue v = ri.runQuery(cuisine, beenB, Double.parseDouble(radius), Double.parseDouble(lat), Double.parseDouble(lon));
        ret = v.convertToJson();

        // Update the user and store
        User u = getUser(token);
        u.forceVenue(v);
        new MongoBridge().storeUser(u);
        System.out.println("Stored User");

        return ret;
    }

}

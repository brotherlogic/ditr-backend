package com.brotherlogic.ditr;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class FoursquareProcess
{
   String oauthCode;

   public FoursquareProcess(String oauthCode)
   {
      this.oauthCode = oauthCode;
   }

   public JsonObject get(String pathExtra) throws IOException
   {
      String url = "https://api.foursquare.com/v2/" + pathExtra + "?oauth_token=" + oauthCode
            + "&v=20140806";

      StringBuffer response = new StringBuffer();
      BufferedReader reader = new BufferedReader(new InputStreamReader(new URL(url).openStream()));
      for (String line = reader.readLine(); line != null; line = reader.readLine())
         response.append(line);

      return new JsonParser().parse(response.toString()).getAsJsonObject();
   }
}

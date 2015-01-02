package com.brotherlogic.ditr.services.mongo;

import java.net.UnknownHostException;

import com.brotherlogic.ditr.datatypes.State;
import com.brotherlogic.ditr.datatypes.User;
import com.brotherlogic.ditr.datatypes.Venue;
import com.brotherlogic.ditr.services.LocalInformation;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

public class MongoBridge implements LocalInformation
{
   private DB db = null;
   private MongoClient client;

   public MongoBridge()
   {
      try
      {
         String conn = System.getenv("MONGOLAB_URI");
         conn = "mongodb://heroku_app32935730:3kpmepg6k2fvtckh1pm5bj2uno@ds029811.mongolab.com:29811/heroku_app32935730";
         if (conn == null)
            client = new MongoClient();
         else
            client = new MongoClient(conn);
         db = client.getDB("ditr");
      }
      catch (Exception e)
      {
         e.printStackTrace();
      }
   }

   public User getUser(String foursquareID)
   {
      connect();

      BasicDBObject query = new BasicDBObject();
      query.append("fsid", foursquareID);
      DBObject response = db.getCollection("user").findOne(query);

      if (response != null)
         return new User(foursquareID, (String) response.get("name"), new State(
               (String) response.get("vid")), (Long) response.get("timestamp"),
               (String) response.get("token"));
      else
         return null;

   }

   public User getUserFromToken(String token)
   {
      BasicDBObject query = new BasicDBObject();
      query.append("token", token);

      // Add the user collection if necessary
      connect();
      if (!db.collectionExists("user"))
         db.createCollection("user", new BasicDBObject());

      DBObject response = db.getCollection("user").findOne(query);

      System.out.println("RESPONSE = " + response);
      if (response != null)
         return new User((String) response.get("id"), (String) response.get("name"), new State(
               (String) response.get("vid"), (Long) response.get("timestamp")),
               (Long) response.get("timestamp"), token);
      else
         return null;
   }

   public boolean lockUser(User u, Venue v)
   {
      BasicDBObject updateQuery = new BasicDBObject();
      updateQuery.append("id", u.getId());

      BasicDBObject update = new BasicDBObject();
      update.append("lastupdate", System.currentTimeMillis());
      update.append("vid", v.getVenueId());
      update.append("timestamp", System.currentTimeMillis());

      connect();
      db.getCollection("user").update(updateQuery, update);

      return true;
   }

   public void shutdown()
   {
      connect();
      client.close();
   }

   public boolean storeUser(User u)
   {
      BasicDBObject addition = new BasicDBObject();
      addition.append("id", u.getId());

      connect();

      if (db.getCollection("user").count(addition) > 0)
      {
         BasicDBObject update = new BasicDBObject();
         update.append("name", u.getName());
         update.append("token", u.getAccessToken());
         update.append("vid", u.getState().getVenueID());
         update.append("lastupdate", u.getLastUpdate());
         update.append("timestamp", u.getState().getTimestamp());
         update.append("id", u.getId());

         System.out.println("UPDATE: " + u.getState().getVenueID());

         db.getCollection("user").update(addition, update);
      }
      else
      {
         addition.append("name", u.getName());
         addition.append("token", u.getAccessToken());
         addition.append("vid", u.getState().getVenueID());
         addition.append("lastupdate", u.getLastUpdate());
         addition.append("timestamp", u.getState().getTimestamp());

         db.getCollection("user").insert(addition);
      }
      return true;
   }

   public boolean unlockUser(User u)
   {
      BasicDBObject updateQuery = new BasicDBObject();
      updateQuery.append("id", u.getId());

      BasicDBObject update = new BasicDBObject();
      update.append("lastupdate", System.currentTimeMillis());
      update.append("vid", -1);

      connect();
      db.getCollection("user").update(updateQuery, update);

      return true;
   }

   private void connect()
   {
      if (db == null)
         try
         {
            client = new MongoClient();
            db = client.getDB("ditr");
         }
         catch (UnknownHostException e)
         {
            e.printStackTrace();
         }
   }
}

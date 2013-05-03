package fr.eurecom.tvrdfizator.db;

import java.net.UnknownHostException;

import com.mongodb.MongoClient;

public class MongoDBUtil {

    private static final MongoClient mongoClient = buildSessionMongo();


    private static MongoClient buildSessionMongo() {
    	try {
			return new MongoClient("localhost", 27017);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
    }

    public static MongoClient getSessionMongo() {
        return mongoClient;
    }

}
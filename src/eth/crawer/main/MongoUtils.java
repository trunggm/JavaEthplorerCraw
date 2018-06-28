package eth.crawer.main;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;

public class MongoUtils {
	
	private static final String HOST = "localhost";
	private static final int PORT = 27017;
	
	private static final String USERNAME = "admin";
	private static final String PASSWORD = "1234";
	public MongoUtils() {
		// TODO Auto-generated constructor stub
	}
	
	public static MongoClient getMongoClient_1() throws UnknownHostException{
		MongoClient mongoClient = new MongoClient(HOST, PORT);
		return mongoClient;
	}
	
	public static MongoClient getMongoClient_2() throws UnknownHostException{
		MongoCredential credemtial = MongoCredential.createMongoCRCredential(USERNAME, MyConstants.DB_NAME, PASSWORD.toCharArray());
		
		@SuppressWarnings("deprecation")
		MongoClient mongoClient = new MongoClient(new ServerAddress(HOST, PORT), Arrays.asList(credemtial));
		
		return mongoClient;
	}
	
	public static MongoClient getMongoClient() throws UnknownHostException {
		return getMongoClient_1();
	}
	
	public static void ping() throws UnknownHostException {
		MongoClient mongoClient = getMongoClient();
		
		System.out.println("\nList all DBs:");
		@SuppressWarnings("deprecation")
		List<String> dbNames = mongoClient.getDatabaseNames();
		
		for (String dbName : dbNames) {
			System.out.println("+ DB name: " + dbName);
		}
		
		System.out.println("\n Done!");
	}
	
	
	public static void main(String[] args) throws UnknownHostException {
		ping();
	}
}

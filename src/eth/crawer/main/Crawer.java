package eth.crawer.main;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.URL;
import java.util.Date;

import javax.net.ssl.HttpsURLConnection;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

import eth.crawer.main.MongoUtils;
import eth.crawer.main.MyConstants;

public class Crawer {
	private static final String USER_AGENT = "Mozilla/5.0";
	public static final String INFURA_MAINNET = "https://mainnet.infura.io/VXPXO7jmWurwcmzsIGdq/";
	
	public static JSONObject sendPost() throws IOException {
		String url = "https://mainnet.infura.io/VXPXO7jmWurwcmzsIGdq/";
		URL obj = new URL(url);
		HttpsURLConnection conn = (HttpsURLConnection) obj.openConnection();
		
		// add reuqest header
		conn.setRequestMethod("POST");
		conn.setRequestProperty("User-Agent", USER_AGENT);
		conn.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
		conn.setRequestProperty("Content-Type", "application/json");
		
		// create json object to request
		JsonObject bodyData = new JsonObject();
		bodyData.addProperty("jsonprc", "2.0");
		bodyData.addProperty("method", "eth_getBlockByNumber");
		
		JsonArray params = new JsonArray();
		String hexBlock = "0x" + Long.toHexString(142);
		params.add(hexBlock);
		params.add(true);
		
		bodyData.add("params", params);
		
		Date date = new Date();
    	long id = date.getTime();
    	bodyData.addProperty("id", id);
    	
    	// add bodyData to request
    	conn.setDoOutput(true);
    	DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
    	dos.writeBytes(bodyData.toString());
    	dos.flush();
    	dos.close();
    	
    	
    	// get response code
    	int resCode = conn.getResponseCode();
    	System.out.println("Connection status code: " + resCode);
    	
    	// get response data
    	BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
    	String inputLine;
    	StringBuffer resData = new StringBuffer();
    	while ((inputLine = br.readLine()) != null) {
			resData.append(inputLine);
		}
    	br.close();
    	
    	System.out.println(resData.toString());
    	
    	JsonObject json = new JsonObject();
    	JSONObject json1 = new JSONObject(resData.toString());
    	
    	
    	System.out.println(json.toString());
    	
    	// JSONObject dung de convert json String to JSONObject;
//    	JSONObject resObj = new JSONObject(resData.toString());
//    	JSONObject blockObj = resObj.getJSONObject("result");
    	
    	
    	return json1;
		
	}
	
	
	
	
	public static String getBlockData(int blockNumber) {
		JsonObject bodyData = new JsonObject();
		bodyData.addProperty("jsonprc", "2.0");
		bodyData.addProperty("method", "eth_getBlockByNumber");
		JsonArray params = new JsonArray();
		String hexBlock = "0x" + Long.toHexString(blockNumber);
		params.add(hexBlock);
		params.add(true);
		bodyData.add("params", params);
		Date date = new Date();
    	long id = date.getTime();
    	bodyData.addProperty("id", id);
		
		return bodyData.toString();
	}
	
	public static String getTransactionReceiptData(String txHash) {
		JsonObject bodyData = new JsonObject();
		bodyData.addProperty("jsonprc", "2.0");
		bodyData.addProperty("method", "eth_getTransactionReceipt");
		
		Date date = new Date();
		long id = date.getTime();
    	bodyData.addProperty("id", id);
    	
		JsonArray params = new JsonArray();
		params.add(txHash);
		
		bodyData.add("params", params);
//		System.out.println(bodyData.toString());
		return bodyData.toString();
	}
	
	public static String getTransactionData(String txHash) {
		JsonObject bodyData = new JsonObject();
		bodyData.addProperty("jsonprc", "2.0");
		bodyData.addProperty("method", "eth_getTransactionByHash");
		
		Date date = new Date();
		long id = date.getTime();
    	bodyData.addProperty("id", id);
    	
		JsonArray params = new JsonArray();
		params.add(txHash);
		
		bodyData.add("params", params);
//		System.out.println(bodyData.toString());
		return bodyData.toString();
	}
	
	// Get Code of address
	public static String getCodeData(String addr, String tag) {
		JsonObject bodyData = new JsonObject();
		bodyData.addProperty("jsonprc", "2.0");
		bodyData.addProperty("method", "eth_getCode");
		
		Date date = new Date();
		long id = date.getTime();
    	bodyData.addProperty("id", id);
    	
		JsonArray params = new JsonArray();
		params.add(addr);
		params.add(tag);
		
		bodyData.add("params", params);
//		System.out.println(bodyData.toString());
		return bodyData.toString();
	}
	
	public static String getTransactionByBlockHashAndIndexData(String blockHash, String index) {
		JsonObject bodyData = new JsonObject();
		bodyData.addProperty("jsonprc", "2.0");
		bodyData.addProperty("method", "eth_getTransactionByBlockHashAndIndex");
		
		Date date = new Date();
		long id = date.getTime();
    	bodyData.addProperty("id", id);
    	
		JsonArray params = new JsonArray();
		params.add(blockHash);
		params.add(index);
		
		bodyData.add("params", params);
//		System.out.println(bodyData.toString());
		return bodyData.toString();
	}
	
	public static String getTransactionByBlockNumberAndIndexData(long blockNumber, String index) {
		JsonObject bodyData = new JsonObject();
		bodyData.addProperty("jsonprc", "2.0");
		bodyData.addProperty("method", "eth_getTransactionByBlockNumberAndIndex");
		
		Date date = new Date();
		long id = date.getTime();
    	bodyData.addProperty("id", id);
    	
		JsonArray params = new JsonArray();
		params.add("0x" + Long.toHexString(blockNumber));
		params.add(index);
		
		bodyData.add("params", params);
//		System.out.println(bodyData.toString());
		return bodyData.toString();
	}
	
	
	public static void parseDataBlock(String blockStr) {
		System.out.println("parse data from string block");
		JSONObject block = new JSONObject(blockStr);
//		System.out.println(block.toString());
		
		JSONArray txs = block.getJSONArray("transactions");
//		System.out.println(txs.toString());
		
		for (int i = 0; i < txs.length(); i++) {
			JSONArray tx = txs.getJSONArray(i);
			System.out.println(tx.toString());
		}
	}
	
	public static void parseTx(JSONObject tx, Long timestamp) {
		// phan loai tx 
		// input = 0x: send eth
		// input = 138: send rc2Token
		// to = null: create smartcontract
		// all tx is save in db, and with create smart contract and send rc2 token,
		// send request to web3 server to get more info 
		try {
			String hash = tx.getString("hash");
			String txReceiptStr = web3Call(getTransactionReceiptData(hash));
//			System.out.println(txReceiptStr);
			JSONObject txReceipt = new JSONObject(txReceiptStr);
			tx.remove("blockHash");
			tx.remove("r");
			tx.remove("s");
			tx.remove("v");
			
//			System.out.println(tx.toString());
			
			String blockNumberHexStr = null; 
			long blockNumber = 0;
			if (!tx.isNull("blockNumber")) {
				blockNumberHexStr = tx.getString("blockNumber");
				blockNumber = Long.decode(blockNumberHexStr);
			}
//			tx.remove("blockNumber");
			tx.put("blockNumber", blockNumber);
			txReceipt.put("blockNumber", blockNumber);
			
			
			String nonceHexStr = null;
			int nonce = 0;
			if (!tx.isNull("nonce")) {
				nonceHexStr = tx.getString("nonce");
				nonce = Integer.decode(nonceHexStr);
			}
			tx.put("nonce", nonce);
			
			String txIndexHexStr = null;
			int txIndex = 0;
			if (!tx.isNull("transactionIndex")) {
				txIndexHexStr = tx.getString("transactionIndex");
				txIndex = Integer.decode(txIndexHexStr);
			}
			tx.put("transactionIndex", txIndex);
			txReceipt.put("transactionIndex", txIndex);
			
			String valueHexStr = null;
			double value = 0;
			if (!tx.isNull("value")) {
				valueHexStr = (tx.getString("value").equals("0x")) ? "0" : tx.getString("value").substring(2);
				value = Double.valueOf(new BigInteger(valueHexStr, 16).toString());
			}
			tx.put("value", value);
			
			String gasHexStr = null;
			double gas = 0;
			if (!tx.isNull("gas")) {
				gasHexStr = tx.getString("gas");
				gasHexStr = gasHexStr.substring(2);
				gas = Double.valueOf(new BigInteger(gasHexStr, 16).toString());
			}
			tx.put("gas", gas);
			
			String gasPriceHexStr = null;
			double gasPrice = 0;
			if (!tx.isNull("gasPrice")) {
				gasPriceHexStr = tx.getString("gasPrice");
				gasPriceHexStr = gasPriceHexStr.substring(2);
				gasPrice = Double.valueOf(new BigInteger(gasPriceHexStr, 16).toString());
			}
			tx.put("gasPrice", gasPrice);
			
			String gasUsedHexStr = null;
			double gasUsed = 0;
			if (!txReceipt.isNull("gasUsed")) {
				gasUsedHexStr = txReceipt.getString("gasUsed");
				gasUsedHexStr = gasUsedHexStr.substring(2);
				gasUsed = Double.valueOf(new BigInteger(gasUsedHexStr, 16).toString());
			}
			txReceipt.put("gasUsed", gasUsed);
			
			String cumulativeGasUsedHexStr = null;
			double cumulativeGasUsed = 0;
			if (!txReceipt.isNull("cumulativeGasUsed")) {
				cumulativeGasUsedHexStr = txReceipt.getString("cumulativeGasUsed");
				cumulativeGasUsedHexStr = cumulativeGasUsedHexStr.substring(2);
				cumulativeGasUsed = Double.valueOf(new BigInteger(cumulativeGasUsedHexStr, 16).toString());
				
			}
			txReceipt.put("cumulativeGasUsed", cumulativeGasUsed);
			
			String statusStr = null;
			Boolean status = true;
			if (!txReceipt.isNull("status")) {
				statusStr = txReceipt.getString("status");
				status = (statusStr.equals("0x01"))?true:false;
				
			}
			tx.put("status", status);
			txReceipt.put("status", status);
			
			
			
			tx.put("receipt", txReceipt);
			
			
			MongoClient mongoClient = MongoUtils.getMongoClient();

	        DB db = mongoClient.getDB("ethplorer");

	        DBCollection txDb = db.getCollection("transactions");
	        
	        String txStr = tx.toString();
	        Object o = BasicDBObject.parse(txStr);
	        DBObject dbObj = (DBObject) o;
			System.out.println(dbObj.toString());
			
			txDb.insert(dbObj);
			
			String input = null;
			if (!tx.isNull("input")) {
				input = tx.getString("input");
			}
			
			
			System.out.println(tx.toString());
			if (input.equals("0x")) {
				System.out.println("send-eth tx");
				// lay transaction receipt
				// save to db
				
			} 
			else if (input.length() == 138) {
				System.out.println("send-rc2Token tx - send request to web3 server");
				
			} 
			else {
				System.out.println("contract operation - send request to web3 server");
				
			}
			
			System.out.println("input " + hash + " " + input + " ");
			
		} catch (Exception e2) {
			// TODO: handle exception
			String tAd = null;
			String hash = tx.getString("hash");
			System.out.println("create smart conctract - send request to web3 server");
		}
		
		
		
	}
	
	
	public static String web3Call(String jsonStr) throws IOException {
		URL obj = new URL(INFURA_MAINNET);
		HttpsURLConnection conn = (HttpsURLConnection) obj.openConnection();
		
		
		// add reuqest header
		conn.setRequestMethod("POST");
		conn.setRequestProperty("User-Agent", USER_AGENT);
		conn.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
		conn.setRequestProperty("Content-Type", "application/json");
		
		conn.setDoOutput(true);
    	DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
    	dos.writeBytes(jsonStr);
    	dos.flush();
    	dos.close();
    	
    	
    	int resCode = conn.getResponseCode();
//    	System.out.println("Connection status code: " + resCode);
    	
    	// get response data
    	BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
    	String inputLine;
    	StringBuffer resData = new StringBuffer();
    	while ((inputLine = br.readLine()) != null) {
			resData.append(inputLine);
		}
    	br.close();
    	
//    	System.out.println(resData.toString());
    	JSONObject json = new JSONObject(resData.toString());
    	try {
			JSONObject rsJson = json.getJSONObject("result");
			
			return rsJson.toString();
		} catch (Exception e) {
			// TODO: handle exception
			try {
				String rsStr = json.getString("result");
				return rsStr;
			} catch (Exception e2) {
				// TODO: handle exception
				return null;
			}
			
		}
	}
	
	
	
	
	
	public static void main(String[] args) throws IOException {
		
		String blockStr = web3Call(getBlockData(5861053));
		
		
		
		JSONObject block = new JSONObject(blockStr);
//		System.out.println(block.toString());
		
		JSONArray txs = block.getJSONArray("transactions");
		String timestampStr = block.getString("timestamp");
		
		
		Long timestamp = Long.decode(timestampStr);
		System.out.println("timestamp: " + timestamp);
		
		
		parseTx(txs.getJSONObject(3), timestamp);
		
//		System.out.println(txs.toString());
		
//		for (int i = 0; i < txs.length(); i++) {
//			System.out.println(i);
//			JSONObject tx = txs.getJSONObject(i);
//			parseTx(tx, timestamp);
//		}
		
	}
}

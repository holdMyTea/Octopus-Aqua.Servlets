package ua.com.octopus_aqua.data;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

public class Decoder {
	
	public final static String JSON_TAG = "json";
    public final String IMG_TAG = "img";
    public final String CONTROL_LENGTH_TAG = "len";

	public static String decodeString(String string){
		try {
			return URLDecoder.decode(string, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			Log.info("Christmas miracle");
			return null;
		}
	}
	
	public static Map<String, String> queryToMap(String query) {
        Map<String, String> result = new HashMap<String, String>();
        for (String param : query.split("&")) {
            String[] pair = param.split("=");
            if (pair.length > 0) {
                result.put(pair[0], pair[1]);
            } else {
                result.put(pair[0], "");
            }
        }
        Log.info(result.get("json"));
        return result;
    }
	
}

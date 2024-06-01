package com.fscan.File.Scanner.utils;

import org.json.JSONException;
import org.json.JSONObject;

public class evalJSON {
    public static JSONObject TextToJSON(String S){
        return new JSONObject(S);
    }

    // Returns the last analysis Stats from entire JSON response if SHA256 is present in VT DB.
    // Returns "NotFoundError" (string) if SHA256 is not present in VT DB.

    public static String analysisStats(String response){
        JSONObject entireResponse = TextToJSON(response);
        try {//hex is present in VT DB.
            JSONObject last_analysis_stats = entireResponse
                    .getJSONObject("data")
                    .getJSONObject("attributes")
                    .getJSONObject("last_analysis_stats");

            return last_analysis_stats.toString();
        }
        catch (JSONException ex){//NOT present in VT DB.

            return "NotFoundError";
        }

    }

    //Returns the analysisId from entire response.
    //Returns the error message as string in case of errors.
    public static String analysisId(String response){
        JSONObject entireResponse = TextToJSON(response);
        try {
            return entireResponse
                    .getJSONObject("data")
                    .get("id").toString();

        }
        catch (JSONException ex){
            return ex + "either `data` or `id` field are not present in:" + response;
        }

    }

    //Returns the Stats from entire JSON response
    //Returns the error message as string in case of errors.
    public static String StatsByAId(String response){
        JSONObject entireResponse = TextToJSON(response);
        try{
            return entireResponse
                    .getJSONObject("data")
                    .getJSONObject("attributes")
                    .getJSONObject("stats").toString();
        }
        catch (JSONException ex){
            return ex+ "one the following key is missing data->attributes->stats in :"+response;
        }
    }


    //Returns the Status from entire JSON response.
    //Returns the error message as String in case of errors.
    public static String Status(String response){
        JSONObject entireResponse = TextToJSON(response);
        try{
            return entireResponse
                    .getJSONObject("data")
                    .getJSONObject("attributes")
                    .get("status").toString();
        }
        catch (JSONException ex){
            return ex+ "one the following key is missing data->attributes->status in :"+response;
        }
    }
}

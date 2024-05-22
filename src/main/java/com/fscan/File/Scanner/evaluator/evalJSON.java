package com.fscan.File.Scanner.evaluator;

import org.json.JSONException;
import org.json.JSONObject;

public class evalJSON {
    private static JSONObject TexttoJSON(String S){
        return new JSONObject(S);
    }
    public static String analysisStats(String response){
        JSONObject entireResponse = TexttoJSON(response);
        try {//hex is present in VT DB.
            JSONObject last_analysis_stats = entireResponse.getJSONObject("data")
                    .getJSONObject("attributes")
                    .getJSONObject("last_analysis_stats");

            return last_analysis_stats.toString();
        }
        catch (JSONException ex){//NOT present in VT DB.

            return "NotFoundError";
        }

    }
    public static String analysisId(String response){
        JSONObject entireResponse = TexttoJSON(response);
        try {//hex is present in VT DB.

            return entireResponse.getJSONObject("data")
                    .getJSONObject("id").toString();
        }
        catch (JSONException ex){//NOT present in VT DB.

            return null;
        }

    }
}

package com.fscan.File.Scanner.utils;


import org.json.JSONObject;

import java.util.Objects;


public class Validators {

    public static boolean IsAnalyisId(String analysisId){

        int len = analysisId.length();

        return len==60 && analysisId.charAt(len - 1) == '=' && analysisId.charAt(len - 2) == '=';

    }

    public static boolean IsValidResponse(String response) {

        String status = evalJSON.Status(response);
        return Objects.equals(status, "completed");
    }

    /*
        scanResponse =
            {
                "malicious": 0,
                "type-unsupported": 15,
                "failure": 0,
                "undetected": 58,
                "suspicious": 0,
                "confirmed-timeout": 0,
                "harmless": 0,
                "timeout": 0
             }
    */

    public static boolean isValidResult(String result){
        return result.contains("malicious") && result.contains("undetected");
    }

    public static String FinalizeVerdict(String result){
        JSONObject stats = evalJSON.TextToJSON(result);
        int maliciousCount = (int) stats.get("malicious");
        int suspiciousCount = (int) stats.get("suspicious");
        if(maliciousCount > 0){
            return "Malicious";
        }

        if(suspiciousCount > 0){
            return "Suspicious";
        }
        return "No Malware Found";
    }


}

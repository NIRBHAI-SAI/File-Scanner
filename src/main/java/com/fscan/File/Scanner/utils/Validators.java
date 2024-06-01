package com.fscan.File.Scanner.utils;

import org.json.JSONObject;

import java.util.Objects;
import java.util.Queue;

public class Validators {

    public static boolean IsAnalyisId(String analysisId){

        int len = analysisId.length();

        return len==60 && analysisId.charAt(len - 1) == '=' && analysisId.charAt(len - 2) == '=';

    }

    public static boolean IsValidResponse(String response) {

        String status = evalJSON.Status(response);
        return Objects.equals(status, "completed");
    }


}

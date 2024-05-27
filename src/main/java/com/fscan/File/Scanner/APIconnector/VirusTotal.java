package com.fscan.File.Scanner.APIconnector;




import com.fscan.File.Scanner.evaluator.evalJSON;
import okhttp3.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Objects;
import java.util.ResourceBundle;

public class VirusTotal {

    private static final String apiKey,scanHex,scanId,upload;
    static {
        ResourceBundle rb = ResourceBundle.getBundle("api-config");
        // store the name of the implementation clas in a static variable
        apiKey = rb.getString("api-key");
        scanHex = rb.getString("url-scan-hex");
        scanId = rb.getString("url-scan-id");
        upload = rb.getString("url-upload");

    }
    public static String ScanByHex(String hex_code){

        String URL = scanHex + hex_code;
//        String API_KEY = "2bd12a101f2e4fee4a17242edd7f5215ccc4350d2ba0417916c87705bf5cf1b3";
        HttpRequest req = HttpRequest.newBuilder().GET()
                .uri(URI.create(URL))
                .setHeader("accept", "application/json")
                .setHeader("X-Apikey", apiKey).build();
        HttpClient client = HttpClient.newBuilder().build();
        HttpResponse<String> Response = null;
        try {
            Response = client.send(req, HttpResponse.BodyHandlers.ofString());
            return Response.body();
        } catch (Exception e) {
            return "00";
        }

    }
    public static String UploadFile(MultipartFile multipartFile)  {//Error needs to be fixed


//        String API_KEY = "2bd12a101f2e4fee4a17242edd7f5215ccc4350d2ba0417916c87705bf5cf1b3";


        OkHttpClient client = new OkHttpClient();

        RequestBody fileBody = null;
        try {//error
            fileBody = RequestBody.create(MediaType.parse(multipartFile.getContentType()), multipartFile.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e + "at Request Body");
        }

        MultipartBody multipartBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)  // Header to show we are sending a Multipart Form Data
                .addFormDataPart("file", multipartFile.getOriginalFilename(),fileBody) // file param
                .build();

        Request request = new Request.Builder()
                .url(upload)
                .post(multipartBody)
                .addHeader("accept", "application/json")
                .addHeader("X-Apikey", apiKey)
                .addHeader("content-type", "multipart/form-data; boundary=---011000010111000001101001")
                .build();

        try {
            Response response = client.newCall(request).execute();
            return evalJSON.analysisId(response.body().string());

        } catch (IOException e) {
            throw new RuntimeException(e + "at client Response");
        }

    }
    public static String ScanById(String analysisID){
//        String API_KEY = "2bd12a101f2e4fee4a17242edd7f5215ccc4350d2ba0417916c87705bf5cf1b3";

        String URL = scanId + analysisID;

        String URL = "https://www.virustotal.com/api/v3/analyses/" + analysisID;
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(URL)
                .get()
                .addHeader("accept", "application/json")
                .addHeader("X-Apikey", API_KEY)
                .build();

        try {
            Response response = client.newCall(request).execute();
            return evalJSON.StatsByAId(response.body().string());
        } catch (IOException e) {
            return e+"while getting response from analysis client";
        }

    }

}

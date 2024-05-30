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
        //After one Client Response the MultiPartfile
        // will be removed from our temp database which cases an error in uploading the file

        String URL = scanHex + hex_code;
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
    public static String UploadFile(MultipartFile multipartFile)  {

        OkHttpClient client = new OkHttpClient();

        RequestBody fileBody = null;
        try {
            fileBody = RequestBody.create(MediaType.parse(multipartFile.getContentType()), multipartFile.getBytes());
        } catch (IOException e) {
            return e + " at creating Request Body for uploading file into VT DB.";
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
            return  e + " while getting Response form upload file end point.";
        }

    }
    public static String[] ScanById(String analysisID){

        String URL = scanId + analysisID;

        String status = "queued";

        HttpRequest req = HttpRequest.newBuilder().GET()
                .uri(URI.create(URL))
                .setHeader("accept", "application/json")
                .setHeader("X-Apikey", apiKey).build();
        HttpClient client = HttpClient.newBuilder().build();
        HttpResponse<String> Response = null;

        try {
            Response = client.send(req, HttpResponse.BodyHandlers.ofString());
            status = evalJSON.Status(Response.body());
            if(Objects.equals(status, "completed")){
                return new String[]{evalJSON.StatsByAId(Response.body()),status};
            } else if (!Objects.equals(status, "queued")) {
                //status is neither completed nor queued i.e. error in evaluating Json
                return new String[]{status};
            }
        } catch (Exception e) {
            return new String[]{e + " while getting response from analysis_id end point"};
        }

        return new String[]{status};
    }

}

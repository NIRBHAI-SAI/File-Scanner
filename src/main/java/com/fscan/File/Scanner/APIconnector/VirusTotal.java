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


public class VirusTotal {
    public static String ScanByHex(String hex_code){

        String URL = "https://www.virustotal.com/api/v3/files/" + hex_code;
        String API_KEY = "2bd12a101f2e4fee4a17242edd7f5215ccc4350d2ba0417916c87705bf5cf1b3";
        HttpRequest req = HttpRequest.newBuilder().GET()
                .uri(URI.create(URL))
                .setHeader("accept", "application/json")
                .setHeader("X-Apikey", API_KEY).build();
        HttpClient client = HttpClient.newBuilder().build();
        HttpResponse<String> Response = null;
        try {
            Response = client.send(req, HttpResponse.BodyHandlers.ofString());
            return Response.body();
        } catch (Exception e) {
            return "00";
        }

    }
    public static String UploadFile(MultipartFile multipartFile) {
        String analysis_id = null;


        String API_KEY = "2bd12a101f2e4fee4a17242edd7f5215ccc4350d2ba0417916c87705bf5cf1b3";
        String URL = "https://www.virustotal.com/api/v3/files";


        OkHttpClient client = new OkHttpClient();

        RequestBody fileBody = null;
        try {
            fileBody = RequestBody.create(MediaType.parse(Objects.requireNonNull(multipartFile.getContentType())), multipartFile.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e + "at Request Body");
        }

        MultipartBody multipartBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)  // Header to show we are sending a Multipart Form Data
                .addFormDataPart("file", multipartFile.getOriginalFilename(),fileBody) // file param
                .build();

        Request request = new Request.Builder()
                .url(URL)
                .post(multipartBody)
                .addHeader("accept", "application/json")
                .addHeader("X-Apikey", API_KEY)
                .addHeader("content-type", "multipart/form-data; boundary=---011000010111000001101001")
                .build();

        try {
            Response response = client.newCall(request).execute();
//        System.out.println(response.body().string());
            analysis_id = evalJSON.analysisId(response.body().string());
        } catch (IOException e) {
            throw new RuntimeException(e + "at client Response");
        }

        return analysis_id;
    }

}

package com.fscan.File.Scanner.APIconnector;


import com.fscan.File.Scanner.service.FileAuditService;
import com.fscan.File.Scanner.utils.Validators;
import com.fscan.File.Scanner.utils.evalJSON;
import com.fscan.File.Scanner.utils.sha256;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockMultipartFile;
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
    private static final Logger log = LoggerFactory.getLogger(VirusTotal.class);

    static {
        ResourceBundle rb = ResourceBundle.getBundle("api-config");
        // store the name of the implementation clas in a static variable
        apiKey = rb.getString("api-key");
        scanHex = rb.getString("url-scan-hex");
        scanId = rb.getString("url-scan-id");
        upload = rb.getString("url-upload");
    }


    private static String ScanByHex(String hex_code){
        //After one Client Response the MultiPartfile
        // will be removed from our temp database which cases an error in uploading the file
        log.info("Calculating HEX");
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


    private static String UploadFile(MultipartFile multipartFile)  {

        OkHttpClient client = new OkHttpClient();

        RequestBody fileBody = null;
        try {
            fileBody = RequestBody.create(MediaType.parse(multipartFile.getContentType()), multipartFile.getBytes());
        } catch (IOException e) {
            return e + " at creating Request Body for uploading file into VT DB.";
        }
        log.info("uploading file");
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


    public static String ScanById(String analysisID,FileAuditService fileAuditService,Long id){

        String URL = scanId + analysisID;
        fileAuditService.updateStatus(id,"Scanning using AnalysisID");
        fileAuditService.updateDT(id);

//        fileAuditService.updateStatus(id,"Scanning using analyis Id");
//        fileAuditService.updateDT(id);

        HttpRequest req = HttpRequest.newBuilder().GET()
                .uri(URI.create(URL))
                .setHeader("accept", "application/json")
                .setHeader("X-Apikey", apiKey).build();
        HttpClient client = HttpClient.newBuilder().build();
        HttpResponse<String> Response = null;

        try {
            Response = client.send(req, HttpResponse.BodyHandlers.ofString());

        } catch (Exception e) {
            return e + " while getting response from analysis id end point";
        }


        if(Validators.IsValidResponse(Response.body())){

            fileAuditService.updateStatus(id,"Scanning Completed(using analysis_id)");
            fileAuditService.updateDT(id);

            return evalJSON.StatsByAId(Response.body());

        }

        fileAuditService.updateStatus(id,"Queued at Virus Total Database");
        fileAuditService.updateDT(id);


        return "Please try again after some time";// even for error

    }

    public static String ScanByFile(MultipartFile file,FileAuditService fileAuditService,Long id){
        String originalFileName = file.getOriginalFilename();
        String name = file.getName();
        String contentType = file.getContentType();
        byte[] content = new byte[0];
        try {
            content = file.getBytes();
        } catch (IOException e) {
            return "Unable to read multipart file";
        }

        String hexCode = sha256.generate(file);
        if(hexCode.equals("00")){//unable to generate SHA256 for given file.
            return "unable to generate SHA256 for given file.";
        }


        fileAuditService.updateSHA256(id,hexCode);
        fileAuditService.updateStatus(id,"Scanning Using SHA256");
        fileAuditService.updateDT(id);

        String result = evalJSON.analysisStats(VirusTotal.ScanByHex(hexCode));
        //get results by using HEXcode
        //result will be either a proper stats(malicious,harmless,undetected)
        // or Not foundError (if hex code is not present in DB).
        if(!Objects.equals(result, "NotFoundError")){
            fileAuditService.updateStatus(id,"Scanning Completed(using SHA256)");
            fileAuditService.updateDT(id);
            return result;//if found in Database.
        }


        MultipartFile mockMultipartFile = new MockMultipartFile(name,originalFileName, contentType, content);
        // initial file will be available for GC as one request is made(Scan by hex-code)
        // so creating a multipart file.

        String analysis_id = VirusTotal.UploadFile(mockMultipartFile);//Getting Analysis ID from VT
        if(Validators.IsAnalyisId(analysis_id)){
            //we received a proper analysis_id
            fileAuditService.updateStatus(id,"File uploaded to Virus Total Database");
            fileAuditService.updateAID(id,analysis_id);
            fileAuditService.updateDT(id);
            return "File uploaded to Virus Total DataBase" +
                    " with analysis ID: \n " +analysis_id;

        }
        return analysis_id;
        //any error while
        //uploading the file,return the error message as String.


    }
}

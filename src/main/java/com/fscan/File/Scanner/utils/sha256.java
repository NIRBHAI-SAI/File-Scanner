package com.fscan.File.Scanner.utils;


import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import com.google.common.io.ByteSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;

public class sha256 {
    @Value("${file.path}")
    private static String filePath;


    public static File multipartToFile(MultipartFile multipart, String fileName) throws IllegalStateException, IOException {

//        String path ="D:\\Phase 2\\File-Scanner\\Uploads";
//        String dir = path +"/"+ multipart.getOriginalFilename();
//        File convFile = new File(dir);//create a file in the specified directory.
//        multipart.transferTo(convFile);

        File convFile = new File(System.getProperty("java.io.tmpdir")+"/"+fileName);//Saving the file in temporary location
        multipart.transferTo(convFile);// this storage will be deleted.
        return convFile;
    }


    public static String generate(MultipartFile mFile)  {

        String fileName = mFile.getOriginalFilename();
        try {
            File file = multipartToFile(mFile,fileName); // Convert Multipart file to File.
            ByteSource byteSource = com.google.common.io.Files.asByteSource(file);
            HashCode hc = byteSource.hash(Hashing.sha256());
            return hc.toString();
        } catch (IOException e) {
            return "00";//if an exception in creating the HexCode, or exception in converting multipart to file.
        }
    }

}

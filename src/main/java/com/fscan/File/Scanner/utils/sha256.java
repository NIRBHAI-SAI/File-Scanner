package com.fscan.File.Scanner.utils;

import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import com.google.common.io.ByteSource;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

public class sha256 {

    public static File multipartToFile(MultipartFile multipart, String fileName) throws IllegalStateException, IOException {
        File convFile = new File(System.getProperty("java.io.tmpdir")+"/"+fileName);
        multipart.transferTo(convFile);// Need to adopt a new way to change multipart file to File.
        return convFile;
    }

    public static String generate(MultipartFile mFile)  {

        String fileName = mFile.getOriginalFilename();
        try {
            File file = multipartToFile(mFile,fileName);
            ByteSource byteSource = com.google.common.io.Files.asByteSource(file);
            HashCode hc = byteSource.hash(Hashing.sha256());
            return hc.toString();
        } catch (IOException e) {
            return "00";
        }
    }

}

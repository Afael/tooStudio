package com.desktopip.exploriztic.tootanium.utilities;


import android.util.Base64;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.List;

/**
 * Created by Jayus on 13/07/2018.
 */

public class StringHelper {

    public static String getExtension(String path){
        String result = "";
        File dir = new File(path);
        String[] extensions = new String[] { "txt", "jsp", "docx", "doc", "xls", "xlsx", "ppt", "pdf"
                , "zip", "rar", "mp3", "3gp", "png", "jpg"};
        try {
            List<File> files = (List<File>) FileUtils.listFiles(dir, extensions, true);
            for (File file : files) {
                result = file.getCanonicalPath();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static String substringAfterLast(String str, String separator) {
        if (isEmpty(str)) {
            return str;
        }
        if (isEmpty(separator)) {
            return "";
        }
        int pos = str.lastIndexOf(separator);
        if (pos == -1 || pos == (str.length() - separator.length())) {
            return "";
        }
        return str.substring(pos + separator.length());
    }

    public static String substringBeforeLast(String str, String separator) {
        if (isEmpty(str)) {
            return str;
        }
        if (isEmpty(separator)) {
            return "";
        }
        int pos = str.lastIndexOf(separator);
//        if (pos == -1 || pos == (str.length() - separator.length())) {
//            return "";
//        }
        return str.substring(0, pos);
    }


    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    public static String base64Encode(String stringToEncode) throws UnsupportedEncodingException {
        byte[] encodedBytes = Base64.encode(stringToEncode.getBytes(), Base64.DEFAULT);
        return new String(encodedBytes, Charset.forName("UTF-8"));
    }


    public static String base64Decode(String stringToDecode) {
        byte[] decodedBytes = Base64.decode(stringToDecode, Base64.DEFAULT);
        return new String(decodedBytes, Charset.forName("UTF-8"));
    }
}
